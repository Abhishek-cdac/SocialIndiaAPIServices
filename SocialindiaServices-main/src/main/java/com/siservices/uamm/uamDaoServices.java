package com.siservices.uamm;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import com.pack.audittrial.AuditTrial;
import com.pack.commonvo.FlatMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.MenuMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.siservices.uam.persistense.RightsMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.vo.NotificationStatusTblVO;


public class uamDaoServices implements uamDao {
	ResourceBundle ivrRbuilder = ResourceBundle.getBundle("aduit");
	Log log=null;
	@Override
	public List<RightsMasterTblVo> getUserForFamilyRights(int grpCode) {
		List<RightsMasterTblVo> rightsList = new ArrayList<RightsMasterTblVo>();
		Session session = null;	Query query = null;	
		try {log = new Log();
			Commonutility.toWriteConsole("Step 1 : Get Family Right Method [Start].");
			session = HibernateUtil.getSession();	
			String qry = "from RightsMasterTblVo where groupCode=:GROUP_CODE" + " order by menuId.rootDesc ASC";
			query = session.createQuery(qry);
			query.setInteger("GROUP_CODE",grpCode);			
			rightsList = query.list();			
		} catch (Exception ex) {
			log.logMessage("Exception found : "+ex, "error", uamDaoServices.class);
			Commonutility.toWriteConsole("Step -1 : Get Family Right Method [Exception Found] : "+ex);
		} finally {
			if(session!=null){session.clear(); session.close();session = null;}
			log = null; query = null;
		}
		Commonutility.toWriteConsole("Step 2 : Get Family Right Method [End].");
		return rightsList;

	}

