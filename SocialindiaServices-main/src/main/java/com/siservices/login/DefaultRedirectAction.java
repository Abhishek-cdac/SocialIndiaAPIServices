package com.siservices.login;

import com.opensymphony.xwork2.ActionSupport;
import com.social.utils.Log;

public class DefaultRedirectAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	@Override
	public String execute() {
		Log lg=new Log();
	    lg.logMessage("Test", "info", DefaultRedirectAction.class);
		System.out.println("Enter to services page");
		
		
		return SUCCESS;
	
	
	}
}
