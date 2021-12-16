package com.siservices.committeeMgmt;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.social.utils.Log;

public class siCommitteeMgmt extends ActionSupport{
	
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
		String lsvSlQry = null;
	//	Session locObjsession = null;		
		String ivrservicecode=null,locvrEventSTS = null,locSrchdtblsrch = null;
		Common lvrCommdaosrobj = null;	
		try{		lvrCommdaosrobj = new CommonDao();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					int startrowfrom = 1, totalNorow = 10;
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");							
					String count1 = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "count1");
					String countF1=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "countF1");
					locvrEventSTS = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "status");
					locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "srchdtsrch");
					String sSoctyId=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "sSoctyId");
					String locStrRow = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startrow");
					String locMaxrow = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "totalrow");
					String selectfield = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "slectfield");
					String searchword = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "searchWord");
					String locVrSlQry="";
					int total = 0;
					int count;
					int countFilter;
					
					if(Commonutility.toCheckisNumeric(locStrRow)){
						 startrowfrom=Integer.parseInt(locStrRow);
					}
					if(Commonutility.toCheckisNumeric(locMaxrow)){
						totalNorow=Integer.parseInt(locMaxrow);
					}
				 	// set group code
					String grpStr = "";
					String logrpcode = null;
					logrpcode = lvrCommdaosrobj.getGroupcodeexistornew(getText("Grp.committee"));
				 if(logrpcode!= null && !logrpcode.equalsIgnoreCase("error")){
					 if(Commonutility.toCheckisNumeric(logrpcode)){
						 //locGrpmstvoobj.setGroupCode(Integer.parseInt(logrpcode));	
						 grpStr = " groupCode = "+Integer.parseInt(logrpcode);
					 }				
				 }else{	
					 grpStr = "1";
				 }												
				 	String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
					String glbSearch="",lvSlqry = null;
					String lvrOrdby = "order by entryDatetime desc";
				 if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
					 if(sSoctyId.equalsIgnoreCase("-1")) {	
					 glbSearch = " and (" + " emailId like ('%" + locTochenull + "%') or " 
								             + " mobileNo like ('%" + locTochenull + "%') or "
								             + " firstName like ('%" + locTochenull + "%') or "
								             + " roleId.roleName like ('%" + locTochenull + "%') or "
								             + " societyId.townshipId.townshipName like ('%" + locTochenull + "%') or "
								             + " societyId.societyName like ('%" + locTochenull + "%') "
								             + ")";
				
						/*if(sSoctyId.equalsIgnoreCase("-1")){
							lvSlqry = "from UserMasterTblVo  where statusFlag=" + locvrEventSTS + "" + glbSearch+" and "+grpStr+"  "+glbSearch +" "+lvrOrdby;
						} else {
							lvSlqry = "from UserMasterTblVo  where statusFlag=" + locvrEventSTS + " and societyId.societyId=" + sSoctyId +" " + glbSearch+" and "+grpStr+"   "+glbSearch +" "+lvrOrdby;
						}*/
				 } else {
					 glbSearch = " and (" + " emailId like ('%" + locTochenull + "%') or " 
				             + " mobileNo like ('%" + locTochenull + "%') or "
				             + " firstName like ('%" + locTochenull + "%') or "
				             + " roleId.roleName like ('%" + locTochenull + "%') or "
				             + " societyId.townshipId.townshipName like ('%" + locTochenull + "%') or "
				             + " societyId.societyName like ('%" + locTochenull + "%') "
				             + ")and societyId.societyId ="+sSoctyId;					
								/*if(sSoctyId.equalsIgnoreCase("-1")){
									lvSlqry = "from UserMasterTblVo  where statusFlag=" + locvrEventSTS +" and "+grpStr+"   "+glbSearch +" "+lvrOrdby;	
								} else {
									lvSlqry = "from UserMasterTblVo  where statusFlag=" + locvrEventSTS +" and societyId.societyId="+sSoctyId+" and "+grpStr+"   "+glbSearch +" "+lvrOrdby;	
								}*/
					}
				 }else{
						if(searchword!=null && !searchword.equalsIgnoreCase("null") && !searchword.equalsIgnoreCase("")){// Top search box
							if(!sSoctyId.equalsIgnoreCase("-1")){
								glbSearch = " and ";
								if(selectfield!=null && selectfield.equalsIgnoreCase("1")){														 		 
									glbSearch = " and (" + " mobileNo like ('%" + searchword + "%')"
											 + ") and societyId.societyId ="+sSoctyId;	
								}else if(selectfield!=null && selectfield.equalsIgnoreCase("2")){
									glbSearch = "  and(" + "firstName like ('%" + searchword + "%')"
											 + ") and societyId.societyId ="+sSoctyId;	
								}
								else if(selectfield!=null && selectfield.equalsIgnoreCase("3")){
									glbSearch = " and (" + " societyId.townshipId.townshipName like ('%" + searchword + "%')"
											 + ") and societyId.societyId ="+sSoctyId;	
									
								}
								else if(selectfield!=null && selectfield.equalsIgnoreCase("4")){
									glbSearch = " and (" + " societyId.societyName like ('%" + searchword + "%') "
											 + ") and societyId.societyId ="+sSoctyId;	
								}
								else{
									glbSearch = " and (" + " emailId like ('%" + searchword + "%') or " 
								             + " mobileNo like ('%" + searchword + "%') or "
								             + " firstName like ('%" + searchword + "%') or "
								             + " roleId.roleName like ('%" + searchword + "%') or "
								             + " societyId.townshipId.townshipName like ('%" + searchword + "%') or "
								             + " societyId.societyName like ('%" + searchword + "%') "
								             + ") and societyId.societyId ="+sSoctyId;	
								}
								
							}else{
								glbSearch = " and ";
								if(selectfield!=null && selectfield.equalsIgnoreCase("1")){														 		 
									glbSearch = " and (" + " mobileNo like ('%" + searchword + "%') ) "; 
								}else if(selectfield!=null && selectfield.equalsIgnoreCase("2")){
									glbSearch = "  and(" + "firstName like ('%" + searchword + "%')) " ; 
								}
								else if(selectfield!=null && selectfield.equalsIgnoreCase("3")){
									glbSearch = " and (" + " societyId.townshipId.townshipName like ('%" + searchword + "%') )" ;
								}
								else if(selectfield!=null && selectfield.equalsIgnoreCase("4")){
									glbSearch = " and (" + " societyId.societyName like ('%" + searchword + "%') )" ;
									
								}else{
									 glbSearch = " and (" + " emailId like ('%" + searchword + "%') or " 
								             + " mobileNo like ('%" + searchword + "%') or "
								             + " firstName like ('%" + searchword + "%') or "
								             + " roleId.roleName like ('%" + searchword + "%') or "
								             + " societyId.townshipId.townshipName like ('%" + searchword + "%') or "
								             + " societyId.societyName like ('%" + searchword + "%') "
								             + ")";
								}
							}
							
						}else{
							if(!sSoctyId.equalsIgnoreCase("-1")){
								glbSearch = " and societyId.societyId = "+sSoctyId;
							}else{
								glbSearch = "";
							}
							
						}										
					
				 }
				 lvSlqry = "from UserMasterTblVo  where statusFlag=" + locvrEventSTS + "" + glbSearch+" and "+grpStr+"  "+lvrOrdby;
				 System.out.println("Step 1 : Select Query ============= "+lvSlqry);	
				 if (count1.equalsIgnoreCase("yes") && countF1.equalsIgnoreCase("yes")) {
						locVrSlQry="SELECT count(userId) " + lvSlqry;	
						count = uam.getInitTotal(locVrSlQry);
					 	countFilter = uam.getTotalFilter(locVrSlQry);
					}else{
						count=0;
						countFilter = 0;
					}
				 
				 /*else if(srchField.equalsIgnoreCase("4")){
					glbSearch = " and (" + " ivrBnENDDATE like ('%" + srchFieldval + "%'))" ;
					lvSlqry = "from EventTblVO  where ivrBnEVENTSTATUS=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;
				}*//*else if(searchWord==null && searchWord==null){
					lvSlqry = "from  MvpMaterialTbl  where status=" + locvrEventSTS + " " +locOrderby;	
				}*/
				 System.out.println("Step 2 : Select Query "+lvSlqry);				
				// userList = uam.getUserDetailList_like(lvSlqry);
				 userList = uam.getUserDetailList_like_limt(lvSlqry, startrowfrom, totalNorow);
				 JSONArray jArray =new JSONArray();
				 UserMasterTblVo userMaster;
				// Commonutility.toWriteConsole("userList : "+userList.size());
				for (Iterator<UserMasterTblVo> it = userList.iterator(); it.hasNext();) {
						userMaster = it.next();
						JSONObject finalJson = new JSONObject();					
						finalJson.put("userId", userMaster.getUserId());
						if (userMaster.getEmailId() != null) {
							finalJson.put("emailId", userMaster.getEmailId());
						} else {
							finalJson.put("emailId", "");
						}
						finalJson.put("mobileNo", userMaster.getMobileNo());
						
						if (userMaster.getFirstName() == null) {
							finalJson.put("firstname", "");
						} else {
							finalJson.put("firstname", userMaster.getFirstName());
						}
						
						if (userMaster.getLastName() == null) {
							finalJson.put("lastname", "");
						} else {
							finalJson.put("lastname", userMaster.getLastName());
						}
						
						if (userMaster.getRoleId() == null) {
							finalJson.put("committeerole", "");
						} else {
							finalJson.put("committeerole", userMaster.getRoleId().getRoleName());
						}
						if(userMaster.getSocietyId()!=null){
							finalJson.put("townshipname", userMaster.getSocietyId().getTownshipId().getTownshipName());
							finalJson.put("townshipid", Commonutility.toCheckNullEmpty(userMaster.getSocietyId().getTownshipId().getTownshipId()));
						}else{
							finalJson.put("townshipname", "");
							finalJson.put("townshipid", "");
						}
					if(userMaster.getSocietyId()!=null){
						finalJson.put("societyname", userMaster.getSocietyId().getSocietyName());
						finalJson.put("societyid", Commonutility.toCheckNullEmpty(userMaster.getSocietyId().getSocietyId()));
					}else{
						finalJson.put("societyname", "");
						finalJson.put("societyid", "");
					}
					finalJson.put("accesschannel", userMaster.getAccessChannel());																				
					jArray.put(finalJson);				
					}				
					jsonFinalObj.put("InitCount", count);
					jsonFinalObj.put("countAfterFilter", countFilter);
					jsonFinalObj.put("userMgmt", jArray);
					serverResponse(ivrservicecode, "0000", "00", "sucess", jsonFinalObj);												
				}else{
					//Response call
					serverResponse(ivrservicecode,"01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}	
			}else{
				//Response call
				serverResponse(ivrservicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}	
		}catch(Exception e){
			System.out.println("Exception found .class execute() Method : "+e);
			log.logMessage("Service code : ivrservicecode, Sorry, an unhandled error occurred", "info", siCommitteeMgmt.class);
			try {
				serverResponse(ivrservicecode,"02","E0002","Sorry, an unhandled error occurred",locObjRspdataJson);
			} catch (Exception e1) {}
		}finally{
			//if(locObjsession!=null){locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
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
