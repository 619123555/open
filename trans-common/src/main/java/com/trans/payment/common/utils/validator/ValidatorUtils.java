package com.trans.payment.common.utils.validator;

import com.trans.payment.common.exception.GatewayException;
import com.trans.payment.common.exception.PreException;
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
     * 校验对象
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws PreException 校验不通过，则报RRException异常
     */
    public static void validateEntity(Object object, Class<?>... groups) throws PreException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation<Object> constraint =
                    constraintViolations.iterator().next();
            throw new PreException(constraint.getMessage());
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
