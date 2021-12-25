package com.siservices.material;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.enewsvo.persistence.EeNewsDao;
import com.pack.enewsvo.persistence.EeNewsDaoService;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.materialVo.MvpMaterialTbl;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class simaterialMgmt extends ActionSupport {
  /**
   *sdsd.
   */
  private static final long serialVersionUID = 1L;
  private String ivrparams;
  /**
   * Executed Method .
   */
  
  public String execute() {
    Log logWrite = null;
	JSONObject locObjRecvJson = null;//Receive over all Json	[String Received]
	JSONObject locObjRecvdataJson = null;// Receive Data Json		
	JSONObject locObjRspdataJson = null;// Response Data Json
	Session locObjsession = null;		
	String ivrservicecode=null;
	String ivrcurrntusridobj=null;
	int ivrCurrntusrid=0;
    try {
      logWrite = new Log();
      locObjsession = HibernateUtil.getSession();
      if (ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0) {
    	  ivrparams = EncDecrypt.decrypt(ivrparams);
    	  boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
    	  if (ivIsJson) {
    		locObjRecvJson = new JSONObject(ivrparams);
    		ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
    		if(ivrservicecode!=null && !ivrservicecode.equalsIgnoreCase("null") && !ivrservicecode.equalsIgnoreCase("")){
    			locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
    			ivrcurrntusridobj =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
        	    if (ivrcurrntusridobj!=null && Commonutility.toCheckisNumeric(ivrcurrntusridobj)) {
    			ivrCurrntusrid= Integer.parseInt(ivrcurrntusridobj);
    			}else { ivrCurrntusrid=0; }
        	    
        	    locObjRspdataJson=toEventselect(locObjRecvdataJson,locObjsession);		
    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
    				AuditTrial.toWriteAudit(getText("MTR0002"), "MTR0002", ivrCurrntusrid);
    				serverResponse("SI8006","0","E8006",getText("material.selectall.error"),locObjRspdataJson);
    			}else{
    				AuditTrial.toWriteAudit(getText("MTR0001"), "MTR0001", ivrCurrntusrid);
    				serverResponse("SI8006","0","S8006",getText("material.selectall.success"),locObjRspdataJson);					
    			}
    		}else {
    			locObjRspdataJson=new JSONObject();
    	    	logWrite.logMessage("Service code : SI8006,"+getText("request.values.empty")+"", "info", simaterialMgmt.class);
    			serverResponse("SI8006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
    	    }			    	   
    	  }else {
          locObjRspdataJson=new JSONObject();
          logWrite.logMessage("Service code : SI8006,"+getText("request.format.notmach")+"", "info", simaterialMgmt.class);
		  serverResponse("SI8006","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
    	  }
      }else {
    	locObjRspdataJson=new JSONObject();
    	logWrite.logMessage("Service code : SI8006,"+getText("request.values.empty")+"", "info", simaterialMgmt.class);
		serverResponse("SI8006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
      }      
    } catch (Exception expObj) {      
      locObjRspdataJson=new JSONObject();
      logWrite.logMessage("Service code : SI8006, Sorry, an unhandled error occurred : "+expObj, "error", simaterialMgmt.class);
	  serverResponse("SI8006","2","ER0002",getText("catch.error"),locObjRspdataJson);
	} finally {
		if (locObjsession!=null) {locObjsession.flush();locObjsession.clear();locObjsession.close(); locObjsession = null;}
		logWrite = null;
		locObjRecvJson = null;//Receive over all Json	[String Received]
		locObjRecvdataJson = null;// Receive Data Json		
		locObjRspdataJson = null;// Response Data Json	
		ivrservicecode=null;
		ivrcurrntusridobj=null;
		ivrCurrntusrid=0;
	}	 
	return SUCCESS;
  }

  /*
   * To select Events.
   */
  private JSONObject toEventselect(JSONObject praDatajson, Session praSession){	
	JSONObject lvrRtnjsonobj = null;
	JSONObject lvrInrJSONObj = null;	
	JSONArray lvrEventjsonaryobj = null;
	Log logWrite = null;
	Common locCommonObj = null;
	Date lvrEntrydate = null;
	Iterator lvrObjeventlstitr = null;
	MvpMaterialTbl materialMst = null;
	String lvrevntcountqry = null,locvrEventSTS = null, locvrCntflg = null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,srchField=null,srchFieldval=null,srchflg=null;	
	int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
	String lvSlqry = null;
    try {
    	logWrite = new Log();
    	locCommonObj=new CommonDao();
    	System.out.println("Step 1 : Select Event process start.");
    	logWrite.logMessage("Step 1 : Select Event process start.", "info", simaterialMgmt.class);
    	
    	locvrEventSTS = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "eventstatus");
		locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countflg");
		locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countfilterflg");
		locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
		locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
		locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
		srchflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchflg");
		srchField=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchField");
		srchFieldval=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchFieldval");
		String searchWord=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "searchWord");
		String societyId=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "societyId");
		String slectfield=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "groupCode");
		int sSoctyId=(int) Commonutility.toHasChkJsonRtnValObj(praDatajson, "sSoctyId");
		/*if (locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))) {
			lvrevntcountqry = "select count(materialId) from MvpMaterialTbl where status="+locvrEventSTS+"";
			System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+lvrevntcountqry);
			logWrite.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+lvrevntcountqry, "info", simaterialMgmt.class);
			EventDao lvrEvntDobj = new EventDaoservice();
			count = lvrEvntDobj.getInitTotal(lvrevntcountqry);
			countFilter = count;
		}else {
			count=0;countFilter=0;
			System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
			logWrite.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", simaterialMgmt.class);
		}
		if (Commonutility.toCheckisNumeric(locStrRow)) {
			 startrowfrom=Integer.parseInt(locStrRow);
		}
		if (Commonutility.toCheckisNumeric(locMaxrow)) {
			totalNorow=Integer.parseInt(locMaxrow);
		}
		*/
		String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
		String glbSearch="";
		String locOrderby =" order by entryDatetime desc";
		if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
			if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
			glbSearch = " and (" + " materialName like ('%" + locTochenull + "%') or " 
					+ " materialCategoryId.materialName like ('%" + locTochenull + "%') or "
					             + " materialDesc like ('%" + locTochenull + "%') or "
					             + " materialQnty like ('%" + locTochenull + "%') or "
					             + " totalQnty like ('%" + locTochenull + "%') or "
					             + " usedQnty like ('%" + locTochenull + "%') or "
					             + " materialPrice like ('%" + locTochenull + "%') or "
					             + " materialCategoryId.materialCategoryId like ('%" + locTochenull + "%') or "
					             + " purchaseDate like ('%" + locTochenull + "%') "
					             + ")";
			}
			else{
				glbSearch = " and (" + " materialName like ('%" + locTochenull + "%') or "
						+ " materialCategoryId.materialName like ('%" + locTochenull + "%') or " 
			             + " materialDesc like ('%" + locTochenull + "%') or "
			             + " materialQnty like ('%" + locTochenull + "%') or "
			             + " totalQnty like ('%" + locTochenull + "%') or "
			             + " usedQnty like ('%" + locTochenull + "%') or "
			             + " materialPrice like ('%" + locTochenull + "%') or "
			             + " materialCategoryId.materialCategoryId like ('%" + locTochenull + "%') or "
			             + " purchaseDate like ('%" + locTochenull + "%') "
			             + ") and societyId.societyId ="+societyId;
			}
			
			lvSlqry = "from MvpMaterialTbl  where status=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;	
			lvrevntcountqry = "select count(*) from MvpMaterialTbl   where  status=" + locvrEventSTS + " " + glbSearch+" ";
			System.out.println("lvSlqry -------auto--- "+lvSlqry);
		}
		else {
			if(searchWord!=null && !searchWord.equalsIgnoreCase("null") && !searchWord.equalsIgnoreCase("")){// Top search box
				if(societyId!=null && !societyId.equalsIgnoreCase("null") && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")){
					glbSearch = " and ";
					if(slectfield!=null && slectfield.equalsIgnoreCase("1")){														 		 
						glbSearch = " and (" + " materialName like ('%" + searchWord + "%')) "; 
					}else if(slectfield!=null && slectfield.equalsIgnoreCase("2")){
						glbSearch = "  and(" + " materialCategoryId.materialName like ('%" + searchWord + "%')) " ; 
					}
					else if(slectfield!=null && slectfield.equalsIgnoreCase("3")){
						glbSearch = " and (" + " materialPrice like ('%" + searchWord + "%'))" ;
					}
					else{
						glbSearch = " and (" + " materialName like ('%" + searchWord + "%') or"
								+ "  materialCategoryId.materialName like ('%" + searchWord + "%') or "
								  + " materialQnty like ('%" + searchWord + "%') or "
					             + " totalQnty like ('%" + searchWord + "%') or "
					             + " usedQnty like ('%" + searchWord + "%') or "
								+ " materialPrice like ('%" + searchWord + "%')) "; 
					}
					glbSearch += " and societyId.societyId="+societyId;
					
				}else{
					glbSearch = " and ";
					if(slectfield!=null && slectfield.equalsIgnoreCase("1")){														 		 
						glbSearch = " and (" + " materialName like ('%" + searchWord + "%')) "; 
					}else if(slectfield!=null && slectfield.equalsIgnoreCase("2")){
						glbSearch = " and (" + " materialCategoryId.materialName like ('%" + searchWord + "%')) " ; 
					}
					else if(slectfield!=null && slectfield.equalsIgnoreCase("3")){
						glbSearch = " and (" + " materialPrice like ('%" + searchWord + "%'))" ;
					}	
					else
					{
						glbSearch = " and (" + " materialName like ('%" + searchWord + "%') or"
								+ "  materialCategoryId.materialName like ('%" + searchWord + "%') or "
								  + " materialQnty like ('%" + searchWord + "%') or "
					             + " totalQnty like ('%" + searchWord + "%') or "
					             + " usedQnty like ('%" + searchWord + "%') or "
								+ " materialPrice like ('%" + searchWord + "%'))";
					}
				}
				
			}else{
				if(societyId!=null && !societyId.equalsIgnoreCase("null") && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")){
					glbSearch = " and societyId.societyId = "+societyId;
				}else{
					glbSearch = "";
				}
				
			}										
				lvSlqry = "from MvpMaterialTbl where  status=" + locvrEventSTS + " "+glbSearch +" "+locOrderby;
				lvrevntcountqry = "select count(*) from  MvpMaterialTbl where  status=" + locvrEventSTS + " "+glbSearch ;
				System.out.println("lvSlqry -------- "+lvSlqry);
		}
		
		if (locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))) {								
			System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+lvrevntcountqry);
			logWrite.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+lvrevntcountqry, "info", simaterialMgmt.class);
			EeNewsDao lvrEnewDobj = new EeNewsDaoService();
			count = lvrEnewDobj.getInitTotal(lvrevntcountqry);
			countFilter = count;			
		}else {
			count=0;countFilter=0;
			System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
			logWrite.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", simaterialMgmt.class);
		}
		
		if (Commonutility.toCheckisNumeric(locStrRow)) {
			 startrowfrom=Integer.parseInt(locStrRow);
		}
		if (Commonutility.toCheckisNumeric(locMaxrow)) {
			totalNorow=Integer.parseInt(locMaxrow);
		}
		
		
		
	/*	//old
		else {
		//	System.out.println("=d=sf=dsf=dfd=s=============="+societyId);
			if(societyId!=null && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("undefined")){
				glbSearch = " and (" + " societyId.societyId =" + Integer.parseInt(societyId) + ")"; 
			       System.out.println("===societyId====99======="+glbSearch);      
				lvSlqry = "from MvpMaterialTbl  where status=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;	
				System.out.println("=========lvSlqry==============dfg==="+lvSlqry);
				} 
			
			
			 if(groupCode!=null && groupCode.equalsIgnoreCase("1")){
				 if(sSoctyId!=-1){
					 glbSearch = " and (" + " materialName like ('%" + searchWord + "%')) and societyId.societyId="+sSoctyId+""; 
				 }else{
					 glbSearch =glbSearch+ " and (" + " materialName like ('%" + searchWord + "%'))"; 
				 }
				lvSlqry = "from MvpMaterialTbl  where status=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;	
				}else if(groupCode!=null &&  groupCode.equalsIgnoreCase("2")){
					if(sSoctyId!=-1){
					glbSearch = " and (" + " materialCategoryId.materialName like ('%" + searchWord + "%'))  and societyId.societyId="+sSoctyId+"" ;
					}else{
						glbSearch =glbSearch+ " and (" + " materialCategoryId.materialName like ('%" + searchWord + "%'))" ;
					}
					lvSlqry = "from MvpMaterialTbl  where status=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;	
				}else if(groupCode!=null &&  groupCode.equalsIgnoreCase("3")){
					if(sSoctyId!=-1){
					glbSearch = " and (" + " materialPrice like ('%" + searchWord + "%')) and societyId.societyId="+sSoctyId+"" ;
					}else{
						glbSearch = glbSearch+" and (" + " materialPrice like ('%" + searchWord + "%'))" ;	
					}
					lvSlqry = "from MvpMaterialTbl  where status=" + locvrEventSTS + "" + glbSearch+" "+locOrderby;
				}
			
			else{
				System.out.println("=================tets==================");
				if(sSoctyId==-1){
			lvSlqry = "from MvpMaterialTbl  where status=" + locvrEventSTS + " " + glbSearch+" "+locOrderby;
				}else{
					lvSlqry = "from MvpMaterialTbl  where status=" + locvrEventSTS + " " + glbSearch+" and societyId.societyId="+sSoctyId+" "+locOrderby+"";	
				}
			}
		}*/
		System.out.println("Step 3 : MvpMaterialTbls Query : "+lvSlqry);
		logWrite.logMessage("Step 3 : MvpMaterialTbl  Query : "+lvSlqry, "info", simaterialMgmt.class);
		lvrObjeventlstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
		lvrEventjsonaryobj = new JSONArray();
		while ( lvrObjeventlstitr.hasNext() ) {
			lvrInrJSONObj=new JSONObject();
			materialMst = (MvpMaterialTbl) lvrObjeventlstitr.next();
			lvrInrJSONObj.put("materialid", Commonutility.toCheckNullEmpty(materialMst.getMaterialId()));
			lvrInrJSONObj.put("societyname", Commonutility.toCheckNullEmpty(materialMst.getSocietyId().getSocietyName()));
			lvrInrJSONObj.put("materialname", Commonutility.toCheckNullEmpty(materialMst.getMaterialName()));
			lvrInrJSONObj.put("materialcategory", Commonutility.toCheckNullEmpty(materialMst.getMaterialCategoryId().getMaterialName()));
			lvrInrJSONObj.put("materialdesc", Commonutility.toCheckNullEmpty(materialMst.getMaterialDesc()));
			lvrInrJSONObj.put("materialqnty", Commonutility.toCheckNullEmpty(materialMst.getMaterialQnty()));
			lvrInrJSONObj.put("totalqnty", Commonutility.toCheckNullEmpty(materialMst.getTotalQnty()));
			lvrInrJSONObj.put("usedqnty", Commonutility.toCheckNullEmpty(materialMst.getUsedQnty()));
			lvrInrJSONObj.put("materialprice", Commonutility.toCheckNullEmpty(materialMst.getMaterialPrice()));
			lvrInrJSONObj.put("purchasedate", Commonutility.toCheckNullEmpty(materialMst.getPurchaseDate()));
			
			lvrEventjsonaryobj.put(lvrInrJSONObj);
			lvrInrJSONObj = null;
		}
		lvrRtnjsonobj=new JSONObject();	
		lvrRtnjsonobj.put("InitCount", count);
		lvrRtnjsonobj.put("countAfterFilter", countFilter);
		lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
		lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
		lvrRtnjsonobj.put("materialdetails", lvrEventjsonaryobj);
		System.out.println("Step 4 : Select material process End");
		logWrite.logMessage("Step 4 : Select material process End", "info", simaterialMgmt.class);
    return lvrRtnjsonobj;
    }catch(Exception expObj) {
    	try {
			System.out.println("Exception in toEventselect() : "+expObj);
			logWrite.logMessage("Step -1 : material select all block Exception : "+expObj, "error", simaterialMgmt.class);	
			lvrRtnjsonobj=new JSONObject();
			lvrRtnjsonobj.put("InitCount", count);
			lvrRtnjsonobj.put("countAfterFilter", countFilter);
			lvrRtnjsonobj.put("materialdetails", "");
			lvrRtnjsonobj.put("CatchBlock", "Error");
			System.out.println("lvrRtnjsonobj : "+lvrRtnjsonobj);
			}catch(Exception ee){}finally{}
     return lvrRtnjsonobj;
    }finally {
    	lvrRtnjsonobj = null;
    	lvrInrJSONObj = null;	
    	lvrEventjsonaryobj = null;
    	logWrite = null;
    	locCommonObj = null;
    	lvrEntrydate = null;
    	lvrObjeventlstitr = null;
    	materialMst = null;
    	lvrevntcountqry = null;locvrEventSTS = null; locvrCntflg = null; locvrFlterflg = null; locStrRow = null; locMaxrow = null; locSrchdtblsrch = null;	
    	count=0; countFilter = 0; startrowfrom = 1; totalNorow = 0;
    	lvSlqry = null;
    }
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
  
}
