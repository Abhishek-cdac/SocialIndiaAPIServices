package com.pack.event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.commonvo.PrivacyInfoTblVO;
import com.mobi.complaints.ComplaintsDao;
import com.mobi.complaints.ComplaintsDaoServices;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.jsonpack.JsonpackDao;
import com.mobi.jsonpack.JsonpackDaoService;
import com.pack.audittrial.AuditTrial;
import com.pack.calendar.OutCalendar;
import com.pack.eventvo.EventDispTblVO;
import com.pack.eventvo.EventTblVO;
import com.pack.eventvo.persistence.EventDao;
import com.pack.eventvo.persistence.EventDaoservice;
import com.pack.labor.LaborUtility;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.persistence.DocumentUtilitiHibernateDao;
import com.socialindiaservices.vo.FacilityMstTblVO;
import com.socialindiaservices.vo.FunctionMasterTblVO;
import com.socialindiaservices.vo.FunctionTemplateTblVO;

public class Eventutility {
	/*
	 * to insert into Event table.
	 */
	static String isvrClientfrmt = "dd-MM-yyyy hh:ss a";
	static String isvrClientfrmt_sub = "dd-MM-yyyy hh:ssa";
	static SimpleDateFormat locSmftclinetfrmt = new SimpleDateFormat("dd-MM-yyyy hh:ss a");// Client Select Date Format	
	
