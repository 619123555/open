package com.open.boss.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.open.boss.entity.PaymentOrder;
import com.open.boss.mapper.PaymentOrderMapper;
import com.open.boss.service.PaymentOrderService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class PaymentOrderServiceImpl extends BaseServiceImpl<PaymentOrder> implements
    PaymentOrderService {

  @Resource
  PaymentOrderMapper paymentOrderMapper;

  @Override
  public PageInfo<PaymentOrder> selectPaymentOrderList(PaymentOrder paymentOrder, Integer pageNo, Integer pageSize) {
    PageHelper.startPage(pageNo, pageSize, "create_time desc");
    Example example = new Example(PaymentOrder.class);
    Criteria criteria = example.createCriteria();
    if(!StringUtils.isEmpty(paymentOrder.getTradeNo())){
      criteria.andEqualTo("tradeNo", paymentOrder.getTradeNo());
    }
    if(!StringUtils.isEmpty(paymentOrder.getStatus())){
      criteria.andEqualTo("status", paymentOrder.getStatus());
    }
    if(!StringUtils.isEmpty(paymentOrder.getChannelId())){
      criteria.andEqualTo("channelId", paymentOrder.getChannelId());
    }
    if(!StringUtils.isEmpty(paymentOrder.getStartTime())){
      example.and().andGreaterThanOrEqualTo("completeTime", paymentOrder.getStartTime());
    }
    if(!StringUtils.isEmpty(paymentOrder.getEndTime())) {
      example.and().andLessThanOrEqualTo("completeTime", paymentOrder.getEndTime());
    }

    List<PaymentOrder> paymentOrderList = paymentOrderMapper.selectByExample(example);
    return new PageInfo<>(paymentOrderList);
  }
}
