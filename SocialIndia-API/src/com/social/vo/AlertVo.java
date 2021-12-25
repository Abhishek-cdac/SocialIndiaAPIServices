package com.social.vo;

import java.io.Serializable;

public class AlertVo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String cls;
  private String msg;

  public String getCls() {
    return cls;
  }

  public void setCls(String cls) {
    this.cls = cls;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
