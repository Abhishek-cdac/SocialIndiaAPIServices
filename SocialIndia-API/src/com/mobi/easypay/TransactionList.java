package com.mobi.easypay;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.easypayvo.MvpTransactionTbl;
import com.mobi.easypayvo.persistence.EasyPaymentDao;
import com.mobi.easypayvo.persistence.EasyPaymentDaoServices;
import com.mobi.easypayvo.persistence.MaintenanceFeeDao;
import com.mobi.easypayvo.persistence.MaintenanceFeeDaoServices;
import com.mobile.facilityBooking.FacilityDao;
import com.mobile.facilityBooking.FacilityDaoServices;
import com.mobile.familymember.familyMemberList;
import com.mobile.infolibrary.InfoLibraryDao;
import com.mobile.infolibrary.InfoLibraryDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.paswordservice.Forgetpassword;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;

public class TransactionList extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	InfoLibraryDao infoLibrary=new InfoLibraryDaoServices();
	FacilityDao facilityhbm=new FacilityDaoServices();
	CommonUtils commjvm=new CommonUtils();
	
	public String execute(){
		System.out.println("************mobile Transaction List services******************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		String servicecode="";
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String referenceNumber = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "reference_number");
				String locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
				String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");
				String paymentStatus = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "payment_status");
				String fromDate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "from_date");
				String toDate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "to_date");
				String serviceType = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "service_type");
				String statementType = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "statement_type");
				
				if (Commonutility.checkempty(servicecode)) {
					if (servicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
						
					} else {
						String[] passData = { getText("service.code.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("Service code cannot be empty")));
				}
				if (Commonutility.checkempty(townshipKey)) {
					if (townshipKey.trim().length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
						
					} else {
						String[] passData = { getText("townshipid.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
				}
				if (Commonutility.checkempty(societykey)) {
					if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {
						
					} else {
						String[] passData = { getText("society.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.error")));
				}
				
				if (locObjRecvdataJson !=null) {
					if (Commonutility.checkempty(rid)) {
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
					}
				}
				
				if(flg){
					if (Commonutility.checkempty(locTimeStamp)) {
					} else {
						locTimeStamp=Commonutility.timeStampRetStringVal();
					}
					
					if(startlimit!=null && startlimit.length()>0){}else{
						startlimit="0";
					}
					
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
			System.out.println("********result*****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			Common locCommonObj = new CommonDao();
			CommonUtils commjvm=new CommonUtils();
			
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
			int societyId=userMst.getSocietyId().getSocietyId();
			
			EasyPaymentDao easyPayhbm=new EasyPaymentDaoServices();
			List<MvpTransactionTbl> transactionListObj = new ArrayList<MvpTransactionTbl>();
			String searchField="";
			int feedTypFlg = 0;
			if(referenceNumber!=null && referenceNumber.length()>0){
				
				searchField=" where orderNo like ('%"+referenceNumber+"%') ";
			}
			if(paymentStatus!=null && paymentStatus.length()>0){
				
				if(searchField.length()>0){
				searchField+=" and txnDatetime='"+paymentStatus+"' ";
				}else{
					searchField+=" where txnDatetime='"+paymentStatus+"' ";
				}
			}
			
			if(fromDate.length() >0 && toDate.length() >0){
				if(searchField.length()>0){
					searchField += " and DATE(txnDatetime) between date('" + fromDate + "')  and date('" + toDate + "') ";
					}else{
						searchField += " where DATE(txnDatetime) between date('" + fromDate + "')  and date('" + toDate + "') ";
					}
			}else if(fromDate.length() >0){
				if(searchField.length()>0){
					searchField += " and DATE(txnDatetime) >= date('" + fromDate + "' ) ";
					}else{
						searchField += " where DATE(txnDatetime) >= date('" + fromDate + "' ) ";
					}
			}else if(toDate.length() >0){
				if(searchField.length()>0){
					searchField += " and DATE(txnDatetime) <= date('" + toDate + "') ";
					}else{
						searchField += " where DATE(txnDatetime) <= date('" + toDate + "') ";
					}
			}
			CommonMobiDao commonHbm=new CommonMobiDaoService();
			String query="select count(*) from MvpTransactionTbl  "+searchField;
			int totalcnt=commonHbm.getTotalCountQuery(query);
			if(statementType!=null && statementType.equalsIgnoreCase("1")){
				if(totalcnt>10){
					totalcnt=10;
				}
			}
			
			query="from MvpTransactionTbl "+searchField+"  order by modifyDateTime desc";
			
			transactionListObj=easyPayhbm.getTransactionList(query,startlimit,getText("total.row"), locTimeStamp);
			JSONObject finalJsonarr=new JSONObject();
			JSONArray jArray =new JSONArray();
			
			if(transactionListObj!=null &&  transactionListObj.size()>0){
				MvpTransactionTbl transactinObj;
				locObjRspdataJson= new JSONObject();
			for(Iterator<MvpTransactionTbl> it=transactionListObj.iterator();it.hasNext();){
				transactinObj=it.next();
				
				
				finalJsonarr=new JSONObject();
				finalJsonarr.put("transaction_unique_id",""+transactinObj.getTranId());
				if(transactinObj.getFinalStatus() !=null && transactinObj.getFinalStatus()==1){
					finalJsonarr.put("payment_status","Pending");
				}else if(transactinObj.getFinalStatus() !=null && transactinObj.getFinalStatus()==0){
					finalJsonarr.put("payment_status","Success");
				}else if(transactinObj.getFinalStatus() !=null && (transactinObj.getFinalStatus()==2 || transactinObj.getFinalStatus()==2)){
					finalJsonarr.put("payment_status","Error");
				}else{
					finalJsonarr.put("payment_status","");
				}
				finalJsonarr.put("reference_no",""+transactinObj.getOrderNo());
				finalJsonarr.put("amount",""+transactinObj.getTxnAmount());
				
				finalJsonarr.put("transaction_date",""+locCommonObj.getDateobjtoFomatDateStr(transactinObj.getTxnDatetime(), "yyyy-MM-dd"));
				finalJsonarr.put("transaction_time",""+locCommonObj.getDateobjtoFomatDateStr(transactinObj.getTxnDatetime(), "HH:mm:ss"));
				finalJsonarr.put("service_type",""+transactinObj.getServiceType());
				if (Commonutility.checkempty(transactinObj.getRemarksMsg())) {
					finalJsonarr.put("remarks",transactinObj.getServiceType());
				} else {
					finalJsonarr.put("remarks","");
				}
				
				//get Bill No.
				String billNo = getBillNo(transactinObj.getMaintenanceId(),locTimeStamp);
				finalJsonarr.put("bill_no",billNo);
				
				jArray.put(finalJsonarr);
			}
			
			locObjRspdataJson.put("transaction_detail", jArray);
			locObjRspdataJson.put("timestamp",locTimeStamp);
			locObjRspdataJson.put("totalrecords",totalcnt);
			serverResponse(servicecode,"00","R0001",getText("R0001"),locObjRspdataJson);
			
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0025",getText("R0025"),locObjRspdataJson);
				}
			
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0029",getText("R0029"),locObjRspdataJson);
			}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0015",getText("R0015"),locObjRspdataJson);		
			}
			
			
			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",getText("R0002"),locObjRspdataJson);
			}
		}else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", Forgetpassword.class);
			serverResponse(servicecode,"01","R0016",getText("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======EventSearchList====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", familyMemberList.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",getText("R0002"),locObjRspdataJson);
		}
		
		return SUCCESS;
	}

	
	private String getBillNo(Integer maintenanceId,String locTimeStamp) {
		String billNo = "";
		MaintenanceFeeDao dao = new MaintenanceFeeDaoServices();
		String searchQury = " and maintenanceId = " + maintenanceId;
		
		//maintenanceBillSearchList(String searchQury, String startlimit, String totalrow,String locTimeStamp) 
		List<MaintenanceFeeTblVO> list = dao.maintenanceBillSearchList(searchQury, "0", "1", locTimeStamp);
		if(list!=null && list.size()==1)
			billNo = list.get(0).getBillno()+"";
		
		Commonutility.toWriteConsole("getBillNo >>>>>>>>>>>>>>>>> searchQury:"+searchQury+" maintenanceId:"+maintenanceId +" billNo:"+billNo);
		
		return billNo;
	}


	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson)
	{
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();		
		try {
			out = response.getWriter();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			mobiCommon mobicomn=new mobiCommon();
			String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
			out.print(as);
			out.close();
		} catch (Exception ex) {
			try{
			out = response.getWriter();
			out.print("{\"servicecode\":\"" + serviceCode + "\",");
			out.print("{\"statuscode\":\"2\",");
			out.print("{\"respcode\":\"E0002\",");
			out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
			out.print("{\"data\":\"{}\"}");
			out.close();
			ex.printStackTrace();
			}catch(Exception e){}finally{}
		}
	}

	public String getIvrparams() {
		return ivrparams;
	}
	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	
}
