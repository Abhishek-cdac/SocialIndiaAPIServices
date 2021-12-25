package com.socialindiaservices.function;

import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.function.persistence.FunctionDao;
import com.socialindiaservices.function.persistence.FunctionDaoservices;
import com.socialindiaservices.vo.FunctionMasterTblVO;
import com.socialindiaservices.vo.FunctionTemplateTblVO;

public class FunctionUtility {
	
	public static String toInsertFunction(JSONObject pDataJson){

		String locFtrnStr=null,funname=null;
		int locFunid=0;
		String functiontype=null;
		Log log=null;	
		FunctionMasterTblVO locfdbkvoObj=null;
		FunctionTemplateTblVO inrlocfun=null;
		CommonUtils locCommutillObj =null;
		FunctionDao cmndao=null;
		ResourceBundle rb = null;
		try{	
			rb = ResourceBundle.getBundle("applicationResources");
			locfdbkvoObj=new FunctionMasterTblVO();
			locCommutillObj = new CommonUtilsDao();
			cmndao=new FunctionDaoservices();
			log=new Log();
			log.logMessage("Step 2 : Function Insert Block.", "info", FunctionUtility.class);
			funname=(String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "funname");
			functiontype=(String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "functiontype");
			locfdbkvoObj.setFunctionName(funname);
			locfdbkvoObj.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			locfdbkvoObj.setStatusFlag(1);
			locfdbkvoObj.setIvrBnobjEVENT_TYPE(Integer.parseInt(functiontype));
			int lofun = cmndao.saveFunctionMas(locfdbkvoObj);
			JSONArray jry=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "functiontxt");
			if(lofun!=0 && lofun!=-1){
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="INSERT INTO mvp_function_tbl(FUNCTION_ID,FUNCTION_NAME,ENTRY_TIME,MODIFY_TIME,STATUS_FLAG,EVENT_TYPE) VALUES("+lofun+",'"+funname+"','"+locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"','"+locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"',1,"+Integer.parseInt(functiontype)+");";
					Commonutility.TextFileWriting(filepath, textvalue);
			for (int i = 0; i < jry.length(); i++) {
				textvalue="";
				inrlocfun=new FunctionTemplateTblVO();
				 String funtxt=(String)jry.getString(i);
					 inrlocfun.setTemplateText(funtxt);
					 inrlocfun.setStatusFlag(1);
					 locfdbkvoObj.setFunctionId(lofun);
					 inrlocfun.setFunctionId(locfdbkvoObj);
					 inrlocfun.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
					 int locflatinsrtrst = cmndao.saveFunctiontxt(inrlocfun);
					 textvalue="INSERT INTO mvp_function_template_tbl(FUNCTION_TEMPLATE_ID,FUNCTION_ID,TEMPLATE_TEXT,ENTRY_TIME,MODIFY_TIME,STATUS_FLAG,TEMPLATE_CONTENT) VALUES("+locflatinsrtrst+", "+lofun+", '"+funtxt+"', '"+locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"', '"+locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"', 1, '');";
						Commonutility.TextFileWriting(filepath, textvalue);
					 System.out.println(locFunid+": id function");
					inrlocfun=null;
				 
			 }
			}else{
				 return "error!_!"+lofun;
			}
			 log.logMessage("Step 3 : FunctionUtility Detail insert will start.", "info", FunctionUtility.class);
			 System.out.println(locFunid+": id function");
			 log.logMessage("Step 4 : FunctionUtility Detail insert End function Id : "+locFunid, "info", FunctionUtility.class);
			 if(locFunid==0){
				 locFtrnStr="success!_!"+locFunid;
			 }else{
				 locFtrnStr="error!_!"+locFunid;
			 }
			 return locFtrnStr;
			 // -----------Function Insert end------------							
		}catch(Exception e){
			System.out.println("Exception found in FunctionUtility.toInsrtfeedback : "+e);
			log.logMessage("Step -1 : Exception : "+e, "error", FunctionUtility.class);
			locFunid=0;
			locFtrnStr="error!_!"+locFunid;
			return locFtrnStr;
		}finally{
			 pDataJson=null;log=null;locfdbkvoObj=null;locfdbkvoObj=null;
				inrlocfun=null;
				locCommutillObj =null;
				cmndao=null;
			 locFunid=0;			
		}
				
	}
	public static String toUpdateaction(JSONObject pDataJson) {
		System.out.println("pDataJson ------------ "+pDataJson);
		String functionid = null;
		String lvrfunmtrqry = null;
		String lvrfuntempqry = null;
		Log log = null;
		Common common = null;
		String jsonTextFinal=null;
		String funname=null;
		String functtype=null;
		int count=0;
		boolean lvrfunmtr=false;
		FunctionTemplateTblVO inrlocfun=null;
		FunctionMasterTblVO locfdbkvoObj=null;
		CommonUtils locCommutillObj =null;
		FunctionDao cmndao=null;
		ResourceBundle rb = null;
		try{	
			rb = ResourceBundle.getBundle("applicationResources");
			locCommutillObj = new CommonUtilsDao();
			locfdbkvoObj=new FunctionMasterTblVO();
			common=new CommonDao();
			cmndao=new FunctionDaoservices();
			functionid=(String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "functionid");
			funname=(String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "funname");
			functtype=(String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "functiontype");
			String lccnt = (String)Commonutility.toHasChkJsonRtnValObj(pDataJson, "count");
			if (Commonutility.checkempty(lccnt)) {
				count = Integer.parseInt(lccnt);
			} else {
				
			}
			
			//count=count+1;
			if(!funname.equalsIgnoreCase("") && funname!=null && !funname.equalsIgnoreCase("null")){
				lvrfunmtrqry ="update FunctionMasterTblVO set functionName='"+funname+"',ivrBnobjEVENT_TYPE='"+Integer.parseInt(functtype)+"' where functionId ="+Integer.parseInt(functionid)+" and statusFlag=1";
				lvrfunmtr=common.commonUpdate(lvrfunmtrqry);
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="update mvp_function_tbl set FUNCTION_NAME='"+funname+"', EVENT_TYPE="+Integer.parseInt(functtype)+", MODIFY_TIME='"+locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"' where FUNCTION_ID ="+Integer.parseInt(functionid)+" and STATUS_FLAG=1;";
				Commonutility.TextFileWriting(filepath, textvalue);
				if(lvrfunmtr){
				JSONArray jry=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "functiontxt");	
				JSONArray jryid=(JSONArray) Commonutility.toHasChkJsonRtnValObj(pDataJson, "functiontxtid");
				 String idd=null;
				 for (int i = 0; i < jry.length(); i++) {
					 String textname=(String)jry.getString(i);
					 if(count >= i){
							textvalue = "";
							System.out.println(i);
							if(textname!=null && !textname.equalsIgnoreCase("") && !textname.equalsIgnoreCase("null")){
								idd = (String) jryid.getString(i);
								if (Commonutility.checkempty(idd)) {
									lvrfuntempqry ="update FunctionTemplateTblVO set templateText='"+textname+"'  where functionTempId ="+Integer.parseInt(idd)+" and statusFlag=1";
									lvrfunmtr=common.commonUpdate(lvrfuntempqry);
									jsonTextFinal="success!_!"+0;
									textvalue="update mvp_function_template_tbl set TEMPLATE_TEXT='"+textname+"', MODIFY_TIME='"+locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"' where FUNCTION_TEMPLATE_ID ="+Integer.parseInt(idd)+" and STATUS_FLAG=1;";
									Commonutility.TextFileWriting(filepath, textvalue);
								} else {
									
								}
								
						
								
							}else{
								lvrfuntempqry ="update FunctionTemplateTblVO set statusFlag=0  where functionTempId ="+Integer.parseInt(idd);
								lvrfunmtr=common.commonUpdate(lvrfuntempqry);
								jsonTextFinal="success!_!"+0; 
						 }
					 } else {
						 if(textname!=null && !textname.equalsIgnoreCase("") && !textname.equalsIgnoreCase("null")){
						 inrlocfun=new FunctionTemplateTblVO();
						 inrlocfun.setTemplateText(textname);
						 inrlocfun.setStatusFlag(1);
						 locfdbkvoObj.setFunctionId(Integer.parseInt(functionid));
						 inrlocfun.setFunctionId(locfdbkvoObj);
						 inrlocfun.setEntryDatetime(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
						 int locflatinsrtrst = cmndao.saveFunctiontxt(inrlocfun);
						 	textvalue="INSERT INTO mvp_function_template_tbl(FUNCTION_TEMPLATE_ID,FUNCTION_ID,TEMPLATE_TEXT,ENTRY_TIME,MODIFY_TIME,STATUS_FLAG,TEMPLATE_CONTENT) VALUES("+locflatinsrtrst+", "+functionid+", '"+textname+"', '"+locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"', '"+locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss")+"',1,'');";
							Commonutility.TextFileWriting(filepath, textvalue);
						 System.out.println(locflatinsrtrst+": id function");
						inrlocfun=null;
						jsonTextFinal="success!_!"+0;
						 }else{
								lvrfuntempqry ="update FunctionTemplateTblVO set statusFlag=0  where functionTempId ="+Integer.parseInt(functionid);
								lvrfunmtr=common.commonUpdate(lvrfuntempqry);
								jsonTextFinal="success!_!"+0; 
							 }
					 }
				 } 
				}else{
					jsonTextFinal="error!_!"+1;	
				}
			}else{
				lvrfunmtrqry ="update FunctionMasterTblVO set statusFlag=0 where functionId ="+Integer.parseInt(functionid);
				lvrfunmtr=common.commonUpdate(lvrfunmtrqry);
			}
			
			
		}catch(Exception ex){
			System.out.println("Test --update--- "+ex);	
			return jsonTextFinal;
		}finally{
			functionid = null;lvrfunmtrqry = null;lvrfuntempqry = null;log = null;common = null;funname=null;count=0;lvrfunmtr=false;
			inrlocfun=null;locfdbkvoObj=null;locCommutillObj =null;cmndao=null;
		}
		return jsonTextFinal;
	}
	public static String toDeactiveFunction(JSONObject pDataJson){
		String functionid = null;
		String lvrfunmtrqry = null;
		String lvrfuntempqry = null;
		Log log = null;
		Common common = null;
		String jsonTextFinal=null;
		String statusflg=null;
		boolean lvrfunmtr=false;
		ResourceBundle rb = null;
		try{	
			rb = ResourceBundle.getBundle("applicationResources");
			log = new Log();
			common=new CommonDao();
			System.out.println("Step 1 : Function Deaction block enter");
			log.logMessage("Step 1 : Select Function Deaction block enter", "info", FunctionUtility.class);
			functionid  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "functionid");
			statusflg  = (String) Commonutility.toHasChkJsonRtnValObj(pDataJson, "statusflg");
			lvrfunmtrqry ="update FunctionMasterTblVO set statusFlag="+Integer.parseInt(statusflg)+" where functionId ="+Integer.parseInt(functionid);
			lvrfuntempqry ="update FunctionTemplateTblVO set statusFlag="+Integer.parseInt(statusflg)+" where functionId ="+functionid;
			lvrfunmtr=common.commonUpdate(lvrfunmtrqry);
			if(lvrfunmtr){
				boolean updafuntem=common.commonUpdate(lvrfunmtrqry);
				String filepath=rb.getString("external.mobiledbfldr.path");
				String textvalue="update mvp_function_tbl set STATUS_FLAG="+Integer.parseInt(statusflg)+" where FUNCTION_ID ="+Integer.parseInt(functionid)+";";
				Commonutility.TextFileWriting(filepath, textvalue);
				if(updafuntem){
					textvalue="";
					lvrfunmtr=common.commonUpdate(lvrfuntempqry);
					textvalue="update mvp_function_template_tbl set STATUS_FLAG="+Integer.parseInt(statusflg)+" where FUNCTION_ID ="+Integer.parseInt(functionid)+";";
					Commonutility.TextFileWriting(filepath, textvalue);
					jsonTextFinal="success!_!"+0;	
				}
			}else{
				jsonTextFinal="error!_!"+1;	
			}
			
		}catch(Exception ex){
			System.out.println("Test ----- "+ex);	
			return jsonTextFinal;
		}finally{
			functionid = null;lvrfunmtrqry = null;lvrfuntempqry = null;log = null;
			common = null;statusflg=null;lvrfunmtr=false;
		}
		
		return jsonTextFinal;
		
	}
}
