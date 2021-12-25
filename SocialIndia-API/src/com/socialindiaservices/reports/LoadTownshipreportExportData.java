package com.socialindiaservices.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.services.ReportDaoServices;
import com.socialindiaservices.services.ReportServices;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class LoadTownshipreportExportData extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList userList = new ArrayList();
	private ReportServices reportDao=new ReportDaoServices();
	private HashMap reportParams = new HashMap();
	private String ivrparams;
	public String execute(){
		JSONObject locObjRecvJson = null;
		String ivrservicecode=null;
		Log logWrtie= null;
		try{
			logWrtie = new Log();
			Map sessionMap = ActionContext.getContext().getSession();
			 if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
				 ivrparams = EncDecrypt.decrypt(ivrparams);
				 boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				 if (ivIsJson) {
					 locObjRecvJson = new JSONObject(ivrparams);
			    		String fromdate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "fromdate");
			    		String todate=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "todate");			    		
			    		String manualsearch="";
						if(fromdate.length() >0 && todate.length() >0){
								manualsearch += " and date(entryDatetime) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')  ";
						}else if(fromdate.length() >0){
								manualsearch += " and date(entryDatetime) >= str_to_date('" + fromdate + "', '%d-%m-%Y') ";
						}else if(todate.length() >0){
								manualsearch += " and date(entryDatetime) <= str_to_date('" + todate + "', '%d-%m-%Y') ";
						}
						String orderBy=" order by modifyDatetime desc";
			String releasereport = (String) sessionMap.get("globquery");
			String locSlqry=null;			
			String authtable = "from TownshipMstTbl where status=1 "+manualsearch+orderBy;
			userList= (ArrayList) reportDao.selectTownshipMgmtByQry(authtable);
		  
			String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/townshipreport/townshipreportPDF.jrxml");
			String lvrJasper = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/townshipreport/townshipreportPDF.jasper");			
			System.out.println("jrxml Path PDF : "+lvrJrxml);
			System.out.println("japer Path PDF : "+lvrJasper);
			logWrtie.logMessage("Township jrxml Path PDF : "+lvrJrxml, "info", LoadMerchantreportExportData.class);
			logWrtie.logMessage("Township japer Path PDF : "+lvrJasper, "info", LoadMerchantreportExportData.class);
			JasperCompileManager.compileReportToFile(lvrJrxml,lvrJasper);
			
			/*
			JasperCompileManager
					.compileReportToFile(
							getText("ApplicationAbsolutePath")
									+ ServletActionContext.getServletContext()
											.getContextPath()
									+ getText("WebContent")
									+ "resources/jsp/PDFCreation/reports/townshipreport/townshipreportPDF.jrxml",
							getText("ApplicationAbsolutePath")
									+ ServletActionContext.getServletContext()
											.getContextPath()
									+ getText("WebContent")
									+ "resources/jsp/PDFCreation/reports/townshipreport/townshipreportPDF.jasper");
			*/
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);
			reportParams.put("externalPath", getText("SOCIAL_INDIA_BASE_URL") + getText("Logo.url"));
			reportParams.put("companyName", getText("report.company.name"));
				 }
			 }
				 
			return SUCCESS;
		}catch (Exception e){
			 AuditTrial.toWriteAudit(getText("TWNPDF0002"), "TWNPDF0002", 2);
			e.printStackTrace();
			return "input";
		}
	}
	public String getUsertXLS(){
		JSONObject locObjRecvJson = null;
		String ivrservicecode=null;
		Log logWrtie= null;
		try{
			logWrtie = new Log();
			Map sessionMap = ActionContext.getContext().getSession();			
			if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
				 ivrparams = EncDecrypt.decrypt(ivrparams);
				 boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				 if (ivIsJson) {
					 locObjRecvJson = new JSONObject(ivrparams);
			    		String fromdate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "fromdate");
			    		String todate=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "todate");			    		
			    		String manualsearch="";
						if(fromdate.length() >0 && todate.length() >0){
								manualsearch += " and date(entryDatetime) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')  ";
						}else if(fromdate.length() >0){
								manualsearch += " and date(entryDatetime) >= str_to_date('" + fromdate + "', '%d-%m-%Y') ";
						}else if(todate.length() >0){
								manualsearch += " and date(entryDatetime) <= str_to_date('" + todate + "', '%d-%m-%Y') ";
						}
						
						String orderBy=" order by modifyDatetime desc";
						String releasereport = (String) sessionMap.get("globquery");
						String locSlqry=null;
						
						String authtable = "from TownshipMstTbl where status=1 "+manualsearch+orderBy;			
						userList= (ArrayList) reportDao.selectTownshipMgmtByQry(authtable);
		  
						String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/townshipreport/townshipreportXLS.jrxml");
						String lvrJasper = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/townshipreport/townshipreportXLS.jasper");			
						System.out.println("jrxml Path XLS : "+lvrJrxml);
						System.out.println("japer Path XLS : "+lvrJasper);
						logWrtie.logMessage("Township jrxml Path XLS : "+lvrJrxml, "info", LoadMerchantreportExportData.class);
						logWrtie.logMessage("Township japer Path XLS : "+lvrJasper, "info", LoadMerchantreportExportData.class);
						JasperCompileManager.compileReportToFile(lvrJrxml,lvrJasper);
						
						/*JasperCompileManager
					.compileReportToFile(
							getText("ApplicationAbsolutePath")
									+ ServletActionContext.getServletContext()
											.getContextPath()
									+ getText("WebContent")
									+ "resources/jsp/PDFCreation/reports/townshipreport/townshipreportXLS.jrxml",
							getText("ApplicationAbsolutePath")
									+ ServletActionContext.getServletContext()
											.getContextPath()
									+ getText("WebContent")
									+ "resources/jsp/PDFCreation/reports/townshipreport/townshipreportXLS.jasper");
			*/
						JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);
						reportParams.put("externalPath", getText("SOCIAL_INDIA_BASE_URL") + getText("Logo.url"));
						reportParams.put("companyName", getText("report.company.name"));
				 }
			 }
			return SUCCESS;
		}catch (Exception e){
			AuditTrial.toWriteAudit(getText("TWNXL0002"), "TWNXL0002", 2);
			e.printStackTrace();
			return "input";
		}
	}
	public ArrayList getUserList() {
		return userList;
	}
	public void setUserList(ArrayList userList) {
		this.userList = userList;
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


}

