package com.open.common.dto.gateway;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class CardTopUpReq implements Serializable {
  private String orderId;
  private String payType;
  @NotBlank(message = "订单号不允许为空")
  private String tradeNo;
  @NotBlank(message = "充值总金额不允许为空")
  private Integer amount;
  @NotBlank(message = "卡类型不允许为空")
  private Integer cardType;
  @NotBlank(message = "卡数据不允许为空")
  private String cardData;
  @NotBlank(message = "ip不允许为空")
  private String ip;
  private String notifyUrl;
  private String remark;
  @NotBlank(message = "提交时间不允许为空")
  private Date createTime;

  private static final long serialVersionUID = 1L;
}