	public static String toInsertEvent(JSONObject prDatajson,String pGrpName, String prAuditMsg, String pAuditCode, String pWebImagpath, String pMobImgpath,Log log){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrEvnttit=null, lvrEvntShrtdesc=null, lvrEvntdesc=null,lvrEvntfilename=null,lvrEvntfiledata=null,lvrEvntVdpath=null, lvrEvnttitfundatid=null, lvrEvntTemplateid = null;//lvrEvnttitfundatid -  auto complete select id on function table
		String lvrEvntFacilityid=null,lvrEvntstartdate=null,lvrEvntenddate=null, lvrEvntlocation=null, lvrEvntSttime=null, lvrEvntEndtime=null, lvrEvntSts=null, lvrEntlatlong = null, desc="You Have Received A New Event Notification";
		String eventtype=null, lvrEvntfilesrchpth = null,lvrEvntrsvp=null;
		Session locSession=null;
		int usrid = 0, sharid = 0; 
		DocumentUtilitiHibernateDao docHibernateUtilService = null;
		UserMasterTblVo locUsrMstrVOobj = null;
		EventTblVO locEvntvoObj = null, lvrEvntvoobj = null;
		CommonUtils locCommutillObj = null;
		EventDao loceventDaoObj = null;
		int locEventid = 0;
		Common lvrCommdaosrobj = null;
		GroupMasterTblVo locGrpmstvoobj = null;
		Iterator locObjSocietyRestlst = null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		try {
			docHibernateUtilService = new DocumentUtilitiHibernateDao();
			locSession = HibernateUtil.getSession();
			locCommutillObj = new CommonUtilsDao();
			lvrCommdaosrobj = new CommonDao();
			log.logMessage("Step 2 : Event Insert Block.", "info", Eventutility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrEvnttit = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventtitle");
			lvrEvntShrtdesc = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "shortdesc");
			lvrEvntdesc = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventdesc");
			lvrEvntfilename = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventfilename");
			//lvrEvntfiledata = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventfiledata");
			lvrEvntfilesrchpth = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventfilesrchpath");
			lvrEvntVdpath = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventvideopath");
			lvrEvntstartdate = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventstartdate");
			lvrEvntenddate = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventenddate");
			lvrEvntSttime = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventstarttime");
			lvrEvntEndtime = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventendtime");
			lvrEvntlocation = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventlocation");
			lvrEntlatlong = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventlatlong");
			lvrEvnttitfundatid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventtitlefunctionid");
			lvrEvntTemplateid  = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventfuntemlateid");
			lvrEvntFacilityid  = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventfacilityid");
			eventtype  = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventtype");
			lvrEvntrsvp = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "eventrsvp");

			
			
			lvrEvntSts="1";			
			locEvntvoObj=new EventTblVO();	
			locEvntvoObj.setIvrBnENTRY_DATETIME(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			locEvntvoObj.setIvrBnEVENTSTATUS(Integer.parseInt(lvrEvntSts));
			if(!Commonutility.toCheckisNumeric(lvrCrntusrid)){
				locEvntvoObj.setIvrBnEVENT_CRT_USR_ID(null);
				locEvntvoObj.setIvrBnENTRY_BY(null);
			}else{
				UserMasterTblVo userinfo=new UserMasterTblVo();
				userinfo.setUserId(Integer.parseInt(lvrCrntusrid));
				locEvntvoObj.setIvrBnEVENT_CRT_USR_ID(userinfo);
				locEvntvoObj.setIvrBnENTRY_BY(Integer.parseInt(lvrCrntusrid));
			}			
			if (!Commonutility.toCheckisNumeric(lvrCrntgrpid)) {
				locEvntvoObj.setIvrBnEVENT_CRT_GROUP_ID(null);
			} else {
				locEvntvoObj.setIvrBnEVENT_CRT_GROUP_ID(Integer
						.parseInt(lvrCrntgrpid));
			}
			if (Commonutility.checkempty(lvrEvntfilename)) {
				lvrEvntfilename = lvrEvntfilename.replaceAll(" ", "_");
			}
			
			locEvntvoObj.setIvrBnEVENT_TITLE(lvrEvnttit);
			locEvntvoObj.setIvrBnSHORT_DESC(lvrEvntShrtdesc);
			locEvntvoObj.setIvrBnEVENT_DESC(lvrEvntdesc);
			locEvntvoObj.setIvrBnEVENT_FILE_NAME(lvrEvntfilename);
			locEvntvoObj.setIvrBnVIDEO_PATH(lvrEvntVdpath);
			locEvntvoObj.setIvrBnSTARTDATE(lvrEvntstartdate);//dd-MM-yyyy
			locEvntvoObj.setIvrBnENDDATE(lvrEvntenddate);//dd-MM-yyyy
			locEvntvoObj.setIvrBnSTARTTIME(lvrEvntSttime);//hh:mm a
			locEvntvoObj.setIvrBnENDTIME(lvrEvntEndtime);//hh:mm a
			locEvntvoObj.setIvrBnEVENT_LOCATION(lvrEvntlocation);
			locEvntvoObj.setIvrBnLAT_LONG(lvrEntlatlong);
			locEvntvoObj.setIvrBnEVENTT_TYPE(Integer.parseInt(eventtype));
			locEvntvoObj.setIvrBnISRSVP(Integer.parseInt(lvrEvntrsvp));
			if(!Commonutility.toCheckisNumeric(lvrEvnttitfundatid)){
				locEvntvoObj.setIvrBnFUNCTION_ID(null);				
			} else {				
				FunctionMasterTblVO lvrfuntbl = new FunctionMasterTblVO();
				lvrfuntbl.setFunctionId(Integer.parseInt(lvrEvnttitfundatid));
				locEvntvoObj.setIvrBnFUNCTION_ID(lvrfuntbl);
				int eventypeflg = (Integer) lvrCommdaosrobj.getuniqueColumnVal("FunctionMasterTblVO", "ivrBnobjEVENT_TYPE", "functionId", lvrEvnttitfundatid);
			}
			
			if(!Commonutility.toCheckisNumeric(lvrEvntTemplateid)){//lvrEvntTemplateid
				locEvntvoObj.setFunctionTemplateId(null);
			} else{
				FunctionTemplateTblVO lvrFunctmobj = new FunctionTemplateTblVO();
				lvrFunctmobj.setFunctionTempId(Integer.parseInt(lvrEvntTemplateid));
				locEvntvoObj.setFunctionTemplateId(lvrFunctmobj);
			}
			if (!Commonutility.toCheckisNumeric(lvrEvntFacilityid)){//lvrEvntTemplateid
				locEvntvoObj.setFunctionTemplateId(null);
			} else{
				FacilityMstTblVO lvrFacilitymobj = new FacilityMstTblVO();
				lvrFacilitymobj.setFacilityId(Integer.parseInt(lvrEvntFacilityid));
				locEvntvoObj.setFaciltyTemplateId(lvrFacilitymobj);
			}
			
			
			//----------- Event Insert Start-----------			
			log.logMessage("Step 3 : Event Detail insert will start.", "info", Eventutility.class);
			loceventDaoObj=new EventDaoservice();
			locEventid = loceventDaoObj.toInsertEvent(locEvntvoObj);
			System.out.println(locEventid+": id Eevnt");
			log.logMessage("Step 4 : Event Detail insert End Event Id : "+locEventid, "info", Eventutility.class);
			// -----------Event Insert end------------		
			if (locEventid>0) {				
				 try {				
					 log.logMessage("Step 5 : Event Image Write will start.", "info", Eventutility.class);														
					 //image write into folder
					 if((lvrEvntfilesrchpth!=null && !lvrEvntfilesrchpth.equalsIgnoreCase("null") && !lvrEvntfilesrchpth.equalsIgnoreCase("")) && (lvrEvntfilename!=null && !lvrEvntfilename.equalsIgnoreCase("") && !lvrEvntfilename.equalsIgnoreCase("null"))){						
						 //image write into folder
						 //String delrs = Commonutility.todelete("", locWebImgFldrPath+merchantObj.getMrchntId()+"/");
						 //String delrs_mob= Commonutility.todelete("", locMobImgFldrPath+merchantObj.getMrchntId()+"/");					  
						 Commonutility.toWriteImageMobWebNewUtill(locEventid, lvrEvntfilename,lvrEvntfilesrchpth,pWebImagpath,pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, Eventutility.class);
						/* 
						 byte imgbytee[]= new byte[1024];
						 imgbytee=Commonutility.toDecodeB64StrtoByary(lvrEvntfiledata);
						 String wrtsts=Commonutility.toByteArraytoWriteFiles(imgbytee, pWebImagpath+locEventid+"/", lvrEvntfilename);						 
						//mobile - small image
						String limgSourcePath=pWebImagpath+locEventid+"/"+lvrEvntfilename;			
				 		String limgDisPath = pMobImgpath+locEventid+"/";
				 		String limgName = FilenameUtils.getBaseName(lvrEvntfilename);
				 		String limageFomat = FilenameUtils.getExtension(lvrEvntfilename);		 	    			 	    	 
				 	    String limageFor = "all";
				 	    int lneedWidth = Commonutility.stringToInteger(rb.getString("thump.img.width"));
		        		int lneedHeight = Commonutility.stringToInteger(rb.getString("thump.img.height"));	
				        ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
				        */
					 }else{
						 
					 }
					 log.logMessage("Step 6 : Event Image Write will end.", "info", Eventutility.class);
				 }catch(Exception imge){
					 System.out.println("Exception in  image write on event insert : "+imge);
					 log.logMessage("step -2 : Exception in Image write on event insert", "info", Eventutility.class);
				 }finally {}	
				 
				 //Feed Insert
				 Query eventQrybj = null;EventTblVO updatedeventObj = null;FeedDAO feadhbm = null;
				 FeedsTblVO feedObj = null;CommonMobiDao commonServ = null;UserMasterTblVo userMst = null;
				 try {
					 	ResourceBundle rbApidetail = ResourceBundle.getBundle("apiDetails");
					 	updatedeventObj = new EventTblVO();
						String eventQry = "from EventTblVO where ivrBnEVENT_ID = "+locEventid;
						eventQrybj = locSession.createQuery(eventQry);
						updatedeventObj = (EventTblVO)eventQrybj.uniqueResult();
						commonServ = new CommonMobiDaoService();
						userMst = new UserMasterTblVo();
						userMst = commonServ.getProfileDetails(Integer.parseInt(lvrCrntusrid));
						feadhbm = new FeedDAOService();
						feedObj = new FeedsTblVO();
						feedObj.setUsrId(userMst);
						int feedTypFlg = 0;
						if (eventtype.equalsIgnoreCase("1")) {  //personal event
							feedTypFlg = 9;
						} else if (eventtype.equalsIgnoreCase("2")) {  //society event
							feedTypFlg = 8;
						} else if (eventtype.equalsIgnoreCase("3")) {  //committee meeting
							feedTypFlg = 12;
						}
						feedObj.setFeedType(feedTypFlg);
						feedObj.setFeedTypeId(locEventid);
						if(updatedeventObj.getIvrBnEVENT_TITLE()!=null){
						feedObj.setFeedTitle(updatedeventObj.getIvrBnEVENT_TITLE());
						}
						String tmpcont="";
						String eventdesc="";
						if(updatedeventObj.getFunctionTemplateId()!=null && updatedeventObj.getFunctionTemplateId().getTemplateText()!=null){
							tmpcont+=updatedeventObj.getFunctionTemplateId().getTemplateText();
						}
						if(updatedeventObj.getIvrBnEVENT_DESC()!=null){
							eventdesc=updatedeventObj.getIvrBnEVENT_DESC();
						}
						if(tmpcont!=null && tmpcont.length()>0){
							tmpcont+="<BR>"+eventdesc;
						}else{
							tmpcont+=eventdesc;
						}
						if(updatedeventObj.getIvrBnFUNCTION_ID()!=null && updatedeventObj.getIvrBnFUNCTION_ID().getFunctionName()!=null){
						feedObj.setFeedCategory(updatedeventObj.getIvrBnFUNCTION_ID().getFunctionName());
						}
						feedObj.setFeedDesc(tmpcont);
						feedObj.setPostBy(Integer.parseInt(lvrCrntusrid));
						feedObj.setOriginatorName(Commonutility.stringToStringempty(userMst.getFirstName()));
						feedObj.setSocietyId(userMst.getSocietyId());
						if(userMst.getCityId()!=null){
						feedObj.setFeedLocation(userMst.getCityId().getCityName());
						}
						feedObj.setOriginatorId(Integer.parseInt(lvrCrntusrid));
						feedObj.setEntryBy(Integer.parseInt(lvrCrntusrid));																												
						feedObj.setFeedStatus(1);
						CommonMobiDao comondaohbm=new CommonMobiDaoService();
						PrivacyInfoTblVO privacydata = new PrivacyInfoTblVO();
						privacydata = comondaohbm.getDefaultPrivacyFlg(Integer.parseInt(lvrCrntusrid));
						if (privacydata != null && Commonutility.checkIntnull(privacydata.getPrivacyFlg())) {
							feedObj.setFeedPrivacyFlg(privacydata.getPrivacyFlg());	
						} else {
							feedObj.setFeedPrivacyFlg(Commonutility.stringToInteger(rbApidetail.getString("default.privacy.category")));	
						}
						feedObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
						feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
						feedObj.setIsMyfeed(1);
						feedObj.setIsShared(0);
						int retFeedId = feadhbm.feedInsert(null, feedObj, null, null, null);
						System.out.println("insert retFeedId---------------" + retFeedId);
						boolean additionUpdate = false;
						if (retFeedId != -1 && locEventid > 0) {
							ComplaintsDao complains=new ComplaintsDaoServices();
							/*Additional data insert*/
							JSONObject jsonPublishObj = new JSONObject(); 
							JsonpackDao jsonPack = new JsonpackDaoService();
							jsonPublishObj = jsonPack.eventTableJasonpackValues(locEventid);
							if (jsonPublishObj != null) {
								additionUpdate = complains.additionalFeedUpdate(jsonPublishObj,retFeedId);
							}
						}
				} catch (Exception feedEx) {
					 System.out.println("Exception in event Feed insert : "+feedEx);
					 log.logMessage("Exception in event Feed insert", "info", Eventutility.class);
				} finally {
					eventQrybj = null;updatedeventObj = null;feadhbm = null;feedObj = null;commonServ = null;userMst = null;
				}
				 							
				 // set group code
				 String logrpcode = null;				 
				 locGrpmstvoobj=new GroupMasterTblVo();						 
				 logrpcode = lvrCommdaosrobj.getGroupcodeexistornew(pGrpName);
				 if(logrpcode!= null && !logrpcode.equalsIgnoreCase("error")){
					 if(Commonutility.toCheckisNumeric(logrpcode)){
						 locGrpmstvoobj.setGroupCode(Integer.parseInt(logrpcode));	
					 }				
				 }else{	
					 locGrpmstvoobj = null;
				 }						
				 EventDispTblVO lvrEvDsiptblvoobj=null;
				 log.logMessage("Event share [invite] table insert will start.", "info", Eventutility.class);
				 JSONArray jry = (JSONArray) Commonutility.toHasChkJsonRtnValObj(prDatajson, "shareusridJary");// Event disp user id insert into
				 //String usrGrpcode = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "shareusrgrpid");//  share user group id
				Commonutility.toWriteConsole("LINE : 1");
				lvrEvDsiptblvoobj = new EventDispTblVO();
				if (Commonutility.toCheckisNumeric(lvrCrntusrid)) {
					lvrEvDsiptblvoobj.setIvrBnENTRY_BY(null);
				} else {
					lvrEvDsiptblvoobj.setIvrBnENTRY_BY(Integer.parseInt(lvrCrntusrid));
				}
				Commonutility.toWriteConsole("LINE : 2");
				lvrEvDsiptblvoobj.setIvrBnEVENTSTATUS(1);

				if (locEventid > 0) {
					lvrEvntvoobj = new EventTblVO();
					lvrEvntvoobj.setIvrBnEVENT_ID(locEventid);
					lvrEvDsiptblvoobj.setIvrBnEVENT_ID(lvrEvntvoobj);
				} else {
					lvrEvDsiptblvoobj.setIvrBnEVENT_ID(null);
				}					
					//lvrEvDsiptblvoobj.getIvrBnEVENT_ID().setIvrBnEVENT_ID(locEventid);
					
				if(locEvntvoObj.getIvrBnEVENT_CRT_USR_ID()!=null){
					lvrEvDsiptblvoobj.setIvrBnUAMMSTtbvoobj(locEvntvoObj.getIvrBnEVENT_CRT_USR_ID());
				} else {
					lvrEvDsiptblvoobj.setIvrBnUAMMSTtbvoobj(null);
				}								 
				if (locGrpmstvoobj != null) {						
					lvrEvDsiptblvoobj.setIvrBnGROUPMSTvoobj(locGrpmstvoobj);
				} else {						
					lvrEvDsiptblvoobj.setIvrBnGROUPMSTvoobj(null);
				}
				loceventDaoObj.toInsertEventDispTbl(lvrEvDsiptblvoobj);
				boolean shrusrflg = false;
				UserMasterTblVo locUsobj = null;
				uamDao lvrUamdaosrv = null;
				lvrUamdaosrv = new uamDaoServices();
				Commonutility.toWriteConsole("LINE : 5 ; jry : " + jry);
				if (jry != null) {
					Commonutility.toWriteConsole("LINE : 6 if ");
					for (int i = 0; i < jry.length(); i++) {
						String locshrusrid = (String) jry.getString(i);
						try {
							if (Commonutility.toCheckisNumeric(locshrusrid)) {
								shrusrflg = true;
								lvrEvDsiptblvoobj = new EventDispTblVO();
								if (Commonutility.toCheckisNumeric(lvrCrntusrid)) {
									lvrEvDsiptblvoobj.setIvrBnENTRY_BY(null);
								}else{
									lvrEvDsiptblvoobj.setIvrBnENTRY_BY(Integer.parseInt(lvrCrntusrid));
								}	
								lvrEvDsiptblvoobj.setIvrBnEVENTSTATUS(1);
								
								//lvrEvDsiptblvoobj.getIvrBnEVENT_ID().setIvrBnEVENT_ID(locEventid);
								if (locEventid > 0) {
									lvrEvntvoobj = new EventTblVO();
									lvrEvntvoobj.setIvrBnEVENT_ID(locEventid);
									lvrEvDsiptblvoobj.setIvrBnEVENT_ID(lvrEvntvoobj);
								} else {
									lvrEvDsiptblvoobj.setIvrBnEVENT_ID(null);
								}
								
								locUsobj=new UserMasterTblVo();
								locUsobj.setUserId(Integer.parseInt(locshrusrid));
								lvrEvDsiptblvoobj.setIvrBnUAMMSTtbvoobj(locUsobj);
						 
								if (locGrpmstvoobj != null) {
									lvrEvDsiptblvoobj.setIvrBnGROUPMSTvoobj(locGrpmstvoobj);
								} else {
									lvrEvDsiptblvoobj.setIvrBnGROUPMSTvoobj(null);
								}
								
								loceventDaoObj.toInsertEventDispTbl(lvrEvDsiptblvoobj);												 
								usrid = Integer.parseInt(lvrCrntusrid);
								sharid = Integer.parseInt(locshrusrid);											
								// docHibernateUtilService.insertNotificationTblByValue(sharid, desc,usrid,1, locEventid);
						
								// Outlook Calendar Invite --- Start
								locUsobj =null;
								locUsobj = lvrUamdaosrv.getregistertable(Integer.parseInt(locshrusrid));						 
								if (locUsobj.getEmailId()!=null && !locUsobj.getEmailId().equalsIgnoreCase("null") && !locUsobj.getEmailId().equalsIgnoreCase("")){
									String locDesc=lvrEvntdesc;
									if (lvrEvntVdpath!=null && !lvrEvntVdpath.equalsIgnoreCase("null") && !lvrEvntVdpath.equalsIgnoreCase("")) {
										locDesc += "Video : "+lvrEvntVdpath;
									}
									String pCategeory = "Event Invite";
									String lvrLocation = lvrEvntlocation;
									Date evntStdate= null;
									Date evntEnddate= null;
									if (lvrEvntstartdate!=null && !lvrEvntstartdate.equalsIgnoreCase("") && !lvrEvntstartdate.equalsIgnoreCase("null")) {
										
										if(lvrEvntSttime!=null && !lvrEvntSttime.equalsIgnoreCase("") && !lvrEvntSttime.equalsIgnoreCase("null")) {
											evntStdate = locSmftclinetfrmt.parse(lvrEvntstartdate +" "+ lvrEvntSttime);								
										}else {
											evntStdate = locSmftclinetfrmt.parse(lvrEvntstartdate);	
										}
																	
									} else {										
										 evntStdate = new Date();
										 Calendar cal = Calendar.getInstance();
										 cal.setTime(evntStdate);
						                 cal.add(Calendar.MINUTE, 10);
						                 evntStdate = cal.getTime();
									}							
									if(lvrEvntenddate!=null && !lvrEvntenddate.equalsIgnoreCase("") && !lvrEvntenddate.equalsIgnoreCase("null")) {
								
										if (lvrEvntEndtime!=null && !lvrEvntEndtime.equalsIgnoreCase("") && !lvrEvntEndtime.equalsIgnoreCase("null")) {
											evntEnddate = locSmftclinetfrmt.parse(lvrEvntenddate + " " + lvrEvntEndtime);
										} else {
											evntEnddate = locSmftclinetfrmt.parse(lvrEvntenddate);
									}
								
									} else {
										evntEnddate = new Date();
										Calendar cal = Calendar.getInstance();
										cal.setTime(evntStdate);
										cal.add(Calendar.DAY_OF_MONTH, 1);
										cal.add(Calendar.MINUTE, 10);
										evntEnddate = cal.getTime();
									}
							    String lcStdate = lvrCommdaosrobj.getDateobjtoFomatDateStr(evntStdate, "dd-MM-yyyy hh:mm a");
							    String lcEnddate = lvrCommdaosrobj.getDateobjtoFomatDateStr(evntEnddate, "dd-MM-yyyy hh:mm a");
							    System.out.println("----------------[Start]");
							    System.out.println("Step 1 : Calendar Invite Start ");
							    System.out.println("User Id : "+locUsobj.getUserId());
							    System.out.println("Email : "+locUsobj.getEmailId());								
								System.out.println("Event Start Date : "+lcStdate);
								System.out.println("Event End Date : "+lcEnddate);
								//setOutlookCallicsRemainder(String to,String stdate, String enddate, String usesubject,String optionsubject,String pCategeory, String bdyy, String pSummary, String alrDesc, String locat)
							    OutCalendar.setOutlookCallicsRemainder(locUsobj.getEmailId(), lcStdate, lcEnddate, lvrEvnttit, lvrEvnttit, pCategeory, locDesc, lvrEvntShrtdesc, lvrEvntShrtdesc, lvrLocation);
							    System.out.println("Step 2 : Calendar Invite End ");
							    System.out.println("------------------[End]");
								}
								// Outlook Calendar Invite --- End

							}
						} catch (Exception ee) {
							Commonutility.toWriteConsole("Exception : "+ee);
							log.logMessage("Exception found in Event insert via share to Member id : "+locshrusrid+", Group id : "+logrpcode +", Exception : "+ee, "error", Eventutility.class);
						} finally {
							lvrEvDsiptblvoobj = null;
							locUsobj = null;
						}
					}
				} else {
					Commonutility.toWriteConsole("LINE : 6 else ");
				}
				
				 if (shrusrflg) {// Audit trial use only Share user update
					 if(Commonutility.toCheckisNumeric(lvrCrntusrid)){
						 AuditTrial.toWriteAudit(prAuditMsg, pAuditCode, Integer.parseInt(lvrCrntusrid));
					 }else{
						 AuditTrial.toWriteAudit(prAuditMsg, pAuditCode, 1);
					 }				 
				 }				
				 jry=null;//user json array clear						
				 log.logMessage("Event share [invite] table insert will end.", "info", Eventutility.class);
				 locFtrnStr="success!_!"+locEventid;
				 int tblflgg=0;
				 if(Commonutility.checkempty(rb.getString("notification.reflg.event"))){
						 tblflgg=Commonutility.stringToInteger(rb.getString("notification.reflg.event"));
				} else {
					tblflgg = 1;
				}
				Query locQryrst = null;
				try {
					String locSlqry = "from UserMasterTblVo where userId = "+Integer.parseInt(lvrCrntusrid);
					locQryrst = locSession.createQuery(locSlqry);					
					locUsrMstrVOobj = (UserMasterTblVo)locQryrst.uniqueResult();
					String cmplttosocietyid = null;
					if (locUsrMstrVOobj.getSocietyId() != null) {
						cmplttosocietyid = Commonutility.toCheckNullEmpty(locUsrMstrVOobj.getSocietyId().getSocietyId());
						String grpcode = Commonutility.toCheckNullEmpty(locUsrMstrVOobj.getGroupCode().getGroupCode());	
						//String locSlqry1 = "from UserMasterTblVo where societyId = "+Integer.parseInt(cmplttosocietyid)+" and statusFlag = 1 and  groupCode= "+grpcode+"";
						String locSlqry1 = "from UserMasterTblVo where societyId = "+Integer.parseInt(cmplttosocietyid)+" and statusFlag = 1 and userId <> "+Integer.parseInt(lvrCrntusrid) +" and groupCode = "+grpcode+"";
						locObjSocietyRestlst=locSession.createQuery(locSlqry1).list().iterator();
						while(locObjSocietyRestlst.hasNext()) {						
							locUsrMstrVOobj = (UserMasterTblVo) locObjSocietyRestlst.next();
							//Commonutility.toWriteConsole("lvrCrntusrid : "+lvrCrntusrid + " === "+locUsrMstrVOobj.getUserId());
							String usridforsoctid=Commonutility.toCheckNullEmpty(locUsrMstrVOobj.getUserId());
							docHibernateUtilService.insertNotificationTblByValue(Integer.parseInt(usridforsoctid), rb.getString("notification.event.raise"),Integer.parseInt(lvrCrntusrid),tblflgg, locEventid);//cmplttoid(committee),desc,cmpltfrmid,tblflag(0-cmplttable),tblrowid)
						}
					}
				} catch (Exception ee) {
					Commonutility.toWriteConsole("Exception found in Eventutility.class block notification insert: "+ ee);
				} finally {
					locQryrst = null;
				}
			} else {
				locFtrnStr="error!_!"+locEventid;
			}
			 return locFtrnStr;
			 		
		} catch(Exception e) {
			System.out.println("Exception found in Eventutility.toInsrtevent : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", Eventutility.class);
			locEventid=0;
			locFtrnStr="error!_!"+locEventid;
			return locFtrnStr;
		}finally{
			if(locSession!=null){locSession.flush();locSession.clear();locSession.close();locSession=null;}
			locFtrnStr=null;lvrCrntusrid=null; lvrCrntgrpid=null;
			lvrEvnttit=null;lvrEvntShrtdesc=null; lvrEvntdesc=null;lvrEvntfilename=null;lvrEvntfiledata=null;lvrEvntVdpath=null;
			lvrEvntstartdate=null;lvrEvntenddate=null; lvrEvntSttime=null; lvrEvntEndtime=null; lvrEvntSts=null;
			locEvntvoObj=null;locCommutillObj =null;loceventDaoObj = null;
			locEventid=0;locGrpmstvoobj=null;
			desc=null;	
			usrid = 0;
			sharid = 0; docHibernateUtilService = null; lvrEvntvoobj = null;
		}
	}
	
