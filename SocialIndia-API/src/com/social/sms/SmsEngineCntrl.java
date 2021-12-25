package com.social.sms;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.jsmpp.extra.SessionState;

import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmssmppConfpojo;

public class SmsEngineCntrl {
  public static HashMap sms_thrdmap = new HashMap();
  private static SmsEngineMain sms_thrdobj = null;
  int mailProcLimit = 0;
  public static int threadCnt = 0;
  public static int smsThreadConf = 0;
  PrintWriter out = null;
  private HttpServletResponse response;
  SmsEngineServices smseng = new SmsEngineDaoServices();
  private SmssmppConfpojo smsconf = new SmssmppConfpojo();
  ServletContext context;
  String locadr = null;
  public static boolean lvrEnginstrt = false;
  /**
   * Sms engine main thread execution. for sms engine start.
   */
  public void execute() {
    try {
      context = ServletActionContext.getServletContext();
      response = ServletActionContext.getResponse();
      locadr =  ServletActionContext.getRequest().getLocalAddr();//for local sys doesn't start
      out = response.getWriter();
      System.out.println("locadr ::::::::::: "+ locadr);
      if (SmsEngineCntrl.sms_thrdobj == null) {
       if((locadr!=null && !locadr.startsWith("192.") && !locadr.startsWith("0.")) || lvrEnginstrt){//for local sys doesn't start
    	   sms_thrdobj = new SmsEngineCntrl.SmsEngineMain();
           sms_thrdobj.flag = true;
           sms_thrdobj.setName("SmsEngineMainThread-1");
           sms_thrdobj.start();
           out.print("SMS Engine Started");
       } else {
    	   System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<Not Started on local or IP - 1 >>>>>>>>>>>>> : " + locadr);
       }
    	 
      } else {
        Thread.State state = sms_thrdobj.getState();
        if (state == Thread.State.BLOCKED || state == Thread.State.RUNNABLE || state == Thread.State.WAITING || state == Thread.State.TIMED_WAITING) {
        	sms_thrdobj.stopThread();
        	out.print("SMS Engine Stopped");
        } else if (state == Thread.State.NEW || state == Thread.State.TERMINATED) {
        	if((locadr!=null && !locadr.startsWith("192.") && !locadr.startsWith("0.")) || lvrEnginstrt){//for local sys doesn't start
        		sms_thrdobj.flag = true;
        		sms_thrdobj.setName("SmsEngineCntrl.SmsEngineMain-Thread");
        		sms_thrdobj.start();
        		out.print("SMS Engine Started");
        	} else {        		
        		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<Not Started on local or IP - 2 >>>>>>>>>>>>> : " + locadr);
        	}
        } else {
        	out.print("No Action Done Current State Is : " + state);
        }
      }
    } catch (Exception ex) {
    	System.out.println("Excception Found in "+getClass().getSimpleName()+".class execute() : " + ex);
    } finally {
      out.close();
    }
  }

	/**
	 * SmsEngineCntrl getInstance method.
	 * 
	 * @return sms thread object.
	 */
	public static SmsEngineMain getInstance() {
		if (sms_thrdobj == null) {
			sms_thrdobj = new SmsEngineCntrl().new SmsEngineMain();
		}
		return sms_thrdobj;
	}

  public class SmsEngineMain extends Thread {
    public boolean flag = true;
    ExecutorService exeserv = null;
    SmsSmppFunction sbf = new SmsSmppFunction();
    SmsSmppFunction em = null;
    SmsFunction emh = null;
    List li = new ArrayList();
    int noofrow = 0;
    int xcnt = 1;
    int ycnt = 0;
    int zcnt = 1;
    String httpurl = "";
    String usrname = "";
    String passwrd = "";
    String sender = "";
    String cdmaSender = "";
    String providername = "";
    int fetchrows = 0;
    int fetchvalue = 0;

		private SmsEngineMain() {
		}

