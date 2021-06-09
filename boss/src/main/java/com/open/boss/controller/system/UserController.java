package com.open.boss.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.open.boss.commons.Result;
import com.open.boss.controller.BaseController;
import com.open.boss.mapper.OrganizationMapper;
import com.open.boss.mapper.UserMapper;
import com.open.boss.service.system.UserService;
import com.open.common.constants.Constants;
import com.open.boss.entity.Organization;
import com.open.boss.entity.User;
import com.open.boss.utils.ConfUtils;
import com.open.boss.utils.ShaPassword;
import com.open.boss.utils.UserUtils;
import com.open.common.utils.DateUtils;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @PostMapping(value = "/listData")
    public Result listData(
            User user,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageHelper.startPage(pageNo, pageSize, "a.id desc");
        List<User> users = userMapper.findList(user);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return ok(pageInfo);
    }

    @PostMapping(value = "updatePassword")
    public Result updatePassword(
            @RequestParam(required = false) String oldPassword,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String password) {
        User user = UserUtils.getUser();
        if (user == null || user.getNo() == null) {
            return error("未登录!");
        } else if (!UserUtils.validatePassword(oldPassword, user.getPassword())) {
            return error("原密码错误!");
        } else if (!newPassword.equals(password)) {
            return error("输入的两次密码不一致!");
        } else {
            User tmpUser = userMapper.selectByPrimaryKey(user.getId());
            tmpUser.setPassword(
                ShaPassword.encryptPassword(Constants.HASH_ALGORITHM, password, tmpUser.getId()));
            userMapper.updateByPrimaryKey(tmpUser);
            return ok("修改成功!");
        }
    }

    @PostMapping(value = "deleteList")
    public Result deleteList(String ids) {
        if (!StringUtils.isNotBlank(ids)) {
            return errorResult("请先选择删除的用户");
        }
        String[] userList = ids.split(",");
        for (int i = 0; i < userList.length; i++) {
            User user = userMapper.selectByPrimaryKey(userList[i]);
            if (UserUtils.getUser().getId().equals(user.getId())) {
                return errorResult("删除用户失败, 不允许删除当前用户");
            } else if (User.isAdmin(user.getId())) {
                return errorResult("删除用户失败, 不允许删除超级管理员用户");
            } else {
                userService.deleteUser(user);
            }
        }
        return successResult("删除用户成功");
    }

    @PostMapping(value = "save")
    public Result save(
            User user, HttpServletRequest request) {
        if (Strings.isNullOrEmpty(user.getId())) {
            // 新增用户校验
            int count = this.userMapper.selectUserExistByEmailOrMobile(user);
            if (count > 0) {
                return error("当前邮箱或手机号已经存在");
            }
            // 所属部门
            Organization organization = this.organizationMapper.selectByPrimaryKey(request.getParameter("office.id"));
            if (organization == null) {
                return error("所属部门参数有误");
            }
            String userNo = this.userService.getUserMaxNoStr(organization);
            user.setNo(userNo);
            user.setCreateTime(DateUtils.getBusinsessDate());
            user.setStatus("0");
        }

        // 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
        user.setCompanyId(request.getParameter("company.id"));
        user.setOfficeId(request.getParameter("office.id"));
        if (StringUtils.isBlank(user.getId()) && !checkNo(user.getNo())) {
            return error("保存用户'" + user.getNo() + "'失败，工号已存在");
        }
        // 保存用户信息
        userService.saveUser(user);
        return ok("保存用户'" + user.getNo() + "'成功");
    }

    @PostMapping(value = "saveUserData")
    public Result saveUserData(User user) {
        if (StringUtils.isBlank(user.getId()) && !checkNo(user.getNo())) {
            // 转换为jsonp类型的数据
            return error("保存用户'" + user.getNo() + "'失败，工号已存在");
        }
        if (StringUtils.isBlank(user.getId())) {
            String dpwd = ConfUtils.getConfValue("common_default_password");
            dpwd = StringUtils.isNotBlank(dpwd) ? dpwd : "123456";
            user.setPassword(UserUtils.entryptPassword(dpwd, user.getId()));
        }
        // 保存用户信息
        userService.saveUser(user);
        return ok("保存用户'" + user.getNo() + "'成功");
    }

    @PostMapping(value = "checkNo")
    public Boolean checkNo(String no) {
        if (no != null && userService.getUserByNo(no) == null) {
            return true;
        }
        return false;
    }

    @PostMapping(value = "resetPsw")
    public Result resetPsw(User user, HttpServletRequest request, Model model) {
        if (user.getId() == null) {
            return error("验证不通过!");
        }
        // 获取默认密码
        String dpwd = ConfUtils.getConfValue("common_default_password");
        dpwd = StringUtils.isNotBlank(dpwd) ? dpwd : "123456";
        user = userMapper.selectByPrimaryKey(user.getId());
        user.setPassword(UserUtils.entryptPassword(dpwd, user.getId()));
        user.setOperTime(new Date());
        int i = userMapper.updateByPrimaryKeySelective(user);
        if (i == 1) {
        }
        return ok("重置密码成功");
    }


    @RequiresRoles(value = "R0059")
    @PostMapping(value = "cleanCache")
    public Result cleanCache(String ids) {
        String[] userList = ids.split(",");
        for (String id : userList) {
            // 冻结用户
            User user = userService.selectUserByPrimaryKey(id);
            if (UserUtils.getUser().getId().equals(user.getId())) {
                return errorResult("冻结用户失败, 不允许冻结当前用户");
            } else if (User.isAdmin(user.getId())) {
                return errorResult("冻结用户失败, 不允许冻结超级管理员用户");
            }
            if ("0".equals(user.getStatus())) {
                user.setStatus("1");
            } else {
                user.setStatus("0");
            }
            userMapper.updateByPrimaryKeySelective(user);
        }
        return successResult("冻结用户成功");
    }
}
