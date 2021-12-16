package com.social.sms.persistense;


import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;

public interface SmsEngine {

  SmssmppConfpojo smssmppconfiguration();

  List<SmsInpojo> smsfunctionList(int fetchrows);
  
  List<SmsInpojo> smsfunctionList(int fetchrows, Session pHbsession);

  String insertsmsout(SmsOutpojo smsout);

  String deletesmsout(SmsOutpojo smsout);

  String insertsmserror(SmsErrorpojo smserror);

  String deletesmserror(SmsErrorpojo smserror);

  public void reportFlgUpdate(String qry);

  public void commonUpdate(String sql);

  SmssmppConfpojo smsConfigTableData();

  public boolean smsConfigUpdate(SmssmppConfpojo smsconfObj);

  public boolean insertSmsInTable(SmsInpojo smsinObj);

  public SmsTemplatepojo smsTemplateData(String qry);

  public HashMap<String, String> smsTemplateMap();

  public String smsTemplateParserChange(String qry, int size, String content);

}
