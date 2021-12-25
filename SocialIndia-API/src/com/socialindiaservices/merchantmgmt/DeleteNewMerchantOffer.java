package com.socialindiaservices.merchantmgmt;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

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
import com.socialindiaservices.vo.MerchantUtilitiImageTblVO;
import com.socialindiaservices.vo.MerchantUtilitiTblVO;

public class DeleteNewMerchantOffer    extends ActionSupport{

	
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;
	private MerchantManageServices merchanthbm=new MerchantManageDaoServices();
	ResourceBundle rb=ResourceBundle.getBundle("applicationResources");
	private CommonUtils common=new CommonUtils();

	public String execute(){
		Log log= new Log();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		String ivrservicecode=null;
		Integer ivrEntryByusrid = 0;
		List<Object> locObjDoclst = null;
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
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
				String isdeleteImage=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"isdeleteImage");
				
				List<MerchantUtilitiImageTblVO> merchantImagelist=new ArrayList<MerchantUtilitiImageTblVO>();
				MerchantUtilitiTblVO merchantutilObj=new MerchantUtilitiTblVO();
				MerchantUtilitiImageTblVO merUtilitiTblImgObj=new MerchantUtilitiImageTblVO();
				merchantutilObj.setMerchantUtilId(merchantUtilId);
				merUtilitiTblImgObj.setMerchantUtilId(merchantutilObj);
				merchantImagelist=merchanthbm.deleteMerchantUtilitiImageTbl(merUtilitiTblImgObj, session);
				boolean ishbmsuccess=merchanthbm.deleteMerchantUtilitiTbl(merchantutilObj, session);
				if(isdeleteImage!=null && isdeleteImage.equalsIgnoreCase("yes")){
				MerchantUtilitiImageTblVO merchantimgobj;
						for (Iterator<MerchantUtilitiImageTblVO> it=merchantImagelist.iterator();it.hasNext();){
					merchantimgobj=it.next();
					String filename=merchantimgobj.getImageName();
					String webFolder=rb.getString("external.offer.fldr")+rb.getString("external.inner.webpath")+merchantimgobj.getMerchantUtilId().getMerchantUtilId()+"/"+merchantimgobj.getDocDateFolderName();
					String mobFolder=rb.getString("external.offer.fldr")+rb.getString("external.inner.mobilepath")+merchantimgobj.getMerchantUtilId().getMerchantUtilId()+"/"+merchantimgobj.getDocDateFolderName();
					common.deleteIfFileExist(webFolder, filename);
					common.deleteIfFileExist(mobFolder, filename);
						}
						merchantimgobj = null;
					}
					merchantImagelist = null;
					if (ishbmsuccess) {
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson.put("merchantUtilId", merchantutilObj.getMerchantUtilId());		
						if(tx!=null){
							tx.commit();
						}
						serverResponse(ivrservicecode,"00","R0134",mobiCommon.getMsg("R0134"),locObjRspdataJson);
					}else{
						locObjRspdataJson=new JSONObject();
						locObjRspdataJson.put("datalst", locObjDoclst);	
						if(tx!=null){
							tx.rollback();
						}
						serverResponse(ivrservicecode,"01","R0135",mobiCommon.getMsg("R0135"),locObjRspdataJson);
					
					}
					groupmstObj = null;
					usermastobj = null;
					merchantObj = null;
				} else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6425, Request values are not correct format", "info", DocMasterlst.class);
					serverResponse("SI6425","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
					if(tx!=null){
						tx.rollback();
					}
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6425, Request values are empty", "info", DocMasterlst.class);
				serverResponse("SI6425","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
				if(tx!=null){
					tx.rollback();
				}
			}	
		}catch(Exception e){
			System.out.println("Exception found CreateNewMerchant.class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6425, Sorry, an unhandled error occurred", "error", DocMasterlst.class);
			serverResponse("SI6425","02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			if(tx!=null){
				tx.rollback();
			}
		}finally{
			locObjRecvJson=null;locObjRspdataJson=null;	//locObjRecvdataJson =null;
			if(session!=null){
				session.close();
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
