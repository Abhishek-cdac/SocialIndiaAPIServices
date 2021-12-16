package com.social.email;


import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

import com.sisocial.load.HibernateUtil;
import com.social.email.persistense.EmailConfigTblVo;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailInTblVo;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletResponse;

public class EmailEngineCntrl {

  private String externalPath;
  ExecutorService exeserv = null;
  public static HashMap email_thrdmap = new HashMap();
  private static EmailEngineMain emai_thrdobj = null;
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  public static int threadConf = 0;
  private HttpServletResponse response;
  PrintWriter out = null;
  private EmailConfigTblVo emailconf = new EmailConfigTblVo();
  EmailEngineServices emailHbm = new EmailEngineDaoServices();

  /**
   * Email engine main.
   * 
   * @return email thread obj.
   */
  public static EmailEngineMain getInstance() {
    if (emai_thrdobj == null) {
    	emai_thrdobj = new EmailEngineCntrl().new EmailEngineMain();
    }
    return emai_thrdobj;
  }

  public class EmailEngineMain extends Thread {
    public boolean flag = true;
    EmailFunctions em = null;
    List li = new ArrayList();
    int noofrow = 0;
    int xval = 1;
    int yval = 0;
    int zval = 1;
    String hostname = "";
    String usrname = "";
    String passwrd = "";
    String fromid = "";
    String protocol = "";
    String portno = "";
    String bodyContentip = "";
    Properties props = System.getProperties();// For SMTP
    int fetchvalue = 0;

    public EmailEngineMain() {
    }

    /**
     * For start thread action.
     */
    public void startThread() {
      try {       
        if (emai_thrdobj != null) {
        		Thread.State state = emai_thrdobj.getState();          
          if (state == Thread.State.NEW || state == Thread.State.TERMINATED) {
        	  	emai_thrdobj.flag = true;
        	  	emai_thrdobj.setName("EmailEngineCntrl.EmailEngineMain-Thread");
        	  	emai_thrdobj.start();
        	  	System.out.println("Email Engine Started");
          }
        } else {
        		emai_thrdobj.start();
        }
      } catch (Exception ex) {
    	  System.out.println("Exception found in Email Engine startThread. \n Exception : "+ex);
      }
    }

    /**
     * Sms SMTP configuration.
     * 
     * @return property data.
     */
		public Properties emailFunctionsSmtp() {
			try {
				emailconf = emailHbm.emailConfigval();
				hostname = emailconf.getHostName();
				usrname = emailconf.getUserName();
				passwrd = emailconf.getPassWord();
				fromid = emailconf.getFromId();
				protocol = emailconf.getProtoCol();
				portno = emailconf.getPortNo();
				fetchvalue = emailconf.getFetchConf();
				bodyContentip = emailconf.getBodcontFileIp();
				if(bodyContentip!=null){
					bodyContentip = bodyContentip;
				} else{
					bodyContentip ="";
				}
				// externalPath =
				// attachExternalPath.getExternalPath()+emailconf.getTempPath();
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", hostname);
				props.put("mail.smtp.user", usrname);
				props.put("mail.smtp.password", passwrd);
				props.put("mail.smtp.port", portno);
				props.put("mail.smtp.auth", "true");
				props.put("BodyContentIp", bodyContentip);
				threadConf = 0;
				return props;
			} catch (Exception ex) {
				System.out.println("Exception found in "+getClass().getSimpleName()+".class emailFunctionsSmtp() : "+ex);
				threadConf = 1;
				return props;
			} finally {
			}
		}

