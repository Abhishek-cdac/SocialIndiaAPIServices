package com.siservices.uamm;

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
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.RightsMasterTblVo;

public class rightsCreation extends ActionSupport {
	private String ivrparams;
	UserMasterTblVo userData = new UserMasterTblVo();
	uamDao uam=new uamDaoServices();
	JSONObject jsonFinalObj=new JSONObject();
	private List<RightsMasterTblVo> rightsList = new ArrayList<RightsMasterTblVo>();
	public String execute() {

		JSONObject json = new JSONObject();
		try{
					boolean result =false;
					boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
					if (ivIsJson) {		
						 json = new JSONObject(ivrparams);
				
				System.out.println("===============json========" + json);
				String servicecode=json.getString("servicecode");
				System.out.println("servicecode   "+servicecode);

			JSONArray ar =null;
			JSONObject json_data = new JSONObject();
			System.out.println("===============json========" + json);
			//json_data=json.getJSONObject("data");
			ar = json.getJSONArray("allMenuID");
			System.out.println("===================dfdf======"+ar.length());
			Integer groupCode =  json.getInt("groupCode");
			if (groupCode > 0 && ar.length() > 0 && ar != null) {
				uam.deleteGroupCode(groupCode);
			JSONObject jsonList = new JSONObject();
			for(int i=0;i<ar.length();i++)
			{	
				jsonList=null;
				jsonList=ar.getJSONObject(i);
				int menuId=jsonList.getInt("menuID");
				//rightsList.add(new MenuMasterTblVo(menuID,menuDesc, menuPath, menuType, rootDesc));
				result=uam.rightsCreation(groupCode,menuId);
			}
			rightsList=uam.getUserRightscurrent(groupCode);
			if (result == true) {				
				RightsMasterTblVo rightsObj;
				JSONObject jsonObj=new JSONObject();
				JSONArray jArray =new JSONArray();
				for (Iterator<RightsMasterTblVo> it = rightsList
						.iterator(); it.hasNext();) {
					rightsObj = it.next();
					JSONObject finalJson = new JSONObject();
					finalJson.put("menuID", rightsObj.getMenuId().getMenuId());
					finalJson.put("menuType", rightsObj.getMenuId().getMenuType());
					finalJson.put("rootDesc", rightsObj.getMenuId().getRootDesc());
					finalJson.put("menuDesc", rightsObj.getMenuId().getMenuDesc());
					finalJson.put("menuPath", rightsObj.getMenuId().getMenuPath());
					if(rightsObj.getMenuId().getMenuClass()!=null){
						finalJson.put("menuClass", rightsObj.getMenuId().getMenuClass());
					} else {
						finalJson.put("menuClass", "");
					}
					
					jArray.put(finalJson);
					
					}
				jsonObj.put("resultFlag",result);
				jsonObj.put("menu", jArray);
				serverResponse(servicecode, "0",
						"0000","sucess", jsonObj);
				
			}
			
			}
		}
					else
					{
						json=new JSONObject();
						serverResponse("SI0017","1","E0001","Request values not not correct format",json);
					}
			
		}catch(Exception ex){
				System.out.println("=======userUpdate====Exception===="+ex);
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
	
	public String getIvrparams() {
		return ivrparams;
	}
	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}
	public UserMasterTblVo getUserData() {
		return userData;
	}
	public void setUserData(UserMasterTblVo userData) {
		this.userData = userData;
	}
	
}
