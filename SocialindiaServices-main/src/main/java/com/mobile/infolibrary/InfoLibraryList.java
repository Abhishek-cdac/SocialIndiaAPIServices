package com.mobile.infolibrary;

import java.io.PrintWriter;
import java.net.URLEncoder;
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
import com.mobile.familymember.familyMemberDao;
import com.mobile.familymember.familyMemberDaoServices;
import com.mobile.familymember.familyMemberList;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.paswordservice.Forgetpassword;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.CarPoolingTblVO;
import com.socialindiaservices.vo.DocumentManageTblVO;
import com.socialindiaservices.vo.DocumentShareTblVO;

public class InfoLibraryList extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	InfoLibraryDao infoLibrary=new InfoLibraryDaoServices();
	profileDao profile=new profileDaoServices();
	List<DocumentShareTblVO> docMShareList=new ArrayList<DocumentShareTblVO>();
	List<DocumentManageTblVO> docMngList=new ArrayList<DocumentManageTblVO>();
	CommonUtils commjvm=new CommonUtils();
	CommonMobiDao commonHbm=new CommonMobiDaoService();
	
	public String execute(){
		
		System.out.println("************mobile otp services******************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		String servicecode="";
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			System.out.println("ivrparams:" + ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String folderType = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "folder_type");
				String timestamp = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "timestamp");
				String startlimit = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startlimit");
				String searchText = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "search_text");
				if (Commonutility.checkempty(servicecode)) {
					if (servicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
						
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
						
					} else {
						String[] passData = { getText("townshipid.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
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
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.error")));
				}
				
				if (locObjRecvdataJson !=null) {
					if (Commonutility.checkempty(rid)) {
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
					}
				}
				
				if(flg){
					
					if (Commonutility.checkempty(timestamp)) {
					} else {
						timestamp=Commonutility.timeStampRetStringVal();
					}
					
					
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
			System.out.println("InfoLibraryList - checkTownshipKey() ********result*****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			
			System.out.println("InfoLibraryList - checkSocietyKeyForList() - userMst.getSocietyId() = "+userMst.getSocietyId() + " 2nd id= "+userMst.getSocietyId().getSocietyId());
			if(userMst!=null){
				
					String locVrSlQry="";
				
					int societyId=userMst.getSocietyId().getSocietyId();
					String searchField="";
					boolean ispublic=false;
					if(folderType!=null && folderType.length()>0 && folderType.equalsIgnoreCase("1")){
						searchField+=" and  docFolder=1 and docSubFolder=2";
						ispublic=true;
					}else if(folderType!=null && folderType.length()>0 && folderType.equalsIgnoreCase("2")){
						searchField+=" and  documentId.docFolder=2 and (documentId.docSubFolder=1 or documentId.docSubFolder=2)";
					}else if(folderType!=null && folderType.length()>0 && folderType.equalsIgnoreCase("3")){
						searchField+=" and  docFolder=1 and docSubFolder=2";
						ispublic=true;
					}
					
					if(searchText!=null && searchText.length()>0){
						String firstlet=searchText.substring(0,1);
						if(firstlet.equalsIgnoreCase("*")){
							//doubt
							searchText=firstlet.substring(1,firstlet.length());
							if(ispublic){
								searchField+=" and  docFileName like('%"+searchText+"')";
							}else{
							searchField+=" and  documentId.docFileName like('%"+searchText+"')";
							}
						}else{
							if(ispublic){
								searchField+=" and  docFileName like('%"+searchText+"')";
							}else{
							searchField+=" and  documentId.docFileName like('%"+searchText+"%')";
							}
						}
						
					}
					int totalcnt=0;
					int pubtotalcnt=0;
					if(ispublic){
						locVrSlQry="select count(*) from DocumentManageTblVO where  entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') "
								+ "and statusFlag = 1 and userId.societyId.societyId="+societyId+" "+searchField;
						pubtotalcnt=commonHbm.getTotalCountQuery(locVrSlQry);
					}else{
						locVrSlQry="select count(*) from DocumentShareTblVO where userId.userId='"+Integer.parseInt(rid)+"' and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') "
								+ "and status = 1 and userId.societyId.societyId="+societyId+" "+searchField;
						totalcnt=commonHbm.getTotalCountQuery(locVrSlQry);
					}
					int gtotal=totalcnt+pubtotalcnt;
					if(gtotal>0){
						
					String qry="from DocumentShareTblVO where userId.userId="+Integer.parseInt(rid)+" and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') "
							+ "and status = 1 and userId.societyId.societyId="+societyId+" "+searchField+" order by documentShareId desc ";
					
					System.out.println("totalcnt-------------"+totalcnt);
					JSONArray jArray =new JSONArray();
					if(totalcnt>0){
					docMShareList=infoLibrary.getDocumentList(qry,startlimit,getText("total.row"),societyId);
					System.out.println("=========doc Share List======="+docMShareList.size());
					JSONObject finalJsonarr=new JSONObject();
					locObjRspdataJson=new JSONObject();
					
					if( docMShareList!=null && docMShareList.size()>0){
						DocumentShareTblVO docahareobj;
						for(Iterator<DocumentShareTblVO> it=docMShareList.iterator();it.hasNext();){
							docahareobj=it.next();
							finalJsonarr = new JSONObject();
							finalJsonarr.put("doc_id", ""+docahareobj.getDocumentId().getDocumentId());//need to doo
							String fileName=docahareobj.getDocumentId().getDocFileName();
							String dateFolderPath=docahareobj.getDocumentId().getDocDateFolderName();
							Integer documentType= docahareobj.getDocumentId().getDocTypId().getIvrBnDOC_TYP_ID();
							Integer shareId= docahareobj.getUserId().getUserId();
							System.out.println("fileName-------------"+fileName);
							if(fileName!=null){
								String extension=commjvm.getFileExtension(fileName);
								System.out.println("extension------------"+extension);
								if(extension!=null){
									System.out.println("extension---------------"+extension);
									mobiCommon mobCom=new mobiCommon();
									 Integer fileType=mobCom.getFileExtensionType(extension);
									 finalJsonarr.put("file_type", ""+fileType);	
								}else{
									finalJsonarr.put("file_type", "9");	
									//finalJsonarr.put("file_url", "");
									//finalJsonarr.put("size", "0");
								}
									
									String docPath=System.getenv("SOCIAL_INDIA_BASE_URL")  +"/externalPath/document/";
									String docsizPath=getText("external.documnet.fldr");
									String publicOrPrivateTath=getText("external.documnet.private.fldr");
									String generalpath="";
									String webpath=getText("external.inner.webpath");
									String userPath="";
									if(shareId==null){
										publicOrPrivateTath=getText("external.documnet.public.fldr");
										dateFolderPath=dateFolderPath+"/";
									}else{
										publicOrPrivateTath=getText("external.documnet.private.fldr");
										userPath=shareId+"/";
										dateFolderPath=dateFolderPath+"/";
									}
									if(documentType==8){
										generalpath=getText("external.documnet.maintenancedoc.fldr");
									}else{
										generalpath=getText("external.documnet.generaldoc.fldr");
										
									}
									String fileUrl=docPath+publicOrPrivateTath+generalpath+webpath+userPath+dateFolderPath+URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
									String filesizeUrl=docsizPath+publicOrPrivateTath+generalpath+webpath+userPath+dateFolderPath+fileName;
									finalJsonarr.put("file_url",fileUrl);
									System.out.println("fileUrl-----------------"+filesizeUrl);
									System.out.println(" commjvm.getFileSizefileUrl-------------"+ commjvm.getFileSize(filesizeUrl, "MB"));
									finalJsonarr.put("size", commjvm.getFileSize(filesizeUrl, "MB"));
								
								finalJsonarr.put("file_name",fileName);
							}else{
								finalJsonarr.put("file_type", "");
								finalJsonarr.put("file_name", "");
								finalJsonarr.put("size", "0");
								finalJsonarr.put("file_url", "");
							}
							
							jArray.put(finalJsonarr);
						}
					
					}
					}
					
						//userId.userId="+Integer.parseInt(rid)+" and
						 qry="from DocumentManageTblVO where  entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') "
								+ "and statusFlag = 1 and userId.societyId.societyId="+societyId+" "+searchField;
						
						System.out.println("pubtotalcnt-------------"+pubtotalcnt);
						if(pubtotalcnt>0){
							docMngList=infoLibrary.getDocumentList(qry,startlimit,getText("total.row"),societyId);
						System.out.println("=========doc Share List======="+docMShareList.size());
						JSONObject finalJsonarr=new JSONObject();
						locObjRspdataJson=new JSONObject();
						
						if( docMngList!=null && docMngList.size()>0){
							DocumentManageTblVO docmangobj;
							for(Iterator<DocumentManageTblVO> it=docMngList.iterator();it.hasNext();){
								docmangobj=it.next();
								finalJsonarr = new JSONObject();
								finalJsonarr.put("doc_id", ""+docmangobj.getDocumentId());//need to doo
								String fileName=docmangobj.getDocFileName();
								String dateFolderPath=docmangobj.getDocDateFolderName();
								Integer documentType= docmangobj.getDocTypId().getIvrBnDOC_TYP_ID();
								Integer shareId= docmangobj.getUserId().getUserId();
								System.out.println("fileName-------------"+fileName);
								if(fileName!=null){
									String extension=commjvm.getFileExtension(fileName);
									System.out.println("extension------------"+extension);
									if(extension!=null){
										System.out.println("extension---------------"+extension);
										mobiCommon mobCom=new mobiCommon();
										 Integer fileType=mobCom.getFileExtensionType(extension);
										 finalJsonarr.put("file_type", ""+fileType);	
									}else{
										finalJsonarr.put("file_type", "9");	
										//finalJsonarr.put("file_url", "");
										//finalJsonarr.put("size", "0");
									}
										
										String docPath=System.getenv("SOCIAL_INDIA_BASE_URL")  +"/externalPath/document/";
										String docsizPath=getText("external.documnet.fldr");
										String publicOrPrivateTath=getText("external.documnet.public.fldr");
										String generalpath="";
										String webpath=getText("external.inner.webpath");
										String userPath="";
										//if(shareId==null){
											publicOrPrivateTath=getText("external.documnet.public.fldr");
											dateFolderPath=dateFolderPath+"/";
										/*}else{
											publicOrPrivateTath=getText("external.documnet.private.fldr");
											userPath=shareId+"/";
											dateFolderPath=dateFolderPath+"/";
										}*/
										if(documentType==8){
											generalpath=getText("external.documnet.maintenancedoc.fldr");
										}else{
											generalpath=getText("external.documnet.generaldoc.fldr");
											
										}
										String fileUrl=docPath+publicOrPrivateTath+generalpath+webpath+userPath+dateFolderPath+URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
										String filesizeUrl=docsizPath+publicOrPrivateTath+generalpath+webpath+userPath+dateFolderPath+fileName;
										finalJsonarr.put("file_url",fileUrl);
										System.out.println("fileUrl-----------------"+filesizeUrl);
										System.out.println(" commjvm.getFileSizefileUrl-------------"+ commjvm.getFileSize(filesizeUrl, "MB"));
										finalJsonarr.put("size", commjvm.getFileSize(filesizeUrl, "MB"));
									
									finalJsonarr.put("file_name",fileName);
								}else{
									finalJsonarr.put("file_type", "");
									finalJsonarr.put("file_name", "");
									finalJsonarr.put("size", "0");
									finalJsonarr.put("file_url", "");
								}
								
								jArray.put(finalJsonarr);
							}
						
						}
					}
					
					
					
					
					locObjRspdataJson.put("docs", jArray);
					locObjRspdataJson.put("timestamp",timestamp);
					locObjRspdataJson.put("totalrecords",gtotal);
					serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
					
					}else{
						locObjRspdataJson=new JSONObject();
						serverResponse(servicecode,"01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson);
					}
					
				}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);
			}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);		
			}
			
			
			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		}else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", Forgetpassword.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======signUpMobile====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", familyMemberList.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
		}
		
		return SUCCESS;
	}

	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson)
	{
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
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

	
}