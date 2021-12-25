package com.socialindiaservices.function;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.FunctionMasterTblVO;

public class FunctionupdateAll extends ActionSupport{
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
						locvrFnrst = FunctionUtility.toInsertFunction(locObjRecvdataJson);
						String locSbt[]=locvrFnrst.split("!_!");	
						System.out.println(locSbt[0]);
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI330001";locRtnStsCd="0"; locRtnRspCode="S330001";locRtnMsg=getText("function.insert.success");
								AuditTrial.toWriteAudit(getText("FUNAD001"), "FUNAD001", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI330001";locRtnStsCd="1"; locRtnRspCode="E330001";locRtnMsg=getText("function.insert.error");
								AuditTrial.toWriteAudit(getText("FUNAD002"), "FUNAD002", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI330001";locRtnStsCd="1"; locRtnRspCode="E330001";locRtnMsg=getText("function.insert.error");
							AuditTrial.toWriteAudit(getText("FUNAD002"), "FUNAD002", ivrEntryByusrid);
						}
						log.logMessage("Step 5 : function Insert process end", "info", FunctionupdateAll.class);
						
					} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){// insert Feedback
						locvrFnrst = FunctionUtility.toUpdateaction(locObjRecvdataJson);
						String locSbt[]=locvrFnrst.split("!_!");	
						System.out.println(locSbt[0]);
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI330001";locRtnStsCd="0"; locRtnRspCode="S330001";locRtnMsg=getText("function.update.success");
								AuditTrial.toWriteAudit(getText("FUNAD003"), "FUNAD003", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI330002";locRtnStsCd="1"; locRtnRspCode="E330002";locRtnMsg=getText("function.update.error");
								AuditTrial.toWriteAudit(getText("FUNAD004"), "FUNAD004", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI330004";locRtnStsCd="1"; locRtnRspCode="E330004";locRtnMsg=getText("function.update.error");
							AuditTrial.toWriteAudit(getText("FUNAD004"), "FUNAD004", ivrEntryByusrid);
						}
						log.logMessage("Step 5 : function Insert process end", "info", FunctionupdateAll.class);
						
					} else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){
						locObjRspdataJson =toFunctionselect(locObjRecvdataJson,locObjsession);
						String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
		    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
		    				serverResponse("SI330003","0","E330003",getText("function.selectall.error"),locObjRspdataJson);
		    			}else{
		    				serverResponse("SI330003","0","S330003",getText("function.selectall.success"),locObjRspdataJson);					
		    			}
						
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){
						locvrFnrst = FunctionUtility.toDeactiveFunction(locObjRecvdataJson);
						String locSbt[]=locvrFnrst.split("!_!");	
						System.out.println(locSbt[0]);
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1")&& !locSbt[0].equalsIgnoreCase("0")){
								locRtnSrvId="SI330004";locRtnStsCd="0"; locRtnRspCode="S330004";locRtnMsg=getText("function.deactive.success");
								AuditTrial.toWriteAudit(getText("FUNAD005"), "FUNAD005", ivrEntryByusrid);
							}else{
								locRtnSrvId="SI330004";locRtnStsCd="1"; locRtnRspCode="E330004";locRtnMsg=getText("function.deactive.error");
								AuditTrial.toWriteAudit(getText("FUNAD006"), "FUNAD006", ivrEntryByusrid);
							}
						}else{
							locRtnSrvId="SI330001";locRtnStsCd="1"; locRtnRspCode="E330004";locRtnMsg=getText("function.deactive.error");
							AuditTrial.toWriteAudit(getText("FUNAD006"), "FUNAD006", ivrEntryByusrid);
						}
					}else{
						locvrFnrst="";
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI330001, "+getText("service.notmach"), "info", FunctionupdateAll.class);
						locRtnSrvId="SI330001";locRtnStsCd="1"; locRtnRspCode="SE330001"; locRtnMsg=getText("service.notmach");	
					}
					serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI330001,"+getText("request.format.notmach")+"", "info", FunctionupdateAll.class);
					serverResponse("SI330001","1","EF330001",getText("request.format.notmach"),locObjRspdataJson);
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI5001,"+getText("request.values.empty")+"", "info", FunctionupdateAll.class);
				serverResponse("SI330001","1","ER3300001",getText("request.values.empty"),locObjRspdataJson);

			}	
			
		}catch(Exception e){
			System.out.println("Exception found FunctionupdateAll.class execute() Method : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI330001, Sorry, an unhandled error occurred", "error", FunctionupdateAll.class);
			serverResponse("SI330001","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
		
	}
	
	private JSONObject toFunctionselect(JSONObject praDatajson, Session praSession){	
		JSONObject lvrRtnjsonobj = null;
		JSONObject lvrInrJSONObj = null;	
		JSONArray lvrEventjsonaryobj = null;
		Log logWrite = null;
		Iterator<Object> lvrObjeventlstitr = null;
		List<Object> lvrObjfunctionlstitr = null;
		FunctionMasterTblVO lvrEvnttblvoobj = null;
		String lvrSlqry = null;
		String lvrfunSlqry="",functionid=null;
	    try {

			functionid  = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "functionid");
			lvrSlqry = "from FunctionMasterTblVO where functionId="+Integer.parseInt(functionid);
			lvrObjeventlstitr=praSession.createQuery(lvrSlqry).list().iterator();
			lvrEventjsonaryobj = new JSONArray();
			while (lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				lvrEvnttblvoobj = (FunctionMasterTblVO) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("functionid", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getFunctionId()));
				lvrInrJSONObj.put("functionname", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getFunctionName()));
				lvrInrJSONObj.put("status", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getStatusFlag()));
				lvrInrJSONObj.put("functiontype",  Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getIvrBnobjEVENT_TYPE()));
				lvrfunSlqry = "select functionTempId,templateText from FunctionTemplateTblVO where functionId="+lvrEvnttblvoobj.getFunctionId();
				lvrObjfunctionlstitr=praSession.createQuery(lvrfunSlqry).list();
				lvrInrJSONObj.put("eventemp", lvrObjfunctionlstitr);	
				lvrEventjsonaryobj.put(lvrInrJSONObj);
			}
			lvrRtnjsonobj=new JSONObject();	
			lvrRtnjsonobj.put("functiondetails", lvrEventjsonaryobj);
			System.out.println("Step 4 : Select Function process End " +lvrRtnjsonobj);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in toFunctionselect() : "+expObj);
				logWrite.logMessage("Step -1 : Function select all block Exception : "+expObj, "error", FunctionupdateAll.class);	
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
