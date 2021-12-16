package com.pack.DisputeAdminlist;

import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

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


public class DisputeAdminlistUtility {

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
	

	
	public static String toCloseddisputeadmin(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrCmpltstatus = null, lvrreason=null,lvrCmpltid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean CmpltUpdsts = false;
		String locUpdQry=null;
		DisputemerchantDao lvrCmpltdaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : Cmplt closed Block Start.", "info", DisputeAdminlistUtility.class);
			
			lvrCmpltid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tomerchantid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrreason = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "reason");
			lvrCmpltstatus = "0";
			locUpdQry = "update DisputeRiseTbl set status ="+Integer.parseInt(lvrCmpltstatus)+" ,disputeclosereason='"+lvrreason+"',usrRegTbl="+lvrCrntusrid+" where disputeId ="+Integer.parseInt(lvrCmpltid)+"";
			logwrite.logMessage("Step 3 : Cmplt closed Update Query : "+locUpdQry, "info", DisputeAdminlistUtility.class);
			lvrCmpltdaoobj = new DisputemerchantDaoservice();
			CmpltUpdsts = lvrCmpltdaoobj.toClosedCmplt(locUpdQry);
			
			if(CmpltUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffCmplt closed Block End.", "info", DisputeAdminlistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in DisputeAdminlistUtility.toclosedCmplt() : "+e);
			logwrite.logMessage("Exception found in DisputeAdminlistUtility.toclosedCmplt() : "+e, "error", DisputeAdminlistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrCmpltdaoobj = null;locIdcardstatusval=null;
		}
	}
	public static String toDeletedisputeadmin(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		Log logwrite = null;
		boolean disputeDlsts = false;
		boolean disputeImgDlsts = false;
		String locDlQry=null,locDldispQry=null,lvrCmpltid = null;
		DisputemerchantDao lvrdisputedaoobj = null;
		EeNewsDao lvrENewsImgUpdaSts = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : dispute Delete Block Start.", "info", DisputeAdminlistUtility.class);
			lvrdisputedaoobj = new DisputemerchantDaoservice();
			lvrCmpltid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tomerchantid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locDlQry = "delete DisputeRiseTbl where disputeId ="+Integer.parseInt(lvrCmpltid)+"";
			logwrite.logMessage("Step 3 : dispute Delete Query : "+locDlQry, "info", DisputeAdminlistUtility.class);
			
			disputeDlsts = lvrdisputedaoobj.toDeletedispute(locDlQry);
			if(disputeDlsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : dispute Delete Block End.", "info", DisputeAdminlistUtility.class);					
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in DisputeAdminlistUtility.toDeletedispute() : "+e);
			logwrite.logMessage("Exception found in DisputeAdminlistUtility.toDeletedispute() : "+e, "error", DisputeAdminlistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrdisputedaoobj = null;lvrENewsImgUpdaSts=null;
		}
	}
	
	public static JSONObject toadmincmpltview(JSONObject prDatajson){
		String lvrCrntusrid = null, lvrCrntgrpid = null;
		String locSlQry=null,lvrdisputeid = null,locMrchSLqry=null,loc_slQry_file=null,dispute_tomerchname=null;
		Query lvrQrybj = null,locMrchQryrst=null;
		Log logwrite = null;
		Date lvrEntrydate=null;
		JSONObject locRtndatajson = null;
		Session locHbsession = null;
		Common locCommonObj = null;
		DisputeRiseTbl lvrEenesvoobj = null;
		Iterator locObjfilelst_itr=null;
		JSONArray locLBRFILEJSONAryobj=null;
		JSONObject locLBRFILEOBJJSONAryobj=null;
		EeNewsDocTblVO locfiledbtbl=null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		UserMasterTblVo locMrchMstrVOobj=null,locMrchMstrvo=null;
		try {
			logwrite = new Log();
			locRtndatajson = new JSONObject();
			locCommonObj = new CommonDao();
			locHbsession = HibernateUtil.getSession();
			logwrite.logMessage("Step 2 : Toadmin Single Select Block Start.", "info", DisputeAdminlistUtility.class);
			lvrdisputeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "tomerchantid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			locSlQry = "from DisputeRiseTbl where disputeId =" +Integer.parseInt(lvrdisputeid) +"";
			logwrite.logMessage("Step 3 : Toadmin Single Select Query : "+locSlQry, "info", DisputeAdminlistUtility.class);
			lvrQrybj = locHbsession.createQuery(locSlQry);
			lvrEenesvoobj = (DisputeRiseTbl)lvrQrybj.uniqueResult();
			logwrite.logMessage("Step 4 : Toadmin JSONData will form.", "info", DisputeAdminlistUtility.class);
			locRtndatajson.put("disp_id", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getDisputeId()));
			locRtndatajson.put("disp_title", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getDisputeTitle()));
			locRtndatajson.put("disp_desc", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getDisputeDesc()));
			locRtndatajson.put("disp_toid", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getDisputeT0Id()));
			// Get User Details
			if (lvrEenesvoobj.getUsrRegTbl() != null) {
				locRtndatajson.put("disp_entbyresidentid", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getUsrRegTbl().getUserId()));
				locRtndatajson.put("disp_entbyresidentfname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getUsrRegTbl().getFirstName()));
				locRtndatajson.put("disp_entbyresidentlname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getUsrRegTbl().getLastName()));
				locRtndatajson.put("disp_entbyresidentemail", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getUsrRegTbl().getEmailId()));
				locRtndatajson.put("disp_entbyresidentmobno", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getUsrRegTbl().getMobileNo()));
				locRtndatajson.put("disp_entbyresidentisdcode", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getUsrRegTbl().getIsdCode()));
				locRtndatajson.put("disp_entbyresidentimgname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getUsrRegTbl().getImageNameWeb()));
				locRtndatajson.put("disp_entbyresidentusrname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getUsrRegTbl().getUserName()));
				if (lvrEenesvoobj.getUsrRegTbl().getSocietyId() != null) {
					locRtndatajson.put("disp_entbyresidentsocietyid", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getUsrRegTbl().getSocietyId().getSocietyId()));
					locRtndatajson.put("disp_entbyresidentsocietyname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getUsrRegTbl().getSocietyId().getSocietyName()));
					locRtndatajson.put("disp_entbyresidenttownshipname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getUsrRegTbl().getSocietyId().getTownshipId().getTownshipName()));
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
			String merchrentid =Commonutility.toCheckNullEmpty(lvrEenesvoobj.getDisputeT0Id());
			 locMrchSLqry="from UserMasterTblVo where userId ="+merchrentid+"";			 
			 locMrchQryrst=locHbsession.createQuery(locMrchSLqry);			
			 locMrchMstrVOobj=(UserMasterTblVo) locMrchQryrst.uniqueResult();
			 if(locMrchMstrVOobj!=null){
				 locMrchMstrvo=new UserMasterTblVo();
				 dispute_tomerchname=locMrchMstrVOobj.getUserName();
				 locRtndatajson.put("disp_toadminname",Commonutility.toCheckNullEmpty(dispute_tomerchname));					 
			 }
			lvrEntrydate=lvrEenesvoobj.getEntryDatetime();
			locRtndatajson.put("eveentrydate", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "yyyy-MM-dd HH:mm:ss"));
			logwrite.logMessage("Step 5 : Toadmin Single Select Block End.", "info", DisputeAdminlistUtility.class);
			logwrite.logMessage("Step 6: Select disp_ files detail block start.", "info",DisputeAdminlistUtility.class);
			
			locRtndatajson.put("jArry_doc_files", locLBRFILEJSONAryobj);
			System.out.println("Step 5 : Return JSON DATA : "+locRtndatajson);						
			System.out.println("Step 6 : Select disp_docment detail block end.");
			return locRtndatajson;
		}catch(Exception e) {
			try{
			System.out.println("Step -1 : Exception found in DisputeMerchantlistUtility.toSelectSngleToadmin() : "+e);
			logwrite.logMessage("Step -1 : Exception found in DisputeMerchantlistUtility.toSelectSngleToadmin() : "+e, "error", DisputeAdminlistUtility.class);
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
