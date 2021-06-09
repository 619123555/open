package com.open.boss.service.system.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.open.boss.mapper.MenuMapper;
import com.open.boss.service.system.MenuService;
import com.open.boss.entity.Menu;
import com.open.boss.entity.MenuNode;
import com.open.boss.entity.User;
import com.open.boss.utils.UserUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuMapper menuMapper;

    @Override
    public int deleteMenu(Menu menu) {
        return menuMapper.deleteByPrimaryKey(menu);
    }

    @Override
    public Menu getMenu(String id) {
        return menuMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Menu> findMenuList() {
        List<Menu> menuList = null;
        User user = UserUtils.getUser();
        Menu m = new Menu();
        if ("1".equals(user.getId())) {
            m.setStatus("0");
            menuList = menuMapper.selectMenuList(m);
        } else {
            m.setUserId(user.getId());
            menuList = menuMapper.findByUserId(m);
        }
        return menuList;
    }

    @Override
    public List<Menu> findMenuCardList() {
        List<Menu> menuList = null;
        User user = UserUtils.getUser();
        Menu m = new Menu();
        if (UserUtils.isSuperAdmin()) {
            m.setStatus("0");
            menuList = menuMapper.selectMenuList(m);
        } else {
            menuList = menuMapper.findMenuListByUserId(user.getId());
        }
        return menuList;
    }

    @Override
    public List<MenuNode> getMenuListToJson(List<Menu> treelist) {
        List<MenuNode> children = new ArrayList<>();
        if (treelist == null || treelist.size() == 0) {
            return children;
        }
        String rootMenuNode = "1";
        for (Menu m : treelist) {
            // 一级菜单
            if (rootMenuNode.equals(m.getParentId())) {
                MenuNode node = new MenuNode();
                node.setMenuId(m.getId());
                node.setKey(m.getId());
                node.setTo(m.getHref());
                node.setParentId(rootMenuNode);
                node.setName(m.getName());
                node.setImage(m.getIcon());
                node.setIsShow(m.getIsShow());
                node.setRemarks(m.getRemarks());
                node.setSystemSign(m.getSystemSign());
                List<MenuNode> menuNodes = new ArrayList<>();
                // 二级菜单
                for (Menu mb : treelist) {
                    if (m.getId().equals(mb.getParentId())) {
                        MenuNode c = new MenuNode();
                        c.setMenuId(mb.getId());
                        c.setKey(mb.getId());
                        c.setTo(mb.getHref());
                        c.setParentId(m.getId());
                        c.setName(mb.getName());
                        c.setImage(mb.getIcon());
                        c.setIsShow(mb.getIsShow());
                        c.setRemarks(mb.getRemarks());
                        c.setSystemSign(mb.getSystemSign());
                        c.setChildren(null);
                        menuNodes.add(c);
                    }
                }
                if (menuNodes.size() > 0) {
                    node.setChildren(menuNodes);
                }
                children.add(node);
            }
        }
        return children;
    }

    @Override
    public List<Menu> findMenuListParent(Set set) {
        return menuMapper.findMenuListParent(set);
    }

    @Override
    public List<MenuNode> getMenuJsonCache() {
        List<MenuNode> menuNodes;
        String userId = UserUtils.getUser().getId();

        // 获取该用户下所有菜单
        List<Menu> treelist = this.findMenuCardList();
        if (UserUtils.isSuperAdmin()) {
            menuNodes = this.getMenuListToJson(treelist);
            String content = JSON.toJSONString(menuNodes);
            return menuNodes;
        }
        Set<String> set = new HashSet<>();
        for (Menu m : treelist) {
            set.add(m.getParentId());
        }
        // 查询一级菜单列表
        List<Menu> parentList = this.findMenuListParent(set);
        // 添加二级菜单
        for (Menu mu : treelist) {
            boolean result = true;
            for (Menu u : parentList) {
                if (u.getId().equals(mu.getId())) {
                    result = false;
                }
            }
            if (result) {
                parentList.add(mu);
            }
        }
        // 生成菜单串
        menuNodes = this.getMenuListToJson(parentList);
        String content = JSON.toJSONString(menuNodes);
        return menuNodes;
    }

    @Override
    public boolean checkAuth(String url) {
        List<MenuNode> menuNodes = this.getMenuJsonCache();
        if (StringUtils.isEmpty(url) || menuNodes == null || menuNodes.size() == 0) {
            return true;
        }
        for (MenuNode m : menuNodes) {
            if (!StringUtils.isEmpty(m.getTo()) && url.contains(m.getTo())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveMenu(Menu menu) {
        // 获取父节点实体
        menu.setParent(this.getMenu(menu.getParent().getId()));
        String oldParentIds = "";
        Menu mu = getMenu(menu.getId());
        if (mu != null) {
            oldParentIds = mu.getParentIds();
        }

        Menu parentMenu = menu.getParent();

        // 设置新的父节点串
        menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");
        // 保存或更新实体
        menu.setStatus("1".equals(menu.getIsShow()) ? "0" : "1");
        if (StringUtils.isEmpty(menu.getId())) {
            menu.setId(IdUtil.simpleUUID());
            menu.setParentId(menu.getParents().getId());
            menu.setSort(1L);
            menu.setCreator(UserUtils.getUser().getId());
            menu.setOperator(menu.getCreator());
            menu.setCreateTime(new Date());
            menu.setOperTime(new Date());
            if (StringUtils.isEmpty(menu.getParentId())) {
                menu.setParentId("1");
            }
            menuMapper.insertSelective(menu);
        } else {
            menuMapper.updateByPrimaryKeySelective(menu);
        }

        // 更新子节点 parentIds
        Menu m = new Menu();
        m.setParentIds("%," + menu.getId() + ",%");
        List<Menu> list = menuMapper.findByParentIdsLike(m);
        for (Menu e : list) {
            e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
            menuMapper.updateParentIds(e);
        }
    }
}
