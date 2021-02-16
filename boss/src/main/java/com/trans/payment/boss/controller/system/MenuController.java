package com.trans.payment.boss.controller.system;

import com.trans.payment.common.constants.Constants;
import com.trans.payment.boss.commons.Result;
import com.trans.payment.boss.controller.BaseController;
import com.trans.payment.boss.entity.Dict;
import com.trans.payment.boss.entity.Menu;
import com.trans.payment.boss.entity.MenuNode;
import com.trans.payment.boss.mapper.MenuMapper;
import com.trans.payment.boss.service.system.MenuService;
import com.trans.payment.boss.utils.DictUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping(
        value = "/menu"
)
public class MenuController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private MenuService menuService;

    @Resource
    private JedisCluster jedisCluster;

    /**
     * 提供菜单节点数据 API接口
     */
    @PostMapping(value = "/treeMenuData")
    public Result treeMenuData() {
        try {
            List<MenuNode> menuNodes = this.menuService.getMenuJsonCache();
            return ok(menuNodes);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            return error("获取菜单异常");
        }
    }

    /**
     * 删除接口菜单信息 API 接口 原接口 delete(Menu menu)
     * @param menu
     * @return
     */
    @RequiresRoles(value = "R0059")
    @PostMapping(value = "/deleteMenuData")
    public Result deleteMenuData(Menu menu) {
        try {
            menuService.deleteMenu(menu);
            return ok("操作成功");
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            return error("处理异常");
        }
    }

    /**
     * 修改菜单信息 Api 接口 原接口 save(Menu menu)
     * @param menu
     * @return
     */
    @RequiresRoles(value = "R0059")
    @PostMapping(value = "updateMenuData")
    public Result saveMenuData(Menu menu) {
        try {
            boolean flag = false;
            List<Dict> dicts = DictUtils.getDictList("Common_DictScope");
            for (Dict dict : dicts) {
                if (menu.getPIds().equals(dict.getValue())) {
                    flag = true;
                    break;
                }
            }
            if (menu.getParent() == null || StringUtils.isBlank(menu.getParentId()) || flag) {
                menu.setParent(new Menu(Menu.getRootId()));
            }
            menuService.saveMenu(menu);
            return ok("操作成功");
        } catch (Exception e) {
            return error("处理异常");
        }
    }

    @PostMapping(value = "/treeData")
    public Result treeData(Menu mm) {
        try {
            mm.setStatus("0");
            List<Menu> treelist = menuMapper.select(mm);
            List<MenuNode> menuNodes = menuService.getMenuListToJson(treelist);
            return ok(menuNodes);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            return error("处理异常");
        }
    }


    /**
     * 保存菜单信息
     * @param menu
     * @return
     */
    @PostMapping(value = "save")
    public Result save(Menu menu) {
        try {
            boolean flag = false;
            List<Dict> dicts = DictUtils.getDictList("Common_DictScope");
            for (Dict dict : dicts) {
                if (menu.getPIds().equals(dict.getValue())) {
                    flag = true;
                    break;
                }
            }
            if (menu.getParent() == null || StringUtils.isBlank(menu.getParentId()) || flag) {
                menu.setParent(new Menu(Menu.getRootId()));
            }
            menu.setParent(menuService.getMenu(menu.getParent().getId()));
            // 获取排序号，最末节点排序号+1
            if (StringUtils.isBlank(menu.getId())) {
                List<Menu> list = new ArrayList<>();
                List<Menu> sourcelist = menuService.findMenuList();
                Menu.sortList(list, sourcelist, menu.getParentId(), false);
                if (list.size() > 0) {
                    menu.setSort(list.get(list.size() - 1).getSort() + 30);
                }
            }
            menuService.saveMenu(menu);
            return ok("操作成功");
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            return error("处理异常");
        }
    }


    @PostMapping(value = "cleanCache")
    public Result cleanCache() {
        String key = Constants.CARD_USER_MENU;
        if (this.jedisCluster.exists(key)) {
            this.jedisCluster.del(key);
        }
        return ok("清除用户缓存成功");
    }
}
