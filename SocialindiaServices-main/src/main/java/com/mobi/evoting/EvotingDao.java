package com.mobi.evoting;

import java.util.List;

import com.socialindiaservices.vo.MvpEvotePoolingTbl;
import com.socialindiaservices.vo.MvpEvotingImageTbl;
import com.socialindiaservices.vo.MvpEvotingMstTbl;

public interface EvotingDao {
	
	public MvpEvotingMstTbl insertEvotingMstTbl(MvpEvotingMstTbl evotingObj);
	public MvpEvotingImageTbl insertEvotingImageTbl(MvpEvotingImageTbl evotingimageObj);
	
	public boolean updateEvotingMstTbl(MvpEvotingMstTbl evotingObj);
	public MvpEvotingImageTbl selectEvotingImageTblbyQuery(String qry);
	public MvpEvotingMstTbl selectEvotingmstTblbyQuery(String qry);
	public boolean updateEvotingMstTblbyQuery(String qry);
	
	public List getEvotingMastergList(String qry, String startlim,String totalrow,String timestamp);
	public long getEvotingCountbyQuery(String qry);
	
	public List<Object[]> getEvotingMastergListbysqlQuery(String qry, String startlim,String totalrow,String timestamp);
	public List<Object[]> getResidentEvotingMastergListbysqlQuery(String qry, String startlim,String totalrow,String timestamp);
	public MvpEvotePoolingTbl insertEvotingPoolingTbl(MvpEvotePoolingTbl evotingObj);
	
	public Object[] getEvotingCountByQuery(String Sql);
	public List<MvpEvotingImageTbl> selectEvotingmstTblListbyQuery(String qry);
	

}
