package com.pack.labor;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class LaborUpdate extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private String ivrparams;
	private String ivrservicefor;
	private File fileUpload;
	private String fileUploadContentType;
	private String fileUploadFileName;
	public String execute() {
		Log log = new Log();
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		//String lsvSlQry = null;
		Session locObjsession = null;
		String ivrservicecode = null;
		String ivrDecissBlkflag=null; // 1- new create, 2- update, 3 - select single[], 4 - deActive , 5 - delete , 6 - Block
		String ivrCurntusridstr=null;
		int ivrEntryByusrid=0;
		boolean flg = true;
		StringBuilder locErrorvalStrBuil =null;
		JSONObject locRspjson=null;
		String iswebmobilefla=null;
		try {
			System.out.println(" laborinsert fileUploadFileName---"+fileUploadFileName);
			CommonMobiDao commonServ = new CommonMobiDaoService();
			locErrorvalStrBuil = new StringBuilder();
			locObjsession = HibernateUtil.getSession();
			if (ivrparams != null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length() > 0) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				System.out.println(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"servicecode");
					ivrCurntusridstr = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"currentloginid");
					 iswebmobilefla =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "is_mobile");
					String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
					String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					if(ivrCurntusridstr!=null && Commonutility.toCheckisNumeric(ivrCurntusridstr)){
						ivrEntryByusrid= Integer.parseInt(ivrCurntusridstr);
					}else{ ivrEntryByusrid=0; }

					if (ivrservicefor != null && !ivrservicefor.equalsIgnoreCase("null") && ivrservicefor.length() > 0) {//get
						ivrDecissBlkflag = ivrservicefor;
					}else{//post
						ivrDecissBlkflag = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicefor");
					}


					String locvrDecissBlkflagchk=Commonutility.toCheckNullEmpty(ivrDecissBlkflag);
					String locvrFnrst=null;
					String locRtnSrvId=null,locRtnStsCd=null, locRtnRspCode=null,locRtnMsg=null;
					locObjRspdataJson=new JSONObject();
					if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("1")){// insert labor
						System.out.println("insert laborinsert labor---"+fileUploadFileName);
						String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.laborfldr")+getText("external.inner.webpath");
						String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.laborfldr")+getText("external.inner.mobilepath");
						 if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
							 System.out.println("-----"+iswebmobilefla);
							 if (Commonutility.checkempty(ivrservicecode)) {
								 System.out.println("--ivrservicecode--"+ivrservicecode);
									if (ivrservicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
										 System.out.println("--ivrservicecode11--"+ivrservicecode);
										 if (flg) {
											flg = commonServ.checkSocietyKey(societykey, ivrCurntusridstr);
										 }
									} else {
										String[] passData = { getText("service.code.fixed.length") };
										flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length", passData)));
									}
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("Service code cannot be empty")));
								}
							 if (Commonutility.checkempty(townshipKey)) {
									if (townshipKey.trim().length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
										//success
										flg=commonServ.checkTownshipKey(townshipKey,ivrCurntusridstr);
										System.out.println("desflg=== "+flg);
									} else {
										String[] passData = { getText("townshipid.fixed.length") };
										flg = false;
										locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length", passData)));
									}
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
								}
							 if (Commonutility.checkempty(societykey)) {
									if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {

									} else {
										String[] passData = { getText("society.fixed.length") };
										flg = false;
										locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
									}
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
								}
							 }
							 else{
								 flg = true;
							 }
						 if(flg) {
							 System.out.println("insert goes............."+locObjRecvdataJson);
							 locvrFnrst = LaborUtility.toInsrtLabor(locObjRecvdataJson, getText("Grp.labor"), getText("LBAD004"), "LBAD004",locWebImgFldrPath,locMobImgFldrPath,iswebmobilefla,fileUploadFileName,fileUpload);//labor group name passing
						 }
						String locSbt[]=locvrFnrst.split("!_!");
						System.out.println(locSbt.length+" : Labour Length");
						if(locSbt!=null && locSbt.length>=2){
							if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("success") && !locSbt[0].equalsIgnoreCase("-1") && !locSbt[0].equalsIgnoreCase("0") && !locSbt[0].equalsIgnoreCase("-2")){
								locObjRspdataJson.put("status", locSbt[0]);
								locObjRspdataJson.put("message", "Labor created successfully.");
								locRtnSrvId="R0124";locRtnStsCd="00"; locRtnRspCode="R0124";locRtnMsg = mobiCommon.getMsg("R0124");
								AuditTrial.toWriteAudit(getText("LBAD001"), "LBAD001", ivrEntryByusrid);
							} else if(locSbt[0]!=null && locSbt[0].equalsIgnoreCase("duplicate") && !locSbt[0].equalsIgnoreCase("-1") && !locSbt[0].equalsIgnoreCase("0")){
								locObjRspdataJson.put("status", locSbt[0]);
								locObjRspdataJson.put("message", "Duplicate, Labor already registered with us.");
								locRtnSrvId="SI3000";locRtnStsCd="02"; locRtnRspCode="R0155"; locRtnMsg = "Duplicate, Labor already registered with us.";
								AuditTrial.toWriteAudit(getText("LBAD007"), "LBAD007", ivrEntryByusrid);
							} else {
								locObjRspdataJson.put("status", locSbt[0]);
								locObjRspdataJson.put("message", "Labor not created");
								locRtnSrvId="SI3000";locRtnStsCd="1"; locRtnRspCode="E3000"; locRtnMsg=getText("labor.create.error");
								AuditTrial.toWriteAudit(getText("LBAD007"), "LBAD007", ivrEntryByusrid);
							}
						}else{
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", "Labor not created");
							locRtnSrvId="SI3000";locRtnStsCd="1"; locRtnRspCode="E3000"; locRtnMsg=getText("labor.create.error");
							AuditTrial.toWriteAudit(getText("LBAD007"), "LBAD007", ivrEntryByusrid);
						}
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("2")){// update labor
						String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.laborfldr")+getText("external.inner.webpath");
						String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.laborfldr")+getText("external.inner.mobilepath");
						if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
							 System.out.println("-----"+iswebmobilefla);
							 if (Commonutility.checkempty(ivrservicecode)) {
								 System.out.println("--ivrservicecode--"+ivrservicecode);
									if (ivrservicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
										 System.out.println("--ivrservicecode11--"+ivrservicecode);

										if(flg)
										{
											flg = commonServ.checkSocietyKey(societykey, ivrCurntusridstr);
										}
									} else {
										String[] passData = { getText("service.code.fixed.length") };
										flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length", passData)));
									}
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("Service code cannot be empty")));
								}
							 if (Commonutility.checkempty(townshipKey)) {
									if (townshipKey.trim().length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
										//success
										flg=commonServ.checkTownshipKey(townshipKey,ivrCurntusridstr);
										System.out.println("desflg=== "+flg);
									} else {
										String[] passData = { getText("townshipid.fixed.length") };
										flg = false;
										locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length", passData)));
									}
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
								}
							 if (Commonutility.checkempty(societykey)) {
									if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {

									} else {
										String[] passData = { getText("society.fixed.length") };
										flg = false;
										locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
									}
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
								}
							 }
							 else{
								 flg = true;
							 }
						if(flg)
						{
						locvrFnrst=LaborUtility.toUpdtLabor(locObjRecvdataJson, getText("LBAD004"), "LBAD004",locWebImgFldrPath,locMobImgFldrPath,iswebmobilefla,fileUploadFileName,fileUpload);
						}
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", "Labor updated successfully.");
							locRtnSrvId="R0126";locRtnStsCd="00"; locRtnRspCode="R0126";locRtnMsg=mobiCommon.getMsg("R0126");
							AuditTrial.toWriteAudit(getText("LBAD002"), "LBAD002", ivrEntryByusrid);
						}else{
							locObjRspdataJson=new JSONObject();
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", "Labor not updated");
							locRtnSrvId="R0127";locRtnStsCd="01"; locRtnRspCode="R0127"; locRtnMsg=mobiCommon.getMsg("R0127");
							AuditTrial.toWriteAudit(getText("LBAD008"), "LBAD008", ivrEntryByusrid);
						}

					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("3")){//select single labor detail
						if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
							 System.out.println("-----"+iswebmobilefla);
							 if (Commonutility.checkempty(ivrservicecode)) {
								 System.out.println("--ivrservicecode--"+ivrservicecode);
									if (ivrservicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
										 System.out.println("--ivrservicecode11--"+ivrservicecode);

										if(flg)
										{
											flg = commonServ.checkSocietyKey(societykey, ivrCurntusridstr);
										}
									} else {
										String[] passData = { getText("service.code.fixed.length") };
										flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length", passData)));
									}
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("Service code cannot be empty")));
								}
							 if (Commonutility.checkempty(townshipKey)) {
									if (townshipKey.trim().length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
										//success
										flg=commonServ.checkTownshipKey(townshipKey,ivrCurntusridstr);
										System.out.println("desflg=== "+flg);
									} else {
										String[] passData = { getText("townshipid.fixed.length") };
										flg = false;
										locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length", passData)));
									}
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
								}
							 if (Commonutility.checkempty(societykey)) {
									if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {

									} else {
										String[] passData = { getText("society.fixed.length") };
										flg = false;
										locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
									}
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
								}
							 }
							 else{
								 flg = true;
							 }
						if(flg)
						{
						 locRspjson=LaborUtility.toSltSingleLabourDtl(locObjRecvdataJson,locObjsession, getText("LBAD006"), "LBAD006",iswebmobilefla);
						}
						locvrFnrst =(String) Commonutility.toHasChkJsonRtnValObj(locRspjson,"catch");
						if(locvrFnrst==null || locvrFnrst.equalsIgnoreCase("null") || locvrFnrst.equalsIgnoreCase("") || !locvrFnrst.equalsIgnoreCase("Error")){
							locObjRspdataJson=locRspjson;
							locRtnSrvId="SI3002";locRtnStsCd="0"; locRtnRspCode="S3002";locRtnMsg=getText("labor.select.success");
							AuditTrial.toWriteAudit(getText("LBAD006"), "LBAD006", ivrEntryByusrid);
						}else{
							locObjRspdataJson=new JSONObject();
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", "Labor not selected.");
							locRtnSrvId="SI3002";locRtnStsCd="0"; locRtnRspCode="E3002";locRtnMsg=getText("labor.select.error");
							AuditTrial.toWriteAudit(getText("LBAD012"), "LBAD012", ivrEntryByusrid);
						}

					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("4")){// deActive labor
						if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
							 System.out.println("-----"+iswebmobilefla);
							 if (Commonutility.checkempty(ivrservicecode)) {
								 System.out.println("--ivrservicecode--"+ivrservicecode);
									if (ivrservicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
										 System.out.println("--ivrservicecode11--"+ivrservicecode);

										if(flg)
										{
											flg = commonServ.checkSocietyKey(societykey, ivrCurntusridstr);
										}
									} else {
										String[] passData = { getText("service.code.fixed.length") };
										flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length", passData)));
									}
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("Service code cannot be empty")));
								}
							 if (Commonutility.checkempty(townshipKey)) {
									if (townshipKey.trim().length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
										//success
										flg=commonServ.checkTownshipKey(townshipKey,ivrCurntusridstr);
										System.out.println("desflg=== "+flg);
									} else {
										String[] passData = { getText("townshipid.fixed.length") };
										flg = false;
										locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length", passData)));
									}
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
								}
							 if (Commonutility.checkempty(societykey)) {
									if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {

									} else {
										String[] passData = { getText("society.fixed.length") };
										flg = false;
										locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
									}
								} else {
									flg = false;
									locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid")));
								}
							 }
							 else{
								 flg = true;
							 }
						if(flg){
						locvrFnrst = LaborUtility.toDeActLabor(locObjRecvdataJson,locObjsession, getText("LBAD003"), "LBAD003");
						System.out.println("-locvrFnrst -"+locvrFnrst);
						}
						if(locvrFnrst!=null && locvrFnrst.equalsIgnoreCase("success")){
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", "Labor deactivate successfully.");
							locRtnSrvId="R0128";locRtnStsCd="00"; locRtnRspCode="R0128";locRtnMsg=mobiCommon.getMsg("R0128");
							AuditTrial.toWriteAudit(getText("LBAD003"), "LBAD003", ivrEntryByusrid);
						}else{
							locObjRspdataJson=new JSONObject();
							locObjRspdataJson.put("status", locvrFnrst);
							locObjRspdataJson.put("message", "Labor not deactivate");
							locRtnSrvId="R0129";locRtnStsCd="01"; locRtnRspCode="R0129"; locRtnMsg=mobiCommon.getMsg("R0129");
							AuditTrial.toWriteAudit(getText("LBAD009"), "LBAD009", ivrEntryByusrid);
						}
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("5")){// delete labor
						locvrFnrst = LaborUtility.toDltLabor(locObjRecvdataJson,locObjsession);
					}else if(locvrDecissBlkflagchk!="" && !locvrDecissBlkflagchk.equalsIgnoreCase("") && locvrDecissBlkflagchk.equalsIgnoreCase("6")){// Block labor
						locvrFnrst = LaborUtility.toDltLabor(locObjRecvdataJson,locObjsession);
					}else{
						locvrFnrst="";
						locObjRspdataJson=new JSONObject();
						log.logMessage("Service code : SI3001, "+getText("service.notmach"), "info", LaborUpdate.class);
						locRtnSrvId="SI3001";locRtnStsCd="1"; locRtnRspCode="SE3001"; locRtnMsg=getText("service.notmach");
					}
					serverResponse(locRtnSrvId,locRtnStsCd,locRtnRspCode,locRtnMsg,locObjRspdataJson,iswebmobilefla);
				} else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI3001,"+getText("request.format.notmach")+"", "info", LaborUpdate.class);
					serverResponse("SI3001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson,iswebmobilefla);
				}
			} else {
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI3001,"+getText("request.values.empty")+"", "info", LaborUpdate.class);
				serverResponse("SI3001","1","ER0001",getText("request.values.empty"),locObjRspdataJson,iswebmobilefla);
			}
		} catch (Exception e) {
			System.out.println("Exception found LaborUpdate.class execute() Method : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI3001, Sorry, an unhandled error occurred", "error", LaborUpdate.class);
			serverResponse("SI3001","2","ER0002",getText("catch.error"),locObjRspdataJson,iswebmobilefla);
		} finally {
			if (locObjsession != null) {locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession = null;}
			locObjRecvJson = null;locObjRecvdataJson = null;locObjRspdataJson = null;
		}
		return SUCCESS;
	}
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson,String iswebmobilefla)
	{
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();
		try {
			if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
				out = response.getWriter();
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-store");
				mobiCommon mobicomn=new mobiCommon();
				String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
				out.print(as);
				out.close();
			}
			else{
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
			}
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
	public File getFileUpload() {
		return fileUpload;
	}
	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}
	public String getFileUploadContentType() {
		return fileUploadContentType;
	}
	public void setFileUploadContentType(String fileUploadContentType) {
		this.fileUploadContentType = fileUploadContentType;
	}
	public String getFileUploadFileName() {
		return fileUploadFileName;
	}
	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}



}
