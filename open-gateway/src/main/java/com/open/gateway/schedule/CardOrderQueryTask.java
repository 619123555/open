package com.open.gateway.schedule;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.ResponseData;
import com.open.common.enums.ResultCode;
import com.open.common.enums.TradeStatusEnum;
import com.open.gateway.channel.card.CardChannelService;
import com.open.gateway.entity.CardOrder;
import com.open.gateway.mapper.CardOrderMapper;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

@Component
@Configuration
@Slf4j
public class CardOrderQueryTask {

  @Autowired
  CardOrderMapper cardOrderMapper;
  @Autowired
  Map<String, CardChannelService> cardChannelServiceMap;

  @Scheduled(cron = "0 * * * * ?")
  public void executor(){
    log.info("开始查询处理中状态的卡交易订单.");
    Example example = new Example(CardOrder.class);
    example.createCriteria()
        .andEqualTo("status", TradeStatusEnum.DOING)
        .andGreaterThanOrEqualTo("createTime", DateUtil.beginOfDay(new Date()));
    List<CardOrder> list = cardOrderMapper.selectByExample(example);
    for(CardOrder cardOrder : list){
      ResponseData result = cardChannelServiceMap.get(cardOrder.getChannelId()).query(cardOrder.getTradeNo());
      if(!ResultCode.SUCCESS.equals(result.getCode())){
        continue;
      }

      JSONObject resultData = (JSONObject) JSONObject.toJSON(result.getData());
      cardOrder.setStatus(resultData.getString("orderStatus"));
      cardOrder.setChannelTradeNo(resultData.getString("channelTradeNo"));
      cardOrder.setRealAmount(StringUtils.isEmpty(resultData.getString("card_real_amt")) ? null : new BigDecimal(resultData.getString("card_real_amt")));
      cardOrder.setSettleAmount(StringUtils.isEmpty(resultData.getString("card_settle_amt")) ? null :new BigDecimal(resultData.getString("card_settle_amt")));
      cardOrderMapper.updateByPrimaryKey(cardOrder);
    }
    log.info("查询处理中状态的卡交易订单完成.");
  }
}
