package com.social.utititymgmt;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.DocumentUtilitiDaoServices;
import com.socialindiaservices.services.DocumentUtilitiServices;
import com.socialindiaservices.vo.DocumentShareTblVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class GenerateMaintenancePdf extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Map parameters = new HashMap();
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	public boolean generatePdf(String filepath,String fileName,MaintenanceFeeTblVO maintanencefee,DocumentShareTblVO documentShare){
		DocumentUtilitiServices docHibernateUtilService = new DocumentUtilitiDaoServices();
		try{
			File file = new File(filepath);
			if (!file.exists()) {
				Commonutility.crdDir(file.getParent()+"/"+ file.getName());
				file.mkdir();
			}
			String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/maintenanceDocument.jrxml");
			 //JasperDesign design = JRXmlLoader.load(getText("ApplicationAbsolutePath")+ServletActionContext.getServletContext().getContextPath()+getText("WebContent")+"resources/jsp/PDFCreation/maintenanceDocument.jrxml");
			JasperDesign design = JRXmlLoader.load(lvrJrxml);
			JasperReport report = JasperCompileManager.compileReport(design);
	         float arrear=0;
	         float amtdue=0;
	         Date dat=new Date();
	         CommonUtils commn=new CommonUtils();
	         String curDate=commn.dateToString(dat,rb.getString("calendar.dateformat"));
	         String billDate=commn.dateToString(maintanencefee.getBillDate(),rb.getString("calendar.dateformat"));
	         String username="";	    
	         if(documentShare.getUserId().getFirstName()!=null && !documentShare.getUserId().getFirstName().equalsIgnoreCase("")){
	        	 username=documentShare.getUserId().getFirstName();
	         }
	         if(documentShare.getUserId().getLastName()!=null && !documentShare.getUserId().getLastName().equalsIgnoreCase("") && !username.equalsIgnoreCase("")){
	        	 username+=" "+documentShare.getUserId().getLastName();
	         }else if(documentShare.getUserId().getLastName()!=null && !documentShare.getUserId().getLastName().equalsIgnoreCase("") && username.equalsIgnoreCase("")){
	        	 username+=documentShare.getUserId().getLastName();
	         }
	         if(username!=null){
	        	 username=username.trim();
	         }
	         if(documentShare.getUserId().getEmailId()!=null && !documentShare.getUserId().getEmailId().equalsIgnoreCase("") && username.equalsIgnoreCase("")){
	        	 username=documentShare.getUserId().getEmailId();
	         }
	         if(documentShare.getUserId().getMobileNo()!=null && !documentShare.getUserId().getMobileNo().equalsIgnoreCase("") && username.equalsIgnoreCase("")){
	        	 username=documentShare.getUserId().getMobileNo();
	         }
	         parameters.put("ReportTitle", "SOCYTEA-A .HSG. SOC.");
	         parameters.put("RegNo", "Regn. No.MUM/W-R/HSG/TC/12918/05-06/YEAR 2005.");
	         parameters.put("Addr1", "Vasant Marvel Complex, Western Express Highway,");
	         parameters.put("Addr2", "Borivli (East), Mumbai -400066.");
	         parameters.put("Name", username);
	        
	         parameters.put("BillDate", maintanencefee.getBillDate());
	         parameters.put("BillAmt", Float.toString(maintanencefee.getTotbillamt()));
	         parameters.put("BillDateFirst", curDate);
	         
	         parameters.put("SerCharAmt", Float.toString(maintanencefee.getServiceCharge()));
	         parameters.put("MunicpalTax", Float.toString(maintanencefee.getMunicipalTax()));
	         parameters.put("PenaltyOthers", Float.toString(maintanencefee.getPenalty()));
	         parameters.put("watChar", Float.toString(maintanencefee.getWaterCharge()));
	         parameters.put("nonOccpachCha", Float.toString(maintanencefee.getNonOccupancyCharge()));
	         
	         parameters.put("fedCul", Float.toString(maintanencefee.getCulturalCharge()));
	         parameters.put("sinkinFund", Float.toString(maintanencefee.getSinkingFund()));
	         parameters.put("repairFund",Float.toString(maintanencefee.getRepairFund()));
	         parameters.put("insCharge", Float.toString(maintanencefee.getInsuranceCharges()));
	         parameters.put("playZone", Float.toString(maintanencefee.getPlayZoneCharge()));
	         parameters.put("majorRepairFund",Float.toString(maintanencefee.getMajorRepairFund()));
	         parameters.put("interest",Float.toString(maintanencefee.getInterest()));
	         parameters.put("totAmt", Float.toString(maintanencefee.getTotbillamt()));
	         parameters.put("ArrearsAmt",Float.toString(arrear));
	         amtdue=maintanencefee.getTotbillamt()-arrear;
	         parameters.put("AmtDue",Float.toString(amtdue));
	         
	         Date futdate=commn.addDays(dat,15);
	         String lastDate=commn.dateToString(futdate,rb.getString("calendar.dateformat"));
	         parameters.put("NOTES", "NOTES  : Please pay this bill on or before "+lastDate+" by CHEQUE only.Interest @21% p.aa will be charged on arrears.  Please indicate the");
	         parameters.put("NOTES1", "");
	         parameters.put("NOTES2", "                flat no. on the overleaf of the cheque. Penalty=Rs.500/-i.e.agst MTNL for VASANT MARVEL GLORY CO-OP .HSG. SOC.LTD penalty");
	         parameters.put("NOTES3", "");
	         parameters.put("NOTES4", "Secretary/Treasurer");
	         parameters.put("NOTES5", "E.&.O.E.  $ VIDHYA ACCOUNTS. &  2880 8507.");
	         parameters.put("NOTES6", "");
	         System.out.println("parameters ----------------- "+parameters);
			 JasperPrint print = JasperFillManager.fillReport(report, parameters);
	         JasperExportManager.exportReportToPdfFile(print, filepath+"/"+fileName); 

			}catch (Exception e) {
				e.printStackTrace();
				System.out.println("Exception found in  :::: -------------->>>"+e); 
			}
		return true;
	}
	public boolean generatePdfthread(String filepath,String fileName,MaintenanceFeeTblVO maintanencefee,DocumentShareTblVO documentShare,String lvrJrxml,String username){
		Commonutility.toWriteConsole("Step 1 : [Thread] Generatemaintenancebill called [Start]");
		DocumentUtilitiServices docHibernateUtilService = new DocumentUtilitiDaoServices();
		try{
			File file = new File(filepath);
			if (!file.exists()) {
				Commonutility.crdDir(file.getParent()+"/"+ file.getName());
				file.mkdir();
			}
			//String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/maintenanceDocument.jrxml");
			 //JasperDesign design = JRXmlLoader.load(getText("ApplicationAbsolutePath")+ServletActionContext.getServletContext().getContextPath()+getText("WebContent")+"resources/jsp/PDFCreation/maintenanceDocument.jrxml");
			JasperDesign design = JRXmlLoader.load(lvrJrxml);
			JasperReport report = JasperCompileManager.compileReport(design);
	         float arrear=0;
	         float amtdue=0;
	         Date dat=new Date();
	         CommonUtils commn=new CommonUtils();
	         String curDate=commn.dateToString(dat,rb.getString("calendar.dateformat"));	
			if (documentShare.getUserId().getFirstName()!=null && !documentShare.getUserId().getFirstName().equalsIgnoreCase("")){
	        	 username = documentShare.getUserId().getFirstName();
			}
			if (documentShare.getUserId().getLastName()!=null && !documentShare.getUserId().getLastName().equalsIgnoreCase("") && !username.equalsIgnoreCase("")){
	        	 username += " " + documentShare.getUserId().getLastName();
			} else if(documentShare.getUserId().getLastName()!=null && !documentShare.getUserId().getLastName().equalsIgnoreCase("") && username.equalsIgnoreCase("")){
	        	 username += documentShare.getUserId().getLastName();
			}
			if (username!=null){
	        	 username = username.trim();
			}
			if (documentShare.getUserId().getEmailId()!=null && !documentShare.getUserId().getEmailId().equalsIgnoreCase("") && username.equalsIgnoreCase("")){
	        	 username = documentShare.getUserId().getEmailId();
			}
			if (documentShare.getUserId().getMobileNo()!=null && !documentShare.getUserId().getMobileNo().equalsIgnoreCase("") && username.equalsIgnoreCase("")){
	        	 username = documentShare.getUserId().getMobileNo();
			}
			String lvrSocietyId = null,lvrSocietyname = null, lvrTwnaddress = null, lvrTwnlandmark = null, lvrSocietyregno = null, lvrTwncntyrname = null, lvrTwnstatename = null, lvrTwncityname = null,lvrTwnpincode = null; 
			if (maintanencefee.getUserId().getSocietyId()!=null) {
				lvrSocietyname = maintanencefee.getUserId().getSocietyId().getSocietyName();
				lvrSocietyregno = Commonutility.toCheckNullEmpty(maintanencefee.getUserId().getSocietyId().getRegisterNo());
				if (maintanencefee.getUserId().getSocietyId().getTownshipId()!=null) {
					lvrTwnaddress = Commonutility.toCheckNullEmpty(maintanencefee.getUserId().getSocietyId().getTownshipId().getAddress());
					lvrTwnlandmark = Commonutility.toCheckNullEmpty(maintanencefee.getUserId().getSocietyId().getTownshipId().getLandMark());									
					if (maintanencefee.getUserId().getSocietyId().getTownshipId().getCountryId()!=null) {
						lvrTwncntyrname = Commonutility.toCheckNullEmpty(maintanencefee.getUserId().getSocietyId().getTownshipId().getCountryId().getCountryName());
					} else {
						lvrTwncntyrname =  "";
					}
					if (maintanencefee.getUserId().getSocietyId().getTownshipId().getStateId()!=null) {
						lvrTwnstatename = Commonutility.toCheckNullEmpty(maintanencefee.getUserId().getSocietyId().getTownshipId().getStateId().getStateName());
					} else {
						lvrTwnstatename =  "";
					}
					if (maintanencefee.getUserId().getSocietyId().getTownshipId().getCityId()!=null) {
						lvrTwncityname = Commonutility.toCheckNullEmpty(maintanencefee.getUserId().getSocietyId().getTownshipId().getCityId().getCityName());
					} else {
						lvrTwncityname =  "";
					}
					if (maintanencefee.getUserId().getSocietyId().getTownshipId().getPstlId()!=null) {
//						lvrTwnpincode = Commonutility.toCheckNullEmpty(maintanencefee.getUserId().getSocietyId().getTownshipId().getPstlId().getPstlCode());
						lvrTwnpincode = Commonutility.toCheckNullEmpty(maintanencefee.getUserId().getSocietyId().getTownshipId().getPstlId());
					} else {
						lvrTwnpincode =  "";
					}																																
				} else {
					lvrSocietyname = "SOCYTEA-A .HSG. SOC.";
					lvrSocietyregno = "Regn. No.MUM/W-R/HSG/TC/12918/05-06/YEAR 2005.";					
					lvrTwnlandmark = "Vasant Marvel Complex, Western Express Highway,";
					lvrTwnaddress = "";
					lvrTwncntyrname = "India";
					lvrTwnstatename = "";
					lvrTwncityname = "Borivli (East) - Mumbai";
					lvrTwnpincode = "400066";
				}
				
			} else {
				lvrSocietyname = "SOCYTEA-A .HSG. SOC.";
				lvrSocietyregno = "Regn. No.MUM/W-R/HSG/TC/12918/05-06/YEAR 2005.";					
				lvrTwnlandmark = "Vasant Marvel Complex, Western Express Highway,";
				lvrTwnaddress = "";
				lvrTwncntyrname = "India";
				lvrTwnstatename = "";
				lvrTwncityname = "Borivli (East) - Mumbai";
				lvrTwnpincode = "400066";
			}
			Commonutility.toWriteConsole("lvrSocietyname : "+lvrSocietyname);
			Commonutility.toWriteConsole("lvrSocietyregno : "+lvrSocietyregno);
			Commonutility.toWriteConsole("lvrTwnlandmark : "+lvrTwnlandmark);
			Commonutility.toWriteConsole("lvrTwnaddress : "+lvrTwnaddress);
			Commonutility.toWriteConsole("lvrTwncityname : "+lvrTwncityname);
			Commonutility.toWriteConsole("lvrTwnpincode : "+lvrTwnpincode);
			
			parameters.put("ReportTitle", lvrSocietyname);
	        parameters.put("RegNo", lvrSocietyregno);
	        parameters.put("Addr1", lvrTwnlandmark + lvrTwnaddress);
	        parameters.put("Addr2", lvrTwncityname + " - " + lvrTwnpincode + ".");
	        parameters.put("Name", username);
	        parameters.put("BillNo", maintanencefee.getBillno());					
			//parameters.put("ReportTitle", "SOCYTEA-A .HSG. SOC.");
	        //parameters.put("RegNo", "Regn. No.MUM/W-R/HSG/TC/12918/05-06/YEAR 2005.");
	        //parameters.put("Addr1", "Vasant Marvel Complex, Western Express Highway,");
	        //parameters.put("Addr2", "Borivli (East), Mumbai -400066.");
	        //parameters.put("Name", username);
	        //parameters.put("BillNo", maintanencefee.getBillno());
	        String billdate="";
			if (maintanencefee.getBillDate() != null) {
				String billdates=String.valueOf(maintanencefee.getBillDate());
	        	String aa[]=billdates.split("-");
				if (aa[1].equalsIgnoreCase("01")) {
					billdate = "January-" + aa[0];
				} else if (aa[1].equalsIgnoreCase("02")) {
					billdate = "February-" + aa[0];
				} else if (aa[1].equalsIgnoreCase("03")) {
					billdate = "March-" + aa[0];
				} else if (aa[1].equalsIgnoreCase("04")) {
					billdate = "April-" + aa[0];
				} else if (aa[1].equalsIgnoreCase("05")) {
					billdate = "May-" + aa[0];
				} else if (aa[1].equalsIgnoreCase("06")) {
					billdate = "June-" + aa[0];
				} else if (aa[1].equalsIgnoreCase("07")) {
					billdate = "July-" + aa[0];
				} else if (aa[1].equalsIgnoreCase("08")) {
					billdate = "August-" + aa[0];
				} else if (aa[1].equalsIgnoreCase("09")) {
					billdate = "September-" + aa[0];
				} else if (aa[1].equalsIgnoreCase("10")) {
					billdate = "October-" + aa[0];
				} else if (aa[1].equalsIgnoreCase("11")) {
					billdate = "November -" + aa[0];
				} else if (aa[1].equalsIgnoreCase("12")) {
					billdate = "December-" + aa[0];
				} else {
				}
	         }
	         parameters.put("BillDate", billdate);
	         parameters.put("BillAmt", Float.toString(maintanencefee.getTotbillamt()));
	         parameters.put("BillDateFirst", curDate);
	         parameters.put("SerCharAmt", Float.toString(maintanencefee.getServiceCharge()));
	         parameters.put("MunicpalTax", Float.toString(maintanencefee.getMunicipalTax()));
	         parameters.put("PenaltyOthers", Float.toString(maintanencefee.getPenalty()));
	         parameters.put("watChar", Float.toString(maintanencefee.getWaterCharge()));
	         parameters.put("nonOccpachCha", Float.toString(maintanencefee.getNonOccupancyCharge()));
	         parameters.put("fedCul", Float.toString(maintanencefee.getCulturalCharge()));
	         parameters.put("sinkinFund", Float.toString(maintanencefee.getSinkingFund()));
	         parameters.put("repairFund",Float.toString(maintanencefee.getRepairFund()));
	         parameters.put("insCharge", Float.toString(maintanencefee.getInsuranceCharges()));
	         parameters.put("playZone", Float.toString(maintanencefee.getPlayZoneCharge()));
	         parameters.put("majorRepairFund",Float.toString(maintanencefee.getMajorRepairFund()));
	         parameters.put("interest",Float.toString(maintanencefee.getInterest()));
	         parameters.put("latefee",Float.toString(maintanencefee.getLatefee()));
	         parameters.put("carpark1",Float.toString(maintanencefee.getCarpark1()));
	         parameters.put("carpark2",Float.toString(maintanencefee.getCarpark2()));
	         parameters.put("twowheeler1",Float.toString(maintanencefee.getTwowheeler1()));
	         parameters.put("twowheeler2",Float.toString(maintanencefee.getTwowheeler2()));
	         parameters.put("sundayadjust",Float.toString(maintanencefee.getSundayadjust()));
	         parameters.put("protax",Float.toString(maintanencefee.getProtax()));
	         parameters.put("exceespay",Float.toString(maintanencefee.getExceespay()));
	         parameters.put("clubhouse",Float.toString(maintanencefee.getClubhouse()));
	         parameters.put("totAmt", Float.toString(maintanencefee.getTotbillamt()));
	         arrear = maintanencefee.getArrears();
	         parameters.put("ArrearsAmt",Float.toString(arrear));
	         amtdue=maintanencefee.getTotbillamt()-arrear;
	         parameters.put("AmtDue",Float.toString(amtdue));
	         parameters.put("rupeess",maintanencefee.getAmountword());
	         Date futdate=commn.addDays(dat,15);
	         String lastDate=commn.dateToString(futdate,rb.getString("calendar.dateformat"));
	         parameters.put("NOTES", "NOTES  : Please pay this bill on or before "+lastDate+" by CHEQUE only.Interest @21% p.aa will be charged on arrears.  Please indicate the");
	         parameters.put("NOTES1", "");
	         parameters.put("NOTES2", "                flat no. on the overleaf of the cheque. Penalty=Rs.500/-i.e.agst MTNL for "+lvrSocietyname+" penalty");
	         parameters.put("NOTES3", "");
	         parameters.put("NOTES4", "Secretary/Treasurer");
	         parameters.put("NOTES5", "E.&.O.E. "+lvrSocietyname);
	         parameters.put("NOTES6", "");
	         
	         parameters.put("previousDues",Float.toString(maintanencefee.getPreviousdue()));
	         parameters.put("appSubscriptionFee",Float.toString(maintanencefee.getAppfees()));
	         
			 JasperPrint print = JasperFillManager.fillReport(report, parameters);
	         JasperExportManager.exportReportToPdfFile(print, filepath+"/"+fileName); 
	         Commonutility.toWriteConsole("Step 2 : [Thread] Generatemaintenancebill called [End]");
		} catch (Exception e) {
				System.out.println("Exception found in GenerateMaintenance.class generatePdfthread() : "+e); 
		} finally {
			
		}
		return true;
	}
	public boolean generatePdfXlthread(){
		
		
		return true;
	}
	public static void main(String args[]){
		GenerateMaintenancePdf gpf=new GenerateMaintenancePdf();
		//gpf.generatePdf("D:/test","test.pdf");
		 
	}


}
