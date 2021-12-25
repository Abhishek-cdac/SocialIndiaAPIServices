package com.pack.expence;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.expencevo.ExpenceTblVO;
import com.pack.expencevo.persistance.ExpenceDao;
import com.pack.expencevo.persistance.ExpenceDaoservice;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.sisocial.load.HibernateUtil;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class Expenceutility {
	/*
	 * to insert into Expence table.
	 */
	static String isvrClientfrmt = "dd-MM-yyyy hh:ss a";
	static String isvrClientfrmt_sub = "dd-MM-yyyy hh:ssa";
	static SimpleDateFormat locSmftclinetfrmt = new SimpleDateFormat("dd-MM-yyyy hh:ss a");// Client Select Date Format	
	public static String toInsertExpence(JSONObject prDatajson, Log log){
    String locFtrnStr = null, lvrCrntusrid = null, lvrCrntgrpid = null;
	String lvrExpencetit=null, lvrExpenceprct=null, lvrExpencedesc=null;
	String lvrCrntSocietyid=null,lvrExpencelocation=null, lvrExpenceamt=null, lvrExpencetotalamt=null, lvrExpenceSts=null,desc="";
	
	
	ExpenceTblVO locExpencevoObj=null;		
	CommonUtils locCommutillObj =null;
	ExpenceDao loctenderDaoObj = null;
	int locExpenceid=0;	
	double locvrPeramt=0.0;
	double locvrTotamt=0.0;
		try {			
			locCommutillObj = new CommonUtilsDao();
			log.logMessage("Step 2 : Expence Insert Block.", "info", Expenceutility.class);
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrgrpid");
			lvrCrntSocietyid = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "crntusrsocietyid");
			lvrExpencetit = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "expencetitle");
			lvrExpenceprct = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "expencenoproduct");
			lvrExpenceamt = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "expenceamt");
			lvrExpencetotalamt = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "expencetotalamt");
			lvrExpencedesc = (String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "expencedesc");
			lvrExpenceSts =(String) Commonutility.toHasChkJsonRtnValObj(prDatajson, "expencestatus");			
			locExpencevoObj=new ExpenceTblVO();	
			locExpencevoObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			
			UserMasterTblVo userObj = new UserMasterTblVo();
			if(lvrCrntSocietyid.equalsIgnoreCase("-1"))
			{
				lvrCrntSocietyid=null;
			}
			if(!Commonutility.toCheckisNumeric(lvrCrntusrid)){
				locExpencevoObj.setUsrid(null);
				locExpencevoObj.setEntryBy(null);
			}else{
				userObj.setUserId(Integer.parseInt(lvrCrntusrid));
				locExpencevoObj.setUsrid(userObj);
				locExpencevoObj.setEntryBy(Integer.parseInt(lvrCrntusrid));
			}			
			if(!Commonutility.toCheckisNumeric(lvrCrntgrpid)){
				locExpencevoObj.setEntryByGrp(null);
			}else{
				//groupObj.setGroupCode(Integer.parseInt(lvrCrntgrpid));
				locExpencevoObj.setEntryByGrp(Integer.parseInt(lvrCrntgrpid));
			}
			
			locExpencevoObj.setExpenceFor(lvrExpencetit);
			locExpencevoObj.setNoOfProduct(Integer.parseInt(lvrExpenceprct));
			if(lvrExpenceamt!=null && !lvrExpenceamt.equalsIgnoreCase("null") && !lvrExpenceamt.equalsIgnoreCase("")){
				locvrPeramt=Double.parseDouble(lvrExpenceamt);
			 }
			locExpencevoObj.setProductPerAmt(locvrPeramt);
			if(lvrExpencetotalamt!=null && !lvrExpencetotalamt.equalsIgnoreCase("null") && !lvrExpencetotalamt.equalsIgnoreCase("")){
				locvrTotamt=Double.parseDouble(lvrExpencetotalamt);
			 }
			locExpencevoObj.setExpenceTotAmt(locvrTotamt);
			locExpencevoObj.setDescr(lvrExpencedesc);
			locExpencevoObj.setExpenceStatus(Integer.parseInt(lvrExpenceSts));
			//----------- Expence Insert Start-----------			
			log.logMessage("Step 3 : Expence Detail insert will start.", "info", Expenceutility.class);
			loctenderDaoObj=new ExpenceDaoservice();
			locExpenceid = loctenderDaoObj.toInsertExpence(locExpencevoObj);
			System.out.println(locExpenceid+": id Expence");
			log.logMessage("Step 4 : Expence Detail insert End Expence Id : "+locExpenceid, "info", Expenceutility.class);
			// -----------Expence Insert end------------		
			if (locExpenceid>0) {				
				
				 locFtrnStr="success!_!"+locExpenceid;
			}else{
				locFtrnStr="error!_!"+locExpenceid;
			}
			 return locFtrnStr;
			 		
		}catch(Exception e){
			System.out.println("Exception found in Expenceutility.toInsrttender : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", Expenceutility.class);
			locExpenceid=0;
			locFtrnStr="error!_!"+locExpenceid;
			return locFtrnStr;
		}finally{
			locFtrnStr=null;lvrCrntusrid=null; lvrCrntgrpid=null;
			lvrExpencetit=null;lvrExpenceprct=null; lvrExpencedesc=null;
			lvrCrntSocietyid=null;lvrExpenceamt=null; lvrExpencetotalamt=null; lvrExpenceSts=null;
			locExpencevoObj=null;locCommutillObj =null;loctenderDaoObj = null;
			locExpenceid=0;
			desc=null;
		}
	}
	
	public static String toUpdateExpence(JSONObject pDataJson,String pGrpName, String pAuditMsg, String pAuditCode, String pWebImagpath, String pMobImgpath){
		String locFtrnStr=null;
		String lvrCrntusrid=null, lvrCrntgrpid=null, lvrExpenceid=null;
		String lvrExpencetit=null,lvrExpenceprct=null, lvrExpencedesc=null,lvrExpencefilename=null,lvrExpencefiledata=null,lvrExpenceVdpath=null;
		String lvrCrntSocietyid=null,lvrExpenceenddate=null, lvrExpenceamt=null, lvrExpencetotalamt=null, lvrExpencelocation = null, lvrExpenceSts=null;
		Log log=null;
		String locUpdQry=null;
		ExpenceDao loctenderDaoObj=null;
		boolean lvrExpenceUpdaSts=false;
		
		Session locSession=null;
		String locSlqry=null;
		Query locQryrst=null;
		//GroupMasterTblVo locGrpMstrVOobj=null;
		Common lvrCommdaosrobj = null;
		GroupMasterTblVo locGrpmstvoobj=null;
		CommonUtils locCommutillObj =null;
		ExpenceTblVO locExpencevoObj=null;	
		UserMasterTblVo userDocObj =new UserMasterTblVo();
		SocietyMstTbl societyObj =new SocietyMstTbl();
		JSONArray imageDetail=null;
		JSONArray fileName=null;
		double locvrPeramt=0.0;
		double locvrTotamt=0.0;
		try{
			log=new Log();
			lvrCommdaosrobj = new CommonDao();
			locCommutillObj = new CommonUtilsDao();
			log.logMessage("Step 2 : Expence Update Block.", "info", Expenceutility.class);
			System.out.println("Step 2 : Expence Update Block.");
			lvrExpenceid=(String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "expenceid");
			lvrCrntusrid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrloginid");
			lvrCrntgrpid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrgrpid");
			lvrCrntSocietyid = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "crntusrsocietyid");
			lvrExpencetit = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "expencetitle");
			lvrExpenceprct = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "expencenoproduct");
			lvrExpenceamt = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "expenceamt");
			lvrExpencetotalamt = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "expencetotalamt");
			lvrExpencedesc = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "expencedesc");
			
			lvrExpenceSts="1";			
			locExpencevoObj=new ExpenceTblVO();	
			locExpencevoObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			
			
			
			locExpencevoObj.setExpenceFor(lvrExpencetit);
			locExpencevoObj.setDescr(lvrExpencedesc);
			locExpencevoObj.setNoOfProduct(Integer.parseInt(lvrExpenceprct));
			if(lvrExpenceamt!=null && !lvrExpenceamt.equalsIgnoreCase("null") && !lvrExpenceamt.equalsIgnoreCase("")){
				locvrPeramt=Double.parseDouble(lvrExpenceamt);
			 }
			locExpencevoObj.setProductPerAmt(locvrPeramt);
			if(lvrExpencetotalamt!=null && !lvrExpencetotalamt.equalsIgnoreCase("null") && !lvrExpencetotalamt.equalsIgnoreCase("")){
				locvrTotamt=Double.parseDouble(lvrExpencetotalamt);
			 }
			locExpencevoObj.setExpenceTotAmt(locvrTotamt);
			
			locUpdQry ="update ExpenceTblVO set expenceFor = '"+lvrExpencetit+"', descr = '"+lvrExpencedesc+"', productPerAmt='"+lvrExpenceamt+"',expenceTotAmt='"+lvrExpencetotalamt+"',noOfProduct ='"+lvrExpenceprct+"', ";
			
			
			if(!Commonutility.toCheckisNumeric(lvrCrntusrid)){
				locUpdQry +="usrid ="+null+", ";
				locUpdQry +="entryBy ="+null+", ";				
			}else{
				
				locUpdQry +="usrid ="+Integer.parseInt(lvrCrntusrid)+", ";
				locUpdQry +="entryBy ="+Integer.parseInt(lvrCrntusrid)+" ";					
			}			
			
			
			locUpdQry +=" where expnId ="+Integer.parseInt(lvrExpenceid)+"";
			log.logMessage("Step 3 : Expence Updqry : "+locUpdQry, "info", Expenceutility.class);
			//--------Expence Update Start-------------
			loctenderDaoObj=new ExpenceDaoservice();
			lvrExpenceUpdaSts=loctenderDaoObj.toUpdateExpence(locUpdQry);
			System.out.println("Expence Update Status [lvrExpenceUpdaSts] : "+lvrExpenceUpdaSts);
			//--------Expence Update End-------------
			 
			
			 if(Commonutility.toCheckNullEmpty(lvrExpenceid)!=null && !Commonutility.toCheckNullEmpty(lvrExpenceid).equalsIgnoreCase("null")&& !Commonutility.toCheckNullEmpty(lvrExpenceid).equalsIgnoreCase("")){
				
				
				 locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			 return locFtrnStr;
		}catch(Exception e){
			System.out.println("Exception found in Expenceutility.toUpdateExpence : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", Expenceutility.class);		
			locFtrnStr="error";
			return locFtrnStr;			
		}finally{
			locGrpmstvoobj=null;
			if(locSession!=null){locSession.close();locSession=null;}
			lvrCrntusrid=null; lvrCrntgrpid=null; lvrExpenceid=null;
			lvrExpencetit=null;lvrExpenceprct=null; lvrExpencedesc=null;lvrExpencefilename=null;lvrExpencefiledata=null;lvrExpenceVdpath=null;
			lvrCrntSocietyid=null;lvrExpenceenddate=null; lvrExpenceamt=null; lvrExpencetotalamt=null; lvrExpenceSts=null;
			log=null;
			locUpdQry=null;
			loctenderDaoObj=null;
			lvrExpenceUpdaSts=false;
			locSlqry=null;locQryrst=null;			
		}
	}

	
	public static String toDeleteExpence(JSONObject pDataJson){
		String locFtrnStr = null;
		String lvrExpenceid = null;
		boolean lvrExpenceUpdaSts = false;
		boolean lvrExpenceDispUpdaSts = false;
		Log log=null;
		String locDldQry=null;
		String locDldispQry=null;
		ExpenceDao loctenderDaoObj=null;
		
		try{
			log=new Log();
			loctenderDaoObj = new ExpenceDaoservice();
			log.logMessage("Step 2 : Expence Delete block enter.", "info", Expenceutility.class);
			lvrExpenceid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "expenceid");			
			locDldispQry= "update ExpenceTblVO set expenceStatus=0 where expnId ="+Integer.parseInt(lvrExpenceid);
			log.logMessage("Step 3 : Expence Display Tbl Delete qry : "+locDldispQry, "info", Expenceutility.class);
			lvrExpenceUpdaSts=loctenderDaoObj.toDeactiveExpence(locDldispQry);
			
			
			if(lvrExpenceUpdaSts){
				locFtrnStr="success";
			}else{
				locFtrnStr="error";
			}
			log.logMessage("Step 5 : Expence Delete block End.", "info", Expenceutility.class);
			return locFtrnStr;
		}catch(Exception e){
			return locFtrnStr;
		}finally{
			lvrExpenceid=null;
			log=null;locDldQry=null;loctenderDaoObj=null;
		}
	}

	public static JSONObject toseletExpencesingledata(JSONObject pDataJson){ 
    String lvrExpenceid = null;
	String lvrSlqry = null,loc_slQry_file=null;
	Query lvrQrybj = null;
	Log log = null;
	Date lvrEntrydate=null;
	ExpenceTblVO locExpencetblobj = null;
	JSONObject locRtndatajson = null;
	Session locHbsession = null;
	Common locCommonObj = null;
	Iterator locObjfilelst_itr=null;
	JSONArray locLBRFILEJSONAryobj=null;
	JSONObject locLBRFILEOBJJSONAryobj=null;
	ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
	String downloadImagePath="";
	try {
		log = new Log();
		locHbsession = HibernateUtil.getSession();
		locCommonObj = new CommonDao();
		System.out.println("Step 1 : Expence detail block enter");
		log.logMessage("Step 1 : Select Expence detail block enter", "info", Expenceutility.class);
		locRtndatajson =  new JSONObject();
		lvrExpenceid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "expenceid");
		lvrSlqry = "from ExpenceTblVO where expnId = "+Integer.parseInt(lvrExpenceid);
		lvrQrybj = locHbsession.createQuery(lvrSlqry);
		locExpencetblobj = (ExpenceTblVO)lvrQrybj.uniqueResult();
		System.out.println("Step 2 : Select Expence detail query executed.");
		locRtndatajson.put("expenceid",Commonutility.toCheckNullEmpty(locExpencetblobj.getExpnId()));
		locRtndatajson.put("expencetitle",Commonutility.toCheckNullEmpty(locExpencetblobj.getExpenceFor()));
		locRtndatajson.put("expencenoofprdt",Commonutility.toCheckNullEmpty(locExpencetblobj.getNoOfProduct()));
		locRtndatajson.put("expenceprdtperamt",Commonutility.toCheckNullEmpty(locExpencetblobj.getProductPerAmt()));
		locRtndatajson.put("expencetotamt",Commonutility.toCheckNullEmpty(locExpencetblobj.getExpenceTotAmt()));
		locRtndatajson.put("expencedesc",Commonutility.toCheckNullEmpty(locExpencetblobj.getDescr()));
		locRtndatajson.put("expencesocietyname",Commonutility.toCheckNullEmpty(locExpencetblobj.getUsrid().getSocietyId().getSocietyName()));
		locRtndatajson.put("expencetownshipname",Commonutility.toCheckNullEmpty(locExpencetblobj.getUsrid().getSocietyId().getTownshipId().getTownshipName()));
		
		
		System.out.println("Step 5 : Return JSON DATA : "+locRtndatajson);						
		log.logMessage("Step 4:  Select expencedocmentt detail block end.", "info", Expenceutility.class);	
		System.out.println("Step 3 : Expence details are put into json.");
		System.out.println("Step 4 : Expence Block end.");
		return locRtndatajson;
	}catch(Exception e) {
		try{
			System.out.println("Step -1 : Select Expence detail Exception found in Expenceutility.toseletExpencesingle : "+e);
			log.logMessage("Exception : "+e, "error", Expenceutility.class);
			locRtndatajson=new JSONObject();
			locRtndatajson.put("catch", "Error");
			}catch(Exception ee){}
			return locRtndatajson;
	}finally {
			if(locHbsession!=null){locHbsession.flush();locHbsession.clear();locHbsession.close();locHbsession=null;}
			 lvrExpenceid = null; lvrSlqry = null; lvrQrybj = null;
			 log = null; lvrEntrydate=null; loc_slQry_file=null;locExpencetblobj = null; locRtndatajson = null;locLBRFILEJSONAryobj=null;locLBRFILEOBJJSONAryobj=null;
			 
	}
  }

   
}
