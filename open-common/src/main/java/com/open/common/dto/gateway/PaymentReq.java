package com.open.common.dto.gateway;

import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class PaymentReq implements Serializable {
    @NotBlank(message = "代付类型不允许为空")
    private String payType;
    @NotBlank(message = "订单号不允许为空")
    private String tradeNo;
    @NotBlank(message = "代付金额不允许为空")
    private Integer amount;
//    private String payerAccNo;
//    private String payerAccName;
    @NotBlank(message = "收款账号不允许为空")
    private String payeeAccNo;
    @NotBlank(message = "收款账号户名不允许为空")
    private String payeeAccName;
    private String payeeAccType;
//    private String payeeCertType;
//    private String payeeCertNo;
//    private String payeePhone;
    private String payeeBankCode;
    private String payeeBankName;
    private String remark;

    private static final long serialVersionUID = 1L;
}
