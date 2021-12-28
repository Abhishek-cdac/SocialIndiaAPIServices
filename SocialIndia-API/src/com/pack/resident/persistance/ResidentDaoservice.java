package com.pack.resident.persistance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.commonvo.FlatMasterTblVO;
import com.pack.residentvo.UsrUpldfrmExceFailedTbl;
import com.pack.residentvo.UsrUpldfrmExceTbl;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class ResidentDaoservice implements ResidentDao{

	@Override
	public int saveResidentInfo_int(UserMasterTblVo pIntcmprobj,String famName,String famEmailId,String famMobileNo,String famISD,String fammemtype,String famprfaccess) {
		List<UserMasterTblVo> userDetailsListmob = new ArrayList<UserMasterTblVo>();
		 MvpFamilymbrTbl userFamilyData=new MvpFamilymbrTbl();
		 Date date;
		 CommonUtils comutil=new CommonUtilsServices();
			date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locLbrid=-1;
		int mobCount=0;
		String result="";	
		session = HibernateUtil.getSession();
		Integer societyId = 0;
		try {
			Commonutility.toWriteConsole("Step 1 : ResidentDaoservice saveResidentInfo_int() called");
			
			System.out.println("================================================================================================");
			System.out.println(pIntcmprobj);
			System.out.println("================================================================================================");
			
			if (Commonutility.checkempty(pIntcmprobj.getMobileNo())) {
				Commonutility.toWriteConsole("Step 2 : Upload Excel Found mobile no");
				try {
					societyId = pIntcmprobj.getSocietyId().getSocietyId();
				}catch (Exception e) {
					Commonutility.toWriteConsole("No records presend in database for key provided in excel against SOCIETY KEY column");
					log.logMessage("No records presend in database for key provided in excel against SOCIETY KEY column,\n"+e, "error", ResidentDaoservice.class);
				}
				String qry1 = "from UserMasterTblVo where mobileNo  ='"+pIntcmprobj.getMobileNo()+"' and societyId ="+societyId+" and statusFlag in(1,0,2) ";		      
				Query query1 = session.createQuery(qry1);
				userDetailsListmob = query1.list();		      
				if(userDetailsListmob!=null &&userDetailsListmob.size()>0){
		    	   result="mobnoExist" ;  
				}	
				
				if (userDetailsListmob.size() == 0) {
					try {
						tx = session.beginTransaction();
						session.save(pIntcmprobj);		
						int userId = pIntcmprobj.getUserId();
						Commonutility.toWriteConsole(userId+" : userIduserIduserIduserIduserIduserIduserIduserIduserId");						
						if (famName!=null && famName.contains("!_!")){		            	
							String name[] = famName.split("!_!", -1);
							String isdarr[] = famISD.split("!_!", -1);
							String mobile[] = famMobileNo.split("!_!", -1);
							String email[] = famEmailId.split("!_!", -1);
							String memtyparr[] = fammemtype.split("!_!", -1);
							String prfaccarr[] = famprfaccess.split("!_!", -1);
							int mobilesize = mobile.length;
							int emailsize = email.length;
							int namesize = name.length;
							int isdsize = isdarr.length;
							int memtypesize = memtyparr.length;
							int prfaccsize = prfaccarr.length;
							if (famName != null) {
								for (int i = 0; i < name.length; i++) {
									userFamilyData = new MvpFamilymbrTbl();
									String familyname = "";
									String mobname = "", isdcode = "", membertype = "", prfaccess = "";
									String emailname = "";
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
										isdcode = isdarr[i];
									}
									if (memtypesize >= i) {
										membertype = memtyparr[i];
									}
									if (prfaccsize >= i) {
										prfaccess = prfaccarr[i];
									}			            		
					            		userFamilyData.setUserId(pIntcmprobj);				            		
					            		 String password=comutil.getRandomval("AZ_09", 10);
					            		// String password="1234567890";
					            		userFamilyData.setFmbrPswd(password);
					            		userFamilyData.setStatus(1);
									if (!familyname.equalsIgnoreCase("NaN")) {
										userFamilyData.setFmbrName(familyname);
									} else {
										userFamilyData.setFmbrName(null);
									}
									if (!emailname.equalsIgnoreCase("NaN")) {
										userFamilyData.setFmbrEmail(emailname);
									} else {
										userFamilyData.setFmbrEmail(null);
									}
									if (!mobname.equalsIgnoreCase("NaN")) {
										userFamilyData.setFmbrPhNo(mobname);
									} else {
										userFamilyData.setFmbrPhNo(null);
									}
									if (!isdcode.equalsIgnoreCase("NaN")) {
										userFamilyData.setFmbrIsdCode(isdcode);
									} else {
										userFamilyData.setFmbrIsdCode(null);
									}
									if (!membertype.equalsIgnoreCase("NaN")) {
										userFamilyData.setFmbrType(Integer.parseInt(membertype));
									} else {
										userFamilyData.setFmbrType(null);
									}
									if (!prfaccess.equalsIgnoreCase("NaN")) {
										userFamilyData.setFmbrProfAcc(Integer.parseInt(prfaccess));
									} else {
										userFamilyData.setFmbrProfAcc(null);
									}
					            		/*if(!emailname.equalsIgnoreCase("NaN")){
					            			userFamilyData.setFmbrEmail(emailname);
					            			//Send Email
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
					            	            
					            	          }	
					            		}else{
					            			userFamilyData.setFmbrEmail(null);	
					            		}*/
					            		userFamilyData.setEntryDatetime(date);
					            		session.save(userFamilyData);
					            	}
					             }
					            } else if (famName!=null && !famName.equalsIgnoreCase("null") && !famName.equalsIgnoreCase("")){
					            	String password=comutil.getRandomval("AZ_09", 10);
					            	// String password="1234567890";
					            	userFamilyData.setUserId(pIntcmprobj);
					            	userFamilyData.setFmbrPswd(password);
					            	userFamilyData.setFmbrName(famName);
					            	userFamilyData.setFmbrIsdCode(famISD);
					            	userFamilyData.setFmbrPhNo(famMobileNo);
					            	userFamilyData.setFmbrEmail(famEmailId);
					            	if(Commonutility.toCheckisNumeric(fammemtype)){
					            		userFamilyData.setFmbrType(Integer.parseInt(fammemtype));
					            	}else{
					            		userFamilyData.setFmbrType(1);
					            	}
					            	if(Commonutility.toCheckisNumeric(famprfaccess)){
					            		userFamilyData.setFmbrProfAcc(Integer.parseInt(famprfaccess));
					            	}else{
					            		userFamilyData.setFmbrProfAcc(1);
					            	}				            					            
					            	userFamilyData.setEntryDatetime(date);
					            	userFamilyData.setStatus(1);
				            		session.save(userFamilyData);
					            } else {
					            	
					            	
					            }
							locLbrid=pIntcmprobj.getUserId();
							Commonutility.toWriteConsole("User iD : "+locLbrid);
							tx.commit();
					} catch (Exception ex) {
						Commonutility.toWriteConsole("Exception found in redisentDaoservice Family memebr insert : "+ex);
						if (tx != null) {
							tx.rollback();
						}
					}
				} else {
					Commonutility.toWriteConsole("Step 3 : Upload Excel Not Correct Format");
					locLbrid=-1;
				}
			} else {
				Commonutility.toWriteConsole("Step 2 : Upload Excel Not Correct Format");
				locLbrid=-1;
			}							
		} catch (Exception e) {			
			Commonutility.toWriteConsole("Exception found in redisentDaoservice : "+e);
			log.logMessage("Exception : "+e, "error", ResidentDaoservice.class);
			locLbrid=-1;
			return locLbrid;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			if(tx!=null){tx=null;}
			
		}
		return locLbrid;
	}
	
	@Override
	public boolean updateResidentInfo(String pIntLabrupdQry,String famName,String famMobileNo,String famEmailId,int restid,String famISD,String fammemtyp,String famprfaccess,String fmbruniqueid) {
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pIntLabrupdQry);	
		UserMasterTblVo userMaster = new UserMasterTblVo();
		MvpFamilymbrTbl userFamilyData = new MvpFamilymbrTbl();
		CommonUtils comutil = new CommonUtilsServices();
		Date date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		Session session = null;
		try {
		    Commonutility.toWriteConsole("Step 1 : User / Member [Resident Mgmt] Profile Update [Start]");
			session = HibernateUtil.getSession();
			Transaction tx = null;
			tx = session.beginTransaction();
			try {
		    	Commonutility.toWriteConsole("Step 2 : Family Member Detail [Resident Mgmt] [Start] ");
				String name[] = famName.split("!_!", -1);
				String mobile[] = famMobileNo.split("!_!", -1);
				String email[] = famEmailId.split("!_!", -1);
				String isdcode[] = famISD.split("!_!", -1);
				String memtyparr[] = fammemtyp.split("!_!", -1);
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
					qy3.setInteger("USER_ID", restid);
					qy3.executeUpdate();*/
				//  tx.commit();
					Commonutility.toWriteConsole("Step 3 : Family Member Details Found : "+name.length);
					String lvrQrychldupd2urgtbl = "";
					if (famName != null && famName.contains("!_!")) {
						for (int i = 0; i < name.length; i++) {
							userFamilyData = new MvpFamilymbrTbl();
							String familyname = "";							
							String mobname = "";							
							String emailname = "", isd = "", fmbrmemtype = "", fmbrprfacc = "", fmbruniquid = "";
							
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
								 Commonutility.toWriteConsole("Step 4 : Family Member Update block called [Family Unique id found : "+fmbruniquid+"]");
								 String qry3 = "update MvpFamilymbrTbl set fmbrName ='"+familyname+"', fmbrPhNo ='"+mobname+"', fmbrEmail ='"+emailname+"', fmbrIsdCode ='"+isd+"' where fmbrTntId = "+fmbruniquid+"";
								 Query qy3 = session.createQuery(qry3);qy3.executeUpdate();
								 
								 lvrQrychldupd2urgtbl = "update UserMasterTblVo set firstName='"+familyname+"', emailId='"+emailname+"' where fmbrTntId = "+fmbruniquid+"";
								 Query lvrQrychldud = session.createQuery(lvrQrychldupd2urgtbl);lvrQrychldud.executeUpdate();							
							} else { // new insert family table
								userMaster.setUserId(restid);
								userFamilyData.setUserId(userMaster);
								String password = comutil.getRandomval("AZ_09", 10);
								// String password="1234567890";
								userFamilyData.setFmbrPswd(password);
								userFamilyData.setStatus(1);
								userFamilyData.setFmbrName(familyname);							
								userFamilyData.setFmbrPhNo(mobname);							
								userFamilyData.setFmbrEmail(emailname); // Future  purpose
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
						Commonutility.toWriteConsole("Step 3 : Family Member Details Found - only one");
						if(Commonutility.toCheckisNumeric(fmbruniqueid)){ // update family table
							Commonutility.toWriteConsole("Step 4 : Family Member Update block called [Family Unique id found : "+fmbruniqueid+"]");
							String qry3 = "update MvpFamilymbrTbl set fmbrName ='"+famName+"', fmbrPhNo ='"+famMobileNo+"', fmbrEmail ='"+famEmailId+"', fmbrIsdCode ='"+famISD+"' where fmbrTntId = "+fmbruniqueid+"";
							Query qy3 = session.createQuery(qry3);qy3.executeUpdate();
						 
							lvrQrychldupd2urgtbl = "update UserMasterTblVo set firstName='"+famName+"', emailId='"+famEmailId+"' where fmbrTntId = "+fmbruniqueid+"";
							Query lvrQrychldud = session.createQuery(lvrQrychldupd2urgtbl);lvrQrychldud.executeUpdate();					
						} else {
							Commonutility.toWriteConsole("Step 4 : Family Member Insert block called [New Memebr]");
							userMaster.setUserId(restid);
							String password = comutil.getRandomval("AZ_09", 10);
							// String password="1234567890";
							userFamilyData.setUserId(userMaster);
							userFamilyData.setFmbrPswd(password);
							userFamilyData.setFmbrName(famName);
							userFamilyData.setFmbrPhNo(famMobileNo);
							userFamilyData.setFmbrEmail(famEmailId);
							userFamilyData.setFmbrIsdCode(famISD);
							userFamilyData.setFmbrType(Integer.parseInt(fammemtyp));
							userFamilyData.setFmbrProfAcc(Integer.parseInt(famprfaccess));
							userFamilyData.setEntryDatetime(date);
							userFamilyData.setStatus(1);
							session.save(userFamilyData);
						}												
						// tx.commit();
					}
				}
				tx.commit();
			} catch (HibernateException ex) {
		        if (tx != null) { tx.rollback(); }		       		        
		      } finally {
		        if(session!=null){session.clear(); session.close();session =null;}
		      }
		    } catch (Exception ex) {
		    	  Commonutility.toWriteConsole("===updateResident=Exception=="+ex);
		    } finally {
		    	if(session!=null){session.flush();session.clear();session.close();session=null;}
		     }		  
		    return locRtnSts;
		
	}

	@Override
	public int getInitTotal(String sql) {
		// TODO Auto-generated method stub
		int totcnt = 0;
		Session session = null;
		Query qy = null;
		try {
			session = HibernateUtil.getSession();
			qy = session.createQuery(sql);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {			
			Commonutility.toWriteConsole("Exception found ResidentDaoservices.getInitTotal(): "+ex);
		} finally {
			 if(session!=null){session.clear(); session.close();session =null;} qy = null;
		}
		return totcnt;
	}

	@Override
	public int getTotalFilter(String sqlQueryFilter) {
		// TODO Auto-generated method stub
		int totcnt = 0;
		Session session = HibernateUtil.getSession();
		Query qy = null;
		try {
			session = HibernateUtil.getSession();
			qy = session.createQuery(sqlQueryFilter);
			totcnt = ((Number) qy.uniqueResult()).intValue();			
		} catch (Exception ex) {
			Commonutility.toWriteConsole("Exception found ResidentDaoservices.getTotalFilter(): "+ex);

		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;} qy = null;
		}
		return totcnt;
	}

	@Override
	public int saveResidentFlatInfo_intRtn(FlatMasterTblVO pIntFlatObj) {
		// TODO Auto-generated method stub
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locLbrid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(pIntFlatObj);
			locLbrid=pIntFlatObj.getFlat_id();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			Commonutility.toWriteConsole("Exception found in ResidentDaoservice : "+e);
			log.logMessage("Exception : "+e, "error", ResidentDaoservice.class);
			locLbrid=-1;
			return locLbrid;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			if(tx!=null){tx=null;}
			
		}
		return locLbrid;
	}

	@Override
	public boolean deleteFlatdblInfo(int pIntFlatid) {
		Session session = null;
		Transaction tx = null;
		String locDlqry = null;
		Query q = null;
		try{
			
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			locDlqry="delete from FlatMasterTblVO where usrid = "+pIntFlatid+"";
			q = session.createQuery(locDlqry);			
			q.executeUpdate();
			tx.commit();	
			return true;
		}catch(Exception e){
			if (tx != null) {
				tx.rollback();
			}			
			Commonutility.toWriteConsole("Exception found in ResidentDaoservice.deleteFlatdblInfo() : "+e);
			return false;
		}finally{
			if(session!=null){session.flush();session.clear();session.close();session=null;} q = null;
		}
		
	}

	@Override
	public boolean duplicateResident_id(UserMasterTblVo pIntdupobj) {
		// TODO Auto-generated method stub
			UserMasterTblVo userDetails = null;
			String result="";		 
			int count=0;
			boolean dup_mobno=true;
			int usercount=0;
		    Session session = null;
		    Transaction tx = null;
		    Query query = null;
		    try {
		    	userDetails = new UserMasterTblVo();
		    	session = HibernateUtil.getSession();
		    	String qry = "";	
		    	if(pIntdupobj.getSocietyId()!=null){
		    		qry = " SELECT count(mobileNo)  FROM UserMasterTblVo where mobileNo=:MOBILE_NO"
					          + " and statusFlag=:STATUS_FLAG and societyId=:SOCIETY_ID";	
		    		query = session.createQuery(qry);
			    	query.setString("MOBILE_NO", pIntdupobj.getMobileNo());			    	
			    	query.setInteger("SOCIETY_ID", pIntdupobj.getSocietyId().getSocietyId());
			    	query.setInteger("STATUS_FLAG", 1);
			    	Commonutility.toWriteConsole("----- > "+pIntdupobj.getMobileNo());
			    	Commonutility.toWriteConsole("----- > "+pIntdupobj.getSocietyId().getSocietyId());
		    	} else{
		    		qry = " SELECT count(mobileNo)  FROM UserMasterTblVo where mobileNo=:MOBILE_NO"
					          + " and statusFlag=:STATUS_FLAG";	
		    		query = session.createQuery(qry);
			    	query.setString("MOBILE_NO", pIntdupobj.getMobileNo());
			    	query.setInteger("STATUS_FLAG", 1);
			    	Commonutility.toWriteConsole("----- else> "+pIntdupobj.getMobileNo());
			    	
		    	}
		    	
		    	count = ((Number) query.uniqueResult()).intValue();		  
		    	
		      if(count>0){
		    	  dup_mobno = false;
		      }
		      
		    } catch (HibernateException ex) {
		      ex.printStackTrace();
		      Commonutility.toWriteConsole("Exception Found in residentxlsUploadCreation : "+ex);
		      dup_mobno = false;
		    } finally {
		    	if(session!=null){session.clear(); session.close();session =null;} query = null;
		    }		   
		    return dup_mobno;
	}

	@Override
	public int saveUplFilepath(UsrUpldfrmExceTbl locObjuplFilevo) {		
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locFileid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(locObjuplFilevo);
			locFileid=locObjuplFilevo.getFileId();			
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			Commonutility.toWriteConsole("Exception found in redisentDaoservice : "+e);
			log.logMessage("Exception : "+e, "error", ResidentDaoservice.class);
			locFileid=-1;
			return locFileid;
		} finally {
			if(session!=null){session.clear(); session.close();session=null;}
			if(tx!=null){tx=null;}
			
		}
		return locFileid;
	}

	@Override
	public int saUplErr_Row(UsrUpldfrmExceFailedTbl locObjUplErrvo) {
		Log log=new Log();
		Session session = null;
		Transaction tx = null;
		int locErrid=-1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(locObjUplErrvo);
			locErrid=locObjUplErrvo.getUniqueId();
			Commonutility.toWriteConsole("locErrid:::  "+locErrid);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			Commonutility.toWriteConsole("Exception found in redisentDaoservice : "+e);
			log.logMessage("Exception : "+e, "error", ResidentDaoservice.class);
			locErrid=-1;
			return locErrid;
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}
			if(tx!=null){tx=null;}
			
		}
		return locErrid;
	}
	
	@Override	
	public List<MvpFamilymbrTbl> getUserFamilyList(int userId) {
		Log log=null;
		List<MvpFamilymbrTbl> userFamilyList=new ArrayList<MvpFamilymbrTbl>();
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			log=new Log();
			String qry = "From MvpFamilymbrTbl where userId=:USER_ID  and status=:STATUS_FLAG";
			Query qy = session.createQuery(qry);
			qy.setInteger("USER_ID", userId);
			qy.setInteger("STATUS_FLAG", 1);			
			userFamilyList =  qy.list();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			Commonutility.toWriteConsole("getUserFamilyList Exception : " + ex);
			log.logMessage("Exception   getUserFamilyList : "+ex, "error", ResidentDaoservice.class);
		} finally {
			if(session!=null){session.flush();session.clear();session.close();session=null;}log = null;
		}
		return userFamilyList;
	}

	@Override
	public boolean updateProductOrdertbl(String pIntLabrupdQry) {
		// TODO Auto-generated method stub
		CommonDao locCmdo=new CommonDao();
		boolean locRtnSts=locCmdo.commonUpdate(pIntLabrupdQry);
		return locRtnSts;		
	}
	


}
