package com.siservices.societyMgmt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.login.EncDecrypt;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class siSocietyUpdateAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<TownshipMstTbl> townShipList = new ArrayList<TownshipMstTbl>();
	TownshipMstTbl townShipMst =new TownshipMstTbl();
	SocietyMstTbl societyMst=new SocietyMstTbl();
	MvpSocietyAccTbl societyAccData=new MvpSocietyAccTbl();
	societyMgmtDao society=new societyMgmtDaoServices();
	JSONObject jsonFinalObj=new JSONObject();
	boolean result=false;
	int uniqueVal;

	Log log=new Log();
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String logoImageFileName=null,icoImageFileName=null,noofblockswings=null,registerno=null,societycolourcode=null;
		String imprintname=null,societyiconame=null,societylogoname=null;
		String accountno=null;
		String bankname=null,accountname=null,ifsccode=null;
		byte conbytetoarr[] = new byte[1024];
		byte conbytetoarr1[] = new byte[1024];
		// Session locObjsession = null;
		String locSlqry = null;
		Query locQryrst = null;
		GroupMasterTblVo locGrpMstrVOobj = null, locGRPMstrvo = null;
		String ivrservicecode = null, flatnames = null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		Session pSession = null;
		try{
			pSession = HibernateUtil.getSession();;
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				
				int societyid = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "societyid");
				int townshipId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "townshipId");
				String societyname = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "societyname");
				int noofmembers = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "noofmembers");
				
				noofblockswings=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "noofblockswings");
				registerno=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "registerno");
				imprintname=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imprintname");
				flatnames=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "flatnames");
				String emailId=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "emailId");
				
				String gstin=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "gstin");
				String hsn=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "hsn");
				
				//Address
