package com.pack.feedback;

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
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.audittrial.AuditTrial;
import com.pack.feedbackvo.FeedbackTblVO;
import com.pack.laborvo.LaborProfileTblVO;
import com.pack.laborvo.persistence.LaborDaoservice;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uamm.uamDao;
import com.siservices.uamm.uamDaoServices;
import com.sisocial.load.HibernateUtil;
import com.social.login.EncDecrypt;
import com.social.utils.Log;

public class Feedbackviewall extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private String ivrparams;
	public String execute(){
		Log log=null;
		JSONObject locObjRecvJson = null;//Receive over all Json	[String Received]
		JSONObject locObjRecvdataJson = null;// Receive Data Json
		JSONObject locObjRspdataJson = null;// Response Data Json
		String lsvSlQry = null;
		Session locObjsession = null;
		String ivrservicecode=null;
		String ivrcurrntusridobj=null;
		int ivrCurrntusrid=0;
		String iswebmobilefla=null;
		String societyKey="";
		StringBuilder locErrorvalStrBuil =null;
		try{log= new Log();
			locErrorvalStrBuil = new StringBuilder();
			CommonMobiDao commonServ = new CommonMobiDaoService();
			locObjsession = HibernateUtil.getSession();
			if(ivrparams!=null && !ivrparams.equalsIgnoreCase("null") && ivrparams.length()>0){
				ivrparams = EncDecrypt.decrypt(ivrparams);
				boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
				if (ivIsJson) {
					locObjRecvJson = new JSONObject(ivrparams);
					ivrservicecode = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
					locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
					ivrcurrntusridobj =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "currentloginid");
					if(ivrcurrntusridobj!=null && Commonutility.toCheckisNumeric(ivrcurrntusridobj)){
						ivrCurrntusrid= Integer.parseInt(ivrcurrntusridobj);
					}else{ ivrCurrntusrid=0; }
					iswebmobilefla =  (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "is_mobile");
					boolean desflg =false;
					if(iswebmobilefla!=null && iswebmobilefla.equalsIgnoreCase("1")){
						  String townShipid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
						  societyKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
						// call all mobile check
						if (Commonutility.checkempty(townShipid)) {
							if (townShipid.length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
								//success
								Commonutility.toWriteConsole("success=== ");
							//	desflg=commonServ.checkTownshipKey(townShipid,Commonutility.intToString(rid));
								desflg=commonServ.checkTownshipKey(townShipid,ivrcurrntusridobj);
								Commonutility.toWriteConsole("desflg=== "+desflg);
								if(desflg)
								{
								desflg = commonServ.checkSocietyKey(societyKey, ivrcurrntusridobj);
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
					}else{
						desflg = true;
					}
					if(desflg){
					CommonDao ccc =new CommonDao();
					int societyidval= (Integer)ccc.getuniqueColumnVal("SocietyMstTbl", "societyId", "activationKey", societyKey);
					locObjRspdataJson=toFeedbackselect(locObjRecvdataJson,locObjsession,societyidval);
					String errocheck=(String) Commonutility.toHasChkJsonRtnValObj(locObjRspdataJson, "CatchBlock");// if Catch block found error occurred in select
					if(errocheck!=null && errocheck.equalsIgnoreCase("Error")){//catch found
						AuditTrial.toWriteAudit(getText("FBKADOO2"), "FBKADOO2", ivrCurrntusrid);
						serverResponse("SI5000","00","R0180",mobiCommon.getMsg("R0180"),locObjRspdataJson,iswebmobilefla);
					}else{
						AuditTrial.toWriteAudit(getText("FBKADOO1"), "FBKADOO1", ivrCurrntusrid);
						serverResponse("SI5000","00","R01179",mobiCommon.getMsg("R01179"),locObjRspdataJson,iswebmobilefla);
					}
				}
				}
					else{
					locObjRspdataJson=new JSONObject();
					log.logMessage("Service code : SI5000,"+getText("request.format.notmach")+"", "info", Feedbackviewall.class);
					serverResponse("SI5000","01","R0067",mobiCommon.getMsg("R0067"),locObjRspdataJson,iswebmobilefla);
				}
			}else{
				locObjRspdataJson=new JSONObject();
				log.logMessage("Service code : SI5000,"+getText("request.values.empty")+"", "info", Feedbackviewall.class);
				serverResponse("SI5000","01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson,iswebmobilefla);
			}
		}catch(Exception e){
			Commonutility.toWriteConsole("Exception found Feedbackviewall.class execute() Method : "+e);
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : SI5000, Sorry, an unhandled error occurred", "error", Feedbackviewall.class);
			serverResponse("SI5000","02","R0003",mobiCommon.getMsg("R0003"),locObjRspdataJson,iswebmobilefla);
		}finally{
			log=null;
			if(locObjsession!=null){locObjsession.flush();locObjsession.clear();locObjsession.close();locObjsession=null;}
			locObjRecvJson=null;locObjRecvdataJson =null;locObjRspdataJson=null;
		}
		return SUCCESS;
	}

	private JSONObject toFeedbackselect(JSONObject pDataJson, Session pObjsession,int societyidval) {
		JSONObject locFinalRTNObj = null;
		JSONObject locInrJSONObj = null;
		JSONArray locDataJSONAryobj = null;
		Log log = null;
		String locvrfdbk_STS=null,locvrCntflg=null,locvrFlterflg=null,locvrfdbk_cntQry=null;
		String loc_slQry=null,locStrRow=null,locMaxrow=null, locsrchdtblsrch=null;
		String loc_fdbkforsocid=null;
        String loc_fdbkforusrid=null, loc_fdbkforgrpid=null;
		int count=0,countFilter=0, startrowfrom=1, totalNorow=10;
		String loc_fdbkcountqry=null;
		Iterator locObjfdbklst_itr=null;
		FeedbackTblVO locfeedbacktblvoObj=null;
		Common locCommonObj=null;
		Date locdate=null;
		String locvrfdbk_GRP=null;
		Iterator locObjGroupgorylst=null;
		Iterator locObjlbrgorylst=null;
		GroupMasterTblVo group=null;
		LaborProfileTblVO lbr=null;
		locdate=null;
		String lsvSlQrygrp=null,lsvSlQryusrgrp=null;
		int locvrfdbk_forusrid=0;
		String slectfield=null,searchname=null,societyId=null,loc_searchflg=null;
		try{
			FunctionUtility common = new FunctionUtilityServices();
			ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
			log= new Log();
			Commonutility.toWriteConsole("Step 1 : Select Feedback process start.");
			log.logMessage("Step 1 : Select Feedback process start.", "info", Feedbackviewall.class);
			locvrfdbk_STS = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "fdbkstatus");
			locvrCntflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countflg");
			locvrFlterflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "countfilterflg");
			locStrRow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "startrow");
			locMaxrow=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "totalrow");
			locsrchdtblsrch=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "srchdtsrch");
			loc_fdbkforsocid=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "fdbkforsocid");
			loc_searchflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "searchflg");
			societyId=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "society");
			slectfield=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "slectfield");
			searchname=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "searchname");
            loc_fdbkforusrid=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "fdbkforuser");
			loc_fdbkforgrpid=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "fdbkforusergrp");
			String laborfdbkflg=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "laborfdbkflg");
			String loctocheNull=Commonutility.toCheckNullEmpty(locsrchdtblsrch);
			String timestamp = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson,"timestamp");
			if (Commonutility.checkempty(societyId)) {

			} else {
				societyId=String.valueOf(societyidval);}
			if (Commonutility.checkempty(timestamp)) {
			} else {
				timestamp=Commonutility.timeStampRetStringVal();
			}
			Commonutility.toWriteConsole("slectfield  "+slectfield);
			Commonutility.toWriteConsole("societyId ----------------- "+societyId+"-------"+searchname);
			String globalsearch="";
			String locOrderby =" order by ivrBnENTRY_DATETIME desc";
			if(loctocheNull!=null && !loctocheNull.equalsIgnoreCase("null") && !loctocheNull.equalsIgnoreCase("")){
				if(societyId==null || societyId.equalsIgnoreCase("") || societyId.equalsIgnoreCase("null") || societyId.equalsIgnoreCase("-1")) {
				globalsearch = "("+"ivrBnFEEDBK_TXT  like('%" + loctocheNull + "%') or ivrBnUarmsttbvoobj.firstName.firstName like('%" + loctocheNull + "%') "
			             + ")";

				 }

				else{
					 globalsearch = "("+"ivrBnFEEDBK_TXT  like('%" + loctocheNull + "%') or ivrBnUarmsttbvoobj.firstName.firstName like('%" + loctocheNull + "%') "
				             + ")  and ivrBnUarmsttbvoobj.societyId.societyId="+loc_fdbkforsocid+" ";

				 }
				loc_slQry = "from FeedbackTblVO where ivrBnFEEDBK_STATUS="+locvrfdbk_STS+" and " + globalsearch+" "+locOrderby;
				loc_fdbkcountqry=	"select count(*) from FeedbackTblVO   where ivrBnFEEDBK_STATUS="+locvrfdbk_STS+" and " + globalsearch;
			}else{
				if(searchname!=null && !searchname.equalsIgnoreCase("null") && !searchname.equalsIgnoreCase("")){
					if(societyId!=null && !societyId.equalsIgnoreCase("null") && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")){
						globalsearch = "  ";
						if(slectfield!=null && slectfield.equalsIgnoreCase("1")) {
							globalsearch += "( ivrBnUarmsttbvoobj.firstName.firstName like('%"+searchname+"%'))";
						} else if(slectfield!=null && slectfield.equalsIgnoreCase("2")) {
							globalsearch += "( ivrBnUarmsttbvoobj.firstName.firstName  like ('%" + searchname + "%') )";
						} else if(slectfield!=null && slectfield.equalsIgnoreCase("3")) {
							globalsearch += " ( ivrBnFEEDBK_TXT like ('%" + loc_searchflg + "%') )";
						} else if(slectfield.equalsIgnoreCase("4")){
							if (searchname.equalsIgnoreCase("very Bad")) {
								searchname = "1";
							} else if (searchname.equalsIgnoreCase("Bad")) {
								searchname = "2";
							} else if (searchname.equalsIgnoreCase("Not Bad")) {
								searchname = "3";
							} else if (searchname.equalsIgnoreCase("Good")) {
								searchname = "4";
							} else if (searchname.equalsIgnoreCase("Excellent")) {
								searchname = "5";
							} else {
								searchname = "0";
							}

			              }
						else{
							globalsearch += "(ivrBnUarmsttbvoobj.firstName.firstName like ('%" + searchname + "%') or ";
							globalsearch += " ivrBnUarmsttbvoobj.firstName.firstName like ('%" + searchname + "%') or ";
							globalsearch += "  ivrBnFEEDBK_TXT like ('%" + searchname + "%') )";
						}
						globalsearch += " and ivrBnUarmsttbvoobj.societyId.societyId = "+societyId;
					}
					else{
						globalsearch = "  ";
						if(slectfield!=null && slectfield.equalsIgnoreCase("1")){
							globalsearch += "( ivrBnUarmsttbvoobj.firstName.firstName like ('%" + searchname + "%') )";
						}else if(slectfield!=null && slectfield.equalsIgnoreCase("2"))
						{
							globalsearch += "( ivrBnUarmsttbvoobj.firstName.firstName like ('%" + searchname + "%') )";
						}
						else if(slectfield!=null && slectfield.equalsIgnoreCase("3"))
						{
							globalsearch += " ( ivrBnFEEDBK_TXT like ('%" + searchname + "%') )";
						}
						else if(slectfield.equalsIgnoreCase("4")){
			            	  if(searchname.equalsIgnoreCase("very Bad")){
			            		  globalsearch += " ( ivrBnRATING like ('%" + 1 + "%') )";
			            	  }else if(searchname.equalsIgnoreCase("Bad")){
			            		  globalsearch += " ( ivrBnRATING like ('%2%') )";
			            		  //searchname="2";
			            	  }else if(searchname.equalsIgnoreCase("Not Bad")){
			            		  globalsearch += " ( ivrBnRATING like ('%3%') )";
			            		 // searchname="3";
			            	  }else if(searchname.equalsIgnoreCase("Good")){
			            		  globalsearch += " ( ivrBnRATING like ('%4%') )";
			            		//  searchname="4";
			            	  }else if(searchname.equalsIgnoreCase("Excellent")){
			            		  globalsearch += " ( ivrBnRATING like ('%5%') )";
			            		//  searchname="5";
			            	  }else{
			            		  searchname="0";
			            	  }

						} else {
							Commonutility.toWriteConsole("esle::::  ");
							globalsearch = "("+"ivrBnFEEDBK_TXT  like('%" + searchname + "%') or ivrBnUarmsttbvoobj.firstName.firstName like('%" + searchname + "%') or ivrBnRATING like('%" + searchname + "%')"
						             + ")";
						}
					}
				} else {
				if(societyId!=null && !societyId.equalsIgnoreCase("null") && !societyId.equalsIgnoreCase("") && !societyId.equalsIgnoreCase("-1")){
					globalsearch = "  ivrBnUarmsttbvoobj.societyId.societyId = "+societyId;

				}else{
					globalsearch = "ivrBnFEEDBK_TXT like ('%%')";

				}

			}
				//Labour View Feedback
				if(laborfdbkflg.equalsIgnoreCase("laborfbdkview"))
				{
					loc_slQry ="from FeedbackTblVO where ivrBnFEEDBK_STATUS="+locvrfdbk_STS+" and ivrBnFEEDBK_FOR_USR_ID ="+loc_fdbkforusrid+locOrderby;
					loc_fdbkcountqry=	"select count(*) from FeedbackTblVO   where ivrBnFEEDBK_STATUS="+locvrfdbk_STS+" and ivrBnFEEDBK_FOR_USR_ID ="+loc_fdbkforusrid;
				}
				else
				{
					loc_slQry = "from FeedbackTblVO where ivrBnFEEDBK_STATUS="+locvrfdbk_STS+" and " + globalsearch+" "+locOrderby;
					loc_fdbkcountqry=	"select count(*) from FeedbackTblVO   where ivrBnFEEDBK_STATUS="+locvrfdbk_STS+" and " + globalsearch;
				}
				/*loc_slQry = "from FeedbackTblVO where  ivrBnFEEDBK_STATUS="+locvrfdbk_STS+" and "+globalsearch +" "+locOrderby;
				loc_fdbkcountqry = "select count(*) from FeedbackTblVO where ivrBnFEEDBK_STATUS="+locvrfdbk_STS+" and  "+globalsearch ;*/
			}
				if(locvrCntflg!=null && (locvrCntflg.equalsIgnoreCase("yes") || locvrFlterflg.equalsIgnoreCase("yes"))){// for web

				//loc_fdbkcountqry="select count(ivrBnFEEDBK_ID) from FeedbackTblVO where ivrBnFEEDBK_STATUS="+locvrfdbk_STS+" and ivrBnFEEDBK_FOR_USR_ID = "+loc_fdbkforusrid+" and ivrBnFEEDBK_FOR_USR_TYP ="+loc_fdbkforgrpid+"";
				Commonutility.toWriteConsole("Step 2 : Count / Filter Count block enter SlQry : "+loc_fdbkcountqry);
				log.logMessage("Step 2 : Count / Filter Count block enter SlQry : "+loc_fdbkcountqry, "info", Feedbackviewall.class);

				LaborDaoservice lbrDaoObj=new LaborDaoservice();
				count=lbrDaoObj.getInitTotal(loc_fdbkcountqry);
				countFilter=count;
			}else{ // for mobile
				 count=0;countFilter=0;
				 Commonutility.toWriteConsole("Step 2 : Count / Filter Count not need.[Mobile Call]");
				 log.logMessage("Step 2 : Count / Filter Count not need.[Mobile Call]", "info", Feedbackviewall.class);
			}

			if(Commonutility.toCheckisNumeric(locStrRow)){
				 startrowfrom=Integer.parseInt(locStrRow);
			}
			if(Commonutility.toCheckisNumeric(locMaxrow)){
				totalNorow=Integer.parseInt(locMaxrow);
			}
			Commonutility.toWriteConsole("Step 3 : Feedback Select Query : "+loc_slQry);
			log.logMessage("Step 3 : Feedback Select Quer : "+loc_slQry, "info", Feedbackviewall.class);
			locObjfdbklst_itr=pObjsession.createQuery(loc_slQry).setFirstResult(startrowfrom).setMaxResults(totalNorow).list().iterator();
			locDataJSONAryobj=new JSONArray();
			locCommonObj=new CommonDao();
			String filepath=System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/web/";
			String filepathmob=System.getenv("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/";
			while (locObjfdbklst_itr.hasNext()) {
				locInrJSONObj=new JSONObject();locdate=null;
				locfeedbacktblvoObj=(FeedbackTblVO)locObjfdbklst_itr.next();
				locInrJSONObj.put("feedbackid", Commonutility.toCheckNullEmpty(locfeedbacktblvoObj.getIvrBnFEEDBK_ID()));
				locInrJSONObj.put("feedbacktext", Commonutility.toCheckNullEmpty(locfeedbacktblvoObj.getIvrBnFEEDBK_TXT()));
				locInrJSONObj.put("feedbackrating", Commonutility.toCheckNullEmpty(locfeedbacktblvoObj.getIvrBnRATING()));
				locInrJSONObj.put("feedbackusrtyp", Commonutility.toCheckNullEmpty(locfeedbacktblvoObj.getIvrBnFEEDBK_FOR_USR_TYP()));
				locdate=locfeedbacktblvoObj.getIvrBnENTRY_DATETIME();

				locInrJSONObj.put("feedbackdate", common.getPostedDateTime(locfeedbacktblvoObj.getIvrBnENTRY_DATETIME()));
				if(locfeedbacktblvoObj.getIvrBnUarmsttbvoobj()!=null){
					locInrJSONObj.put("fdbkby_userid", Commonutility.toCheckNullEmpty(locfeedbacktblvoObj.getIvrBnUarmsttbvoobj().getUserId()));
					locInrJSONObj.put("dbkby_fname", Commonutility.toCheckNullEmpty(locfeedbacktblvoObj.getIvrBnUarmsttbvoobj().getFirstName()));
					locInrJSONObj.put("dbkby_lname", Commonutility.toCheckNullEmpty(locfeedbacktblvoObj.getIvrBnUarmsttbvoobj().getLastName()));
					locInrJSONObj.put("dbkby_imgweb", Commonutility.toCheckNullEmpty(locfeedbacktblvoObj.getIvrBnUarmsttbvoobj().getImageNameWeb()));
					locInrJSONObj.put("dbkby_imgmob", Commonutility.toCheckNullEmpty(locfeedbacktblvoObj.getIvrBnUarmsttbvoobj().getImageNameMobile()));
					if(locfeedbacktblvoObj.getIvrBnUarmsttbvoobj().getImageNameWeb()!=null){
						locInrJSONObj.put("dbkby_imgweb",Commonutility.toCheckNullEmpty(filepath+locfeedbacktblvoObj.getIvrBnUarmsttbvoobj().getUserId()+"/"+locfeedbacktblvoObj.getIvrBnUarmsttbvoobj().getImageNameWeb()));
					} else {
						locInrJSONObj.put("dbkby_imgweb","");
					}
					if(locfeedbacktblvoObj.getIvrBnUarmsttbvoobj().getImageNameMobile()!=null){
						locInrJSONObj.put("dbkby_imgmob",Commonutility.toCheckNullEmpty(filepathmob+locfeedbacktblvoObj.getIvrBnUarmsttbvoobj().getUserId()+"/"+locfeedbacktblvoObj.getIvrBnUarmsttbvoobj().getImageNameMobile()));
					} else {
						locInrJSONObj.put("dbkby_imgmob","");
					}
				}
				if(locfeedbacktblvoObj.getIvrBnFEEDBK_FOR_USR_TYP()!=null && locfeedbacktblvoObj.getIvrBnFEEDBK_FOR_USR_ID() != null) {
					locvrfdbk_GRP = (String) Commonutility.toCheckNullEmpty(locfeedbacktblvoObj.getIvrBnFEEDBK_FOR_USR_TYP());
					locvrfdbk_forusrid = locfeedbacktblvoObj.getIvrBnFEEDBK_FOR_USR_ID();// feed back for user id - labor id, committee id, merchant id
					lsvSlQrygrp="from GroupMasterTblVo where groupCode='"+locvrfdbk_GRP+"' ";
					locObjGroupgorylst=pObjsession.createQuery(lsvSlQrygrp).list().iterator();
					while(locObjGroupgorylst.hasNext()) {
						group = (GroupMasterTblVo) locObjGroupgorylst.next();
						locInrJSONObj.put("dbkby_group", Commonutility.toCheckNullEmpty(group.getGroupName()));
						if(getText("Grp.committee").equalsIgnoreCase(group.getGroupName())){
							uamDao uam=new uamDaoServices();
							UserMasterTblVo fbktousr= uam.editUser(locvrfdbk_forusrid);
							if(locfeedbacktblvoObj.getIvrBnUarmsttbvoobj()!=null){
								locInrJSONObj.put("feedbacttoid", Commonutility.toCheckNullEmpty(fbktousr.getUserId()));
								if(Commonutility.checkempty(fbktousr.getFirstName())){
									locInrJSONObj.put("feedbacttoname", Commonutility.toCheckNullEmpty(fbktousr.getFirstName()));
								} else if(Commonutility.checkempty(fbktousr.getLastName())){
									locInrJSONObj.put("feedbacttoname", Commonutility.toCheckNullEmpty(fbktousr.getLastName()));
								} else if(Commonutility.checkempty(fbktousr.getMobileNo())){
									locInrJSONObj.put("feedbacttoname", Commonutility.toCheckNullEmpty(fbktousr.getMobileNo()));
								} else{
									locInrJSONObj.put("feedbacttoname", Commonutility.toCheckNullEmpty(fbktousr.getMobileNo()));
								}

								locInrJSONObj.put("lbrserviceid", Commonutility.toCheckNullEmpty(locfeedbacktblvoObj.getIvrBnUarmsttbvoobj().getFirstName()));

							}
						}else if(getText("Grp.labor").equalsIgnoreCase(group.getGroupName())){
							lsvSlQryusrgrp="from LaborProfileTblVO where ivrBnLBR_ID='"+locvrfdbk_forusrid+"'";
							locObjlbrgorylst = pObjsession.createQuery(lsvSlQryusrgrp).list().iterator();
							while(locObjlbrgorylst.hasNext()) {
								lbr=(LaborProfileTblVO)locObjlbrgorylst.next();
								locInrJSONObj.put("feedbacttoname", Commonutility.toCheckNullEmpty(lbr.getIvrBnLBR_NAME()));
								locInrJSONObj.put("feedbacttoid", Commonutility.toCheckNullEmpty(lbr.getIvrBnLBR_ID()));
								locInrJSONObj.put("lbrserviceid", Commonutility.toCheckNullEmpty(lbr.getIvrBnLBR_SERVICE_ID()));
							}

						}else{
							locInrJSONObj.put("feedbacttoname", "");
							locInrJSONObj.put("feedbacttoid", "");
							locInrJSONObj.put("lbrserviceid", "");
						}
						/*else if(getText("Grp.merchant").equalsIgnoreCase(group.getGroupName())){
						}


						}*/
					}
				} else {
					locInrJSONObj.put("feedbacttoname", "");
					locInrJSONObj.put("feedbacttoid", "");
					locInrJSONObj.put("lbrserviceid", "");
					locInrJSONObj.put("dbkby_group", "");
				}

				locDataJSONAryobj.put(locInrJSONObj);
				locInrJSONObj=null;
			}
			Commonutility.toWriteConsole("Step 4: Feedback JSON ARRAY DATA : "+locDataJSONAryobj);
			log.logMessage("Step 4 : Feedback JSON ARRAY DATA : "+locDataJSONAryobj, "info", Feedbackviewall.class);
			locFinalRTNObj=new JSONObject();
			locFinalRTNObj.put("InitCount", count);
			locFinalRTNObj.put("countAfterFilter", countFilter);
			locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
			locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
			locFinalRTNObj.put("feedbackdetails", locDataJSONAryobj);
			Commonutility.toWriteConsole("locFinalRTNObj : "+locFinalRTNObj);
			Commonutility.toWriteConsole("Step 5: Feedback FIANL JSON DATA : "+locFinalRTNObj);
			log.logMessage("Step 5 : Feedback FIANL JSON DATA : "+locFinalRTNObj, "info", Feedbackviewall.class);

			Commonutility.toWriteConsole("Step 6: Feedback Select Process End.");
			log.logMessage("Step 6: Feedback Select Process End.", "info", Feedbackviewall.class);

			return locFinalRTNObj;
		}catch(Exception e){
			try{
				Commonutility.toWriteConsole("Step -1 : Exception found in Feedbackviewall.toFeedbackselect() : "+e);
				log.logMessage("Step -1 : Exception found in Feedbackviewall.toFeedbackselect() : "+e, "info", Feedbackviewall.class);
				locFinalRTNObj=new JSONObject();
				locFinalRTNObj.put("InitCount", count);
				locFinalRTNObj.put("countAfterFilter", countFilter);
				locFinalRTNObj.put("rowstart", String.valueOf(startrowfrom));
				locFinalRTNObj.put("totalnorow", String.valueOf(totalNorow));
				locFinalRTNObj.put("feedbackdetails", "");
				locFinalRTNObj.put("CatchBlock", "Error");
				Commonutility.toWriteConsole("locFinalRTNObj : "+locFinalRTNObj);
				}catch(Exception ee){}finally{}
				return locFinalRTNObj;
		}finally{
			 locFinalRTNObj=null;locInrJSONObj=null;locDataJSONAryobj=null;log=null;
			 locvrfdbk_STS=null;locvrCntflg=null;locvrFlterflg=null;locvrfdbk_cntQry=null;
			 loc_slQry=null;locStrRow=null;locMaxrow=null;locsrchdtblsrch=null;	loc_fdbkcountqry=null;
			 loc_fdbkforusrid=null;loc_fdbkforgrpid=null; count=0;countFilter=0;startrowfrom=1; totalNorow=10;
			 locObjfdbklst_itr=null; locfeedbacktblvoObj=null;
			 slectfield=null;searchname=null;societyId=null;loc_searchflg=null;
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
