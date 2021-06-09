package com.open.boss.mapper;

import com.open.boss.entity.Role;
import com.open.boss.entity.User;
import com.open.boss.entity.UserOthProObj;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
  List<User> findList(User user);

  int deleteUserRole(User user);

  int insertUserRole(User user);

  UserOthProObj getUserMaxNo(UserOthProObj userOthProObj);

  List<User> showSelectedRoleUser(Role role);

  User selectOrgUser(User user);

  /** 删除机构下的人员与角色关联信息 */
  int deleteOrgUserRole(User user);

  User getByNo(User user);

  /**
   * 根据登录名、邮箱、手机查询用户
   *
   * @return
   */
  User getByNoOrMobileOrEmail(User user);

  /**
   * 通过no进行查询，返回所有的用户编号和名称map集合
   *
   * @param no
   * @return
   */
  List<User> findUserInfoList(List<String> no);

  User login(String email);

  /**
   * 插入用户菜单关联数据
   *
   * @param id
   * @param menuId
   * @return
   */
  int addUserMenu(@Param("id") String id, @Param("menuId") String menuId);

  /**
   * 删除用户菜单关联数据
   *
   * @param id
   * @return
   */
  int deleteUserMenu(@Param("id") String id);

  int updateManageChannel(User user);

  int deleteUserRoleId(@Param("userId") String userId, @Param("roleId") String roleId);

  int selectUserExistByEmailOrMobile(User user);

  User loginByMobile(@Param("mobile") String mobile);


  List<User> selectUserByRoleId(@Param("roleId") String roleId);
}
