package com.open.common.dto.gateway;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CardTopUpRsp implements Serializable {
  private Integer subCode;
  private String subMsg;
  private String orderId;
  private String tradeNo;
  private String status;
  private Integer amount;
  private BigDecimal realAmount;
  private BigDecimal settleAmount;
  private Integer cardType;
  private String cardData;
}
