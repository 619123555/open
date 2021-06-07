package com.trans.payment.core.filter.rate;

import com.trans.payment.common.service.RedisCacheDaoService;
import com.trans.payment.core.entity.GatewayService;
import com.trans.payment.core.mapper.GatewayServiceMapper;
import com.trans.payment.core.util.RedisKeyConfigure;
import com.trans.payment.core.util.WebConstant;
import java.util.Objects;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService implements RateLimit {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitService.class);

    @Resource
    private RedisCacheDaoService redisCacheDaoService;

    @Resource
    private GatewayServiceMapper gatewayServiceMapper;

    @Override
    public void limitCheck(String cacheKey, Integer limit, Long seconds) {
        if (limit == 0) {
            return;
        }
        Integer val = this.redisCacheDaoService.read(cacheKey, Integer.class);
        if (Objects.isNull(val)) {
            this.redisCacheDaoService.save(cacheKey, 1, seconds);
        } else {
            this.limit(val, limit);
            this.redisCacheDaoService.update(cacheKey, ++val);
        }
    }

    @Override
    public GatewayService selectServiceDetail(String organizationId, String service, String version) {
        String key = RedisKeyConfigure
            .genKey(WebConstant.MODULE_NAME, organizationId, service, version);
        GatewayService gs = null;
        try {
            gs = this.redisCacheDaoService.read(key, GatewayService.class);
            if (gs == null) {
                gs = this.gatewayServiceMapper.selectGatewayService(organizationId, service, version);
                if (gs != null) {
                    this.redisCacheDaoService.save(key, gs, 3600L);
                }
            }
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        return gs;
    }
}