//				String country=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "country");
//				String state=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "state");
//				String city=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "city");
//				String postalcode=(int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "postalcode")+"";
//				String address=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "address");
				
				accountno = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "accountno");
				bankname=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "bankname");
				accountname=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "accountname");
				ifsccode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "ifsccode");
				
				societycolourcode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "societycolourcode");
				societylogoname=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "societylogoname");
				societyiconame=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "societyiconame");
				
				logoImageFileName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "logoImageFileName");
				icoImageFileName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "icoImageFileName");
				societyMst.setSocietyId(societyid);
				societyMst.setSocietyName(societyname);
				societyMst.setNoOfMemebers(noofmembers);
				societyMst.setNoOfBlocksWings(noofblockswings);
				societyMst.setRegisterNo(registerno);
				societyMst.setImprintName(imprintname);
				societyMst.setColourCode(societycolourcode);
				
				societyMst.setGstin(gstin);
				societyMst.setHsn(hsn);
				
				societyAccData.setBankAccNo(accountno);
				societyAccData.setBankName(bankname);
				societyAccData.setBankAccName(accountname);
				societyAccData.setIfscCode(ifsccode);
				int locGrpcode=0;				
				locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+getText("Grp.society")+"') or groupName=upper('"+getText("Grp.society")+"')";			 
				locQryrst=pSession.createQuery(locSlqry);			
				 locGrpMstrVOobj=(GroupMasterTblVo) locQryrst.uniqueResult();
				 if(locGrpMstrVOobj!=null){
					 locGRPMstrvo=new GroupMasterTblVo();
					 locGrpcode=locGrpMstrVOobj.getGroupCode();
									 
				 }else{// new group create
					 uamDao uam=new uamDaoServices();
					  locGrpcode=uam.createnewgroup_rtnId(getText("Grp.society"));
					 		 				
				 }
				if (logoImageFileName!=null && !logoImageFileName.equalsIgnoreCase("")) {
					logoImageFileName = logoImageFileName.replaceAll(" ", "_");
					societyMst.setLogoImage(logoImageFileName);
				} else {
						societyMst.setLogoImage("");
				}
				if(icoImageFileName!=null && !icoImageFileName.equalsIgnoreCase("")) {
						icoImageFileName = icoImageFileName.replaceAll(" ", "_");						
						societyMst.setIcoImage(icoImageFileName);
				}else{
						societyMst.setIcoImage("");
				}
					String locVrSlQry="";				
					uniqueVal = society.societyInfoUpdate(societyMst,townshipId,flatnames,societyAccData,emailId,locGrpcode);
				
					if (uniqueVal == -2) {// society name exist
						locObjRspdataJson.put("servicecode", ivrservicecode);
						serverResponse(ivrservicecode, "02", "R0065", mobiCommon.getMsg("R0065"), jsonFinalObj);
					} else if (uniqueVal == -3) {
						locObjRspdataJson.put("servicecode", ivrservicecode);
						serverResponse(ivrservicecode,"01","R0176",mobiCommon.getMsg("R0176"),locObjRspdataJson);
					} else {		
				//File to write  Mobiledb
				String filepath=rb.getString("external.mobiledbfldr.path");
				CommonUtils comutil=new CommonUtilsServices();
				String textvalue="update SOCIETY_MST_TBL set SOCIETY_NAME='"+societyMst.getSocietyName()+"', NO_OF_BLOCK_WINGS='"+societyMst.getNoOfBlocksWings()+"', NO_OF_COMMITTEE_MEMBRS="+societyMst.getNoOfMemebers()+", " + " REGISTRATION_NO='"+societyMst.getRegisterNo()+"', IMPRINT_NAME='"+societyMst.getImprintName()+"', COLOR_CODE='"+societyMst.getColourCode()+"', LOGO_NAME='" + societyMst.getLogoImage()+"', ICO_NAME= '"+societyMst.getIcoImage()+"', MODIFY_DATETIME='"+comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"', " 
				+ " GSTIN = '"+societyMst.getGstin()+"', HSN = '"+societyMst.getHsn() + "' " + " where SOCIETY_ID="+societyMst.getSocietyId()+";";					
				Commonutility.TextFileWriting(filepath, textvalue);				
				/*if(logoImageFileName!=null && logoImageFileName!="" && !logoImageFileName.equalsIgnoreCase("") && uniqueVal>0){
					conbytetoarr=Commonutility.toDecodeB64StrtoByary(societylogoname);
					
					String topath=rb.getString("external.uploadfile.society.webpath")+societyMst.getSocietyId();					
					String wrtsts=Commonutility.toByteArraytoWriteFiles(conbytetoarr, topath, logoImageFileName);	
					String limgSourcePath=rb.getString("external.uploadfile.society.webpath")+societyMst.getSocietyId()+"/"+logoImageFileName;				
					String limgDisPath = rb.getString("external.uploadfile.society.mobilepath")+societyMst.getSocietyId()+"/";	 		 	
					String limgName = FilenameUtils.getBaseName(logoImageFileName);
					String limageFomat = FilenameUtils.getExtension(logoImageFileName);
					String limageFor = "all";
					int lneedWidth = Commonutility.stringToInteger(getText("thump.img.width"));
					int lneedHeight = Commonutility.stringToInteger(getText("thump.img.height"));	
					ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile	        	
				}
	        	 if(icoImageFileName!=null && icoImageFileName!="" && !icoImageFileName.equalsIgnoreCase("") && uniqueVal>0){						
						conbytetoarr1=Commonutility.toDecodeB64StrtoByary(societyiconame);
						String topath1=rb.getString("external.uploadfile.society.webpath")+societyMst.getSocietyId();
						String wrtsts1=Commonutility.toByteArraytoWriteFiles(conbytetoarr1, topath1, icoImageFileName);												
				}*/
				 
				// Create MNTNC BILL EXCEL
				JSONObject chkboxs = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "chkboxs");
				
				filepath = rb.getString("external.mntnc_bill_excel") + "/" + societyMst.getSocietyId();
				Commonutility.toWriteConsole("siSocietyMgmtCreate.class execute() Method filepath: "+ filepath);
				File file = new File(filepath);
				if(!file.exists()){
					file.mkdirs();
				}
				createExcelTemplate(filepath + "/" + societyMst.getSocietyId() + ".xlsx", chkboxs);
				
	        	 serverResponse(ivrservicecode,"00","R0175",mobiCommon.getMsg("R0175"),locObjRspdataJson);
				}
				
				}else{
					//Response call
					serverResponse(ivrservicecode,"01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}	
			}else{
				//Response call
				serverResponse(ivrservicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);

			}	
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Exception found .class execute() Method : "+e);
			log.logMessage("Service code : ivrservicecode, Sorry, an unhandled error occurred", "info", siSocietyUpdateAction.class);
			try {
				serverResponse(ivrservicecode,"02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			} catch (Exception e1) {}
		}finally{
			if(pSession!=null){pSession.flush();pSession.clear();pSession.close();pSession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	System.gc();
		}				
		return SUCCESS;
	} 
	
	private void createExcelTemplate(String excelFileName, JSONObject chkboxs) throws IOException, JSONException {
		
//		Map<String,Integer> map = new HashMap<String,Integer>();
//		map.put("Monthly_QTLY_Maintenances", 1);
//		map.put("Municipal_Tax", 2);
//		map.put("Water_Charge", 3);
//		map.put("Federation_Charges", 4);
//		map.put("Repair_Fund", 5);
//		map.put("Sinking_Fund", 6);
//		map.put("Major_Repair_Funds", 7);
//		map.put("Non_Occupancy_Charges", 8);
//		map.put("Play_Zone_Fees", 9);
//		map.put("Penalties", 10);
//		map.put("Interest", 11);
//		map.put("Late_Fees", 12);
//		map.put("Insurance_Cost", 13);
//		map.put("Car_Parking_1", 14);
//		map.put("Car_Parking_2", 15);
//		map.put("Two_Wheeler_1", 16);
//		map.put("Two_Wheeler_2", 17);
//		map.put("Sundry_Adjustment", 18);
//		map.put("Property_Tax", 19);
//		map.put("Excess_Payments", 20);
//		map.put("Club_House", 21);
//		map.put("Arrears", 22);
//		map.put("Previous_Dues", 23);
//		map.put("APP_Subscription_Fee", 24);
//		map.put("Total_Payable", 25);
//		map.put("Amount_in_Words", 26);
//		map.put("Bill_No", 27);
//		map.put("Flat_No", 28);
//		map.put("Flat_Area", 29);
//		map.put("Notes", 30);
//		map.put("RSRVD1", 31);
//		map.put("RSRVD2", 32);
//		map.put("RSRVD3", 33);
//		map.put("RSRVD4", 34);
//		map.put("RSRVD5", 35);
//		map.put("RSRVD6", 36);
//		map.put("RSRVD7", 37);
//		map.put("RSRVD8", 38);
//		map.put("RSRVD9", 39);
//		map.put("RSRVD10", 40);

		String sheetName = "bill";// name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		XSSFRow row = sheet.createRow(0);

		// iterating c number of columns
//		String[] cols = { "Monthly / QTLY maintenances(Numeric) ", "Municipal tax(Numeric)",
//				"Water charge(Numeric)", "Federation charges(Numeric)", "Repair fund(Numeric)",
//				"Sinking fund(Numeric)", "Major repair funds(Numeric)", "Non-Occupancy charges(Numeric)",
//				"Play zone fees(Numeric)", "Penalties(Numeric)", "Interest(Numeric)", "Late fees(Numeric)",
//				"Insurance cost(Numeric)", "Car parking 1(Numeric)", "Car parking 2(Numeric)",
//				"Two wheeler 1(Numeric)", "Two wheeler 2(Numeric)", "Sundry adjustment(Numeric)",
//				"Property tax(Numeric)", "Excess Payments(Numeric)", "Club House(Numeric)", "Arrears(Numeric)",
//				"Previous Dues(Numeric)", "APP Subscription Fee(Numeric)", "Total Payable(Numeric)",
//				"Amount in Words(Text)", "Bill No(Alpha Numeric)", "Flat No(Alpha Numeric)", "Flat Area(Alpha Numeric)", "Notes(Text)",
//				"RSRVD 1(Alpha Numeric)", "RSRVD 2(Alpha Numeric)", "RSRVD 3(Alpha Numeric)", "RSRVD 4(Alpha Numeric)", "RSRVD 5(Alpha Numeric)",
//				"RSRVD 6(Alpha Numeric)", "RSRVD 7(Alpha Numeric)", "RSRVD 8(Alpha Numeric)", "RSRVD 9(Alpha Numeric)", "RSRVD 10(Alpha Numeric)" };
//
//		System.out.println(cols.length);

		int cnt = 0;
		Iterator<String> keys = chkboxs.keys();
		while(keys.hasNext()){
			String key = keys.next();
		    String val = chkboxs.getString(key);
//		    Commonutility.toWriteConsole("Step 1 : Society Create Checkbox : key = "+ key + " val = " +val);
			if(val!=null && val.equalsIgnoreCase("true")){
//				XSSFCell cell = row.createCell(map.get(key));
				XSSFCell cell = row.createCell(cnt);
				cell.setCellValue(key);
				cnt++;
			}
		}
		
//		for (int i = 0; i < chkboxs.length(); i++) {
//			
//		}

		FileOutputStream fileOut = new FileOutputStream(excelFileName);

		// write this workbook to an Outputstream.
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
	}
	
	private void serverResponse(String serviceCode, String statusCode,
			String respCode, String message, JSONObject dataJson)
			throws Exception {
		PrintWriter out;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response;
		response = ServletActionContext.getResponse();
		out = response.getWriter();
		try {
			responseMsg = new JSONObject();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			responseMsg.put("servicecode", serviceCode);
			responseMsg.put("statuscode", statusCode);
			responseMsg.put("respcode", respCode);
			responseMsg.put("message", message);
			responseMsg.put("data", dataJson);
			String as = responseMsg.toString();
			out.print(as);
			out.close();
		} catch (Exception ex) {
			out = response.getWriter();
			out.print("{\"servicecode\":\"" + serviceCode + "\",");
			out.print("{\"statuscode\":\"2\",");
			out.print("{\"respcode\":\"E0002\",");
			out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
			out.print("{\"data\":\"{}\"}");
			out.close();
			ex.printStackTrace();
		}
	}

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	public TownshipMstTbl getTownShipMst() {
		return townShipMst;
	}

	public void setTownShipMst(TownshipMstTbl townShipMst) {
		this.townShipMst = townShipMst;
	}

}
