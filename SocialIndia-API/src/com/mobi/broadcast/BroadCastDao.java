package com.mobi.broadcast;

import java.util.List;

public interface BroadCastDao {
	public List getEventBbroadcastSearchList(String qry, String startlim,
			String totalrow, String timestamp);
	
	public List getEventBbroadcastDocuumentSearchList(String qry, String startlim,
			String totalrow, String timestamp);

}
