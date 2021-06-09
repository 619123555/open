package com.open.gateway.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "t_gateway_rsa")
public class GatewayRsa implements Serializable {
    @Id
    @Column(name = "organization_id")
    private String organizationId;

    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;

    /**
     * 平台公钥
     */
    @Column(name = "public_key")
    private String publicKey;

    /**
     * 平台私钥
     */
    @Column(name = "private_key")
    private String privateKey;

    /**
     * 下游公钥
     */
    @Column(name = "channel_public_key")
    private String channelPublicKey;

    /**
     * 0 启用  1 停用
     */
    @Column(name = "rsa_status")
    private Integer rsaStatus;

    /**
     * 系统id
     */
    @Column(name = "system_id")
    private String systemId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "update_user")
    private String updateUser;

    private static final long serialVersionUID = 1L;
}
