package com.trans.payment.boss.utils;

import com.trans.payment.common.constants.Constants;
import com.trans.payment.boss.entity.Organization;
import com.trans.payment.boss.entity.User;
import com.trans.payment.boss.mapper.OrganizationMapper;
import com.trans.payment.boss.mapper.UserMapper;
import com.trans.payment.common.utils.SpringContextHolder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class UserUtils {

    public UserUtils() {
    }

    private static final Logger logger = LoggerFactory.getLogger(UserUtils.class);


    private static OrganizationMapper organizationMapper =
            SpringContextHolder.getBean(OrganizationMapper.class);

    private static UserMapper userMapper =
            SpringContextHolder.getBean(UserMapper.class);

    public static User getUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

    public static User getUser(String no) {
        User u = new User();
        u.setNo(no);
        return userMapper.selectOne(u);
    }

    public static Organization getUserOrganization() {
        User user = getUser();
        Organization o = new Organization();
        o.setId(user.getCompanyId());
        return organizationMapper.selectOne(o);
    }

    public static Organization getUserOrganization(User user) {
        Organization org = null;
        if (user != null) {
            org = organizationMapper.selectByPrimaryKey(user.getCompanyId());
        }
        return org;
    }

    public static Session getSession() {
        Subject subject = SecurityUtils.getSubject();
        return subject.getSession();
    }

    public static boolean isSuperAdmin() {
        User user = getUser();
        return "1".equals(user.getId());
    }

    public static boolean isSuperAdmin(String id) {
        return "1".equals(id);
    }

    public static boolean validatePassword(String plainPassword, String password) {
        User user = getUser();
        String pwd = ShaPassword.encryptPassword(
                Constants.HASH_ALGORITHM, plainPassword, user.getId());
        return password.equals(pwd);
    }

    public static String entryptPassword(String pwd, String salt) {
        return ShaPassword.encryptPassword(
                Constants.HASH_ALGORITHM, pwd, salt);
    }

    public static boolean checkRole(String roleNo) {
        if (StringUtils.isEmpty(roleNo)) {
            return false;
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            return UserUtils.isSuperAdmin() || subject.hasRole(roleNo);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        return false;
    }
}
