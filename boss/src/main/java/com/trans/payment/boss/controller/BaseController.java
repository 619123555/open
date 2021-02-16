package com.trans.payment.boss.controller;

import com.trans.payment.boss.commons.Result;
import com.trans.payment.boss.commons.ResultGenerator;
import com.trans.payment.boss.entity.User;
import com.trans.payment.boss.utils.UserUtils;
import com.trans.payment.common.exception.PreException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);


    public Result successResult(Object object) {
        return ok(object);
    }

    public Result errorResult(String message) {
        return error(message);
    }

    public String bindingResultToString(BindingResult bindingResult) {
        List<String> errorReason = new ArrayList<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorReason.add(error.getDefaultMessage());
        }
        return StringUtils.join(errorReason.toArray(), ",");
    }

    public Result ok() {
        return ResultGenerator.ok();
    }

    public Result ok(Object o) {
        return ResultGenerator.ok(o);
    }

    public Result error(String o) {
        return ResultGenerator.error(o);
    }

    public Result error() {
        return ResultGenerator.error();
    }


    @ExceptionHandler({BindException.class, MissingServletRequestParameterException.class,
            PreException.class,
            TypeMismatchException.class, RuntimeException.class, UnauthorizedException.class})
    @ResponseBody
    public Result bindException(Exception e) {
        logger.error("异常消息", e);
        if (e instanceof BindException) {
            return error("参数绑定异常");
        } else if (e instanceof PreException) {
            return error(((PreException) e).getMsg());
        } else if (e instanceof UnauthorizedException) {
            return error("缺少该操作权限，请联系管理员");
        }
        return error("处理异常");
    }

    /**
     * 渠道帐号获取管理的代理商编码
     * @return 代理商编号
     */
    public String getShiroSessionAgentNo() {
        User user = UserUtils.getUser();
        if (user == null) {
            throw new PreException("登录会话已过期", 401);
        }
        String agentNo = user.getManageChannel();
        if (StringUtils.isEmpty(agentNo)) {
            agentNo = user.getNo();
        }
        return agentNo;
    }

    /**
     * 获取当前登录帐号用户编号
     * @return 用户编号
     */
    public String getShiroSessionNo() {
        User user = UserUtils.getUser();
        if (user == null) {
            throw new PreException("登录会话已过期", 401);
        }
        return user.getNo();
    }
}
