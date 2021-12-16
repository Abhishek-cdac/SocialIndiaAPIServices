package com.social.sms;

import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsErrorpojo;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmsOutpojo;
import com.social.sms.persistense.SmssmppConfpojo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;

import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * sms SMPP function.
 * 
 * @author PL0043
 *
 */
public class SmsSmppFunction extends Thread {

  private static TimeFormatter timeFormatter = new AbsoluteTimeFormatter();
  public int smsstatus = 0;
  Properties props = System.getProperties();// For SMTP
  private SmssmppConfpojo smsconf = new SmssmppConfpojo();
  private SmsErrorpojo smserror = new SmsErrorpojo();
  private SmsOutpojo smsout = new SmsOutpojo();
  private List lis = new ArrayList();

  SmsEngineServices smseng = new SmsEngineDaoServices();
  CommonUtils common = new CommonUtilsDao();

  static SMPPSession session = null;
  GeneralDataCoding dataCoding = new GeneralDataCoding(false, true,
      MessageClass.CLASS1, Alphabet.ALPHA_DEFAULT);
  ESMClass esmClass = new ESMClass();
  private static TypeOfNumber ton1 = TypeOfNumber.INTERNATIONAL;
  private static NumberingPlanIndicator npi1 = NumberingPlanIndicator.ISDN;
  private static RegisteredDelivery registeredDelivery = new RegisteredDelivery();
  private static String schedule_delivery_time = null;

  public SmsSmppFunction() {

  }

