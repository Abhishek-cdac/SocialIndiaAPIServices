package com.socialindia.generalmgnt;



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
import com.social.utils.Log;
import com.socialindia.generalmgnt.persistance.StaffMasterTblVo;

public class StaffmgmtAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<StaffMasterTblVo> userList = new ArrayList<StaffMasterTblVo>();
	generalmgmtDao uam=new generalmgntDaoServices();
	JSONObject jsonFinalObj=new JSONObject();
	Log log=new Log();
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null,locvrStaffSTS = null, locStrRow =null, locMaxrow=null,locSrchdtblsrch = null;		
		String ivrservicecode=null;
		int startrowfrom=1, totalNorow=10, lvrstaffsts = 1;
		try{
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				//ivrparams = EncDecrypt.decrypt(ivrparams);				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					String count1 = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "count1");
					String countF1 = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"countF1");
					locvrStaffSTS = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"staffstatus");
					locSrchdtblsrch =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "srchdtsrch");
					locStrRow = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startrow");
					locMaxrow = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "totalrow");
					String locTochenull = Commonutility.toCheckNullEmpty(locSrchdtblsrch);
					String locVrSlQry = "";
					int total = 0;
					int count;
					int countFilter;
					if(Commonutility.toCheckisNumeric(locStrRow)){
						 startrowfrom=Integer.parseInt(locStrRow);
					}
					if(Commonutility.toCheckisNumeric(locMaxrow)){
						totalNorow=Integer.parseInt(locMaxrow);
					}	
					if(locvrStaffSTS!=null && Commonutility.toCheckisNumeric(locvrStaffSTS)){
						lvrstaffsts = Integer.parseInt(locvrStaffSTS);
					}
					String srchcntqry = "";
					String lvrDataqry = "";
					String lvrOrderby = "order by entryDatetime desc";
					if (count1.equalsIgnoreCase("yes") && countF1.equalsIgnoreCase("yes")) {
						if (!locTochenull.equalsIgnoreCase("") && !locTochenull.equalsIgnoreCase("null")) {
							srchcntqry = " and (" + " staffName like ('%" + locTochenull + "%') or " 
									 + " staffEmail like ('%" + locTochenull + "%') or "
									 + " staffMobno like ('%" + locTochenull + "%') or "
									 + " staffAddr like ('%" + locTochenull + "%') or "
									 + " staffIdcardno like ('%" + locTochenull + "%') "									 
									 
									 + ")";
							locVrSlQry = "SELECT count(staffID) FROM StaffMasterTblVo where status = "+lvrstaffsts+" "+srchcntqry;
							lvrDataqry = " from StaffMasterTblVo where status = "+lvrstaffsts+" "+srchcntqry + " " + lvrOrderby;
						}else{
							locVrSlQry = "SELECT count(staffID) FROM StaffMasterTblVo where status = "+lvrstaffsts+"";
							lvrDataqry = " from StaffMasterTblVo where status = "+lvrstaffsts+ " " + lvrOrderby;
						}
						System.out.println("Select Query : " + locVrSlQry);
						total = uam.gettotalcount(locVrSlQry);
					} else {
						count = 0;
						countFilter = 0;
						if (!locTochenull.equalsIgnoreCase("") && !locTochenull.equalsIgnoreCase("null")) {
							srchcntqry = " and (" + " staffName like ('%" + locTochenull + "%') or " 
									 + " staffEmail like ('%" + locTochenull + "%') or "
									 + " staffMobno like ('%" + locTochenull + "%') or "
									 + " staffAddr like ('%" + locTochenull + "%') or "
									 + " staffIdcardno like ('%" + locTochenull + "%') "									 									 
									 + ")";
							lvrDataqry = " from StaffMasterTblVo where status = "+lvrstaffsts+" "+srchcntqry;
						}else{
							lvrDataqry = " from StaffMasterTblVo where status = "+lvrstaffsts+ " " + lvrOrderby;
						}
					}
					System.out.println("Select Data : "+ lvrDataqry);
					count = uam.getInitTotal(locVrSlQry);
					countFilter = uam.getTotalFilter(locVrSlQry);
					userList = uam.getUserDetailList(lvrDataqry, startrowfrom, totalNorow);
					JSONArray jArray = new JSONArray();
					StaffMasterTblVo staffMaster;
				for (Iterator<StaffMasterTblVo> it = userList.iterator(); it.hasNext();) {
					staffMaster = it.next();
					JSONObject finalJson = new JSONObject();					
					finalJson.put("staffName", staffMaster.getStaffName());
					finalJson.put("staffMobno", staffMaster.getStaffMobno());
					finalJson.put("staffEmail", staffMaster.getStaffEmail());
					finalJson.put("staffID", staffMaster.getStaffID());
					//finalJson.put("staffage", staffMaster.getGroupCode().getGroupName());
					finalJson.put("staffAddr", staffMaster.getStaffAddr());
					jArray.put(finalJson);
					
				}
					jsonFinalObj.put("InitCount", count);
					jsonFinalObj.put("countAfterFilter", countFilter);
					jsonFinalObj.put("userMgmt", jArray);
					serverResponse(ivrservicecode, "0000","0", "sucess", jsonFinalObj);								
					//Response call				
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
			log.logMessage("Service code : ivrservicecode, Sorry, an unhandled error occurred", "info", StaffmgmtAction.class);
			try {
				serverResponse(ivrservicecode,"2","E0002","Sorry, an unhandled error occurred",locObjRspdataJson);
			} catch (Exception e1) {}
		}finally{			
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
