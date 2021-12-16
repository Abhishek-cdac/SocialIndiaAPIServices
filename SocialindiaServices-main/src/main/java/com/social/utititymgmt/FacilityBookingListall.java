package com.social.utititymgmt;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.merchantCategorylistvo.persistance.merchantCategoryDao;
import com.pack.merchantCategorylistvo.persistance.merchantCategoryDaoservice;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;
import com.socialindiaservices.vo.FacilityBookingTblVO;

public class FacilityBookingListall extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute(){
		Log log = new Log();
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		Session locObjsession = null;		
		String ivrservicecode=null,ivrcurrntusridStr=null;
		int ivrCurrntusrid=0;
		StringBuilder locErrorvalStrBuil =null;
		String sqlQury = "";
		String societyKey="";
		String iswebmobilefla ="";
		try {
			locErrorvalStrBuil = new StringBuilder();
			locObjsession = HibernateUtil.getSession();
			CommonMobiDao commonServ = new CommonMobiDaoService();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				
				if (ivIsJson) {
				locObjRecvJson = new JSONObject(ivrparams);
				ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");														
				ivrcurrntusridStr =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
				 iswebmobilefla =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "is_mobile"); // 1 - mobile, 0 - web, null - web
				
				
				if(ivrcurrntusridStr!=null && Commonutility.toCheckisNumeric(ivrcurrntusridStr)){
					ivrCurrntusrid= Integer.parseInt(ivrcurrntusridStr);
				}else{
					ivrCurrntusrid=0;
				}
					
				boolean desflg =false;
				if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
					 
					String townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
					 societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
					// call all mobile check
					
					if (Commonutility.checkempty(townShipid)) {
							if (townShipid.length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
								//success
								//	desflg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
								desflg=commonServ.checkTownshipKey(townShipid,ivrcurrntusridStr);							
								if (desflg) {
									desflg = commonServ.checkSocietyKey(societyKey, ivrcurrntusridStr);
								}
						} else {
							String[] passData = { getText("townshipid.fixed.length") };
							desflg = false;
							locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length.error", passData)));
						}
					} else {
						desflg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
					}
					//desflg =  true;
					
					//desflg =  false;
				} else{
					desflg = true;
				}
				
