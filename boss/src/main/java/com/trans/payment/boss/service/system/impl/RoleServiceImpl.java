package com.trans.payment.boss.service.system.impl;


import cn.hutool.core.util.IdUtil;
import com.trans.payment.boss.entity.Menu;
import com.trans.payment.boss.entity.Role;
import com.trans.payment.boss.entity.User;
import com.trans.payment.boss.entity.UserRole;
import com.trans.payment.boss.mapper.MenuMapper;
import com.trans.payment.boss.mapper.RoleMapper;
import com.trans.payment.boss.mapper.UserMapper;
import com.trans.payment.boss.mapper.UserRoleMapper;
import com.trans.payment.boss.service.system.RoleService;
import com.trans.payment.boss.utils.UserUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public void saveRole(Role role) {
        if (StringUtils.isEmpty(role.getId())) {
            role.setCreator(UserUtils.getUser().getId());
            role.setOperator(role.getCreator());
            role.setCreateDt(new Date());
            role.setOperDt(new Date());
            role.setId(IdUtil.simpleUUID());
            role.setStatus("1");
            roleMapper.insert(role);
        } else {
            role.setOperDt(new Date());
            roleMapper.updateByPrimaryKeySelective(role);
        }
        // 更新角色与菜单关联
        roleMapper.deleteRoleMenu(role);
        // 清除该角色下所有用户缓存
        clearRoleMenu(role.getId());
        if (role.getMenuList().size() > 0) {
            // 菜单id包含字母 则不保存
            String regex = ".*[a-zA-Z]+.*";
            for (int i = 0; i < role.getMenuList().size(); i++) {
                Matcher m = Pattern.compile(regex).matcher(role.getMenuList().get(i).getId());
                if (m.matches()) {
                    role.getMenuList().remove(i);
                }
            }
            roleMapper.insertRoleMenu(role);
        }
    }

    public void clearRoleMenu(String roleId) {
        List<User> users = userMapper.selectUserByRoleId(roleId);
        if (users != null) {
            for (User ur : users) {
                // 清除缓存
            }
        }
    }

    @Override
    public int deleteRoleMenuByRoleId(Role role) {
        if (role == null || StringUtils.isEmpty(role.getId())) {
            return 0;
        }
        clearRoleMenu(role.getId());
        return roleMapper.deleteRoleMenu(role);
    }

    @Override
    public int batchInsertUserRoleList(String roleId, String idsStr) {
        String[] idsArr = idsStr.split(",");
        List<UserRole> roles = new ArrayList<>();
        UserRole tmp = new UserRole();
        tmp.setRoleId(roleId);
        List<UserRole> existList = userRoleMapper.select(tmp);
        for (String s : idsArr) {
            UserRole r = new UserRole();
            r.setRoleId(roleId);
            r.setUserId(s);
            boolean result = true;
            if (existList != null && existList.size() > 0) {
                for (UserRole ur : existList) {
                    if (ur.getUserId().equals(s)) {
                        result = false;
                        break;
                    }
                }
            }
            if (result) {
                roles.add(r);
            }
        }
        if (roles.size() > 0) {
            return userRoleMapper.insertList(roles);
        }
        return 0;
    }

    @Override
    public List<Menu> findAllMenu() {
        List<Menu> menuList;
        User user = UserUtils.getUser();
        if (user == null) {
            return null;
        }
        Menu m = new Menu();
        m.setStatus("0");
        if ("1".equals(user.getId())) {
            menuList = menuMapper.select(m);
        } else {
            m.setUserId(user.getId());
            menuList = menuMapper.findByUserId(m);
        }
        return menuList;
    }

    @Override
    public void deleteUserRole(User user) {
        if (!StringUtils.isEmpty(user.getId())) {
            // 删除用户与角色关联
            roleMapper.deleteUserRole(user.getId());
        }
    }

    @Override
    public List<Role> findRole(Role role) {
        return roleMapper.findList(role);
    }

    @Override
    public List<Role> findUserRole(String no) {
        return roleMapper.findUserRole(no);
    }

    @Override
    public Integer checkPerssion(String id) {
        return roleMapper.checkPerssion(id);
    }
}
