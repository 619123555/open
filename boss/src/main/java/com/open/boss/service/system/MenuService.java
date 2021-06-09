package com.open.boss.service.system;

import com.open.boss.entity.Menu;
import com.open.boss.entity.MenuNode;
import java.util.List;
import java.util.Set;

public interface MenuService {

    int deleteMenu(Menu menu);


    void saveMenu(Menu menu);


    Menu getMenu(String id);


    List<Menu> findMenuList();


    List<Menu> findMenuCardList();


    List<MenuNode> getMenuListToJson(List<Menu> lists);


    List<Menu> findMenuListParent(Set set);


    List<MenuNode> getMenuJsonCache();

    boolean checkAuth(String url);
}
