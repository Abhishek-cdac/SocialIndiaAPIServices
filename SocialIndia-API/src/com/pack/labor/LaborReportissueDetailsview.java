package com.pack.labor;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.laborvo.LaborSkillTblVO;
import com.pack.laborvo.persistence.LaborDaoservice;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.issuemgmt.persistence.IssueTblVO;
import com.socialindiaservices.vo.MerchantIssuePostingTblVO;
import com.socialindiaservices.vo.MerchantIssueTblVO;
import com.socialindiaservices.vo.MerchantTblVO;

public class LaborReportissueDetailsview extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String ivrparams;

	public String execute(){
		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		Session locObjsession = null;
		String ivrservicecode=null,ivrcurrntusridStr=null;
		int ivrCurrntusrid=0;
		StringBuilder locErrorvalStrBuil =null;
		String societyKey="";
		String iswebmobilefla=null;
		try{
			locErrorvalStrBuil = new StringBuilder();
			locObjsession = HibernateUtil.getSession();
			CommonMobiDao commonServ = new CommonMobiDaoService();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				ivrcurrntusridStr =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
				iswebmobilefla =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "is_mobile");
				if(ivrcurrntusridStr!=null && Commonutility.toCheckisNumeric(ivrcurrntusridStr)){
					ivrCurrntusrid= Integer.parseInt(ivrcurrntusridStr);
				}else{
					ivrCurrntusrid=0;
				}

				boolean desflg =false;
				if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){

					String townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
					  societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
					// call all mobile check

					if (Commonutility.checkempty(townShipid)) {
						if (townShipid.length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
							//success
							System.out.println("success=== ");
						//	desflg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
							desflg=commonServ.checkTownshipKey(townShipid,ivrcurrntusridStr);
							System.out.println("desflg=== "+desflg);
							if(desflg)
							{
							desflg = commonServ.checkSocietyKey(societyKey, ivrcurrntusridStr);
							}
						} else {
							String[] passData = { getText("townshipid.fixed.length") };
							desflg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length.error", passData)));
						}
					} else {
						desflg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
					}
					//desflg =  true;

					//desflg =  false;
				} else{
					desflg = true;
				}
				if(desflg){
					CommonDao ccc =new CommonDao();
					int societyidval= (Integer)ccc.getuniqueColumnVal("SocietyMstTbl", "societyId", "activationKey", societyKey);
				locObjRspdataJson=toLBRRepIssueSelectAll(locObjRecvdataJson,locObjsession,societyidval);
				String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
				JSONArray labourdetails=(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "disputemerchant_labourdetails");
				if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
					//AuditTrial.toWriteAudit(getText("LBAD014"), "LBAD014", ivrCurrntusrid);
					serverResponse("SI3005","0","E3005",getText("labor.selectall.error"),locObjRspdataJson,iswebmobilefla);
				}else{
					//AuditTrial.toWriteAudit(getText("LBAD013"), "LBAD013", ivrCurrntusrid);
					if(labourdetails==null || labourdetails.length() <=0 ){//bookingdetails found
						locObjRspdataJson=null;
						serverResponse("SI00033","01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson,iswebmobilefla);
					}
					else
					{
						serverResponse("SI3005","00","SI3005",getText("labor.selectall.success"),locObjRspdataJson,iswebmobilefla);
					}
				}
				}
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI3005, "+getText("request.format.notmach"), "info", LaborReportissueDetailsview.class);
					serverResponse("SI3005","1","EF0001",getText("request.format.notmach"),locObjRspdataJson,iswebmobilefla);

				}
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI3005, "+getText("request.values.empty"), "info", LaborReportissueDetailsview.class);
				serverResponse("SI3005","1","ER0001",getText("request.values.empty"),locObjRspdataJson,iswebmobilefla);

			}
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI3005, "+getText("catch.error"), "error", LaborReportissueDetailsview.class);
			serverResponse("SI3005","2","ER0002",getText("catch.error"),locObjRspdataJson,iswebmobilefla);
		}finally{
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;
		}
		return SUCCESS;
	}

	public static JSONObject toLBRRepIssueSelectAll(JSONObject pDataJson, Session pSession,int societyidval) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;

		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrLBR_cntQry=null;
		String loc_slQry = null, locStrRow = null, locMaxrow = null, locsrchdtblsrch = null, locMrchSLqry = null, dispute_tomerchname = null, locMrchSLqry1 = null, dispute_issuetype = "";
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		MerchantIssuePostingTblVO lbrDtlVoObj=null;
		String locvrLBR_ID=null,locvrLBR_SERVICE_ID=null, locvrLBR_name=null, locvrLBR_Email=null;
		int count=0,countFilter=0, startrowfrom=0, totalNorow=10;
		MerchantTblVO locMrchMstrVOobj=null,locMrchMstrvo=null;
		LaborProfileTblVO locLbrMstrVOobj=null,locLbrMstrvo=null;
		MerchantIssueTblVO locMrchIssVOobj=null,locMrchIssrvo=null;
		IssueTblVO locMstrIssVOobj=null,locMstrIssrvo=null;
		String loc_slQry_categry=null;
		Iterator locObjLbrcateglst_itr=null;
		LaborSkillTblVO locLbrSkiltbl=null;
		JSONArray locLBRSklJSONAryobj=null,locLBRSklJSONAryobj1=null;
		JSONObject locInrLbrSklJSONObj=null,locInrLbrSklJSONObj1=null;
		Query locMrchQryrst=null;
		String disp_issdesc="";
		String grpcode="",grpcodeappend="";
		try {
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			log=new Log();	lbrDtlVoObj=new MerchantIssuePostingTblVO();
			System.out.println("Step 1 : labor rep_issue select all block enter");
			log.logMessage("Step 1 : labor rep_issue select all block enter", "info", LaborReportissueDetailsview.class);
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "status");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			 grpcode = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "grpcode");
			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
			String societyId = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "society");
			String timestamp = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"timestamp");
			if (Commonutility.checkempty(societyId)) {

			} else {
				societyId=String.valueOf(societyidval);}
			if (Commonutility.checkempty(timestamp)) {
			} else {
				timestamp=Commonutility.timeStampRetStringVal();
			}
			if (grpcode.equalsIgnoreCase("labour_report")) {
				grpcode ="7";
			} else {
				grpcode ="8";}
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web
				if (loctocheNull.equalsIgnoreCase("")) {
					locvrLBR_cntQry="select count(issueId) from MerchantIssuePostingTblVO where status="+locvrLBR_STS+" and grp_code ="+grpcode+" and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S')";
				} else {
					locvrLBR_cntQry="select count(issueId) from MerchantIssuePostingTblVO where status="+locvrLBR_STS+" and grp_code ="+grpcode+" and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') and (" + "ivrBnLBR_NAME like ('%" + loctocheNull+ "%') or "
			                                + "ivrBnLBR_PH_NO like ('%" + loctocheNull + "%')  or "
			                                + "ivrBnLBR_EMAIL like ('%" + loctocheNull + "%')  "
			                               // + "ivrBNLbrSkilObj.iVOCATEGORY_ID.iVOCATEGORY_NAME like ('%" + loctocheNull + "%') or "
			                                + ")";
				}
				System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+locvrLBR_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrLBR_cntQry, "info", LaborReportissueDetailsview.class);

				LaborDaoservice lbrDaoObj=new LaborDaoservice();
				count=lbrDaoObj.getInitTotal(locvrLBR_cntQry);
				countFilter=count;
			}else{ // for mobile
				 count=0;countFilter=0;
				 System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", LaborReportissueDetailsview.class);
			}

				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}

				String globalsearch=null;
				String locOrderby =" order by entryDatetime desc";
			if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				  globalsearch = " AND (" + "ivrBnLBR_NAME like ('%" + loctocheNull+ "%') or "
			                                + "ivrBnLBR_PH_NO like ('%" + loctocheNull + "%')  or "
			                                + "ivrBnLBR_EMAIL like ('%" + loctocheNull + "%')  "
			                               // + "ivrBNLbrSkilObj.iVOCATEGORY_ID.iVOCATEGORY_NAME like ('%" + loctocheNull + "%') or "
			                                + ")";
				  loc_slQry="from MerchantIssuePostingTblVO  where status=" + locvrLBR_STS + " and grp_code ="+grpcode+" and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') " + globalsearch + " " + locOrderby;
			}else{
				loc_slQry="from MerchantIssuePostingTblVO  where status=" + locvrLBR_STS + " and grp_code ="+grpcode+" and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') " + locOrderby;
			}
			//loc_slQry="from MerchantIssuePostingTblVO  where status="+locvrLBR_STS+"";
			System.out.println("Step 3 : labor rep_issue Details Query : "+loc_slQry);
			log.logMessage("Step 3 : labor rep_issue Details Query : "+loc_slQry, "info", LaborReportissueDetailsview.class);
			locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			locLBRJSONAryobj=new JSONArray();
			String filepath=rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/labor/mobile/";
			String filepathweb=rb.getString("SOCIAL_INDIA_BASE_URL") +"/templogo/labor/web/";
			while ( locObjLbrlst_itr.hasNext() ) {
				locInrJSONObj=new JSONObject();
				System.out.println("------------------------Start-------------------------");
				lbrDtlVoObj = (MerchantIssuePostingTblVO) locObjLbrlst_itr.next();				
				System.out.println("Name : "+lbrDtlVoObj.getIssueId());
				locInrJSONObj.put("lbrrepiss_id",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIssueId()));
				locInrJSONObj.put("lbrrepiss__raisedby",String.valueOf(lbrDtlVoObj.getIssueRaisedBy().getUserId()));
				locInrJSONObj.put("lbrrepiss_mrchtorlaborid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getMrchntId()));
				locInrJSONObj.put("lbrrepiss_grpcode",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getGrp_code()));
				locInrJSONObj.put("lbrrepiss_comment",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getComments()));
				locInrJSONObj.put("lbrrepiss_issuetypes",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIssueTypes()));
				locInrJSONObj.put("disp_usrname",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIssueRaisedBy().getFirstName()+" "+lbrDtlVoObj.getIssueRaisedBy().getLastName()));
				if(lbrDtlVoObj.getIssueRaisedBy().getSocietyId()!=null){
					locInrJSONObj.put("disp_societyname",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIssueRaisedBy().getSocietyId().getSocietyName()));
					locInrJSONObj.put("disp_townshipname",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIssueRaisedBy().getSocietyId().getTownshipId().getTownshipName()));

				} else {
					locInrJSONObj.put("disp_societyname", "");
					locInrJSONObj.put("disp_townshipname", "");
				}

				String merchrentid =Commonutility.toCheckNullEmpty(lbrDtlVoObj.getMrchntId());
				if (grpcode.equalsIgnoreCase("7")) {
				 locMrchSLqry="from LaborProfileTblVO where ivrBnLBR_ID ="+merchrentid+"";
				 locMrchQryrst=pSession.createQuery(locMrchSLqry);
				 locLbrMstrVOobj=(LaborProfileTblVO) locMrchQryrst.uniqueResult();
					if (locLbrMstrVOobj != null) {
					 locLbrMstrvo=new LaborProfileTblVO();
					 dispute_tomerchname=locLbrMstrVOobj.getIvrBnLBR_NAME();
					 locInrJSONObj.put("disp_tomerchantname",Commonutility.toCheckNullEmpty(dispute_tomerchname));
					}
				} else {
					 locMrchSLqry="from MerchantTblVO where mrchntId ="+merchrentid+"";
					 locMrchQryrst=pSession.createQuery(locMrchSLqry);
					 locMrchMstrVOobj=(MerchantTblVO) locMrchQryrst.uniqueResult();
					 if(locMrchMstrVOobj!=null){
						 locMrchMstrvo=new MerchantTblVO();
						 dispute_tomerchname=locMrchMstrVOobj.getMrchntName();
						 locInrJSONObj.put("disp_tomerchantname",Commonutility.toCheckNullEmpty(dispute_tomerchname));
					 }
				}
				String resport_issuetype=  Commonutility.toCheckNullEmpty(lbrDtlVoObj.getIssueTypes());
				String splitval[] = resport_issuetype.split(",");
				dispute_issuetype = "";
				for (int i = 0; i < splitval.length; i++) {
					System.out.println("spv:::::::: "+splitval[i]);
					System.out.println("grpcode:::::::: "+grpcode);
					if (grpcode.equalsIgnoreCase("7")){
						grpcodeappend =" and ivrGrpcodeobj ="+grpcode+" ";
						locMrchSLqry1="from IssueTblVO where issueId ="+splitval[i]+"  ";
						locMrchQryrst = pSession.createQuery(locMrchSLqry1);
						 locMstrIssVOobj = (IssueTblVO) locMrchQryrst.uniqueResult();
						 if(locMstrIssVOobj!=null){
							 if (Commonutility.checkempty(locMstrIssVOobj.getIssueList())) {
								 dispute_issuetype += locMstrIssVOobj.getIssueList()+", ";
							 }
							
						}
					} else {
						grpcodeappend =" and ivrGrpcodeobj = 8 ";
						locMrchSLqry1="from MerchantIssueTblVO where issueId ="+splitval[i]+" and mcrctLaborId ="+merchrentid+" "+grpcodeappend+"";
						 locMrchQryrst=pSession.createQuery(locMrchSLqry1);
						 locMrchIssVOobj= (MerchantIssueTblVO) locMrchQryrst.uniqueResult();
						if (locMrchIssVOobj!=null){
							 if (Commonutility.checkempty(locMrchIssVOobj.getDescription())) {
								 dispute_issuetype += locMrchIssVOobj.getDescription()+", ";
							 }
						 }
					}

				}
				Commonutility.toWriteConsole("dispute_issuetype : " + Commonutility.toCheckNullEmpty(dispute_issuetype));
				if (dispute_issuetype !=null && dispute_issuetype.endsWith(", ")){
					disp_issdesc = dispute_issuetype.substring(0,dispute_issuetype.length() - 2);
					Commonutility.toWriteConsole("dispute_issuetype if : " + Commonutility.toCheckNullEmpty(dispute_issuetype));
				} else {
					Commonutility.toWriteConsole("dispute_issuetype else: " + Commonutility.toCheckNullEmpty(dispute_issuetype));
				}
				
				Commonutility.toWriteConsole("DESC : " + Commonutility.toCheckNullEmpty(disp_issdesc));
				locInrJSONObj.put("disp_issuetypdesc",Commonutility.toCheckNullEmpty(disp_issdesc));
				System.out.println("Name : "+lbrDtlVoObj.getIssueId());
				locLBRJSONAryobj.put(locInrJSONObj);
				locInrJSONObj=null;
				disp_issdesc = "";
			}
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("timestamp", Commonutility.toCheckNullEmpty(timestamp));
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
			locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
			locFinalRTNObj.put("disputemerchant_labourdetails", locLBRJSONAryobj);

			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			System.out.println("Step 4 : Select labor rep_issue detail block end.");
			log.logMessage("Step 4: Select labor rep_issue detail block end.", "info", LaborReportissueDetailsview.class);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			System.out.println("Exception in toLBRRepIssueSelectAll() : "+e);
			log.logMessage("Step -1 : labor rep_issue select all block Exception : "+e, "error", LaborReportissueDetailsview.class);
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("disputemerchant_labourdetails", "");
			locFinalRTNObj.put("CatchBlock", "Error");
			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {
			locStrRow=null;locMaxrow=null;locMstrIssVOobj=null;locMstrIssrvo=null;
			lbrDtlVoObj=null;log=null;locObjLbrlst_itr=null;locFinalRTNObj=null;locInrJSONObj=null;locLBRJSONAryobj=null;
			locObjLbrcateglst_itr=null;locLbrSkiltbl=null;locInrLbrSklJSONObj=null;locLBRSklJSONAryobj=null;loc_slQry_categry=null;

		}
	}
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson,String iswebmobilefla)
	{
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();
		try {
			if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
				out = response.getWriter();
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-store");
				mobiCommon mobicomn=new mobiCommon();
				String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
				out.print(as);
				out.close();
			}
			else{
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
			}
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
