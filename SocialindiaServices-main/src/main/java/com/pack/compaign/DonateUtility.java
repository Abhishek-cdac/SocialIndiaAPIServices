package com.pack.compaign;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.compaignVo.CompaignDao;
import com.pack.compaignVo.CompaignServices;
import com.pack.flashnews.FlashNewUtility;
import com.pack.labor.LaborUtility;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtilFunctions;
import com.socialindiaservices.vo.MvpDonationInstitutionTbl;

public class DonateUtility extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String toInsertFunction(JSONObject pDataJson) {
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		int resval=0;
		String locFtrnStr=null;
		String societyId=null,instituname=null,entryby=null,institucontact=null,instituaddress=null,image=null;
		String imgsrchpath=null;
		Log log=null;	
		SocietyMstTbl society=null;
		CommonUtilFunctions locCommutillObj =null;
		CompaignDao compaigndao=null;
		MvpDonationInstitutionTbl donateObj=null;
		UserMasterTblVo userObj=null;
		try{
			log=new Log();
			compaigndao=new CompaignServices();
			society=new SocietyMstTbl();
			locCommutillObj = new CommonUtilFunctions();
			donateObj=new MvpDonationInstitutionTbl();
			societyId=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"societyId");
			instituname=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"instituname");
			entryby=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"entryby");
			institucontact= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"institucontact");
			instituaddress= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"instituaddress");
			imgsrchpath= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"imgsrchpath");
			image= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"imageName");
			
			society.setSocietyId(Integer.parseInt(societyId));
			donateObj.setSocietyId(society);
			userObj=new UserMasterTblVo();
			userObj.setUserId(Integer.parseInt(entryby));
			donateObj.setImageName(image);
			donateObj.setInstitutionName(instituname);
			donateObj.setInstitutionContact(institucontact);
			donateObj.setEntryBy(userObj);
			donateObj.setStatus(1);
			donateObj.setInstitutionAddress(instituaddress);
			donateObj.setEntryDate(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			int donateId=compaigndao.insertdate(donateObj);
			try {
				 if(donateId!=0 && donateId!=-1){
					 if(Commonutility.checkempty(image)){
						 String lvrWebImagpath = rb.getString("external.uploadfile.donate.webpath");
						 String lvrMobImgpath = rb.getString("external.uploadfile.donate.mobilepath");
						/* String delrs = Commonutility.todelete("", lvrWebImagpath+donateId+"/");
						 String delrs_mob= Commonutility.todelete("", lvrMobImgpath+donateId+"/");	*/
						 Commonutility.toWriteImageMobWebNewUtill(donateId,image,imgsrchpath,lvrWebImagpath,lvrMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, DonateUtility.class);
					 }
				 }
			} catch(Exception imger){
				 System.out.println("Exception in  image write on DonateUtility insert : "+imger);
				 log.logMessage("Exception in Image write on labor DonateUtility", "info", LaborUtility.class);
			 }finally{

			 }
			
			if(resval!=-1){
				locFtrnStr="success!_!"+resval;
			}else{
				locFtrnStr="error!_!"+resval;
			}
			
		}catch(Exception e){
			System.out.println("Exception found in DonateUtility.toInsertFunction : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", DonateUtility.class);
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
		MvpDonationInstitutionTbl  locdonobj = null;
		String lvrSlqry = null;
		String donateId=null;
	    try {
	    	donateId  = (String)Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "donateId");
			lvrSlqry = "from MvpDonationInstitutionTbl  where institutionId="+donateId;
			lvrObjeventlstitr=locObjsession.createQuery(lvrSlqry).list().iterator();
			while (lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				locdonobj = (MvpDonationInstitutionTbl) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("insId",Commonutility.toCheckNullEmpty(locdonobj.getInstitutionId()));
				lvrInrJSONObj.put("insName",Commonutility.toCheckNullEmpty(locdonobj.getInstitutionName()));
				lvrInrJSONObj.put("inscontact",Commonutility.toCheckNullEmpty(locdonobj.getInstitutionContact()));
				lvrInrJSONObj.put("insAdd",Commonutility.toCheckNullEmpty(locdonobj.getInstitutionAddress()));
				lvrInrJSONObj.put("insimage",Commonutility.toCheckNullEmpty(locdonobj.getImageName()));
				if(locdonobj.getSocietyId()!=null){
				lvrInrJSONObj.put("societyId",Commonutility.toCheckNullEmpty(locdonobj.getSocietyId().getSocietyId()));
				lvrInrJSONObj.put("societyName",Commonutility.toCheckNullEmpty(locdonobj.getSocietyId().getSocietyName()));
				lvrInrJSONObj.put("townId",Commonutility.toCheckNullEmpty(locdonobj.getSocietyId().getTownshipId().getTownshipId()));
				lvrInrJSONObj.put("townName",Commonutility.toCheckNullEmpty(locdonobj.getSocietyId().getTownshipId().getTownshipName()));
				}else{
					lvrInrJSONObj.put("societyId","");
					lvrInrJSONObj.put("societyName","");
					lvrInrJSONObj.put("townId","");
					lvrInrJSONObj.put("townName","");
				}
				
			}
			lvrRtnjsonobj=new JSONObject();	
			lvrRtnjsonobj.put("donation", lvrInrJSONObj);
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
		String societyId=null,instituname=null,entryby=null,institucontact=null,instituaddress=null,image=null;
		String imgsrchpath=null;
		Log log=null;	
		String updateQry=null;
		Common common = null;
		boolean lvrfunmtr = false;
		int donateId=0;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		try{
			common=new CommonDao();
			log =  new Log();
			donateId=Integer.parseInt((String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"donateId"));
			societyId=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"societyId");
			instituname=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"instituname");
			entryby=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"entryby");
			institucontact= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"institucontact");
			instituaddress= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"instituaddress");
			imgsrchpath= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"imgsrchpath");
			image= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"imageName");
			if(image!=null && !image.equalsIgnoreCase("null") && !image.equalsIgnoreCase("")){
			updateQry="update MvpDonationInstitutionTbl set institutionName='"+instituname+"',societyId="+societyId+",entryBy="+entryby+",institutionAddress='"+instituaddress+"',institutionContact='"+institucontact+"',imageName='"+image+"' where institutionId="+donateId+"";
			System.out.println("updateQry ---1------ "+updateQry);
			lvrfunmtr=common.commonUpdate(updateQry);
			}else{
				updateQry="update MvpDonationInstitutionTbl set institutionName='"+instituname+"',societyId="+societyId+",entryBy="+entryby+",institutionAddress='"+instituaddress+"',institutionContact='"+institucontact+"' where institutionId="+donateId+"";
				System.out.println("updateQry ---2------ "+updateQry);
				lvrfunmtr=common.commonUpdate(updateQry);	
			}
			try {
				 if(donateId!=0 && donateId!=-1){
					 if(Commonutility.checkempty(image)){
						 String lvrWebImagpath = rb.getString("external.uploadfile.donate.webpath");
						 String lvrMobImgpath = rb.getString("external.uploadfile.donate.mobilepath");
						 String delrs = Commonutility.todelete("", lvrWebImagpath+donateId+"/");
						 String delrs_mob= Commonutility.todelete("", lvrMobImgpath+donateId+"/");	
						 Commonutility.toWriteImageMobWebNewUtill(donateId,image,imgsrchpath,lvrWebImagpath,lvrMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, DonateUtility.class);
					 }
				 }
			} catch(Exception imger){
				 System.out.println("Exception in  image write on DonateUtility insert : "+imger);
				 log.logMessage("Exception in Image write on labor DonateUtility", "info", LaborUtility.class);
			 }finally{

			 }
			
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
	public static String toDeleteaction(JSONObject locObjRecvdataJson,Session locObjsession) {
		int resval=0;
		String locFtrnStr=null;
		String donateId=null;
		Log log=null;	
		String updateQry=null;
		Common common = null;
		boolean lvrfunmtr = false;
		try{
			
			common=new CommonDao();
			log =  new Log();
			donateId=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"donateId");
			updateQry="update MvpDonationInstitutionTbl set status=0 where institutionId="+donateId+"";
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
