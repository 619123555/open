package com.open.boss.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "sys_dict")
public class Dict implements Serializable {
    /**
     * 编号
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 数据值
     */
    @Column(name = "value")
    private String value;

    /**
     * 标签名
     */
    @Column(name = "label")
    private String label;

    /**
     * 类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 排序（升序）
     */
    @Column(name = "sort")
    private Long sort;

    /**
     * 父级编号
     */
    @Column(name = "parent")
    private String parent;

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


    private DictType dictType;

    private static final long serialVersionUID = 1L;
}
