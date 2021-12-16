package com.pack.Committeelist;

import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.categorylistvo.persistance.CategoryDao;
import com.pack.categorylistvo.persistance.CategoryDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.siservices.committeeMgmt.persistense.CommittteeRoleMstTbl;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Committeeviewall extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute(){
		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		Session locObjsession = null;		
		String ivrservicecode=null,ivrcurrntusridStr=null;
		int ivrCurrntusrid=0;
		try{
			locObjsession = HibernateUtil.getSession();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");														
				ivrcurrntusridStr =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
				if(ivrcurrntusridStr!=null && Commonutility.toCheckisNumeric(ivrcurrntusridStr)){
					ivrCurrntusrid= Integer.parseInt(ivrcurrntusridStr);
				}else{
					ivrCurrntusrid=0;
				}
				locObjRspdataJson=tocommitteeSelectAll(locObjRecvdataJson,locObjsession);	
				String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						AuditTrial.toWriteAudit(getText("CMTRLAD014"), "CMTRLAD014", ivrCurrntusrid);
						serverResponse("SI16000","0","E16000",getText("committeerole.selectall.error"),locObjRspdataJson);
					}else{
						AuditTrial.toWriteAudit(getText("CMTRLAD013"), "CMTRLAD013", ivrCurrntusrid);
						serverResponse("SI16000","0","S16000",getText("committeerole.selectall.success"),locObjRspdataJson);					
					}
				
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI16000, "+getText("request.format.notmach"), "info", Committeeviewall.class);
					serverResponse("SI16000","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);

				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI16000, "+getText("request.values.empty"), "info", Committeeviewall.class);
				serverResponse("SI16000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI16000, "+getText("catch.error"), "error", Committeeviewall.class);
			serverResponse("SI16000","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	}
	
	public static JSONObject tocommitteeSelectAll(JSONObject pDataJson, Session pSession) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;
		
		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrIDCard_cntQry=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null;	
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		CommittteeRoleMstTbl lbrDtlVoObj=null;
		int count=0,countFilter=0, startrowfrom=1, totalNorow=10;		
		try {
			log=new Log();	lbrDtlVoObj=new CommittteeRoleMstTbl();			
			System.out.println("Step 1 : Committee Role  select all block enter");
			log.logMessage("Step 1 : Committee Role  select all block enter", "info", Committeeviewall.class);			
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "status");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web 
				if(loctocheNull.equalsIgnoreCase(""))
				{
					locvrIDCard_cntQry="select count(roleId) from CommittteeRoleMstTbl where status in(0,1)";
				}
				else
				{
					locvrIDCard_cntQry="select count(roleId) from CommittteeRoleMstTbl where status in(0,1) AND (" + "roleName like ('%" + loctocheNull+ "%')  " 
			                                + ")";
				}
				System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+locvrIDCard_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrIDCard_cntQry, "info", Committeeviewall.class);
				
				CategoryDao IdcardDaoObj=new CategoryDaoservice();
				count=IdcardDaoObj.getInitTotal(locvrIDCard_cntQry);
				countFilter=IdcardDaoObj.getTotalFilter(locvrIDCard_cntQry);
			}else{ // for mobile
				 count=0;countFilter=0;
				 System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", Committeeviewall.class);
			}
			
				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}
				
				String globalsearch=null;
				String lvrOrdby =" order by roleId desc";
			if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				  globalsearch = " AND (" + "status like ('%" + loctocheNull+ "%') or " 
			                                + "roleName like ('%" + loctocheNull + "%'))";
				  loc_slQry="from CommittteeRoleMstTbl  where status in (0,1) " +globalsearch + " " +lvrOrdby;
			}else{
				loc_slQry="from CommittteeRoleMstTbl  where status in (0,1) " + lvrOrdby;	
			}				
			//loc_slQry="from LaborDetailsTblVO  where ivrBnLBR_STS="+locvrLBR_STS+"";	
			System.out.println("Step 3 : Committee Role  Details Query : "+loc_slQry);
			log.logMessage("Step 3 : Committee Role  Details Query : "+loc_slQry, "info", Committeeviewall.class);
			locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();			
					
			locLBRJSONAryobj=new JSONArray();
			while ( locObjLbrlst_itr.hasNext() ) {
				locInrJSONObj=new JSONObject();
				lbrDtlVoObj = (CommittteeRoleMstTbl) locObjLbrlst_itr.next();								
				locInrJSONObj.put("committeerole_id",lbrDtlVoObj.getRoleId());	
				locInrJSONObj.put("committeerole_name",lbrDtlVoObj.getRoleName());
				locInrJSONObj.put("committeerole_status",lbrDtlVoObj.getStatus());									
				locLBRJSONAryobj.put(locInrJSONObj);
				locInrJSONObj=null; 
			}	
			System.out.println("Step 3 : Return JSON Array DATA : "+locLBRJSONAryobj);				
			locFinalRTNObj=new JSONObject();	
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
			locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
			locFinalRTNObj.put("committeedetails", locLBRJSONAryobj);			
			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			System.out.println("Step 6 : Select category detail block end.");
			log.logMessage("Step 4: Select category detail block end.", "info", Committeeviewall.class);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			System.out.println("Exception in toCMPYSelectAll() : "+e);
			log.logMessage("Step -1 : category select all block Exception : "+e, "error", Committeeviewall.class);	
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("committeedetails", "");
			locFinalRTNObj.put("CatchBlock", "Error");
			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {
			locStrRow=null;locMaxrow=null;
			lbrDtlVoObj=null;log=null;locObjLbrlst_itr=null;locFinalRTNObj=null;locInrJSONObj=null;locLBRJSONAryobj=null;

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
	
}
