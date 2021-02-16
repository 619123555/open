package com.trans.payment.boss.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Table(name = "sys_menu")
public class Menu implements Serializable {

    public Menu() {
    }

    public Menu(String id) {
        this.id = id;
    }
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
     * 链接
     */
    @Column(name = "href")
    private String href;

    @Column(name = "to_url")
    private String toUrl;

    /**
     * 目标
     */
    @Column(name = "target")
    private String target;

    /**
     * 图标
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 是否在菜单中显示
     */
    @Column(name = "is_show")
    private String isShow;

    /**
     * 权限标识
     */
    @Column(name = "permission")
    private String permission;

    /**
     * 系统
     */
    @Column(name = "system_sign")
    private String systemSign;

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
     * 备注信息
     */
    @Column(name = "remarks")
    private String remarks;

    /**
     * 状态
     */
    @Column(name = "status")
    private String status;

    private Menu parents;

    @Transient
    private String userId;

    private Menu parent;

    @Transient
    public String getPIds() {
        return parent != null && parent.getId() != null ? parent.getId() : "0";
    }

    @JsonIgnore
    public static String getRootId() {
        return "1";
    }

    private static final long serialVersionUID = 1L;

    /**
     * 是否有子菜单
     */
    @Transient
    @JSONField(serialize = false)
    private Boolean hasChild = false;

    public static void sortList(
            List<Menu> list, List<Menu> sourcelist, String parentId, boolean cascade) {
        for (int i = 0; i < sourcelist.size(); i++) {
            Menu e = sourcelist.get(i);
            if (e.getParent() != null
                    && e.getParent().getId() != null
                    && e.getParent().getId().equals(parentId)) {
                list.add(e);
                if (cascade) {
                    // 判断是否还有子节点, 有则继续获取子节点
                    for (int j = 0; j < sourcelist.size(); j++) {
                        Menu child = sourcelist.get(j);
                        if (child.getParent() != null
                                && child.getParent().getId() != null
                                && child.getParent().getId().equals(e.getId())) {
                            e.hasChild(true);
                            sortList(list, sourcelist, e.getId(), true);
                            break;
                        }
                    }
                }
            }
        }
    }

    public Boolean hasChild() {
        return hasChild;
    }

    public void hasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }
}