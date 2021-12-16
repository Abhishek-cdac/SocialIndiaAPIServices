package com.siservices.login;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.uam.persistense.MenuMasterTblVo;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;

public class menuListLoad extends ActionSupport {
	private static final long serialVersionUID = 1L;
	List<MenuMasterTblVo> menuList = new ArrayList<MenuMasterTblVo>();
	private List<TownshipMstTbl> Townshiplist = new ArrayList<TownshipMstTbl>();
	uamDao uam = new uamDaoServices();
	private String paramval;
	@Override
	public String execute() {
		try {
			menuList = uam.getAllMenuMasterList();
			MenuMasterTblVo menuObj;
			JSONArray jArray = new JSONArray();
			JSONObject jsonObj = new JSONObject();
			JSONArray jArraytown = new JSONArray();
			TownshipMstTbl townshipObj = null;
		for (Iterator<MenuMasterTblVo> it = menuList.iterator(); it.hasNext();) {
				menuObj = it.next();
				JSONObject finalJson = new JSONObject();
				finalJson.put("menuId", menuObj.getMenuId());
				finalJson.put("menuType", menuObj.getMenuType());
				finalJson.put("menuPath", menuObj.getMenuPath());
				finalJson.put("rootDesc", menuObj.getRootDesc());
				finalJson.put("menuDesc", menuObj.getMenuDesc());
				if (menuObj.getMenuClass() != null) {
					finalJson.put("menuClass", menuObj.getMenuClass());
				} else {
					finalJson.put("menuClass", "");
				}
				jArray.put(finalJson);

		}
			Townshiplist = uam.gettownshiplist();
		for (Iterator<TownshipMstTbl> it1 = Townshiplist.iterator(); it1.hasNext();) {
				townshipObj = it1.next();
				JSONObject finalJsontoenship = new JSONObject();
				finalJsontoenship.put("townshipuniid", townshipObj.getTownshipId());
				finalJsontoenship.put("townshipname", townshipObj.getTownshipName());
				jArraytown.put(finalJsontoenship);			
		}
			jsonObj.put("allMenuList", jArray);
			jsonObj.put("townshipdetail", jArraytown);
		serverResponse(getText("SI0004"), getText("E0001"), getText("1"), getText("EM0001"), jsonObj);
		}catch (Exception ex){
			Commonutility.toWriteConsole("Exception found  menuListLoad.class : "+ex);
		} finally {
			
		}
		return SUCCESS;
	}
	private void serverResponse(String serviceCode, String statusCode,
			String respCode, String message, JSONObject dataJson)
			throws Exception {
		PrintWriter out;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response;
		response = ServletActionContext.getResponse();
		out = response.getWriter();
		try {
			responseMsg = new JSONObject();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			responseMsg.put("servicecode", serviceCode);
			responseMsg.put("statuscode", statusCode);
			responseMsg.put("respcode", respCode);
			responseMsg.put("message", message);
			responseMsg.put("data", dataJson);
			String as = responseMsg.toString();
			out.print(as);
			out.close();
		} catch (Exception ex) {
			out = response.getWriter();
			out.print("{\"servicecode\":\"" + serviceCode + "\",");
			out.print("{\"statuscode\":\"2\",");
			out.print("{\"respcode\":\"E0002\",");
			out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
			out.print("{\"data\":\"{}\"}");
			out.close();
			ex.printStackTrace();
		}
	}
	public List<MenuMasterTblVo> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<MenuMasterTblVo> menuList) {
		this.menuList = menuList;
	}
	public String getParamval() {
		return paramval;
	}
	public void setParamval(String paramval) {
		this.paramval = paramval;
	}
}
