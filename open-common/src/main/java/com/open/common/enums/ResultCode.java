package com.open.common.enums;


public enum ResultCode {
  /** 成功 */
  SUCCESS(10000, "成功"),
  /** 失败 */
  FAIL(20000, "失败"),
  DOING(30000, "处理中"),
  /** 拒绝访问 */
  UNAUTHORIZED(403, "拒绝访问"),
  /** 接口不存在 */
  NOT_FOUND(404, "路径不存在"),
  NO_TOKEN(401, "认证失败，缺少头部token值"),
  /** 服务器内部错误 */
  SERVER_ERROR(500, "系统错误");

  private int code;
  private String msg;

  ResultCode(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
