package com.open.boss.service;

import com.github.pagehelper.PageInfo;
import com.open.boss.entity.CardOrder;

public interface CardOrderService extends BaseService<CardOrder> {
  PageInfo<CardOrder> selectCardOrderList(CardOrder cardOrder, Integer pageNo, Integer pageSize);
}
