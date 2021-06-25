package com.open.boss.service;

import com.github.pagehelper.PageInfo;
import com.open.boss.entity.PaymentOrder;

public interface PaymentOrderService extends BaseService<PaymentOrder> {
  PageInfo<PaymentOrder> selectPaymentOrderList(PaymentOrder paymentOrder, Integer pageNo, Integer pageSize);
}
