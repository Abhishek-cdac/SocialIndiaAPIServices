package com.socialindiaservices.merchantmgmt;

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

import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonapi.DocMasterlst;
import com.pack.utilitypkg.Commonutility;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.services.MerchantManageDaoServices;
import com.socialindiaservices.services.MerchantManageServices;
import com.socialindiaservices.vo.MerchantIssueTblVO;
import com.socialindiaservices.vo.MerchantStockDetailTblVO;
import com.socialindiaservices.vo.MerchantTblVO;

public class Getmerchantviewdetails   extends ActionSupport{

	
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
				Integer mrchntId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mrchntId");
				MerchantTblVO merchObj=new MerchantTblVO();
				MerchantIssueTblVO merchissue=new MerchantIssueTblVO();
				lsvSlQry="from MerchantTblVO where mrchntActSts='1' and mrchntId='"+mrchntId+"'";
				System.out.println("Select Query : "+lsvSlQry);
				merchObj=merchHbm.getMerchantTblObjByQuery(lsvSlQry, locObjsession);
				if(merchObj.getMrchCategoryId()!=null){
					jsonFinalObj.put("merchantCategoryId",merchObj.getMrchCategoryId().getMrchCategoryId());
					jsonFinalObj.put("merchantCategoryidname",merchObj.getMrchCategoryId().getMrchCategoryName());
				} else {
					jsonFinalObj.put("merchantCategoryId",0);
					jsonFinalObj.put("merchantCategoryidname","");
				}
				
				
				jsonFinalObj.put("storeimage",merchObj.getStoreimage());
				jsonFinalObj.put("mrchntName",merchObj.getMrchntName());
				jsonFinalObj.put("mrchntEmail",merchObj.getMrchntEmail());
				jsonFinalObj.put("mrchntIsdCode",merchObj.getMrchntIsdCode());
				jsonFinalObj.put("mrchntPhNo",merchObj.getMrchntPhNo());
				jsonFinalObj.put("keyForSearch",merchObj.getKeyForSearch());
				jsonFinalObj.put("storeLocation",merchObj.getStoreLocation());
				jsonFinalObj.put("storeOpentime",merchObj.getStoreOpentime());
				jsonFinalObj.put("storeClosetime",merchObj.getStoreClosetime());
				jsonFinalObj.put("mrchntAdd1",merchObj.getMrchntAdd1());
				jsonFinalObj.put("mrchntAdd2",merchObj.getMrchntAdd2());
				if(merchObj.getCountryId()!=null){
				jsonFinalObj.put("countryName",merchObj.getCountryId().getCountryName());
				jsonFinalObj.put("countryId",merchObj.getCountryId().getCountryId());
				}else{
					jsonFinalObj.put("countryName","");
					jsonFinalObj.put("countryId",0);
				}
				if(merchObj.getStateId()!=null){
					jsonFinalObj.put("stateName",merchObj.getStateId().getStateName());
					jsonFinalObj.put("stateId",merchObj.getStateId().getStateId());
					}else{
						jsonFinalObj.put("stateName","");
						jsonFinalObj.put("stateId",0);
					}
				if(merchObj.getCityId()!=null){
					jsonFinalObj.put("cityName",merchObj.getCityId().getCityName());
					jsonFinalObj.put("cityId",merchObj.getCityId().getCityId());
					}else{
						jsonFinalObj.put("cityName","");
						jsonFinalObj.put("cityId",0);
					}
				if(merchObj.getPstlId()!=null){
//					jsonFinalObj.put("pstlcodeDesc",merchObj.getPstlId().getPstlCode());
//					jsonFinalObj.put("pstlId",merchObj.getPstlId().getPstlId());
					jsonFinalObj.put("pstlcodeDesc","");
					jsonFinalObj.put("pstlId",merchObj.getPstlId());
					}else{
						jsonFinalObj.put("pstlcodeDesc","");
						jsonFinalObj.put("pstlId",0);
					}
				jsonFinalObj.put("merchDescription",merchObj.getMerchDescription());
				jsonFinalObj.put("website",merchObj.getWebsite());
				jsonFinalObj.put("shopName",merchObj.getShopName());
				
				// Merchant Group code insert into mvp_merchant_issue_tbl
				GroupMasterTblVo lvrGrpmstobj = null, lvrGrpmstIDobj = null;
				String pGrpName = getText("Grp.merchant");
				Integer lvrMrcnhtgrpid = null;
				 // Select Group Code on Merchant
				String locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"')";
				Query locQryrst=locObjsession.createQuery(locSlqry);
				lvrGrpmstobj = (GroupMasterTblVo) locQryrst.uniqueResult();
				 if(lvrGrpmstobj!=null){					
					 lvrMrcnhtgrpid = lvrGrpmstobj.getGroupCode();
				 }else {
					 lvrMrcnhtgrpid = null;
				 }
				
				
				
