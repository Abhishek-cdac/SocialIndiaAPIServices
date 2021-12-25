package com.siservices.societyMgmt;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;

public class siSocietyDeleteAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ivrparams;
	List<TownshipMstTbl> townShipList = new ArrayList<TownshipMstTbl>();
	TownshipMstTbl townShipMst =new TownshipMstTbl();
	SocietyMstTbl societyMst=new SocietyMstTbl();
	MvpSocietyAccTbl societyAccData=new MvpSocietyAccTbl();
	societyMgmtDao society=new societyMgmtDaoServices();
	JSONObject jsonFinalObj=new JSONObject();
	boolean result=false;
	int uniqueVal;

	Log log=new Log();
	public String execute(){
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		Session pSession = null;
		String ivrservicecode = null;
		try{
			pSession = HibernateUtil.getSession();;
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					
					String societyid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "uniqSocietyId");
					
					int result = society.delete(societyid);
					
					locObjRspdataJson=new JSONObject();
					locObjRspdataJson.put("resultFlag", result);
					
		        	serverResponse(ivrservicecode,"00","R0175",mobiCommon.getMsg("R0175"),locObjRspdataJson);
				
				}else{
					//Response call
					serverResponse(ivrservicecode,"01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson);
				}	
			}else{
				//Response call
				serverResponse(ivrservicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);

			}	
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Exception found siSocietyDeleteAction.class execute() Method : "+e);
			log.logMessage("Service code : ivrservicecode, Sorry, an unhandled error occurred", "info", siSocietyDeleteAction.class);
			try {
				serverResponse(ivrservicecode,"02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson);
			} catch (Exception e1) {}
		}finally{
			if(pSession!=null){pSession.flush();pSession.clear();pSession.close();pSession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	System.gc();
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

	public TownshipMstTbl getTownShipMst() {
		return townShipMst;
	}

	public void setTownShipMst(TownshipMstTbl townShipMst) {
		this.townShipMst = townShipMst;
	}

}
