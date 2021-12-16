package com.pack.DisputeLaborlist;

import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.Worktypelistvo.persistance.WorktypeDao;
import com.pack.Worktypelistvo.persistance.WorktypeDaoservice;
import com.pack.commonvo.LaborWrkTypMasterTblVO;
import com.pack.enewsvo.EeNewsDocTblVO;
import com.pack.enewsvo.EeNewsTblVO;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.issuemgmt.persistence.IssueTblVO;
import com.socialindiaservices.vo.DisputeRiseTbl;
import com.socialindiaservices.vo.MerchantIssuePostingTblVO;
import com.socialindiaservices.vo.MerchantTblVO;

public class DisputeLaborlistUtility {

	public static String toInsertWorktype(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrWorkTypetitle = null, lvrWorkTypedesc = null, lvrWorkTypeshdesc = null,lvrWorkTypestatus = null,lvrWorkTypefiledata=null,lvrWorkTypefilename=null;
		String lvrexistname = null, lvrexistnamedata = null, lvrWorkTypeimgscnd = null, lvrWorkTypeimgscnddata = null, lvrWorkTypeimgtrd = null, lvrWorkTypeimgtrddata = null, lvrWorkTypeimgfrth = null, lvrWorkTypeimgfrthdata = null ;
		int locWorkTypeid = 0;
		Log logwrite = null;
		CommonUtils locCommutillObj = null;
		GroupMasterTblVo locGrpmstvoobj = null;
		UserMasterTblVo locUammstrvoobj = null;
		LaborWrkTypMasterTblVO WorkTypeTblObj=null;
		WorktypeDao lvrWorkTypedaoobj=null;
		try {			
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			WorkTypeTblObj= new LaborWrkTypMasterTblVO();
			logwrite.logMessage("Step 2 : Work Type Insert Block.", "info", DisputeLaborlistUtility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrWorkTypetitle = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "worktypename");
			lvrWorkTypestatus = "1";
			WorkTypeTblObj.setIVOlbrWORK_TYPE(lvrWorkTypetitle);
			WorkTypeTblObj.setStatus(Integer.parseInt(lvrWorkTypestatus));
			
			//----------- WorkType Insert Start-----------			
			logwrite.logMessage("Step 3 : WorkType Detail insert will start.", "info", DisputeLaborlistUtility.class);
			lvrWorkTypedaoobj = new WorktypeDaoservice();
			lvrexistname = lvrWorkTypedaoobj.toExistWorktypelistname(lvrWorkTypetitle);
			if (lvrWorkTypetitle != null && lvrWorkTypetitle.length() > 0 && lvrexistname.equalsIgnoreCase("NEW")){
			locWorkTypeid = lvrWorkTypedaoobj.toInsertWorkType(WorkTypeTblObj);
			}
			System.out.println(locWorkTypeid+": id WorkType");
			logwrite.logMessage("Step 4 : WorkType Detail insert End WorkType Id : "+locWorkTypeid, "info", DisputeLaborlistUtility.class);
			// -----------WorkType Insert end------------		
			if (locWorkTypeid>0) {	
				
				locFtrnStr = "success!_!"+locWorkTypeid;
			}
			else if(lvrexistname.equalsIgnoreCase("ALREADY EXISTS"))
			{
				locFtrnStr = "input!_!"+lvrexistname;
			}
			else{
				locFtrnStr = "error!_!"+locWorkTypeid;
			}
			logwrite.logMessage("Step 7 : WorkType Insert Block End.", "info", DisputeLaborlistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EWorkTypeUtility.toInsertWorkType() : "+e);
			logwrite.logMessage("Exception found in EWorkTypeUtility.toInsertWorkType() : "+e, "error", DisputeLaborlistUtility.class);
			locFtrnStr="error!_!"+locWorkTypeid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; locGrpmstvoobj = null; locUammstrvoobj = null; WorkTypeTblObj = null; lvrWorkTypedaoobj = null;
			locFtrnStr = null;lvrWorkTypedaoobj=null;lvrWorkTypefiledata=null;WorkTypeTblObj=null;lvrWorkTypeshdesc = null;
		}
	}
	
	public static String toDeactiveworktype(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrWorkTypestatus = null, lvrWorkTypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean WorkTypeUpdsts = false;
		String locUpdQry=null;
		WorktypeDao lvrWorkTypedaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : WorkType Deactive Block Start.", "info", DisputeLaborlistUtility.class);
			
			lvrWorkTypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "worktypeid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrWorkTypestatus = "0";
			locUpdQry = "update LaborWrkTypMasterTblVO set status ="+Integer.parseInt(lvrWorkTypestatus)+" where wrktypId ="+Integer.parseInt(lvrWorkTypeid)+"";
			logwrite.logMessage("Step 3 : WorkType Deactive Update Query : "+locUpdQry, "info", DisputeLaborlistUtility.class);
			lvrWorkTypedaoobj = new WorktypeDaoservice();
			WorkTypeUpdsts = lvrWorkTypedaoobj.toDeactivateWorkType(locUpdQry);
			
			if(WorkTypeUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffWorkType Deactive Block End.", "info", DisputeLaborlistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in CommitteeUtility.toDeactiveWorkType() : "+e);
			logwrite.logMessage("Exception found in CommitteeUtility.toDeactiveWorkType() : "+e, "error", DisputeLaborlistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrWorkTypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	public static JSONObject tolaborcmpltview(JSONObject prDatajson) {
		String lvrCrntusrid = null, lvrCrntgrpid = null;
		String locSlQry=null,lvrdisputeid = null,locMrchSLqry=null,loc_slQry_file=null,dispute_tomerchname=null;
		Query lvrQrybj = null,locMrchQryrst=null;
		Log logwrite = null;
		Date lvrEntrydate=null;
		//EeNewsDao lvreNewsdaoobj = null;
		JSONObject locRtndatajson = null;
		Session locHbsession = null;
		Common locCommonObj = null;
		//DisputeRiseTbl lvrEenesvoobj = null;
		Iterator locObjfilelst_itr=null;
		JSONArray locLBRFILEJSONAryobj=null;
		JSONObject locLBRFILEOBJJSONAryobj=null;
		EeNewsDocTblVO locfiledbtbl=null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		LaborProfileTblVO locMrchMstrVOobj=null,locMrchMstrvo=null;
		MerchantIssuePostingTblVO lvrEenesvoobj= null;
		try {
			logwrite = new Log();
			locRtndatajson = new JSONObject();
			locCommonObj = new CommonDao();
			locHbsession = HibernateUtil.getSession();
			logwrite.logMessage("Step 2 : Tomerchant Single Select Block Start.", "info", DisputeLaborlistUtility.class);
			lvrdisputeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tomerchantid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			//locSlQry = "from DisputeRiseTbl where disputeId =" +Integer.parseInt(lvrdisputeid) +"";
			locSlQry = "from MerchantIssuePostingTblVO where issueId =" +Integer.parseInt(lvrdisputeid) +"";
			logwrite.logMessage("Step 3 : Tomerchant Single Select Query : "+locSlQry, "info", DisputeLaborlistUtility.class);
			lvrQrybj = locHbsession.createQuery(locSlQry);
			
			lvrEenesvoobj = (MerchantIssuePostingTblVO)lvrQrybj.uniqueResult();
			//lvrEenesvoobj = (DisputeRiseTbl) lvrQrybj.uniqueResult();
			logwrite.logMessage("Step 4 : Tomerchant JSONData will form.", "info", DisputeLaborlistUtility.class);
			locRtndatajson.put("disp_id", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIssueId()));
			locRtndatajson.put("disp_title", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIssueTypes()));
			locRtndatajson.put("disp_desc", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getComments()));
			locRtndatajson.put("disp_toid", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getMrchntId()));
			String lvrIssuTypes = "";
			String locMrchSLqry1 = "";
			Query lvrQryIssuetbl=null;
			IssueTblVO lvrIsuetblvo= null;
			if (lvrEenesvoobj.getIssueTypes()!= null && lvrEenesvoobj.getIssueTypes().contains(",")) {
				String splitval[] = lvrEenesvoobj.getIssueTypes().split(",");
				
				for (int i = 0; i < splitval.length; i++) {
					locMrchSLqry1="from IssueTblVO where issueId ="+splitval[i]+"  ";
					lvrQryIssuetbl = locHbsession.createQuery(locMrchSLqry1);
					lvrIsuetblvo = (IssueTblVO) lvrQryIssuetbl.uniqueResult();
					if(lvrIsuetblvo!=null){
						 if (Commonutility.checkempty(lvrIsuetblvo.getIssueList())) {
							 lvrIssuTypes += lvrIsuetblvo.getIssueList()+", ";
						 }						
					}
				}
				if (Commonutility.checkempty(lvrIssuTypes) && lvrIssuTypes.endsWith(", ")) {
					lvrIssuTypes = lvrIssuTypes.substring(0, lvrIssuTypes.length()-2);
				}
			} else {
				if (lvrEenesvoobj.getIssueTypes()!= null ){
					locMrchSLqry1="from IssueTblVO where issueId ="+lvrEenesvoobj.getIssueTypes()+"  ";
					lvrQryIssuetbl = locHbsession.createQuery(locMrchSLqry1);
					lvrIsuetblvo = (IssueTblVO) lvrQryIssuetbl.uniqueResult();
					if(lvrIsuetblvo!=null){
						if (Commonutility.checkempty(lvrIsuetblvo.getIssueList())) {
						 lvrIssuTypes = lvrIsuetblvo.getIssueList()+", ";
						}						
					}
				}
			}
			
			locRtndatajson.put("disp_isuuetype", lvrIssuTypes);
			
			
			// Get User Details
			if (lvrEenesvoobj.getIssueRaisedBy() != null) {
				locRtndatajson.put("disp_entbyresidentid", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIssueRaisedBy().getUserId()));
				locRtndatajson.put("disp_entbyresidentfname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIssueRaisedBy().getFirstName()));
				locRtndatajson.put("disp_entbyresidentlname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIssueRaisedBy().getLastName()));
				locRtndatajson.put("disp_entbyresidentemail", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIssueRaisedBy().getEmailId()));
				locRtndatajson.put("disp_entbyresidentmobno", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIssueRaisedBy().getMobileNo()));
				locRtndatajson.put("disp_entbyresidentisdcode", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIssueRaisedBy().getIsdCode()));
				locRtndatajson.put("disp_entbyresidentimgname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIssueRaisedBy().getImageNameWeb()));
				locRtndatajson.put("disp_entbyresidentusrname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIssueRaisedBy().getUserName()));
				if (lvrEenesvoobj.getIssueRaisedBy().getSocietyId() != null) {
					locRtndatajson.put("disp_entbyresidentsocietyid", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIssueRaisedBy().getSocietyId().getSocietyId()));
					locRtndatajson.put("disp_entbyresidentsocietyname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIssueRaisedBy().getSocietyId().getSocietyName()));
					locRtndatajson.put("disp_entbyresidenttownshipname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIssueRaisedBy().getSocietyId().getTownshipId().getTownshipName()));
				} else {
					locRtndatajson.put("disp_entbyresidentsocietyid", "");
					locRtndatajson.put("disp_entbyresidentsocietyname", "");
					locRtndatajson.put("disp_entbyresidenttownshipname", "");
				}							
			} else {
				locRtndatajson.put("disp_entbyresidentid", "");
				locRtndatajson.put("disp_entbyresidentfname", "");
				locRtndatajson.put("disp_entbyresidentlname", "");
				locRtndatajson.put("disp_entbyresidentemail", "");
				locRtndatajson.put("disp_entbyresidentmobno", "");
				locRtndatajson.put("disp_entbyresidentisdcode", "");
				locRtndatajson.put("disp_entbyresidentimgname", "");
				
				locRtndatajson.put("disp_entbyresidentsocietyid", "");
				locRtndatajson.put("disp_entbyresidentsocietyname", "");
				locRtndatajson.put("disp_entbyresidenttownshipname", "");
			}
			//get tomerchant name
			String merchrentid =Commonutility.toCheckNullEmpty(lvrEenesvoobj.getMrchntId());
			 locMrchSLqry="from LaborProfileTblVO where ivrBnLBR_ID ="+merchrentid+"";			 
			 locMrchQryrst=locHbsession.createQuery(locMrchSLqry);			
			 locMrchMstrVOobj=(LaborProfileTblVO) locMrchQryrst.uniqueResult();
			 if(locMrchMstrVOobj!=null){
				 locMrchMstrvo=new LaborProfileTblVO();
				 dispute_tomerchname=locMrchMstrVOobj.getIvrBnLBR_NAME();
				 locRtndatajson.put("disp_tolaborname",Commonutility.toCheckNullEmpty(dispute_tomerchname));
				 locRtndatajson.put("disp_tolaborsrvcid",Commonutility.toCheckNullEmpty(locMrchMstrVOobj.getIvrBnLBR_SERVICE_ID()));
				 
			 } else {
				 locRtndatajson.put("disp_tolaborname","");
				 locRtndatajson.put("disp_tolaborsrvcid","");
			 }
			lvrEntrydate=lvrEenesvoobj.getEntryDatetime();
			locRtndatajson.put("eveentrydate", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "yyyy-MM-dd HH:mm:ss"));
			logwrite.logMessage("Step 5 : Tomerchant Single Select Block End.", "info", DisputeLaborlistUtility.class);
			logwrite.logMessage("Step 6: Select disp_ files detail block start.", "info",DisputeLaborlistUtility.class);
			
			locRtndatajson.put("jArry_doc_files", locLBRFILEJSONAryobj);
			System.out.println("Step 5 : Return JSON DATA : "+locRtndatajson);						
			System.out.println("Step 6 : Select disp_docment detail block end.");
			return locRtndatajson;
		}catch(Exception e) {
			try{
			System.out.println("Step -1 : Exception found in DisputeMerchantlistUtility.toSelectSngleTomerchant() : "+e);
			logwrite.logMessage("Step -1 : Exception found in DisputeMerchantlistUtility.toSelectSngleTomerchant() : "+e, "error", DisputeLaborlistUtility.class);
			locRtndatajson=new JSONObject();
			locRtndatajson.put("catch", "Error");
			}catch(Exception ee){}finally{}
			return locRtndatajson;
		}finally {
			if(locHbsession!=null){locHbsession.flush();locHbsession.clear();locHbsession.close();locHbsession = null;}
			logwrite = null; lvrEntrydate=null; locCommonObj = null; lvrEenesvoobj = null;
			lvrCrntusrid = null; lvrCrntgrpid = null;
			locSlQry = null; lvrdisputeid = null; lvrQrybj = null;loc_slQry_file=null;locMrchMstrVOobj=null;locMrchMstrvo=null;locMrchQryrst=null;locMrchSLqry=null;
		}
	}
	
}
