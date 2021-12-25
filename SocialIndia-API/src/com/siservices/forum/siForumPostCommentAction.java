package com.siservices.forum;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

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
import com.siservices.forumVo.MvpFourmDiscussTbl;
import com.siservices.forumVo.MvpFourmTopicsTbl;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.persistence.DocumentUtilitiHibernateDao;

public class siForumPostCommentAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	uamDao uam = new uamDaoServices();
	MvpFourmTopicsTbl forumTopicMst = new MvpFourmTopicsTbl();
	UserMasterTblVo userInfo = new UserMasterTblVo();
	forumDao forum = new forumServices();
	CommonUtils comutil = new CommonUtilsServices();
	MvpFourmDiscussTbl forumDiscussMst = new MvpFourmDiscussTbl();
	JSONObject jsonFinalObj = new JSONObject();
	CommonUtilsDao common = new CommonUtilsDao();
	Log log = new Log();
	boolean flag = true;
	Session locSession = null;
	DocumentUtilitiHibernateDao docHibernateUtilService = null;
	UserMasterTblVo locUsrMstrVOobj = null;
	Iterator locObjSocietyRestlst = null;
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	public String execute() throws JSONException{		
		JSONObject json = new JSONObject();
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json				
		try{
			docHibernateUtilService=new DocumentUtilitiHibernateDao();
			locSession=HibernateUtil.getSession();
			if (ivrparams!= null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				String servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String postcomment = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "postcomment");
				int useruniqueId = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "useruniqueId");
				int topicsId = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "topicsId");
				Date date1;
				date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
				userInfo.setUserId(useruniqueId);
				forumDiscussMst.setDiscussionDesc(postcomment);
				forumDiscussMst.setUserId(userInfo);
				forumTopicMst.setTopicsId(topicsId);
				forumDiscussMst.setTopicsId(forumTopicMst);
				forumDiscussMst.setEntryBy(useruniqueId);
				forumDiscussMst.setStatus(1);
				forumDiscussMst.setEntryDatetime(date1);	
				boolean result = forum.forumCreationComments(forumDiscussMst);
				int tblflgg=0;
				if (Commonutility.checkempty(rb.getString("notification.reflg.fourm"))) {
					tblflgg=Commonutility.stringToInteger(rb.getString("notification.reflg.fourm"));
				} else {
					tblflgg=4;
				}	
				
				String locSlqry = "from UserMasterTblVo where userId = "+useruniqueId;
				Query locQryrst = locSession.createQuery(locSlqry);
				locUsrMstrVOobj = (UserMasterTblVo)locQryrst.uniqueResult();
				
				String comenttosocietyid = Commonutility.toCheckNullEmpty(locUsrMstrVOobj.getSocietyId().getSocietyId());
				
				String grpcode = Commonutility.toCheckNullEmpty(locUsrMstrVOobj.getGroupCode().getGroupCode());
				String locSlqry1 = "from UserMasterTblVo where societyId = "+Integer.parseInt(comenttosocietyid)+" and statusFlag =1  and groupCode= "+grpcode+"";
				locObjSocietyRestlst=locSession.createQuery(locSlqry1).list().iterator();
				while (locObjSocietyRestlst.hasNext()) {						
					locUsrMstrVOobj=(UserMasterTblVo)locObjSocietyRestlst.next();
					String usridforsoctid=Commonutility.toCheckNullEmpty(locUsrMstrVOobj.getUserId());
					docHibernateUtilService.insertNotificationTblByValue(Integer.parseInt(usridforsoctid), rb.getString("notification.fourm.raise"),useruniqueId,tblflgg, topicsId);//cmplttoid(committee),desc,cmpltfrmid,tblflag(0-cmplttable),tblrowid)
				}		 
					if (result == true) {
						serverResponse(servicecode,"00", "R0035", mobiCommon.getMsg("R0035"), jsonFinalObj);
					} else {
						serverResponse(servicecode,"01", "R0165", mobiCommon.getMsg("R0165"), jsonFinalObj);
					}
				} else {
					json=new JSONObject();
					serverResponse("SI0007","01","R0067",mobiCommon.getMsg("R0067"),json);
				}
			} else {
				json = new JSONObject();
				serverResponse("SI0007", "01", "R0002",mobiCommon.getMsg("R0002"), json);
			}
		} catch(Exception ex){
			try {
				Commonutility.toWriteConsole("Step -1 : Comment post in topic - Exception found in siForumPostCommentAction.class : "+ex);
				log.logMessage("Service code : ivrservicecode, Sorry, siForumPostCommentAction an unhandled error occurred", "info", siForumPostCommentAction.class);
				json = new JSONObject();
				serverResponse("SI0007", "01", "R0003",mobiCommon.getMsg("R0003"), json);
			} catch(Exception e){
				
			} finally {
				
			}
		} finally {
			if(locSession!=null){locSession.flush();locSession.clear();locSession.close();locSession=null;}
		}
		return SUCCESS;
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
	
}
