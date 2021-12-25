package com.pack.audittrial;

import java.util.Date;

import com.pack.audittrialvo.AuditlogTblVO;
import com.pack.audittrialvo.persistence.AuditLogDao;
import com.pack.audittrialvo.persistence.AuditLogDaoservice;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;

public class AuditTrial{
	static Log log =new Log();
	public static void toWriteAudit(String pAudContent, String pAudCode, int pUsrid){
		AuditlogTblVO locAuditlogObj=null;
		CommonUtilsDao cmmn=null;
		UserMasterTblVo locUsrMstrObj=null;
		Date cntDate=null;
		AuditLogDao locAdldao=null;
		try{
			locAuditlogObj = new AuditlogTblVO();
			locAuditlogObj.setIvrBnLOG_CODE(pAudCode);
			locAuditlogObj.setIvrBnOPERATIONS(pAudContent);			
			locAuditlogObj.setIvrBnENTRY_BY(pUsrid);
			cmmn=new CommonUtilsDao();
			cntDate=cmmn.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			locAuditlogObj.setIvrBnENTRY_DATETIME(cntDate);
			if(pUsrid!=0 && pUsrid!=-1){
				locUsrMstrObj=new UserMasterTblVo();
				locUsrMstrObj.setUserId(pUsrid);
				locAuditlogObj.setIvrBnUserMstrTblobj(locUsrMstrObj);
			}else{
				locAuditlogObj.setIvrBnUserMstrTblobj(null);
			}
			locAdldao=new AuditLogDaoservice();
			locAdldao.toWriteAudit(locAuditlogObj);
		}catch(Exception e){
			log.logMessage("Exception found in audit write. Exception : "+e, "error", AuditTrial.class);	
			System.out.println("Excption Found in AuditTrialAction.class toWriteAudit() : "+e);
		}finally{
			locAuditlogObj=null;
			cmmn=null;
			locUsrMstrObj=null;
			cntDate=null;locAdldao=null;
		}
	}

	public static void main(String ww[]) {	
		toWriteAudit("Test Audit","TAD001",1);

	}
}
