package com.open.boss.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.open.boss.entity.CardOrder;
import com.open.boss.mapper.CardOrderMapper;
import com.open.boss.service.CardOrderService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class CardOrderServiceImpl extends BaseServiceImpl<CardOrder> implements CardOrderService {

  @Resource
  CardOrderMapper cardOrderMapper;

  @Override
  public PageInfo<CardOrder> selectCardOrderList(CardOrder cardOrder, Integer pageNo,
      Integer pageSize) {
    PageHelper.startPage(pageNo, pageSize, "create_time desc");
    Example example = new Example(CardOrder.class);
    Criteria criteria = example.createCriteria();

    if(!StringUtils.isEmpty(cardOrder.getTradeNo())){
      criteria.andEqualTo("tradeNo", cardOrder.getTradeNo());
    }
    if(!StringUtils.isEmpty(cardOrder.getStatus())){
      criteria.andEqualTo("status", cardOrder.getStatus());
    }
    if(!StringUtils.isEmpty(cardOrder.getChannelId())){
      criteria.andEqualTo("channelId", cardOrder.getChannelId());
    }
    if(!StringUtils.isEmpty(cardOrder.getStartTime())){
      example.and().andGreaterThanOrEqualTo("completeTime", cardOrder.getStartTime());
    }
    if(!StringUtils.isEmpty(cardOrder.getEndTime())) {
      example.and().andLessThanOrEqualTo("completeTime", cardOrder.getEndTime());
    }

    List<CardOrder> cardOrderList = cardOrderMapper.selectByExample(example);
    return new PageInfo<>(cardOrderList);
  }
}
