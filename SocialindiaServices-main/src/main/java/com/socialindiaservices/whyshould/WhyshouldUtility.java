package com.socialindiaservices.whyshould;

import java.util.Iterator;
import org.hibernate.Session;
import org.json.JSONObject;

import com.mobi.commonvo.WhyShouldIUpdateTblVO;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.facilityVo.Facilitydaosevices;
import com.socialindiaservices.facilityVo.facilityDao;

public class WhyshouldUtility {

	public static String toInsertWhyshould(JSONObject locObjRecvdataJson) {
		int resval=0;
		String locFtrnStr=null;
		String whyshtdes=null,whydes=null,status=null,entryby=null;
		Log log=null;	
		WhyShouldIUpdateTblVO whyshouldObj=null;
		CommonUtils locCommutillObj =null;
		facilityDao facdao=null;
		try{
			locCommutillObj = new CommonUtilsDao();
			whyshouldObj=new WhyShouldIUpdateTblVO();
			whyshtdes=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"whyshtdes");
			whydes=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"whydes");
			status=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"status");	
			entryby=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"entryby");							
			facdao = new Facilitydaosevices();
			whyshouldObj.setShortDescp(whyshtdes);
			whyshouldObj.setDescp(whydes);
			whyshouldObj.setStatus(Integer.parseInt(status));
			whyshouldObj.setEntryBy(Integer.parseInt(entryby));
			whyshouldObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			resval = facdao.toInsertwhyshould(whyshouldObj);
			if (resval != -1) {
				locFtrnStr = "success!_!" + resval;
			} else {
				locFtrnStr = "error!_!" + resval;
			}
		}catch(Exception e){
			System.out.println("Exception found in WhyshouldUtility.toInsrtWhyshould: "+ e);
			log.logMessage("Step -1 : Exception : " + e, "error",WhyshouldUtility.class);
			locFtrnStr = "error!_!" + resval;
			return locFtrnStr;
		}finally{
			resval = 0;
			whyshtdes = null;
			whydes = null;
			status = null;
			entryby = null;
			log = null;
			whyshouldObj = null;
			locCommutillObj = null;
			facdao = null;
		}
		return locFtrnStr;
	}

	public static JSONObject toSelectwhy(JSONObject locObjRecvdataJson,Session locObjsession) {
		JSONObject lvrRtnjsonobj = null;
		JSONObject lvrInrJSONObj = null;
		Log logWrite = null;
		Iterator<Object> lvrObjeventlstitr = null;
		WhyShouldIUpdateTblVO whyshould = null;
		String lvrSlqry = null;
		String uniqueid = null;
	    try {
	    	
			uniqueid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "uniqueid");
			lvrSlqry = "from WhyShouldIUpdateTblVO  where uniqId='"+ Integer.parseInt(uniqueid) + "'";
			lvrObjeventlstitr = locObjsession.createQuery(lvrSlqry).list().iterator();
			while (lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				whyshould = (WhyShouldIUpdateTblVO) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("whyshouldId",Commonutility.toCheckNullEmpty(whyshould.getUniqId()));
				lvrInrJSONObj.put("whyshouldshrtdesc",Commonutility.toCheckNullEmpty(whyshould.getShortDescp()));
				lvrInrJSONObj.put("whyshouldstatus",Commonutility.toCheckNullEmpty(whyshould.getStatus()));
				lvrInrJSONObj.put("whyshoulddesc",Commonutility.toCheckNullEmpty(whyshould.getDescp()));
			}
			lvrRtnjsonobj = new JSONObject();
			lvrRtnjsonobj.put("whyshould", lvrInrJSONObj);
			System.out.println("Step 4 : Select whyshould process End "+ lvrRtnjsonobj);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in towhyshould() : " + expObj);
				logWrite.logMessage("Step -1 : whyshould select all block Exception : "+ expObj, "error", WhyshouldUtility.class);
				lvrRtnjsonobj = new JSONObject();
				lvrRtnjsonobj.put("CatchBlock", "Error");
				System.out.println("lvrRtnjsonobj : " + lvrRtnjsonobj);
			} catch (Exception ee) {
			} finally {
			}
			return lvrRtnjsonobj;
	    }finally {
	    	lvrRtnjsonobj = null;
	    	lvrInrJSONObj = null;	
	    	logWrite = null;
	    	lvrObjeventlstitr = null;lvrSlqry = null;
			uniqueid=null;
	    }
}
	public static String toModifywhy(JSONObject locObjRecvdataJson,Session locObjsession) {	
		String locFtrnStr = null;
		String whyshtdes = null, whydes = null, entryby = null;
		Log log = null;
		String lvrfunmtrqry = "";
		String whyshouldId = null;
		int resval = 0;
		Common common = null;
		boolean lvrfunmtr = false;
		try {
			log = new Log();
			common=new CommonDao();
			whyshouldId=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"whyshouldId");
			whyshtdes=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"whyshtdes");
			whydes=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"whydes");
			entryby=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"entryby");
			if(whyshouldId!=null && !whyshouldId.equalsIgnoreCase("") && !whyshouldId.equalsIgnoreCase("null")){
				lvrfunmtrqry = "update WhyShouldIUpdateTblVO set shortDescp='"+whyshtdes+"',descp='"+whydes+"',entryBy='"+Integer.parseInt(entryby)+"' where uniqId ="+Integer.parseInt(whyshouldId)+" and status=1";
				lvrfunmtr = common.commonUpdate(lvrfunmtrqry);
			}
			if (lvrfunmtr) {
				locFtrnStr = "success!_!" + resval;
				return locFtrnStr;
			} else {
				locFtrnStr = "error!_!" + resval;
			}
		
	}catch(Exception e){
			System.out.println("Exception found in toModifywhy.toupdate : " + e);
			log.logMessage("Step -1 : Exception : " + e, "error",WhyshouldUtility.class);
			locFtrnStr = "error!_!" + resval;
			return locFtrnStr;
	}finally{
			log = null;
			whyshtdes = null;
			whydes = null;
			entryby = null;
			lvrfunmtrqry = null;
			whyshouldId = null;
			resval = 0;
			common = null;
	}
	return locFtrnStr;
}

	public static String toActiveorDeactive(JSONObject locObjRecvdataJson,Session locObjsession) {
		String whyshouldId = null;
		String lvrfunmtrqry = null;
		Log log = null;
		Common common = null;
		String jsonTextFinal = null;
		String whyshouldstatus = null;
		boolean lvrfunmtr = false;
	try {
		log = new Log();
		common=new CommonDao();
		System.out.println("Step 1 : whyshould Deaction block enter");
		log.logMessage("Step 1 : Select whyshould Deaction block enter", "info", WhyshouldUtility.class);
		whyshouldId  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "whyshouldId");
		whyshouldstatus  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "whyshouldstatus");
		lvrfunmtrqry ="update WhyShouldIUpdateTblVO set status="+Integer.parseInt(whyshouldstatus)+" where uniqId ="+Integer.parseInt(whyshouldId);
		lvrfunmtr=common.commonUpdate(lvrfunmtrqry);
			if (lvrfunmtr) {
				jsonTextFinal = "success!_!" + 0;
			} else {
				jsonTextFinal = "error!_!" + 1;
			}
		
	}catch(Exception ex){
			System.out.println("Test ----- " + ex);
			jsonTextFinal = "error!_!" + 1;
			return jsonTextFinal;
	}finally{
			whyshouldId = null;
			lvrfunmtrqry = null;
			log = null;
			common = null;
			whyshouldstatus = null;
			lvrfunmtr = false;
		}

		return jsonTextFinal;

	}

}
