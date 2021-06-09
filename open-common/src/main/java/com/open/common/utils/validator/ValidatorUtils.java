package com.open.common.utils.validator;

import com.open.common.exception.GatewayException;
import com.open.common.exception.ParamErrorException;
import com.open.common.exception.PreException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

public class ValidatorUtils {

    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 通用参数校验
     * @param object 待校验对象
     * @param groups 待校验的组
     * @return
     */
    public static void validateEntity(Object object, Class<?>... groups) throws PreException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation<Object> constraint =
                constraintViolations.iterator().next();
            throw new ParamErrorException((constraint.getMessage()));
        }
    }


    public static void gatewayValidateEntity(Object object, Class<?>... groups) throws PreException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation<Object> constraint =
                    constraintViolations.iterator().next();
            throw new GatewayException(constraint.getMessage());
        }
    }
}
