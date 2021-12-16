package com.mobile.infolibrary;

import java.util.List;

import com.socialindiaservices.vo.DocumentManageTblVO;


public interface InfoLibraryDao {
	
	public List getDocumentList(String qry,String startlim, String totalrow,Integer societyId);
	public boolean deleteInfoDocument(String userId,String docId);
	
	public List getDocumentimageList(String qry);

}
