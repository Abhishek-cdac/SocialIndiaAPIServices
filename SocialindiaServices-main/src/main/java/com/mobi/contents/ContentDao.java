package com.mobi.contents;

import java.util.List;

public interface ContentDao {

	public List getFlashNewsList(String qry);
	public int getInitTotal(String preNewscntqry);
	public int toInsertFlashNew(FlashNewsTblVO flashObj);
	public List<MvpFaqMstTbl> getFrequentQuestList(int rid, String searchQryText);
	 
}
