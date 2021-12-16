package com.siservices.material;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.login.EncDecrypt;
import com.siservices.materialVo.MaterialCategoryTbl;
import com.siservices.materialVo.MvpMaterialTbl;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class simaterialUpdateAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	materialDao material = new materialDaoServices();
	MvpMaterialTbl materialMst = new MvpMaterialTbl();
	CommonUtils comutil = new CommonUtilsServices();
	JSONObject jsonFinalObj = new JSONObject();
	CommonUtilsDao common = new CommonUtilsDao();
	
	MaterialCategoryTbl materialCategory = new MaterialCategoryTbl();
	JSONObject finalJson = new JSONObject();
	boolean flag = true;
	
	public String execute() throws JSONException {		
		JSONObject json = new JSONObject();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json				
		Log log = null;
		try {
			log = new Log();			
			if(Commonutility.checkempty(ivrparams)) {
				Commonutility.toWriteConsole("Step 1 : Material Update called [Start]");
				log.logMessage("Step 1 : Material Update called [Start]", "info", simaterialUpdateAction.class);
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {		
					locObjRecvJson = new JSONObject(ivrparams);
					String servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					int materialid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "materialid");
					int categoryid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "categoryid");
					String materialname = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "materialname");
					
					int totalqnty = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "totalqnty");
					int usedqnty = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "usedqnty");
					int materialqnty = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "materialqnty");
					String materialprice = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "materialprice");
					
					String purchasedate = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "purchasedate");
					String materialdesc = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "materialdesc");
					
					materialMst.setMaterialId(materialid);
					materialMst.setMaterialName(materialname);
					materialCategory.setMaterialCategoryId(categoryid);
					materialMst.setTotalQnty(totalqnty);
					materialMst.setUsedQnty(usedqnty);
					materialMst.setMaterialQnty(materialqnty);
					materialMst.setMaterialPrice(Float.parseFloat(materialprice));
					materialMst.setMaterialDesc(materialdesc);
					materialMst.setMaterialCategoryId(materialCategory);
					SimpleDateFormat df = new SimpleDateFormat(getText("calander.format.date"));//dd-MM-yyyy
					SimpleDateFormat dbfrmt = new SimpleDateFormat("yyyy-MM-dd");
					Date datematerial;
					datematerial = df.parse(purchasedate);
					datematerial= dbfrmt.parse(dbfrmt.format(datematerial));				
			    	materialMst.setPurchaseDate(datematerial);
		    	
					boolean result=material.getmaterialUpdate(materialMst);
					if(result){		
						Commonutility.toWriteConsole("Step 2 : Material Update Success [End]");
						log.logMessage("Step 2 : Material Update Success [End]", "info", simaterialUpdateAction.class);
						serverResponse(servicecode, "00", "R0191", mobiCommon.getMsg("R0191"), finalJson);
					} else {
						Commonutility.toWriteConsole("Step 2 : Material Update Error [End]");
						log.logMessage("Step 2 : Material Update Error [End]", "info", simaterialUpdateAction.class);
						serverResponse(servicecode, "01", "R0192", mobiCommon.getMsg("R0192"), finalJson);
					}
				} else {
					Commonutility.toWriteConsole("Step 2 : Material Update Error : "+ mobiCommon.getMsg("R0067") +" [End]");
					log.logMessage("Step 2 : Material Update Error : "+ mobiCommon.getMsg("R0067") +" [End]", "info", simaterialUpdateAction.class);
					json=new JSONObject();
					serverResponse("SI0007", "01", "R0067", mobiCommon.getMsg("R0067"), json);
				}
			} else {
				Commonutility.toWriteConsole("Step 1 : Material Update Error [Start] : "+ mobiCommon.getMsg("R0002") +"");
				log.logMessage("Step 1 : Material Update Error [Start] : "+ mobiCommon.getMsg("R0002") +" ", "info", simaterialUpdateAction.class);
				json=new JSONObject();				
				serverResponse("SI0007", "01", "R0002", mobiCommon.getMsg("R0002"), json);
			}
			
		} catch(Exception ex) {
				try {
					Commonutility.toWriteConsole("Step 2 : Material Update Exception . Sorry, simaterialUpdateAction an unhandled error occurred [End] : "+ ex +" ");
					log.logMessage("Sorry, simaterialUpdateAction an unhandled error occurred : "+ex, "info", simaterialUpdateAction.class);
					serverResponse("SI0007","02","R0003",mobiCommon.getMsg("R0003"),json);
				} catch (Exception ee) {} finally {}
		} finally {
			json = null;
			locObjRecvJson = null;//Receive String to json	
			locObjRecvdataJson = null;// Receive Data Json
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
