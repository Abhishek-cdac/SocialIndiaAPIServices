package com.pack.Notificationlist;

import org.json.JSONObject;

import com.pack.Educationlistvo.persistance.EducationDao;
import com.pack.Educationlistvo.persistance.EducationDaoservice;
import com.pack.Notificationlistvo.persistance.NotificationDao;
import com.pack.Notificationlistvo.persistance.NotificationDaoservice;
import com.pack.commonvo.EduMstrTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class NotificationlistUtility {

	/*public static String toInsertEducationtype(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrEducationTypetitle = null, lvrEducationTypedesc = null, lvrEducationTypeshdesc = null,lvrEducationTypestatus = null,lvrEducationTypefiledata=null,lvrEducationTypefilename=null;
		String lvrexistname = null, lvrexistnamedata = null, lvrEducationTypeimgscnd = null, lvrEducationTypeimgscnddata = null, lvrEducationTypeimgtrd = null, lvrEducationTypeimgtrddata = null, lvrEducationTypeimgfrth = null, lvrEducationTypeimgfrthdata = null ;
		int locEducationTypeid = 0;
		Log logwrite = null;
		CommonUtils locCommutillObj = null;
		GroupMasterTblVo locGrpmstvoobj = null;
		UserMasterTblVo locUammstrvoobj = null;
		EduMstrTblVO EducationTypeTblObj=null;
		EducationDao lvrEducationTypedaoobj=null;
		try {			
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			EducationTypeTblObj= new EduMstrTblVO();
			logwrite.logMessage("Step 2 : Education Type Insert Block.", "info", NotificationlistUtility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrEducationTypetitle = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "educationtypename");
			lvrEducationTypestatus = "1";
			EducationTypeTblObj.setiVoEDU_NAME(lvrEducationTypetitle);
			EducationTypeTblObj.setiVoACT_STS(Integer.parseInt(lvrEducationTypestatus));
			
			//----------- EducationType Insert Start-----------			
			logwrite.logMessage("Step 3 : EducationType Detail insert will start.", "info", NotificationlistUtility.class);
			lvrEducationTypedaoobj = new EducationDaoservice();
			lvrexistname = lvrEducationTypedaoobj.toExistEducationtypelistname(lvrEducationTypetitle);
			if (lvrEducationTypetitle != null && lvrEducationTypetitle.length() > 0 && lvrexistname.equalsIgnoreCase("NEW")){
				locEducationTypeid = lvrEducationTypedaoobj.toInsertEducationType(EducationTypeTblObj);
			}
			Commonutility.toWriteConsole(locEducationTypeid+": id EducationType");
			logwrite.logMessage("Step 4 : EducationType Detail insert End EducationType Id : "+locEducationTypeid, "info", NotificationlistUtility.class);
			// -----------EducationType Insert end------------		
			if (locEducationTypeid>0) {	
				
				locFtrnStr = "success!_!"+locEducationTypeid;
			}
			else if(lvrexistname.equalsIgnoreCase("ALREADY EXISTS"))
			{
				locFtrnStr = "input!_!"+lvrexistname;
			}
			else{
				locFtrnStr = "error!_!"+locEducationTypeid;
			}
			logwrite.logMessage("Step 7 : EducationType Insert Block End.", "info", NotificationlistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			Commonutility.toWriteConsole("Exception found in EEducationTypeUtility.toInsertEducationType() : "+e);
			logwrite.logMessage("Exception found in EEducationTypeUtility.toInsertEducationType() : "+e, "error", NotificationlistUtility.class);
			locFtrnStr="error!_!"+locEducationTypeid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; locGrpmstvoobj = null; locUammstrvoobj = null; EducationTypeTblObj = null; lvrEducationTypedaoobj = null;
			locFtrnStr = null;lvrEducationTypedaoobj=null;lvrEducationTypefiledata=null;EducationTypeTblObj=null;lvrEducationTypeshdesc = null;
		}
	}
	*/
	
	public static String toDeactiveNotification(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrNotifyTypestatus = null, lvrNotifyTypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean NotifyTypeUpdsts = false;
		String locUpdQry=null;
		NotificationDao lvrNotifyTypedaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : NotifyType Deactive Block Start.", "info", NotificationlistUtility.class);
			
			lvrNotifyTypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "notificationid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrNotifyTypestatus = "0";
			locUpdQry = "update NotificationTblVO set statusFlag ="+Integer.parseInt(lvrNotifyTypestatus)+" where notificationId ="+Integer.parseInt(lvrNotifyTypeid)+"";
			logwrite.logMessage("Step 3 : NotifyType Deactive Update Query : "+locUpdQry, "info", NotificationlistUtility.class);
			lvrNotifyTypedaoobj = new NotificationDaoservice();
			NotifyTypeUpdsts = lvrNotifyTypedaoobj.toDeactivateNotifyType(locUpdQry);
			
			if(NotifyTypeUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffNotifyType Deactive Block End.", "info", NotificationlistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			Commonutility.toWriteConsole("Exception found in CommitteeUtility.toDeactiveNotifyType() : "+e);
			logwrite.logMessage("Exception found in CommitteeUtility.toDeactiveNotifyType() : "+e, "error", NotificationlistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrNotifyTypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	public static String toReadNotification(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrNotifyTypestatus = null, lvrNotifyTypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean NotifyTypeUpdsts = false;
		String locUpdQry=null;
		NotificationDao lvrNotifyTypedaoobj = null;
		
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : NotifyType Deactive Block Start.", "info", NotificationlistUtility.class);
			
			lvrNotifyTypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "notificationid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrNotifyTypestatus = "0";
			locUpdQry = "update NotificationTblVO set readStatus ="+Integer.parseInt(lvrNotifyTypestatus)+" where notificationId ="+Integer.parseInt(lvrNotifyTypeid)+"";
			logwrite.logMessage("Step 3 : NotifyType Deactive Update Query : "+locUpdQry, "info", NotificationlistUtility.class);
			lvrNotifyTypedaoobj = new NotificationDaoservice();
			NotifyTypeUpdsts = lvrNotifyTypedaoobj.toDeactivateNotifyType(locUpdQry);
			
			if(NotifyTypeUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffNotifyType Deactive Block End.", "info", NotificationlistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			Commonutility.toWriteConsole("Exception found in StaffdoctypeUtility.toDeactiveNotifyType() : "+e);
			logwrite.logMessage("Exception found in StaffdoctypeUtility.toDeactiveNotifyType() : "+e, "error", NotificationlistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrNotifyTypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	
}
