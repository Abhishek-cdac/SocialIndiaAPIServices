package com.pack.Responsecodelist;

import java.util.ResourceBundle;

import org.json.JSONObject;

import com.mobi.commonvo.ResponseMsgTblVo;
import com.pack.Worktypelistvo.persistance.WorktypeDao;
import com.pack.Worktypelistvo.persistance.WorktypeDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class ResponsecodelistUtility {

	public static String toInsertResponseCode(JSONObject prDatajson){
		String locFtrnStr = null;
		String lvrresponsemsg = null, lvrresponsecodests = null, lvrresponsecodeshdesc = null,lvrresponsecodestatus = null,lvrresponsecodefiledata=null,lvrresponsecodefilename=null;
		String lvrexistname = null;
		int locresponsecodeid = 0;
		Log logwrite = null;
		CommonUtils locCommutillObj = null;
		//GroupMasterTblVo locGrpmstvoobj = null;
		//UserMasterTblVo locUammstrvoobj = null;
		ResponseMsgTblVo responsecodeTblObj=null;
		WorktypeDao lvrresponsecodedaoobj=null;

		try {			
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			responsecodeTblObj= new ResponseMsgTblVo();
			logwrite.logMessage("Step 2 :Response Message Insert Block.", "info", ResponsecodelistUtility.class);
			//lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			//lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrresponsemsg = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "responsemsg");
			lvrresponsecodestatus = "1";
			lvrresponsecodests = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statustype");
			responsecodeTblObj.setMessage(lvrresponsemsg);
			responsecodeTblObj.setStatusCode(lvrresponsecodests);
			responsecodeTblObj.setStatus(Integer.parseInt(lvrresponsecodestatus));
			responsecodeTblObj.setEnrtyDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			//----------- responsecode Insert Start-----------			
			logwrite.logMessage("Step 3 : Response Message Detail insert will start.", "info", ResponsecodelistUtility.class);
			lvrresponsecodedaoobj = new WorktypeDaoservice();
			lvrexistname = lvrresponsecodedaoobj.toExistResponselistname(lvrresponsemsg);
			if (lvrresponsemsg != null && lvrresponsemsg.length() > 0 && lvrexistname.equalsIgnoreCase("NEW")){
				locresponsecodeid = lvrresponsecodedaoobj.toInsertResponseMsg(responsecodeTblObj);
			}
			System.out.println(locresponsecodeid+": id responsecode");
			logwrite.logMessage("Step 4 : Response Message Detail insert End Response Message Id : "+locresponsecodeid, "info", ResponsecodelistUtility.class);
			// -----------responsecode Insert end------------		
			if (locresponsecodeid > 0) {	
				String filepath=rb.getString("external.mobiledbfldr.path");
				String responsecodev = Commonutility.toGenerateID("R",locresponsecodeid,"5");
				String textvalue="INSERT INTO `mvp_response_code_tbl` (`UNIQ_ID`,`STATUS_CODE`, `RESPONSE_CODE`, `MESSAGE`,`STATUS`) VALUES("+locresponsecodeid+",'"+lvrresponsecodests+"', '"+responsecodev+"', '"+lvrresponsemsg+"','1')";
			//	Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr = "success!_!"+locresponsecodeid;
			} else if (lvrexistname.equalsIgnoreCase("ALREADY EXISTS")) {
				locFtrnStr = "input!_!"+lvrexistname;
			} else {
				locFtrnStr = "error!_!"+locresponsecodeid;
			}
			logwrite.logMessage("Step 7 : responsecode Insert Block End.", "info", ResponsecodelistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EresponsecodeUtility.toInsertresponsecode() : "+e);
			logwrite.logMessage("Exception found in EresponsecodeUtility.toInsertresponsecode() : "+e, "error", ResponsecodelistUtility.class);
			locFtrnStr="error!_!"+locresponsecodeid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; responsecodeTblObj = null; lvrresponsecodedaoobj = null;
			locFtrnStr = null;lvrresponsecodedaoobj=null;lvrresponsecodefiledata=null;responsecodeTblObj=null;lvrresponsecodeshdesc = null;
			System.gc();
		}
	}
	

	
	public static String toDeactiveResponseCode(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrresponsecodestatus = null, lvrresponsecodeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean responsecodeUpdsts = false;
		String locUpdQry=null;
		WorktypeDao lvrresponsecodedaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : responsecode Deactive Block Start.", "info", ResponsecodelistUtility.class);
			
			lvrresponsecodeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "responseid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrresponsecodestatus = "0";
			locUpdQry = "update ResponseMsgTblVo set status ="+Integer.parseInt(lvrresponsecodestatus)+" where uniqId ="+Integer.parseInt(lvrresponsecodeid)+"";
			logwrite.logMessage("Step 3 : responsecode Deactive Update Query : "+locUpdQry, "info", ResponsecodelistUtility.class);
			lvrresponsecodedaoobj = new WorktypeDaoservice();
			responsecodeUpdsts = lvrresponsecodedaoobj.toDeactivateWorkType(locUpdQry);
			
			if(responsecodeUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffresponsecode Deactive Block End.", "info", ResponsecodelistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in toDeactiveresponsecode() : "+e);
			logwrite.logMessage("Exception found in toDeactiveresponsecode() : "+e, "error", ResponsecodelistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrresponsecodedaoobj = null;locIdcardstatusval=null;System.gc();
		}
	}
	
	public static String toactiveResponseCode(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrresponsecodestatus = null, lvrresponsecodeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean responsecodeUpdsts = false;
		String locUpdQry=null;
		WorktypeDao lvrresponsecodedaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : responsecode active Block Start.", "info", ResponsecodelistUtility.class);
			
			lvrresponsecodeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "responseid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrresponsecodestatus = "1";
			locUpdQry ="update ResponseMsgTblVo set status ="+Integer.parseInt(lvrresponsecodestatus)+" where uniqId ="+Integer.parseInt(lvrresponsecodeid)+"";
			logwrite.logMessage("Step 3 : responsecode Deactive Update Query : "+locUpdQry, "info", ResponsecodelistUtility.class);
			lvrresponsecodedaoobj = new WorktypeDaoservice();
			responsecodeUpdsts = lvrresponsecodedaoobj.toDeactivateWorkType(locUpdQry);
			
			if(responsecodeUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffresponsecode active Block End.", "info", ResponsecodelistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in toactiveresponsecode() : "+e);
			logwrite.logMessage("Exception found in toactiveresponsecode() : "+e, "error", ResponsecodelistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrresponsecodedaoobj = null;locIdcardstatusval=null;System.gc();
		}
	}
	
	public static String toSelectResponseMsg(JSONObject prDatajson) {// Select on single user data
		String locFtrnStr = null, lvrresponsemsg = null, lvrCrntgrpid = null;
		String lvrresponsestatus = null, lvrresponseid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean responseUpdsts = false;
		String locUpdQry=null;
		WorktypeDao lvrresponsedaoobj = null;
		
		try {
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			logwrite = new Log();
			logwrite.logMessage("Step 2 : response Update Block Start.", "info", ResponsecodelistUtility.class);
			
			lvrresponseid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "editresponseId");
			lvrresponsemsg = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "Responsemsg");
			locUpdQry = "update ResponseMsgTblVo set message ='"+lvrresponsemsg+"' where uniqId ='"+Integer.parseInt(lvrresponseid)+"'";
			logwrite.logMessage("Step 3 : response Deactive Update Query : "+locUpdQry, "info", ResponsecodelistUtility.class);
			lvrresponsedaoobj = new WorktypeDaoservice();
			responseUpdsts = lvrresponsedaoobj.toDeactivateWorkType(locUpdQry);
			
			if(responseUpdsts){
				
				String filepath=rb.getString("external.mobiledbfldr.path");
								String textvalue="update  `mvp_response_code_tbl` set `MESSAGE`=  '"+lvrresponsemsg+"' where UNIQ_ID="+lvrresponseid+" ";
						//		Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : response Update Block End.", "info", ResponsecodelistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in toSelectupdateResponseMsg() : "+e);
			logwrite.logMessage("Exception found in toSelectupdateResponseMsg() : "+e, "error", ResponsecodelistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrresponsedaoobj = null;locIdcardstatusval=null;System.gc();
		}
}
}
