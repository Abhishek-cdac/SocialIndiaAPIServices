package com.siservices.townShipMgmt;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.CityMasterTblVO;
import com.pack.commonvo.CountryMasterTblVO;
import com.pack.commonvo.PostalCodeMasterTblVO;
import com.pack.commonvo.StateMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.signUpDao;
import com.siservices.signup.persistense.signUpDaoServices;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class siTownShipEditUpdateAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	signUpDao signup = new signUpDaoServices();
	TownshipMstTbl townshipData = new TownshipMstTbl();
	townShipMgmtDao townShip=new townShipMgmtDaoServices();
	CountryMasterTblVO countryMst=new CountryMasterTblVO();
	StateMasterTblVO stateMST=new StateMasterTblVO();
	CityMasterTblVO cityMst=new CityMasterTblVO();
	PostalCodeMasterTblVO postalMst=new PostalCodeMasterTblVO();
	Log log=new Log();		
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = new JSONObject();// Response Data Json		
		JSONObject data=new JSONObject();		
		String ivrservicecode=null;
		 int uniqueVal;
		String occupation=null;
		String logoImageFileName=null,icoImageFileName=null,countryCode=null,stateCode=null,postalCode=null;
		String cityCode=null,address=null,townshipname=null;
		int noofmember,uniqTownShipIdEdit;
		
		String pincode=null,townshipcolor=null,townshiplogo=null,townshipico=null;
		String noofsociety=null,noofflats=null,townshipidmodify=null;
		String statecode=null,citycode=null;
		String buildername=null,isdcode=null,mobileno=null,emailid=null,landmark=null,imprintname=null;
		byte conbytetoarr[]= new byte[1024];
		byte conbytetoarr1[]= new byte[1024];
		uamDao uam=new uamDaoServices();
		CommonUtilsDao common=new CommonUtilsDao();		
		ResourceBundle rb=ResourceBundle.getBundle("applicationResources");
		try{				
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);									
					locObjRecvdataJson=(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");					
					ivrservicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");						
					uniqTownShipIdEdit=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "uniqTownShipIdEdit");
					townshipname=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "townshipname");
					noofsociety=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "noofsociety");
					noofflats=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "noofflats");					
					townshipcolor=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "townshipcolorcode");
					townshiplogo=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "townshiplogo");
					townshipico=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "townshipico");
					logoImageFileName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "logoImageFileName");
					icoImageFileName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "icoImageFileName");
					countryCode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "countryCode");
					address=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "address");
					stateCode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "stateCode");
					postalCode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "postalCode");
					cityCode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "cityCode");
					
					buildername=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "buildername");
					isdcode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "isdcode");
					mobileno=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "mobileno");
					emailid=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "emailid");
					landmark=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "landmark");
					imprintname=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imprintname");										
					if(logoImageFileName!=null && !logoImageFileName.equalsIgnoreCase("")){		
						logoImageFileName = logoImageFileName.replaceAll(" ", "_");
						townshipData.setTownshiplogoname(logoImageFileName);
					}else{
						townshipData.setTownshiplogoname("");
					}
					if (icoImageFileName != null && !icoImageFileName.equalsIgnoreCase("")) {
						icoImageFileName = icoImageFileName.replaceAll(" ", "_");
						townshipData.setTownshipiconame(icoImageFileName);
					} else {
						townshipData.setTownshipiconame("");
					}
					
					townshipData.setNoOfSociety(Integer.parseInt(noofsociety));
					townshipData.setNoOfFlats(Integer.parseInt(noofflats));
					townshipData.setTownshipcolourcode(townshipcolor);
					townshipData.setAddress(address);
					townshipData.setTownshipName(townshipname);
					townshipData.setBuilderName(buildername);
					townshipData.setIsdCode(isdcode);
					townshipData.setMobileNo(mobileno);
					townshipData.setEmailId(emailid);
					townshipData.setLandMark(landmark);
					townshipData.setImprintName(imprintname);
					uniqueVal=townShip.townShipEditUpdate(townshipData,uniqTownShipIdEdit,countryCode,stateCode,cityCode,postalCode);					
					if(uniqueVal==-2){
						locObjRspdataJson.put("servicecode", ivrservicecode);
						serverResponse(ivrservicecode, "02", "R0062", mobiCommon.getMsg("R0062"), locObjRspdataJson);
					}
					if(uniqueVal==-3){
						locObjRspdataJson.put("servicecode", ivrservicecode);
						serverResponse(ivrservicecode, "03", "R0063", mobiCommon.getMsg("R0063"), locObjRspdataJson);
					} else{						
						locObjRspdataJson.put("townshipid", uniqueVal);
						serverResponse(ivrservicecode,"00","R0061", mobiCommon.getMsg("R0064"), locObjRspdataJson);
					}
					
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI0024, "+mobiCommon.getMsg("R0003"), "info", siTownShipEditUpdateAction.class);
				serverResponse(ivrservicecode, "1", "R0003", mobiCommon.getMsg("R0003"), locObjRspdataJson);
			}
			
			}	
		}catch(Exception e){
			System.out.println("Service code : SI0024, Exception found in siTownShipEditUpdateAction.class execute() Method : "+e);
			log.logMessage("Service code : SI0024, Sorry, an unhandled error occurred", "info", siTownShipEditUpdateAction.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(ivrservicecode,"2","SI0024","Sorry, an unhandled error occurred",locObjRspdataJson);
		}finally{		
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;
		}				
			return SUCCESS;
	}
	
	
	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson)
	{
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();		
		try {
			out = response.getWriter();
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
			try{
			out = response.getWriter();
			out.print("{\"servicecode\":\"" + serviceCode + "\",");
			out.print("{\"statuscode\":\"2\",");
			out.print("{\"respcode\":\"E0002\",");
			out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
			out.print("{\"data\":\"{}\"}");
			out.close();
			ex.printStackTrace();
			}catch(Exception e){}finally{}
		}
	}

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}
	
	
}
