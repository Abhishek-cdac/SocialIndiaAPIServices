package com.social.email.persistense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.social.load.HibernateUtil;

public class EmailEngineDao implements EmailEngine {

  @Override
  public EmailConfigTblVo emailConfigval() {
    EmailConfigTblVo emlconf = new EmailConfigTblVo();
    Session session = HibernateUtil.getSession();
    String qry = "FROM EmailConfigTblVo where actStatus =1";
    Query qy = null;
    try {
    	qy = session.createQuery(qry);
    	emlconf = (EmailConfigTblVo) qy.uniqueResult();
    } catch (HibernateException ex) {
    	ex.printStackTrace();
    } finally {
    	if(session!=null){
    		session.clear();session.close(); session = null; qy = null;
    	}      
    }
    return emlconf;
  }

  @Override
  public List<EmailInTblVo> emailintblval(int fetchvalue) {
    List<EmailInTblVo> emailinflist = new ArrayList<EmailInTblVo>();
    Session session = HibernateUtil.getSession();
    String qry = "FROM  EmailInTblVo where statFlag='P'";
    Transaction tx = null;
    Query qy = null, qw = null;
    try {
      tx = session.beginTransaction();
      qy = session.createQuery(qry);
      qy.setMaxResults(fetchvalue);
      emailinflist = qy.list();
      EmailInTblVo empIdObj;
      for (Iterator<EmailInTblVo> it = emailinflist.iterator(); it.hasNext();) {
        empIdObj = it.next();
        String qr = "update EmailInTblVo set statFlag='SU' where emailId='"+ empIdObj.getEmailId() + "'";
        qw = session.createQuery(qr);
        qw.executeUpdate();       
      }
      tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {
        tx.rollback();
      }
      ex.printStackTrace();
    } finally {
    	session.clear(); session.close(); session = null; qy = null;qw = null;
    }
    return emailinflist;
  }
  
  @Override
  public List<EmailInTblVo> emailintblval(int fetchvalue, Session prmHbsession) {
    List<EmailInTblVo> emailinflist = new ArrayList<EmailInTblVo>();
    //Session session = HibernateUtil.getSession();
    String qry = "FROM  EmailInTblVo where statFlag='P'";
    Transaction tx = null;
    Query qy = null, qw = null;
    try {
      tx = prmHbsession.beginTransaction();
      qy = prmHbsession.createQuery(qry);
      qy.setMaxResults(fetchvalue);
      emailinflist = qy.list();
      EmailInTblVo empIdObj;
      for (Iterator<EmailInTblVo> it = emailinflist.iterator(); it.hasNext();) {
        empIdObj = it.next();
        String qr = "update EmailInTblVo set statFlag='SU' where emailId='"+ empIdObj.getEmailId() + "'";
        qw = prmHbsession.createQuery(qr);
        qw.executeUpdate();       
      }
      tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {
        tx.rollback();
      }
      ex.printStackTrace();
    } finally {    	
    }
    return emailinflist;
  }

  @Override
  public String insertemailOuttblval(EmailOutTblVo emailout) {

    Session session = HibernateUtil.getSession();
    Transaction tx = null;
    try {
    	tx = session.beginTransaction();
    	session.save(emailout);
    	tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {tx.rollback();}
      ex.printStackTrace();
    } finally {
    	if(session!=null){
    		session.flush();session.clear(); session.close(); session = null;
    	}
    }
    return "Successfully Insert";
  }

  @Override
  public String deletemailintblval(int sno) {

    Session session = HibernateUtil.getSession();
    Transaction tx = null;
    Query qy = null;
    try {
      tx = session.beginTransaction();
      qy = session.createQuery("delete from EmailInTblVo where emailId=" + sno + "");
      qy.executeUpdate();
      tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {
        tx.rollback();
      }
      ex.printStackTrace();
    } finally {
    	if(session!=null){
    		session.clear();session.close(); session = null; qy = null;
    	}      
    }
    return "Successfully Deleted";
  }

