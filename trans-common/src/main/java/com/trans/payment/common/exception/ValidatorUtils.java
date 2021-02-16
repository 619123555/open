package com.trans.payment.common.exception;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

public class ValidatorUtils {

    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
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
