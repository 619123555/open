package com.trans.payment.boss.service.system;

import com.trans.payment.boss.commons.Result;
import com.trans.payment.boss.entity.Menu;
import com.trans.payment.boss.entity.Organization;
import com.trans.payment.boss.entity.Role;
import com.trans.payment.boss.entity.User;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

public interface UserService {

    void deleteUser(User user);


    User getUserByNo(String no);


    void saveUser(User user);

    String getUserMaxNoStr(Organization organization);


    List<Role> getRoleByUserId(String id);

    Role getSpecialMenu(String id);

    List<String> getMenuKeys(String userId);

    User assignUserToRole(Role role, User user);

    User getByNo(User user);

    User getByNoOrMobileOrEmail(User user);

    List<User> findUserInfoList(List<String> userNos);

    List<User> findUserPageList(User user);


    /**
     * 登录
     * @param user user
     * @return TbUser
     */
    User login(String user);

    /**
     * 根据手机号查询
     * @param mobile mobile
     * @return user
     */
    User selectUserByMobile(String mobile);


    /**
     * 根据主键查询用户信息
     * @param id id
     * @return TbUser
     */
    User selectUserByPrimaryKey(String id);


    Result login(HttpServletRequest request);


    int addMenuToUser(User user, String menuIds);


    void updateManageChannel(User user);


    User selectUserByNo(String email);

    /**
     * selectUserMenu set
     * @param set set
     * @return List
     */
    List<Menu> selectUserMenu(Set<String> set);
}
