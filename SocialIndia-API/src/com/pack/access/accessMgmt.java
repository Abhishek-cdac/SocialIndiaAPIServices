package com.pack.access;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.access.persistance.AccessDaoservice;
import com.pack.accessvo.AccessDetailsVO;
import com.pack.company.CompanyDetailsview;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class accessMgmt extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	
	public String execute(){		
		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		Session locObjsession = null;		
		String ivrcurrntusridStr=null;
		try{			
			locObjsession = HibernateUtil.getSession();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");														
				ivrcurrntusridStr =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
				if(ivrcurrntusridStr!=null && Commonutility.toCheckisNumeric(ivrcurrntusridStr)){
				}else{
				}
				locObjRspdataJson=toAccessSelectAll(locObjRecvdataJson,locObjsession);	
				String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						serverResponse("SI10002","0","E10002",getText("access.selectall.error"),locObjRspdataJson);
					}else{
						serverResponse("SI10002","0","S10002",getText("access.selectall.success"),locObjRspdataJson);					
					}
				
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI10002, "+getText("request.format.notmach"), "info", accessMgmt.class);
					serverResponse("SI10002","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI10002, "+getText("request.values.empty"), "info", accessMgmt.class);
				serverResponse("SI10002","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			Commonutility.toWriteConsole("Exception found .class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI10002, "+getText("catch.error"), "error", accessMgmt.class);
			serverResponse("SI10002","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	}
	public static JSONObject toAccessSelectAll(JSONObject pDataJson, Session pSession) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locACSJSONAryobj=null;
		
		String locvrCntflg=null,locvrFlterflg=null,locvrLBR_cntQry=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null;	
		String selectfield=null,srchval=null,srchflg=null;
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		AccessDetailsVO accessDtlVoObj=null;
		int count=0,countFilter=0, startrowfrom=1, totalNorow=10;		
		try {
			log=new Log();	
			accessDtlVoObj=new AccessDetailsVO();			
			Commonutility.toWriteConsole("Step 1 : company select all block enter");
			log.logMessage("Step 1 : company select all block enter", "info", CompanyDetailsview.class);			
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			
			selectfield=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "searchfield");
			srchval=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "usersearchname");
			srchflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "searchval");
			
			
			
				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}
				String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
				String globalsearch=null;
				String locOrderby ="order by client desc";
			if(srchflg.equalsIgnoreCase("")){
				if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
					globalsearch = " (" + "usrId.firstName like ('%" + loctocheNull+ "%') or " 
			                                + "ipaddress like ('%" + loctocheNull + "%')  or "			                               
			                                + "accessCount like ('%" + loctocheNull + "%')  or "
			                                 + "language like ('%" + loctocheNull + "%')  or "
			                                + "entryDate like ('%" + loctocheNull + "%')  or "
			                                + " gmtTime like ('%" + loctocheNull + "%'))";
				  	loc_slQry="from AccessDetailsVO  where " +globalsearch +" "+locOrderby;
				}else{
					loc_slQry="from AccessDetailsVO "+locOrderby;
				}
			} else {
				if(selectfield.equalsIgnoreCase("1")) {
					 globalsearch = " (" + "usrId.firstName like ('%" + srchval+ "%') or " 
                             + "ipaddress like ('%" + srchval + "%')  or "			                               
                             + "accessCount like ('%" + srchval + "%')  or "
                             + "language like ('%" + srchval + "%')  or "
                             + "entryDate like ('%" + srchval + "%')  or "
                             + "modifyDate like ('%" + srchval + "%')  or "
                             + " gmtTime like ('%" + srchval + "%'))";
					 loc_slQry="from AccessDetailsVO  where " +globalsearch +" "+locOrderby;
				} else if (selectfield.equalsIgnoreCase("2")){
					 globalsearch = " (" + "usrId.firstName like ('%" + srchval+ "%') )";
					 loc_slQry="from AccessDetailsVO  where " +globalsearch +" "+locOrderby;
				} else if (selectfield.equalsIgnoreCase("3")){
					 globalsearch = " (" + "ipaddress like ('%" + srchval+ "%')) ";
					 loc_slQry="from AccessDetailsVO  where " +globalsearch +" "+locOrderby;
				}else if(selectfield.equalsIgnoreCase("4")){
					 globalsearch = " (" + "accessCount like ('%" + srchval+ "%') )";
					 loc_slQry="from AccessDetailsVO  where " +globalsearch +" "+locOrderby;
				}else if(selectfield.equalsIgnoreCase("5")){
					 globalsearch = " (" + "language like ('%" + srchval+ "%')) ";
					 loc_slQry="from AccessDetailsVO  where " +globalsearch +" "+locOrderby;
				}
			}
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web 
				locvrLBR_cntQry="select count(id) " + loc_slQry;
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrLBR_cntQry, "info", accessMgmt.class);
				
				AccessDaoservice locaccessObj=new AccessDaoservice();
				count = locaccessObj.getInitTotal(locvrLBR_cntQry);
				countFilter=count;
			}else{ // for mobile
				 count=0;countFilter=0;
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", accessMgmt.class);
			}
			Commonutility.toWriteConsole("Step 3 : Access Management Details Query : "+loc_slQry);
			log.logMessage("Step 3 : Access Management Details Query : "+loc_slQry, "info", accessMgmt.class);
			locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");			
			locACSJSONAryobj=new JSONArray();
			while ( locObjLbrlst_itr.hasNext() ) {			
				locInrJSONObj=new JSONObject();
				accessDtlVoObj = (AccessDetailsVO) locObjLbrlst_itr.next();
				if(accessDtlVoObj.getUsrId()!=null){
					if(accessDtlVoObj.getUsrId().getFirstName()!=null){
						locInrJSONObj.put("usrId", Commonutility.toCheckNullEmpty(accessDtlVoObj.getUsrId().getFirstName()));	
					} else {
						locInrJSONObj.put("usrId",Commonutility.toCheckNullEmpty(accessDtlVoObj.getUsrId().getMobileNo()));
					}
				}else{
					locInrJSONObj.put("usrId","");
				}
				
				locInrJSONObj.put("ipAddress", accessDtlVoObj.getIpaddress());
				locInrJSONObj.put("accessCount", accessDtlVoObj.getAccessCount());
				locInrJSONObj.put("hostName", accessDtlVoObj.getHostName());
				locInrJSONObj.put("portNo", accessDtlVoObj.getPortNo());
				locInrJSONObj.put("protocolType", accessDtlVoObj.getProtocolType());
				locInrJSONObj.put("methoType", accessDtlVoObj.getMethodType());
				locInrJSONObj.put("countryName", accessDtlVoObj.getCountryName());
				locInrJSONObj.put("language", accessDtlVoObj.getLanguage());
				String entrydate = simpleDateFormat.format(accessDtlVoObj.getClient()); 
				String gmtTime = simpleDateFormat.format(accessDtlVoObj.getGmtTime()); 
				String updateTime = simpleDateFormat.format(accessDtlVoObj.getModifyDate()); 
				locInrJSONObj.put("entryTime", entrydate);
				locInrJSONObj.put("gmtTime", gmtTime);
				locInrJSONObj.put("updateTime", updateTime );
				locACSJSONAryobj.put(locInrJSONObj);
			}	
			
			locInrJSONObj=null; 
			Commonutility.toWriteConsole("Step 3 : Return JSON Array DATA : "+locACSJSONAryobj);				
			locFinalRTNObj=new JSONObject();	
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
			locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
			locFinalRTNObj.put("accessdetails", locACSJSONAryobj);			
			log.logMessage("Step 4: Select access detail block end.", "info", accessMgmt.class);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			Commonutility.toWriteConsole("Exception in toAccessSelectAll() : "+e);
			log.logMessage("Step -1 : toAccess select all block Exception : "+e, "error", accessMgmt.class);	
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("accessdetails", "");
			locFinalRTNObj.put("CatchBlock", "Error");
			Commonutility.toWriteConsole("locFinalRTNObj : "+locFinalRTNObj);
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {
			locStrRow=null;locMaxrow=null;
			accessDtlVoObj=null;log=null;locObjLbrlst_itr=null;locFinalRTNObj=null;locInrJSONObj=null;locACSJSONAryobj=null;
		}
	}

	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson) {

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
