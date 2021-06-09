package com.open.boss.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "sys_user_role")
public class UserRole implements Serializable {
    /**
     * 用户编号
     */
    @Id
    @Column(name = "user_id")
    private String userId;

    /**
     * 角色编号
     */
    @Id
    @Column(name = "role_id")
    private String roleId;

    private static final long serialVersionUID = 1L;
}
