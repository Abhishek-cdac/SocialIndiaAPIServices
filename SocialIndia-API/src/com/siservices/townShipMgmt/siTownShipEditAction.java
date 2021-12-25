package com.siservices.townShipMgmt;



import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.social.utils.Log;

public class siTownShipEditAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<TownshipMstTbl> townShipList = new ArrayList<TownshipMstTbl>();
	TownshipMstTbl townShipMst =new TownshipMstTbl();
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
				String uniqTownShipIdEdit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "uniqTownShipIdEdit");
				String locVrSlQry="";				
				townShipMst = townShip.getTownShipDetailView(uniqTownShipIdEdit);
				if(townShipMst!=null){
						jsonFinalObj.put("townshipname", townShipMst.getTownshipName());
						jsonFinalObj.put("noofsociety", townShipMst.getNoOfSociety());
						jsonFinalObj.put("noofflats", townShipMst.getNoOfFlats());				
						if(townShipMst.getTownshipcolourcode()==null){
							jsonFinalObj.put("townshipcolourcode", "");
						}else{
							jsonFinalObj.put("townshipcolourcode", townShipMst.getTownshipcolourcode());
						}
						if(townShipMst.getTownshiplogoname()==null){
							jsonFinalObj.put("townshiplogo", "");
						}else{
							jsonFinalObj.put("townshiplogo", townShipMst.getTownshiplogoname());
						}
						if(townShipMst.getTownshipiconame()==null){
							jsonFinalObj.put("townshipico", "");
						}else{
							jsonFinalObj.put("townshipico", townShipMst.getTownshipiconame());
						}
						jsonFinalObj.put("townshipid", townShipMst.getTownshipId());
						if(townShipMst.getCountryId()==null){
							jsonFinalObj.put("countryid", 0);
						}else{
							jsonFinalObj.put("countryid", townShipMst.getCountryId().getCountryId());
							jsonFinalObj.put("countryname", townShipMst.getCountryId().getCountryName());
						}
						if(townShipMst.getStateId()==null){
							jsonFinalObj.put("stateid", 0);
						}else{
							jsonFinalObj.put("stateid", townShipMst.getStateId().getStateId());
							jsonFinalObj.put("statename", townShipMst.getStateId().getStateName());
						}if( townShipMst.getCityId()==null){
							jsonFinalObj.put("cityid", 0);
						}else{
							jsonFinalObj.put("cityid", townShipMst.getCityId().getCityId());
							jsonFinalObj.put("cityname", townShipMst.getCityId().getCityName());
						}if(townShipMst.getPstlId()==null){
							jsonFinalObj.put("pincode", 0);
						}else{
							jsonFinalObj.put("pincode", townShipMst.getPstlId());
	//						jsonFinalObj.put("pincode", townShipMst.getPstlId().getPstlId());
	//						jsonFinalObj.put("pincodename", townShipMst.getPstlId().getPstlCode());
						}if( townShipMst.getAddress()==null){
							jsonFinalObj.put("address","");
						}else{
							jsonFinalObj.put("address", townShipMst.getAddress());
						}if(townShipMst.getBuilderName()==null){
							jsonFinalObj.put("buildername","");
						}else{
							jsonFinalObj.put("buildername", townShipMst.getBuilderName());
						}if(townShipMst.getIsdCode()==null){
							jsonFinalObj.put("isdcode","");
						}else{
							jsonFinalObj.put("isdcode", townShipMst.getIsdCode());
						}if(townShipMst.getMobileNo()==null){
							jsonFinalObj.put("mobileno","");
						}else{
							jsonFinalObj.put("mobileno", townShipMst.getMobileNo());
						}if(townShipMst.getEmailId()==null){
							jsonFinalObj.put("emailid","");
						}else{
							jsonFinalObj.put("emailid", townShipMst.getEmailId());
						}if(townShipMst.getLandMark()==null){
							jsonFinalObj.put("landmark","");
						}else{
							jsonFinalObj.put("landmark", townShipMst.getLandMark());
						}if(townShipMst.getImprintName()==null){
							jsonFinalObj.put("imprintname","");
						}else{
							jsonFinalObj.put("imprintname", townShipMst.getImprintName());
						}				
						serverResponse(ivrservicecode, "00", "R0173", mobiCommon.getMsg("R0173"), jsonFinalObj);
				
					} else {
						serverResponse(ivrservicecode, "01", "R0003", mobiCommon.getMsg("R0003"), locObjRspdataJson);
					}
					
				}else{
					//Response call
					serverResponse(ivrservicecode,"01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}	
			}else{
				//Response call
				serverResponse(ivrservicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);

			}	
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			log.logMessage("Service code : ivrservicecode, Sorry, an unhandled error occurred", "info", siTownShipEditAction.class);
			try {
				serverResponse(ivrservicecode,"02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
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
