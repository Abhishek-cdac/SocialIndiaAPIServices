package com.socialindiaservices.facility;

import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.categorylist.CategoryUtility;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.societyMgmt.societyMgmtDao;
import com.siservices.societyMgmt.societyMgmtDaoServices;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.facilityVo.Facilitydaosevices;
import com.socialindiaservices.facilityVo.facilityDao;
import com.socialindiaservices.vo.FacilityMstTblVO;

public class FacilityUtility extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String toInsertFunction(JSONObject pDataJson){
		int resval=0;
		String locFtrnStr=null;
		String societyId=null,facname=null,place=null,facdesc=null,imagename=null,imgencdata=null,status=null,entryby=null,lvrImgsrchpth = null;
		Log log=null;	
		byte conbytetoarr[] = new byte[1024];
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		FacilityMstTblVO facObj=null;
		societyMgmtDao society=new societyMgmtDaoServices();
		String locVrSlQry="";
		CommonUtils locCommutillObj =null;
		facilityDao facdao=null;
		try{
			log =  new Log();
			locCommutillObj = new CommonUtilsDao();
			facObj=new FacilityMstTblVO();
			societyId=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"societyId");
			facname=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"facname");
			place=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"place");
			facdesc=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"facdesc");
			//imgencdata=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"imgencdata");
			lvrImgsrchpth=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"imgsrchpth");
			imagename=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"imagename");
			status=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"status");
			entryby=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"entryby");
			locVrSlQry="SELECT activationKey FROM SocietyMstTbl where societyId="+societyId+" and statusFlag = 1";
			String result= society.getactiveKey(locVrSlQry);
			UserMasterTblVo userObj=null;
			if(result!=null && !result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null")){
				userObj=new UserMasterTblVo();
				facdao=new Facilitydaosevices();
				facObj.setFacilityImg(imagename);
				facObj.setFacilityName(facname);
				facObj.setDescription(facdesc);
				facObj.setSocietyKey(result);
				facObj.setPlace(place);
				userObj.setUserId(Integer.parseInt(entryby));
				facObj.setEntryBy(userObj);
				facObj.setStatusFlag(Integer.parseInt(status));
				facObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));				
				resval=facdao.toInsertFacility(facObj);				
			}
			String filepath=rb.getString("external.mobiledbfldr.path");
			
			if(result!=null && !result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null")){
				String textvalue="INSERT INTO facility_mst_tbl(FACILITY_ID,FACILITY_NAME,FACILITY_IMG,PLACE,DESCRIPTION,SOCIETY_KEY,ACT_STAT,ENTRY_BY,UPDATED_BY,ENTRY_DATETIME,MODIFY_DATETIME) VALUES("+resval+", '"+facname+"', '"+imagename+"', '"+place+"', '"+facdesc+"', '"+result+"', "+Integer.parseInt(status)+", "+Integer.parseInt(entryby)+", '', '"+locCommutillObj.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"', '"+locCommutillObj.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"');";
				Commonutility.TextFileWriting(filepath, textvalue);
			}
			if(imagename!=null && imagename!=""){		
				String pWebImagpath = rb.getString("external.path")+rb.getString("external.images.fldr")+rb.getString("external.inner.facilityfdr") +rb.getString("external.inner.webpath");
				String pMobImgpath = rb.getString("external.path")+rb.getString("external.images.fldr")+rb.getString("external.inner.facilityfdr") +rb.getString("external.inner.mobilepath");
				Commonutility.toWriteImageMobWebNewUtill(resval, imagename,lvrImgsrchpth,pWebImagpath,pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, CategoryUtility.class);
				/*
				//web - org image
				conbytetoarr = Commonutility.toDecodeB64StrtoByary(imgencdata);
				String topath = rb.getString("external.path")+rb.getString("external.images.fldr")+rb.getString("external.inner.facilityfdr") +rb.getString("external.inner.webpath") + resval+"/";
				String wrtsts = Commonutility.toByteArraytoWriteFiles(conbytetoarr, topath, imagename);															
				//mobile - small image
				String limgSourcePath = topath+"/"+imagename;						
 		 		String limgDisPath = rb.getString("external.path")+rb.getString("external.images.fldr")+rb.getString("external.inner.facilityfdr") +rb.getString("external.inner.mobilepath")+resval+"/";
 		 	
 		 	 	String limgName = FilenameUtils.getBaseName(imagename);
 		 	 	String limageFomat = FilenameUtils.getExtension(imagename);		 	    			 	    	 
 	    	 	String limageFor = "all";
        		int lneedWidth = Commonutility.stringToInteger(rb.getString("thump.img.width"));
        		int lneedHeight = Commonutility.stringToInteger(rb.getString("thump.img.height"));	
        		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
			*/	
			}
			if(resval!=-1){
				locFtrnStr="success!_!"+resval;
			}else{
				locFtrnStr="error!_!"+resval;
			}
			
		}catch(Exception e){
			System.out.println("Exception found in FunctionUtility.toInsertFunction : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", FacilityUtility.class);
			locFtrnStr="error!_!"+resval;
			return locFtrnStr;
		}finally{
			 		
		}
		return locFtrnStr;
	}

	public static JSONObject toeditFacility(JSONObject locObjRecvdataJson,Session praDatajson) {
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		JSONObject lvrRtnjsonobj = null;
		JSONObject lvrInrJSONObj = null;	
		JSONArray lvrEventjsonaryobj = null;
		Log logWrite = null;
		Iterator<Object> lvrObjeventlstitr = null;
		List<Object> lvrObjfunctionlstitr = null;
		FacilityMstTblVO  locfacobj = null;
		SocietyMstTbl locSoc=null;
		String lvrSlqry = null;
		String lvrfunSlqry="",facilityId=null,activeKey=null;
	    try {
	    	
	    	facilityId  = (String)Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "facilityId");
	    	activeKey  = (String)Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "activeKey");
			lvrSlqry = "from SocietyMstTbl  where activationKey='"+activeKey+"'";
			lvrObjeventlstitr=praDatajson.createQuery(lvrSlqry).list().iterator();
			String lveWebpath = rb.getString("external.path")+rb.getString("external.images.fldr")+rb.getString("external.inner.facilityfdr") +rb.getString("external.inner.webpath");			
			lvrEventjsonaryobj = new JSONArray();
			while (lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				locSoc = (SocietyMstTbl) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("townshipId",Commonutility.toCheckNullEmpty(locSoc.getTownshipId().getTownshipId()));
				lvrInrJSONObj.put("townshipname",Commonutility.toCheckNullEmpty(locSoc.getTownshipId().getTownshipName()));
				lvrInrJSONObj.put("societyId",Commonutility.toCheckNullEmpty(locSoc.getSocietyId()));
				lvrInrJSONObj.put("societyName",Commonutility.toCheckNullEmpty(locSoc.getSocietyName()));
				lvrInrJSONObj.put("imagePath", lveWebpath);
				lvrInrJSONObj.put("imagewebPath",lveWebpath);
				lvrfunSlqry = "select facilityName,place,description,facilityImg,statusFlag,facilityId,societyKey from FacilityMstTblVO where facilityId="+Integer.parseInt(facilityId);
				System.out.println("lvrfunSlqry ----------"+lvrfunSlqry);
				lvrObjfunctionlstitr=praDatajson.createQuery(lvrfunSlqry).list();
				lvrInrJSONObj.put("societyidss", lvrObjfunctionlstitr);	
			}
			lvrRtnjsonobj=new JSONObject();	
			lvrRtnjsonobj.put("facilitydetails", lvrInrJSONObj);
			System.out.println("Step 4 : Select Facility process End " +lvrRtnjsonobj);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in toFacility() : "+expObj);
				logWrite.logMessage("Step -1 : Facility select all block Exception : "+expObj, "error", FacilityUtility.class);	
				lvrRtnjsonobj=new JSONObject();
				lvrRtnjsonobj.put("CatchBlock", "Error");
				System.out.println("lvrRtnjsonobj : "+lvrRtnjsonobj);
				}catch(Exception ee){}finally{}
	     return lvrRtnjsonobj;
	    }finally {
	    	lvrRtnjsonobj = null;
	    	lvrInrJSONObj = null;	
	    	lvrEventjsonaryobj = null;
	    	logWrite = null;
	    	lvrObjeventlstitr = null;
	    }
}
	
	public static String toUpdateFunction(JSONObject pDataJson,Session praDatajson){	
		String locFtrnStr = null;
		String societyId = null, facname = null, place = null, facdesc = null, imagename = null, /*imgencdata = null,*/ entryby = null, lvrImgsrchpth = null;
		Log log = null;
		byte conbytetoarr[] = new byte[1024];
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		societyMgmtDao society = new societyMgmtDaoServices();
		String locVrSlQry = "";
		String lvrfunmtrqry = "";
		String facilityId = null;
		int resval = 0;
		Common common = null;
		boolean lvrfunmtr = false;
		CommonUtils locCommutillObj = null;
		try {
			log = new Log();
			common=new CommonDao();locCommutillObj = new CommonUtilsDao();
			facilityId=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"facilityId");
			societyId=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"societyId");
			facname=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"facname");
			place=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"place");
			facdesc=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"facdesc");
			//imgencdata= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"imgencdata");
			lvrImgsrchpth = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"imgsrchpth");
			imagename=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"imagename");
			entryby=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"entryby");
			locVrSlQry="SELECT activationKey FROM SocietyMstTbl where societyId="+societyId+" and statusFlag = 1";
			resval=Integer.parseInt(facilityId);
			String result= society.getactiveKey(locVrSlQry);
			if(result!=null && !result.equalsIgnoreCase("") && !result.equalsIgnoreCase("null")){
				String textvalue= "";
				if(imagename!=null && Commonutility.checkempty(imagename)){	
					lvrfunmtrqry ="update FacilityMstTblVO set facilityName='"+facname+"',place='"+place+"',description='"+facdesc+"',facilityImg='"+imagename+"',societyKey='"+result+"',updatedBy='"+Integer.parseInt(entryby)+"' where facilityId ="+Integer.parseInt(facilityId)+" and statusFlag=1";
					textvalue="update facility_mst_tbl set FACILITY_NAME='"+facname+"', PLACE='"+place+"', DESCRIPTION='"+facdesc+"', FACILITY_IMG='"+imagename+"', SOCIETY_KEY='"+result+"', UPDATED_BY="+Integer.parseInt(entryby)+", MODIFY_DATETIME='"+locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"' where FACILITY_ID ="+Integer.parseInt(facilityId)+" and ACT_STAT=1;";
				} else {
					lvrfunmtrqry ="update FacilityMstTblVO set facilityName='"+facname+"',place='"+place+"',description='"+facdesc+"',societyKey='"+result+"',updatedBy='"+Integer.parseInt(entryby)+"' where facilityId ="+Integer.parseInt(facilityId)+" and statusFlag=1";
					textvalue="update facility_mst_tbl set FACILITY_NAME='"+facname+"', PLACE='"+place+"', DESCRIPTION='"+facdesc+"', SOCIETY_KEY='"+result+"', UPDATED_BY="+Integer.parseInt(entryby)+", MODIFY_DATETIME='"+locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"' where FACILITY_ID ="+Integer.parseInt(facilityId)+" and ACT_STAT=1;";
				}
				
				lvrfunmtr=common.commonUpdate(lvrfunmtrqry);
				
				String filepath=rb.getString("external.mobiledbfldr.path");
				if(lvrfunmtr){
					Commonutility.TextFileWriting(filepath, textvalue);
				}
				//String textvalue="update facility_mst_tbl set FACILITY_NAME='"+facname+"', PLACE='"+place+"', DESCRIPTION='"+facdesc+"', FACILITY_IMG='"+imagename+"', SOCIETY_KEY='"+result+"', UPDATED_BY="+Integer.parseInt(entryby)+", MODIFY_DATETIME='"+locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"' where FACILITY_ID ="+Integer.parseInt(facilityId)+" and ACT_STAT=1;";
				
			}
			
			if(imagename!=null && Commonutility.checkempty(imagename)){	
			String lvrWebimgpath = rb.getString("external.path")+rb.getString("external.images.fldr")+rb.getString("external.inner.facilityfdr") +rb.getString("external.inner.webpath");
			String lvrMobileimgepath = rb.getString("external.path")+rb.getString("external.images.fldr")+rb.getString("external.inner.facilityfdr") +rb.getString("external.inner.mobilepath");
			String delrs= Commonutility.todelete("", lvrWebimgpath);	
			String delrs1= Commonutility.todelete("", lvrMobileimgepath);
			//Commonutility.toWriteImgWebAndMob(resval, imagename, imgencdata, lvrWebimgpath, lvrMobileimgepath, rb.getString("thump.img.width"), rb.getString("thump.img.height"), log, FacilityUtility.class);			
			Commonutility.toWriteImageMobWebNewUtill(resval, imagename, lvrImgsrchpth, lvrWebimgpath, lvrMobileimgepath, rb.getString("thump.img.width"), rb.getString("thump.img.height"), log, FacilityUtility.class);					
			
			/*
			//String lvrWebimgpath = rb.getString("external.path")+rb.getString("external.images.fldr")+rb.getString("external.inner.facilityfdr") +rb.getString("external.inner.webpath") + resval+"/";
			String delrs= Commonutility.todelete("", lvrWebimgpath);	
			//web - org image
			conbytetoarr = Commonutility.toDecodeB64StrtoByary(imgencdata);			
			String wrtsts = Commonutility.toByteArraytoWriteFiles(conbytetoarr, lvrWebimgpath, imagename);
									
			//mobile - small image
			//String lvrMobileimgepath = rb.getString("external.path")+rb.getString("external.images.fldr")+rb.getString("external.inner.facilityfdr") +rb.getString("external.inner.mobilepath")+resval+"/";
			String delrs1= Commonutility.todelete("", lvrMobileimgepath);
			String limgSourcePath = lvrWebimgpath+"/"+imagename;						
		 	String limgDisPath = lvrMobileimgepath;
		 	
		 	String limgName = FilenameUtils.getBaseName(imagename);
		 	String limageFomat = FilenameUtils.getExtension(imagename);		 	    			 	    	 
	    	String limageFor = "all";
    		int lneedWidth = Commonutility.stringToInteger(rb.getString("thump.img.width"));
    		int lneedHeight = Commonutility.stringToInteger(rb.getString("thump.img.height"));	
    		ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
			*/
			}
		if(resval!=-1){
			locFtrnStr="success!_!"+resval;
		}else{
			locFtrnStr="error!_!"+resval;
		}
		
	}catch(Exception e){
		System.out.println("Exception found in facility.toupdate : "+e);
		log.logMessage("Step -1 : Exception : "+e, "error", FacilityUtility.class);
		locFtrnStr="error!_!"+resval;
		return locFtrnStr;
	}finally{
		 		log = null;
	}
	return locFtrnStr;
}

