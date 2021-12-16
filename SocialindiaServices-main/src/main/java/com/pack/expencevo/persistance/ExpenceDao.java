package com.pack.expencevo.persistance;

import com.pack.eventvo.EventDispTblVO;
import com.pack.eventvo.EventTblVO;
import com.pack.expencevo.ExpenceTblVO;
import com.pack.tenderVO.TenderDocTblVO;
import com.pack.tenderVO.TenderTblVO;

public interface ExpenceDao {

  public int toInsertExpence(ExpenceTblVO prExpencevoobj);
  
  public int saveTenderDoc_intRtn(TenderDocTblVO prTendervoobj);
  
  public boolean toUpdateExpence(String prExpenceupdqry);
  
  public boolean toDeactiveExpence(String prEventvoobj);
  
  public boolean toDeleteTender(String prTenderDlQry);
  
  public boolean deleteTenderDocdblInfo(int pIntFlatid);
  
  public boolean toDeleteTenderDispTbl(String prEvntdispdlqry);
  
  public int getInitTotal(String prEntcntqry);
  
  public int getTotalFilter(String prEntfilterqry);
}
