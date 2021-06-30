package com.open.gateway.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import tk.mybatis.mapper.annotation.Version;

@Data
@Table(name = "t_card_order")
public class CardOrder implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "version")
    @Version
    private Integer version;

    @Column(name = "organization_id")
    private String organizationId;

    @Column(name = "pay_type")
    private String payType;

    @Column(name = "trade_no")
    private String tradeNo;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "merc_fee")
    private BigDecimal mercFee;

    @Column(name = "channel_cost")
    private BigDecimal channelCost;

    @Column(name = "real_amount")
    private BigDecimal realAmount;

    @Column(name = "settle_amount")
    private BigDecimal settleAmount;

    @Column(name = "status")
    private String status;

    /**
     * 卡类型
     */
    @Column(name = "card_type")
    private Integer cardType;

    /**
     * 卡数据
     */
    @Column(name = "card_data")
    private String cardData;

    @Column(name = "ip")
    private String ip;

    /**
     * 下游通知url
     */
    @Column(name = "notify_url")
    private String notifyUrl;

    @Column(name = "remark")
    private String remark;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "complete_time")
    private Date completeTime;

    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "channel_trade_no")
    private String channelTradeNo;

    private static final long serialVersionUID = 1L;
}
