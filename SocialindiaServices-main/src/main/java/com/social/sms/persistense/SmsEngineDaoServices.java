package com.social.sms.persistense;


import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;

public class SmsEngineDaoServices implements SmsEngineServices {

  SmsEngine dao = new SmsEngineDao();

  @Override
  public SmssmppConfpojo smssmppconfiguration() {
    // TODO Auto-generated method stub
    return dao.smssmppconfiguration();
  }

  @Override
  public List<SmsInpojo> smsfunctionList(int fetchrows) {
    // TODO Auto-generated method stub
    return dao.smsfunctionList(fetchrows);
  }
  
  @Override
  public List<SmsInpojo> smsfunctionList(int fetchrows, Session pHbsession) {
    // TODO Auto-generated method stub
    return dao.smsfunctionList(fetchrows, pHbsession);
  }

  @Override
  public String insertsmsout(SmsOutpojo smsout) {
    // TODO Auto-generated method stub
    return dao.insertsmsout(smsout);
  }

  @Override
  public String deletesmsout(SmsOutpojo smsout) {
    // TODO Auto-generated method stub
    return dao.deletesmsout(smsout);
  }

  @Override
  public String insertsmserror(SmsErrorpojo smserror) {
    // TODO Auto-generated method stub
    return dao.insertsmserror(smserror);
  }

  @Override
  public String deletesmserror(SmsErrorpojo smserror) {
    // TODO Auto-generated method stub
    return dao.deletesmserror(smserror);
  }

  @Override
  public void reportFlgUpdate(String qry) {
    // TODO Auto-generated method stub
    dao.reportFlgUpdate(qry);
  }

  @Override
  public void commonUpdate(String sql) {
    // TODO Auto-generated method stub
    dao.commonUpdate(sql);
  }

  @Override
  public SmssmppConfpojo smsConfigTableData() {
    // TODO Auto-generated method stub
    return dao.smsConfigTableData();
  }

  @Override
  public boolean smsConfigUpdate(SmssmppConfpojo smsconfObj) {
    // TODO Auto-generated method stub
    return dao.smsConfigUpdate(smsconfObj);
  }

  @Override
  public boolean insertSmsInTable(SmsInpojo smsinObj) {
    // TODO Auto-generated method stub
    return dao.insertSmsInTable(smsinObj);
  }

  @Override
  public SmsTemplatepojo smsTemplateData(String qry) {
    // TODO Auto-generated method stub
    return dao.smsTemplateData(qry);
  }

  @Override
  public HashMap<String, String> smsTemplateMap() {
    // TODO Auto-generated method stub
    return dao.smsTemplateMap();
  }

  @Override
  public String smsTemplateParserChange(String qry, int size, String content) {
    // TODO Auto-generated method stub
    return dao.smsTemplateParserChange(qry, size, content);
  }
}
