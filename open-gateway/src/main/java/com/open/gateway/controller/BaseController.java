package com.open.gateway.controller;

import com.open.common.constants.CommonEnum;
import com.open.common.exception.GatewayException;
import com.open.common.exception.PreException;
import com.open.gateway.util.Result;
import com.open.gateway.util.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected Result ok() {
        return ResultGenerator.ok();
    }

    protected Result ok(String msg) {
        return ResultGenerator.ok(msg);
    }

    protected Result error(String o) {
        return ResultGenerator.error(o);
    }

    public static Result error(String code, String message) {
        return ResultGenerator.error(code, message);
    }

    @ExceptionHandler({BindException.class, GatewayException.class, PreException.class, RuntimeException.class})
    @ResponseBody
    public Result bindException(Exception e) {
        logger.error("Exception", e);
        if (e instanceof BindException) {
            return error(CommonEnum.ISV_INVALID_PARAMETER.getMsg());
        } else if (e instanceof GatewayException) {
            return error(((GatewayException) e).getCode(), ((GatewayException) e).getMsg());
        } else if (e instanceof PreException) {
            return error(((PreException) e).getCode() + "", ((PreException) e).getMsg());
        }
        return error(CommonEnum.ISV_SUSPECTED_ATTACK.getMsg());
    }
}
