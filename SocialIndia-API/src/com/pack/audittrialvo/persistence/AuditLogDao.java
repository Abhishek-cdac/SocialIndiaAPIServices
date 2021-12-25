package com.pack.audittrialvo.persistence;

import com.pack.audittrialvo.AuditlogTblVO;





public interface AuditLogDao {
	public int toWriteAudit(AuditlogTblVO pAuditobj);	
	
}
