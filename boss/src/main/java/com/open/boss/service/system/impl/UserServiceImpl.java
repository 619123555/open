package com.open.boss.service.system.impl;

import cn.hutool.core.util.IdUtil;
import com.open.boss.commons.Result;
import com.open.boss.commons.ResultGenerator;
import com.open.boss.entity.Menu;
import com.open.boss.entity.Organization;
import com.open.boss.entity.Role;
import com.open.boss.entity.User;
import com.open.boss.entity.UserOthProObj;
import com.open.boss.mapper.MenuMapper;
import com.open.boss.mapper.RoleMapper;
import com.open.boss.mapper.UserMapper;
import com.open.boss.service.system.UserService;
import com.open.boss.utils.ConfUtils;
import com.open.boss.utils.GoogleAuthenticator;
import com.open.boss.utils.ShaPassword;
import com.open.boss.utils.UserUtils;
import com.open.common.constants.Constants;
import com.open.common.utils.DateUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private JedisCluster jedisCluster;

    @Value("${login.pwWrongNum:5}")
    private Integer pwWrongNum;

    @Value("${login.lockTime:30}")
    private Integer lockTime;

    @Override
    public void deleteUser(User user) {
        userMapper.delete(user);
    }

    @Override
    public User getUserByNo(String no) {
        User user = new User();
        user.setNo(no);
        return userMapper.selectOne(user);
    }

    @Override
    @Transactional(
            readOnly = false,
            rollbackFor = {Exception.class, RuntimeException.class}
    )
    public void saveUser(User user) {
        if (StringUtils.isBlank(user.getId())) {
            user.setId(IdUtil.simpleUUID());
            user.setCreator(UserUtils.getUser().getId());
            user.setOperator(user.getCreator());
            user.setCreateTime(new Date());
            user.setOperTime(user.getCreateTime());
            user.setStatus("1");
            user.setPhone(user.getMobile());
            user.setSecret(GoogleAuthenticator.genSecret());
            String dpwd = ConfUtils.getConfValue("common_default_password");
            user.setPassword(UserUtils.entryptPassword(dpwd, user.getId()));
            userMapper.insertSelective(user);
        } else {
            User oldUser = userMapper.selectByPrimaryKey(user.getId());
            oldUser.setName(user.getName());
            oldUser.setGender(user.getGender());
            if (user.getOffice() != null) {
                oldUser.setOfficeId(user.getOffice().getId());
            }
            if (user.getCompany() != null && StringUtils.isNotBlank(user.getCompany().getId())) {
                oldUser.setCompanyId(user.getCompany().getId());
            }
            oldUser.setMobile(user.getMobile());
            oldUser.setPhone(user.getMobile());
            oldUser.setEmail(user.getEmail());
            oldUser.setUserType(user.getUserType());
            oldUser.setManageChannel(user.getManageChannel());
            userMapper.updateByPrimaryKeySelective(oldUser);
        }
    }

    @Override
    public String getUserMaxNoStr(Organization organization) {
        UserOthProObj userOthProObj = new UserOthProObj();
        userOthProObj.setCompanyID(organization.getId());
        if (!StringUtils.isNoneBlank(organization.getSimpleCode())) {
            organization.setSimpleCode("qd");
        }
        userOthProObj.setUserNOStr(organization.getSimpleCode());

        userOthProObj = userMapper.getUserMaxNo(userOthProObj);
        // 最大用户编号加1
        int uno = userOthProObj.getMaxNo() + 1;
        String userNOStr = String.valueOf(uno);
        // 进行补充0操作
        userNOStr = StringUtils.leftPad(userNOStr, 4, "0");
        userNOStr = organization.getSimpleCode() + userNOStr;
        return userNOStr;
    }

    @Override
    public List<Role> getRoleByUserId(String id) {
        if (UserUtils.isSuperAdmin(id)) {
            return this.roleMapper.selectAll();
        }
        return roleMapper.getRoleByUserId(id);
    }

    @Override
    public Role getSpecialMenu(String id) {
        Role role = new Role();
        //List<Menu> userMenus = menuMapper.getUserMenu(id);
        //role.setMenuList(userMenus);
        return role;
    }


    @Override
    public List<String> getMenuKeys(String userId) {
        List<Menu> userMenus = menuMapper.getUserMenu(userId);
        List<String> keys = new ArrayList();
        for (Menu m : userMenus) {
            String pids = m.getParentIds();
            String tempKey = "";
            if (StringUtils.isNotBlank(pids)) {
                String[] pidArr = pids.split(",");
                StringBuffer key = new StringBuffer(m.getSystemSign() + "/");
                for (String pid : pidArr) {
                    key.append(pid + "/");
                }
                key.append(m.getId() + "/");
                tempKey = key.toString();
            } else {
                tempKey = m.getId() + "/";
            }
            String key2 = tempKey;
            tempKey = !StringUtils.isBlank(key2) ? key2.substring(0, key2.length() - 1) : key2;

            keys.add(tempKey);
        }
        return keys;
    }

    @Override
    public User assignUserToRole(Role role, User user) {
        //if (user == null) {
        //    return null;
        //}
        //List<String> roleIds = user.getRoleIdList();
        //if (roleIds.contains(role.getId())) {
        //    return user;
        //}
        //user.getRoleList().add(role);
        //saveUser(user);
        return user;
    }

    @Override
    public User getByNo(User user) {
        return userMapper.getByNo(user);
    }

    @Override
    public User getByNoOrMobileOrEmail(User user) {
        return userMapper.getByNoOrMobileOrEmail(user);
    }

    @Override
    public List<User> findUserInfoList(List<String> userNos) {
        return userMapper.findUserInfoList(userNos);
    }

    @Override
    public List<User> findUserPageList(User user) {
        return null;
    }

    @Override
    public User login(String email) {
        return userMapper.login(email);
    }

    @Override
    public User selectUserByMobile(String mobile) {
        return userMapper.loginByMobile(mobile);
    }

    @Override
    public User selectUserByPrimaryKey(String id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Result login(HttpServletRequest request) {
        String password = request.getParameter("password");
        String email = request.getParameter("username");
//        String validateCode = request.getParameter("validateCode");
//        logger.info("帐户{},口令{}", email, validateCode);
        logger.info("帐户{}, 密码{}", email, password);
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            return ResultGenerator.error("数据项不能为空");
        }
        Subject subject = SecurityUtils.getSubject();
        User usertmp = this.selectUserByNo(email);
        try {
            if (usertmp == null) {
                return ResultGenerator.error("帐号数据有误");
            }
//            if (!Global.isDevMode()) {
//                logger.info("验证动态口令...");
//                try {
//                    long codeLong = Long.parseLong(validateCode);
//                    logger.info("是否是纯数字:{}", codeLong);
//                } catch (NumberFormatException e) {
//                    lock(usertmp);
//                    return ResultGenerator.error("动态口令输入错误");
//                }
//                if (!GoogleAuthenticator.authcode(validateCode, usertmp.getSecret())) {
//                    // 记录器
//                    lock(usertmp);
//                    return ResultGenerator.error("动态口令输入错误");
//                }
//            }
            password = ShaPassword.encryptPassword(Constants.HASH_ALGORITHM, password, usertmp.getId());
            UsernamePasswordToken token = new UsernamePasswordToken(email, password, true);
            subject.login(token);
        } catch (UnknownAccountException e) {
            logger.error("Exception:{}", e);
            return ResultGenerator.error("密码不能为空");
        } catch (LockedAccountException e) {
            logger.error("Exception:{}", e);
            return ResultGenerator.error("帐号已经被锁定");
        } catch (CredentialsException e) {
            logger.error("Exception:{}", e);
            return ResultGenerator.error("帐号密码错误");
        } catch (ExcessiveAttemptsException e) {
            logger.error("Exception:{}", e);
            return ResultGenerator.error("登录失败次数过多");
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            return ResultGenerator.error("未知异常");
        }
        // 验证是否成功
        if (subject.isAuthenticated()) {
//            usertmp.setLoginIp();
            usertmp.setLoginDate(new Date());
            this.userMapper.updateByPrimaryKeySelective(usertmp);
            Session session = subject.getSession();
            session.setAttribute(Constants.SESSION_USER, subject.getPrincipal());
            User user = (User) subject.getPrincipal();
            return ResultGenerator.ok(user);
        }
        return ResultGenerator.error();
    }

    private void lock(User employee) {
        int lock =
                DateUtils.getSecondsBetweenDate(
                        new Date(), DateUtils.parseToTodayDesignatedDate(new Date(), "23:59:59"));
        String redisKey = "system:user:login:wrong:" + employee.getId();
        String wrongnum = jedisCluster.get(redisKey);
        if (StringUtils.isBlank(wrongnum)) {
            jedisCluster.set(redisKey, "1");
            jedisCluster.expire(redisKey, lock);
        } else {
            Long increment = jedisCluster.incr(redisKey);
            String lockKey = "system:user:login:locktime:" + employee.getId();
            if (increment > pwWrongNum) {
                jedisCluster.set(lockKey, "300");
                jedisCluster.expire(lockKey, lockTime);
                jedisCluster.set(redisKey, increment + "");
                jedisCluster.expire(redisKey, lockTime);
                logger.error("用户[{}]密码/动态口令输错次数[{}]已被锁定，锁定时间[{}]", employee.getName(), increment, lockTime);
            }
            jedisCluster.set(redisKey, increment + "");
            jedisCluster.expire(redisKey, lock);
        }
    }

    @Override
    public int addMenuToUser(User user, String menuIds) {
        String[] mIds = menuIds.split(",");
        // 删除用户下所有特殊菜单
        int count = userMapper.deleteUserMenu(user.getId());
        for (String menuId : mIds) {
            if ("".equals(menuId)) {
                return count;
            }
            // 菜单id包含字母 则不保存
            String regex = ".*[a-zA-Z]+.*";
            Matcher m = Pattern.compile(regex).matcher(menuId);
            if (m.matches()) {
                continue;
            }
            // 新增用户菜单关系
            userMapper.addUserMenu(user.getId(), menuId);
            count++;
        }
        return count;
    }

    @Override
    public void updateManageChannel(User user) {
        userMapper.updateManageChannel(user);
    }


    @Override
    public User selectUserByNo(String email) {
        User usertmp = null;
        if (email.matches(Constants.EMAILMATCH)) {
            usertmp = userMapper.login(email);
        } else if (email.matches(Constants.MOBILE_MATCH)) {
            usertmp = userMapper.loginByMobile(email);
        } else {
            usertmp = getUserByNo(email);
        }
        return usertmp;
    }

    @Override
    public List<Menu> selectUserMenu(Set<String> set) {
        List<Menu> lists = menuMapper.selectUserMenuList(set);
        return lists;
    }
}
