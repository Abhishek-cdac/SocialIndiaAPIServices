package com.mobi.easypay;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.mobi.easypayvo.persistence.EasyPaymentDao;
import com.mobi.easypayvo.persistence.EasyPaymentDaoServices;
import com.mobile.facilityBooking.FacilityDao;
import com.mobile.facilityBooking.FacilityDaoServices;
import com.mobile.familymember.familyMemberList;
import com.mobile.infolibrary.InfoLibraryDao;
import com.mobile.infolibrary.InfoLibraryDaoServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;

public class TansactionListPDF extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	InfoLibraryDao infoLibrary=new InfoLibraryDaoServices();
	FacilityDao facilityhbm=new FacilityDaoServices();
	CommonUtils commjvm=new CommonUtils();
	private HashMap reportParams = new HashMap();
	private ArrayList transactionListObj = new ArrayList();
	
	public String execute(){
		System.out.println("************mobile Transaction report List PDF services******************");
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				String referenceNumber = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "reference_number");
				String paymentStatus = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "payment_status");
				String fromDate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "from_date");
				String toDate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "to_date");
				
					
			String locTimeStamp=Commonutility.timeStampRetStringVal();
			
			EasyPaymentDao easyPayhbm=new EasyPaymentDaoServices();
			
			String searchField="";
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
			
			String query="from MvpTransactionTbl "+searchField+"  order by modifyDateTime desc";
			
			transactionListObj=(ArrayList) easyPayhbm.getTransactionListForReport(query, locTimeStamp);
			if(transactionListObj!=null &&  transactionListObj.size()>0){
				System.out.println("transactionListObj.size()----------------"+transactionListObj.size());
				
				String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/transactionListXls.jrxml");
				String lvrJasper = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/transactionListXls.jasper");	
				
				JasperCompileManager
				.compileReportToFile(lvrJrxml,lvrJasper);
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
				transactionListObj);
			
				}else{
					return "input";
				}
		}else{
			return "input";
		}
		}catch(Exception ex){
			System.out.println("=======EventSearchList====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", familyMemberList.class);
			return "input";
		}
		
		return SUCCESS;
	}

	public String getIvrparams() {
		return ivrparams;
	}
	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}
	public HashMap getReportParams() {
		return reportParams;
	}

	public void setReportParams(HashMap reportParams) {
		this.reportParams = reportParams;
	}

	public ArrayList getTransactionListObj() {
		return transactionListObj;
	}
	public void setTransactionListObj(ArrayList transactionListObj) {
		this.transactionListObj = transactionListObj;
	}

	
}