package com.pack.complaints;

import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.complaintVO.ComplaintattchTblVO;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.complaintsvo.persistence.ComplaintsDao;
import com.pack.complaintsvo.persistence.ComplaintsDaoservice;
import com.pack.labor.LaborUtility;
import com.pack.laborvo.LaborDetailsTblVO;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.persistence.DocumentUtilitiHibernateDao;

public class Complaintstutility {
	/*
	 * to insert into Complaint table.
	 */
	static ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	
	public static String toInsertComplaint(JSONObject prDatajson,String pGrpName, String prAuditMsg, String pAuditCode, String pWebImagpath, String pMobImgpath,Log log,String iswebmobilefla){
		String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
		String lvrCmplttit=null, lvrLabid=null, lvrCmpltdesc=null,lvrCmpltVdpath=null, lvrCmplttoflg = null;
		//String lvrCmpltfilename1=null,lvrCmpltfiledata1=null,lvrCmpltfilename2=null,lvrCmpltfiledata2=null,lvrCmpltfilename3=null,lvrCmpltfiledata3=null;	
		String lvrLabGrpid=null, lvrCmpltSts=null,lvrCmpltshrtdesc=null,lvrCmpltotheremail=null;
		ComplaintsTblVO locCmpltvoObj = null;
		CommonUtils locCommutillObj = null;
		ComplaintsDao locmpltDaoObj = null;
		uamDao lvrUamdaosrobj = null;
		int locComplaintid = 0;
		DocumentUtilitiHibernateDao docHibernateUtilService = null;
		Session locSession = null;
		String locSlqry = null;
		Query locQryrst = null;
		UserMasterTblVo locUsrMstrVOobj = null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		Iterator locObjSocietyRestlst = null;
		try {
			lvrUamdaosrobj = new uamDaoServices();
			docHibernateUtilService = new DocumentUtilitiHibernateDao();
			locSession = HibernateUtil.getSession();
			locCommutillObj = new CommonUtilsDao();
			log.logMessage("Step 2 : Complaint Insert Block.", "info", Complaintstutility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrCmpltotheremail = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "cmplintemail");
			lvrCmpltshrtdesc=(String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "cmplintshrtdesc");
			lvrLabid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "cmplintousrid");
			lvrLabGrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "cmplintogrpid");
			lvrCmplttit = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "cmplinttitle");			
			lvrCmpltdesc = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "cmplintdesc");						
			lvrCmpltVdpath = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "cmplintvideopath");		
			lvrCmplttoflg = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "complainttoflag");
			lvrCmpltSts="1";			
			locCmpltvoObj=new ComplaintsTblVO();	
			locCmpltvoObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			locCmpltvoObj.setStatus(Integer.parseInt(lvrCmpltSts));
			UserMasterTblVo userObj = new UserMasterTblVo();
			if(!Commonutility.toCheckisNumeric(lvrCrntusrid)){
				locCmpltvoObj.setUsrRegTblByFromUsrId(null);
				locCmpltvoObj.setEntryBy(null);
			}else{
				userObj.setUserId(Integer.parseInt(lvrCrntusrid));
				locCmpltvoObj.setUsrRegTblByFromUsrId(userObj);
				locCmpltvoObj.setEntryBy(Integer.parseInt(lvrCrntusrid));
			}	
			GroupMasterTblVo grpObj = new GroupMasterTblVo();
			
			if(!Commonutility.toCheckisNumeric(lvrCrntgrpid)){
				locCmpltvoObj.setGroupMstTblByFromGroupId(null);
			}else{
				grpObj.setGroupCode(Integer.parseInt(lvrCrntgrpid));
				locCmpltvoObj.setGroupMstTblByFromGroupId(grpObj);
			}
			if(!Commonutility.toCheckisNumeric(lvrLabid)){
				locCmpltvoObj.setUsrRegTblByToUsrId(null);
			}else{
				locCmpltvoObj.setUsrRegTblByToUsrId(Integer.parseInt(lvrLabid));
			}
			if(!Commonutility.toCheckisNumeric(lvrLabGrpid)){
				locCmpltvoObj.setGroupMstTblByToGroupId(null);
			}else{
				locCmpltvoObj.setGroupMstTblByToGroupId(Integer.parseInt(lvrLabGrpid));
			}
			
			locCmpltvoObj.setComplaintsOthersEmail(lvrCmpltotheremail);
			locCmpltvoObj.setComplaintsTitle(lvrCmplttit);
			locCmpltvoObj.setShrtDesc(lvrCmpltshrtdesc);
			locCmpltvoObj.setComplaintsDesc(lvrCmpltdesc);	
			
			if(!Commonutility.toCheckisNumeric(lvrCmplttoflg)){
				locCmpltvoObj.setCompliantsToFlag(0);
			} else {
				locCmpltvoObj.setCompliantsToFlag(Integer.parseInt(lvrCmplttoflg));
			}			
			/*locCmpltvoObj.setComplaintsFileName1(lvrCmpltfilename1);
			locCmpltvoObj.setComplaintsFileName2(lvrCmpltfilename2);
			locCmpltvoObj.setComplaintsFileName3(lvrCmpltfilename3);*/						
			locCmpltvoObj.setVideoPath(lvrCmpltVdpath);			
			//----------- Complaint Insert Start-----------			
			log.logMessage("Step 3 : Complaint Detail insert will start.", "info", Complaintstutility.class);
			locmpltDaoObj = new ComplaintsDaoservice();
			locComplaintid = locmpltDaoObj.toInsertComplaint(locCmpltvoObj);
			System.out.println(locComplaintid+": id complaint");
			log.logMessage("Step 4 : Complaint Detail insert End Complaint Id : "+locComplaintid, "info", Complaintstutility.class);
			// -----------Complaint Insert end------------		
			if (locComplaintid > 0) {
				String lvrSocyname = "", lvrFname="", lvrMob = "";
				if (lvrCrntusrid!=null && Commonutility.checkempty(lvrCrntusrid)) {
					UserMasterTblVo userInfo = null;
					userInfo = lvrUamdaosrobj.editUser(Integer.parseInt(lvrCrntusrid));
					
					if (userInfo.getFirstName()!=null) {
						lvrFname = userInfo.getFirstName();
					}
					if (userInfo.getLastName()!=null) {
						lvrFname += " "+ userInfo.getLastName();
					}
					lvrMob = userInfo.getMobileNo();
					if (userInfo.getSocietyId()!=null) {
						lvrSocyname = userInfo.getSocietyId().getSocietyName();
					}
				}
				//Email Templates															
				CommonUtilsDao common=new CommonUtilsDao();	
				String locDesc=lvrCmpltdesc;
				if (lvrCmpltVdpath!=null && !lvrCmpltVdpath.equalsIgnoreCase("null") && !lvrCmpltVdpath.equalsIgnoreCase("")) {
					locDesc += "Video : "+lvrCmpltVdpath;
				}
				EmailEngineServices emailservice = new EmailEngineDaoServices();
				EmailTemplateTblVo emailTemplate = null;
				try {
		            String emqry = "FROM EmailTemplateTblVo where tempName ='External Complaint' and status ='1'";
		            emailTemplate = emailservice.emailTemplateData(emqry);
		            String emailMessage = emailTemplate.getTempContent();
		            Commonutility.toWriteConsole("emqry RPK : "+emqry);
		            Commonutility.toWriteConsole("emailMessage : "+emailMessage);
		           
		            
		            if (Commonutility.checkempty(lvrFname)) {
		            	emailMessage = emailMessage.replace("[FIRSTNAME]", lvrFname);
		            } else {
		            	emailMessage = emailMessage.replace("[FIRSTNAME]", "Resident");
		            }
		            if (Commonutility.checkempty(lvrMob)) {
		            	emailMessage = emailMessage.replace("[MOBILENO]", lvrMob);
		            } else {
		            	emailMessage = emailMessage.replace("[MOBILENO]", "");
		            }
		            if (Commonutility.checkempty(lvrSocyname)) {
		            	emailMessage = emailMessage.replace("[SOCIETY NAME]", lvrSocyname);
		            } else {
		            	emailMessage = emailMessage.replace("[SOCIETY NAME]", "SOCYTEA");
		            }
		            
		            Commonutility.toWriteConsole("emailMessage Final : "+emailMessage);
		            emqry = common.templateParser(emailMessage, 1, "", "");
		            Commonutility.toWriteConsole("emqry emqry Final: "+emqry);
		            String[] qrySplit = emqry.split("!_!");
		            String qry = qrySplit[0] + " FROM ComplaintsTblVO as cmplt where cmplt.complaintsId ='"+locComplaintid+"'";						           
		            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
		            Commonutility.toWriteConsole(emailMessage);
		            //emailMessage =  emailMessage.replace("[ACTIVATIONKEY]", userData.getSocietyId().getActivationKey());
		           
		            
		            
		            emailTemplate.setTempContent(emailMessage);
		            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
		            
		            EmailInsertFuntion emailfun = new EmailInsertFuntion();
		            emailfun.test2(lvrCmpltotheremail, emailTemplate.getSubject(), emailMessage);
		          } catch (Exception ex) {
		            System.out.println("Excption found in Complaintemail  : " + ex);
		            log.logMessage("Exception in Complaintemail admin flow emailFlow----> " + ex, "error",siLaborMerchantSendEmail.class);
		            
		          }	
									
				 locFtrnStr="success!_!"+locComplaintid;
				
				 int tblflgg = 0;
				 if (Commonutility.checkempty(rb.getString("notification.reflg.complaints"))) {
					tblflgg = Commonutility.stringToInteger(rb.getString("notification.reflg.complaints"));
				 } else {
					tblflgg = 0;
				 }
				/*// not need for external complaints
				 // Insert Notification
				 log.logMessage("Step 6 : Complaint Insert Notification will Start.", "info", Complaintstutility.class);				
				 locSlqry = "from UserMasterTblVo where userId = "+Integer.parseInt(lvrCrntusrid);
				 locQryrst = locSession.createQuery(locSlqry);
				 locUsrMstrVOobj = (UserMasterTblVo)locQryrst.uniqueResult();
				 String cmplttosocietyid=Commonutility.toCheckNullEmpty(locUsrMstrVOobj.getSocietyId().getSocietyId());
				 String grpcode=Commonutility.toCheckNullEmpty(locUsrMstrVOobj.getGroupCode().getGroupCode());						
				 String locSlqry1 = "from UserMasterTblVo where societyId = "+Integer.parseInt(cmplttosocietyid)+" and statusFlag =1  and groupCode= "+grpcode+"";				
			     locObjSocietyRestlst=locSession.createQuery(locSlqry1).list().iterator();
				 while(locObjSocietyRestlst.hasNext()) {						
				    locUsrMstrVOobj=(UserMasterTblVo)locObjSocietyRestlst.next();
					String usridforsoctid=Commonutility.toCheckNullEmpty(locUsrMstrVOobj.getUserId());
					docHibernateUtilService.insertNotificationTblByValue(Integer.parseInt(usridforsoctid), rb.getString("notification.cmplt.raise"),Integer.parseInt(lvrCrntusrid),tblflgg, locComplaintid);//cmplttoid(committee),desc,cmpltfrmid,tblflag(0-cmplttable),tblrowid)
				 }		*/		 				
			}else{
				locFtrnStr="error!_!"+locComplaintid;
			}
			 return locFtrnStr;
			 		
		}catch(Exception e){
			System.out.println("Exception found in Complaintutility.toInsrtevent : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", Complaintstutility.class);
			locComplaintid=0;
			locFtrnStr="error!_!"+locComplaintid;
			return locFtrnStr;
		}finally{
			locFtrnStr=null;lvrCrntusrid=null; lvrCrntgrpid=null;
			lvrCmplttit=null;lvrLabid=null; lvrCmpltdesc=null;lvrCmpltVdpath=null;
			lvrLabGrpid=null;lvrCmpltSts=null;
			locCmpltvoObj=null;locCommutillObj =null;locmpltDaoObj = null;
			locComplaintid=0;
		}
	}
	
	public static String toUpdateComplaint(JSONObject pDataJson,String pGrpName, String pAuditMsg, String pAuditCode, String pWebImagpath, String pMobImgpath){
		String locFtrnStr=null;
		String lvrCrntusrid=null, lvrCrntgrpid=null, lvrComplaintid=null;
		String lvrCmplttit=null,lvrLabid=null, lvrCmpltdesc=null,lvrCmpltfilename1=null,lvrCmpltfiledata1=null,lvrCmpltfilename2=null,lvrCmpltfiledata2=null,lvrCmpltfilename3=null,lvrCmpltfiledata3=null,lvrCmpltVdpath=null;
		String lvrLabGrpid=null,lvrCmpltenddate=null, lvrCmpltSttime=null, lvrCmpltEndtime=null, lvrCmpltSts=null;
		Log log=null;
		String locUpdQry=null;
		ComplaintsDao locmpltDaoObj=null;
		boolean lvrComplaintUpdaSts=false;
		
		Session locSession=null;
		String locSlqry=null;
		Query locQryrst=null;
		GroupMasterTblVo locGrpMstrVOobj=null;
		try{
			log=new Log();
			log.logMessage("Step 2 : Complaint Update Block.", "info", Complaintstutility.class);
			System.out.println("Step 2 : Complaint Update Block.");
			lvrComplaintid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "complaintid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrgrpid");
			lvrCmplttit = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmplinttitle");
			lvrLabid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmplintousrid");
			lvrCmpltdesc = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmplintdesc");
			lvrCmpltfilename1 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmplintfilename1");
			lvrCmpltfiledata1 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmplintfiledata1");
			lvrCmpltfilename2 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmplintfilename2");
			lvrCmpltfiledata2 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmplintfiledata2");
			lvrCmpltfilename3 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmplintfilename3");
			lvrCmpltfiledata3 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmplintfiledata3");
			lvrCmpltVdpath = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmplintvideopath");
			lvrLabGrpid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmplintogrpid");
			
			locUpdQry ="update ComplaintsTblVO set complaintsTitle = '"+lvrCmplttit+"', usrRegTblByToUsrId = '"+lvrLabid+"', complaintsDesc ='"+lvrCmpltdesc+"',";
			if(!Commonutility.toCheckisNumeric(lvrCrntusrid)){
				locUpdQry +="usrRegTblByFromUsrId ="+null+", ";
				locUpdQry +="entryBy ="+null+", ";				
			}else{
				locUpdQry +="usrRegTblByFromUsrId ="+Integer.parseInt(lvrCrntusrid)+", ";
				locUpdQry +="entryBy ="+Integer.parseInt(lvrCrntusrid)+", ";					
			}			
			if(!Commonutility.toCheckisNumeric(lvrCrntgrpid)){
				locUpdQry +="groupMstTblByFromGroupId ="+null+", ";
			}else{
				locUpdQry +="groupMstTblByFromGroupId ="+Integer.parseInt(lvrCrntgrpid)+", ";				
			}
			
			/*if(lvrCmpltfilename1!=null && !lvrCmpltfilename1.equalsIgnoreCase("null")&& !lvrCmpltfilename1.equalsIgnoreCase("")){
				locUpdQry +="complaintsFileName1 ='"+lvrCmpltfilename1+"', ";
			}
			if(lvrCmpltfilename2!=null && !lvrCmpltfilename2.equalsIgnoreCase("null")&& !lvrCmpltfilename2.equalsIgnoreCase("")){
				locUpdQry +="complaintsFileName2 ='"+lvrCmpltfilename2+"', ";
			}
			if(lvrCmpltfilename3!=null && !lvrCmpltfilename3.equalsIgnoreCase("null")&& !lvrCmpltfilename3.equalsIgnoreCase("")){
				locUpdQry +="complaintsFileName3 ='"+lvrCmpltfilename3+"', ";
			} */
			locUpdQry +="videoPath ='"+lvrCmpltVdpath+"' where complaintsId ="+Integer.parseInt(lvrComplaintid)+"";
			log.logMessage("Step 3 : Complaint Updqry : "+locUpdQry, "info", Complaintstutility.class);
			//--------Complaint Update Start-------------
			locmpltDaoObj=new ComplaintsDaoservice();
			lvrComplaintUpdaSts=locmpltDaoObj.toUpdateComplaint(locUpdQry);
			//--------Complaint Update End-------------
			 /*if(Commonutility.toCheckNullEmpty(lvrComplaintid)!=null && !Commonutility.toCheckNullEmpty(lvrComplaintid).equalsIgnoreCase("null")&& !Commonutility.toCheckNullEmpty(lvrComplaintid).equalsIgnoreCase("")){
				 try{
					 log.logMessage("Step 4 : Complaint Image Write will start.[Update] ", "info", Complaintstutility.class);
					
					 //image write into folder
					 if((lvrCmpltfiledata1!=null && !lvrCmpltfiledata1.equalsIgnoreCase("null") && !lvrCmpltfiledata1.equalsIgnoreCase("")) && (lvrCmpltfilename1!=null && !lvrCmpltfilename1.equalsIgnoreCase("") && !lvrCmpltfilename1.equalsIgnoreCase("null"))){						
				        Commonutility.toWriteImgWebAndMob(Integer.parseInt(lvrComplaintid), lvrCmpltfilename1, lvrCmpltfiledata1, pWebImagpath, pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, Commonutility.class);
					 }else{
						 
					 }
					 
					 //2 nd image
					 if((lvrCmpltfiledata2!=null && !lvrCmpltfiledata2.equalsIgnoreCase("null") && !lvrCmpltfiledata2.equalsIgnoreCase("")) && (lvrCmpltfilename2!=null && !lvrCmpltfilename2.equalsIgnoreCase("") && !lvrCmpltfilename2.equalsIgnoreCase("null")))
					 {						
				        Commonutility.toWriteImgWebAndMob(Integer.parseInt(lvrComplaintid), lvrCmpltfilename2, lvrCmpltfiledata2, pWebImagpath, pMobImgpath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, Commonutility.class);
					 }else{
						
					 }
					 
					//3 nd image					 					
					 if((lvrCmpltfiledata3!=null && !lvrCmpltfiledata3.equalsIgnoreCase("null") && !lvrCmpltfiledata3.equalsIgnoreCase("")) && (lvrCmpltfilename3!=null && !lvrCmpltfilename3.equalsIgnoreCase("") && !lvrCmpltfilename3.equalsIgnoreCase("null"))){						
				        Commonutility.toWriteImgWebAndMob(Integer.parseInt(lvrComplaintid), lvrCmpltfilename3, lvrCmpltfiledata3, pWebImagpath, pMobImgpath, rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, Commonutility.class);
					 
					 }else{
						 
					 }
					 
					 log.logMessage("Step 4 : Complaint Image Write will end.[Update] ", "info", Complaintstutility.class);
					} catch(Exception imger){
						 System.out.println("Exception in  image write on Complaint update : "+imger);
						 log.logMessage("Exception in Image write on Complaint Update", "info", LaborUtility.class);
					}finally{
						 
					} 
			 }*/
			 if(lvrComplaintUpdaSts){
				 System.out.println("lvrComplaintUpdaSts:::wwww  "+lvrComplaintUpdaSts);
				 return "success";
			 }else{
				 return "error";
			 }	
		}catch(Exception e){
			System.out.println("Exception found in Complaintutility.toUpdateComplaint : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", Complaintstutility.class);		
			locFtrnStr="error";
			return locFtrnStr;			
		}finally{
			locSession.close();locSession=null;
			lvrCrntusrid=null; lvrCrntgrpid=null; lvrComplaintid=null;
			lvrCmplttit=null;lvrLabid=null; lvrCmpltdesc=null;lvrCmpltfilename1=null;lvrCmpltfiledata1=null;lvrCmpltfilename2=null;lvrCmpltfiledata2=null;lvrCmpltfilename3=null;lvrCmpltfiledata3=null;lvrCmpltVdpath=null;
			lvrLabGrpid=null;lvrCmpltenddate=null; lvrCmpltSttime=null; lvrCmpltEndtime=null; lvrCmpltSts=null;
			log=null;
			locUpdQry=null;
			locmpltDaoObj=null;
			lvrComplaintUpdaSts=false;
			locSlqry=null;locQryrst=null;
			locGrpMstrVOobj=null;
		}
	}
	
	public static String toDeactivateComplaint(JSONObject pDataJson){
		String locFtrnStr=null;
		String lvrComplaintid=null,lvrComplaintclosedid=null,cmplintclosereason=null,cmplintstatusflg=null;
		boolean lvrComplaintUpdaSts=false;
		Log log=null;
		String locUpdQry=null,locupdreasonQry=null;
		ComplaintsDao locmpltDaoObj=null;
		
		Session session = null;
		try{
			log=new Log();
			session = HibernateUtil.getSession();	
			//session = HibernateUtil.getSessionFactory().openSession();
			
			locmpltDaoObj = new ComplaintsDaoservice();
			log.logMessage("Step 2 : Complaint DeActive block enter.", "info", Complaintstutility.class);
			lvrComplaintid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "complaintid");	
			lvrComplaintclosedid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmplintclosedby");	
			cmplintclosereason  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "desc");	
			cmplintstatusflg  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "status");
			locUpdQry="update ComplaintsTblVO set status="+cmplintstatusflg+", complaintsCompleteBy ="+Integer.parseInt(lvrComplaintclosedid)+",closereason='"+cmplintclosereason+"' where complaintsId ="+Integer.parseInt(lvrComplaintid);
			log.logMessage("Step 3 : Complaint DeActive qry : "+locUpdQry, "info", Complaintstutility.class);
			lvrComplaintUpdaSts=locmpltDaoObj.toDeactiveComplaint(locUpdQry);
			
			if(lvrComplaintUpdaSts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			
			log.logMessage("Step 4 : Complaint DeActive block End.", "info", Complaintstutility.class);
			return locFtrnStr;
			
		}catch(Exception e){
			log.logMessage("Step -1 : Exception found in toDeactivateComplaint() : "+e, "error", Complaintstutility.class);
			locFtrnStr="error";
			return locFtrnStr;
		}finally{
			lvrComplaintid=null;lvrComplaintclosedid=null;
			log=null;locUpdQry=null;locmpltDaoObj=null;
		}
	}

	public static String toDeleteComplaint(JSONObject pDataJson){
		String locFtrnStr = null;
		String lvrComplaintid = null;
		boolean lvrComplaintUpdaSts = false;
		Log log=null;
		String locDldQry=null;
		ComplaintsDao locmpltDaoObj=null;
		try{
			log=new Log();
			locmpltDaoObj = new ComplaintsDaoservice();
			log.logMessage("Step 2 : Complaint Delete block enter.", "info", Complaintstutility.class);
			lvrComplaintid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "complaintid");	
			locDldQry="update ComplaintsTblVO set status=0 where complaintsId = "+Integer.parseInt(lvrComplaintid);
			//locDldQry="delete ComplaintsTblVO where complaintsId ="+Integer.parseInt(lvrComplaintid);
			log.logMessage("Step 4 : Complaint Delete qry : "+locDldQry, "info", Complaintstutility.class);
			lvrComplaintUpdaSts=locmpltDaoObj.toDeleteComplaint(locDldQry);
			
			if(lvrComplaintUpdaSts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			log.logMessage("Step 5 : Complaint Delete block End.", "info", Complaintstutility.class);
			return locFtrnStr;
		}catch(Exception e){
			return locFtrnStr;
		}finally{
			lvrComplaintid=null;
			log=null;locDldQry=null;locmpltDaoObj=null;
		}
	}
	
    public static JSONObject toseletComplaintsingle(JSONObject pDataJson){ 
    String lvrComplaintid = null, lvrCmpltstats = null;
	String lvrSlqry = null,lvrSlgrpcodeqry=null,lvrslqlabnameqry=null,loc_slQry_file=null;
	Query lvrQrybj = null,lvrQrygrpbj=null,lvrQrylabname=null;
	Log log = null;
	Date lvrEntrydate=null;
	Date lvrModifydate=null;
	ComplaintsTblVO locComplainttblobj = null;
	GroupMasterTblVo locGrptblobj = null;
	LaborProfileTblVO locLabnameObj=null;
	JSONObject locRtndatajson = null;
	Session locHbsession = null;
	Common locCommonObj = null;
	Iterator locObjfilelst_itr=null;
	JSONArray locLBRFILEJSONAryobj=null;
	ComplaintattchTblVO locfiledbtbl=null;
	JSONObject locLBRFILEOBJJSONAryobj=null;
	try {
		log = new Log();
		locHbsession = HibernateUtil.getSession();
		locCommonObj=new CommonDao();
		System.out.println("Step 1 : Complaint detail block enter");
		log.logMessage("Step 1 : Select Complaint detail block enter", "info", Complaintstutility.class);
		locRtndatajson =  new JSONObject();
		lvrComplaintid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "complaintid");
		lvrSlqry = "from ComplaintsTblVO where complaintsId = "+Integer.parseInt(lvrComplaintid);
		lvrQrybj = locHbsession.createQuery(lvrSlqry);
		locComplainttblobj = (ComplaintsTblVO)lvrQrybj.uniqueResult();
		System.out.println("Step 2 : Select Complaint detail query executed.");
		locRtndatajson.put("cmpltfrmusrid",Commonutility.toCheckNullEmpty(locComplainttblobj.getUsrRegTblByFromUsrId().getUserId()));
		locRtndatajson.put("cmpltfrmusrname",Commonutility.toCheckNullEmpty(locComplainttblobj.getUsrRegTblByFromUsrId().getFirstName())+Commonutility.toCheckNullEmpty(locComplainttblobj.getUsrRegTblByFromUsrId().getLastName()));
		locRtndatajson.put("cmpltfrmgrpid",Commonutility.toCheckNullEmpty(locComplainttblobj.getGroupMstTblByFromGroupId().getGroupCode()));
		locRtndatajson.put("cmpltfrmgrpname",Commonutility.toCheckNullEmpty(locComplainttblobj.getUsrRegTblByFromUsrId().getGroupCode().getGroupName()));
		//--Labor - or -- merchant--- id
		/*String grpcode=Commonutility.toCheckNullEmpty(locComplainttblobj.getGroupMstTblByToGroupId());
		String laborid=Commonutility.toCheckNullEmpty(locComplainttblobj.getUsrRegTblByToUsrId());
		if(grpcode!=null ||!grpcode.equalsIgnoreCase("Null") ||grpcode!="" ||!grpcode.equalsIgnoreCase(""))
		{
			System.out.println("true==");
			lvrSlgrpcodeqry = "from GroupMasterTblVo where groupCode = "+Integer.parseInt(grpcode);
			lvrQrygrpbj = locHbsession.createQuery(lvrSlgrpcodeqry);
			locGrptblobj = (GroupMasterTblVo)lvrQrygrpbj.uniqueResult();
			System.out.println("Step grupcodeget : Select Groupcode detail query executed.");
			System.out.println("group name   "+locGrptblobj.getGroupName());
			if(locGrptblobj.getGroupName().equalsIgnoreCase("Labor") ||locGrptblobj.getGroupName()=="Labor")
			{
				lvrslqlabnameqry="from LaborProfileTblVO where ivrBnLBR_ID = "+Integer.parseInt(laborid);
				lvrQrylabname = locHbsession.createQuery(lvrslqlabnameqry);
				locLabnameObj = (LaborProfileTblVO)lvrQrylabname.uniqueResult();
				locRtndatajson.put("cmpltgrpname",Commonutility.toCheckNullEmpty(locGrptblobj.getGroupName()));
				locRtndatajson.put("cmpltlaborname",Commonutility.toCheckNullEmpty(locLabnameObj.getIvrBnLBR_NAME()));
			}
		}
		else
		{
			System.out.println("false==+++");
			locRtndatajson.put("cmpltgrpname","");
			locRtndatajson.put("cmpltlaborname","");
		}
		*/
		
		if(locComplainttblobj.getCompliantsToFlag() == 0) {
			locRtndatajson.put("cmplttodest", "External [Out to Society]");
		} else if(locComplainttblobj.getCompliantsToFlag() == 1){
			locRtndatajson.put("cmplttodest", "Committee");
		} else if(locComplainttblobj.getCompliantsToFlag() == 2){
			locRtndatajson.put("cmplttodest", "Admin");
		} else {
			locRtndatajson.put("cmplttodest", "External [Out to Society]");
		}
		locRtndatajson.put("cmplttoflg", Commonutility.toCheckNullEmpty(locComplainttblobj.getCompliantsToFlag()));
		
		locRtndatajson.put("cmpltlaborid",Commonutility.toCheckNullEmpty(locComplainttblobj.getUsrRegTblByToUsrId()));
		locRtndatajson.put("cmpltlaborgrpid",Commonutility.toCheckNullEmpty(locComplainttblobj.getGroupMstTblByToGroupId()));
		locRtndatajson.put("cmplttitle",Commonutility.toCheckNullEmpty(locComplainttblobj.getComplaintsTitle()));
		locRtndatajson.put("cmpltdesc",Commonutility.toCheckNullEmpty(locComplainttblobj.getComplaintsDesc()));
		/*locRtndatajson.put("cmpltfilename1",Commonutility.toCheckNullEmpty(locComplainttblobj.getComplaintsFileName1()));
		locRtndatajson.put("cmpltfilename2",Commonutility.toCheckNullEmpty(locComplainttblobj.getComplaintsFileName2()));
		locRtndatajson.put("cmpltfilename3",Commonutility.toCheckNullEmpty(locComplainttblobj.getComplaintsFileName3()));*/
		locRtndatajson.put("cmpltvideopath",Commonutility.toCheckNullEmpty(locComplainttblobj.getVideoPath()));
		locRtndatajson.put("cmpltotheremail",Commonutility.toCheckNullEmpty(locComplainttblobj.getComplaintsOthersEmail()));
		locRtndatajson.put("cmpltstatus",Commonutility.toCheckNullEmpty(locComplainttblobj.getStatus()));
		locRtndatajson.put("cmpltentryby",Commonutility.toCheckNullEmpty(locComplainttblobj.getEntryBy()));
		locRtndatajson.put("cmpltid",Commonutility.toCheckNullEmpty(locComplainttblobj.getComplaintsId()));
		locRtndatajson.put("cmpltshortdesc",Commonutility.toCheckNullEmpty(locComplainttblobj.getShrtDesc()));
		lvrEntrydate=locComplainttblobj.getEntryDatetime();
		locRtndatajson.put("cmpltentrydate", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "dd-MM-yyyy HH:mm:ss"));
		lvrModifydate=locComplainttblobj.getModifyDatetime();
		locRtndatajson.put("cmpltmodifydate", locCommonObj.getDateobjtoFomatDateStr(lvrModifydate, "dd-MM-yyyy HH:mm:ss"));
		loc_slQry_file="from ComplaintattchTblVO where ivrBnCOMPLAINTS_ID="+Integer.parseInt(lvrComplaintid)+" ";
		locObjfilelst_itr=locHbsession.createQuery(loc_slQry_file).list().iterator();	
		System.out.println("Step 3 : Select ComplaintattchTblVO files detail query executed.");
		locLBRFILEJSONAryobj=new JSONArray();
		while (locObjfilelst_itr!=null &&  locObjfilelst_itr.hasNext() ) {
			locfiledbtbl=(ComplaintattchTblVO)locObjfilelst_itr.next();
			locLBRFILEOBJJSONAryobj=new JSONObject();
			if(locfiledbtbl.getIvrBnATTCH_ID()!=null){
				locLBRFILEOBJJSONAryobj.put("filesname", locfiledbtbl.getIvrBnATTACH_NAME());
				locLBRFILEOBJJSONAryobj.put("thumpimg", locfiledbtbl.getThumbImage());
			}	
			locLBRFILEJSONAryobj.put(locLBRFILEOBJJSONAryobj);
		}
		locRtndatajson.put("jArry_doc_files", locLBRFILEJSONAryobj);
		System.out.println("Step 3 : Complaint details are put into json.");
		System.out.println("Step 4 : Complaint Block end.");
		return locRtndatajson;
	}catch(Exception e) {
		try{
			System.out.println("Step -1 : Select Complaint detail Exception found in Complaintutility.toseletComplaintsingle : "+e);
			log.logMessage("Exception : "+e, "error", Complaintstutility.class);
			locRtndatajson=new JSONObject();
			locRtndatajson.put("catch", "Error");
			}catch(Exception ee){}
			return locRtndatajson;
	}finally {
			if(locHbsession!=null){locHbsession.close();locHbsession=null;}
			 lvrComplaintid = null; lvrCmpltstats = null; lvrSlqry = null; lvrQrybj = null;
			 log = null; lvrEntrydate=null; locComplainttblobj = null; locRtndatajson = null;loc_slQry_file=null;locLBRFILEOBJJSONAryobj=null;
			 
	}
  }
    
    public static String toShareCmplt(JSONObject pDataJson,String pGrpName){
	    String locFtrnStr = null;
		String lvrCmpltid = null,lvrCrntusrid = null;		
		Log log = null;
		String lvrDlQry = null;		
		ComplaintsDao loccmpltDaoObj = null;
		// get cmplt details
		String lvrCmpltslqry = null;
		Query locCmpltqryrst = null;
		ComplaintsTblVO locCmplttblobj = null;
		Common lvrCommdaosrobj = null;	
		String lvrCmpltdesc = null, lvrCmpltVdpath = null, lvrCmpltstartdate = null, lvrCmpltSttime = null, lvrCmpltenddate = null, lvrCmpltEndtime = null;
		String lvrCmplttit = null, lvrCmpltShrtdesc = null, lvrCmpltlocation = null;
		
		// get Group details
		Session locSession = null;
		String locSlqry = null;
		Query locQryrst = null;
		GroupMasterTblVo locGrpMstrVOobj = null;
		// insert into event disp table
		LaborDetailsTblVO locUsobj=null;
		GroupMasterTblVo locGrpmstvoobj=null;		
		ComplaintsTblVO lvrEvDsiptblvoobj=null;
		ComplaintsDao	lvrlbrdaosrv= null;
		try {
			log = new Log();boolean shrusrflg=false;
			locSession=HibernateUtil.getSession();
			loccmpltDaoObj = new ComplaintsDaoservice();
			lvrCommdaosrobj = new CommonDao();
			lvrlbrdaosrv = new ComplaintsDaoservice();
			
			log.logMessage("Step 2 : Cmplt Share block enter.", "info", Complaintstutility.class);
			lvrCmpltid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmpltid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrloginid");			
			
			JSONArray jry=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "sharelaboridJary");// Cmplt disp user id insert into
			String usrGrpcode=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "shareusrgrpid");//  share user group id
			log.logMessage("Step 3 : Cmplt share [invite] table insert will start.[toShareCmplt]", "info", Complaintstutility.class);
									
			// Get Cmplt Details
			lvrCmpltslqry = "from ComplaintsTblVO where complaintsId = "+Integer.parseInt(lvrCmpltid);
			locCmpltqryrst = locSession.createQuery(lvrCmpltslqry);
			locCmplttblobj = (ComplaintsTblVO) locCmpltqryrst.uniqueResult();
			lvrCmpltdesc = Commonutility.toCheckNullEmpty(locCmplttblobj.getComplaintsDesc());
			lvrCmpltVdpath = Commonutility.toCheckNullEmpty(locCmplttblobj.getVideoPath());
			lvrCmplttit = Commonutility.toCheckNullEmpty(locCmplttblobj.getComplaintsTitle());
			lvrCmpltShrtdesc = Commonutility.toCheckNullEmpty(locCmplttblobj.getShrtDesc());
			//lvrCmpltlocation = Commonutility.toCheckNullEmpty(locCmplttblobj.getIvrBnEVENT_LOCATION());
			CommonUtilsDao common=new CommonUtilsDao();	
			
			String lvrSocyname = "", lvrFname="", lvrMob = "";		
			if (locCmplttblobj.getUsrRegTblByFromUsrId()!=null) {												
				if (locCmplttblobj.getUsrRegTblByFromUsrId().getFirstName()!=null) {
					lvrFname = locCmplttblobj.getUsrRegTblByFromUsrId().getFirstName();
				}
				if (locCmplttblobj.getUsrRegTblByFromUsrId().getLastName()!=null) {
					lvrFname += " "+ locCmplttblobj.getUsrRegTblByFromUsrId().getLastName();
				}
				lvrMob = locCmplttblobj.getUsrRegTblByFromUsrId().getMobileNo();
				if (locCmplttblobj.getUsrRegTblByFromUsrId().getSocietyId()!=null) {
					lvrSocyname = locCmplttblobj.getUsrRegTblByFromUsrId().getSocietyId().getSocietyName();
				}
			}
			
			for (int i = 0; i < jry.length(); i++) {
				 String locshrusrid = (String) jry.getString(i);				 
				 try {
					 if(Commonutility.toCheckisNumeric(locshrusrid)){
						 shrusrflg=true;
						 locUsobj = lvrlbrdaosrv.getregistertable(Integer.parseInt(locshrusrid)); 
						 if (locUsobj.getIvrBnLBR_EMAIL()!=null && !locUsobj.getIvrBnLBR_EMAIL().equalsIgnoreCase("null") && !locUsobj.getIvrBnLBR_EMAIL().equalsIgnoreCase("")){							 
							 	String locDesc=lvrCmpltdesc;
								if (lvrCmpltVdpath!=null && !lvrCmpltVdpath.equalsIgnoreCase("null") && !lvrCmpltVdpath.equalsIgnoreCase("")) {
									locDesc += "Video : "+lvrCmpltVdpath;
								}
								EmailEngineServices emailservice = new EmailEngineDaoServices();
								EmailTemplateTblVo emailTemplate;															
								try {
						            String emqry = "FROM EmailTemplateTblVo where tempName ='Share Complaint' and status ='1'";
						            emailTemplate = emailservice.emailTemplateData(emqry);
						            String emailMessage = emailTemplate.getTempContent();
						           
						            if (Commonutility.checkempty(lvrFname)) {
						            	emailMessage = emailMessage.replace("[FIRSTNAME]", lvrFname);
						            } else {
						            	emailMessage = emailMessage.replace("[FIRSTNAME]", "Sir/Madam");
						            }
						            if (Commonutility.checkempty(lvrSocyname)) {
						            	emailMessage = emailMessage.replace("[SOCIETY NAME]", lvrSocyname);
						            } else {
						            	emailMessage = emailMessage.replace("[SOCIETY NAME]", "SOCYTEA");
						            }
						            if (Commonutility.checkempty(lvrMob)) {
						            	emailMessage = emailMessage.replace("[MOBILENO]", lvrMob);
						            } else {
						            	emailMessage = emailMessage.replace("[MOBILENO]", "-");
						            }
						            if (Commonutility.checkempty(locUsobj.getIvrBnLBR_NAME())) {
						            	emailMessage = emailMessage.replace("[LABOURNAME]", locUsobj.getIvrBnLBR_NAME());
						            } else {
						            	emailMessage = emailMessage.replace("[LABOURNAME]", "-");
						            }
						           emqry = common.templateParser(emailMessage, 1, "", "");
						            String[] qrySplit = emqry.split("!_!");
						            String qry = qrySplit[0] + " FROM ComplaintsTblVO as cmplt where cmplt.complaintsId='"+lvrCmpltid+"'";						           
						            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
						            //emailMessage =  emailMessage.replace("[ACTIVATIONKEY]", userData.getSocietyId().getActivationKey());
						            emailTemplate.setTempContent(emailMessage);
						            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
						            
						            EmailInsertFuntion emailfun = new EmailInsertFuntion();
						            emailfun.test2(locUsobj.getIvrBnLBR_EMAIL(), emailTemplate.getSubject(), emailMessage);
						          } catch (Exception ex) {
						            System.out.println("Excption found in Complaintemail  : " + ex);
						            log.logMessage("Exception in Complaintemail admin flow emailFlow----> " + ex, "error",siLaborMerchantSendEmail.class);
						            
						          }	
																						
								
						 }
					 }
				}catch(Exception ee){
					log.logMessage("Exception found in Cmplt Update via share to Member id : "+locshrusrid+", Group id : "+usrGrpcode +" Exception : "+ee, "error", Complaintstutility.class);
				}finally{
					lvrEvDsiptblvoobj=null;locGrpmstvoobj=null;locUsobj=null; lvrDlQry=null;
				}				 
			 }
			log.logMessage("Step 4 : Cmplt share [invite] table insert will end[toShareCmplt].", "info", Complaintstutility.class);
			 jry=null;//user json array clear
			if(shrusrflg){// Audit trial use only Share user update
				locFtrnStr = "success";		 
			 }else{
				 locFtrnStr = "error";
			 }
			return locFtrnStr;
		}catch(Exception e){
			System.out.println("Exception found in Complaintstutility.toShareCmplt : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", Complaintstutility.class);		
			locFtrnStr="error";
			return locFtrnStr;
		}finally{
			if(locSession!=null){locSession.close();locSession=null;}
			lvrCmpltid = null;
			log = null;lvrDlQry=null;loccmpltDaoObj = null;
			locFtrnStr = null; lvrCrntusrid = null;		
			locSlqry=null; locQryrst=null; locGrpMstrVOobj=null;
			
			lvrCmpltslqry = null;locCmpltqryrst = null;locCmplttblobj = null;lvrlbrdaosrv=null;lvrCommdaosrobj = null;
		}
  }
    
    public static String toimguploadCmplt(JSONObject pDataJson,String pGrpName, String pAuditMsg, String pAuditCode, String pWebImagpath, String pMobImgpath){
		String locFtrnStr=null;
		String lvrCrntusrid=null, lvrCrntgrpid=null, lvrComplaintid=null;
		String lvrCmplttit=null,lvrLabid=null, lvrCmpltdesc=null,lvrCmpltfilename1=null,lvrCmpltfiledata1=null,lvrCmpltfilename2=null,lvrCmpltfiledata2=null,lvrCmpltfilename3=null,lvrCmpltfiledata3=null,lvrCmpltVdpath=null;
		String lvrLabGrpid=null,lvrCmpltenddate=null, lvrCmpltSttime=null, lvrCmpltEndtime=null, lvrCmpltSts=null;
		Log log=null;
		String locUpdQry=null,lvrSlqry=null;
		ComplaintsDao locmpltDaoObj=null;
		boolean lvrComplaintUpdaSts=false;
		ComplaintsTblVO locComplainttblobj = null;
		Session locSession=null;
		String locSlqry=null;
		Query locQryrst=null,lvrQrybj=null;
		GroupMasterTblVo locGrpMstrVOobj=null;
		try{
			locSession = HibernateUtil.getSession();
			log=new Log();
			log.logMessage("Step 2 : Complaint Image Update Block.", "info", Complaintstutility.class);
			System.out.println("Step 2 : Complaint Update Block.");
			lvrComplaintid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "complaintid");
			lvrCmpltfilename1 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmplintfilename1");
			lvrCmpltfiledata1 = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "cmplintfiledata1");
			locSlqry = "from ComplaintsTblVO where complaintsId = "+Integer.parseInt(lvrComplaintid)+"";
			System.out.println("locSlqry  "+locSlqry);
			locQryrst = locSession.createQuery(locSlqry);
			locComplainttblobj = (ComplaintsTblVO)locQryrst.uniqueResult();	
			if(Commonutility.checkempty(lvrCmpltfilename1)){
				lvrCmpltfilename1 = lvrCmpltfilename1.replaceAll(" ", "_");
			}
			if(locComplainttblobj.getComplaintsFileName1()==null || locComplainttblobj.getComplaintsFileName1().equalsIgnoreCase("null") || locComplainttblobj.getComplaintsFileName1().equalsIgnoreCase("")) {
				locUpdQry ="update ComplaintsTblVO set complaintsFileName1 = '"+lvrCmpltfilename1+"'where complaintsId ="+Integer.parseInt(lvrComplaintid)+"";
			} else if(locComplainttblobj.getComplaintsFileName2()==null || locComplainttblobj.getComplaintsFileName2().equalsIgnoreCase("null") || locComplainttblobj.getComplaintsFileName2().equalsIgnoreCase("")) {
			
				locUpdQry ="update ComplaintsTblVO set complaintsFileName2 = '"+lvrCmpltfilename1+"'where complaintsId ="+Integer.parseInt(lvrComplaintid)+"";
			} else if(locComplainttblobj.getComplaintsFileName3()==null || locComplainttblobj.getComplaintsFileName3().equalsIgnoreCase("null") || locComplainttblobj.getComplaintsFileName3().equalsIgnoreCase("")) {
				locUpdQry ="update ComplaintsTblVO set complaintsFileName3 = '"+lvrCmpltfilename1+"'where complaintsId ="+Integer.parseInt(lvrComplaintid)+"";
			} else {
				locUpdQry ="update ComplaintsTblVO set complaintsFileName1 = '"+lvrCmpltfilename1+"'where complaintsId ="+Integer.parseInt(lvrComplaintid)+"";
			}
			
			log.logMessage("Step 3 : Complaint Updqry : "+locUpdQry, "info", Complaintstutility.class);
			//--------Complaint Update Start-------------
			locmpltDaoObj=new ComplaintsDaoservice();
			lvrComplaintUpdaSts=locmpltDaoObj.toUpdateComplaint(locUpdQry);
			//--------Complaint Update End-------------
			 if(Commonutility.toCheckNullEmpty(lvrComplaintid)!=null && !Commonutility.toCheckNullEmpty(lvrComplaintid).equalsIgnoreCase("null")&& !Commonutility.toCheckNullEmpty(lvrComplaintid).equalsIgnoreCase("")){
				 try{
					 log.logMessage("Step 4 : Complaint Image Write will start.[Update] ", "info", Complaintstutility.class);
					 //image write into folder
					 if((lvrCmpltfiledata1!=null && !lvrCmpltfiledata1.equalsIgnoreCase("null") && !lvrCmpltfiledata1.equalsIgnoreCase("")) && (lvrCmpltfilename1!=null && !lvrCmpltfilename1.equalsIgnoreCase("") && !lvrCmpltfilename1.equalsIgnoreCase("null"))){						
				        Commonutility.toWriteImgWebAndMob(Integer.parseInt(lvrComplaintid), lvrCmpltfilename1, lvrCmpltfiledata1, pWebImagpath, pMobImgpath, rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, Commonutility.class);					 
					 }else{						 
					 }
					 
				
					 log.logMessage("Step 4 : Complaint Image Upload Write will end.[Update] ", "info", Complaintstutility.class);
					} catch(Exception imger){
						 System.out.println("Exception in  image Upload write on Complaint update : "+imger);
						 log.logMessage("Exception in Image Upload write on Complaint Update", "info", LaborUtility.class);
					}finally{
						 
					} 
			 }
			 if(lvrComplaintUpdaSts){
				 System.out.println("lvrComplaintUpdaSts:::wwww  "+lvrComplaintUpdaSts);
				 return "success";
			 }else{
				 return "error";
			 }	
		}catch(Exception e){
			System.out.println("Exception found in Complaintutility.toUpdateComplaint : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", Complaintstutility.class);		
			locFtrnStr="error";
			return locFtrnStr;			
		}finally{
			locSession.close();locSession=null;
			lvrCrntusrid=null; lvrCrntgrpid=null; lvrComplaintid=null;
			lvrCmplttit=null;lvrLabid=null; lvrCmpltdesc=null;lvrCmpltfilename1=null;lvrCmpltfiledata1=null;lvrCmpltfilename2=null;lvrCmpltfiledata2=null;lvrCmpltfilename3=null;lvrCmpltfiledata3=null;lvrCmpltVdpath=null;
			lvrLabGrpid=null;lvrCmpltenddate=null; lvrCmpltSttime=null; lvrCmpltEndtime=null; lvrCmpltSts=null;
			log=null;
			locUpdQry=null;
			locmpltDaoObj=null;
			lvrComplaintUpdaSts=false;
			locSlqry=null;locQryrst=null;
			locGrpMstrVOobj=null;
		}
	}
}
