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
import com.pack.utilitypkg.Commonutility;
import com.social.login.EncDecrypt;
import com.socialindiaservices.services.ReportDaoServices;
import com.socialindiaservices.services.ReportServices;

public class LoadReconicileExportData extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList reconicile = new ArrayList();
	private ReportServices reportDao=new ReportDaoServices();
	private HashMap reportParams = new HashMap();
	private String ivrparams;

	public String execute(){
		JSONObject locObjRecvJson = null;
		Map sessionMap = null;
		try{
			sessionMap = ActionContext.getContext().getSession();
			 if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
				 ivrparams = EncDecrypt.decrypt(ivrparams);
				 boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				 if (ivIsJson) {
					String societyid = "";
					locObjRecvJson = new JSONObject(ivrparams);
			    	String fromdate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "fromdate");
					String todate=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "todate");
					String startamount=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "startamount");
					String endamount=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "endamount");
					String selectid=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "selectid");
					societyid=String.valueOf((Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "sSoctyId"));
					String transacttype=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "transtype");
					String manualsearch="";
					if(selectid!=null && !selectid.equalsIgnoreCase("null") && !selectid.equalsIgnoreCase("")){
						if(selectid.trim().equalsIgnoreCase("5")){
						if (startamount.length()>0 && endamount.length()>0) {
							manualsearch += " and ivrTXNAMOUNT between ('"+startamount+"') and ('"+endamount+"')";
						}else if(startamount.length()>0){
							manualsearch += " and ivrTXNAMOUNT >= ('"+startamount+"')";
						}else if(endamount.length()>0){
							manualsearch += " and ivrTXNAMOUNT <= ('"+endamount+"')";
						}
						}if (selectid.trim().equalsIgnoreCase("6")) {
							if (transacttype.trim().equalsIgnoreCase("1")) {
								manualsearch += "  and ivrTRANSID.paymentType= 1";
							} else if (transacttype.trim().equalsIgnoreCase("2")) {
								manualsearch += "  and ivrTRANSID.paymentType=2";
							} else {

							}
						}else if(selectid.trim().equalsIgnoreCase("1")){
							manualsearch += " and ivrTRANMATCHSTATUS= 0";
						}else if(selectid.trim().equalsIgnoreCase("2")){
							manualsearch += " and ivrTRANMATCHSTATUS= 1";
						}else if(selectid.trim().equalsIgnoreCase("2")){
							manualsearch += " and ivrTRANMATCHSTATUS= 1";
						}else if(selectid.trim().equalsIgnoreCase("3")){
							manualsearch += " and ivrTRANRECONSTATUS= 0";
						}else if(selectid.trim().equalsIgnoreCase("4")){
							manualsearch += " and ivrTRANRECONSTATUS= 1";
						}
					}
					if (fromdate.length() >0 && todate.length() >0 ){
						manualsearch += " and date(entryDateTime) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')  ";
					} else if (fromdate.length() > 0) {
						manualsearch += " and date(entryDateTime) >= str_to_date('" + fromdate + "', '%d-%m-%Y') ";
					} else if (todate.length() > 0) {
						manualsearch += " and date(entryDateTime) <= str_to_date('" + todate + "', '%d-%m-%Y') ";
					}

					String releasereport = (String) sessionMap.get("globquery");
					String locSlqry = null;
					int locGrpcode = 0;
					String authtable = "";
					//if (societyid==null || societyid.equalsIgnoreCase("null") ||societyid.equalsIgnoreCase("-1")){
						//authtable = "from ReconicileresultTblVo where ivrSTATUS=0 "+ manualsearch;		
					//} else {
						authtable = "from ReconicileresultTblVo where ivrSTATUS=0 "+ manualsearch;	
						System.out.println("manualsearch ----------- "+manualsearch);
					//}
				
					reconicile= (ArrayList) reportDao.selectReconicile(authtable);		  
					String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/reconicile/reconicileReportPDF.jrxml");
					String lvrJasper = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/reconicile/reconicileReportPDF.jasper");			
					System.out.println("jrxml Path PDF : "+lvrJrxml);
					System.out.println("japer Path PDF : "+lvrJasper);
					JasperCompileManager.compileReportToFile(lvrJrxml,lvrJasper);
			/*JasperCompileManager
					.compileReportToFile(
							getText("ApplicationAbsolutePath")
									+ ServletActionContext.getServletContext()
											.getContextPath()
									+ getText("WebContent")
									+ "resources/jsp/PDFCreation/reports/societyreport/societyreportPDF.jrxml",
							getText("ApplicationAbsolutePath")
									+ ServletActionContext.getServletContext()
											.getContextPath()
									+ getText("WebContent")
									+ "resources/jsp/PDFCreation/reports/societyreport/societyreportPDF.jasper");*/
					JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reconicile);
					reportParams.put("externalPath", System.getenv("SOCIAL_INDIA_BASE_URL") + getText("Logo.url"));
					// reportParams.put("companyName",
					// getText("report.company.name"));
				}
			}
				
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return "input";
		}
	}
	public String getReconXLS(){

		JSONObject locObjRecvJson = null;
		String ivrservicecode = null;
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
			    		societyid=String.valueOf((Integer)  Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "sSoctyId"));
			    		String startamount=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "startamount");
						String endamount=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "endamount");
						String selectid=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "selectid");
						String transacttype=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "transtype");
			    		String manualsearch="";
			    		if(selectid!=null && !selectid.equalsIgnoreCase("null") && !selectid.equalsIgnoreCase("")){
							if(selectid.trim().equalsIgnoreCase("5")){
							if (startamount.length()>0 && endamount.length()>0) {
								manualsearch += " and ivrTXNAMOUNT between ('"+startamount+"') and ('"+endamount+"')";
							}else if(startamount.length()>0){
								manualsearch += " and ivrTXNAMOUNT >= ('"+startamount+"')";
							}else if(endamount.length()>0){
								manualsearch += " and ivrTXNAMOUNT <= ('"+endamount+"')";
							}
							}if (selectid.trim().equalsIgnoreCase("6")) {
								if (transacttype.trim().equalsIgnoreCase("1")) {
									manualsearch += "  and ivrTRANSID.paymentType= 1";
								} else if (transacttype.trim().equalsIgnoreCase("2")) {
									manualsearch += "  and ivrTRANSID.paymentType=2";
								} else {

								}
							}else if(selectid.trim().equalsIgnoreCase("1")){
								manualsearch += " and ivrTRANMATCHSTATUS= 0";
							}else if(selectid.trim().equalsIgnoreCase("2")){
								manualsearch += " and ivrTRANMATCHSTATUS= 1";
							}else if(selectid.trim().equalsIgnoreCase("2")){
								manualsearch += " and ivrTRANMATCHSTATUS= 1";
							}else if(selectid.trim().equalsIgnoreCase("3")){
								manualsearch += " and ivrTRANRECONSTATUS= 0";
							}else if(selectid.trim().equalsIgnoreCase("4")){
								manualsearch += " and ivrTRANRECONSTATUS= 1";
							}
						}
			    		if(fromdate.length() >0 && todate.length() >0 ){
							manualsearch += " and date(entryDateTime) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')  ";
					}else if(fromdate.length() >0 ){
							manualsearch += " and date(entryDateTime) >= str_to_date('" + fromdate + "', '%d-%m-%Y') ";
					}else if(todate.length() >0 ){
							manualsearch += " and date(entryDateTime) <= str_to_date('" + todate + "', '%d-%m-%Y') ";
					}
				 
					String releasereport = (String) sessionMap.get("globquery");
					String locSlqry = null;
					int locGrpcode = 0;
					String authtable = "";
					//if (societyid==null || societyid.equalsIgnoreCase("null") ||societyid.equalsIgnoreCase("-1")){
						//authtable = "from ReconicileresultTblVo where ivrSTATUS=0 "+ manualsearch;		
					//}else{
						authtable = "from ReconicileresultTblVo where ivrSTATUS=0 "+ manualsearch;	
						System.out.println("manualsearch ----------- "+manualsearch);
					//}			
					reconicile= (ArrayList) reportDao.selectReconicile(authtable);
		  
			String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/reconicile/reconicileReportXLS.jrxml");
			String lvrJasper = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/reports/reconicile/reconicileReportXLS.jasper");			
			System.out.println("jrxml Path XLs : "+lvrJrxml);
			System.out.println("japer Path XLs : "+lvrJasper);
			JasperCompileManager.compileReportToFile(lvrJrxml,lvrJasper);
			/*JasperCompileManager
					.compileReportToFile(
							getText("ApplicationAbsolutePath")
									+ ServletActionContext.getServletContext()
											.getContextPath()
									+ getText("WebContent")
									+ "resources/jsp/PDFCreation/reports/societyreport/societyreportPDF.jrxml",
							getText("ApplicationAbsolutePath")
									+ ServletActionContext.getServletContext()
											.getContextPath()
									+ getText("WebContent")
									+ "resources/jsp/PDFCreation/reports/societyreport/societyreportPDF.jasper");*/
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reconicile);
			reportParams.put("externalPath", System.getenv("SOCIAL_INDIA_BASE_URL") + getText("Logo.url"));
			//reportParams.put("companyName", getText("report.company.name"));
				}
			}

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return "input";
		}
	
	}

	public ArrayList getReconicile() {
		return reconicile;
	}
	public void setReconicile(ArrayList reconicile) {
		this.reconicile = reconicile;
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

