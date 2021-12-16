package com.siservices.material;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.forumVo.MvpFourmDiscussTbl;
import com.siservices.login.EncDecrypt;
import com.siservices.materialVo.MvpMaterialTbl;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class simaterialViewAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<MvpFourmDiscussTbl> forumDiscussList = new ArrayList<MvpFourmDiscussTbl>();
	materialDao material=new materialDaoServices();
	CommonUtils comutil=new CommonUtilsServices();
	MvpMaterialTbl materialMst=new MvpMaterialTbl();
	JSONObject jsonFinalObj=new JSONObject();
	CommonUtilsDao common=new CommonUtilsDao();	
	Log log=new Log();	
	JSONObject finalJson = new JSONObject();
	boolean flag = true;
	
	public String execute() throws JSONException{
		System.out.println("************ForumviewAction******************");
		JSONObject json = new JSONObject();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		try{
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
		String servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
		locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
		int materialId = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "materialId");
		System.out.println("============materialId========="+materialId);
		materialMst=material.getMaterialView(materialId);
		JSONObject finalJsonarr=new JSONObject();
		JSONArray jArray =new JSONArray();
		
			if(materialMst!=null){
				
			finalJson.put("categoryname", materialMst.getMaterialCategoryId().getMaterialName());
			finalJson.put("categoryid", materialMst.getMaterialCategoryId().getMaterialCategoryId());
			finalJson.put("materialid", materialMst.getMaterialId());
			finalJson.put("societyname", materialMst.getSocietyId().getSocietyName());
			finalJson.put("townshipname", materialMst.getSocietyId().getTownshipId().getTownshipName());
			finalJson.put("materialname", materialMst.getMaterialName());
			finalJson.put("materialqnty", materialMst.getMaterialQnty());
			finalJson.put("totalqnty", materialMst.getTotalQnty());
			finalJson.put("usedqnty", materialMst.getUsedQnty());
			finalJson.put("materialprice", materialMst.getMaterialPrice().toString());
			finalJson.put("purchasedate", materialMst.getPurchaseDate());
			if(materialMst.getMaterialDesc()!=null){
				finalJson.put("materialdesc", materialMst.getMaterialDesc());
			}else{
				finalJson.put("materialdesc", "");
			}
			
			serverResponse(servicecode,"0",
					"0000", "success", finalJson);
			}else{
				finalJson=new JSONObject();
				serverResponse(servicecode,"1",
						"E001", "data not found", finalJson);
			}
			}else
			{
				json=new JSONObject();
				serverResponse("SI0007","1","E0001","Request values not not correct format",json);
			}
		}catch(Exception ex){
			System.out.println("=======siCommitteeCreationAction====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, siCommitteeCreationAction an unhandled error occurred", "info", simaterialViewAction.class);
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

	public MvpMaterialTbl getMaterialMst() {
		return materialMst;
	}

	public void setMaterialMst(MvpMaterialTbl materialMst) {
		this.materialMst = materialMst;
	}
	
}