				List<Object> lvrObjfunctionlstitr = null;
				String lvrfunSlqry = "select issueId,description from MerchantIssueTblVO where mcrctLaborId="+mrchntId+" and status=1 and ivrGrpcodeobj = "+lvrMrcnhtgrpid;
				System.out.println("lvrfunSlqry -------- "+lvrfunSlqry);
				lvrObjfunctionlstitr=locObjsession.createQuery(lvrfunSlqry).list();
				jsonFinalObj.put("issuedata", lvrObjfunctionlstitr);	
				JSONArray merchStockarray=new JSONArray();
				JSONObject merstkjobj=new JSONObject();
				if(merchObj.getMrchCategoryId()!=null && merchObj.getMrchCategoryId().getMrchCategoryId()==1){
					List<MerchantStockDetailTblVO> merchantStockList=new ArrayList<MerchantStockDetailTblVO>();
					lsvSlQry="from MerchantStockDetailTblVO where mrchntId.mrchntId="+mrchntId;
					merchantStockList=merchHbm.getMerchantStockDetailTblObjByQuery(lsvSlQry, locObjsession);
					MerchantStockDetailTblVO mercstkObj;
					for (Iterator<MerchantStockDetailTblVO> it = merchantStockList
							.iterator(); it.hasNext();) {
						mercstkObj=it.next();
						merstkjobj=new JSONObject();
						merstkjobj.put("typeName", mercstkObj.getTypeName());
						merstkjobj.put("quantity", mercstkObj.getQuantity());
						merchStockarray.put(merstkjobj);
						merstkjobj=null;
					}
					jsonFinalObj.put("merchantStockData",merchStockarray);
					merchStockarray=null;
				}else if(merchObj.getMrchCategoryId()!=null && merchObj.getMrchCategoryId().getMrchCategoryId()==2){
					List<MerchantStockDetailTblVO> merchantStockList=new ArrayList<MerchantStockDetailTblVO>();
					lsvSlQry="from MerchantStockDetailTblVO where mrchntId.mrchntId="+mrchntId;
					merchantStockList=merchHbm.getMerchantStockDetailTblObjByQuery(lsvSlQry, locObjsession);
					MerchantStockDetailTblVO mercstkObj;
					for (Iterator<MerchantStockDetailTblVO> it = merchantStockList
							.iterator(); it.hasNext();) {
						mercstkObj=it.next();
						merstkjobj=new JSONObject();
						Integer qty=0;
						if(mercstkObj.getTypeName()!=null){
							qty=mercstkObj.getQuantity();
						}else{
							qty=0;
						}
						merstkjobj.put("typeName", mercstkObj.getTypeName());
						merstkjobj.put("quantity", qty);
						if(mercstkObj.getPower()!=null){
							merstkjobj.put("power", mercstkObj.getPower().toString());
						}else{
							merstkjobj.put("power", "");
						}
						if(mercstkObj.getCompanyName()!=null){
							merstkjobj.put("companyName", mercstkObj.getCompanyName());
						}else{
							merstkjobj.put("companyName", "");
						}
						merchStockarray.put(merstkjobj);
						merstkjobj=null;
					}
					jsonFinalObj.put("merchantStockData",merchStockarray);
					merchStockarray=null;
				}else if(merchObj.getMrchCategoryId()!=null && merchObj.getMrchCategoryId().getMrchCategoryId()==3){
					List<MerchantStockDetailTblVO> merchantStockList=new ArrayList<MerchantStockDetailTblVO>();
					lsvSlQry="from MerchantStockDetailTblVO where mrchntId.mrchntId="+mrchntId;
					merchantStockList=merchHbm.getMerchantStockDetailTblObjByQuery(lsvSlQry, locObjsession);
					MerchantStockDetailTblVO mercstkObj;
					for (Iterator<MerchantStockDetailTblVO> it = merchantStockList
							.iterator(); it.hasNext();) {
						mercstkObj=it.next();
						merstkjobj=new JSONObject();
						if(mercstkObj.getTypeName()!=null){
							merstkjobj.put("typeName", mercstkObj.getTypeName());
						}else{
							merstkjobj.put("typeName", "");
						}
						if(mercstkObj.getQuantity()!=null){
							merstkjobj.put("quantity", mercstkObj.getQuantity());
						}else{
							merstkjobj.put("quantity", 0);
						}
						
						
						merchStockarray.put(merstkjobj);
						merstkjobj=null;
					}
					jsonFinalObj.put("merchantStockData",merchStockarray);
					merchStockarray=null;
				}else if(merchObj.getMrchCategoryId()!=null && merchObj.getMrchCategoryId().getMrchCategoryId()==4){
					List<MerchantStockDetailTblVO> merchantStockList=new ArrayList<MerchantStockDetailTblVO>();
					lsvSlQry="from MerchantStockDetailTblVO where mrchntId.mrchntId="+mrchntId;
					merchantStockList=merchHbm.getMerchantStockDetailTblObjByQuery(lsvSlQry, locObjsession);
					MerchantStockDetailTblVO mercstkObj;
					for (Iterator<MerchantStockDetailTblVO> it = merchantStockList
							.iterator(); it.hasNext();) {
						mercstkObj=it.next();
						merstkjobj=new JSONObject();
						if(mercstkObj.getCuisines()!=null){
							merstkjobj.put("cuisines", mercstkObj.getCuisines());
						}else{
							merstkjobj.put("cuisines", "");
						}
						if(mercstkObj.getTypeName()!=null){
							merstkjobj.put("typeName", mercstkObj.getTypeName());
						}else{
							merstkjobj.put("typeName", "");
						}
						
					}
					jsonFinalObj.put("merchantStockData",merstkjobj);
					merstkjobj=null;
				}
				
				serverResponse(ivrservicecode,"0","0000","success",jsonFinalObj);
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6419, Request values are not correct format", "info", DocMasterlst.class);
					serverResponse("SI6419","1","E0001","Request values are not correct format",locObjRspdataJson);
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6419, Request values are empty", "info", DocMasterlst.class);
				serverResponse("SI6419","1","E0001","Request values are empty",locObjRspdataJson);
			}	
		}catch(Exception e){
			System.out.println("Exception found DocMasterlst.class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6419, Sorry, an unhandled error occurred", "error", DocMasterlst.class);
			serverResponse("SI6419","2","E0002","Sorry, an unhandled error occurred",locObjRspdataJson);
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