package com.mobi.easypayvo.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobi.easypayvo.MvpPaygateTbl;
import com.mobi.easypayvo.MvpTransactionTbl;
import com.mobi.easypayvo.MvpUtilityPayLogTbl;
import com.mobi.easypayvo.MvpUtilityPayTbl;
import com.pack.billpaymentvo.CyberplatoptrsTblVo;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class EasyPaymentDaoServices implements EasyPaymentDao {
	Log log = new Log();

	@Override
	public int transactionFirstInsert(MvpTransactionTbl transActObj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		int retUniqId = -1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(transActObj);
			retUniqId = transActObj.getTranId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log.logMessage("Exception insert transactionFirstInsert: "+e, "error", EasyPaymentDaoServices.class);
			retUniqId=-1;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}		
		}
		return retUniqId;
	}

	@Override
	public int payGateInsert(MvpPaygateTbl payGateObj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		int retUniqId = -1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(payGateObj);
			retUniqId = payGateObj.getPaygateId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log.logMessage("Exception insert payGateInsert: "+e, "error", EasyPaymentDaoServices.class);
			retUniqId=-1;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}		
		}
		return retUniqId;
	}

	@Override
	public boolean payGateUpdate(MvpPaygateTbl payGateUpdateObj) {
		// TODO Auto-generated method stub
		Session session = null;
	    Transaction tx = null;
	    Log log = null;
	    Query qy = null;boolean result = false;
	    try {
	    	log = new Log();
	    	System.out.println("MvpPaygateTbl edit Start.");
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	//String updateQry = "";
	    	qy = session.createQuery("update MvpPaygateTbl set modifyDatetime=:MODIFY_DATETIME,txnDate=:TXN_DATE,txnTime=:TXN_TIME, "
	    			+ " agRef=:AG_REF,pgRef=:PG_REF,status=:STATUS,resCode=:RES_CODE,resMessage=:RES_MESSAGE, "
	    			+ " finalStatus=:FINAL_STATUS,pgDetails=:PG_DETAILS,fraudDetails=:FRAUD_DETAILS,otherDetails=:OTHER_DETAILS "
	    			+ " where orderNo.orderNo=:ORDER_NO");
	    	qy.setTimestamp("MODIFY_DATETIME", Commonutility.enteyUpdateInsertDateTime());
	    	qy.setString("TXN_DATE", payGateUpdateObj.getTxnDate());
	    	qy.setString("TXN_TIME", payGateUpdateObj.getTxnTime());
	    	qy.setString("AG_REF", payGateUpdateObj.getAgRef());
	    	qy.setString("PG_REF", payGateUpdateObj.getPgRef());
	    	qy.setString("STATUS", payGateUpdateObj.getStatus());
	    	qy.setString("RES_CODE", payGateUpdateObj.getResCode());
	    	qy.setString("RES_MESSAGE", payGateUpdateObj.getResMessage());
	    	qy.setInteger("FINAL_STATUS", payGateUpdateObj.getFinalStatus());
	    	qy.setString("PG_DETAILS", payGateUpdateObj.getPgDetails());
	    	qy.setString("FRAUD_DETAILS", payGateUpdateObj.getFraudDetails());
	    	qy.setString("OTHER_DETAILS", payGateUpdateObj.getOtherDetail());
	    	qy.setString("ORDER_NO", payGateUpdateObj.getOrderNo());
	    	qy.executeUpdate();
	    	tx.commit();
	    	result = true;
	    } catch (Exception ex) {
	    	log = new Log();
	    	if (tx != null) {tx.rollback();}	
	    	result = false;
			log.logMessage("Exception found in  edit payGateUpdate : "+ex, "error", EasyPaymentDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
		return result;
	}

	@Override
	public boolean transactionUpdate(MvpTransactionTbl transActUpdateObj) {
		// TODO Auto-generated method stub
		Session session = null;
	    Transaction tx = null;
	    Log log = null;
	    Query qy = null;boolean result = false;
	    try {
	    	log = new Log();
	    	System.out.println("MvpTransactionTbl edit Start.");
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	//String updateQry = "";
	    	qy = session.createQuery("update MvpTransactionTbl set modifyDateTime=:MODIFY_DATE_TIME, "
	    			+ " pamentGatewayStatus=:PAMENT_GATEWAY_STATUS, finalStatus=:FINAL_STATUS,session=:SESSION where orderNo=:ORDER_NO");
	    	qy.setTimestamp("MODIFY_DATE_TIME", Commonutility.enteyUpdateInsertDateTime());
	    	qy.setInteger("FINAL_STATUS", transActUpdateObj.getFinalStatus());
	    	qy.setInteger("PAMENT_GATEWAY_STATUS", transActUpdateObj.getPamentGatewayStatus());
	    	qy.setString("SESSION", transActUpdateObj.getSession());
	    	qy.setString("ORDER_NO", transActUpdateObj.getOrderNo());
	    	qy.executeUpdate();
	    	tx.commit();
	    	result = true;
	    } catch (Exception ex) {
	    	log = new Log();
	    	if (tx != null) {tx.rollback();}	
	    	result = false;
			log.logMessage("Exception found in  edit transactionUpdate : "+ex, "error", EasyPaymentDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
		return result;
	}

	@Override
	public int utilityPayInsert(MvpUtilityPayTbl utilityPayObj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		int retUniqId = -1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(utilityPayObj);
			retUniqId = utilityPayObj.getUtilityPayId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log.logMessage("Exception insert utilityPayInsert: "+e, "error", EasyPaymentDaoServices.class);
			retUniqId=-1;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}		
		}
		return retUniqId;
	
	}

	@Override
	public boolean utilityPayUpdate(MvpUtilityPayTbl utilityPayUpdaeObj) {
		// TODO Auto-generated method stub
		Session session = null;
	    Transaction tx = null;
	    Log log = null;
	    Query qy = null;boolean result = false;
	    try {
	    	log = new Log();
	    	System.out.println("utilityPayUpdate edit Start.");
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	//String updateQry = "";
	    	qy = session.createQuery("update MvpUtilityPayTbl set date=:DATE,error=:ERROR,result=:RESULT,transid=:TRANSID, "
	    			+ " phonenumber=:PHONENUMBER,circle=:CIRCLE,account=:ACCOUNT,authcode=:AUTHCODE,addinfo=:ADDINFO, "
	    			+ " price=:PRICE,billdate=:BILLDATE,billnumber=:BILLNUMBER,errmsg=:ERRMSG,trnxstatus=:TRNXSTATUS,"
	    			+ " finalStatus=:FINAL_STATUS,modifyDatetime=:MODIFY_DATETIME "
	    			+ " where session=:SESSION");
	    	qy.setString("DATE", utilityPayUpdaeObj.getDate());
	    	qy.setString("ERROR", utilityPayUpdaeObj.getError());
	    	qy.setString("RESULT", utilityPayUpdaeObj.getResult());
	    	qy.setString("TRANSID", utilityPayUpdaeObj.getTransid());
	    	qy.setString("PHONENUMBER", utilityPayUpdaeObj.getPhonenumber());
	    	qy.setString("CIRCLE", utilityPayUpdaeObj.getCircle());
	    	qy.setString("ACCOUNT", utilityPayUpdaeObj.getAccount());
	    	qy.setString("AUTHCODE", utilityPayUpdaeObj.getAuthcode());
	    	qy.setString("ADDINFO", utilityPayUpdaeObj.getAddinfo());
	    	qy.setString("PRICE", utilityPayUpdaeObj.getPrice());
	    	qy.setString("BILLDATE", utilityPayUpdaeObj.getBilldate());
	    	qy.setString("BILLNUMBER", utilityPayUpdaeObj.getBillnumber());
	    	qy.setString("ERRMSG", utilityPayUpdaeObj.getErrmsg());
	    	qy.setString("TRNXSTATUS", utilityPayUpdaeObj.getTrnxstatus());
	    	qy.setInteger("FINAL_STATUS", utilityPayUpdaeObj.getFinalStatus());
	    	qy.setTimestamp("MODIFY_DATETIME", Commonutility.enteyUpdateInsertDateTime());
	    	//qy.setInteger("USR_ID", utilityPayUpdaeObj.getUsrId());
	    	qy.setString("SESSION", utilityPayUpdaeObj.getSession());
	    	qy.executeUpdate();
	    	tx.commit();
	    	result = true;
	    } catch (Exception ex) {
	    	log = new Log();
	    	if (tx != null) {tx.rollback();}	
	    	result = false;
			log.logMessage("Exception found in  utilityPayUpdate edit : "+ex, "error", EasyPaymentDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
		return result;
	}

	@Override
	public boolean transactionSecondUpdate (
			MvpTransactionTbl transActSecondUpdate) {
		// TODO Auto-generated method stub
		Session session = null;
	    Transaction tx = null;
	    Log log = null;
	    Query qy = null;boolean result = false;
	    try {
	    	log = new Log();
	    	System.out.println("MvpTransactionTbl second edit Start.");
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	//String updateQry = "";
	    	qy = session.createQuery("update MvpTransactionTbl set modifyDateTime=:MODIFY_DATE_TIME, "
	    			+ " utilityPaymentStatus=:UTILITY_PAYMENT_STATUS,finalStatus=:FINAL_STATUS where orderNo=:ORDER_NO and session=:SESSION");
	    	qy.setTimestamp("MODIFY_DATE_TIME", Commonutility.enteyUpdateInsertDateTime());
	    	qy.setString("SESSION", transActSecondUpdate.getSession());
	    	qy.setInteger("UTILITY_PAYMENT_STATUS", transActSecondUpdate.getUtilityPaymentStatus());
	    	qy.setInteger("FINAL_STATUS", transActSecondUpdate.getFinalStatus());
	    	qy.setString("ORDER_NO", transActSecondUpdate.getOrderNo());
	    	qy.executeUpdate();
	    	tx.commit();
	    	result = true;
	    } catch (Exception ex) {
	    	log = new Log();
	    	if (tx != null) {tx.rollback();}	
	    	result = false;
			log.logMessage("Exception found in  edit transactionSecondUpdate : "+ex, "error", EasyPaymentDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
		return result;
	}

	@Override
	public String getConfigDetails(String key) {
		// TODO Auto-generated method stub 
		String configObj = "";
		Session session = HibernateUtil.getSession();
		try {
			String qry = "select extranalValue from MvpExternalOperatorConfigTbl where extranalKey='"+key+"' and status = 1";
			Query qy = session.createQuery(qry);
			qy.setMaxResults(1);
			configObj = (String) qy.uniqueResult();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			System.out.println("getConfigDetails======" + ex);
			 log.logMessage("Exception MvpExternalOperatorConfigTbl : "+ex, "error", EasyPaymentDaoServices.class);
		} finally {
			session.flush();session.clear(); session.close();session = null;
		}
		return configObj;
	}

	@Override
	public MvpPaygateTbl getPaygateDetails(int paymGateid, int rid) {
		// TODO Auto-generated method stub
		MvpPaygateTbl getpayGateData = new MvpPaygateTbl();
		Session session = HibernateUtil.getSession();
		try {
			String qry = "From MvpPaygateTbl where paygateId=:PAYGATE_ID and usrId=:USR_ID";
			Query qy = session.createQuery(qry);
			qy.setInteger("PAYGATE_ID", paymGateid);
			qy.setInteger("USR_ID", rid);
			getpayGateData = (MvpPaygateTbl) qy.uniqueResult();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			System.out.println("getPaygateDetails======" + ex);
			 log.logMessage("Exception getPaygateDetails : "+ex, "error", EasyPaymentDaoServices.class);
		} finally {
			session.flush();session.clear(); session.close();session = null;
		}
		return getpayGateData;
	}

	@Override
	public MvpUtilityPayTbl getUtilityPayDetails(String resOrderno) {
		// TODO Auto-generated method stub
		MvpUtilityPayTbl utilityPayData = new MvpUtilityPayTbl();
		Session session = HibernateUtil.getSession();
		try {
			String qry = "From MvpUtilityPayTbl where session=:SESSION";
			Query qy = session.createQuery(qry);
			qy.setString("SESSION", resOrderno);
			//qy.setInteger("USR_ID", rid);
			utilityPayData = (MvpUtilityPayTbl) qy.uniqueResult();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			System.out.println("getUtilityPayDetails======" + ex);
			 log.logMessage("Exception getUtilityPayDetails : "+ex, "error", EasyPaymentDaoServices.class);
		} finally {
			session.flush();session.clear(); session.close();session = null;
		}
		return utilityPayData;
	}

	@Override
	public int payLogInsert(MvpUtilityPayLogTbl utilpayLogObj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		int retUniqId = -1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(utilpayLogObj);
			retUniqId = utilpayLogObj.getLogId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log.logMessage("Exception insert payLogInsert: "+e, "error", EasyPaymentDaoServices.class);
			retUniqId=-1;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}		
		}
		return retUniqId;
	}

	@Override
	public boolean transactionThirdUpdate(MvpTransactionTbl transActthirdUpdate) {
		// TODO Auto-generated method stub
		Session session = null;
	    Transaction tx = null;
	    Log log = null;
	    Query qy = null;boolean result = false;
	    try {
	    	log = new Log();
	    	System.out.println("transactionThirdUpdate edit Start.");
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	//String updateQry = "";
	    	qy = session.createQuery("update MvpTransactionTbl set modifyDateTime=:MODIFY_DATE_TIME, "
	    			+ " utilityPaymentStatus=:UTILITY_PAYMENT_STATUS, finalStatus=:FINAL_STATUS,remarksMsg=:REMARKS where session=:SESSION");
	    	qy.setTimestamp("MODIFY_DATE_TIME", Commonutility.enteyUpdateInsertDateTime());
	    	qy.setInteger("FINAL_STATUS", transActthirdUpdate.getFinalStatus());
	    	qy.setInteger("UTILITY_PAYMENT_STATUS", transActthirdUpdate.getUtilityPaymentStatus());
	    	qy.setString("REMARKS", transActthirdUpdate.getRemarksMsg());
	    	qy.setString("SESSION", transActthirdUpdate.getSession());
	    	qy.executeUpdate();
	    	tx.commit();
	    	result = true;
	    } catch (Exception ex) {
	    	log = new Log();
	    	if (tx != null) {tx.rollback();}	
	    	result = false;
			log.logMessage("Exception found in  edit transactionThirdUpdate : "+ex, "error", EasyPaymentDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
		return result;
	}

	@Override
	public List getTransactionList(String query, String startlim,
			String totalrow, String timestamp) {
		// TODO Auto-generated method stub
		List<MvpTransactionTbl> transactionList=new ArrayList<MvpTransactionTbl>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(query);
			qy.setFirstResult(Integer.parseInt(startlim));
			if(totalrow!=null && !totalrow.equalsIgnoreCase("0")){
			qy.setMaxResults(Integer.parseInt(totalrow));
			}
			transactionList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getTransactionList======" + ex);
			log.logMessage("EasyPaymentDaoServices Exception getTransactionList : "
							+ ex, "error", EasyPaymentDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return transactionList;
	}
	
	@Override
	public MvpTransactionTbl transGetFinalResponse(MvpTransactionTbl transActthirdUpdate) {
		// TODO Auto-generated method stub
		MvpTransactionTbl getFinalTrans = new MvpTransactionTbl();
		Session session = HibernateUtil.getSession();
		try {
			String qry = "From MvpTransactionTbl where session=:SESSION";
			Query qy = session.createQuery(qry);
			System.out.println("qry----" + qy);
			qy.setString("SESSION", transActthirdUpdate.getSession());
			//qy.setInteger("FINAL_STATUS", transActthirdUpdate.getFinalStatus());
			System.out.println("qry----" + transActthirdUpdate.getSession());
			getFinalTrans = (MvpTransactionTbl) qy.uniqueResult();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			System.out.println("transGetFinalResponse======" + ex);
			 log.logMessage("Exception transGetFinalResponse : "+ex, "error", EasyPaymentDaoServices.class);
		} finally {
			session.flush();session.clear(); session.close();session = null;
		}
		return getFinalTrans;
	}

	@Override
	public CyberplatoptrsTblVo getCyperPlateOperator(int billerId, int billerCode) {
		// TODO Auto-generated method stub
		CyberplatoptrsTblVo cyperPlateObj = new CyberplatoptrsTblVo();
		Session session = HibernateUtil.getSession();
		try {
			String qry = "From CyberplatoptrsTblVo where ivrBILLER_CATEGORY=:BILLER_CATEGORY and ivrBnBILLER_ID=:BILLER_ID";
			Query qy = session.createQuery(qry);
			qy.setInteger("BILLER_CATEGORY", billerId);
			qy.setInteger("BILLER_ID", billerCode);
			cyperPlateObj = (CyberplatoptrsTblVo) qy.uniqueResult();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			System.out.println("getPaygateDetails======" + ex);
			 log.logMessage("Exception getCyperPlateOperator : "+ex, "error", EasyPaymentDaoServices.class);
		} finally {
			session.flush();session.clear(); session.close();session = null;
		}
		return cyperPlateObj;
	}
	
	@Override
	public List getTransactionListForReport(String query, String timestamp) {
		// TODO Auto-generated method stub
		List<MvpTransactionTbl> transactionList=new ArrayList<MvpTransactionTbl>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery(query);
			transactionList = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getTransactionList======" + ex);
			log.logMessage("EasyPaymentDaoServices Exception getTransactionList : "
							+ ex, "error", EasyPaymentDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return transactionList;
	}

	@Override
	public MvpTransactionTbl transReturnResponse(MvpTransactionTbl transActgetData) {
		// TODO Auto-generated method stub
		MvpTransactionTbl getFinalTrans = new MvpTransactionTbl();
		Session session = HibernateUtil.getSession();
		try {
			String qry = "From MvpTransactionTbl where orderNo=:ORDER_NO";
			Query qy = session.createQuery(qry);
			System.out.println("qry----" + qy);
			qy.setString("ORDER_NO", transActgetData.getOrderNo());
			System.out.println("qry----" + transActgetData.getOrderNo());
			getFinalTrans = (MvpTransactionTbl) qy.uniqueResult();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			System.out.println("transReturnResponse======" + ex);
			 log.logMessage("Exception transReturnResponse : "+ex, "error", EasyPaymentDaoServices.class);
		} finally {
			session.flush();session.clear(); session.close();session = null;
		}
		return getFinalTrans;
	}

	@Override
	public boolean transRemarksUpdate(MvpTransactionTbl transRemarks,String seqNo) {
		// TODO Auto-generated method stub
		Session session = null;
	    Transaction tx = null;
	    Log log = null;
	    Query qy = null;boolean result = false;
	    String whChk = "";
	    try {
	    	log = new Log();
	    	System.out.println("transRemarksUpdate Start----------");
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	if (seqNo.equalsIgnoreCase("ON")) {
	    		whChk = "orderNo=:ORDER_NO";	
	    	} else if (seqNo.equalsIgnoreCase("SN")) {
	    		whChk = "session=:SESSION";
	    	} else {
	    		whChk = "session=:SESSION";
	    	}
	    	qy = session.createQuery("update MvpTransactionTbl set modifyDateTime=:MODIFY_DATE_TIME, "
	    			+ " remarksMsg=:REMARKS where "+whChk+ "");
	    	qy.setTimestamp("MODIFY_DATE_TIME", Commonutility.enteyUpdateInsertDateTime());
	    	qy.setString("REMARKS", transRemarks.getRemarksMsg());
	    	if (seqNo.equalsIgnoreCase("ON")) {
	    		qy.setString("ORDER_NO", transRemarks.getOrderNo());
	    	} else if (seqNo.equalsIgnoreCase("SN")) {
	    		qy.setString("SESSION", transRemarks.getSession());
	    	} else {
	    		qy.setString("SESSION", transRemarks.getSession());
	    	}
	    	qy.executeUpdate();
	    	tx.commit();
	    	result = true;
	    } catch (Exception ex) {
	    	log = new Log();
	    	if (tx != null) {tx.rollback();}	
	    	result = false;
			log.logMessage("Exception found in  edit transRemarksUpdate : "+ex, "error", EasyPaymentDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
		return result;
	}

}
