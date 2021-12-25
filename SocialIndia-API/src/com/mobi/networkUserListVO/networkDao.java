package com.mobi.networkUserListVO;

import java.util.List;

public interface networkDao {
	
	public List<MvpNetworkTbl> getnetworkList(String userId,String timestamp, String startlim,String totalrow,int societyId);
	
	public List<Object[]> getnetworkListSearch(String qry,String startlim,String totalrow);

	
	public boolean inviteNetwork(String userId,String nwuserId,String type);
	
	public MvpNetworkTbl checkInviteExist(String fromid,String toid,String request);
	
	public boolean insertInvite(MvpNetworkTbl networkmst);
	
	public boolean checkApproveStatus(String fromid,String toid,String request);
	
	public boolean deleteNetwork(String fromid,String toid,String request);
	public MvpNetworkTbl getNetWorkDetailById(int networkId);
	public MvpNetworkTbl getNetWorkDetailByUsers(String fromid,String toid,String request);
	
	public int getnetworkListSearchcount(String qry);
}
