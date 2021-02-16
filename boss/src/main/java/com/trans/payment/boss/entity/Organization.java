package com.trans.payment.boss.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Table(name = "sys_organization")
public class Organization implements Serializable {
    /**
     * 编号
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 父级编号
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 所有父级编号
     */
    @Column(name = "parent_ids")
    private String parentIds;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Long sort;

    /**
     * 机构编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 机构类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 机构等级
     */
    @Column(name = "grade")
    private String grade;

    /**
     * 联系地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 邮政编码
     */
    @Column(name = "zip_code")
    private String zipCode;

    /**
     * 负责人
     */
    @Column(name = "master")
    private String master;

    /**
     * 电话
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 传真
     */
    @Column(name = "fax")
    private String fax;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 主负责人
     */
    @Column(name = "primary_person")
    private String primaryPerson;

    /**
     * 副负责人
     */
    @Column(name = "deputy_person")
    private String deputyPerson;

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
     * 删除标记
     */
    @Column(name = "status")
    private String status;

    /**
     * OA系统部门ID
     */
    @Column(name = "oa_id")
    private String oaId;

    /**
     * 管理渠道
     */
    @Column(name = "manage_channel")
    private String manageChannel;

    /**
     * 编码缩写
     */
    @Column(name = "simple_code")
    private String simpleCode;

    @Transient
    protected String parentCodes;

    @Transient
    private int maxId;

    private List<String> childDeptList;

    private String[] ids;

    private Organization parent;

    private static final long serialVersionUID = 1L;
}