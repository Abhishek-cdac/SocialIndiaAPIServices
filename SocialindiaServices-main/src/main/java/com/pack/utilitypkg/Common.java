package com.pack.utilitypkg;

import java.util.Date;

public interface Common {
	
	public boolean commonUpdate(String sql);
	
	public String commonDelete(int uid, String pojonam, String colmn);
	
	public Object getuniqueColumnVal(String tblname, String sltcolname, String wherecolumn, String val);
	
	public String getDateobjtoFomatDateStr(Date pDataobj, String pDateformat);

	public String getGroupcodeexistornew(String pGrpNme);
	public String commonLarmctDelete(int uid, String pojonam, String colmn,int pGrpNme);
}