		/**
		 * Start thread.
		 */
		public void startThread() {
			try {
				if (sms_thrdobj != null) {
					//RPK - LOCAL
					try { if(ServletActionContext.getRequest()!=null){
						locadr =  ServletActionContext.getRequest().getLocalAddr();//for local sys doesn't start
					} } catch (Exception ee){/*System.out.println("Excception Found in "+getClass().getSimpleName()+".class startThread() 1111 : " + ee);*/}
					 
					Thread.State state = sms_thrdobj.getState();
					if (state == Thread.State.NEW || state == Thread.State.TERMINATED) {
						if((locadr!=null && !locadr.startsWith("192.") && !locadr.startsWith("0.")) || lvrEnginstrt){//for local sys doesn't start
							sms_thrdobj.flag = true;
							sms_thrdobj.setName("SmsEngineCntrl.SmsEngineMain-Thread");
							sms_thrdobj.start();
							System.out.println("SMS Engine Started");
						} else {							
							System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<Not Started on local or IP - 3 >>>>>>>>>>>>> : " + locadr);
						}
					}
				}
			} catch (Exception ex) {
				System.out.println("Excception Found in "+getClass().getSimpleName()+".class startThread() : " + ex);
			}
    }

    /**
     * Sms configuration fucntion.
     * 
     * @return sms configuration details.
     */
    public SmssmppConfpojo smsFunctionsSmtp() {
      try {
				smsconf = smseng.smssmppconfiguration();
				fetchrows = smsconf.getFetchcount();
				smsThreadConf = 1;
				return smsconf;
      } catch (Exception ex) {
    	  System.out.println("Excception Found in "+getClass().getSimpleName()+".class smsFunctionsSmtp() : " + ex);
        return smsconf;
      } finally {
        /**
         * empty.
         */
      }
    }

    /**
     * Sms engine run.
     */
    public void run() {
			SmssmppConfpojo pro = null;
			//Session lvrHbsession = null;
			final Logger write = Logger.getLogger(SmsEngineCntrl.class);
			try {
				//lvrHbsession = HibernateUtil.getSession();
				System.out.println("{SmsEngineCntrl.SmsEngineMain} :: SMS Engine [started] ::");
				while (flag) {
					try {
						if (exeserv == null || exeserv.isShutdown()) {
							exeserv = null;
							exeserv = Executors.newFixedThreadPool(15);
						}
						if (pro == null || smsThreadConf == 0) { // change 0 to // 1
							System.out .println("SmsEngineCntrl.SmsEngineMain :: [Configuration Reloaded]");
							pro = smsFunctionsSmtp();
						}
						while (flag) {
							/*if(lvrHbsession.isConnected()){ 
								
							} else {
			            		if(lvrHbsession!=null){
			            			lvrHbsession.flush();lvrHbsession.clear();lvrHbsession.close(); lvrHbsession = null;
			            		}
			            		lvrHbsession = HibernateUtil.getSession();
			            	}
							List<SmsInpojo> smsinrecord = smseng.smsfunctionList(fetchrows,lvrHbsession);*/
							List<SmsInpojo> smsinrecord = smseng.smsfunctionList(fetchrows);
							int listsiz = smsinrecord.size();
							if (listsiz > 0 && fetchrows > 0) {
								if (pro.getConfigtype() == 2) {
									exeserv.execute(new SmsSmppFunction(smsinrecord, pro));
									Thread.sleep(4000);
								} else {
									exeserv.execute(new SmsFunction(smsinrecord, pro));
									Thread.sleep(4000);
								}
							} else {
								break;
							}

						}
					} catch (Exception ex) {
						System.out.println("Exception Found "+getClass().getSimpleName()+" Thread While loop : "+ex);
					}
					if (flag) {
						Thread.sleep(10000);
					} else {
						//System.out.println(Thread.currentThread().getName() + " :: [Thread Going to Terminated Now] :: " + Thread.currentThread().getState().name());
					}
				}

				if (sms_thrdmap != null) {
					sms_thrdmap.clear();
				}

				if (exeserv != null) {
					exeserv.shutdown();
					exeserv = null;
				}

				if (sbf != null && sbf.session != null) {
					sbf.session.unbindAndClose();
					sbf.session = null;
				}
				if (sms_thrdobj != null) {
					Thread.State state = sms_thrdobj.getState();
					if ((state == Thread.State.RUNNABLE) || (state == Thread.State.TIMED_WAITING) || (state == Thread.State.WAITING) || (state == Thread.State.BLOCKED)) {
						sms_thrdobj.stop();
						sms_thrdobj = null;
					}
				}
      } catch (Exception ex) {
    	  	System.out.println("Excception Found in "+getClass().getSimpleName()+".class : " + ex);
      } finally {
        /**
         * Empty.
         */
      }

    }

