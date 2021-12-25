package com.pack.categorylist;

import java.util.Iterator;
import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.categorylistvo.persistance.CategoryDao;
import com.pack.categorylistvo.persistance.CategoryDaoservice;
import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.labor.LaborUtility;
import com.pack.laborvo.LaborSkillTblVO;
import com.pack.laborvo.persistence.LaborDao;
import com.pack.laborvo.persistence.LaborDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class CategoryUtility {

	public static String toInsertcategory(JSONObject prDatajson, String pWebImagpath, String pMobImgpath){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrcategorytypetitle = null, lvrcategorytypedesc = null, lvrcategorytypeshdesc = null,lvrcategorytypestatus = null,lvrcategorytypefiledata=null,lvrcategorytypefilename=null;
		String lvrexistname = null, lvrexistnamedata = null, lvrcategorytypeimgscnd = null, lvrcategorytypeimgscnddata = null, lvrcategorytypeimgtrd = null, lvrcategorytypeimgtrddata = null, lvrcategorytypeimgfrth = null, lvrcategorytypeimgfrthdata = null ;
		int loccategorytypeid = 0;
		Log logwrite = null;
		CommonUtils locCommutillObj = null;
		GroupMasterTblVo locGrpmstvoobj = null;
		UserMasterTblVo locUammstrvoobj = null;
		CategoryMasterTblVO categorytypeTblObj=null;
		CategoryDao lvrcategorytypedaoobj=null;
		String locimagename=null, locimg_encdata=null, lvrImgsrchpth = null;
		ResourceBundle rb = null;
		try {
			 rb = ResourceBundle.getBundle("applicationResources");			
			logwrite = new Log();
			locCommutillObj = new CommonUtilsDao();
			categorytypeTblObj= new CategoryMasterTblVO();
			logwrite.logMessage("Step 2 : categorytype Insert Block.", "info", CategoryUtility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrcategorytypetitle = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "categoryname");
			 locimagename=(String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imagename");						
			 locimg_encdata = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imgencdata");
			 lvrImgsrchpth = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "imgsrchpth");
			 if(Commonutility.checkempty(locimagename)){
				 locimagename = locimagename.replaceAll(" ", "_");
			 }
			lvrcategorytypestatus = "1";
			categorytypeTblObj.setiVOCATEGORY_NAME(lvrcategorytypetitle);
			categorytypeTblObj.setiVOACT_STAT(Integer.parseInt(lvrcategorytypestatus));
			categorytypeTblObj.setCategoryImageName(locimagename);
			categorytypeTblObj.setIvrCatetype(1); // 1 - General category, 2 - Donation Category
			//----------- categorytype Insert Start-----------			
			logwrite.logMessage("Step 3 : categorytype Detail insert will start.", "info", CategoryUtility.class);
			lvrcategorytypedaoobj = new CategoryDaoservice();
			lvrexistname = lvrcategorytypedaoobj.toExistCategoryname(lvrcategorytypetitle);
			if (lvrcategorytypetitle != null && lvrcategorytypetitle.length() > 0 && lvrexistname.equalsIgnoreCase("NEW")){
			loccategorytypeid = lvrcategorytypedaoobj.toInsertcategorytype(categorytypeTblObj);
			}
			System.out.println(loccategorytypeid+": id categorytype");
			logwrite.logMessage("Step 4 : categorytype Detail insert End categorytype Id : "+loccategorytypeid, "info", CategoryUtility.class);
			// -----------categorytype Insert end------------		
			if (loccategorytypeid>0) {	
				try{
					 //image write into folder
					 if((lvrImgsrchpth!=null && !lvrImgsrchpth.equalsIgnoreCase("null") && !lvrImgsrchpth.equalsIgnoreCase("")) && (locimagename!=null && !locimagename.equalsIgnoreCase("") && !locimagename.equalsIgnoreCase("null"))){											
						 Commonutility.toWriteImageMobWebNewUtill(loccategorytypeid, locimagename,lvrImgsrchpth,pWebImagpath,pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), logwrite, CategoryUtility.class);
						 /*
						 byte imgbytee[]= new byte[1024];
						 imgbytee=Commonutility.toDecodeB64StrtoByary(locimg_encdata);
						 String wrtsts=Commonutility.toByteArraytoWriteFiles(imgbytee, pWebImagpath+loccategorytypeid+"/", locimagename);
					 					 
						//mobile - small image
						String limgSourcePath=pWebImagpath+loccategorytypeid+"/"+locimagename;			
			 		 	String limgDisPath = pMobImgpath+loccategorytypeid+"/";
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
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="INSERT INTO category_mst_tbl (CATEGORY_ID, CATEGORY_NAME,CTG_SHTFORM,CATEGORY_DESCP,CREATED_BY,CREATOR_FLAG,CATEGORY_TYPE,CATEGORY_IMAGE_NAME,ENTRY_DATETIME,MODY_DATETIME, ACT_STAT) VALUES("+loccategorytypeid+", '"+lvrcategorytypetitle+"','','','"+lvrCrntusrid+"' ,'',1,'"+locimagename+"','"+locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"','"+locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"',1);";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr = "success!_!"+loccategorytypeid;
			}
			else if(lvrexistname.equalsIgnoreCase("ALREADY EXISTS"))
			{
				locFtrnStr = "input!_!"+lvrexistname;
			}
			else{
				locFtrnStr = "error!_!"+loccategorytypeid;
			}
			logwrite.logMessage("Step 7 : categorytype Insert Block End.", "info", CategoryUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EcategorytypeUtility.toInsertcategorytype() : "+e);
			logwrite.logMessage("Exception found in EcategorytypeUtility.toInsertcategorytype() : "+e, "error", CategoryUtility.class);
			locFtrnStr="error!_!"+loccategorytypeid;
			return locFtrnStr;
		}finally {
			logwrite = null; locCommutillObj =null; locGrpmstvoobj = null; locUammstrvoobj = null; categorytypeTblObj = null; lvrcategorytypedaoobj = null;
			locFtrnStr = null;lvrcategorytypedaoobj=null;lvrcategorytypefiledata=null;categorytypeTblObj=null;lvrcategorytypeshdesc = null;
		}
	}
	

	
	public static String toDeactivecategory(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrcategorytypestatus = null, lvrcategorytypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean categorytypeUpdsts = false;
		String locUpdQry=null;
		CategoryDao lvrcategorytypedaoobj = null;
		ResourceBundle rb = null;
		try {
			 rb = ResourceBundle.getBundle("applicationResources");	
			logwrite = new Log();
			logwrite.logMessage("Step 2 : categorytype Deactive Block Start.", "info", CategoryUtility.class);
			
			lvrcategorytypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "categoryid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrcategorytypestatus = "0";
			locUpdQry = "update CategoryMasterTblVO set iVOACT_STAT ="+Integer.parseInt(lvrcategorytypestatus)+" where iVOCATEGORY_ID ="+Integer.parseInt(lvrcategorytypeid)+"";
			logwrite.logMessage("Step 3 : categorytype Deactive Update Query : "+locUpdQry, "info", CategoryUtility.class);
			lvrcategorytypedaoobj = new CategoryDaoservice();
			categorytypeUpdsts = lvrcategorytypedaoobj.toDeactivatecategorytype(locUpdQry);
			
			if(categorytypeUpdsts){
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="update category_mst_tbl set ACT_STAT="+Integer.parseInt(lvrcategorytypestatus)+" where CATEGORY_ID="+Integer.parseInt(lvrcategorytypeid)+";";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : categorytype Deactive Block End.", "info", CategoryUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EcategorytypeUtility.toDeactivecategorytype() : "+e);
			logwrite.logMessage("Exception found in EcategorytypeUtility.toDeactivecategorytype() : "+e, "error", CategoryUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrcategorytypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	public static String toactivecategory(JSONObject prDatajson){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrcategorytypestatus = null, lvrcategorytypeid = null;
		String locIdcardstatusval = null;
		Log logwrite = null;
		boolean categorytypeUpdsts = false;
		String locUpdQry=null;
		CategoryDao lvrcategorytypedaoobj = null;
		ResourceBundle rb = null;
		try {
			 rb = ResourceBundle.getBundle("applicationResources");	
			logwrite = new Log();
			logwrite.logMessage("Step 2 : categorytype Deactive Block Start.", "info", CategoryUtility.class);
			
			lvrcategorytypeid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "categoryid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			locIdcardstatusval = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "statusflg");
			lvrcategorytypestatus = "1";
			locUpdQry = "update CategoryMasterTblVO set iVOACT_STAT ="+Integer.parseInt(lvrcategorytypestatus)+" where iVOCATEGORY_ID ="+Integer.parseInt(lvrcategorytypeid)+"";
			logwrite.logMessage("Step 3 : categorytype Deactive Update Query : "+locUpdQry, "info", CategoryUtility.class);
			lvrcategorytypedaoobj = new CategoryDaoservice();
			categorytypeUpdsts = lvrcategorytypedaoobj.toDeactivatecategorytype(locUpdQry);
			
			if(categorytypeUpdsts){
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="update category_mst_tbl set ACT_STAT="+Integer.parseInt(lvrcategorytypestatus)+" where CATEGORY_ID="+Integer.parseInt(lvrcategorytypeid)+";";
				Commonutility.TextFileWriting(filepath, textvalue);
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			logwrite.logMessage("Step 4 : categorytype Deactive Block End.", "info", CategoryUtility.class);
			return locFtrnStr;
		}catch(Exception e) {
			System.out.println("Exception found in EcategorytypeUtility.toDeactivecategorytype() : "+e);
			logwrite.logMessage("Exception found in EcategorytypeUtility.toDeactivecategorytype() : "+e, "error", CategoryUtility.class);
			locFtrnStr="error";			
			return locFtrnStr;
		}finally {
			logwrite = null;
			locFtrnStr = null;lvrcategorytypedaoobj = null;locIdcardstatusval=null;
		}
	}
	
	public static JSONObject toSltSingleCategoryDtl(JSONObject pDataJson, Session pSession, String pAuditMsg, String pAuditCode) {// Select on single user data
		String locvrLBR_ID=null, locvrLBR_SERVICE_ID=null, locvrLBR_STS=null; 
		//LaborProfileTblVO locLPTblvoObj=null;
		CategoryMasterTblVO locLDTblvoObj=null;
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
			locvrLBR_ID  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "categoryid");
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "status");	
			loc_slQry="from CategoryMasterTblVO where iVOCATEGORY_ID="+locvrLBR_ID+"  and iVOACT_STAT="+locvrLBR_STS+"";
			locQryObj=pSession.createQuery(loc_slQry);			
			locLDTblvoObj = (CategoryMasterTblVO)locQryObj.uniqueResult();
			System.out.println("Step 2 : Select category detail query executed.");
			
			locRtnDataJSON.put("intv_cat_id", locLDTblvoObj.getiVOCATEGORY_ID());
			locRtnDataJSON.put("strv_cat_name", Commonutility.toCheckNullEmpty(locLDTblvoObj.getiVOCATEGORY_NAME()));
			locRtnDataJSON.put("strv_cat_desc", Commonutility.toCheckNullEmpty(locLDTblvoObj.getCategoryDescription()));
			locRtnDataJSON.put("strv_cat_status",Commonutility.toCheckNullEmpty(locLDTblvoObj.getiVOACT_STAT()));
			locRtnDataJSON.put("strv_cat_shrtdesc", Commonutility.toCheckNullEmpty(locLDTblvoObj.getiVOCTG_SHTFORM()));
			locRtnDataJSON.put("str_cat_webimage", Commonutility.toCheckNullEmpty(locLDTblvoObj.getCategoryImageName()));
			locRtnDataJSON.put("str_cat_mobileimage", Commonutility.toCheckNullEmpty(locLDTblvoObj.getCategoryImageName()));
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
	
	
	public static String toUpdtCategory(JSONObject pDataJson,String pAuditMsg, String pAuditCode,String pWebImagpath, String pMobImgpath) {// Update
		//JSONObject locRtnJson=null;
		Log log=null;
		String locvrLBR_NAME = null,locvrLBR_EMAIL=null,locvrLBR_ADD_1 = null, locvrLBR_ADD_2 = null;
		String locvrENTRY_BY=null;
		String lvrCateid=null,locvrLBR_Image_Mobile=null,locvrLBR_Image_web=null,locvrID_CARD_TYP=null,locvrID_CARD_NO=null,locvrimagename=null;
		String locimagename=null, locimg_encdata=null,locvrLBR_COST=null,locvrLBR_COSTPER=null,locvrLBR_EXP=null,locvrCITY_NAME=null, lvrImgsrchpath = null;
		String locLbrUpdqry="";
		boolean locLbrUpdSts=false;
		 LaborDao locLabrObj=null;
			try {
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			log=new Log();
			lvrCateid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "categoryid");
			 locvrLBR_NAME = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "name");
			
			 locvrENTRY_BY = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrloginid");
			 locimagename=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imagename");						
			 locimg_encdata = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imgencdata");
			 lvrImgsrchpath = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "imgsrchpth");
			 if(Commonutility.checkempty(locimagename)){
				 locimagename = locimagename.replaceAll(" ", "_");
			 }
			 locvrLBR_Image_web=locimagename;
			 locvrLBR_Image_Mobile=locimagename;
			 if(locvrLBR_Image_web!=null && !locvrLBR_Image_web.equalsIgnoreCase("null") && !locvrLBR_Image_web.equalsIgnoreCase("")){
				 locvrimagename = ", categoryImageName='"+locvrLBR_Image_web+"'";
			 }
			 else
			 {
				 locvrimagename ="";
			 }
			 locLbrUpdqry ="update CategoryMasterTblVO set iVOCATEGORY_NAME='"+locvrLBR_NAME+"'  "+locvrimagename+" where iVOCATEGORY_ID="+lvrCateid+"";
			 log.logMessage("Updqry : "+locLbrUpdqry, "info", CategoryUtility.class);
			 String filepath1=rb.getString("external.mobiledbfldr.path");
			 String textvalue="update category_mst_tbl set CATEGORY_NAME='"+locvrLBR_NAME+"',CATEGORY_IMAGE_NAME='"+locimagename+"' where CATEGORY_ID="+lvrCateid+" and ACT_STAT=1;";
			 Commonutility.TextFileWriting(filepath1, textvalue);
			 locLabrObj=new LaborDaoservice();
			 locLbrUpdSts = locLabrObj.updateLaborInfo(locLbrUpdqry);			 						
			 if(Commonutility.toCheckNullEmpty(lvrCateid)!=null && !Commonutility.toCheckNullEmpty(lvrCateid).equalsIgnoreCase("null")){
				 try{
				 //image write into folder
				 if((lvrImgsrchpath!=null && !lvrImgsrchpath.equalsIgnoreCase("null") && !lvrImgsrchpath.equalsIgnoreCase("")) && (locimagename!=null && !locimagename.equalsIgnoreCase("") && !locimagename.equalsIgnoreCase("null"))){
					 
					 String delrs= Commonutility.todelete("", pWebImagpath+lvrCateid+"/");	
					 String delrs_mob= Commonutility.todelete("", pMobImgpath+lvrCateid+"/");
					 Commonutility.toWriteImageMobWebNewUtill(Integer.parseInt(lvrCateid), locimagename,lvrImgsrchpath,pWebImagpath,pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, CategoryUtility.class);
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
					 log.logMessage("Exception in Image write on labor insert", "info", CategoryUtility.class);
				 }finally{
					 
				 }	
			 }			
			
			 log.logMessage("Labor  Category Detail insert will start.", "info", CategoryUtility.class);
			 
			
			 if(locLbrUpdSts){
				 String filepath=rb.getString("external.mobiledbfldr.path");
				 return "success";
			 }else{
				 return "error";
			 }			
		} catch (Exception e) {
			System.out.println("Exception found in CategoryUtility.toUpdtLabor : "+e);
			log.logMessage("Exception : "+e, "error", CategoryUtility.class);
			return "error";						
		} finally {
			 log=null; locLabrObj=null;
			 locvrLBR_NAME = null; locvrLBR_EMAIL = null;
			 locvrLBR_ADD_1 = null; locvrLBR_COST=null;locvrLBR_COSTPER=null;locvrLBR_EXP=null;
			 locvrENTRY_BY = null;
		}		
	}
	
}


