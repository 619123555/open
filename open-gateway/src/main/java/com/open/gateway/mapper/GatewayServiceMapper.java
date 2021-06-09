package com.open.gateway.mapper;

import com.open.gateway.entity.GatewayService;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface GatewayServiceMapper extends Mapper<GatewayService> {
  /**
   * 根据organizationId + 服务名+版本查询可用的服务
   * @param organizationId 机构编号
   * @param service 服务名
   * @param version 版本
   * @return GatewayService
   */
  GatewayService selectGatewayService(@Param("organizationId") String organizationId,
      @Param("service") String service,
      @Param("version") String version);
}
