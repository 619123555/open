package com.open.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.gateway.ApiReq;
import com.open.common.utils.IdGen;
import com.open.gateway.entity.GatewayLog;
import com.open.gateway.mapper.GatewayLogMapper;
import com.open.gateway.util.WebConstant;
import java.util.Date;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class GatewayLogService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayLogService.class);

    @Resource
    private GatewayLogMapper GatewayLogMapper;

    @Async
    public void requestLog(ApiReq api, Long time, JSONObject rsp, String logId) {
        Long cost = System.currentTimeMillis() - time;
        GatewayLog gc = new GatewayLog(api, cost);
        gc.setId(IdGen.uuidLong());
        gc.setCreateTime(new Date());
        gc.setLogId(logId);
        if (rsp != null) {
            gc.setSubCode(rsp.getString(WebConstant.SERVICE_SUB_CODE));
            gc.setSubMsg(rsp.getString(WebConstant.SERVICE_SUB_MSG));
            logger.info("流水号:{},耗时:{}ms,响应结果:{}", api.getRequestId(), cost, rsp);
        }
        try {
            this.GatewayLogMapper.insertSelective(gc);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}
