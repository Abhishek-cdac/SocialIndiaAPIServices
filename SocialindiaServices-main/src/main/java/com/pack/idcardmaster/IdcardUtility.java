package com.pack.idcardmaster;

import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.idcardmastervo.IdcardDao;
import com.pack.idcardmastervo.IdcardDaoservice;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class IdcardUtility {

	public static String toInsertIdcardtype(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrIdcardtypetitle = null, lvrIdcardtypedesc = null, lvrIdcardtypeshdesc = null,lvrIdcardtypestatus = null,lvrIdcardtypefiledata=null,lvrIdcardtypefilename=null;
		String lvrexistname = null, lvrexistnamedata = null, lvrIdcardtypeimgscnd = null, lvrIdcardtypeimgscnddata = null, lvrIdcardtypeimgtrd = null, lvrIdcardtypeimgtrddata = null, lvrIdcardtypeimgfrth = null, lvrIdcardtypeimgfrthdata = null ;
		int locIdcardtypeid = 0;
		Log logwrite = null;
		CommonUtils locCommutillObj = null;
		GroupMasterTblVo locGrpmstvoobj = null;
		UserMasterTblVo locUammstrvoobj = null;
		IDCardMasterTblVO IdcardtypeTblObj=null;
		IdcardDao lvrIdcardtypedaoobj=null;
		try {			
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			IdcardtypeTblObj= new IDCardMasterTblVO();
			logwrite.logMessage("Step 2 : Idcardtype Insert Block.", "info", IdcardUtility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrIdcardtypetitle = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "idcardname");
			lvrIdcardtypestatus = "1";
			
			IdcardtypeTblObj.setiVOcradname(lvrIdcardtypetitle);
			IdcardtypeTblObj.setiVOcardstatus(Integer.parseInt(lvrIdcardtypestatus));
			
			
			
			//----------- Idcardtype Insert Start-----------			
			logwrite.logMessage("Step 3 : Idcardtype Detail insert will start.", "info", IdcardUtility.class);
			lvrIdcardtypedaoobj = new IdcardDaoservice();
			lvrexistname = lvrIdcardtypedaoobj.toExistIdcardname(lvrIdcardtypetitle);
			if (lvrIdcardtypetitle != null && lvrIdcardtypetitle.length() > 0 && lvrexistname.equalsIgnoreCase("NEW")){
			locIdcardtypeid = lvrIdcardtypedaoobj.toInsertIdcardtype(IdcardtypeTblObj);
			}
			System.out.println(locIdcardtypeid+": id Idcardtype");
			logwrite.logMessage("Step 4 : Idcardtype Detail insert End Idcardtype Id : "+locIdcardtypeid, "info", IdcardUtility.class);
			// -----------Idcardtype Insert end------------		
			if (locIdcardtypeid>0) {	
				String filepath=rb.getString("external.mobiledbfldr.path");
				
				String textvalue="INSERT INTO mvp_idcard_tbl (CARD_ID,CARD_NAME, ACT_STS,CREATED_BY,ENRTY_DATETIME, MODY_DATETIME) VALUES("+locIdcardtypeid+",'"+lvrIdcardtypetitle+"', "+lvrIdcardtypestatus+","+Integer.parseInt(lvrCrntusrid)+",'"+locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"','"+locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"');";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr = "success!_!"+locIdcardtypeid;
			}
			else if(lvrexistname.equalsIgnoreCase("ALREADY EXISTS"))
			{
				locFtrnStr = "input!_!"+lvrexistname;
			}
			else{
				locFtrnStr = "error!_!"+locIdcardtypeid;
			}
			logwrite.logMessage("Step 7 : Idcardtype Insert Block End.", "info", IdcardUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EIdcardtypeUtility.toInsertIdcardtype() : "+e);
			logwrite.logMessage("Exception found in EIdcardtypeUtility.toInsertIdcardtype() : "+e, "error", IdcardUtility.class);
			locFtrnStr="error!_!"+locIdcardtypeid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; locGrpmstvoobj = null; locUammstrvoobj = null; IdcardtypeTblObj = null; lvrIdcardtypedaoobj = null;
			locFtrnStr = null;lvrIdcardtypedaoobj=null;lvrIdcardtypefiledata=null;IdcardtypeTblObj=null;lvrIdcardtypeshdesc = null;
		}
	}
	

	
	public static String toDeactiveIdcardtype(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrIdcardtypestatus = null, lvrIdcardtypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean IdcardtypeUpdsts = false;
		String locUpdQry=null;
		IdcardDao lvrIdcardtypedaoobj = null;
		ResourceBundle rb = null;
		CommonUtils locCommutillObj = null;
		try {
			locCommutillObj = new CommonUtilsDao();
			rb = ResourceBundle.getBundle("applicationResources");
			logwrite = new Log();
			logwrite.logMessage("Step 2 : Idcardtype Deactive Block Start.", "info", IdcardUtility.class);
			
			lvrIdcardtypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "idcardid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrIdcardtypestatus = "0";
			locUpdQry = "update IDCardMasterTblVO set iVOcardstatus ="+Integer.parseInt(lvrIdcardtypestatus)+" where iVOcradid ="+Integer.parseInt(lvrIdcardtypeid)+"";
			logwrite.logMessage("Step 3 : Idcardtype Deactive Update Query : "+locUpdQry, "info", IdcardUtility.class);
			lvrIdcardtypedaoobj = new IdcardDaoservice();
			IdcardtypeUpdsts = lvrIdcardtypedaoobj.toDeactivateIdcardtype(locUpdQry);
			
			if(IdcardtypeUpdsts){
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="update mvp_idcard_tbl set ACT_STS ="+Integer.parseInt(lvrIdcardtypestatus)+" where CARD_ID ="+Integer.parseInt(lvrIdcardtypeid)+";";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : Idcardtype Deactive Block End.", "info", IdcardUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EIdcardtypeUtility.toDeactiveIdcardtype() : "+e);
			logwrite.logMessage("Exception found in EIdcardtypeUtility.toDeactiveIdcardtype() : "+e, "error", IdcardUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrIdcardtypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	public static String toactiveIdcardtype(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrIdcardtypestatus = null, lvrIdcardtypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean IdcardtypeUpdsts = false;
		String locUpdQry=null;
		IdcardDao lvrIdcardtypedaoobj = null;
		ResourceBundle rb = null;
		CommonUtils locCommutillObj = null;
		try {
			locCommutillObj = new CommonUtilsDao();
			rb = ResourceBundle.getBundle("applicationResources");
			logwrite = new Log();
			logwrite.logMessage("Step 2 : Idcardtype Deactive Block Start.", "info", IdcardUtility.class);
			
			lvrIdcardtypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "idcardid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrIdcardtypestatus = "1";
			locUpdQry = "update IDCardMasterTblVO set iVOcardstatus ="+Integer.parseInt(lvrIdcardtypestatus)+" where iVOcradid ="+Integer.parseInt(lvrIdcardtypeid)+"";
			logwrite.logMessage("Step 3 : Idcardtype Deactive Update Query : "+locUpdQry, "info", IdcardUtility.class);
			lvrIdcardtypedaoobj = new IdcardDaoservice();
			IdcardtypeUpdsts = lvrIdcardtypedaoobj.toDeactivateIdcardtype(locUpdQry);
			
			if(IdcardtypeUpdsts){
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="update mvp_idcard_tbl set ACT_STS ="+Integer.parseInt(lvrIdcardtypestatus)+" where CARD_ID ="+Integer.parseInt(lvrIdcardtypeid)+";";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : Idcardtype Deactive Block End.", "info", IdcardUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EIdcardtypeUtility.toDeactiveIdcardtype() : "+e);
			logwrite.logMessage("Exception found in EIdcardtypeUtility.toDeactiveIdcardtype() : "+e, "error", IdcardUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrIdcardtypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	
}
