package com.pack.DisputeMerchantlist;

import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.DisputeMerchantlistvo.persistance.DisputemerchantDao;
import com.pack.DisputeMerchantlistvo.persistance.DisputemerchantDaoservice;
import com.pack.Worktypelistvo.persistance.WorktypeDao;
import com.pack.Worktypelistvo.persistance.WorktypeDaoservice;
import com.pack.audittrial.AuditTrial;
import com.pack.commonvo.LaborWrkTypMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.DisputeRiseTbl;
import com.socialindiaservices.vo.MerchantTblVO;

public class DisputeMerchantlistviewall extends ActionSupport {

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
				locObjRspdataJson=toDisputeMerchantlistSelectAll(locObjRecvdataJson,locObjsession,getText("Grp.merchant"));	
				String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						AuditTrial.toWriteAudit(getText("DISPMRAD014"), "DISPMRAD014", ivrCurrntusrid);
						serverResponse("SI30000","0","E30000",getText("disputemerchant.selectall.error"),locObjRspdataJson);
					}else{
						AuditTrial.toWriteAudit(getText("DISPMRAD013"), "DISPMRAD013", ivrCurrntusrid);
						serverResponse("SI30000","0","S30000",getText("disputemerchant.selectall.success"),locObjRspdataJson);					
					}
				
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI30000, "+getText("request.format.notmach"), "info", DisputeMerchantlistviewall.class);
					serverResponse("SI30000","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);

				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI30000, "+getText("request.values.empty"), "info", DisputeMerchantlistviewall.class);
				serverResponse("SI30000","1","ER0001",getText("request.values.empty"),locObjRspdataJson);

			}	
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI30000, "+getText("catch.error"), "error", DisputeMerchantlistviewall.class);
			serverResponse("SI30000","2","ER0002",getText("catch.error"),locObjRspdataJson);
		}finally{
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
		}				
		return SUCCESS;
	}
	
	public static JSONObject toDisputeMerchantlistSelectAll(JSONObject pDataJson, Session pSession,String pGrpName) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;
		
		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrEdu_cntQry=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null;	
		String locSlqry=null,locMrchSLqry=null,dispute_tomerchname=null;
		Query locQryrst=null,locMrchQryrst=null;
		GroupMasterTblVo locGrpMstrVOobj=null,locGRPMstrvo=null;
		MerchantTblVO locMrchMstrVOobj=null,locMrchMstrvo=null;
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		DisputeRiseTbl lbrDtlVoObj=null;
		int count=0,countFilter=0, startrowfrom=1, totalNorow=10;		
		try {
			log=new Log();	lbrDtlVoObj=new DisputeRiseTbl();			
			System.out.println("Step 1 : DisputeMerchant Type  select all block enter");
			log.logMessage("Step 1 : DisputeMerchant Type  select all block enter", "info", DisputeMerchantlistviewall.class);			
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "status");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			String societyId = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "societyid");
			int locGrpcode=0;
			//Group code get 
			 locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"') or groupName=upper('"+pGrpName+"')";			 
			 locQryrst=pSession.createQuery(locSlqry);			
			 locGrpMstrVOobj=(GroupMasterTblVo) locQryrst.uniqueResult();
			 if(locGrpMstrVOobj!=null){
				 locGRPMstrvo=new GroupMasterTblVo();
				 locGrpcode=locGrpMstrVOobj.getGroupCode();
								 
			 }else{// new group create
				 uamDao uam=new uamDaoServices();
				  locGrpcode=uam.createnewgroup_rtnId(pGrpName);
				 		 				
			 }		
			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web 
				if(loctocheNull.equalsIgnoreCase(""))
				{
					if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {	//admin societyid -1
					locvrEdu_cntQry="select count(disputeId) from DisputeRiseTbl where status =1 and disputeT0Groupid ="+locGrpcode+" ";
					}
					else
					{
						locvrEdu_cntQry="select count(disputeId) from DisputeRiseTbl where status =1 and disputeT0Groupid ="+locGrpcode+" and usrRegTbl.societyId.societyId = "+societyId+" ";
					}
				}
				else
				{
					if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {	//admin societyid -1
					locvrEdu_cntQry="select count(disputeId) from DisputeRiseTbl where status =1 and disputeT0Groupid ="+locGrpcode+" AND (" + " usrRegTbl.societyId.societyName like ('%" + loctocheNull + "%') or "
							+ "disputeTitle like ('%" + loctocheNull+ "%') or " 
							+ " usrRegTbl.userName like ('%" + loctocheNull+ "%')  " 
			                                + ")";
					}
					else
					{
						locvrEdu_cntQry="select count(disputeId) from DisputeRiseTbl where status =1 and disputeT0Groupid ="+locGrpcode+" AND (" + " usrRegTbl.societyId.societyName like ('%" + loctocheNull + "%') or "
								+ "disputeTitle like ('%" + loctocheNull+ "%') or " 
								+ " usrRegTbl.userName like ('%" + loctocheNull+ "%')  " 
				                                + ")and usrRegTbl.societyId.societyId = "+societyId;
					}
						
				}
				System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+locvrEdu_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrEdu_cntQry, "info", DisputeMerchantlistviewall.class);
				
				DisputemerchantDao IdcardDaoObj=new DisputemerchantDaoservice();
				count=IdcardDaoObj.getInitTotal(locvrEdu_cntQry);
				countFilter=IdcardDaoObj.getTotalFilter(locvrEdu_cntQry);
			}else{ // for mobile
				 count=0;countFilter=0;
				 System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", DisputeMerchantlistviewall.class);
			}
			
				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}
				
				String globalsearch=null;
			if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
				  globalsearch = " AND (" + " usrRegTbl.societyId.societyName like ('%" + loctocheNull + "%') or "
							+ "disputeTitle like ('%" + loctocheNull+ "%') or " 
							+ " usrRegTbl.userName like ('%" + loctocheNull+ "%') " 
			                                + ")";
				 
				}
				else
				{
					globalsearch = " AND (" + " usrRegTbl.societyId.societyName like ('%" + loctocheNull + "%') or "
							+ "disputeTitle like ('%" + loctocheNull+ "%') or " 
							+ " usrRegTbl.userName like ('%" + loctocheNull+ "%') " 
			                                + ") and usrRegTbl.societyId.societyId = "+societyId;
				}
				 loc_slQry="from DisputeRiseTbl  where status =1 and disputeT0Groupid ="+locGrpcode+" " +globalsearch;
			}else{
				if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
					loc_slQry="from DisputeRiseTbl  where status =1 and disputeT0Groupid ="+locGrpcode+" ";	
				}
				else
				{
					loc_slQry="from DisputeRiseTbl  where status =1 and usrRegTbl.societyId.societyId = "+societyId+" and disputeT0Groupid ="+locGrpcode+" ";	
				}
				
			}				
			//loc_slQry="from LaborDetailsTblVO  where ivrBnLBR_STS="+locvrLBR_STS+"";	
			System.out.println("Step 3 : DisputeMerchant Type  Details Query : "+loc_slQry);
			log.logMessage("Step 3 : DisputeMerchant Type  Details Query : "+loc_slQry, "info", DisputeMerchantlistviewall.class);
			locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();								
			locLBRJSONAryobj=new JSONArray();
			while ( locObjLbrlst_itr.hasNext() ) {
				locInrJSONObj=new JSONObject();
				lbrDtlVoObj = (DisputeRiseTbl) locObjLbrlst_itr.next();								
				locInrJSONObj.put("disp_id",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getDisputeId()));	
				locInrJSONObj.put("disp_usrid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getUsrRegTbl().getUserId()));
				if(lbrDtlVoObj.getUsrRegTbl()!=null) {
					locInrJSONObj.put("disp_usrname",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getUsrRegTbl().getFirstName() +" "+ lbrDtlVoObj.getUsrRegTbl().getLastName()));
				} else {
					locInrJSONObj.put("disp_usrname","");
				}
				
				if(lbrDtlVoObj.getUsrRegTbl().getSocietyId()!=null){
					locInrJSONObj.put("disp_societyname",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getUsrRegTbl().getSocietyId().getSocietyName()));
					locInrJSONObj.put("disp_townshipname",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getUsrRegTbl().getSocietyId().getTownshipId().getTownshipName()));
					
					}
					else
					{
						locInrJSONObj.put("disp_societyname","");
						locInrJSONObj.put("disp_townshipname","");
					}
				locInrJSONObj.put("disp_desc",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getDisputeDesc()));
				locInrJSONObj.put("disp_title",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getDisputeTitle()));
				locInrJSONObj.put("disp_toid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getDisputeT0Id()));
				String merchrentid =Commonutility.toCheckNullEmpty(lbrDtlVoObj.getDisputeT0Id());
				 locMrchSLqry="from MerchantTblVO where mrchntId ="+merchrentid+"";			 
				 locMrchQryrst=pSession.createQuery(locMrchSLqry);			
				 locMrchMstrVOobj=(MerchantTblVO) locMrchQryrst.uniqueResult();
				 if(locMrchMstrVOobj!=null){
					 locMrchMstrvo=new MerchantTblVO();
					 dispute_tomerchname=locMrchMstrVOobj.getMrchntName();
					 locInrJSONObj.put("disp_tomerchantname",Commonutility.toCheckNullEmpty(dispute_tomerchname));					 
				 }
				locInrJSONObj.put("disp_togrpid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getDisputeT0Groupid()));
				locLBRJSONAryobj.put(locInrJSONObj);
				locInrJSONObj=null; 
			}	
			System.out.println("Step 3 : Return JSON Array DATA : "+locLBRJSONAryobj);				
			locFinalRTNObj=new JSONObject();	
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
			locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
			locFinalRTNObj.put("disputemerchantdetails", locLBRJSONAryobj);			
			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			System.out.println("Step 6 : Select category detail block end.");
			log.logMessage("Step 4: Select category detail block end.", "info", DisputeMerchantlistviewall.class);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			System.out.println("Exception in toCMPYSelectAll() : "+e);
			log.logMessage("Step -1 : category select all block Exception : "+e, "error", DisputeMerchantlistviewall.class);	
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("disputemerchantdetails", "");
			locFinalRTNObj.put("CatchBlock", "Error");
			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {
			locStrRow=null;locMaxrow=null;
			lbrDtlVoObj=null;log=null;locObjLbrlst_itr=null;locFinalRTNObj=null;locInrJSONObj=null;
			locLBRJSONAryobj=null;locGrpMstrVOobj=null;locGRPMstrvo=null;locMrchMstrVOobj=null;locMrchMstrvo=null;

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
