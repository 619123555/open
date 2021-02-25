package com.trans.payment.boss.service.impl;

import com.trans.payment.boss.entity.PaymentOrder;
import com.trans.payment.boss.mapper.PaymentOrderMapper;
import com.trans.payment.boss.service.PaymentOrderService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PaymentOrderServiceImpl extends BaseServiceImpl<PaymentOrder> implements
    PaymentOrderService {

  @Resource
  PaymentOrderMapper paymentOrderMapper;

}
