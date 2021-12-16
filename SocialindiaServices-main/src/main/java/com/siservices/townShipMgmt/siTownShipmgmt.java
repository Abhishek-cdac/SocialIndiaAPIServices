package com.siservices.townShipMgmt;

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
import com.siservices.uam.persistense.TownshipMstTbl;
import com.social.utils.Log;

public class siTownShipmgmt extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<TownshipMstTbl> townShipList = new ArrayList<TownshipMstTbl>();
	townShipMgmtDao townShip=new townShipMgmtDaoServices();
	JSONObject jsonFinalObj=new JSONObject();

	Log log=new Log();
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null, locvrEventSTS=null, locSrchdtblsrch=null;
		String ivrservicecode=null;
		int startrowfrom =1;
		int totalNorow = 10;
		try{
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				//ivrparams = EncDecrypt.decrypt(ivrparams);				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				
				locvrEventSTS = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "societystatus");
				locSrchdtblsrch =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "srchdtsrch");
				String count1 = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "count1");
				String countF1=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "countF1");
				String locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startrow");
				String locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "totalrow");
				String locTochenull = Commonutility.toCheckNullEmpty(locSrchdtblsrch);
				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}
				
				String locVrSlQry="",lvSlqry="",srchcntqry="";
				int total = 0;
				int count;
				int countFilter,lvrsoctysts = 1;
				String locOrderby =" order by entryDatetime desc";
					if (count1.equalsIgnoreCase("yes") && countF1.equalsIgnoreCase("yes")) {
						if (!locTochenull.equalsIgnoreCase("") && !locTochenull.equalsIgnoreCase("null")) {
							srchcntqry = " (" + " townshipId.townshipName like ('%" + locTochenull + "%') or " 
						             + " townshipId.stateId.stateName like ('%" + locTochenull + "%') or "
						             + " townshipId.countryId.countryName like ('%" + locTochenull + "%') or "
						             + " townshipId.cityId.cityName like ('%" + locTochenull + "%')"
						             + ")";
							locVrSlQry="SELECT count(townshipId) FROM TownshipMstTbl where "+srchcntqry;
						} else {
							locVrSlQry="SELECT count(townshipId) FROM TownshipMstTbl";
						}
						total = townShip.gettotalcount(locVrSlQry);
					} else {
						count = 0;
						countFilter = 0;
					}
					count = townShip.getInitTotal(locVrSlQry);
					countFilter = townShip.getTotalFilter(locVrSlQry);
					if(!locTochenull.equalsIgnoreCase("") && !locTochenull.equalsIgnoreCase("null")){
						lvSlqry="FROM TownshipMstTbl where "+srchcntqry +locOrderby ;
					}else{
						lvSlqry = "FROM TownshipMstTbl"+locOrderby;
					}
					townShipList = townShip.getTownShipDetailListPagenation(lvSlqry,startrowfrom, totalNorow);				
					JSONArray jArray = new JSONArray();
					TownshipMstTbl townShipMaster;
					for (Iterator<TownshipMstTbl> it = townShipList.iterator(); it.hasNext();) {
					townShipMaster = it.next();
					JSONObject finalJson = new JSONObject();
					finalJson.put("townshipid", townShipMaster.getTownshipId());
						if (townShipMaster.getTownshipName()==null){
							finalJson.put("townshipname", "");
						} else {
							finalJson.put("townshipname", townShipMaster.getTownshipName());
						}
						if (townShipMaster.getActivationKey()==null){
							finalJson.put("activationkey", "");
						} else {
							finalJson.put("activationkey", townShipMaster.getActivationKey());
						}
						finalJson.put("townshipuniqueid", Commonutility.toCheckNullEmpty(townShipMaster.getTownshipUniqueId()));
					if(townShipMaster.getCountryId().getCountryName()==null){
						finalJson.put("countryname", "");
					}else{
					finalJson.put("countryname", townShipMaster.getCountryId().getCountryName());
					}
					if(townShipMaster.getStateId().getStateName()==null){
					finalJson.put("statename", "");
					}else{
						finalJson.put("statename", townShipMaster.getStateId().getStateName());
					}
					if(townShipMaster.getCityId().getCityName()==null){
					finalJson.put("cityname", "");
					}else{
						finalJson.put("cityname", townShipMaster.getCityId().getCityName());
					}
					
					finalJson.put("landmark",Commonutility.toCheckNullEmpty(townShipMaster.getLandMark()));
					
					jArray.put(finalJson);
					
					}
					jsonFinalObj.put("InitCount", count);
					jsonFinalObj.put("countAfterFilter", countFilter);
					jsonFinalObj.put("townShipMgmt", jArray);
					serverResponse(ivrservicecode, "0000", "0", "sucess", jsonFinalObj);												
				}else{
					//Response call
					serverResponse(ivrservicecode,"1","E0001","Request values are not correct format",locObjRspdataJson);
				}	
			}else{
				//Response call
				serverResponse(ivrservicecode,"1","E0001","Request values are empty",locObjRspdataJson);

			}	
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			log.logMessage("Service code : ivrservicecode, Sorry, an unhandled error occurred", "info", siTownShipmgmt.class);
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
	
	

}
