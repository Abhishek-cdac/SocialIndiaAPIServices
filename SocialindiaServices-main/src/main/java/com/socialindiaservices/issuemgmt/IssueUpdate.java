package com.socialindiaservices.issuemgmt;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.issuemgmt.persistence.IssueTblVO;
import com.socialindiaservices.vo.MerchantIssueTblVO;

public class IssueUpdate extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicefor;
	
	public String execute(){
		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json	
		String ivrCurntusridstr=null;  
		String ivrDecissBlkflag=null; // 1- new create, 2- update, 3 - select single[], 4 - deActive , 5 - delete , 6 - Block
		int ivrEntryByusrid=0;
		Session locObjsession = null;
		try{
			locObjsession = HibernateUtil.getSession();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrCurntusridstr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"crntusrloginid");
					System.out.println("ivrCurntusridstr --------------- "+ivrCurntusridstr);
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
						locvrFnrst = IssueUtility.toInsertIssue(locObjRecvdataJson);
						String locSbt[]=locvrFnrst.split("!_!");	
						System.out.println(locSbt[0]);
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI350001";locRtnStsCd="0"; locRtnRspCode="S350001";locRtnMsg=getText("issue.insert.success");
								AuditTrial.toWriteAudit(getText("ISSUE001"), "ISSUE001", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI350001";locRtnStsCd="1"; locRtnRspCode="E350001";locRtnMsg=getText("issue.insert.error");
								AuditTrial.toWriteAudit(getText("ISSUEOO2"), "ISSUEOO2", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI350001";locRtnStsCd="1"; locRtnRspCode="E350001";locRtnMsg=getText("issue.insert.error");
							AuditTrial.toWriteAudit(getText("ISSUE002"), "ISSUE002", ivrEntryByusrid);
						}
						log.logMessage("Step 5 : Issue list Insert process end", "info", IssueUpdate.class);
						
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){
						locObjRspdataJson =toIssueselect(locObjRecvdataJson,locObjsession);
						serverResponse("SI350001","1","S350001",getText("request.values.empty"),locObjRspdataJson);
						/*String locSbt[]=locvrFnrst1.split("!_!");	
						System.out.println(locSbt[0]);
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI330004";locRtnStsCd="0"; locRtnRspCode="S330004";locRtnMsg=getText("issue.update.success");
								AuditTrial.toWriteAudit(getText("ISSUE005"), "ISSUE005", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI350004";locRtnStsCd="1"; locRtnRspCode="E330004";locRtnMsg=getText("issue.update.error");
								AuditTrial.toWriteAudit(getText("ISSUE006"), "ISSUE006", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI350001";locRtnStsCd="1"; locRtnRspCode="E330004";locRtnMsg=getText("issue.update.error");
							AuditTrial.toWriteAudit(getText("ISSUEOO6"), "ISSUEOO6", ivrEntryByusrid);
						}*/
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){
						locvrFnrst = IssueUtility.toupdateAction(locObjRecvdataJson);
						String locSbt[]=locvrFnrst.split("!_!");	
						System.out.println(locSbt[0]);
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI330004";locRtnStsCd="0"; locRtnRspCode="S330004";locRtnMsg=getText("issue.update.success");
								AuditTrial.toWriteAudit(getText("ISSUE005"), "ISSUE005", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI350004";locRtnStsCd="1"; locRtnRspCode="E330004";locRtnMsg=getText("issue.update.error");
								AuditTrial.toWriteAudit(getText("ISSUE006"), "ISSUE006", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI350001";locRtnStsCd="1"; locRtnRspCode="E330004";locRtnMsg=getText("issue.update.error");
							AuditTrial.toWriteAudit(getText("ISSUE006"), "ISSUE006", ivrEntryByusrid);
						}
					}else{
						locvrFnrst="";
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI330001, "+getText("service.notmach"), "info", IssueUpdate.class);
						locRtnSrvId="SI350001";locRtnStsCd="1"; locRtnRspCode="SE350001"; locRtnMsg=getText("service.notmach");	
					}
					serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI350001,"+getText("request.format.notmach")+"", "info", IssueUpdate.class);
					serverResponse("SI350001","1","EF350001",getText("request.format.notmach"),locObjRspdataJson);
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI350001,"+getText("request.values.empty")+"", "info", IssueUpdate.class);
				serverResponse("SI350001","1","ER350001",getText("request.values.empty"),locObjRspdataJson);

			}	
			
		}catch(Exception e){
			System.out.println("Exception found FunctionupdateAll.class execute() Method : " + e);
			AuditTrial.toWriteAudit(getText("ISSUE0015"), "ISSUE0015", ivrEntryByusrid);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI330001, Sorry, an unhandled error occurred", "error", IssueUpdate.class);
			serverResponse("SI330001","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
		
	}
	private JSONObject toIssueselect(JSONObject praDatajson, Session praSession){	
		JSONObject lvrRtnjsonobj = null;
		JSONObject lvrInrJSONObj = null;
		JSONObject lvrInrJSONObj1 = null;	
		JSONArray lvrEventjsonaryobj = null;
		Log logWrite = null;
		Iterator<Object> lvrObjeventlstitr = null;
		List<Object> lvrObjfunctionlstitr = null;
		IssueTblVO lvrEvnttblvoobj = null;
		String lvrSlqry = null;
		String lvrfunSlqry="",mrchntId=null,groupflg=null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	    try {
	    	mrchntId  = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "mrchntId");
	    	groupflg  = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "grouptxtflg");
	    	// Merchant Group code insert into mvp_merchant_issue_tbl
			GroupMasterTblVo lvrGrpmstobj = null, lvrGrpmstIDobj = null;
			String pGrpName=null;
			if(groupflg.equalsIgnoreCase("labor")){
			pGrpName = getText("Grp.labor");
			}else{
				pGrpName = getText("Grp.merchant");
			}
			 // Select Group Code on Merchant
			String locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"')";
			Query locQryrst=praSession.createQuery(locSlqry);
			lvrGrpmstobj = (GroupMasterTblVo) locQryrst.uniqueResult();
			 if(lvrGrpmstobj!=null){
				 lvrGrpmstIDobj = new GroupMasterTblVo();
				 lvrGrpmstIDobj.setGroupCode(lvrGrpmstobj.getGroupCode());								 
			 }else {
				 lvrGrpmstIDobj = null;
			 }
			lvrSlqry = "from IssueTblVO where status=1";
			lvrObjeventlstitr=praSession.createQuery(lvrSlqry).list().iterator();
			lvrEventjsonaryobj = new JSONArray();
			while (lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				lvrEvnttblvoobj = (IssueTblVO) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("issueid", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIssueId()));
				lvrInrJSONObj.put("issdesc", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIssueList()));
				lvrfunSlqry = "select issueId,description from MerchantIssueTblVO where mcrctLaborId="+Integer.parseInt(mrchntId)+" and ivrGrpcodeobj="+lvrGrpmstobj.getGroupCode();
				lvrObjfunctionlstitr=praSession.createQuery(lvrfunSlqry).list();
				lvrInrJSONObj.put("issuedesc", lvrObjfunctionlstitr);
				lvrEventjsonaryobj.put(lvrInrJSONObj);
				lvrInrJSONObj=null;
			}
			
			lvrRtnjsonobj=new JSONObject();	
			lvrRtnjsonobj.put("issuedetails", lvrEventjsonaryobj);
			System.out.println("Step 4 : Select toIssueselect process End " +lvrRtnjsonobj);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in toIssueselect() : "+expObj);
				logWrite.logMessage("Step -1 : toIssueselect select all block Exception : "+expObj, "error", IssueUpdate.class);	
				lvrRtnjsonobj=new JSONObject();
				lvrRtnjsonobj.put("CatchBlock", "Error");
				System.out.println("lvrRtnjsonobj : "+lvrRtnjsonobj);
				}catch(Exception ee){}finally{}
	     return lvrRtnjsonobj;
	    }finally {
	    	lvrRtnjsonobj = null;
	    	lvrInrJSONObj = null;	
	    	lvrEventjsonaryobj = null;
	    	logWrite = null;
	    	lvrObjeventlstitr = null;
	    	lvrEvnttblvoobj = null;
	    }
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
