package com.social.billmaintenance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.billmaintenanceDao.BillMaintenanceDao;
import com.social.billmaintenanceDao.BillMaintenanceDaoServices;
import com.social.billmaintenanceVO.BillmaintenanceVO;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.social.utititymgmt.MaintenanceExcelDocUploadEngine;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.DocumentUtilitiDaoServices;
import com.socialindiaservices.services.DocumentUtilitiServices;
import com.socialindiaservices.vo.MaintenanceFileUploadTblVO;

public class DocumentBill extends ActionSupport{
	private String ivrparams;
	private String ivrservicefor;
	private String ivrservicecode;
	private String entryby;
	int ivrEntryByusrid = 0;
	Log log = new Log();
	String societyid = "";
	CommonUtils common = new CommonUtils();
	private DocumentUtilitiServices docHibernateUtilService = new DocumentUtilitiDaoServices();
	ResourceBundle ivrRsbdle = ResourceBundle.getBundle("inputfilespecify");
	private static final long serialVersionUID = 1L;
	public String execute(){
		MaintenanceFileUploadTblVO maintenancefiles=null;
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		
		Integer ivrEntryByusrid = 0;
		String locRtnSrvId = null, locRtnStsCd = null, locRtnRspCode = null, locRtnMsg = null;
		Session session = null;
		UserMasterTblVo entrydet = null;
		BillmaintenanceVO billdata = null;
		BillMaintenanceDao billmainDao = null;
		
		Double lvrDataTotalval = 0.0;
		String lvrMessagertn = "";
		try {
			entrydet = new UserMasterTblVo();
			billmainDao = new BillMaintenanceDaoServices();
			maintenancefiles = new MaintenanceFileUploadTblVO();
			session = HibernateUtil.getSessionFactory().openSession();
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {	
					UserMasterTblVo userdet = new UserMasterTblVo();
					GroupMasterTblVo groupdet = new GroupMasterTblVo();
					
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");	
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"userId");
					if (ivrEntryByusrid != null) {
					} else {
						ivrEntryByusrid = 0;
					}					
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					int userId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "userId");
					int usrTyp = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "usrTyp");
					String societyCodestr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "societyCode");					
					
					Integer societyCode = 0;
					if (Commonutility.checkempty(societyCodestr)) {
						societyCode = Integer.parseInt(societyCodestr);
					}
					userdet.setUserId(userId);
					groupdet.setGroupCode(usrTyp);
					String imageDetail = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imageDetail");
					String fileName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "fileName");	
					String lvrUplarsrchpath = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "uploadsrchpath");						
					
					String limgName = FilenameUtils.getBaseName(fileName);
					if(limgName==null || limgName.equalsIgnoreCase("null")){
						limgName ="";
					}
					
					String limageFomat = FilenameUtils.getExtension(fileName);
					if(limageFomat==null || limageFomat.equalsIgnoreCase("null")){
		 		 		limageFomat ="";
					}
					Date date = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH-mm-ss") ;
					String file_name = "";
					if (limgName.length() > 0) {
						  file_name =limgName+dateFormat.format(date)+"."+limageFomat;
					}
					String locupFldrPath = getText("external.fldr") + getText("external.maintenancefld");//file upload path
					//backupfile path
					 String locuplbackupPath=getText("external.fldr")+getText("external.backupfldr");
					 String filepath=locupFldrPath+ivrEntryByusrid+"/"+file_name;
					 String lvrFldrpath=locupFldrPath+ivrEntryByusrid+"/";
					 
					 
					 if(Commonutility.checkempty(lvrUplarsrchpath) && Commonutility.checkempty(file_name)){
							/*	byte imgbytee[]= new byte[1024];
								imgbytee=Commonutility.toDecodeB64StrtoByary(imageDetail);
								String wrtsts=Commonutility.toByteArraytoWriteFiles(imgbytee, locupFldrPath+ivrEntryByusrid+"/", file_name);
							*/
						 	Commonutility.crdDir(lvrFldrpath);
							Commonutility.toWriteConsole("File Upload Start");	
							File lvrimgSrchPathFileobj = new File(lvrUplarsrchpath);// source path
							Commonutility.toWriteConsole("Source Path : "+lvrimgSrchPathFileobj.getAbsolutePath());
							File lvrFileToCreate = new File (locupFldrPath+ivrEntryByusrid+"/",file_name);  
							Commonutility.toWriteConsole("lvrFileToCreate : "+lvrFileToCreate.getAbsolutePath());						
					        FileUtils.copyFile(lvrimgSrchPathFileobj, lvrFileToCreate);//copying source file to new file
					 
					 
					 }
					 
					String locuplFldrPath = locupFldrPath+ivrEntryByusrid+"/"+file_name;
					String checksum = Commonutility.getChecksumValue(filepath);
					String qryCheck="select checksum from MaintenanceFileUploadTblVO where checksum='"+checksum+"'";
					String checkdubval=billmainDao.checkDuplicate(qryCheck);
					Commonutility.toWriteConsole("checkdubval ---------21122121212-------- "+checkdubval);
					if(checkdubval==null ||checksum.equalsIgnoreCase("null") || checkdubval.equalsIgnoreCase("")) {
						Date curDates = new Date();
						entrydet.setUserId(userId);
						maintenancefiles.setFileName(file_name);
						maintenancefiles.setStatus(0);
						maintenancefiles.setChecksum(checksum);
						maintenancefiles.setEntryBy(entrydet);
						maintenancefiles.setEntryDatetime(curDates);
						if (societyCode>0) {
							maintenancefiles.setSocietyId(societyCode);
						} else {
							maintenancefiles.setSocietyId(null);
						}
						
						int ids = docHibernateUtilService.insertMaintenanceFilesTbl(maintenancefiles,session);
						Commonutility.toWriteConsole("insertMaintenanceFilesTbl :  "+ids);					
//						boolean lvrInsertsts = false;
//						String input = "";
//						String qry = "";
//						boolean lvrdelete = false;
//						boolean header = false;
//						boolean fooder = false;
//						boolean bodycontent = false;
						
//						FileInputStream fis = new FileInputStream(filepath);
//			        	InputStreamReader sr = new InputStreamReader(fis);
//						BufferedReader br = new BufferedReader(sr);
//						while ((input = br.readLine()) != null) {
//							
//							if(input.isEmpty())
//								continue;
//							
//							String data = input;
//							Commonutility.toWriteConsole("input : "+input);
//							
//							billdata = new BillmaintenanceVO();
//			            	Commonutility.toWriteConsole("Data Length : "+data.length());
//			            	if (Commonutility.checkempty(data) && data.length() == Integer.parseInt(getText("input.header.data")) && data.startsWith(getText("starting_char"))) {
//			            		String townId = ivrRsbdle.getString("Township_ID");
//								String[] townsplit = townId.split("-");
//								Integer start = Integer.parseInt(townsplit[0]);
//								Integer end = Integer.parseInt(townsplit[1]);								
//								String township = data.substring(start, end);
//								end = 0;
//								start = 0;
//
//								String socId = ivrRsbdle.getString("Society_ID");
//								String[] socsplit = socId.split("-");
//								start = Integer.parseInt(socsplit[0]);
//								end = Integer.parseInt(socsplit[1]); 
//								String society = data.substring(start, end);
//								end = 0;
//								start = 0;	
//																
//								Commonutility.toWriteConsole("Township : "+township.trim());
//								Commonutility.toWriteConsole("Society : "+society.trim());
//								boolean result = billmainDao.checksocietytown(township.trim(),society.trim());
//								Commonutility.toWriteConsole("Society and township check : "+result);
//								societyid=society;
//			            		if (result) {
//				            		billdata.setRefId(userId);
//				            		billdata.setData(data);
//				            		billdata.setHeader(1);
//				            		billdata.setFooder(0);
//				            		billdata.setStatus(1);
//				            		if (societyCode>0) {
//				            			billdata.setRefId(societyCode);
//				            		} else {
//				            			billdata.setRefId(null);
//				            		}
//				            		billdata.setEntryDatetime(Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));			  
//				            		lvrInsertsts=billmainDao.insertbilldata(billdata, session);
//				            		header = lvrInsertsts;		
//			            		} else {
//			            			Commonutility.toWriteConsole("Step 1 : Society and township check (SOCIETY ID AND TOWNSHP ID NOT MATCH): "+result);
//			            			header = false;
//			            			lvrMessagertn = "Society id and township id not correct.";
//			            			break;
//			            		}
//			            	} else if (header && Commonutility.checkempty(data) &&  data.length() == Integer.parseInt(getText("input.bodycontent.data"))) {
//			            		
//			            		Commonutility.toWriteConsole("data : "+data);
//			            		
//			            		String totalpayId = ivrRsbdle.getString("Total_Payable");
//								String[] totalpayIdsplt = totalpayId.split("-");
//								Integer startPostion = Integer.parseInt(totalpayIdsplt[0]);
//								Integer endPostion = Integer.parseInt(totalpayIdsplt[1]); 
//								String totbillamt = data.substring(startPostion, endPostion);
//								Commonutility.toWriteConsole("totbillamt : "+totbillamt);
//								
//								String flatnoId = ivrRsbdle.getString("Flat_No");
//								String[] flatnoIdsplt = flatnoId.split("-");
//								// for(int i=0; i<townsplit.length;i++){
//								Integer start = Integer.parseInt(flatnoIdsplt[0]);
//								Integer end = Integer.parseInt(flatnoIdsplt[1]); 
//								// }
//								String flatno = data.substring(start, end);
//								Commonutility.toWriteConsole("Flat_No : "+flatno);
//								
//								Integer usrId = billmainDao.togetusrvalue(societyid.trim(),flatno.trim());
//								Commonutility.toWriteConsole("usrId : "+usrId);
//								System.out.println("usrId =--------billdata-------- "+usrId);
//								end = 0;
//								start = 0;
//								if (usrId > 0) {
//			            		if (Commonutility.checkempty(totbillamt)) {			            			
//			            			lvrDataTotalval += Double.parseDouble(totbillamt);
//			            			billdata.setRefId(userId);
//				            		billdata.setData(data);
//				            		billdata.setHeader(0);
//				            		billdata.setFooder(0);
//				            		billdata.setStatus(1);
//				            		if (societyCode>0) {
//				            			billdata.setRefId(societyCode);
//				            		} else {
//				            			billdata.setRefId(null);
//				            		}
//				            		billdata.setEntryDatetime(Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
//				            		lvrInsertsts=billmainDao.insertbilldata(billdata,session);
//									bodycontent = lvrInsertsts;
//			            		} else {
//			            			Commonutility.toWriteConsole("Step 2: Bill amount not found in some record");
//			            			bodycontent = false;
//			            			lvrMessagertn = "Bill amount not found in some record.";
//			            			break;
//			            		}
//								}else{
//									Commonutility.toWriteConsole("Step 2: Not match for Flat No.");
//			            			bodycontent = false;
//			            			lvrMessagertn = "There is no flat no. in this file.";
//			            			break;
//								}
//								
//								
//							} else if (header && bodycontent && Commonutility.checkempty(data) &&  data.length()==Integer.parseInt(getText("input.fooder.data")) && data.endsWith(getText("ending_char"))) {
//								
//								Commonutility.toWriteConsole("footer data : "+data);
//								
//								String sumofallamtrecordsId = ivrRsbdle.getString("Sum_of_all_amounts_in_all_records");
//								if(Commonutility.checkempty(sumofallamtrecordsId)) {
//									String[] sumofallamtrecordsIdsplt = sumofallamtrecordsId.split("-");						
//									Integer startPostion = Integer.parseInt(sumofallamtrecordsIdsplt[0]);
//									Integer endPostion = Integer.parseInt(sumofallamtrecordsIdsplt[1]);
//									String sumofallamtrecords = data.substring(startPostion, endPostion);
//									if(Commonutility.checkempty(sumofallamtrecords)) {
//										double lvrFinalTotla = Double.parseDouble(sumofallamtrecords);
//										Commonutility.toWriteConsole("Record of total payamout : "+lvrDataTotalval);
//										Commonutility.toWriteConsole("Sum of total : "+lvrFinalTotla);
//										if (lvrFinalTotla == lvrDataTotalval) {
//											billdata.setRefId(userId);
//											billdata.setData(data);
//											billdata.setHeader(0);
//											billdata.setFooder(1);
//											billdata.setStatus(1);
//											if (societyCode > 0) {
//											billdata.setRefId(societyCode);
//											} else {
//												billdata.setRefId(null);
//											}
//											billdata.setEntryDatetime(Commonutility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
//											lvrInsertsts = billmainDao.insertbilldata(billdata,session);
//											fooder = lvrInsertsts;
//										} else {
//											Commonutility.toWriteConsole("Step 5: Sum of all amount and total record of bill amount not match.");
//											fooder = false;
//											lvrMessagertn = "Sum of all amount and total record of bill amount not match.";
//					            			break;
//										}
//									} else {
//										Commonutility.toWriteConsole("Step 4: Sum of all amount and total record of bill amount not match.");
//										fooder = false;
//										lvrMessagertn = "Sum of all amount not found in footer record.";
//				            			break;
//									}
//																	
//								} else {
//									Commonutility.toWriteConsole("Step 3 : Sum of all amount not found / not match in footer record.");
//									fooder = false;
//									lvrMessagertn = "Sum of all amount not found / not match in footer record.";
//			            			break;
//									
//								}															
//							} else {
//								Commonutility.toWriteConsole("header : "+header + " bodycontent:"+bodycontent);
//								lvrMessagertn = mobiCommon.getMsg("R0231");
//								break;
//							}
//			           
//			            }
//						Commonutility.toWriteConsole("header Status : "+header);
//						Commonutility.toWriteConsole("bodycontent Status : "+bodycontent);
//						Commonutility.toWriteConsole("Footer Status : "+fooder);
//						
//						if (fooder && bodycontent && header) {
							String lvrJrxml = ServletActionContext.getServletContext().getRealPath("/resources/jsp/PDFCreation/maintenanceDocument.jrxml");
							Commonutility.toWriteConsole("Details==============="+locupFldrPath+ivrEntryByusrid+"/------"+locuplbackupPath+"-------------"+locuplFldrPath+"-----------"+userId+"----------"+limageFomat+"------------------"+usrTyp+"-------------"+lvrJrxml);
//							BillMaintenanceDocumentUploadEngine maintenanceBillDocEngine = new BillMaintenanceDocumentUploadEngine(checksum,userId,societyCode,usrTyp,file_name,lvrJrxml);
//							maintenanceBillDocEngine.start();
							
//							BillMaintenanceDocUploadEngine maintenanceBillDocEngine = new BillMaintenanceDocUploadEngine(checksum,userId,societyCode,usrTyp,file_name,lvrJrxml);
//							maintenanceBillDocEngine.run();
							
							//Excel
							 String xlUpldFldr = locupFldrPath+ivrEntryByusrid;
							 String xlBkupFldr = locuplbackupPath;
							 String xlUpldFile = filepath;
							 String xlExt = FilenameUtils.getExtension(file_name);
							 
							 
							MaintenanceExcelDocUploadEngine uploadEngine = new MaintenanceExcelDocUploadEngine(xlUpldFldr,xlBkupFldr,xlUpldFile,ivrEntryByusrid,xlExt,usrTyp,lvrJrxml, societyCode, file_name, checksum);
							String result = uploadEngine.run();
							
							if(result!=null && result.equalsIgnoreCase("success")){
								locObjRspdataJson=new JSONObject();	
								locObjRspdataJson.put("status", "SUCCESS");
								locObjRspdataJson.put("message", "Successfully Uploaded Data Will be Extracted.");
								locRtnSrvId="SI6406";locRtnStsCd="00"; locRtnRspCode="R0084";locRtnMsg=mobiCommon.getMsg("R0084");	
								serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
							}
							else{
								locObjRspdataJson=new JSONObject();	
								locObjRspdataJson.put("status", "ERROR");
								locObjRspdataJson.put("message", "ERROR Uploading Data. "+result);
								locRtnSrvId="SI6406";locRtnStsCd="02"; locRtnRspCode="R0087";locRtnMsg=mobiCommon.getMsg("R0087");	
								serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg + "	" + result,locObjRspdataJson);
							}
