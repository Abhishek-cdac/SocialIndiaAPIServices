package com.pack.reconcile;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.reconcilevo.ReconicileresultTblVo;
import com.pack.reconcilevo.persistance.ReconcileDao;
import com.pack.reconcilevo.persistance.ReconcileDaoService;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class ReconicileMgmtTbl extends ActionSupport {
	  /**
	   *sdsd.
	   */
	  private static final long serialVersionUID = 1L;
	  private String ivrparams;
	  /**
	   * Executed Method .
	   */
	  ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
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
	        	    
	        	    locObjRspdataJson=toreconicile(locObjRecvdataJson,locObjsession);		
	    			String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
	    			if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
	    				AuditTrial.toWriteAudit(getText("PAGATE0002"), "PAGATE0002", ivrCurrntusrid);
	    				serverResponse("SI8006","0","E8006",getText("paygate.selectall.error"),locObjRspdataJson);
	    			}else{
	    				AuditTrial.toWriteAudit(getText("PAGATE0001"), "PAGATE0001", ivrCurrntusrid);
	    				serverResponse("SI8006","0","S8006",getText("paygate.selectall.success"),locObjRspdataJson);					
	    			}
	    		}else {
	    			locObjRspdataJson=new JSONObject();
	    	    	logWrite.logMessage("Service code : SI8006,"+getText("request.values.empty")+"", "info", ReconicileMgmtTbl.class);
	    			serverResponse("SI8006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	    	    }			    	   
	    	  }else {
	          locObjRspdataJson=new JSONObject();
	          logWrite.logMessage("Service code : SI8006,"+getText("request.format.notmach")+"", "info", ReconicileMgmtTbl.class);
			  serverResponse("SI8006","1","EF0001",getText("request.format.notmach"),locObjRspdataJson);  
	    	  }
	      }else {
	    	locObjRspdataJson=new JSONObject();
	    	logWrite.logMessage("Service code : SI8006,"+getText("request.values.empty")+"", "info", ReconicileMgmtTbl.class);
			serverResponse("SI8006","1","ER0001",getText("request.values.empty"),locObjRspdataJson);
	      }      
	    } catch (Exception expObj) {      
	      locObjRspdataJson=new JSONObject();
	      logWrite.logMessage("Service code : SI8006, Sorry, an unhandled error occurred : "+expObj, "error", ReconicileMgmtTbl.class);
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
	  private JSONObject toreconicile(JSONObject praDatajson, Session praSession){	
		JSONObject lvrRtnjsonobj = null;
		JSONObject lvrInrJSONObj = null;	
		JSONArray lvrEventjsonaryobj = null;
		Log logWrite = null;
		Common locCommonObj = null;
		Iterator lvrObjeventlstitr = null;
		ReconicileresultTblVo reconicile = null;
		String lvrevntcountqry = null,locvrEventSTS = null, locvrCntflg = null, locvrFlterflg = null, locStrRow = null, locMaxrow = null, locSrchdtblsrch = null,srchField=null,srchFieldval=null,srchflg=null;	
		int count=0, countFilter = 0, startrowfrom = 1, totalNorow = 10;
		String lvSlqry = null;
		String manualsearch="";
	    try {
	    	logWrite = new Log();
	    	locCommonObj=new CommonDao();
	    	System.out.println("Step 1 : Select Reconicileresult process start.");
	    	logWrite.logMessage("Step 1 : Select Reconicileresult process start.", "info", ReconicileMgmtTbl.class);
	    	
	    	locvrEventSTS = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "status");
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
			String slectfield=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "selectfield");
			int sSoctyId=(int) Commonutility.toHasChkJsonRtnValObj(praDatajson, "sSoctyId");
			String fromdate = (String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "fromdate");
    		String todate=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "todate");
    		String selectId=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "selectId");
    		String startamount=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "startamount");
    		String endamount=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "endamount");
    		String transacttype=(String) Commonutility.toHasChkJsonRtnValObj(praDatajson, "transtype");
			if (Commonutility.toCheckisNumeric(locStrRow)) {
				 startrowfrom=Integer.parseInt(locStrRow);
			}
			if (Commonutility.toCheckisNumeric(locMaxrow)) {
				totalNorow=Integer.parseInt(locMaxrow);
			}
			
			String locTochenull=Commonutility.toCheckNullEmpty(locSrchdtblsrch);
			String glbSearch="";
			if (locTochenull!=null && !locTochenull.equalsIgnoreCase("null") && !locTochenull.equalsIgnoreCase("")) {
				if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
					/*glbSearch = " and (" + " ivrBnFLIENAME like ('%" + locTochenull + "%') " 
						             + ")";*/
				} else {
					/*glbSearch = " and (" + " ivrBnFLIENAME like ('%" + locTochenull + "%') or "
							+ " ivrBnENTRY_BY.societyId.societyName like ('%" + locTochenull + "%') or " 
				             + "ivrBnENTRY_BY.societyId.townshipId.townshipName like ('%" + locTochenull + "%') "
				             + ") and ivrBnENTRY_BY.societyId.societyId ="+societyId;*/
				}
			} else {
				if(selectId!=null && !selectId.equalsIgnoreCase("null") && !selectId.equalsIgnoreCase("")){
					if(selectId.trim().equalsIgnoreCase("5")){
					if (startamount.length()>0 && endamount.length()>0) {
						manualsearch += " and ivrTXNAMOUNT between ('"+startamount+"') and ('"+endamount+"')";
					}else if(startamount.length()>0){
						manualsearch += " and ivrTXNAMOUNT >= ('"+startamount+"')";
					}else if(endamount.length()>0){
						manualsearch += " and ivrTXNAMOUNT <= ('"+endamount+"')";
					}
					}
					if (selectId.trim().equalsIgnoreCase("6")) {
						if (transacttype.trim().equalsIgnoreCase("1")) {
							manualsearch += "  and ivrTRANSID.paymentType= 1";
						} else if (transacttype.trim().equalsIgnoreCase("2")) {
							manualsearch += "  and ivrTRANSID.paymentType=2";
						} else {

						}
					} else if (selectId.trim().equalsIgnoreCase("1")) {
						manualsearch += " and ivrTRANMATCHSTATUS= 0";
					}else if(selectId.trim().equalsIgnoreCase("2")){
						manualsearch += " and ivrTRANMATCHSTATUS= 1";
					}else if(selectId.trim().equalsIgnoreCase("2")){
						manualsearch += " and ivrTRANMATCHSTATUS= 1";
					}else if(selectId.trim().equalsIgnoreCase("3")){
						manualsearch += " and ivrTRANRECONSTATUS= 0";
					}else if(selectId.trim().equalsIgnoreCase("4")){
						manualsearch += " and ivrTRANRECONSTATUS= 1";
					}
				}
					if ( fromdate.length() > 0 && todate.length() > 0) {
						manualsearch += " and date(entryDateTime) between str_to_date('" + fromdate + "', '%d-%m-%Y')  and str_to_date('" + todate + "', '%d-%m-%Y')";
					}else if (fromdate.length() > 0) {
						manualsearch += " and date(entryDateTime) >= str_to_date('" + fromdate + "', '%d-%m-%Y')  ";
					} else if (todate.length() > 0) {
						manualsearch += " and date(entryDateTime) <= str_to_date('" + todate + "', '%d-%m-%Y')  ";
					} else {
					}
			}
			lvSlqry = "from ReconicileresultTblVo  where ivrSTATUS=" + locvrEventSTS + " " + manualsearch;	
			lvrevntcountqry = "select count(*) from ReconicileresultTblVo   where  ivrSTATUS=" + locvrEventSTS+ " " + manualsearch;	
			ReconcileDao reconDobj=new ReconcileDaoService();
			count = reconDobj.getInitTotal(lvrevntcountqry);
			countFilter = count;
			
			System.out.println("Step 3 : Reconicileresult Query : "+lvSlqry);
			logWrite.logMessage("Step 3 : Reconicileresult  Query : "+lvSlqry, "info", CyberPlatMgmtTable.class);
			lvrObjeventlstitr=praSession.createQuery(lvSlqry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			lvrEventjsonaryobj = new JSONArray();
			while ( lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				reconicile = (ReconicileresultTblVo) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("paygateId", Commonutility.toCheckNullEmpty(reconicile.getIvrTRANSID().getTranId()));
				lvrInrJSONObj.put("transtype", Commonutility.toCheckNullEmpty(reconicile.getIvrTRANSID().getPaymentType()));
				lvrInrJSONObj.put("paygateId1", Commonutility.toCheckNullEmpty(reconicile.getIvrUTILITYPAYID()));
				lvrInrJSONObj.put("paygateId2", Commonutility.toCheckNullEmpty(reconicile.getIvrPAYGATEID()));
				lvrInrJSONObj.put("orderno", Commonutility.toCheckNullEmpty(reconicile.getIvrORDERNO()));
				lvrInrJSONObj.put("tnxdate", Commonutility.toCheckNullEmpty(locCommonObj.getDateobjtoFomatDateStr(reconicile.getIvrTRANS_DATE(), "dd-MM-yyyy HH:mm:ss")));				
				lvrInrJSONObj.put("amount", Commonutility.toCheckNullEmpty(Commonutility.toAmontDecimalfrmt(reconicile.getIvrTXNAMOUNT())));
				String stval="";
				if(reconicile.getIvrSTATUS_COMMENT()!=null && !reconicile.getIvrSTATUS_COMMENT().equalsIgnoreCase("null") && !reconicile.getIvrSTATUS_COMMENT().equalsIgnoreCase("")){
					String stscode=reconicile.getIvrSTATUS_COMMENT();
					if (Commonutility.checkempty(stscode) && stscode.contains(",")) {
						String statuscode[] = stscode.split(",");
						for(int i=0;i<statuscode.length;i++){
							String stsval="";
							stsval = mobiCommon.getMsg(statuscode[i]);						
							stval += stsval + ", ";											
						}
					} else {
						stval = mobiCommon.getMsg(stscode);		
					}
					if (Commonutility.checkempty(stval) && stval.endsWith(", ")) {
						stval = stval.substring(0, stval.length()-2);
					}
					
				}else{
					lvrInrJSONObj.put("ststuscommand", "");
				}
				lvrInrJSONObj.put("ststuscommand", stval);
				lvrInrJSONObj.put("tnxmatchstatus", Commonutility.toCheckNullEmpty(reconicile.getIvrTRANMATCHSTATUS()));
				lvrInrJSONObj.put("tnxreconstatus", Commonutility.toCheckNullEmpty(reconicile.getIvrTRANRECONSTATUS()));
				lvrInrJSONObj.put("commands", Commonutility.toCheckNullEmpty(reconicile.getIvrCOMMENTS()));
				
				lvrEventjsonaryobj.put(lvrInrJSONObj);
				lvrInrJSONObj = null;
			}
			lvrRtnjsonobj=new JSONObject();	
			lvrRtnjsonobj.put("InitCount", count);
			lvrRtnjsonobj.put("countAfterFilter", countFilter);
			lvrRtnjsonobj.put("rowstart", String.valueOf(startrowfrom));
			lvrRtnjsonobj.put("totalnorow", String.valueOf(totalNorow));
			lvrRtnjsonobj.put("paygatedetails", lvrEventjsonaryobj);
			System.out.println("Step 4 : Select Reconicile Result process End");
			logWrite.logMessage("Step 4 : Select Reconicile Result process End", "info", ReconicileMgmtTbl.class);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in Reconicile Result() : "+expObj);
				logWrite.logMessage("Step -1 : Reconicile Result select all block Exception : "+expObj, "error", ReconicileMgmtTbl.class);	
				lvrRtnjsonobj=new JSONObject();
				lvrRtnjsonobj.put("InitCount", count);
				lvrRtnjsonobj.put("countAfterFilter", countFilter);
				lvrRtnjsonobj.put("paygatedetails", "");
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
	    	lvrObjeventlstitr = null;
	    	reconicile = null;
	    	lvrevntcountqry = null;locvrEventSTS = null; locvrCntflg = null; locvrFlterflg = null; locStrRow = null; locMaxrow = null; locSrchdtblsrch = null;	
	    	count=0; countFilter = 0; startrowfrom = 1; totalNorow = 0;
	    	lvSlqry = null;
	    }
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
