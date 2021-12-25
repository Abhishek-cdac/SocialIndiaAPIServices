package com.mobi.easypay;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.easypayvo.persistence.MaintenanceFeeDao;
import com.mobi.easypayvo.persistence.MaintenanceFeeDaoServices;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.MaintenanceFeeTblVO;

public class MaintenanceFeeList extends ActionSupport {
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	Log log= new Log();
	
	public String execute() {
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		FunctionUtility commonFn = new FunctionUtilityServices();
		try {
			locErrorvalStrBuil = new StringBuilder();
			String townShipid = null;
			String societyKey = null;
			int rid = 0;
			String locRid = null;
			String searchQury = "";
			String locTimeStamp = null;
			String startlimit = null;
			int totCnt = 0;
			log.logMessage("Enter into MaintenanceFeeList ", "info", MaintenanceFeeList.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("MaintenanceFeeList ivrparams :" + ivrparams, "info", MaintenanceFeeList.class);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						locRid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						locTimeStamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
						startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");
						//searchFlg = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"search_flg");
					}
					if (Commonutility.checkempty(ivrservicecode)) {
						if (ivrservicecode.length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
							
						} else {
							String[] passData = { getText("service.code.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.error")));
					}					
					if (Commonutility.checkempty(townShipid)) {
						if (townShipid.length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
							
						} else {
							String[] passData = { getText("townshipid.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
					}
					
					if (Commonutility.checkempty(societyKey)) {
						if (societyKey.length() == Commonutility.stringToInteger(getText("societykey.fixed.length"))) {
							
						} else {
							String[] passData = { getText("societykey.fixed.length") };
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length.error", passData)));
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.error")));
					}				
					
					if (locObjRecvdataJson !=null) {
						if (Commonutility.checkempty(locRid) && Commonutility.toCheckisNumeric(locRid)) {
							rid = Commonutility.stringToInteger(locRid);
							if (rid !=0 ) {
								searchQury += " and userId.userId = '"+rid+"' and paidStatus='0' ";
							} else {
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
							}
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
						}
						if (Commonutility.checkempty(locTimeStamp)) {
						} else {
							locTimeStamp=Commonutility.timeStampRetStringVal();
						}
						if(startlimit!=null && startlimit.length()>0) { } else {
							startlimit="0";
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("json.data.object.error")));
					}
					
				}else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.encode.error")));
				}
			} else {
				flg = false;
				locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("params.error")));
			}
			log.logMessage("MaintenanceFeeList flg :" +flg, "info", MaintenanceFeeList.class);
			if (flg) {
				locObjRspdataJson=new JSONObject();
				CommonMobiDao commonServ = new CommonMobiDaoService();
				flg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
				if(flg){
					flg = commonServ.checkSocietyKey(societyKey, Commonutility.intToString(rid));
					if (flg) {
						MaintenanceFeeTblVO maintenanceFeeObj = new MaintenanceFeeTblVO();
						MaintenanceFeeDao maintenanceFeeDAOobj = new MaintenanceFeeDaoServices();
						List<MaintenanceFeeTblVO> billListObj = new ArrayList<MaintenanceFeeTblVO>();
						billListObj = maintenanceFeeDAOobj.maintenanceBillSearchList(searchQury,startlimit,getText("total.row"), locTimeStamp);
						JSONObject finalJsonarr=new JSONObject();
						JSONArray jsonArr = new JSONArray();
						if (billListObj != null && billListObj.size() > 0) {
							locObjRspdataJson = new JSONObject();
							maintenanceFeeObj = new MaintenanceFeeTblVO();
							for (Iterator<MaintenanceFeeTblVO> it = billListObj.iterator();it.hasNext();) {
								maintenanceFeeObj = it.next();
								finalJsonarr=new JSONObject();
								
								System.out.println("maintenanceFeeObj.getTotbillamt()----" + maintenanceFeeObj.getTotbillamt());
								if (Commonutility.checkempty(Commonutility.floatToString(maintenanceFeeObj.getTotbillamt()))) {
									float priceAmt = maintenanceFeeObj.getTotbillamt();
									DecimalFormat df = new DecimalFormat("#.00");
									df.setMaximumFractionDigits(2);
									finalJsonarr.put("total_amount",df.format(priceAmt));				
								} else {
									finalJsonarr.put("total_amount","");
								}
								System.out.println("maintenanceFeeObj.getPaidStatus()----" + maintenanceFeeObj.getPaidStatus());
								if (Commonutility.checkIntnull(maintenanceFeeObj.getPaidStatus())) {
									finalJsonarr.put("paid_status",Integer.toString(maintenanceFeeObj.getPaidStatus()));
								} else {
									finalJsonarr.put("paid_status","");
								}
								if (Commonutility.checkIntnull(maintenanceFeeObj.getMaintenanceId())) {
									finalJsonarr.put("maintenance_id",Integer.toString(maintenanceFeeObj.getMaintenanceId()));
								} else {
									finalJsonarr.put("maintenance_id","");
								}
								System.out.println("maintenanceFeeObj.getBillDate()----" + maintenanceFeeObj.getBillDate());
								if (maintenanceFeeObj.getBillDate()!=null && !maintenanceFeeObj.getBillDate().equals(null) ) {
									 DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
									 String getBilldate = date.format(maintenanceFeeObj.getBillDate());
									 String billdateWords = commonFn.getMothName(maintenanceFeeObj.getBillDate().getMonth()).substring(0, 3)+" "+getBilldate.substring(0, 4);
									finalJsonarr.put("bill_date",Commonutility.stringToStringempty(getBilldate));	
									finalJsonarr.put("bill_title",Commonutility.stringToStringempty(billdateWords));	
								} else {
									finalJsonarr.put("bill_date","");
									finalJsonarr.put("bill_title","");	
								}
								System.out.println("maintenanceFeeObj.getBillno()----" + maintenanceFeeObj.getBillno());
								if (Commonutility.checkempty(maintenanceFeeObj.getBillno())) {
									finalJsonarr.put("bill_no",maintenanceFeeObj.getBillno());
								} else {
									finalJsonarr.put("bill_no","");
								}
								System.out.println("maintenanceFeeObj.getFlatno()----" + maintenanceFeeObj.getFlatno());
								if (Commonutility.checkempty(maintenanceFeeObj.getFlatno())) {
									finalJsonarr.put("flat_no",maintenanceFeeObj.getFlatno().trim());
								} else {
									finalJsonarr.put("flat_no","");
								}
								System.out.println("maintenanceFeeObj.getNotes()----" + maintenanceFeeObj.getNotes());
								if (Commonutility.checkempty(maintenanceFeeObj.getNotes())) {
									finalJsonarr.put("notes",maintenanceFeeObj.getNotes());
								} else {
									finalJsonarr.put("notes","");
								}
								System.out.println("maintenanceFeeObj.getMaintenanceId()=========="+ maintenanceFeeObj.getMaintenanceId());
								int varDoucmnetAttach = maintenanceFeeObj.getMaintenanceId();
								List<DocumentManageTblVO> documentList=new ArrayList<DocumentManageTblVO>();
								System.out.println("varDoucmnetAttach=========="+ String.valueOf(varDoucmnetAttach));
								documentList = maintenanceFeeDAOobj.documnetAttachList(String.valueOf(varDoucmnetAttach),rid);
								System.out.println("===documentList==="+documentList.size());
								DocumentManageTblVO documenteAttachObj;
								for (Iterator<DocumentManageTblVO> itObj = documentList.iterator(); itObj.hasNext();) {
									documenteAttachObj = itObj.next();
									//finalJsonarr = new JSONObject();
									if(documenteAttachObj.getDocFileName() != null) {
										String docPath=getText("SOCIAL_INDIA_BASE_URL") +"/externalPath/document/";
										String publicOrPrivateTath = "";
										String generalpath = "";
										String userPath="";
										String dateFolderPath = "";
										String fileUrl = "";
										if (documenteAttachObj.getDocFolder() == 1) { //public
											publicOrPrivateTath = getText("external.documnet.public.fldr");
										} else if (documenteAttachObj.getDocFolder() == 2) { //private
											publicOrPrivateTath = getText("external.documnet.private.fldr");
										}
										if (documenteAttachObj.getDocSubFolder() == 1) { //Maintenance
											generalpath = getText("external.documnet.maintenancedoc.fldr");
										} else if (documenteAttachObj.getDocFolder() == 2) { //General
											generalpath = getText("external.documnet.generaldoc.fldr");
										}
										String webpath=getText("external.inner.webpath");
										userPath = locRid+"/";
										if (Commonutility.checkempty(documenteAttachObj.getDocDateFolderName())) {
											dateFolderPath = documenteAttachObj.getDocDateFolderName()+"/";
										}
										fileUrl = docPath+publicOrPrivateTath+generalpath+webpath+userPath+dateFolderPath+URLEncoder.encode(documenteAttachObj.getDocFileName(), "UTF-8").replace("+", "%20");
										finalJsonarr.put("file_url",fileUrl);
									} else {
										finalJsonarr.put("file_url","");
									}
								}
								jsonArr.put(finalJsonarr);
							}
							locObjRspdataJson.put("maintenance_details", jsonArr);
							locObjRspdataJson.put("timestamp", locTimeStamp);
							CommonMobiDao commonHbm=new CommonMobiDaoService();
							String locVrSlQry="SELECT count(maintenanceId) FROM MaintenanceFeeTblVO where statusFlag=1 " + searchQury + " and entryDatetime <STR_TO_DATE('" + locTimeStamp + "','%Y-%m-%d %H:%i:%S') ";
							System.out.println("=======locVrSlQry======="+ locVrSlQry);
							totCnt = commonHbm.getTotalCountQuery(locVrSlQry);
							System.out.println("=======totCnt======="+ totCnt);
							locObjRspdataJson.put("totalrecords", totCnt);
							if (flg) {
								serverResponse(ivrservicecode,getText("status.success"),"R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
							} else {
								locObjRspdataJson = new JSONObject();
								serverResponse(ivrservicecode,getText("status.warning"),"R0006",mobiCommon.getMsg("R0006"),locObjRspdataJson);
							}
						} else{
							locObjRspdataJson=new JSONObject();
							serverResponse(ivrservicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
						}
						
					} else {
						locObjRspdataJson=new JSONObject();
						serverResponse(ivrservicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);	
					}
				} else {
					locObjRspdataJson=new JSONObject();
					serverResponse(ivrservicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);	
				}												
			} else {
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			locObjRspdataJson=new JSONObject();
			log.logMessage(getText("Eex") + ex, "error", MaintenanceFeeList.class);
			serverResponse(ivrservicecode,getText("status.error"),"R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
		} finally {			
		}
		return SUCCESS;
	}
	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson)
	{
		PrintWriter out=null;
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();		
		try {
			out = response.getWriter();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			mobiCommon mobicomn=new mobiCommon();
			String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
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

	public String getIvrservicecode() {
		return ivrservicecode;
	}

	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}

}
