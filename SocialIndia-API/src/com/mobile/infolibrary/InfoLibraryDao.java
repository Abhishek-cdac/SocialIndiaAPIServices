package com.mobile.infolibrary;

import java.util.List;


public interface InfoLibraryDao {
	
	public List getDocumentList(String qry,String startlim, String totalrow,Integer societyId);
	public boolean deleteInfoDocument(String userId,String docId);
	
	public List getDocumentimageList(String qry);

}
