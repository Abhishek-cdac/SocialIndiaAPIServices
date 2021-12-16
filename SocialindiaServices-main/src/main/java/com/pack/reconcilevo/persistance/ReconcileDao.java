package com.pack.reconcilevo.persistance;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pack.reconcilevo.CyberplatrecondataTblVo;
import com.pack.reconcilevo.CyberplatsetmtfileTblVo;
import com.pack.reconcilevo.PaygaterecondataTblVo;
import com.pack.reconcilevo.PaygatesetmtfileTblVo;



public interface ReconcileDao {
  
  public boolean toInsertPaygateReconData(PaygaterecondataTblVo prPaygatereconobj,Session txsession);
  
  public boolean toInsertCyberplatReconData(CyberplatrecondataTblVo prCyberplatreconobj,Session txsession);
  
  public boolean toUpdateReconcileData(String prReconupdqry);
  
  public List<PaygatesetmtfileTblVo> togetPaygateReconcilFiles();
  
  public List<CyberplatsetmtfileTblVo> togetCyberplatReconcilFiles();
  
  public boolean updateReconFileStatus(String updQuery);
  
  public String toCallReconcileProc(int SocietyId);
  
  public int toInsertPaygateData(PaygatesetmtfileTblVo prPaygatereconobj);
  public int toInsertcyberplateData(CyberplatsetmtfileTblVo prcyberplatereconobj);
  public int getInitTotal(String preNewscntqry);

}