				if(desflg){
					CommonDao ccc =new CommonDao();					
					int societyidval= (Integer)ccc.getuniqueColumnVal("SocietyMstTbl", "societyId", "activationKey", societyKey);					
					locObjRspdataJson=toBookinglistSelectAll(locObjRecvdataJson,locObjsession,societyidval);	
					String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					JSONArray bookingdetails=(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "bookingdetails");
					System.out.println("bookingdetails:::  "+bookingdetails);
						if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
							serverResponse("SI00033","01","E00033",getText("booking.selectall.error"),locObjRspdataJson,iswebmobilefla);
						} else {
							if(bookingdetails==null || bookingdetails.length() <=0 ){//bookingdetails found
								locObjRspdataJson=null;
								serverResponse("SI00033","01","R0025",mobiCommon.getMsg("R0025"),locObjRspdataJson,iswebmobilefla);	
							} else {
								serverResponse("SI00033","00","S00033",getText("booking.selectall.success"),locObjRspdataJson,iswebmobilefla);		
							}
									
					}
				} else {
					serverResponse("SI00033","00","S00033",getText("booking.selectall.success"),locObjRspdataJson,iswebmobilefla);
				}				
				} else {
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI00033, "+getText("request.format.notmach"), "info", FacilityBookingListall.class);
					serverResponse("SI00033","01","EF0001",getText("request.format.notmach"),locObjRspdataJson,iswebmobilefla);

				}	
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI00033, "+getText("request.values.empty"), "info", FacilityBookingListall.class);
				serverResponse("SI00033","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson,iswebmobilefla);

			}	
		} catch (Exception e){			
			Commonutility.toWriteConsole("Step -1 : Exception found in "+ this.getClass().getSimpleName() +".class in method of "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"() : " + e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI00033, "+getText("catch.error"), "error", FacilityBookingListall.class);
			serverResponse("SI00033","02","ER0002",getText("catch.error"),locObjRspdataJson,iswebmobilefla);
		} finally {
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}			
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;	locErrorvalStrBuil =null;
		}				
		return SUCCESS;
	}
	
	public static JSONObject toBookinglistSelectAll(JSONObject pDataJson, Session pSession,int societyidval) {
		JSONObject locFinalRTNObj=null;
		JSONObject locInrJSONObj=null;
		JSONArray locLBRJSONAryobj=null;
		
		String locvrLBR_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrMCat_cntQry=null,currentloginid=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null;	
		Log log=null;
		Iterator locObjLbrlst_itr=null;
		FacilityBookingTblVO lbrDtlVoObj=null;
		int count=0,countFilter=0, startrowfrom=0, totalNorow=10;	
		Date lvrEntrydate = null;
		Date lvrenddate = null;
		Common locCommonObj = null;
		String timestamp="";
		
		try {
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			locCommonObj=new CommonDao();
			log=new Log();	lbrDtlVoObj=new FacilityBookingTblVO();			
			System.out.println("Step 1 : booking Type  select all block enter");
			log.logMessage("Step 1 : booking Type  select all block enter", "info", FacilityBookingListall.class);	
			
			locvrLBR_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "status");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			currentloginid=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "currentloginid");
			String societyId = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "society");
			 timestamp = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"timestamp");
			String booking_status= (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "booking_status");
			String facilityBookingId = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "facility_booking_id");
			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
			String bookingSts="";
			if (Commonutility.checkempty(societyId)) {
				
			} else {
				societyId=String.valueOf(societyidval);}
			if (Commonutility.checkempty(timestamp)) {
			} else {
				timestamp=Commonutility.timeStampRetStringVal();
			}
			if(booking_status!=null && !booking_status.equalsIgnoreCase("null") && booking_status.equalsIgnoreCase("1")) {
				bookingSts=" bookingStatus in(1,2) ";
			} else if(booking_status!=null && !booking_status.equalsIgnoreCase("null") && booking_status.equalsIgnoreCase("0")) {
				bookingSts=" bookingStatus in(0) ";
			} else{
				bookingSts=" bookingStatus in(0,1,2,3) ";
			}
			String search="";
			if(facilityBookingId!=null && facilityBookingId.length()>0){
				search+=" and bookingId='"+facilityBookingId+"'";
			}
			
				String globalsearch=null;
				String locOrderby =" order by entryDatetime desc";
			if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")) {
				if (societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
					globalsearch = " AND (" + "bookedBy.firstName like ('%" + loctocheNull+ "%') or " 
						  + "facilityId.facilityName like ('%" + loctocheNull+ "%') or "
						  + "facilityId.place like ('%" + loctocheNull+ "%') or "
						   + "commiteecomment like ('%" + loctocheNull+ "%') or "
						    + "facilityId.description like ('%" + loctocheNull+ "%') or "
						     + "description like ('%" + loctocheNull+ "%') or "
						  + "title like ('%" + loctocheNull+ "%') or "
			                                + "contactNo like ('%" + loctocheNull + "%'))";
				  loc_slQry="from FacilityBookingTblVO  where  "+bookingSts+" and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') " +globalsearch+search +locOrderby;
				} else {
					globalsearch = " AND (" + "bookedBy.firstName like ('%" + loctocheNull+ "%') or " 
							  + "facilityId.facilityName like ('%" + loctocheNull+ "%') or "
							  + "facilityId.place like ('%" + loctocheNull+ "%') or "
							  + "title like ('%" + loctocheNull+ "%') or "
							  + "commiteecomment like ('%" + loctocheNull+ "%') or "
						    + "facilityId.description like ('%" + loctocheNull+ "%') or "
						    + "description like ('%" + loctocheNull+ "%') or "
						  + "title like ('%" + loctocheNull+ "%') or "
				                                + "contactNo like ('%" + loctocheNull + "%'))";
					  loc_slQry="from FacilityBookingTblVO  where "+bookingSts+" and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') and entryBy.societyId.societyId=" + Integer.parseInt(societyId) + globalsearch + search + locOrderby;
				}
			} else {
				if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
					loc_slQry="from FacilityBookingTblVO  where "+bookingSts+" and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') " +search + locOrderby;
				} else {
					
					loc_slQry="from FacilityBookingTblVO  where "+bookingSts+" and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S')  and entryBy.societyId.societyId=" + Integer.parseInt(societyId) + search + locOrderby;
				}
			}	
							
			if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))) {// for web
				
			/*	if (loctocheNull.equalsIgnoreCase("")) {
					if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
						locvrMCat_cntQry="select count(bookingId) from FacilityBookingTblVO where "+bookingSts+" and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S')"+search;
					} else {
						locvrMCat_cntQry="select count(bookingId) from FacilityBookingTblVO where  "+bookingSts+" and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') and entryBy.societyId.societyId=" + Integer.parseInt(societyId)+search;
					}
				} else {
					if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
					locvrMCat_cntQry="select count(bookingId) from FacilityBookingTblVO where  "+bookingSts+" and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') AND (" + "bookedBy.firstName like ('%" + loctocheNull+ "%') or " 
							+ "facilityId.facilityName like ('%" + loctocheNull+ "%') or "
							+ "facilityId.place like ('%" + loctocheNull+ "%') or "
						  	+ "commiteecomment like ('%" + loctocheNull+ "%') or "
						    + "facilityId.description like ('%" + loctocheNull+ "%') or "
						    + "description like ('%" + loctocheNull+ "%') or "
						    + "title like ('%" + loctocheNull+ "%') or "
			                                + "contactNo like ('%" + loctocheNull + "%'))"+search;
					} else {
						locvrMCat_cntQry="select count(bookingId) from FacilityBookingTblVO where "+bookingSts+" and entryDatetime<STR_TO_DATE('" + timestamp + "','%Y-%m-%d %H:%i:%S') and entryBy.societyId.societyId=" + Integer.parseInt(societyId)+" AND (" + "bookedBy.firstName like ('%" + loctocheNull+ "%') or " 
								  + "facilityId.facilityName like ('%" + loctocheNull+ "%') or "
								  + "facilityId.place like ('%" + loctocheNull+ "%') or "
								  + "commiteecomment like ('%" + loctocheNull+ "%') or "
								  + "facilityId.description like ('%" + loctocheNull+ "%') or "
								  + "description like ('%" + loctocheNull+ "%') or "
								  + "title like ('%" + loctocheNull+ "%') or "
					                                + "contactNo like ('%" + loctocheNull + "%'))"+search;
					}
				} */
				locvrMCat_cntQry = "select count(bookingId) " + loc_slQry;
				System.out.println("Step 2 : Count / Filter Count block enter SlQry : "+locvrMCat_cntQry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+locvrMCat_cntQry, "info", FacilityBookingListall.class);
				
				merchantCategoryDao IdcardDaoObj=new merchantCategoryDaoservice();
				count = IdcardDaoObj.getInitTotal(locvrMCat_cntQry);
				countFilter = count;
				//countFilter=IdcardDaoObj.getTotalFilter(locvrMCat_cntQry);
			} else { // for mobile
				 count=0;countFilter=0;
				 System.out.println("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", FacilityBookingListall.class);
			}
			
			if(Commonutility.toCheckisNumeric(locStrRow)){
					 startrowfrom=Integer.parseInt(locStrRow);
				}
				if(Commonutility.toCheckisNumeric(locMaxrow)){
					totalNorow=Integer.parseInt(locMaxrow);
				}
			System.out.println("Step 3 : booking Type  Details Query : "+loc_slQry);
			log.logMessage("Step 3 : booking Type  Details Query : "+loc_slQry, "info", FacilityBookingListall.class);
			locObjLbrlst_itr=pSession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			System.out.println("locObjLbrlst_itr=== : "+locObjLbrlst_itr.toString());
					
			locLBRJSONAryobj=new JSONArray();
			String filepath=System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/";
			String filepathweb=System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/web/";
			String facilityimgpathweb=System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/"+rb.getString("external.inner.facilityfdr")+"mobile/";
			System.out.println("sss=== ");
			String timestampval="";			
			while ( locObjLbrlst_itr.hasNext() ) {				
				locInrJSONObj=new JSONObject();
				lbrDtlVoObj = (FacilityBookingTblVO) locObjLbrlst_itr.next();								
				locInrJSONObj.put("booking_id",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getBookingId()));	
				locInrJSONObj.put("booking_name",lbrDtlVoObj.getFacilityId().getFacilityName());
				locInrJSONObj.put("booking_facid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getFacilityId().getFacilityId()));
				locInrJSONObj.put("booking_place",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getFacilityId().getPlace()));	
				locInrJSONObj.put("booking_mobno",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getContactNo()));	
				lvrEntrydate=lbrDtlVoObj.getStartTime();
				locInrJSONObj.put("booking_startdate", locCommonObj.getDateobjtoFomatDateStr(lvrEntrydate, "yyyy-MM-dd HH:mm:ss"));
				lvrenddate=lbrDtlVoObj.getEndTime();
				locInrJSONObj.put("booking_enddate", locCommonObj.getDateobjtoFomatDateStr(lvrenddate, "yyyy-MM-dd HH:mm:ss"));	
				/*locInrJSONObj.put("booking_usrname",lbrDtlVoObj.getEntryBy().getUserName());*/
				locInrJSONObj.put("booking_status",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getBookingStatus()));
				locInrJSONObj.put("booking_usrid",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getEntryBy().getUserId()));	
				locInrJSONObj.put("profile_name",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getEntryBy().getFirstName())+" "+Commonutility.toCheckNullEmpty(lbrDtlVoObj.getEntryBy().getLastName()));
				
				
				if(!Commonutility.checkempty(lbrDtlVoObj.getContactNo())) {
					if (lbrDtlVoObj.getEntryBy()!=null) {
						locInrJSONObj.put("booking_mobno", Commonutility.toCheckNullEmpty(lbrDtlVoObj.getEntryBy().getMobileNo()));
					} else {
						locInrJSONObj.put("booking_mobno",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getContactNo()));
					}
					
				}
				if(lbrDtlVoObj.getEntryBy()!=null && lbrDtlVoObj.getEntryBy().getImageNameWeb()!=null){
					locInrJSONObj.put("profilepic_thumbnail",Commonutility.toCheckNullEmpty(filepath+lbrDtlVoObj.getEntryBy().getUserId()+"/"+lbrDtlVoObj.getEntryBy().getImageNameWeb()));
				}else if(lbrDtlVoObj.getEntryBy()!=null && lbrDtlVoObj.getEntryBy().getSocialProfileUrl()!=null){
					locInrJSONObj.put("profilepic_thumbnail",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getEntryBy().getSocialProfileUrl()));
				}else{
					locInrJSONObj.put("profilepic_thumbnail","");
				}
				if(lbrDtlVoObj.getEntryBy().getImageNameMobile()!=null){
					locInrJSONObj.put("profilepic",Commonutility.toCheckNullEmpty(filepathweb+lbrDtlVoObj.getEntryBy().getUserId()+"/"+lbrDtlVoObj.getEntryBy().getImageNameMobile()));
					}else if(lbrDtlVoObj.getEntryBy().getSocialProfileUrl()!=null){
						locInrJSONObj.put("profilepic",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getEntryBy().getSocialProfileUrl()));
						}else{
						locInrJSONObj.put("profilepic","");
					}
				
				locInrJSONObj.put("title",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getTitle()));
				locInrJSONObj.put("committee_desc",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getCommiteecomment()));
				locInrJSONObj.put("resident_desc",Commonutility.toCheckNullEmpty(lbrDtlVoObj.getDescription()));
				if(lbrDtlVoObj.getFacilityId().getFacilityImg()!=null){
					locInrJSONObj.put("facility_img",Commonutility.toCheckNullEmpty(facilityimgpathweb+lbrDtlVoObj.getFacilityId().getFacilityId()+"/"+lbrDtlVoObj.getFacilityId().getFacilityImg()));
				} else {
						locInrJSONObj.put("facility_img","");
				}
				System.out.println("locInrJSONObj======== "+locInrJSONObj);
				locLBRJSONAryobj.put(locInrJSONObj);
				locInrJSONObj=null; 
			
			}	
			System.out.println("Step 3 : Return JSON Array DATA : "+locLBRJSONAryobj);				
			locFinalRTNObj=new JSONObject();	
			locFinalRTNObj.put("timestamp", Commonutility.toCheckNullEmpty(timestamp));
			locFinalRTNObj.put("InitCount", Commonutility.toCheckNullEmpty(count));
			locFinalRTNObj.put("countAfterFilter", Commonutility.toCheckNullEmpty(countFilter));
			locFinalRTNObj.put("rowstart", Commonutility.toCheckNullEmpty(startrowfrom));
			locFinalRTNObj.put("totalnorow", Commonutility.toCheckNullEmpty(totalNorow));
			locFinalRTNObj.put("bookingdetails", locLBRJSONAryobj);	
			
			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			System.out.println("Step 6 : Select category detail block end.");
			log.logMessage("Step 4: Select category detail block end.", "info", FacilityBookingListall.class);
			return locFinalRTNObj;
		} catch (Exception e) {
			try{
			System.out.println("Exception in toCMPYSelectAll() : "+e);
			log.logMessage("Step -1 : category select all block Exception : "+e, "error", FacilityBookingListall.class);	
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("bookingdetails", "");
			locFinalRTNObj.put("CatchBlock", "Error");
			locFinalRTNObj.put("timestamp",timestamp);
			System.out.println("locFinalRTNObj : "+locFinalRTNObj);
			}catch(Exception ee){}finally{}
			return locFinalRTNObj;
		} finally {
			locStrRow=null;locMaxrow=null;
			lbrDtlVoObj=null;log=null;locObjLbrlst_itr=null;locFinalRTNObj=null;locInrJSONObj=null;locLBRJSONAryobj=null;
			count=0; countFilter = 0; startrowfrom = 0; totalNorow = 0;
		}
	}
	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson,String iswebmobilefla)
	{
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();		
		try {
			if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
			out = response.getWriter();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			mobiCommon mobicomn=new mobiCommon();
			String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
			out.print(as);
			out.close();
		}
		else{
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
		}
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
