package com.trans.payment.common.utils.validator;

import com.trans.payment.common.exception.PreException;
import org.apache.commons.lang3.StringUtils;

public class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new PreException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new PreException(message);
        }
    }
}