	@Override
	public List<RightsMasterTblVo> getUserRights(UserMasterTblVo userData) {
		List<RightsMasterTblVo> rightsList = new ArrayList<RightsMasterTblVo>();
		Session session = null;	Query query = null;	
		try {	
			log = new Log();
			Commonutility.toWriteConsole("Step 1 : Get User Right Method [Start].");
			session = HibernateUtil.getSession();
			String qry = "from RightsMasterTblVo where groupCode=:GROUP_CODE"
					+ " order by menuId.rootDesc ASC";
			query = session.createQuery(qry);
			query.setInteger("GROUP_CODE",userData.getGroupCode().getGroupCode());
			// q.setInteger("GROUP_CODE", userInfo.getGroupId().getGroupCode());
			rightsList = query.list();			
		} catch (Exception ex) {
			log.logMessage("Exception found getUserRights() : "+ex, "error", uamDaoServices.class);
			Commonutility.toWriteConsole("Step -1 : Exception found getUserRights() : "+ex);
		} finally {
			if(session!=null){session.clear(); session.close();session = null;}
			log = null; query = null;
		}
		Commonutility.toWriteConsole("Step 2 : Get User Right Method [End].");
		return rightsList;

	}
	@Override
	public int getInitTotal(String sql) {
		int totcnt = 0;
		Session session = null;
		Query qy = null;
		try {log = new Log();
			session = HibernateUtil.getSession();
			qy = session.createQuery(sql);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {
			log.logMessage("Exception found getInitTotal() : "+ex, "error", uamDaoServices.class);
			Commonutility.toWriteConsole("Step -1 : Exception found getInitTotal() : "+ex);
		} finally {
			if(session!=null){session.clear(); session.close();session = null;}
			qy = null; log = null;
		}
		return totcnt;
	}

	@Override
	public int getTotalFilter(String sqlQueryFilter) {
		int totcnt = 0;
		Session session = null;
		Query qy = null;
		try {log = new Log();
			session = HibernateUtil.getSession();
			qy = session.createQuery(sqlQueryFilter);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {
			log.logMessage("Exception found getTotalFilter() : "+ex, "error", uamDaoServices.class);
			Commonutility.toWriteConsole("Step -1 : Exception found getTotalFilter() : "+ex);
		} finally {
			if(session!=null){session.clear(); session.close();session = null;}
			qy = null; log = null;
		}
		return totcnt;
	}

	@Override
	public List<UserMasterTblVo> getUserDetailList() {
		List<UserMasterTblVo> userdata = new ArrayList<UserMasterTblVo>();
		Session session = null; Query qy = null;
		try {log = new Log();
			session = HibernateUtil.getSession();
			String qry = "From UserMasterTblVo where statusFlag=:STATUS_FLAG";
			qy = session.createQuery(qry);
			qy.setString("STATUS_FLAG", "1");
			userdata = qy.list();
		} catch (HibernateException ex) {
			log.logMessage("Exception found getUserDetailList() : "+ex, "error", uamDaoServices.class);
			Commonutility.toWriteConsole("Step -1 : Exception found getUserDetailList() : "+ex);
		} finally {
			if(session!=null){session.clear(); session.close();session = null;}
			log = null; qy = null;
		}
		return userdata;
	}

	@Override
	public List<GroupMasterTblVo> getGroupDetailList() {
		List<GroupMasterTblVo> groupdata = new ArrayList<GroupMasterTblVo>();
		Session session = null; Query qy = null;
		try {log = new Log();
			session = HibernateUtil.getSession();
			String qry = "From GroupMasterTblVo where statusFlag=:STATUS_FLAG order by groupCode asc";
			qy = session.createQuery(qry);
			qy.setString("STATUS_FLAG", "1");
			groupdata = qy.list();
		} catch (HibernateException ex) {
			log.logMessage("Exception found getGroupDetailList() : "+ex, "error", uamDaoServices.class);
			Commonutility.toWriteConsole("Step -1 : Exception found getGroupDetailList() : "+ex);
		} finally {
			if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
		}
		return groupdata;
	}
	
	@Override
	public List<GroupMasterTblVo> getGroupDetailListSrh(String prmSrchstr) {
		List<GroupMasterTblVo> groupdata = new ArrayList<GroupMasterTblVo>();
		Session session = null; Query qy = null;
		try {log = new Log();
		String globalsearch = " where statusFlag = 1 and (" + "groupName like ('%" + prmSrchstr
				+ "%') or entryDatetime like ('%" + prmSrchstr + "%') )";
			session = HibernateUtil.getSession();
			String qry = "From GroupMasterTblVo "+globalsearch+"  order by groupCode asc";
			qy = session.createQuery(qry);
			//qy.setString("STATUS_FLAG", "1");
			groupdata = qy.list();
		} catch (HibernateException ex) {
			log.logMessage("Exception found getGroupDetailList() : "+ex, "error", uamDaoServices.class);
			Commonutility.toWriteConsole("Step -1 : Exception found getGroupDetailList() : "+ex);
		} finally {
			if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
		}
		return groupdata;
	}

	@Override
	public List<MenuMasterTblVo> getAllMenuMasterList() {
		List<MenuMasterTblVo> menu = new ArrayList<MenuMasterTblVo>();
		Session session = null; Query query =  null;
		try {log = new Log();
			 Commonutility.toWriteConsole("Step 1 : Get all Menu list method [Start].");
			 session = HibernateUtil.getSession();
			 ServletContext context = ServletActionContext.getServletContext();
			 String qry = " From MenuMasterTblVo order by rootDesc ASC";
			 query = session.createQuery(qry);
			 menu = query.list();
			 if(menu!=null){
				 Commonutility.toWriteConsole("Step 2 : Menu Size : "+menu.size());
			 }else{
				 Commonutility.toWriteConsole("Step 2 : Menu Size : Empty");
			 }			 
		} catch (Exception ex) {
			log.logMessage("Step -1 : Exception found getAllMenuMasterList() : "+ex, "error", uamDaoServices.class);
			Commonutility.toWriteConsole("Step -1 : Exception found getAllMenuMasterList() : "+ex);
		} finally {
			if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; query = null;
		}
		Commonutility.toWriteConsole("Step 3 : Get all Menu list method [End].");
		return menu;
	}

	@Override
	public String getMenuRightsMenu(int menuid) {
		String groupname = "";
		List<RightsMasterTblVo> rightsMasterList = new ArrayList<RightsMasterTblVo>();
		Session session = null;
		String qry = null;
		Query qy = null;
		try {
			log = new Log();
			Commonutility.toWriteConsole("Step 1 : Get  Menu Rights getMenuRightsMenu() [Start].");
			session = HibernateUtil.getSession();
			qry = " select  r FROM RightsMasterTblVo r , MenuMasterTblVo m where " + " r.menuId=m.menuId and r.groupCode=" + menuid + " order by m.rootDesc ASC";
			qy = session.createQuery(qry);
			rightsMasterList = qy.list();
			RightsMasterTblVo empIdObj = null;
			for (Iterator<RightsMasterTblVo> it = rightsMasterList.iterator(); it.hasNext();) {
				empIdObj = it.next();
				if (groupname.equalsIgnoreCase("")) {
					groupname += "'" + empIdObj.getMenuId().getRootDesc() + "'";
				} else {
					groupname += ",'" + empIdObj.getMenuId().getRootDesc() + "'";
				}
			}
			Commonutility.toWriteConsole("Step 2 : Get  Menu Rights getMenuRightsMenu() [End].");
		} catch (HibernateException ex) {
			log.logMessage("Step -1 : Exception found getMenuRightsMenu() : "+ex, "error", uamDaoServices.class);
			Commonutility.toWriteConsole("Step -1 : Exception found getMenuRightsMenu() : "+ex);
		} finally {
			if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
		}
		return groupname;
	}

	@Override
	public boolean createnewgroup(String sql, String groupName) {
			boolean result = false;
			String getexistGroup = "";
			GroupMasterTblVo groupmang = new GroupMasterTblVo();
			Transaction tx = null;		 
			Session session = null;
			Query qy = null;
		try {
			log = new Log();
			Commonutility.toWriteConsole("Step 1 : Create New Group [Start].");
			session = HibernateUtil.getSession();
			qy = session.createQuery(sql);
			getexistGroup = qy.uniqueResult().toString();			
			if (groupName != null && groupName.length() > 0 && getexistGroup.equalsIgnoreCase("NEW")){
					
					Date date = null;
					CommonUtils comutil=new CommonUtilsServices();
					date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
					groupmang.setStatusFlag("1");
					groupmang.setGroupName(groupName);					
					groupmang.setUpdateBy(1);
					groupmang.setEntryDatetime(date);
					groupmang.setModifyDatetime(date);	
					groupmang.setAccessMedia(3);
		          try {
		        	  tx = session.beginTransaction();
		        	  session.save(groupmang);
		        	  tx.commit();
		        	  Commonutility.toWriteConsole("Step 2 : New Group will created.");
		        	  result = true;
		          } catch (HibernateException ex) {
		        	  if (tx != null) {
		        		  tx.rollback();
		        	  }		   
		        	  log.logMessage("Step -1 : Exception found createnewgroup() 1 : "+ex, "error", uamDaoServices.class);
		  			  Commonutility.toWriteConsole("Step -1 : Exception found createnewgroup() : "+ex);
		        	  result = false;
		          } finally {		          
		          }
			}
		} catch (HibernateException ex) {
			log.logMessage("Step -1 : Exception found createnewgroup() 2 : "+ex, "error", uamDaoServices.class);
			Commonutility.toWriteConsole("Step -1 : Exception found createnewgroup() : "+ex);
			result=false;
		} finally {
			if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
		}
		Commonutility.toWriteConsole("Step 3 : Create New Group [End].");
		return result;
		
	}

	@Override
	public boolean deletegroup(int delId) {
		 	boolean result = false;
		    Session session = null;
		    Transaction tx = null;
		    Query qy = null;
		    GroupMasterTblVo groupmang = null;
		    try {
		    	log = new Log();
		    	Commonutility.toWriteConsole("Step 1 : Delete Group block [Start].");
		    	groupmang = new GroupMasterTblVo();
		    	session = HibernateUtil.getSession();
		    	tx = session.beginTransaction();
		    	qy = session.createQuery("update GroupMasterTblVo set statusFlag='3' ,entryBy=:ENTRY_BY, "
		              + " modifyDatetime=:MODY_DATETIME where groupCode="
		              + delId + "");
		    	qy.setParameter("ENTRY_BY", groupmang.getEntryBy());
		    	qy.setDate("MODY_DATETIME", groupmang.getModifyDatetime());
		    	qy.executeUpdate();
		    	tx.commit();
		    	result = true;
		    } catch (HibernateException ex) {
		    	if (tx != null) {tx.rollback();}		     
		    	result = false;
		    	log.logMessage("Step -1 : Exception found deletegroup() : "+ex, "error", uamDaoServices.class);
				Commonutility.toWriteConsole("Step -1 : Exception found deletegroup() : "+ex);
		    } finally {
		    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
				log = null; qy = null;
		    }
		    Commonutility.toWriteConsole("Step 3 : Delete Group block [End].");
		    return result;
	}

	@Override
	public boolean editgroup(int delId, String groupName) {
		 	boolean result = false;
		    Session session = null;
		    Transaction tx = null;
		    Query qy = null;
		    try {log = new Log();
		    	Commonutility.toWriteConsole("Step 1 : Group Edit [Start].");
		    	session = HibernateUtil.getSession();
		    	tx = session.beginTransaction();		      		    
		    	String query = "update GroupMasterTblVo set groupName='"
		          + groupName + "'"
		          + " where groupCode="
		          + delId + "";
		    	qy = session.createQuery(query);
		    	//Date date;
		    	//CommonUtils comutil=new CommonUtilsServices();
		    	//date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		    	//qy.setDate("MODY_DATETIME", date);
		    	qy.executeUpdate();
		    	tx.commit();
		    	Commonutility.toWriteConsole("Step 2 : Group Edit [Success].");
		    	result = true;
		    } catch (HibernateException ex) {
		      if (tx != null) { tx.rollback(); }		      
		      result = false;
		      log.logMessage("Step -1 : Exception found editgroup() : "+ex, "error", uamDaoServices.class);
			  Commonutility.toWriteConsole("Step -1 : Exception found editgroup() : "+ex);
		    } finally {
		    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
				log = null; qy = null;
		    }
		    	Commonutility.toWriteConsole("Step 2 : Group Edit [End].");
		    return result;
	}

	@Override
	public String userCreation(UserMasterTblVo userMaster,String blockNameList,String flatNameList,String famName,String famMobileNo,String famEmailId,SocietyMstTbl societyMaster, GroupMasterTblVo groupMaster,int accessMedia,String famISD,String famMemTyp,String famPrfAccess) {
		 UserMasterTblVo userDetails = new UserMasterTblVo();
		 MvpFamilymbrTbl userFamilyData=new MvpFamilymbrTbl();
		 List<UserMasterTblVo> userDetailsList = new ArrayList<UserMasterTblVo>();
		 List<UserMasterTblVo> userDetailsListmob = new ArrayList<UserMasterTblVo>();
		 FlatMasterTblVO flatMst=new FlatMasterTblVO ();
		 UserMasterTblVo userIdobj=new UserMasterTblVo();
		 String result="";		
		 int count=0;
		 int mobCount=0;
		 int usercount=0;
		 Date date;
		 CommonUtils comutil=new CommonUtilsServices();
		 date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		 Session session = null;
		 Transaction tx = null;
		 Query query1 = null;
		    try {log = new Log();
		    	session = HibernateUtil.getSession();
		    	Commonutility.toWriteConsole("Step 2 : uamDaoServices.userCreation() method enter");
		    	String qry1 = "";
		    	if(userMaster.getSocietyId()==null){
		    		qry1 = "from UserMasterTblVo  where mobileNo='"+userMaster.getMobileNo()+"' and statusFlag=1";
		    	}else{
		    		qry1 = "from UserMasterTblVo  where mobileNo='"+userMaster.getMobileNo()+"' and societyId="+userMaster.getSocietyId().getSocietyId()+" and statusFlag=1";
		    	}		    		    
		    	query1 = session.createQuery(qry1);
		    	if(query1!=null){		    		
		    		userDetailsListmob = query1.list();	
		    	} else {
		    		//Commonutility.toWriteConsole("----1 else");
		    	}
		    		       
		    	if(userDetailsListmob==null || userDetailsListmob.size()==0){
		    	   try {
		    		   Commonutility.toWriteConsole("INSERT Will Start");
			        	  tx = session.beginTransaction();
			        	  if(accessMedia!=0){
			        		  userMaster.setAccessChannel(accessMedia);
			        	  }else{
			        		  userMaster.setAccessChannel(0);  
			        	  }
			              session.save(userMaster);			            			           
			              int userId=userMaster.getUserId();
			              if(famName!=null && !famName.equalsIgnoreCase("null") && !famName.equalsIgnoreCase("") && famName.contains("!_!")){			            	
			            	String name[]=famName.split("!_!",-1);
			            	String mobile[]=famMobileNo.split("!_!",-1);
			            	String email[]=famEmailId.split("!_!",-1);
			            	String isdarr[]=famISD.split("!_!",-1);
			            	String memtyparr[]=famMemTyp.split("!_!",-1);
			            	String prfaccarr[] =famPrfAccess.split("!_!",-1);
			            	
			            	int mobilesize=mobile.length;
			            	int emailsize=email.length;
			            	int namesize=name.length;
			            	int  isdsize=isdarr.length;
			            	int memtypesize =memtyparr.length;
			            	int prfaccsize =prfaccarr.length;
			            	if(famName!=null ){
			            	for( int i=0;i<name.length;i++){
			            		userFamilyData=new MvpFamilymbrTbl();
			            		String familyname="";			            		
			            		String mobname="",isdcode="",membertype="",prfaccess="";
			            		String emailname="";
			            		if(namesize>=i){ familyname=name[i]; }			            		
			            		if(mobilesize>=i){ mobname=mobile[i]; }			            		
			            		if(emailsize>=i){ emailname=email[i]; }			            		
			            		if(isdsize>=i)
			            		{
			            			isdcode =isdarr[i];
			            		}
			            		if(memtypesize>=i)
			            		{
			            			membertype =memtyparr[i];
			            		}
			            		if(prfaccsize>=i)
			            		{
			            			prfaccess =prfaccarr[i];
			            		}			            		
			            		userFamilyData.setUserId(userMaster);			            		
			            		String password=comutil.getRandomval("AZ_09", 10);
			            		userFamilyData.setFmbrPswd(password);
			            		userFamilyData.setStatus(1);
			            		if(!familyname.equalsIgnoreCase("")){
			            			userFamilyData.setFmbrName(familyname);
			            		}else{
			            			userFamilyData.setFmbrName(null);
			            		}if(!isdcode.equalsIgnoreCase("")){
			            			userFamilyData.setFmbrIsdCode(isdcode);
			            		}else{
			            			userFamilyData.setFmbrIsdCode(null);
			            		}if(!mobname.equalsIgnoreCase("")){
			            			userFamilyData.setFmbrPhNo(mobname);
			            		}else{
			            			userFamilyData.setFmbrPhNo(null);
			            		}if(!emailname.equalsIgnoreCase("")){
			            			userFamilyData.setFmbrEmail(emailname);
			            		}
			            		else{
			            			userFamilyData.setFmbrEmail(null);
			            		}if(!isdcode.equalsIgnoreCase("")){
				            			userFamilyData.setFmbrIsdCode(isdcode);
			            		} else{userFamilyData.setFmbrIsdCode(null);}
			            		
				            			userFamilyData.setFmbrType(Integer.parseInt(membertype));
				            			userFamilyData.setFmbrProfAcc(Integer.parseInt(prfaccess));				            	
			            		/*	//Send Email
			            			EmailEngineServices emailservice = new EmailEngineDaoServices();
			            			EmailTemplateTblVo emailTemplate;
			            			Commonutility.toWriteConsole("===========email===");
			            			try {
			            	            String emqry = "FROM EmailTemplateTblVo where "+ "tempName ='Create Customer' and status ='1'";
			            	            emailTemplate = emailservice.emailTemplateData(emqry);
			            	            String emailMessage = emailTemplate.getTempContent();
			            	            CommonUtilsDao common=new CommonUtilsDao();	
			            	            emqry = common.templateParser(emailMessage, 1, "", password);
			            	            String[] qrySplit = emqry.split("!_!");
			            	            String qry4 = qrySplit[0] + " FROM UserMasterTblVo as cust where cust.emailId='"+emailname+"'";
			            	            emailMessage = emailservice.templateParserChange(qry4, Integer.parseInt(qrySplit[1]),emailMessage);
			            	            emailTemplate.setTempContent(emailMessage);
			            	            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
			            	            
			            	            EmailInsertFuntion emailfun = new EmailInsertFuntion();
			            	            emailfun.test2(emailname, emailTemplate.getSubject(), emailMessage);
			            	          } catch (Exception ex) {
			            	            Commonutility.toWriteConsole("Excption found in Emailsend Forgetpassword.class : " + ex);
			            	            log.logMessage("Exception in uam create user flow emailFlow----> " + ex, "error",signUpProcess.class);
			            	            
			            	          }	*/			            		
			            		userFamilyData.setEntryDatetime(date);
			            		session.save(userFamilyData);
							}
						}
					} else {
			            	if(famName!=null && !famName.equalsIgnoreCase("null") && !famName.equalsIgnoreCase("")){
			            		String password=comutil.getRandomval("AZ_09", 10);
				            	userFamilyData.setUserId(userMaster);
				            	userFamilyData.setFmbrPswd(password);
				            	userFamilyData.setFmbrName(famName);
				            	userFamilyData.setFmbrPhNo(famMobileNo);
				            	userFamilyData.setFmbrEmail(famEmailId);
				            	userFamilyData.setFmbrIsdCode(famISD);
				            	userFamilyData.setFmbrType(Integer.parseInt(famMemTyp));
				            	userFamilyData.setFmbrProfAcc(Integer.parseInt(famPrfAccess));
				            	userFamilyData.setEntryDatetime(date);
				            	userFamilyData.setStatus(1);
			            		session.save(userFamilyData);
			            	}			            				            
			        }
			            if(flatNameList!=null && flatNameList.contains("!_!") || blockNameList!=null && blockNameList.contains("!_!") ){
							String spli[] = flatNameList.split("!_!");
							String spli1[] = blockNameList.split("!_!");
							if (flatNameList != null) {
								for (int i = 0; i < spli.length; i++) {
									flatMst = new FlatMasterTblVO();
									String flatname = spli[i];
									String blockname = spli1[i];
									if(Commonutility.checkempty(flatname) && Commonutility.checkempty(blockname)){
									flatMst.setFlatno(flatname);
									flatMst.setWingsname(blockname);
									userIdobj.setUserId(userMaster.getUserId());
									flatMst.setUsrid(userIdobj);
									flatMst.setStatus(1);
									flatMst.setEntryby(1);
									flatMst.setEntryDatetime(date);
									if (i == 0) {
										flatMst.setIvrBnismyself(1);
									} else {
										flatMst.setIvrBnismyself(0);
									}	
									session.save(flatMst);
									}
			            	}
			            }
			           
					} else {
						if (Commonutility.checkempty(flatNameList) && Commonutility.checkempty(blockNameList)){
							flatMst.setFlatno(flatNameList);
							flatMst.setWingsname(blockNameList);
							userIdobj.setUserId(userMaster.getUserId());
							flatMst.setUsrid(userIdobj);
							flatMst.setStatus(1);
							flatMst.setEntryby(1);
							flatMst.setEntryDatetime(date);
							flatMst.setIvrBnismyself(1);
							session.save(flatMst);
						}
			        }
			            
			           
			            tx.commit();
			            result = "insertsuccess";
			          } catch (HibernateException ex) {
			        		Commonutility.toWriteConsole("Exception found in uamDaoSrvices Family memebr insert : "+ex);
			            if (tx != null) {
			              tx.rollback();
			            }
			            Commonutility.toWriteConsole("uamDaoSrvices.java : "+ex);
			            result = "error";
			          } 
		       } else{
		    	   result = "mobnoExist";
		       }
		    } catch (HibernateException ex) {
		    	 result = "error";
		    	 log.logMessage("Step -1 : Exception found userCreation() : "+ex, "error", uamDaoServices.class);
				 Commonutility.toWriteConsole("Step -1 : Exception found userCreation() : "+ex);     
		    } finally {
		    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
				log = null; query1 = null;
		    }
		    Commonutility.toWriteConsole("Step 3 : uamDaoServices.userCreation() method End");
		    return result;
		  }

	@Override
	public boolean deleteuser(int delUsrId) {
		 	boolean result = false;
		    Session session = null;
		    Transaction tx = null;
		    GroupMasterTblVo groupmang = new GroupMasterTblVo();
		    Query qy = null;
		    try {log = new Log();
		    	Commonutility.toWriteConsole("Step 1 : Delete User [Start].");
		    	session = HibernateUtil.getSession();
		    	tx = session.beginTransaction();
		    	qy = session.createQuery("update UserMasterTblVo set statusFlag='0' "
		              + "  where userId="
		              + delUsrId + "");
		    	qy.executeUpdate();
		    	tx.commit();
		    	Commonutility.toWriteConsole("Step 2 : User Deleted."); 
		    	result = true;
		    } catch (HibernateException ex) {
		    	if (tx != null) { tx.rollback(); }		     
		    	result = false;
		    	log.logMessage("Step -1 : Exception found deleteuser() : "+ex, "error", uamDaoServices.class);
				Commonutility.toWriteConsole("Step -1 : Exception found deleteuser() : "+ex);   
		    } finally {
		    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
		    	qy = null; log = null;
		    }
		    Commonutility.toWriteConsole("Step 3 : Delete User [End].");
		    return result;
	}

	@Override
	public UserMasterTblVo editUser(int userId) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		Session session = null;
		Query qy = null;
		try {log = new Log();
			session = HibernateUtil.getSession();
    		Commonutility.toWriteConsole("Step 1 : User Edit editUser() [Start].");
			String qry = "From UserMasterTblVo where userId=:UNI_ID";
			qy = session.createQuery(qry);
			qy.setInteger("UNI_ID", userId);
			userdata = (UserMasterTblVo) qy.uniqueResult();
		} catch (HibernateException ex) {
			log.logMessage("Step -1 : Exception found editUser() : "+ex, "error", uamDaoServices.class);
			Commonutility.toWriteConsole("Step -1 : Exception found editUser() : "+ex);
		} finally {
			if(session!=null){session.flush();session.clear(); session.close();session = null;}
			//HibernateUtil.shutdown();
			qy = null; log = null;
		}
		Commonutility.toWriteConsole("Step 2 : User Edit editUser() [End].");
		return userdata;
	}

	
	@Override
	public boolean rightsCreation(int grpId, int menuId) {
		 	boolean result = false;
		  	MenuMasterTblVo menu = new MenuMasterTblVo();
		  	GroupMasterTblVo groupval = new GroupMasterTblVo();
		  	RightsMasterTblVo rightsMast = new RightsMasterTblVo();
		  	Session session = null;		   		    
		    Transaction tx = null;
		    	try {
		    		 Commonutility.toWriteConsole("Step 1 : Rights Creation rightsCreation() [Start].");
		    		 log = new Log();
		    		 session = HibernateUtil.getSession();
		        	 groupval.setGroupCode(grpId);
		             menu.setMenuId(menuId);
		             rightsMast.setMenuId(menu);
		             rightsMast.setGroupCode(groupval);
		             rightsMast.setEntryBy(1);
		             rightsMast.setRightsAdd(1);
		             rightsMast.setRightsEdit(1);
		             rightsMast.setRightsDelete(1);
		             rightsMast.setRightsView(1);
		             tx = session.beginTransaction();
		             session.save(rightsMast);
		             tx.commit();
		             result = true;		             
		      } catch (HibernateException ex) {
		    	  if (tx != null) { tx.rollback(); }		       
		    	  result = false;
		    	  log.logMessage("Step -1 : Exception found rightsCreation() : "+ex, "error", uamDaoServices.class);
				  Commonutility.toWriteConsole("Step -1 : Exception found rightsCreation() : "+ex);
		      } finally {
		    	  if(session!=null){session.flush(); session.clear(); session.close();session = null;}
				  log = null;
		      }
		    Commonutility.toWriteConsole("Step 2 : Rights Creation rightsCreation() [End].");   
		    return result;
	}

	@Override
	public boolean deleteGroupCode(int grpCode) {
		 	  boolean result = false;		    
		      Session session = null;
		      Transaction tx = null;
		      Query qy = null;
		      try {
		    	  Commonutility.toWriteConsole("Step 1 : Delete Group Code deleteGroupCode() [Start].");		    	  
	    		  log = new Log();	
	    		  log.logMessage("Step 1 : Delete Group Code deleteGroupCode() [Start].", "info", uamDaoServices.class);
		    	  session = HibernateUtil.getSession();
		          tx = session.beginTransaction();
		          qy = session.createQuery("delete from RightsMasterTblVo where groupCode=:GROUP_CODE");
		          qy.setInteger("GROUP_CODE", grpCode);
		          qy.executeUpdate();
		          tx.commit();
		          result = true;
		          log.logMessage("Step 2 : Delete Group Code deleteGroupCode() [End].", "info", uamDaoServices.class);
		      } catch (HibernateException ex) {
		    	  if (tx != null) { tx.rollback(); }		        
		    	  result = false;
		    	  log.logMessage("Step -1 : Exception found deleteGroupCode() : "+ex, "error", uamDaoServices.class);
				  Commonutility.toWriteConsole("Step -1 : Exception found deleteGroupCode() : "+ex);
		      } finally {
		    	  if(session!=null){session.flush();session.clear(); session.close();session = null;}
				  log = null; qy = null;
		      }	
		      Commonutility.toWriteConsole("Step 2 : Delete Group Code deleteGroupCode() [End].");
		      
		    return result;
	}

	@Override
	public List<RightsMasterTblVo> getUserRightscurrent(int grpCode) {
		List<RightsMasterTblVo> rightsList = new ArrayList<RightsMasterTblVo>();
		Session session = null;
		log=new Log();
		try {		
			log.logMessage("Get User Rights getUserRightscurrent() [Start]", "info", uamDaoServices.class);
			session = HibernateUtil.getSession();
			if(session.isConnected()){
			String qry = "from RightsMasterTblVo where groupCode=:GROUP_CODE" + " order by menuId.rootDesc ASC";
			log.logMessage("Rights qry : "+qry, "info", uamDaoServices.class);
			Query query = session.createQuery(qry);
			query.setInteger("GROUP_CODE", grpCode);			
			rightsList = query.list();	
			}else{
				 rightsList=null;	 
				 log.logMessage("Get User Rights getUserRightscurrent() [Database not connected.]", "info", uamDaoServices.class);
			}
		} catch (Exception ex) {
			log.logMessage("Exception found : "+ex, "error", uamDaoServices.class);
		} finally {
			if (session!=null) { session.flush();session.clear(); session.close();session = null; }
			log.logMessage("Get User Rights getUserRightscurrent() [End]", "info", uamDaoServices.class);
		}
		return rightsList;
	}

	@Override
	public UserMasterTblVo loginusercheck(UserMasterTblVo userinfo,int societyId) {
		UserMasterTblVo userDetails = new UserMasterTblVo();
		Session session = null;
		Transaction tx = null;
		Query query = null;
		try {
			log = new Log();
		    session = HibernateUtil.getSession();
		    if(session.isConnected()){
		    	tx=session.beginTransaction();
		    	String qry="";		    	
		    	if(societyId!=0){
		         qry = " from UserMasterTblVo where (userName=:USER_NAME"  + " or emailId=:USER_NAME or mobileNo=:USER_NAME) "
		        		+ " and " +"password=:PASSWORD and societyId.societyId=:SOCIETY_ID  and statusFlag=:STATUS_FLAG ";
		    	}else{
		    		qry = " from UserMasterTblVo where (userName=:USER_NAME"  + " or emailId=:USER_NAME or mobileNo=:USER_NAME) "
			        		+ " and " +"password=:PASSWORD  and statusFlag=:STATUS_FLAG ";
		    	}
				log.logMessage("Login Query : " + qry, "info", uamDaoServices.class);
				query = session.createQuery(qry);
				query.setString("USER_NAME", userinfo.getUserName());
				query.setString("PASSWORD", userinfo.getPassword());
				if (societyId != 0) {
					query.setInteger("SOCIETY_ID", societyId);
				}
		        query.setInteger("STATUS_FLAG", 1);
		        userDetails = (UserMasterTblVo) query.uniqueResult();	
		        
		        Commonutility.toWriteConsole("Password : " + userinfo.getPassword() + " societyId:"+societyId + " getUserName:"+userinfo.getUserName() +" userDetails:"+userDetails);
		        
		        if (userDetails!=null) {
		        	
		        	AuditTrial.toWriteAudit(ivrRbuilder.getString("LG0001"), "LG0001", userDetails.getUserId());
		        	String qry1="update UserMasterTblVo set currentLogins=:CURRENT_LOGINS+1, loginCount=:LOGIN_COUNT+1, loginDatetime=:LAST_LOGIN_Date, ivrBnISONLINEFLG=:onlinestatus, loggedOut=:loggedOutStatus "
			    	  		+ " where userId=:UNIQ_ID"; 
					log.logMessage("update Query : " + qry1, "info", uamDaoServices.class);
					Query query1 = session.createQuery(qry1);
					CommonUtils comutil = new CommonUtilsServices();
					Date date = null;
					date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
					query1.setInteger("CURRENT_LOGINS", userDetails.getCurrentLogins());
					query1.setInteger("LOGIN_COUNT", userDetails.getLoginCount());
					query1.setInteger("UNIQ_ID", userDetails.getUserId());
					query1.setTimestamp("LAST_LOGIN_Date", date);
					query1.setInteger("onlinestatus", 1);// 1 is online, 0 - off line
					query1.setInteger("loggedOutStatus", 0);// 1 is logged Out 0 - logged in
					query1.executeUpdate();
					tx.commit();
				}
			} else {
		    		log.logMessage("Database Connection Not Found.", "info", uamDaoServices.class);
		    		userDetails=null;
			}
		    } catch (Exception ex) {
		    	 log.logMessage("Exception found loginusercheck() mthd :"+ex, "error", uamDaoServices.class);
		    } finally {if(session!=null){session.flush();session.clear(); session.close();session = null;} tx = null;	
			    log=null;
			    query = null;
		    }
		    return userDetails;
	}
	
	@Override
	public void resetLogin(String userName, String password, int societyId, int flag) {
		Session session = null;
		Transaction tx = null;
		Query query1 = null;
		try {
			log = new Log();
		    session = HibernateUtil.getSession();
		    if(session.isConnected()){
		    	tx=session.beginTransaction();
		        String qry1=""; 
		        if(societyId!=0){
		    		if(flag == 2){
		    			qry1 = " update UserMasterTblVo set currentLogins=:CURRENT_LOGINS, resetDatetime=:RESET_DATETIME where (userName=:USER_NAME or emailId=:USER_NAME or mobileNo=:USER_NAME) "
				        		+ " and password=md5(:PASSWORD) and societyId.societyId=:SOCIETY_ID  and statusFlag=:STATUS_FLAG ";
		    		}
		    		else{
		    			qry1 = " update UserMasterTblVo set currentLogins=:CURRENT_LOGINS, resetDatetime=:RESET_DATETIME where (userName=:USER_NAME or emailId=:USER_NAME or mobileNo=:USER_NAME) "
		        		+ " and password=:PASSWORD and societyId.societyId=:SOCIETY_ID  and statusFlag=:STATUS_FLAG ";
		    		}
		    	}else{
		    		if(flag == 2){
		    			qry1 = " update UserMasterTblVo set currentLogins=:CURRENT_LOGINS, resetDatetime=:RESET_DATETIME where (userName=:USER_NAME or emailId=:USER_NAME or mobileNo=:USER_NAME) "
				        		+ " and password=md5(:PASSWORD) and statusFlag=:STATUS_FLAG ";
		    		}
		    		else{
		    			qry1 = " update UserMasterTblVo set currentLogins=:CURRENT_LOGINS, resetDatetime=:RESET_DATETIME where (userName=:USER_NAME or emailId=:USER_NAME or mobileNo=:USER_NAME) "
			        		+ " and password=:PASSWORD  and statusFlag=:STATUS_FLAG ";
		    		}
		    	}
		        
				log.logMessage("update Query : " + qry1, "info", uamDaoServices.class);
					
				
				CommonUtils comutil=new CommonUtilsServices();
				Date date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
				
				query1 = session.createQuery(qry1);
				query1.setInteger("CURRENT_LOGINS", 0);
				query1.setString("USER_NAME", userName);
				query1.setString("PASSWORD", password);
				query1.setTimestamp("RESET_DATETIME", date);
				
				if (societyId != 0) {
					query1.setInteger("SOCIETY_ID", societyId);
				}
				query1.setInteger("STATUS_FLAG", 1);
				query1.executeUpdate();
				tx.commit();
			} else {
		    		log.logMessage("Database Connection Not Found.", "info", uamDaoServices.class);
			}
		} catch (Exception ex) {
		    	 log.logMessage("Exception found loginusercheck() mthd :"+ex, "error", uamDaoServices.class);
		} finally {if(session!=null){session.flush();session.clear(); session.close();session = null;} tx = null;	
			    log=null;
			    query1 = null;
		}
	}
	
	
	@Override
	public UserMasterTblVo getUserDetails(String userName, String password, int societyId, int flag) {
		UserMasterTblVo userDetails = new UserMasterTblVo();
		Session session = null;
		Query query = null;
		try {
			log = new Log();
		    session = HibernateUtil.getSession();
		    if(session.isConnected()){
		    	String qry="";		    	
		    	if(societyId!=0){
		    		if(flag == 2){
		    			qry = " from UserMasterTblVo where (userName=:USER_NAME or emailId=:USER_NAME or mobileNo=:USER_NAME) "
				        		+ " and password=md5(:PASSWORD) and societyId.societyId=:SOCIETY_ID  and statusFlag=:STATUS_FLAG ";
		    		}
		    		else{
		    			qry = " from UserMasterTblVo where (userName=:USER_NAME or emailId=:USER_NAME or mobileNo=:USER_NAME) "
		        		+ " and password=:PASSWORD and societyId.societyId=:SOCIETY_ID  and statusFlag=:STATUS_FLAG ";
		    		}
		    	}else{
		    		if(flag == 2){
		    			qry = " from UserMasterTblVo where (userName=:USER_NAME or emailId=:USER_NAME or mobileNo=:USER_NAME) "
				        		+ " and password=md5(:PASSWORD) and statusFlag=:STATUS_FLAG ";
		    		}
		    		else{
		    			qry = " from UserMasterTblVo where (userName=:USER_NAME or emailId=:USER_NAME or mobileNo=:USER_NAME) "
			        		+ " and password=:PASSWORD  and statusFlag=:STATUS_FLAG ";
		    		}
		    	}
				log.logMessage("Login Query : " + qry, "info", uamDaoServices.class);
				Commonutility.toWriteConsole("Password : " + password);
				query = session.createQuery(qry);
				query.setString("USER_NAME", userName);
				query.setString("PASSWORD", password);
				if (societyId != 0) {
					query.setInteger("SOCIETY_ID", societyId);
				}
		        query.setInteger("STATUS_FLAG", 1);
		        userDetails = (UserMasterTblVo) query.uniqueResult();		     
			} else {
		    		log.logMessage("Database Connection Not Found.", "info", uamDaoServices.class);
		    		userDetails=null;
			}
		    } catch (Exception ex) {
		    	 log.logMessage("Exception found loginusercheck() mthd :"+ex, "error", uamDaoServices.class);
		    } finally {if(session!=null){session.flush();session.clear(); session.close();session = null;} 	
			    log=null;
			    query = null;
		    }
		    return userDetails;
	}

	@Override
	public List<TownshipMstTbl> gettownshiplist() {
			List<TownshipMstTbl> townshiplist = new ArrayList<TownshipMstTbl>();
			Session session = HibernateUtil.getSession();
		    try {
		      String qry = " from TownshipMstTbl";
		      Query qy = session.createQuery(qry);
		      townshiplist = qy.list();
		    } catch (Exception ex) {
		    	ex.printStackTrace();
		    } finally {
		    	session.flush();session.clear();session.close(); session = null;	      
		    }
		    return townshiplist;
		}

	@Override
	public UserMasterTblVo getregistertable(int userid) {
		UserMasterTblVo userdata = new UserMasterTblVo();
		Session session = HibernateUtil.getSession();
	    try {
	      String qry = " from UserMasterTblVo where userId=:USER_ID";
	      Query qy = session.createQuery(qry);
	      qy.setInteger("USER_ID", userid);
	      userdata = (UserMasterTblVo) qy.uniqueResult();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	      
	    }
	    return userdata;
	}

	@Override
	public List<SocietyMstTbl> societyListpertown(int societyid) {
		List<SocietyMstTbl> societylist = new ArrayList<SocietyMstTbl>();
		Session session = null;		
		Query qy = null;
		try {log=new Log();session = HibernateUtil.getSession();
			String qry = "From SocietyMstTbl where societyId=:SOCIETY_ID";
			qy = session.createQuery(qry);
			qy.setInteger("SOCIETY_ID", societyid);
			societylist = qy.list();
		} catch (HibernateException ex) {			
			log.logMessage("Exception found in societyListpertown() : "+ex, "error", uamDaoServices.class);
		} finally {
			if(session!=null){session.flush();session.clear(); session.close(); session = null;}
			qy =null; log =null;			
		}
		return societylist;
	}
	
	
	@Override
	public int createnewgroup_rtnId(String groupName) {// it return group id
		 int locRtnGrpId=-1;		
		 GroupMasterTblVo groupmang = new GroupMasterTblVo();
		 Transaction tx = null;		 
		 Session session = HibernateUtil.getSession();
		try {			
					Date date;
					CommonUtils comutil=new CommonUtilsServices();
					date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
					groupmang.setStatusFlag("1");
					groupmang.setGroupName(groupName);					
					groupmang.setUpdateBy(1);
					groupmang.setEntryDatetime(date);
					groupmang.setModifyDatetime(date);		         
		          try {
		        	  tx = session.beginTransaction();		        	 
		        	  session.save(groupmang);
		        	  locRtnGrpId=groupmang.getGroupCode();
		        	  tx.commit();		        	 
		          } catch (HibernateException ex) {
		        	  if (tx != null) { tx.rollback();}
		        	  ex.printStackTrace();
		        	  locRtnGrpId = 0;
		          } finally {		         
		          }
		} catch (HibernateException ex) {
			ex.printStackTrace();
			locRtnGrpId=-1;
		} finally {
			if(session!=null){
				session.flush();session.clear(); session.close(); session = null;
			}
		}
		return locRtnGrpId;		
	}

	@Override
	public String loginusercondition(UserMasterTblVo userinfo,int flag,int societyId) {
		String result="";		
		UserMasterTblVo userDetails = new UserMasterTblVo();
	    Session session = null;	 
	    String qry="";
	    boolean qrflg = true;
	    log = new Log();
	    Commonutility.toWriteConsole("1 . Login User Name : " + userinfo.getUserName());
	    Commonutility.toWriteConsole("2 . flag : " + flag);
	    Commonutility.toWriteConsole("3 . societyId : " + societyId);
	    log.logMessage(" Login user condition Block Enter", "info", uamDaoServices.class);
	    try {session = HibernateUtil.getSession();
	    
	    	if(flag==1){ // web via called
	    		
	    		if(societyId > 0) {
	    			 qry = " from UserMasterTblVo where (userName=:USER_NAME"
		    		     + " or emailId=:USER_NAME or mobileNo=:USER_NAME)  and "
		    		     +"password=:PASSWORD and statusFlag=:STATUS_FLAG and societyId.societyId=:SOCIETY_ID and groupCode.accessMedia in (1,3) and accessChannel in (1,3)";
	    		} else {
	    			qry = " from UserMasterTblVo where (userName=:USER_NAME"
		    				+ " or emailId=:USER_NAME or mobileNo=:USER_NAME)  and "
		    				+"password=:PASSWORD and statusFlag=:STATUS_FLAG and groupCode.accessMedia in (1,3) and accessChannel in (1,3)";
	    		}
	    		
	    	} else if(flag==2){ // mobile via called
	    		if(societyId > 0){
	    			qry = " from UserMasterTblVo where (userName=:USER_NAME"
		    		        + " or emailId=:USER_NAME or mobileNo=:USER_NAME)  and "
		    		    	+"password=:PASSWORD and statusFlag=:STATUS_FLAG and societyId.societyId=:SOCIETY_ID and groupCode.accessMedia in (2,3) and accessChannel in (2,3)";
	    		} else {
	    			qry = " from UserMasterTblVo where (userName=:USER_NAME"
		    		        + " or emailId=:USER_NAME or mobileNo=:USER_NAME)  and "
		    		    	+"password=:PASSWORD and statusFlag=:STATUS_FLAG and groupCode.accessMedia in (2,3) and accessChannel in (2,3)";
	    		}
	    	} else{	    		
	    		qrflg = false;
	    	}
	    
	    
	    
	    	/*if(flag==1 && societyId > 0){//for web society id found, Committee and admin login, 1 - web, 3 - both , 
	    		Commonutility.toWriteConsole("Society Id : "+societyId);
	    		 qry = " from UserMasterTblVo where (userName=:USER_NAME"
	    		          + " or emailId=:USER_NAME or mobileNo=:USER_NAME)  and "
	    		    		  +"password=:PASSWORD and statusFlag=:STATUS_FLAG and societyId.societyId=:SOCIETY_ID and groupCode.accessMedia in (1,3)";	
	    	} else if(flag==2 && societyId > 0){// for mobile app
	    		Commonutility.toWriteConsole("Society Id : "+societyId);
	    		qry = " from UserMasterTblVo where (userName=:USER_NAME"
	    		        + " or emailId=:USER_NAME or mobileNo=:USER_NAME)  and "
	    		    	+"password=:PASSWORD and statusFlag=:STATUS_FLAG and societyId.societyId=:SOCIETY_ID and groupCode.accessMedia in (2,3)";	
	    	}else{
	    		
	    	}
	    	
	    	if(flag==1 && societyId==0){
	    		qry = " from UserMasterTblVo where (userName=:USER_NAME"
	    				+ " or emailId=:USER_NAME or mobileNo=:USER_NAME)  and "
	    				+"password=:PASSWORD and statusFlag=:STATUS_FLAG and groupCode.accessMedia in (1,3)";
	    	}else if(flag==2 && societyId==0){
	    		qry = " from UserMasterTblVo where (userName=:USER_NAME"
	    		        + " or emailId=:USER_NAME or mobileNo=:USER_NAME)  and "
	    		    	+"password=:PASSWORD and statusFlag=:STATUS_FLAG and groupCode.accessMedia in (2,3)";	
	    	}else{
	    		
	    	}*/
	    	
	      	Commonutility.toWriteConsole("user Name : " + userinfo.getUserName());
	      	Commonutility.toWriteConsole("Password : " + userinfo.getPassword());
	      	Commonutility.toWriteConsole("Query : " + qry);
	      	if(qrflg){
	      	Query query = session.createQuery(qry);
	      	query.setString("USER_NAME", userinfo.getUserName());
	      	if (societyId!=0) {
	        	 query.setInteger("SOCIETY_ID", societyId);
	        }
	      	
	     /* query.setString("MOBILE_NO", userinfo.getUserName()); query.setString("EMAIL_ID", userinfo.getUserName());*/
	      	query.setString("PASSWORD", userinfo.getPassword());
	      	query.setInteger("STATUS_FLAG", 1);
	      	userDetails = (UserMasterTblVo) query.uniqueResult();	
			} else {
				userDetails = null;
			}
			if (userDetails != null) {
				result = "valid";
			} else {
				result = "invalid";
			}
	    } catch (Exception ex) {
	    	log.logMessage("Exception Found loginusercondition() : "+ex, "error", uamDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}log = null;
	    }
	    return result;
}
	
	public boolean UpdateGroupBoth(int delId, String groupName,String flg) {
	 	boolean result = false;
	    Session session = HibernateUtil.getSession();
	    Transaction tx = null;
	    log = new Log();
	    try {
	      tx = session.beginTransaction();
	      String query = "update UserMasterTblVo set groupCode="+groupName+", accessChannel=3 where userId="+delId+"";
	      Query qy = session.createQuery(query);
	      Date date;
		  CommonUtils comutil=new CommonUtilsServices();
		  int i=  qy.executeUpdate();
	      tx.commit();
	      result = true;
	    } catch (Exception ex) {
	      if (tx != null) {
	        tx.rollback();
	      }
	      log.logMessage("Exception Found UpdateGroup() : "+ex, "error", uamDaoServices.class);
	      Commonutility.toWriteConsole("Exception Found UpdateGroup() : "+ex);
	      result = false;
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    	log = null;
	    }
	    return result;
}

	public boolean UpdateGroup(int delId, String groupName) {
	 	boolean result = false;
	    Session session = HibernateUtil.getSession();
	    Transaction tx = null;
	    log = new Log();
	    try {
	      tx = session.beginTransaction();
	      String query = "update UserMasterTblVo set groupCode="+groupName+" where userId="+delId+"";
	      Query qy = session.createQuery(query);
	      Date date;
		  CommonUtils comutil=new CommonUtilsServices();
		  int i=  qy.executeUpdate();
	      tx.commit();
	      result = true;
	    } catch (Exception ex) {
	      if (tx != null) {
	        tx.rollback();
	      }	
	      log.logMessage("Exception Found UpdateGroup() : "+ex, "error", uamDaoServices.class);
	      Commonutility.toWriteConsole("Exception Found UpdateGroup() : "+ex);
	      result = false;
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    	log = null;
	    }		   
	    return result;
}
	
	@Override	
	public String updateUser(int uniId, String username, String fname,String lname, int townshipId,int societyId,String societyname,
		String emailId,String isdno,String mobileNo,int idcardtype,String idcardno,String gender,String dob,int noofFlats,String address1,
		String address2,int country,int state,int city,Integer pin,String occupation,String bloodtype,int familyno,int accesschannel,
		String blockNameList,String flatNameList,String famName,String famMobileNo,String famEmailId,String noofwings,String famISD,String fammemtype,String famprfaccess,String fmbruniqueid, int noOfLogins) {
		 String result="";	
		 	List<UserMasterTblVo> userDetailsList = new ArrayList<UserMasterTblVo>();
			List<UserMasterTblVo> userDetailsListmob = new ArrayList<UserMasterTblVo>();
			 MvpFamilymbrTbl userFamilyData=new MvpFamilymbrTbl();
			 UserMasterTblVo userMaster = new UserMasterTblVo();
			 FlatMasterTblVO flatMst=new FlatMasterTblVO ();
			 CommonUtils comutil=new CommonUtilsServices();
			 Date date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			 UserMasterTblVo userId=new UserMasterTblVo();
			 
		try {
			Commonutility.toWriteConsole("Step 1 : User / Member [UAM] Profile Update [Start]");
			Session session = HibernateUtil.getSession();
			Transaction tx = null;
			tx = session.beginTransaction();
			Commonutility.toWriteConsole("mobileNo : " + mobileNo);
			Commonutility.toWriteConsole("societyId : " + societyId);
			Commonutility.toWriteConsole("uniId : " + uniId);
			String qry2 = "from UserMasterTblVo  where mobileNo='" + mobileNo
					+ "' and societyId=" + societyId + " and "
					+ "statusFlag=1 and userId<>" + uniId + "";
			Query query1 = session.createQuery(qry2);
			userDetailsListmob = query1.list();
			if (userDetailsListmob != null && userDetailsListmob.size() > 0) {
				result = "mobnoExist";
			}
			try {
				if (userDetailsListmob.size() == 0) {
		    	  String qry="update UserMasterTblVo set userName=:USER_NAME,societyId=:SOCIETY_ID,"
		    	  		+ "firstName=:F_NAME ,lastName=:L_NAME,emailId=:EMAIL_ID,isdCode=:ISD_NO,mobileNo=:MOBILE_NO,iVOcradid=:IDCARD_TYPE,"
		    	  		+ "idProofNo=:IDCARD_NO,gender=:GENDER,dob=:DOB,noofflats=:NO_OFFLATS,address1=:ADDRESS1,address2=:ADDRESS2,"
		    	  		+ "countryId=:COUNTRY,stateId=:STATE,cityId=:CITY,pstlId=:PIN,occupation=:OCCUPATION,bloodType=:BLOODGROUP,"
		    	  		+ "membersInFamily=:FAMILY_NO,noofblocks=:NO_OF_WINGS,accessChannel=:ACCESSCHANNEL, numOfLogins=:NUM_OF_LOGINS where userId=:UNIQ_ID";
					Query qy = session.createQuery(qry);
					qy.setString("USER_NAME", username);
					qy.setString("F_NAME", fname);
					qy.setString("L_NAME", lname);
					qy.setInteger("UNIQ_ID", uniId);
					qy.setInteger("SOCIETY_ID", societyId);
					if (emailId != null && !emailId.equalsIgnoreCase("")) {
						qy.setString("EMAIL_ID", emailId);

					} else {
						qy.setString("EMAIL_ID", null);
					}
					qy.setString("ISD_NO", isdno);
					qy.setString("MOBILE_NO", mobileNo);
					if (idcardtype != 0) {
						qy.setInteger("IDCARD_TYPE", idcardtype);

					} else {
						qy.setString("IDCARD_TYPE", null);
					}
					if (idcardno != null && !idcardno.equalsIgnoreCase("")) {
						qy.setString("IDCARD_NO", idcardno);

					} else {
						qy.setString("IDCARD_NO", null);

					}
					qy.setString("GENDER", gender);
					qy.setString("DOB", dob);
					qy.setInteger("NO_OFFLATS", noofFlats);
					qy.setString("NO_OF_WINGS", noofwings);
					qy.setString("ADDRESS1", address1);
					qy.setString("ADDRESS2", address2);		     
					if (country == 0) {
						qy.setString("COUNTRY", null);
					} else {
						qy.setInteger("COUNTRY", country);
					}
					if (state == 0) {
						qy.setString("STATE", null);
					} else {
						qy.setInteger("STATE", state);
					}
					if (city == 0) {
						qy.setString("CITY", null);
					} else {
						qy.setInteger("CITY", city);
					}
					
					if (pin == 0) {
						qy.setString("PIN", null);
					} else {
						qy.setInteger("PIN", pin);
					}
		      
					qy.setString("OCCUPATION", occupation);
					qy.setString("BLOODGROUP", bloodtype);
					if (familyno == 0) {
						qy.setInteger("FAMILY_NO", 1);
					} else {
						qy.setInteger("FAMILY_NO", familyno);
					}
		     
					qy.setInteger("ACCESSCHANNEL", accesschannel);
					qy.setInteger("NUM_OF_LOGINS", noOfLogins);
					
					Commonutility.toWriteConsole("Step 2 : Query : "+qry);
					qy.executeUpdate();
					Commonutility.toWriteConsole("Step 3 : Family Member Detail [UAM] [Start] ");									
					String name[] = famName.split("!_!", -1);
					String mobile[] = famMobileNo.split("!_!", -1);
					String email[] = famEmailId.split("!_!", -1);
					String isdcode[] = famISD.split("!_!", -1);
					String memtyparr[] = fammemtype.split("!_!", -1);
					String prfaccarr[] = famprfaccess.split("!_!", -1);
					String lvrFmbruniqidary [] = null;
					int fmbrunisize = 0;
					if(Commonutility.checknull(fmbruniqueid)){//fmbruniqueid
						lvrFmbruniqidary=fmbruniqueid.split("!_!", -1);
						fmbrunisize=lvrFmbruniqidary.length;
					}
					int mobilesize = mobile.length;
					int emailsize = email.length;
					int namesize = name.length;
					int isdsize = isdcode.length;
					int memtypsize = memtyparr.length;
					int prfaccsize = prfaccarr.length;
					if (famName != null) {
						/*String qry3 = "delete from MvpFamilymbrTbl  where userId=" + uniId + "";
						Query qy3 = session.createQuery(qry3);
						qy3.executeUpdate();*/
						// tx.commit();
						Commonutility.toWriteConsole("Step 4 : Family Member Details Found : "+name.length);
						String lvrQrychldupd2urgtbl = "";
						if (famName != null && famName.contains("!_!")) {			  
							for (int i = 0; i < name.length; i++) {
								userFamilyData = new MvpFamilymbrTbl();
								String familyname = "";
								String mobname = "", isd = "", fmbrmemtype = "", fmbrprfacc = "";								
								String emailname = "", fmbruniquid = "";						
								if(fmbrunisize>=i){
									try{ fmbruniquid = lvrFmbruniqidary[i]; }catch(Exception e){fmbruniquid = "";}finally{}
								} else {
									fmbruniquid = "";
								}
								
								if (namesize >= i) {
									familyname = name[i];
								}
								if (mobilesize >= i) {
									mobname = mobile[i];
								}
								if (emailsize >= i) {
									emailname = email[i];
								}
								if (isdsize >= i) {
									isd = isdcode[i];
								}
								if (memtypsize >= i) {
									fmbrmemtype = memtyparr[i];
								}
								if (prfaccsize >= i) {
									fmbrprfacc = prfaccarr[i];
								}
								if(fmbruniquid!=null && Commonutility.toCheckisNumeric(fmbruniquid)){ // update family table
									 Commonutility.toWriteConsole("Step 5 : Family Member Update block called [Family Unique id found : "+fmbruniquid+"]");
									 String qry3 = "update MvpFamilymbrTbl set fmbrName ='"+familyname+"', fmbrPhNo ='"+mobname+"', fmbrEmail ='"+emailname+"', fmbrIsdCode ='"+isd+"' where fmbrTntId = "+fmbruniquid+"";
									 Query qy3 = session.createQuery(qry3);qy3.executeUpdate();
									 
									 lvrQrychldupd2urgtbl = "update UserMasterTblVo set firstName='"+familyname+"', emailId='"+emailname+"' where fmbrTntId = "+fmbruniquid+"";
									 Query lvrQrychldud = session.createQuery(lvrQrychldupd2urgtbl);lvrQrychldud.executeUpdate();
								
								} else { // new insert family table
									Commonutility.toWriteConsole("Step 5 : Family Member Insert block called ");
									userMaster.setUserId(uniId);								
									userFamilyData.setUserId(userMaster);
									String password = comutil.getRandomval("AZ_09",10);
									userFamilyData.setFmbrPswd(password);
									userFamilyData.setStatus(1);
									if (!familyname.equalsIgnoreCase("")) {
										userFamilyData.setFmbrName(familyname);
									} else {
										userFamilyData.setFmbrName(null);
									}
									if (!mobname.equalsIgnoreCase("")) {
										userFamilyData.setFmbrPhNo(mobname);
									} else {
										userFamilyData.setFmbrPhNo(null);
									}
									if (!emailname.equalsIgnoreCase("")) {
										userFamilyData.setFmbrEmail(emailname); // Future
									} else {
										userFamilyData.setFmbrEmail(null); // purpose
									}
									userFamilyData.setFmbrIsdCode(isd);
									if (!fmbrmemtype.equalsIgnoreCase("")) {
										userFamilyData.setFmbrType(Integer.parseInt(fmbrmemtype));
									}
									if (!fmbrprfacc.equalsIgnoreCase("")) {
										userFamilyData.setFmbrProfAcc(Integer.parseInt(fmbrprfacc));
									}								
									userFamilyData.setEntryDatetime(date);
									session.save(userFamilyData);								
								}								
							}
						} else {
							Commonutility.toWriteConsole("Step 4 : Family Member Details Found - only one");
							if(Commonutility.toCheckisNumeric(fmbruniqueid)) { // update family table
								Commonutility.toWriteConsole("Step 5 : Family Member Update block called [Family Unique id found : "+fmbruniqueid+"]");
								String qry3 = "update MvpFamilymbrTbl set fmbrName ='"+famName+"', fmbrPhNo ='"+famMobileNo+"', fmbrEmail ='"+famEmailId+"', fmbrIsdCode ='"+famISD+"' where fmbrTntId = "+fmbruniqueid+"";
								Query qy3 = session.createQuery(qry3);qy3.executeUpdate();
							 
								lvrQrychldupd2urgtbl = "update UserMasterTblVo set firstName='"+famName+"', emailId='"+famEmailId+"' where fmbrTntId = "+fmbruniqueid+"";
								Query lvrQrychldud = session.createQuery(lvrQrychldupd2urgtbl);lvrQrychldud.executeUpdate();
						
							} else {
								Commonutility.toWriteConsole("Step 5 : Family Member Insert block called [New Memebr]");								
								userMaster.setUserId(uniId);
							    String password = comutil.getRandomval("AZ_09", 10);
								userFamilyData.setUserId(userMaster);
								userFamilyData.setFmbrPswd(password);
								userFamilyData.setFmbrName(famName);
								userFamilyData.setFmbrPhNo(famMobileNo);
								userFamilyData.setFmbrEmail(famEmailId);
								userFamilyData.setFmbrIsdCode(famISD);
								if (Commonutility.toCheckisNumeric(fammemtype)) {
									userFamilyData.setFmbrType(Integer.parseInt(fammemtype));
								} else {
									userFamilyData.setFmbrType(1);
								}
								if (Commonutility.toCheckisNumeric(famprfaccess)) {
									userFamilyData.setFmbrProfAcc(Integer.parseInt(famprfaccess));
								} else {
									userFamilyData.setFmbrProfAcc(0);
								}
								userFamilyData.setEntryDatetime(date);
								userFamilyData.setStatus(1);
								session.save(userFamilyData);
								// tx.commit();
							}					  						
		            }
					}	
						 if(flatNameList!=null && flatNameList.contains("!_!") &&  blockNameList!=null && blockNameList.contains("!_!") ) {
							
							String qry6 = "delete from FlatMasterTblVO  where usrid=:USER_ID";
							Query qy6 = session.createQuery(qry6);
							qy6.setInteger("USER_ID", uniId);
							qy6.executeUpdate();
							if(flatNameList!=null && flatNameList.contains("!_!") && blockNameList!=null && blockNameList.contains("!_!") ){
								Commonutility.toWriteConsole("Step 6.1 : Flat details insert called");
								String spli[] = flatNameList.split("!_!");
								String spli1[] = blockNameList.split("!_!");
								if (flatNameList != null) {
									for (int i = 0; i < spli.length; i++) {
										flatMst = new FlatMasterTblVO();
										String flatname = spli[i];
										String blockname = spli1[i];
										if(Commonutility.checkempty(flatname) && Commonutility.checkempty(blockname)){
											flatMst.setFlatno(flatname);
											flatMst.setWingsname(blockname);
											userId.setUserId(uniId);
											flatMst.setUsrid(userId);
											flatMst.setStatus(1);
											flatMst.setEntryby(1);
											flatMst.setEntryDatetime(date);
											if (i == 0) {
												flatMst.setIvrBnismyself(1);
											} else {
												flatMst.setIvrBnismyself(0);
											}
											session.save(flatMst);
										}
										
									}
								}
							} else {
								if(Commonutility.checkempty(flatNameList) && Commonutility.checkempty(blockNameList)){
									Commonutility .toWriteConsole("Step 6.1 : Single Flat details are found");
									flatMst.setFlatno(flatNameList);
									flatMst.setWingsname(blockNameList);
									userId.setUserId(uniId);
									flatMst.setUsrid(userId);
									flatMst.setStatus(1);
									flatMst.setEntryby(1);
									flatMst.setEntryDatetime(date);
									flatMst.setIvrBnismyself(1);
									session.save(flatMst);
								}
							}
						}else {
							String qry6 = "delete from FlatMasterTblVO  where usrid=:USER_ID";
							Query qy6 = session.createQuery(qry6);
							qy6.setInteger("USER_ID", uniId);
							qy6.executeUpdate();
							if(Commonutility.checkempty(flatNameList) && Commonutility.checkempty(blockNameList)){
								Commonutility .toWriteConsole("Step 6.2 : Single Flat details are found");
								flatMst.setFlatno(flatNameList);
								flatMst.setWingsname(blockNameList);
								userId.setUserId(uniId);
								flatMst.setUsrid(userId);
								flatMst.setStatus(1);
								flatMst.setEntryby(1);
								flatMst.setEntryDatetime(date);
								flatMst.setIvrBnismyself(1);
								session.save(flatMst);
							} else {
								Commonutility .toWriteConsole("Step 6.2 : Flat details not found");
							}
							
						}					
						Commonutility.toWriteConsole("Step 7 : Profile Update Complete [End]");
					tx.commit();
					result = "success";
				} else {
					result = "error";
				}
			} catch (Exception ex) {
				Commonutility.toWriteConsole("Exception Found in uamDaoServices.class updateUser() : " + ex);
				if (tx != null) {
					tx.rollback();
				}
				result = "error";
			} finally {
				if (session != null) {
					session.flush();
					session.clear();
					session.close();
					session = null;
				}
			}
		} catch (Exception ex) {
			Commonutility.toWriteConsole("Exception Found in uamDaoServices.class updateUser() : " + ex);
		}
		  
		return result;
	}
	
	public List<UserMasterTblVo> getUserDetailList_like(String qry) {
		List<UserMasterTblVo> userdata = new ArrayList<UserMasterTblVo>();
		Session session = null;
		Query qy = null;
		try {
			session = HibernateUtil.getSession();			
			qy = session.createQuery(qry);	
			userdata = qy.list();
		} catch (HibernateException ex) {			
			Commonutility.toWriteConsole("Exception found in UuamDaoServices.class getUserDetailList_like() : " + ex);
		} finally {
			if(session!=null){
				session.flush();session.clear(); session.close(); session = null;
			}qy = null;
		}		
		return userdata;
	}
	
    public List<UserMasterTblVo> getUserDetailList_like_limt(String qry, int startrowfrom, int totrow) {
		List<UserMasterTblVo> userdata = new ArrayList<UserMasterTblVo>();
		Session session = null;
		Query qy = null;
		try {
			session = HibernateUtil.getSession();			
			qy = session.createQuery(qry);	
			userdata = qy.setFirstResult(startrowfrom).setMaxResults(totrow).list();
		} catch (HibernateException ex) {			
			Commonutility.toWriteConsole("Exception found in UuamDaoServices.class getUserDetailList_like() : " + ex);
		} finally {
			if(session!=null){
				session.flush();session.clear(); session.close(); session = null;
			}qy = null;
		}		
		return userdata;
	}

	@Override
	public List<FlatMasterTblVO> getUserFlatDeatil(int userId) {
		List<FlatMasterTblVO> flatList=new ArrayList<FlatMasterTblVO>();
		Session session = null;
		log = new Log();
		Query qy = null;
		try {
			session = HibernateUtil.getSession();
			String qry = "From FlatMasterTblVO where  usrid=:UNIQUE_ID and status=:STATUS_FLAG";
			qy = session.createQuery(qry);
			qy.setInteger("UNIQUE_ID", userId);
			qy.setInteger("STATUS_FLAG", 1);			
			flatList =  qy.list();			
		} catch (HibernateException ex) {			
			Commonutility.toWriteConsole("Exception found  uamDaoServices.class getUserFlatDeatil()  : " + ex);
			log.logMessage("Exception found  getUserFlatDeatil : "+ex, "error", uamDaoServices.class);
		} finally {
			log = null;
			if(session!=null){
				session.flush();session.clear(); session.close(); session = null;
			}qy = null;
		}
		return flatList;
	}

	@Override
	public List<MvpFamilymbrTbl> getUserFamilyList(int userId) {
		List<MvpFamilymbrTbl> userFamilyList=new ArrayList<MvpFamilymbrTbl>();
		Session session = null;
		log = new Log();
		Query qy = null;
		try {
			session = HibernateUtil.getSession();
			String qry = "From MvpFamilymbrTbl where  userId=:USER_ID   and status=:STATUS_FLAG";
			qy = session.createQuery(qry);
			qy.setInteger("USER_ID", userId);
			qy.setInteger("STATUS_FLAG", 1);			
			userFamilyList =  qy.list();
		} catch (HibernateException ex) {			
			Commonutility.toWriteConsole("getUserFamilyList Exception : " + ex);
			log.logMessage("Exception   getUserFamilyList : "+ex, "error", uamDaoServices.class);
		} finally {
			if(session!=null){
				session.flush();session.clear(); session.close(); session = null;
			}qy = null;
		}
		return userFamilyList;
	}

	@Override
	public String checkFamilyMember(UserMasterTblVo userinfo,int flag) {
		String result="";		
		MvpFamilymbrTbl userFamily = new MvpFamilymbrTbl();
	    Session session = null;
	    Transaction tx = null;
	    String qry="";
	    Query query = null;
	    try {
	    	session = HibernateUtil.getSession();
	    	tx=session.beginTransaction();
	    	if(flag==1){
	    		qry = " from MvpFamilymbrTbl where ( fmbrEmail=:USER_NAME or fmbrPhNo=:USER_NAME)  and "
	    		  +"fmbrPswd=md5(:PASSWORD) and status=:STATUS_FLAG and userId.groupCode.accessMedia in (1,3)";
	    	}else{
	    		 qry = " from MvpFamilymbrTbl where ( fmbrEmail=:USER_NAME or fmbrPhNo=:USER_NAME)  and "
	   	    		  +"fmbrPswd=md5(:PASSWORD) and status=:STATUS_FLAG and userId.groupCode.accessMedia in (2,3)";
	    	}
	      //Commonutility.toWriteConsole("unam---" + userinfo.getUserName());
	     // Commonutility.toWriteConsole("pwd---" + userinfo.getPassword());
	      //Commonutility.toWriteConsole("Qry---" + qry);

	      query = session.createQuery(qry);
	      query.setString("USER_NAME", userinfo.getUserName());
	     /* query.setString("MOBILE_NO", userinfo.getUserName());
	      query.setString("EMAIL_ID", userinfo.getUserName());*/
	      query.setString("PASSWORD", userinfo.getPassword());
	      query.setInteger("STATUS_FLAG", 1);
	    //  query.setInteger("GROUP_CODE", 3);
	      userFamily = (MvpFamilymbrTbl) query.uniqueResult();
	      	if (userFamily!=null) {
	    	   	result="valid";
	     	}else{
	    	 	result="invalid";
	     	}	      
	    } catch (HibernateException ex) {
	      ex.printStackTrace();
	      log.logMessage("Exception   checkFamilyMember : "+ex, "error", uamDaoServices.class);
	      result="invalid";
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}query = null;
	    }
	    return result;
	}

