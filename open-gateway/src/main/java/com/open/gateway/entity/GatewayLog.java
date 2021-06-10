package com.open.gateway.entity;

import com.open.common.dto.gateway.ApiReq;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "t_gateway_log")
public class GatewayLog implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 机构编号
     */
    @Column(name = "organization_id")
    private String organizationId;

    /**
     * 服务名
     */
    @Column(name = "service")
    private String service;

    /**
     * 请求流水号
     */
    @Column(name = "request_id")
    private String requestId;

    /**
     * 签名类型
     */
    @Column(name = "sign_type")
    private String signType;

    /**
     * 版本
     */
    @Column(name = "interface_version")
    private String interfaceVersion;

    /**
     * 系统id
     */
    @Column(name = "system_id")
    private String systemId;

    /**
     * 业务数据
     */
    @Column(name = "data")
    private String data;

    /**
     * 下游请求时间戳
     */
    @Column(name = "timestamp")
    private String timestamp;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 执行耗时
     */
    @Column(name = "cost_time")
    private Long costTime;

    /**
     * 响应编码
     */
    @Column(name = "sub_code")
    private String subCode;

    /**
     * 响应内容
     */
    @Column(name = "sub_msg")
    private String subMsg;

    @Column(name = "log_id")
    private String logId;

    private static final long serialVersionUID = 1L;

    public GatewayLog() {}

    public GatewayLog(ApiReq api, Long time) {
        this.organizationId = api.getOrganizationId();
        this.service = api.getService();
        this.requestId = api.getRequestId();
        this.signType = api.getSignType();
        this.interfaceVersion = api.getVersion();
        this.systemId = api.getSystemId();
        this.costTime = time;
        this.timestamp = api.getTimestamp();
    }
}