    /**
     * Email engine run action.
     */
    public void run() {
      Properties pro = emailFunctionsSmtp();
     // Session lvrSession = HibernateUtil.getSession();
      try {
       
        while (flag) {
          try {

            if (exeserv == null || exeserv.isShutdown()) {
            	exeserv = null;
            	exeserv = Executors.newFixedThreadPool(15);
            } else {
            }
           
            if (pro == null || threadConf == 1) {
            	 System.out.println("EmailEngineCntrl.EmailEngineMain - pro : "+pro);
                 System.out.println("EmailEngineCntrl.EmailEngineMain - threadConf : "+threadConf);
                 System.out.println("EmailEngineCntrl.EmailEngineMain : "+ " [Configuration Reloaded] ");
            	pro = emailFunctionsSmtp();
            }
            while (flag) {
            	/*if(lvrSession.isConnected()){ } else{
            		if(lvrSession!=null){
            			lvrSession.flush();lvrSession.clear();lvrSession.close();
            		}
            		lvrSession = HibernateUtil.getSession();
            	}
            	List<EmailInTblVo> emailinrecord = emailHbm.emailintblval(fetchvalue,lvrSession);*/
            	List<EmailInTblVo> emailinrecord = emailHbm.emailintblval(fetchvalue);
            	int listsiz = emailinrecord.size();
            	if (listsiz > 0 && fetchvalue > 0) {
            		exeserv.execute(new EmailFunctions(emailinrecord, pro, usrname, passwrd, hostname, externalPath));
            		Thread.sleep(3000);
            	} else {
            		break;
            	}
            }
          } catch (Exception ex) {
            System.out.println("Exception Found in "+getClass().getSimpleName()+" : "+ex);
          }
          if (flag) { 
        	  Thread.sleep(5000);
          } else {
            //System.out.println(Thread.currentThread().getName() + " going to Terminated now---------------------" + Thread.currentThread().getState().name());
          }
        }
        //System.out.println(Thread.currentThread().getName()  + " going to Terminated now.................." + Thread.currentThread().getState().name());
				if (email_thrdmap != null) {
					email_thrdmap.clear();
				}
				if (exeserv != null) {
					exeserv.shutdown();
					exeserv = null;
				}
				if (emai_thrdobj != null) {
					Thread.State state = emai_thrdobj.getState();				
					if ((state == Thread.State.RUNNABLE)
							|| (state == Thread.State.TIMED_WAITING)
							|| (state == Thread.State.WAITING)
							|| (state == Thread.State.BLOCKED)) {
						emai_thrdobj.stop();
						emai_thrdobj = null;
					}
				}
      } catch (Exception ex) {
        System.out.println("EmailEngineMain - Exception Found : " + ex);
      } finally {
        /**
         * empty block.
         */
      }

    }

    /**
     * set time to sleep thread.
     * 
     * @param time
     *          .
     */
    public void sleepThread(long time) {
      try {
        if (this != null) {
          Thread.State state = this.getState();
          email_thrdmap.clear();
          if ((state == Thread.State.TERMINATED)
              || (state == Thread.State.BLOCKED) || (state == Thread.State.NEW)) {
            /**
             * Empty block.
             */
          } else {
            Thread.sleep(time);
          }
        }
      } catch (Exception ex) {
        System.out.println("can't sleep timed thread");
      }
    }

    /**
     * To stop thread.
     */
    public void stopThread() {
      try {
        System.out.println(emai_thrdobj
            + " going to Terminated now............stopThread()...Enter");
        flag = false;
        if (exeserv != null) {
          exeserv.shutdown();
          exeserv = null;
        }

        System.out.println("emai_thrdobj------------>" + emai_thrdobj);
        if (emai_thrdobj != null) {
          Thread.State state = emai_thrdobj.getState();
          System.out.println("sms_thrdobj*************>" + state.name());
          if ((state == Thread.State.RUNNABLE)
              || (state == Thread.State.TIMED_WAITING)
              || (state == Thread.State.WAITING)
              || (state == Thread.State.BLOCKED)) {
            emai_thrdobj.stop();
            emai_thrdobj = null;
            System.out.println("Email Engine Stoped");
          }
        }
      } catch (Exception ex) {
        System.out.println("Exception found in IB Email stopThread.");
        ex.printStackTrace();
      }

    }

    /**
     * For thread status.
     */
    public void threadStatus() {
      String status = "NEW";
      try {
        response = ServletActionContext.getResponse();
        out = response.getWriter();
        try {
          System.out.println("emai_thrdobj------------>" + emai_thrdobj);
          if (emai_thrdobj != null) {
            Thread.State state = emai_thrdobj.getState();
            System.out.println("emai_thrdobj*************>" + state);
            System.out.println("emai_thrdobj*************>" + Thread.State.NEW);
            out.print("EMAIL ENGINE Status -------" + state);
          } else {
            status = "NEW";
            out.print("EMAIL ENGINE Status -------" + status);
          }
        } catch (Exception ex) {
          out.print("EMAIL ENGINE Status -------" + status);
          System.out.println("Exception found in EMAIL ENGINE status.");
          ex.printStackTrace();
        }

      } catch (Exception ex) {
        System.out.println("Exception occur==========" + ex);
      }
    }

    /**
     * Get status of thread is stop or run.
     * 
     * @return status.
     */
    public String getthreadStatus() {
      String status = "STOPPED";
      try {
        System.out.println("emai_thrdobj------------>" + emai_thrdobj);
        if (emai_thrdobj != null) {
          Thread.State state = emai_thrdobj.getState();
          System.out.println("emai_thrdobj*************>" + state);
          if (state == Thread.State.TERMINATED || state == Thread.State.NEW) {
            System.out.println("state======if======" + state);
            status = "STOPPED";
          } else {
            System.out.println("state======else======" + state);
            status = "RUNNING";
          }
        } else {
          status = "STOPPED";
        }
      } catch (Exception ex) {
        status = "STOPPED";
        System.out.println("Exception occur==========" + ex);
      }
      return status;
    }

  }
}
