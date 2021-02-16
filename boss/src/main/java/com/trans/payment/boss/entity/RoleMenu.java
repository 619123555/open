package com.trans.payment.boss.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "sys_role_menu")
public class RoleMenu implements Serializable {
    /**
     * 角色编号
     */
    @Id
    @Column(name = "role_id")
    private String roleId;

    /**
     * 菜单编号
     */
    @Id
    @Column(name = "menu_id")
    private String menuId;

    private static final long serialVersionUID = 1L;
}