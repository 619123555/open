package com.open.boss.entity;


import java.util.List;

public class OrganizationNode {

    private String orgId;     //机构ID
    private String orgName;   //机构名称
    private String orgCode;    //机构编号
    private String parentId;   //父节点ID
    private String parentIds;   //父节点字符串
    private String orgType;     //机构类型
    private String master;      //负责人
    private String grade;   //层级
    private String key;   //等级
    private String simpleCode;   //等级
    private List<OrganizationNode> children;   //子节点

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public List<OrganizationNode> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizationNode> children) {
        this.children = children;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSimpleCode() {
        return simpleCode;
    }

    public void setSimpleCode(String simpleCode) {
        this.simpleCode = simpleCode;
    }
}
