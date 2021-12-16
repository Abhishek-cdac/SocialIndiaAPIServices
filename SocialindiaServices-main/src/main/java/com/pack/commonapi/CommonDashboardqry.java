package com.pack.commonapi;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.complaintsvo.persistence.ComplaintsDao;
import com.pack.complaintsvo.persistence.ComplaintsDaoservice;
import com.pack.laborvo.persistence.LaborDaoservice;
import com.pack.merchantCategorylistvo.persistance.merchantCategoryDao;
import com.pack.merchantCategorylistvo.persistance.merchantCategoryDaoservice;
import com.pack.utilitypkg.Commonutility;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.failedSignon.persistense.FailsignDao;
import com.social.failedSignon.persistense.FailsignServices;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class CommonDashboardqry extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ivrparams;
	uamDao uam=null;
	JSONObject jsonFinalObj=new JSONObject();
	Log log=new Log();	
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		String ivrservicecode=null;String locVrSlQry="", lvrnoappdownldSlQry = "", lvrappusgSlQry = "" ;int count=0, noofappdownld = 0, lvrAppusecnt = 0;
		String lvrcmpltcountqry = null;int complaintcount=0;
		String locvrLBR_cntQry=null;int lbrCount=0;
		String locvrMCat_cntQry=null;int mctcount=0;
		String failedsign=null;int failedsigncount=0;
		JSONObject locInrJSONObj=null;
		ComplaintsDao lvrEvntDobj = null;LaborDaoservice lbrDaoObj=null;merchantCategoryDao IdcardDaoObj=null;FailsignDao fail=null;
		try{
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				 ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				if(ivrservicecode!="" && ivrservicecode!=null && !ivrservicecode.equalsIgnoreCase("")){
					uam=new uamDaoServices();
					locVrSlQry="SELECT count(userId) FROM UserMasterTblVo where statusFlag=1";	
					count=uam.getInitTotal(locVrSlQry);
					
					lvrnoappdownldSlQry="SELECT count(userId) FROM UserMasterTblVo where statusFlag=1 and accessChannel in (2,3) and emailVerifyFlag =1 and groupCode in (5,6)";	
					noofappdownld=uam.getInitTotal(lvrnoappdownldSlQry);
					
					lvrappusgSlQry="SELECT count(userId) FROM UserMasterTblVo where statusFlag=1 and accessChannel in (2,3) and emailVerifyFlag =1 and groupCode in (5,6)";	
					lvrAppusecnt=uam.getInitTotal(lvrnoappdownldSlQry);
					
					lvrcmpltcountqry = "select count(*) from ComplaintsTblVO  where status=1";
					lvrEvntDobj = new ComplaintsDaoservice();
					complaintcount = lvrEvntDobj.getInitTotal(lvrcmpltcountqry);
					
					locvrLBR_cntQry="select count(ivrBnLBR_ID) from LaborDetailsTblVO where ivrBnLBR_STS=1";
					lbrDaoObj=new LaborDaoservice();
					lbrCount=lbrDaoObj.getInitTotal(locvrLBR_cntQry);
					
					locvrMCat_cntQry="select count(mrchntId) from MerchantTblVO where mrchntActSts=1";
					IdcardDaoObj=new merchantCategoryDaoservice();
					mctcount=IdcardDaoObj.getInitTotal(locvrMCat_cntQry);
					
					failedsign="select count(id) from FailedSignOnTbl where status=1";
					fail=new FailsignServices();
					failedsigncount= fail.getInitTotal(failedsign);
								
						long MEGABYTE = 1024L * 1024L;
						// Get the Java runtime
						Runtime runtime = Runtime.getRuntime();
						// Run the garbage collector
						// runtime.gc();
						// Calculate the used memory
			            long memory = runtime.totalMemory() - runtime.freeMemory();
			            Commonutility.toWriteConsole("Runtime memory is runtime.totalMemory() : " + runtime.totalMemory()/MEGABYTE);
			            Commonutility.toWriteConsole("Free memory is runtime.freeMemory() : " + runtime.freeMemory()/MEGABYTE);
			            Commonutility.toWriteConsole("Used memory is bytes: " + memory);
			            Commonutility.toWriteConsole("Used memory is megabytes: "  + memory/MEGABYTE);
			        
					
					
					
					
					locInrJSONObj=new JSONObject();
					locInrJSONObj.put("activeuser", count);
					locInrJSONObj.put("usercomplaints", complaintcount);
					locInrJSONObj.put("lbrCount", lbrCount);
					locInrJSONObj.put("mctcount", mctcount);
					locInrJSONObj.put("noappDownload", noofappdownld);
					locInrJSONObj.put("failedlogon", failedsigncount);
					locInrJSONObj.put("appuse", lvrAppusecnt);
					locInrJSONObj.put("statusHealth", memory/MEGABYTE);
					serverResponse(ivrservicecode, "0000","0", "sucess", locInrJSONObj);		
				}
				
				}
			}
		}catch(Exception ex){
			Commonutility.toWriteConsole("Exception found .class execute() Method : "+ex);
			log.logMessage("Service : userMgmtAction error occurred : " + ex, "error",CommonDashboardqry.class);
			try {
				serverResponse(ivrservicecode,"2","E0002","Sorry, an unhandled error occurred",locObjRecvJson);
		}catch (Exception e1) {}
	}finally{
		locObjRecvJson = null;	    
		ivrservicecode=null;locVrSlQry="";count=0;
		lvrcmpltcountqry = null;complaintcount=0;
		locvrLBR_cntQry=null;lbrCount=0;
		locvrMCat_cntQry=null;mctcount=0;
		locInrJSONObj=null;
		failedsign=null;failedsigncount=0;
		lvrEvntDobj = null;lbrDaoObj=null;IdcardDaoObj=null;fail=null;
	}
		return SUCCESS;
	}
	private void serverResponse(String serviceCode, String statusCode,
			String respCode, String message, JSONObject dataJson)
			throws Exception {
		PrintWriter out;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response;
		response = ServletActionContext.getResponse();
		out = response.getWriter();
		try {
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
			out = response.getWriter();
			log.logMessage("Service : userMgmtAction error occurred : " + ex, "error",CommonDashboardqry.class);
			out.print("{\"servicecode\":\"" + serviceCode + "\",");
			out.print("{\"statuscode\":\"2\",");
			out.print("{\"respcode\":\"E0002\",");
			out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
			out.print("{\"data\":\"{}\"}");
			out.close();
			ex.printStackTrace();
		}
	}

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}
	
	

}
