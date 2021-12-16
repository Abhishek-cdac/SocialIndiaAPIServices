package com.socialindia.generalmgnt;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.BiometricUserInfoTblVO;

public class biouserMgmtAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<BiometricUserInfoTblVO> userList = new ArrayList<BiometricUserInfoTblVO>();
	generalmgmtDao biometricmgmt = new generalmgntDaoServices();
	JSONObject jsonFinalObj = new JSONObject();

	Log log = new Log();

	public String execute() {
		Commonutility.toWriteConsole("biouserMgmtAction : Start");
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		String ivrservicecode = null;
		String globalsearch = "";
		int flag = 1;
		try {
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				Commonutility.toWriteConsole("Receive Param : " + ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					String society = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "society");
					String slectfield = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "slectfield");
					String searchname = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "searchname");
					String searchflg = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "searchflg");
					String count1 = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "count1");
					String countF1 = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "countF1");
					String locStrRow = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startrow");
					String locMaxrow = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,	"totalrow");
					String locpagefor = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "pagefor");
					String locSrchdtblsrch = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "srchdtsrch");
					String locTochenull = Commonutility.toCheckNullEmpty(locSrchdtblsrch);
					
					String fromd8 = null;
					String tod8 = null;
					try{
						flag = locObjRecvdataJson.getInt("flag");
						fromd8 = locObjRecvdataJson.getString("from");
						tod8 = locObjRecvdataJson.getString("to");
					}
					catch (Exception e) {
						// TODO: handle exception 
					}
					
					Commonutility.toWriteConsole("Received Param : flag, fromd8, tod8 " + flag +"  "+ fromd8 + "  " + tod8);
					
					String locVrSlQry = "";
					int startrowfrom = 0, totalNorow = 0;
					if (locStrRow != null && Commonutility.toCheckisNumeric(locStrRow)) {
						startrowfrom = Integer.parseInt(locStrRow);
					}
					
					if (locMaxrow !=null && Commonutility.toCheckisNumeric(locMaxrow)) {
						totalNorow = Integer.parseInt(locMaxrow);
					}
					
					String locOrderby = "";
					if(fromd8 == null || fromd8.isEmpty() || tod8 == null || tod8.isEmpty()){
						locOrderby = " order by bioLogDate desc";
					}
					else{
//						locOrderby = " and bioinsertdate >= '"+fromd8+"' and bioinsertdate <= '"+tod8+"') order by bioinsertdate desc";
//						locOrderby = " and bioinsertdate between '"+fromd8+"' and '"+tod8+"') order by bioinsertdate desc";
						locOrderby = " and bioLogDate between '"+fromd8+"' and '"+tod8+"') order by bioLogDate desc";
						
					}
					
					if (searchflg != null && !searchflg.equalsIgnoreCase("")) {
						if (slectfield != null && slectfield.length() > 0) {
							if (slectfield.equalsIgnoreCase("1")) {
								globalsearch = "(" + "bioResidentName like ('%"	+ searchname + "%') or "+ "bioEmail like ('%" + searchname+ "%') or " + "bioMobileNo like ('%"+ searchname + "%'))";
							} else if (slectfield.equalsIgnoreCase("2")) {
								globalsearch = "(" + "bioEmail like('%"+ searchname + "%') )";
							} else if (slectfield.equalsIgnoreCase("3")) {
								globalsearch = "(" + "bioMobileNo like('%"+ searchname + "%') )";
							} else if (slectfield.equalsIgnoreCase("4")) {
								globalsearch = "(" + "bioResidentName like('%"+ searchname + "%') )";
							}
						} else {
							globalsearch = "(" + "bioResidentName like ('%"+ searchname + "%') or "+ "bioEmail like ('%" + searchname+ "%') or " + "bioMobileNo like ('%"+ searchname + "%'))";
						}
						String qry = "From BiometricUserInfoTblVO where socyteaId = '"+ society+ "' and "+ globalsearch+ " "+ locOrderby + "";
						locVrSlQry = "SELECT count(bioResidentId) FROM BiometricUserInfoTblVO where socyteaId = '"+ society + "' and " + globalsearch + " " + locOrderby;
						userList = biometricmgmt.getUserDetailList_like_limt(qry, startrowfrom, totalNorow);
					} else {
						if (!locTochenull.equalsIgnoreCase("")	&& !locTochenull.equalsIgnoreCase("null")) { // datatable search
							globalsearch = "(" + "bioResidentName like ('%"+ locTochenull + "%') or "+ "bioEmail like ('%" + locTochenull+ "%') or " + "bioMobileNo like ('%"+ locTochenull + "%')  )";
							String qry = "From BiometricUserInfoTblVO where socyteaId = '"+ society	+ "' and "+ globalsearch+ " "+ locOrderby + "";
							locVrSlQry = "SELECT count(bioResidentId) FROM BiometricUserInfoTblVO where socyteaId = '"+ society + "' and " + globalsearch + " " + locOrderby;
							userList = biometricmgmt.getUserDetailList_like_limt(qry,startrowfrom, totalNorow);

						} else { // default load
							if (!Commonutility.checkempty(locpagefor) && totalNorow > 0) {
								String qry = "From BiometricUserInfoTblVO where socyteaId = '"+ society + "'" + locOrderby + "";
								locVrSlQry = "SELECT count(bioResidentId) FROM BiometricUserInfoTblVO where socyteaId = '"+ society + "' " + locOrderby;
								userList = biometricmgmt.getUserDetailList_like_limt(qry,startrowfrom, totalNorow);
							} else {
								if (society != null
										&& !society.equalsIgnoreCase("-1")
										&& !society.equalsIgnoreCase("")) {
									String qry = "From BiometricUserInfoTblVO where socyteaId = '"+ society + "'" + locOrderby + "";
									locVrSlQry = "SELECT count(bioResidentId) FROM BiometricUserInfoTblVO where socyteaId ='"+ society +"' "+ locOrderby;
									userList = biometricmgmt.getUserDetailList_like(qry);
								} else {
									String qry = "From BiometricUserInfoTblVO where socyteaId = '"+ society + "'" + locOrderby + "";
									locVrSlQry = "SELECT count(bioResidentId) FROM BiometricUserInfoTblVO where socyteaId = '"+ society + "' "+ locOrderby;
									userList = biometricmgmt.getUserDetailList_like(qry);
								}
							}
						}
					}
					
					
					int count;
					int countFilter;
					if (count1 !=null && countF1!=null && count1.equalsIgnoreCase("yes")
							&& countF1.equalsIgnoreCase("yes")) {
					} else {
						count = 0;
						countFilter = 0;
					}
					count = biometricmgmt.getInitTotal(locVrSlQry);
					countFilter = biometricmgmt.getTotalFilter(locVrSlQry);

					Commonutility.toWriteConsole("biouserMgmtAction : userList:"+userList.size());
					
					JSONArray jArray = new JSONArray();
					BiometricUserInfoTblVO userMaster;
					for (Iterator<BiometricUserInfoTblVO> it = userList.iterator(); it.hasNext();) {
						userMaster = it.next();

						JSONObject finalJson = new JSONObject();
						if (userMaster.getBioResidentId() != null) {
							finalJson.put("bioResidentId",
							userMaster.getBioResidentId());
						} else {
							finalJson.put("bioResidentId", "");
						}
						if (userMaster.getBioResidentName() != null) {
							finalJson.put("bioResidentName",
							userMaster.getBioResidentName());
						} else {
							finalJson.put("bioResidentName", "");
						}
						if (userMaster.getBioMobileNo() != null) {
							finalJson.put("bioMobileNo",
							userMaster.getBioMobileNo());
						} else {
							finalJson.put("bioMobileNo", "");
						}
						if (userMaster.getBioEmail() != null) {
							finalJson.put("bioEmail", 
							userMaster.getBioEmail());
						} else {
							finalJson.put("bioEmail", "");
						}
						if (userMaster.getBioRecordStatus() != null) {
							finalJson.put("bioRecordStatus",
							userMaster.getBioRecordStatus());
						} else {
							finalJson.put("bioRecordStatus", "");
						}
						if (userMaster.getBioLocation() != null) {
							finalJson.put("bioLocation",
							userMaster.getBioLocation());
						} else {
							finalJson.put("bioLocation", "");
						}
						if (userMaster.getBioIsSendPushNotification() != null) {
							finalJson.put("bioIsSendPushNotification",
							userMaster.getBioIsSendPushNotification());
						} else {
							finalJson.put("bioIsSendPushNotification", "");
						}
						
						
						if (userMaster.getBioDeviceLogId() != null) {
							finalJson.put("bioDeviceLogId",
							userMaster.getBioDeviceLogId());
						} else {
							finalJson.put("bioDeviceLogId", "");
						}
						if (userMaster.getBioDeviceId() != null) {
							finalJson.put("bioDeviceId",
							userMaster.getBioDeviceId());
						} else {
							finalJson.put("bioDeviceId", "");
						}
						if (userMaster.getBioUserId() != null) {
							finalJson.put("bioUserId",
							userMaster.getBioUserId());
						} else {
							finalJson.put("bioUserId", "");
						}
						if (userMaster.getBioLogDate() != null) {
							finalJson.put("bioLogDate", 
						    userMaster.getBioLogDate());
						} else {
							finalJson.put("bioLogDate", "");
						}
						if (userMaster.getBioStatusCode() != null) {
							finalJson.put("bioStatusCode",
							userMaster.getBioStatusCode());
						} else {
							finalJson.put("bioStatusCode", "");
						}
						if (userMaster.getBioDuration() != null) {
							finalJson.put("bioDuration",
							userMaster.getBioDuration());
						} else {
							finalJson.put("bioDuration", "");
						}
						if (userMaster.getBioVerificationMode() != null) {
							finalJson.put("bioVerificationMode",
							userMaster.getBioVerificationMode());
						} else {
							finalJson.put("bioVerificationMode", "");
						}
						
						jArray.put(finalJson);

					}
					jsonFinalObj.put("InitCount", count);
					jsonFinalObj.put("countAfterFilter", countFilter);
					jsonFinalObj.put("userMgmt", jArray);
					serverResponse(ivrservicecode, "0000", "0", "sucess", jsonFinalObj, flag);
				} else {
					// Response call
					serverResponse(ivrservicecode, "1", "E0001", "Request values are not correct format", locObjRspdataJson, flag);
				}
			} else {
				// Response call
				serverResponse(ivrservicecode, "1", "E0001", "Request values are empty", locObjRspdataJson, flag);
			}
			System.out.println("---------Completed services Block--------");
		} catch (Exception e) {
			StackTraceElement[] err = e.getStackTrace();
			StringBuilder error = new StringBuilder();
			for (int i = 0; i < err.length; i++) {
				error.append(err[i].toString() + "\n");
			}
			Commonutility.toWriteConsole("biouserMgmtAction : Start"+error.toString());
		} finally {
			// if(locObjsession!=null){locObjsession.close();locObjsession=null;}
			locObjRecvJson = null;
			locObjRecvdataJson = null;
			locObjRspdataJson = null;
		}
		return SUCCESS;
	}

	private void serverResponse(String serviceCode, String statusCode,String respCode, String message, JSONObject dataJson, int flag)
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
				
				if(flag == 1){
					responseMsg.put("servicecode", serviceCode);
					responseMsg.put("statuscode", statusCode);
					responseMsg.put("respcode", respCode);
					responseMsg.put("message", message);
					responseMsg.put("data", dataJson);
					String as = responseMsg.toString();
					out.print(as);
					out.close();
				}
				else{
					mobiCommon mobicomn=new mobiCommon();
					
					if(statusCode !=null && statusCode.equalsIgnoreCase("0000"))
						statusCode = "00";
					
					String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
					out.print(as);
					out.close();
				}
				
			} catch (Exception ex) {
				out = response.getWriter();
				log.logMessage("Service : biouserMgmtAction error occurred : " + ex,"error", biouserMgmtAction.class);
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
