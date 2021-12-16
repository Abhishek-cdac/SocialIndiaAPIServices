package com.social.common;

/*import com.remittance.vo.OtpConfigTblVo;
 import com.remittance.vo.OtpDetailTblVo;*/

import com.pack.commonvo.SkillMasterTblVO;
import com.social.load.HibernateUtil;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.function.persistence.FunctionDaoservices;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * For using common actions.
 * 
 * @author PL0043
 * 
 */
public class CommonDao implements Common {

  // public static Logger log = Logger.getLogger(DbUtil.class);
  static Log log = new Log();
  CommonUtils common = new CommonUtilsDao();

  @Override
  public boolean commonUpdate(String sql) {
    boolean result = false;
    try {
      Session session = HibernateUtil.getSession();
      Transaction tx = null;
      try {
        tx = session.beginTransaction();
        Query qy = session.createQuery(sql);
        int cnt = qy.executeUpdate();
        tx.commit();
        if (cnt > 0) {
          result = true;
        } else {
          result = false;
        }
        // log.logMessage("Common Update Query--->"+sql, "info",
        // CommonDao.class);
      } catch (HibernateException ex) {
        if (tx != null) {
          tx.rollback();
        }
        ex.printStackTrace();
        result = false;
      } finally {
    	  session.flush();session.clear(); session.close();session = null;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      log.logMessage("Exception Common Update Query--->" + sql
          + " \t- Exception-" + ex, "error", CommonDao.class);
      result = false;
    }
    return result;
  }

  /**
   * Delete action.
   */
  public String commonDelete(int sno, String tblpojoname, String whr) {

    String pojonam = "";
    String wherecolumn = "";
    Query qy = null;

    pojonam = tblpojoname;
    wherecolumn = whr;
    Session session = HibernateUtil.getSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      qy = session.createQuery("delete from " + pojonam + " where "
          + wherecolumn + "=" + sno + "");
      qy.executeUpdate();
      tx.commit();
    } catch (HibernateException ex) {
      if (tx != null) {
        tx.rollback();
      }
      ex.printStackTrace();
      log.logMessage("Exception Common Delete Query--->" + qy
          + " \t- Exception-" + ex, "error", CommonDao.class);
    } finally {
    	session.flush();session.clear(); session.close();session = null;
      log = null;
    }
    return "Successfully Deleted";
  }

  /**
   * Call procedure fucntion.
   * 
   * @param callproc
   *          .
   * @param procName
   *          .
   * @param ArrayList
   *          .
   * @param connection
   *          .
   * @return Resultset returned.
   */
  public static ResultSet call_proc(String procName, ArrayList al, Connection cn) {
    String qst = "call " + procName + "(";
    String dbug = "call " + procName + "(";
    String proc = "";
    ResultSet rst = null;
    try {
      for (int i = 0; i < al.size(); i++) {
        if (i == (al.size() - 1)) {
          qst += "?)";
          dbug += "'" + al.get(i) + "')";
        } else {
          qst += "?,";
          dbug += "'" + al.get(i) + "',";
        }
      }
      proc += qst;
      log.logMessage("Call proc--->" + dbug, "info", CommonDao.class);
      CallableStatement cst = cn.prepareCall(proc);
      for (int i = 0; i < al.size(); i++) {
        cst.setObject(i + 1, al.get(i));
      }
      rst = cst.executeQuery();
    } catch (Exception ex) {
      System.out.println("Exception : " + ex);
      try {
        String errMsg = "";
        ResultSet rset = cn.createStatement().executeQuery("show warnings");
        int cc = rset.getMetaData().getColumnCount();
        while (rset != null && rset.next()) {
          errMsg += rset.getString("Level") + "  |  " + rset.getString("Code")
              + "  |  " + rset.getString("Message") + "\n";
        }
        log.logMessage("Exception Callproc - " + dbug + " \t- Exception-" + ex,
            "error", CommonDao.class);
        log.logMessage("Exception Callproc - " + errMsg + " \t- Exception-"
            + ex, "error", CommonDao.class);
      } catch (Exception exp) {
        log.logMessage("Exception 2exception Callproc - " + proc
            + " \t- Exception-" + exp, "error", CommonDao.class);
      } finally {
        log = null;
      }
    }
    return rst;
  }

