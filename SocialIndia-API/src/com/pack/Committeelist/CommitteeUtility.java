package com.pack.Committeelist;

import java.util.ResourceBundle;

import org.json.JSONObject;

import com.pack.committeelistvo.persistance.CommitteeroleDao;
import com.pack.committeelistvo.persistance.CommitteeroleDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.siservices.committeeMgmt.persistense.CommittteeRoleMstTbl;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class CommitteeUtility {

	public static String toInsertcommitteerole(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrcmtroletypetitle = null, lvrcmtroletypedesc = null, lvrcmtroletypeshdesc = null,lvrcmtroletypestatus = null,lvrcmtroletypefiledata=null,lvrcmtroletypefilename=null;
		String lvrexistname = null, lvrexistnamedata = null, lvrcmtroletypeimgscnd = null, lvrcmtroletypeimgscnddata = null, lvrcmtroletypeimgtrd = null, lvrcmtroletypeimgtrddata = null, lvrcmtroletypeimgfrth = null, lvrcmtroletypeimgfrthdata = null ;
		int loccmtroletypeid = 0;
		Log logwrite = null;
		CommonUtils locCommutillObj = null;
		GroupMasterTblVo locGrpmstvoobj = null;
		UserMasterTblVo locUammstrvoobj = null;
		CommittteeRoleMstTbl cmtroletypeTblObj=null;
		CommitteeroleDao lvrcmtroletypedaoobj=null;
		try {		
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			cmtroletypeTblObj= new CommittteeRoleMstTbl();
			logwrite.logMessage("Step 2 : cmtroletype Insert Block.", "info", CommitteeUtility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrcmtroletypetitle = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "committeename");
			lvrcmtroletypestatus = "1";
			cmtroletypeTblObj.setRoleName(lvrcmtroletypetitle);
			cmtroletypeTblObj.setRoleDescp(lvrcmtroletypetitle);
			cmtroletypeTblObj.setStatus(Integer.parseInt(lvrcmtroletypestatus));
			cmtroletypeTblObj.setEntryBy(Integer.parseInt(lvrCrntusrid));
			
			//----------- cmtroletype Insert Start-----------			
			logwrite.logMessage("Step 3 : staffcmtroletype Detail insert will start.", "info", CommitteeUtility.class);
			lvrcmtroletypedaoobj = new CommitteeroleDaoservice();
			lvrexistname = lvrcmtroletypedaoobj.toExistcommitteelistname(lvrcmtroletypetitle);
			if (lvrcmtroletypetitle != null && lvrcmtroletypetitle.length() > 0 && lvrexistname.equalsIgnoreCase("NEW")){
			loccmtroletypeid = lvrcmtroletypedaoobj.toInsertcmtroletype(cmtroletypeTblObj);
			}
			System.out.println(loccmtroletypeid+": id cmtroletype");
			logwrite.logMessage("Step 4 : cmtroletype Detail insert End cmtroletype Id : "+loccmtroletypeid, "info", CommitteeUtility.class);
			// -----------cmtroletype Insert end------------		
			if (loccmtroletypeid>0) {	
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="INSERT INTO `committtee_role_mst_tbl` (`ROLE_ID`,`ROLE_NAME`, `ROLE_DESCP`,`ENTRY_BY`,`STATUS`) VALUES("+loccmtroletypeid+",'"+lvrcmtroletypetitle+"', '"+lvrcmtroletypetitle+"',"+lvrCrntusrid+",'1')";
			//	Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr = "success!_!"+loccmtroletypeid;
			}
			else if(lvrexistname.equalsIgnoreCase("ALREADY EXISTS"))
			{
				locFtrnStr = "input!_!"+lvrexistname;
			}
			else{
				locFtrnStr = "error!_!"+loccmtroletypeid;
			}
			logwrite.logMessage("Step 7 : cmtroletype Insert Block End.", "info", CommitteeUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EcmtroletypeUtility.toInsertcmtroletype() : "+e);
			logwrite.logMessage("Exception found in EcmtroletypeUtility.toInsertcmtroletype() : "+e, "error", CommitteeUtility.class);
			locFtrnStr="error!_!"+loccmtroletypeid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; locGrpmstvoobj = null; locUammstrvoobj = null; cmtroletypeTblObj = null; lvrcmtroletypedaoobj = null;
			locFtrnStr = null;lvrcmtroletypedaoobj=null;lvrcmtroletypefiledata=null;cmtroletypeTblObj=null;lvrcmtroletypeshdesc = null;
		}
	}
	

	
	public static String toDeactivecmtrole(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrcmtroletypestatus = null, lvrcmtroletypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean cmtroletypeUpdsts = false;
		String locUpdQry=null;
		CommitteeroleDao lvrcmtroletypedaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : staffcmtroletype Deactive Block Start.", "info", CommitteeUtility.class);
			
			lvrcmtroletypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "committeeid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrcmtroletypestatus = "0";
			locUpdQry = "update CommittteeRoleMstTbl set status ="+Integer.parseInt(lvrcmtroletypestatus)+" where roleId ="+Integer.parseInt(lvrcmtroletypeid)+"";
			logwrite.logMessage("Step 3 : staffcmtroletype Deactive Update Query : "+locUpdQry, "info", CommitteeUtility.class);
			lvrcmtroletypedaoobj = new CommitteeroleDaoservice();
			cmtroletypeUpdsts = lvrcmtroletypedaoobj.toDeactivatecmtroletype(locUpdQry);
			
			if(cmtroletypeUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffcmtroletype Deactive Block End.", "info", CommitteeUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in CommitteeUtility.toDeactivecmtroletype() : "+e);
			logwrite.logMessage("Exception found in CommitteeUtility.toDeactivecmtroletype() : "+e, "error", CommitteeUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrcmtroletypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	public static String toactivecmtrole(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrcmtroletypestatus = null, lvrcmtroletypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean cmtroletypeUpdsts = false;
		String locUpdQry=null;
		CommitteeroleDao lvrcmtroletypedaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : staffcmtroletype Deactive Block Start.", "info", CommitteeUtility.class);
			
			lvrcmtroletypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "committeeid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrcmtroletypestatus = "1";
			locUpdQry = "update CommittteeRoleMstTbl set status ="+Integer.parseInt(lvrcmtroletypestatus)+" where roleId ="+Integer.parseInt(lvrcmtroletypeid)+"";
			logwrite.logMessage("Step 3 : cmtroletype Deactive Update Query : "+locUpdQry, "info", CommitteeUtility.class);
			lvrcmtroletypedaoobj = new CommitteeroleDaoservice();
			cmtroletypeUpdsts = lvrcmtroletypedaoobj.toDeactivatecmtroletype(locUpdQry);
			
			if(cmtroletypeUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffcmtroletype Deactive Block End.", "info", CommitteeUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in StaffcmtroleUtility.toDeactivecmtroletype() : "+e);
			logwrite.logMessage("Exception found in StaffcmtroleUtility.toDeactivecmtroletype() : "+e, "error", CommitteeUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrcmtroletypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	
}
