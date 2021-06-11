package com.open.common.dto.gateway;

import com.open.common.utils.validator.group.AddGroup;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class CardTopUpReq implements Serializable {
  private String orderId;
  private String payType;
  @NotBlank(message = "订单号不允许为空", groups = {AddGroup.class})
  private String tradeNo;
  @NotBlank(message = "充值总金额不允许为空", groups = {AddGroup.class})
  private BigDecimal amount;
  @NotBlank(message = "卡类型不允许为空", groups = {AddGroup.class})
  private Integer cardType;
  @NotBlank(message = "卡数据不允许为空", groups = {AddGroup.class})
  private String cardData;
  @NotBlank(message = "ip不允许为空", groups = {AddGroup.class})
  private String ip;
  private String notifyUrl;
  private String remark;
  private Date createTime;
  private String channelTradeNo;

  private static final long serialVersionUID = 1L;
}
