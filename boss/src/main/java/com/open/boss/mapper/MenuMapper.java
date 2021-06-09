package com.open.boss.mapper;

import com.open.boss.entity.Menu;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface MenuMapper extends Mapper<Menu> {
  List<Menu> selectParentList(Menu menu);

  List<Menu> getUserMenu(String id);

  int deleteMenu(Menu var);

  List<Menu> findByParentIdsLike(Menu menu);

  int updateParentIds(Menu menu);

  List<Menu> selectMenuList(Menu menu);

  List<Menu> findByUserId(Menu menu);

  List<Menu> findAllList(Menu menu);

  List<Menu> findMenuListByUserId(@Param("userId") String userId);

  List<Menu> findMenuListParent(Set set);

  List<Menu> selectUserMenuByRoleId(@Param("roleId") String roleId);

  /**
   * 根据角色查询用户所有菜单
   *
   * @param set set
   * @return List
   */
  List<Menu> selectUserMenuList(Set<String> set);
}