  public String getSwiftcode(int branchid) {
    // TODO Auto-generated method stub
    String swiftcode = "";
    Session session = HibernateUtil.getSession();
    try {
      String qry = "select swiftcode from BranchMasterTblVo where branchid=:BRANCHID";
      Query qy = session.createQuery(qry);
      qy.setInteger("BRANCHID", branchid);
      qy.setMaxResults(1);
      swiftcode = (String) qy.uniqueResult();
      System.out.println("swiftcode value :" + swiftcode);
    } catch (Exception ex) {
      log.logMessage("Exception in getSwiftcode()--->" + ex, "error",
          CommonDao.class);
    } finally {
    	session.flush();session.clear(); session.close();session = null;
      log = null;
    }
    return swiftcode;
  }

  @Override
  public String getSinglevalfromtbl(String tblname, String colname, String val) {
    // TODO Auto-generated method stub
    String retval = "";
    Session session = HibernateUtil.getSession();
    try {
      System.out.println("-----Enter to get username--:" + colname);
      String qry = "select " + colname + " from " + tblname + " where "
          + colname + "=:PVALUE";
      Query qy = session.createQuery(qry);
      qy.setString("PVALUE", val);
      qy.setMaxResults(1);
      retval = (String) qy.uniqueResult();
      System.out.println("retval  ------:" + retval);
    } catch (Exception ex) {
      System.out.println("Exeption found in getSinglevalfromtbl:" + ex);
      log.logMessage("Exception in getSinglevalfromtbl()--->" + ex, "error",
          CommonDao.class);
    } finally {
    	if(session!=null){ session.flush();session.clear(); session.close();session = null; }
    }
    return retval;
  }

  @Override
  public String commonAutogenval(int lengthval, String type, String tblname,
      String colname) {
    String retval = "";// common
    try {
      System.out.println("----1");
      boolean flg = true;
      int ilen = 0;
      while (flg) {
        System.out.println("----2");
        String keyval = common.getRandomval(type, lengthval);
        System.out.println("----3 keyval:" + keyval);
        String rettblval = getSinglevalfromtbl(tblname, colname, keyval);
        System.out.println("----3:" + keyval);
        if (!common.checkEmpty(rettblval)) {
          retval = keyval;
          flg = false;
        } else {
          if (ilen == 50) {
            lengthval = lengthval + 5;
          }
        }
        ilen++;
      }
      System.out.println("-----retval:" + retval);

    } catch (Exception ex) {
      System.out.println("Exeption found in commonAutogenval:" + ex);
      log.logMessage("Exception in commonAutogenval()--->" + ex, "error",
          CommonDao.class);
    }
    return retval;
  }

  @Override
  public int gettotalcount(String sql) {
    int totcnt = 0;
    Session session = HibernateUtil.getSession();
    try {
      Query qy = session.createQuery(sql);
      totcnt = ((Number) qy.uniqueResult()).intValue();
      System.out.println("totalcount------------" + totcnt);
    } catch (Exception ex) {
      ex.printStackTrace();
      log.logMessage("Exception in gettotalcount()--->" + ex, "error",
          CommonDao.class);
    } finally {
    	if(session!=null){ session.flush();session.clear(); session.close();session = null; }
      log = null;
    }
    return totcnt;
  }
  @Override
  public String getPath(String contextPath, String requestUri,
      StringBuffer requestUrl) {
    // TODO Auto-generated method stub
    String path = "";
    try {
      if (contextPath != null && requestUri != null && requestUrl != null) {
        path = requestUrl.toString().replaceAll(requestUri, "");
        path = path + contextPath;
      }
    } catch (Exception ex) {
      log.write.error("path==" + path + "\n ex==" + ex);
    }
    return path;
  }

