package com.pack.billpayment;

import java.util.Date;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import com.mobi.easypayvo.MvpUtilityPayTbl;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class ValidatonUtility {
	
	public static void main(String ad[]){
	//	toCheckPerdaylimit("1000", 102.0, "5464");
		tocheckDuplicateTimegap ("5464","3","Seconds");
	}
	
	public static boolean toCheckPerdaylimit(String prmDaylimitonDBval, Double prmRcamtusr, String prmRcnumber) {//, String prmRcnumber
		boolean lvrRtnval = false;
		
		Log logWrite = null;	
		Session lvrSession = null;		
		Query lvrQry = null;
		String lvrSltqry = null;
		Iterator locObjutlpayitr=null;
		MvpUtilityPayTbl lvrUtilpayobj= null;
		Double lvrExitingusrrechareamt = 0.0;
		Double lvrDaillimitamtDb = 0.0;// limit allowed
		Double lvrOverrcperdayAmt = 0.0;
		try {
			String crntdate =Commonutility.getCurrentDate("yyyy-MM-dd");
			lvrSession = HibernateUtil.getSession();
			logWrite = new Log ();
			logWrite.logMessage("Step 1 : ValidatonUtility toCheckPerdaylimit() Called [Start]", "info", ValidatonUtility.class);
			Commonutility.toWriteConsole("Step 1 : ValidatonUtility toCheckPerdaylimit() Called [Start]");
			lvrSltqry = "from MvpUtilityPayTbl where number = '"+prmRcnumber+"' and finalStatus = 0 and DATE(entryDatetime) = '"+crntdate+"'";
			lvrQry = lvrSession.createQuery(lvrSltqry);
			locObjutlpayitr = lvrQry.list().iterator();
			while ( locObjutlpayitr.hasNext() ) {
				lvrUtilpayobj = (MvpUtilityPayTbl) locObjutlpayitr.next();
				lvrExitingusrrechareamt += lvrUtilpayobj.getAmount();	// existing amount						
			}
			if(Commonutility.checkempty(prmDaylimitonDBval)){
				lvrDaillimitamtDb = Double.parseDouble(prmDaylimitonDBval);				
			}
			
			if(prmRcamtusr!=null && lvrExitingusrrechareamt!=null ){
				lvrOverrcperdayAmt = prmRcamtusr+lvrExitingusrrechareamt;
			}
			
			Commonutility.toWriteConsole("Existing recharge amount : "+lvrExitingusrrechareamt);
			Commonutility.toWriteConsole("Current recharge amount : "+prmRcamtusr);
			Commonutility.toWriteConsole("Overall recharge amount : "+lvrOverrcperdayAmt);
			Commonutility.toWriteConsole("lvrDaillimitamtDb recharge amount : "+lvrDaillimitamtDb);
			if(lvrOverrcperdayAmt!=null && lvrDaillimitamtDb!=null && lvrOverrcperdayAmt <= lvrDaillimitamtDb){
				lvrRtnval = true;
			} else {
				lvrRtnval = false;
			}	
			Commonutility.toWriteConsole("Status : "+lvrRtnval);
			logWrite.logMessage("Step 2 : ValidatonUtility toCheckPerdaylimit() Called [End]", "info", ValidatonUtility.class);
			Commonutility.toWriteConsole("Step 2 : ValidatonUtility toCheckPerdaylimit() Called [End]");
		} catch (Exception e) {
			lvrRtnval = false;
			Commonutility.toWriteConsole("Exception found in "+ Thread.currentThread().getStackTrace()[1].getMethodName() +"() : " + e);
		} finally {
			if(lvrSession!=null) {lvrSession.flush();lvrSession.clear();lvrSession.close();lvrSession = null;}
			logWrite = null; lvrQry = null;
		}
		return lvrRtnval;
	}
	
	
	public static boolean tocheckDuplicateTimegap (String prmRcnumber, String prmTimegapCnfg, String prmDuptimegaponCnfg) {
		boolean lvrRtnval = false;
		Log logWrite = null;
		Session lvrSession = null;
		Query lvrQry = null;
		String lvrSltqry = null;
		MvpUtilityPayTbl lvrUtilpayobj= null;
		Date lvrEntrydate = null;
		Date lvrCurrentDate = null;
		long lvrTimggapDB = 0; // Allowed Time Gap 		
		try {
			String crntdate = Commonutility.getCurrentDate("yyyy-MM-dd");
			lvrSession = HibernateUtil.getSession();
			logWrite = new Log ();
			lvrCurrentDate = new Date();			
			logWrite.logMessage("Step 1 : ValidatonUtility tocheckDuplicateTimegap() Called [Start]", "info", ValidatonUtility.class);
			Commonutility.toWriteConsole("Step 1 : ValidatonUtility tocheckDuplicateTimegap() Called [Start]");
			lvrSltqry = "from MvpUtilityPayTbl where number = '"+prmRcnumber+"' and finalStatus = 0 order by entryDatetime desc";
			lvrQry = lvrSession.createQuery(lvrSltqry);
			lvrQry.setMaxResults(1);
			lvrUtilpayobj = (MvpUtilityPayTbl) lvrQry.uniqueResult();
			Commonutility.toWriteConsole("prmDuptimegaponCnfg : "+prmDuptimegaponCnfg);
			Commonutility.toWriteConsole("lvrSltqry : "+lvrSltqry);
			Commonutility.toWriteConsole("prmRcnumber : "+prmRcnumber);
			Commonutility.toWriteConsole("lvrUtilpayobj : "+lvrUtilpayobj);
			if(lvrUtilpayobj!=null && lvrUtilpayobj.getEntryDatetime()!=null){ 
				lvrEntrydate = lvrUtilpayobj.getEntryDatetime();
				long diff = lvrCurrentDate.getTime() - lvrEntrydate.getTime();
			    long diffSeconds = diff / 1000 % 60;
			    long diffMinutes = diff / (60 * 1000) % 60;
			    long diffHours = diff / (60 * 60 * 1000);
			    int diffInDays = (int) ((lvrCurrentDate.getTime() - lvrEntrydate.getTime()) / (1000 * 60 * 60 * 24));
			    Commonutility.toWriteConsole("Differ Day : "+diffInDays);
			    Commonutility.toWriteConsole("Differ Seconds : "+diffSeconds);
			    Commonutility.toWriteConsole("Differ Minutes : "+diffMinutes);
			    Commonutility.toWriteConsole("Differ Hours : "+diffHours);
			    if (Commonutility.checkempty(prmTimegapCnfg)) { 
			    	lvrTimggapDB = Long.parseLong(prmTimegapCnfg);
			    	
			    	if(Commonutility.checkempty(prmDuptimegaponCnfg) && prmDuptimegaponCnfg.equalsIgnoreCase("minutes")) {								
						if (diffMinutes > lvrTimggapDB) {
							lvrRtnval = true;
						} else {
							lvrRtnval = false;
						}			
					} else if(Commonutility.checkempty(prmDuptimegaponCnfg) && prmDuptimegaponCnfg.equalsIgnoreCase("hour")) {
						if (diffHours > lvrTimggapDB) {
							lvrRtnval = true;
						} else {
							lvrRtnval = false;
						}
					} else if(Commonutility.checkempty(prmDuptimegaponCnfg) && prmDuptimegaponCnfg.equalsIgnoreCase("day")) {
						if (diffInDays > lvrTimggapDB) {
							lvrRtnval = true;
						} else {
							lvrRtnval = false;
						}
					} else if(Commonutility.checkempty(prmDuptimegaponCnfg) && prmDuptimegaponCnfg.equalsIgnoreCase("seconds")) {
						if (diffSeconds > lvrTimggapDB) {
							lvrRtnval = true;
						} else {
							lvrRtnval = false;
						}
					} else {
						lvrRtnval = true;
					}
			    	
			    } else {
					lvrRtnval = true;
				}
			} else {
				lvrRtnval = true;
			}
			
			
			Commonutility.toWriteConsole("Status : "+lvrRtnval);	
			logWrite.logMessage("Step 2 : ValidatonUtility tocheckDuplicateTimegap() Called [End]", "info", ValidatonUtility.class);
			Commonutility.toWriteConsole("Step 2 : ValidatonUtility tocheckDuplicateTimegap() Called [End]");
		} catch (Exception e) {
			lvrRtnval = false;
			Commonutility.toWriteConsole("Exception found in "+ Thread.currentThread().getStackTrace()[1].getMethodName() +"() : " + e);
		} finally {
		if(lvrSession!=null) {lvrSession.flush();lvrSession.clear();lvrSession.close();lvrSession = null;}
		logWrite = null; lvrQry = null;
		}
	return lvrRtnval;
	}
}
