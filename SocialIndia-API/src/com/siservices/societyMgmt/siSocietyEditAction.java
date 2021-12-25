package com.siservices.societyMgmt;

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

public class siSocietyEditAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	societyMgmtDao society=new societyMgmtDaoServices();
	List<SocietyWingsDetailTbl> societyWingList=new ArrayList<SocietyWingsDetailTbl>();
	JSONObject jsonFinalObj=new JSONObject();
	
	MvpSocietyAccTbl societyAcc = new MvpSocietyAccTbl();
	SocietyMstTbl societyMst = new SocietyMstTbl();
	String locSlqry = null;
	Query locQryrst = null;
	GroupMasterTblVo locGrpMstrVOobj = null, locGRPMstrvo = null;
	Log log = new Log();
	public String execute(){
		UserMasterTblVo userInfo = null;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		String ivrservicecode = null;
		Session pSession = null;
		try{
			 pSession = HibernateUtil.getSession();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");								
				int uniqSocietyId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "uniqSocietyId");
				String locVrSlQry="";
				JSONArray jArray =new JSONArray();
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
					societyMst = society.getSocietyDetailView(uniqSocietyId);
					societyWingList = society.getSocietyWingsDetailView(uniqSocietyId);
					societyAcc = society.getSocietyAccData(uniqSocietyId);
					userInfo = society.getSocietyUserRegisterData(uniqSocietyId,locGrpcode);
					Commonutility.toWriteConsole("userInfo : "+userInfo);
				if(societyMst!=null){
					jsonFinalObj.put("townshipname", societyMst.getTownshipId().getTownshipName());
					jsonFinalObj.put("townshipId", societyMst.getTownshipId().getTownshipId());
					jsonFinalObj.put("activationkey", societyMst.getActivationKey());
					jsonFinalObj.put("societyname", societyMst.getSocietyName());
					jsonFinalObj.put("noofmember", societyMst.getNoOfMemebers());
					jsonFinalObj.put("address", societyMst.getTownshipId().getAddress());
					jsonFinalObj.put("country", societyMst.getTownshipId().getCountryId().getCountryName());
					jsonFinalObj.put("city", societyMst.getTownshipId().getCityId().getCityName());
					jsonFinalObj.put("state", societyMst.getTownshipId().getStateId().getStateName());
//					jsonFinalObj.put("postalcode", societyMst.getTownshipId().getPstlId().getPstlCode());
					jsonFinalObj.put("postalcode", societyMst.getTownshipId().getPstlId());
					jsonFinalObj.put("societyid", societyMst.getSocietyId());
					jsonFinalObj.put("noofblockswings", societyMst.getNoOfBlocksWings());
					jsonFinalObj.put("registerno", societyMst.getRegisterNo());		
					
//					Commonutility.toWriteConsole("gstin >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+societyMst.getGstin());
					jsonFinalObj.put("gstin", societyMst.getGstin() == null ? "" : societyMst.getGstin());
					jsonFinalObj.put("hsn", societyMst.getHsn() == null ? "" :  societyMst.getHsn());
					
					if(societyMst.getColourCode()==null){
						jsonFinalObj.put("societycolourcode","");
					}else{
						jsonFinalObj.put("societycolourcode",societyMst.getColourCode());
					}
					if(societyMst.getLogoImage()==null){
						jsonFinalObj.put("societylogoimage","");
					}else{
						jsonFinalObj.put("societylogoimage",societyMst.getLogoImage());
					}
					if(societyMst.getIcoImage()==null){
						jsonFinalObj.put("societyicoimage","");
					}else{
						jsonFinalObj.put("societyicoimage",societyMst.getIcoImage());
					}
					if(societyMst.getImprintName()==null){
					jsonFinalObj.put("imprintname","");
				}else{
					jsonFinalObj.put("imprintname",societyMst.getImprintName());
				}
				if(societyAcc!=null){
					if(societyAcc.getBankAccNo()!=null){
						jsonFinalObj.put("accountno", Commonutility.toCheckNullEmpty(societyAcc.getBankAccNo()));
					}else{
						jsonFinalObj.put("accountno", "");
					}if(societyAcc.getBankName()!=null){
					jsonFinalObj.put("bankname", Commonutility.toCheckNullEmpty(societyAcc.getBankName()));
					}else{
						jsonFinalObj.put("bankname", "");
					}if(societyAcc.getBankAccName()!=null){
						jsonFinalObj.put("accountname", societyAcc.getBankAccName());
					}else{
						jsonFinalObj.put("accountname","");
					}
							if(societyAcc.getIfscCode()!=null){
								jsonFinalObj.put("ifsccode", societyAcc.getIfscCode());
							} else {
								jsonFinalObj.put("ifsccode", "");
							}
						} else {
					jsonFinalObj.put("accountno", "");
					jsonFinalObj.put("bankname", "");
					jsonFinalObj.put("accountname","");
					jsonFinalObj.put("ifsccode", "");
					
				}
				if(userInfo!=null) {
					if(userInfo.getEmailId()!=null){
						jsonFinalObj.put("emailid", userInfo.getEmailId());
					}else{
						jsonFinalObj.put("emailid", "");
					}if(userInfo.getIsdCode()!=null){
						jsonFinalObj.put("isdcode", userInfo.getIsdCode());	
					}else{
						jsonFinalObj.put("isdcode", "");	
					}if(userInfo.getMobileNo()!=null){
						jsonFinalObj.put("mobileno", userInfo.getMobileNo());	
					}else{
						jsonFinalObj.put("mobileno", "");	
					}
				}else{
					jsonFinalObj.put("emailid", "");
					jsonFinalObj.put("isdcode", "");
					jsonFinalObj.put("mobileno", "");	
				}
				JSONObject finalJson=new JSONObject();
				if( societyWingList!=null && societyWingList.size()>0){
				SocietyWingsDetailTbl societyWingObj;
				for (Iterator<SocietyWingsDetailTbl> it = societyWingList.iterator(); it.hasNext();) {
					societyWingObj = it.next();
					 finalJson = new JSONObject();
					finalJson.put("societywingsname", societyWingObj.getWingsName());
					jArray.put(finalJson);
				}
				jsonFinalObj.put("societyWingsName", jArray);
				}else{
					finalJson.put("societywingsname", "");
					jArray.put(finalJson);
					jsonFinalObj.put("societyWingsName", jArray);
				}
				serverResponse(ivrservicecode, "00", "R0177", mobiCommon.getMsg("R0177"), jsonFinalObj);
				}else{
					serverResponse(ivrservicecode,"01","R0178",mobiCommon.getMsg("R0178"),locObjRspdataJson);
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
			System.out.println("Exception found .class execute() Method : "+e);
			log.logMessage("Service code : ivrservicecode, Sorry, an unhandled error occurred", "info", siSocietyEditAction.class);
			try {
				serverResponse(ivrservicecode,"02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			} catch (Exception e1) {}
		}finally{
			if( pSession!=null){pSession.flush();pSession.clear();pSession.close();pSession=null;}			
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

	public List<SocietyWingsDetailTbl> getSocietyWingList() {
		return societyWingList;
	}

	public void setSocietyWingList(List<SocietyWingsDetailTbl> societyWingList) {
		this.societyWingList = societyWingList;
	}

	public MvpSocietyAccTbl getSocietyAcc() {
		return societyAcc;
	}

	public void setSocietyAcc(MvpSocietyAccTbl societyAcc) {
		this.societyAcc = societyAcc;
	}
	

}
