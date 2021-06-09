package com.open.boss.mapper;

import com.open.boss.entity.Menu;
import com.open.boss.entity.Role;
import com.open.boss.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface RoleMapper extends Mapper<Role> {
  List<Role> getRoleByUserId(@Param("userId") String userId);

  List<Role> findList(Role role);

  Role getRoleByName(@Param("name") String name);

  Role getRoleByCode(@Param("code") String code);

  int deleteRoleMenu(Role role);

  int insertRoleMenu(Role role);

  Role selectRoleMaxCode(Role role);

  Role getRoleMenuList(String id);

  List<User> showSelectedRoleUser(Role role);


  /**
   * 根据用户no查询角色列表信息 yzy
   * @param no
   * @return
   */
  List<Role> findUserRole(@Param("no") String no);


  int deleteUserRole(@Param("userId") String userId);


  /**
   * 批量删除角色
   */
  int deleteDataScope(Role role);


  int deleteUserRoleByRoleId(Role r);


  List<Menu> selectRoleMenu(@Param("roleId") String roleId);

  Integer checkPerssion(String id);
}
