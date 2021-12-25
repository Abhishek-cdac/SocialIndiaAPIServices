package com.socialindiaservices.merchantmgmt;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonapi.DocMasterlst;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindia.generalmgnt.staffcreation;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.services.MerchantManageDaoServices;
import com.socialindiaservices.services.MerchantManageServices;
import com.socialindiaservices.vo.MerchantUtilitiImageTblVO;
import com.socialindiaservices.vo.MerchantUtilitiTblVO;

public class AddNewMerchantOfferImages   extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;
	private MerchantManageServices merchanthbm=new MerchantManageDaoServices();
	private CommonUtils common=new CommonUtils();
	byte conbytetoarr[] = new byte[1024];
	ResourceBundle rb=ResourceBundle.getBundle("applicationResources");

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
					MerchantUtilitiTblVO merUtilitiTblObj=new MerchantUtilitiTblVO();
					MerchantUtilitiImageTblVO merUtilitiTblImgObj=new MerchantUtilitiImageTblVO();
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
				
				Integer merchantUtilId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"merchantUtilId");
				String fileName = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"fileName");
				//String imageDetail = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"imageDetail");
				String lvrimgsrchpth = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"imgsrthpath");
				String isdeleteImage = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"isdeleteImage");
				if(Commonutility.checkempty(fileName)){
					fileName = fileName.replaceAll(" ", "_");
				}
				
				//conbytetoarr = Commonutility.toDecodeB64StrtoByary(imageDetail);
				String commonDate = common.getDateYYMMDDFormat();
				String offerFolder=rb.getString("external.offer.fldr");
				common.createDirIfNotExist(offerFolder);
				common.createDirIfNotExist(offerFolder+rb.getString("external.inner.webpath"));
				common.createDirIfNotExist(offerFolder+rb.getString("external.inner.mobilepath"));
				common.createDirIfNotExist(offerFolder+rb.getString("external.inner.webpath")+merchantUtilId);
				common.createDirIfNotExist(offerFolder+rb.getString("external.inner.mobilepath")+merchantUtilId);
				
				String lvrWebpath = offerFolder + rb.getString("external.inner.webpath")+merchantUtilId+"/"+commonDate;
				String mobtopath = offerFolder+ rb.getString("external.inner.mobilepath")+merchantUtilId+"/"+commonDate;	
				
				Commonutility.toWriteImageMobWebNewUtill(null, fileName,lvrimgsrchpth,lvrWebpath,mobtopath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, staffcreation.class);
				
				/*				
				String wrtsts = Commonutility.toByteArraytoWriteFiles(conbytetoarr, topath, fileName);									
				String limageFor = "all";
				int lneedWidth = Commonutility.stringToInteger(rb.getString("thump.img.width"));
	        	int lneedHeight = Commonutility.stringToInteger(rb.getString("thump.img.height"));
	        	String limgName = FilenameUtils.getBaseName(fileName);
				if (limgName==null || limgName.equalsIgnoreCase("null")) {
					limgName ="";
				}
				String limageFomat = FilenameUtils.getExtension(fileName);
				if (limageFomat==null || limageFomat.equalsIgnoreCase("null")) {
	 		 		limageFomat ="";
				}
				lvrWebpath=lvrWebpath+"/"+fileName;
	        	wrtsts= ImageCompress.toCompressImage(lvrWebpath, mobtopath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
			*/
	        	
	        	//wrtsts = Commonutility.toByteArraytoWriteFiles(conbytetoarr, topath, fileName);
				Date dat=new Date();
				merUtilitiTblObj.setMerchantUtilId(merchantUtilId);
				merUtilitiTblImgObj.setMerchantUtilId(merUtilitiTblObj);
				usermastobj.setUserId(ivrEntryByusrid);
				groupmstObj.setGroupCode(groupcode);
				merUtilitiTblImgObj.setImageName(fileName);
				merUtilitiTblImgObj.setStatusFlag(1);
				merUtilitiTblImgObj.setEntryBy(usermastobj);
				merUtilitiTblImgObj.setEntryDatetime(dat);
				merUtilitiTblImgObj.setModifyDatetime(dat);
				merUtilitiTblImgObj.setDocDateFolderName(commonDate);
				if(isdeleteImage!=null && isdeleteImage.equalsIgnoreCase("yes")){
					List<MerchantUtilitiImageTblVO> merchantImagelist = new ArrayList<MerchantUtilitiImageTblVO>();
					merchantImagelist=merchanthbm.deleteMerchantUtilitiImageTbl(merUtilitiTblImgObj, session);
					MerchantUtilitiImageTblVO merchantimgobj;
					for(Iterator<MerchantUtilitiImageTblVO> it=merchantImagelist.iterator();it.hasNext();){
						merchantimgobj=it.next();
						String filename=merchantimgobj.getImageName();
						String webFolder=rb.getString("external.offer.fldr")+rb.getString("external.inner.webpath")+merchantimgobj.getMerchantUtilId().getMerchantUtilId()+"/"+merchantimgobj.getDocDateFolderName();
						String mobFolder=rb.getString("external.offer.fldr")+rb.getString("external.inner.mobilepath")+merchantimgobj.getMerchantUtilId().getMerchantUtilId()+"/"+merchantimgobj.getDocDateFolderName();
						common.deleteIfFileExist(webFolder, filename);
						common.deleteIfFileExist(mobFolder, filename);
					}
				}
				
				boolean ishbmsuccess=merchanthbm.insertMerchantUtilitiImageTbl(merUtilitiTblImgObj, session);
				merUtilitiTblObj=null;
				groupmstObj=null;
				usermastobj=null;
				if(ishbmsuccess){
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("datalst", locObjDoclst);				
				serverResponse(ivrservicecode,"0","0000","success",locObjRspdataJson);
				if(tx!=null){
					tx.commit();
				}
				}else{
					locObjRspdataJson=new JSONObject();
					locObjRspdataJson.put("datalst", locObjDoclst);				
					serverResponse(ivrservicecode,"1","0000","error",locObjRspdataJson);
					if(tx!=null){
						tx.rollback();
					}
				}
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6423, Request values are not correct format", "info", DocMasterlst.class);
					serverResponse("SI6423","1","E0001","Request values are not correct format",locObjRspdataJson);
					if(tx!=null){
						tx.rollback();
					}
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6423, Request values are empty", "info", DocMasterlst.class);
				serverResponse("SI6423","1","E0001","Request values are empty",locObjRspdataJson);
				if(tx!=null){
					tx.rollback();
				}
			}	
		}catch(Exception e){
			System.out.println("Exception found CreateNewMerchant.class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6423, Sorry, an unhandled error occurred", "error", DocMasterlst.class);
			serverResponse("SI6423","2","E0002","Sorry, an unhandled error occurred",locObjRspdataJson);
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