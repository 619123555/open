package com.open.boss.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Table(name = "sys_user")
public class User implements Serializable {

  private Role role;

  public User(Role role) {
    this.role = role;
  }

  public User() {
  }

  public User(String id, String no) {
    this.id = id;
    this.no = no;
  }

  /**
   * 用户ID字符串
   */
  @Transient
  private String userIds;

  private List<Role> roleList = new ArrayList<>();

  public boolean isAdmin() {
    return isAdmin(this.id);
  }

  public static boolean isAdmin(String id) {
    return id != null && "1".equals(id);
  }
  /** 编号 */
  @Id
  @Column(name = "id")
  private String id;

  /** 归属公司 */
  @Column(name = "company_id")
  private String companyId;

  /** 归属部门 */
  @Column(name = "office_id")
  private String officeId;

  /** 密码 */
  @Column(name = "password")
  private String password;

  /** 工号 */
  @Column(name = "no")
  private String no;

  /** 姓名 */
  @Column(name = "name")
  private String name;

  @Column(name = "gender")
  private String gender;

  /** 邮箱 */
  @Column(name = "email")
  private String email;

  /** 电话 */
  @Column(name = "phone")
  private String phone;

  /** 用户类型 */
  @Column(name = "user_type")
  private String userType;

  /** 用户头像 */
  @Column(name = "photo")
  private String photo;

  @Column(name = "mobile")
  private String mobile;

  /** 最后登陆IP */
  @Column(name = "login_ip")
  private String loginIp;

  /** 最后登陆时间 */
  @Column(name = "login_date")
  private Date loginDate;

  /** 是否可登录 */
  @Column(name = "login_flag")
  private String loginFlag;

  /** 管理渠道 */
  @Column(name = "manage_channel")
  private String manageChannel;

  /** 创建者 */
  @Column(name = "creator")
  private String creator;

  /** 创建时间 */
  @Column(name = "create_time")
  private Date createTime;

  /** 更新者 */
  @Column(name = "operator")
  private String operator;

  /** 更新时间 */
  @Column(name = "oper_time")
  private Date operTime;

  /** 状态 */
  @Column(name = "status")
  private String status;

  /** OA员工编码 */
  @Column(name = "oa_code")
  private String oaCode;

  /** 手机 */
  @Column(name = "secret")
  private String secret;

  /** 归属公司 */
  private Organization company;
  /** 归属部门 */
  private Organization office;

  private static final long serialVersionUID = 1L;
}
