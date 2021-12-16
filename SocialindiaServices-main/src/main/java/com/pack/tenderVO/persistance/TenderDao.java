package com.pack.tenderVO.persistance;

import com.pack.eventvo.EventDispTblVO;
import com.pack.eventvo.EventTblVO;
import com.pack.tenderVO.TenderDocTblVO;
import com.pack.tenderVO.TenderTblVO;

public interface TenderDao {

  public int toInsertTender(TenderTblVO prTendervoobj);
  
  public int saveTenderDoc_intRtn(TenderDocTblVO prTendervoobj);
  
  public boolean toUpdateTender(String prTenderupdqry);
  
  public boolean toDeactiveEvent(String prEventvoobj);
  
  public boolean toDeleteTender(String prTenderDlQry);
  
  public boolean deleteTenderDocdblInfo(int pIntFlatid);
  
  public boolean toDeleteTenderDispTbl(String prEvntdispdlqry);
  
  public int getInitTotal(String prEntcntqry);
  
  public int getTotalFilter(String prEntfilterqry);
}
