package com.pack.flashnews;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.json.JSONObject;

import com.mobi.contents.ContentDao;
import com.mobi.contents.ContentDaoServices;
import com.mobi.contents.FlashNewsTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtilFunctions;

public class FlashNewUtility {


	public static String toInsertFunction(JSONObject pDataJson) {
		int resval=0;
		String locFtrnStr=null;
		String societyId=null,flashcontent=null,entryby=null,exdate=null,extime=null,imageName=null,title=null;
		Log log=null;	
		String imgsrchpath=null;
		SocietyMstTbl society=null;
		CommonUtilFunctions locCommutillObj =null;
		ContentDao contentdao=null;
		FlashNewsTblVO flashObj=null;
		UserMasterTblVo userObj=null;
		ResourceBundle rb = null;
		try{
			rb = ResourceBundle.getBundle("applicationResources");
			contentdao=new ContentDaoServices();
			society=new SocietyMstTbl();
			log =  new Log();
			locCommutillObj = new CommonUtilFunctions();
			flashObj=new FlashNewsTblVO();
			societyId=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"societyId");
			flashcontent=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"flashnewcontent");
			entryby=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"entryby");
			exdate= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"exdate");
			extime= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"extime");
			imgsrchpath= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"flashnewimage");
			imageName= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"flashnewimageName");
			title= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"title");
			
			SimpleDateFormat recfrmt = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			SimpleDateFormat convrtfrmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date lvrexpdate = null;
			if (Commonutility.checkempty(exdate) && Commonutility.checkempty(extime)) {
				lvrexpdate = recfrmt.parse(exdate + " "+extime);
			}
			//Commonutility.toWriteConsole("lvrexpdate 1 : "+lvrexpdate);
			//Commonutility.toWriteConsole("lvrexpdate 2 : "+convrtfrmt.parse(convrtfrmt.format(lvrexpdate)));
			userObj=new UserMasterTblVo();
				society.setSocietyId(Integer.parseInt(societyId));
				flashObj.setSocietyId(society);
				flashObj.setNewsContent(flashcontent);
				userObj.setUserId(Integer.parseInt(entryby));
				flashObj.setEntryBy(userObj);
				flashObj.setNewsimageName(imageName);
				flashObj.setTitle(title);
				flashObj.setExpiryDate(lvrexpdate);
				flashObj.setStatus(1);
				flashObj.setEntryDateTime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				resval=contentdao.toInsertFlashNew(flashObj);				
			if(resval!=-1){
				 if(Commonutility.checkempty(imgsrchpath)){
					 String lvrWebImagpath = rb.getString("external.uploadfile.flashnews.webpath");
					 String lvrMobImgpath = rb.getString("external.uploadfile.flashnews.mobilepath");
					/* String delrs = Commonutility.todelete("", lvrWebImagpath+donateId+"/");
					 String delrs_mob= Commonutility.todelete("", lvrMobImgpath+donateId+"/");	*/
					 Commonutility.toWriteImageMobWebNewUtill(resval,imageName,imgsrchpath,lvrWebImagpath,lvrMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, FlashNewUtility.class);
				 }
				locFtrnStr="success!_!"+resval;
			}else{
				locFtrnStr="error!_!"+resval;
			}
			
		}catch(Exception e){
			System.out.println("Exception found in FlashNewUtility.toInsertFunction : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", FlashNewUtility.class);
			locFtrnStr="error!_!"+resval;
			return locFtrnStr;
		}finally{
			 		
		}
		return locFtrnStr;
	}

	public static JSONObject toSingleSelect(JSONObject locObjRecvdataJson,Session locObjsession) {
		JSONObject lvrRtnjsonobj = null;
		JSONObject lvrInrJSONObj = null;	
		Log logWrite = null;
		Iterator<Object> lvrObjeventlstitr = null;
		FlashNewsTblVO  locfacobj = null;
		String lvrSlqry = null;
		String flashId=null;
	    try {
	    	flashId  = (String)Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "flashId");
			lvrSlqry = "from FlashNewsTblVO  where newsId="+flashId;
			lvrObjeventlstitr=locObjsession.createQuery(lvrSlqry).list().iterator();
			while (lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				locfacobj = (FlashNewsTblVO) lvrObjeventlstitr.next();
				if(locfacobj.getSocietyId()!=null){
				lvrInrJSONObj.put("societyName",Commonutility.toCheckNullEmpty(locfacobj.getSocietyId().getSocietyName()));
				lvrInrJSONObj.put("societyId",Commonutility.toCheckNullEmpty(locfacobj.getSocietyId().getSocietyId()));
				lvrInrJSONObj.put("townshipId",Commonutility.toCheckNullEmpty(locfacobj.getSocietyId().getTownshipId().getTownshipId()));
				lvrInrJSONObj.put("townshipname",Commonutility.toCheckNullEmpty(locfacobj.getSocietyId().getTownshipId().getTownshipName()));
				}else{
					lvrInrJSONObj.put("societyName" ,"");
					lvrInrJSONObj.put("societyId","");
					lvrInrJSONObj.put("townshipId","");
					lvrInrJSONObj.put("townshipname","");
				}
				lvrInrJSONObj.put("newstitle",Commonutility.toCheckNullEmpty(locfacobj.getTitle()));
				lvrInrJSONObj.put("fleshnewId",Commonutility.toCheckNullEmpty(locfacobj.getNewsId()));
				if(locfacobj.getNewsimageName()!=null){
				lvrInrJSONObj.put("newsimagename",Commonutility.toCheckNullEmpty(locfacobj.getNewsimageName()));
				}else{
					lvrInrJSONObj.put("newsimagename","");	
				}
				lvrInrJSONObj.put("newcontent",Commonutility.toCheckNullEmpty(locfacobj.getNewsContent()));
				if(locfacobj.getExpiryDate()!=null){
				//lvrInrJSONObj.put("expirydate",Commonutility.toCheckNullEmpty(locfacobj.getExpiryDate()));
				Common lvddd= new CommonDao();
				lvrInrJSONObj.put("expirydate",lvddd.getDateobjtoFomatDateStr(locfacobj.getExpiryDate(), "dd-MM-yyyy HH:mm:ss"));
				}else{
					lvrInrJSONObj.put("expirydate","");	
				}
			}
			lvrRtnjsonobj=new JSONObject();	
			lvrRtnjsonobj.put("flashnewdetails", lvrInrJSONObj);
			System.out.println("Step 4 : Select flashnewdetails process End " +lvrRtnjsonobj);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in flashnewdetails() : "+expObj);
				logWrite.logMessage("Step -1 : flashnewdetails select all block Exception : "+expObj, "error", FlashNewUtility.class);	
				lvrRtnjsonobj=new JSONObject();
				lvrRtnjsonobj.put("CatchBlock", "Error");
				System.out.println("lvrRtnjsonobj : "+lvrRtnjsonobj);
				}catch(Exception ee){}finally{}
	     return lvrRtnjsonobj;
	    }finally {
	    	lvrRtnjsonobj = null;
	    	lvrInrJSONObj = null;	
	    	logWrite = null;
	    	lvrObjeventlstitr = null;
	    }

	}

	public static String toUpdateFunction(JSONObject pDataJson,Session locObjsession) {
		int resval=0;
		String locFtrnStr=null;
		String societyId=null,flashcontent=null,entryby=null,flashId=null,exdate=null,extime=null,imgsrchpath=null,imageName=null,title=null;
		Log log=null;	
		String updateQry=null;
		Common common = null;
		boolean lvrfunmtr = false;
		CommonUtilFunctions locCommutillObj=null;
		ResourceBundle rb = null;
		try{
			rb = ResourceBundle.getBundle("applicationResources");
			locCommutillObj = new CommonUtilFunctions();
			common=new CommonDao();
			log =  new Log();
			flashId=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"flashId");
			societyId=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"societyId");
			flashcontent=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"flashnewcontent");
			entryby=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"entryby");
			exdate=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"exdate");
			extime=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"extime");
			SimpleDateFormat recfrmt = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			SimpleDateFormat convrtfrmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			imgsrchpath= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"flashnewimage");
			imageName= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"flashnewimageName");
			title= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"title");
			Date lvrexpdate = null;
			String conrtdate=null;
			if (Commonutility.checkempty(exdate) && Commonutility.checkempty(extime)) {
				lvrexpdate = recfrmt.parse(exdate + " "+extime);
				conrtdate=convrtfrmt.format(lvrexpdate);
			}
			if(imageName!=null && !imageName.equalsIgnoreCase("null") && !imageName.equalsIgnoreCase("")){
			
				updateQry="update FlashNewsTblVO set newsContent='"+flashcontent+"',societyId="+societyId+",entryBy="+entryby+",expiryDate='"+conrtdate+"',newsimageName='"+imageName+"',title='"+title+"' where newsId="+flashId+"";
			}else{
				updateQry="update FlashNewsTblVO set newsContent='"+flashcontent+"',societyId="+societyId+",entryBy="+entryby+",expiryDate='"+conrtdate+"',title='"+title+"' where newsId="+flashId+"";	
			}
			lvrfunmtr=common.commonUpdate(updateQry);
				System.out.println("updateQry ----------- "+updateQry);
				
			if(lvrfunmtr){
				if(Integer.parseInt(flashId)!=0 && Integer.parseInt(flashId)!=-1){
					if(Commonutility.checkempty(imgsrchpath)){
						String lvrWebImagpath = rb.getString("external.uploadfile.flashnews.webpath");
						String lvrMobImgpath = rb.getString("external.uploadfile.flashnews.mobilepath");
						String delrs = Commonutility.todelete("", lvrWebImagpath+Integer.parseInt(flashId)+"/");
						String delrs_mob= Commonutility.todelete("", lvrMobImgpath+Integer.parseInt(flashId)+"/");	
						Commonutility.toWriteImageMobWebNewUtill(Integer.parseInt(flashId),imageName,imgsrchpath,lvrWebImagpath,lvrMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, FlashNewUtility.class);
					}
						locFtrnStr="success!_!"+resval;
					
				 }
				locFtrnStr="success!_!"+resval;
			}else{
				locFtrnStr="error!_!"+resval;
			}
			
		}catch(Exception e){
			System.out.println("Exception found in FlashNewUtility.tomodifyFunction : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", FlashNewUtility.class);
			locFtrnStr="error!_!"+resval;
			return locFtrnStr;
		}finally{
			 		
		}
		return locFtrnStr;
	
	}

	public static String toDeleteaction(JSONObject locObjRecvdataJson,Session locObjsession) {

		int resval=0;
		String locFtrnStr=null;
		String flashId=null;
		Log log=null;	
		String updateQry=null;
		Common common = null;
		boolean lvrfunmtr = false;
		try{
			
			common=new CommonDao();
			log =  new Log();
			flashId=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"flashId");
			updateQry="update FlashNewsTblVO set status=0 where newsId="+flashId+"";
			lvrfunmtr=common.commonUpdate(updateQry);
				System.out.println("lvrfunmtr ----------- "+lvrfunmtr);
			if(lvrfunmtr){
				locFtrnStr="success!_!"+resval;
			}else{
				locFtrnStr="error!_!"+resval;
			}
			
		}catch(Exception e){
			System.out.println("Exception found in FlashNewUtility.tomodifyFunction : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", FlashNewUtility.class);
			locFtrnStr="error!_!"+resval;
			return locFtrnStr;
		}finally{
			 		
		}
		return locFtrnStr;
	
	
	}

}
