package com.siservices.townShipMgmt;



import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.login.EncDecrypt;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.social.utils.Log;

public class siTownShipGenerateNewKey extends ActionSupport{
	
	private String ivrparams;
	List<TownshipMstTbl> townShipList = new ArrayList<TownshipMstTbl>();
	TownshipMstTbl townShipMst =new TownshipMstTbl();
	private String newActivationKey;
	boolean result;
	townShipMgmtDao townShip=new townShipMgmtDaoServices();
	JSONObject jsonFinalObj=new JSONObject();

	Log log=new Log();
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
	//	Session locObjsession = null;		
		String ivrservicecode=null;
		try{
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				
				
				int uniqTownShipIdEdit = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "uniqTownShipIdEdit");
				String locVrSlQry="";
				
				System.out.println("==========uniqTownShipIdEdit====="+uniqTownShipIdEdit);
				CommonUtils comutil=new CommonUtilsServices();
				String activationKey=comutil.getRandomval("AZ_09", 25);
				System.out.println("===========activationKey======="+activationKey);
				townShipMst=townShip.getTownShipDetailView(String.valueOf(uniqTownShipIdEdit));
				if(townShipMst!=null){
					result = townShip.generateNewTownshipPassword(uniqTownShipIdEdit,townShipMst,activationKey);
					if(result==true){
						jsonFinalObj.put("newactivationkey", activationKey);
						serverResponse(ivrservicecode, "0",
								"0000", "success", jsonFinalObj);
					}else{
						serverResponse(ivrservicecode, "1",
								"E0001", "Error in updating", jsonFinalObj);
					}
				}
				System.out.println("======eeeeeee===========ddddddddddddddddddd=========");
				serverResponse(ivrservicecode, "0",
						"0000", "success", jsonFinalObj);
				
				}else{
					serverResponse(ivrservicecode,"1","E0001","database error occured",locObjRspdataJson);
				}
					
				}else{
					//Response call
					serverResponse(ivrservicecode,"1","E0001","Request values are not correct format",locObjRspdataJson);
				}	
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			log.logMessage("Service code : ivrservicecode, Sorry, an unhandled error occurred", "info", siTownShipGenerateNewKey.class);
			try {
				serverResponse(ivrservicecode,"2","E0002","Sorry, an unhandled error occurred",locObjRspdataJson);
			} catch (Exception e1) {}
		}finally{
			//if(locObjsession!=null){locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
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

	public TownshipMstTbl getTownShipMst() {
		return townShipMst;
	}

	public void setTownShipMst(TownshipMstTbl townShipMst) {
		this.townShipMst = townShipMst;
	}

}
