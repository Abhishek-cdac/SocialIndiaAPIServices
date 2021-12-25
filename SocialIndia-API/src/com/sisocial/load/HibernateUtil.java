package com.sisocial.load;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

  private static SessionFactory sessionFactory;
  static SessionFactory factory = null;
  static {
    try {
      // Create the SessionFactory from hibernate.cfg.xml
      sessionFactory = new Configuration().configure().buildSessionFactory();     
    } catch (Throwable ex) {
      // Make sure you log the exception, as it might be swallowed
      System.err.println("Initial SessionFactory creation failed." + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  public static Session getSession() {
    return sessionFactory.openSession();
  }
  
  public static void shutdown() {
      // Close caches and connection pools
      getSessionFactory().close();
  }

  /**
   * main method.
   * 
   * @param args
   *          .
   */
  public static void main(String[] args) {
    try {
      System.out.println("voidmain hibernate.cfg.xml");
      factory = new Configuration().configure("com/pack/vo/hibernate.cfg.xml").buildSessionFactory();
      System.out.println(factory);
      new HibernateUtil().selectAddress();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * selectAddress.
   */
  public void selectAddress() {
    Session session;
    Transaction tx = null;
    try {
      session = factory.openSession();
      Query qry = session.createQuery("from Address");      
      List<HibernateUtil> lis = qry.list();
      Iterator it = lis.iterator();
      while (it.hasNext()) {
        HibernateUtil hiber = (HibernateUtil) it.next();        
        // System.out.println(o.getAddresId()+"="+o.getState()+"="+o.getCity());
      }
      session.close();
    } catch (Exception ex) {
      System.out.println("Exception found........");
      ex.printStackTrace();
      tx.rollback();
    }
  }

}
