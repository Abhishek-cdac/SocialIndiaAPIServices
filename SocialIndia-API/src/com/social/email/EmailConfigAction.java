package com.social.email;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.social.common.CommonDaoService;
import com.social.common.CommonService;
import com.social.email.persistense.EmailConfigTblVo;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.sms.SmsEngineCntrl;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmssmppConfpojo;
import com.social.vo.AlertVo;

public class EmailConfigAction extends ActionSupport {

  private static final long serialVersionUID = 1L;

  EmailConfigTblVo emailconfObj = new EmailConfigTblVo();
  EmailEngineServices emaileng = new EmailEngineDaoServices();
  AlertVo alert = new AlertVo();
  List<AlertVo> alertList = new ArrayList<AlertVo>();

  SmsInpojo smsinObj = new SmsInpojo();
  SmssmppConfpojo smsconfObj = new SmssmppConfpojo();
  SmsEngineServices smseng = new SmsEngineDaoServices();
  CommonService commonHibernate = new CommonDaoService();

  List<SmssmppConfpojo> smsconfListObj = new ArrayList<SmssmppConfpojo>();

  public String actiontypeonoff;

  /**
   * Email configure action.
   */
  @Override
  public String execute() {
    boolean rslt = false;
    try {
      rslt = emaileng.emailConfigUpdate(emailconfObj);
      if (rslt) {
    	  alert.setCls("success");
    	  alert.setMsg("Successfully Updated");
    	  alertList.add(alert);
      } else {
    	  alert.setCls("error");
          alert.setMsg("Error occur while updating. Please try again");
          alertList.add(alert);
      }
    } catch (Exception ex) {
    	ex.printStackTrace();
    	alert.setCls("error");
    	alert.setMsg("Error occur while updating. Please try again");
    	alertList.add(alert);
    }

    return SUCCESS;
  }

  /**
   * Getting sms congig details and insert into in tbl.
   * 
   * @return Records inserted.
   */
  public String smsConfigPageredirect() {
    try {
      smsconfObj = smseng.smsConfigTableData();
      System.out.println("Result smsconfObj---" + smsconfObj);
      boolean insrt = false;
      Date dat = new Date();

      for (int i = 0; i < 1; i++) {
        smsinObj.setSmsMobNumber("9840959885");
        smsinObj.setSmsCardNo("234564454" + i);
        smsinObj.setSmsMessage("Hello Test Msg");
        smsinObj.setSmspollFlag("F");
        smsinObj.setSmsEntryTime(dat);
        smsinObj.setTrycount(0);
        insrt = smseng.insertSmsInTable(smsinObj);
        System.out.println("Sms insert result---" + insrt);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return SUCCESS;
  }

  /**
   * Thread start and stop action.
   * 
   * @return Engine started or stopped.
   */
  public String threadStartStopMethod() {
    try {
      System.out.println("actiontypeonoff---" + actiontypeonoff);
      if (actiontypeonoff.equalsIgnoreCase("on")) {
        System.out.println("---Engine Start-----");
        // ---------------------------------------Engine
        // start---------------------------------------------
        SmsEngineCntrl.getInstance().startThread();
        EmailEngineCntrl.getInstance().startThread();

        alert.setCls("success");
        alert.setMsg("Engine Started");
        alertList.add(alert);
      } else if (actiontypeonoff.equalsIgnoreCase("off")) {
        System.out.println("---Engine Stop-----");
        // ---------------------------------------Engine
        // Stop---------------------------------------------
        SmsEngineCntrl.getInstance().stopThread();
        EmailEngineCntrl.getInstance().stopThread();
        alert.setCls("success");
        alert.setMsg("Engine Stopped");
        alertList.add(alert);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      alert.setCls("error");
      alert.setMsg("Error occur while updating. Please try again");
      alertList.add(alert);
    }
    return SUCCESS;
  }

  /**
   * Test email configure.
   * 
   * @return success.
   */
  public String emailconfigdata() {
    emailconfObj = emaileng.emailConfigval();
    System.out.println("testtt email fun");
    return SUCCESS;
  }

  /**
   * Check common function.
   * 
   * @return Updated.
   */
  public String commonfncheck() {
    String id = "";
    String sql = "update Vopojoname set column='test' where whrcolumn =" + id
        + "";
    commonHibernate.commonUpdate(sql);

    commonHibernate.commonDelete(1, "SmssmppConfpojo", "Flg");

    return SUCCESS;
  }

  public AlertVo getAlert() {
    return alert;
  }

  public void setAlert(AlertVo alert) {
    this.alert = alert;
  }

  public List<AlertVo> getAlertList() {
    return alertList;
  }

  public void setAlertList(List<AlertVo> alertList) {
    this.alertList = alertList;
  }

  public EmailConfigTblVo getEmailconfObj() {
    return emailconfObj;
  }

  public void setEmailconfObj(EmailConfigTblVo emailconfObj) {
    this.emailconfObj = emailconfObj;
  }

  public SmssmppConfpojo getSmsconfObj() {
    return smsconfObj;
  }

  public void setSmsconfObj(SmssmppConfpojo smsconfObj) {
    this.smsconfObj = smsconfObj;
  }

  public String getActiontypeonoff() {
    return actiontypeonoff;
  }

  public void setActiontypeonoff(String actiontypeonoff) {
    this.actiontypeonoff = actiontypeonoff;
  }

}
