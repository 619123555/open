package com.open.boss.configuration.shiro;


import com.open.boss.entity.Role;
import com.open.boss.service.system.UserService;
import com.open.boss.entity.User;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author yangzhongying
 * @date 2019/1/11 17:36
 */
@Component("authShiroRealm")
public class AuthShiroRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(AuthShiroRealm.class);

    @Resource
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();
        String userId = user.getId();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<Role> roles = this.userService.getRoleByUserId(userId);
        if (CollectionUtils.isNotEmpty(roles)) {
            logger.info("用户:{}角色有{}个", user.getName(), roles.size());
            Set<String> set = new HashSet<>();
            for (Role r : roles) {
                set.add(r.getId());
            }
            roles.forEach(
                    role -> {
                        info.addRole(role.getCode());
                    }
            );
            userService.selectUserMenu(set).forEach(
                    permission -> info.addStringPermission(permission.getHref())
            );
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws LockedAccountException, UnknownAccountException {
        if (token.getPrincipal() == null) {
            return null;
        }
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String no = usernamePasswordToken.getUsername();
        String password = new String((char[]) usernamePasswordToken.getCredentials());
        logger.info("[{}],[{}]", no, password);
        User user = this.userService.selectUserByNo(no);
        if (user == null) {
            throw new UnknownAccountException("帐号不存在");
        }
        if (StringUtils.isNoneBlank(user.getStatus()) && !"1".equals(user.getStatus())) {
            throw new LockedAccountException("帐号已被锁定");
        }
        return new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                ByteSource.Util.bytes(user.getId()),
                getName());
    }
}
