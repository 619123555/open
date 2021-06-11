package com.open.common.dto.gateway;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CardTopUpResp implements Serializable {
  private String orderId;
  private String tradeNo;
  private String status;
  private BigDecimal amount;
  private BigDecimal realAmount;
  private BigDecimal settleAmount;
  private Integer cardType;
  private String cardData;
}
