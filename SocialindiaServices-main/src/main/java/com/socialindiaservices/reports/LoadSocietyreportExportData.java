package com.socialindiaservices.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.chainsaw.Main;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.social.login.EncDecrypt;
import com.socialindiaservices.services.ReportDaoServices;
import com.socialindiaservices.services.ReportServices;

public class LoadSocietyreportExportData extends ActionSupport {
	private ArrayList userList = new ArrayList();
	private ReportServices reportDao=new ReportDaoServices();
	private HashMap reportParams = new HashMap();
	private String ivrparams;

	public String execute(){
		JSONObject locObjRecvJson = null;
		String ivrservicecode=null;
		try{
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
				 
			String releasereport = (String) sessionMap.get("globquery");
			String locSlqry=null;
			int locGrpcode=0;
			GroupMasterTblVo locGrpMstrVOobj=new GroupMasterTblVo();
			String pGrpName=getText("Grp.society");
			 locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"') or groupName=upper('"+pGrpName+"')";
			 locGrpMstrVOobj=reportDao.selectGroupByQry(locSlqry);
					if (locGrpMstrVOobj != null) {
						locGrpcode=locGrpMstrVOobj.getGroupCode();
					}
			
			 		String authtable = "from UserMasterTblVo where statusFlag=1 and groupCode="+locGrpcode+" "+manualsearch+" order by modifyDatetime desc";			
					userList = (ArrayList) reportDao.selectuserMstPdf(authtable);
		  
					String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/societyreport/societyreportPDF.jrxml");
					String lvrJasper = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/societyreport/societyreportPDF.jasper");			
					System.out.println("jrxml Path PDF : "+lvrJrxml);
					System.out.println("japer Path PDF : "+lvrJasper);
					JasperCompileManager.compileReportToFile(lvrJrxml,lvrJasper);
			
			
					JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);
					reportParams.put("externalPath", System.getenv("SOCIAL_INDIA_BASE_URL") + getText("Logo.url"));
					reportParams.put("companyName", getText("report.company.name"));
				 }
			 }
				
			return SUCCESS;
		}catch (Exception e){
			AuditTrial.toWriteAudit(getText("SOCPDF0002"), "SOCPDF0002", 2);
			e.printStackTrace();
			return "input";
		}
	}
	public String getUsertXLS(){
		JSONObject locObjRecvJson = null;
		String ivrservicecode=null;
		try{
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
						
			String locSlqry=null;
			int locGrpcode=0;
			GroupMasterTblVo locGrpMstrVOobj=new GroupMasterTblVo();
			String pGrpName=getText("Grp.society");
			 locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"') or groupName=upper('"+pGrpName+"')";
			 locGrpMstrVOobj=reportDao.selectGroupByQry(locSlqry);
			 if(locGrpMstrVOobj!=null){
				 locGrpcode=locGrpMstrVOobj.getGroupCode();
				 }
			
			String authtable = "from UserMasterTblVo where statusFlag=1 and groupCode="+locGrpcode+" "+manualsearch+" order by modifyDatetime desc";			
			userList= (ArrayList) reportDao.selectuserMstPdf(authtable);
		  
			String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/societyreport/societyreportXLS.jrxml");
			String lvrJasper = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/societyreport/societyreportXLS.jasper");			
			System.out.println("jrxml Path XLS : "+lvrJrxml);
			System.out.println("japer Path XLS : "+lvrJasper);
			JasperCompileManager.compileReportToFile(lvrJrxml,lvrJasper);
			
			/*
			JasperCompileManager
					.compileReportToFile(
							getText("ApplicationAbsolutePath")
									+ ServletActionContext.getServletContext()
											.getContextPath()
									+ getText("WebContent")
									+ "resources/jsp/PDFCreation/reports/societyreport/societyreportXLS.jrxml",
							getText("ApplicationAbsolutePath")
									+ ServletActionContext.getServletContext()
											.getContextPath()
									+ getText("WebContent")
									+ "resources/jsp/PDFCreation/reports/societyreport/societyreportXLS.jasper");
			*/
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
					userList);
			reportParams.put("externalPath", System.getenv("SOCIAL_INDIA_BASE_URL") + getText("Logo.url"));
			reportParams.put("companyName", getText("report.company.name"));
				 }
			 }
			return SUCCESS;
		}catch (Exception e){
			AuditTrial.toWriteAudit(getText("SOCXL0002"), "SOCXL0002", 2);
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

