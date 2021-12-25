package com.social.failedSignon;

import java.text.ParseException;

import com.opensymphony.xwork2.ActionSupport;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.failedSignon.persistense.FailsignDao;
import com.social.failedSignon.persistense.FailsignServices;
import com.social.failedSignonvo.FailedSignOnTbl;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;

public class FailedSignOn extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @return
	 * @throws ParseException 
	 */
	public String toInsertfailed(String uname,String pass) throws ParseException{
		Commonutility.toWriteConsole("Step 1 : Sign on failed called");
		FailedSignOnTbl failTbl=null;
		CommonUtils locCommutillObj = null;
		FailsignDao faildao=null;
		String result=null;
		Common lvrCommobj = null;
		Integer lvrUseridInt = null, lvrExitcount = null;
		UserMasterTblVo lvrUamobj =null;		
		try{
			lvrCommobj = new CommonDao();
			locCommutillObj=new CommonUtilsDao();			
			lvrUseridInt = (Integer) lvrCommobj.getuniqueColumnVal("UserMasterTblVo where (mobileNo ='"+uname+"' or emailId = '"+uname+"' or userName ='"+uname+"')", "userId", "", "");
			lvrExitcount = (Integer)  lvrCommobj.getuniqueColumnVal("FailedSignOnTbl where failUsername = '"+uname+"' and status = 1", "failcount","","");
			if (lvrExitcount!=null && lvrExitcount > 0 ) {
				Commonutility.toWriteConsole("Step 2 : Sign on failed Register user [update] : "+lvrUseridInt);
				lvrExitcount +=1; 
				String locUpdQry = "update FailedSignOnTbl set failcount ="+lvrExitcount+" where failUsername ='"+uname+"'";
				Common lvrComobj = new CommonDao();
				lvrComobj.commonUpdate(locUpdQry);
			} else {
				lvrExitcount = 1; 
				if(lvrUseridInt!=null && lvrUseridInt > 0 ){				
					Commonutility.toWriteConsole("Step 2 : Sign on failed Register user [Insert] : "+lvrUseridInt);
					lvrUamobj = new UserMasterTblVo();
					lvrUamobj.setUserId(lvrUseridInt);
					faildao=new FailsignServices();
					failTbl=new FailedSignOnTbl();			
					failTbl.setFailUsername(uname);
					failTbl.setFailPassword(pass);
					failTbl.setIvrBnENTRY_BY(lvrUamobj);
					failTbl.setFailcount(lvrExitcount);
					failTbl.setStatus(1);
					failTbl.setEntryDate(locCommutillObj.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
					result=faildao.failCreationForm(failTbl);
				} else {
					Commonutility.toWriteConsole("Step 2 : Sign on failed UnRegister User");
				}
			}
			
			Commonutility.toWriteConsole("Step 2 : Sign on failed called [End]");
		}catch(Exception ex){
			Commonutility.toWriteConsole("Step -1 : Sign on failed FailedSignOn.class Exception : "+ex);
		}finally{
			failTbl=null; lvrUamobj = null;
		}
		
		return SUCCESS;
		
	}

	
}
