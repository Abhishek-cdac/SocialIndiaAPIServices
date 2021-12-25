package com.pack.enewsvo.persistence;

import com.pack.enewsvo.EeNewsDocTblVO;
import com.pack.enewsvo.EeNewsTblVO;

public interface EeNewsDao {

	public int toInserteNews(EeNewsTblVO pEnewsobj);

	public boolean toUpdateeNews(String pEnewsupdqry);

	public boolean toDeleteEnewImgTbl(String pEnewsimgupdqry);

	public boolean toDeactivateeNews(String pEnewsdactveqry);

	public boolean toDeleteeNews(String pEnewsdeleteqry);

	public int getInitTotal(String preNewscntqry);

	public int getTotalFilter(String preNewsfilterqry);

	public int saveEnewsDoc_intRtn(EeNewsDocTblVO prEnewsvoobj);

	public boolean toDeleteEnewImgupdTbl(String lvrEnewsid);

}
