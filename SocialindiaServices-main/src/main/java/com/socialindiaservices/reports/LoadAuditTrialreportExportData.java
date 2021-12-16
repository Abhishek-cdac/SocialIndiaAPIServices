package com.socialindiaservices.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

public class LoadAuditTrialreportExportData  extends ActionSupport {
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
		Log logWrite = null;
		try{
			//Map sessionMap = ActionContext.getContext().getSession();
			logWrite = new Log();
			logWrite.logMessage("Step 1 : Audit Trail PDF Export Called.[Start]", "info", LoadAuditTrialreportExportData.class);			
			Commonutility.toWriteConsole("Step 1 : Audit Trail PDF Export Called [Start]"); 
			if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
				 ivrparams = EncDecrypt.decrypt(ivrparams);
				 boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				 if (ivIsJson) {
					 
					    locObjRecvJson = new JSONObject(ivrparams);
			    		String fromdate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "fromdate");
			    		String todate=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "todate");
			    		Integer sSoctyId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "sSoctyId");
			    		Integer sTowshipId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "sTowshipId");			    
			    		String manualsearch="";
			    		if(sSoctyId!=null && sSoctyId>0){
							manualsearch += " and ivrBnUserMstrTblobj.societyId.societyId="+sSoctyId;
						}
			    		if(sTowshipId!=null && sTowshipId>0){
							manualsearch += " and ivrBnUserMstrTblobj.societyId.townshipId.townshipId="+sTowshipId;
						}
						if(fromdate.length() >0 && todate.length() >0){
								manualsearch += " and date(ivrBnENTRY_DATETIME) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')  ";
						}else if(fromdate.length() >0){
								manualsearch += " and date(ivrBnENTRY_DATETIME) >= str_to_date('" + fromdate + "', '%d-%m-%Y') ";
						}else if(todate.length() >0){
								manualsearch += " and date(ivrBnENTRY_DATETIME) <= str_to_date('" + todate + "', '%d-%m-%Y') ";
						}
				 
			//String releasereport = (String) sessionMap.get("globquery");
			//String locSlqry=null;
			
			String authtable = "from AuditlogTblVO where 1=1 "+manualsearch+" order by ivrBnMODIFY_DATETIME desc";
			userList = (ArrayList) reportDao.selectuserMstPdf(authtable);
			logWrite.logMessage("Step 2 : Query : "+authtable, "info", LoadAuditTrialreportExportData.class);			
			Commonutility.toWriteConsole("Step 2 : Query : "+authtable);
			String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/auditTrialreport/auditTrialreportPDF.jrxml");
			String lvrJasper = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/auditTrialreport/auditTrialreportPDF.jasper");			
			Commonutility.toWriteConsole("jrxml Path PDF : "+lvrJrxml);
			Commonutility.toWriteConsole("japer Path PDF : "+lvrJasper);
			try{
				JasperCompileManager.compileReportToFile(lvrJrxml,lvrJasper);				
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);				
				reportParams.put("externalPath", System.getenv("SOCIAL_INDIA_BASE_URL") + getText("Logo.url"));				
				reportParams.put("companyName", getText("report.company.name"));				
			}catch(Exception e){System.out.println("Exception Found loadAudittrialreort.class JasperCompileManager: "+e);}finally{}
			/*JasperCompileManager
					.compileReportToFile(
							getText("ApplicationAbsolutePath")
									+ ServletActionContext.getServletContext()
											.getContextPath()
									+ getText("WebContent")
									+ "resources/jsp/PDFCreation/reports/auditTrialreport/auditTrialreportPDF.jrxml",
							getText("ApplicationAbsolutePath")
									+ ServletActionContext.getServletContext()
											.getContextPath()
									+ getText("WebContent")
									+ "resources/jsp/PDFCreation/reports/auditTrialreport/auditTrialreportPDF.jasper");
			*/				
			 	AuditTrial.toWriteAudit(getText("DOWNPDF0002"), "DOWNPDF0002",-1);
					logWrite.logMessage("Step 3 : Audit Trail PDF Export Called.[End]", "info", LoadAuditTrialreportExportData.class);					
					Commonutility.toWriteConsole("Step 3 : Audit Trail PDF Export Called.[End]");
				 } else {
					 logWrite.logMessage("Step 2 : Request Format not correct.", "info", LoadAuditTrialreportExportData.class);					
					 Commonutility.toWriteConsole("Step 2 : Request Format not correct.");
				}
			} else {
				 logWrite.logMessage("Step 2 : Request Data is empty.", "info", LoadAuditTrialreportExportData.class);				
				 Commonutility.toWriteConsole("Step 2 : Request Data is empty.");
			}
				 
			return SUCCESS;
		}catch (Exception e){						
			logWrite.logMessage("Step -1 : Exception Found in "+getClass().getSimpleName()+" : "+e, "error", LoadAuditTrialreportExportData.class);			
			Commonutility.toWriteConsole("Step -1 : Exception Found in "+getClass().getSimpleName()+" : "+e);
			 AuditTrial.toWriteAudit(getText("DOWNPDF0002"), "DOWNPDF0002",-1);
			return SUCCESS;
		} finally{
			locObjRecvJson = null;
			ivrservicecode=null; logWrite = null;
		}
	}
	public String getUsertXLS(){
		JSONObject locObjRecvJson = null;
		String ivrservicecode=null;
		Log logWrite = null;
		try{
			logWrite = new Log();
			logWrite.logMessage("Step 1 : Audit Trail XLS Export Called.[Start]", "info", LoadAuditTrialreportExportData.class);			
			Commonutility.toWriteConsole("Step 1 : Audit Trail XLS Export Called [Start]"); 
			//Map sessionMap = ActionContext.getContext().getSession();
			if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
				 ivrparams = EncDecrypt.decrypt(ivrparams);
				 boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				 if (ivIsJson) {
					 locObjRecvJson = new JSONObject(ivrparams);
			    		String fromdate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "fromdate");
			    		String todate=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "todate");
			    		Integer sSoctyId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "sSoctyId");
			    		Integer sTowshipId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "sTowshipId");			    		
			    		String manualsearch="";
			    		if(sSoctyId!=null && sSoctyId>0){
							manualsearch += " and ivrBnUserMstrTblobj.societyId.societyId="+sSoctyId;
						}
			    		if(sTowshipId!=null && sTowshipId>0){
							manualsearch += " and ivrBnUserMstrTblobj.societyId.townshipId.townshipId="+sTowshipId;
						}
						if(fromdate.length() >0 && todate.length() >0){
								manualsearch += " and date(ivrBnENTRY_DATETIME) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')  ";
						}else if(fromdate.length() >0){
								manualsearch += " and date(ivrBnENTRY_DATETIME) >= str_to_date('" + fromdate + "', '%d-%m-%Y') ";
						}else if(todate.length() >0){
								manualsearch += " and date(ivrBnENTRY_DATETIME) <= str_to_date('" + todate + "', '%d-%m-%Y') ";
						}
						
			
			String authtable = "from AuditlogTblVO where 1=1 "+manualsearch+" order by ivrBnMODIFY_DATETIME desc";
			logWrite.logMessage("Step 2 : Query : "+authtable, "info", LoadAuditTrialreportExportData.class);			
			Commonutility.toWriteConsole("Step 2 : Query : "+authtable);
			userList= (ArrayList) reportDao.selectuserMstPdf(authtable);		  
			String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/auditTrialreport/auditTrialreportXLS.jrxml");
			String lvrJasper = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/auditTrialreport/auditTrialreportXLS.jasper");			
			Commonutility.toWriteConsole("jrxml Path XLS : "+lvrJrxml);
			Commonutility.toWriteConsole("japer Path XLS : "+lvrJasper);
			JasperCompileManager.compileReportToFile(lvrJrxml,lvrJasper);
			/*
			JasperCompileManager
					.compileReportToFile(
							getText("ApplicationAbsolutePath")
									+ ServletActionContext.getServletContext()
											.getContextPath()
									+ getText("WebContent")
									+ "resources/jsp/PDFCreation/reports/auditTrialreport/auditTrialreportXLS.jrxml",
							getText("ApplicationAbsolutePath")
									+ ServletActionContext.getServletContext()
											.getContextPath()
									+ getText("WebContent")
									+ "resources/jsp/PDFCreation/reports/auditTrialreport/auditTrialreportXLS.jasper");
			*/
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);
			reportParams.put("externalPath", System.getenv("SOCIAL_INDIA_BASE_URL") + getText("Logo.url"));
			reportParams.put("companyName", getText("report.company.name"));
			logWrite.logMessage("Step 3 : Audit Trail XLS Export Called.[End]", "info", LoadAuditTrialreportExportData.class);					
			Commonutility.toWriteConsole("Step 3 : Audit Trail XLS Export Called.[End]");
				} else {
					 logWrite.logMessage("Step 2 : Request Format not correct.", "info", LoadAuditTrialreportExportData.class);					
					 Commonutility.toWriteConsole("Step 2 : Request Format not correct.");
				}
			} else {
				logWrite.logMessage("Step 2 : Request Data is empty.", "info", LoadAuditTrialreportExportData.class);				
				 Commonutility.toWriteConsole("Step 2 : Request Data is empty.");
			}
			return SUCCESS;
		}catch (Exception e){
			logWrite.logMessage("Step -1 : Exception Found in "+getClass().getSimpleName()+" : "+e, "error", LoadAuditTrialreportExportData.class);			
			Commonutility.toWriteConsole("Step -1 : Exception Found in "+getClass().getSimpleName()+" : "+e);
			AuditTrial.toWriteAudit(getText("DOWNXLS0002"), "DOWNXLS0002", 0);
			return "input";
		} finally {
			locObjRecvJson = null;
			ivrservicecode=null;
			logWrite = null;
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


