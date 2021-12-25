package com.pack.Knownuslist;

import java.util.ResourceBundle;

import org.json.JSONObject;

import com.pack.Worktypelistvo.persistance.WorktypeDao;
import com.pack.Worktypelistvo.persistance.WorktypeDaoservice;
import com.pack.commonvo.KnownusTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class KnownuslistUtility {

	public static String toInsertKnownus(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrknownusknownus = null, lvrknownusdesc = null, lvrknownusshdesc = null,lvrknownusstatus = null,lvrknownusfiledata=null,lvrknownusfilename=null;
		String lvrexistname = null, lvrexistnamedata = null, lvrknownusimgscnd = null, lvrknownusimgscnddata = null, lvrknownusimgtrd = null, lvrknownusimgtrddata = null, lvrknownusimgfrth = null, lvrknownusimgfrthdata = null ;
		int locknownusid = 0;
		Log logwrite = null;
		CommonUtils locCommutillObj = null;
		GroupMasterTblVo locGrpmstvoobj = null;
		UserMasterTblVo locUammstrvoobj = null;
		KnownusTblVO knownusTblObj=null;
		WorktypeDao lvrknownusdaoobj=null;
		try {			
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			knownusTblObj= new KnownusTblVO();
			logwrite.logMessage("Step 2 : Work Type Insert Block.", "info", KnownuslistUtility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrknownusknownus = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "knownusname");
			lvrknownusstatus = "1";
			knownusTblObj.setiVOKNOWNUS(lvrknownusknownus);
			knownusTblObj.setiVOACT_STS(Integer.parseInt(lvrknownusstatus));
			
			//----------- knownus Insert Start-----------			
			logwrite.logMessage("Step 3 : knownus Detail insert will start.", "info", KnownuslistUtility.class);
			lvrknownusdaoobj = new WorktypeDaoservice();
			lvrexistname = lvrknownusdaoobj.toExistKnownuname(lvrknownusknownus);
			if (lvrknownusknownus != null && lvrknownusknownus.length() > 0 && lvrexistname.equalsIgnoreCase("NEW")){
			locknownusid = lvrknownusdaoobj.toInsertknownus(knownusTblObj);
			}
			System.out.println(locknownusid+": id knownus");
			logwrite.logMessage("Step 4 : knownus Detail insert End knownus Id : "+locknownusid, "info", KnownuslistUtility.class);
			// -----------knownus Insert end------------		
			if (locknownusid>0) {	
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="INSERT INTO mvp_knws_tbl (KNOWN_US_ID, KNOWNUS, ACT_STS) VALUES("+locknownusid+", '"+lvrknownusknownus+"', 1);";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr = "success!_!"+locknownusid;
			}
			else if(lvrexistname.equalsIgnoreCase("ALREADY EXISTS"))
			{
				locFtrnStr = "input!_!"+lvrexistname;
			}
			else{
				locFtrnStr = "error!_!"+locknownusid;
			}
			logwrite.logMessage("Step 7 : knownus Insert Block End.", "info", KnownuslistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EknownusUtility.toInsertknownus() : "+e);
			logwrite.logMessage("Exception found in EknownusUtility.toInsertknownus() : "+e, "error", KnownuslistUtility.class);
			locFtrnStr="error!_!"+locknownusid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; locGrpmstvoobj = null; locUammstrvoobj = null; knownusTblObj = null; lvrknownusdaoobj = null;
			locFtrnStr = null;lvrknownusdaoobj=null;lvrknownusfiledata=null;knownusTblObj=null;lvrknownusshdesc = null;
		}
	}
	

	
	public static String toDeactiveKnownus(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrknownusstatus = null, lvrknownusid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean knownusUpdsts = false;
		String locUpdQry=null;
		WorktypeDao lvrknownusdaoobj = null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : knownus Deactive Block Start.", "info", KnownuslistUtility.class);
			
			lvrknownusid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "knownusid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrknownusstatus = "0";
			locUpdQry = "update KnownusTblVO set iVOACT_STS ="+Integer.parseInt(lvrknownusstatus)+" where iVOKNOWN_US_ID ="+Integer.parseInt(lvrknownusid)+"";
			logwrite.logMessage("Step 3 : knownus Deactive Update Query : "+locUpdQry, "info", KnownuslistUtility.class);
			lvrknownusdaoobj = new WorktypeDaoservice();
			knownusUpdsts = lvrknownusdaoobj.toDeactivateWorkType(locUpdQry);
			
			if(knownusUpdsts){
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="update mvp_knws_tbl set ACT_STS ="+Integer.parseInt(lvrknownusstatus)+" where KNOWN_US_ID ="+Integer.parseInt(lvrknownusid)+";";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffknownus Deactive Block End.", "info", KnownuslistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in toDeactiveknownus() : "+e);
			logwrite.logMessage("Exception found in toDeactiveknownus() : "+e, "error", KnownuslistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrknownusdaoobj = null;locIdcardstatusval=null;
		}
	}
	
	public static String toactiveKnownus(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrknownusstatus = null, lvrknownusid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean knownusUpdsts = false;
		String locUpdQry=null;
		WorktypeDao lvrknownusdaoobj = null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : knownus Deactive Block Start.", "info", KnownuslistUtility.class);
			
			lvrknownusid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "knownusid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrknownusstatus = "1";
			locUpdQry = "update KnownusTblVO set iVOACT_STS ="+Integer.parseInt(lvrknownusstatus)+" where iVOKNOWN_US_ID ="+Integer.parseInt(lvrknownusid)+"";
			logwrite.logMessage("Step 3 : knownus Deactive Update Query : "+locUpdQry, "info", KnownuslistUtility.class);
			lvrknownusdaoobj = new WorktypeDaoservice();
			knownusUpdsts = lvrknownusdaoobj.toDeactivateWorkType(locUpdQry);
			
			if(knownusUpdsts){
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="update mvp_knws_tbl set ACT_STS ="+Integer.parseInt(lvrknownusstatus)+" where KNOWN_US_ID ="+Integer.parseInt(lvrknownusid)+";";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffknownus Deactive Block End.", "info", KnownuslistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in toDeactiveknownus() : "+e);
			logwrite.logMessage("Exception found in toDeactiveknownus() : "+e, "error", KnownuslistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrknownusdaoobj = null;locIdcardstatusval=null;
		}
	}
	
	
}
