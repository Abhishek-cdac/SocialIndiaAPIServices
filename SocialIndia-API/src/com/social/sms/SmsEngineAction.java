package com.social.sms;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmssmppConfpojo;
import com.social.vo.AlertVo;

public class SmsEngineAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	AlertVo alert = new AlertVo();
	List<AlertVo> alertList = new ArrayList<AlertVo>();
	SmssmppConfpojo smsconfObj = new SmssmppConfpojo();
	SmsEngineServices smseng = new SmsEngineDaoServices();

	/**
	 * Sms engine execute action.
	 */
	public String execute() {
		boolean rslt = false;
		try {
			System.out.println("smsconfig execute method");
			rslt = smseng.smsConfigUpdate(smsconfObj);			
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

	public SmssmppConfpojo getSmsconfObj() {
		return smsconfObj;
	}

	public void setSmsconfObj(SmssmppConfpojo smsconfObj) {
		this.smsconfObj = smsconfObj;
	}

}
