package com.pack.enews;

import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.enewsvo.EeNewsDocTblVO;
import com.pack.enewsvo.EeNewsTblVO;
import com.pack.enewsvo.persistence.EeNewsDao;
import com.pack.enewsvo.persistence.EeNewsDaoService;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class EeNewsUtility {
	static ResourceBundle lvrRbld = ResourceBundle.getBundle("applicationResources");
	public static String toInserteNews(JSONObject prDatajson, String pWebImagpath, String pMobImgpath){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrEnewstitle = null, lvrEnewsdesc = null, lvrEnewsshdesc = null,lvrEnewsstatus = null,lvrEnewsfiledata=null,lvrEnewsfilename=null;
		String lvrEnewsimgfirst = null, lvrEnewsimgfirstdata = null, lvrEnewsimgscnd = null, lvrEnewsimgscnddata = null, lvrEnewsimgtrd = null, lvrEnewsimgtrddata = null, lvrEnewsimgfrth = null, lvrEnewsimgfrthdata = null ;
		int loceNewsid = 0;
		Log logwrite = null;
		CommonUtils locCommutillObj = null;
		GroupMasterTblVo locGrpmstvoobj = null;
		UserMasterTblVo locUammstrvoobj = null;
		EeNewsTblVO lvrEenesvoobj = null;
		EeNewsDao lvreNewsdaoobj = null;
		EeNewsDao locEnewsdaoObj=null;
		EeNewsTblVO EeNewsTblObj=null;
		try {			
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			locGrpmstvoobj = new GroupMasterTblVo();
			locUammstrvoobj = new UserMasterTblVo();
			logwrite.logMessage("Step 2 : eNews Insert Block.", "info", EeNewsUtility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrEnewstitle = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "enewstitle");
			lvrEnewsdesc = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "enewsdesc");
			lvrEnewsshdesc = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "enewsshdesc");
			
			//JSONArray imageDetail=(JSONArray) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imageDetail");
			JSONArray lvrImgsrchpath=(JSONArray) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imgsrchpth");
			JSONArray fileName=(JSONArray) Commonutility.toHasChkJsonRtnValObj(prDatajson, "fileName");			
			lvrEnewsstatus = "1";
			lvrEenesvoobj = new EeNewsTblVO();
			lvrEenesvoobj.setIvrBnENTRY_DATETIME(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			lvrEenesvoobj.setIvrBnSTATUS(Integer.parseInt(lvrEnewsstatus));
			
			if(!Commonutility.toCheckisNumeric(lvrCrntusrid)){
				lvrEenesvoobj.setIvrBnENTRY_BY(null);
				lvrEenesvoobj.setIvrBnUAMObj(null);
			}else{				
				lvrEenesvoobj.setIvrBnENTRY_BY(Integer.parseInt(lvrCrntusrid));
				locUammstrvoobj.setUserId(Integer.parseInt(lvrCrntusrid));
				lvrEenesvoobj.setIvrBnUAMObj(locUammstrvoobj);
			}	
			
			if(!Commonutility.toCheckisNumeric(lvrCrntgrpid)){
				lvrEenesvoobj.setIvrBnGRPObj(null);
			}else{
				locGrpmstvoobj.setGroupCode(Integer.parseInt(lvrCrntgrpid));	
				lvrEenesvoobj.setIvrBnGRPObj(locGrpmstvoobj);
			}
			lvrEenesvoobj.setIvrBnTITLE(lvrEnewstitle);
			lvrEenesvoobj.setIvrBnDESCRIPTION(lvrEnewsdesc);
			lvrEenesvoobj.setIvrBnSHRTDESC(lvrEnewsshdesc);
			
			//----------- eNews Insert Start-----------			
			logwrite.logMessage("Step 3 : eNews Detail insert will start.", "info", EeNewsUtility.class);
			lvreNewsdaoobj = new EeNewsDaoService();
			loceNewsid = lvreNewsdaoobj.toInserteNews(lvrEenesvoobj);
			System.out.println(loceNewsid+": id eNews");
			logwrite.logMessage("Step 4 : eNews Detail insert End eNews Id : "+loceNewsid, "info", EeNewsUtility.class);
			// -----------eNews Insert end------------		
			if (loceNewsid>0) {	
				logwrite.logMessage("Step 5 : eNews Image Write will start.", "info", EeNewsUtility.class);		
				 EeNewsDocTblVO iocEnewsdocObj =null;
				 locEnewsdaoObj = new EeNewsDaoService();
				 boolean Enewsflg=false;
				//image write into folder
				if (fileName != null) {
					for (int i = 0; i < fileName.length(); i++) {
					 	if(fileName!=null && lvrImgsrchpath!=null){
							lvrEnewsfiledata = (String) lvrImgsrchpath.getString(i);
							lvrEnewsfilename = (String) fileName.getString(i);
							if(Commonutility.checkempty(lvrEnewsfilename)){
								lvrEnewsfilename = lvrEnewsfilename.replaceAll(" ", "_");
							}
							//Commonutility.toWriteImgWebAndMob(loceNewsid, lvrEnewsfilename, lvrEnewsfiledata, pWebImagpath, pMobImgpath,lvrRbld.getString("thump.img.width"),lvrRbld.getString("thump.img.height"), logwrite, EeNewsUtility.class);
							Commonutility.toWriteImageMobWebNewUtill(loceNewsid, lvrEnewsfilename, lvrEnewsfiledata, pWebImagpath, pMobImgpath,lvrRbld.getString("thump.img.width"),lvrRbld.getString("thump.img.height"), logwrite, EeNewsUtility.class);
							iocEnewsdocObj=new EeNewsDocTblVO();
							EeNewsTblObj=new EeNewsTblVO();
							EeNewsTblObj.setIvrBnENEWS_ID(loceNewsid);
							iocEnewsdocObj.setEnewid(EeNewsTblObj);
							iocEnewsdocObj.setImgname(lvrEnewsfilename);
							iocEnewsdocObj.setStatus(1);
							iocEnewsdocObj.setEntryBy(Integer.parseInt(lvrCrntusrid));
							iocEnewsdocObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							int locdocinsrtrst = locEnewsdaoObj.saveEnewsDoc_intRtn(iocEnewsdocObj);							
							iocEnewsdocObj = null;
						} else {

						}						
					}
				}
				
				/* if(Enewsflg){// Audit trial use only Enewsdocumeent update
					 if(Commonutility.toCheckisNumeric(lvrCrntusrid)){
						 AuditTrial.toWriteAudit(prAuditMsg, pAuditCode, Integer.parseInt(lvrCrntusrid));
					 }else{
						 AuditTrial.toWriteAudit(prAuditMsg, pAuditCode, 1);
					 }				 
				 }*/
				logwrite.logMessage("Step 6 : eNews Image Write will end.", "info", EeNewsUtility.class);
				locFtrnStr = "success!_!"+loceNewsid;
			}else{
				locFtrnStr = "error!_!"+loceNewsid;
			}
			logwrite.logMessage("Step 7 : eNews Insert Block End.", "info", EeNewsUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EeNewsUtility.toInserteNews() : "+e);
			logwrite.logMessage("Exception found in EeNewsUtility.toInserteNews() : "+e, "error", EeNewsUtility.class);
			locFtrnStr="error!_!"+loceNewsid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; locGrpmstvoobj = null; locUammstrvoobj = null; lvrEenesvoobj = null; lvreNewsdaoobj = null;
			locFtrnStr = null;locEnewsdaoObj=null;lvrEnewsfiledata=null;EeNewsTblObj=null;lvrEnewsshdesc = null;
		}
	}
	
	public static String toUpdateeNews(JSONObject prDatajson, String pWebImagpath, String pMobImgpath){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrEnewstitle = null, lvrEnewsdesc = null, lvrEnewsstatus = null, lvrEnewsid = null, lvrEnewsupqry = null;
		String lvrEnewsshdesc = null, lvrCrntSocietyid = null, lvrImg3exist = null, lvrImg4exist = null;
		String lvrEnewsfilename = null, lvrEnewsfiledata = null, lvrEnewsimgscnd = null, lvrEnewsimgscnddata = null, lvrEnewsimgtrd = null, lvrEnewsimgtrddata = null, lvrEnewsimgfrth = null, lvrEnewsimgfrthdata = null ;
		int loceNewsid = 0;
		boolean eNewsUpdsts = false;
		Log logwrite = null;	
		EeNewsTblVO lvrEenesvoobj = null;
		EeNewsDao lvreNewsdaoobj = null;
		String lvrDeleteFstimgnme = null, lvrDeleteSndimgnme = null, lvrDeleteTrdimgnme = null, lvrDeleteFrthimgnme = null;
		Query lvrQry = null;
		EeNewsDao locEnewsdaoObj=null;
		EeNewsTblVO EeNewsTblObj=null;
		CommonUtils locCommutillObj = null;
		
		try {
			logwrite = new Log();
			//lvrCommdaosrobj = new CommonDao();
			locCommutillObj = new CommonUtilsDao();
			//locGrpmstvoobj = new GroupMasterTblVo();
			//locUammstrvoobj = new UserMasterTblVo();
			System.out.println("Step 2 : eNews Modify Block.");
			logwrite.logMessage("Step 2 : eNews Modify Block.", "info", EeNewsUtility.class);
			lvrEnewsstatus = "1";
			lvrEnewsid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "enewsid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrEnewstitle = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "enewstitle");
			lvrEnewsdesc = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "enewsdesc");
			lvrEnewsshdesc = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "enewsshdesc");
			lvrCrntSocietyid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrsocietyid");
					
			if((lvrEnewsid != null && !lvrEnewsid.equalsIgnoreCase("null") && !lvrEnewsid.equalsIgnoreCase("")) && Commonutility.toCheckisNumeric(lvrEnewsid)){
				loceNewsid = Integer.parseInt(lvrEnewsid);
				lvrEnewsupqry = "update EeNewsTblVO set ivrBnTITLE = '"+lvrEnewstitle+"', ivrBnDESCRIPTION = '"+lvrEnewsdesc+"',ivrBnSHRTDESC ='"+lvrEnewsshdesc+"', ";
				if(lvrCrntSocietyid.equalsIgnoreCase("-1"))
				{
					lvrCrntSocietyid=null;
				}
				
				if(!Commonutility.toCheckisNumeric(lvrCrntusrid)){
					lvrEnewsupqry +="ivrBnUAMObj ="+null+", ";
					lvrEnewsupqry +="ivrBnENTRY_BY ="+null+", ";				
				}else{
					
					lvrEnewsupqry +="ivrBnUAMObj ="+Integer.parseInt(lvrCrntusrid)+", ";
					lvrEnewsupqry +="ivrBnENTRY_BY ="+Integer.parseInt(lvrCrntusrid)+", ";					
				}			
				if(!Commonutility.toCheckisNumeric(lvrCrntgrpid)){
					lvrEnewsupqry +="ivrBnGRPObj ="+null+", ";
				}else{
					lvrEnewsupqry +="ivrBnGRPObj ="+Integer.parseInt(lvrCrntgrpid)+", ";				
				}
				
				
				lvrEnewsupqry +=" ivrBnSTATUS = "+Integer.parseInt(lvrEnewsstatus) + " ";
				lvrEnewsupqry +=" where ivrBnENEWS_ID = "+Integer.parseInt(lvrEnewsid);
				System.out.println("Step 3 : eNews update query : "+lvrEnewsupqry);
				logwrite.logMessage("Step 3 : eNews update query : "+lvrEnewsupqry, "info", EeNewsUtility.class);
				logwrite.logMessage("Step 4 : eNews Detail update will start.", "info", EeNewsUtility.class);
				System.out.println("Step 4 : eNews Detail update will start.");
				//--------eNews Start Update--------------
				lvreNewsdaoobj = new EeNewsDaoService();
				eNewsUpdsts = lvreNewsdaoobj.toUpdateeNews(lvrEnewsupqry);
				System.out.println("eNews Update Status [eNewsUpdsts] : "+eNewsUpdsts);
				//--------eNews End Update----------------													
				logwrite.logMessage("Step 5 : eNews update query Executed [End]: "+eNewsUpdsts, "info", EeNewsUtility.class);
				//JSONArray imageDetail=(JSONArray) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imageDetail");
				JSONArray lvrImgsrchpath=(JSONArray) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imgsrchpth");
				JSONArray fileName=(JSONArray) Commonutility.toHasChkJsonRtnValObj(prDatajson, "fileName");	
				if(fileName!=null){
				boolean dlrst=lvreNewsdaoobj.toDeleteEnewImgupdTbl(lvrEnewsid);
				}
				logwrite.logMessage("Step 6 : eNews Image Write will start.", "info", EeNewsUtility.class);				
				 EeNewsDocTblVO iocEnewsdocObj =null;
				 locEnewsdaoObj = new EeNewsDaoService();
				 boolean Enewsflg=false;
				//image write into folder
				 if(fileName!=null)  {
				 for (int i = 0; i < fileName.length(); i++) {
					 if(fileName!=null && lvrImgsrchpath!=null){						
						  lvrEnewsfiledata = (String) lvrImgsrchpath.getString(i);
						  lvrEnewsfilename = (String)fileName.getString(i);
						  if(Commonutility.checkempty(lvrEnewsfilename)){
							  lvrEnewsfilename = lvrEnewsfilename.replaceAll(" ", "_");
							  Commonutility.toWriteImageMobWebNewUtill(loceNewsid, lvrEnewsfilename, lvrEnewsfiledata, pWebImagpath, pMobImgpath,lvrRbld.getString("thump.img.width"),lvrRbld.getString("thump.img.height"), logwrite, EeNewsUtility.class);
							  //Commonutility.toWriteImgWebAndMob(loceNewsid, lvrEnewsfilename, lvrEnewsfiledata, pWebImagpath, pMobImgpath,lvrRbld.getString("thump.img.width"),lvrRbld.getString("thump.img.height"), logwrite, EeNewsUtility.class);
								 iocEnewsdocObj=new EeNewsDocTblVO();
								 EeNewsTblObj=new EeNewsTblVO();
								 EeNewsTblObj.setIvrBnENEWS_ID(loceNewsid);
								 iocEnewsdocObj.setEnewid(EeNewsTblObj);
								 iocEnewsdocObj.setImgname(lvrEnewsfilename);
								 iocEnewsdocObj.setStatus(1);
								 iocEnewsdocObj.setEntryBy(Integer.parseInt(lvrCrntusrid));
								 iocEnewsdocObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
									int locdocinsrtrst = locEnewsdaoObj.saveEnewsDoc_intRtn(iocEnewsdocObj);
									iocEnewsdocObj=null;
								 }else{
									 
								 }
						  }						
				 }
				 }	 			
				logwrite.logMessage("Step 6 : eNews Image Write will end.", "info", EeNewsUtility.class);							
				locFtrnStr = "success";
			}else{
				locFtrnStr = "error";
			}
			logwrite.logMessage("Step 6 : eNews Modify End.", "info", EeNewsUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EeNewsUtility.toUpdateeNews() : "+e);
			logwrite.logMessage("Exception found in EeNewsUtility.toUpdateeNews() : "+e, "error", EeNewsUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {//lvrCommdaosrobj = null;
			lvrQry = null;
			logwrite = null;lvrEenesvoobj = null;lvreNewsdaoobj = null;
			locFtrnStr = null;			
			lvrDeleteFstimgnme = null; lvrDeleteSndimgnme = null; lvrDeleteTrdimgnme = null; lvrDeleteFrthimgnme = null;
			locCommutillObj = null;
			lvrEnewstitle = null; lvrEnewsdesc = null; lvrEnewsstatus = null; lvrEnewsid = null; lvrEnewsupqry = null;
			lvrEnewsshdesc = null; lvrCrntSocietyid = null; lvrImg3exist = null; lvrImg4exist = null;
			lvrEnewsfilename = null; lvrEnewsfiledata = null; lvrEnewsimgscnd = null; lvrEnewsimgscnddata = null; lvrEnewsimgtrd = null; lvrEnewsimgtrddata = null; lvrEnewsimgfrth = null; lvrEnewsimgfrthdata = null;
		}
	}
	
	public static String toDeactiveeNews(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrEnewsstatus = null, lvrEnewsid = null;
		int loceNewsid = 0;
		Log logwrite = null;
		boolean eNewsUpdsts = false;
		String locUpdQry=null;
		EeNewsDao lvreNewsdaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : eNews Deactive Block Start.", "info", EeNewsUtility.class);
			lvrEnewsstatus = "0";
			lvrEnewsid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "enewsid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locUpdQry = "update EeNewsTblVO set ivrBnSTATUS ="+Integer.parseInt(lvrEnewsstatus)+" where ivrBnENEWS_ID ="+Integer.parseInt(lvrEnewsid)+"";
			logwrite.logMessage("Step 3 : eNews Deactive Update Query : "+locUpdQry, "info", EeNewsUtility.class);
			lvreNewsdaoobj = new EeNewsDaoService();
			eNewsUpdsts = lvreNewsdaoobj.toDeactivateeNews(locUpdQry);
			
			if(eNewsUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : eNews Deactive Block End.", "info", EeNewsUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EeNewsUtility.toDeactiveeNews() : "+e);
			logwrite.logMessage("Exception found in EeNewsUtility.toDeactiveeNews() : "+e, "error", EeNewsUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvreNewsdaoobj = null;
		}
	}
	
	public static String toDeleteeNews(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		Log logwrite = null;
		boolean eNewsDlsts = false;
		boolean eNewsImgDlsts = false;
		String locDlQry=null,locDldispQry=null,lvrEnewsid = null;
		EeNewsDao lvreNewsdaoobj = null;
		EeNewsDao lvrENewsImgUpdaSts = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : eNews Delete Block Start.", "info", EeNewsUtility.class);
			lvreNewsdaoobj = new EeNewsDaoService();
			lvrEnewsid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "enewsid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locDldispQry= "delete EeNewsDocTblVO where Enewid="+Integer.parseInt(lvrEnewsid)+"";
			logwrite.logMessage("Step 3 : Enews Image Tbl Delete qry : "+locDldispQry, "info", EeNewsUtility.class);
			eNewsImgDlsts=lvreNewsdaoobj.toDeleteEnewImgTbl(locDldispQry);
			locDlQry = "delete EeNewsTblVO where ivrBnENEWS_ID ="+Integer.parseInt(lvrEnewsid)+"";
			logwrite.logMessage("Step 3 : eNews Delete Query : "+locDlQry, "info", EeNewsUtility.class);
			
			eNewsDlsts = lvreNewsdaoobj.toDeleteeNews(locDlQry);
			if(eNewsDlsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : eNews Delete Block End.", "info", EeNewsUtility.class);					
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EeNewsUtility.toDeleteeNews() : "+e);
			logwrite.logMessage("Exception found in EeNewsUtility.toDeleteeNews() : "+e, "error", EeNewsUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvreNewsdaoobj = null;lvrENewsImgUpdaSts=null;
		}
	}
	
	public static JSONObject toSelectSngleeNews(JSONObject prDatajson, String pWebImagpath, String pMobImgpath){
		String lvrCrntusrid = null, lvrCrntgrpid = null;
		String locSlQry=null,lvrEnewsid = null,loc_slQry_file=null;
		Query lvrQrybj = null;
		Log logwrite = null;
		Date lvrEntrydate=null;
		//EeNewsDao lvreNewsdaoobj = null;
		JSONObject locRtndatajson = null;
		Session locHbsession = null;
		Common locCommonObj = null;
		EeNewsTblVO lvrEenesvoobj = null;
		EeNewsDocTblVO lvrEenewsimgvoobj=null;
		Iterator locObjfilelst_itr=null;
		JSONArray locLBRFILEJSONAryobj=null;
		JSONObject locLBRFILEOBJJSONAryobj=null;
		EeNewsDocTblVO locfiledbtbl=null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		try {
			logwrite = new Log();
			locRtndatajson = new JSONObject();
			locCommonObj = new CommonDao();
			locHbsession = HibernateUtil.getSession();
			logwrite.logMessage("Step 2 : eNews Single Select Block Start.", "info", EeNewsUtility.class);
			lvrEnewsid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "enewsid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");			
			locSlQry = "from EeNewsTblVO where ivrBnENEWS_ID =" +Integer.parseInt(lvrEnewsid) +"";
			logwrite.logMessage("Step 3 : eNews Single Select Query : "+locSlQry, "info", EeNewsUtility.class);
			lvrQrybj = locHbsession.createQuery(locSlQry);
			lvrEenesvoobj = (EeNewsTblVO)lvrQrybj.uniqueResult();
			logwrite.logMessage("Step 4 : eNews JSONData will form.", "info", EeNewsUtility.class);
			locRtndatajson.put("enewsid", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnENEWS_ID()));
			locRtndatajson.put("enewstitle", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnTITLE()));
			locRtndatajson.put("enewsdesc", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnDESCRIPTION()));
			locRtndatajson.put("enewsshrtdesc", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnSHRTDESC()));
			
			locRtndatajson.put("enewswebimgfldrpath", pWebImagpath+ Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnENEWS_ID())+"/");
			locRtndatajson.put("enewsmobileimgfldrpath", pMobImgpath+ Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnENEWS_ID())+"/");
			// Get User Details
			if (lvrEenesvoobj.getIvrBnUAMObj() != null) {
				locRtndatajson.put("enewsentbyresidentid", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnUAMObj().getUserId()));
				locRtndatajson.put("enewsentbyresidentfname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnUAMObj().getFirstName()));
				locRtndatajson.put("enewsentbyresidentlname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnUAMObj().getLastName()));
				locRtndatajson.put("enewsentbyresidentemail", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnUAMObj().getEmailId()));
				locRtndatajson.put("enewsentbyresidentmobno", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnUAMObj().getMobileNo()));
				locRtndatajson.put("enewsentbyresidentisdcode", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnUAMObj().getIsdCode()));
				locRtndatajson.put("enewsentbyresidentimgname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnUAMObj().getImageNameWeb()));
				
				if (lvrEenesvoobj.getIvrBnUAMObj().getSocietyId() != null) {
					locRtndatajson.put("enewsentbyresidentsocietyid", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnUAMObj().getSocietyId().getSocietyId()));
					locRtndatajson.put("enewsentbyresidentsocietyname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnUAMObj().getSocietyId().getSocietyName()));
					locRtndatajson.put("enewsentbyresidenttownshipname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnUAMObj().getSocietyId().getTownshipId().getTownshipName()));
				} else {
					locRtndatajson.put("enewsentbyresidentsocietyid", "");
					locRtndatajson.put("enewsentbyresidentsocietyname", "");
					locRtndatajson.put("enewsentbyresidenttownshipname", "");
				}							
			} else {
				locRtndatajson.put("enewsentbyresidentid", "");
				locRtndatajson.put("enewsentbyresidentfname", "");
				locRtndatajson.put("enewsentbyresidentlname", "");
				locRtndatajson.put("enewsentbyresidentemail", "");
				locRtndatajson.put("enewsentbyresidentmobno", "");
				locRtndatajson.put("enewsentbyresidentisdcode", "");
				locRtndatajson.put("enewsentbyresidentimgname", "");
				
				locRtndatajson.put("enewsentbyresidentsocietyid", "");
				locRtndatajson.put("enewsentbyresidentsocietyname", "");
			}
			// Get Group Details
			if (lvrEenesvoobj.getIvrBnGRPObj() != null) {
				locRtndatajson.put("enewsentbyresidentgrpcode", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnGRPObj().getGroupCode()));
				locRtndatajson.put("enewsentbyresidentgrpname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnGRPObj().getGroupName()));
				locRtndatajson.put("enewsentbyresidentgrpalias", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnGRPObj().getGroupAliasName()));
			} else {
				if (lvrEenesvoobj.getIvrBnUAMObj() != null) {
					if (lvrEenesvoobj.getIvrBnUAMObj().getGroupCode() != null) {
						locRtndatajson.put("enewsentbyresidentgrpcode", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnUAMObj().getGroupCode().getGroupCode()));
						locRtndatajson.put("enewsentbyresidentgrpname", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnUAMObj().getGroupCode().getGroupName()));
						locRtndatajson.put("enewsentbyresidentgrpalias", Commonutility.toCheckNullEmpty(lvrEenesvoobj.getIvrBnUAMObj().getGroupCode().getGroupAliasName()));
					}
				}else{
					locRtndatajson.put("enewsentbyresidentgrpcode", "");
					locRtndatajson.put("enewsentbyresidentgrpname", "");
					locRtndatajson.put("enewsentbyresidentgrpalias", "");
				}
			}					
			lvrEntrydate=lvrEenesvoobj.getIvrBnENTRY_DATETIME();
			locRtndatajson.put("eveentrydate", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "yyyy-MM-dd HH:mm:ss"));
			logwrite.logMessage("Step 5 : eNews Single Select Block End.", "info", EeNewsUtility.class);
			logwrite.logMessage("Step 6: Select enews files detail block start.", "info",EeNewsUtility.class);
			loc_slQry_file="from EeNewsDocTblVO where Enewid="+Integer.parseInt(lvrEnewsid)+" ";
			locObjfilelst_itr=locHbsession.createQuery(loc_slQry_file).list().iterator();	
			System.out.println("Step 3 : Select enews files detail query executed.");
			locLBRFILEJSONAryobj=new JSONArray();
			while (locObjfilelst_itr!=null &&  locObjfilelst_itr.hasNext() ) {
				locfiledbtbl=(EeNewsDocTblVO)locObjfilelst_itr.next();
				locLBRFILEOBJJSONAryobj=new JSONObject();
				if(locfiledbtbl.getEnewuniqId()!=null){
					locLBRFILEOBJJSONAryobj.put("filesname", locfiledbtbl.getImgname());
					
					
				
				}				
				locLBRFILEJSONAryobj.put(locLBRFILEOBJJSONAryobj);
			}
			
			logwrite.logMessage("Step 3: Select file name and type detail block end.", "info", EeNewsUtility.class);
			locRtndatajson.put("jArry_doc_files", locLBRFILEJSONAryobj);
			System.out.println("Step 5 : Return JSON DATA : "+locRtndatajson);						
			System.out.println("Step 6 : Select enewsdocment detail block end.");
			return locRtndatajson;
		}catch(Exception e) {
			try{
			System.out.println("Step -1 : Exception found in EeNewsUtility.toSelectSngleeNews() : "+e);
			logwrite.logMessage("Step -1 : Exception found in EeNewsUtility.toSelectSngleeNews() : "+e, "error", EeNewsUtility.class);
			locRtndatajson=new JSONObject();
			locRtndatajson.put("catch", "Error");
			}catch(Exception ee){}finally{}
			return locRtndatajson;
		}finally {
			if(locHbsession!=null){locHbsession.close();locHbsession = null;}
			logwrite = null; lvrEntrydate=null; locCommonObj = null; lvrEenesvoobj = null;
			lvrCrntusrid = null; lvrCrntgrpid = null;
			locSlQry = null; lvrEnewsid = null; lvrQrybj = null;loc_slQry_file=null;
		}
	}
	
}
