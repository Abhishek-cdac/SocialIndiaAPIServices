package com.socialindiaservices.reports;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.siservices.townShipMgmt.townShipMgmtDao;
import com.siservices.townShipMgmt.townShipMgmtDaoServices;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.social.utils.Log;

public class LoadTownshipReportTableData extends ActionSupport{
	
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
		String lsvSlQry = null;
	//	Session locObjsession = null;		
		String ivrservicecode=null;
		String currentloginid=null;
		try{
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				//ivrparams = EncDecrypt.decrypt(ivrparams);
				System.out.println("ivrparams-----------------"+ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					String fromdate=null,todate=null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,srchField=null,srchFieldval=null,srchflg=null;
					int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "countfilterflg");
				locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startrow");
				locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "totalrow");
				locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "srchdtsrch");
				srchflg=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "srchflg");
				srchField=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "srchField");
				srchFieldval=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "srchFieldval");
				fromdate=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "fromdate");
				todate=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "todate");
				currentloginid=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
				
				String count1 = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "count1");
				String countF1=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "countF1");
				String locVrSlQry="";
				int total = 0;
				if (Commonutility.toCheckisNumeric(locStrRow)) {
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if (Commonutility.toCheckisNumeric(locMaxrow)) {
					totalNorow=Integer.parseInt(locMaxrow);
				}
				String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
				
				String manualsearch="";
				
				if(fromdate.length() >0 && todate.length() >0){
						manualsearch += " and date(entryDatetime) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')  ";
				}else if(fromdate.length() >0){
						manualsearch += " and date(entryDatetime) >= str_to_date('" + fromdate + "', '%d-%m-%Y') ";
				}else if(todate.length() >0){
						manualsearch += " and date(entryDatetime) <= str_to_date('" + todate + "', '%d-%m-%Y') ";
				}
				
				String orderBy=" order by modifyDatetime desc";
                 String searchField="";
				
				if(locTochenull!=null && locTochenull.length()>0){
					
					searchField =" and (townshipName like('%"+locTochenull+"%') or noOfFlats like('%"+locTochenull+"%') or noOfSociety like('%"+locTochenull+"%'))";
					
					locVrSlQry="SELECT count(townshipId) FROM TownshipMstTbl where status=1 "+searchField+""+manualsearch;	
				}
				
				if(count1!=null && (count1.equalsIgnoreCase("yes") || countF1.equalsIgnoreCase("yes")))
				{
					locVrSlQry="SELECT count(townshipId) FROM TownshipMstTbl where status=1 "+searchField+""+manualsearch;
					System.out.println("Select Query : "+locVrSlQry);
					total = townShip.gettotalcount(locVrSlQry);
					System.out.println("total---- "+total);
				}else{
					count=0;
					countFilter = 0;
				}
				count=townShip.getInitTotal(locVrSlQry);
				 countFilter=townShip.getTotalFilter(locVrSlQry);
				 String qry = "From TownshipMstTbl  where status=1 "+manualsearch+" "+searchField+" "+orderBy;
				 townShipList = townShip.getTownShipDetailListByQry(qry,startrowfrom,totalNorow);				
				JSONArray jArray =new JSONArray();
				TownshipMstTbl townShipMaster;
				for (Iterator<TownshipMstTbl> it = townShipList
						.iterator(); it.hasNext();) {
					townShipMaster = it.next();
					JSONObject finalJson = new JSONObject();
					finalJson.put("townshipid", townShipMaster.getTownshipId());
					if(townShipMaster.getTownshipName()==null){
						finalJson.put("townshipname", "");
					}else{
						finalJson.put("townshipname", townShipMaster.getTownshipName());
					}
					if(townShipMaster.getActivationKey()==null){
					finalJson.put("activationkey", "");
					}else{
						finalJson.put("activationkey", townShipMaster.getActivationKey());
					}
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
					finalJson.put("noOfFlats", townShipMaster.getNoOfFlats());
					finalJson.put("noOfSociety", townShipMaster.getNoOfSociety());
					finalJson.put("address", townShipMaster.getAddress());
					jArray.put(finalJson);
					
					}
				jsonFinalObj.put("InitCount", count);
				jsonFinalObj.put("countAfterFilter", countFilter);
				jsonFinalObj.put("townShipMgmt", jArray);
				serverResponse(ivrservicecode, "0000",
						"0", "sucess", jsonFinalObj);
				
				
					//Response call
					serverResponse(ivrservicecode,"0","0000","Succes",locObjRspdataJson);
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
			 AuditTrial.toWriteAudit(getText("TWN0002"), "TWN0002", Integer.parseInt(currentloginid));
			log.logMessage("Service code : ivrservicecode, Sorry, an unhandled error occurred", "info", LoadTownshipreportExportData.class);
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
			 AuditTrial.toWriteAudit(getText("TWN0002"), "TWN0002", -1);
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