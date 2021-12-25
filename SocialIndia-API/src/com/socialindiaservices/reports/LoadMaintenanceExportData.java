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
import com.siservices.societyMgmt.societyMgmtDao;
import com.siservices.societyMgmt.societyMgmtDaoServices;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.social.login.EncDecrypt;
import com.socialindiaservices.services.ReportDaoServices;
import com.socialindiaservices.services.ReportServices;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class LoadMaintenanceExportData extends ActionSupport {
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
		SocietyMstTbl societyMst = null;
		societyMgmtDao lvrSocietydoobj = null;
		try{
			Map sessionMap = ActionContext.getContext().getSession();
			 if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
				 ivrparams = EncDecrypt.decrypt(ivrparams);
				 boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				 if (ivIsJson) {
					 String societyid="";
					 locObjRecvJson = new JSONObject(ivrparams);
			    		String fromdate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "fromdate");
			    		String todate=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "todate");
			    		societyid=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "sSoctyId");
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
					String locSlqry = null;
					int locGrpcode = 0;
					String authtable = "";
					if(societyid==null || societyid.equalsIgnoreCase("null") ||societyid.equalsIgnoreCase("-1")){
						authtable = "from MaintenanceFileUploadTblVO where status=1";		
					}else{
						authtable = "from MaintenanceFileUploadTblVO where societyId="+Integer.parseInt(societyid)+" and status=1";	
					}
				
					userList = (ArrayList) reportDao.selectDocumentPdf(authtable);		  
					String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/maintenanceBill/maintenancereportPDF.jrxml");
					String lvrJasper = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/maintenanceBill/maintenancereportPDF.jasper");			
					System.out.println("jrxml Path PDF : "+lvrJrxml);
					System.out.println("japer Path PDF : "+lvrJasper);
					JasperCompileManager.compileReportToFile(lvrJrxml,lvrJasper);			
					JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);
					String lvrLogimg = "", lvrSoctyname = "";
					Commonutility.toWriteConsole("[PDF] Current Society id : "+lvrCuntusrsocytid);
					if(Commonutility.checkempty(lvrCuntusrsocytid) && !lvrCuntusrsocytid.equalsIgnoreCase("null") && !lvrCuntusrsocytid.equalsIgnoreCase("") && !lvrCuntusrsocytid.equalsIgnoreCase("-1")) {
						lvrSocietydoobj = societyMgmtDaoServices.getInstanceSocymgmtdao();
						societyMst = lvrSocietydoobj.getSocietyDetailView(Integer.parseInt(lvrCuntusrsocytid));
						String imgname = societyMst.getLogoImage();
						lvrSoctyname = societyMst.getSocietyName();
						Commonutility.toWriteConsole("[PDF] Current Society Log Name : "+imgname);
						if (Commonutility.checkempty(imgname)) {
							lvrLogimg = getText("SOCIAL_INDIA_BASE_URL") + getText("project.login.url") + getText("external.templogo") + getText("external.society.fldr") + getText("external.inner.webpath")+lvrCuntusrsocytid+"/"+imgname;
						} else {
							lvrLogimg =  getText("SOCIAL_INDIA_BASE_URL") + getText("Logo.url");
						}							
						Commonutility.toWriteConsole("[PDF] Current Society Log Path : "+lvrLogimg);
						if (Commonutility.checkempty(lvrSoctyname)){		
							Commonutility.toWriteConsole("[PDF] Current Society Name [Society] : "+lvrSoctyname);
						} else {
							lvrSoctyname = getText("report.company.name");
							Commonutility.toWriteConsole("[PDF] Current Society Name [Admin] : "+lvrSoctyname);
						}						
					} else {
						lvrLogimg =  getText("SOCIAL_INDIA_BASE_URL") + getText("Logo.url");
						lvrSoctyname = getText("report.company.name");
						Commonutility.toWriteConsole("[PDF] Current Society Log Path : "+lvrLogimg);						
					}
					reportParams.put("externalPath", lvrLogimg);
					reportParams.put("companyName", lvrSoctyname);
				}
			}
				
			return SUCCESS;
		} catch (Exception e) {
			AuditTrial.toWriteAudit(getText("SOCPDF0002"), "SOCPDF0002", 2);
			e.printStackTrace();
			return "input";
		} finally {
			locObjRecvJson = null;
			ivrservicecode = null;
			societyMst = null;
			lvrSocietydoobj = null;
		}
	}
	public String getUsertXLS(){

		JSONObject locObjRecvJson = null;
		String ivrservicecode=null;
		SocietyMstTbl societyMst = null;
		societyMgmtDao lvrSocietydoobj = null;
		try{
			String societyid="";
			Map sessionMap = ActionContext.getContext().getSession();
			 if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
				 ivrparams = EncDecrypt.decrypt(ivrparams);
				 boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				 if (ivIsJson) {
					 locObjRecvJson = new JSONObject(ivrparams);
			    		String fromdate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "fromdate");
			    		String todate=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "todate");	
			    		societyid=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "sSoctyId");
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
						String locSlqry=null;
						int locGrpcode=0;
						String authtable="";
						if(societyid==null || societyid.equalsIgnoreCase("null") || societyid.equalsIgnoreCase("-1")){
							authtable = "from MaintenanceFileUploadTblVO where status=1";		
						}else{
							authtable = "from MaintenanceFileUploadTblVO where societyId="+Integer.parseInt(societyid)+" and status=1";	
						}			
						//	String authtable = "from MaintenanceFileUploadTblVO where statusFlag=1";			
						userList= (ArrayList) reportDao.selectDocumentPdf(authtable);
		  
						String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/maintenanceBill/maintenancereportXLS.jrxml");
						String lvrJasper = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/maintenanceBill/maintenancereportXLS.jasper");			
						System.out.println("jrxml Path PDF : "+lvrJrxml);
						System.out.println("japer Path PDF : "+lvrJasper);
						JasperCompileManager.compileReportToFile(lvrJrxml,lvrJasper);
			
						JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);
						String lvrLogimg = "", lvrSoctyname = "";
						if (Commonutility.checkempty(lvrCuntusrsocytid) && !lvrCuntusrsocytid.equalsIgnoreCase("null") && !lvrCuntusrsocytid.equalsIgnoreCase("") && !lvrCuntusrsocytid.equalsIgnoreCase("-1")) {
							lvrSocietydoobj = societyMgmtDaoServices.getInstanceSocymgmtdao();
							societyMst = lvrSocietydoobj.getSocietyDetailView(Integer.parseInt(lvrCuntusrsocytid));
							String imgname = societyMst.getLogoImage();
							lvrSoctyname = societyMst.getSocietyName();
							Commonutility.toWriteConsole("[PDF] Current Society Log Name : "+imgname);
							if (Commonutility.checkempty(imgname)) {
								lvrLogimg = getText("SOCIAL_INDIA_BASE_URL") + getText("project.login.url") + getText("external.templogo") + getText("external.society.fldr") + getText("external.inner.webpath")+lvrCuntusrsocytid+"/"+imgname;
							} else {
								lvrLogimg = getText("SOCIAL_INDIA_BASE_URL") + getText("Logo.url");
							}						
							Commonutility.toWriteConsole("[XLS] Current Society Log Path : "+lvrLogimg);
							if (Commonutility.checkempty(lvrSoctyname)){		
								Commonutility.toWriteConsole("[XLS] Current Society Name [Society] : "+lvrSoctyname);
							} else {
								lvrSoctyname = getText("report.company.name");
								Commonutility.toWriteConsole("[XLS] Current Society Name [Admin] : "+lvrSoctyname);
							}						
						} else {
							lvrLogimg = getText("SOCIAL_INDIA_BASE_URL") + getText("Logo.url");
							lvrSoctyname = getText("report.company.name");
							Commonutility.toWriteConsole("[XLS] Current Society Log Path [Admin] : "+ lvrLogimg);
						}
						reportParams.put("externalPath", lvrLogimg);
						reportParams.put("companyName", lvrSoctyname);
					
				}
			}

			return SUCCESS;
		}catch (Exception e){
			AuditTrial.toWriteAudit(getText("SOCPDF0002"), "SOCPDF0002", 2);
			e.printStackTrace();
			return "input";
		} finally {
			locObjRecvJson = null;
			 ivrservicecode = null;
			 societyMst = null;
			 lvrSocietydoobj = null;
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

