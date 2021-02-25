package com.trans.payment.boss.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Table(name = "t_payment_order")
public class PaymentOrder implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "version")
    private Integer version;

    @Column(name = "pay_type")
    private String payType;

    @Column(name = "trade_no")
    private String tradeNo;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "status")
    private String status;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "complete_time")
    private Date completeTime;

    @Column(name = "currency")
    private String currency;

    @Column(name = "payer_acc_no")
    private String payerAccNo;

    @Column(name = "payer_acc_name")
    private String payerAccName;

    @Column(name = "payee_acc_no")
    private String payeeAccNo;

    @Column(name = "payee_acc_name")
    private String payeeAccName;

    @Column(name = "payee_acc_type")
    private String payeeAccType;

    @Column(name = "payee_cert_type")
    private String payeeCertType;

    @Column(name = "payee_cert_no")
    private String payeeCertNo;

    @Column(name = "payee_phone")
    private String payeePhone;

    @Column(name = "payee_bank_code")
    private String payeeBankCode;

    @Column(name = "payee_bank_name")
    private String payeeBankName;

    @Column(name = "remark")
    private String remark;

    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "channel_trade_no")
    private String channelTradeNo;

    private static final long serialVersionUID = 1L;
    @Transient
    private String startTime;

    @Transient
    private String endTime;
}