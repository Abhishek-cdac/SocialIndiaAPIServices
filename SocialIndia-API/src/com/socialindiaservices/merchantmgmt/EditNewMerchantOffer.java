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
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.MerchantManageDaoServices;
import com.socialindiaservices.services.MerchantManageServices;
import com.socialindiaservices.vo.MerchantTblVO;
import com.socialindiaservices.vo.MerchantUtilitiTblVO;

public class EditNewMerchantOffer   extends ActionSupport{

	
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;
	private MerchantManageServices merchanthbm = new MerchantManageDaoServices();
	private CommonUtils common = new CommonUtils();

	public String execute(){
		Log log = null;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json	
		String ivrservicecode = null;
		Integer ivrEntryByusrid = 0;
		List<Object> locObjDoclst = null;
		Session session = null;
		Transaction tx = null;
		try {
			log= new Log();
			log.logMessage("Step 1 : Offer modify called [Start]", "info", EditNewMerchantOffer.class);
			Commonutility.toWriteConsole("Step 1 : Offer modify called [Start]");
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					MerchantUtilitiTblVO merUtilitiTblObj=new MerchantUtilitiTblVO();
					MerchantTblVO merchantObj=new MerchantTblVO();
					UserMasterTblVo usermastobj=new UserMasterTblVo();
					GroupMasterTblVo groupmstObj=new GroupMasterTblVo();
					
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"userId");
					Integer groupcode=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"usrTyp");
					if (ivrEntryByusrid != null) {
					} else {
						ivrEntryByusrid = 0;
					}
				
					Integer mrchntId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mrchntId");
					Integer merchantUtilId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"merchantUtilId");
					String broucherName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"broucherName");
					String offerName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"offerName");
					String offerPrice=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"offerPrice");
					String actualPrice=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"actualPrice");
					String offerValidFrom=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"offerValidFrom");
					String offerValidTo=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"offerValidTo");
					String description=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"description");							
					Date dat = new Date();
					merchantObj.setMrchntId(mrchntId);
					usermastobj.setUserId(ivrEntryByusrid);
					groupmstObj.setGroupCode(groupcode);
					merUtilitiTblObj.setMerchantUtilId(merchantUtilId);
					merUtilitiTblObj.setBroucherName(broucherName);
					merUtilitiTblObj.setOfferName(offerName);
					merUtilitiTblObj.setOfferPrice(Float.parseFloat(offerPrice));
					merUtilitiTblObj.setActualPrice(Float.parseFloat(actualPrice));
					if (offerValidFrom != null&& !offerValidFrom.equalsIgnoreCase("")) {
						merUtilitiTblObj.setOfferValidFrom(common.stringTODate(offerValidFrom));
					}
					if (offerValidTo!=null && !offerValidTo.equalsIgnoreCase("")){
						merUtilitiTblObj.setOfferValidTo(common.stringTODate(offerValidTo));
					}
					merUtilitiTblObj.setDescription(description);
					merUtilitiTblObj.setMrchntId(merchantObj);
					merUtilitiTblObj.setEntryBy(usermastobj);
					merUtilitiTblObj.setStatusFlag(1);
					merUtilitiTblObj.setEntryDatetime(dat);
					merUtilitiTblObj.setModifyDatetime(dat);
				
					boolean ishbmsuccess=merchanthbm.updateMerchantUtilitiTbl(merUtilitiTblObj, session);
					log.logMessage("Step 2 : Offer modify status : "+ishbmsuccess, "info", EditNewMerchantOffer.class);
					Commonutility.toWriteConsole("Step 2 : Offer modify status : "+ishbmsuccess);
					if (ishbmsuccess) {
						locObjRspdataJson = new JSONObject();
						locObjRspdataJson.put("merchantUtilId", merUtilitiTblObj.getMerchantUtilId());										
						if(tx!=null){
							tx.commit();
						}
						log.logMessage("Step 3 : Offer modify called success [End]", "info", EditNewMerchantOffer.class);
						Commonutility.toWriteConsole("Step 3 : Offer modify called success[End]");
						serverResponse(ivrservicecode,"00","R0132",mobiCommon.getMsg("R0132"),locObjRspdataJson);
						
					} else {
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson.put("datalst", locObjDoclst);			
						if(tx!=null){
							tx.rollback();
						}
						log.logMessage("Step 3 : Offer modify called error [End]", "info", EditNewMerchantOffer.class);
						Commonutility.toWriteConsole("Step 3 : Offer modify called error [End]");
						serverResponse(ivrservicecode,"01","R0133",mobiCommon.getMsg("R0133"),locObjRspdataJson);						
					}
					merUtilitiTblObj = null;
					groupmstObj = null;
					usermastobj = null;
					merchantObj = null;
				} else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Step -1 : Service code : SI6424, Request values are not correct format", "info", DocMasterlst.class);
					serverResponse("SI6424","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
					if(tx!=null){
						tx.rollback();
					}
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Step -1  Service code : SI6424, Request values are empty", "info", DocMasterlst.class);
				serverResponse("SI6424","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
				if(tx!=null){
					tx.rollback();
				}
			}	
		}catch(Exception e){
			Commonutility.toWriteConsole("Exception found CreateNewMerchant.class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6424, Sorry, an unhandled error occurred", "error", DocMasterlst.class);
			serverResponse("SI6424","02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			if(tx!=null){
				tx.rollback();
			}
		}finally{
			locObjRecvJson=null;locObjRspdataJson=null;	//locObjRecvdataJson =null;
			if(session!=null){
				session.flush();session.clear();session.close();session=null;
			}
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

