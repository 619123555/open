package com.open.common.dto.gateway;

import com.open.common.utils.validator.group.AddGroup;
import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class CardOrderQueryReq implements Serializable {
  @NotBlank(message = "订单号不允许为空", groups = {AddGroup.class})
  private String tradeNo;
}
