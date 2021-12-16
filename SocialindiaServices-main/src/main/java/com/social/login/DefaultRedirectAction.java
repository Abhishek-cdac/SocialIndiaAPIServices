package com.social.login;

import com.opensymphony.xwork2.ActionSupport;
import com.social.utils.Log;

import org.apache.struts2.ServletActionContext;



import javax.servlet.ServletContext;

public class DefaultRedirectAction extends ActionSupport {

  private static final long serialVersionUID = 1L;

  @Override  
  public String execute() {
	  	Log lg=new Log();
	    lg.logMessage("Test", "info", DefaultRedirectAction.class);
	    System.out.println("Enter to login page");
	    ServletContext context = ServletActionContext.getServletContext();
    
   /* LoadServices loadservic = new LoadDaoServices();
    if (context.getAttribute("MENUTYP") == null) {
      loadservic.getAllMenuMasterList();
    }*/

    return SUCCESS;
  }
}
