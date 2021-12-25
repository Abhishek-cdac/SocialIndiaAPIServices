package com.pack.merchantCategorylist;

import java.util.Iterator;
import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.categorylist.CategoryUtility;
import com.pack.labor.LaborUtility;
import com.pack.laborvo.LaborSkillTblVO;
import com.pack.laborvo.persistence.LaborDao;
import com.pack.laborvo.persistence.LaborDaoservice;
import com.pack.merchantCategorylistvo.persistance.merchantCategoryDao;
import com.pack.merchantCategorylistvo.persistance.merchantCategoryDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.vo.MerchantCategoryTblVO;

public class merchantCategorylistUtility {

	public static String toInsertmerchantCategory(JSONObject prDatajson,String pWebImagpath, String pMobImgpath){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrmerchantCategorytitle = null, lvrmerchantCategorydesc = null, lvrmerchantCategoryshdesc = null,lvrmerchantCategorystatus = null,lvrmerchantCategoryfiledata=null,lvrmerchantCategoryfilename=null, lvrImgsrchpth = null;
		String lvrexistname = null, lvrexistnamedata = null, locimagename = null, /*locimg_encdata = null, */ lvrmerchantCategoryimgtrd = null, lvrmerchantCategoryimgtrddata = null, lvrmerchantCategoryimgfrth = null, lvrmerchantCategoryimgfrthdata = null ;
		int locmerchantCategoryid = 0;
		Log logwrite = null;
		CommonUtils locCommutillObj = null;
		GroupMasterTblVo locGrpmstvoobj = null;
		UserMasterTblVo locUammstrvoobj = null;
		MerchantCategoryTblVO merchantCategoryTblObj=null;
		merchantCategoryDao lvrmerchantCategorydaoobj=null;
		ResourceBundle rb = null;
		try {			
			 rb = ResourceBundle.getBundle("applicationResources");			
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			merchantCategoryTblObj= new MerchantCategoryTblVO();
			logwrite.logMessage("Step 2 : Merchant Category Insert Block.", "info", merchantCategorylistUtility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrmerchantCategorytitle = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "merchantCategoryname");
			lvrmerchantCategorydesc = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "desc");
			 locimagename=(String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imagename");						
			// locimg_encdata = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imgencdata");
			 lvrImgsrchpth = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imgsrchpth");
			lvrmerchantCategorystatus = "1";
			if(Commonutility.checkempty(locimagename)){
				locimagename = locimagename.replaceAll(" ", "_");
			}
			merchantCategoryTblObj.setMrchCategoryName(lvrmerchantCategorytitle);
			merchantCategoryTblObj.setMrchCategoryDesc(lvrmerchantCategorydesc);
			merchantCategoryTblObj.setMrchCategoryImage(locimagename);
			merchantCategoryTblObj.setStatus(Integer.parseInt(lvrmerchantCategorystatus));
			locUammstrvoobj = new UserMasterTblVo();
			//locUammstrvoobj.setEntryBy(Integer.parseInt(lvrCrntusrid));
			//merchantCategoryTblObj.setEntryBy(locUammstrvoobj);
			merchantCategoryTblObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			//----------- merchantCategory Insert Start-----------			
			logwrite.logMessage("Step 3 : merchantCategory Detail insert will start.", "info", merchantCategorylistUtility.class);
			lvrmerchantCategorydaoobj = new merchantCategoryDaoservice();
			lvrexistname = lvrmerchantCategorydaoobj.toExistmerchantCategorylistname(lvrmerchantCategorytitle);
			if (lvrmerchantCategorytitle != null && lvrmerchantCategorytitle.length() > 0 && lvrexistname.equalsIgnoreCase("NEW")){
			locmerchantCategoryid = lvrmerchantCategorydaoobj.toInsertmerchantCategory(merchantCategoryTblObj);
			}
			System.out.println(locmerchantCategoryid+": id merchantCategory");
			logwrite.logMessage("Step 4 : merchantCategory Detail insert End merchantCategory Id : "+locmerchantCategoryid, "info", merchantCategorylistUtility.class);
			// -----------merchantCategory Insert end------------		
			if (locmerchantCategoryid>0) {	
				try{
					 //image write into folder
					 if((lvrImgsrchpth!=null && !lvrImgsrchpth.equalsIgnoreCase("null") && !lvrImgsrchpth.equalsIgnoreCase("")) && (locimagename!=null && !locimagename.equalsIgnoreCase("") && !locimagename.equalsIgnoreCase("null"))){
						 Commonutility.toWriteImageMobWebNewUtill(locmerchantCategoryid, locimagename,lvrImgsrchpth,pWebImagpath,pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), logwrite, CategoryUtility.class);
						 /*
						 byte imgbytee[]= new byte[1024];
						 imgbytee=Commonutility.toDecodeB64StrtoByary(locimg_encdata);
						 String wrtsts=Commonutility.toByteArraytoWriteFiles(imgbytee, pWebImagpath+locmerchantCategoryid+"/", locimagename);				 
						//mobile - small image
						String limgSourcePath=pWebImagpath+locmerchantCategoryid+"/"+locimagename;			
			 		 	String limgDisPath = pMobImgpath+locmerchantCategoryid+"/";
			 		 	String limgName = FilenameUtils.getBaseName(locimagename);
			 		 	String limageFomat = FilenameUtils.getExtension(locimagename);		 	    			 	    	 
			 	    	String limageFor = "all";
			 	    	int lneedWidth = Commonutility.stringToInteger(rb.getString("thump.img.width"));
			        	int lneedHeight = Commonutility.stringToInteger(rb.getString("thump.img.height"));	
			        	ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile				 				 				
					 */
					 }
					 } catch(Exception imger){
						 System.out.println("Exception in  image write on labor insert : "+imger);
						 logwrite.logMessage("Exception in Image write on labor insert", "info", LaborUtility.class);
					 }finally{
						 
					 }		
				
				locFtrnStr = "success!_!"+locmerchantCategoryid;
			}
			else if(lvrexistname.equalsIgnoreCase("ALREADY EXISTS"))
			{
				locFtrnStr = "input!_!"+lvrexistname;
			}
			else{
				locFtrnStr = "error!_!"+locmerchantCategoryid;
			}
			logwrite.logMessage("Step 7 : merchantCategory Insert Block End.", "info", merchantCategorylistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EmerchantCategoryUtility.toInsertmerchantCategory() : "+e);
			logwrite.logMessage("Exception found in EmerchantCategoryUtility.toInsertmerchantCategory() : "+e, "error", merchantCategorylistUtility.class);
			locFtrnStr="error!_!"+locmerchantCategoryid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; locGrpmstvoobj = null; locUammstrvoobj = null; merchantCategoryTblObj = null; lvrmerchantCategorydaoobj = null;
			locFtrnStr = null;lvrmerchantCategorydaoobj=null;lvrmerchantCategoryfiledata=null;merchantCategoryTblObj=null;lvrmerchantCategoryshdesc = null;
		}
	}
	
