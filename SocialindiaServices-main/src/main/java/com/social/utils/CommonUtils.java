package com.social.utils;

import java.util.Date;

public interface CommonUtils {

  public String checkNull(String str);

  public Date getCurrentDateTime(String dateformattype);

  public String stringToMD5(String password);

  public String getRandomval(String type, int dataLength);

  public String getStrCurrentDateTime(String dateformattype);

  public boolean checkEmpty(String str);

  public String randInt(int min, int max);

  public String templateParser(String content, int custid, String availbal,
      String password);

  public String smsTemplateParser(String smsMessage, int custid,
      String availbal, String password);
  public boolean deleteallFileInDirectory(String filepath);
}

