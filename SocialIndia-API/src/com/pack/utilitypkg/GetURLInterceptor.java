package com.pack.utilitypkg;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class GetURLInterceptor implements Interceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("GetURLInterceptor url  : DEST");
	}

	@Override
	public void init() {
		System.out.println("GetURLInterceptor url : initttt");
	}

	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		ActionContext context = arg0.getInvocationContext();
		String result = arg0.invoke();
		HttpServletRequest request = (HttpServletRequest)context.get(StrutsStatics.HTTP_REQUEST);
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String fullUrl = url + (queryString==null ? "" : ("?" + queryString));
		    
//		HttpServletRequest request = ServletActionContext.getRequest();
//		String url = request.getServletPath();
		System.out.println("GetURLInterceptor url : "+fullUrl);
		return null;
	}

}
