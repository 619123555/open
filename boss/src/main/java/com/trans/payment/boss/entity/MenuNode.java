package com.trans.payment.boss.entity;

import java.util.ArrayList;
import java.util.List;

public class MenuNode {

    private String menuId;
    private String key;
    private String to;
    private String parentId;
    private String image;
    private String name;
    private String isShow;
    private String remarks;
    private String systemSign;
    private List<MenuNode> children = new ArrayList<>();

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuNode> getChildren() {
        return children;
    }

    public void setChildren(List<MenuNode> children) {
        this.children = children;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSystemSign() {
        return systemSign;
    }

    public void setSystemSign(String systemSign) {
        this.systemSign = systemSign;
    }
}
