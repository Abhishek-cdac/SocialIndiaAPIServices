package com.social.utils;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.social.sms.SmsEngineCntrl;

public class Startsmsengine extends HttpServlet implements Runnable {

	/**
	 * 
	 */
	Thread ivrThrdobj = null;
	private static final long serialVersionUID = 1L;

	public Startsmsengine() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String enginevalue = config.getInitParameter("Engineflag");
		System.out.println("enginevalue = " + enginevalue);
		if(enginevalue!=null && enginevalue.equalsIgnoreCase("true")){
			SmsEngineCntrl.lvrEnginstrt = true;
		} else {
			SmsEngineCntrl.lvrEnginstrt = false;
		}		
		ivrThrdobj = new Thread(this, "Startsmsengine-Thread");
		ivrThrdobj.start();
	}

	/**
	 * @ServletConfig.
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response).
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response).
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	public void destroy() {
		try {
			System.out.println("[SMS Stoped]");
			SmsEngineCntrl.getInstance().stopThread();
		} catch (Exception ee) {
			System.out.println("Exception found destroy() : "+ee);
		}
	}

	@Override
	public void run() {
		try {
			System.out.println("[SMS Started]");
			Thread.sleep(30000);
		
			SmsEngineCntrl.getInstance().startThread();
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}

	}

}
