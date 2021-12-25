package com.siservices.societyMgmt;



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
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class siSocietymgmt extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<SocietyMstTbl> societyList = new ArrayList<SocietyMstTbl>();
	societyMgmtDao society=new societyMgmtDaoServices();
	JSONObject jsonFinalObj=new JSONObject();

	Log log=new Log();
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lvSlqry = null,locSrchdtblsrch = null;
		Session pSession = null;				
		String ivrservicecode=null,locvrEventSTS = null, locStrRow =null, locMaxrow=null;
		int startrowfrom=1, totalNorow=10;
		try{
			pSession = HibernateUtil.getSession();;	
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				
				locvrEventSTS = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "societystatus");
				locSrchdtblsrch =(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "srchdtsrch");
				String count1 = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "count1");
				String countF1 = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "countF1");
				int sSoctyId = (int) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "sSoctyId");	
				locStrRow = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "startrow");
				locMaxrow = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "totalrow");
				String locTochenull = Commonutility.toCheckNullEmpty(locSrchdtblsrch);
				
				if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}												
				String locVrSlQry="";
				int total = 0;
				int count;
				int countFilter,lvrsoctysts = 1;
				if(locvrEventSTS!=null && Commonutility.toCheckisNumeric(locvrEventSTS)){
					lvrsoctysts = Integer.parseInt(locvrEventSTS);
				}
				if(count1.equalsIgnoreCase("yes") && countF1.equalsIgnoreCase("yes")){
					if (!locTochenull.equalsIgnoreCase("") && !locTochenull.equalsIgnoreCase("nul")) {
						String srchcntqry = " and (" + " townshipId.townshipName like ('%" + locTochenull + "%') or " 
					             + " societyName like ('%" + locTochenull + "%') or "
					             + " townshipId.stateId.stateName like ('%" + locTochenull + "%') or "
					             + " townshipId.countryId.countryName like ('%" + locTochenull + "%') or "
					             + " townshipId.cityId.cityName like ('%" + locTochenull + "%') or "
					             + " townshipId.pstlId.pstlCode like ('%" + locTochenull + "%')  "
					             + ")";
						locVrSlQry="SELECT count(societyId) FROM SocietyMstTbl where statusFlag = "+lvrsoctysts+" "+srchcntqry;
					} else {
						locVrSlQry="SELECT count(societyId) FROM SocietyMstTbl where statusFlag = "+lvrsoctysts+"";
					}
										
					
					total = society.gettotalcount(locVrSlQry);					
				} else {
					count=0;
					countFilter = 0;
				}												
				count=society.getInitTotal(locVrSlQry);
				
				
				String glbSearch=null;				
				String locOrderby =" order by entryDatetime desc";
				if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
						glbSearch = " and (" + " townshipId.townshipName like ('%" + locTochenull + "%') or " 
							             + " societyName like ('%" + locTochenull + "%') or "
							             + " townshipId.stateId.stateName like ('%" + locTochenull + "%') or "
							             + " townshipId.countryId.countryName like ('%" + locTochenull + "%') or "
							             + " townshipId.cityId.cityName like ('%" + locTochenull + "%') or "
							             + " townshipId.pstlId.pstlCode like ('%" + locTochenull + "%')  "
							             + ")";
					
					lvSlqry = "from SocietyMstTbl  where statusFlag=" + lvrsoctysts + "" + glbSearch+" "+locOrderby;	
				}else {										
					if(sSoctyId==-1){
						lvSlqry = "from SocietyMstTbl  where statusFlag=" + lvrsoctysts +" "+locOrderby;	
					}else{
//						lvSlqry = "from SocietyMstTbl  where statusFlag=" + lvrsoctysts +" and societyId = "+sSoctyId+" "+locOrderby;
						lvSlqry = "from SocietyMstTbl  where statusFlag=" + lvrsoctysts +" "+locOrderby;
					}
					
				}
				 	System.out.println("Step 3 : society details Query : "+lvSlqry);
				 	countFilter=society.getTotalFilter(locVrSlQry);
					societyList = society.getsocietyDetailList(lvSlqry,startrowfrom,totalNorow);		
					JSONArray jArray = new JSONArray();
					SocietyMstTbl societyObj;
					int locGrpcode = 0;
						
					String locSlqry = null;
					Query locQryrst = null;
					GroupMasterTblVo locGrpMstrVOobj = null, locGRPMstrvo = null;
					locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+getText("Grp.society")+"') or groupName=upper('"+getText("Grp.society")+"')";				
					locQryrst=pSession.createQuery(locSlqry);			
					locGrpMstrVOobj=(GroupMasterTblVo) locQryrst.uniqueResult();
					if(locGrpMstrVOobj!=null){
						locGRPMstrvo=new GroupMasterTblVo();
						locGrpcode=locGrpMstrVOobj.getGroupCode();									 
					}else{// new group create
						uamDao uam=new uamDaoServices();
						locGrpcode=uam.createnewgroup_rtnId(getText("Grp.society"));					 		 				
					}	
				for (Iterator<SocietyMstTbl> it = societyList.iterator(); it.hasNext();) {
					societyObj = it.next();
					JSONObject finalJson = new JSONObject();
					finalJson.put("societyid", societyObj.getSocietyId());
					finalJson.put("societyuniqueid", Commonutility.toCheckNullEmpty(societyObj.getSocietyUniqyeId()));
					if(societyObj.getTownshipId().getTownshipName()==null){
						finalJson.put("townshipname", "");
					}else{
						finalJson.put("townshipname", societyObj.getTownshipId().getTownshipName());
					}
					finalJson.put("townshipid", Commonutility.toCheckNullEmpty(societyObj.getTownshipId().getTownshipId()));
					if(societyObj.getSocietyName()==null){
						finalJson.put("societyname", "");
					}else{
						finalJson.put("societyname", societyObj.getSocietyName());
					}
					if(societyObj.getTownshipId().getActivationKey()==null){
					finalJson.put("activationkey", "");
					}else{
						finalJson.put("activationkey", societyObj.getActivationKey());
					}
					if(societyObj.getTownshipId().getCountryId().getCountryName()==null){
					finalJson.put("countryname", "");
					}else{
					finalJson.put("countryname",societyObj.getTownshipId().getCountryId().getCountryName());
					}
					if(societyObj.getTownshipId().getStateId().getStateName()==null){
					finalJson.put("statename", "");
					}else{
						finalJson.put("statename", societyObj.getTownshipId().getStateId().getStateName());
					}
					if(societyObj.getTownshipId().getCityId().getCityName()==null){
					finalJson.put("cityname", "");
					}else{
						finalJson.put("cityname", societyObj.getTownshipId().getCityId().getCityName());
					}
					if(locGrpcode!=0){
						finalJson.put("groupcode", locGrpcode);
					}else{
						finalJson.put("groupcode", "");
					}
					if(societyObj.getSocietyId()!=null){
						String email=society.getsocietyemailid(societyObj.getSocietyId(),locGrpcode);
						if(email!=null && !email.equalsIgnoreCase("") && email!=""){
							finalJson.put("emailid", email);
						}else{
							finalJson.put("emailid", "");
						}
					}else{
						finalJson.put("emailid", "");
					}
					jArray.put(finalJson);
					
					}
					jsonFinalObj.put("InitCount", count);
					jsonFinalObj.put("countAfterFilter", countFilter);
					jsonFinalObj.put("societyMgmt", jArray);
				serverResponse(ivrservicecode, "0000","0", "sucess", jsonFinalObj);								
					//Response call
					serverResponse(ivrservicecode,"0","0000","Succes",locObjRspdataJson);
				}else{
					//Response call
					serverResponse(ivrservicecode,"1","E0001","Request values are not correct format",locObjRspdataJson);
				}	
			}else{
				//Response call
				serverResponse(ivrservicecode,"1","E0001","Request values are empty",locObjRspdataJson);

			}	
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Exception found .class execute() Method : "+e);
			log.logMessage("Service code : ivrservicecode, Sorry, an unhandled error occurred", "info", siSocietymgmt.class);
			try {
				serverResponse(ivrservicecode,"2","E0002","Sorry, an unhandled error occurred",locObjRspdataJson);
			} catch (Exception e1) {}
		}finally{			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	
			if(pSession!=null){pSession.flush();pSession.clear();pSession.close();pSession=null;}
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

	public List<SocietyMstTbl> getSocietyList() {
		return societyList;
	}

	public void setSocietyList(List<SocietyMstTbl> societyList) {
		this.societyList = societyList;
	}
	
	

}
