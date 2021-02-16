package com.trans.payment.boss.entity;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement.Organization;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Table(name = "sys_role")
public class Role implements Serializable {
    /**
     * 编号
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 归属机构
     */
    @Column(name = "office_id")
    private String officeId;

    /**
     * 角色名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 角色类型
     */
    @Column(name = "role_type")
    private String roleType;

    /**
     * 数据范围
     */
    @Column(name = "data_scope")
    private String dataScope;

    /**
     * 是否系统数据
     */
    @Column(name = "is_sys")
    private String isSys;

    /**
     * 创建者
     */
    @Column(name = "creator")
    private String creator;

    /**
     * 创建时间
     */
    @Column(name = "create_dt")
    private Date createDt;

    /**
     * 更新者
     */
    @Column(name = "operator")
    private String operator;

    /**
     * 更新时间
     */
    @Column(name = "oper_dt")
    private Date operDt;

    @Column(name = "remarks")
    private String remarks;

    /**
     * 删除标记
     */
    @Column(name = "status")
    private String status;

    /**
     * 系统标示
     */
    @Column(name = "system_sign")
    private String systemSign;

    @Transient
    private String sysData;

    private List<Menu> menuList = new ArrayList<>();
    /**
     * 按明细设置数据范围
     */
    private List<Organization> officeList = new ArrayList<>();

    private static final long serialVersionUID = 1L;

    @Transient
    public String getMenuIds() {
        return StringUtils.join(getMenuIdList(), ",");
    }

    public void setMenuIds(String menuIds) {
        menuList = new ArrayList<>();
        if (menuIds != null) {
            String[] ids = StringUtils.split(menuIds, ",");
            setMenuIdList(Lists.newArrayList(ids));
        }
    }

    public List<String> getMenuIdList() {
        List<String> menuIdList = new ArrayList<>();
        for (Menu menu : menuList) {
            menuIdList.add(menu.getId());
        }
        return menuIdList;
    }

    public void setMenuIdList(List<String> menuIdList) {
        menuList = new ArrayList<>();
        for (String menuId : menuIdList) {
            Menu menu = new Menu();
            menu.setId(menuId);
            menuList.add(menu);
        }
    }
}