	public static String toUpdateEvent(JSONObject pDataJson,String pGrpName, String pAuditMsg, String pAuditCode, String pWebImagpath, String pMobImgpath){
		String locFtrnStr=null;
		String lvrCrntusrid=null, lvrCrntgrpid=null, lvrEventid=null,lvrEvnttitfundatid =null,lvrEvntTemplateid = null;
		String lvrEvnttit=null,lvrEvntShrtdesc=null, lvrEvntdesc=null,lvrEvntfilename=null,lvrEvntfiledata=null,lvrEvntVdpath=null,eventtype=null,eventrsvp=null;
		String lvrEvntstartdate=null,lvrEvntenddate=null, lvrEvntSttime=null, lvrEvntEndtime=null, lvrEvntlocation = null, lvrEvntSts=null,lvrEntlatlong = null,lvrFacilityid=null;
		Log log=null;
		String locUpdQry=null, lvrEvntfilesrchpth = null;
		EventDao loceventDaoObj=null;
		boolean lvrEventUpdaSts=false;
		
		Session locSession=null;
		String locSlqry=null;
		Query locQryrst=null;
		//GroupMasterTblVo locGrpMstrVOobj=null;
		Common lvrCommdaosrobj = null;
		GroupMasterTblVo locGrpmstvoobj=null;
		ResourceBundle rb = null;
		try{
			rb = ResourceBundle.getBundle("applicationResources");
			log=new Log();
			lvrCommdaosrobj = new CommonDao();
			log.logMessage("Step 2 : Event Update Block.", "info", Eventutility.class);
			System.out.println("Step 2 : Event Update Block.");
			lvrEventid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrgrpid");
			lvrEvnttit = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventtitle");
			lvrEvntShrtdesc = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "shortdesc");
			lvrEvntdesc = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventdesc");
			lvrEvntfilename = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventfilename");
			//lvrEvntfiledata = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventfiledata");
			lvrEvntfilesrchpth = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventfilesrchpath");
			lvrEvntVdpath = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventvideopath");
			lvrEvntstartdate = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventstartdate");
			lvrEvntenddate = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventenddate");
			lvrEvntSttime = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventstarttime");
			lvrEvntEndtime = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventendtime");
			lvrEvntlocation = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventlocation");
			lvrEntlatlong = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventlatlong");
			lvrEvnttitfundatid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventtitlefunctionid");
			lvrEvntTemplateid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventfuntemlateid");
			lvrFacilityid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventfacilityid");
			eventtype  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventtype");
			eventrsvp = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventrsvp");
			System.out.println("----------eventrsvp----------update---"+eventrsvp);
			locUpdQry ="update EventTblVO set ivrBnEVENT_TITLE = '"+lvrEvnttit+"', ivrBnSHORT_DESC = '"+lvrEvntShrtdesc+"', ivrBnEVENT_DESC ='"+lvrEvntdesc+"', ivrBnEVENT_LOCATION = '"+lvrEvntlocation+"', ivrBnLAT_LONG = '"+lvrEntlatlong+"', ";
			if(!Commonutility.toCheckisNumeric(lvrCrntusrid)){
				locUpdQry +="ivrBnEVENT_CRT_USR_ID ="+null+", ";
				locUpdQry +="ivrBnENTRY_BY ="+null+", ";				
			}else{
				locUpdQry +="ivrBnEVENT_CRT_USR_ID ="+Integer.parseInt(lvrCrntusrid)+", ";
				locUpdQry +="ivrBnENTRY_BY ="+Integer.parseInt(lvrCrntusrid)+", ";					
			}
			/*if(!Commonutility.toCheckisNumeric(eventtype)){
				locUpdQry +="ivrBnEVENTT_TYPE ="+null+", ";
			}else{
				locUpdQry +="ivrBnEVENTT_TYPE ="+Integer.parseInt(eventtype)+", ";
			}*/
			if(!Commonutility.toCheckisNumeric(lvrCrntgrpid)){
				locUpdQry +="ivrBnEVENT_CRT_GROUP_ID ="+null+", ";
			}else{
				locUpdQry +="ivrBnEVENT_CRT_GROUP_ID ="+Integer.parseInt(lvrCrntgrpid)+", ";				
			}
			
			if(lvrEvntfilename!=null && !lvrEvntfilename.equalsIgnoreCase("null")&& !lvrEvntfilename.equalsIgnoreCase("")){
				lvrEvntfilename = lvrEvntfilename.replaceAll(" ", "_");
				locUpdQry +="ivrBnEVENT_FILE_NAME ='"+lvrEvntfilename+"', ";
			}
			
			
			if(!Commonutility.toCheckisNumeric(lvrEvnttitfundatid)){
				locUpdQry +="ivrBnFUNCTION_ID ="+null+", ";		
				locUpdQry +="ivrBnEVENTT_TYPE ="+null+", ";	
			} else {		
				
				locUpdQry +="ivrBnFUNCTION_ID ="+Integer.parseInt(lvrEvnttitfundatid)+", ";	
				int eventypeflg = (Integer) lvrCommdaosrobj.getuniqueColumnVal("FunctionMasterTblVO", "ivrBnobjEVENT_TYPE", "functionId", lvrEvnttitfundatid);
				locUpdQry +="ivrBnEVENTT_TYPE ="+eventtype+", ";	
			}
									
			if(!Commonutility.toCheckisNumeric(eventrsvp)){
				locUpdQry +="ivrBnISRSVP ="+null+", ";	
			} else{
				locUpdQry +="ivrBnISRSVP ="+Integer.parseInt(eventrsvp)+", ";	
			}
			
			if(!Commonutility.toCheckisNumeric(lvrEvntTemplateid)){//lvrEvntTemplateid				
				locUpdQry +="functionTemplateId ="+null+", ";	
			} else{				
				locUpdQry +="functionTemplateId ="+Integer.parseInt(lvrEvntTemplateid)+", ";
			}if(!Commonutility.toCheckisNumeric(lvrFacilityid)){//facility id				
				locUpdQry +="faciltyTemplateId ="+null+", ";	
			} else{		
				System.out.println("lvrFacilityid --------------- "+lvrFacilityid);
				locUpdQry +="faciltyTemplateId ="+Integer.parseInt(lvrFacilityid)+", ";
			}
			
			
			locUpdQry +="ivrBnVIDEO_PATH ='"+lvrEvntVdpath+"', ivrBnSTARTDATE='"+lvrEvntstartdate+"', ivrBnENDDATE='"+lvrEvntenddate+"', ivrBnSTARTTIME='"+lvrEvntSttime+"', ivrBnENDTIME='"+lvrEvntEndtime+"' where ivrBnEVENT_ID ="+Integer.parseInt(lvrEventid)+"";
			log.logMessage("Step 3 : Event Updqry : "+locUpdQry, "info", Eventutility.class);
			//--------Event Update Start-------------
			loceventDaoObj=new EventDaoservice();
			lvrEventUpdaSts=loceventDaoObj.toUpdateEvent(locUpdQry);
			System.out.println("Event Update Status [lvrEventUpdaSts] : "+lvrEventUpdaSts);
			//--------Event Update End-------------
			 if(Commonutility.toCheckNullEmpty(lvrEventid)!=null && !Commonutility.toCheckNullEmpty(lvrEventid).equalsIgnoreCase("null")&& !Commonutility.toCheckNullEmpty(lvrEventid).equalsIgnoreCase("")){
				 try{
					 log.logMessage("Step 4 : Event Image Write will start.[Update] ", "info", Eventutility.class);
					 //image write into folder
					 if((lvrEvntfilesrchpth!=null && !lvrEvntfilesrchpth.equalsIgnoreCase("null") && !lvrEvntfilesrchpth.equalsIgnoreCase("")) && (lvrEvntfilename!=null && !lvrEvntfilename.equalsIgnoreCase("") && !lvrEvntfilename.equalsIgnoreCase("null"))){						 						
						//image write into folder
						 String delrs = Commonutility.todelete("", pWebImagpath+lvrEventid+"/");
						 String delrs_mob= Commonutility.todelete("", pMobImgpath+lvrEventid+"/");					  
						 Commonutility.toWriteImageMobWebNewUtill(Integer.parseInt(lvrEventid), lvrEvntfilename,lvrEvntfilesrchpth,pWebImagpath,pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, Eventutility.class);
					/*	 
					String delrs= Commonutility.todelete("", pWebImagpath+lvrEventid+"/");					 
						 byte imgbytee[]= new byte[1024];
						 imgbytee=Commonutility.toDecodeB64StrtoByary(lvrEvntfiledata);
						 String wrtsts=Commonutility.toByteArraytoWriteFiles(imgbytee, pWebImagpath+lvrEventid+"/", lvrEvntfilename);

					String delrs_mob= Commonutility.todelete("", pMobImgpath+lvrEventid+"/");
						//mobile - small image
						String limgSourcePath=pWebImagpath+lvrEventid+"/"+lvrEvntfilename;			
			 		 	String limgDisPath = pMobImgpath+lvrEventid+"/";
			 		 	String limgName = FilenameUtils.getBaseName(lvrEvntfilename);
			 		 	String limageFomat = FilenameUtils.getExtension(lvrEvntfilename);		 	    			 	    	 
			 	    	String limageFor = "all";
			 	    	int lneedWidth = Commonutility.stringToInteger(rb.getString("thump.img.width"));
			        	int lneedHeight = Commonutility.stringToInteger(rb.getString("thump.img.height"));		
			        	ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile				 				 				
					*/
					 }
					 log.logMessage("Step 4 : Event Image Write will end.[Update] ", "info", Eventutility.class);
					} catch(Exception imger){
						 System.out.println("Exception in  image write on Event update : "+imger);
						 log.logMessage("Exception in Image write on Event Update", "info", LaborUtility.class);
					}finally{
						 
					} 
			 }
			 
			 //Feed Edit
			 Query eventQrybj = null;EventTblVO updatedeventObj = null;FeedDAO feadhbm = null;
			 FeedsTblVO feedObj = null;CommonMobiDao commonServ = null;UserMasterTblVo userMst = null;
			  try {
				  	ResourceBundle rbApidetail = ResourceBundle.getBundle("apiDetails");
					updatedeventObj=new EventTblVO();
					String eventQry = "from EventTblVO where ivrBnEVENT_ID = "+Integer.parseInt(lvrEventid);
					eventQrybj = locSession.createQuery(eventQry);
					updatedeventObj = (EventTblVO)eventQrybj.uniqueResult();
					FeedsTblVO getfeedData = new FeedsTblVO();
					feadhbm = new FeedDAOService();
					userMst = new UserMasterTblVo();
					userMst = commonServ.getProfileDetails(Integer.parseInt(lvrCrntusrid));
					int feedTypFlg = 0;
					if (eventtype.equalsIgnoreCase("1")) {  //personal event
						feedTypFlg = 9;
					} else if (eventtype.equalsIgnoreCase("2")) {  //society event
						feedTypFlg = 8;
					} else if (eventtype.equalsIgnoreCase("3")) {  //committee meeting
						feedTypFlg = 12;
					}
					getfeedData=feadhbm.getFeedDetailsByEventId(Integer.parseInt(lvrEventid),feedTypFlg);
					if (getfeedData!=null) {
					feedObj = new FeedsTblVO();
					feedObj.setFeedId(getfeedData.getFeedId());
					feedObj.setUsrId(userMst);
					feedObj.setFeedType(feedTypFlg);
					feedObj.setFeedTypeId(Integer.parseInt(lvrEventid));
					feedObj.setFeedTitle(updatedeventObj.getIvrBnEVENT_TITLE());
					String tmpcont="";
					String eventdesc="";
					if(updatedeventObj.getFunctionTemplateId()!=null && updatedeventObj.getFunctionTemplateId().getTemplateText()!=null){
						tmpcont+=updatedeventObj.getFunctionTemplateId().getTemplateText();
					}
					if(updatedeventObj.getIvrBnEVENT_DESC()!=null){
						eventdesc=updatedeventObj.getIvrBnEVENT_DESC();
					}
					if(tmpcont!=null && tmpcont.length()>0){
						tmpcont+="<BR>"+eventdesc;
					}else{
						tmpcont+=eventdesc;
					}
					if(updatedeventObj.getIvrBnFUNCTION_ID()!=null && updatedeventObj.getIvrBnFUNCTION_ID().getFunctionName()!=null){
					feedObj.setFeedCategory(updatedeventObj.getIvrBnFUNCTION_ID().getFunctionName());
					}
					feedObj.setFeedDesc(tmpcont);
					feedObj.setPostBy(Integer.parseInt(lvrCrntusrid));
					if(userMst.getFirstName()!=null){
					feedObj.setOriginatorName(Commonutility.stringToStringempty(userMst.getFirstName()));
					}
					feedObj.setSocietyId(userMst.getSocietyId());
					if(userMst.getCityId()!=null){
						feedObj.setFeedLocation(userMst.getCityId().getCityName());
					}
					CommonMobiDao comondaohbm=new CommonMobiDaoService();
					PrivacyInfoTblVO privacydata = new PrivacyInfoTblVO();
					privacydata = comondaohbm.getDefaultPrivacyFlg(Integer.parseInt(lvrCrntusrid));
					if (privacydata != null && Commonutility.checkIntnull(privacydata.getPrivacyFlg())) {
						feedObj.setFeedPrivacyFlg(privacydata.getPrivacyFlg());	
					} else {
						feedObj.setFeedPrivacyFlg(Commonutility.stringToInteger(rbApidetail.getString("default.privacy.category")));	
					}
					feedObj.setOriginatorId(Integer.parseInt(lvrCrntusrid));
					feedObj.setEntryBy(Integer.parseInt(lvrCrntusrid));																												
					feedObj.setFeedStatus(1);
					feedObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
					feedObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
					feedObj.setIsMyfeed(1);
					feedObj.setIsShared(0);
					System.out.println("updating feed table");
					feadhbm.feedEdit(feedObj, "", null, null, null, null);
					boolean additionUpdate = false;
					/*Additional data insert*/
					JSONObject jsonPublishObj = new JSONObject(); 
					ComplaintsDao complains=new ComplaintsDaoServices();
					JsonpackDao jsonPack = new JsonpackDaoService();
					jsonPublishObj = jsonPack.eventTableJasonpackValues(Integer.parseInt(lvrEventid));
					if (jsonPublishObj != null) {
						additionUpdate = complains.additionalFeedUpdate(jsonPublishObj,getfeedData.getFeedId());
					}
					}
			} catch (Exception feedEx) {
				System.out.println("Exception in event Feed Edit : " + feedEx);
				log.logMessage("Exception in event Feed Edit", "info",Eventutility.class);
			} finally {
				eventQrybj = null;updatedeventObj = null;feadhbm = null;feedObj = null;commonServ = null;userMst = null;
			}
			
			 EventDispTblVO lvrEvDsiptblvoobj=null;
			 JSONArray jry = (JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "shareusridJary");// Event disp user id insert into
			 //String usrGrpcode = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "shareusrgrpid");//  share user group id
			 String logrpcode = null;
			 
			 // set group code			 
			 locGrpmstvoobj=new GroupMasterTblVo();						 
			 logrpcode = lvrCommdaosrobj.getGroupcodeexistornew(pGrpName);
			 if(logrpcode!= null && !logrpcode.equalsIgnoreCase("error")){
				 if(Commonutility.toCheckisNumeric(logrpcode)){
					 locGrpmstvoobj.setGroupCode(Integer.parseInt(logrpcode));	
				 }				
			 }else{	
				 locGrpmstvoobj = null;
			 }
			 
			 
			 log.logMessage("Step 5 : Event share [invite] table insert will start.[Update]", "info", Eventutility.class);
			 boolean shrusrflg=false;
			 UserMasterTblVo locUsobj=null;
			
			 locSession=HibernateUtil.getSession();
			 String lvrDlQry=null;
			 for (int i = 0; i < jry.length(); i++) {
				 String locshrusrid=(String)jry.getString(i);
				 try{
				 if(Commonutility.toCheckisNumeric(locshrusrid)){
					 lvrDlQry ="delete EventDispTblVO where ivrBnEVENT_ID.ivrBnEVENT_ID="+Integer.parseInt(lvrEventid)+" and ivrBnUAMMSTtbvoobj.userId = "+Integer.parseInt(locshrusrid)+"";
					 loceventDaoObj.toDeleteEventDispTbl(lvrDlQry);
					 shrusrflg=true;
					 lvrEvDsiptblvoobj= new EventDispTblVO();
					 if(Commonutility.toCheckisNumeric(lvrCrntusrid)){
						 lvrEvDsiptblvoobj.setIvrBnENTRY_BY(null);
					 }else{
						 lvrEvDsiptblvoobj.setIvrBnENTRY_BY(Integer.parseInt(lvrCrntusrid));
					 }	
					 lvrEvDsiptblvoobj.setIvrBnEVENTSTATUS(1);
					 //lvrEvDsiptblvoobj.getIvrBnEVENT_ID().setIvrBnEVENT_ID(Integer.parseInt(lvrEventid));
					 if (Integer.parseInt(lvrEventid) > 0) {
						 	EventTblVO lvrEvntvoobj = new EventTblVO();
							lvrEvntvoobj.setIvrBnEVENT_ID(Integer.parseInt(lvrEventid));
							lvrEvDsiptblvoobj.setIvrBnEVENT_ID(lvrEvntvoobj);
						} else {
							lvrEvDsiptblvoobj.setIvrBnEVENT_ID(null);
						}
					 
					 locUsobj=new UserMasterTblVo();
					 locUsobj.setUserId(Integer.parseInt(locshrusrid));
					 lvrEvDsiptblvoobj.setIvrBnUAMMSTtbvoobj(locUsobj);					 					 				
					 
					 if(locGrpmstvoobj!=null){
						 lvrEvDsiptblvoobj.setIvrBnGROUPMSTvoobj(locGrpmstvoobj);
					 }else{
						 lvrEvDsiptblvoobj.setIvrBnGROUPMSTvoobj(null);
					 }
					 					
					 loceventDaoObj.toInsertEventDispTbl(lvrEvDsiptblvoobj);
					 lvrEvDsiptblvoobj=null;locUsobj=null;
				 }
				}catch(Exception ee){
					System.out.println("Exception found in Event Update via share to Member id : "+locshrusrid+", Group id : "+logrpcode+" Exception : "+ee);
					log.logMessage("Exception found in Event Update via share to Member id : "+locshrusrid+", Group id : "+logrpcode+" Exception : "+ee, "error", Eventutility.class);
				}finally{
					lvrEvDsiptblvoobj=null;locUsobj=null;
				}				 
			 }
			 if(shrusrflg){// Audit trial use only Share user update
				 if(Commonutility.toCheckisNumeric(lvrCrntusrid)){
					 AuditTrial.toWriteAudit(pAuditMsg, pAuditCode, Integer.parseInt(lvrCrntusrid));
				 }else{
					 AuditTrial.toWriteAudit(pAuditMsg, pAuditCode, 1);
				 }				 
			 }
			 log.logMessage("Step 6 : Event share [invite] table insert will end[Update].", "info", Eventutility.class);
			 jry=null;//user json array clear
			 if(lvrEventUpdaSts){
				 return "success";
			 }else{
				 return "error";
			 }	
		}catch(Exception e){
			System.out.println("Exception found in Eventutility.toUpdateEvent : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", Eventutility.class);		
			locFtrnStr="error";
			return locFtrnStr;			
		}finally{
			locGrpmstvoobj=null;
			if(locSession!=null){locSession.close();locSession=null;}
			lvrCrntusrid=null; lvrCrntgrpid=null; lvrEventid=null;
			lvrEvnttit=null;lvrEvntShrtdesc=null; lvrEvntdesc=null;lvrEvntfilename=null;lvrEvntfiledata=null;lvrEvntVdpath=null;
			lvrEvntstartdate=null;lvrEvntenddate=null; lvrEvntSttime=null; lvrEvntEndtime=null; lvrEvntSts=null;
			log=null;
			locUpdQry=null;
			loceventDaoObj=null;
			lvrEventUpdaSts=false;
			locSlqry=null;locQryrst=null;rb=null;	
		}
	}
	
    public static String toDeactivateEvent(JSONObject pDataJson){
		String locFtrnStr=null;
		String lvrEventid=null;
		boolean lvrEventUpdaSts=false;
		Log log=null;
		String locUpdQry=null;
		EventDao loceventDaoObj=null;
		try{
			log=new Log();
			loceventDaoObj = new EventDaoservice();
			log.logMessage("Step 2 : Event DeActive block enter.", "info", Eventutility.class);
			lvrEventid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventid");			
			locUpdQry="update EventTblVO set ivrBnEVENTSTATUS=0 where ivrBnEVENT_ID ="+Integer.parseInt(lvrEventid);
			log.logMessage("Step 3 : Event DeActive qry : "+locUpdQry, "info", Eventutility.class);
			lvrEventUpdaSts=loceventDaoObj.toDeactiveEvent(locUpdQry);
			if(lvrEventUpdaSts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			log.logMessage("Step 4 : Event DeActive block End.", "info", Eventutility.class);
			return locFtrnStr;
		}catch(Exception e){
			log.logMessage("Step -1 : Exception found in toDeactivateEvent() : "+e, "error", Eventutility.class);
			locFtrnStr="error";
			return locFtrnStr;
		}finally{
			lvrEventid=null;
			log=null;locUpdQry=null;loceventDaoObj=null;
		}
	}
	
	public static String toDeleteEvent(JSONObject pDataJson){
		String locFtrnStr = null;
		String lvrEventid = null;
		boolean lvrEventUpdaSts = false;
		boolean lvrEventDispUpdaSts = false;
		Log log=null;
		String locDldQry=null;
		String locDldispQry=null;
		EventDao loceventDaoObj=null;
		try{
			log=new Log();
			loceventDaoObj = new EventDaoservice();
			log.logMessage("Step 2 : Event Delete block enter.", "info", Eventutility.class);
			lvrEventid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventid");			
			locDldispQry= "delete EventDispTblVO where ivrBnEVENT_ID.ivrBnEVENT_ID="+Integer.parseInt(lvrEventid)+"";
			log.logMessage("Step 3 : Event Display Tbl Delete qry : "+locDldispQry, "info", Eventutility.class);
			lvrEventDispUpdaSts=loceventDaoObj.toDeleteEventDispTbl(locDldispQry);
			locDldQry="delete EventTblVO where ivrBnEVENT_ID ="+Integer.parseInt(lvrEventid);
			log.logMessage("Step 4 : Event Delete qry : "+locDldQry, "info", Eventutility.class);
			lvrEventUpdaSts=loceventDaoObj.toDeleteEvent(locDldQry);
			
			if(lvrEventUpdaSts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			log.logMessage("Step 5 : Event Delete block End.", "info", Eventutility.class);
			return locFtrnStr;
		}catch(Exception e){
			return locFtrnStr;
		}finally{
			lvrEventid=null;
			log=null;locDldQry=null;loceventDaoObj=null;
		}
	}
	
   public static JSONObject toseletEventsingledata(JSONObject pDataJson, String pWebImagpath, String pMobImgpath){ 
    String lvrEventid = null;
	String lvrSlqry = null;
	Query lvrQrybj = null;
	Log log = null;
	Date lvrEntrydate=null;
	EventTblVO locEventtblobj = null;
	JSONObject locRtndatajson = null;
	Session locHbsession = null;
	Common locCommonObj = null;
	try {
		log = new Log();
		locHbsession = HibernateUtil.getSession();
		locCommonObj = new CommonDao();
		System.out.println("Step 1 : Event detail block enter");
		log.logMessage("Step 1 : Select Event detail block enter", "info", Eventutility.class);
		locRtndatajson =  new JSONObject();
		lvrEventid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventid");
		lvrSlqry = "from EventTblVO where ivrBnEVENT_ID = "+Integer.parseInt(lvrEventid);
		lvrQrybj = locHbsession.createQuery(lvrSlqry);
		locEventtblobj = (EventTblVO)lvrQrybj.uniqueResult();
		System.out.println("Step 2 : Select Event detail query executed.");
		locRtndatajson.put("eventid",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnEVENT_ID()));
		locRtndatajson.put("evetitle",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnEVENT_TITLE()));
		locRtndatajson.put("eveshrtdesc",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnSHORT_DESC()));
		locRtndatajson.put("evedesc",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnEVENT_DESC()));
		locRtndatajson.put("evefilename",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnEVENT_FILE_NAME()));
		locRtndatajson.put("evevideopath",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnVIDEO_PATH()));
		locRtndatajson.put("evestartdate",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnSTARTDATE()));
		locRtndatajson.put("eveenddate",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnENDDATE()));
		locRtndatajson.put("evestarttime",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnSTARTTIME()));
		locRtndatajson.put("eveendtime",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnENDTIME()));
		locRtndatajson.put("evelocation",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnEVENT_LOCATION()));
		locRtndatajson.put("evestatus",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnEVENTSTATUS()));
		locRtndatajson.put("eveentryby",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnENTRY_BY()));
		locRtndatajson.put("eventlatlong",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnLAT_LONG()));
		locRtndatajson.put("eventtype",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnEVENTT_TYPE()));
		locRtndatajson.put("eventrsvp",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnISRSVP()));
		System.out.println("----------eventrsvp----------update---"+(locEventtblobj.getIvrBnISRSVP()));
		
		if(locEventtblobj.getIvrBnFUNCTION_ID()!=null){
			locRtndatajson.put("eventfunctionid",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnFUNCTION_ID().getFunctionId()));
			locRtndatajson.put("eventfunctiontext",Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnFUNCTION_ID().getFunctionName()));
		} else {
			locRtndatajson.put("eventfunctionid","");
			locRtndatajson.put("eventfunctiontext","");
		}
		
		if(locEventtblobj.getFunctionTemplateId()!=null){
			locRtndatajson.put("eventfuntemplateid",Commonutility.toCheckNullEmpty(locEventtblobj.getFunctionTemplateId().getFunctionTempId()));
			locRtndatajson.put("eventfuntemplatetext",Commonutility.toCheckNullEmpty(locEventtblobj.getFunctionTemplateId().getTemplateText()));
		} else {
			locRtndatajson.put("eventfuntemplateid","");
			locRtndatajson.put("eventfuntemplatetext","");
		}
		if(locEventtblobj.getFaciltyTemplateId()!=null){
			locRtndatajson.put("eventfacilitytemplateid",Commonutility.toCheckNullEmpty(locEventtblobj.getFaciltyTemplateId().getFacilityId()));
			locRtndatajson.put("eventfacilitytemplatetext",Commonutility.toCheckNullEmpty(locEventtblobj.getFaciltyTemplateId().getFacilityName()+" "+locEventtblobj.getFaciltyTemplateId().getPlace()));
		} else {
			locRtndatajson.put("eventfacilitytemplateid","");
			locRtndatajson.put("eventfacilitytemplatetext","");
		}
		
		
		locRtndatajson.put("evewebimgfldr",pWebImagpath+Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnEVENT_ID())+"/");
		locRtndatajson.put("evemobileimgfldr",pMobImgpath+Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnEVENT_ID())+"/");
		lvrEntrydate=locEventtblobj.getIvrBnENTRY_DATETIME();
		locRtndatajson.put("eveentrydate", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "yyyy-MM-dd HH:mm:ss"));
		System.out.println("Step 3 : Event details are put into json.");
		System.out.println("Step 4 : Event Block end.");
		return locRtndatajson;
	}catch(Exception e) {
		try{
			System.out.println("Step -1 : Select Event detail Exception found in Eventutility.toseletEventsingle : "+e);
			log.logMessage("Exception : "+e, "error", Eventutility.class);
			locRtndatajson=new JSONObject();
			locRtndatajson.put("catch", "Error");
			}catch(Exception ee){}
			return locRtndatajson;
	}finally {
			if(locHbsession!=null){locHbsession.close();locHbsession=null;}
			 lvrEventid = null; lvrSlqry = null; lvrQrybj = null;
			 log = null; lvrEntrydate=null; locEventtblobj = null; locRtndatajson = null;
			 
	}
  }

    public static String toShareEvent(JSONObject pDataJson,String pGrpName){
	    String locFtrnStr = null;
		String lvrEventid = null,lvrCrntusrid = null;		
		Log log = null;
		String lvrDlQry = null;		
		EventDao loceventDaoObj = null;
		// get event details
		String lvrEvntslqry = null;
		Query locEventqryrst = null;
		EventTblVO locEventtblobj = null;
		Common lvrCommdaosrobj = null;	
		String lvrEvntdesc = null, lvrEvntVdpath = null, lvrEvntstartdate = null, lvrEvntSttime = null, lvrEvntenddate = null, lvrEvntEndtime = null;
		String lvrEvnttit = null, lvrEvntShrtdesc = null, lvrEventlocation = null,desc="You Have Received A New Event Notification";
		int usrid = 0;
		int sharid = 0;
		DocumentUtilitiHibernateDao docHibernateUtilService = null;
		
		// get Group details
		Session locSession = null;
		
		// insert into event disp table
		UserMasterTblVo locUsobj=null;
		GroupMasterTblVo locGrpmstvoobj=null;		
		EventDispTblVO lvrEvDsiptblvoobj=null;
		uamDao	lvrUamdaosrv= null;
		try {
			docHibernateUtilService = new DocumentUtilitiHibernateDao();
			log = new Log();boolean shrusrflg=false;
			locSession=HibernateUtil.getSession();
			loceventDaoObj = new EventDaoservice();
			lvrCommdaosrobj = new CommonDao();
			lvrUamdaosrv = new uamDaoServices();
			
			log.logMessage("Step 2 : Event Share block enter.", "info", Eventutility.class);
			lvrEventid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "eventid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrloginid");			
			
			JSONArray jry=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "shareusridJary");// Event disp user id insert into
			//String usrGrpcode=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "shareusrgrpid");//  share user group id									 
			 // set group code	
			 String logrpcode = null;
			 locGrpmstvoobj=new GroupMasterTblVo();						 
			 logrpcode = lvrCommdaosrobj.getGroupcodeexistornew(pGrpName);
			 if(logrpcode!= null && !logrpcode.equalsIgnoreCase("error")){
				 if(Commonutility.toCheckisNumeric(logrpcode)){
					 locGrpmstvoobj.setGroupCode(Integer.parseInt(logrpcode));	
				 }				
			 }else{	
				 locGrpmstvoobj = null;
			 }			
			log.logMessage("Step 3 : Event share [invite] table insert will start.[toShareEvent]", "info", Eventutility.class);
									
			// Get Event Details
			lvrEvntslqry = "from EventTblVO where ivrBnEVENT_ID = "+Integer.parseInt(lvrEventid);
			locEventqryrst = locSession.createQuery(lvrEvntslqry);
			locEventtblobj = (EventTblVO) locEventqryrst.uniqueResult();
			lvrEvntdesc = Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnEVENT_DESC());
			lvrEvntVdpath = Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnVIDEO_PATH());
			lvrEvntstartdate = Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnSTARTDATE());
			lvrEvntSttime = Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnSTARTTIME());
			lvrEvntenddate = Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnENDDATE());
			lvrEvntEndtime = Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnENDTIME());
			lvrEvnttit = Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnEVENT_TITLE());
			lvrEvntShrtdesc = Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnSHORT_DESC());
			lvrEventlocation = Commonutility.toCheckNullEmpty(locEventtblobj.getIvrBnEVENT_LOCATION());
			for (int i = 0; i < jry.length(); i++) {
				 String locshrusrid=(String)jry.getString(i);
				 try{
				 if(Commonutility.toCheckisNumeric(locshrusrid)){
					 lvrDlQry ="delete EventDispTblVO where ivrBnEVENT_ID.ivrBnEVENT_ID="+Integer.parseInt(lvrEventid)+" and ivrBnUAMMSTtbvoobj.userId = "+Integer.parseInt(locshrusrid)+"";
					 loceventDaoObj.toDeleteEventDispTbl(lvrDlQry);
					 shrusrflg=true;
					 lvrEvDsiptblvoobj= new EventDispTblVO();
					 if(Commonutility.toCheckisNumeric(lvrCrntusrid)){
						 lvrEvDsiptblvoobj.setIvrBnENTRY_BY(null);
					 }else{
						 lvrEvDsiptblvoobj.setIvrBnENTRY_BY(Integer.parseInt(lvrCrntusrid));
					 }	
					 lvrEvDsiptblvoobj.setIvrBnEVENTSTATUS(1);
					 //lvrEvDsiptblvoobj.getIvrBnEVENT_ID().setIvrBnEVENT_ID(Integer.parseInt(lvrEventid));
					 if (Integer.parseInt(lvrEventid) > 0) {
						 	EventTblVO lvrEvntvoobj = new EventTblVO();
							lvrEvntvoobj.setIvrBnEVENT_ID(Integer.parseInt(lvrEventid));
							lvrEvDsiptblvoobj.setIvrBnEVENT_ID(lvrEvntvoobj);
						} else {
							lvrEvDsiptblvoobj.setIvrBnEVENT_ID(null);
						}
					 locUsobj=new UserMasterTblVo();
					 locUsobj.setUserId(Integer.parseInt(locshrusrid));
					 lvrEvDsiptblvoobj.setIvrBnUAMMSTtbvoobj(locUsobj);
					 
					 if(locGrpmstvoobj!=null){
						 lvrEvDsiptblvoobj.setIvrBnGROUPMSTvoobj(locGrpmstvoobj);
					 }else{
						 lvrEvDsiptblvoobj.setIvrBnGROUPMSTvoobj(null);
					 }					 									
					// lvrEvDsiptblvoobj.setIvrBnGROUPMSTvoobj(locGrpmstvoobj);	
					 loceventDaoObj.toInsertEventDispTbl(lvrEvDsiptblvoobj);
							
					 usrid=Integer.parseInt(lvrCrntusrid);
					 sharid=Integer.parseInt(locshrusrid);											
					 docHibernateUtilService.insertNotificationTblByValue(sharid, desc,usrid,1, Integer.parseInt(lvrEventid));//flag for event = 1,  event id
					 
					// Outlook Calendar Invite --- Start
					 locUsobj = lvrUamdaosrv.getregistertable(Integer.parseInt(locshrusrid));						 
					 if (locUsobj.getEmailId()!=null && !locUsobj.getEmailId().equalsIgnoreCase("null") && !locUsobj.getEmailId().equalsIgnoreCase("")){
						String locDesc=lvrEvntdesc;
						if (lvrEvntVdpath!=null && !lvrEvntVdpath.equalsIgnoreCase("null") && !lvrEvntVdpath.equalsIgnoreCase("")) {
							locDesc += "Video : "+lvrEvntVdpath;
						}
						String pCategeory="Event Invite";
						String lvrLocation = lvrEventlocation;
						Date evntStdate= null;
						Date evntEnddate= null;
						if (lvrEvntstartdate!=null && !lvrEvntstartdate.equalsIgnoreCase("") && !lvrEvntstartdate.equalsIgnoreCase("null")) {
							
							if(lvrEvntSttime!=null && !lvrEvntSttime.equalsIgnoreCase("") && !lvrEvntSttime.equalsIgnoreCase("null")) {
								Date locstDt= Commonutility.toString2UtilDate(lvrEvntenddate + " " + lvrEvntEndtime,isvrClientfrmt);
								if(locstDt==null){
									locstDt= Commonutility.toString2UtilDate(lvrEvntenddate + " " + lvrEvntEndtime,isvrClientfrmt_sub);
								}								
								evntStdate = locstDt;
								//evntStdate = locSmftclinetfrmt.parse(lvrEvntstartdate +" "+ lvrEvntSttime);								
							}else {
								evntStdate = locSmftclinetfrmt.parse(lvrEvntstartdate);	
							}														
						}else {
							 evntStdate = new Date();
							 Calendar cal = Calendar.getInstance();
							 cal.setTime(evntStdate);
			                 cal.add(Calendar.MINUTE, 10);
			                 evntStdate = cal.getTime();
						}						
						if(lvrEvntenddate!=null && !lvrEvntenddate.equalsIgnoreCase("") && !lvrEvntenddate.equalsIgnoreCase("null")) {							
							if (lvrEvntEndtime!=null && !lvrEvntEndtime.equalsIgnoreCase("") && !lvrEvntEndtime.equalsIgnoreCase("null")) {
								Date loctestEnddt= Commonutility.toString2UtilDate(lvrEvntenddate + " " + lvrEvntEndtime,"dd-MM-yyyy hh:mm a");
								if(loctestEnddt==null){
									loctestEnddt= Commonutility.toString2UtilDate(lvrEvntenddate + " " + lvrEvntEndtime,"dd-MM-yyyy hh:mma");
								}
								//evntEnddate = locSmftclinetfrmt.parse(lvrEvntenddate + " " + lvrEvntEndtime);
								evntEnddate = loctestEnddt;
							}else {
								evntEnddate = locSmftclinetfrmt.parse(lvrEvntenddate);
							}
							
						}else{
							 evntEnddate = new Date();
							 Calendar cal = Calendar.getInstance();
							 cal.setTime(evntStdate);
							 cal.add(Calendar.DAY_OF_MONTH, 1);
			                 cal.add(Calendar.MINUTE, 10);
			                 evntEnddate = cal.getTime();
						}
						    String lcStdate = lvrCommdaosrobj.getDateobjtoFomatDateStr(evntStdate, "dd-MM-yyyy hh:mm a");
						    String lcEnddate = lvrCommdaosrobj.getDateobjtoFomatDateStr(evntEnddate, "dd-MM-yyyy hh:mm a");
						    System.out.println("----------------[Start]");
						    System.out.println("Step 1 : Calendar Invite Start ");
						    System.out.println("User Id : "+locUsobj.getUserId());
						    System.out.println("Email : "+locUsobj.getEmailId());								
							System.out.println("Event Start Date : "+lcStdate);
							System.out.println("Event End Date : "+lcEnddate);
										//setOutlookCallicsRemainder(String to,String stdate, String enddate, String usesubject,String optionsubject,String pCategeory, String bdyy, String pSummary, String alrDesc, String locat)
						    OutCalendar.setOutlookCallicsRemainder(locUsobj.getEmailId(), lcStdate, lcEnddate, lvrEvnttit, lvrEvnttit, pCategeory, locDesc, lvrEvntShrtdesc, lvrEvntShrtdesc, lvrLocation);
						    System.out.println("Step 2 : Calendar Invite End ");
						    System.out.println("------------------[End]");
					 }
		// Outlook Calendar Invite --- End					 					 					 			
					 lvrEvDsiptblvoobj=null;locUsobj=null;
					 lvrDlQry=null;
				 }
				}catch(Exception ee){
					log.logMessage("Exception found in Event Update via share to Member id : "+locshrusrid+", Group id : "+logrpcode +" Exception : "+ee, "error", Eventutility.class);
				}finally{
					lvrEvDsiptblvoobj=null;locUsobj=null; lvrDlQry=null;
				}				 
			 }
			 log.logMessage("Step 4 : Event share [invite] table insert will end[toShareEvent].", "info", Eventutility.class);
			 jry=null;//user json array clear
			if(shrusrflg){// Audit trial use only Share user update
				locFtrnStr = "success";		 
			 }else{
				 locFtrnStr = "error";
			 }
			return locFtrnStr;
		}catch(Exception e){
			System.out.println("Exception found in Eventutility.toShareEvent : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", Eventutility.class);		
			locFtrnStr="error";
			return locFtrnStr;
		}finally{
			if(locSession!=null){locSession.close();locSession=null;}
			lvrEventid = null;
			log = null;lvrDlQry=null;loceventDaoObj = null;
			locFtrnStr = null; lvrCrntusrid = null;	
			locGrpmstvoobj=null;
			lvrEvntslqry = null;locEventqryrst = null;locEventtblobj = null;lvrUamdaosrv=null;lvrCommdaosrobj = null;desc=null;	
			usrid=0;
			sharid=0;docHibernateUtilService = null;
		}
  }
}
