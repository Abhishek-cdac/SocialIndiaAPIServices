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
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.societyMgmt.societyMgmtDao;
import com.siservices.societyMgmt.societyMgmtDaoServices;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;

import com.social.login.EncDecrypt;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.socialindiaservices.services.ReportDaoServices;
import com.socialindiaservices.services.ReportServices;

public class LoadResidentreportExportData extends ActionSupport {
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
		try{
			Map sessionMap = ActionContext.getContext().getSession();
			 if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
				 ivrparams = EncDecrypt.decrypt(ivrparams);
				 boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				 if (ivIsJson) {
					 locObjRecvJson = new JSONObject(ivrparams);
			    		String fromdate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "fromdate");
			    		String todate=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "todate");
			    		Integer sSoctyId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "sSoctyId");
			    		Integer sTowshipId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "sTowshipId");
			    		String lvrCuntusrsocytid =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "crntsocietyid");
			    		String manualsearch="";
			    		if(sSoctyId!=null && sSoctyId>0){
							manualsearch += " and societyId.societyId="+sSoctyId;
						}
			    		if(sTowshipId!=null && sTowshipId>0){
							manualsearch += " and societyId.townshipId.townshipId="+sTowshipId;
						}
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
					GroupMasterTblVo locGrpMstrVOobj=new GroupMasterTblVo();
					String pGrpName=getText("Grp.resident");
					locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"') or groupName=upper('"+pGrpName+"')";
					locGrpMstrVOobj=reportDao.selectGroupByQry(locSlqry);
					if(locGrpMstrVOobj!=null){
						locGrpcode=locGrpMstrVOobj.getGroupCode();
					}
			
					String authtable = "from UserMasterTblVo where statusFlag=1 and groupCode="+locGrpcode+" "+manualsearch+" order by modifyDatetime desc";			
					userList = (ArrayList) reportDao.selectuserMstPdf(authtable);
		  
					String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/residentreport/residentreportPDF.jrxml");
					String lvrJasper = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/residentreport/residentreportPDF.jasper");			
					Commonutility.toWriteConsole("jrxml Path PDF : "+lvrJrxml);
					Commonutility.toWriteConsole("japer Path PDF : "+lvrJasper);
					JasperCompileManager.compileReportToFile(lvrJrxml,lvrJasper);						
					JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);
					
					String lvrLogimg = "", lvrSoctyname = "";
					if(Commonutility.checkempty(lvrCuntusrsocytid) && !lvrCuntusrsocytid.equalsIgnoreCase("null") && !lvrCuntusrsocytid.equalsIgnoreCase("") && !lvrCuntusrsocytid.equalsIgnoreCase("-1")) {
						societyMgmtDao society=new societyMgmtDaoServices();
						societyMst = society.getSocietyDetailView(Integer.parseInt(lvrCuntusrsocytid));
						//Common lvrCommdaosrobj = null;
						//lvrCommdaosrobj = new CommonDao();
						//String imgname = (String) lvrCommdaosrobj.getuniqueColumnVal("SocietyMstTbl", "logoImage", "societyId", lvrCuntusrsocytid);
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
		}catch (Exception e){
			AuditTrial.toWriteAudit(getText("RPTPDF0002"), "RPTPDF0002", 2);
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
			    		Integer sSoctyId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "sSoctyId");
			    		Integer sTowshipId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "sTowshipId");	
			    		String lvrCuntusrsocytid =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "crntsocietyid");
			    		String manualsearch="";
			    		if(sSoctyId!=null && sSoctyId>0){
							manualsearch += " and societyId.societyId="+sSoctyId;
						}
			    		if(sTowshipId!=null && sTowshipId!=null && sTowshipId>0){
							manualsearch += " and societyId.townshipId.townshipId="+sTowshipId;
						}
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
			String pGrpName=getText("Grp.resident");
			 locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"') or groupName=upper('"+pGrpName+"')";
			 locGrpMstrVOobj = reportDao.selectGroupByQry(locSlqry);
			 if(locGrpMstrVOobj!=null){
				 locGrpcode=locGrpMstrVOobj.getGroupCode();
				 }
			
			 		String authtable = "from UserMasterTblVo where statusFlag=1 and groupCode="+locGrpcode+" "+manualsearch+" order by modifyDatetime desc";			
			 		userList= (ArrayList) reportDao.selectuserMstPdf(authtable);
		  
					String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/residentreport/residentreportXLS.jrxml");
					String lvrJasper = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/residentreport/residentreportXLS.jasper");			
					Commonutility.toWriteConsole("jrxml Path XLS : "+lvrJrxml);
					Commonutility.toWriteConsole("japer Path XLS : "+lvrJasper);
					JasperCompileManager.compileReportToFile(lvrJrxml,lvrJasper);
						
					JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);
					String lvrLogimg = "", lvrSoctyname = "";
					if(Commonutility.checkempty(lvrCuntusrsocytid) && !lvrCuntusrsocytid.equalsIgnoreCase("null") && !lvrCuntusrsocytid.equalsIgnoreCase("") && !lvrCuntusrsocytid.equalsIgnoreCase("-1")) {
						societyMgmtDao society=new societyMgmtDaoServices();
						SocietyMstTbl societyMst = society.getSocietyDetailView(Integer.parseInt(lvrCuntusrsocytid));
						//Common lvrCommdaosrobj = null;
						//lvrCommdaosrobj = new CommonDao();
						//String imgname = (String) lvrCommdaosrobj.getuniqueColumnVal("SocietyMstTbl", "logoImage", "societyId", lvrCuntusrsocytid);
						String imgname = societyMst.getLogoImage();
						lvrSoctyname = societyMst.getSocietyName();
						Commonutility.toWriteConsole("[XLS] Current Society Log Name : "+imgname);						
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
						reportParams.put("externalPath", lvrLogimg);
						reportParams.put("companyName", lvrSoctyname);
					} else {
						lvrLogimg =  System.getenv("SOCIAL_INDIA_BASE_URL") + getText("Logo.url");
						lvrSoctyname = getText("report.company.name");
						Commonutility.toWriteConsole("[XLS] Current Society Log Path [Admin] : "+lvrLogimg);
						reportParams.put("externalPath", lvrLogimg);
						reportParams.put("companyName", lvrSoctyname);
					}
				}
			 }
			return SUCCESS;
		}catch (Exception e){
			AuditTrial.toWriteAudit(getText("RPTXL0002"), "RPTXL0002", 2);
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
