package com.mobi.addpost;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.timelinefeedvo.FeedsViewTblVO;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.mobi.addpost.AddPostDao;
import com.mobi.common.mobiCommon;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;

public class AddPostDelete extends ActionSupport{
	
	private static final long serialVersionUID =1l;
	
	private String ivrparams;
	private String ivrservicecode;
	Log log= new Log();
	MvpAdpostTbl mvpObj = new MvpAdpostTbl();
	public AddPostDao addPostObj =new  AddPostServices();
    String userId="";
	String postalId ="";
	otpDao otp=new otpDaoService();
	
	public String execute() {
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		//Session locObjsession = null;
		
		StringBuilder locErrorvalStrBuil =null;
		boolean flg = true;
		
		try {
			ivrservicecode = getText("report.issue");
		//	locObjsession = HibernateUtil.getSession();
			locErrorvalStrBuil = new StringBuilder();
			
			log.logMessage("Enter into addpost ", "info", AddPostDelete.class);
			if (Commonutility.checkempty(ivrparams)) {
				System.out.println("==========ivrparams========="+ivrparams);
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					String townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"townshipid");
					String societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"societykey");
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
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					if (locObjRecvdataJson !=null) {
						userId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"rid");
						postalId = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"ad_id");
						if (Commonutility.checkempty(userId)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
						}
						
						if (Commonutility.checkempty(postalId)) {
							
						} else {
							flg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("addpost.delete.error")));
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
			log.logMessage("flg :" +flg, "info", AddPostDelete.class);
			if (flg) {
				
				boolean deleteObjRet = addPostObj.updateAddpostVal(userId,postalId);
				System.out.println("deleteObjRet======>"+deleteObjRet);
				if(deleteObjRet)
				{
					FeedsTblVO feedObj = new FeedsTblVO();
					FeedsViewTblVO feedViewObj = new FeedsViewTblVO();
					FeedDAO feedService = new FeedDAOService();
					AddPostDao adPost =new  AddPostServices();
					feedObj=feedService.getFeedDetails(Commonutility.stringToInteger(postalId));
//					boolean feedDeleFlg = feedService.feedDelete(feedObj);
					boolean feedDeleFlg = feedService.feedDelete(feedObj.getFeedId()+"", userId);
					if (feedDeleFlg) {
						int retFeedViewId = 0;
						int userid = 0;
						if (feedObj.getFeedPrivacyFlg() == 2) {
							userid = -1;
						} else if (feedObj.getFeedPrivacyFlg() == 3) {
							userid = -2;
						} else {
							userid = Integer.parseInt(userId);
						}
						retFeedViewId = adPost.getadpostFeedViewId(Integer.parseInt(userId), feedObj.getSocietyId().getSocietyId(), feedObj.getFeedId(), userid,0,"1");
						if (retFeedViewId != 0) {
							feedViewObj = feedService.getFeedViewDetails(retFeedViewId);
							if (feedViewObj != null) {
								flg = feedService.feedViewDelete(feedViewObj,2);
							} 
							if (flg) {
							} else { 
								flg = false;
								locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("addpost.datadetail.error")));
							}
						}
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("addpost.datadetail.error")));
					}
					
					locObjRspdataJson=new JSONObject();
					System.out.println("ff======>"+mobiCommon.getMsg("R0001"));
					serverResponse(ivrservicecode,getText("status.success"),"R0245",mobiCommon.getMsg("R0245"),locObjRspdataJson);
				}
				else
				{
					locObjRspdataJson=new JSONObject();
					locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
					serverResponse(ivrservicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
				}
				/*locObjRspdataJson=new JSONObject();
				serverResponse(ivrservicecode,getText("status.success"),"R0001",getText("R0001"),locObjRspdataJson);*/
			} 
			
			else {
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode,getText("status.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		} catch (Exception ex) {
			locObjRspdataJson=new JSONObject();
			log.logMessage(getText("Eex") + ex, "error", AddPostDelete.class);
			serverResponse(ivrservicecode,getText("status.system.error"),"R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
		} finally {
			
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

	public String getIvrservicecode() {
		return ivrservicecode;
	}

	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}
	
	

}
