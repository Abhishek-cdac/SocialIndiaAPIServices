package com.siservices.material;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.committeeMgmt.persistense.CommittteeRoleMstTbl;
import com.siservices.committeeMgmt.persistense.committeeDao;
import com.siservices.committeeMgmt.persistense.committeeServices;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.forumVo.MvpFourmTopicsTbl;
import com.siservices.login.EncDecrypt;
import com.siservices.materialVo.MaterialCategoryTbl;
import com.siservices.materialVo.MvpMaterialTbl;
import com.siservices.signup.signUpProcess;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.signup.persistense.signUpDao;
import com.siservices.signup.persistense.signUpDaoServices;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.RightsMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class simaterialCreationAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<UserMasterTblVo> userList = new ArrayList<UserMasterTblVo>();
	private GroupMasterTblVo groupMaster = new GroupMasterTblVo();
	private SocietyMstTbl societyMaster = new SocietyMstTbl();
	MaterialCategoryTbl materialCategory = new MaterialCategoryTbl();
	uamDao uam = new uamDaoServices();
	materialDao material = new materialDaoServices();
	signUpDao signup = new signUpDaoServices();
	UserMasterTblVo userInfo = new UserMasterTblVo();
	MvpMaterialTbl materialMst = new MvpMaterialTbl();
	CommonUtils comutil = new CommonUtilsServices();
	JSONObject jsonFinalObj = new JSONObject();
	CommonUtilsDao common = new CommonUtilsDao();
	
	boolean flag = true;

	public String execute() throws JSONException{
		JSONObject json = new JSONObject();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json	
		Log log = null;
		try{
			log = new Log();			
			log.logMessage("Step 1 : Material Add called [Start]", "info", getClass());
			Commonutility.toWriteConsole("Step 1 : Material Add called [Start]");
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				String servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				int societyid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "societyid");
				int categorytype = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "categorytype");
				String materialname = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "materialname");
				int totalqnty = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "totalqnty");
				int usedqnty = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "usedqnty");
				int materialqnty = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "materialqnty");
				String materialprice = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "meterialprice");				
				String purchaseDate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "purchaseDate");
				String materialdesc = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "materialdesc");
				societyMaster.setSocietyId(societyid);
				materialCategory.setMaterialCategoryId(categorytype);
				materialMst.setMaterialName(materialname);
				materialMst.setTotalQnty(totalqnty);
				materialMst.setUsedQnty(usedqnty);
				materialMst.setMaterialQnty(materialqnty);
				materialMst.setMaterialDesc(materialdesc);
				materialMst.setMaterialPrice(Float.parseFloat(materialprice));				
				materialMst.setSocietyId(societyMaster);
				materialMst.setMaterialCategoryId(materialCategory);		
				Date date1;
				Date datematerial;
				date1 = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat df = new SimpleDateFormat(getText("calander.format.date"));//dd-MM-yyyy
				SimpleDateFormat dbfrmt = new SimpleDateFormat("yyyy-MM-dd");//dd-MM-yyyy		
				datematerial = df.parse(purchaseDate);
				datematerial= dbfrmt.parse(dbfrmt.format(datematerial));				
				materialMst.setPurchaseDate(datematerial);
				materialMst.setStatus(1);
				materialMst.setEntryBy(1);
				materialMst.setEntryDatetime(date1);
				String result = material.materialCreationForm(materialMst);
				if (result.equalsIgnoreCase("success")) {
					log.logMessage("Step 2 : Material Add Success fully [End]", "info", getClass());
					Commonutility.toWriteConsole("Step 2 : Material Add Success fully [End]");
					serverResponse(servicecode,"00", "R0189", mobiCommon.getMsg("R0189"), jsonFinalObj);
				} else {
					serverResponse(servicecode,"01", "R0190", mobiCommon.getMsg("R0190"), jsonFinalObj);
				}
			} else {
				json=new JSONObject();
				serverResponse("SI0007","01","R0067", mobiCommon.getMsg("R0067"), json);
			}
		} catch(Exception ex){	try{
			Commonutility.toWriteConsole("Step -1 : Material Add Exception : "+ex);
			log.logMessage("Step -1 : Material Add Exception : "+ex, "info", simaterialCreationAction.class);
			serverResponse("SI0007","02","R0003",mobiCommon.getMsg("R0003"),json);
		} catch(Exception ee){}finally{}
		} finally {
			log = null;
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
