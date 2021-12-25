package com.pack.Docconfiglist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.json.JSONObject;

import com.pack.Docconfiglistvo.persistance.DocConfigDao;
import com.pack.Docconfiglistvo.persistance.DocConfigDaoservice;
import com.pack.commonvo.DoctypMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class DocConfiglistUtility {
	
	public static String toInsertDocumenttype(JSONObject prDatajson){
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrdoctypetypetitle = null, lvrdoctypetypedesc = null, lvrdoctypetypeshdesc = null,lvrdoctypetypestatus = null,lvrdoctypetypefiledata=null,lvrdoctypetypefilename=null;
		String lvrexistname = null, lvrexistnamedata = null, lvrdoctypetypeimgscnd = null, lvrdoctypetypeimgscnddata = null, lvrdoctypetypeimgtrd = null, lvrdoctypetypeimgtrddata = null, lvrdoctypetypeimgfrth = null, lvrdoctypetypeimgfrthdata = null ;
		int locdoctypetypeid = 0;
		Log logwrite = null;
		CommonUtils locCommutillObj = null;
		GroupMasterTblVo locGrpmstvoobj = null;
		UserMasterTblVo locUammstrvoobj = null;
		DoctypMasterTblVO doctypetypeTblObj=null;
		DocConfigDao lvrdoctypetypedaoobj=null;
		try {			
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			doctypetypeTblObj= new DoctypMasterTblVO();
			logwrite.logMessage("Step 2 : Document type Insert Block.", "info", DocConfiglistUtility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrdoctypetypetitle = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "documenttypename");
			lvrdoctypetypestatus = "1";
			doctypetypeTblObj.setIvrBnDOC_TYP_NAME(lvrdoctypetypetitle);
			doctypetypeTblObj.setIvrBnDESCR("NULL");
			doctypetypeTblObj.setIvrBnACT_STS(Integer.parseInt(lvrdoctypetypestatus));
			doctypetypeTblObj.setIvrBnENTRY_BY(Integer.parseInt(lvrCrntusrid));
			doctypetypeTblObj.setIvrBnENTRY_DATETIME(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			//----------- doctypetype Insert Start-----------			
			logwrite.logMessage("Step 3 : doctypetype Detail insert will start.", "info", DocConfiglistUtility.class);
			lvrdoctypetypedaoobj = new DocConfigDaoservice();
			lvrexistname = lvrdoctypetypedaoobj.toExistDocumenttypelistname(lvrdoctypetypetitle);
			if (lvrdoctypetypetitle != null && lvrdoctypetypetitle.length() > 0 && lvrexistname.equalsIgnoreCase("NEW")){
			locdoctypetypeid = lvrdoctypetypedaoobj.toInsertdoctypetype(doctypetypeTblObj);
			}
			System.out.println(locdoctypetypeid+": id doctypetype");
			logwrite.logMessage("Step 4 : doctypetype Detail insert End doctypetype Id : "+locdoctypetypeid, "info", DocConfiglistUtility.class);
			// -----------doctypetype Insert end------------		
			if (locdoctypetypeid>0) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		         String strDate = dateFormat.format(date);
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="INSERT INTO mvp_docmstr_tbl (DOC_TYP_ID,DOC_TYP_NAME,DESCR,ENTRY_BY,ACT_STS, ENTRY_DATETIME,MODIFY_DATETIME) VALUES("+locdoctypetypeid+",'"+lvrdoctypetypetitle+"', NULL,"+Integer.parseInt(lvrCrntusrid)+","+Integer.parseInt(lvrdoctypetypestatus)+",'"+strDate+"','"+strDate+"');";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr = "success!_!"+locdoctypetypeid;
			}
			else if(lvrexistname.equalsIgnoreCase("ALREADY EXISTS"))
			{
				locFtrnStr = "input!_!"+lvrexistname;
			}
			else{
				locFtrnStr = "error!_!"+locdoctypetypeid;
			}
			logwrite.logMessage("Step 7 : doctypetype Insert Block End.", "info", DocConfiglistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EdoctypetypeUtility.toInsertdoctypetype() : "+e);
			logwrite.logMessage("Exception found in EdoctypetypeUtility.toInsertdoctypetype() : "+e, "error", DocConfiglistUtility.class);
			locFtrnStr="error!_!"+locdoctypetypeid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; locGrpmstvoobj = null; locUammstrvoobj = null; doctypetypeTblObj = null; lvrdoctypetypedaoobj = null;
			locFtrnStr = null;lvrdoctypetypedaoobj=null;lvrdoctypetypefiledata=null;doctypetypeTblObj=null;lvrdoctypetypeshdesc = null;
		}
	}
	

	
	public static String toDeactivedoctype(JSONObject prDatajson){
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrdoctypetypestatus = null, lvrdoctypetypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean doctypetypeUpdsts = false;
		String locUpdQry=null;
		DocConfigDao lvrdoctypetypedaoobj = null;
		String textvalue=null;
		String filepath=null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : doctypetype Deactive Block Start.", "info", DocConfiglistUtility.class);
			
			lvrdoctypetypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "documenttypeid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrdoctypetypestatus = "0";
			locUpdQry = "update DoctypMasterTblVO set ivrBnACT_STS ="+Integer.parseInt(lvrdoctypetypestatus)+" where DOC_TYP_ID ="+Integer.parseInt(lvrdoctypetypeid)+"";
			logwrite.logMessage("Step 3 : doctypetype Deactive Update Query : "+locUpdQry, "info", DocConfiglistUtility.class);
			lvrdoctypetypedaoobj = new DocConfigDaoservice();
			doctypetypeUpdsts = lvrdoctypetypedaoobj.toDeactivatedoctypetype(locUpdQry);
			
			if(doctypetypeUpdsts){
				filepath=rb.getString("external.mobiledbfldr.path");
				textvalue="update mvp_docmstr_tbl set ACT_STS ="+Integer.parseInt(lvrdoctypetypestatus)+" where DOC_TYP_ID ="+Integer.parseInt(lvrdoctypetypeid)+";";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffdoctypetype Deactive Block End.", "info", DocConfiglistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in CommitteeUtility.toDeactivedoctypetype() : "+e);
			logwrite.logMessage("Exception found in CommitteeUtility.toDeactivedoctypetype() : "+e, "error", DocConfiglistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrdoctypetypedaoobj = null;locIdcardstatusval=null;textvalue=null;filepath=null;
		}
	}
	
	public static String toactivedoctype(JSONObject prDatajson){
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrdoctypetypestatus = null, lvrdoctypetypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean doctypetypeUpdsts = false;
		String locUpdQry=null;
		DocConfigDao lvrdoctypetypedaoobj = null;
		String textvalue=null;
		String filepath=null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : doctypetype Deactive Block Start.", "info", DocConfiglistUtility.class);
			
			lvrdoctypetypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "documenttypeid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrdoctypetypestatus = "1";
			locUpdQry = "update DoctypMasterTblVO set ivrBnACT_STS ="+Integer.parseInt(lvrdoctypetypestatus)+" where ivrBnDOC_TYP_ID ="+Integer.parseInt(lvrdoctypetypeid)+"";
			logwrite.logMessage("Step 3 : doctypetype Deactive Update Query : "+locUpdQry, "info", DocConfiglistUtility.class);
			lvrdoctypetypedaoobj = new DocConfigDaoservice();
			doctypetypeUpdsts = lvrdoctypetypedaoobj.toDeactivatedoctypetype(locUpdQry);		
			if(doctypetypeUpdsts){
				filepath=rb.getString("external.mobiledbfldr.path");
				textvalue="update mvp_docmstr_tbl set ACT_STS ="+Integer.parseInt(lvrdoctypetypestatus)+" where DOC_TYP_ID ="+Integer.parseInt(lvrdoctypetypeid)+";";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffdoctypetype Deactive Block End.", "info", DocConfiglistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in StaffdoctypeUtility.toDeactivedoctypetype() : "+e);
			logwrite.logMessage("Exception found in StaffdoctypeUtility.toDeactivedoctypetype() : "+e, "error", DocConfiglistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrdoctypetypedaoobj = null;locIdcardstatusval=null;textvalue=null;filepath=null;
		}
	}
	
	
}
