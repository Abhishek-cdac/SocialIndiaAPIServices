package com.pack.DisputeMerchantlist;

import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.DisputeAdminlist.DisputeAdminlistUtility;
import com.pack.DisputeMerchantlistvo.persistance.DisputemerchantDao;
import com.pack.DisputeMerchantlistvo.persistance.DisputemerchantDaoservice;
import com.pack.enewsvo.EeNewsDocTblVO;
import com.pack.enewsvo.persistence.EeNewsDao;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.vo.DisputeRiseTbl;
import com.socialindiaservices.vo.MerchantIssuePostingTblVO;
import com.socialindiaservices.vo.MerchantIssueTblVO;
import com.socialindiaservices.vo.MerchantTblVO;

public class DisputeMerchantlistUtility {

	public static String toInsertComplaint(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrCmplttitle = null, lvrCmpltdesc = null, lvrCmpltshdesc = null,lvrCmpltstatus = null,lvrCmplttoid=null,lvrCmplttogrpid=null;
		String lvrexistname = null, lvrexistnamedata = null, lvrCmpltimgscnd = null, lvrCmpltimgscnddata = null, lvrCmpltimgtrd = null, lvrCmpltimgtrddata = null, lvrCmpltimgfrth = null, lvrCmpltimgfrthdata = null ;
		int locCmpltid = 0;
		Log logwrite = null;
		CommonUtils locCommutillObj = null;
		GroupMasterTblVo locGrpmstvoobj = null;
		UserMasterTblVo locUammstrvoobj = null;
		DisputeRiseTbl CmpltTblObj=null;
		DisputemerchantDao lvrCmpltdaoobj=null;
		try {
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			CmpltTblObj= new DisputeRiseTbl();
			logwrite.logMessage("Step 2 : Work Type Insert Block.", "info", DisputeAdminlistUtility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrCmplttitle = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "cmplttitle");
			lvrCmpltdesc = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "cmpltdesc");
			lvrCmplttoid=(String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "cmplttoid");
			lvrCmplttogrpid=(String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "cmplttogrpid");
			lvrCmpltstatus = "1";
			CmpltTblObj.setDisputeTitle(lvrCmplttitle);
			CmpltTblObj.setDisputeDesc(lvrCmpltdesc);
			locUammstrvoobj =new UserMasterTblVo();
			locUammstrvoobj.setUserId(Integer.parseInt(lvrCrntusrid));
			CmpltTblObj.setUsrRegTbl(locUammstrvoobj);
			CmpltTblObj.setDisputeT0Id(Integer.parseInt(lvrCmplttoid));
			CmpltTblObj.setDisputeT0Groupid(Integer.parseInt(lvrCmplttogrpid));
			CmpltTblObj.setStatus(Integer.parseInt(lvrCmpltstatus));
			CmpltTblObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			//----------- Cmplt Insert Start-----------
			logwrite.logMessage("Step 3 : Cmplt Detail insert will start.", "info", DisputeAdminlistUtility.class);
			lvrCmpltdaoobj = new DisputemerchantDaoservice();
			locCmpltid = lvrCmpltdaoobj.toInsertCmplt(CmpltTblObj);

			System.out.println(locCmpltid+": id Cmplt");
			logwrite.logMessage("Step 4 : Cmplt Detail insert End Cmplt Id : "+locCmpltid, "info", DisputeAdminlistUtility.class);
			// -----------Cmplt Insert end------------
			if (locCmpltid>0) {

				locFtrnStr = "success!_!"+locCmpltid;
			}
			else if(lvrexistname.equalsIgnoreCase("ALREADY EXISTS"))
			{
				locFtrnStr = "input!_!"+lvrexistname;
			}
			else{
				locFtrnStr = "error!_!"+locCmpltid;
			}
			logwrite.logMessage("Step 7 : Cmplt Insert Block End.", "info", DisputeAdminlistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in DisputeAdminlistUtility.toInsertCmplt() : "+e);
			logwrite.logMessage("Exception found in DisputeAdminlistUtility.toInsertCmplt() : "+e, "error", DisputeAdminlistUtility.class);
			locFtrnStr="error!_!"+locCmpltid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; locGrpmstvoobj = null; locUammstrvoobj = null; CmpltTblObj = null; lvrCmpltdaoobj = null;
			locFtrnStr = null;lvrCmpltdaoobj=null;lvrCmplttoid=null;CmpltTblObj=null;lvrCmpltshdesc = null;
		}
	}

