package com.social.common;

import com.pack.commonvo.SkillMasterTblVO;

public interface Common {

  public boolean commonUpdate(String sql);

  public String commonDelete(int uid, String pojonam, String colmn);

  public String getSwiftcode(int branchid);

  public String getSinglevalfromtbl(String tblname, String colname, String val);

  public String commonAutogenval(int lengthval, String type, String tblname, String colname);

  public int gettotalcount(String sql);

  public String getPath(String contextPath, String requestUri, StringBuffer requestUrl);
  
  public String getRandomval(String password,int size);

  public int toIsertskill(SkillMasterTblVO skillObj);

public String getcyberval(String loc_slQry);

}
