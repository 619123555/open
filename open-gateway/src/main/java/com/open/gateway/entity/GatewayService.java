package com.open.gateway.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Table(name = "t_gateway_service")
public class GatewayService implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 方法名
     */
    @Column(name = "service")
    private String service;

    /**
     * 方法类型
     */
    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "service_name")
    private String serviceName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_user")
    private String updateUser;

    /**
     * 版本
     */
    @Column(name = "version")
    private Integer version;

    /**
     * 0 可用 1 不可用
     */
    @Column(name = "use_status")
    private Integer useStatus;

    /**
     * 每分钟流量最大值 0 无上限  其他正整数位限流值
     */
    @Column(name = "limiting")
    private Integer limiting;

    /**
     * 方法版本
     */
    @Column(name = "service_version")
    private String serviceVersion;

    /**
     * 扩展属性(机构限流字段)
     */
    @Transient
    private Integer limits;

    private static final long serialVersionUID = 1L;
}
