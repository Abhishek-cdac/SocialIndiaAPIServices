package com.socialindiaservices.skillmgmt;

import java.util.Iterator;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.commonvo.CategoryMasterTblVO;
import com.pack.commonvo.SkillMasterTblVO;
import com.pack.utilitypkg.Commonutility;
import com.social.common.Common;
import com.social.common.CommonDao;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class SkillUtility {

	public static String toInsertSkill(JSONObject locObjRecvdataJson) {
		int resval=0;
		String locFtrnStr=null;
		String categoryid=null,status=null,entryby=null;
		Log log=null;	
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		CommonUtils locCommutillObj =null;
		Common commonservices=null;
		CategoryMasterTblVO category=null;
		SkillMasterTblVO skillObj=null;
		try{
			locCommutillObj = new CommonUtilsDao();
			commonservices=new CommonDao();
			category=new CategoryMasterTblVO();
			skillObj=new SkillMasterTblVO();
			categoryid=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"categoryid");
			status=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"status");
			entryby=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"entryby");
			JSONArray jry=(JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "skilllist");
			if(categoryid!=null && !categoryid.equalsIgnoreCase("") && !categoryid.equalsIgnoreCase("null")){
				for (int i = 0; i < jry.length(); i++) {
					 String skillname=(String)jry.getString(i);
					 category.setiVOCATEGORY_ID(Integer.parseInt(categoryid));
					 skillObj.setIvrBnCategoryid(category);
					 skillObj.setIvrBnACT_STAT(Integer.parseInt(status));
					 skillObj.setIvrBnSKILL_NAME(skillname);
					 skillObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
					 skillObj.setIvrcreateflg(1);
					 skillObj.setIvrcreatorby(entryby);
					 int result=commonservices.toIsertskill(skillObj);
					 String filepath=rb.getString("external.mobiledbfldr.path");
						String textvalue="INSERT INTO mvp_skill_tbl(SKILL_ID,SKILL_NAME,CATEG_ID,ACT_STAT) VALUES("+result+", '"+skillname+"', "+Integer.parseInt(categoryid)+","+status+");";
						if(result!=0 && result!=-1){
							Commonutility.TextFileWriting(filepath, textvalue);
						}
						skillname="";
				}
			}
			/**/

			if(resval!=-1){
				locFtrnStr="success!_!"+resval;
			}else{
				locFtrnStr="error!_!"+resval;
			}
			
		}catch(Exception e){
			System.out.println("Exception found in SkillUtility.toInsrtskill : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", SkillUtility.class);
			locFtrnStr="error!_!"+resval;
			return locFtrnStr;
		}finally{
			 		
		}
		return locFtrnStr;
	}

	public static JSONObject toSelectSkill(JSONObject locObjRecvdataJson,Session locObjsession) {
		JSONObject lvrRtnjsonobj = null;
		JSONObject lvrInrJSONObj = null;	
		Log logWrite = null;
		Iterator<Object> lvrObjeventlstitr = null;
		SkillMasterTblVO skillObj=null;
		String lvrSlqry = null;
		String skillid=null;
	    try {
	    	skillObj=new SkillMasterTblVO();
	    	skillid  = (String)Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "skilllist");
			lvrSlqry = "from SkillMasterTblVO  where ivrBnSKILL_ID="+skillid+" and ivrBnACT_STAT=1";
			lvrObjeventlstitr=locObjsession.createQuery(lvrSlqry).list().iterator();
			while (lvrObjeventlstitr.hasNext() ) {
				lvrInrJSONObj=new JSONObject();
				skillObj = (SkillMasterTblVO) lvrObjeventlstitr.next();
				lvrInrJSONObj.put("skillid",Commonutility.toCheckNullEmpty(skillObj.getIvrBnSKILL_ID()));
				lvrInrJSONObj.put("skillname",Commonutility.toCheckNullEmpty(skillObj.getIvrBnSKILL_NAME()));
				lvrInrJSONObj.put("skillcategoryid",Commonutility.toCheckNullEmpty(skillObj.getIvrBnCategoryid().getiVOCATEGORY_ID()));
				lvrInrJSONObj.put("skillcategoryname",Commonutility.toCheckNullEmpty(skillObj.getIvrBnCategoryid().getiVOCATEGORY_NAME()));
			}
			lvrRtnjsonobj=new JSONObject();	
			lvrRtnjsonobj.put("skilldetails", lvrInrJSONObj);
			System.out.println("Step 4 : Select skilldetail process End " +lvrRtnjsonobj);
	    return lvrRtnjsonobj;
	    }catch(Exception expObj) {
	    	try {
				System.out.println("Exception in skilldetails() : "+expObj);
				logWrite.logMessage("Step -1 : skilldetails select all block Exception : "+expObj, "error", SkillUtility.class);	
				lvrRtnjsonobj=new JSONObject();
				lvrRtnjsonobj.put("CatchBlock", "Error");
				System.out.println("lvrRtnjsonobj : "+lvrRtnjsonobj);
				}catch(Exception ee){}finally{}
	     return lvrRtnjsonobj;
	    }finally {
	    	lvrRtnjsonobj = null;
	    	lvrInrJSONObj = null;	
	    	logWrite = null;
	    	lvrObjeventlstitr = null;
	    }

	}

	public static String toModifyskill(JSONObject locObjRecvdataJson,Session locObjsession) {
		String locFtrnStr = null;
		Log log = null;
		ResourceBundle rb = ResourceBundle.getBundle("applicationResources");
		String locVrSlQry = "";
		int resval = 0;
		Common common = null;
		boolean lvrfunmtr = false;
		CommonUtils locCommutillObj = null;
		String skillname=null,skillid=null;
		try {
			log = new Log();
			common=new CommonDao();
			locCommutillObj = new CommonUtilsDao();
			skillname=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"skillname");
			skillid=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson,"skillid");
			locVrSlQry="update SkillMasterTblVO set ivrBnSKILL_NAME='"+skillname+"' where ivrBnSKILL_ID="+Integer.parseInt(skillid)+" and ivrBnACT_STAT = 1";
			lvrfunmtr=common.commonUpdate(locVrSlQry);
			String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="update mvp_skill_tbl set SKILL_NAME='"+skillname+"' where SKILL_ID="+Integer.parseInt(skillid)+" and ACT_STAT =1;";
				if(lvrfunmtr){
					System.out.println("textvalue --------- "+textvalue);
				Commonutility.TextFileWriting(filepath, textvalue);
				}
		if(resval!=-1){
			locFtrnStr="success!_!"+resval;
		}else{
			locFtrnStr="error!_!"+resval;
		}
		
	}catch(Exception e){
		System.out.println("Exception found in facility.toupdate : "+e);
		log.logMessage("Step -1 : Exception : "+e, "error", SkillUtility.class);
		locFtrnStr="error!_!"+resval;
		return locFtrnStr;
	}finally{
		 		log = null;
	}
	return locFtrnStr;

	}

	public static String toActiveorDeactive(JSONObject locObjRecvdataJson,Session locObjsession) {
		String skillid = null;
		String lvrfunmtrqry = null;
		Log log = null;
		Common common = null;
		String jsonTextFinal = null;
		String statusflg = null;
		boolean lvrfunmtr = false;
		ResourceBundle rb = null;
		try {
			log = new Log();
			common = new CommonDao();
			rb = ResourceBundle.getBundle("applicationResources");
			System.out.println("Step 1 : Facilty Deaction block enter");
			log.logMessage("Step 1 : Select Facilty Deaction block enter","info", SkillUtility.class);
			skillid  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "skillid");
			statusflg  = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "status");
			lvrfunmtrqry ="update SkillMasterTblVO set ivrBnACT_STAT="+Integer.parseInt(statusflg)+" where ivrBnSKILL_ID ="+Integer.parseInt(skillid);
			lvrfunmtr=common.commonUpdate(lvrfunmtrqry);
			if(lvrfunmtr){
				String filepath = rb.getString("external.mobiledbfldr.path");
				String textvalue = "update mvp_skill_tbl set ACT_STAT="+Integer.parseInt(statusflg)+" where SKILL_ID ="+Integer.parseInt(skillid)+";";
				if (lvrfunmtr) {
					Commonutility.TextFileWriting(filepath, textvalue);
				}
				jsonTextFinal = "success!_!" + 0;
			} else {
				jsonTextFinal = "error!_!" + 1;
			}
		
		} catch (Exception ex) {
			System.out.println("Test ----- " + ex);
			jsonTextFinal = "error!_!" + 1;
			return jsonTextFinal;
		} finally {
			skillid = null;
			lvrfunmtrqry = null;
			log = null;
			common = null;
			statusflg = null;
			lvrfunmtr = false;
		}

		return jsonTextFinal;

	}

}
