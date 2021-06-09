package com.open.boss.utils;

import com.open.common.utils.SpringContextHolder;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

public class ValidateUtil {
    /**
     * 实体校验
     *
     * @param obj
     * @throws
     */
    public static <T> String validate(T obj) throws RuntimeException {
        Validator validator = SpringContextHolder.getBean(Validator.class);
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj, Default.class);
        if (constraintViolations.size() > 0) {
            ConstraintViolation<T> validateInfo =
                    (ConstraintViolation<T>) constraintViolations.iterator().next();
            return validateInfo.getMessage();
            // validateInfo.getMessage() 校验不通过时的信息，即message对应的值
            //  throw new BusinessException(BusinessException.UNKNOWN_ERROR);
        }
        return null;
    }
}
