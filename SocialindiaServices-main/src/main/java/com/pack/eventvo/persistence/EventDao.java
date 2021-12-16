package com.pack.eventvo.persistence;

import com.pack.eventvo.EventDispTblVO;
import com.pack.eventvo.EventTblVO;

public interface EventDao {

  public int toInsertEvent(EventTblVO prEventvoobj);
  
  public boolean toUpdateEvent(String prEventupdqry);
  
  public boolean toDeactiveEvent(String prEventvoobj);
  
  public boolean toDeleteEvent(String prEventDlQry);
  
  public int toInsertEventDispTbl(EventDispTblVO prEventdsipvoobj);
  
  public boolean toDeleteEventDispTbl(String prEvntdispdlqry);
  
  public int getInitTotal(String prEntcntqry);
  
  public int getTotalFilter(String prEntfilterqry);
}
