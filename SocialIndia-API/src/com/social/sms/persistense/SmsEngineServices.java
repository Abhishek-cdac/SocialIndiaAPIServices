package com.social.sms.persistense;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;

public interface SmsEngineServices {

  public SmssmppConfpojo smssmppconfiguration();

  public List<SmsInpojo> smsfunctionList(int fetchrows);
  
  public List<SmsInpojo> smsfunctionList(int fetchrows, Session pHbsession);

  public String insertsmsout(SmsOutpojo smsout);

  public String deletesmsout(SmsOutpojo smsout);

  public String insertsmserror(SmsErrorpojo smserror);

  public String deletesmserror(SmsErrorpojo smserror);

  public void reportFlgUpdate(String qry);

  public void commonUpdate(String sql);

  public SmssmppConfpojo smsConfigTableData();

  public boolean smsConfigUpdate(SmssmppConfpojo smsconfObj);

  public boolean insertSmsInTable(SmsInpojo smsinObj);

  public SmsTemplatepojo smsTemplateData(String qry);

  public HashMap<String, String> smsTemplateMap();

  public String smsTemplateParserChange(String qry, int size, String content);

}
