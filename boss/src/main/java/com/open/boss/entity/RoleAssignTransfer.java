package com.open.boss.entity;

import lombok.Data;

@Data
public class RoleAssignTransfer {

    private String key;
    private String title;
    private String description;
    private boolean chosen;
}
