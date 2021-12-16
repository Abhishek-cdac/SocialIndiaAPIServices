package com.pack.complaintsvo.persistence;

import com.pack.complaintVO.ComplaintattchTblVO;
import com.pack.complaintVO.ComplaintsTblVO;
import com.pack.enewsvo.EeNewsDocTblVO;
import com.pack.eventvo.EventDispTblVO;
import com.pack.eventvo.EventTblVO;
import com.pack.laborvo.LaborDetailsTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;

public interface ComplaintsDao {

  public int toInsertComplaint(ComplaintsTblVO prEventvoobj);
  
  public boolean toUpdateComplaint(String pCmpltupdqry);
  
  public boolean toDeactiveComplaint(String pCmpltvoobj);
  
  public boolean toDeleteComplaint(String pCmpltDlQry);
  
  public int toInsertEventDispTbl(EventDispTblVO prEventdsipvoobj);
  
  public boolean toDeleteEventDispTbl(String prEvntdispdlqry);
  
  public int getInitTotal(String prEntcntqry);
  
  public int getTotalFilter(String prEntfilterqry);
	 public LaborDetailsTblVO getregistertable(int userid);
	 
	 public int savecmpltattachfile(ComplaintattchTblVO iocCmpltattachObj);
}
