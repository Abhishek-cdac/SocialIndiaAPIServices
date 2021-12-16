package com.pack.feedback;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class FeedbackUpdate extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicefor;
	
	public String execute(){
		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json	
		String ivrservicecode=null,ivrCurntusridstr=null;  
		String ivrDecissBlkflag=null; // 1- new create, 2- update, 3 - select single[], 4 - deActive , 5 - delete , 6 - Block
		int ivrEntryByusrid=0;
		try{
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					ivrCurntusridstr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"currentloginid");									
					if(ivrCurntusridstr!=null && Commonutility.toCheckisNumeric(ivrCurntusridstr)){
						ivrEntryByusrid= Integer.parseInt(ivrCurntusridstr);
					}else{ ivrEntryByusrid=0; }
					
					if (ivrservicefor != null && !ivrservicefor.equalsIgnoreCase("null") && ivrservicefor.length() > 0) {//get
						ivrDecissBlkflag = ivrservicefor;
					}else{//post
						ivrDecissBlkflag = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicefor");
					}					
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					String locvrDecissBlkflagchk=Commonutility.toCheckNullEmpty(ivrDecissBlkflag);
					String locvrFnrst=null;
					String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;
					locObjRspdataJson=new JSONObject();
					if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert Feedback
						
						log.logMessage("Step 1 : Feedback Insert precess will start.", "info", FeedbackUpdate.class);
						locvrFnrst = FeedbackUtility.toInsertFeedback(locObjRecvdataJson);
						String locSbt[]=locvrFnrst.split("!_!");					
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI5001";locRtnStsCd="00"; locRtnRspCode="R0167";/*locRtnMsg=getText("feedbck.insert.success");*/locRtnMsg=mobiCommon.getMsg("R0167");
								AuditTrial.toWriteAudit(getText("FBKADOO1"), "FBKADOO1", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI5001";locRtnStsCd="01"; locRtnRspCode="R0168";/*locRtnMsg=getText("feedbck.insert.error");*/locRtnMsg=mobiCommon.getMsg("R0168");
								AuditTrial.toWriteAudit(getText("FBKADOO2"), "FBKADOO2", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI5001";locRtnStsCd="01"; locRtnRspCode="R0168";/*locRtnMsg=getText("feedbck.insert.error");*/locRtnMsg=mobiCommon.getMsg("R0168");
							AuditTrial.toWriteAudit(getText("FBKADOO2"), "FBKADOO2", ivrEntryByusrid);
						}
						log.logMessage("Step 5 : Feedback Insert process end", "info", FeedbackUpdate.class);
						
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){// update Feedback
						
						log.logMessage("Step 1 : Feedback update will start.", "info", FeedbackUpdate.class);
						locvrFnrst=FeedbackUtility.toUpdateFeedback(locObjRecvdataJson);
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
							locRtnSrvId="SI5002";locRtnStsCd="00"; locRtnRspCode="R0169";/*locRtnMsg=getText("feedbck.update.success");*/locRtnMsg=mobiCommon.getMsg("R0169");
							AuditTrial.toWriteAudit(getText("FBKAD005"), "FBKAD005", ivrEntryByusrid);
						}else{
							locRtnSrvId="SI5002";locRtnStsCd="01"; locRtnRspCode="R0170";/*locRtnMsg=getText("feedbck.update.error");*/locRtnMsg=mobiCommon.getMsg("R0170");
							AuditTrial.toWriteAudit(getText("FBKAD006"), "FBKAD006", ivrEntryByusrid);
						}
						log.logMessage("Step 5 : Feedback update Process End.", "info", FeedbackUpdate.class);
						
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){// select Feedback
						
						log.logMessage("Step 1 : Feedback singleSelect will start.", "info", FeedbackUpdate.class);
						JSONObject locRspjson=FeedbackUtility.toselctFeedbacksingle(locObjRecvdataJson);					
						locvrFnrst =(String) Commonutility.toHasChkJsonRtnValObj(locRspjson,"catch");											
						if(locvrFnrst==null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
							locObjRspdataJson=locRspjson;
							locRtnSrvId="SI5003";locRtnStsCd="00"; locRtnRspCode="R0181";/*locRtnMsg=getText("feedbck.select.success");*/locRtnMsg=mobiCommon.getMsg("R0181");
							AuditTrial.toWriteAudit(getText("FBKAD007"), "FBKAD007", ivrEntryByusrid);
							
						}else{
							locObjRspdataJson=new JSONObject();
							locRtnSrvId="SI5003";locRtnStsCd="01"; locRtnRspCode="R0182";/*locRtnMsg=getText("feedbck.select.error");*/locRtnMsg=mobiCommon.getMsg("R0182");
							AuditTrial.toWriteAudit(getText("FBKAD008"), "FBKAD008", ivrEntryByusrid);
														
						}
						log.logMessage("Step 4 : Feedback singleSelect Process End.", "info", FeedbackUpdate.class);
					
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// Deactive Feedback
						
						log.logMessage("Step 1 : Feedback deactive will start.", "info", FeedbackUpdate.class);
						locvrFnrst=FeedbackUtility.toDeactiveFeedback(locObjRecvdataJson);
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
							
							locRtnSrvId="SI5004";locRtnStsCd="00"; locRtnRspCode="R0183";/*locRtnMsg=getText("feedbck.deactivate.success");*/locRtnMsg=mobiCommon.getMsg("R0183");
							AuditTrial.toWriteAudit(getText("FBKAD009"), "FBKAD009", ivrEntryByusrid);
						}else{
							
							locRtnSrvId="SI5004";locRtnStsCd="01"; locRtnRspCode="R0184";/*locRtnMsg=getText("feedbck.deactivate.error");*/locRtnMsg=mobiCommon.getMsg("R0184");
							AuditTrial.toWriteAudit(getText("FBKAD010"), "FBKAD010", ivrEntryByusrid);
						}
						log.logMessage("Step 4 : Feedback deactive Process End.", "info", FeedbackUpdate.class);
					
					}else{
						locvrFnrst="";
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI5001, "+getText("service.notmach"), "info", FeedbackUpdate.class);
						locRtnSrvId="SI5001";locRtnStsCd="01"; locRtnRspCode="SE5001"; /*locRtnMsg=getText("service.notmach");	*/locRtnMsg=mobiCommon.getMsg("R0111");
					}
					serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI5001,"+getText("request.format.notmach")+"", "info", FeedbackUpdate.class);
					serverResponse("SI5001","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI5001,"+getText("request.values.empty")+"", "info", FeedbackUpdate.class);
				serverResponse("SI5001","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);

			}	
			
		}catch(Exception e){
			System.out.println("Exception found FeedbackUpdate.class execute() Method : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI5001, Sorry, an unhandled error occurred", "error", FeedbackUpdate.class);
			serverResponse("SI5001","02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
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

	public String getIvrservicefor() {
		return ivrservicefor;
	}

	public void setIvrservicefor(String ivrservicefor) {
		this.ivrservicefor = ivrservicefor;
	}
	
	
	
}