	public static String toDeactivemerchantCategory(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrmerchantCategorystatus = null, lvrmerchantCategoryid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean merchantCategoryUpdsts = false;
		String locUpdQry=null;
		merchantCategoryDao lvrmerchantCategorydaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : merchantCategory Deactive Block Start.", "info", merchantCategorylistUtility.class);
			
			lvrmerchantCategoryid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "merchantCategoryid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrmerchantCategorystatus = "0";
			locUpdQry = "update MerchantCategoryTblVO set status ="+Integer.parseInt(lvrmerchantCategorystatus)+" where mrchCategoryId ="+Integer.parseInt(lvrmerchantCategoryid)+"";
			logwrite.logMessage("Step 3 : merchantCategory Deactive Update Query : "+locUpdQry, "info", merchantCategorylistUtility.class);
			lvrmerchantCategorydaoobj = new merchantCategoryDaoservice();
			merchantCategoryUpdsts = lvrmerchantCategorydaoobj.toDeactivatemerchantCategory(locUpdQry);
			
			if(merchantCategoryUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffmerchantCategory Deactive Block End.", "info", merchantCategorylistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in merchantCategorylistUtility.toDeactivemerchantCategory() : "+e);
			logwrite.logMessage("Exception found in merchantCategorylistUtility.toDeactivemerchantCategory() : "+e, "error", merchantCategorylistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrmerchantCategorydaoobj = null;locIdcardstatusval=null;
		}
	}
	
