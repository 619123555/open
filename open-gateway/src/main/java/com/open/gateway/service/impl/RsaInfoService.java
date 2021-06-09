package com.open.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.open.common.constants.CommonEnum;
import com.open.common.dto.gateway.ApiReq;
import com.open.common.dto.gateway.RsaSecurityReq;
import com.open.common.enums.OperationEnum;
import com.open.common.exception.GatewayException;
import com.open.common.service.RedisCacheDaoService;
import com.open.gateway.entity.GatewayRsa;
import com.open.gateway.mapper.GatewayRsaMapper;
import com.open.gateway.util.WebConstant;
import java.util.Date;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RsaInfoService {

    private static final Logger logger = LoggerFactory.getLogger(RsaInfoService.class);

    @Resource
    private GatewayRsaMapper gatewayRsaMapper;

    @Resource
    private RedisCacheDaoService redisCacheDaoService;

    public JSONObject execute(ApiReq api) throws GatewayException {
        String data = api.getData();
        String cacheKey = String.format("gateway:rsa:security:%s", api.getAppId());
        logger.info("秘钥缓存:{}", cacheKey);
        RsaSecurityReq rsa = JSONObject.parseObject(data, RsaSecurityReq.class);
        GatewayRsa gatewayRsa = this.selectGatewayRsa(api.getAppId());
        if (gatewayRsa == null || rsa == null) {
            throw new GatewayException(CommonEnum.ISV_AUTH_RSA_TIME_OUT.getMsg());
        }
        JSONObject rsp = new JSONObject();
        rsp.put(WebConstant.SERVICE_SUB_CODE, CommonEnum.SUCCESS.getCode());
        rsp.put(WebConstant.SERVICE_SUB_MSG, CommonEnum.SUCCESS.getMsg());
        if (OperationEnum.query.name().equals(rsa.getOperatMode())) {
            rsp.put("publicKey", gatewayRsa.getPublicKey());
            return rsp;
        } else if (OperationEnum.reset.name().equals(rsa.getOperatMode())) {
            gatewayRsa.setChannelPublicKey(rsa.getPublicKey());
            gatewayRsa.setUpdateTime(new Date());
            int count = this.gatewayRsaMapper.updateByPrimaryKeySelective(gatewayRsa);
            if (count == 1) {
                try {
                    this.redisCacheDaoService.delete(cacheKey, GatewayRsa.class);
                    logger.error("delete {} redis cache success", cacheKey);
                } catch (Exception e) {
                    logger.error("delete redis cache fail");
                    logger.error("Exception:", e);
                }
                return rsp;
            }
        }
        throw new GatewayException(CommonEnum.ISV_RSA_SECURITY_ERROR.getMsg());
    }

    public GatewayRsa selectGatewayRsa(String agentNo) {
        String cacheKey = String.format("gateway:rsa:security:%s", agentNo);
        GatewayRsa gatewayRsa = this.redisCacheDaoService.read(cacheKey, GatewayRsa.class);
        if (gatewayRsa == null) {
            gatewayRsa = this.gatewayRsaMapper.selectByPrimaryKey(agentNo);
            if (gatewayRsa != null) {
                this.redisCacheDaoService.save(cacheKey, gatewayRsa);
            }
        }
        return gatewayRsa;
    }
}
