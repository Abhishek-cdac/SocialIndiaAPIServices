package com.social.common;

import com.social.common.Common;
import com.social.common.CommonDao;

import java.util.List;

/**
 * Implementing interface services for common action.
 * 
 * @author PL0043
 * 
 */
public class CommonDaoService implements CommonService {
  private Common dao;

  public CommonDaoService() {
    dao = new CommonDao();
  }

  @Override
  public boolean commonUpdate(String sql) {
    // TODO Auto-generated method stub
    return dao.commonUpdate(sql);
  }

  @Override
  public String commonDelete(int uid, String pojonam, String colmn) {
    // TODO Auto-generated method stub
    return dao.commonDelete(uid, pojonam, colmn);
  }

  

  @Override
  public String getSwiftcode(int branchid) {
    // TODO Auto-generated method stub
    return dao.getSwiftcode(branchid);
  }

  @Override
  public String getSinglevalfromtbl(String tblname, String colname, String val) {
    // TODO Auto-generated method stub
    return dao.getSinglevalfromtbl(tblname, colname, val);
  }

  @Override
  public String commonAutogenval(int lengthval, String type, String tblname,
      String colname) {
    // TODO Auto-generated method stub
    return dao.commonAutogenval(lengthval, type, tblname, colname);
  }

  public int gettotalcount(String sql) {
    // TODO Auto-generated method stub
    return dao.gettotalcount(sql);
  }

  @Override
  public String getPath(String contextPath, String requestUri,
      StringBuffer requestUrl) {
    // TODO Auto-generated method stub
    return dao.getPath(contextPath, requestUri, requestUrl);
  }


}
