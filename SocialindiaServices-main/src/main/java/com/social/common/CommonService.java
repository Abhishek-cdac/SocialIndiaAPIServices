package com.social.common;

/*import com.remittance.vo.OtpConfigTblVo;
 import com.remittance.vo.OtpDetailTblVo;*/

import java.util.List;

public interface CommonService {

  public boolean commonUpdate(String sql);

  public String commonDelete(int uid, String pojonam, String colmn);


  public String getSwiftcode(int branchid);

  public String getSinglevalfromtbl(String tblname, String colname, String val);

  public String commonAutogenval(int lengthval, String type, String tblname,
      String colname);

  public int gettotalcount(String sql);

  public String getPath(String contextPath, String requestUri,
      StringBuffer requestUrl);

}