  @Override
  public String getRandomval(String type, int dataLength) {
    char[] result = new char[dataLength];
    try {
      // final char[] CHARSET_aZ_09 =
      // "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
      // .toCharArray();

      final char[] charSetAz = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
          .toCharArray();
      final char[] charSetAz09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
          .toCharArray();
      final char[] charSetHex = "0123456789ABCDEF".toCharArray();
      final char[] charSetSpecial = { '!', 'A', 'B' };
      final char[] charSet09 = "0123456789".toCharArray();
      final char[] charSetaZ09sp = "abcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*()_+{}|[]?/><,.;':ABCDEFGHIJKLMNOPQRSTUVWXYZ"
          .toCharArray();

      final char[] charSetAZaz09 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
          .toCharArray();
      final char[] charSetAZazSP = "abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+{}|[]?/><,.;':ABCDEFGHIJKLMNOPQRSTUVWXYZ"
          .toCharArray();
      final char[] charSet09SP = "0123456789~!@#$%^&*()_+{}|[]?/><,.;':"
          .toCharArray();
      final char[] charSetSP = "~!@#$%^&*()_+{}|[]?/><,.;':".toCharArray();

      char[] characterSet = null;
      if (type != null && !type.equalsIgnoreCase("")
          && !type.equalsIgnoreCase("null")) {
        if (type.equalsIgnoreCase("aZ")) {
          characterSet = charSetAz;
        } else if (type.equalsIgnoreCase("AZ_09")) {
          characterSet = charSetAz09;
        } else if (type.equalsIgnoreCase("09")) {
          characterSet = charSet09;
        } else if (type.equalsIgnoreCase("aZ_09")) {
          characterSet = charSet09;
        } else if (type.equalsIgnoreCase("aZ_09_sp")) {
          characterSet = charSetaZ09sp;
        } else if (type.equalsIgnoreCase("AZaz09")) {
          characterSet = charSetAZaz09;
        } else if (type.equalsIgnoreCase("AZazSP")) {
          characterSet = charSetAZazSP;
        } else if (type.equalsIgnoreCase("09SP")) {
          characterSet = charSet09SP;
        } else if (type.equalsIgnoreCase("SP")) {
          characterSet = charSetSP;
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
public int toIsertskill(SkillMasterTblVO skillObj) {
	Log log=new Log();
	Session session = null;
	Transaction tx = null;
	int locfunid=-1;
	try {
		session = HibernateUtil.getSessionFactory().openSession();
		tx = session.beginTransaction();			
		session.save(skillObj);
		locfunid=skillObj.getIvrBnSKILL_ID();
		tx.commit();
	} catch (Exception e) {
		if (tx != null) {
			tx.rollback();
		}
		System.out.println("Exception found in skillDaoservices : "+e);
		log.logMessage("Exception : "+e, "error", FunctionDaoservices.class);
		locfunid=-1;
		return locfunid;
	} finally {
		if(session!=null){session.flush();session.clear();session.close();session=null;}
		if(tx!=null){tx=null;}
		
	}
	return locfunid;
}

@Override
public String getcyberval(String qry) {

    // TODO Auto-generated method stub
    String swiftcode = "";
    Session session = HibernateUtil.getSession();
    try {
      Query qy = session.createQuery(qry);
      swiftcode = (String) qy.uniqueResult();
      System.out.println("swiftcode value :" + swiftcode);
    } catch (Exception ex) {
      log.logMessage("Exception in getSwiftcode()--->" + ex, "error",
          CommonDao.class);
    } finally {
    	session.flush();session.clear(); session.close();session = null;
      log = null;qry=null;
    }
    return swiftcode;
  
}
  
  

}
