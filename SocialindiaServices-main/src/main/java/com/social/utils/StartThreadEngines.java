package com.social.utils;

import com.pack.utilitypkg.Commonutility;
import com.social.email.EmailEngineCntrl;
import com.social.sms.SmsEngineCntrl;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StartThreadEngines extends HttpServlet implements Runnable {

  Thread thrd = null;
  private static final long serialVersionUID = 1L;
  static ResourceBundle ivrRsbundelappresorce = ResourceBundle.getBundle("applicationResources");
  /**
   * StartThreadEngines.
   */
  public StartThreadEngines() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * init.
   */
  public void init(ServletConfig config) throws ServletException {    
    thrd = new Thread(this, "StartThreadlEngines-Thread");
    thrd.start();
    try {
    	Commonutility.toWriteConsole("Start 1 : Start DB Version to write");
    	String lvrpropath = new Commonutility().togetFullpath("apiversioninfo.properties").replaceAll("%20", " ");
    	String  lvrMobildbfldr = ivrRsbundelappresorce.getString("external.fldr") + ivrRsbundelappresorce.getString("external.api.fldr")+ ivrRsbundelappresorce.getString("external.dbversion.fldr");				
    	Commonutility.toWriteConsole("lvrpropath [apiversioninfo] : "+lvrpropath);
    	Commonutility.toWriteConsole("lvrMobildbfldr : "+lvrMobildbfldr);
    	File lvrFile = new File(lvrMobildbfldr);
    	
    	if(lvrFile!=null && lvrFile.exists()){
    		File lvrSt[] = lvrFile.listFiles();
    		int lvrLentgh = lvrSt.length;
    		Commonutility.toWriteConsole("Start 2 : lvrSt : "+lvrLentgh);
    		String rst =Commonutility.toWritepropertiesfile(lvrpropath,"currentdbversion",Commonutility.intToString(lvrLentgh));
    	} else {
    		Commonutility.toWriteConsole("Start 2 : Folder not found : "+lvrMobildbfldr);
    	}
    	}catch(Exception e){
    		Commonutility.toWriteConsole("Start -1 : Exception found : "+e);
    	}finally{}
  }

  /**
   * destroy.
   */
  public void destroy() {
    try {
      System.out.println("StartThreadlEngines----destroy()---------Enter");

      // ------------------------------------------SMPP PUSH Sms
      // Stop---------------------------------------------
      // SmsMobileBankingEngineControl.getInstance().stopThread();
      // ------------------------------------------SMPP PUL Sms
      // Stop---------------------------------------------
      // SmppPullMessage.getInstance().stopThread();

      // ---------------------------------------IB Sms Engine
      // Stop-------------------------------------------------------
      SmsEngineCntrl.getInstance().stopThread();
      // ---------------------------------------IB Email Engine
      // Stop-------------------------------------------------------
      System.out.println("[EMAIL Stoped]");
      EmailEngineCntrl.getInstance().stopThread();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @ServletConfig.
   */
  public ServletConfig getServletConfig() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response).
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response).
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // TODO Auto-generated method stub
  }

  @Override
  public void run() {
    try {
//      Thread.sleep(30000);
      System.out.println("StartThreadlEngines-run()----------------Enter");
      System.out.println(this);

      // ------------------------------------------SMPP PUSH Sms
      // Start---------------------------------------------
      // SmsMobileBankingEngineControl.getInstance().startThread();
//      Thread.sleep(5000);
      // ---------------------------------------SMPP PULL Sms
      // Start-------------------------------------------------------
      // SmppPullMessage.getInstance().startThread();
//      Thread.sleep(5000);

      // Start-------------------------------------------------------
     /* SmsEngineCntrl.getInstance().startThread();
     	Thread.sleep(5000);*/
      // ---------------------------------------IB Email Engine
      // Start-------------------------------------------------------
      System.out.println("[EMAIL Started]");
      EmailEngineCntrl.getInstance().startThread();
      Thread.sleep(5000);

    } catch (Exception ex) {
      System.err.println("Exception found in StartThreadlEngines run()");
      ex.printStackTrace();
    } finally {
      System.out.println("StartThreadlEngines-run()----------------Exit");
    }
  }
}
