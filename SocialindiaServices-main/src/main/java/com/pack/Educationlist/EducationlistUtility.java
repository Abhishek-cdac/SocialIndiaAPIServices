package com.pack.Educationlist;

import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.Educationlistvo.persistance.EducationDao;
import com.pack.Educationlistvo.persistance.EducationDaoservice;
import com.pack.commonvo.EduMstrTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class EducationlistUtility {

	public static String toInsertEducationtype(JSONObject prDatajson){
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
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			EducationTypeTblObj= new EduMstrTblVO();
			logwrite.logMessage("Step 2 : Education Type Insert Block.", "info", EducationlistUtility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrEducationTypetitle = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "educationtypename");
			lvrEducationTypestatus = "1";
			EducationTypeTblObj.setiVoEDU_NAME(lvrEducationTypetitle);
			EducationTypeTblObj.setiVoACT_STS(Integer.parseInt(lvrEducationTypestatus));
			
			//----------- EducationType Insert Start-----------			
			logwrite.logMessage("Step 3 : EducationType Detail insert will start.", "info", EducationlistUtility.class);
			lvrEducationTypedaoobj = new EducationDaoservice();
			lvrexistname = lvrEducationTypedaoobj.toExistEducationtypelistname(lvrEducationTypetitle);
			if (lvrEducationTypetitle != null && lvrEducationTypetitle.length() > 0 && lvrexistname.equalsIgnoreCase("NEW")){
			locEducationTypeid = lvrEducationTypedaoobj.toInsertEducationType(EducationTypeTblObj);
			}
			System.out.println(locEducationTypeid+": id EducationType");
			logwrite.logMessage("Step 4 : EducationType Detail insert End EducationType Id : "+locEducationTypeid, "info", EducationlistUtility.class);
			// -----------EducationType Insert end------------		
			if (locEducationTypeid>0) {	
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="INSERT INTO `mvp_edu_mst_tbl` (`EDU_ID`,`EDU_NAME`, `ACT_STS`,`ENTRY_BY`) VALUES("+locEducationTypeid+",'"+lvrEducationTypetitle+"', '"+lvrEducationTypestatus+"','"+lvrCrntusrid+"')";
			//	Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr = "success!_!"+locEducationTypeid;
			}
			else if(lvrexistname.equalsIgnoreCase("ALREADY EXISTS"))
			{
				locFtrnStr = "input!_!"+lvrexistname;
			}
			else{
				locFtrnStr = "error!_!"+locEducationTypeid;
			}
			logwrite.logMessage("Step 7 : EducationType Insert Block End.", "info", EducationlistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EEducationTypeUtility.toInsertEducationType() : "+e);
			logwrite.logMessage("Exception found in EEducationTypeUtility.toInsertEducationType() : "+e, "error", EducationlistUtility.class);
			locFtrnStr="error!_!"+locEducationTypeid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; locGrpmstvoobj = null; locUammstrvoobj = null; EducationTypeTblObj = null; lvrEducationTypedaoobj = null;
			locFtrnStr = null;lvrEducationTypedaoobj=null;lvrEducationTypefiledata=null;EducationTypeTblObj=null;lvrEducationTypeshdesc = null;
		}
	}
	

	
	public static String toDeactivedoctype(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrEducationTypestatus = null, lvrEducationTypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean EducationTypeUpdsts = false;
		String locUpdQry=null;
		EducationDao lvrEducationTypedaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : EducationType Deactive Block Start.", "info", EducationlistUtility.class);
			
			lvrEducationTypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "educationtypeid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrEducationTypestatus = "0";
			locUpdQry = "update EduMstrTblVO set iVoACT_STS ="+Integer.parseInt(lvrEducationTypestatus)+" where iVoEDU_ID ="+Integer.parseInt(lvrEducationTypeid)+"";
			logwrite.logMessage("Step 3 : EducationType Deactive Update Query : "+locUpdQry, "info", EducationlistUtility.class);
			lvrEducationTypedaoobj = new EducationDaoservice();
			EducationTypeUpdsts = lvrEducationTypedaoobj.toDeactivateEducationType(locUpdQry);
			
			if(EducationTypeUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffEducationType Deactive Block End.", "info", EducationlistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in CommitteeUtility.toDeactiveEducationType() : "+e);
			logwrite.logMessage("Exception found in CommitteeUtility.toDeactiveEducationType() : "+e, "error", EducationlistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrEducationTypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	public static String toactivedoctype(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrEducationTypestatus = null, lvrEducationTypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean EducationTypeUpdsts = false;
		String locUpdQry=null;
		EducationDao lvrEducationTypedaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : EducationType Deactive Block Start.", "info", EducationlistUtility.class);
			
			lvrEducationTypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "educationtypeid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrEducationTypestatus = "1";
			locUpdQry = "update EduMstrTblVO set iVoACT_STS ="+Integer.parseInt(lvrEducationTypestatus)+" where iVoEDU_ID ="+Integer.parseInt(lvrEducationTypeid)+"";
			logwrite.logMessage("Step 3 : EducationType Deactive Update Query : "+locUpdQry, "info", EducationlistUtility.class);
			lvrEducationTypedaoobj = new EducationDaoservice();
			EducationTypeUpdsts = lvrEducationTypedaoobj.toDeactivateEducationType(locUpdQry);
			
			if(EducationTypeUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffEducationType Deactive Block End.", "info", EducationlistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in StaffdoctypeUtility.toDeactiveEducationType() : "+e);
			logwrite.logMessage("Exception found in StaffdoctypeUtility.toDeactiveEducationType() : "+e, "error", EducationlistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrEducationTypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	
}
