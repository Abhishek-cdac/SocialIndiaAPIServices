package com.mobi.easypayvo.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.easypayvo.PassportTblVo;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class PassportDaoServices implements PassportDao {
	Log log = new Log();
	
	@Override
	public List<PassportTblVo> passportListDetail(PassportTblVo passportObj) {
		// TODO Auto-generated method stub
		List<PassportTblVo> passprtObj = new ArrayList<PassportTblVo>();
		Session session = HibernateUtil.getSession();
		try {
			Query qy = session.createQuery("from PassportTblVo where status=:STATUS and usrId=:USR_ID");
			qy.setInteger("STATUS", 1);
			qy.setInteger("USR_ID", passportObj.getUsrId().getUserId());
			passprtObj = qy.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" getCarPoolingList======" + ex);
			log.logMessage("CarPoolDaoServices Exception getCarPoolingList : "
							+ ex, "error", PassportDaoServices.class);
		} finally {
			if (session != null) {
				session.flush();
				session.clear();
				session.close();
				session = null;
			}
		}
		return passprtObj;
	}

	@Override
	public JSONArray passportDetailsPack(List<PassportTblVo> listObj) {
		// TODO Auto-generated method stub
		JSONArray returnJsonData = new JSONArray();
		JSONObject finalJsonarr = new JSONObject();
		try {
			if (listObj != null) {
				PassportTblVo passListObj;
				
				for (Iterator<PassportTblVo> it = listObj.iterator(); it.hasNext(); ) {
					passListObj = it.next();
					finalJsonarr = new JSONObject();
					if (passListObj.getPassId() != 0 && passListObj.getPassId() != null) {
						finalJsonarr.put("passid",Commonutility.intToString(passListObj.getPassId()));
					} else {
						finalJsonarr.put("passid","");
					}
					
					finalJsonarr.put("profile_name",Commonutility.stringToStringempty(passListObj.getProfileName()));
					
					finalJsonarr.put("mobile_no",Commonutility.stringToStringempty(passListObj.getMobileNumber()));
					
					finalJsonarr.put("dth_no",Commonutility.stringToStringempty(passListObj.getDthConsumerNumber()));
					
					finalJsonarr.put("datacard_no",Commonutility.stringToStringempty(passListObj.getDatacardConsumerNumber()));
					
					finalJsonarr.put("gas_no",Commonutility.stringToStringempty(passListObj.getGasConsumerNumber()));
					
					finalJsonarr.put("electricity_no",Commonutility.stringToStringempty(passListObj.getElectricityConsumerNumber()));
					
					finalJsonarr.put("landline_no",Commonutility.stringToStringempty(passListObj.getLandlineNo()));
					
					finalJsonarr.put("broadband_no",Commonutility.stringToStringempty(passListObj.getBroadbandNumber()));
					
					finalJsonarr.put("bankacc_no",Commonutility.stringToStringempty(passListObj.getBankAccountNumber()));
					
					finalJsonarr.put("reaccount_no",Commonutility.stringToStringempty(passListObj.getReEnterAccountNumber()));
					
					finalJsonarr.put("bankacc_name",Commonutility.stringToStringempty(passListObj.getBankAccountName()));
					
					finalJsonarr.put("accountType",Commonutility.stringToStringempty(passListObj.getAccountType()));
					
					finalJsonarr.put("bank_name",Commonutility.stringToStringempty(passListObj.getBankName()));
					
					finalJsonarr.put("ifsc_name",Commonutility.stringToStringempty(passListObj.getIfscName()));
					
					finalJsonarr.put("virtual_payaddr",Commonutility.stringToStringempty(passListObj.getUpiIdVirtualPaymentAddress()));
					
					if (passListObj.getUsrId() != null) {
						finalJsonarr.put("rid",Commonutility.intToString(passListObj.getUsrId().getUserId()));
					} else {
						finalJsonarr.put("rid","");
					}
					
					returnJsonData.put(finalJsonarr);
				}
			} else {
				returnJsonData = null;
			}

		} catch (Exception ex) {
			log.logMessage("Exception found in publish list:" + ex, "error",
					PassportDaoServices.class);
			returnJsonData = null;
		}
		return returnJsonData;
	}

	@Override
	public int insetPassportData(PassportTblVo passportObj) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		int retUniqId = -1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();			
			session.save(passportObj);
			retUniqId = passportObj.getPassId();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log.logMessage("Exception insetPublishSkill: "+e, "error",PassportDaoServices.class);
			retUniqId=-1;
		} finally {
			if(session!=null){session.close();session=null;}
			if(tx!=null){tx=null;}		
		}
		return retUniqId;
	}

	@Override
	public boolean editPassportData(PassportTblVo passportObj) {
		// TODO Auto-generated method stub
		Session session = null;
	    Transaction tx = null;
	    Log log = null;
	    Query qy = null;boolean result = false;
	    try {
	    	log = new Log();
	    	System.out.println("editPublishSkill Start.");
	    	session = HibernateUtil.getSession();
	    	tx = session.beginTransaction();
	    	String updateQry = "";
	    	if (Commonutility.checkempty(passportObj.getProfileName())) {
	    		updateQry +="profileName=:PROFILE_NAME,";
	    	}
	    	if (Commonutility.checkempty(passportObj.getMobileNumber())) {
	    		updateQry +="mobileNumber=:MOBILE_NUMBER,";
	    	}
	    	if (Commonutility.checkempty(passportObj.getDthConsumerNumber())) {
	    		updateQry +="dthConsumerNumber=:DTH_CONSUMER_NUMBER,";
	    	}
	    	if (Commonutility.checkempty(passportObj.getDatacardConsumerNumber())) {
	    		updateQry +="datacardConsumerNumber=:DATACARD_CONSUMER_NUMBER,";
	    	}
	    	if (Commonutility.checkempty(passportObj.getGasConsumerNumber())) {
	    		updateQry +="gasConsumerNumber=:GAS_CONSUMER_NUMBER,";
	    	}
	    	if (Commonutility.checkempty(passportObj.getElectricityConsumerNumber())) {
	    		updateQry +="electricityConsumerNumber=:ELECTRICITY_CONSUMER_NUMBER,";
	    	}
	    	if (Commonutility.checkempty(passportObj.getLandlineNo())) {
	    		updateQry +="landlineNo=:LANDLINE_NO,";
	    	}
	    	if (Commonutility.checkempty(passportObj.getBroadbandNumber())) {
	    		updateQry +="broadbandNumber=:BROADBAND_NUMBER,";
	    	}
	    	if (Commonutility.checkempty(passportObj.getBankAccountNumber())) {
	    		updateQry +="bankAccountNumber=:BANK_ACCOUNT_NUMBER,";
	    	}
	    	if (Commonutility.checkempty(passportObj.getReEnterAccountNumber())) {
	    		updateQry +="reEnterAccountNumber=:RE_ENTER_ACCOUNT_NUMBER,";
	    	}
	    	if (Commonutility.checkempty(passportObj.getBankAccountName())) {
	    		updateQry +="bankAccountName=:BANK_ACCOUNT_NAME,";
	    	}
	    	if (Commonutility.checkempty(passportObj.getAccountType())) {
	    		updateQry +="accountType=:ACCOUNT_TYPE,";
	    	}
	    	if (Commonutility.checkempty(passportObj.getBankName())) {
	    		updateQry +="bankName=:BANK_NAME,";
	    	}
	    	if (Commonutility.checkempty(passportObj.getIfscName())) {
	    		updateQry +="ifscName=:IFSC_NAME,";
	    	}
	    	if (Commonutility.checkempty(passportObj.getUpiIdVirtualPaymentAddress())) {
	    		updateQry +="upiIdVirtualPaymentAddress=:UPI_ID_VIRTUAL_PAYMENT_ADDRESS,";
	    	}
	    	qy = session.createQuery("update PassportTblVo set " + updateQry + " modifyDateTime=:MODIFY_DATE_TIME,entryBy=:ENTRY_BY where "
	    			+ " passId=:PASS_ID and usrId.userId=:USR_ID and status=:STATUS");
	    	qy.setTimestamp("MODIFY_DATE_TIME", Commonutility.enteyUpdateInsertDateTime());
	    	qy.setInteger("ENTRY_BY", passportObj.getEntryBy());
	    	qy.setInteger("STATUS", 1);
	    	qy.setInteger("PASS_ID", passportObj.getPassId());
	    	qy.setInteger("USR_ID", passportObj.getUsrId().getUserId());
	    	if (Commonutility.checkempty(passportObj.getProfileName())) {
	    		qy.setString("PROFILE_NAME", passportObj.getProfileName());
	    	}
	    	if (Commonutility.checkempty(passportObj.getMobileNumber())) {
	    		qy.setString("MOBILE_NUMBER", passportObj.getMobileNumber());
	    	}
	    	if (Commonutility.checkempty(passportObj.getDthConsumerNumber())) {
	    		qy.setString("DTH_CONSUMER_NUMBER", passportObj.getDthConsumerNumber());
	    	}
	    	if (Commonutility.checkempty(passportObj.getDatacardConsumerNumber())) {
	    		qy.setString("DATACARD_CONSUMER_NUMBER", passportObj.getDatacardConsumerNumber());
	    	}
	    	if (Commonutility.checkempty(passportObj.getGasConsumerNumber())) {
	    		qy.setString("GAS_CONSUMER_NUMBER", passportObj.getGasConsumerNumber());
	    	}
	    	if (Commonutility.checkempty(passportObj.getElectricityConsumerNumber())) {
	    		qy.setString("ELECTRICITY_CONSUMER_NUMBER", passportObj.getElectricityConsumerNumber());
	    	}
	    	if (Commonutility.checkempty(passportObj.getLandlineNo())) {
	    		qy.setString("LANDLINE_NO", passportObj.getLandlineNo());
	    	}
	    	if (Commonutility.checkempty(passportObj.getBroadbandNumber())) {
	    		qy.setString("BROADBAND_NUMBER", passportObj.getBroadbandNumber());
	    	}
	    	if (Commonutility.checkempty(passportObj.getBankAccountNumber())) {
	    		qy.setString("BANK_ACCOUNT_NUMBER", passportObj.getBankAccountNumber());
	    	}
	    	if (Commonutility.checkempty(passportObj.getReEnterAccountNumber())) {
	    		qy.setString("RE_ENTER_ACCOUNT_NUMBER", passportObj.getReEnterAccountNumber());
	    	}
	    	if (Commonutility.checkempty(passportObj.getBankAccountName())) {
	    		qy.setString("BANK_ACCOUNT_NAME", passportObj.getBankAccountName());
	    	}
	    	if (Commonutility.checkempty(passportObj.getAccountType())) {
	    		qy.setString("ACCOUNT_TYPE", passportObj.getAccountType());
	    	}
	    	if (Commonutility.checkempty(passportObj.getBankName())) {
	    		qy.setString("BANK_NAME", passportObj.getBankName());
	    	}
	    	if (Commonutility.checkempty(passportObj.getIfscName())) {
	    		qy.setString("IFSC_NAME", passportObj.getIfscName());
	    	}
	    	if (Commonutility.checkempty(passportObj.getUpiIdVirtualPaymentAddress())) {
	    		qy.setString("UPI_ID_VIRTUAL_PAYMENT_ADDRESS", passportObj.getUpiIdVirtualPaymentAddress());
	    	}
	    	qy.executeUpdate();
	    	tx.commit();
	    	result = true;
	    } catch (Exception ex) {
	    	log = new Log();
	    	if (tx != null) {tx.rollback();}	
	    	result = false;
			 log.logMessage("Exception found in  editPublishSkill : "+ex, "error",PassportDaoServices.class);
	    } finally {
	    	if(session!=null){session.flush();session.clear(); session.close();session = null;}
			log = null; qy = null;
	    }
		return result;
	}

}