//						} else {
//							qry= "delete BillmaintenanceVO where refId ="+societyCode;
//			            	Commonutility.toWriteConsole("Delete Query : "+qry );
//							lvrdelete = billmainDao.toDeleteEvent(qry);
//							
//							locObjRspdataJson=new JSONObject();								
//							locRtnSrvId="SI6406";locRtnStsCd="01"; locRtnRspCode="R0231";locRtnMsg=lvrMessagertn;	
//							serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson);
//							
//						}
			            
						
						}else{
							locObjRspdataJson=new JSONObject();
							log.logMessage("Service code : SI6406,"+mobiCommon.getMsg("R0229")+"", "info", DocumentBill.class);
							serverResponse("SI6403","01","R0067",mobiCommon.getMsg("R0229"),locObjRspdataJson);	
						}
					
				} else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6406,"+getText("request.format.notmach")+"", "info", DocumentBill.class);
					serverResponse("SI6403","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6406,"+mobiCommon.getMsg("R0003")+"", "info", DocumentBill.class);
				serverResponse("SI6403","01","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			}
			
		} catch (Exception ex){
			Commonutility.toWriteConsole(mobiCommon.getMsg("R0087") + " : "+ex);
			log.logMessage("Service code : SI6406,"+mobiCommon.getMsg("R0087")+" : "+ex, "error", DocumentBill.class);
			serverResponse("SI6403", "02", "R0087", mobiCommon.getMsg("R0087"), locObjRspdataJson);
		} finally {
			if (session!=null) {session.flush();session.clear();session.close();session=null;}
		}
		return SUCCESS;
	}
	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson){
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();		
		try {
			out = response.getWriter();
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
			try{
			out = response.getWriter();
			out.print("{\"servicecode\":\"" + serviceCode + "\",");
			out.print("{\"statuscode\":\"2\",");
			out.print("{\"respcode\":\"E0002\",");
			out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
			out.print("{\"data\":\"{}\"}");
			out.close();
			ex.printStackTrace();
			}catch(Exception e){}finally{}
		}
	}
	public String getIvrparams() {
		return ivrparams;
	}
	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}
	public String getIvrservicefor() {
		return ivrservicefor;
	}
	public void setIvrservicefor(String ivrservicefor) {
		this.ivrservicefor = ivrservicefor;
	}
	public String getIvrservicecode() {
		return ivrservicecode;
	}
	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}
	public String getEntryby() {
		return entryby;
	}
	public void setEntryby(String entryby) {
		this.entryby = entryby;
	}
	public int getIvrEntryByusrid() {
		return ivrEntryByusrid;
	}
	public void setIvrEntryByusrid(int ivrEntryByusrid) {
		this.ivrEntryByusrid = ivrEntryByusrid;
	}
	

}
