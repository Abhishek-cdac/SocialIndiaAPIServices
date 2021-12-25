package com.social.utils;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import org.apache.log4j.Logger;

import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;

public class CommonUtilsDao implements CommonUtils {
  private static Logger log = Logger.getLogger(CommonUtilsDao.class);

  @Override
  public String checkNull(String str) {

    if (str == null) {
      return "";
    }
    return str;
  }

  @Override
  public Date getCurrentDateTime(String dateformattype) {
    Date date = new Date();
    try {
      DateFormat dateFormat = new SimpleDateFormat(dateformattype);
      Date dateFor = new Date();
      date = dateFormat.parse(dateFormat.format(dateFor));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return date;
  }

  @Override
  public String stringToMD5(String password) {
    String result = null;
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("MD5");
      md.update(password.getBytes(Charset.forName("UTF-8")));
      result = String.format(Locale.ROOT, "%032x",
          new BigInteger(1, md.digest()));
    } catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException(ex);
    }

    return result;
  }

  @Override
  public String getRandomval(String type, int dataLength) {
    // TODO Auto-generated method stub
    char[] result = new char[dataLength];
    try {
      final char[] charset_aZ = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
          .toCharArray();

      final char[] charset_AZ_09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
          .toCharArray();

      final char[] charset_09 = "0123456789".toCharArray();
      char[] characterSet = null;
      if (type != null && !type.equalsIgnoreCase("")
          && !type.equalsIgnoreCase("null")) {
        if (type.equalsIgnoreCase("aZ")) {
          characterSet = charset_aZ;
        } else if (type.equalsIgnoreCase("AZ_09")) {
          characterSet = charset_AZ_09;
        } else if (type.equalsIgnoreCase("09")) {
          characterSet = charset_09;
        } else if (type.equalsIgnoreCase("aZ_09")) {
          characterSet = charset_09;
        }
      }
      Random random = new SecureRandom();

      for (int i = 0; i < result.length; i++) {
        // picks a random index out of character set > random character
        int randomCharIndex = random.nextInt(characterSet.length);
        result[i] = characterSet[randomCharIndex];
      }

    } catch (Exception ex) {
      System.out.println("Exception found ger random number:" + ex);
    }
    return new String(result);
  }

  @Override
  public String getStrCurrentDateTime(String dateformattype) {
    String dateformat = "";
    try {
      DateFormat fileDateFormat = new SimpleDateFormat(dateformattype);
      Date fileDate = new Date();
      dateformat = fileDateFormat.format(fileDate);
    } catch (Exception ex) {
      System.out.println("Exception found in getStrCurrentDateTime:" + ex);
    }
    return dateformat;
  }

  @Override
  public boolean checkEmpty(String str) {
    // TODO Auto-generated method stub
    boolean retval = false;
    try {
      if (str != null && !str.equalsIgnoreCase("")
          && !str.equalsIgnoreCase("null")) {
        retval = true;
      }
    } catch (Exception ex) {
      System.out.println("Exception found in checkEmpty:" + ex);
    }
    return retval;
  }

  /**
   * randInt.
   */
  public String randInt(int min, int max) {

    Random rand = new Random();
    int randomNum = rand.nextInt((max - min) + 1) + min;
    return Integer.toString(randomNum);
  }

  /**
   * smsTemplateParser.
   */
  public String smsTemplateParser(String smsMessage, int custid,
      String availbal, String password) {   
    String qry = "select ";
    boolean flag = true;
    int size = 0;
    try {
      SmsEngineServices smsHbm = new SmsEngineDaoServices();
      if (smsMessage.contains("[") && smsMessage.contains("]")) {

        String colnam = "";
        int fsr = 0;
        int sec = 1;
        HashMap hm = new HashMap();
        hm = smsHbm.smsTemplateMap();
        int inc = 0;
        String com = "";
        while (flag) {
          inc++;
          if (inc > 1) {
            com = ",";
          }
          fsr = smsMessage.indexOf("[", fsr);
          sec = smsMessage.indexOf("]", fsr);
          if (sec > 0 && fsr > 0) {
            sec = sec + 1;
            colnam = smsMessage.substring(fsr, sec);
            String colname = (String) hm.get(colnam);
            System.out.println("Column  name : "+colname);
            if (colname != null) {
              if (colnam.equalsIgnoreCase("[RANDOM PASSWORD]")
                  || colnam.equalsIgnoreCase("[RESETPASSWORD]")
                  || colnam.equalsIgnoreCase("[PASSWORD]") || colnam.equalsIgnoreCase("[OTP]") || colnam.equalsIgnoreCase("[ORDER_URL]")) {

                if (password != null) {
                  qry += com + "'" + password + "'";
                } else {
                  qry += com + "";
                }

              } else {
                qry += com + "" + colname;
              }
              size++;
            } else {
              /**
               * colname is null or empty.
               */
            }
            fsr = fsr + 1;

          } else {
            flag = false;
          }

        }

      }
    } catch (Exception ex) {
      log.error("Exception in smsTemplateParser()--- " + ex);
    }
    return qry + "!_!" + size;
  }

  /**
 * templateParser.
 */
  public String templateParser(String content, int custid, String availbal, String password) {
    // TODO Auto-generated method stub
    String qry = "select ";
    boolean flag = true;
    int size = 0;
    HashMap hm = null;
    try {
    	hm = new HashMap();
      EmailEngineServices emailHbm = new EmailEngineDaoServices();
      if (content.contains("[") && content.contains("]")) {

        String colnam = "";
        int fsr = 0;
        int sec = 1;
        
        hm = emailHbm.emailTemplatemap();
        int inc = 0;
        String com = "";
        while (flag) {
          inc++;
          if (inc > 1) {
            com = ",";
          }
          fsr = content.indexOf("[", fsr);
          sec = content.indexOf("]", fsr);
          if (sec > 0 && fsr > 0) {
            sec = sec + 1;
            colnam = content.substring(fsr, sec);
            String colname = (String) hm.get(colnam);
            if (colname != null) {
              if (colnam.equalsIgnoreCase("[RANDOM PASSWORD]") || colnam.equalsIgnoreCase("[RESETPASSWORD]") || colnam.equalsIgnoreCase("[PASSWORD]") || colnam.equalsIgnoreCase("[ORDER_URL]")) {

                if (password != null) {
                  qry += com + "'" + password + "'";
                } else {
                  qry += com + "";
                }

              } else {
                qry += com + "" + colname;
              }
              size++;
            } else {
              /**
               * colname is null or empty.
               */
            }
            fsr = fsr + 1;

          } else {
            flag = false;
          }

        }

      }
    } catch (Exception ex) {
      log.error("Exception in templateParser()--- " + ex);
    } finally{
    	hm = null;
    }
    return qry + "!_!" + size;
  }
  public boolean deleteallFileInDirectory(String filepath) {
		File lcMovefile = null;
		File directoryfile = null;
		try {
			directoryfile = new File(filepath);
			
			if (directoryfile.isDirectory()) {
				directoryfile.delete();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}
}
