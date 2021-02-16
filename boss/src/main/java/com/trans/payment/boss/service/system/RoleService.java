package com.trans.payment.boss.service.system;

import com.trans.payment.boss.entity.Menu;
import com.trans.payment.boss.entity.Role;
import com.trans.payment.boss.entity.User;
import java.util.List;

public interface RoleService {

    void saveRole(Role role);


    int batchInsertUserRoleList(String roleId, String ids);


    List<Menu> findAllMenu();

    void deleteUserRole(User user);


    List<Role> findRole(Role role);

    List<Role> findUserRole(String no);

    Integer checkPerssion(String id);

    int deleteRoleMenuByRoleId(Role role);
}
