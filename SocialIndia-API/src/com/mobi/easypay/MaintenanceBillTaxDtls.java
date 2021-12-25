package com.mobi.easypay;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobi.easypayvo.persistence.MaintenanceFeeDao;
import com.mobi.easypayvo.persistence.MaintenanceFeeDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.MaintenanceBillTaxDtlsTblVO;

public class MaintenanceBillTaxDtls extends ActionSupport {

	private static final long serialVersionUID = 1l;

	private String ivrparams;
	private String ivrservicecode;
	Log log = new Log();

	public String execute() {
		JSONObject locObjRecvJson = null;// Receive String to json
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		StringBuilder locErrorvalStrBuil = null;
		boolean flg = true;
		try {
			locErrorvalStrBuil = new StringBuilder();
			log.logMessage("Enter into MaintenanceFeeList ", "info", MaintenanceBillTaxDtls.class);
			if (Commonutility.checkempty(ivrparams)) {
				ivrparams = EncDecrypt.decrypt(ivrparams);
				log.logMessage("MaintenanceFeeList ivrparams :" + ivrparams, "info", MaintenanceBillTaxDtls.class);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					locObjRecvdataJson = (JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");
					log.logMessage("MaintenanceFeeList flg :" + flg, "info", MaintenanceBillTaxDtls.class);
					
						locObjRspdataJson = new JSONObject();
						JSONArray jsonArr = new JSONArray();
						
						MaintenanceFeeDao maintenanceFeeDAOobj = new MaintenanceFeeDaoServices();
						List<MaintenanceBillTaxDtlsTblVO> taxDtlsTblVOList  = maintenanceFeeDAOobj.getMNTNCBillTaxDetails();
						MaintenanceBillTaxDtlsTblVO taxDtlsTblVO = null;
						for (Iterator<MaintenanceBillTaxDtlsTblVO> it = taxDtlsTblVOList.iterator();it.hasNext();) {
							taxDtlsTblVO = it.next();
							JSONObject finalJsonarr = new JSONObject();
							finalJsonarr.put("id", taxDtlsTblVO.getID());
							finalJsonarr.put("mod_of_payment", taxDtlsTblVO.getModOfPayment());
							finalJsonarr.put("fixed_charges", taxDtlsTblVO.getFixedCharges());
							finalJsonarr.put("percentage", taxDtlsTblVO.getPercentageVO());
							finalJsonarr.put("amnt_gt_1000", taxDtlsTblVO.getAmntGt1000());
							finalJsonarr.put("amnt_lt_1000", taxDtlsTblVO.getAmntLt1000());
							finalJsonarr.put("calc_logic", taxDtlsTblVO.getCalcLogic());
							finalJsonarr.put("conv_charge", taxDtlsTblVO.getConvCharge());
							finalJsonarr.put("gst", taxDtlsTblVO.getGstVO());
							
							jsonArr.put(finalJsonarr);
						}
							
						locObjRspdataJson.put("maintenance_bill_tax_details", jsonArr);
						serverResponse(ivrservicecode, getText("status.success"), "R0001", mobiCommon.getMsg("R0001"), locObjRspdataJson);
				
				} else {
					locObjRspdataJson = new JSONObject();
					locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
					serverResponse(ivrservicecode, getText("status.validation.error"), "R0002",	mobiCommon.getMsg("R0002"), locObjRspdataJson);
				}
			} else {
				locObjRspdataJson = new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(ivrservicecode, getText("status.validation.error"), "R0002",	mobiCommon.getMsg("R0002"), locObjRspdataJson);
			}
		} catch (Exception ex) {
			locObjRspdataJson = new JSONObject();
			log.logMessage(getText("Eex") + ex, "error", MaintenanceBillTaxDtls.class);
			serverResponse(ivrservicecode, getText("status.error"), "R0003", mobiCommon.getMsg("R0003"), locObjRspdataJson);
		} finally {
		}
		return SUCCESS;
	}

	private void serverResponse(String serviceCode, String statusCode,
			String respCode, String message, JSONObject dataJson) {
		PrintWriter out = null;
		HttpServletResponse response = null;
		response = ServletActionContext.getResponse();
		try {
			out = response.getWriter();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			mobiCommon mobicomn = new mobiCommon();
			String as = mobicomn.getServerResponse(serviceCode, statusCode,
					respCode, message, dataJson);
			out.print(as);
			out.close();
		} catch (Exception ex) {
			try {
				out = response.getWriter();
				out.print("{\"servicecode\":\"" + serviceCode + "\",");
				out.print("{\"statuscode\":\"2\",");
				out.print("{\"respcode\":\"E0002\",");
				out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
				out.print("{\"data\":\"{}\"}");
				out.close();
				ex.printStackTrace();
			} catch (Exception e) {
			} finally {
			}
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
