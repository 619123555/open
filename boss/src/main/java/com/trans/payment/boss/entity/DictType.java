package com.trans.payment.boss.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Table(name = "sys_dict_type")
public class DictType implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "label")
    private String label;

    @Column(name = "value")
    private String value;

    @Column(name = "description")
    private String description;

    @Column(name = "creator")
    private String creator;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "operator")
    private String operator;

    @Column(name = "oper_time")
    private Date operTime;

    @Column(name = "status")
    private String status;

    @Column(name = "scope")
    private String scope;

    /**
     * 版本
     */
    @Column(name = "version")
    private Integer version;

    @Transient
    private String key;

    private static final long serialVersionUID = 1L;
}