	public static String toactivemerchantCategory(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrmerchantCategorystatus = null, lvrmerchantCategoryid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean merchantCategoryUpdsts = false;
		String locUpdQry=null;
		merchantCategoryDao lvrmerchantCategorydaoobj = null;
		try {
			logwrite = new Log();
			logwrite.logMessage("Step 2 : merchantCategory Deactive Block Start.", "info", merchantCategorylistUtility.class);
			
			lvrmerchantCategoryid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "merchantCategoryid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrmerchantCategorystatus = "1";
			locUpdQry = "update MerchantCategoryTblVO set status ="+Integer.parseInt(lvrmerchantCategorystatus)+" where mrchCategoryId ="+Integer.parseInt(lvrmerchantCategoryid)+"";
			logwrite.logMessage("Step 3 : merchantCategory Deactive Update Query : "+locUpdQry, "info", merchantCategorylistUtility.class);
			lvrmerchantCategorydaoobj = new merchantCategoryDaoservice();
			merchantCategoryUpdsts = lvrmerchantCategorydaoobj.toDeactivatemerchantCategory(locUpdQry);
			
			if(merchantCategoryUpdsts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : staffmerchantCategory Deactive Block End.", "info", merchantCategorylistUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in merchantCategorylistUtility.toactivemerchantCategory() : "+e);
			logwrite.logMessage("Exception found in merchantCategorylistUtility.toactivemerchantCategory() : "+e, "error", merchantCategorylistUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrmerchantCategorydaoobj = null;locIdcardstatusval=null;
		}
	}
	
	public static JSONObject toSltSingleMrchCategoryDtl(JSONObject pDataJson, Session pSession, String pAuditMsg, String pAuditCode) {// Select on single user data
		String locvrLBR_ID=null, locvrLBR_SERVICE_ID=null, locvrLBR_STS=null; 
		//LaborProfileTblVO locLPTblvoObj=null;
		MerchantCategoryTblVO locLDTblvoObj=null;
		String loc_slQry=null;
		Query locQryObj= null;
		JSONObject locRtnDataJSON=null;
		
		String loc_slQry_categry=null;		
		Iterator locObjLbrcateglst_itr=null;
		LaborSkillTblVO locLbrSkiltbl=null;
		JSONArray locLBRSklJSONAryobj=null;
		JSONObject locInrLbrSklJSONObj=null;
		Log log=null;
		try {
			log=new Log();			
			System.out.println("Step 1 : category detail block enter");
			log.logMessage("Step 1 : Select category detail block enter", "info", CategoryUtility.class);
			
			locRtnDataJSON=new JSONObject();
			locvrLBR_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "merchantCategoryid");
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "status");	
			loc_slQry="from MerchantCategoryTblVO where mrchCategoryId="+locvrLBR_ID+"  ";
			locQryObj=pSession.createQuery(loc_slQry);			
			locLDTblvoObj = (MerchantCategoryTblVO)locQryObj.uniqueResult();
			System.out.println("Step 2 : Select category detail query executed.");
			
			locRtnDataJSON.put("intv_mrchcat_id", locLDTblvoObj.getMrchCategoryId());
			locRtnDataJSON.put("strv_mrchcat_name", Commonutility.toCheckNullEmpty(locLDTblvoObj.getMrchCategoryName()));
			locRtnDataJSON.put("strv_mrchcat_desc", Commonutility.toCheckNullEmpty(locLDTblvoObj.getMrchCategoryDesc()));
			locRtnDataJSON.put("strv_mrchcat_status",Commonutility.toCheckNullEmpty(locLDTblvoObj.getStatus()));
			locRtnDataJSON.put("str_mrchcat_webimage", Commonutility.toCheckNullEmpty(locLDTblvoObj.getMrchCategoryImage()));
			locRtnDataJSON.put("str_mrchcat_mobileimage", Commonutility.toCheckNullEmpty(locLDTblvoObj.getMrchCategoryImage()));
			System.out.println("Step 5 : Return JSON DATA : "+locRtnDataJSON);						
			System.out.println("Step 6 : Select category detail block end.");
			log.logMessage("Step 4: Select category detail block end.", "info", CategoryUtility.class);			
			return locRtnDataJSON;
		} catch (Exception e) {
			try{
			System.out.println("Step -1 : Select category detail Exception found in CategoryUtility.toSltSingleLabourDtl : "+e);
			log.logMessage("Exception : "+e, "error", CategoryUtility.class);
			locRtnDataJSON=new JSONObject();
			locRtnDataJSON.put("catch", "Error");
			}catch(Exception ee){}
			return locRtnDataJSON;
		} finally {
			 locvrLBR_ID=null; locvrLBR_SERVICE_ID=null; locLDTblvoObj=null; loc_slQry=null; locRtnDataJSON=null;log=null;			 
			 locObjLbrcateglst_itr=null;locLbrSkiltbl=null;locInrLbrSklJSONObj=null;locLBRSklJSONAryobj=null;loc_slQry_categry=null;
		}
	}
	
	public static String toUpdtMrchCategory(JSONObject pDataJson,String pAuditMsg, String pAuditCode,String pWebImagpath, String pMobImgpath) {// Update
		//JSONObject locRtnJson=null;
		Log log=null;
		String locvrLBR_NAME = null,locvrDesc=null,locvrLBR_ADD_1 = null, locvrLBR_ADD_2 = null;
		String locvrENTRY_BY=null;
		String locvrLBR_ID=null,locvrLBR_Image_Mobile=null,locvrLBR_Image_web=null,locvrID_CARD_TYP=null,locvrID_CARD_NO=null,locvrimagename=null;
		String locimagename=null, locimg_encdata=null,locvrLBR_COST=null,locvrLBR_COSTPER=null,locvrLBR_EXP=null,locvrCITY_NAME=null;
		String locLbrUpdqry="", lvrImgsrchpth = null;
		boolean locLbrUpdSts=false;
		 LaborDao locLabrObj=null;
		 
		try {
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			log=new Log();
			 locvrLBR_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "merchantCategoryid");
			 locvrLBR_NAME = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "merchantCategoryname");
			 locvrDesc= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "desc");
			 locvrENTRY_BY = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrloginid");
			 locimagename=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imagename");						
			// locimg_encdata = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imgencdata");
			 lvrImgsrchpth = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imgsrchpth");
			 if(Commonutility.checkempty(locimagename)){
				 locimagename = locimagename.replaceAll(" ", "_");
			 }
			 locvrLBR_Image_web=locimagename;
			 locvrLBR_Image_Mobile=locimagename;
			 if(locvrLBR_Image_web!=null && !locvrLBR_Image_web.equalsIgnoreCase("null") && !locvrLBR_Image_web.equalsIgnoreCase("")){
				 locvrimagename = "mrchCategoryImage='"+locvrLBR_Image_web+"',";
			 } else {
				 locvrimagename ="";
			 }
			 locLbrUpdqry ="update MerchantCategoryTblVO set mrchCategoryName='"+locvrLBR_NAME+"' ,"+locvrimagename+" mrchCategoryDesc='"+locvrDesc+"'  where mrchCategoryId="+locvrLBR_ID+"";
			 log.logMessage("Updqry : "+locLbrUpdqry, "info", CategoryUtility.class);
			 locLabrObj=new LaborDaoservice();
			 locLbrUpdSts = locLabrObj.updateLaborInfo(locLbrUpdqry);			 						
			 if(Commonutility.toCheckNullEmpty(locvrLBR_ID)!=null && !Commonutility.toCheckNullEmpty(locvrLBR_ID).equalsIgnoreCase("null")){
				 try{
				 //image write into folder
				 if((lvrImgsrchpth!=null && !lvrImgsrchpth.equalsIgnoreCase("null") && !lvrImgsrchpth.equalsIgnoreCase("")) && (locimagename!=null && !locimagename.equalsIgnoreCase("") && !locimagename.equalsIgnoreCase("null"))){
					 
					 String delrs= Commonutility.todelete("", pWebImagpath+locvrLBR_ID+"/");		
					 String delrs_mob= Commonutility.todelete("", pMobImgpath+locvrLBR_ID+"/");
					 Commonutility.toWriteImageMobWebNewUtill(Integer.parseInt(locvrLBR_ID), locimagename,lvrImgsrchpth,pWebImagpath,pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, CategoryUtility.class);
				/*
					byte imgbytee[]= new byte[1024];
					 imgbytee=Commonutility.toDecodeB64StrtoByary(locimg_encdata);
					 String wrtsts=Commonutility.toByteArraytoWriteFiles(imgbytee, pWebImagpath+locvrLBR_ID+"/", locimagename);

				
					//mobile - small image
					String limgSourcePath=pWebImagpath+locvrLBR_ID+"/"+locimagename;			
		 		 	String limgDisPath = pMobImgpath+locvrLBR_ID+"/";
		 		 	String limgName = FilenameUtils.getBaseName(locimagename);
		 		 	String limageFomat = FilenameUtils.getExtension(locimagename);		 	    			 	    	 
		 	    	String limageFor = "all";
		 	    	int lneedWidth = Commonutility.stringToInteger(rb.getString("thump.img.width"));
		        	int lneedHeight = Commonutility.stringToInteger(rb.getString("thump.img.height"));	
		        	ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile				 				 				
					*/
				 }
				 } catch(Exception imger){
					 System.out.println("Exception in  image write on labor insert : "+imger);
					 log.logMessage("Exception in Image write on labor insert", "info", merchantCategorylistUtility.class);
				 }finally{
					 
				 }	
			 }			
			
			 log.logMessage("Labor  Category Detail insert will start.", "info", merchantCategorylistUtility.class);			 		
			 if(locLbrUpdSts){
				 return "success";
			 }else{
				 return "error";
			 }			
		} catch (Exception e) {
			System.out.println("Exception found in merchantCategorylistUtility.toUpdtLabor : "+e);
			log.logMessage("Exception : "+e, "error", merchantCategorylistUtility.class);
			return "error";						
		} finally {
			 log=null; locLabrObj=null;
			 locvrLBR_NAME = null; locvrDesc = null;
			 locvrLBR_ADD_1 = null; locvrLBR_COST=null;locvrLBR_COSTPER=null;locvrLBR_EXP=null;
			 locvrENTRY_BY = null;
		}		
	}
}
