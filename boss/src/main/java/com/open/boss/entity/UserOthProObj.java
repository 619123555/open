package com.open.boss.entity;


public class UserOthProObj {

    /**
     * 最大用户ID值
     */
    private int maxId;
    /**
     * 最大编号
     */
    private int maxNo;
    /**
     * 归属公司
     */
    private String companyID;
    private String userNOStr;

    public int getMaxId() {
        return maxId;
    }

    public void setMaxId(int maxId) {
        this.maxId = maxId;
    }

    public int getMaxNo() {
        return maxNo;
    }

    public void setMaxNo(int maxNo) {
        this.maxNo = maxNo;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getUserNOStr() {
        return userNOStr;
    }

    public void setUserNOStr(String userNOStr) {
        this.userNOStr = userNOStr;
    }
}