  /**
   * SMPP configure action.
   * 
   * @param list
   *          .
   * @param smscon
   *          .
   */
  public SmsSmppFunction(List list, SmssmppConfpojo smscon) {
    try {
			lis = list;
			smsconf.setSystemid(smscon.getSystemid());
			smsconf.setPassword(smscon.getPassword());
			smsconf.setServerip(smscon.getServerip());
			smsconf.setPort(smscon.getPort());
			smsconf.setSystemtype(smscon.getSystemtype());
			smsconf.setSourceaddress(smscon.getSourceaddress());
			smsconf.setActflag(smscon.getActflag());
			smsconf.setFetchcount(smscon.getFetchcount());
			smsconf.setPid(smscon.getPid());
			smsconf.setSdt(smscon.getSdt());
			smsconf.setRdy(smscon.getRdy());
			if (smscon.getServietype() == null) {
				smscon.setServietype("");
			}
			smsconf.setServietype(smscon.getServietype());
			smsconf.setPushbindtype(smscon.getPushbindtype());
			smsconf.setPullbindtype(smscon.getPullbindtype());

      if (session == null || !session.getSessionState().isBound() || session.getSessionState().equals(SessionState.CLOSED)) {
				session = null;
				session = new SMPPSession();
				int ton;
				int npi;
				ton = smscon.getTon();
				npi = smscon.getNpi();
				BindType pbt = null;
				if (smsconf.getPushbindtype() != null && smsconf.getPushbindtype().equalsIgnoreCase("trx")) {
					pbt = BindType.BIND_TRX;
				} else if (smsconf.getPushbindtype() != null && smsconf.getPushbindtype().equalsIgnoreCase("tx")) {
					pbt = BindType.BIND_TX;
				} else if (smsconf.getPushbindtype() != null && smsconf.getPushbindtype().equalsIgnoreCase("rx")) {
					pbt = BindType.BIND_RX;
				}
        if (ton == 0) {
          ton1 = TypeOfNumber.UNKNOWN;
        } else if (ton == 1) {
          ton1 = TypeOfNumber.INTERNATIONAL;
        } else if (ton == 2) {
          ton1 = TypeOfNumber.NATIONAL;
        } else if (ton == 3) {
          ton1 = TypeOfNumber.NETWORK_SPECIFIC;
        } else if (ton == 4) {
          ton1 = TypeOfNumber.SUBSCRIBER_NUMBER;
        } else if (ton == 5) {
          ton1 = TypeOfNumber.ALPHANUMERIC;
        } else if (ton == 6) {
          ton1 = TypeOfNumber.ABBREVIATED;
        } else {
          ton1 = TypeOfNumber.INTERNATIONAL;
        }

        if (npi == 0) {
          npi1 = NumberingPlanIndicator.UNKNOWN;
        } else if (npi == 1) {
          npi1 = NumberingPlanIndicator.ISDN;
        } else if (npi == 2) {
          npi1 = NumberingPlanIndicator.DATA;
        } else if (npi == 3) {
          npi1 = NumberingPlanIndicator.TELEX;
        } else if (npi == 4) {
          npi1 = NumberingPlanIndicator.LAND_MOBILE;
        } else if (npi == 8) {
          npi1 = NumberingPlanIndicator.NATIONAL;
        } else if (npi == 9) {
          npi1 = NumberingPlanIndicator.PRIVATE;
        } else if (npi == 16) {
          npi1 = NumberingPlanIndicator.ERMES;
        } else if (npi == 20) {
          npi1 = NumberingPlanIndicator.INTERNET;
        } else if (npi == 24) {
          npi1 = NumberingPlanIndicator.WAP;
        } else {
          npi1 = NumberingPlanIndicator.ISDN;
        }

        if (smscon.getRdy() == 0) {
          registeredDelivery.setSMSCDeliveryReceipt(SMSCDeliveryReceipt.DEFAULT);
        } else {
          registeredDelivery.setSMSCDeliveryReceipt(SMSCDeliveryReceipt.SUCCESS_FAILURE);
        }
        if (smscon.getSdt() != null && smscon.getSdt().equals("1")) {
          schedule_delivery_time = timeFormatter.format(new Date());
        } else {
          schedule_delivery_time = null;
        }
        session.connectAndBind(smsconf.getServerip(), smsconf.getPort(),
            new BindParameter(pbt, smsconf.getSystemid(),
                smsconf.getPassword(), smsconf.getSystemtype(),
                TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN,
                smsconf.getSourceaddress()));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * SMPP Thread run execution.
   */
  public void run() {
    try {
      Thread ct = Thread.currentThread();
      System.out.println("Thread Name :: " + ct.getName() + " Enter the block");
      if (session != null && session.getSessionState().isBound() && !session.getSessionState().equals(SessionState.CLOSED)) {        
    	  getSmsDeatilDb(lis);
      } else {
    	  System.out.println("smspollFlag='F' update in run()");
    	  String sql = "update SmsInpojo set smspollFlag='F' where smsId in";
    	  sql += " (";
    	  SmsInpojo empIdObj;
    	  int kj = 0;
    	  for (Iterator<SmsInpojo> it = lis.iterator(); it.hasNext();) {
    		  empIdObj = it.next();
          if (kj == 0) {
            sql += "'" + empIdObj.getSmsId() + "'";
          } else {
            sql += ",'" + empIdObj.getSmsId() + "'";
          }
          kj++;
        }
        sql += ")";
        smseng.commonUpdate(sql);
      }
      System.out.println(":: Thread Name : " + ct.getName() + ":: Exit the block");
    } catch (Exception ex) {
      System.out.println("Exception in smppfunction run() :  " + ex);
    }
  }

  /**
   * SMPP - getting sms details from DB.
   * 
   * @param lisst
   *          .
   * @return call sendsms().
   */
  private String getSmsDeatilDb(List lisst) {
    String result = "error<IB>";
    try {
      SmsInpojo empIdObj;
      List lst = lisst;
      for (Iterator<SmsInpojo> it = lst.iterator(); it.hasNext();) {
        empIdObj = it.next();
        result = sendSms(empIdObj);
      }
      return result;
    } catch (Exception ex) {
      System.out.println("exception in=== " + ex);
      return "error";
    }
  }

  /**
   * sending sms(Records move into out table).
   * 
   * @param empIdObj
   *          .
   * @return send success.
   */
  public String sendSms(SmsInpojo empIdObj) {
    String retval = "error<IB>";
    try {
			System.out.println(":: SMS START TO SEND ::");
			String response = empIdObj.getSmsMessage();
			Date date = common.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			System.out.println("Mobile No : " + empIdObj.getSmsMobNumber());
			if (response.length() <= 160) {  // String messageId="2312312312";
				System.out.println("Response : " + response.length());
				String mobno = empIdObj.getSmsMobNumber();
				empIdObj.setSmsMobNumber(empIdObj.getSmsMobNumber().replace("+", ""));
				String messageId = session.submitShortMessage(smsconf.getServietype(),
						ton1, npi1, smsconf.getSourceaddress(), ton1, npi1,
						empIdObj.getSmsMobNumber(), esmClass, (byte) smsconf.getPid(),
						(byte) 1, schedule_delivery_time, null, registeredDelivery,
						(byte) 0, dataCoding, (byte) 0, response.getBytes());
				empIdObj.setSmsMobNumber(mobno);
				
				if (messageId != null && !messageId.isEmpty()) {
					System.out.println(":: Message ID ["+messageId+"] :: ");
					smsout.setMobNo(empIdObj.getSmsMobNumber());
					smsout.setCardNo(empIdObj.getSmsCardNo());
					smsout.setMsgid(messageId);
					smsout.setMessage(empIdObj.getSmsMessage());
					smsout.setTempId("");
					smsout.setTempVAl("");
					smsout.setPoolFlag("P");
					smsout.setSmstempId("");
					smsout.setEntryDate(date);
					insetSmsOutTable(empIdObj, smsout);
					retval = "Send Success";
				} else {
					System.out.println(":: Invalid Message ID ["+messageId+"] :: ");
					smserror.setMobNo(empIdObj.getSmsMobNumber());
					smserror.setCardNo(empIdObj.getSmsCardNo());
					smserror.setMsgid("");
					smserror.setMessage(empIdObj.getSmsMessage());
					smserror.setTempId("");
					smserror.setTempVAl("");
					smserror.setPoolFlag("P");
					smserror.setSmstempId("");
					smserror.setEntryDate(date);
					smserror.setErrorstatus("1");
					insetSmsErrorTable(smserror);
					retval = "error";

				}
      } else {
       
        System.out.println(":: Message Length Greater Than 160 : Length is ["+response.length()+"] :: ");
        smserror.setMobNo(empIdObj.getSmsMobNumber());
        smserror.setCardNo(empIdObj.getSmsCardNo());
        smserror.setMsgid("");
        smserror.setMessage(empIdObj.getSmsMessage());
        smserror.setTempId("");
        smserror.setTempVAl("");
        smserror.setPoolFlag("P");
        smserror.setSmstempId("");
        smserror.setEntryDate(date);
        smserror.setErrorstatus("1");
        insetSmsErrorTable(smserror);
        retval = "error";
      }
      return retval;
    } catch (Exception ex) {
      if (session != null && session.getSessionState().isBound()  && !session.getSessionState().equals(SessionState.CLOSED)) {
        try {
					System.out.println("Error ::: Exception occur");
					Date date = common
							.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
					smserror.setMobNo(empIdObj.getSmsMobNumber());
					smserror.setCardNo(empIdObj.getSmsCardNo());
					smserror.setMsgid("");
					smserror.setMessage(empIdObj.getSmsMessage());
					smserror.setTempId("");
					smserror.setTempVAl("");
					smserror.setPoolFlag("P");
					smserror.setSmstempId("");
					smserror.setEntryDate(date);
					smserror.setErrorstatus("1");
					insetSmsErrorTable(smserror);
        } catch (Exception excep) {
          System.out.println("Exception in set smserror sendSms() : " + excep);
        }
      }
      System.out.println("exception set smserror sendSms() : " + ex);
      return "error<IB>";
    } finally {
      // Thread.currentThread().destroy();

    }
  }

  /**
   * Insert sms records into out table.
   * 
   * @param empIdObj
   *          .
   * @param smso
   *          .
   * @return Records inserted.
   */
  public String insetSmsOutTable(SmsInpojo empIdObj, SmsOutpojo smso) {
    String retu = "Inserted";
    try {
      System.out.println("smso=================" + smso);
      System.out.println("insert into smsout table");
      retu = smseng.insertsmsout(smso);
      smseng.deletesmsout(smso);
      return retu;
    } catch (Exception ex) {
      System.out.println("Exception found ---->InsetSMS_Out_TABLE" + ex);
      return retu;
    } finally {
      /**
       * Empty block.
       */
    }

  }

  /**
   * Insert sms records into error table.
   * 
   * @param empIdObj
   *          .
   * @return Records inserted.
   */
  private String insetSmsErrorTable(SmsErrorpojo empIdObj) {
    String retu = "Not Inserted";
    try {
      System.out.println("empIdObj=================" + empIdObj);
      System.out.println("insert into smserror table");
      retu = smseng.insertsmserror(empIdObj);
      smseng.deletesmserror(empIdObj);
      return retu;
    } catch (Exception ex) {
      System.out.println("Exception found ---->InsetSMS_Error_TABLE" + ex);
      return "Not Insert";
    } finally {
      /**
       * Empty block.
       */
    }
  }
}
