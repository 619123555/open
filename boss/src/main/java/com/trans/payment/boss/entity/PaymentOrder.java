package com.trans.payment.boss.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "t_payment_order")
public class PaymentOrder implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "version")
    private Integer version;

    @Column(name = "pay_type")
    private String pay_type;

    @Column(name = "trade_no")
    private String trade_no;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "status")
    private String status;

    @Column(name = "create_time")
    private Date create_time;

    @Column(name = "complete_time")
    private Date complete_time;

    @Column(name = "currency")
    private String currency;

    @Column(name = "payer_acc_no")
    private String payer_acc_no;

    @Column(name = "payer_acc_name")
    private String payer_acc_name;

    @Column(name = "payee_acc_no")
    private String payee_acc_no;

    @Column(name = "payee_acc_name")
    private String payee_acc_name;

    @Column(name = "payee_acc_type")
    private String payee_acc_type;

    @Column(name = "payee_cert_type")
    private String payee_cert_type;

    @Column(name = "payee_cert_no")
    private String payee_cert_no;

    @Column(name = "payee_phone")
    private String payee_phone;

    @Column(name = "payee_bank_code")
    private String payee_bank_code;

    @Column(name = "payee_bank_name")
    private String payee_bank_name;

    @Column(name = "remark")
    private String remark;

    @Column(name = "channel_id")
    private String channel_id;

    @Column(name = "channel_trade_no")
    private String channel_trade_no;

    private static final long serialVersionUID = 1L;
}