package com.open.boss.service.impl;

import com.open.boss.entity.PaymentOrder;
import com.open.boss.mapper.PaymentOrderMapper;
import com.open.boss.service.PaymentOrderService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PaymentOrderServiceImpl extends BaseServiceImpl<PaymentOrder> implements
    PaymentOrderService {

  @Resource
  PaymentOrderMapper paymentOrderMapper;

}
