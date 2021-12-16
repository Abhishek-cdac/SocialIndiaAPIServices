package com.social.login;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class loginUserCheckSociety extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	JSONObject jsonFinalObj = new JSONObject();
	List<UserMasterTblVo> userInfoList = new ArrayList<UserMasterTblVo>();
	uamDao uam = new uamDaoServices();
	SocietyMstTbl societyMst = new SocietyMstTbl();
	UserMasterTblVo userData = new UserMasterTblVo();
	String locSlqry = null;
	Query locQryrst = null;
	GroupMasterTblVo locGrpMstrVOobj = null, locGRPMstrvo = null;
	Log log = new Log();
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		String ivrservicecode = null;
		Session pSession = null;
		try {
			if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
				log.logMessage("Step 1 : Login user check society - Select society. [Start]", "info", loginUserCheckSociety.class);
				Commonutility.toWriteConsole("Step 1 : Login user check society - Select society. [Start]");
				ivrparams = EncDecrypt.decrypt(ivrparams);				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				
				
				String userNameOrMobile = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "userNameOrMobile");
				String locVrSlQry="";
				JSONArray jArray =new JSONArray();
				int societyGrpCode=0;
				pSession = HibernateUtil.getSession();;
					locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+getText("Grp.admin")+"') or groupName=upper('"+getText("Grp.admin")+"')";			 
					locQryrst = pSession.createQuery(locSlqry);
					locGrpMstrVOobj = (GroupMasterTblVo) locQryrst.uniqueResult();
					if (locGrpMstrVOobj!=null){
						locGRPMstrvo=new GroupMasterTblVo();
						societyGrpCode = locGrpMstrVOobj.getGroupCode();
									 
					} else { // new group create
						uamDao uam = new uamDaoServices();
						societyGrpCode = uam.createnewgroup_rtnId(getText("Grp.admin"));
					 		 				
					}
					int siGrpCode = 0;
					locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+getText("Grp.social.admin")+"') or groupName=upper('"+getText("Grp.social.admin")+"')";			 
					locQryrst=pSession.createQuery(locSlqry);			
					locGrpMstrVOobj=(GroupMasterTblVo) locQryrst.uniqueResult();
					if(locGrpMstrVOobj!=null){
						 locGRPMstrvo=new GroupMasterTblVo();
						 siGrpCode=locGrpMstrVOobj.getGroupCode();
										 
					}else{// new group create
						 uamDao uam=new uamDaoServices();
						 siGrpCode=uam.createnewgroup_rtnId(getText("Grp.social.admin"));
						 		 				
					}
					log.logMessage("Step 2 : Login user [Admin] Group code" + societyGrpCode + ", [Social Admin] Group code" + siGrpCode, "info", loginUserCheckSociety.class);
					Commonutility.toWriteConsole("Step 2 : Login user [Admin] Group code" + societyGrpCode + ", [Social Admin] Group code" + siGrpCode);
					userInfoList = uam.getloginUserSocietyDetails(userNameOrMobile,societyGrpCode,siGrpCode);					
					JSONObject finalJsonarr = new JSONObject();
					JSONObject finalJson = new JSONObject();
				 if( userInfoList!=null && userInfoList.size()>0){
						UserMasterTblVo userObj;
						Integer lvrSocietyid = 0; String lvrSocietyname = null;
						for (Iterator<UserMasterTblVo> it = userInfoList .iterator(); it.hasNext();) {
							userObj = it.next();
							finalJsonarr = new JSONObject();
							if(lvrSocietyid!=userObj.getSocietyId().getSocietyId()){								
								finalJsonarr.put("societyid", userObj.getSocietyId().getSocietyId());
								finalJsonarr.put("societyname", userObj.getSocietyId().getSocietyName());
								jArray.put(finalJsonarr);
								lvrSocietyid  = userObj.getSocietyId().getSocietyId();
							}
						}
						finalJson.put("UserInfoDetail", jArray);
						log.logMessage("Step 3 : Login user have more than one society [End]", "info", loginUserCheckSociety.class);
						Commonutility.toWriteConsole("Step 3 :  Login user have more than one society [End]" + societyGrpCode + ", [Social Admin] Group code" + siGrpCode);
						serverResponse(ivrservicecode, "00", "0000", "success", finalJson);
				} else {
						//finalJsonarr.put("societyid", "");
						//finalJsonarr.put("societyname", "");
						log.logMessage("Step 3 : Login user have more than one society [End]", "info", loginUserCheckSociety.class);
						Commonutility.toWriteConsole("Step 3 :  Login user have more than one society [End]" + societyGrpCode + ", [Social Admin] Group code" + siGrpCode);
						finalJson.put("UserInfoDetail", jArray);
						System.out.println("---------Completed services Block - No society found-----------------");
						serverResponse(ivrservicecode, "00", "0000", "success", finalJson);
				}
					
					
				/*if(userInfoList!=null){
					
				}
				else{
					serverResponse(ivrservicecode,"1","E0001","database error occured",locObjRspdataJson);
				}*/
					
				}else{
					//Response call
					serverResponse(ivrservicecode,"01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}	
			}else{
				//Response call
				serverResponse(ivrservicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);

			}	
		}catch(Exception e){
			System.out.println("Exception found loginUserCheckSociety.class execute() Method : "+e);
			log.logMessage("Service code : ivrservicecode, Sorry, an unhandled error occurred", "info", loginUserCheckSociety.class);
			try {
				serverResponse(ivrservicecode,"02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			} catch (Exception e1) {}
		}finally{
			if(pSession!=null){pSession.flush();pSession.clear();pSession.close();pSession =null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
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

	public SocietyMstTbl getSocietyMst() {
		return societyMst;
	}

	public void setSocietyMst(SocietyMstTbl societyMst) {
		this.societyMst = societyMst;
	}

	

}
