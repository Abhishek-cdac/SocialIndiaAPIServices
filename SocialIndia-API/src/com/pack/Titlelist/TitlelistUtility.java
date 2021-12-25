package com.pack.Titlelist;

import java.util.ResourceBundle;

import org.json.JSONObject;

import com.pack.Worktypelistvo.persistance.WorktypeDao;
import com.pack.Worktypelistvo.persistance.WorktypeDaoservice;
import com.pack.commonvo.MvpTitleMstTbl;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class TitlelistUtility {

	public static String toInsertTitle(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrtitletitle = null, lvrtitledesc = null, lvrtitleshdesc = null,lvrtitlestatus = null,lvrtitlefiledata=null,lvrtitlefilename=null;
		String lvrexistname = null, lvrexistnamedata = null, lvrtitleimgscnd = null, lvrtitleimgscnddata = null, lvrtitleimgtrd = null, lvrtitleimgtrddata = null, lvrtitleimgfrth = null, lvrtitleimgfrthdata = null ;
		int loctitleid = 0;
		Log logwrite = null;
		CommonUtils locCommutillObj = null;
		GroupMasterTblVo locGrpmstvoobj = null;
		UserMasterTblVo locUammstrvoobj = null;
		MvpTitleMstTbl titleTblObj=null;
		WorktypeDao lvrtitledaoobj=null;
		try {			
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			titleTblObj= new MvpTitleMstTbl();
			logwrite.logMessage("Step 2 : Work Type Insert Block.", "info", TitlelistUtility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrtitletitle = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "titlename");
			lvrtitlestatus = "1";
			titleTblObj.setDescription(lvrtitletitle);
			titleTblObj.setStatus(Integer.parseInt(lvrtitlestatus));
			
			//----------- title Insert Start-----------			
			logwrite.logMessage("Step 3 : title Detail insert will start.", "info", TitlelistUtility.class);
			lvrtitledaoobj = new WorktypeDaoservice();
			lvrexistname = lvrtitledaoobj.toExistTitlelistname(lvrtitletitle);
			if (lvrtitletitle != null && lvrtitletitle.length() > 0 && lvrexistname.equalsIgnoreCase("NEW")){
			loctitleid = lvrtitledaoobj.toInserttitle(titleTblObj);
			}
			System.out.println(loctitleid+": id title");
			logwrite.logMessage("Step 4 : title Detail insert End title Id : "+loctitleid, "info", TitlelistUtility.class);
			// -----------title Insert end------------		
			if (loctitleid>0) {	
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="INSERT INTO MVP_TITLE_MST_TBL (TITLE_ID, DESCRIPTION, STATUS) VALUES("+loctitleid+", '"+lvrtitletitle+"', 1);";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr = "success!_!"+loctitleid;
			}
			else if(lvrexistname.equalsIgnoreCase("ALREADY EXISTS"))
			{
				locFtrnStr = "input!_!"+lvrexistname;
			}
			else{
				locFtrnStr = "error!_!"+loctitleid;
			}
			logwrite.logMessage("Step 7 : title Insert Block End.", "info", TitlelistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EtitleUtility.toInserttitle() : "+e);
			logwrite.logMessage("Exception found in EtitleUtility.toInserttitle() : "+e, "error", TitlelistUtility.class);
			locFtrnStr="error!_!"+loctitleid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; locGrpmstvoobj = null; locUammstrvoobj = null; titleTblObj = null; lvrtitledaoobj = null;
			locFtrnStr = null;lvrtitledaoobj=null;lvrtitlefiledata=null;titleTblObj=null;lvrtitleshdesc = null;
		}
	}
	

	
	public static String toDeactiveTitle(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrtitlestatus = null, lvrtitleid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean titleUpdsts = false;
		String locUpdQry=null;
		WorktypeDao lvrtitledaoobj = null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : title Deactive Block Start.", "info", TitlelistUtility.class);
			
			lvrtitleid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "titleid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrtitlestatus = "0";
			locUpdQry = "update MvpTitleMstTbl set status ="+Integer.parseInt(lvrtitlestatus)+" where titleId ="+Integer.parseInt(lvrtitleid)+"";
			logwrite.logMessage("Step 3 : title Deactive Update Query : "+locUpdQry, "info", TitlelistUtility.class);
			lvrtitledaoobj = new WorktypeDaoservice();
			titleUpdsts = lvrtitledaoobj.toDeactivateWorkType(locUpdQry);
			
			if(titleUpdsts){
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="update mvp_title_mst_tbl set STATUS ="+Integer.parseInt(lvrtitlestatus)+" where TITLE_ID ="+Integer.parseInt(lvrtitleid)+";";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : stafftitle Deactive Block End.", "info", TitlelistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in toDeactivetitle() : "+e);
			logwrite.logMessage("Exception found in toDeactivetitle() : "+e, "error", TitlelistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrtitledaoobj = null;locIdcardstatusval=null;
		}
	}
	
	public static String toactiveTitle(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrtitlestatus = null, lvrtitleid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean titleUpdsts = false;
		String locUpdQry=null;
		WorktypeDao lvrtitledaoobj = null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : title Deactive Block Start.", "info", TitlelistUtility.class);
			
			lvrtitleid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "titleid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrtitlestatus = "1";
			locUpdQry = "update MvpTitleMstTbl set status ="+Integer.parseInt(lvrtitlestatus)+" where titleId ="+Integer.parseInt(lvrtitleid)+"";
			logwrite.logMessage("Step 3 : title Deactive Update Query : "+locUpdQry, "info", TitlelistUtility.class);
			lvrtitledaoobj = new WorktypeDaoservice();
			titleUpdsts = lvrtitledaoobj.toDeactivateWorkType(locUpdQry);
			
			if(titleUpdsts){
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="update mvp_title_mst_tbl set STATUS ="+Integer.parseInt(lvrtitlestatus)+" where TITLE_ID ="+Integer.parseInt(lvrtitleid)+";";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : stafftitle Deactive Block End.", "info", TitlelistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in toDeactivetitle() : "+e);
			logwrite.logMessage("Exception found in toDeactivetitle() : "+e, "error", TitlelistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrtitledaoobj = null;locIdcardstatusval=null;
		}
	}
	
	
}
