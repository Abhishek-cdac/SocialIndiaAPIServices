package com.pack.complaints;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.complaintVO.ComplaintattchTblVO;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.complaintsvo.persistence.ComplaintsDao;
import com.pack.complaintsvo.persistence.ComplaintsDaoservice;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.MerchantTblVO;

public class Complaintsviewall extends ActionSupport {
  /**
   *sdsd.
   */
  private static final long serialVersionUID = 1L;
  private String ivrparams;
  /**
   * Executed Method .
   */
  
  public String execute() {
    Log logWrite = null;
	JSONObject locObjRecvJson = null;//Receive over all Json	[String Received]
	JSONObject locObjRecvdataJson = null;// Receive Data Json		
	JSONObject locObjRspdataJson = null;// Response Data Json
	Session locObjsession = null;		
	String ivrservicecode=null;
	String ivrcurrntusridobj=null;
	int ivrCurrntusrid=0;
	boolean flg = true;
	StringBuilder locErrorvalStrBuil =null;
	otpDao otp=new otpDaoService();
    try {
    	locErrorvalStrBuil = new StringBuilder();
      logWrite = new Log();
      locObjsession = HibernateUtil.getSession();
      if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
    	  ivrparams = EncDecrypt.decrypt(ivrparams);
    	  boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
    	  if (ivIsJson) {
    		locObjRecvJson = new JSONObject(ivrparams);
    		ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
    		String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
			String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
			
			String isMobile = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "is_mobile");
			
    		if(ivrservicecode!=null && !ivrservicecode.equalsIgnoreCase("null") && !ivrservicecode.equalsIgnoreCase("")){
    			locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
    			ivrcurrntusridobj =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
        	   /* if (ivrcurrntusridobj!=null && Commonutility.toCheckisNumeric(ivrcurrntusridobj)) {
    			ivrCurrntusrid= Integer.parseInt(ivrcurrntusridobj);
    			}else { ivrCurrntusrid=0; }
        	    */
    			if(isMobile!=null && isMobile.equalsIgnoreCase("1")){
    			if (Commonutility.checkempty(ivrservicecode)) {
					if (ivrservicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
						
					} else {
						String[] passData = { getText("service.code.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("Service code cannot be empty")));
				}
        	    if (Commonutility.checkempty(townshipKey)) {
					if (townshipKey.trim().length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
						
					} else {
						String[] passData = { getText("townshipid.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
				}
				if (Commonutility.checkempty(societykey)) {
					if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {
						
					} else {
						String[] passData = { getText("society.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
				}
				
				if (locObjRecvdataJson !=null) {
					if (Commonutility.checkempty(ivrcurrntusridobj)) {
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
					}
				}
    			}
    			
				if(flg){
					boolean result=true;
					if(isMobile!=null && isMobile.equalsIgnoreCase("1")){
					result=otp.checkTownshipKey(ivrcurrntusridobj,townshipKey);
					}
					
					if(result==true){
						if(isMobile!=null && isMobile.equalsIgnoreCase("1")){
						UserMasterTblVo userMst=new UserMasterTblVo();
						userMst=otp.checkSocietyKeyForList(societykey,ivrcurrntusridobj);
						if(userMst!=null){
							result=true;
						}else{
							result=false;
						}
						}
						if(result){
        	    locObjRspdataJson=toComplaintselect(locObjRecvdataJson,locObjsession);		
    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
    			JSONArray cmpltntdetails=(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "cmpltntdetails");
    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
    				AuditTrial.toWriteAudit(getText("EVEAD012"), "CMPTAD012", ivrCurrntusrid);
    				serverResponse(ivrservicecode,getText("status.error"),"R0136",mobiCommon.getMsg("R0136"),locObjRspdataJson);
    			}else{
    				//AuditTrial.toWriteAudit(getText("CMPTAD011"), "CMPTAD011", ivrCurrntusrid);
    				if(cmpltntdetails==null || cmpltntdetails.length() <=0 ){//bookingdetails found
						locObjRspdataJson=null;
						serverResponse("SI00033","01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);	
					}
					else
					{
						serverResponse(ivrservicecode,getText("status.success"),"R0137",mobiCommon.getMsg("R0137"),locObjRspdataJson);	
					}	
    							
    			}
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(ivrservicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);
					}
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(ivrservicecode,getText("status.error"),"R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);		
				}
				}else{
					locObjRspdataJson=new JSONObject();
					locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
					serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
				}
    		}else {
    			locObjRspdataJson=new JSONObject();
    	    	logWrite.logMessage("Service code : SI9006,"+getText("request.values.empty")+"", "info", Complaintsviewall.class);
    	    	  serverResponse(ivrservicecode,getText("status.error"),"R0148",mobiCommon.getMsg("R0148"),locObjRspdataJson);  
    	    }			    	   
    	  }else {
          locObjRspdataJson=new JSONObject();
          logWrite.logMessage("Service code : SI9006,"+getText("request.format.notmach")+"", "info", Complaintsviewall.class);
		  serverResponse(ivrservicecode,getText("status.error"),"R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);  
    	  }
      }else {
    	locObjRspdataJson=new JSONObject();
    	logWrite.logMessage("Service code : SI9006,"+getText("request.values.empty")+"", "info", Complaintsviewall.class);
    	 serverResponse(ivrservicecode,getText("status.error"),"R0148",mobiCommon.getMsg("R0148"),locObjRspdataJson);  
      }      
    } catch (Exception expObj) {      
      locObjRspdataJson=new JSONObject();
      logWrite.logMessage("Service code : SI9006, Sorry, an unhandled error occurred : "+expObj, "error", Complaintsviewall.class);
      serverResponse(ivrservicecode,getText("status.error"),"R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
	} finally {
		if (locObjsession!=null) {locObjsession.flush();locObjsession.clear();locObjsession.close(); locObjsession = null;}
		logWrite = null;
		locObjRecvJson = null;//Receive over all Json	[String Received]
		locObjRecvdataJson = null;// Receive Data Json		
		locObjRspdataJson = null;// Response Data Json	
		ivrservicecode=null;
		ivrcurrntusridobj=null;
		ivrCurrntusrid=0;
	}	 
	return SUCCESS;
  }

  /*
   * To select Events.
   */
  private JSONObject toComplaintselect(JSONObject praDatajson, Session praSession){	
	JSONObject lvrRtnjsonobj = null;
	JSONObject lvrInrJSONObj = null;	
	JSONArray lvrComplaintjsonaryobj = null;
	Log logWrite = null;
	Common locCommonObj = null;
	Date lvrEntrydate = null;
	Date lvrModifydate = null;
	Iterator lvrObjcmpltlstitr = null;
	ComplaintsTblVO lvrCmplttblvoobj = null;
	Query lvrQrybj = null,lvrQrygrpbj=null,lvrQryToidqry=null,lvrQryAttachqry=null;
	UserMasterTblVo locGrptblobj = null;
	LaborProfileTblVO locLabnameObj=null;
	ComplaintattchTblVO locCmpltAttchfnameObj=null;
	MerchantTblVO locMrchnameObj=null;
	String Complt_toname=null,Complt_toemail=null,Complt_tomobno=null,lvrcmpltcountqry = null,locvrComplaintSTS = null, locvrCntflg = null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,lvrSlgrpcodeqry=null,lvrslqlabnameqry=null,loccrntusrloginid=null,attachfname=null;	
	int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
	String lvSlqry = null,locToidSLqry=null,locAttachFnameSLqry;
	Session locHbsession = null;
	ResourceBundle lvrRbdl = ResourceBundle.getBundle("applicationResources");
	Iterator locObjfilelst_itr=null;
	JSONArray locLBRFILEJSONAryobj=null;JSONArray locLBRFILEJSONAryobj1=null;
	ComplaintattchTblVO locfiledbtbl=null;
	JSONObject locLBRFILEOBJJSONAryobj=null;
	JSONObject locLBRFILEOBJJSONAryobj1=null;
    try {
    	logWrite = new Log();
    	locCommonObj=new CommonDao();
    	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
    	locHbsession = HibernateUtil.getSession();
    	Map sessionMap =ActionContext.getContext().getSession();
    	Commonutility.toWriteConsole("Step 1 : Select Complaint process start.");
    	logWrite.logMessage("Step 1 : Select Complaint process start.", "info", Complaintsviewall.class);
    	String locvrsocietyid = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "loginsocietyid");
    	locvrComplaintSTS = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "complaintstatus");
		locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countflg");
		locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countfilterflg");
		locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
		locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
		locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
		loccrntusrloginid=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "crntusrloginid");
		String societyId = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "society");
		String slectfield = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "slectfield");
		String searchWord = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "searchname");	
		String searchflg = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "searchflg");
		
		//String complaintId = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "complaintid");
		String locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "timestamp");
		String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
		//--Labor - or -- merchant--- id
		String filepathweb=getText("SOCIAL_INDIA_BASE_URL") +"/templogo/complaint/mobile/";
		String filepathlabweb=getText("SOCIAL_INDIA_BASE_URL") +"/templogo/labor/mobile/";
		String filepathmrchweb=getText("SOCIAL_INDIA_BASE_URL") +"/templogo/Merchant/mobile/";
		String filepathrest=getText("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/";
		String filepathrestweb=getText("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/web/";
		lvrSlgrpcodeqry = "from UserMasterTblVo where userId = "+Integer.parseInt(loccrntusrloginid);
		lvrQrygrpbj = locHbsession.createQuery(lvrSlgrpcodeqry);
		locGrptblobj = (UserMasterTblVo) lvrQrygrpbj.uniqueResult();
		Commonutility.toWriteConsole("Step societyidget : Select usermaster detail query executed."+locGrptblobj.getSocietyId());
		//int grpcode = locGrptblobj.getGroupCode().getGroupCode();	
		
		String lvrLoginusrgrpcode = Commonutility.intToString(locGrptblobj.getGroupCode().getGroupCode());
		String lvrComtgrpcode = locCommonObj.getGroupcodeexistornew(lvrRbdl.getString("Grp.committee"));
		String lvrSoctygrpcode = locCommonObj.getGroupcodeexistornew(lvrRbdl.getString("Grp.society"));
		String lvrAdmingrpcode = locCommonObj.getGroupcodeexistornew(lvrRbdl.getString("Grp.admin"));
		
		Commonutility.toWriteConsole("lvrLoginusrgrpcode : "+lvrLoginusrgrpcode);
		Commonutility.toWriteConsole("lvrComtgrpcode : "+lvrComtgrpcode);
		Commonutility.toWriteConsole("lvrSoctygrpcode : "+lvrSoctygrpcode);
		Commonutility.toWriteConsole("lvrAdmingrpcode : "+lvrAdmingrpcode);
		
		String glbSearch="";
		String locOrderby =" order by entryDatetime desc";
		Commonutility.toWriteConsole("step3: datatable search field:  "+societyId+" : selectfield::: "+slectfield);
		//Datatable search
		if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
			if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
				glbSearch = " and (" + " usrRegTblByFromUsrId.societyId.societyName like ('%" + locTochenull + "%') or " 
			             + " complaintsTitle like ('%" + locTochenull + "%') or "
			             + " complaintsDesc like ('%" + locTochenull + "%') or "
			              + " usrRegTblByFromUsrId.groupCode.groupName like ('%" + locTochenull + "%') or "
			             + "  usrRegTblByFromUsrId.emailId like ('%" + locTochenull + "%') "
			             + ")";
			}
			else{
				glbSearch = " and (" + " usrRegTblByFromUsrId.societyId.societyName like ('%" + locTochenull + "%') or " 
			             + " complaintsTitle like ('%" + locTochenull + "%') or "
			             + " complaintsDesc like ('%" + locTochenull + "%') or "
			             + "  usrRegTblByFromUsrId.emailId like ('%" + locTochenull + "%') or "
			              + "usrRegTblByFromUsrId.groupCode.groupName like ('%" + locTochenull + "%')"
			             + ") and  usrRegTblByFromUsrId.societyId.societyId=" + Integer.parseInt(societyId) ; 
			}	
			
			if (Commonutility.checkempty(locTimeStamp)) {
			} else {
				locTimeStamp=Commonutility.timeStampRetStringVal();
			}
			
			glbSearch +=" and entryDatetime < STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S')";
			
			if(lvrLoginusrgrpcode.equalsIgnoreCase(lvrComtgrpcode)){// committee
				lvSlqry = "from ComplaintsTblVO   where compliantsToFlag in(0,1,2)  " + glbSearch+" "+locOrderby;
				lvrcmpltcountqry = "select count(*) from ComplaintsTblVO  where compliantsToFlag in(0,1,2)  " + glbSearch+" ";
			} else if(lvrLoginusrgrpcode.equalsIgnoreCase(lvrSoctygrpcode)) {// society
				lvSlqry = "from ComplaintsTblVO   where compliantsToFlag in(0,1,2) " + glbSearch+" "+locOrderby;
				lvrcmpltcountqry = "select count(*) from ComplaintsTblVO   where compliantsToFlag in(0,1,2) " + glbSearch+" ";
			} else if(lvrLoginusrgrpcode.equalsIgnoreCase(lvrAdmingrpcode)) {// admin
				lvSlqry = "from ComplaintsTblVO   where (compliantsToFlag in(0,1,2) or compliantsToFlag is null) " + glbSearch+" "+locOrderby;
				lvrcmpltcountqry = "select count(*) from ComplaintsTblVO   where (compliantsToFlag in(0,1,2) or compliantsToFlag is null) " + glbSearch+" ";
			} else {
				lvSlqry = "from ComplaintsTblVO   where (compliantsToFlag in(0,1,2) or compliantsToFlag is null) " + glbSearch+" "+locOrderby;
				lvrcmpltcountqry = "select count(*) from ComplaintsTblVO   where (compliantsToFlag in(0,1,2) or compliantsToFlag is null) " + glbSearch+" ";
			}		
			
			} else {
			if(searchWord!=null && !searchWord.equalsIgnoreCase("null") && !searchWord.equalsIgnoreCase("")){// Top search box
				if(societyId!=null && !societyId.equalsIgnoreCase("null") && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")){
					glbSearch = " and ";
					if(slectfield!=null && slectfield.equalsIgnoreCase("1")){
						/*glbSearch += "( usrRegTblByFromUsrId.societyId.societyName like ('%" + searchWord + "%') )";*/
						glbSearch = " and (" + " usrRegTblByFromUsrId.societyId.societyName like ('%" + searchWord + "%') or " 
					             + " complaintsTitle like ('%" + searchWord + "%') or "
					             + " complaintsDesc like ('%" + searchWord + "%') or "
					              + "usrRegTblByFromUsrId.groupCode.groupName like ('%" + searchWord + "%') or "
					             + " usrRegTblByFromUsrId.userName like ('%" + searchWord + "%')) ";
					            /* + ") and  usrRegTblByFromUsrId.societyId.societyId=" + Integer.parseInt(societyId) ; */
					}else if(slectfield!=null && slectfield.equalsIgnoreCase("2")){														 		 
						glbSearch += "( usrRegTblByFromUsrId.userName like ('%" + searchWord + "%') )";
					}
					else if(slectfield!=null && slectfield.equalsIgnoreCase("3")){														 		 
						glbSearch += "( complaintsTitle  like ('%" + searchWord + "%') )";
					}else if(slectfield!=null && slectfield.equalsIgnoreCase("4")){														 		 
						glbSearch += "(usrRegTblByFromUsrId.groupCode.groupName like ('%" + searchWord + "%'))";
					}else{
						glbSearch += "(usrRegTblByFromUsrId.societyId.societyName like ('%" + searchWord + "%') or ";					
						glbSearch += " usrRegTblByFromUsrId.userName like ('%" + searchWord + "%') or"
								 + "usrRegTblByFromUsrId.groupCode.groupName like ('%" + searchWord + "%') or"
								  + "  usrRegTblByFromUsrId.emailId  like ('%" + searchWord + "%')  )";
					}									 		 
					glbSearch += " and  usrRegTblByFromUsrId.societyId.societyId=" + Integer.parseInt(societyId);
					
				}else{
					glbSearch = " and ";
					if(slectfield!=null && slectfield.equalsIgnoreCase("1")){
						/*glbSearch += "( usrRegTblByFromUsrId.societyId.societyName like ('%" + searchWord + "%') )";*/
						glbSearch = " and (" + " usrRegTblByFromUsrId.societyId.societyName like ('%" + searchWord + "%') or " 
					             + " complaintsTitle like ('%" + searchWord + "%') or "
					             + " complaintsDesc like ('%" + searchWord + "%') or "
					              + " usrRegTblByFromUsrId.groupCode.groupName like ('%" + searchWord + "%') or"
					             + " usrRegTblByFromUsrId.userName like ('%" + searchWord + "%')) ";
				
					}else if(slectfield!=null && slectfield.equalsIgnoreCase("2")){														 		 
						glbSearch += "( usrRegTblByFromUsrId.userName like ('%" + searchWord + "%') )";
					}else if(slectfield!=null && slectfield.equalsIgnoreCase("3")){														 		 
						glbSearch += "(  complaintsTitle  like ('%" + searchWord + "%') )";
					}else if(slectfield!=null && slectfield.equalsIgnoreCase("4")){														 		 
						glbSearch += "(usrRegTblByFromUsrId.groupCode.groupName like ('%" + searchWord + "%'))";
					}else{
						glbSearch = " and (" + " usrRegTblByFromUsrId.societyId.societyName like ('%" + searchWord + "%') or " 
					             + " complaintsTitle like ('%" + searchWord + "%') or "
					             + " complaintsDesc like ('%" + searchWord + "%') or "
					              + " usrRegTblByFromUsrId.groupCode.groupName like ('%" + searchWord + "%') or "
					             + " usrRegTblByFromUsrId.userName like ('%" + searchWord + "%') "
					             + ")";
					}									 		 
				}
			} else {
				if(societyId!=null && !societyId.equalsIgnoreCase("null") && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")){
					glbSearch = " and usrRegTblByFromUsrId.societyId.societyId=" + Integer.parseInt(societyId);
				}else{
					glbSearch = "";
				}
				
				
			}	
			
			if (Commonutility.checkempty(locTimeStamp)) {
			} else {
				locTimeStamp=Commonutility.timeStampRetStringVal();
			}
			
			glbSearch+=" and entryDatetime < STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S')";
			if(lvrLoginusrgrpcode.equalsIgnoreCase(lvrComtgrpcode)){// committee
				lvSlqry = "from ComplaintsTblVO   where compliantsToFlag in(0,1,2) " + glbSearch+" "+locOrderby;
				lvrcmpltcountqry = "select count(*) from ComplaintsTblVO  where compliantsToFlag in(0,1,2)  " + glbSearch+" ";
			} else if(lvrLoginusrgrpcode.equalsIgnoreCase(lvrSoctygrpcode)) {// society
				lvSlqry = "from ComplaintsTblVO where compliantsToFlag in(0,1,2) " + glbSearch+" "+locOrderby;
				lvrcmpltcountqry = "select count(*) from ComplaintsTblVO   where compliantsToFlag in(0,1,2) " + glbSearch+" ";
			} else if(lvrLoginusrgrpcode.equalsIgnoreCase(lvrAdmingrpcode)) {// admin
				lvSlqry = "from ComplaintsTblVO   where (compliantsToFlag in (0,1,2) or compliantsToFlag is null) " + glbSearch+" "+locOrderby;
				lvrcmpltcountqry = "select count(*) from ComplaintsTblVO   where (compliantsToFlag in(0,1,2) or compliantsToFlag is null) " + glbSearch+" ";
			} else {
				lvSlqry = "from ComplaintsTblVO   where (compliantsToFlag in(0,1,2) or compliantsToFlag is null) " + glbSearch+" "+locOrderby;
				lvrcmpltcountqry = "select count(*) from ComplaintsTblVO   where (compliantsToFlag in(0,1,2) or compliantsToFlag is null) " + glbSearch+" ";
			}
			
				//lvSlqry = "from ComplaintsTblVO   where status in(0,1)  "+glbSearch +" "+locOrderby;
				//lvrcmpltcountqry = "select count(*) from ComplaintsTblVO   where status in(0,1) "+glbSearch ;
		}
			
				
		if (locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))) {
			
			Commonutility.toWriteConsole("Step 2 : Count / Filter Count block enter SlQry : "+lvrcmpltcountqry);
			logWrite.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+lvrcmpltcountqry, "info", Complaintsviewall.class);
			ComplaintsDao lvrEvntDobj = new ComplaintsDaoservice();
			count = lvrEvntDobj.getInitTotal(lvrcmpltcountqry);
			countFilter = count;
			// countFilter=lvrEvntDobj.getTotalFilter(lvrcmpltcountqry);
		}else {
			count=0;countFilter=0;
			Commonutility.toWriteConsole("Step 2 : Count / Filter Count not need.[Mobile Call]");
			logWrite.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", Complaintsviewall.class);
		}
		if (Commonutility.toCheckisNumeric(locStrRow)) {
			 startrowfrom=Integer.parseInt(locStrRow);
		}
		if (Commonutility.toCheckisNumeric(locMaxrow)) {
			totalNorow=Integer.parseInt(locMaxrow);
		}
		Commonutility.toWriteConsole("Step 3 : Complaint Details Query : "+lvSlqry);
		logWrite.logMessage("Step 3 : Complaint Details Query : "+lvSlqry, "info", Complaintsviewall.class);
		lvrObjcmpltlstitr = praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
		lvrComplaintjsonaryobj = new JSONArray();		
		while (lvrObjcmpltlstitr.hasNext()) {			
			lvrInrJSONObj=new JSONObject();
			lvrCmplttblvoobj = (ComplaintsTblVO) lvrObjcmpltlstitr.next();			
			//if(sessionMap.get("USERID")
			if(lvrCmplttblvoobj.getCompliantsToFlag() == 0) {
				lvrInrJSONObj.put("cmplttodest", "External [Out to Society]");
			} else if(lvrCmplttblvoobj.getCompliantsToFlag() == 1){
				lvrInrJSONObj.put("cmplttodest", "Committee");
			} else if(lvrCmplttblvoobj.getCompliantsToFlag() == 2){
				lvrInrJSONObj.put("cmplttodest", "Admin");
			} else {
				lvrInrJSONObj.put("cmplttodest", "External [Out to Society]");
			}
			lvrInrJSONObj.put("cmplttoflg", Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getCompliantsToFlag()));
			
			if(lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getSocietyId()!=null){
				lvrInrJSONObj.put("cmpltsocietyname",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getSocietyId().getSocietyName()));
				lvrInrJSONObj.put("cmplttownshipname",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getSocietyId().getTownshipId().getTownshipName()));
			} else {
				lvrInrJSONObj.put("cmpltsocietyname","");
				lvrInrJSONObj.put("cmplttownshipname","");
			}
			lvrInrJSONObj.put("cmpltfrmusrid",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getUserId()));
			
			if(lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getGroupCode()!=null){
				lvrInrJSONObj.put("cmpltfrmusrgrpcode",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getGroupCode().getGroupName()));
				
			}else{
				lvrInrJSONObj.put("cmpltfrmusrgrpcode","");				
			}
			
			if(lvrCmplttblvoobj.getGroupMstTblByFromGroupId()!=null){
				lvrInrJSONObj.put("cmpltfrmgrpid",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getGroupMstTblByFromGroupId().getGroupCode()));
			} else {
				lvrInrJSONObj.put("cmpltfrmgrpid","");
			}
			lvrInrJSONObj.put("cmpltfrmusrname",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getFirstName())+Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getLastName()));
			lvrInrJSONObj.put("profile_name",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getFirstName()));
			if(lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getImageNameWeb()!=null){
			lvrInrJSONObj.put("profilepic_thumbnail",Commonutility.toCheckNullEmpty(filepathrest+lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getUserId()+"/"+lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getImageNameWeb()));}
			else
			{
				lvrInrJSONObj.put("profilepic_thumbnail","");
			}
			if(lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getImageNameMobile()!=null){
				lvrInrJSONObj.put("profilepic",Commonutility.toCheckNullEmpty(filepathrestweb+lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getUserId()+"/"+lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getImageNameMobile()));}
				else
				{
					lvrInrJSONObj.put("profilepic","");
				}

			lvrInrJSONObj.put("cmpltcommiteeemail",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getEmailId()));
			lvrInrJSONObj.put("cmpltfrmmobno",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getUsrRegTblByFromUsrId().getMobileNo()));
			
			
			lvrInrJSONObj.put("cmpltshrtdesc",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getShrtDesc()));
			lvrInrJSONObj.put("cmplttouserid",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getUsrRegTblByToUsrId()));
			lvrInrJSONObj.put("cmplttogrpid",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getGroupMstTblByToGroupId()));
			
			String Cmplttoid = Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getUsrRegTblByToUsrId());
			String Cmplttogrpcode = Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getGroupMstTblByToGroupId());
			String lbrGrpcode = locCommonObj.getGroupcodeexistornew(lvrRbdl.getString("Grp.labor"));
			String mrchntGrpcode = locCommonObj.getGroupcodeexistornew(lvrRbdl.getString("Grp.merchant"));
			
			locLabnameObj = null; locMrchnameObj = null;
			if (Cmplttogrpcode!=null && Cmplttogrpcode.equalsIgnoreCase(lbrGrpcode) && !Cmplttoid.equalsIgnoreCase("null")) {
			 locToidSLqry="from LaborProfileTblVO where ivrBnLBR_ID ="+Cmplttoid+"";			 
			 lvrQryToidqry=locHbsession.createQuery(locToidSLqry);			
			 locLabnameObj=(LaborProfileTblVO) lvrQryToidqry.uniqueResult();			
			 	if(locLabnameObj!=null){
					 locLabnameObj=new LaborProfileTblVO();
					 Complt_toemail=locLabnameObj.getIvrBnLBR_EMAIL();
					 Complt_tomobno=locLabnameObj.getIvrBnLBR_PH_NO();
					 Complt_toname=locLabnameObj.getIvrBnLBR_NAME();
					
					 lvrInrJSONObj.put("cmplt_toname",Commonutility.toCheckNullEmpty(Complt_toname));	
					 lvrInrJSONObj.put("cmplt_toemail",Commonutility.toCheckNullEmpty(Complt_toemail));	
					 lvrInrJSONObj.put("cmplt_tomobno",Commonutility.toCheckNullEmpty(Complt_tomobno));	
					 lvrInrJSONObj.put("cmplt_toprofilethumbail",Commonutility.toCheckNullEmpty(filepathlabweb +Cmplttoid+"/"+locLabnameObj.getIvrBnIMAGE_NAME_MOBILE()));	
					 lvrInrJSONObj.put("cmplt_toprofileimg",Commonutility.toCheckNullEmpty(filepathlabweb +Cmplttoid+"/"+locLabnameObj.getIvrBnIMAGE_NAME_WEB()));	
			 	}
			} else if(Cmplttogrpcode!=null && Cmplttogrpcode.equalsIgnoreCase(mrchntGrpcode) && !Cmplttoid.equalsIgnoreCase("null")) {
				 locToidSLqry="from MerchantTblVO where mrchntId ="+Cmplttoid+"";			 
				 lvrQryToidqry=locHbsession.createQuery(locToidSLqry);			
				 locMrchnameObj=(MerchantTblVO) lvrQryToidqry.uniqueResult();
				 if(locMrchnameObj!=null){
					 locMrchnameObj=new MerchantTblVO();
					 Complt_toemail=locMrchnameObj.getMrchntEmail();
					 Complt_tomobno=locMrchnameObj.getMrchntPhNo();
					 Complt_toname=locMrchnameObj.getMrchntName();
					 lvrInrJSONObj.put("cmplt_toname",Commonutility.toCheckNullEmpty(Complt_toname));	
					 lvrInrJSONObj.put("cmplt_toemail",Commonutility.toCheckNullEmpty(Complt_toemail));	
					 lvrInrJSONObj.put("cmplt_tomobno",Commonutility.toCheckNullEmpty(Complt_tomobno));	
					 lvrInrJSONObj.put("cmplt_toprofilethumbail",Commonutility.toCheckNullEmpty(filepathmrchweb +Cmplttoid+"/"+locMrchnameObj.getStoreimage()));	
					 lvrInrJSONObj.put("cmplt_toprofileimg",Commonutility.toCheckNullEmpty(filepathmrchweb +Cmplttoid+"/"+locMrchnameObj.getStoreimage()));
				 }
			} else {
				 lvrInrJSONObj.put("cmplt_toname","");	
				 lvrInrJSONObj.put("cmplt_toemail","");	
				 lvrInrJSONObj.put("cmplt_tomobno","");	
				 lvrInrJSONObj.put("cmplt_toprofilethumbail","");	
				 lvrInrJSONObj.put("cmplt_toprofileimg","");
			}
			locLabnameObj = null; locMrchnameObj = null;					
			lvrInrJSONObj.put("cmpltid",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getComplaintsId()));
			lvrInrJSONObj.put("cmplttitle",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getComplaintsTitle()));
			lvrInrJSONObj.put("cmpltdesc",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getComplaintsDesc()));
			lvrInrJSONObj.put("cmpltfilename1",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getComplaintsFileName1()));
			lvrInrJSONObj.put("cmpltfilename2",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getComplaintsFileName2()));
			lvrInrJSONObj.put("cmpltfilename3",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getComplaintsFileName3()));
			lvrInrJSONObj.put("cmpltvideopath",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getVideoPath()));
			lvrInrJSONObj.put("cmpltstatus",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getStatus()));
			lvrInrJSONObj.put("cmpltentryby",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getEntryBy()));
			lvrInrJSONObj.put("cmpltclosereason",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getClosereason()));
			lvrInrJSONObj.put("cmpltotheremail",Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getComplaintsOthersEmail()));
			lvrEntrydate = lvrCmplttblvoobj.getEntryDatetime();
			lvrInrJSONObj.put("cmpltentrydate", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "dd-MM-yyyy HH:mm:ss"));
			lvrModifydate =lvrCmplttblvoobj.getModifyDatetime();
			lvrInrJSONObj.put("cmpltmodifydate", locCommonObj.getDateobjtoFomatDateStr(lvrModifydate, "dd-MM-yyyy HH:mm:ss"));
			
			locAttachFnameSLqry="from ComplaintattchTblVO where ivrBnCOMPLAINTS_ID ="+Commonutility.toCheckNullEmpty(lvrCmplttblvoobj.getComplaintsId())+"";			 
			// lvrQryToidqry=locHbsession.createQuery(locAttachFnameSLqry);			
			 //locCmpltAttchfnameObj=(ComplaintattchTblVO) lvrQryToidqry.uniqueResult();	
			 locObjfilelst_itr=locHbsession.createQuery(locAttachFnameSLqry).list().iterator();	
				//System.out.println("Step 3 : Select ComplaintattchTblVO files detail query executed.");
				locLBRFILEJSONAryobj=new JSONArray();
				locLBRFILEJSONAryobj1=new JSONArray();
				while (locObjfilelst_itr!=null &&  locObjfilelst_itr.hasNext() ) {
					locfiledbtbl=(ComplaintattchTblVO)locObjfilelst_itr.next();
					locLBRFILEOBJJSONAryobj=new JSONObject();
					locLBRFILEOBJJSONAryobj1=new JSONObject();
						
					if(locfiledbtbl.getIvrBnATTCH_ID()!=null && locfiledbtbl.getIvrBnFILE_TYPE() == 1){
						locLBRFILEOBJJSONAryobj1.put("img_id", Commonutility.toCheckNullEmpty(locfiledbtbl.getIvrBnATTCH_ID()));
						locLBRFILEOBJJSONAryobj1.put("img_url", Commonutility.toCheckNullEmpty(filepathweb+lvrCmplttblvoobj.getComplaintsId()+"/"+locfiledbtbl.getIvrBnATTACH_NAME()));
						locLBRFILEJSONAryobj1.put(locLBRFILEOBJJSONAryobj1);
					}
					
					
					
					if(locfiledbtbl.getIvrBnATTCH_ID()!=null && locfiledbtbl.getIvrBnFILE_TYPE() == 2){
						locLBRFILEOBJJSONAryobj.put("video_id", Commonutility.toCheckNullEmpty(locfiledbtbl.getIvrBnATTCH_ID()));
						locLBRFILEOBJJSONAryobj.put("thumb_img", Commonutility.toCheckNullEmpty(filepathweb+lvrCmplttblvoobj.getComplaintsId()+"/"+locfiledbtbl.getThumbImage()));
						locLBRFILEOBJJSONAryobj.put("video_url", Commonutility.toCheckNullEmpty(filepathweb+lvrCmplttblvoobj.getComplaintsId()+"/"+locfiledbtbl.getThumbImage()));
						locLBRFILEJSONAryobj.put(locLBRFILEOBJJSONAryobj);
					}
					
					
				}
				lvrInrJSONObj.put("images", locLBRFILEJSONAryobj1);
				lvrInrJSONObj.put("videos", locLBRFILEJSONAryobj);
				
			lvrComplaintjsonaryobj.put(lvrInrJSONObj);
			lvrInrJSONObj = null;
		}
		
		
		lvrRtnjsonobj=new JSONObject();	
		lvrRtnjsonobj.put("InitCount", count);
		lvrRtnjsonobj.put("countAfterFilter", countFilter);
		lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
		lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
		lvrRtnjsonobj.put("cmpltntdetails", lvrComplaintjsonaryobj);
		lvrRtnjsonobj.put("timestamp",locTimeStamp);
		Commonutility.toWriteConsole("Step 4 : Select Complaint process End");
		logWrite.logMessage("Step 4 : Select Complaint process End", "info", Complaintsviewall.class);
    return lvrRtnjsonobj;
    }catch(Exception expObj) {
    	try {
			Commonutility.toWriteConsole("Exception in toComplaintselect() : "+expObj);
			logWrite.logMessage("Step -1 : Complaint select all block Exception : "+expObj, "error", Complaintsviewall.class);	
			lvrRtnjsonobj=new JSONObject();
			lvrRtnjsonobj.put("InitCount", count);
			lvrRtnjsonobj.put("countAfterFilter", countFilter);
			lvrRtnjsonobj.put("labordetails", "");
			lvrRtnjsonobj.put("CatchBlock", "Error");
			Commonutility.toWriteConsole("lvrRtnjsonobj : "+lvrRtnjsonobj);
			}catch(Exception ee){}finally{}
     return lvrRtnjsonobj;
    }finally {
    	if(locHbsession!=null){locHbsession.flush();locHbsession.clear();locHbsession.close();locHbsession=null;}
    	lvrRtnjsonobj = null;
    	lvrInrJSONObj = null;	
    	lvrComplaintjsonaryobj = null;
    	logWrite = null;
    	locCommonObj = null;
    	lvrEntrydate = null;
    	lvrModifydate = null;
    	lvrObjcmpltlstitr = null;
    	lvrCmplttblvoobj = null;
    	lvrcmpltcountqry = null;locvrComplaintSTS = null; locvrCntflg = null; locvrFlterflg = null; locStrRow = null; locMaxrow = null; locSrchdtblsrch = null;loccrntusrloginid=null;	
    	count=0; countFilter = 0; startrowfrom = 1; totalNorow = 0;
    	lvSlqry = null;locLabnameObj=null;locMrchnameObj=null;
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
