package com.open.boss.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.open.boss.commons.Result;
import com.open.boss.entity.Role;
import com.open.boss.entity.RoleAssignTransfer;
import com.open.boss.mapper.RoleMapper;
import com.open.boss.mapper.UserMapper;
import com.open.boss.service.system.RoleService;
import com.open.boss.controller.BaseController;
import com.open.boss.entity.Menu;
import com.open.boss.entity.Organization;
import com.open.boss.entity.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "/role"
)
public class RoleController extends BaseController {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping(value = "/showRolePageData")
    public Result listData(
            Role role,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
            role.setStatus("1");
            PageHelper.startPage(pageNo, pageSize, "create_dt DESC");
            List<Role> users = roleMapper.select(role);
            PageInfo<Role> pageInfo = new PageInfo<>(users);
            return ok(pageInfo);
    }

    /**
     * 验证角色名是否有效 API 接口
     * @param name
     * @return
     */
    @PostMapping(value = "/checkName")
    public String checkName(String name) {
        if (name != null && roleMapper.getRoleByName(name) == null) {
            return "true";
        }
        return "false";
    }

    /**
     * 验证角色英文名是否有效 API 接口
     * @param code
     * @return
     */
    @PostMapping(value = "/checkEnname")
    public String checkEnname(String code) {
        if (code != null && roleMapper.getRoleByCode(code) == null) {
            return "true";
        }
        return "false";
    }

    @PostMapping(value = "/saveRoleData")
    public Result saveRoleData(Role role, HttpServletRequest request, HttpServletResponse response) {
        if (!"true".equals(checkName(role.getName()))) {
            return error("保存角色'" + role.getName() + "'失败, 角色名已存在");
        }
        if (!"true".equals(checkEnname(role.getCode()))) {
            return error("保存角色'" + role.getName() + "'失败, 英文名已存在");
        }
        roleService.saveRole(role);
        return ok("保存角色'" + role.getName() + "'成功");
    }

    @PostMapping(value = "/delete")
    public Result delete(Role role) {
        roleMapper.deleteByPrimaryKey(role);
        int count = roleService.deleteRoleMenuByRoleId(role);
        return ok("删除角色成功");
    }

    @PostMapping(value = "/selectRoleMaxCode")
    public Result selectRoleMaxCode() {
        Role role = roleMapper.selectRoleMaxCode(new Role());
        String code = String.valueOf(Integer.parseInt(role.getCode()) + 1);
        code = StringUtils.leftPad(code, 4, "0");
        code = "R" + code;
        return ok(code);
    }

    /**
     * 根据角色Id,查询角色对象信息 API 接口 原接口 form(Role role, Model model),detail(Role role, Model model)
     * @param role
     * @return
     */
    @PostMapping(value = "/getRoleDetail")
    public Result getRoleObj(Role role) {
        role = roleMapper.selectByPrimaryKey(role.getId());
        List<Menu> menuList = roleMapper.selectRoleMenu(role.getId());
        if (menuList != null && menuList.size() > 0) {
            role.setMenuList(menuList);
        }
        List<Menu> lists = roleService.findAllMenu();
        // 获取选中菜单的数组
        Map map = new HashMap<>(16);
        map.put("data", role);
        map.put("dataMenuList", lists);
        List<String> keys = new ArrayList();
        for (Menu m : role.getMenuList()) {
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
            keys.add(m.getId());
        }
        map.put("keyArr", keys);
        return ok(map);
    }

    @PostMapping(value = "/assignRoleData")
    public Result assignRoleData(
            Role role,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        User user = new User(role);
        role = roleMapper.selectByPrimaryKey(role.getId());

        PageHelper.startPage(pageNo, pageSize, "a.id desc");
        List<User> users = userMapper.findList(user);
        PageInfo<User> pageInfo = new PageInfo<>(users);

        Map<String, Object> map = new HashMap(16);
        map.put("role", role);
        map.put("page", pageInfo);
        return ok(map);
    }

    /**
     * 根据角色ID 查询对应的角色已分配用户及根据机构ID查询机构下的人员信息 API 接口
     */
    @PostMapping(value = "showUsersAndOrg")
    public Result showUsersAndOrg(
            @RequestParam(required = false) String orgId, @RequestParam(required = false) String roleId) {
        Organization org = new Organization();
        org.setId(orgId);
        User user = new User();
        user.setOffice(org);
        List<User> ulist = userMapper.findList(user);
        // 机构下的用户列表
        List<RoleAssignTransfer> alllists = transformUser(ulist);
        Role role = new Role();
        role.setId(roleId);
        List<User> slist = userMapper.showSelectedRoleUser(role);
        // 机构下已分配的用户列表
        String[] selectedlists = new String[slist.size()];
        for (int i = 0; i < slist.size(); i++) {
            selectedlists[i] = slist.get(i).getId();
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("allLists", alllists);
        map.put("selectedLists", selectedlists);
        return ok(map);
    }

    /**
     * 用户转换对象
     * @param lists
     * @return
     */
    public static List<RoleAssignTransfer> transformUser(List<User> lists) {
        List<RoleAssignTransfer> tlists = new ArrayList<>();
        for (User u : lists) {
            RoleAssignTransfer uassignTransfer = new RoleAssignTransfer();
            uassignTransfer.setKey(u.getId());
            uassignTransfer.setChosen(false);
            uassignTransfer.setDescription(u.getName());
            uassignTransfer.setTitle(u.getNo());
            tlists.add(uassignTransfer);
        }
        return tlists;
    }

    @PostMapping(value = "assignRoleDetailData")
    public Result assignRoleDetail(String roleId, String idsStr, String orgId) {
        if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(idsStr)) {
            return errorResult("参数不能为空");
        }
        Role role = roleMapper.selectByPrimaryKey(roleId);
        if (role == null) {
            return errorResult("当前角色不存在");
        }
        int newNum = roleService.batchInsertUserRoleList(roleId, idsStr);
        return successResult("已成功分配 " + newNum + " 个用户到角色【" + role.getName() + "】");
    }

    /**
     * 移出用户 API 接口 原接口 outrole(String userId, String roleId, RedirectAttributes redirectAttributes)
     * @return
     */
    @PostMapping(value = "outRoleData")
    public Result outRoleData(String userId, String roleId) {
        Role role = roleMapper.selectByPrimaryKey(roleId);
        userMapper.deleteUserRoleId(userId, roleId);
        return successResult("角色移出'" + role.getName() + "'成功");
    }

    /**
     * 删除角色信息数据
     * @param role
     * @return
     */
    @PostMapping(value = "deleteRoleData")
    public Result deleteRoleData(Role role) {
        role.setDataScope(subSplitToInStr(role.getDataScope()));
        roleMapper.deleteDataScope(role);
        roleMapper.deleteUserRoleByRoleId(role);
        return ok("删除角色成功");
    }

    public static String subSplitToInStr(String str) {
        StringBuffer conditionStr = new StringBuffer();
        String resStr = "";
        if ((str != null) && (!"".equals(str))) {
            conditionStr.append("('");
            String[] strs = str.split(",");
            for (String strp : strs) {
                conditionStr.append(strp).append("','");
            }
            resStr = new StringBuilder().append(conditionStr.substring(0, conditionStr.length() - 2)).append(")").toString();
        }
        return resStr;
    }
}
