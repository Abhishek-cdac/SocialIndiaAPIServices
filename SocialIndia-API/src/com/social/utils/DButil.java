package com.social.utils;

import java.sql.Connection;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

/**
 * DBUtil.
 * 
 * @author PL0038.
 *
 */
public class DButil {

  public static Logger lg = Logger.getLogger(DButil.class);

  /**
   * NS.
   * 
   * @param str
   *          .
   * @return.
   */
  public static String ns(String str) {
    if (str == null || str.equalsIgnoreCase("") || str.equalsIgnoreCase("null")) {
      return "";
    }
    return str;
  }

  /**
   * close.
   * 
   * @param cn
   *          .
   * @param st
   *          .
   */
  public static void close(Connection cn, Statement st) {
    try {
      if (st != null) {
        st.close();
      }
      if (cn != null) {
        cn.close();
      }
    } catch (Exception ex) {
      lg.error("Exception on closing [st, cn] objects---" + ex);
    } finally {
      lg = null;
    }
  }

  /**
   * close.
   * 
   * @param cn
   *          .
   * @param st
   *          .
   */
  public static void close(Connection cn, PreparedStatement st) {
    try {
      if (st != null) {
        st.close();
      }
      if (cn != null) {
        cn.close();
      }
    } catch (Exception ex) {
      lg.error("Exception on closing [prepared-st, cn] objects---" + ex);
    } finally {
      lg = null;
    }
  }

  /**
   * close.
   * 
   * @param cn
   *          .
   * @param st
   *          .
   */
  public static void close(Connection cn, Statement st, ResultSet rs) {
    try {
      if (rs != null) {
        rs.close();
      }
      if (st != null) {
        st.close();
      }
      if (cn != null) {
        cn.close();
      }
    } catch (Exception ex) {
      lg.error("Exception on closing [rs, st, cn] objects---" + ex);
    } finally {
      lg = null;
    }
  }

  /**
   * close.
   * 
   
   * @param st
   *          .
   */
  public static void close(Statement st, ResultSet rs) {
    try {
      if (rs != null) {
        rs.close();
      }
      if (st != null) {
        st.close();
      }
    } catch (Exception ex) {
      lg.error("Exception on closing [rs, st] objects---" + ex);
    } finally {
      lg = null;
    }
  }

  /**
   * close.
   * 
   * @param cn
   *          .
  
  
   */
  public static void close(Connection cn) {
    try {
      if (cn != null) {
        cn.close();
      }
    } catch (Exception ex) {
      lg.error("Exception on closing [cn] objects---" + ex);
    } finally {
      lg = null;
    }
  }

}
