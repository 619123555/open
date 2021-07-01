package com.open.common.dto.gateway;

import java.io.Serializable;
import lombok.Data;

@Data
public class PaymentRsp implements Serializable {
  private Integer subCode;
  private String subMsg;
  private String orderId;
  private String tradeNo;
  private String status;
}
