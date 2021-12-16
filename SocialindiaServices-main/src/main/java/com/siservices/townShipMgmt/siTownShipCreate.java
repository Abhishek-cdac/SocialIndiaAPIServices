package com.siservices.townShipMgmt;

import java.io.PrintWriter;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.CityMasterTblVO;
import com.pack.commonvo.CountryMasterTblVO;
import com.pack.commonvo.PostalCodeMasterTblVO;
import com.pack.commonvo.StateMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.common.CommonUtils;
import com.siservices.common.CommonUtilsServices;
import com.siservices.signup.persistense.signUpDao;
import com.siservices.signup.persistense.signUpDaoServices;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class siTownShipCreate extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	signUpDao signup = new signUpDaoServices();
	TownshipMstTbl townshipData = new TownshipMstTbl();
	townShipMgmtDao townShip=new townShipMgmtDaoServices();
	CountryMasterTblVO countryMst=new CountryMasterTblVO();
	StateMasterTblVO stateMST=new StateMasterTblVO();
	CityMasterTblVO cityMst=new CityMasterTblVO();
	PostalCodeMasterTblVO postalMst=new PostalCodeMasterTblVO();
	Log log=new Log();		
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = new JSONObject();// Response Data Json		
		JSONObject data=new JSONObject();		
		String ivrservicecode=null;
		 int uniqueVal;
		String occupation=null;
		String logoImageFileName=null,icoImageFileName=null,countryCode=null,stateCode=null,postalCode=null;		
		int noofmember;		
		String pincode=null,townshipcolor=null,townshiplogo=null,townshipico=null;
		String townshipname=null,noofsociety=null,noofflats=null,address=null,countrycode=null;
		String buildername=null,buildernameemail=null,isdcode=null,mobileno=null,landmark=null,imprintname=null;
		String statecode=null,citycode=null;
		byte conbytetoarr[]= new byte[1024];
		byte conbytetoarr1[]= new byte[1024];
		uamDao uam=new uamDaoServices();
		Integer ivrEntryByusrid = 0;
		CommonUtilsDao common=new CommonUtilsDao();		
		ResourceBundle rb=ResourceBundle.getBundle("applicationResources");
		Session pSession = null;
		Query locQryrst = null;
		GroupMasterTblVo locGrpMstrVOobj = null, locGRPMstrvo = null;
		try{	
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				pSession = HibernateUtil.getSession();
				ivrparams = EncDecrypt.decrypt(ivrparams);				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);								
					 locObjRecvdataJson=(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "data");					
					 ivrservicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");					
					 townshipname=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "townshipname");
					 noofsociety=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "noofsociety");
					 noofflats=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "noofflats");
					 address=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "address");
					 countrycode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "countrycode");
					 statecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "statecode");
					 citycode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "citycode");
					 pincode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "pincode");
					stateCode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "stateCode");
					//noofmember=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "noofmember");
					
					buildername=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "buildername");
					buildernameemail=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "buildernameemail");
					isdcode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "isdcode");
					mobileno=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "mobileno");
					landmark=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "landmark");
					imprintname=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "imprintname");
					
					townshipcolor=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "townshipcolor");
					townshiplogo=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "townshiplogo");
					townshipico=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "townshipico");
					logoImageFileName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "logoImageFileName");
					icoImageFileName=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "icoImageFileName");
					ivrEntryByusrid = (Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"userId");
					Integer groupcode=(Integer) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"usrTyp");
					if (ivrEntryByusrid != null) {
					} else {
						ivrEntryByusrid = 0;
					}					
					if (logoImageFileName != null && logoImageFileName != "") {
						logoImageFileName = logoImageFileName.replaceAll(" ", "_");
						townshipData.setTownshiplogoname(logoImageFileName);						
					}
					if (icoImageFileName != null && icoImageFileName != "") {	
						icoImageFileName = icoImageFileName.replaceAll(" ", "_");
						townshipData.setTownshipiconame(icoImageFileName);
					}
					int locGrpcode=0; String townshipUniId = null;
					String locSlqry="from GroupMasterTblVo where upper(groupName) = upper('"+getText("Grp.township")+"') or groupName=upper('"+getText("Grp.township")+"')";			 				
					locQryrst = pSession.createQuery(locSlqry);
					locGrpMstrVOobj = (GroupMasterTblVo) locQryrst.uniqueResult();
					if (locGrpMstrVOobj != null) {
						locGRPMstrvo = new GroupMasterTblVo();
						locGrpcode = locGrpMstrVOobj.getGroupCode();
						 townshipUniId=Commonutility.toGetKeyIDforTbl("TownshipMstTbl","max(townshipId)",Commonutility.toCheckNullEmpty(locGrpcode),"7");
					} else {
						uamDao lvrUamobj=new uamDaoServices();
					 	locGrpcode=lvrUamobj.createnewgroup_rtnId(getText("Grp.township"));
					 	 townshipUniId=Commonutility.toGetKeyIDforTbl("TownshipMstTbl","max(townshipId)",Commonutility.toCheckNullEmpty(locGrpcode),"7");
					 	lvrUamobj = null;
					}
					
					townshipData.setTownshipName(townshipname);	
					CommonUtils comutil=new CommonUtilsServices();
					Date date;
					date = comutil.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss");
					townshipData.setBuilderName(buildername);					
					townshipData.setEmailId(buildernameemail);
					townshipData.setIsdCode(isdcode);
					townshipData.setMobileNo(mobileno);
					townshipData.setLandMark(landmark);
					townshipData.setImprintName(imprintname);
					townshipData.setNoOfSociety(Integer.parseInt(noofsociety));
					townshipData.setNoOfFlats(Integer.parseInt(noofflats));
					townshipData.setTownshipUniqueId(townshipUniId);
					townshipData.setAddress(address);
					String activationKey=comutil.getRandomval("AZ_09", 25);
					townshipData.setEntryDatetime(date);
					townshipData.setEntryby(ivrEntryByusrid);
					townshipData.setActivationKey(activationKey);
					if(countrycode!=null && countrycode!=""){
					countryMst.setCountryId(Integer.parseInt(countrycode));
					townshipData.setCountryId(countryMst);
					}if(statecode!=null && statecode!=""){
					stateMST.setStateId(Integer.parseInt(statecode));
					townshipData.setStateId(stateMST);
					}if(citycode!=null && citycode!=""){
					cityMst.setCityId(Integer.parseInt(citycode));
					townshipData.setCityId(cityMst);
					}if(pincode!=null && pincode!=""){
					postalMst.setPstlId(Integer.parseInt(pincode));
//					townshipData.setPstlId(postalMst);
					townshipData.setPstlId(Integer.parseInt(pincode));
					}
					townshipData.setTownshipcolourcode(townshipcolor);
					townshipData.setStatus(1);
					uniqueVal=townShip.townShipInfoSave(townshipData);
					if (uniqueVal==-2) {
						locObjRspdataJson.put("servicecode", ivrservicecode);
						serverResponse(ivrservicecode,"02","R0062",mobiCommon.getMsg("R0062"),locObjRspdataJson);
					} else if(uniqueVal==-3) {
						locObjRspdataJson.put("servicecode", ivrservicecode);
						serverResponse(ivrservicecode,"03","R0063",mobiCommon.getMsg("R0063"),locObjRspdataJson);
					} else {
						locObjRspdataJson.put("townshipid", uniqueVal);
		        	    serverResponse(ivrservicecode,"00","R0061",mobiCommon.getMsg("R0061"),locObjRspdataJson);
					}
				} else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI0024, "+mobiCommon.getMsg("R0002"), "info", siTownShipCreate.class);
					serverResponse(ivrservicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
				}

			}
		}catch(Exception e){
			Commonutility.toWriteConsole("Service code : SI0024, Exception found in siTownShipCreate Method : "+e);
			log.logMessage("Service code : SI0024, Sorry, an unhandled error occurred", "info", siTownShipCreate.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(ivrservicecode,"01","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
		}finally{		
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null; locGrpMstrVOobj =null;
			if(pSession!=null){
				pSession.flush();pSession.clear();pSession.close();pSession = null;
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

	public String getIvrparams() {
		return ivrparams;
	}

	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}
	
	
}
