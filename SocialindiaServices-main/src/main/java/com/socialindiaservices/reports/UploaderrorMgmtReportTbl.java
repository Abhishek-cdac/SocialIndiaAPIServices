package com.socialindiaservices.reports;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.residentvo.UsrUpldfrmExceFailedTbl;
import com.pack.utilitypkg.Commonutility;
import com.siservices.townShipMgmt.townShipMgmtDao;
import com.siservices.townShipMgmt.townShipMgmtDaoServices;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class UploaderrorMgmtReportTbl extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<UsrUpldfrmExceFailedTbl> UplErrorDateList = new ArrayList<UsrUpldfrmExceFailedTbl>();
	townShipMgmtDao UplErrorDate=new townShipMgmtDaoServices();
	JSONObject jsonFinalObj=new JSONObject();

	Log log=new Log();
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
	//	Session locObjsession = null;		
		String ivrservicecode=null;
		CommonUtils locCommutillObj =null;
		Date entrydatetime=null;	
		try{
			 DateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			locCommutillObj = new CommonUtilsDao();
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
				String manualsearch="";
				if(fromdate.length() >0 && todate.length() >0){
						manualsearch += " and date(entryDate) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')  ";
				}else if(fromdate.length() >0){
						manualsearch += " and date(entryDate) >= str_to_date('" + fromdate + "', '%d-%m-%Y') ";
				}else if(todate.length() >0){
						manualsearch += " and date(entryDate) <= str_to_date('" + todate + "', '%d-%m-%Y') ";
				}
				String orderBy=" order by modifyDate desc";
				if(count1.equalsIgnoreCase("yes") && countF1.equalsIgnoreCase("yes"))
				{
					locVrSlQry="SELECT count(uniqueId) FROM UsrUpldfrmExceFailedTbl where status=1 "+manualsearch;
					System.out.println("Select Query : "+locVrSlQry);
					total = UplErrorDate.gettotalcount(locVrSlQry);
					System.out.println("total---- "+total);
				}else{
					count=0;
					countFilter = 0;
				}
				count=UplErrorDate.getInitTotal(locVrSlQry);
				 countFilter=UplErrorDate.getTotalFilter(locVrSlQry);
				 String qry = "From UsrUpldfrmExceFailedTbl  where status=1 "+manualsearch+orderBy;
				 UplErrorDateList = UplErrorDate.getUploaderrorDetailListByQry(qry,startrowfrom,totalNorow);				
				JSONArray jArray =new JSONArray();
				UsrUpldfrmExceFailedTbl UplErrorDateMaster;
				for (Iterator<UsrUpldfrmExceFailedTbl> it = UplErrorDateList
						.iterator(); it.hasNext();) {
					UplErrorDateMaster = it.next();
					JSONObject finalJson = new JSONObject();
					finalJson.put("uniqueid", UplErrorDateMaster.getUniqueId());
					finalJson.put("fileid", UplErrorDateMaster.getFileId());
					finalJson.put("filename", UplErrorDateMaster.getFileId().getFileName());
					finalJson.put("uploadusrid", UplErrorDateMaster.getUsrId().getUserName());
					finalJson.put("errorrowno", UplErrorDateMaster.getExcelErrRowId());
					entrydatetime = UplErrorDateMaster.getModifyDate();
					finalJson.put("entrydate",date.format(entrydatetime));
					jArray.put(finalJson);
					
					}
				jsonFinalObj.put("InitCount", count);
				jsonFinalObj.put("countAfterFilter", countFilter);
				jsonFinalObj.put("UplErrorDateMgmt", jArray);
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