    /**
     * Check thread state for sleep.
     * 
     * @param time
     *          .
     */
		public void sleepThread(long time) {
			try {
				if (this != null) {
					Thread.State state = this.getState();
					sms_thrdmap.clear();
					if ((state == Thread.State.TERMINATED)
							|| (state == Thread.State.BLOCKED)
							|| (state == Thread.State.NEW)) {
						/**
						 * Empty.
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
     * Stop thread.
     */
    public void stopThread() {
      try {       
				flag = false;
				if (exeserv != null) {
					exeserv.shutdown();
					exeserv = null;
				}

				if (sbf != null && sbf.session != null) {
					sbf.session.unbindAndClose();
					sbf.session = null;
				}
				if (sms_thrdobj != null) {
					Thread.State state = sms_thrdobj.getState();
					if ((state == Thread.State.RUNNABLE)
							|| (state == Thread.State.TIMED_WAITING)
							|| (state == Thread.State.WAITING)
							|| (state == Thread.State.BLOCKED)) {
						sms_thrdobj.stop();
						sms_thrdobj = null;
						System.out.println(" SMS Engine Stoped");
					}
				}
      } catch (Exception ex) {        
        System.out.println("Excception Found in "+getClass().getSimpleName()+".class SMS stopThread : " + ex);        
      }

    }

    /**
     * For NEW thread sms staus.
     */
    public void threadStatus() {
      String status = "NEW";  
        try {
        	response = ServletActionContext.getResponse();
            out = response.getWriter();
          if (sms_thrdobj != null) {
            Thread.State state = sms_thrdobj.getState();            
            out.print("SMS Status -------" + state);
            if (SmsSmppFunction.session != null) {
              out.print("<BR>SmsEngineCntrl.SmsEngineMain-Thread SMPP Bind Status :: \n isBound="
                  + SmsSmppFunction.session.getSessionState().isBound()
                  + "\n SessionId="
                  + SmsSmppFunction.session.getSessionId()
                  + "\n SessionState.CLOSED="
                  + SmsSmppFunction.session.getSessionState().equals(
                      SessionState.CLOSED));
            } else {
              out.print("<BR>SmsEngineCntrl.SmsEngineMain-Thread SMPP Bind Status :: "
                  + SmsSmppFunction.session);
            }
          } else {
            status = "NEW";
            out.print("SMS Status -------" + status);
          }
        } catch (Exception ex) {
          out.print("SMS Status -------" + status);
          System.out.println("Exception found in SMS PUSH stopThread. : "+ex);
        }     
    }

    /**
     * For stopped thread sms status.
     * 
     * @return status.
     */
    public String ibSmsThreadStatus() {
      String status = "STOPPED";      
        try {          
          if (sms_thrdobj != null) {
            Thread.State state = sms_thrdobj.getState();            
            if (state == Thread.State.TERMINATED || state == Thread.State.NEW) {
              status = "STOPPED";
            } else {
              status = "RUNNING";
            }
          } else {
            status = "STOPPED";
          }
        } catch (Exception ex) {
          status = "STOPPED";
          System.out.println("Excception Found in "+getClass().getSimpleName()+".class ibSmsThreadStatus() : " + ex);
        }     
      return status;
    }
  }

  /**
   * Thread new status.
   */
	public void threadStatus() {
		String status = "NEW";
		try {
			response = ServletActionContext.getResponse();
			out = response.getWriter();
			if (sms_thrdobj != null) {
				Thread.State state = sms_thrdobj.getState();
				out.print("SMS Status -------" + state);
			} else {
				status = "NEW";
				out.print("SMS Status -------" + status);
			}
		} catch (Exception ex) {
			out.print("SMS Status -------" + status);
			 System.out.println("Excception Found in "+getClass().getSimpleName()+".class threadStatus() : " + ex);
		}

	}

  public SmssmppConfpojo getSmsconf() {
    return smsconf;
  }

  public void setSmsconf(SmssmppConfpojo smsconf) {
    this.smsconf = smsconf;
  }
}
