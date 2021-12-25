package com.social.email.persistense;


import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;

public interface EmailEngineServices {

  public EmailConfigTblVo emailConfigval();

  public List<EmailInTblVo> emailintblval(int fetchvalue);
  
  public List<EmailInTblVo> emailintblval(int fetchvalue, Session phbsession);

  public String insertemailOuttblval(EmailOutTblVo emailout);

  public String deletemailintblval(int sno);

  public String insertemailErrortblval(EmailErrorTblVo emailerror);

  public void reportFlgUpdate(String qry);

  public String insertemailIntblval(EmailInTblVo emailin);

  public boolean emailConfigUpdate(EmailConfigTblVo emailconfObj);

  public EmailTemplateTblVo emailTemplateData(String emqry);

  public HashMap<String, String> emailTemplatemap();

  public String templateParserChange(String qry, int size, String content);

}
