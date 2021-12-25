package com.mobi.messagevo.persistence;

import java.io.File;
import java.util.List;

import org.json.JSONObject;

import com.mobi.messagevo.ChatBlockUnblockTblVO;
import com.mobi.messagevo.ChatTblVO;
import com.mobi.messagevo.GroupChatMemTblVO;
import com.mobi.messagevo.GroupChatMstrTblVO;
import com.mobi.messagevo.GroupChatTblVO;

public interface MessageDAO {
	
	public int chatInsert(ChatTblVO chatobj, List<File> fileUpload,List<String> fileUploadContentType,List<String> fileUploadFileName);
	
	public int groupChatInsert(GroupChatTblVO grpChatObj, List<File> fileUpload,List<String> fileUploadContentType,List<String> fileUploadFileName);
	
	public List<Object[]> privateChatMsgList(int userid,int toId,String timestamp,int startLimit,int endLimit,String locSearchText);
	
	public List<Object[]> groupChatMsgList(int groupId,String timestamp,int startLimit,int endLimit,String locSearchText);
	
	public List<Object[]> privateChatList(int userid,String timestamp,int startLimit,int endLimit);
	
	public List<Object[]> groupChatList(int userid,String timestamp,int startLimit,int endLimit);
	
	public List<Object[]> privateChatListSearch(int userid,String srchValue,String timestamp,int startLimit,int endLimit);
	
	public List<Object[]> groupChatListSearch(int userid,String srchValue,String timestamp,int startLimit,int endLimit);
	
	public boolean deleteGroup(GroupChatMstrTblVO groupObj);
	
	public boolean updateGroupPicture(GroupChatMstrTblVO groupObj);
	
	public boolean updateGroupName(GroupChatMstrTblVO groupObj);
	
	public boolean blockChat(ChatBlockUnblockTblVO chatObj);
	
	public List<Object[]> selectBlockChat(ChatBlockUnblockTblVO chatObj);
	
	public int addGroupMember(GroupChatMemTblVO groupMemObj);
	
	public boolean removeGroupMember(GroupChatMemTblVO groupMemObj);
	
	public int createGroup(GroupChatMstrTblVO groupObj);
	
	public int insertGroupMember(GroupChatMemTblVO groupMemObj);
	
	public List<Object[]> onlineUserList(int userid,String timestamp,int startLimit,int endLimit);
	
	public List<Object[]> getmessageContactsByProc(int userId,int chatType,int startLimit,String searchByName);
	public List<Object[]> getNotificationmessageContactsByProc(int userId);

	public List<ChatTblVO> getchatInsertDetails(int chatId);
	
	public List<GroupChatTblVO> getGroupChartInsertDetails(int chatId);

	public JSONObject jsonChatPack(List<ChatTblVO> sendChatMsgObj, String profileimgPath,String imagePathWeb,String imagePathMobi,String videoPath,String videoPathThumb);

	public JSONObject jsonGroupChatPack(List<GroupChatTblVO> sendGrpChatMsgObj, String profileimgPath,String imagePathWeb,String imagePathMobi,String videoPath,String videoPathThumb);

	public int deleteMemberList(String deletQry);

	public JSONObject getGroupNameDetails(List<Object[]> chatListObj);
	

}
