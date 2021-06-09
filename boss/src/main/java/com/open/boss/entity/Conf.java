package com.open.boss.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "sys_config")
public class Conf implements Serializable {
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 常量配置的key值
     */
    @Column(name = "conf_key")
    private String confKey;

    /**
     * 常量配置的scope域
     */
    @Column(name = "scope")
    private String scope;

    /**
     * 创建者
     */
    @Column(name = "creator")
    private String creator;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新者
     */
    @Column(name = "operator")
    private String operator;

    /**
     * 更新时间
     */
    @Column(name = "oper_time")
    private Date operTime;

    /**
     * 状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 常量配置的value值
     */
    @Column(name = "conf_value")
    private String confValue;


    private List<String> scopes;

    private String[] ids;

    private static final long serialVersionUID = 1L;
}
