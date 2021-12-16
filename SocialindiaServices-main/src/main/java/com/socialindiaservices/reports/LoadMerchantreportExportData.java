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
import com.siservices.societyMgmt.societyMgmtDao;
import com.siservices.societyMgmt.societyMgmtDaoServices;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.services.ReportDaoServices;
import com.socialindiaservices.services.ReportServices;

public class LoadMerchantreportExportData   extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList merchantList = new ArrayList();
	private ReportServices reportDao=new ReportDaoServices();
	private HashMap reportParams = new HashMap();
	private String ivrparams;	
	public String execute(){
		JSONObject locObjRecvJson = null;
		String ivrservicecode=null;
		SocietyMstTbl societyMst = null;
		societyMgmtDao lvrSocietydoobj = null;
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
			    		String lvrCuntusrsocytid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "crntsocietyid");
			    		String manualsearch="";
						if(fromdate.length() >0 && todate.length() >0){
								manualsearch += " and date(entryDatetime) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')  ";
						}else if(fromdate.length() >0){
								manualsearch += " and date(entryDatetime) >= str_to_date('" + fromdate + "', '%d-%m-%Y') ";
						}else if(todate.length() >0){
								manualsearch += " and date(entryDatetime) <= str_to_date('" + todate + "', '%d-%m-%Y') ";
						}
			
					String releasereport = (String) sessionMap.get("globquery");
					String authtable = "from MerchantTblVO where mrchntActSts=1 "+manualsearch+" order by modifyDatetime desc";			
					merchantList= (ArrayList) reportDao.selectMerchantPdf(authtable);
		  
					String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/merchantreport/merchantreportPDF.jrxml");
					String lvrJasper = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/merchantreport/merchantreportPDF.jasper");			
					Commonutility.toWriteConsole("jrxml Path PDF : "+lvrJrxml);
					Commonutility.toWriteConsole("japer Path PDF : "+lvrJasper);
					logWrtie.logMessage("jrxml Path PDF : "+lvrJrxml, "info", LoadMerchantreportExportData.class);
					logWrtie.logMessage("japer Path PDF : "+lvrJasper, "info", LoadMerchantreportExportData.class);
					JasperCompileManager.compileReportToFile(lvrJrxml,lvrJasper);			
					JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(merchantList);
					
					String lvrLogimg = "", lvrSoctyname = "";
					Commonutility.toWriteConsole("[PDF] Current Society id : "+lvrCuntusrsocytid);
					if(Commonutility.checkempty(lvrCuntusrsocytid) && !lvrCuntusrsocytid.equalsIgnoreCase("null") && !lvrCuntusrsocytid.equalsIgnoreCase("") && !lvrCuntusrsocytid.equalsIgnoreCase("-1")) {
						lvrSocietydoobj = societyMgmtDaoServices.getInstanceSocymgmtdao();
						societyMst = lvrSocietydoobj.getSocietyDetailView(Integer.parseInt(lvrCuntusrsocytid));
						String imgname = societyMst.getLogoImage();
						lvrSoctyname = societyMst.getSocietyName();
						Commonutility.toWriteConsole("[PDF] Current Society Log Name : "+imgname);
						if (Commonutility.checkempty(imgname)) {
							lvrLogimg = System.getenv("SOCIAL_INDIA_BASE_URL") + getText("project.login.url") + getText("external.templogo") + getText("external.society.fldr") + getText("external.inner.webpath")+lvrCuntusrsocytid+"/"+imgname;
						} else {
							lvrLogimg =  System.getenv("SOCIAL_INDIA_BASE_URL") + getText("Logo.url");
						}							
						Commonutility.toWriteConsole("[PDF] Current Society Log Path : "+lvrLogimg);
						if (Commonutility.checkempty(lvrSoctyname)){		
							Commonutility.toWriteConsole("[PDF] Current Society Name [Society] : "+lvrSoctyname);
						} else {
							lvrSoctyname = getText("report.company.name");
							Commonutility.toWriteConsole("[PDF] Current Society Name [Admin] : "+lvrSoctyname);
						}						
					} else {
						lvrLogimg =  System.getenv("SOCIAL_INDIA_BASE_URL") + getText("Logo.url");
						lvrSoctyname = getText("report.company.name");
						Commonutility.toWriteConsole("[PDF] Current Society Log Path : "+lvrLogimg);						
					}
					reportParams.put("externalPath", lvrLogimg);
					reportParams.put("companyName", lvrSoctyname);
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			AuditTrial.toWriteAudit(getText("MCHTPDF0002"), "MCHTPDF0002", 2);
			e.printStackTrace();
			return "input";
		} finally {
			societyMst = null;
		}
	}
	public String getMerchantXLS(){
		JSONObject locObjRecvJson = null;
		SocietyMstTbl societyMst = null;
		societyMgmtDao lvrSocietydoobj = null;
		Log logWrtie = null;
		try {
			logWrtie = new Log();
			Map sessionMap = ActionContext.getContext().getSession();
			if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
				 ivrparams = EncDecrypt.decrypt(ivrparams);
				 boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				 if (ivIsJson) {
					 locObjRecvJson = new JSONObject(ivrparams);
			    		String fromdate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "fromdate");
			    		String todate=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "todate");
			    		String lvrCuntusrsocytid =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "crntsocietyid");
			    		String manualsearch="";
						if(fromdate.length() >0 && todate.length() >0){
								manualsearch += " and date(entryDatetime) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')  ";
						}else if(fromdate.length() >0){
								manualsearch += " and date(entryDatetime) >= str_to_date('" + fromdate + "', '%d-%m-%Y') ";
						}else if(todate.length() >0){
								manualsearch += " and date(entryDatetime) <= str_to_date('" + todate + "', '%d-%m-%Y') ";
						}
						
					String releasereport = (String) sessionMap.get("globquery");
					String authtable = "from MerchantTblVO where mrchntActSts=1 "+manualsearch+" order by modifyDatetime desc";
					merchantList= (ArrayList) reportDao.selectMerchantPdf(authtable);		  
					String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/merchantreport/merchantreportXLS.jrxml");
					String lvrJasper = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/merchantreport/merchantreportXLS.jasper");			
					Commonutility.toWriteConsole("jrxml Path XLS : "+lvrJrxml);
					Commonutility.toWriteConsole("japer Path XLS : "+lvrJasper);
					logWrtie.logMessage("jrxml Path XLS : "+lvrJrxml, "info", LoadMerchantreportExportData.class);
					logWrtie.logMessage("japer Path XLS : "+lvrJasper, "info", LoadMerchantreportExportData.class);
					JasperCompileManager.compileReportToFile(lvrJrxml,lvrJasper);					
					JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(merchantList);
					
					String lvrLogimg = "", lvrSoctyname = "";
					if (Commonutility.checkempty(lvrCuntusrsocytid) && !lvrCuntusrsocytid.equalsIgnoreCase("null") && !lvrCuntusrsocytid.equalsIgnoreCase("") && !lvrCuntusrsocytid.equalsIgnoreCase("-1")) {
						lvrSocietydoobj = societyMgmtDaoServices.getInstanceSocymgmtdao();
						societyMst = lvrSocietydoobj.getSocietyDetailView(Integer.parseInt(lvrCuntusrsocytid));
						String imgname = societyMst.getLogoImage();
						lvrSoctyname = societyMst.getSocietyName();
						Commonutility.toWriteConsole("[PDF] Current Society Log Name : "+imgname);
						if (Commonutility.checkempty(imgname)) {
							lvrLogimg = System.getenv("SOCIAL_INDIA_BASE_URL") + getText("project.login.url") + getText("external.templogo") + getText("external.society.fldr") + getText("external.inner.webpath")+lvrCuntusrsocytid+"/"+imgname;
						} else {
							lvrLogimg = System.getenv("SOCIAL_INDIA_BASE_URL") + getText("Logo.url");
						}						
						Commonutility.toWriteConsole("[XLS] Current Society Log Path : "+lvrLogimg);
						if (Commonutility.checkempty(lvrSoctyname)){		
							Commonutility.toWriteConsole("[XLS] Current Society Name [Society] : "+lvrSoctyname);
						} else {
							lvrSoctyname = getText("report.company.name");
							Commonutility.toWriteConsole("[XLS] Current Society Name [Admin] : "+lvrSoctyname);
						}						
					} else {
						lvrLogimg = System.getenv("SOCIAL_INDIA_BASE_URL") + getText("Logo.url");
						lvrSoctyname = getText("report.company.name");
						Commonutility.toWriteConsole("[XLS] Current Society Log Path [Admin] : "+ lvrLogimg);
					}
					reportParams.put("externalPath", lvrLogimg);
					reportParams.put("companyName", lvrSoctyname);
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			AuditTrial.toWriteAudit(getText("MCHTXL0002"), "MCHTXL0002", 2);
			e.printStackTrace();
			return "input";
		} finally {
			societyMst = null;locObjRecvJson = null;lvrSocietydoobj = null;
		}
	}

	public ArrayList getMerchantList() {
		return merchantList;
	}

	public void setMerchantList(ArrayList merchantList) {
		this.merchantList = merchantList;
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