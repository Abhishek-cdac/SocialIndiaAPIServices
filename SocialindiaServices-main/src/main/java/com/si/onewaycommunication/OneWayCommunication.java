package com.si.onewaycommunication;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.OneWayCommunicationTblVO;

public class OneWayCommunication extends ActionSupport {

	private static final long serialVersionUID = 1l;

	private String ivrparams;
	private String ivrservicecode;
	Log log = new Log();

	
	
	public String execute() {
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil = null;
		int flag = 1;
		try {
			locErrorvalStrBuil = new StringBuilder();
			log.logMessage("Enter into OneWayCommunication ", "info", OneWayCommunication.class);
			if (Commonutility.checkempty(ivrparams)) {
				Commonutility.toWriteConsole("OneWayCommunication Receive Param : " + ivrparams);
				ivrparams = EncDecrypt.decrypt(ivrparams);
				Commonutility.toWriteConsole("OneWayCommunication Receive Param : " + ivrparams);
				log.logMessage("OneWayCommunication ivrparams :" + ivrparams, "info", OneWayCommunication.class);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					String society = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "society");
					String method = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "method");
					String right = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rights");
					
					if(method == null || method.isEmpty())
						method = "get";
					
					try{
						flag = locObjRecvdataJson.getInt("flag");
					}
					catch (Exception e) {
						flag = 2;//mobile
					}
					
					log.logMessage("OneWayCommunication flag :" + flag, "info", OneWayCommunication.class);
					
						locObjRspdataJson = new JSONObject();
						JSONArray jsonArr = new JSONArray();
						
						OneWayCommunicationDao dAOobj = new OneWayCommunicationDaoServices();
						
						if(method.equalsIgnoreCase("get")){
							List<OneWayCommunicationTblVO> tblVOList  = dAOobj.getAccessFlags(society,right);
							OneWayCommunicationTblVO tblVO = null;
							for (Iterator<OneWayCommunicationTblVO> it = tblVOList.iterator();it.hasNext();) {
								tblVO = it.next();
								JSONObject finalJsonarr = new JSONObject();
								finalJsonarr.put("buysell", tblVO.getBuysell());
								finalJsonarr.put("sharecare", tblVO.getSharecare());
								finalJsonarr.put("skillhelp", tblVO.getSkillhelp());
								finalJsonarr.put("mydocuments", tblVO.getMydocuments());
								finalJsonarr.put("facilitybooking", tblVO.getFacilitybooking());
								finalJsonarr.put("personalevents", tblVO.getPersonalevents());
								finalJsonarr.put("gatepass", tblVO.getGatepass());
								finalJsonarr.put("profileinfo", tblVO.getProfileinfo());
								finalJsonarr.put("timelinefeeds", tblVO.getTimelinefeeds());
								finalJsonarr.put("onlineusers", tblVO.getOnlineusers());
								finalJsonarr.put("shophome", tblVO.getShophome());
								finalJsonarr.put("easypay", tblVO.getEasypay());
								finalJsonarr.put("complaints", tblVO.getComplaints());
								
								finalJsonarr.put("publishasbuyerorseller", tblVO.getPublishasbuyerorseller());
								finalJsonarr.put("whatsonstore", tblVO.getWhatsonstore());
								finalJsonarr.put("carpooling", tblVO.getCarpooling());
								finalJsonarr.put("donate", tblVO.getDonate());
								finalJsonarr.put("publishskils", tblVO.getPublishskils());
								finalJsonarr.put("skilldirctory", tblVO.getSkilldirctory());
								finalJsonarr.put("myservicebooking", tblVO.getMyservicebooking());
								finalJsonarr.put("knowledgebase", tblVO.getKnowledgebase());
								finalJsonarr.put("personalbriefcase", tblVO.getPersonalbriefcase());
								finalJsonarr.put("societyinfo", tblVO.getSocietyinfo());
								finalJsonarr.put("searchdocument", tblVO.getSearchdocument());
								finalJsonarr.put("newbooking", tblVO.getNewbooking());
								finalJsonarr.put("createinvitation", tblVO.getCreateinvitation());
								finalJsonarr.put("issuegatepass", tblVO.getIssuegatepass());
								finalJsonarr.put("feeds", tblVO.getFeeds());
								finalJsonarr.put("onlineuser", tblVO.getOnlineuser());
								finalJsonarr.put("browsebycategory", tblVO.getBrowsebycategory());
								finalJsonarr.put("searchwithkeywords", tblVO.getSearchwithkeywords());
								finalJsonarr.put("rateyourexperience", tblVO.getRateyourexperience());
								finalJsonarr.put("reportproblem", tblVO.getReportproblem());
								finalJsonarr.put("mypassport", tblVO.getMypassport());
								finalJsonarr.put("utilitypayments", tblVO.getUtilitypayments());
								finalJsonarr.put("postcomplaint", tblVO.getPostcomplaint());
								
								jsonArr.put(finalJsonarr);
							}
								
							locObjRspdataJson.put("onewaycommunication", jsonArr);
							serverResponse(ivrservicecode, getText("status.success"), "0000", "OneWayCommunication success", locObjRspdataJson, flag);
						}
						else{
							//set flags
							OneWayCommunicationTblVO tblVO = new OneWayCommunicationTblVO();
							tblVO.setSid(society);
							tblVO.setBuysell(locObjRecvdataJson.getInt("buysell"));
							tblVO.setSharecare(locObjRecvdataJson.getInt("sharecare"));
							tblVO.setSkillhelp(locObjRecvdataJson.getInt("skillhelp"));
							tblVO.setMydocuments(locObjRecvdataJson.getInt("mydocuments"));
							tblVO.setFacilitybooking(locObjRecvdataJson.getInt("facilitybooking"));
							tblVO.setPersonalevents(locObjRecvdataJson.getInt("personalevents"));
							tblVO.setGatepass(locObjRecvdataJson.getInt("gatepass"));
							tblVO.setProfileinfo(locObjRecvdataJson.getInt("profileinfo"));
							tblVO.setTimelinefeeds(locObjRecvdataJson.getInt("timelinefeeds"));
							tblVO.setOnlineusers(locObjRecvdataJson.getInt("onlineusers"));
							tblVO.setShophome(locObjRecvdataJson.getInt("shophome"));
							tblVO.setEasypay(locObjRecvdataJson.getInt("easypay"));			
							tblVO.setComplaints(locObjRecvdataJson.getInt("complaints"));
							tblVO.setPublishasbuyerorseller(locObjRecvdataJson.getInt("publishasbuyerorseller"));
							tblVO.setWhatsonstore(locObjRecvdataJson.getInt("whatsonstore"));
							tblVO.setCarpooling(locObjRecvdataJson.getInt("carpooling"));
							tblVO.setDonate(locObjRecvdataJson.getInt("donate"));
							tblVO.setPublishskils(locObjRecvdataJson.getInt("publishskils"));
							tblVO.setSkilldirctory(locObjRecvdataJson.getInt("skilldirctory"));
							tblVO.setMyservicebooking(locObjRecvdataJson.getInt("myservicebooking"));
							tblVO.setKnowledgebase(locObjRecvdataJson.getInt("knowledgebase"));
							tblVO.setPersonalbriefcase(locObjRecvdataJson.getInt("personalbriefcase"));
							tblVO.setSocietyinfo(locObjRecvdataJson.getInt("societyinfo"));
							tblVO.setSearchdocument(locObjRecvdataJson.getInt("searchdocument"));
							tblVO.setNewbooking(locObjRecvdataJson.getInt("newbooking"));
							tblVO.setCreateinvitation(locObjRecvdataJson.getInt("createinvitation"));
							tblVO.setIssuegatepass(locObjRecvdataJson.getInt("issuegatepass"));
							tblVO.setFeeds(locObjRecvdataJson.getInt("feeds"));
							tblVO.setOnlineuser(locObjRecvdataJson.getInt("onlineuser"));
							tblVO.setBrowsebycategory(locObjRecvdataJson.getInt("browsebycategory"));
							tblVO.setSearchwithkeywords(locObjRecvdataJson.getInt("searchwithkeywords"));
							tblVO.setRateyourexperience(locObjRecvdataJson.getInt("rateyourexperience"));
							tblVO.setReportproblem(locObjRecvdataJson.getInt("reportproblem"));
							tblVO.setMypassport(locObjRecvdataJson.getInt("mypassport"));
							tblVO.setUtilitypayments(locObjRecvdataJson.getInt("utilitypayments"));
							tblVO.setPostcomplaint(locObjRecvdataJson.getInt("postcomplaint"));
							tblVO.setRights(right);
							int id = dAOobj.setAccessFlags(tblVO);
							
							if(id != -1){
								serverResponse(ivrservicecode, getText("status.success"), "0000", "OneWayCommunication success", locObjRspdataJson, flag);
							}
							else{
								locObjRspdataJson = new JSONObject();
								locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
								serverResponse(ivrservicecode, getText("status.validation.error"), "ERROR",	"OneWayCommunication set access rights failed", locObjRspdataJson, flag);
							}
						}
				
				} else {
					locObjRspdataJson = new JSONObject();
					locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
					serverResponse(ivrservicecode, getText("status.validation.error"), "ERROR",	"OneWayCommunication error occured", locObjRspdataJson, flag);
				}
			} else {
				locObjRspdataJson = new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode, getText("status.validation.error"), "ERROR",	"OneWayCommunication error occured", locObjRspdataJson, flag);
			}
		} catch (Exception ex) {
			locObjRspdataJson = new JSONObject();
			log.logMessage(getText("Eex") + ex, "error", OneWayCommunication.class);
			try {
				serverResponse(ivrservicecode, getText("status.error"), "ERROR", "OneWayCommunication error occured"+ex.getMessage(), locObjRspdataJson, flag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
		}
		return SUCCESS;
	}

	private void serverResponse(String serviceCode, String statusCode,String respCode, String message, JSONObject dataJson, int flag)
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
				
				if(flag == 1){
					responseMsg.put("servicecode", serviceCode);
					responseMsg.put("statuscode", statusCode);
					responseMsg.put("respcode", respCode);
					responseMsg.put("message", message);
					responseMsg.put("data", dataJson);
					String as = responseMsg.toString();
					out.print(as);
					out.close();
				}
				else{
					mobiCommon mobicomn=new mobiCommon();
					
					if(statusCode !=null && statusCode.equalsIgnoreCase("0000"))
						statusCode = "00";
					
					String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
					out.print(as);
					out.close();
				}
				
			} catch (Exception ex) {
				out = response.getWriter();
				log.logMessage("Service : OneWayCommunication error occurred : " + ex,"error", OneWayCommunication.class);
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

	public String getIvrservicecode() {
		return ivrservicecode;
	}

	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}

}
