package com.socialindiaservices.issuemgmt;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.event.Eventutility;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.issuemgmt.persistence.IssueTblVO;
import com.socialindiaservices.issuemgmtDao.IssueDao;
import com.socialindiaservices.issuemgmtDao.IssuedaoServices;


public class IssueUtility {
	public static String toInsertIssue(JSONObject pDataJson){
		System.out.println("Tsest --------------IssueUtility ------------- ");
		String locFtrnStr=null,issuetxt=null;
		int locFunid=0;
		Log log=null;	
		IssueTblVO locissvoObj=null;
		CommonUtils locCommutillObj =null;
		
		IssueDao cmndao=null;
		try{	
			locissvoObj=new IssueTblVO();
			locCommutillObj = new CommonUtilsDao();
			cmndao=new IssuedaoServices();
			log=new Log();
			log.logMessage("Step 2 : Issue Insert Block.", "info", IssueUtility.class);
			issuetxt=(String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "issuetxt");
			locissvoObj.setIssueList(issuetxt);
			locissvoObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			locissvoObj.setStatus(1);
			int lofun = cmndao.saveIssue(locissvoObj);
			System.out.println("lofun ------------- "+lofun);
			 log.logMessage("Step 3 : IssueUtility Detail insert will start.", "info", IssueUtility.class);
			 System.out.println(locFunid+": id function");
			 log.logMessage("Step 4 : IssueUtility Detail insert End function Id : "+locFunid, "info", IssueUtility.class);
			 if(lofun>=0){
				 locFtrnStr="success!_!"+locFunid;
			 }else{
				 locFtrnStr="error!_!"+locFunid;
			 }
			 return locFtrnStr;
			 // -----------Function Insert end------------							
		}catch(Exception e){
			System.out.println("Exception found in IssueUtility.toInsrtfeedback : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", IssueUtility.class);
			locFunid=0;
			locFtrnStr="error!_!"+locFunid;
			return locFtrnStr;
		}finally{
			 pDataJson=null;log=null;locissvoObj=null;
				locCommutillObj =null;
				cmndao=null;
				locFunid=0;			
		}
				
	}
	public static String toupdateAction(JSONObject pDataJson){
		String issueid = null;
		String lvrfunmtrqry = null;
		String lvrfuntempqry = null;
		Log log = null;
		Common common = null;
		String jsonTextFinal=null;
		String flg=null;
		String issuetxt=null;
		boolean lvrfunmtr=false;
		try {
			log = new Log();
			common=new CommonDao();
			System.out.println("Step 1 : toupdateAction  block enter");
			log.logMessage("Step 1 : Select toupdateAction  block enter", "info", IssueUtility.class);
			issueid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "issueid");
			flg  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "flg");
			issuetxt  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "issuetxt");
			if(flg!=null && flg.equalsIgnoreCase("issuelistupdate")){
				lvrfuntempqry ="update IssueTblVO set issueList='"+issuetxt+"' where issueId ="+Integer.parseInt(issueid);
				lvrfunmtr=common.commonUpdate(lvrfuntempqry);
				jsonTextFinal="success!_!"+0;
			}else{
				lvrfuntempqry ="update IssueTblVO set status='"+Integer.parseInt(issuetxt)+"' where issueId ="+Integer.parseInt(issueid);
				lvrfunmtr=common.commonUpdate(lvrfuntempqry);
					jsonTextFinal="success!_!"+0;
			}
			
		}catch(Exception ex){
			System.out.println("Test ----- "+ex);	
			jsonTextFinal="error!_!"+1;	
			return jsonTextFinal;
		}finally{
			lvrfunmtrqry = null;lvrfuntempqry = null;log = null;
			common = null;lvrfunmtr=false;
		}
		
		return jsonTextFinal;
		
	}
	
}