	@Override
	public MvpFamilymbrTbl checkFamilyLogin(UserMasterTblVo userinfo) {
		MvpFamilymbrTbl userFamily = new MvpFamilymbrTbl();
	    Session session = HibernateUtil.getSession();
	    Transaction tx = null;
	    String qry="";	
	    Query query = null;
	    try {
	    	session = HibernateUtil.getSession();
	    	tx=session.beginTransaction();
	    	qry = " from MvpFamilymbrTbl where ( fmbrEmail=:USER_NAME or fmbrPhNo=:USER_NAME)  and "
	    		  +"fmbrPswd=md5(:PASSWORD) and status=:STATUS_FLAG ";	    			    
	    	query = session.createQuery(qry);
	    	query.setString("USER_NAME", userinfo.getUserName());
	     /* query.setString("MOBILE_NO", userinfo.getUserName());
	      query.setString("EMAIL_ID", userinfo.getUserName());*/
	    	query.setString("PASSWORD", userinfo.getPassword());
	    	query.setInteger("STATUS_FLAG", 1);
	    //  query.setInteger("GROUP_CODE", 3);
	    	userFamily = (MvpFamilymbrTbl) query.uniqueResult();
	    } catch (HibernateException ex) {
	    	Commonutility.toWriteConsole("Exception Found checkFamilyLogin()  : "+ex);
	      log.logMessage("Exception   checkFamilyLogin : "+ex, "error", uamDaoServices.class);
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}query = null;
	    }
	    return userFamily;
	}

	@Override
	public String updateUserProfileExceptAdmin(int uniId, String username,
			String fname, String lname, int idcardtype, String idcardno,
			String gender, String dob, int noofFlats, String address1,
			String address2, int country, int state, int city, int pin,
			String occupation, String bloodtype, int familyno,
			int accesschannel, String blockNameList, String flatNameList,
			String famName, String famMobileNo, String famEmailId,
			String noofwings,String profileImageFileName,String emailid,String famISD,String fammemtype,String famprfaccess, String fmbruniqueid) {
			String result="";	
			List<UserMasterTblVo> userDetailsList = new ArrayList<UserMasterTblVo>();
			List<UserMasterTblVo> userDetailsListmob = new ArrayList<UserMasterTblVo>();
			 MvpFamilymbrTbl userFamilyData=new MvpFamilymbrTbl();
			 UserMasterTblVo userMaster = new UserMasterTblVo();
			 CommonUtils comutil=new CommonUtilsServices();
			 FlatMasterTblVO flatMst=new FlatMasterTblVO ();
			 UserMasterTblVo userId=new UserMasterTblVo();
			 Date date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			 Session session = HibernateUtil.getSession();
		      Transaction tx = null;
		      tx=session.beginTransaction();
		      String qry="";
		     
		      try {
		    	  Commonutility.toWriteConsole("Step 1 : User / Member Profile Update [Start]");
		    	  if(profileImageFileName!=null ){
		    		   qry="update UserMasterTblVo set firstName=:F_NAME,emailId=:EMAIL_ID,"
				    	  		+ "lastName=:L_NAME,iVOcradid=:IDCARD_TYPE,imageNameWeb=:PROFILE_IMAGE,imageNameMobile=:MOBILE_IMAGE,"
				    	  		+ "idProofNo=:IDCARD_NO,gender=:GENDER,dob=:DOB,noofflats=:NO_OFFLATS,address1=:ADDRESS1,address2=:ADDRESS2,"
				    	  		+ "countryId=:COUNTRY,stateId=:STATE,cityId=:CITY,pstlId=:PIN,occupation=:OCCUPATION,bloodType=:BLOODGROUP,"
				    	  		+ "membersInFamily=:FAMILY_NO,noofblocks=:NO_OF_WINGS,accessChannel=:ACCESSCHANNEL where userId=:UNIQ_ID";
		    	  }else{
		    	   qry="update UserMasterTblVo set firstName=:F_NAME,emailId=:EMAIL_ID,"
		    	  		+ "lastName=:L_NAME,iVOcradid=:IDCARD_TYPE,"
		    	  		+ "idProofNo=:IDCARD_NO,gender=:GENDER,dob=:DOB,noofflats=:NO_OFFLATS,address1=:ADDRESS1,address2=:ADDRESS2,"
		    	  		+ "countryId=:COUNTRY,stateId=:STATE,cityId=:CITY,pstlId=:PIN,occupation=:OCCUPATION,bloodType=:BLOODGROUP,"
		    	  		+ "membersInFamily=:FAMILY_NO,noofblocks=:NO_OF_WINGS,accessChannel=:ACCESSCHANNEL where userId=:UNIQ_ID";
		    	  }
		    	 
			Query qy = session.createQuery(qry);
			qy.setString("F_NAME", fname);
			qy.setString("L_NAME", lname);

			qy.setInteger("UNIQ_ID", uniId);
			if (profileImageFileName != null) {
				qy.setString("PROFILE_IMAGE", profileImageFileName);
				qy.setString("MOBILE_IMAGE", profileImageFileName);
			}
			if (idcardtype != 0) {
				qy.setInteger("IDCARD_TYPE", idcardtype);
			} else {
				qy.setString("IDCARD_TYPE", null);
			}
			qy.setString("IDCARD_NO", idcardno);
			qy.setString("GENDER", gender);
			qy.setString("EMAIL_ID", emailid);
			qy.setString("DOB", dob);
			qy.setInteger("NO_OFFLATS", noofFlats);
			qy.setString("NO_OF_WINGS", noofwings);
			qy.setString("ADDRESS1", address1);
			qy.setString("ADDRESS2", address2);
			if (country == 0) {
				qy.setString("COUNTRY", null);
			} else {
				qy.setInteger("COUNTRY", country);
			}
			if (state == 0) {
				qy.setString("STATE", null);
			} else {
				qy.setInteger("STATE", state);
			}
			if (city == 0) {
				qy.setString("CITY", null);
			} else {
				qy.setInteger("CITY", city);
			}
			if (pin == 0) {
				qy.setString("PIN", null);
			} else {
				qy.setInteger("PIN", pin);
			}
			qy.setString("OCCUPATION", occupation);
			qy.setString("BLOODGROUP", bloodtype);

			qy.setInteger("FAMILY_NO", familyno);
			if (accesschannel != 0) {
				qy.setInteger("ACCESSCHANNEL", accesschannel);
			} else {
				qy.setString("ACCESSCHANNEL", null);
			}
			Commonutility.toWriteConsole("Step 2 : Query : "+qry);
			qy.executeUpdate();
			Commonutility.toWriteConsole("Step 3 : Faamily Member Detail [Start] ");
			String name[] = famName.split("!_!", -1);
			String mobile[] = famMobileNo.split("!_!", -1);
			String email[] = famEmailId.split("!_!", -1);
			String isdcode[] = famISD.split("!_!", -1);
			String memtyparr[] = fammemtype.split("!_!", -1);
			String prfaccarr[] = famprfaccess.split("!_!", -1);
			String lvrFmbruniqidary [] = null;
			int fmbrunisize = 0;
			if(Commonutility.checknull(fmbruniqueid)){//fmbruniqueid
				lvrFmbruniqidary=fmbruniqueid.split("!_!", -1);
				fmbrunisize=lvrFmbruniqidary.length;
			}
			int mobilesize = mobile.length;
			int emailsize = email.length;
			int namesize = name.length;
			int isdsize = isdcode.length;
			int memtypsize = memtyparr.length;
			int prfaccsize = prfaccarr.length;
			if (famName != null) {
				/*String qry3 = "delete from MvpFamilymbrTbl  where userId=:USER_ID";
				Query qy3 = session.createQuery(qry3);
				qy3.setInteger("USER_ID", uniId);
				qy3.executeUpdate();*/
					//  tx.commit();
				Commonutility.toWriteConsole("Step 4 : Family Member Details Found : "+name.length);
				String lvrQrychldupd2urgtbl = "";
				if (famName != null && famName.contains("!_!")) {	
										
					for (int i = 0; i < name.length; i++) {
						lvrQrychldupd2urgtbl = "";
						userFamilyData = new MvpFamilymbrTbl();
						String familyname = "";
						String mobname = "", isd = "", fmbrmemtype = "", fmbrprfacc = "";
						String emailname = "", fmbruniquid = "";						
						if(fmbrunisize>=i){
							try{ fmbruniquid = lvrFmbruniqidary[i]; }catch(Exception e){fmbruniquid = "";}finally{}
						} else {
							fmbruniquid = "";
						}
						
						if (namesize >= i) {
							familyname = name[i];
						}
						if (mobilesize >= i) {
							mobname = mobile[i];
						}
						if (emailsize >= i) {
							emailname = email[i];
						}
						if (isdsize >= i) {
							isd = isdcode[i];
						}
						if (memtypsize >= i) {
							fmbrmemtype = memtyparr[i];
						}
						if (prfaccsize >= i) {
							fmbrprfacc = prfaccarr[i];
						}
						
						if(fmbruniquid!=null && Commonutility.toCheckisNumeric(fmbruniquid)){ // update family table
							 Commonutility.toWriteConsole("Step 5 : Family Member Update block called [Family Unique id found : "+fmbruniquid+"]");
							 String qry3 = "update MvpFamilymbrTbl set fmbrName ='"+familyname+"', fmbrPhNo ='"+mobname+"', fmbrEmail ='"+emailname+"', fmbrIsdCode ='"+isd+"' where fmbrTntId = "+fmbruniquid+"";
							 Query qy3 = session.createQuery(qry3);qy3.executeUpdate();
							 
							 lvrQrychldupd2urgtbl = "update UserMasterTblVo set firstName='"+familyname+"', emailId='"+emailname+"' where fmbrTntId = "+fmbruniquid+"";
							 Query lvrQrychldud = session.createQuery(lvrQrychldupd2urgtbl);lvrQrychldud.executeUpdate();
						
						} else { // new insert family table
							Commonutility.toWriteConsole("Step 5 : Family Member Insert block called ");
							userMaster.setUserId(uniId);
							userFamilyData.setUserId(userMaster);
							String password = comutil.getRandomval("AZ_09", 10);
							userFamilyData.setFmbrPswd(password);
							userFamilyData.setStatus(1);
							userFamilyData.setFmbrName(familyname);
							userFamilyData.setFmbrPhNo(mobname);						
							userFamilyData.setFmbrEmail(emailname); // Future purpose
							userFamilyData.setFmbrIsdCode(isd);
							if (!fmbrmemtype.equalsIgnoreCase("")) {
								userFamilyData.setFmbrType(Integer.parseInt(fmbrmemtype));
							}
							if (!fmbrprfacc.equalsIgnoreCase("")) {
								userFamilyData.setFmbrProfAcc(Integer.parseInt(fmbrprfacc));
							}
							userFamilyData.setEntryDatetime(date);
							session.save(userFamilyData);
						}																	
					}
				} else {
					 Commonutility.toWriteConsole("Step 4 : Family Member Details Found - only one");
					if(Commonutility.toCheckisNumeric(fmbruniqueid)){ // update family table
						 Commonutility.toWriteConsole("Step 5 : Family Member Update block called [Family Unique id found : "+fmbruniqueid+"]");
						 String qry3 = "update MvpFamilymbrTbl set fmbrName ='"+famName+"', fmbrPhNo ='"+famMobileNo+"', fmbrEmail ='"+famEmailId+"', fmbrIsdCode ='"+famISD+"' where fmbrTntId = "+fmbruniqueid+"";
						 Query qy3 = session.createQuery(qry3);qy3.executeUpdate();
						 
						 lvrQrychldupd2urgtbl = "update UserMasterTblVo set firstName='"+famName+"', emailId='"+famEmailId+"' where fmbrTntId = "+fmbruniqueid+"";
						 Query lvrQrychldud = session.createQuery(lvrQrychldupd2urgtbl);lvrQrychldud.executeUpdate();
					
					} else {
						Commonutility.toWriteConsole("Step 5 : Family Member Insert block called [New Memebr]");
						userMaster.setUserId(uniId);
		            	String password=comutil.getRandomval("AZ_09", 10);
		            	userFamilyData.setUserId(userMaster);
		            	userFamilyData.setFmbrPswd(password);
		            	userFamilyData.setFmbrName(famName);
		            	userFamilyData.setFmbrPhNo(famMobileNo);
		            	userFamilyData.setFmbrEmail(famEmailId);
		            	userFamilyData.setFmbrIsdCode(famISD);  			            				            	
		            	if(Commonutility.toCheckisNumeric(fammemtype)){
							userFamilyData.setFmbrType(Integer.parseInt(fammemtype));
						}else{
							userFamilyData.setFmbrType(1);
						}
						if(Commonutility.toCheckisNumeric(famprfaccess)){
							userFamilyData.setFmbrProfAcc(Integer.parseInt(famprfaccess));
						}else{
							userFamilyData.setFmbrProfAcc(0);
						}				           
		            	userFamilyData.setEntryDatetime(date);
		            	userFamilyData.setStatus(1);
	          		    session.save(userFamilyData);
					}
					
				    	  	
					// tx.commit();
				}
			}
         	 if(flatNameList!=null && blockNameList!=null) {
				Commonutility.toWriteConsole("Step 6 : Flat details insert called");
				String qry6 = "delete from FlatMasterTblVO  where usrid=:USER_ID";
				Query qy6 = session.createQuery(qry6);
				qy6.setInteger("USER_ID", uniId);
				qy6.executeUpdate();
         		 if((flatNameList!=null && flatNameList.contains("!_!")) && (blockNameList!=null && blockNameList.contains("!_!"))) {
					String spli[] = flatNameList.split("!_!");
					String spli1[] = blockNameList.split("!_!");
					if (flatNameList != null) {
						for (int i = 0; i < spli.length; i++) {
							flatMst = new FlatMasterTblVO();
							String flatname = spli[i];
							String blockname = spli1[i];
							if(Commonutility.checkempty(flatname) && Commonutility.checkempty(blockname) ){
							flatMst.setFlatno(flatname);
							flatMst.setWingsname(blockname);
							userId.setUserId(uniId);
							flatMst.setUsrid(userId);
							flatMst.setStatus(1);
							flatMst.setEntryby(1);
							flatMst.setEntryDatetime(date);
							if (i == 0) {
								flatMst.setIvrBnismyself(1);
							} else {
								flatMst.setIvrBnismyself(0);
							}
							session.save(flatMst);
							}
						}
	            }
	           
				} else {
					if(Commonutility.checkempty(flatNameList) && Commonutility.checkempty(blockNameList) ){
						flatMst.setFlatno(flatNameList);
						flatMst.setWingsname(blockNameList);
						userId.setUserId(uniId);
						flatMst.setUsrid(userId);
						flatMst.setStatus(1);
						flatMst.setEntryby(1);
						flatMst.setEntryDatetime(date);
						flatMst.setIvrBnismyself(1);
						session.save(flatMst);
					}
				}
         	 } else{
         		Commonutility.toWriteConsole("Step 6 : Flat details not found");
         	 }
         	Commonutility.toWriteConsole("Step 7 : Profile Update Complete [End]");
			tx.commit();
			result = "success";

		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();

			}
		        Commonutility.toWriteConsole("Exception   updateUserProfileExceptAdmin :"+ex);
		    	  log.logMessage("Exception   updateUserProfileExceptAdmin : "+ex, "error", uamDaoServices.class);
		        result = "error";
		      } finally {
		    	  if(session!=null){
		    		  session.flush();session.clear(); session.close(); session = null;
					}
		      }
		  
		  
		    return result;
	}

	@Override
	public List<UserMasterTblVo> getloginUserSocietyDetails(String userOrMobile, int adminCode, int siadminCode) {		
		List<UserMasterTblVo> userList= new ArrayList<UserMasterTblVo>();
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			String qry = "From UserMasterTblVo where (userName=:USER_NAME"  + " or emailId=:USER_NAME or mobileNo=:USER_NAME) and "
					+ "groupCode.groupCode<>:ADMIN_CODE and groupCode.groupCode<>:SI_ADMIN_CODE  and  statusFlag=:STATUS_FLAG";
			Query qy = session.createQuery(qry);
			qy.setString("USER_NAME", userOrMobile);
			qy.setInteger("ADMIN_CODE", adminCode);
			qy.setInteger("SI_ADMIN_CODE", siadminCode);
			qy.setString("STATUS_FLAG", "1");
			userList = qy.list();
		} catch (Exception ex) {			
			log.logMessage("Exception   getloginUserSocietyDetails : "+ex, "error", uamDaoServices.class);
		} finally {
			if(session!=null){
				session.flush();session.clear(); session.close(); session = null;
			}
		}		
		return userList;
	}

	@Override
	public FlatMasterTblVO getOneFlatDetail(UserMasterTblVo userMaster) {
		// TODO Auto-generated method stub
		FlatMasterTblVO flatDetail=new FlatMasterTblVO();
		Session session = null;
		Query qy = null;
		try {
			session = HibernateUtil.getSession();
			String qry = "From FlatMasterTblVO where  usrid=:USER_ID order by flatno DESC";
			qy = session.createQuery(qry);
			qy.setInteger("USER_ID", userMaster.getUserId());
			qy.setMaxResults(1);			
			flatDetail = (FlatMasterTblVO) qy.uniqueResult();
		} catch (Exception ex) {			
			log.logMessage("Exception   getUserFamilyList : "+ex, "error", uamDaoServices.class);
		} finally {
			if(session!=null){
				session.flush();session.clear(); session.close(); session = null;
			}
		}
		return flatDetail;
	}

	@Override
	public String getDashboardDetails(int useId) {
		Session session = null;
		String returnparam = "";
		String townshipname = "";
		String societycountdetail = "";
		String residentcountdetail = "";
		Query callStoredProcedure_MYSQL = null;
		try {
			session = HibernateUtil.getSession();
			callStoredProcedure_MYSQL = session.createSQLQuery("CALL PROC_TOWNSHIP (:USER_ID)");
			callStoredProcedure_MYSQL.setInteger("USER_ID", useId);
			List result = callStoredProcedure_MYSQL.list();	
			String lvrFlgMonetown="NODATA";
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				if (obj != null && obj.length >= 4) {
					String lfg = (String) obj[0];
					if (i == 0) {						
						//Commonutility.toWriteConsole("---------------------WEL : ["+lfg+"]");
						if(lfg!=null && lfg.trim().equalsIgnoreCase("1")){// Single Township (Society Name return)
							townshipname += obj[2];
							societycountdetail += obj[3];
							lvrFlgMonetown="SOCY";
						}else{ // townshipname more than one
							townshipname += obj[2];
							societycountdetail += obj[3];
							residentcountdetail += obj[4];
							lvrFlgMonetown="TOWN";							
						}											
					} else {
						if(lfg!=null && lfg.trim().equalsIgnoreCase("1")){
							townshipname += "!_!" + obj[2];
							societycountdetail += "!_!" + obj[3];
							lvrFlgMonetown="SOCY";
							
						}else{
							townshipname += "!_!" + obj[2];
							societycountdetail += "!_!" + obj[3];
							residentcountdetail += "!_!" + obj[4];
							lvrFlgMonetown="TOWN";
							
						}						
					}

				} else {

				}
			}
			Commonutility.toWriteConsole("lvrFlgMonetown : "+lvrFlgMonetown);
			returnparam = lvrFlgMonetown + "<DEL>" + townshipname + "<DEL>" + societycountdetail + "<DEL>"
					+ residentcountdetail+"<DEL>";
		} catch (Exception e) {
			Commonutility.toWriteConsole("Exception Found : " + e);
			returnparam = "ERROR" + "<DEL>" + townshipname + "<DEL>" + societycountdetail + "<DEL>"
					+ residentcountdetail+"<DEL>";
		} finally {
			callStoredProcedure_MYSQL =null;
			session.flush();
			session.clear();
			session.close();
			session = null;
			
		}
		return returnparam;
	}

	@Override
	public boolean updateNotificationStatus(NotificationStatusTblVO notifiobj) {
		// TODO Auto-generated method stub
		Session session = null;
		Query qy = null;
		boolean isupdate=false;
		int returnVal=0;
		Transaction tx = null;		 
		try {
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			String qry = "select count(*) From NotificationStatusTblVO where  mobileNo=:MOBILE_NO";
			qy = session.createQuery(qry);
			qy.setString("MOBILE_NO", notifiobj.getMobileNo());
			qy.setMaxResults(1);			
			returnVal = ((Number) qy.uniqueResult()).intValue();
			if(returnVal>0){
				String sql="update NotificationStatusTblVO set notificationFlag=:NOTIFY_FLAG where mobileNo=:MOBILE_NO";
				qy = session.createQuery(sql);
				qy.setInteger("NOTIFY_FLAG", notifiobj.getNotificationFlag());
				qy.setString("MOBILE_NO", notifiobj.getMobileNo());
				int result=qy.executeUpdate();
				if(result>0){
					isupdate=true;
				}else{
					isupdate=false;
				}
			}else{
	        	  session.save(notifiobj);
	        	  isupdate=true;
	        	  
			}
			if(isupdate){
				tx.commit();
			}
		} catch (Exception ex) {			
			Commonutility.toWriteConsole("updateNotificationStatus HibernateException======" + ex);
			log.logMessage("Exception   updateNotificationStatus : "+ex, "error", uamDaoServices.class);
		} finally {
			if(session!=null){
				session.flush();session.clear(); session.close(); session = null;
			}
		}
		
		return isupdate;
	}

	@Override
	public boolean updateOnlineStatus(int rid, String resetLogin) {
		// TODO Auto-generated method stub
		Session session = null;
		Query qy = null;
		boolean isupdate=false;
		Transaction tx = null;		 
		try {
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
				String sql="update UserMasterTblVo set ivrBnISONLINEFLG=:IS_ONLINE, loggedOut=1 where userId=:USER_ID";
				qy = session.createQuery(sql);
				qy.setInteger("IS_ONLINE", 0);
				qy.setInteger("USER_ID", rid);
				int result=qy.executeUpdate();
				
				if(resetLogin!=null && resetLogin.equalsIgnoreCase("false")){
					sql="update UserMasterTblVo set currentLogins = currentLogins - 1 where currentLogins > 0 and userId=:USER_ID";
					qy = session.createQuery(sql);
					qy.setInteger("USER_ID", rid);
					result=qy.executeUpdate();
				}
				
				if(result>0) {
					isupdate=true;
				} else {
					isupdate=false;
				}
			if(isupdate){
				tx.commit();
			}
		} catch (Exception ex) {			
			Commonutility.toWriteConsole("updateOnlineStatus HibernateException======" + ex);
			log.logMessage("Exception   updateOnlineStatus : "+ex, "error", uamDaoServices.class);
		} finally {
			if(session!=null){
				session.flush();session.clear(); session.close(); session = null;
			}
		}
		
		return isupdate;
	}

	public boolean UpdateGroupsMobiAccess(int delId, String groupName, String flg) {
	 	boolean result = false;
	    Session session = HibernateUtil.getSession();
	    Transaction tx = null;
	    log = new Log();
	    try {
	      tx = session.beginTransaction();
	      String query = "update UserMasterTblVo set groupCode="+groupName+", accessChannel=2 where userId="+delId+"";
	      Query qy = session.createQuery(query);
	      Date date;
		  CommonUtils comutil=new CommonUtilsServices();
		  int i=  qy.executeUpdate();
	      tx.commit();
	      result = true;
	    } catch (Exception ex) {
	      if (tx != null) {
	        tx.rollback();
	      }
	      log.logMessage("Exception Found UpdateGroup() : "+ex, "error", uamDaoServices.class);
	      Commonutility.toWriteConsole("Exception Found UpdateGroup() : "+ex);
	      result = false;
	    } finally {
	    	if(session!=null){
	    		session.flush();session.clear(); session.close(); session = null;
			}
	    	log = null;
	    }
	    return result;
	}
	
	public String UserFlatandwings(int userId){ // it return [ wingsname - flatno, wingsname - flatno,  wingsname - flatno ]
		String lvrRtnstr ="";
		List<FlatMasterTblVO> flatMstList = new ArrayList<FlatMasterTblVO>();
		JSONObject finalJsonarr = null;
		try{
			flatMstList = getUserFlatDeatil(userId);
			if( flatMstList!=null && flatMstList.size()>0){
				FlatMasterTblVO flatObj;
				for (Iterator<FlatMasterTblVO> it = flatMstList.iterator(); it.hasNext();) {
					flatObj = it.next();
					//finalJsonarr = new JSONObject();
					//finalJsonarr.put("userwingsname", flatObj.getWingsname());
					//finalJsonarr.put("userflatno", flatObj.getFlatno());
					//jArray.put(finalJsonarr);
					lvrRtnstr += Commonutility.toCheckNullEmpty(flatObj.getWingsname()) + "-"+Commonutility.toCheckNullEmpty(flatObj.getFlatno())+", ";
				}
				if(Commonutility.checkempty(lvrRtnstr) && lvrRtnstr.endsWith(", ")){
					lvrRtnstr = lvrRtnstr.substring(0, lvrRtnstr.length()-2);
				}
			} else{
				lvrRtnstr = "";
			}
			
		} catch (Exception e) {
			lvrRtnstr = "";
		} finally {
			
		}
		return lvrRtnstr;
	}
	
}
