package com.social.email.persistense;


import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;

public class EmailEngineDaoServices implements EmailEngineServices {
  EmailEngine dao = new EmailEngineDao();

  @Override
  public EmailConfigTblVo emailConfigval() {
    // TODO Auto-generated method stub
    return dao.emailConfigval();
  }

  @Override
  public List<EmailInTblVo> emailintblval(int fetchvalue) {
    // TODO Auto-generated method stub
    return dao.emailintblval(fetchvalue);
  }
  
  @Override
  public List<EmailInTblVo> emailintblval(int fetchvalue, Session prmHbsession) {
    // TODO Auto-generated method stub
    return dao.emailintblval(fetchvalue, prmHbsession);
  }

  @Override
  public String insertemailOuttblval(EmailOutTblVo emailout) {
    // TODO Auto-generated method stub
    return dao.insertemailOuttblval(emailout);
  }

  @Override
  public String deletemailintblval(int sno) {
    // TODO Auto-generated method stub
    return dao.deletemailintblval(sno);
  }

  @Override
  public String insertemailErrortblval(EmailErrorTblVo emailerror) {
    // TODO Auto-generated method stub
    return dao.insertemailErrortblval(emailerror);
  }

  @Override
  public void reportFlgUpdate(String qry) {
    // TODO Auto-generated method stub
    dao.reportFlgUpdate(qry);
  }

  @Override
  public String insertemailIntblval(EmailInTblVo emailin) {
    // TODO Auto-generated method stub
    return dao.insertemailIntblval(emailin);
  }

  @Override
  public boolean emailConfigUpdate(EmailConfigTblVo emailconfObj) {
    // TODO Auto-generated method stub
    return dao.emailConfigUpdate(emailconfObj);
  }

  @Override
  public EmailTemplateTblVo emailTemplateData(String emqry) {
    // TODO Auto-generated method stub
    return dao.emailTemplateData(emqry);
  }

  @Override
  public HashMap<String, String> emailTemplatemap() {
    // TODO Auto-generated method stub
    return dao.emailTemplatemap();
  }

  @Override
  public String templateParserChange(String qry, int size, String content) {
    // TODO Auto-generated method stub
    return dao.templateParserChange(qry, size, content);
  }

}
