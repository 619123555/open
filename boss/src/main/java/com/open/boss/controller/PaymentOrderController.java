package com.open.boss.controller;

import com.github.pagehelper.PageInfo;
import com.open.boss.commons.Result;
import com.open.boss.entity.PaymentOrder;
import com.open.boss.service.PaymentOrderService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/paymentOrder")
public class PaymentOrderController extends BaseController{

  @Resource
  PaymentOrderService paymentOrderService;

  @RequestMapping(value = "/getPaymentOrderList")
  public Result getPaymentOrderList(PaymentOrder paymentOrder,
      @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize){
    PageInfo<PaymentOrder> page = paymentOrderService.selectEntityPage(paymentOrder, pageNo, pageSize, "id DESC");
    return ok(page);
  }
}
