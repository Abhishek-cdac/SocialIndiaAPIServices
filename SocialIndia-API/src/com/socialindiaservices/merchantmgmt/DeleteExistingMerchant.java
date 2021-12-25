package com.socialindiaservices.merchantmgmt;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonapi.DocMasterlst;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.services.MerchantManageDaoServices;
import com.socialindiaservices.services.MerchantManageServices;
import com.socialindiaservices.vo.MerchantTblVO;

public class DeleteExistingMerchant extends ActionSupport{

	
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;
	private MerchantManageServices merchHbm=new MerchantManageDaoServices();

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
		Transaction tx = null;
		try{
			locObjsession = HibernateUtil.getSession();
			tx = locObjsession.beginTransaction();
			if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					Date date =null;
					CommonUtils comutil=new CommonUtilsServices();
					date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"userId");
					Integer groupcode=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"usrTyp");
					Integer mrchntId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mrchntId");
					MerchantTblVO merchantobj=new MerchantTblVO();
					merchantobj.setModifyDatetime(date);
					lsvSlQry="update MerchantTblVO set mrchntActSts=0 where mrchntId='"+mrchntId+"'";				
					boolean isupdate=merchHbm.deleteMerchantTblObjByQuery(lsvSlQry, locObjsession);
					merchantobj = null;
					if (isupdate) {
						if (tx != null) {
							tx.commit();
						}
						serverResponse(ivrservicecode,"00","R0122",mobiCommon.getMsg("R0122"),jsonFinalObj);
					} else {
						if (tx != null) {
							tx.rollback();
						}
						serverResponse(ivrservicecode,"01","R0123",mobiCommon.getMsg("R0123"),jsonFinalObj);
					}
				} else {
					if(tx!=null){
						tx.rollback();
					}
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6421, Request values are not correct format", "info", DocMasterlst.class);
					serverResponse("SI6421","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}	
			}else{
				if(tx!=null){
					tx.rollback();
				}
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6421, Request values are empty", "info", DocMasterlst.class);
				serverResponse("SI6421","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}	
		}catch(Exception e){
			System.out.println("Exception found DocMasterlst.class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6421, Sorry, an unhandled error occurred", "error", DocMasterlst.class);
			serverResponse("SI6421","02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			if(tx!=null){
				tx.rollback();
			}
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
