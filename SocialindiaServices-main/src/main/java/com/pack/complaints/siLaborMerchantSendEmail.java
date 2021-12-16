package com.pack.complaints;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.societyMgmt.societyMgmtDao;
import com.siservices.societyMgmt.societyMgmtDaoServices;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class siLaborMerchantSendEmail extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	UserMasterTblVo userData = new UserMasterTblVo();
	ComplaintsTblVO cmpltData = new ComplaintsTblVO();
	societyMgmtDao society=new societyMgmtDaoServices();
	uamDao uam=new uamDaoServices();
	Log log=new Log();
	JSONObject jsonFinalObj = new JSONObject();
	private List<UserMasterTblVo> userList=new ArrayList<UserMasterTblVo>();
	private List<TownshipMstTbl> Townshiplist = new ArrayList<TownshipMstTbl>();

	public String execute() throws Exception {
		JSONObject json = new JSONObject();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = new JSONObject();// Response Data Json		
		String servicecode=null,lvrSlgrpcodeqry=null,lvrslqlabnameqry=null;
		Query lvrQrygrpbj=null,lvrQrylabname=null;
		CommonUtilsDao common=new CommonUtilsDao();	
		boolean result;		
		ComplaintsTblVO locComplainttblobj = null;
		GroupMasterTblVo locGrptblobj = null;
		LaborProfileTblVO locLabnameObj=null;
		JSONObject locRtndatajson = null;
		uamDao lvrUamdaosrobj = null;
		try {
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				
				ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			locObjRecvJson = new JSONObject(ivrparams);
			
			if (ivIsJson) {
				lvrUamdaosrobj = new uamDaoServices();
				System.out.println("===============json========" + json);
				locObjRecvdataJson=(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
				servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");	
				Integer uniqCmpltId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "uniqcmpltid");
				String complaintfrmemailid =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "complaintfrmemailid");
				String complaintfrmgroupcode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "complaintfrmgroupcode");
				String complaintfrmusrid =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "cmpltfromuserid");
				
				/*lvrSlgrpcodeqry = "from GroupMasterTblVo where groupCode = "+complaintfrmgroupcode;
				lvrQrygrpbj = locHbsession.createQuery(lvrSlgrpcodeqry);
				locGrptblobj = (GroupMasterTblVo)lvrQrygrpbj.uniqueResult();
				System.out.println("Step grupcodeget : Select Groupcode detail query executed.");
				System.out.println("group id   "+locGrptblobj.getGroupCode());*/
				
				//cmpltData = society.getSocietyPrintData(uniqCmpltId,groupcode);													
				if(complaintfrmemailid!=null && Commonutility.checkempty(complaintfrmemailid)){
					String lvrSocyname = "", lvrFname="", lvrMob = "";
					if (complaintfrmusrid!=null && Commonutility.checkempty(complaintfrmusrid)) {
						UserMasterTblVo userInfo = null;
						userInfo = lvrUamdaosrobj.editUser(Integer.parseInt(complaintfrmusrid));
						if (userInfo.getFirstName()!=null) {
							lvrFname = userInfo.getFirstName();
						}
						if (userInfo.getLastName()!=null) {
							lvrFname += " "+ userInfo.getLastName();
						}
						lvrMob = userInfo.getMobileNo();
						if (userInfo.getSocietyId()!=null) {
							lvrSocyname = userInfo.getSocietyId().getSocietyName();
						}
					}
					//Send Email
					
					EmailEngineServices emailservice = new EmailEngineDaoServices();
					EmailTemplateTblVo emailTemplate;
					try {
			            String emqry = "FROM EmailTemplateTblVo where tempName ='Create Complaint' and status ='1'";
			            emailTemplate = emailservice.emailTemplateData(emqry);
			            String emailMessage = emailTemplate.getTempContent();
			           
			            /*emqry = common.templateParser(emailMessage, 1, "", EncDecrypt.decrypt(userData.getEncryprPassword()));
			            String[] qrySplit = emqry.split("!_!");*/
			            /*String qry = qrySplit[0] + " FROM UserMasterTblVo as cust where cust.userId='"+complaintfrmusrid+"'";
			            System.out.println("===============json=rrr======="+qrySplit[0]);
			            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);*/
			            //emailMessage =  emailMessage.replace("[ACTIVATIONKEY]", userData.getSocietyId().getActivationKey());
			           
			            if (Commonutility.checkempty(lvrFname)) {
			            	emailMessage = emailMessage.replace("[FIRSTNAME]", lvrFname);
			            } else {
			            	emailMessage = emailMessage.replace("[FIRSTNAME]", "Resident");
			            }
			            if (Commonutility.checkempty(lvrSocyname)) {
			            	emailMessage = emailMessage.replace("[SOCIETY NAME]", lvrSocyname);
			            } else {
			            	emailMessage = emailMessage.replace("[SOCIETY NAME]", "SOCYTEA");
			            }
			            Commonutility.toWriteConsole("emailMessage Final : "+emailMessage);
			            emqry = common.templateParser(emailMessage, 1, "", "");
			            Commonutility.toWriteConsole("emqry emqry Final: "+emqry);
			            String[] qrySplit = emqry.split("!_!");
			            String qry = qrySplit[0] + " FROM ComplaintsTblVO as cmplt where cmplt.complaintsId ='"+uniqCmpltId+"'";						           
			            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);
			            Commonutility.toWriteConsole(emailMessage);
			            
			            emailTemplate.setTempContent(emailMessage);
			            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
			            
			            EmailInsertFuntion emailfun = new EmailInsertFuntion();
			            emailfun.test2(complaintfrmemailid, emailTemplate.getSubject(), emailMessage);
			          } catch (Exception ex) {
			            System.out.println("Excption found in Complaintemail  : " + ex);
			            log.logMessage("Exception in Complaintemail admin flow emailFlow----> " + ex, "error",siLaborMerchantSendEmail.class);
			            
			          }	
					
					
					serverResponse(servicecode, "0",
							"0000", "sucess", jsonFinalObj);
				}else{
					jsonFinalObj=null;
					serverResponse(servicecode, "1", "E0001", "User not fount", jsonFinalObj);
				}
				
				
			} else {
				json = new JSONObject();
				serverResponse(servicecode, "1", "E0001",
						"Request values not not correct format", json);
			}
			}
		} catch (Exception ex) {
			System.out.println("=======userEditAction====Exception====" + ex);
			json = new JSONObject();
			serverResponse("SI0008", "2", "E0002",
					"Sorry, an unhandled error occurred", json);
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

	public UserMasterTblVo getUserData() {
		return userData;
	}

	public void setUserData(UserMasterTblVo userData) {
		this.userData = userData;
	}

}
