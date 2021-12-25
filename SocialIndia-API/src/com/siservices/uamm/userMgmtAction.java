package com.siservices.uamm;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.FlatMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.social.utils.Log;

public class userMgmtAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<UserMasterTblVo> userList = new ArrayList<UserMasterTblVo>();
	uamDao uam=new uamDaoServices();
	JSONObject jsonFinalObj=new JSONObject();

	Log log=new Log();
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		SocietyMstTbl socName=new SocietyMstTbl();
		String lsvSlQry = null;		
		String ivrservicecode=null;
		  String globalsearch = "";
		try{
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");				
				String society = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "society");
				/*String society = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "socitid");*/
				String slectfield = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "slectfield");
				String searchname = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "searchname");	
				String searchflg = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "searchflg");
				String count1 = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "count1");
				String countF1=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "countF1");
				String locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startrow");
				String locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "totalrow");
				String locpagefor = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "pagefor");
				String locSrchdtblsrch =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "srchdtsrch");
				String locTochenull = Commonutility.toCheckNullEmpty(locSrchdtblsrch);
				String locVrSlQry="";
				int startrowfrom = 0 , totalNorow = 0;
				if (Commonutility.toCheckisNumeric(locStrRow)) {
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if (Commonutility.toCheckisNumeric(locMaxrow)) {
					totalNorow=Integer.parseInt(locMaxrow);
				}
				String locOrderby =" order by entryDatetime desc";		 
				 if (searchflg!=null && !searchflg.equalsIgnoreCase("")) {
					  if(society!=null && society.length()>0) { 
						  if(!society.equalsIgnoreCase("")&& searchname.equalsIgnoreCase("")){
							  globalsearch = "("+"societyId =('"+society+"'))";		
						  } else if(!society.equalsIgnoreCase("") && slectfield.equalsIgnoreCase("1")){
							  globalsearch = "societyId ='"+society+"' and (fname like ('%" + searchname + "%') or "
				        					+ "emailId like ('%" + searchname + "%') or "
				        					+ "mobileNo like ('%" + searchname + "%') or "
				        					+ "groupCode.groupName like ('%" + searchname + "%'))";
						  } else if(!society.equalsIgnoreCase("") && slectfield.equalsIgnoreCase("2")){
								  globalsearch = "societyId ='"+society+"' and (emailId like ('%" + searchname + "%') )"; 
						  } else if(!society.equalsIgnoreCase("") && slectfield.equalsIgnoreCase("3")){
							  globalsearch = "societyId ='"+society+"' and (mobileNo like ('%" + searchname + "%') )";	
						  } else if(!society.equalsIgnoreCase("") && slectfield.equalsIgnoreCase("4")){
							  globalsearch = "societyId ='"+society+"' and (fname like ('%" + searchname + "%') )";	
						  }else if(!society.equalsIgnoreCase("") && slectfield.equalsIgnoreCase("5")){
							  globalsearch = "societyId ='"+society+"' and ( groupCode.groupName like ('%" + searchname + "%') )";	
						  }else{
							  globalsearch ="(" + "userName like ('%" + searchname + "%') or "
			        					+ "emailId like ('%" + searchname + "%') or "
			        					+ "mobileNo like ('%" + searchname + "%') or "
			        					+ "groupCode.groupName like ('%" + searchname + "%') )";  
						  }
					 }else {						
						 	if(slectfield!=null && slectfield.length()>0){ 
				        	   if(slectfield.equalsIgnoreCase("1")){       
				        		globalsearch ="(" + "fname like ('%" + searchname + "%') or "
				        					+ "emailId like ('%" + searchname + "%') or "
				        					+ "mobileNo like ('%" + searchname + "%') or "
				        					+ "groupCode.groupName like ('%" + searchname + "%') )";
				              }else if(slectfield.equalsIgnoreCase("2")){
				            	  globalsearch = "("+"emailId like('%"+searchname+"%') )";				            	 
				              }else if(slectfield.equalsIgnoreCase("3")){
				            	  globalsearch = "("+"mobileNo like('%"+searchname+"%') )";
				              }else if(slectfield.equalsIgnoreCase("4")){
				            	  globalsearch = "("+"fname like('%"+searchname+"%') )";				            	 
				              }else if(slectfield.equalsIgnoreCase("5")){
				            	  globalsearch = "("+"groupCode.groupName like('%"+searchname+"%') )"; 
				              }
				        	  }else{
				        		  globalsearch ="(" + "userName like ('%" + searchname + "%') or "
				        					+ "emailId like ('%" + searchname + "%') or "
				        					+ "mobileNo like ('%" + searchname + "%') or "
				        					+ "groupCode.groupName like ('%" + searchname + "%') )";
				        	  }
					 }
					  String qry="From UserMasterTblVo where statusFlag='1' and "+globalsearch+" "+locOrderby+"";
					  locVrSlQry="SELECT count(userId) FROM UserMasterTblVo where statusFlag='1' and "+globalsearch+"";
					  userList = uam.getUserDetailList_like_limt(qry, startrowfrom, totalNorow); 
				 }else{
					 if (!locTochenull.equalsIgnoreCase("") && !locTochenull.equalsIgnoreCase("null")) { // datatable search
						 globalsearch ="(" + "fname like ('%" + locTochenull + "%') or "
		        					+ "emailId like ('%" + locTochenull + "%') or "
		        					+ "mobileNo like ('%" + locTochenull + "%') or "
		        					+ "groupCode.groupName like ('%" + locTochenull + "%') )";
						  String qry="From UserMasterTblVo where statusFlag='1' and "+globalsearch+" "+locOrderby+"";
						  locVrSlQry="SELECT count(userId) FROM UserMasterTblVo where statusFlag='1' and "+globalsearch;
						  userList = uam.getUserDetailList_like_limt(qry, startrowfrom, totalNorow); 
						 
					 } else { //  default load
						 if(!Commonutility.checkempty(locpagefor)){
							  String qry="From UserMasterTblVo where statusFlag='1' "+locOrderby+"";
							  locVrSlQry="SELECT count(userId) FROM UserMasterTblVo where statusFlag='1'";
							  userList = uam.getUserDetailList_like_limt(qry, startrowfrom, totalNorow); 
						 } else{
							 if(society!=null && !society.equalsIgnoreCase("-1") && !society.equalsIgnoreCase("")){
							 String qry="From UserMasterTblVo where societyId ="+society+" and statusFlag='1' "+locOrderby+"";
							  locVrSlQry="SELECT count(userId) FROM UserMasterTblVo where societyId ="+society+" and statusFlag='1'";
							  userList = uam.getUserDetailList_like(qry); 
							 }else{
								 String qry="From UserMasterTblVo where statusFlag='1' "+locOrderby+"";
								  locVrSlQry="SELECT count(userId) FROM UserMasterTblVo where statusFlag='1'";
								  userList = uam.getUserDetailList_like(qry);  
							 }
						 }
					 }
				 }
				 	int count;
					int countFilter;
					if (count1.equalsIgnoreCase("yes") && countF1.equalsIgnoreCase("yes")) {						
					} else {
						count=0;
						countFilter = 0;
					}				
					 count = uam.getInitTotal(locVrSlQry);
					 countFilter = uam.getTotalFilter(locVrSlQry);		
				 
					 JSONArray jArray =new JSONArray();
					 UserMasterTblVo userMaster;
					 FlatMasterTblVO flatDetailObj=new FlatMasterTblVO();
					 for (Iterator<UserMasterTblVo> it = userList.iterator(); it.hasNext();) {
					userMaster = it.next();
					flatDetailObj=new FlatMasterTblVO();
					flatDetailObj=uam.getOneFlatDetail(userMaster);
					
					JSONObject finalJson = new JSONObject();
					if(userMaster.getUserName()!=null){
						finalJson.put("userName", userMaster.getUserName());
					}else{
						finalJson.put("userName", "");
					}
					finalJson.put("mobileNo", userMaster.getMobileNo());
					if(userMaster.getEmailId()!=null){
							finalJson.put("emailId", userMaster.getEmailId());
					}else{
						finalJson.put("emailId", "");
					}
					finalJson.put("userId", userMaster.getUserId());	
					if(userMaster.getFirstName()!=null){
					finalJson.put("fname", userMaster.getFirstName());	
					}else{
						finalJson.put("fname", "");		
					}
					if(userMaster.getLastName()!=null){
						finalJson.put("lastName", userMaster.getLastName());	
						}else{
							finalJson.put("lastName", "");		
						}
					
					finalJson.put("accessMedia", userMaster.getAccessChannel());	
					
					if(userMaster.getGroupCode()!=null){
						finalJson.put("groupName",  Commonutility.toCheckNullEmpty(userMaster.getGroupCode().getGroupName()));
					}else{
						finalJson.put("groupName", "");
					}
					
					if(userMaster.getGroupCode()!=null){
						finalJson.put("groupidstring", Commonutility.toCheckNullEmpty(userMaster.getGroupCode().getGroupCode()));
					}else{
						finalJson.put("groupidstring", "");
					}
					if(flatDetailObj!=null){
						finalJson.put("flatNo",  Commonutility.toCheckNullEmpty(flatDetailObj.getFlatno()));
					}else{
						finalJson.put("flatNo", "");
					}
					
					if(userMaster.getImageNameMobile()!=null){
						finalJson.put("imageNameMobile", Commonutility.toCheckNullEmpty(userMaster.getImageNameMobile()));
					}else{
						finalJson.put("imageNameMobile", "");
					}
					
					if (userMaster.getSocietyId()!=null) {
						finalJson.put("societyname", Commonutility.toCheckNullEmpty(userMaster.getSocietyId().getSocietyName()));
						if (userMaster.getSocietyId().getTownshipId()!=null) {
							finalJson.put("townshipname", Commonutility.toCheckNullEmpty(userMaster.getSocietyId().getTownshipId().getTownshipName()));
						} else {
							finalJson.put("townshipname", "");
						}
						
					} else {
						finalJson.put("societyname", "");
						finalJson.put("townshipname", "");
					}									
					jArray.put(finalJson);
					
					}
					jsonFinalObj.put("InitCount", count);
					jsonFinalObj.put("countAfterFilter", countFilter);
					jsonFinalObj.put("userMgmt", jArray);
					serverResponse(ivrservicecode, "0000","0", "sucess", jsonFinalObj);																	
				}else{
					//Response call
					serverResponse(ivrservicecode,"1","E0001","Request values are not correct format",locObjRspdataJson);
				}	
			}else{
				//Response call
				serverResponse(ivrservicecode,"1","E0001","Request values are empty",locObjRspdataJson);

			}
			System.out.println("---------Completed services Block--------");
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			log.logMessage("Service : userMgmtAction error occurred : " + e, "error",userMgmtAction.class);
			try {
				serverResponse(ivrservicecode,"2","E0002","Sorry, an unhandled error occurred",locObjRspdataJson);
			} catch (Exception e1) {}
		}finally{
			//if(locObjsession!=null){locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null; locObjRspdataJson=null;	
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
			log.logMessage("Service : userMgmtAction error occurred : " + ex, "error",userMgmtAction.class);
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
