package com.social.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.pack.utilitypkg.Commonutility;
import com.social.sms.SmsEngineCntrl;

@WebListener
public class SMSServletContextListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			System.out.println("[SMS Started]");
//			Thread.sleep(30000);
			SmsEngineCntrl.getInstance().startThread();
//			Thread.sleep(5000);
		} catch (Exception e) {
			Commonutility.toWriteConsole("[SMS Thread ERROR contextInitialized]:"+e.getMessage());
			e.printStackTrace();
		} finally {
			
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			System.out.println("[SMS Stoped]");
			SmsEngineCntrl.getInstance().stopThread();
		} catch (Exception ee) {
			Commonutility.toWriteConsole("SMS Thread error destroy() : "+ee.getMessage());
		}
	}
	
}