  @Override
  public String insertemailErrortblval(EmailErrorTblVo emailerror) {
    Session session = HibernateUtil.getSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      session.save(emailerror);
      tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {tx.rollback();}
      ex.printStackTrace();
    } finally {
    	if(session != null){
    		session.clear(); session.close(); session = null;
    	}
    }
    return "Successfully Inserted";
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
      } finally {tx = null;
    	  if(session!=null){
      		session.clear(); session.close(); session = null;
      	}
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public String insertemailIntblval(EmailInTblVo emailin) {

    Session session = HibernateUtil.getSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      session.save(emailin);
      tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {
        tx.rollback();
      }
      ex.printStackTrace();
    } finally {tx = null;
    	if(session!=null){
    		session.clear();session.close();session = null;
    	}
    }
    return "Successfully Insert";
  }

  @Override
  public boolean emailConfigUpdate(EmailConfigTblVo emailconfObj) {

    Session session = HibernateUtil.getSession();
    Transaction tx = null;
    boolean rsult = false;
    try {
      tx = session.beginTransaction();
      String qry = "update EmailConfigTblVo set userName=:UNAME, passWord=:PSWRD, hostName=:HOST_NAME, portNo=:PORT_NO, fromId=:FROM_ID where actStatus=:ACT_STS";
      Query qy = session.createQuery(qry);
      qy.setString("UNAME", emailconfObj.getUserName());
      qy.setString("PSWRD", emailconfObj.getPassWord());
      qy.setString("HOST_NAME", emailconfObj.getHostName());
      qy.setString("PORT_NO", emailconfObj.getPortNo());
      qy.setString("FROM_ID", emailconfObj.getFromId());
      qy.setInteger("ACT_STS", 1);
      qy.executeUpdate();
      tx.commit();
      rsult = true;
    } catch (Exception ex) {
      ex.printStackTrace();
      rsult = false;
    } finally {tx = null;
    	if(session!=null){
    		session.clear();session.close();session = null;
    	}
    }
    return rsult;
  }

  @Override
  public EmailTemplateTblVo emailTemplateData(String emqry) {
    // TODO Auto-generated method stub
    EmailTemplateTblVo emailTemplate = new EmailTemplateTblVo();
    Session session = null;
	Query qy = null;        	
      try {
    	  session = HibernateUtil.getSession();
    	  qy = session.createQuery(emqry);
    	  emailTemplate = (EmailTemplateTblVo) qy.uniqueResult();
      } catch (HibernateException ex) {
    	  ex.printStackTrace();
      } finally {
    	  	if(session!=null){
    	  		session.flush();session.clear();session.close();session = null;
      		}
    	  	 qy = null;
      }
    
    return emailTemplate;
  }

  @Override
  public HashMap<String, String> emailTemplatemap() {    
    HashMap hm = new HashMap();
    List<EmailTemplateParserTblVo> temppareser = new ArrayList<EmailTemplateParserTblVo>();
    Session session =null;
    String qry = "From EmailTemplateParserTblVo";
    Query qy = null;       
      try {
    	  session = HibernateUtil.getSession();
    	  qy = session.createQuery(qry);
    	  temppareser = qy.list();
    	  EmailTemplateParserTblVo empIdObj;
    	  for (Iterator<EmailTemplateParserTblVo> it = temppareser.iterator(); it.hasNext();) {
    		  empIdObj = it.next();
    		  hm.put(empIdObj.getTemplateParser(), empIdObj.getColumnName());
    	  }
      } catch (HibernateException ex) {
    	  ex.printStackTrace();
      } finally {
    	  session.clear(); session.close(); session = null; qy = null;
      }    
    return hm;
  }

  @Override
  public String templateParserChange(String qry, int size, String content) {    
    Session session =null;    
    	try { 
    		session= HibernateUtil.getSession();
			int fsr = 0;
			int sec = 1;
			String tempTagName = "";
			Query qy = session.createQuery(qry);
			List val = qy.list();
			if (size > 1) {
    	  		Iterator itr = val.iterator();
    	  		int inc = 1;
      			while (itr.hasNext()) {    
      				if(inc==1){
				        Object[] aval = (Object[]) itr.next();
						for (int i = 0; i < size; i++) {
							fsr = content.indexOf("[", fsr);
							sec = content.indexOf("]", fsr);
							System.out.println("fsr : "+fsr);
							System.out.println("sec : "+ (sec+1));
							tempTagName = content.substring(fsr, sec + 1);							
							if (aval[i] != null) {
								content = content.replace(tempTagName, aval[i].toString());
							} else {
								content = content.replace(tempTagName, "");
							}													
						}
      				} else {
      					break;
      				}
					inc++;
				}
      }else{
    	  String repText="";
    	  for (int i = 0; i < size; i++) {
              fsr = content.indexOf("[", fsr);
              sec = content.indexOf("]", fsr);
              tempTagName = content.substring(fsr, sec + 1);
              if(val!=null){
              repText=val.toString();
              System.out.println("============repText=========="+repText);
              if(repText!=null && repText.contains("[")){
            	  repText=repText.replace("[", "");
              }
              if(repText!=null && repText.contains("]")){
            	  repText=repText.replace("]", "");
              }
              }
              System.out.println(tempTagName+"============repText=========="+repText);
              content = content.replace(tempTagName, repText.toString());
              
            }
      }
    } catch (Exception ex) {
      System.out.println("Error / Exception found "+getClass().getSimpleName()+".class templateParserChange() : " + ex);
    } finally {
    	if (session!=null) {
    		session.clear(); session.close(); session = null; 
    	}
    }
    return content;
  }

}
