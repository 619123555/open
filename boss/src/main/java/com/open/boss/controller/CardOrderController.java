package com.open.boss.controller;

import com.github.pagehelper.PageInfo;
import com.open.boss.commons.Result;
import com.open.boss.entity.CardOrder;
import com.open.boss.service.CardOrderService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cardOrder")
public class CardOrderController extends BaseController{

  @Resource
  CardOrderService cardOrderService;

  @RequestMapping(value = "/getCardOrderList")
  public Result getPaymentOrderList(CardOrder cardOrder,
      @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize){
    PageInfo<CardOrder> page = cardOrderService.selectCardOrderList(cardOrder, pageNo, pageSize);
    return ok(page);
  }
}
