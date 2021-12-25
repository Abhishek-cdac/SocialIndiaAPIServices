package com.pack.enews;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.enewsvo.EeNewsDocTblVO;
import com.pack.enewsvo.EeNewsTblVO;
import com.pack.enewsvo.persistence.EeNewsDao;
import com.pack.enewsvo.persistence.EeNewsDaoService;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class EeNewsviewall extends ActionSupport {
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
        	    
        	    locObjRspdataJson=toeNewsselect(locObjRecvdataJson,locObjsession);		
    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
    				AuditTrial.toWriteAudit(getText("ENWSAD012"), "ENWSAD012", ivrCurrntusrid);
    				serverResponse("SI9006","0","E9006",getText("eNews.selectall.error"),locObjRspdataJson);
    			}else{
    				AuditTrial.toWriteAudit(getText("ENWSAD011"), "ENWSAD011", ivrCurrntusrid);
    				serverResponse("SI9006","0","S9006",getText("eNews.selectall.success"),locObjRspdataJson);					
    			}
    		}else {
    			locObjRspdataJson=new JSONObject();
    	    	logWrite.logMessage("Service code : SI9006,"+getText("request.values.empty")+"", "info", EeNewsviewall.class);
    			serverResponse("SI9006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
    	    }			    	   
    	  }else {
          locObjRspdataJson=new JSONObject();
          logWrite.logMessage("Service code : SI9006,"+getText("request.format.notmach")+"", "info", EeNewsviewall.class);
		  serverResponse("SI9006","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
    	  }
      }else {
    	locObjRspdataJson=new JSONObject();
    	logWrite.logMessage("Service code : SI9006,"+getText("request.values.empty")+"", "info", EeNewsviewall.class);
		serverResponse("SI9006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
      }      
    } catch (Exception expObj) {      
      locObjRspdataJson=new JSONObject();
      logWrite.logMessage("Service code : SI9006, Sorry, an unhandled error occurred : "+expObj, "error", EeNewsviewall.class);
	  serverResponse("SI9006","2","ER0002",getText("catch.error"),locObjRspdataJson);
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
  private JSONObject toeNewsselect(JSONObject praDatajson, Session praSession){	
	JSONObject lvrRtnjsonobj = null;
	JSONObject lvrInrJSONObj = null;	
	JSONArray lvreNewsjsonaryobj = null;
	Log logWrite = null;
	Common locCommonObj = null;
	Date lvrEntrydate = null;
	Iterator lvrObjeNewslstitr = null;
	EeNewsTblVO lvrEnewtblvoobj = null;
	Query lvrQrybj = null,lvrQrygrpbj=null,lvrQrylabname=null;
	UserMasterTblVo locGrptblobj = null;
	LaborProfileTblVO locLabnameObj=null;
	String lvreNewscountqry = null,locvreNewsSTS = null, locvrCntflg = null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,lvrSlgrpcodeqry=null,lvrslqlabnameqry=null,loccrntusrloginid=null;	
	int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
	String lvSlqry = null;
	String loc_slQry_file=null;
	Iterator locObjfilelst_itr=null;
	JSONArray locLBRFILEJSONAryobj=null;
	JSONObject locLBRFILEOBJJSONAryobj=null;
	EeNewsDocTblVO locfiledbtbl=null;
	String downloadImagePath="";
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	Session locHbsession =null;
    try {
    	logWrite = new Log();
    	locCommonObj=new CommonDao();
    	locHbsession = HibernateUtil.getSession();
    	Map sessionMap =ActionContext.getContext().getSession();
    	System.out.println("Step 1 : Select eNews process start.");
    	logWrite.logMessage("Step 1 : Select eNews process start.", "info", EeNewsviewall.class);
    	//String locvrsocietyid = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "eNewssocietyid");
    	locvreNewsSTS = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "eNewsstatus");
		locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countflg");
		locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "countfilterflg");
		locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startrow");
		locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "totalrow");
		locSrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchdtsrch");
		loccrntusrloginid=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "crntusrloginid");
		String societyId = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "societyid");
		String slectfield = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "slectfield");
		String searchWord = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "srchFieldval");	
		String searchflg = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "searchflg");
		String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
				
		String glbSearch="";
		String locOrderby =" order by ivrBnENTRY_DATETIME desc";
		System.out.println("step3: datatable search field:  "+societyId+" : selectfield::: "+slectfield +" [searcWord: "+searchWord +"]");
		
		if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {// data table search			
			if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
						glbSearch = " and (" + "  ivrBnUAMObj.societyId.societyName like ('%" + locTochenull + "%') or " 
		           		             + " ivrBnTITLE like ('%" + locTochenull + "%') "
		           		             + ")";
			} else {
						glbSearch = " and ( ivrBnUAMObj.societyId.societyName like ('%" + locTochenull + "%') or " 
									+ " ivrBnTITLE like ('%" + locTochenull + "%') "
									+ ") and ivrBnUAMObj.societyId.societyId = "+societyId;
			}
				lvSlqry = "from EeNewsTblVO   where  ivrBnSTATUS=" + locvreNewsSTS + " " + glbSearch+" "+locOrderby;
				lvreNewscountqry = "select count(*) from EeNewsTblVO   where  ivrBnSTATUS=" + locvreNewsSTS + " " + glbSearch+" ";
		} else {
			if(searchWord!=null && !searchWord.equalsIgnoreCase("null") && !searchWord.equalsIgnoreCase("")){// Top search box
				if(societyId!=null && !societyId.equalsIgnoreCase("null") && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")){
					glbSearch = " and ";
					if(slectfield!=null && slectfield.equalsIgnoreCase("1")){														 		 
						glbSearch += "( ivrBnTITLE like ('%" + searchWord + "%') )";
					}else{
						glbSearch += "(ivrBnUAMObj.societyId.societyName like ('%" + searchWord + "%') or ";					
				 		 
						glbSearch += " ivrBnTITLE like ('%" + searchWord + "%') )";
					}									 		 
					glbSearch += " and ivrBnUAMObj.societyId.societyId = "+societyId;
					
				}else{
					
					glbSearch = " and ";
					if(slectfield!=null && slectfield.equalsIgnoreCase("1")){														 		 
						glbSearch += "( ivrBnTITLE like ('%" + searchWord + "%') )";
					}else{
						glbSearch += "(ivrBnUAMObj.societyId.societyName like ('%" + searchWord + "%') or ";					
				 		 
						glbSearch += " ivrBnTITLE like ('%" + searchWord + "%') )";
					}									 		 
				}
				
			}else{
				if(societyId!=null && !societyId.equalsIgnoreCase("null") && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")){
					glbSearch = " and ivrBnUAMObj.societyId.societyId = "+societyId;
				}else{
					glbSearch = "";
				}
				
			}										
				lvSlqry = "from EeNewsTblVO where  ivrBnSTATUS=" + locvreNewsSTS + " "+glbSearch +" "+locOrderby;
				lvreNewscountqry = "select count(*) from EeNewsTblVO where  ivrBnSTATUS=" + locvreNewsSTS + " "+glbSearch ;
		}
				
		if (locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))) {								
			System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+lvreNewscountqry);
			logWrite.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+lvreNewscountqry, "info", EeNewsviewall.class);
			EeNewsDao lvrEnewDobj = new EeNewsDaoService();
			count = lvrEnewDobj.getInitTotal(lvreNewscountqry);
			countFilter = count;			
		}else {
			count=0;countFilter=0;
			System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
			logWrite.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", EeNewsviewall.class);
		}
		
		if (Commonutility.toCheckisNumeric(locStrRow)) {
			 startrowfrom=Integer.parseInt(locStrRow);
		}
		if (Commonutility.toCheckisNumeric(locMaxrow)) {
			totalNorow=Integer.parseInt(locMaxrow);
		}
		
		
		
		System.out.println("Step 3 : eNews Details Query : "+lvSlqry);
		logWrite.logMessage("Step 3 : eNews Details Query : "+lvSlqry, "info", EeNewsviewall.class);
		lvrObjeNewslstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
		lvreNewsjsonaryobj = new JSONArray();
		while ( lvrObjeNewslstitr.hasNext() ) {
			lvrInrJSONObj=new JSONObject();
			lvrEnewtblvoobj = (EeNewsTblVO) lvrObjeNewslstitr.next();
			
			//if(sessionMap.get("USERID")
			if(lvrEnewtblvoobj.getIvrBnUAMObj().getSocietyId()!=null){
			lvrInrJSONObj.put("eNewssocietyname",Commonutility.toCheckNullEmpty(lvrEnewtblvoobj.getIvrBnUAMObj().getSocietyId().getSocietyName()));
			lvrInrJSONObj.put("eNewstownshipname",Commonutility.toCheckNullEmpty(lvrEnewtblvoobj.getIvrBnUAMObj().getSocietyId().getTownshipId().getTownshipName()));
			}
			else
			{
				lvrInrJSONObj.put("eNewssocietyname","");
				lvrInrJSONObj.put("eNewstownshipname","");
			}
			lvrInrJSONObj.put("eNewsfrmusrid",Commonutility.toCheckNullEmpty(lvrEnewtblvoobj.getIvrBnUAMObj().getUserId()));
			lvrInrJSONObj.put("eNewsfrmusrgrpcode",Commonutility.toCheckNullEmpty(lvrEnewtblvoobj.getIvrBnUAMObj().getGroupCode().getGroupName()));
			lvrInrJSONObj.put("eNewsfrmusrname",Commonutility.toCheckNullEmpty(lvrEnewtblvoobj.getIvrBnUAMObj().getUserName()));
			lvrInrJSONObj.put("eNewscommiteeemail",Commonutility.toCheckNullEmpty(lvrEnewtblvoobj.getIvrBnUAMObj().getEmailId()));
		
			lvrInrJSONObj.put("eNewsid",Commonutility.toCheckNullEmpty(lvrEnewtblvoobj.getIvrBnENEWS_ID()));
			lvrInrJSONObj.put("eNewstitle",Commonutility.toCheckNullEmpty(lvrEnewtblvoobj.getIvrBnTITLE()));
			lvrInrJSONObj.put("eNewsdesc",Commonutility.toCheckNullEmpty(lvrEnewtblvoobj.getIvrBnDESCRIPTION()));
			
			lvrInrJSONObj.put("eNewsstatus",Commonutility.toCheckNullEmpty(lvrEnewtblvoobj.getIvrBnSTATUS()));
			lvrInrJSONObj.put("eNewsentryby",Commonutility.toCheckNullEmpty(lvrEnewtblvoobj.getIvrBnENTRY_BY()));
			
			lvrEntrydate=lvrEnewtblvoobj.getIvrBnENTRY_DATETIME();
			lvrInrJSONObj.put("eNewsentrydate", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "yyyy-MM-dd hh:mm:ss"));
			
			logWrite.logMessage("Step 2: Select eNews files detail block start.", "info",EeNewsviewall.class);
			loc_slQry_file="from EeNewsDocTblVO where Enewid="+lvrEnewtblvoobj.getIvrBnENEWS_ID()+" ";
			locObjfilelst_itr=locHbsession.createQuery(loc_slQry_file).list().iterator();	
			System.out.println("Step 3 : Select eNews files detail query executed.");
			locLBRFILEJSONAryobj=new JSONArray();
			while (locObjfilelst_itr!=null &&  locObjfilelst_itr.hasNext() ) {
				locfiledbtbl=(EeNewsDocTblVO)locObjfilelst_itr.next();
				locLBRFILEOBJJSONAryobj=new JSONObject();
				if(locfiledbtbl.getEnewuniqId()!=null){
					locLBRFILEOBJJSONAryobj.put("filesname", locfiledbtbl.getImgname());


					downloadImagePath=rb.getString("external.imagesfldr.path")+rb.getString("external.inner.enewsfldr")+rb.getString("external.inner.webpath")+lvrEnewtblvoobj.getIvrBnENEWS_ID()+"/"+locfiledbtbl.getImgname();
					locLBRFILEOBJJSONAryobj.put("filepath",downloadImagePath);
				}				
				locLBRFILEJSONAryobj.put(locLBRFILEOBJJSONAryobj);
			}
			
			logWrite.logMessage("Step 3: Select file name and type detail block end.", "info", EeNewsviewall.class);
			lvrInrJSONObj.put("jArry_doc_files", locLBRFILEJSONAryobj);
			lvreNewsjsonaryobj.put(lvrInrJSONObj);
			lvrInrJSONObj = null;
		}				
		lvrRtnjsonobj=new JSONObject();	
		lvrRtnjsonobj.put("InitCount", count);
		lvrRtnjsonobj.put("countAfterFilter", countFilter);
		lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
		lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
		lvrRtnjsonobj.put("eNewsdetails", lvreNewsjsonaryobj);
		System.out.println("Step 4 : Select eNews process End");
		logWrite.logMessage("Step 4 : Select eNews process End", "info", EeNewsviewall.class);
    return lvrRtnjsonobj;
    }catch(Exception expObj) {
    	try {
			System.out.println("Exception in toeNewsselect() : "+expObj);
			logWrite.logMessage("Step -1 : eNews select all block Exception : "+expObj, "error", EeNewsviewall.class);	
			lvrRtnjsonobj=new JSONObject();
			lvrRtnjsonobj.put("InitCount", count);
			lvrRtnjsonobj.put("countAfterFilter", countFilter);
			lvrRtnjsonobj.put("eNewsdetails", "");
			lvrRtnjsonobj.put("CatchBlock", "Error");
			System.out.println("lvrRtnjsonobj : "+lvrRtnjsonobj);
			}catch(Exception ee){}finally{}
     return lvrRtnjsonobj;
    }finally {
    	if(locHbsession!=null){locHbsession.flush();locHbsession.clear();locHbsession.close();locHbsession = null;}
    	lvrRtnjsonobj = null;
    	lvrInrJSONObj = null;	
    	lvreNewsjsonaryobj = null;
    	logWrite = null;
    	locCommonObj = null;
    	lvrEntrydate = null;
    	lvrObjeNewslstitr = null;
    	lvrEnewtblvoobj = null;
    	lvreNewscountqry = null;locvreNewsSTS = null; locvrCntflg = null; locvrFlterflg = null; locStrRow = null; locMaxrow = null; locSrchdtblsrch = null;loccrntusrloginid=null;	
    	count=0; countFilter = 0; startrowfrom = 1; totalNorow = 0;
    	lvSlqry = null;loc_slQry_file=null;locObjfilelst_itr=null;
    	 locLBRFILEJSONAryobj=null;
    	 locLBRFILEOBJJSONAryobj=null;locfiledbtbl=null;
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
