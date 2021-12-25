package com.socialindiaservices.merchantmgmt;

import java.io.PrintWriter;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonapi.DocMasterlst;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.MerchantManageDaoServices;
import com.socialindiaservices.services.MerchantManageServices;
import com.socialindiaservices.vo.MerchantUtilitiTblVO;

public class Getmerchantlutiviewdetails    extends ActionSupport{

	
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;
	private MerchantManageServices merchHbm=new MerchantManageDaoServices();
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");

	public String execute(){
		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		Session locObjsession = null;		
		String ivrservicecode=null;
		List<Object> locObjDoclst = null;
		JSONObject jsonFinalObj=new JSONObject();
		Integer ivrEntryByusrid = 0;
		CommonUtils common=new CommonUtils();
		try{
			locObjsession = HibernateUtil.getSession();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				System.out.println("ivrparams-----------------"+ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				System.out.println("ivIsJson--------------"+ivIsJson);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"userId");
				Integer groupcode=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"usrTyp");
				Integer merchantUtilId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"merchantUtilId");
				MerchantUtilitiTblVO merchObj=new MerchantUtilitiTblVO();
				lsvSlQry="from MerchantUtilitiTblVO where statusFlag='1' and merchantUtilId='"+merchantUtilId+"'";
				System.out.println("Select Query : "+lsvSlQry);
				merchObj=merchHbm.getMerchantUtilitiTblObjByQuery(lsvSlQry, locObjsession);
				jsonFinalObj.put("broucherName",merchObj.getBroucherName());
				jsonFinalObj.put("offerName",merchObj.getOfferName());
				if(merchObj.getOfferPrice()>0){
					jsonFinalObj.put("offerPrice",Float.toString(merchObj.getOfferPrice()));
				}else{
					jsonFinalObj.put("offerPrice","");
				}
				if(merchObj.getActualPrice()>0){
					jsonFinalObj.put("actualPrice",Float.toString(merchObj.getActualPrice()));
				}else{
					jsonFinalObj.put("actualPrice","");
				}
				jsonFinalObj.put("description",merchObj.getDescription());
				if(merchObj.getOfferValidFrom()!=null){
					jsonFinalObj.put("offerValidFrom",common.dateToString(merchObj.getOfferValidFrom(),rb.getString("calendar.dateformat")));
				}else{
					jsonFinalObj.put("offerValidFrom","");
				}
				if(merchObj.getOfferValidTo()!=null){
					jsonFinalObj.put("offerValidTo",common.dateToString(merchObj.getOfferValidTo(),rb.getString("calendar.dateformat")));
				}else{
					jsonFinalObj.put("offerValidTo","");
				}
				jsonFinalObj.put("mrchntId",merchObj.getMrchntId().getMrchntId());
				
				serverResponse(ivrservicecode,"0","0000","success",jsonFinalObj);
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6423, Request values are not correct format", "info", DocMasterlst.class);
					serverResponse("SI6423","1","E0001","Request values are not correct format",locObjRspdataJson);
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6423, Request values are empty", "info", DocMasterlst.class);
				serverResponse("SI6423","1","E0001","Request values are empty",locObjRspdataJson);
			}	
		}catch(Exception e){
			System.out.println("Exception found DocMasterlst.class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6423, Sorry, an unhandled error occurred", "error", DocMasterlst.class);
			serverResponse("SI6423","2","E0002","Sorry, an unhandled error occurred",locObjRspdataJson);
		}finally{
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRspdataJson=null;	//locObjRecvdataJson =null;
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

	public String getIvrservicecode() {
		return ivrservicecode;
	}

	public void setIvrservicecode(String ivrservicecode) {
		this.ivrservicecode = ivrservicecode;
	}

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}
	

}
