package com.trans.payment.common.dto.gateway;

import java.io.Serializable;
import lombok.Data;

@Data
public class RsaSecurityReq implements Serializable {

    private static final long serialVersionUID = -3512019659016255289L;

    /**
     * 操作操作方式:query  查询、reset  重置
     */
    private String operatMode;

    /**
     * 公钥
     */
    private String publicKey;
}