	public static String toDeletedisputemerchant(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		Log logwrite = null;
		boolean disputeDlsts = false;
		boolean disputeImgDlsts = false;
		String locDlQry=null,locDldispQry=null,lvrCmpltid = null;
		DisputemerchantDao lvrdisputedaoobj = null;
		EeNewsDao lvrENewsImgUpdaSts = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : dispute Delete Block Start.", "info", DisputeMerchantlistUtility.class);
			lvrdisputedaoobj = new DisputemerchantDaoservice();
			lvrCmpltid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tomerchantid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locDlQry = "delete DisputeRiseTbl where disputeId ="+Integer.parseInt(lvrCmpltid)+"";
			logwrite.logMessage("Step 3 : dispute Delete Query : "+locDlQry, "info", DisputeMerchantlistUtility.class);

			disputeDlsts = lvrdisputedaoobj.toDeletedispute(locDlQry);
			if(disputeDlsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : dispute Delete Block End.", "info", DisputeMerchantlistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in DisputeMerchantlistUtility.toDeletedispute() : "+e);
			logwrite.logMessage("Exception found in DisputeMerchantlistUtility.toDeletedispute() : "+e, "error", DisputeMerchantlistUtility.class);
			locFtrnStr="error";
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrdisputedaoobj = null;lvrENewsImgUpdaSts=null;
		}
	}

	public static JSONObject tomerchantcmpltview(JSONObject prDatajson){
		String lvrCrntusrid = null, lvrCrntgrpid = null;
		String locSlQry=null,lvrdisputeid = null,locMrchSLqry=null,loc_slQry_file=null,dispute_tomerchname=null;
		Query lvrQrybj = null,locMrchQryrst=null;
		Log logwrite = null;
		Date lvrEntrydate=null;
		//EeNewsDao lvreNewsdaoobj = null;
		JSONObject locRtndatajson = null;
		Session locHbsession = null;
		Common locCommonObj = null;
		MerchantIssuePostingTblVO lvrEenesvoobj = null;
		Iterator locObjfilelst_itr=null;
		JSONArray locLBRFILEJSONAryobj=null;
		JSONObject locLBRFILEOBJJSONAryobj=null;
		EeNewsDocTblVO locfiledbtbl=null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		MerchantTblVO locMrchMstrVOobj=null,locMrchMstrvo=null;
		try {
			logwrite = new Log();
			locRtndatajson = new JSONObject();
			locCommonObj = new CommonDao();
			locHbsession = HibernateUtil.getSession();
			logwrite.logMessage("Step 2 : Tomerchant Single Select Block Start.", "info", DisputeMerchantlistUtility.class);
			lvrdisputeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tomerchantid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			locSlQry = "from MerchantIssuePostingTblVO where issueId =" +Integer.parseInt(lvrdisputeid) +"";
			logwrite.logMessage("Step 3 : Tomerchant Single Select Query : "+locSlQry, "info", DisputeMerchantlistUtility.class);
			lvrQrybj = locHbsession.createQuery(locSlQry);
			lvrEenesvoobj = (MerchantIssuePostingTblVO)lvrQrybj.uniqueResult();
			logwrite.logMessage("Step 4 : Tomerchant JSONData will form.", "info", DisputeMerchantlistUtility.class);
			locRtndatajson.put("disp_id", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIssueId()));
			locRtndatajson.put("disp_title", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIssueTypes()));
			locRtndatajson.put("disp_desc", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getComments()));
			locRtndatajson.put("disp_toid", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getMrchntId()));
			
			String lvrIssuTypes = "";
			String locMrchSLqry1 = "";
			Query lvrQryIssuetbl=null;
			MerchantIssueTblVO lvrMrchisuetblvo= null;
			if (lvrEenesvoobj.getIssueTypes()!= null && lvrEenesvoobj.getIssueTypes().contains(",")) {
				String splitval[] = lvrEenesvoobj.getIssueTypes().split(",");				
				for (int i = 0; i < splitval.length; i++) {
					locMrchSLqry1="from MerchantIssueTblVO where issueId ="+splitval[i]+"  ";
					lvrQryIssuetbl = locHbsession.createQuery(locMrchSLqry1);
					lvrMrchisuetblvo = (MerchantIssueTblVO) lvrQryIssuetbl.uniqueResult();
					if(lvrMrchisuetblvo!=null){
						 if (Commonutility.checkempty(lvrMrchisuetblvo.getDescription())) {
							 lvrIssuTypes += lvrMrchisuetblvo.getDescription()+", ";
						 }						
					}
				}
				splitval = null;
				if (Commonutility.checkempty(lvrIssuTypes) && lvrIssuTypes.endsWith(", ")) {
					lvrIssuTypes = lvrIssuTypes.substring(0, lvrIssuTypes.length()-2);
				}
			} else {
				if (lvrEenesvoobj.getIssueTypes()!= null ){
					locMrchSLqry1="from MerchantIssueTblVO where issueId ="+lvrEenesvoobj.getIssueTypes()+"  ";
					lvrQryIssuetbl = locHbsession.createQuery(locMrchSLqry1);
					lvrMrchisuetblvo = (MerchantIssueTblVO) lvrQryIssuetbl.uniqueResult();
					if(lvrMrchisuetblvo!=null){
						if (Commonutility.checkempty(lvrMrchisuetblvo.getDescription())) {
						 lvrIssuTypes = lvrMrchisuetblvo.getDescription()+", ";
						}						
					}
				}
			}
			lvrMrchisuetblvo = null;
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
			 locMrchSLqry="from MerchantTblVO where mrchntId ="+merchrentid+"";
			 locMrchQryrst=locHbsession.createQuery(locMrchSLqry);
			 locMrchMstrVOobj=(MerchantTblVO) locMrchQryrst.uniqueResult();
			 if(locMrchMstrVOobj!=null){
				 locMrchMstrvo=new MerchantTblVO();
				 dispute_tomerchname=locMrchMstrVOobj.getMrchntName();
				 locRtndatajson.put("disp_tomerchantname",Commonutility.toCheckNullEmpty(dispute_tomerchname));
			 }
			lvrEntrydate=lvrEenesvoobj.getEntryDatetime();
			locRtndatajson.put("eveentrydate", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "yyyy-MM-dd HH:mm:ss"));
			logwrite.logMessage("Step 5 : Tomerchant Single Select Block End.", "info", DisputeMerchantlistUtility.class);
			logwrite.logMessage("Step 6: Select disp_ files detail block start.", "info",DisputeMerchantlistUtility.class);

			locRtndatajson.put("jArry_doc_files", locLBRFILEJSONAryobj);
			System.out.println("Step 5 : Return JSON DATA : "+locRtndatajson);
			System.out.println("Step 6 : Select disp_docment detail block end.");
			return locRtndatajson;
		}catch(Exception e) {
			try{
			System.out.println("Step -1 : Exception found in DisputeMerchantlistUtility.toSelectSngleTomerchant() : "+e);
			logwrite.logMessage("Step -1 : Exception found in DisputeMerchantlistUtility.toSelectSngleTomerchant() : "+e, "error", DisputeMerchantlistUtility.class);
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
