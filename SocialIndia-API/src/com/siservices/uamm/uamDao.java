package com.siservices.uamm;

import java.util.List;

import com.pack.commonvo.FlatMasterTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;
import com.siservices.uam.persistense.MenuMasterTblVo;
import com.siservices.uam.persistense.MvpFamilymbrTbl;
import com.siservices.uam.persistense.RightsMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.siservices.uam.persistense.TownshipMstTbl;
import com.socialindiaservices.vo.NotificationStatusTblVO;


public interface uamDao {
	
	 public List<RightsMasterTblVo> getUserRights(UserMasterTblVo userMaster);
	
	 public List<RightsMasterTblVo> getUserForFamilyRights(int grpCode);
	 
	 public int getInitTotal(String sqlQuery);
	 
	 public int getTotalFilter(String sqlQueryFilter);
	 
	 public List<UserMasterTblVo> getUserDetailList();
	 
	 public List<GroupMasterTblVo> getGroupDetailList();
	 
	 public List<MenuMasterTblVo> getAllMenuMasterList();
	 
	 public String getMenuRightsMenu(int menuid);
	 
	 public boolean createnewgroup(String sql,String groupName);
	 
	 public boolean deletegroup(int delId);
	 
	 public boolean editgroup(int delId,String groupName);
	 
	 public String userCreation(UserMasterTblVo userMaster,String blockNameList,String flatNameList,String famName,String famMobileNo,String famEmailId,SocietyMstTbl societyMaster, GroupMasterTblVo groupMaster,int accessMedia,String famISD,String famMemTyp,String famPrfAccess);
	 
	 public boolean deleteuser(int delUsrId);
	 
	 public UserMasterTblVo editUser(int userId);
	 
	 public List<FlatMasterTblVO> getUserFlatDeatil(int userId);
	 
	 public List<MvpFamilymbrTbl> getUserFamilyList(int userId);
	 
	 public boolean rightsCreation(int grpid,int menuId);
	 
	 public boolean deleteGroupCode(int grpCode);
	 
	 public List<RightsMasterTblVo> getUserRightscurrent(int grpCode);
	 
	 public String  loginusercondition(UserMasterTblVo userinfo,int flag,int societyId);
	 
	 public UserMasterTblVo  loginusercheck(UserMasterTblVo userinfo,int societyId);
	 
	 public List<TownshipMstTbl> gettownshiplist();
	 
	 public UserMasterTblVo getregistertable(int userid);
	 
	 public List<SocietyMstTbl> societyListpertown(int societyid);
	 
	 public int createnewgroup_rtnId(String pGrpName);
	 
	 public MvpFamilymbrTbl  checkFamilyLogin(UserMasterTblVo userinfo);

	 public boolean UpdateGroupBoth(int delId,String groupName,String flg);
	 
	 public boolean UpdateGroup(int delId,String groupName);
	 
	 public List<UserMasterTblVo> getloginUserSocietyDetails(String userOrMobile,int adminCode,int siadminCode);
	 
	 public String checkFamilyMember(UserMasterTblVo userinfo,int flag);
	 
	 public List<UserMasterTblVo> getUserDetailList_like(String globalsearch);
	 
	 public List<UserMasterTblVo> getUserDetailList_like_limt(String globalsearch, int startrowfrom, int totrow);
	 
	 public String updateUser(int uniId,String username,String fname,String lname,
			 int townshipId,int societyId,String societyname, String emailId, String isdno, String mobileNo, int idcardtype, String idcardno, String gender,
			 String dob, int noofFlats, String address1, String address2, int country, int state, int city, Integer pin, String occupation, String bloodtype, 
			 int familyno, int accesschannel,String blockNameList,String flatNameList,String 
				famName,String famMobileNo,String famEmailId,String noofwings,String famISD,String fammemtype,String famprfaccess, String fmbruniqueid, int noOfLogins);
	 
	 public String updateUserProfileExceptAdmin(int uniId,String username,String fname,String lname, int idcardtype, String idcardno, String gender,
			 String dob, int noofFlats, String address1, String address2, int country, int state, int city, int pin, String occupation, String bloodtype, 
			 int familyno, int accesschannel,String blockNameList,String flatNameList,String 
				famName,String famMobileNo,String famEmailId,String noofwings,String profileImageFileName,String emailId,String famISD,String fammemtype,String famprfaccess, String fmbruniqueid);
	 
	 public FlatMasterTblVO getOneFlatDetail(UserMasterTblVo userMaster);
	 
	 public String getDashboardDetails(int useId); 
	 
	 public boolean updateNotificationStatus(NotificationStatusTblVO notifiobj); 
	 
	 public boolean updateOnlineStatus(int rid, String resetLogin); 
	 
	 public boolean UpdateGroupsMobiAccess(int delId,String groupName,String flg);
	 
	 public List<GroupMasterTblVo> getGroupDetailListSrh(String prmSrchstr);
	 
	 public String UserFlatandwings(int userId);
	 
	 public UserMasterTblVo getUserDetails(String userName, String password, int societyId, int flag);
	 
	 public void resetLogin(String userName, String password, int societyId, int flag);
}