package com.pack.feedback;

import java.util.Date;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONObject;

import com.pack.feedbackvo.FeedbackTblVO;
import com.pack.feedbackvo.persistence.FeedbackDao;
import com.pack.feedbackvo.persistence.FeedbackDaoservice;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class FeedbackUtility {

	public static String toInsertFeedback(JSONObject pDataJson){
		String locFtrnStr=null;
		int locFeedbkid=0;
		String locvrBnFEEDBK_TXT=null,locvrBnFEEDBK_FOR_USR_ID=null,locvrBnFEEDBK_FOR_USR_TYP=null,locCurrentusr=null;
		String locRating=null, locStatus=null;
		
		Log log=null;	
		CommonUtils locCommutillObj =null;
		FeedbackTblVO locfdbkvoObj=null;
		UserMasterTblVo locusrtbvo=null;
		FeedbackDao locfeedbackdaoObj=null;
		try{			
			locCommutillObj = new CommonUtilsDao();
			log=new Log();
			log.logMessage("Step 2 : Feedback Insert Block.", "info", FeedbackUtility.class);
			locvrBnFEEDBK_TXT=(String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "fdbkdesc");
			locvrBnFEEDBK_FOR_USR_ID=(String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "fdbkforuser");
			locvrBnFEEDBK_FOR_USR_TYP=(String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "fdbkforusergrp");
			locCurrentusr=(String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "currentloginid");
			locRating = (String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "fdbkrating");
			locStatus="1";
			locfdbkvoObj=new FeedbackTblVO();
			locfdbkvoObj.setIvrBnFEEDBK_TXT(locvrBnFEEDBK_TXT);
			
			if(!Commonutility.toCheckisNumeric(locvrBnFEEDBK_FOR_USR_ID)){
				locfdbkvoObj.setIvrBnFEEDBK_FOR_USR_ID(null);
			}else{
				locfdbkvoObj.setIvrBnFEEDBK_FOR_USR_ID(Integer.parseInt(locvrBnFEEDBK_FOR_USR_ID));
			}
			
			if(!Commonutility.toCheckisNumeric(locvrBnFEEDBK_FOR_USR_TYP)){
				locfdbkvoObj.setIvrBnFEEDBK_FOR_USR_TYP(null);
			}else{
				locfdbkvoObj.setIvrBnFEEDBK_FOR_USR_TYP(Integer.parseInt(locvrBnFEEDBK_FOR_USR_TYP));
			}
			
			if(!Commonutility.toCheckisNumeric(locStatus)){
				locfdbkvoObj.setIvrBnFEEDBK_STATUS(null);
			}else{
				locfdbkvoObj.setIvrBnFEEDBK_STATUS(Integer.parseInt(locStatus));
			}
			
			if(!Commonutility.toCheckisNumeric(locRating)){
				locfdbkvoObj.setIvrBnRATING(null);
			}else{
				locfdbkvoObj.setIvrBnRATING(Integer.parseInt(locRating));
			}
			
			
			if(!Commonutility.toCheckisNumeric(locCurrentusr)){
				locfdbkvoObj.setIvrBnUarmsttbvoobj(null);
			}else{
				locusrtbvo=new UserMasterTblVo();
				locusrtbvo.setUserId(Integer.parseInt(locCurrentusr));
				locfdbkvoObj.setIvrBnUarmsttbvoobj(locusrtbvo);
			}
			locfdbkvoObj.setIvrBnENTRY_DATETIME(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			
			 //----------- Feedback Insert Start-----------			
			 log.logMessage("Step 3 : Feedback Detail insert will start.", "info", FeedbackUtility.class);
			 locfeedbackdaoObj=new FeedbackDaoservice();
			 locFeedbkid=locfeedbackdaoObj.toInsertFeedback(locfdbkvoObj);
			 Commonutility.toWriteConsole(locFeedbkid+": id Feedback");
			 log.logMessage("Step 4 : Feedback Detail insert End Feedback Id : "+locFeedbkid, "info", FeedbackUtility.class);
			 if(locFeedbkid!=0){
				 locFtrnStr="success!_!"+locFeedbkid;
			 }else{
				 locFtrnStr="error!_!"+locFeedbkid;
			 }
			 return locFtrnStr;
			 // -----------Feedback Insert end------------							
		} catch (Exception e) {
			Commonutility.toWriteConsole("Exception found in FeedbackUtility.toInsrtfeedback : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", FeedbackUtility.class);
			locFeedbkid=0;
			locFtrnStr="error!_!"+locFeedbkid;
			return locFtrnStr;
		}finally{
			 pDataJson=null;log=null;locCommutillObj =null;locfdbkvoObj=null;locusrtbvo=null;locfeedbackdaoObj=null;
			 locvrBnFEEDBK_TXT=null;locvrBnFEEDBK_FOR_USR_ID=null;locvrBnFEEDBK_FOR_USR_TYP=null;locCurrentusr=null;locRating=null; locStatus=null; 
			 locFeedbkid=0;			
		}
		
	}
	
	public static String toUpdateFeedback(JSONObject pDataJson){
		String locFtrnStr=null;		
		String locvrBnFEEDBK_TXT=null,locCurrentusr=null, locvrBnFEEDBK_ID=null;
		String locRating=null, locStatus=null;
		
		Log log=null;			
		String locFdbkUpdqry="";
		boolean locFdbkUpdSts=false;
		FeedbackDao locfeedbackdaoObj=null;
		try{			
			log=new Log();
			log.logMessage("Step 2 : Feedback Update Block.", "info", FeedbackUtility.class);
			Commonutility.toWriteConsole("Step 2 : Feedback Update Block.");
			locvrBnFEEDBK_TXT=(String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "fdbkdesc");			
			locCurrentusr=(String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "currentloginid");
			locvrBnFEEDBK_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "feedbckid");
			locRating = (String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "fdbkrating");
			locStatus="1";
			if(Commonutility.toCheckisNumeric(locvrBnFEEDBK_ID)){
				locFdbkUpdqry ="update FeedbackTblVO set ivrBnFEEDBK_TXT = '"+locvrBnFEEDBK_TXT+"', ";
			
				if(!Commonutility.toCheckisNumeric(locRating)){
					locFdbkUpdqry+="ivrBnRATING = "+null+", ";
				}else{
					locFdbkUpdqry+="ivrBnRATING = "+Integer.parseInt(locRating)+", ";
				}	
				
				if(!Commonutility.toCheckisNumeric(locStatus)){
					locFdbkUpdqry+="ivrBnFEEDBK_STATUS = 0 ";
				}else{
					locFdbkUpdqry+="ivrBnFEEDBK_STATUS = "+Integer.parseInt(locStatus)+" ";
				}
				
				locFdbkUpdqry +=" where ivrBnFEEDBK_ID="+Integer.parseInt(locvrBnFEEDBK_ID)+"";			
				if(Commonutility.toCheckisNumeric(locCurrentusr)){
					locFdbkUpdqry+= " and ivrBnUarmsttbvoobj = "+Integer.parseInt(locCurrentusr)+" ";		
				}
				
				log.logMessage("Step 3 : Feedback Update Qry : "+locFdbkUpdqry, "info", FeedbackUtility.class);
				Commonutility.toWriteConsole("Step 3 : Feedback Update Qry : "+locFdbkUpdqry);
				
				locfeedbackdaoObj=new FeedbackDaoservice();
				locFdbkUpdSts=locfeedbackdaoObj.toUpdateFeedback(locFdbkUpdqry);				
			}else{
				locFdbkUpdSts=false;
			}
			log.logMessage("Step 4 : Feedback Update Status : "+locFdbkUpdSts, "info", FeedbackUtility.class);
			Commonutility.toWriteConsole("Step 4 : Feedback Update Status : "+locFdbkUpdSts);
			if(locFdbkUpdSts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			return locFtrnStr;			
		}catch(Exception e){			
			locFtrnStr="error";
			log.logMessage("Step -1 : Feedback Update Exception : "+e, "error", FeedbackUtility.class);
			Commonutility.toWriteConsole("Step -1 : Feedback Update Exception : "+e);			
			return locFtrnStr;
		}finally{			
			locFtrnStr=null;locvrBnFEEDBK_TXT=null;locCurrentusr=null; locvrBnFEEDBK_ID=null;locRating=null; locStatus=null;locFdbkUpdqry="";
			locFdbkUpdSts=false;
			log=null;locfeedbackdaoObj=null;
		}		
	}
	
	public static String toDeactiveFeedback(JSONObject pDataJson){
		String locFtrnStr=null;
		Log log=null;			
		String locFdbkUpdqry="";
		boolean locFdbkUpdSts=false;
		FeedbackDao locfeedbackdaoObj=null;
		String locvrBnFEEDBK_ID=null,locStatus=null;
		try{			
			log=new Log();
			log.logMessage("Step 2 : Feedback Deactive Block.", "info", FeedbackUtility.class);
			Commonutility.toWriteConsole("Step 2 : Feedback Deactive Block.");			
			locvrBnFEEDBK_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "feedbckid");
			locStatus="0";
			
			locFdbkUpdqry ="update FeedbackTblVO set ivrBnFEEDBK_STATUS = "+Integer.parseInt(locStatus)+"  where ivrBnFEEDBK_ID="+Integer.parseInt(locvrBnFEEDBK_ID)+"";
			log.logMessage("Step 3 : Feedback Deactive query : "+locFdbkUpdqry, "info", FeedbackUtility.class);
			Commonutility.toWriteConsole("Step 3 : Feedback Deactive query : "+locFdbkUpdqry);			
			locfeedbackdaoObj=new FeedbackDaoservice();
			locFdbkUpdSts=locfeedbackdaoObj.toUpdateFeedback(locFdbkUpdqry);	
			if(locFdbkUpdSts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}			
			return locFtrnStr;
		}catch(Exception e){			
			locFtrnStr="error";
			log.logMessage("Step -1 : Feedback Deactive Exception : "+e, "error", FeedbackUtility.class);
			Commonutility.toWriteConsole("Step -1 : Feedback Deactive Exception : "+e);			
			return locFtrnStr;
		}finally{
			
		}
		
	}
	
	public static JSONObject toselctFeedbacksingle(JSONObject pDataJson){
		String locvrBnFEEDBK_ID=null,locslQry=null;
		Log log=null;
		Session locHbSession=null;
		Query locQryObj= null;
		FeedbackTblVO locfeedbkvoobj=null;
		JSONObject locRtnDataJSON=null;
		Date locdate=null;
		Common locCommonObj=new CommonDao();
		String locvrfdbk_GRP=null;
		Iterator locObjGroupgorylst=null;
		Iterator locObjlbrgorylst=null;
		GroupMasterTblVo group=null;
		LaborProfileTblVO lbr=null;
		locdate=null;
		String lsvSlQrygrp=null,lsvSlQryusrgrp=null;
		int locvrfdbk_usrGRP=0;
		try{
			log=new Log();locRtnDataJSON=new JSONObject();
			log.logMessage("Step 2 : Feedback singleSelect Block.", "info", FeedbackUtility.class);
			Commonutility.toWriteConsole("Step 2 : Feedback singleSelect Block.");	
			locHbSession=HibernateUtil.getSession();
			//locCurrentusr=(String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "currentloginid");
			locvrBnFEEDBK_ID = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "feedbckid");
			locslQry="from FeedbackTblVO where ivrBnFEEDBK_ID="+Integer.parseInt(locvrBnFEEDBK_ID)+"";
			log.logMessage("Step 3 : Feedback singleSelect locslQry : "+locslQry, "info", FeedbackUtility.class);
			Commonutility.toWriteConsole("Step 3 : Feedback singleSelect locslQry : "+locslQry);	
			locQryObj=locHbSession.createQuery(locslQry);	
			locfeedbkvoobj=(FeedbackTblVO)locQryObj.uniqueResult();
			locdate=locfeedbkvoobj.getIvrBnENTRY_DATETIME();
			Commonutility.toWriteConsole("Step 3 : Select feedback detail query executed.");
			locRtnDataJSON.put("feedbackid", Commonutility.toCheckNullEmpty(locfeedbkvoobj.getIvrBnFEEDBK_ID()));
			locRtnDataJSON.put("feedbacktext", Commonutility.toCheckNullEmpty(locfeedbkvoobj.getIvrBnFEEDBK_TXT()));
			locRtnDataJSON.put("feedbackrating", Commonutility.toCheckNullEmpty(locfeedbkvoobj.getIvrBnRATING()));
			locRtnDataJSON.put("feedbackforusrid", Commonutility.toCheckNullEmpty(locfeedbkvoobj.getIvrBnFEEDBK_FOR_USR_ID()));
			locRtnDataJSON.put("feedbackforusrtyp", Commonutility.toCheckNullEmpty(locfeedbkvoobj.getIvrBnFEEDBK_FOR_USR_TYP()));
			locRtnDataJSON.put("feedbackstatus", Commonutility.toCheckNullEmpty(locfeedbkvoobj.getIvrBnFEEDBK_STATUS()));
			locRtnDataJSON.put("feedbackdate", locCommonObj.getDateobjtoFomatDateStr(locdate, "yyyy-MM-dd HH:mm:ss"));
			locRtnDataJSON.put("dbkby_fname", Commonutility.toCheckNullEmpty(locfeedbkvoobj.getIvrBnUarmsttbvoobj().getFirstName()));
			if(locfeedbkvoobj.getIvrBnUarmsttbvoobj()!=null){					
				locvrfdbk_GRP = (String)Commonutility.toCheckNullEmpty(locfeedbkvoobj.getIvrBnFEEDBK_FOR_USR_TYP());
				locvrfdbk_usrGRP =locfeedbkvoobj.getIvrBnFEEDBK_FOR_USR_ID();
				lsvSlQrygrp="from GroupMasterTblVo where groupCode='"+locvrfdbk_GRP+"' ";
				locObjGroupgorylst=locHbSession.createQuery(lsvSlQrygrp).list().iterator();
				while(locObjGroupgorylst.hasNext()) {						
					group=(GroupMasterTblVo)locObjGroupgorylst.next();
					locRtnDataJSON.put("dbkby_group", Commonutility.toCheckNullEmpty(group.getGroupName()));
						lsvSlQryusrgrp="from LaborProfileTblVO where ivrBnLBR_ID='"+locvrfdbk_usrGRP+"'";
						locObjlbrgorylst = locHbSession.createQuery(lsvSlQryusrgrp).list().iterator();
						while(locObjlbrgorylst.hasNext()) {	
							lbr=(LaborProfileTblVO)locObjlbrgorylst.next();
							locRtnDataJSON.put("feedbacttoname", Commonutility.toCheckNullEmpty(lbr.getIvrBnLBR_NAME()));
							locRtnDataJSON.put("lbrserviceid", Commonutility.toCheckNullEmpty(lbr.getIvrBnLBR_SERVICE_ID()));
						
						
					}/*else if(getText("Grp.merchant").equalsIgnoreCase(group.getGroupName())){
						
						
					}*/
				}
			}
			return locRtnDataJSON;
		}catch(Exception e){
			try{
				Commonutility.toWriteConsole("Step -1 : Select Feedback singleSelect Exception found in FeedbackUtility.toselctFeedbacksingle : "+e);
				log.logMessage("Exception : "+e, "error", FeedbackUtility.class);
				locRtnDataJSON=new JSONObject();
				locRtnDataJSON.put("catch", "Error");
				}catch(Exception ee){}
			return locRtnDataJSON;
		}finally{
			locHbSession.flush();locHbSession.clear();locHbSession.close();
			  locvrBnFEEDBK_ID=null;locslQry=null;
			 log=null;
			 locHbSession=null;
			 locQryObj= null;
			 locfeedbkvoobj=null;
			 locRtnDataJSON=null;
		}
		
	}
	
}
