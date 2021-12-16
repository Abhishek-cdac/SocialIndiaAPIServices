package com.social.sms.persistense;

import com.social.load.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SmsEngineDao implements SmsEngine {

  @Override
  public SmssmppConfpojo smssmppconfiguration() {
    SmssmppConfpojo smsDetail = new SmssmppConfpojo();
    try {
      Session session = HibernateUtil.getSession();
      String qry = "from SmssmppConfpojo where actflag=1 ";
      Query qy = null;
      try {
        qy = session.createQuery(qry);
        smsDetail = (SmssmppConfpojo) qy.uniqueResult();       
      } catch (HibernateException ex) {
        ex.printStackTrace();
        System.out.println("error occur in SmsEngineDao" + " HibernateException :::: smssmtpconfiguration ::::: :" + ex);
      } finally {
    	  session.flush();session.clear(); session.close(); session =null; qy =null;
      }
    } catch (Exception ex) {
      System.out.println("error occur in SmsEngineDao HibernateException" + " :::: smssmtpconfiguration ::::: :" + ex);
    }
    return smsDetail;
  }

  @Override
  public List<SmsInpojo> smsfunctionList(int fetchrows) {
    List<SmsInpojo> smsconflist = new ArrayList<SmsInpojo>();
    Session session = HibernateUtil.getSession();
    String qry = "FROM  SmsInpojo where smspollFlag='F'";
    Transaction tx = null;
    Query qy = null;
    try {
      tx = session.beginTransaction();
      qy = session.createQuery(qry);
      qy.setMaxResults(fetchrows);
      smsconflist = qy.list();
      SmsInpojo empIdObj;
      for (Iterator<SmsInpojo> it = smsconflist.iterator(); it.hasNext();) {
    	  empIdObj = it.next();
    	  System.out.println("smsfunctionlist----" + fetchrows + " <> update smspollflag=p " + empIdObj.getSmsCardNo());
    	  String qr = "update SmsInpojo set smspollFlag='P' where smsCardNo='" + empIdObj.getSmsCardNo() + "'";
    	  Query qw = session.createQuery(qr);
    	  qw.executeUpdate();
      }
      tx.commit();
    } catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}  
      System.out.println("Exception Found in SmsEngineDao  smsfunctionList :" + ex);
    } finally {
    	session.flush();session.clear();session.close(); session =null; qy = null;
    }
    return smsconflist;
  }
  
  
  @Override
  public List<SmsInpojo> smsfunctionList(int fetchrows, Session pHbsession) {
    List<SmsInpojo> smsconflist = new ArrayList<SmsInpojo>();
    //Session session = HibernateUtil.getSession();
    String qry = "FROM  SmsInpojo where smspollFlag='F'";
    Transaction tx = null;
    Query qy = null;
    try {
      tx = pHbsession.beginTransaction();
      qy = pHbsession.createQuery(qry);
      qy.setMaxResults(fetchrows);
      smsconflist = qy.list();
      SmsInpojo empIdObj;
      for (Iterator<SmsInpojo> it = smsconflist.iterator(); it.hasNext();) {
    	  empIdObj = it.next();
    	  System.out.println("smsfunctionlist----" + fetchrows + " <> update smspollflag=p" + empIdObj.getSmsCardNo());
    	  String qr = "update SmsInpojo set smspollFlag='P' where smsCardNo='"
            + empIdObj.getSmsCardNo() + "'";
    	  Query qw = pHbsession.createQuery(qr);
    	  qw.executeUpdate();
      }
      tx.commit();
    } catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}  
      System.out.println("Exception Found in SmsEngineDao  smsfunctionList :" + ex);
    } finally {
    	//session.clear();session.close(); session =null; qy = null;
    }
    return smsconflist;
  }
  

  @Override
  public String insertsmsout(SmsOutpojo smsout) {
    Session session = HibernateUtil.getSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      session.save(smsout);
      tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {
        tx.rollback();
      }
      System.out.println("error occur in SmsEngineDao HibernateException " + ":::: insertsmsout ::::: :" + ex);
      ex.printStackTrace();
    } finally {
    	session.flush();session.clear();session.close(); session = null;
    }
    return "Successfully Insert";
  }

  @Override
  public String deletesmsout(SmsOutpojo smsout) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			Query qy = session.createQuery("delete from SmsInpojo where smsCardNo=" + smsout.getCardNo() + "");
			qy.executeUpdate();
			tx.commit();
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
			System.out.println("error occur in SmsEngineDao HibernateException"
					+ "  :::: deletesmsout ::::: :" + ex);
		} finally {
			session.clear();
			session.close();
			session = null;
		}
		return "Successfully Insert";
	}

  @Override
  public String insertsmserror(SmsErrorpojo smserror) {
    Session session = HibernateUtil.getSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      session.save(smserror);
      tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {
        tx.rollback();
      }
      System.out.println("error occur in SmsEngineDao"
          + " HibernateException :::: insertsmserror ::::: :" + ex);
      ex.printStackTrace();
    } finally {
    	session.flush();session.clear();session.close(); session = null;
    }
    return "Successfully Insert";
  }

  @Override
  public String deletesmserror(SmsErrorpojo smserror) {
    Session session = HibernateUtil.getSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      Query qy = session.createQuery("delete from SmsInpojo "
          + " where smsCardNo=" + smserror.getCardNo() + "");
      qy.executeUpdate();
      tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {
        tx.rollback();
      }
      ex.printStackTrace();
      System.out.println("error occur in SmsEngineDao "
          + "HibernateException :::: deletesmserror ::::: :" + ex);
    } finally {
    	session.flush();session.clear();session.close(); session = null;
    }
    return "Successfully Insert";
  }

  @Override
  public void reportFlgUpdate(String qry) {
    try {
      Session session = HibernateUtil.getSession();
      Transaction tx = null;
      try {
        tx = session.beginTransaction();
        Query qy = session.createQuery(qry);
        qy.executeUpdate();
        tx.commit();
      } catch (HibernateException ex) {
        if (tx != null) {
          tx.rollback();
        }
        ex.printStackTrace();
        System.out.println("error occur in SmsEngineDao "
            + " HibernateException :::: reportFlgUpdate::::: :" + ex);
      } finally {
    	  session.clear(); session.close(); session = null;
      }
    } catch (Exception ex) {
      System.out.println("error occur in SmsEngineDao "
          + " HibernateException :::: reportFlgUpdate::::: :" + ex);
    }
  }

  @Override
  public void commonUpdate(String sql) {
    try {
      Session session = HibernateUtil.getSession();
      Transaction tx = null;
      try {
        tx = session.beginTransaction();
        Query qy = session.createQuery(sql);
        qy.executeUpdate();
        tx.commit();
      } catch (HibernateException ex) {
        if (tx != null) {
          tx.rollback();
        }
        ex.printStackTrace();
        System.out.println("error occur in SmsEngineDao"
            + " HibernateException :::: updateLoginDateTime::::: :" + ex);
      } finally {
    	  session.clear(); session.close(); session = null;
      }
    } catch (Exception ex) {
      System.out.println("error occur in SmsEngineDao "
          + "HibernateException :::: updateLoginDateTime::::: :" + ex);
    }
  }

  @Override
  public SmssmppConfpojo smsConfigTableData() {

    SmssmppConfpojo smsconf = new SmssmppConfpojo();
    Session session = HibernateUtil.getSession();
    String qry = "FROM SmssmppConfpojo where actflag =1";
    try {
      Query qy = session.createQuery(qry);
      smsconf = (SmssmppConfpojo) qy.uniqueResult();
    } catch (HibernateException ex) {
      ex.printStackTrace();
    } finally {
    	session.clear(); session.close(); session = null;
    }
    return smsconf;
  }

  @Override
  public boolean smsConfigUpdate(SmssmppConfpojo smsconfObj) {

    Session session = HibernateUtil.getSession();
    Transaction tx = null;
    boolean rsult = false;
    try {
      tx = session.beginTransaction();
      String qry = "update SmssmppConfpojo set httpUrl=:HTTP_URL, userName=:UNAME, "
          + " senderName=:SENDER_NAME, cdmaSender=:CDMASENDER_NAME, providerName=:PROVIDER_NAME, "
          + " systemid=:SYS_ID,  password=:PSWRD, serverip=:SRVR_IP, port=:PORT_NO, "
          + " configtype=:CONFIG_TYP where actflag=:ACT_FLG";
      Query qy = session.createQuery(qry);
      qy.setString("HTTP_URL", smsconfObj.getHttpUrl());
      qy.setString("UNAME", smsconfObj.getUserName());
      qy.setString("SENDER_NAME", smsconfObj.getSenderName());
      qy.setString("CDMASENDER_NAME", smsconfObj.getCdmaSender());
      qy.setString("PROVIDER_NAME", smsconfObj.getCdmaSender());
      qy.setString("SYS_ID", smsconfObj.getCdmaSender());
      qy.setString("PSWRD", smsconfObj.getPassword());
      qy.setString("SRVR_IP", smsconfObj.getServerip());
      qy.setInteger("PORT_NO", smsconfObj.getPort());
      qy.setInteger("CONFIG_TYP", smsconfObj.getConfigtype());
      qy.setInteger("ACT_FLG", 1);
      qy.executeUpdate();
      tx.commit();
      rsult = true;
    } catch (Exception ex) {
      ex.printStackTrace();
      rsult = false;
    } finally {
    	session.clear(); session.close(); session = null;
    }
    return rsult;
  }

  @Override
  public boolean insertSmsInTable(SmsInpojo smsinObj) {

    Session session = HibernateUtil.getSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      session.save(smsinObj);
      tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {
        tx.rollback();
      }
      ex.printStackTrace();
      System.out.println("Error occur in smsinsert HibernateException : " + ex);
      return false;
    } finally {
    	session.clear(); session.close(); session = null;
    }

    return true;
  }

  @Override
  public SmsTemplatepojo smsTemplateData(String qry) {
    // TODO Auto-generated method stub
    SmsTemplatepojo smsTemplate = new SmsTemplatepojo();
    try {
      Session session = HibernateUtil.getSession();
      try {
        Query qy = session.createQuery(qry);
        smsTemplate = (SmsTemplatepojo) qy.uniqueResult();
      } catch (HibernateException ex) {
        ex.printStackTrace();
      } finally {
    	  session.clear(); session.close(); session = null;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return smsTemplate;
  }

  @Override
  public HashMap<String, String> smsTemplateMap() {
    // TODO Auto-generated method stub
    HashMap hm = new HashMap();
    List<SmsTemplateParserpojo> smstempParse = new ArrayList<SmsTemplateParserpojo>();
    try {
      Session session = HibernateUtil.getSession();
      String qry = "From SmsTemplateParserpojo";
      try {
        Query qy = session.createQuery(qry);
        smstempParse = qy.list();
        SmsTemplateParserpojo smsObj;
        for (Iterator<SmsTemplateParserpojo> it = smstempParse.iterator(); it
            .hasNext();) {
          smsObj = it.next();
          hm.put(smsObj.getTemplateParser(), smsObj.getColumnName());
        }
      } catch (HibernateException ex) {
        ex.printStackTrace();
      } finally {
    	  session.clear(); session.close(); session = null;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return hm;
  }

  @Override
  public String smsTemplateParserChange(String qry, int size, String content) {
    // TODO Auto-generated method stub
	  	Session session = HibernateUtil.getSession();
		int fsr = 0;
		int sec = 1;
		String tempTagName;
		Query qy = null; 
    try {
    	System.out.println("Step 1 : smsTemplateParserChange block called.");
    		qy = session.createQuery(qry);
			List val = qy.list();
			if (size > 1) {
				Iterator itr = val.iterator();
				while (itr.hasNext()) {
					Object[] aval = (Object[]) itr.next();
					for (int i = 0; i < size; i++) {
						fsr = content.indexOf("[", fsr);
						sec = content.indexOf("]", fsr);
						tempTagName = content.substring(fsr, sec + 1);
						content = content.replace(tempTagName, aval[i].toString());
					}
				}
			}else{
				String repText="";
				for (int i = 0; i < size; i++) {
	              fsr = content.indexOf("[", fsr);
	              sec = content.indexOf("]", fsr);
	              tempTagName = content.substring(fsr, sec + 1);
	              if(val!=null){
	              repText=val.toString();
	              //System.out.println("============repText=========="+repText);
	              if(repText!=null && repText.contains("[")){
	            	  repText=repText.replace("[", "");
	              }
	              if(repText!=null && repText.contains("]")){
	            	  repText=repText.replace("]", "");
	              }
	              }
	              //System.out.println(tempTagName+"============repText=========="+repText);
	              content = content.replace(tempTagName, repText.toString());              
				}
      }
			System.out.println("Step 2 : smsTemplateParserChange block called [End].");
    } catch (Exception ex) {
      //System.out.println("error occur in sms HibernateException " + " :::: smstemplateParserChange ::::: :" + ex);
    } finally {
    	session.flush();session.clear();session.close(); session = null;
    }   
    return content;
  }

}
