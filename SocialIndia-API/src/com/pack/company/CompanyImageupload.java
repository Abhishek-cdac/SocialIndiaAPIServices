package com.pack.company;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.company.persistance.CompanyDao;
import com.pack.company.persistance.CompanyDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;

public class CompanyImageupload extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	private String ivrservicecode;
	private CommonUtils common=new CommonUtils();
	//ResourceBundle rb=ResourceBundle.getBundle("applicationResources");
	public String execute(){
		Log log = null;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		Session locObjsession = null;		
		String ivrservicecode=null, lvrCompnyid = null, lvrimagename = null,locimg_encdata = null, lvrimagedelsts=null;
		Integer ivrEntryByusrid = null;
		CompanyDao lvrcmpdaoobj = null;
		try{
			log= new Log();
			log.logMessage("Step 1 : Company Image upload [Start]", "info", CompanyImageupload.class);
			Commonutility.toWriteConsole("Step 1 : Company Image upload [Start]");
			locObjsession = HibernateUtil.getSession();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"userId");
				Integer groupcode=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"usrTyp");
				
				lvrCompnyid = (String)  Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"companyid");
				lvrimagename = (String)  Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"imagename");
				locimg_encdata = (String)  Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"imgencdata");
				lvrimagedelsts = (String)  Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"deleteimage");
				if (ivrEntryByusrid != null) {
				} else {
					ivrEntryByusrid = 0;
				}
				if(Commonutility.checkempty(lvrimagename)){
					lvrimagename = lvrimagename.replaceAll(" ", "_");
				}
				String pWebImagpath=getText("external.imagesfldr.path")+getText("external.inner.companyfldr")+getText("external.inner.webpath");
				String pMobImgpath=getText("external.imagesfldr.path")+getText("external.inner.companyfldr")+getText("external.inner.mobilepath");				
				if(lvrCompnyid!=null && !lvrCompnyid.equalsIgnoreCase("null") && !lvrCompnyid.equalsIgnoreCase("") && Commonutility.toCheckisNumeric(lvrCompnyid)){
					log.logMessage("Step 2 : Company Id Found [lvrCompnyid] : "+lvrCompnyid, "info", CompanyImageupload.class);
					Commonutility.toWriteConsole("Step 2 : Company Id Found [lvrCompnyid] : "+lvrCompnyid);
					try{
						  int lvrcmpidint = Integer.parseInt(lvrCompnyid);
						  String lvrUdqry ="update CompanyMstTblVO set imageNameWeb = '"+lvrimagename+"', imageNameMob ='"+lvrimagename+"' where companyId = "+lvrcmpidint+"";
						  log.logMessage("Step 3 : Update query : "+lvrUdqry, "info", CompanyImageupload.class);
						  Commonutility.toWriteConsole("Step 3 : Update query : "+lvrUdqry);						 					 
						  lvrcmpdaoobj = new CompanyDaoservice();
						  boolean  lvrupdsts = lvrcmpdaoobj.updateCompanyInfo(lvrUdqry);							  
						  log.logMessage("Step 4 : Update Query Result: "+lvrupdsts, "info", CompanyImageupload.class);
						  Commonutility.toWriteConsole("Step 4 : Update Query Result : "+lvrupdsts);
						  
						  if(lvrimagedelsts!=null && lvrimagedelsts.equalsIgnoreCase("1")){
							  String delrs= Commonutility.todelete("", pWebImagpath+lvrCompnyid+"/");
							  String delrs_mob= Commonutility.todelete("", pMobImgpath+lvrCompnyid+"/");
						  } else {
							  
						  }
						  Commonutility.toWriteImgWebAndMob(lvrcmpidint,lvrimagename,locimg_encdata,pWebImagpath,pMobImgpath,getText("thump.img.width"),getText("thump.img.height"), log, getClass());
						  log.logMessage("Step 5 : Image Write Finished", "info", CompanyImageupload.class);
						  Commonutility.toWriteConsole("Step 5 : Image Write Finished");
					 } catch(Exception imger){
						 System.out.println("Step -1 : Exception in  image write on company insert : "+imger);
						 log.logMessage("Step -1 : Exception in Image write on company insert", "error", CompanyUtility.class);
					 }finally{
						 
					 }	
					log.logMessage("Step 6 : Company Image upload [END]", "info", CompanyImageupload.class);
					  Commonutility.toWriteConsole("Step 6 : Company Image upload [END]");
					 serverResponse(ivrservicecode,"0","0000","success",locObjRspdataJson);
				}
				}else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI5001,"+getText("request.format.notmach")+"", "info", CompanyImageupload.class);
					serverResponse("SI5001","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);
				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI5001,"+getText("request.values.empty")+"", "info", CompanyImageupload.class);
				serverResponse("SI5001","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
			}	
		} catch(Exception e){
			Commonutility.toWriteConsole("Exception found CompanyImageupload.class execute() Method : "+e);			
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI5001, Sorry, an unhandled error occurred", "error", CompanyImageupload.class);
			serverResponse("SI5001","2","ER0002",getText("catch.error"),locObjRspdataJson);

		}finally{
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null; lvrcmpdaoobj = null;	 
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
