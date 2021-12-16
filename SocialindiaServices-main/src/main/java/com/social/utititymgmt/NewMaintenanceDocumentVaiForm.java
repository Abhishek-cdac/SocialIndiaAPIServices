package com.social.utititymgmt;

import java.io.PrintWriter;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.DoctypMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.DocumentUtilitiDaoServices;
import com.socialindiaservices.services.DocumentUtilitiServices;
import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.DocumentShareTblVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;
import com.socialindiaservices.vo.NotificationTblVO;

public class NewMaintenanceDocumentVaiForm extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicefor;
	CommonUtils common = new CommonUtils();
	private DocumentUtilitiServices docHibernateUtilService = new DocumentUtilitiDaoServices();
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	byte conbytetoarr[] = new byte[1024];

	public String execute() {
		Log log = new Log();
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		// String lsvSlQry = null;
		String ivrservicecode = null;
		String ivrCurntusridstr = null;
		Integer ivrEntryByusrid = 0;
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					DocumentManageTblVO documentmng = new DocumentManageTblVO();
					DocumentShareTblVO documentShare = new DocumentShareTblVO();
					MaintenanceFeeTblVO maintanencefee = new MaintenanceFeeTblVO();
					UserMasterTblVo userdet = new UserMasterTblVo();
					UserMasterTblVo sharedet = new UserMasterTblVo();
					GroupMasterTblVo groupdet = new GroupMasterTblVo();
					DoctypMasterTblVO docmasdet = new DoctypMasterTblVO();
					NotificationTblVO notificationdet=new NotificationTblVO();

					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");					
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"currentloginid");					
					if (ivrEntryByusrid != null) {
					} else {
						ivrEntryByusrid = 0;
					}

					locObjRecvdataJson = (JSONObject) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					int userId = (int) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "userId");
					int usrTyp = (int) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "usrTyp");
					int docTypId = (int) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "docTypId");
					userdet.setUserId(userId);
					System.out.println("userId ------------ "+userId);
					groupdet.setGroupCode(usrTyp);
					// userdet.setGroupCode(groupdet);
					docmasdet.setIvrBnDOC_TYP_ID(docTypId);	
					JSONArray docShareId = (JSONArray) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"docShareId");
					int docSubFolder = (int) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"docSubFolder");
					String subject = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"subject");
					String descr = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson, "descr");
					int emailStatus = (int) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"emailStatus");

					int docsiz = docShareId.length();
					Date curDate = new Date();
					float serviceCharge=0;
					float municipalTax=0;
					float penalty=0;
					float waterCharge=0;
					float nonOccupancyCharge=0;
					float culturalCharge=0;
					float sinkingFund=0;
					float repairFund=0;
					float insuranceCharges=0;
					float playZoneCharge=0;
					float majorRepairFund=0;
					float interest=0;
					float totalval=0;

					String mtserviceCharge= (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"serviceCharge");
					String billDate = (String) Commonutility.toHasChkJsonRtnValObj(
							locObjRecvdataJson, "billDate");
					String mtmunicipalTax = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"municipalTax");
					String mtpenalty = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"penalty");
					String mtwaterCharge = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"waterCharge");
					String mtnonOccupancyCharge = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"nonOccupancyCharge");
					String mtculturalCharge = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"culturalCharge");
					String mtsinkingFund = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"sinkingFund");
					String mtrepairFund = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"repairFund");
					String mtinsuranceCharges = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"insuranceCharges");
					String mtplayZoneCharge = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"playZoneCharge");
					String mtmajorRepairFund = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"majorRepairFund");
					String mtinterest = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"interest");
					String total = (String) Commonutility
							.toHasChkJsonRtnValObj(locObjRecvdataJson,
									"total");
					
					if(mtserviceCharge!=null && mtserviceCharge.length()>0){
						serviceCharge=Float.parseFloat(mtserviceCharge);
					}
					if(mtmunicipalTax!=null && mtmunicipalTax.length()>0){
						municipalTax=Float.parseFloat(mtmunicipalTax);
					}
					if(mtpenalty!=null && mtpenalty.length()>0){
						penalty=Float.parseFloat(mtpenalty);
					}
					if(mtwaterCharge!=null && mtwaterCharge.length()>0){
						waterCharge=Float.parseFloat(mtwaterCharge);
					}
					if(mtnonOccupancyCharge!=null && mtnonOccupancyCharge.length()>0){
						nonOccupancyCharge=Float.parseFloat(mtnonOccupancyCharge);
					}
					if(mtculturalCharge!=null && mtculturalCharge.length()>0){
						culturalCharge=Float.parseFloat(mtculturalCharge);
					}
					if(mtsinkingFund!=null && mtsinkingFund.length()>0){
						sinkingFund=Float.parseFloat(mtsinkingFund);
					}
					if(mtrepairFund!=null && mtrepairFund.length()>0){
						repairFund=Float.parseFloat(mtrepairFund);
					}
					if(mtinsuranceCharges!=null && mtinsuranceCharges.length()>0){
						insuranceCharges=Float.parseFloat(mtinsuranceCharges);
					}
					if(mtplayZoneCharge!=null && mtplayZoneCharge.length()>0){
						playZoneCharge=Float.parseFloat(mtplayZoneCharge);
					}
					if(mtmajorRepairFund!=null && mtmajorRepairFund.length()>0){
						majorRepairFund=Float.parseFloat(mtmajorRepairFund);
					}
					if(mtinterest!=null && mtinterest.length()>0){
						interest=Float.parseFloat(mtinterest);
					}
					if(total!=null && total.length()>0){
						totalval=Float.parseFloat(total);
					}

					Date bilDt=common.stringTODate(billDate);
					String filename=common.getUniqStringueDateTime();
					maintanencefee.setUserId(userdet);
					maintanencefee.setServiceCharge(serviceCharge);
					maintanencefee.setBillDate(bilDt);
					maintanencefee.setMunicipalTax(municipalTax);
					maintanencefee.setPenalty(penalty);
					maintanencefee.setWaterCharge(waterCharge);
					maintanencefee.setNonOccupancyCharge(nonOccupancyCharge);
					maintanencefee.setCulturalCharge(culturalCharge);
					maintanencefee.setSinkingFund(sinkingFund);
					maintanencefee.setRepairFund(repairFund);
					maintanencefee.setInsuranceCharges(insuranceCharges);
					maintanencefee.setPlayZoneCharge(playZoneCharge);
					maintanencefee.setMajorRepairFund(majorRepairFund);
					maintanencefee.setInterest(interest);
					maintanencefee.setTotbillamt(totalval);
					maintanencefee.setEntryDatetime(curDate);
					//maintanencefee.setUploadFileName(filename+".pdf");
					maintanencefee.setEntryBy(userdet);
					maintanencefee.setStatusFlag(1);
					maintanencefee.setPaidStatus(0);
					maintanencefee.setDataUploadFrom(1);
					docHibernateUtilService.insertMaintenanceFeeTbl(maintanencefee,session);
					
					System.out.println("------------Inserted into Maintenance Table-----------------");
					documentmng.setUserId(userdet);
					documentmng.setUsrTyp(groupdet);
					documentmng.setDocTypId(docmasdet);
					documentmng.setMaintenanceId(maintanencefee);
					documentmng.setDocSubFolder(docSubFolder);
					documentmng.setDocDateFolderName(common.getDateYYMMDDFormat());
					documentmng.setSubject(subject);
					documentmng.setDescr(descr);
					documentmng.setEmailStatus(emailStatus);
					documentmng.setStatusFlag(1);
					documentmng.setEntryBy(userdet);
					documentmng.setEntryDatetime(curDate);
					documentmng.setDocFileName(filename+".pdf");
					
					
					if (docsiz > 0) {
						documentmng.setDocFolder(2);
					} else {
						documentmng.setDocFolder(1);
					}
					docHibernateUtilService.insertDocumentManageTbl(documentmng,session);
					System.out.println("------------Inserted into DocumentManage Table-----------------");
					
					int docsharsiz = docShareId.length();
					if (docsharsiz > 0) {
						for (int dk = 0; dk < docsharsiz; dk++) {
							
							sharedet = new UserMasterTblVo();
							int userd = Integer.parseInt(docShareId.get(dk)
									.toString());
							String qur="from UserMasterTblVo where userId="+userd;
							sharedet=docHibernateUtilService.getUserDetailByquery(qur);
							String userpath=Integer.toString(userd);
							documentShare = new DocumentShareTblVO();
							
							String docfold=rb.getString("external.documnet.fldr");
							common.createDirIfNotExist(docfold);
							String privatepath=docfold+rb.getString("external.documnet.private.fldr");
							common.createDirIfNotExist(privatepath);
							String maintfolder=privatepath+rb.getString("external.documnet.maintenancedoc.fldr");
							common.createDirIfNotExist(maintfolder);
							common.createDirIfNotExist(maintfolder+"/web");

							documentShare.setDocumentId(documentmng);
							documentShare.setUserId(sharedet);
							documentShare.setStatus(1);
							documentShare.setEntryBy(userdet);
							documentShare.setEntryDatetime(curDate);
							boolean isshared=docHibernateUtilService.insertDocumentShareTbl(documentShare,session);
							String topath=maintfolder+"web/"+userpath+"/"+common.getDateYYMMDDFormat();
							GenerateMaintenancePdf generaepdf=new GenerateMaintenancePdf();
							boolean fileuploadstatus=generaepdf.generatePdf(topath, filename+".pdf",maintanencefee,documentShare);
							System.out.println("------------File Saved To External Folder-----------------");
							if(isshared){
								
							notificationdet.setUserId(sharedet);
							String randnumber=common.randomString(Integer.parseInt(rb.getString("Notification.random.string.length")));
							notificationdet.setUniqueId(randnumber);
							notificationdet.setReadStatus(Integer.parseInt(rb.getString("Initial.read.Status")));
							notificationdet.setSentStatus(Integer.parseInt(rb.getString("Initial.sent.Status")));
							notificationdet.setStatusFlag(Integer.parseInt(rb.getString("Initial.statusflag")));
							notificationdet.setDescr(rb.getString("notification.document"));		
							notificationdet.setEntryBy(userdet);
							notificationdet.setEntryDatetime(curDate);
							if(Commonutility.checkempty(rb.getString("notification.reflg.document"))){
								notificationdet.setTblrefFlag(Commonutility.stringToInteger(rb.getString("notification.reflg.document")));
							} else {
								notificationdet.setTblrefFlag(3);
							}															
							notificationdet.setTblrefID(Commonutility.insertchkintnull(documentmng.getDocumentId()));
							docHibernateUtilService.insertNotificationTbl(notificationdet,session);
							System.out.println("------------Inserted into Notification Table-----------------");
						}

						}
					} else {
						// no user to share
						System.out.println("------------No user to share the document (No insert in tables)-----------------");
					}
					locObjRspdataJson = new JSONObject();
					locObjRspdataJson.put("servicecode", ivrservicecode);
					serverResponse(ivrservicecode, "0", "R0081", mobiCommon.getMsg("R0081"),locObjRspdataJson);
					tx.commit();
				} else {
					locObjRspdataJson = new JSONObject();
					log.logMessage("Service code : SI6404,"+ getText("request.format.notmach") + "", "info",NewGeneralDocument.class);
					serverResponse("SI6404", "1", "R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}
			} else {
				locObjRspdataJson = new JSONObject();
				log.logMessage("Service code : SI6404," + getText("request.values.empty") + "", "info", NewGeneralDocument.class);
				serverResponse("SI6404", "1", "R0003", mobiCommon.getMsg("R0003"), locObjRspdataJson);
			}
		} catch (Exception e) {
			if(tx!=null){
				tx.rollback();
			}
			System.out.println("Exception found NewMaintenanceDocument.class execute() Method : "+ e);
			locObjRspdataJson = new JSONObject();
			log.logMessage( "Service code : SI6404, Sorry, an unhandled error occurred", "error", NewGeneralDocument.class);
			serverResponse("SI6404", "2", "R0082", mobiCommon.getMsg("R0082"), locObjRspdataJson);
		} finally {
			if(session!=null){
				session.close();
			}
			locObjRecvJson = null;
			locObjRecvdataJson = null;
			locObjRspdataJson = null;
		}
		return SUCCESS;
	}

	private void serverResponse(String serviceCode, String statusCode,
			String respCode, String message, JSONObject dataJson) {
		PrintWriter out = null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response = null;
		response = ServletActionContext.getResponse();
		try {
			out = response.getWriter();
			responseMsg = new JSONObject();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			responseMsg.put("servicecode", serviceCode);
			responseMsg.put("statuscode", statusCode);
			responseMsg.put("respcode", respCode);
			responseMsg.put("message", message);
			responseMsg.put("data", dataJson);
			String as = responseMsg.toString();
			out.print(as);
			out.close();
		} catch (Exception ex) {
			try {
				out = response.getWriter();
				out.print("{\"servicecode\":\"" + serviceCode + "\",");
				out.print("{\"statuscode\":\"2\",");
				out.print("{\"respcode\":\"E0002\",");
				out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
				out.print("{\"data\":\"{}\"}");
				out.close();
				ex.printStackTrace();
			} catch (Exception e) {
			} finally {
			}
		}
	}

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	public String getIvrservicefor() {
		return ivrservicefor;
	}

	public void setIvrservicefor(String ivrservicefor) {
		this.ivrservicefor = ivrservicefor;
	}
	
	

}
