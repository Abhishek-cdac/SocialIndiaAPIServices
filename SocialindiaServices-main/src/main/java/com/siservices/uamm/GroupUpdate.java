package com.siservices.uamm;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;

public class GroupUpdate extends ActionSupport{
	private String ivrparams;
	uamDao uam = new uamDaoServices();
	UserMasterTblVo userData = new UserMasterTblVo();
	JSONObject finalJson = new JSONObject();
	JSONObject finalJsondata = new JSONObject();
	
	public String execute() {
		JSONObject json = new JSONObject();
		try{
			 boolean result;

			 boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				json = new JSONObject(ivrparams);
				String servicecode = json.getString("servicecode");

				System.out.println("servicecode   " + servicecode);
				if (ivIsJson) {
					String groupcode =  json.getString("groupcode");
					String flg =  json.getString("flg");
					String st="Both";
					int groupco=Integer.parseInt(groupcode);
					Integer userId=json.getInt("userId");
					System.out.println(userId);
					if(groupcode.equalsIgnoreCase("6")){
						result=uam.UpdateGroupsMobiAccess(userId,groupcode,flg);
					}else{
						result=uam.UpdateGroupBoth(userId,groupcode,flg);
					}


					if(result==true){
						serverResponse(servicecode, "0",
								"0000", "sucess", finalJson);
					}else{
					serverResponse(servicecode, "1",
							"E0001", "Error in updating", finalJson);
					}
						}
						else {
							json = new JSONObject();
							serverResponse(servicecode, "1", "E0001",
									"Request values not not correct format", json);
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



}
