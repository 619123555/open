package com.open.gateway.filter.rate;


import com.open.common.constants.CommonEnum;
import com.open.common.exception.GatewayException;
import com.open.gateway.entity.GatewayService;

public interface RateLimit {

    /**
     * 流量监测
     * @param cacheKey cacheKey
     * @param limit limit
     * @param seconds seconds
     */
    void limitCheck(String cacheKey, Integer limit, Long seconds);

    /**
     * 默认的限流 limit
     * @param val 当前流量
     * @param limit 设定流量
     */
    default void limit(Integer val, Integer limit) {
        if (val > limit) {
            throw new GatewayException(CommonEnum.ISP_MORE_THAN_LIMIT.getMsg());
        }
    }

    /**
     * selectServiceDetail
     * @param organizationId organizationId
     * @param serviceName 方法名
     * @param version 版本
     * @return GatewayService
     */
    GatewayService selectServiceDetail(String organizationId, String serviceName, String version);
}
