package com.socialindiaservices.merchantmgmt;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonapi.DocMasterlst;
import com.pack.commonvo.CityMasterTblVO;
import com.pack.commonvo.CountryMasterTblVO;
import com.pack.commonvo.PostalCodeMasterTblVO;
import com.pack.commonvo.StateMasterTblVO;
import com.pack.labor.LaborUtility;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.services.MerchantManageDaoServices;
import com.socialindiaservices.services.MerchantManageServices;
import com.socialindiaservices.vo.MerchantCategoryTblVO;
import com.socialindiaservices.vo.MerchantIssueTblVO;
import com.socialindiaservices.vo.MerchantStockDetailTblVO;
import com.socialindiaservices.vo.MerchantTblVO;

public class EditNewMerchants  extends ActionSupport{

	
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;
	private MerchantManageServices merchanthbm=new MerchantManageDaoServices();

	public String execute(){
		Log log = null;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String ivrservicecode=null;
		Integer ivrEntryByusrid = 0;
		List<Object> locObjDoclst = null;
		Session session = null;
		Transaction tx = null;
		try{
			log= new Log();
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			Commonutility.toWriteConsole("Step 1 : Merchant Modify Called [Start]");
			log.logMessage("Step 1 : Merchant Modify Called [Start]", "info", EditNewMerchants.class);
			String locWebImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.marchantmstfldr")+getText("external.inner.webpath");
			String locMobImgFldrPath=getText("external.imagesfldr.path")+getText("external.inner.marchantmstfldr")+getText("external.inner.mobilepath");
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					MerchantStockDetailTblVO stockDetail=new MerchantStockDetailTblVO();
					MerchantTblVO merchantObj=new MerchantTblVO();
					MerchantCategoryTblVO merchantCategoryOlbj=new MerchantCategoryTblVO();
					CountryMasterTblVO countryobj=new CountryMasterTblVO();
					StateMasterTblVO stateobj=new StateMasterTblVO();
					CityMasterTblVO cityobj=new CityMasterTblVO();
					PostalCodeMasterTblVO pstlcodeobj=new PostalCodeMasterTblVO();
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
				Integer mrchCategoryId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"merchantCategoryId");
				String mrchntName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mrchntName");
				String mrchntEmail=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mrchntEmail");
				String mrchntIsdCode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mrchntIsdCode");
				String mrchntPhNo=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mrchntPhNo");
				String keyForSearch=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"keyForSearch");
				String storeLocation=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"storeLocation");
				String storeOpentime=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"storeOpentime");
				String storeClosetime=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"storeClosetime");
				String mrchntAdd1=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mrchntAdd1");
				String mrchntAdd2=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"mrchntAdd2");
				Integer countryId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"countryId");
				Integer stateId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"stateId");
				Integer cityId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"cityId");
				Integer pstlId=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"pstlId");
				String merchDescription=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"merchDescription");
				String website=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"website");
				String shopName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"shopName");
				String locimagename = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imagename");						
				//String locimg_encdata = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imgencdata");
				String lvrimgsrchpth = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "mrchimgsrchpth");
				int count=(Integer)Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "count");
				Date dat=new Date();
				
				JSONArray jry=(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "issueid");	
				JSONArray jryid=(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "issuetxt");				 
					String idd = null;
					String lvrfunmtr = "";
					String lvrfuntempqry = null;
					MerchantIssueTblVO inrlocfun = null;
					String mvpMrchTblid = "mcrctLaborId";
					String pojo = "MerchantIssueTblVO";
					// Merchant Group code insert into mvp_merchant_issue_tbl
					GroupMasterTblVo lvrGrpmstobj = null, lvrGrpmstIDobj = null;
					String pGrpName = getText("Grp.merchant");
					 // Select Group Code on Merchant
					String locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+pGrpName+"')";
					Query locQryrst=session.createQuery(locSlqry);
					lvrGrpmstobj = (GroupMasterTblVo) locQryrst.uniqueResult();
					 if(lvrGrpmstobj!=null){
						 lvrGrpmstIDobj = new GroupMasterTblVo();
						 lvrGrpmstIDobj.setGroupCode(lvrGrpmstobj.getGroupCode());								 
					 }else {
						 lvrGrpmstIDobj = null;
					 }
					
					 Common common = new CommonDao();
						lvrfunmtr=common.commonLarmctDelete(mrchntId, pojo, mvpMrchTblid,lvrGrpmstobj.getGroupCode());
						
					for (int i = 0; i < jryid.length(); i++) {
					 String textname=(String)jryid.getString(i);
						 if(textname!=null && !textname.equalsIgnoreCase("") && !textname.equalsIgnoreCase("null")){
							 inrlocfun=new MerchantIssueTblVO();
							 inrlocfun.setDescription(textname.trim());
							 inrlocfun.setEntryBy(1);
							 inrlocfun.setStatus(1);
							 inrlocfun.setMcrctLaborId(mrchntId);
							// inrlocfun.setMvpMrchTblid(merchantObj);
							 inrlocfun.setEntryDatetime(dat);
							 inrlocfun.setIvrGrpcodeobj(lvrGrpmstIDobj);
							 boolean issuesuss = merchanthbm.insertMerchantissueTbl(inrlocfun, session);	
							inrlocfun=null;
						
					 }
				 } 
				 
					usermastobj.setUserId(ivrEntryByusrid);
					groupmstObj.setGroupCode(groupcode);
					merchantCategoryOlbj.setMrchCategoryId(mrchCategoryId);
					merchantObj.setMrchntId(mrchntId);
					merchantObj.setMrchCategoryId(merchantCategoryOlbj);
					merchantObj.setMrchntName(mrchntName);
					merchantObj.setMrchntEmail(mrchntEmail);
					merchantObj.setMrchntIsdCode(mrchntIsdCode);
					merchantObj.setMrchntPhNo(mrchntPhNo);
					merchantObj.setKeyForSearch(keyForSearch);
					merchantObj.setStoreLocation(storeLocation);
					merchantObj.setStoreOpentime(storeOpentime);
					merchantObj.setStoreClosetime(storeClosetime);
					merchantObj.setMrchntAdd1(mrchntAdd1);
					merchantObj.setMrchntAdd2(mrchntAdd2);
					merchantObj.setShopName(shopName);
					if (locimagename != null && Commonutility.checkempty(locimagename)) {
						locimagename = locimagename.replaceAll(" ", "_");
						merchantObj.setStoreimage(locimagename);
					}
					if (countryId != null) {
						countryobj.setCountryId(countryId);
						merchantObj.setCountryId(countryobj);
					}
					if (stateId != null) {
						stateobj.setStateId(stateId);
						merchantObj.setStateId(stateobj);
					}
					if (cityId != null) {
						cityobj.setCityId(cityId);
						merchantObj.setCityId(cityobj);
					}
					if (pstlId != null) {
						pstlcodeobj.setPstlId(pstlId);
//						merchantObj.setPstlId(pstlcodeobj);
						merchantObj.setPstlId(pstlId);
					}
				merchantObj.setMerchDescription(merchDescription);
				merchantObj.setWebsite(website);
				merchantObj.setEntryBy(usermastobj);
				merchantObj.setGroupCode(groupmstObj);
				merchantObj.setMrchntActSts(1);
				merchantObj.setModifyDatetime(dat);
				
				boolean ishbmsuccess=merchanthbm.updateMerchantTbl(merchantObj, session);
				ishbmsuccess = merchanthbm.deleteStockDetailBymerchantId(merchantObj, session);
				Commonutility.toWriteConsole("Step 2 : Merchant update Status : "+ishbmsuccess);
				log.logMessage("Step 2 : Merchant update Status : "+ishbmsuccess, "info", EditNewMerchants.class);
				if(ishbmsuccess || mrchntId!=null){
					 try{
						 //image write into folder
						 if((lvrimgsrchpth!=null && !lvrimgsrchpth.equalsIgnoreCase("null") && !lvrimgsrchpth.equalsIgnoreCase("")) && (locimagename!=null && !locimagename.equalsIgnoreCase("") && !locimagename.equalsIgnoreCase("null"))){
							 Commonutility.toWriteConsole("Step 3 : Merchant image writen.");
							 log.logMessage("Step 3 : Merchant image writen.", "info", EditNewMerchants.class);
							 String delrs = Commonutility.todelete("", locWebImgFldrPath+merchantObj.getMrchntId()+"/");
							 String delrs_mob= Commonutility.todelete("", locMobImgFldrPath+merchantObj.getMrchntId()+"/");					  
							 Commonutility.toWriteImageMobWebNewUtill(merchantObj.getMrchntId(), locimagename,lvrimgsrchpth,locWebImgFldrPath,locMobImgFldrPath,rb.getString("thump.img.width"),rb.getString("thump.img.height"), log, EditNewMerchants.class);
							 
							 /*
							 String delrs= Commonutility.todelete("", locWebImgFldrPath+mrchntId+"/");					 
							 byte imgbytee[]= new byte[1024];
							 imgbytee=Commonutility.toDecodeB64StrtoByary(locimg_encdata);
							 String wrtsts=Commonutility.toByteArraytoWriteFiles(imgbytee, locWebImgFldrPath+mrchntId+"/", locimagename);

							 String delrs_mob= Commonutility.todelete("",locMobImgFldrPath+mrchntId+"/");
							//mobile - small image
							String limgSourcePath=locWebImgFldrPath+mrchntId+"/"+locimagename;			
				 		 	String limgDisPath = locMobImgFldrPath+mrchntId+"/";
				 		 	String limgName = FilenameUtils.getBaseName(locimagename);
				 		 	String limageFomat = FilenameUtils.getExtension(locimagename);		 	    			 	    	 
				 	    	String limageFor = "all";
				 	    	int lneedWidth = Commonutility.stringToInteger(rb.getString("thump.img.width"));
			        		int lneedHeight = Commonutility.stringToInteger(rb.getString("thump.img.height"));	
				        	ImageCompress.toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile				 				 				
							*/
						 }
						 } catch(Exception imger){
							 Commonutility.toWriteConsole("Exception in  image write on labor insert : "+imger);
							 log.logMessage("Exception in Image write on labor insert", "info", LaborUtility.class);
						 }finally{
							 
						 }	
				if(mrchCategoryId!=null && mrchCategoryId==1){
					String typeName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"typeName");
					String quantity=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"quantity");
					Commonutility.toWriteConsole("typeName----------------------"+typeName);
					if(typeName != null && typeName.contains("!_!")){
						String[] typeeventaray=typeName.split("!_!");
						String[] quantityeventarray=quantity.split("!_!");
						for(int k=0;k<typeeventaray.length;k++){
							Commonutility.toWriteConsole("typeeventaray[k]---------------"+typeeventaray[k]);
							stockDetail=new MerchantStockDetailTblVO();
							stockDetail.setMrchntId(merchantObj);
							stockDetail.setMrchCategoryId(merchantCategoryOlbj);
							stockDetail.setTypeName(typeeventaray[k]);
							stockDetail.setQuantity(Integer.parseInt(quantityeventarray[k]));
							stockDetail.setEntryBy(usermastobj);
							stockDetail.setEntryDatetime(dat);
							stockDetail.setModifyDatetime(dat);
							ishbmsuccess=merchanthbm.insertMerchantStockDetailTbl(stockDetail, session);
							if(!ishbmsuccess){
								break;
							}
						
						}
					} else {
						if(typeName!= null && quantity!=null && quantity.length()>0 && typeName.length()>0){
							stockDetail=new MerchantStockDetailTblVO();
							stockDetail.setMrchntId(merchantObj);
							stockDetail.setMrchCategoryId(merchantCategoryOlbj);
							stockDetail.setTypeName(typeName);
							stockDetail.setQuantity(Integer.parseInt(quantity));
							stockDetail.setEntryBy(usermastobj);
							stockDetail.setEntryDatetime(dat);
							stockDetail.setModifyDatetime(dat);
							ishbmsuccess=merchanthbm.insertMerchantStockDetailTbl(stockDetail, session);
						}
					}
				}else if(mrchCategoryId!=null && mrchCategoryId==2){
					String typeName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"typeName");
					String quantity=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"quantity");
					String power = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"power");
					String cmpny = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"companyName");
					if(typeName!=null && typeName.contains("!_!")){
						String[] typeeventaray=typeName.split("!_!");
						String[] quantityeventarray=quantity.split("!_!");
						String [] lvrcmpnyName = cmpny.split("!_!");
						String [] lvrPwr = power.split("!_!");
						for(int k=0;k<typeeventaray.length;k++){
							Commonutility.toWriteConsole("typeeventaray[k]---------------"+typeeventaray[k]);
							stockDetail=new MerchantStockDetailTblVO();
							stockDetail.setMrchntId(merchantObj);
							stockDetail.setMrchCategoryId(merchantCategoryOlbj);
							stockDetail.setTypeName(typeeventaray[k]);
							stockDetail.setQuantity(Integer.parseInt(quantityeventarray[k]));
							stockDetail.setCompanyName(lvrcmpnyName[k]);
							if(Commonutility.checkempty(lvrPwr[k])){
								stockDetail.setPower(Float.parseFloat(lvrPwr[k]));
							} else {
								stockDetail.setPower(null);
							}						
							stockDetail.setEntryBy(usermastobj);
							stockDetail.setEntryDatetime(dat);
							stockDetail.setModifyDatetime(dat);
							ishbmsuccess=merchanthbm.insertMerchantStockDetailTbl(stockDetail, session);
							if(!ishbmsuccess){
								break;
							}
						
						}
					} else {
						if(typeName!= null && quantity!=null && quantity.length()>0 && typeName.length()>0){
							stockDetail=new MerchantStockDetailTblVO();
							stockDetail.setMrchntId(merchantObj);
							stockDetail.setMrchCategoryId(merchantCategoryOlbj);
							stockDetail.setTypeName(typeName);
							stockDetail.setQuantity(Integer.parseInt(quantity));
							stockDetail.setCompanyName(cmpny);
							if(Commonutility.checkempty(power)){
								stockDetail.setPower(Float.parseFloat(power));
							} else {
								stockDetail.setPower(null);
							}
							stockDetail.setEntryBy(usermastobj);
							stockDetail.setEntryDatetime(dat);
							stockDetail.setModifyDatetime(dat);
							ishbmsuccess=merchanthbm.insertMerchantStockDetailTbl(stockDetail, session);
						}
					}
				}else if(mrchCategoryId!=null && mrchCategoryId==3){
					String typeName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"typeName");
					String quantity=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"quantity");
					if(typeName.contains("!_!")){
						String[] typeeventaray=typeName.split("!_!");
						String[] quantityeventarray=quantity.split("!_!");
						for(int k=0;k<typeeventaray.length;k++){
						Commonutility.toWriteConsole("typeeventaray[k]---------------"+typeeventaray[k]);
						stockDetail=new MerchantStockDetailTblVO();
						stockDetail.setMrchntId(merchantObj);
						stockDetail.setMrchCategoryId(merchantCategoryOlbj);
						stockDetail.setTypeName(typeeventaray[k]);
						stockDetail.setQuantity(Integer.parseInt(quantityeventarray[k]));
						stockDetail.setEntryBy(usermastobj);
						stockDetail.setEntryDatetime(dat);
						stockDetail.setModifyDatetime(dat);
						ishbmsuccess=merchanthbm.insertMerchantStockDetailTbl(stockDetail, session);
							if(!ishbmsuccess){
								break;
							}
						
						}
					} else {
						if(typeName!= null && quantity!=null && quantity.length()>0 && typeName.length()>0){
						stockDetail=new MerchantStockDetailTblVO();
						stockDetail.setMrchntId(merchantObj);
						stockDetail.setMrchCategoryId(merchantCategoryOlbj);
						stockDetail.setTypeName(typeName);
						stockDetail.setQuantity(Integer.parseInt(quantity));
						stockDetail.setEntryBy(usermastobj);
						stockDetail.setEntryDatetime(dat);
						stockDetail.setModifyDatetime(dat);
						ishbmsuccess=merchanthbm.insertMerchantStockDetailTbl(stockDetail, session);
						}
					}
				}else if(mrchCategoryId!=null && mrchCategoryId==4){
					String cuisines=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"cuisines");
					String typeName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"typeName");
					Commonutility.toWriteConsole("restarunt typeName-----------------"+typeName);
					stockDetail.setMrchntId(merchantObj);
					stockDetail.setMrchCategoryId(merchantCategoryOlbj);
					stockDetail.setCuisines(cuisines);
					stockDetail.setTypeName(typeName);
					stockDetail.setEntryBy(usermastobj);
					stockDetail.setEntryDatetime(dat);
					stockDetail.setModifyDatetime(dat);
					ishbmsuccess=merchanthbm.insertMerchantStockDetailTbl(stockDetail, session);
				} else{
					
				}
				Commonutility.toWriteConsole("Step 4 : Merchant category updated.");
				log.logMessage("Step 4 : Merchant category updated.", "info", EditNewMerchants.class);
				
				}
				Commonutility.toWriteConsole("Step 5 : Merchant Modify [End]");
				log.logMessage("Step 5 : Merchant Modify [End]", "info", CreateNewMerchant.class);
				if(ishbmsuccess){
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("datalst", locObjDoclst);				
				serverResponse(ivrservicecode,"00","R0120",mobiCommon.getMsg("R0120"),locObjRspdataJson);
				if(tx!=null){
					tx.commit();
				}
				}else{
					locObjRspdataJson=new JSONObject();
					locObjRspdataJson.put("datalst", locObjDoclst);				
					serverResponse(ivrservicecode,"01","R0121",mobiCommon.getMsg("R0121"),locObjRspdataJson);
					if(tx!=null){
						tx.rollback();
					}
				}
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI6417, Request values are not correct format", "info", DocMasterlst.class);
					serverResponse("SI6417","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
					if(tx!=null){
						tx.rollback();
					}
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI6417, Request values are empty", "info", DocMasterlst.class);
				serverResponse("SI6417","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
				if(tx!=null){
					tx.rollback();
				}
			}	
		}catch(Exception e){
			Commonutility.toWriteConsole("Exception found CreateNewMerchant.class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI6417, Sorry, an unhandled error occurred", "error", DocMasterlst.class);
			serverResponse("SI6417","02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
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
	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson) {
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
