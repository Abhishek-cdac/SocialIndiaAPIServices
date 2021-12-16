package com.social.billmaintenance;

import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.common.Common;
import com.social.common.CommonDao;
import com.social.utils.Log;
import com.social.utititymgmt.GenerateMaintenancePdf;
import com.socialindiaservices.vo.DocumentShareTblVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;

public class CommonBillPack {
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	boolean ifsaved=false;
	public boolean pdfGeneration(String lvrJxml) {
		
		Session session = null;
		JSONObject lvrInrJSONObj = null;
		JSONArray lvrEventjsonaryobj = null;
		Iterator lvrObjeventlstitr = null;
		List<Object> lvrObjfunctionlstitr = null;
		MaintenanceFeeTblVO lvrEvnttblvoobj = null;
		JSONObject lvroutdata = new JSONObject();
		String lvSlqry = null;
		String lvSlfunqry = null;
		Common common = null;
		Log logWrite = null;
		try {
			logWrite = new Log();
			Commonutility.toWriteConsole("Step 1 : PDF Created Called [Start]");
			logWrite.logMessage("Step 1 : PDF Created Called [Start]", "info", CommonBillPack.class);
			common = new CommonDao();
			session = HibernateUtil.getSession();
			lvSlqry = "from MaintenanceFeeTblVO where pdfstatus='P' and statusFlag = 1";
			Commonutility.toWriteConsole("Step 2 : select query lvSlqry : "+lvSlqry);
			logWrite.logMessage("Step 2 : select query lvSlqry : "+lvSlqry, "info", CommonBillPack.class);
			lvrObjeventlstitr = session.createQuery(lvSlqry).list().iterator();
			lvrEventjsonaryobj = new JSONArray();
			while (lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj = new JSONObject();
				lvrEvnttblvoobj = (MaintenanceFeeTblVO) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("usrid", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getUserId().getUserId()));
				String userpath = String.valueOf(lvrEvnttblvoobj.getUserId().getUserId());
				lvrInrJSONObj.put("mainId", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getMaintenanceId()));
				lvrInrJSONObj.put("total", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getTotbillamt()));
				lvrInrJSONObj.put("servicescharge", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getServiceCharge()));
				lvrInrJSONObj.put("billdate", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getBillDate()));
				lvrInrJSONObj.put("munipaltax", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getMunicipalTax()));
				lvrInrJSONObj.put("penality", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getPenalty()));
				lvrInrJSONObj.put("watercharge", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getWaterCharge()));
				lvrInrJSONObj.put("nonoccupancy", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getNonOccupancyCharge()));
				lvrInrJSONObj.put("culturecharge", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getCulturalCharge()));
				lvrInrJSONObj.put("skinfund", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getSinkingFund()));
				lvrInrJSONObj.put("repairfund", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getRepairFund()));
				lvrInrJSONObj.put("insurancefund", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getInsuranceCharges()));
				lvrInrJSONObj.put("playzone", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getPlayZoneCharge()));
				lvrInrJSONObj.put("majorrepairfund", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getMajorRepairFund()));
				lvrInrJSONObj.put("interest", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getInterest()));
				lvrInrJSONObj.put("latefee", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getLatefee()));
				lvrInrJSONObj.put("carpark1", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getCarpark1()));
				lvrInrJSONObj.put("carpark2", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getCarpark2()));
				lvrInrJSONObj.put("twowheeler1", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getTwowheeler1()));
				lvrInrJSONObj.put("twowheeler2", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getTwowheeler2()));
				lvrInrJSONObj.put("sundayadjust", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getSundayadjust()));
				lvrInrJSONObj.put("protax", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getProtax()));
				lvrInrJSONObj.put("exceespay", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getExceespay()));
				lvrInrJSONObj.put("clubhouse", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getClubhouse()));
				lvrInrJSONObj.put("ruppess", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getAmountword()));
				
				lvrInrJSONObj.put("previousDues", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getPreviousdue()));
				lvrInrJSONObj.put("appSubscriptionFee", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getAppfees()));
				lvrInrJSONObj.put("arrears", Commonutility.toCheckNullEmpty(lvrEvnttblvoobj.getArrears()));
				
				lvSlfunqry = "select docDateFolderName,docFileName  from DocumentManageTblVO where maintenanceId="+lvrEvnttblvoobj.getMaintenanceId();
				lvrObjfunctionlstitr = session.createQuery(lvSlfunqry).list();
				lvrInrJSONObj.put("doclist", lvrObjfunctionlstitr);	
				JSONArray jary=(JSONArray) Commonutility.toHasChkJsonRtnValObj(lvrInrJSONObj,"doclist");
				String file = "";
				String filename = "";
				for (int i = 0; i < jary.length(); i++) {
					JSONArray temmauto = jary.getJSONArray(i);
					file = (String) temmauto.get(0);
					filename = (String) temmauto.get(1);
				}
				DocumentShareTblVO documentShare = new DocumentShareTblVO();
				UserMasterTblVo userdet = new UserMasterTblVo();
				String username="";
				if(lvrEvnttblvoobj.getUserId().getFirstName()!=null && !lvrEvnttblvoobj.getUserId().getFirstName().equalsIgnoreCase("")){
		        	 username=lvrEvnttblvoobj.getUserId().getFirstName();
		         }
		         if(lvrEvnttblvoobj.getUserId().getLastName()!=null && !lvrEvnttblvoobj.getUserId().getLastName().equalsIgnoreCase("") && !username.equalsIgnoreCase("")){
		        	 username+=" "+lvrEvnttblvoobj.getUserId().getLastName();
		         }else if(lvrEvnttblvoobj.getUserId().getLastName()!=null && !lvrEvnttblvoobj.getUserId().getLastName().equalsIgnoreCase("") && username.equalsIgnoreCase("")){
		        	 username+=lvrEvnttblvoobj.getUserId().getLastName();
		         }
		         if(username!=null){
		        	 username=username.trim();
		         }
		         if(lvrEvnttblvoobj.getUserId().getEmailId()!=null && !lvrEvnttblvoobj.getUserId().getEmailId().equalsIgnoreCase("") && username.equalsIgnoreCase("")){
		        	 username=lvrEvnttblvoobj.getUserId().getEmailId();
		         }
		         if(lvrEvnttblvoobj.getUserId().getMobileNo()!=null && !lvrEvnttblvoobj.getUserId().getMobileNo().equalsIgnoreCase("") && username.equalsIgnoreCase("")){
		        	 username=lvrEvnttblvoobj.getUserId().getMobileNo();
		         }
				
				userdet.setUserId(lvrEvnttblvoobj.getUserId().getUserId());
				documentShare.setUserId(userdet);
				String topath=rb.getString("external.documnet.fldr")+rb.getString("external.documnet.private.fldr")+rb.getString("external.documnet.maintenancedoc.fldr")+rb.getString("external.inner.webpath")+userpath+"/"+file;					
				GenerateMaintenancePdf generaepdf = new GenerateMaintenancePdf();
				boolean fileuploadstatus = generaepdf.generatePdfthread(topath, filename,lvrEvnttblvoobj,documentShare,lvrJxml,username);
				boolean lvrfunmtr=false;
				String updatesql ="update MaintenanceFeeTblVO set pdfstatus='S' where maintenanceId="+lvrEvnttblvoobj.getMaintenanceId()+" and statusFlag=1";
				lvrfunmtr=common.commonUpdate(updatesql);
				Commonutility.toWriteConsole("lvrfunmtr ----------- "+lvrfunmtr);
			}
			Commonutility.toWriteConsole("Step 3 : PDF Created Called [End]");
			logWrite.logMessage("Step 3: PDF Created Called [End]", "info", CommonBillPack.class);				
		}catch(Exception ex){
			Commonutility.toWriteConsole("Step -1: PDF Created Called [Exception] : "+ex);
			logWrite.logMessage("Step -1: PDF Created Called [Exception] : "+ex, "error", CommonBillPack.class);				
		} finally {
			if (session!=null) {session.flush();session.clear();session.close();session = null;} 
		
			lvrInrJSONObj = null;
			lvrEventjsonaryobj = null;
			lvrObjeventlstitr = null;
			lvrObjfunctionlstitr = null;
			lvrEvnttblvoobj = null;
			lvroutdata = new JSONObject();
			lvSlqry = null;
			lvSlfunqry = null;
			common = null;logWrite = null;
		}
		
		return ifsaved;
		
	
		
	}
}