public static String toActiveorDeactive(JSONObject locObjRecvdataJson,Session locObjsession) {
System.out.println("locObjRecvdataJson----------- "+locObjRecvdataJson);
	String facilityId = null;
	String lvrfunmtrqry = null;
	Log log = null;
	Common common = null;
	String jsonTextFinal=null;
	String statusflg=null;
	boolean lvrfunmtr=false;
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	try {
		log = new Log();
		common=new CommonDao();
		System.out.println("Step 1 : Facilty Deaction block enter");
		log.logMessage("Step 1 : Select Facilty Deaction block enter", "info", FacilityUtility.class);
		facilityId  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "facilityId");
		statusflg  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "statusflg");
		lvrfunmtrqry ="update FacilityMstTblVO set statusFlag="+Integer.parseInt(statusflg)+" where facilityId ="+Integer.parseInt(facilityId);
		System.out.println("lvrfunmtrqry ------------ "+lvrfunmtrqry);
		lvrfunmtr=common.commonUpdate(lvrfunmtrqry);
		if(lvrfunmtr){
			String filepath=rb.getString("external.mobiledbfldr.path");
			String textvalue="update facility_mst_tbl set ACT_STAT="+Integer.parseInt(statusflg)+" where FACILITY_ID ="+Integer.parseInt(facilityId)+";";
			if(lvrfunmtr){
			Commonutility.TextFileWriting(filepath, textvalue);
			}
				jsonTextFinal="success!_!"+0;	
		}else{
			jsonTextFinal="error!_!"+1;	
		}
		
	}catch(Exception ex){
		System.out.println("Test ----- "+ex);	
		jsonTextFinal="error!_!"+1;	
		return jsonTextFinal;
	}finally{
		facilityId = null;lvrfunmtrqry = null;log = null;
		common = null;statusflg=null;lvrfunmtr=false;
	}
	
	return jsonTextFinal;
	

}
}


