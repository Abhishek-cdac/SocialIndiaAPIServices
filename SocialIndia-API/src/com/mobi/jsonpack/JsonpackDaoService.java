package com.mobi.jsonpack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.addpost.AddPostDao;
import com.mobi.addpost.AddPostServices;
import com.mobi.addpost.MvpAdpostAttchTbl;
import com.mobi.addpost.MvpAdpostTbl;
import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.common.mobiCommon;
import com.mobi.commonvo.PrivacyInfoTblVO;
import com.mobi.event.EventDao;
import com.mobi.event.EventDaoServices;
import com.mobi.feedvo.persistence.FeedDAO;
import com.mobi.feedvo.persistence.FeedDAOService;
import com.mobi.messagevo.ChatAttachTblVO;
import com.mobi.messagevo.GrpChatAttachTblVO;
import com.mobi.messagevo.persistence.MessageDAOService;
import com.mobi.publishskillvo.PublishSkillTblVO;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.mobile.profile.profileDao;
import com.mobile.profile.profileDaoServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.commonvo.CityMasterTblVO;
import com.pack.commonvo.FlatMasterTblVO;
import com.pack.commonvo.IDCardMasterTblVO;
import com.pack.donation.DonationDao;
import com.pack.donation.DonationDaoServices;
import com.pack.eventvo.EventTblVO;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.SocietyMstTbl;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.MvpDonationAttachTblVo;
import com.socialindiaservices.vo.MvpDonationItemTypeTblVo;
import com.socialindiaservices.vo.MvpDonationTbl;

public class JsonpackDaoService extends ActionSupport implements JsonpackDao {

	@Override
	public JSONArray laborRegDetail(List<Object[]> laborDetailObj,String imagepath) {
		JSONArray responsDataJson = new JSONArray();
		Log log= new Log();
		try {
			JSONArray dataJsonArray = new JSONArray();
			Object[] objList;
			String cost = null;
			String costPerTime = null;// 0 - hour 1-daily 3-monthly
			String lidImagePath = "";
			for(Iterator<Object[]> it=laborDetailObj.iterator();it.hasNext();) {
				objList = it.next();
				JSONObject jsonPack = new JSONObject();
				if (objList[0] != null) {
					jsonPack.put("labor_id", Commonutility.stringToStringempty((String)objList[0]));
					if (Commonutility.checkempty((String)objList[0])) {
						lidImagePath = (String)objList[0] + "/";
					}
				} else  {
					jsonPack.put("labor_id", "");
				}
				if (objList[1] != null) {
					jsonPack.put("labor_name", Commonutility.stringToStringempty((String)objList[1]));
				} else {
					jsonPack.put("labor_name", "");
				}
				if (objList[2] != null) {
					jsonPack.put("email_id", Commonutility.stringToStringempty((String)objList[2]));					
				} else {
					jsonPack.put("email_id", "");
				}
				if (objList[3] != null) {
					jsonPack.put("contact_no", Commonutility.stringToStringempty((String)objList[3]));
				} else {
					jsonPack.put("contact_no", "");
				}
				// objList[4] - web labor image objList[5] - mobile labor image
				if (objList[5] != null) {
					String img = Commonutility.stringToStringempty((String)objList[5]);
					if (Commonutility.checkempty(lidImagePath) && Commonutility.checkempty(img)) {
						jsonPack.put("prof_img", imagepath + lidImagePath + img);
					} else {
						jsonPack.put("prof_img","");
					}
										
				} else {
					jsonPack.put("prof_img","");
				}
				if (objList[6] != null) {
					jsonPack.put("desc", Commonutility.stringToStringempty((String)objList[6]));
					
				} else {
					jsonPack.put("desc","");
				}
				if (objList[7] != null) { // city
					jsonPack.put("location", Commonutility.stringToStringempty((String)objList[7]));
				} else {
					jsonPack.put("location","");
				}
				if (objList[8] != null) {
//					state value
				}
				if (objList[9] != null) {
//					country value
				}
				if (objList[10] != null) {
					jsonPack.put("rating", Commonutility.stringToStringempty((String)objList[10]));
				} else {
					jsonPack.put("rating","");
				}
				if (objList[11] != null) {
					cost = (String)objList[11];
				}
				if (objList[12] != null) {
					costPerTime = (String)objList[12];
				}
				if (Commonutility.checkempty(cost) && Commonutility.checkempty(costPerTime)) {
					if (costPerTime.equalsIgnoreCase("0")) {
						jsonPack.put("rate_card",cost +"  hour");
					} else if (costPerTime.equalsIgnoreCase("1")) {
						jsonPack.put("rate_card",cost +"  daily");
					} else if (costPerTime.equalsIgnoreCase("2")) {
						jsonPack.put("rate_card",cost +"  monthly");
					}
				} else {
					jsonPack.put("rate_card","");
				}
				if (objList[13] != null) {
					String exp =  Commonutility.stringToStringempty((String)objList[13]);
					if (Commonutility.checkempty(exp)) {
						jsonPack.put("work_exp", exp + " years");
					} else {
						jsonPack.put("work_exp","");
					}					
				} else {
					jsonPack.put("work_exp","");
				}
				if (objList[14] != null) {
					jsonPack.put("skill_category", Commonutility.stringToStringempty((String)objList[14]));
				} else {
					jsonPack.put("skill_category","");
				}
				if (objList[15] != null) {
					jsonPack.put("skill_type", Commonutility.stringToStringempty((String)objList[15]));
				} else {
					jsonPack.put("skill_type","");
				}
				
				if (objList[16] != null) {
					jsonPack.put("is_favourite", ""+Commonutility.stringToStringempty((String)objList[16]));
				} else {
					jsonPack.put("is_favourite","");
				}
				responsDataJson.put(jsonPack);
			}
//			responsDataJson.put("labor_det", dataJsonArray);
			
		} catch(Exception ex) {
			log.logMessage("Exception found in :" +ex, "error", JsonpackDaoService.class);
			responsDataJson = null;
		}
		return responsDataJson;
	}

	@Override
	public JSONArray bookingListDetil(List<Object[]> bookingListObj,String imagepath) {
		JSONArray returnJsonData = new JSONArray();
		Log log= new Log();
		try {
			Object[] objList;
			String formatToString="yyyy-MM-dd HH:mm:ss";
			CommonUtils commjvm=new CommonUtils();
			for(Iterator<Object[]> it=bookingListObj.iterator();it.hasNext();) {
				objList = it.next();
				String lbrIdPath = "";
				JSONObject jsonPack = new JSONObject();
				if (objList[0] != null) {
					jsonPack.put("booking_id",Commonutility.intToString((int)objList[0]));
				} else {
					jsonPack.put("booking_id","");
				}				
				if (objList[1] != null) {
					jsonPack.put("problem",Commonutility.stringToStringempty((String)objList[1]));					
				} else {
					jsonPack.put("problem","");
				}
				if (objList[2] != null) {
					jsonPack.put("prefered_datetime",commjvm.dateToString((Date)objList[2], formatToString));
				} else {
					jsonPack.put("prefered_datetime","");
				}
				if (objList[3] != null) {
					jsonPack.put("start_datetime",commjvm.dateToString((Date)objList[3], formatToString));
				} else {
					jsonPack.put("start_datetime","");
				}
				if (objList[4] != null) {
					jsonPack.put("end_datetime",commjvm.dateToString((Date)objList[4], formatToString));
				} else {
					jsonPack.put("end_datetime","");
				}
				if (objList[5] != null) {
					jsonPack.put("service_cost",Commonutility.floatToString((float)objList[5]));
				} else {
					jsonPack.put("service_cost","");
				}
				if (objList[6] != null) {
					jsonPack.put("booking_datetime",Commonutility.dateToString((Date)objList[6]));
				} else {
					jsonPack.put("booking_datetime","");
				}
				System.out.println("--------3");
				if (objList[7] != null) {
					jsonPack.put("labor_id",Commonutility.intToString((int)objList[7]));
					if(Commonutility.checkIntempty((int)objList[7])) {
						lbrIdPath = (int)objList[7] + "/";
					}					
				} else {
					jsonPack.put("labor_id","");
				}
				if (objList[8] != null) {
					jsonPack.put("labor_name",Commonutility.stringToStringempty((String)objList[8]));				
				} else {
					jsonPack.put("labor_name","");
				}
				if (objList[9] != null) {
					if (Commonutility.checkempty(lbrIdPath) && Commonutility.checkempty((String)objList[9])) {
						jsonPack.put("prof_img",imagepath + lbrIdPath + Commonutility.stringToStringempty((String)objList[9]));
					} else {
						jsonPack.put("prof_img","");
					}					
				} else {
					jsonPack.put("prof_img","");
				}
				System.out.println("--------4");
				if (objList[10] != null) {
					jsonPack.put("status",Commonutility.intToString((int)objList[10]));				
				} else {
					jsonPack.put("status","");
				}
				if (objList[11] != null) {
					jsonPack.put("loaction",Commonutility.stringToStringempty((String)objList[11]));
				} else {
					jsonPack.put("loaction","");
				}
				if (objList[12] != null) {
					jsonPack.put("skill_category",Commonutility.stringToStringempty((String)objList[12]));
				} else {
					jsonPack.put("skill_category","");
				}
				if (objList[13] != null) {
					jsonPack.put("skill_type",Commonutility.stringToStringempty((String)objList[13]));
				} else {
					jsonPack.put("skill_type","");
				}
				if (objList[14] != null) {
					jsonPack.put("feedback",Commonutility.stringToStringempty((String)objList[14]));
				} else {
					jsonPack.put("feedback","");
				}
				if (objList[15] != null) {
					jsonPack.put("rating",Commonutility.intToString((int)objList[15]));
				} else {
					jsonPack.put("rating","");
				}	
				System.out.println("--------6");
				returnJsonData.put(jsonPack);
			}
			
		} catch(Exception ex) {
			log.logMessage("Exception found in bookingListDetil:" +ex, "error", JsonpackDaoService.class);
			returnJsonData = null;
		}
		return returnJsonData;
	}

	@Override
	public JSONArray feedCommentListDetil(List<Object[]> feedCommentListObj,String externalUserImagePath) {
		JSONArray returnJsonData = new JSONArray();
		Log log= new Log();
		FunctionUtility common = new FunctionUtilityServices();
		try {
			Object[] objList;
			for(Iterator<Object[]> it=feedCommentListObj.iterator();it.hasNext();) {
				objList = it.next();
				JSONObject jsonPack = new JSONObject();
				System.out.println("-----------");
				String imgPathId = "";
				if (objList[0] != null) {
					jsonPack.put("comment_id",Commonutility.intToString((int)objList[0]));
				} else {
					jsonPack.put("comment_id","");
				}				
				System.out.println("-----------1");
				if (objList[1] != null) {
					jsonPack.put("usr_id",Commonutility.intToString((int)objList[1]));
					imgPathId = Commonutility.intToString((int)objList[1]) + "/";
				} else {
					jsonPack.put("usr_id","");
				}
				System.out.println("-----------2");
				if (objList[2] != null) {
					jsonPack.put("comment",Commonutility.stringToStringempty((String)objList[2]));
				} else {
					jsonPack.put("comment","");
				}
				System.out.println("-----------3");
				if (objList[6] != null) {
					Date entryDateTime = (Date)objList[6];
					jsonPack.put("comment_time",common.getPostedDateTime(entryDateTime));
				} else {
					jsonPack.put("comment_time","");
				}
				System.out.println("-----------4");
				if (objList[4] != null) {					
					jsonPack.put("usr_name",Commonutility.stringToStringempty((String)objList[4]));
				} else {
					jsonPack.put("usr_name","");
				}
				System.out.println("-----------5");
				if (objList[5] != null) {
					jsonPack.put("usr_img",externalUserImagePath + imgPathId + Commonutility.stringToStringempty((String)objList[5]));
				}else if (objList[7] != null) {
					jsonPack.put("usr_img",Commonutility.stringToStringempty((String)objList[7]));
				} else {
					jsonPack.put("usr_img","");
				}				
				
				System.out.println("--------6");
				returnJsonData.put(jsonPack);
			}
			
		} catch(Exception ex) {
			log.logMessage("Exception found in bookingListDetil:" +ex, "error", JsonpackDaoService.class);
			returnJsonData = null;
		}
		return returnJsonData;
	}

	@Override
	public JSONArray feedListDetails(List<Object[]> feedListListObj) {
		JSONArray returnJsonData = new JSONArray();
		Log log= new Log();
		FunctionUtility common = new FunctionUtilityServices();
		try {
			Object[] objList;
			for(Iterator<Object[]> it=feedListListObj.iterator();it.hasNext();) {
				objList = it.next();
				JSONObject jsonPack = new JSONObject();
				System.out.println("-----------");
				if (objList[0] != null) {
					jsonPack.put("feed_id",Commonutility.intToString((int)objList[0]));
				} else {
					jsonPack.put("feed_id","");
				}				
				System.out.println("-----------1");
				if (objList[1] != null) {
					jsonPack.put("feed_type",Commonutility.intToString((int)objList[1]));					
				} else {
					jsonPack.put("feed_type","");
				}
				System.out.println("-----------2");
				JSONArray list = new JSONArray();
				if (objList[2] != null) {
					
					jsonPack.put("comment",Commonutility.stringToStringempty((String)objList[2]));
				} else {
					jsonPack.put("comment","");
				}
				System.out.println("-----------3");
				if (objList[6] != null) {
					Date entryDateTime = (Date)objList[6];
					jsonPack.put("comment_time",common.getPostedDateTime(entryDateTime));
				} else {
					jsonPack.put("comment_time","");
				}
				System.out.println("-----------4");
				if (objList[4] != null) {
					jsonPack.put("usr_name",Commonutility.stringToStringempty((String)objList[4]));
				} else {
					jsonPack.put("usr_name","");
				}
				System.out.println("-----------5");
				if (objList[5] != null) {
					jsonPack.put("usr_img",Commonutility.stringToStringempty((String)objList[5]));
				} else {
					jsonPack.put("usr_img","");
					
				}				
				
				System.out.println("--------6");
				returnJsonData.put(jsonPack);
			}
			
		} catch(Exception ex) {
			log.logMessage("Exception found in bookingListDetil:" +ex, "error", JsonpackDaoService.class);
			returnJsonData = null;
		}
		return returnJsonData;
	}

	@Override
	public JSONArray privacyListCategory() {
		JSONArray returnJsonData = new JSONArray();
		Log log= new Log();
		try {
			JSONObject jsonPack = new JSONObject();
			jsonPack.put("cate_type","1");
			jsonPack.put("cate_name",getText("cate.type.1"));
			jsonPack.put("cate_url",getText("SOCIAL_INDIA_BASE_URL") +getText("external.templogo")+getText("external.cate.img.path")+"1"+getText("external.cate.img.path.extension"));
			jsonPack.put("is_list_users","0");
			returnJsonData.put(jsonPack);
			jsonPack = new JSONObject();
			jsonPack.put("cate_type","2");
			jsonPack.put("cate_name",getText("cate.type.2"));
			jsonPack.put("cate_url",getText("SOCIAL_INDIA_BASE_URL") +getText("external.templogo")+getText("external.cate.img.path")+"2"+getText("external.cate.img.path.extension"));
			jsonPack.put("is_list_users","0");			
			returnJsonData.put(jsonPack);
			jsonPack = new JSONObject();
			jsonPack.put("cate_type","3");
			jsonPack.put("cate_name",getText("cate.type.3"));
			jsonPack.put("cate_url",getText("SOCIAL_INDIA_BASE_URL") +getText("external.templogo")+getText("external.cate.img.path")+"3"+getText("external.cate.img.path.extension"));
			jsonPack.put("is_list_users","0");
			returnJsonData.put(jsonPack);
			jsonPack = new JSONObject();
			jsonPack.put("cate_type","4");
			jsonPack.put("cate_name",getText("cate.type.4"));
			jsonPack.put("cate_url",getText("SOCIAL_INDIA_BASE_URL") +getText("external.templogo")+getText("external.cate.img.path")+"4"+getText("external.cate.img.path.extension"));
			jsonPack.put("is_list_users","1");	
			returnJsonData.put(jsonPack);			
			
		} catch(Exception ex) {
			log.logMessage("Exception found in privacyListCategory:" +ex, "error", JsonpackDaoService.class);
			returnJsonData = null;
		}
		return returnJsonData;
	}

	@Override
	public JSONArray privacyListUsers(List<Object[]> privacyListUserObj,String imagePath) {
		JSONArray returnJsonData = new JSONArray();
		Log log= new Log();
		FunctionUtility common = new FunctionUtilityServices();
		try {
			Object[] objList;
			for(Iterator<Object[]> it=privacyListUserObj.iterator();it.hasNext();) {
				objList = it.next();
				JSONObject jsonPack = new JSONObject();
				System.out.println("-----------");
				String uidPath="";
				if (objList[0] != null) {
					jsonPack.put("user_id",Commonutility.stringToStringempty((String)objList[0]));
					if (Commonutility.checkempty((String)objList[0])) {
						uidPath = Commonutility.stringToStringempty((String)objList[0]) + "/";
					}
					
				} else {
					jsonPack.put("user_id","");
				}				
				System.out.println("-----------1");
				String pfName=(String)objList[1];
				if (pfName != null && pfName.length()>0) {
					jsonPack.put("profile_name",Commonutility.stringToStringempty((String)objList[1]));					
				} else {
					jsonPack.put("profile_name",getText("anonymous.user.name"));
				}
				System.out.println("-----------2");
				JSONArray list = new JSONArray();
					String imgPath = "";
					if (Commonutility.checkempty(uidPath) && Commonutility.checkempty((String)objList[2])) {
						imgPath = imagePath + uidPath + Commonutility.stringToStringempty((String)objList[2]);
						jsonPack.put("profilepic",imgPath);
						jsonPack.put("profilepic_thumbnail",imgPath);
					}else if (Commonutility.checkempty((String)objList[5])) {
						imgPath = Commonutility.stringToStringempty((String)objList[5]);
						jsonPack.put("profilepic",imgPath);
						jsonPack.put("profilepic_thumbnail",imgPath);
					}else{
						jsonPack.put("profilepic","");
						jsonPack.put("profilepic_thumbnail","");
					}	
				System.out.println("-----------3");
				if (objList[3] != null) {					
					jsonPack.put("flat_no",Commonutility.stringToStringempty((String)objList[3]));
				} else {
					jsonPack.put("flat_no","");
				}				
				System.out.println("--------5");
				if (objList[4] != null) {					
					jsonPack.put("is_ur_friend",Commonutility.stringToStringempty((String)objList[4]));
				} else {
					jsonPack.put("is_ur_friend","");
				}
				System.out.println("--------6");
				returnJsonData.put(jsonPack);
			}
			
		} catch(Exception ex) {
			log.logMessage("Exception found in bookingListDetil:" +ex, "error", JsonpackDaoService.class);
			returnJsonData = null;
		}
		return returnJsonData;
	}

	@Override
	public JSONArray listOfGroupPrivateContact(
			List<Object[]> privacyListUserObj, String imagePath) {
		JSONArray returnJsonData = new JSONArray();
		Log log= new Log();
		FunctionUtility common = new FunctionUtilityServices();
		try {
			Object[] objList;
			for(Iterator<Object[]> it=privacyListUserObj.iterator();it.hasNext();) {
				objList = it.next();
				JSONObject jsonPack = new JSONObject();
				String imgPath = "";
				System.out.println("-----------");
				if (objList[0] != null) {
					jsonPack.put("id",Commonutility.stringToStringempty((String)objList[0]));
					imgPath = (String)objList[0] + "/";
				} else {
					jsonPack.put("id","");
				}				
				System.out.println("-----------1");
				if (objList[1] != null) {
					jsonPack.put("name",Commonutility.stringToStringempty((String)objList[1]));					
				} else {
					jsonPack.put("name","");
				}
				System.out.println("-----------2");
				if (objList[2] != null) {
					if (Commonutility.checkempty(Commonutility.stringToStringempty((String)objList[2]))) {
						imagePath = imagePath + imgPath + Commonutility.stringToStringempty((String)objList[2]) ;
					} else {
						imagePath = "";
					}					
					jsonPack.put("img",imagePath);
				} else {
					jsonPack.put("img","");
				}
				System.out.println("-----------3");
				if (objList[3] != null) {
					String lastSeen = Commonutility.stringToStringempty((String)objList[3]) ;
					if (Commonutility.checkempty(lastSeen)) {
						Date date = new Date();
						date = Commonutility.stringtoTimestamp(lastSeen);
						lastSeen = common.getPostedDateTime(date);
					} else {
						lastSeen = "You added into group";
					}					
					jsonPack.put("last_access_time",lastSeen);
				} else {
					jsonPack.put("last_access_time","");
				}
				System.out.println("--------6");
				returnJsonData.put(jsonPack);
			}
			
		} catch(Exception ex) {
			log.logMessage("Exception found in listOfGroupPrivateContact:" +ex, "error", JsonpackDaoService.class);
			returnJsonData = null;
		}
		return returnJsonData;
	}

	@Override
	public JSONArray listOfGroupMsg(List<Object[]> groupMsgObj,String imagePath) {
		JSONArray returnJsonData = new JSONArray();
		Log log= new Log();
		FunctionUtility common = new FunctionUtilityServices();
		try {
			Object[] objList;
			for(Iterator<Object[]> it=groupMsgObj.iterator();it.hasNext();) {
				objList = it.next();
				JSONObject jsonPack = new JSONObject();
				System.out.println("-----------");
				if (objList[0] != null) {
					jsonPack.put("id",Commonutility.stringToStringempty((String)objList[0]));
				} else {
					jsonPack.put("id","");
				}				
				System.out.println("-----------1");
				if (objList[1] != null) {
					jsonPack.put("name",Commonutility.stringToStringempty((String)objList[1]));					
				} else {
					jsonPack.put("name","");
				}
				System.out.println("-----------11");
				if (objList[2] != null) {
					jsonPack.put("msg",Commonutility.stringToStringempty((String)objList[2]));					
				} else {
					jsonPack.put("msg","");
				}
				System.out.println("-----------2");
				if (objList[3] != null) {
					imagePath = imagePath + Commonutility.stringToStringempty((String)objList[3]) ;
					jsonPack.put("attachment",imagePath);
				} else {
					jsonPack.put("attachment","");
				}
				System.out.println("-----------3");
				if (objList[4] != null) {
					String lastSeen = Commonutility.stringToStringempty((String)objList[4]) ;
					if (Commonutility.checkempty(lastSeen)) {
						Date date = new Date();
						date = Commonutility.stringtoTimestamp(lastSeen);
						lastSeen = common.getPostedDateTime(date);
					} else {
						lastSeen = "You added into group";
					}					
					jsonPack.put("last_access_time",lastSeen);
				} else {
					jsonPack.put("last_access_time","");
				}
				System.out.println("--------6");
				returnJsonData.put(jsonPack);
			}
			
		} catch(Exception ex) {
			log.logMessage("Exception found in listOfGroupPrivateContact:" +ex, "error", JsonpackDaoService.class);
			returnJsonData = null;
		}
		return returnJsonData;
	}

	@Override
	public JSONArray listOfGrpMsgPrivateMsg(List<Object[]> privateMsgObj,String profileimgPath,String imagePathWeb,String imagePathMobi,String videoPath,String videoPathThumb,int groupContactFlg) {
		JSONArray returnJsonData = new JSONArray();
		Log log= new Log();
		FunctionUtility common = new FunctionUtilityServices();
		CommonMobiDao commonServ = new CommonMobiDaoService();
		try {
			Object[] objList;
			UserMasterTblVo usrmas = new UserMasterTblVo();
			for(Iterator<Object[]> it=privateMsgObj.iterator();it.hasNext();) {
				objList = it.next();
				JSONObject jsonPack = new JSONObject();
				System.out.println("-----------");
				String msgIdPath = "";
				if (objList[0] != null) {
					jsonPack.put("msgid",Commonutility.stringToStringempty((String)objList[0]));
					msgIdPath = (String)objList[0] + "/";
				} else {
					jsonPack.put("msgid","");
				}
				System.out.println("-----------11");
				if (objList[1] != null) {
					jsonPack.put("usrid",Commonutility.stringToStringempty((String)objList[1]));	
					usrmas = commonServ.getProfileDetails(Commonutility.stringToInteger((String)objList[1]));
				} else {
					jsonPack.put("usrid","");
				}
				System.out.println("-----------11");
				String profileImgname = "";
				String profileImgUrl = "";
				if (objList[2] != null && usrmas != null) {
					profileImgname = Commonutility.stringToStringempty(usrmas.getImageNameMobile());
					profileImgUrl = profileimgPath +usrmas.getUserId()+"/"+ profileImgname ;
					jsonPack.put("usr_img",profileImgUrl);	
					profileImgname = "";
					profileImgUrl = "";
				} else {
					jsonPack.put("usr_img","");
				}
				System.out.println("-----------2");
				if (objList[3] != null) {
					jsonPack.put("content",Commonutility.stringToStringempty((String)objList[3]));
				} else {
					jsonPack.put("content","");
				}
				System.out.println("----21-------2");
				if (objList[4] != null) {
					jsonPack.put("msgtype",Commonutility.stringToStringempty((String)objList[4]));
				} else {
					jsonPack.put("msgtype","");
				}				
				System.out.println("-----------3");
				if (objList[5] != null) {
					String lastSeen = Commonutility.stringToStringempty((String)objList[5]) ;
					String shortTime = "";
					if (Commonutility.checkempty(lastSeen)) {
						shortTime = common.getShortTime(lastSeen);
						lastSeen = common.getLongTime(lastSeen);
					}					
					jsonPack.put("short_time",shortTime);
					jsonPack.put("full_time",lastSeen);
				} else {
					jsonPack.put("short_time","");
					jsonPack.put("full_time","");
				}
				
				JSONArray urlArray=new JSONArray();
				String urlThumbnail = null;
				String urlTittle = null;
				String pageurl = null;
				if (objList[7] != null) {
					urlThumbnail = Commonutility.stringToStringempty((String)objList[7]);
				}else{
					urlThumbnail = "";
				}
				if (objList[8] != null) {
					urlTittle = Commonutility.stringToStringempty((String)objList[8]);
				}else{
					urlTittle = "";
				}
				if (objList[9] != null) {
					pageurl = Commonutility.stringToStringempty((String)objList[9]);
				}else{
					pageurl = "";
				}
				if (Commonutility.checkempty(urlThumbnail) && Commonutility.checkempty(urlTittle) && Commonutility.checkempty(pageurl)) {
					ArrayList<String> thumbList = new ArrayList<String>();
					JsonSimplepackDaoService jsonsimlObj = new JsonSimplepackDaoService();
					thumbList = jsonsimlObj.spTabSplitIntoArraylist(urlThumbnail);
					ArrayList<String> pageTittleList = new ArrayList<String>();
					pageTittleList = jsonsimlObj.spTabSplitIntoArraylist(urlTittle);
					ArrayList<String> pageurlList = new ArrayList<String>();
					pageurlList = jsonsimlObj.spTabSplitIntoArraylist(pageurl);					
					System.out.println("uu---"+thumbList.size());
					System.out.println("---io--:"+pageTittleList.size());
					System.out.println("--kl--:"+pageurlList.size());
					if (thumbList.size() == pageTittleList.size()) {
						if ( pageTittleList.size() == pageurlList.size()) {
							for (int i=0;i<thumbList.size();i++) {
								JSONObject dataJsonObj = new JSONObject();
								dataJsonObj.put("thumb_img", thumbList.get(i));
								dataJsonObj.put("title", pageTittleList.get(i));
								dataJsonObj.put("pageurl", pageurlList.get(i));
								urlArray.put(dataJsonObj);
								dataJsonObj=null;
							}
						}						
					}
					if (urlArray != null) {
						jsonPack.put("urls",urlArray);
					} else {
						jsonPack.put("urls",urlArray);
					}
				} else {
					jsonPack.put("urls",urlArray);
				}
				/*if (objList[10] != null) {
					urlObj.put("to_id",objList[10]);
				}else{
					urlObj.put("to_id","");
				}*/
				System.out.println("--------6");
				//JSONArray jsArr = new JSONArray();
				//JSONObject locJsonObj = new JSONObject();
				if (objList[6] != null) {
					if (groupContactFlg == 2) {
						MessageDAOService msgService = new MessageDAOService();
						String chatAttUniqId = (String)objList[0];
						int varDonatoinAttach = 0;
						if (Commonutility.checkempty(chatAttUniqId)) {
							varDonatoinAttach = Commonutility.stringToInteger(chatAttUniqId);
						}
						JSONObject comVido = new JSONObject();
						JSONObject images = new JSONObject();
						JSONArray imagesArr = new JSONArray();
						JSONArray comVidoArr = new JSONArray();
						boolean flag = false;
						List<ChatAttachTblVO> chatAttachList = new ArrayList<ChatAttachTblVO>();
						chatAttachList = msgService.getChatAttachList(varDonatoinAttach);
						System.out.println("===chatAttachList==="+chatAttachList.size());
						
						if (chatAttachList != null) {
							ChatAttachTblVO chatAttachObj = new ChatAttachTblVO();
							for (Iterator<ChatAttachTblVO> itObj = chatAttachList.iterator();itObj.hasNext();) {
								chatAttachObj = itObj.next();
								flag = true;
										images = new JSONObject();
										comVido = new JSONObject();
											if (chatAttachObj.getFileType() == 1) {
												if (chatAttachObj.getFileType()!=null) {
												images.put("img_id",String.valueOf(chatAttachObj.getUniqId()));
												} else {
												images.put("img_id","");
												} if(chatAttachObj.getAttachName()!=null) {
												images.put("img_url",imagePathMobi + msgIdPath + chatAttachObj.getAttachName());
												} else {
												images.put("img_url","");
												}
											}
											if (chatAttachObj.getFileType() == 2) {
												System.out.println("==thumb_img===="+ chatAttachObj.getThumpImage());
												if (chatAttachObj.getFileType()!=null) {
												comVido.put("video_id",String.valueOf(chatAttachObj.getUniqId()));
												} else {
												comVido.put("video_id","");
												} if (chatAttachObj.getThumpImage()!=null) {
												comVido.put("thumb_img", videoPathThumb + msgIdPath + Commonutility.stringToStringempty(chatAttachObj.getThumpImage()));
												} else {
												comVido.put("thumb_img","");
												} if (chatAttachObj.getAttachName()!=null) {
												comVido.put("video_url", videoPath + msgIdPath +  Commonutility.stringToStringempty(chatAttachObj.getAttachName()));
												} else {
												comVido.put("video_url","");
												}
											}
											if (images!=null && images.length()>0) {
												imagesArr.put(images);
												jsonPack.put("images",imagesArr);
											} else {
												jsonPack.put("images",imagesArr); 
											}
											if (comVido!=null && comVido.length()>0) {
												comVidoArr.put(comVido);
												jsonPack.put("videos",comVidoArr);
											} else {
												jsonPack.put("videos",comVidoArr); 
											}
							}
							if (flag == false) {
								jsonPack.put("images",imagesArr);
								jsonPack.put("videos",comVidoArr);
							}
						}
						
					} if (groupContactFlg == 3) {
						MessageDAOService msgService = new MessageDAOService();
						String chatAttUniqId = (String)objList[0];
						int varDonatoinAttach = 0;
						if (Commonutility.checkempty(chatAttUniqId)) {
							varDonatoinAttach = Commonutility.stringToInteger(chatAttUniqId);
						}
						JSONObject comVido = new JSONObject();
						JSONObject images = new JSONObject();
						JSONArray imagesArr = new JSONArray();
						JSONArray comVidoArr = new JSONArray();
						boolean flag = false;
						
						List<GrpChatAttachTblVO> grpChatAttachList = new ArrayList<GrpChatAttachTblVO>();
						grpChatAttachList = msgService.getGroupChatAttachList(varDonatoinAttach);
						System.out.println("===grpChatAttachList==="+grpChatAttachList.size());
						if (grpChatAttachList != null) {
							GrpChatAttachTblVO grpChatAttachObj = new GrpChatAttachTblVO();
							for (Iterator<GrpChatAttachTblVO> itObj = grpChatAttachList.iterator();itObj.hasNext();) {
								grpChatAttachObj = itObj.next();
								flag = true;
								images = new JSONObject();
								comVido = new JSONObject();
									if (grpChatAttachObj.getFileType() == 1) {
										if (grpChatAttachObj.getFileType()!=null) {
											images.put("img_id",String.valueOf(grpChatAttachObj.getUniqId()));
										} else {
											images.put("img_id","");
										} if(grpChatAttachObj.getAttachName()!=null) {
											images.put("img_url",imagePathMobi + msgIdPath + grpChatAttachObj.getAttachName());
										} else {
											images.put("img_url","");
										}
									}
									if (grpChatAttachObj.getFileType() == 2) {
											System.out.println("==thumb_img===="+ grpChatAttachObj.getThumpImage());
										if (grpChatAttachObj.getFileType()!=null) {
											comVido.put("video_id",String.valueOf(grpChatAttachObj.getUniqId()));
										} else {
											comVido.put("video_id","");
										} if (grpChatAttachObj.getThumpImage()!=null) {
											comVido.put("thumb_img", videoPathThumb + msgIdPath + Commonutility.stringToStringempty(grpChatAttachObj.getThumpImage()));
										} else {
											comVido.put("thumb_img","");
										} if (grpChatAttachObj.getAttachName()!=null) {
											comVido.put("video_url", videoPath + msgIdPath +  Commonutility.stringToStringempty(grpChatAttachObj.getAttachName()));
										} else {
											comVido.put("video_url","");
										}
									}
										if (images!=null && images.length()>0) {
											imagesArr.put(images);
											jsonPack.put("images",imagesArr);
										} else {
											jsonPack.put("images",imagesArr); 
										}
										if (comVido!=null && comVido.length()>0) {
											comVidoArr.put(comVido);
											jsonPack.put("videos",comVidoArr);
										} else {
											jsonPack.put("videos",comVidoArr); 
										}
							}
							if (flag == false) {
								jsonPack.put("images",imagesArr);
								jsonPack.put("videos",comVidoArr);
							}
						}
					}
					
					/*String fileList = (String) objList[6];
					boolean fg = false;
					if (Commonutility.checkempty(fileList)) {
						if (fileList.contains("<SP>")) {
							String[] fileArr = fileList.split("<SP>");
							if (fileArr.length == 4) {
								String fileType = fileArr[3];
								if (Commonutility.checkempty(fileType) && Commonutility.checkLengthNotZero(fileType)) {									
									fileType = fileType.trim();
									fg = true;
									if (fileType.equalsIgnoreCase("2")) {										
										locJsonObj.put("img_id", Commonutility.stringToStringempty(fileArr[0]));
										locJsonObj.put("img_url", imagePathMobi + msgIdPath + Commonutility.stringToStringempty(fileArr[1]));
										jsArr.put(locJsonObj);
									} else if (fileType.equalsIgnoreCase("3")) {
										locJsonObj.put("video_id", Commonutility.stringToStringempty(fileArr[0]));
										locJsonObj.put("video_url", videoPath + msgIdPath +  Commonutility.stringToStringempty(fileArr[1]));
										locJsonObj.put("thumb_img", videoPathThumb + msgIdPath + Commonutility.stringToStringempty(fileArr[2]));
										jsArr.put(locJsonObj);
									}else if (fileType.equalsIgnoreCase("4")) {
										locJsonObj.put("url_id", Commonutility.stringToStringempty(fileArr[0]));
										locJsonObj.put("pageurl", videoPath + msgIdPath +  Commonutility.stringToStringempty(fileArr[1]));
										locJsonObj.put("thumb_img", videoPathThumb + msgIdPath + Commonutility.stringToStringempty(fileArr[2]));
										jsArr.put(locJsonObj);
									}									
								}
							}
						}
					}*/					
				}
				//jsonPack.put("images",jsArr);
				//jsonPack.put("videos",jsArr);
				returnJsonData.put(jsonPack);
			}
			
		} catch(Exception ex) {
			log.logMessage("Exception found in listOfGrpMsgPrivateMsg:" +ex, "error", JsonpackDaoService.class);
			returnJsonData = null;
		}
		return returnJsonData;
	}

	@Override
	public JSONObject loginUserDetail_t(UserMasterTblVo userObj,String profileImgPath,String societyImgPath,int committeeGrpcode,int residenteGrpcode) {
		JSONObject responsDataJson = new JSONObject();
		Log log= new Log();
		try {
			log.logMessage("Enter into loginUserDetail ", "info", JsonpackDaoService.class);
			JSONArray jsonArry = new JSONArray();
			if (userObj != null) {
				JSONObject locJsonObj = new JSONObject();
				// Society details 
				SocietyMstTbl socityMstObj = new SocietyMstTbl();
				socityMstObj = userObj.getSocietyId();
				if (userObj.getSocietyId() != null) {
					if (Commonutility.checkempty(userObj.getSocietyId().getLogoImage())) {
						responsDataJson.put("societylogo", societyImgPath + userObj.getSocietyId().getLogoImage());
					}else if (Commonutility.checkempty(userObj.getSocietyId().getTownshipId().getTownshiplogoname())) {
						responsDataJson.put("societylogo", societyImgPath + userObj.getSocietyId().getTownshipId().getTownshiplogoname());
					} else {
						responsDataJson.put("societylogo", "");
					}				
					responsDataJson.put("societyname", Commonutility.stringToStringempty(userObj.getSocietyId().getSocietyName()));
					responsDataJson.put("societydesc", Commonutility.stringToStringempty(userObj.getSocietyId().getImprintName()));
					responsDataJson.put("societykey", Commonutility.stringToStringempty(userObj.getSocietyId().getActivationKey()));
					responsDataJson.put("totalusers", Commonutility.intToString(userObj.getSocietyId().getNoOfMemebers()));
				} else {
					responsDataJson.put("societylogo","" );
					responsDataJson.put("societyname","" );
					responsDataJson.put("societykey", "");
					responsDataJson.put("societydesc", "");
					responsDataJson.put("totalusers", "");
				}				
//				jsonArry.put(locJsonObj);
//				/responsDataJson.put("societies", jsonArry);
				responsDataJson.put("profile_name", Commonutility.stringToStringempty(userObj.getFirstName() + " " + Commonutility.stringToStringempty(userObj.getLastName())));				
				CityMasterTblVO cityObj = new CityMasterTblVO();
				cityObj = userObj.getCityId();
				if (cityObj != null) {
					responsDataJson.put("profile_location", Commonutility.stringToStringempty(cityObj.getCityName()));
				} else {
					responsDataJson.put("profile_location", "");
				}
				if (Commonutility.checkempty(userObj.getImageNameMobile())) {
					String imageName = FilenameUtils.getBaseName(userObj.getImageNameMobile());
					String limageFomat = FilenameUtils.getExtension(userObj.getImageNameMobile());
					responsDataJson.put("profilepic_thumbnail", profileImgPath + imageName + "_small." + limageFomat);
					responsDataJson.put("profilepic", profileImgPath + userObj.getImageNameMobile());
				} else {
					responsDataJson.put("profilepic_thumbnail", "");
					responsDataJson.put("profilepic", "");
				}
				responsDataJson.put("profile_email", Commonutility.stringToStringempty(userObj.getEmailId()));
				responsDataJson.put("profile_mobile", Commonutility.stringToStringempty(userObj.getMobileNo()));				
				IDCardMasterTblVO inCardObj = new IDCardMasterTblVO();
				inCardObj = userObj.getiVOcradid();
				if (inCardObj != null) {
					responsDataJson.put("profile_proof_id", Commonutility.intToString(inCardObj.getiVOcradid()));
					responsDataJson.put("profile_proof_name", Commonutility.stringToStringempty(inCardObj.getiVOcradname()));
				} else {
					responsDataJson.put("profile_proof_id", "");
					responsDataJson.put("profile_proof_name", "");
				}
				responsDataJson.put("profile_proof_value", Commonutility.stringToStringempty(userObj.getIdProofNo()));
				if (Commonutility.checkIntempty(userObj.getParentId())) {
					log.logMessage("Enter into loginUserDetail parent id:" + userObj.getParentId(), "info", JsonpackDaoService.class);
					otpDao otp = new otpDaoService();
					UserMasterTblVo userData = new UserMasterTblVo();
					userData = otp.getParentForRidDetails(String.valueOf(userObj.getParentId()), "",committeeGrpcode, residenteGrpcode);
				/*	if (Commonutility.checkempty(userData.getImageNameMobile())) {
						responsDataJson.put("ppr_profilepic_thumbnail",profileImgPath + userData.getUserId() + "/"+ userData.getImageNameMobile());
						responsDataJson.put("ppr_profilepic",profileImgPath + userData.getUserId() + "/"+ userData.getImageNameMobile());
					} else if (Commonutility.checkempty(userData.getSocialProfileUrl())) { 
						responsDataJson.put("ppr_profilepic_thumbnail",userData.getSocialProfileUrl());
						responsDataJson.put("ppr_profilepic",userData.getSocialProfileUrl());
					} else {
						responsDataJson.put("ppr_profilepic", "");
						responsDataJson.put("ppr_profilepic_thumbnail", "");
					}*/
					if(userObj.getImageNameMobile()!=null && !userObj.getImageNameMobile().equalsIgnoreCase("")){
						String imageName = FilenameUtils.getBaseName(userObj.getImageNameMobile());
						String limageFomat = FilenameUtils.getExtension(userObj.getImageNameMobile());
						responsDataJson.put("profilepic_thumbnail", getText("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+userObj.getUserId()+"/"+imageName+"_small."+limageFomat);
						responsDataJson.put("profilepic", getText("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+userObj.getUserId()+"/"+userObj.getImageNameMobile());
					}else if(userObj.getSocialProfileUrl()!=null && !userObj.getSocialProfileUrl().equalsIgnoreCase("")){
						responsDataJson.put("profilepic_thumbnail", userObj.getSocialProfileUrl());
						responsDataJson.put("profilepic", userObj.getSocialProfileUrl());
					}else{
						responsDataJson.put("profilepic_thumbnail", "");
						responsDataJson.put("profilepic", "");
					}
					responsDataJson.put("ppr_name", Commonutility.stringToStringempty(userData.getFirstName()));
					if (userData.getSocietyId() !=null) {
						responsDataJson.put("ppr_desc", Commonutility.stringToStringempty(userData.getFirstName()));
					} else {
						responsDataJson.put("ppr_desc", "");
					}
					responsDataJson.put("ppr_mobile", Commonutility.stringToStringempty(userData.getMobileNo()));
					responsDataJson.put("ppr_block", Commonutility.stringToStringempty(userData.getFlatNo()));
					log.logMessage("######ppr  Id::" + userData.getUserId(), "info", JsonpackDaoService.class);
					responsDataJson.put("ppr_id", Commonutility.intToString(userData.getUserId()));
				} else {
					log.logMessage("Enter into loginUserDetail ParentId empty", "info", JsonpackDaoService.class);
					responsDataJson.put("ppr_profilepic", "");
					responsDataJson.put("ppr_profilepic_thumbnail", "");
					responsDataJson.put("ppr_name", "");
					responsDataJson.put("ppr_desc", "");
					responsDataJson.put("ppr_mobile", "");
					responsDataJson.put("ppr_block", "");
					responsDataJson.put("ppr_id", "");
				}
				CommonMobiDao commonServ = new CommonMobiDaoService();
				String locVrSlQry = "SELECT count(notificationId) FROM NotificationTblVO where statusFlag='1' and readStatus=1 and userId.userId='"+userObj.getUserId()+"'";
				int count = commonServ.getTotalCountQuery(locVrSlQry);
				log.logMessage("####### Notification Count = :" + count + " #######", "info", JsonpackDaoService.class);
				mobiCommon mobcom=new mobiCommon();
				int percentageCount=mobcom.getProfilePercentageCal(userObj);	
				log.logMessage("####### percentageCount = :" + percentageCount + " #######", "info", JsonpackDaoService.class);
				responsDataJson.put("profile_percentage", Commonutility.intToString(percentageCount));
				responsDataJson.put("notification_count", Commonutility.intToString(count));
			} else {
				responsDataJson.put("societies", jsonArry);
			}
			
		} catch (Exception ex) {
			log.logMessage("Exception found in loginUserDetail:" + ex, "error", JsonpackDaoService.class);
			responsDataJson = null;
		}
		return responsDataJson;
	}

	@Override
	public JSONArray loginUserDetail(String userid, String profileImgPath,
			String societyImgPath, int committeeGrpcode, int residenteGrpcode) {
		Log log = new Log();
		JSONObject returnJson = new JSONObject();
		JSONArray responsDataJsonarr = new JSONArray();
		List<UserMasterTblVo> userInfoList = new ArrayList<UserMasterTblVo>();
		profileDao profile = new profileDaoServices();
		try {
			userInfoList = profile.getUserSocietyDeatils(userid,committeeGrpcode, residenteGrpcode, "");
			System.out.println("==userInfoList=====" + userInfoList.size());
			JsonpackDao jsonPack = new JsonpackDaoService(); 
			if (userInfoList != null && userInfoList.size() > 0) {
				UserMasterTblVo userObj;
				CommonMobiDao commonServ = new CommonMobiDaoService();
				for (Iterator<UserMasterTblVo> it = userInfoList.iterator(); it.hasNext();) {
					userObj = it.next();
					JSONObject responsDataJson = new JSONObject();
					
					// Society details 
					if (userObj.getSocietyId() != null) {
						if (Commonutility.checkempty(userObj.getSocietyId().getLogoImage())) {
							responsDataJson.put("societylogo", societyImgPath+userObj.getSocietyId().getSocietyId()+"/" + userObj.getSocietyId().getLogoImage());
						}else if (Commonutility.checkempty(userObj.getSocietyId().getTownshipId().getTownshiplogoname())) {
							societyImgPath=societyImgPath.replaceAll("/society/", "/township/");
							responsDataJson.put("societylogo", societyImgPath+userObj.getSocietyId().getTownshipId().getTownshipId()+"/" + userObj.getSocietyId().getTownshipId().getTownshiplogoname());
						} else {
							responsDataJson.put("societylogo", "");
						}				
						responsDataJson.put("societyname", Commonutility.stringToStringempty(userObj.getSocietyId().getSocietyName()));
						responsDataJson.put("societydesc", Commonutility.stringToStringempty(userObj.getSocietyId().getImprintName()));
						responsDataJson.put("societykey", Commonutility.stringToStringempty(userObj.getSocietyId().getActivationKey()));
						responsDataJson.put("totalusers", Commonutility.intToString(userObj.getSocietyId().getNoOfMemebers()));
					} else {
						responsDataJson.put("societylogo","" );
						responsDataJson.put("societyname","" );
						responsDataJson.put("societykey", "");
						responsDataJson.put("societydesc", "");
						responsDataJson.put("totalusers", "");
					}				
//					jsonArry.put(locJsonObj);
//					/responsDataJson.put("societies", jsonArry);
					responsDataJson.put("profile_name", Commonutility.stringToStringempty(userObj.getFirstName() + " " + Commonutility.stringToStringempty(userObj.getLastName())));				
					CityMasterTblVO cityObj = new CityMasterTblVO();
					cityObj = userObj.getCityId();
					if (cityObj != null) {
						responsDataJson.put("profile_location", Commonutility.stringToStringempty(cityObj.getCityName()));
					} else {
						responsDataJson.put("profile_location", "");
					}
					/*if (Commonutility.checkempty(userObj.getImageNameMobile())) {
						String imageName = FilenameUtils.getBaseName(userObj.getImageNameMobile());
						String limageFomat = FilenameUtils.getExtension(userObj.getImageNameMobile());
						responsDataJson.put("profilepic_thumbnail", profileImgPath + imageName + "_small." + limageFomat);
						responsDataJson.put("profilepic", profileImgPath + userObj.getImageNameMobile());
					} else {
						responsDataJson.put("profilepic_thumbnail", "");
						responsDataJson.put("profilepic", "");
					}*/
					if(userObj.getImageNameMobile()!=null && !userObj.getImageNameMobile().equalsIgnoreCase("")){
						String imageName = FilenameUtils.getBaseName(userObj.getImageNameMobile());
						String limageFomat = FilenameUtils.getExtension(userObj.getImageNameMobile());
						responsDataJson.put("profilepic_thumbnail", getText("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+userObj.getUserId()+"/"+imageName+"_small."+limageFomat);
						responsDataJson.put("profilepic", getText("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+userObj.getUserId()+"/"+userObj.getImageNameMobile());
					}else if(userObj.getSocialProfileUrl()!=null && !userObj.getSocialProfileUrl().equalsIgnoreCase("")){
						responsDataJson.put("profilepic_thumbnail", userObj.getSocialProfileUrl());
						responsDataJson.put("profilepic", userObj.getSocialProfileUrl());
					}else{
						responsDataJson.put("profilepic_thumbnail", "");
						responsDataJson.put("profilepic", "");
					}
					responsDataJson.put("profile_email", Commonutility.stringToStringempty(userObj.getEmailId()));
					responsDataJson.put("profile_mobile", Commonutility.stringToStringempty(userObj.getMobileNo()));
					responsDataJson.put("rid", Commonutility.intToString(userObj.getUserId()));
					IDCardMasterTblVO inCardObj = new IDCardMasterTblVO();
					inCardObj = userObj.getiVOcradid();
					if (inCardObj != null) {
						responsDataJson.put("profile_proof_id", Commonutility.intToString(inCardObj.getiVOcradid()));
						responsDataJson.put("profile_proof_name", Commonutility.stringToStringempty(inCardObj.getiVOcradname()));
					} else {
						responsDataJson.put("profile_proof_id", "");
						responsDataJson.put("profile_proof_name", "");
					}
					responsDataJson.put("profile_proof_value", Commonutility.stringToStringempty(userObj.getIdProofNo()));
//					GroupMasterTblVo groupCodeObj = new GroupMasterTblVo();
//					groupCodeObj = userObj.getGroupCode();
					if (userObj.getGroupCode() != null) {
						if (userObj.getGroupCode().getGroupCode() != 0) {
							responsDataJson.put("group_code", Commonutility.intToString(userObj.getGroupCode().getGroupCode()));
						} else {
							responsDataJson.put("group_code", "");
						}
					} else {
						responsDataJson.put("group_code", "");
					}
					
					if (Commonutility.checkIntempty(userObj.getParentId())) {
						log.logMessage("Enter into loginUserDetail parent id:" + userObj.getParentId(), "info", JsonpackDaoService.class);
						otpDao otp = new otpDaoService();
						UserMasterTblVo userData = new UserMasterTblVo();
						userData = otp.getParentForRidDetails(String.valueOf(userObj.getParentId()), "",committeeGrpcode, residenteGrpcode);
						if (Commonutility.checkempty(userData.getImageNameMobile())) {
							responsDataJson.put("ppr_profilepic_thumbnail",profileImgPath + userData.getUserId() + "/"+ userData.getImageNameMobile());
							responsDataJson.put("ppr_profilepic",profileImgPath + userData.getUserId() + "/"+ userData.getImageNameMobile());
						} else if (Commonutility.checkempty(userData.getSocialProfileUrl())) { 
							responsDataJson.put("ppr_profilepic_thumbnail",userData.getSocialProfileUrl());
							responsDataJson.put("ppr_profilepic",userData.getSocialProfileUrl());
						} else {
							responsDataJson.put("ppr_profilepic", "");
							responsDataJson.put("ppr_profilepic_thumbnail", "");
						}
						responsDataJson.put("ppr_name", Commonutility.stringToStringempty(userData.getFirstName()));
						if (userData.getSocietyId() !=null) {
							responsDataJson.put("ppr_desc", Commonutility.stringToStringempty(userData.getSocietyId().getImprintName()));
						} else {
							responsDataJson.put("ppr_desc", "");
						}
						if (Commonutility.checkempty(userData.getMobileNo())) {
							responsDataJson.put("ppr_mobile", mobiCommon.maskMobileNo(userData.getMobileNo()));
						} else {
							responsDataJson.put("ppr_mobile", "");
						}
						String blockName = Commonutility.stringToStringempty(mobiCommon.getBlockName(userData.getUserId()));
						if (blockName != null && userData.getFlatNo()!=null) {
							responsDataJson.put("ppr_block", blockName+"-"+Commonutility.stringToStringempty(userData.getFlatNo()));
						} else {
							responsDataJson.put("ppr_block", "");
						}
						log.logMessage("####### Parent Id#######" + userData.getUserId(), "info", JsonpackDaoService.class);
						responsDataJson.put("ppr_id", Commonutility.intToString(userData.getUserId()));
					} else {
						log.logMessage("Enter into loginUserDetail ParentId empty", "info", JsonpackDaoService.class);
						responsDataJson.put("ppr_profilepic", "");
						responsDataJson.put("ppr_profilepic_thumbnail", "");
						responsDataJson.put("ppr_name", "");
						responsDataJson.put("ppr_desc", "");
						responsDataJson.put("ppr_mobile", "");
						responsDataJson.put("ppr_block", "");
						responsDataJson.put("ppr_id", "");
					}	
					List<FlatMasterTblVO> flatMastList = mobiCommon.blockDetailData(userObj.getUserId());
					FlatMasterTblVO flatMastObj;
					JSONArray flatMastArray = new JSONArray();
					if (flatMastList != null) {
						for (Iterator<FlatMasterTblVO> fmt = flatMastList.iterator(); fmt.hasNext();) {
							flatMastObj = fmt.next();
							JSONObject jsonData = new JSONObject();
							jsonData.put("unique_id", Commonutility.intnullChek(flatMastObj.getFlat_id()));
							if (flatMastObj.getWingsname() != null && !flatMastObj.getWingsname().equalsIgnoreCase("") 
									&& !flatMastObj.getWingsname().equalsIgnoreCase("null")) {
								jsonData.put("block_flat", flatMastObj.getWingsname()+"-"+flatMastObj.getFlatno());
							} else {
								jsonData.put("block_flat", flatMastObj.getFlatno());
							}
							jsonData.put("is_myself", Commonutility.intnullChek(flatMastObj.getIvrBnismyself()));
							flatMastArray.put(jsonData);
						}
					}
					responsDataJson.put("block_detail", flatMastArray);
					String locVrSlQry = "SELECT count(notificationId) FROM NotificationTblVO where statusFlag='1' and readStatus=1 and userId.userId='"+userObj.getUserId()+"'";
					int count = commonServ.getTotalCountQuery(locVrSlQry);
					log.logMessage("####### Notification Count = :" + count + " #######", "info", JsonpackDaoService.class);
					mobiCommon mobcom=new mobiCommon();
					int percentageCount=mobcom.getProfilePercentageCal(userObj);	
					log.logMessage("####### percentageCount = :" + percentageCount + " #######", "info", JsonpackDaoService.class);
					responsDataJson.put("profile_percentage", Commonutility.intToString(percentageCount));
					responsDataJson.put("notification_count", Commonutility.intToString(count));
					
					/**
					 * privacy categories and default categories flag
					 */
					JSONArray jsonArry = new JSONArray();
					jsonArry = jsonPack.privacyListCategory();
					responsDataJson.put("privacy_categories", jsonArry);
					PrivacyInfoTblVO privacyFlgObj = new PrivacyInfoTblVO();
					privacyFlgObj = commonServ.getDefaultPrivacyFlg(userObj.getUserId());
					System.out.println("------3--------");
					jsonArry = new JSONArray();
					if (privacyFlgObj != null) {
						System.out.println("-----o---:" + privacyFlgObj.getPrivacyFlg());
						responsDataJson.put("default_flg", Commonutility.intToString(privacyFlgObj.getPrivacyFlg()));
						if (privacyFlgObj.getPrivacyFlg() == 4) {
							JsonSimplepackDao simpleJson = new JsonSimplepackDaoService();
							responsDataJson.put("users", simpleJson.packSingleArry(privacyFlgObj.getUsrIds()));
						} else {
							jsonArry = new JSONArray();
							responsDataJson.put("users", jsonArry);
						}	
						responsDataJson.put("favourite_menuid", privacyFlgObj.getFavouriteMenuIds());
					} else {
						privacyFlgObj = new PrivacyInfoTblVO();
						System.out.println("-----hello");
						UserMasterTblVo userInfoObj = new UserMasterTblVo();
						userInfoObj.setUserId(userObj.getUserId());		
						System.out.println("----uid000:" + userObj.getUserId());
						privacyFlgObj.setUsrId(userInfoObj);
						System.out.println("909090909");
						System.out.println("dddd:" + getText("default.privacy.category"));
						privacyFlgObj.setPrivacyFlg(Commonutility.stringToInteger(getText("default.privacy.category")));
						System.out.println();
						privacyFlgObj.setStatus(1);
						privacyFlgObj.setEntryDatetime(Commonutility.enteyUpdateInsertDateTime());
						privacyFlgObj.setModifyDatetime(Commonutility.enteyUpdateInsertDateTime());
						System.out.println("--------------");
						int privacyId = commonServ.insertPrivacyFlg(privacyFlgObj);
						responsDataJson.put("default_flg", getText("default.privacy.category"));						
						responsDataJson.put("users", jsonArry);
						responsDataJson.put("favourite_menuid", "");
					}
//					responsDataJson.put("group_id", Commonutility.intToString(userObj.getGroupCode().getGroupCode()));
					// get notification flag on or off
					responsDataJson.put("notificationstatus", ""+commonServ.notifictionFlgGet(userObj.getMobileNo()));
					responsDataJsonarr.put(responsDataJson);					
				}
			} else {
				responsDataJsonarr = null;
			}
		} catch(Exception ex) {
			log.logMessage("Exception found in loginUserDetail:" + ex, "error", JsonpackDaoService.class);
			responsDataJsonarr = null;
		}
		return responsDataJsonarr;
	}

	@Override
	public JSONArray userOnlineDetails(List<Object[]> userOnlineListObj,String imagePath) {
		JSONArray returnJsonData = new JSONArray();
		Log log= new Log();
		try {
			Object[] objList;
			for(Iterator<Object[]> it=userOnlineListObj.iterator();it.hasNext();) {
				objList = it.next();
				JSONObject jsonPack = new JSONObject();
				System.out.println("-----------");
				String useridPath = "";
				if (objList[0] != null) {
					jsonPack.put("uid",Commonutility.stringToStringempty((String)objList[0]));
					useridPath = Commonutility.stringToStringempty((String)objList[0]) + "/";
				} else {
					jsonPack.put("uid","");
				}				
				System.out.println("-----------1");
				if (objList[1] != null) {
					jsonPack.put("uname",Commonutility.stringToStringempty((String)objList[1]));					
				} else {
					jsonPack.put("uname","");
				}
				System.out.println("-----------2");
				if (objList[2] != null) {
					if (Commonutility.checkempty(Commonutility.stringToStringempty((String)objList[0])) && Commonutility.checkempty(Commonutility.stringToStringempty((String)objList[2]))) {
						imagePath = imagePath + useridPath + Commonutility.stringToStringempty((String)objList[2]) ;
					} else {
						imagePath = "";
					}					
					jsonPack.put("pic_url",imagePath);
				} else {
					jsonPack.put("pic_url","");
				}
				System.out.println("-----------3");
				if (objList[3] != null) {										
					jsonPack.put("is_online",Commonutility.stringToStringempty((String)objList[3]));
				} else {
					jsonPack.put("is_online","");
				}
				System.out.println("--------6");
				returnJsonData.put(jsonPack);
			}
			
		} catch(Exception ex) {
			log.logMessage("Exception found in listOfGroupPrivateContact:" +ex, "error", JsonpackDaoService.class);
			returnJsonData = null;
		}
		return returnJsonData;
	}

	@Override
	public JSONObject getAdsPostJasonpackValues(MvpAdpostTbl adPost) {
		// TODO Auto-generated method stub
		JSONObject returnJsonData = new JSONObject();
		MvpAdpostTbl adspostObj = new MvpAdpostTbl();
		AddPostDao addPostObj =new  AddPostServices();
		Log log= new Log();
		try {
			String userId = Commonutility.intToString(adPost.getUserId().getUserId());
			String postalId= Commonutility.intToString(adPost.getPostUniqueId());
			adspostObj = addPostObj.mvpAddPostDetail(userId,postalId);
			if (adspostObj != null) {
				if (adspostObj.getPostUniqueId() != 0 && adspostObj.getPostUniqueId() !=null ) {
					returnJsonData.put("ad_id", Commonutility.intToString(adspostObj.getPostUniqueId()));
				} else {
					returnJsonData.put("ad_id", "");
				}
				
				if (adspostObj.getUserId().getUserId() != 0 && adspostObj.getUserId() !=null) {
					returnJsonData.put("post_by", Commonutility.intToString(adspostObj.getUserId().getUserId()));
				} else {
					returnJsonData.put("post_by", "");
				}
				
				if (adspostObj.getiVOCATEGORY_ID().getiVOCATEGORY_ID() != 0 && adspostObj.getiVOCATEGORY_ID() !=null) {
					returnJsonData.put("feed_category_id", Commonutility.intToString(adspostObj.getiVOCATEGORY_ID().getiVOCATEGORY_ID()));
					returnJsonData.put("feed_category", Commonutility.toCheckNullEmpty(adspostObj.getiVOCATEGORY_ID().getiVOCATEGORY_NAME()));
				} else {
					returnJsonData.put("feed_category_id", "");
					returnJsonData.put("feed_category", "");
				}
				
				if (adspostObj.getIvrBnSKILL_ID() !=null) {
					returnJsonData.put("skill_id", Commonutility.intToString(adspostObj.getIvrBnSKILL_ID().getIvrBnSKILL_ID()));
				} else {
					returnJsonData.put("skill_id", "");
				}
				
				if (adspostObj.getFeedId() != 0 && adspostObj.getFeedId()!=null) {
					returnJsonData.put("feed_id", Commonutility.intToString(adspostObj.getFeedId()));
				} else {
					returnJsonData.put("feed_id", "");
				}
				
				if (adspostObj.getPostTitle() != null && adspostObj.getPostTitle()!="" 
						&& adspostObj.getPostTitle()!="null" && adspostObj.getPostTitle().length()>0) {
					returnJsonData.put("feed_title", Commonutility.stringToStringempty(adspostObj.getPostTitle()));
				} else {
					returnJsonData.put("feed_title", "");
				}
				
				if (adspostObj.getShortDesc() != null && adspostObj.getShortDesc()!="" 
						&& adspostObj.getShortDesc()!="null" && adspostObj.getShortDesc().length()>0) {
					returnJsonData.put("feed_stitle", Commonutility.stringToStringempty(adspostObj.getShortDesc()));
				} else {
					returnJsonData.put("feed_stitle", "");
				}
				
				if (adspostObj.getDescr() != null && adspostObj.getDescr()!="" 
						&& adspostObj.getDescr()!="null" && adspostObj.getDescr().length() > 0) {
					returnJsonData.put("feed_desc", Commonutility.stringToStringempty(adspostObj.getDescr()));
				} else {
					returnJsonData.put("feed_desc", "");
				}
				
				if (adspostObj.getContactNo() != null && adspostObj.getContactNo()!="" 
						&& adspostObj.getContactNo()!="null" && adspostObj.getContactNo().length() > 0) {
					returnJsonData.put("conatct_no", Commonutility.stringToStringempty(adspostObj.getContactNo()));
				} else {
					returnJsonData.put("conatct_no", "");
				}
				
				if (adspostObj.getPrice() != 0 && adspostObj.getPrice() != null) {
					float priceAmt = adspostObj.getPrice();
					DecimalFormat df = new DecimalFormat("#.00");
					df.setMaximumFractionDigits(2);
					returnJsonData.put("amount", Commonutility.toCheckNullEmpty(df.format(priceAmt)));
				} else {
					returnJsonData.put("amount", "");
				}
				
				if (adspostObj.getBuyerSellerFlag() != 0 && adspostObj.getBuyerSellerFlag() != null) {
					returnJsonData.put("ad_user_type", Commonutility.intToString(adspostObj.getBuyerSellerFlag()));
					if (adspostObj.getBuyerSellerFlag() == 1) {
						returnJsonData.put("post_as", "Buyer");
					} else if (adspostObj.getBuyerSellerFlag() == 2) {
						returnJsonData.put("post_as", "Seller");
					}
				} else {
					returnJsonData.put("ad_user_type", "");
				}
				
				if (adspostObj.getActSts() != null) {
					returnJsonData.put("ad_status", Commonutility.intToString(adspostObj.getActSts()));
				} else {
					returnJsonData.put("ad_status", "");
				}
				
				if (adspostObj.getEntryBy() != 0 && adspostObj.getEntryBy() != null) {
					returnJsonData.put("post_by", Commonutility.intToString(adspostObj.getEntryBy()));
				} else {
					returnJsonData.put("post_by", "");
				}
				
				String profileFirstName="",profileName; 
				String profileLastName="";
				profileFirstName=Commonutility.toCheckNullEmpty(adspostObj.getUserId().getFirstName());
				profileLastName=Commonutility.toCheckNullEmpty(adspostObj.getUserId().getLastName());
				if(!profileFirstName.equalsIgnoreCase("")){
					profileName=profileFirstName+" "+profileLastName;
				}else{
					profileName=profileLastName;
				}
				returnJsonData.put("profile_name", Commonutility.toCheckNullEmpty(profileName));
				
				if(adspostObj.getUserId()!=null && adspostObj.getUserId().getImageNameMobile()!=null){
					returnJsonData.put("profile_picture", getText("SOCIAL_INDIA_BASE_URL") +"/templogo/addpost/mobile/"+adspostObj.getUserId().getUserId()+"/"+adspostObj.getUserId().getImageNameMobile());	
				}else if(adspostObj.getUserId()!=null && adspostObj.getUserId().getSocialProfileUrl()!=null){
					returnJsonData.put("profile_picture",adspostObj.getUserId().getSocialProfileUrl());	
				}else{
					returnJsonData.put("profile_picture","");
				}
				
				FeedsTblVO feedMst=new FeedsTblVO();
				FeedDAO feed=new FeedDAOService();
				FunctionUtility commonFn = new FunctionUtilityServices();
				AddPostDao adPostServ =new  AddPostServices();
				JsonSimplepackDao simpleJson = new JsonSimplepackDaoService();
				feedMst=feed.getFeedDetails(adspostObj.getPostUniqueId());
				if(feedMst!=null){
					returnJsonData.put("is_liked", Commonutility.toCheckNullEmpty(Commonutility.intToString(feedMst.getIsMyfeed())));
					returnJsonData.put("like_count", Commonutility.toCheckNullEmpty(commonFn.likeCountFormat(feedMst.getLikeCount())));
					returnJsonData.put("share_count",  Commonutility.toCheckNullEmpty(commonFn.likeCountFormat(feedMst.getShareCount())));
					returnJsonData.put("comment_count",  Commonutility.toCheckNullEmpty(commonFn.likeCountFormat(feedMst.getCommentCount())));
					returnJsonData.put("originator_name", Commonutility.toCheckNullEmpty(feedMst.getOriginatorName()));
					returnJsonData.put("originator_id", Commonutility.toCheckNullEmpty(feedMst.getOriginatorId()));
					returnJsonData.put("feed_time", commonFn.getPostedDateTime(feedMst.getFeedTime()));
					returnJsonData.put("feed_location", Commonutility.toCheckNullEmpty(feedMst.getFeedLocation()));
					returnJsonData.put("feed_id", Commonutility.toCheckNullEmpty(feedMst.getFeedId()));
					returnJsonData.put("feed_type", Commonutility.toCheckNullEmpty(feedMst.getFeedType()));
					returnJsonData.put("feed_type_id", Commonutility.toCheckNullEmpty(feedMst.getFeedTypeId()));
					returnJsonData.put("feed_privacy_flg", Commonutility.toCheckNullEmpty(feedMst.getFeedPrivacyFlg()));
					returnJsonData.put("is_myfeed", Commonutility.toCheckNullEmpty(feedMst.getIsMyfeed()));
					returnJsonData.put("privacy_categories", ((JsonSimplepackDaoService) simpleJson).catPack(feedMst.getFeedPrivacyFlg().intValue()));
					int retFeedViewId = 0;
					int userid = 0;
					if (feedMst.getFeedPrivacyFlg() == 2) {
						userid = -1;
					} else if (feedMst.getFeedPrivacyFlg() == 3) {
						userid = -2;
					} else {
						userid = adspostObj.getUserId().getUserId();
					}
					String searchFlag = "2";
					if (searchFlag.equalsIgnoreCase("2")) {
					retFeedViewId = adPostServ.getadpostFeedViewId(adspostObj.getUserId().getUserId(), feedMst.getSocietyId().getSocietyId(), feedMst.getFeedId(), userid,0,searchFlag);
					}else{
					retFeedViewId = adPostServ.getadpostFeedViewId(adspostObj.getUserId().getUserId(), feedMst.getSocietyId().getSocietyId(), feedMst.getFeedId(), userid,0,searchFlag);
					}
					returnJsonData.put("feed_view_id", Commonutility.toCheckNullEmpty(retFeedViewId));
				}else{
					returnJsonData.put("is_liked", "");
					returnJsonData.put("like_count", "");
					returnJsonData.put("share_count", "");
					returnJsonData.put("comment_count", "");
					returnJsonData.put("originator_name","");
					returnJsonData.put("originator_id", "");
					
					returnJsonData.put("feed_time", "");
					returnJsonData.put("feed_location", "");
					returnJsonData.put("feed_id", "");
					returnJsonData.put("feed_type", "");
					returnJsonData.put("feed_type_id", "");
					returnJsonData.put("feed_privacy_flg", "");
					returnJsonData.put("is_myfeed", "");
				}
				
				int varComplaintAttach = adspostObj.getPostUniqueId();
				List<MvpAdpostAttchTbl> adPostAttachList=new ArrayList<MvpAdpostAttchTbl>();
				
				JSONObject comVido = new JSONObject();
				JSONObject images = new JSONObject();
				JSONArray imagesArr = new JSONArray();
				JSONArray comVidoArr = new JSONArray();
				
				boolean flag=false;
				adPostAttachList=adPostServ.getAdPostAttachList(String.valueOf(varComplaintAttach));
				System.out.println("===adPostAttachList==="+adPostAttachList.size());
				MvpAdpostAttchTbl adPostAttachObj;
				for (Iterator<MvpAdpostAttchTbl> itObj = adPostAttachList
						.iterator(); itObj.hasNext();) {
					adPostAttachObj = itObj.next();
					flag=true;
					//finalJsonarr = new JSONObject();
					images = new JSONObject();
					comVido = new JSONObject();
						if(adPostAttachObj.getIvrBnFILE_TYPE() == 1)
						{
							if(adPostAttachObj.getIvrBnFILE_TYPE()!=null){
							images.put("img_id",String.valueOf(adPostAttachObj.getAdpostAttchId()));
							}else{
								images.put("img_id","");
							}if(adPostAttachObj.getAttachName()!=null){
							images.put("img_url",getText("SOCIAL_INDIA_BASE_URL") +"/templogo/addpost/mobile/"+adPostAttachObj.getPostUniqueId().getPostUniqueId()+"/"+adPostAttachObj.getAttachName());
							}else{
								images.put("img_url","");
							}
						}
						 if(adPostAttachObj.getIvrBnFILE_TYPE() == 2)
						{
							System.out.println("==thumb_img===="+adPostAttachObj.getThumbImage());
							if(adPostAttachObj.getIvrBnFILE_TYPE()!=null){
							comVido.put("video_id",String.valueOf(adPostAttachObj.getAdpostAttchId()) );
							}else{
								comVido.put("video_id","");
							}if(adPostAttachObj.getThumbImage()!=null){
							comVido.put("thumb_img", getText("SOCIAL_INDIA_BASE_URL") +"/templogo/addpost/thumbimage/"+adPostAttachObj.getPostUniqueId().getPostUniqueId()+"/"+adPostAttachObj.getThumbImage());
							}else{
								comVido.put("thumb_img","");
							}if(adPostAttachObj.getAttachName()!=null){
							comVido.put("video_url", getText("SOCIAL_INDIA_BASE_URL") +"/templogo/addpost/videos/"+adPostAttachObj.getPostUniqueId().getPostUniqueId()+"/"+adPostAttachObj.getAttachName());
							}else{
								comVido.put("video_url","");
							}
							System.out.println("==========dsf=comVido============"+comVido);
						}
						 System.out.println("==========comVidocomVidocomVido=========="+comVido);
						 
						 System.out.println("==========imagesimages=========="+images);
						 if(images!=null && images.length()>0){
						 imagesArr.put(images);
						 returnJsonData.put("images",imagesArr);
						 }else{
							 returnJsonData.put("images",imagesArr); 
						 }
						 if(comVido!=null && comVido.length()>0){
						 comVidoArr.put(comVido);
						 returnJsonData.put("videos",comVidoArr);
						 }else{
							 returnJsonData.put("videos",comVidoArr); 
						 }
						System.out.println("ds=fd=s===");
					}
				
				if(flag==false){
					returnJsonData.put("images",imagesArr);
					returnJsonData.put("videos",comVidoArr);
				}
				
			}
		} catch(Exception ex) {
			log.logMessage("Exception found in getAdsPostJasonpackValues:" +ex, "error", JsonpackDaoService.class);
			returnJsonData = null;
		} 
		return returnJsonData;
	}

	@Override
	public JSONObject publishSkilJasonpackValues(PublishSkillTblVO publishSkilObj) {
		// TODO Auto-generated method stub
		JSONObject returnJsonData = new JSONObject();
		Log log= new Log();
		try {
			if (publishSkilObj != null) {
				if (publishSkilObj.getPubSkilId() != 0) {				
					returnJsonData.put("publish_skil_id",Commonutility.intToString(publishSkilObj.getPubSkilId()));					
				} else {
					returnJsonData.put("publish_skil_id","");
				}				
				System.out.println("-----------1");
				if (publishSkilObj.getTitle() != null && publishSkilObj.getTitle() != "" 
						&& publishSkilObj.getTitle() != "null" && publishSkilObj.getTitle().length() > 0) {
					returnJsonData.put("title",Commonutility.stringToStringempty(publishSkilObj.getTitle()));		
				} else {
					returnJsonData.put("title","");
				}
				String duration = null;
				if (publishSkilObj.getDuration() != null && publishSkilObj.getDuration() != 0) {
					duration = Commonutility.intToString(publishSkilObj.getDuration());
				}
				if (publishSkilObj.getDurationFlg() != null && publishSkilObj.getDurationFlg() !=0) {
					String durationFlg = Commonutility.intToString(publishSkilObj.getDurationFlg());
					if (Commonutility.checkempty(duration) && Commonutility.checkempty(durationFlg)) {
						if (durationFlg.equalsIgnoreCase("1")) {//1-HOURS PER DAY,2-HOURS PER MONTH
							returnJsonData.put("duration",duration);
						} else if (durationFlg.equalsIgnoreCase("2")) {
							returnJsonData.put("duration",duration);
						} else {
							returnJsonData.put("duration","");
						}
						returnJsonData.put("duration_flag",durationFlg);
					} else {
						returnJsonData.put("duration","");
						returnJsonData.put("duration_flag","");
					}
				} else {
					returnJsonData.put("duration","");
					returnJsonData.put("duration_flag","");
				}
				JSONArray jsArr = new JSONArray();
				if (publishSkilObj.getAvbilDays() != null && publishSkilObj.getAvbilDays() != "" 
						&& publishSkilObj.getAvbilDays() != "null" && publishSkilObj.getAvbilDays().length() > 0) {
					String aviDays = Commonutility.stringToStringempty((publishSkilObj.getAvbilDays()));
					if (Commonutility.checkempty(aviDays)) {
						returnJsonData.put("avbil_days",aviDays);
					} else {
						returnJsonData.put("avbil_days","");
					}
									
				} else {
					returnJsonData.put("avbil_days","");
				}
				if (publishSkilObj.getBriefDesc() != null && publishSkilObj.getBriefDesc() != "" 
						&& publishSkilObj.getBriefDesc() != "null" && publishSkilObj.getBriefDesc().length() > 0) {
					returnJsonData.put("brief_desc",Commonutility.stringToStringempty(publishSkilObj.getBriefDesc()));			
				} else {
					returnJsonData.put("brief_desc","");
				}
				if (publishSkilObj.getFees() != 0 && publishSkilObj.getFees() != null) {
					returnJsonData.put("fees",Commonutility.floatToString(publishSkilObj.getFees()));				
				} else {
					returnJsonData.put("fees","");
				}
				if (publishSkilObj.getStatus() != null) {
					returnJsonData.put("status",Commonutility.intToString(publishSkilObj.getStatus()));					
				} else {
					returnJsonData.put("status","");
				}
				if (publishSkilObj.getCategoryId() != null) {
					returnJsonData.put("category_id",Commonutility.intToString(publishSkilObj.getCategoryId().getiVOCATEGORY_ID()));
					returnJsonData.put("category_name",Commonutility.stringToStringempty(publishSkilObj.getCategoryId().getiVOCATEGORY_NAME()));	
				} else {
					returnJsonData.put("category_id","");
					returnJsonData.put("category_name","");
				}
				if (publishSkilObj.getSkillId() != null) {
					returnJsonData.put("skill_id",Commonutility.intToString(publishSkilObj.getSkillId().getIvrBnSKILL_ID()));		
					returnJsonData.put("skill_name",Commonutility.stringToStringempty(publishSkilObj.getSkillId().getIvrBnSKILL_NAME()));
				} else {
					returnJsonData.put("skill_id","");
					returnJsonData.put("skill_name","");
				}
				String userId = "";
				if (publishSkilObj.getUserId() != null) {
					userId = Commonutility.intToString(publishSkilObj.getUserId().getUserId());
					returnJsonData.put("user_id",userId);
					String profileFirstName="",profileName; 
					String profileLastName="";
					profileFirstName=Commonutility.toCheckNullEmpty(publishSkilObj.getUserId().getFirstName());
					profileLastName=Commonutility.toCheckNullEmpty(publishSkilObj.getUserId().getLastName());
					if(!profileFirstName.equalsIgnoreCase("")){
						profileName=profileFirstName+" "+profileLastName;
					}else{
						profileName=profileLastName;
					}
					returnJsonData.put("profile_name", Commonutility.toCheckNullEmpty(profileName));
					returnJsonData.put("profile_mobile",Commonutility.stringToStringempty(publishSkilObj.getUserId().getMobileNo()));
					returnJsonData.put("profile_email",Commonutility.stringToStringempty(publishSkilObj.getUserId().getEmailId()));
					String profileimgPath = getText("SOCIAL_INDIA_BASE_URL") + getText("external.templogo") + getText("external.view.profile.mobilepath");
					String imgName = Commonutility.stringToStringempty(publishSkilObj.getUserId().getImageNameMobile());
					if (Commonutility.checkempty(imgName) && Commonutility.checkempty(userId)) {
						returnJsonData.put("profilepic", profileimgPath + userId + "/" + imgName);
						returnJsonData.put("profilepic_thumbnail",profileimgPath + userId + "/" + imgName);
					} else {
						returnJsonData.put("profilepic","");
						returnJsonData.put("profilepic_thumbnail","");
					}
					
					FlatMasterTblVO flatUserdata = new FlatMasterTblVO();
					profileDao profile=new profileDaoServices();
					flatUserdata = profile.getFlatUserInfo(publishSkilObj.getUserId().getUserId());
					if (flatUserdata != null) {
						if(Commonutility.intToString(flatUserdata.getIvrBnismyself()).equalsIgnoreCase("1"))
						{
							returnJsonData.put("profile_location",Commonutility.stringToStringempty(flatUserdata.getWingsname()+" , "+(flatUserdata.getFlatno())));		
						}
						else
						{
							returnJsonData.put("profile_location","");	
						}
						
						
					} else {
						returnJsonData.put("ismyself","");
					}
				} else {
					returnJsonData.put("user_id","");
					returnJsonData.put("profile_name", "");
					returnJsonData.put("profile_mobile","");
					returnJsonData.put("profile_email","");
				}
							
				if (publishSkilObj.getFeedId() != null) {
					returnJsonData.put("feedId",Commonutility.intToString(publishSkilObj.getFeedId().getFeedId()));		
				} else {
					returnJsonData.put("feedId","");
				}	
				
				/*if (publishSkilObj.getPubSkilId() != 0) {				
					returnJsonData.put("publish_skil_id",Commonutility.intToString(publishSkilObj.getPubSkilId()));					
				} else {
					returnJsonData.put("publish_skil_id","");
				}		
				
				if (publishSkilObj.getUserId() != null) {
					returnJsonData.put("rid",Commonutility.intToString(publishSkilObj.getUserId().getUserId()));
				} else {
					returnJsonData.put("rid","");
				}
				
				if (publishSkilObj.getFeedId() != null) {
					returnJsonData.put("feedId",Commonutility.intToString(publishSkilObj.getFeedId().getFeedId()));		
				} else {
					returnJsonData.put("feedId","");
				}	
				
				if (publishSkilObj.getCategoryId() != null) {
					returnJsonData.put("category_id",Commonutility.intToString(publishSkilObj.getCategoryId().getiVOCATEGORY_ID()));				
				} else {
					returnJsonData.put("category_id","");
				}
				
				if (publishSkilObj.getSkillId() != null) {
					returnJsonData.put("skill_id",Commonutility.intToString(publishSkilObj.getSkillId().getIvrBnSKILL_ID()));					
				} else {
					returnJsonData.put("skill_id","");
				}
				
				if (publishSkilObj.getTitle() != null && publishSkilObj.getTitle() != "" 
						&& publishSkilObj.getTitle() != "null" && publishSkilObj.getTitle().length() > 0) {
					returnJsonData.put("title",Commonutility.stringToStringempty(publishSkilObj.getTitle()));			
				} else {
					returnJsonData.put("title","");
				}
				
				if (publishSkilObj.getDuration() != null && publishSkilObj.getDuration() != 0) {
					returnJsonData.put("duration",Commonutility.intToString(publishSkilObj.getDuration()));				
				} else {
					returnJsonData.put("duration","");	
				}
				
				if (publishSkilObj.getDurationFlg() != null && publishSkilObj.getDurationFlg() !=0) {
					returnJsonData.put("duration_flag",publishSkilObj.getDurationFlg());
				} else {
					returnJsonData.put("duration_flag","");
				}
				
				if (publishSkilObj.getAvbilDays() != null && publishSkilObj.getAvbilDays() != "" 
						&& publishSkilObj.getAvbilDays() != "null" && publishSkilObj.getAvbilDays().length() > 0) {
					String aviDays = Commonutility.stringToStringempty((publishSkilObj.getAvbilDays()));
					if (Commonutility.checkempty(aviDays)) {
						returnJsonData.put("avbil_days",aviDays);
					} else {
						returnJsonData.put("avbil_days","");
					}
									
				} else {
					returnJsonData.put("avbil_days","");
				}
				
				if (publishSkilObj.getBriefDesc() != null && publishSkilObj.getBriefDesc() != "" 
						&& publishSkilObj.getBriefDesc() != "null" && publishSkilObj.getBriefDesc().length() > 0) {
					returnJsonData.put("brief_desc",Commonutility.stringToStringempty(publishSkilObj.getBriefDesc()));			
				} else {
					returnJsonData.put("brief_desc","");
				}
				
				if (publishSkilObj.getFees() != 0 && publishSkilObj.getFees() != null) {
					returnJsonData.put("fees",Commonutility.floatToString(publishSkilObj.getFees()));				
				} else {
					returnJsonData.put("fees","");
				}
				
				if (publishSkilObj.getStatus() != null) {
					returnJsonData.put("status",Commonutility.intToString(publishSkilObj.getStatus()));					
				} else {
					returnJsonData.put("status","");
				}
				
				if (publishSkilObj.getEntryBy() != 0 && publishSkilObj.getEntryBy() != null) {
					returnJsonData.put("entry_by",Commonutility.intToString(publishSkilObj.getEntryBy()));					
				} else {
					returnJsonData.put("entry_by","");	
				}*/
					
				}
		} catch(Exception ex) {
			log.logMessage("Exception found in getAdsPostJasonpackValues:" +ex, "error", JsonpackDaoService.class);
			returnJsonData = null;
		} 
		return returnJsonData;
	}

	@Override
	public JSONObject eventTableJasonpackValues(int eventId) {
		// TODO Auto-generated method stub
		JSONObject evntobj=new JSONObject();
		EventTblVO eventTblObj = new EventTblVO();
		EventDao eventDaoObj = new EventDaoServices();
		Log log= new Log();
		try {
			String qry="from EventTblVO where ivrBnEVENT_ID="+eventId;
			eventTblObj = eventDaoObj.geteventobjectByQuery(qry);
			FeedDAO feedHbm=new FeedDAOService();
			
			CommonUtils commjvm=new CommonUtils();
			if (eventTblObj != null) {
				evntobj=new JSONObject();
				
				Integer functionId= eventTblObj.getIvrBnFUNCTION_ID().getFunctionId();
				System.out.println("eventId-------"+eventId);
				
				evntobj.put("event_id", ""+eventId);
				FeedsTblVO feedobj=new FeedsTblVO();
				int feedTypFlg = 0;
				if (eventTblObj.getIvrBnEVENTT_TYPE() == 1) {  //personal event
					feedTypFlg = 9;
				} else if (eventTblObj.getIvrBnEVENTT_TYPE() == 2) {  //society event
					feedTypFlg = 8;
				} else if (eventTblObj.getIvrBnEVENTT_TYPE() == 3) {  //committee meeting
					feedTypFlg = 12;
				}
				feedobj=feedHbm.getFeedDetailsByEventId(eventId,feedTypFlg);
				if(feedobj!=null){
				evntobj.put("feed_id", feedobj.getFeedId());
				}else{
					evntobj.put("feed_id", "");
				}
				
				if(functionId!=null && functionId!=0){
				evntobj.put("function_id",functionId);
				}else{
					evntobj.put("function_id","");
				}
				if(eventTblObj.getIvrBnFUNCTION_ID()!=null){
				evntobj.put("function_name",  ""+eventTblObj.getIvrBnFUNCTION_ID().getFunctionName());
				}else{
					evntobj.put("function_name","");
				}
				evntobj.put("event_title",eventTblObj.getIvrBnEVENT_TITLE());
				
				if(eventTblObj.getFaciltyTemplateId()!=null){
					Integer facilityId=eventTblObj.getFaciltyTemplateId().getFacilityId();
				evntobj.put("facility_id", facilityId);
				}else{
					evntobj.put("facility_id", "");
				}
				
				if(eventTblObj.getFunctionTemplateId()!=null){
					Integer facilityTemplateId=eventTblObj.getFunctionTemplateId().getFunctionTempId();
				evntobj.put("template_id", facilityTemplateId);
				}else{
					evntobj.put("template_id","");
				}
				evntobj.put("template_msg", eventTblObj.getIvrBnSHORT_DESC());
				evntobj.put("desc", eventTblObj.getIvrBnEVENT_DESC());
				
				if(eventTblObj.getIvrBnEVENT_CRT_USR_ID()!=null && eventTblObj.getIvrBnEVENT_CRT_USR_ID().getImageNameMobile()!=null){
					String externalUserImagePath = getText("SOCIAL_INDIA_BASE_URL") +"/templogo/profile/mobile/"+eventTblObj.getIvrBnEVENT_CRT_USR_ID().getUserId()+"/"+eventTblObj.getIvrBnEVENT_CRT_USR_ID().getImageNameMobile();
				evntobj.put("inviter_img", externalUserImagePath);
				}else if(eventTblObj.getIvrBnEVENT_CRT_USR_ID()!=null && eventTblObj.getIvrBnEVENT_CRT_USR_ID().getSocialProfileUrl()!=null){
				evntobj.put("inviter_img", eventTblObj.getIvrBnEVENT_CRT_USR_ID().getSocialProfileUrl());
				}else{
					evntobj.put("inviter_img", "");
				}
				if(eventTblObj.getFaciltyBookingId()!=null && eventTblObj.getFaciltyBookingId().getBookingId()!=null){
				evntobj.put("facility_booking_id", ""+eventTblObj.getFaciltyBookingId().getBookingId());
				}else{
					evntobj.put("facility_booking_id","");
				}
				if(eventTblObj.getIvrBnEVENT_LOCATION()!=null){
				evntobj.put("location", eventTblObj.getIvrBnEVENT_LOCATION());
				}else{
					evntobj.put("location", "");
				}
				JSONObject locdetails=new JSONObject();
				if(eventTblObj.getIvrBnLAT_LONG()!=null){
					locdetails.put("lat_long",eventTblObj.getIvrBnLAT_LONG());
				}else{
					locdetails.put("lat_long","");
				}
				if(eventTblObj.getIvrBnENDDATE()!=null && eventTblObj.getIvrBnSTARTTIME()!=null){
					String startDt=eventTblObj.getIvrBnENDDATE()+" "+eventTblObj.getIvrBnSTARTTIME();
					evntobj.put("starttime",commjvm.dbEventToMobiledate(startDt));
				}else{
					evntobj.put("starttime","");
				}
				String endDt="";
				if(eventTblObj.getIvrBnENDDATE()!=null && eventTblObj.getIvrBnENDTIME()!=null){
					 endDt=eventTblObj.getIvrBnENDDATE()+" "+eventTblObj.getIvrBnENDTIME();
					evntobj.put("endtime",commjvm.dbEventToMobiledate(endDt));
				}else{
					evntobj.put("endtime",endDt);
				}
				
				String isCloed=commjvm.dbEventGreaterThanCurrDate(endDt);
				evntobj.put("is_closed",isCloed);
				
				evntobj.put("location_details", locdetails);
				
				JSONArray imagearray=new JSONArray();
				if(eventTblObj.getIvrBnEVENT_FILE_NAME()!=null){
					JSONObject imageObj=new JSONObject();
					String eventpath=getText("SOCIAL_INDIA_BASE_URL") +"/templogo/"+getText("external.inner.eventfldr")+"/mobile/"+eventId+"/"+eventTblObj.getIvrBnEVENT_FILE_NAME();
					imageObj.put("img_id", ""+eventId);
					imageObj.put("img_url", eventpath);
					imagearray.put(imageObj);
				}
				evntobj.put("images",imagearray);
				if(eventTblObj.getIvrBnISRSVP()!=null){
					evntobj.put("is_rsvp",eventTblObj.getIvrBnISRSVP());
					}else{
						evntobj.put("is_rsvp","0");
					}
				
					
				}
		} catch(Exception ex) {
			log.logMessage("Exception found in getAdsPostJasonpackValues:" +ex, "error", JsonpackDaoService.class);
			evntobj = null;
		} 
		return evntobj;
	}

	@Override
	public JSONObject jsonFeedDetailsPack(Object[] objList, String imagePathWeb,String imagePathMobi, String videoPath, String videoPathThumb,String profileimgPath) {
		// TODO Auto-generated method stub
		JSONObject jsonPack = new JSONObject();
		Log log= new Log();
		FunctionUtility commonFn = new FunctionUtilityServices();
try {
			
			if (objList != null) {
				System.out.println("objList size---------------------"+objList.length);
//				JSONObject jsonPack = new JSONObject();
				System.out.println("-----------"+objList[32]);
				System.out.println("-----------"+objList[30]);
				System.out.println("-----------"+objList[29]);
				System.out.println("-----------"+objList[28]);
				System.out.println("-----------"+objList[27]);
				System.out.println("-----------"+objList[26]);
				System.out.println("-----------"+objList[25]);
				System.out.println("feed _id-----------"+objList[1]);
				
				
				String feedIdPath ="";
				if (objList[0] != null) {					
					jsonPack.put("feed_id",Commonutility.intToString((int)objList[0]));
					feedIdPath = (int)objList[0] + "/";
				} else {
					jsonPack.put("feed_id","");
				}				
				System.out.println("-----------1");
				if (objList[1] != null) {
					jsonPack.put("feed_type",Commonutility.intToString((int)objList[1]));					
				} else {
					jsonPack.put("feed_type","");
				}
				System.out.println("-----------2");
				JSONArray listData = new JSONArray();
				JSONArray listDataV = new JSONArray();
				JSONObject locJsonImg = new JSONObject();
				JSONObject locJsonVid = new JSONObject();
				String attachNameList = null;
				boolean fg = false;
				if (objList[2] != null) {
					attachNameList = (String) objList[2];
					fg = true;
				} else {
					System.out.println("############# NO IMAGE ############");
				}
				System.out.println("############# IMAGE ##############-----:" + attachNameList);
				// 260<!_!>IMG-20161124-WA0005_201611250524201120.jpg<!_!><!_!>1
				boolean spTag = false;
				if (attachNameList != null && attachNameList.contains("<SP>")) {
					System.out.println("______ yu------");
					spTag = true;
					String[] arratchArr = attachNameList.split("<SP>");
					String fileList = null;
					for (int i = 0;i<arratchArr.length;i++) {
						fileList = arratchArr[i];
						if (fileList.contains("<!_!>")) {
							String[] fileArr = fileList.split("<!_!>");
							if (fileArr.length == 4) {							
								System.out.println(i + "---Attach ID---" + fileArr[0]);
								System.out.println(i + "---Attach Name---" + fileArr[1]);
								System.out.println(i + "---Thumb Name---" + fileArr[2]);
								System.out.println(i + "---File Type---" + fileArr[3]);
								String fileType = fileArr[3];
								if (Commonutility.checkempty(fileType) && Commonutility.checkLengthNotZero(fileType)) {									
									fileType = fileType.trim();
									fg = true;
									if((int)objList[1]==9|| (int)objList[1]==10 || (int)objList[1]==8 || (int)objList[1]==12) {
										feedIdPath=Commonutility.stringToStringempty(fileArr[0])+"/";
										imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.uploadfile.event.img.webpath");
										imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.event.img.mobilepath");
										videoPath = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.event.videopath");
										videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.event.video.thumbpath");
									}
									if((int)objList[1]==7) {
										if (objList[27] != null) {					
											feedIdPath=Commonutility.stringToStringempty(Commonutility.intToString((int)objList[27]))+"/";
										} else {
											feedIdPath=Commonutility.stringToStringempty(fileArr[0])+"/";
										}
										imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +"complaint/web/";
										imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +"complaint/mobile/";
										videoPath = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +"complaint/videos/";
										videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +"complaint/thumbimage/";
									}
									if (fileType.equalsIgnoreCase("1")) {
										locJsonImg = new JSONObject();
										if(fileArr[1]!=null && fileArr[1].length()>0){
										locJsonImg.put("img_id", Commonutility.stringToStringempty(fileArr[0]));
										locJsonImg.put("img_url", imagePathMobi + feedIdPath + Commonutility.stringToStringempty(fileArr[1]));
										listData.put(locJsonImg);
										}
										
										locJsonImg=null;
									} else if (fileType.equalsIgnoreCase("2")) {
										locJsonVid=new JSONObject();
										if(fileArr[1]!=null && fileArr[1].length()>0){
										locJsonVid.put("video_id", Commonutility.stringToStringempty(fileArr[0]));
										locJsonVid.put("video_url", videoPath + feedIdPath +  Commonutility.stringToStringempty(fileArr[1]));
										locJsonVid.put("thumb_img", videoPathThumb + feedIdPath + Commonutility.stringToStringempty(fileArr[2]));
										listDataV.put(locJsonVid);
										}
										locJsonVid=null;
									}
									
								}
							}
						}
					}
				} else if (attachNameList != null && attachNameList.contains("<!_!>") && !spTag ) {
					System.out.println("############ else part attachNameList:" + attachNameList);
					String[] fileArr = attachNameList.split("<!_!>");
					if (fileArr.length == 4) {							
						System.out.println("---Attach ID---" + fileArr[0]);
						System.out.println("---Attach Name---" + fileArr[1]);
						System.out.println("---Thumb Name---" + fileArr[2]);
						System.out.println("---File Type---" + fileArr[3]);
						String fileType = fileArr[3];
						if (Commonutility.checkempty(fileType) && Commonutility.checkLengthNotZero(fileType)) {									
							fileType = fileType.trim();
							fg = true;
							if((int)objList[1]==9|| (int)objList[1]==10 || (int)objList[1]==8 || (int)objList[1]==12) {
								feedIdPath=Commonutility.stringToStringempty(fileArr[0])+"/";
								imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") + getText("external.uploadfile.event.img.webpath");
								imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.event.img.mobilepath");
								videoPath = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.event.videopath");
								videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +  getText("external.uploadfile.event.video.thumbpath");
							}
							if((int)objList[1]==7) {
								if (objList[27] != null) {					
									feedIdPath=Commonutility.stringToStringempty(Commonutility.intToString((int)objList[27]))+"/";
								} else {
									feedIdPath=Commonutility.stringToStringempty(fileArr[0])+"/";
								}
								imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +"complaint/web/";
								imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +"complaint/mobile/";
								videoPath = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +"complaint/videos/";
								videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")  + getText("external.templogo") +"complaint/thumbimage/";
							}
							if (fileType.equalsIgnoreCase("1")) {
								locJsonImg=new JSONObject();
								if(fileArr[1]!=null && fileArr[1].length()>0){
								locJsonImg.put("img_id", Commonutility.stringToStringempty(fileArr[0]));
								locJsonImg.put("img_url", imagePathMobi + feedIdPath + Commonutility.stringToStringempty(fileArr[1]));
								listData.put(locJsonImg);
								}
								
								locJsonImg=null;
							} else if (fileType.equalsIgnoreCase("2")) {
								locJsonVid=new JSONObject();
								if(fileArr[1]!=null && fileArr[1].length()>0){
								locJsonVid.put("video_id", Commonutility.stringToStringempty(fileArr[0]));
								locJsonVid.put("video_url", videoPath + feedIdPath +  Commonutility.stringToStringempty(fileArr[1]));
								locJsonVid.put("thumb_img", videoPathThumb + feedIdPath + Commonutility.stringToStringempty(fileArr[2]));
								listDataV.put(locJsonVid);
								}
								
								locJsonVid=null;
							}
							
						}
					}
				}
				System.out.println("listData:" + listData);
				jsonPack.put("images",listData);
				jsonPack.put("videos",listDataV);				
				System.out.println("-----------5");
				String urlThumbnail = null;
				String urlTittle = null;
				String pageurl = null;				
				if (objList[3] != null) {
					urlThumbnail = Commonutility.stringToStringempty((String)objList[3]);
				}
				if (objList[4] != null) {
					urlTittle = Commonutility.stringToStringempty((String)objList[4]);
				}
				if (objList[5] != null) {
					pageurl = Commonutility.stringToStringempty((String)objList[5]);
				}
				System.out.println("urlThumbnail:" + urlThumbnail);
				System.out.println("urlTittle:" + urlTittle);
				System.out.println("pageurl:" + pageurl);
				JSONArray dataJsonArry = new JSONArray();	
				if (Commonutility.checkempty(urlThumbnail) && Commonutility.checkempty(urlTittle) && Commonutility.checkempty(pageurl)) {
					ArrayList<String> thumbList = new ArrayList<String>();
					thumbList = jsonspTabSplitIntoArraylist(urlThumbnail);
					ArrayList<String> pageTittleList = new ArrayList<String>();
					pageTittleList = jsonspTabSplitIntoArraylist(urlTittle);
					ArrayList<String> pageurlList = new ArrayList<String>();
					pageurlList = jsonspTabSplitIntoArraylist(pageurl);					
					System.out.println("uu---"+thumbList.size());
					System.out.println("---io--:"+pageTittleList.size());
					System.out.println("--kl--:"+pageurlList.size());
					if (thumbList.size() == pageTittleList.size()) {
						if ( pageTittleList.size() == pageurlList.size()) {
							for (int i=0;i<thumbList.size();i++) {
								JSONObject dataJsonObj = new JSONObject();
								dataJsonObj.put("thumb_img", thumbList.get(i));
								dataJsonObj.put("title", pageTittleList.get(i));
								dataJsonObj.put("pageurl", pageurlList.get(i));
								dataJsonArry.put(dataJsonObj);
								dataJsonObj=null;
							}
						}						
					}
					if (dataJsonArry != null) {
						jsonPack.put("urls",dataJsonArry);
					} else {
						jsonPack.put("urls",dataJsonArry);
					}
				} else {
					jsonPack.put("urls",dataJsonArry);
				}
				System.out.println("-----6------");
				if (objList[6] != null) {					
					jsonPack.put("feed_category",Commonutility.stringToStringempty((String)objList[6]));
				} else {
					jsonPack.put("feed_category","");
				}
				System.out.println("-------7----");
				if (objList[7] != null) {					
					jsonPack.put("feed_title",Commonutility.stringToStringempty((String)objList[7]));
				} else {
					jsonPack.put("feed_title","");
				}
				System.out.println("-------8----");
				if (objList[8] != null) {					
					jsonPack.put("feed_stitle",Commonutility.stringToStringempty((String)objList[8]));
				} else {
					jsonPack.put("feed_stitle","");
				}
				System.out.println("----9-------");
				if (objList[9] != null) {					
					jsonPack.put("feed_desc",Commonutility.stringToStringempty((String)objList[9]));
				} else {
					jsonPack.put("feed_desc","");
				}
				System.out.println("-------10----");
				if (objList[10] != null) {
					Date entryDateTime = (Date)objList[10];
					System.out.println("###### feed time:" + commonFn.getPostedDateTime(entryDateTime));
					jsonPack.put("feed_time",commonFn.getPostedDateTime(entryDateTime));
				} else {
					System.out.println("###### feed time empty");
					jsonPack.put("feed_time","");
				}
				System.out.println("-----11------");
				if (objList[11] != null) {					
					jsonPack.put("amount",Commonutility.floatToString((float)objList[11]));
				} else {
					jsonPack.put("amount","");
				}
				System.out.println("--------12---");
				if (objList[12] != null) {					
					jsonPack.put("post_as",Commonutility.stringToStringempty((String)objList[12]));
				} else {
					jsonPack.put("post_as","");
				}
//				JSONArray catJsonArr = new JSONArray();
				JSONObject catJsonArr = new JSONObject();
				if (objList[13] != null) {					
					jsonPack.put("feed_privacy_flg",Commonutility.intToString((int)objList[13]));
					int privacyFlg = (int)objList[13];
					if (privacyFlg !=0 ) {
						jsonPack.put("privacy_categories", jsoncatPack(privacyFlg));
					} else {
						jsonPack.put("privacy_categories",catJsonArr);
					}
				} else {
					jsonPack.put("feed_privacy_flg","");
					jsonPack.put("privacy_categories",catJsonArr);//privacy_categories:[cate_type:"4",cate_name:"custom",cate_url:"url"]
				}
				System.out.println("---13--------");
				int postUser = 0;
				if (objList[14] != null) {	
					postUser = (int)objList[14];
					jsonPack.put("post_by",Commonutility.intToString(postUser));
				} else {
					jsonPack.put("post_by","");
				}
				if (objList[15] != null) {					
					jsonPack.put("profile_name",Commonutility.stringToStringempty((String)objList[15]));
				} else {
					jsonPack.put("profile_name","Anonymous");
				}
				System.out.println("-----14------");
			
				if (objList[17] != null) {					
					jsonPack.put("feed_location",Commonutility.stringToStringempty((String)objList[17]));
				} else {
					jsonPack.put("feed_location","");
				}/////////////////
				System.out.println("------15-----");
				if (objList[18] != null) {					
					jsonPack.put("feed_msg",Commonutility.stringToStringempty((String)objList[18]));
				} else {
					jsonPack.put("feed_msg","");
				}
				if (objList[19] != null) {					
					jsonPack.put("is_myfeed",Commonutility.intToString((int)objList[19]));
				} else {
					jsonPack.put("is_myfeed","");
				}
				System.out.println("----16-------");
				String shareFlg = "";
				if (objList[20] != null) {
					shareFlg = Commonutility.intToString((int)objList[20]);
					if (!Commonutility.checkempty(shareFlg)) {
						shareFlg = "0";
					}
					jsonPack.put("is_shared",shareFlg);
				} else {
					jsonPack.put("is_shared","0");
				}
				if (objList[21] != null) {				
					jsonPack.put("originator_name",Commonutility.stringToStringempty((String)objList[21]));
				} else {
					jsonPack.put("originator_name","Anonymous");
				}
				System.out.println("-----17------");
				int originatorId = 0;
				if (objList[22] != null) {
					originatorId = (int)objList[22];
					System.out.println("originator_id:::" + originatorId);
					jsonPack.put("originator_id",Commonutility.intToString(originatorId));
				} else {
					jsonPack.put("originator_id","");
				}
				System.out.println("#############originator_id:::" + originatorId);
				
				String profileImgNmae = "";
				if (objList[16] != null ) {
					System.out.println("profile_picturename :" + (String)objList[16]);
					profileImgNmae = Commonutility.stringToStringempty((String)objList[16]);					
				} else{
					jsonPack.put("profile_picture","");
				}
				
				System.out.println("#############profileImgNmae:::" + profileImgNmae);
				if (originatorId != 0) {
					/*if (Commonutility.checkempty(profileImgNmae)) {*/
						if (objList[16] != null ){
							jsonPack.put("profile_picture",profileimgPath + originatorId + "/" +profileImgNmae);
						}else if (objList[31] != null ){
							profileImgNmae = Commonutility.stringToStringempty((String)objList[31]);
							jsonPack.put("profile_picture",profileImgNmae);
						}else{
							jsonPack.put("profile_picture","");
						}
						
					/*} else {
						jsonPack.put("profile_picture","");
					}*/
					
				} else {
					jsonPack.put("profile_picture","");
				}
				if (objList[23] != null) {	
					System.out.println("liked-----------:" + (int)objList[23]);
					String isLiked = Commonutility.intToString((int)objList[23]);
					if (!Commonutility.checkempty(isLiked)) {
						isLiked = "0";
					}
					jsonPack.put("is_liked",isLiked);
				} else {
					System.out.println("############### likedddd");
					jsonPack.put("is_liked","0");
				}
				System.out.println("-----18------");
				if (objList[24] != null) {			
					jsonPack.put("like_count",commonFn.likeCountFormat((long)objList[24]));
				} else {
					jsonPack.put("like_count","");
				}
				if (objList[25] != null) {				
					jsonPack.put("share_count",commonFn.likeCountFormat((long)objList[25]));
				} else {
					jsonPack.put("share_count","");
				}
				System.out.println("----19-------");
				if (objList[26] != null) {					
					jsonPack.put("comment_count",commonFn.likeCountFormat((long)objList[26]));
				} else {
					jsonPack.put("comment_count","");
				}
				System.out.println("--------6666");
				if (objList[27] != null) {					
					jsonPack.put("feed_type_id",Commonutility.intToString((int)objList[27]));
				} else {
					jsonPack.put("feed_type_id","");
				}
				System.out.println("9999");
				if (objList[28] != null) {
					int sharedUserId = (int)objList[28];
					System.out.println("######## sharedUserId:" + sharedUserId + ":###shareFlg:" + shareFlg);
					if (Commonutility.checkempty(shareFlg) && shareFlg.equalsIgnoreCase("1")) {
						if (objList[16] != null ){
							jsonPack.put("profile_picture",profileimgPath + sharedUserId + "/" +profileImgNmae);
						}else if (objList[31] != null ){
							jsonPack.put("profile_picture",profileImgNmae);
						}else{
							jsonPack.put("profile_picture","");
						}
						
						String userName = "";
						try {
							UserMasterTblVo usrmas = new UserMasterTblVo();
							CommonMobiDao commonServ = new CommonMobiDaoService();
							usrmas = commonServ.getProfileDetails(sharedUserId);
							userName = usrmas.getFirstName();
						}catch(Exception ex) {
							System.out.println("Exception found in feed list shared user name get:" + ex);
						}
						jsonPack.put("profile_name",Commonutility.stringToStringempty(userName));
					} else {
//						jsonPack.put("profile_picture","");
//						jsonPack.put("profile_name","");
					}
					
				}
				if (objList[29] != null) {
					jsonPack.put("feed_view_id",Commonutility.intToString((int)objList[29]));
				} else {
					jsonPack.put("feed_view_id","");
				}
				JSONArray jsArr = new JSONArray();
				if (objList[30] != null) {
					String cusFriends = Commonutility.stringToStringempty((String)objList[30]);					
					if (Commonutility.checkempty(cusFriends)) {
						if (cusFriends.contains(",")) {
							String[] daysArr = cusFriends.split(",");
							for (int i =0;i<daysArr.length;i++) {
								System.out.println(i + "---" + daysArr[i]);
								if (Commonutility.toCheckisNumeric(daysArr[i])) {
									int day = Commonutility.stringToInteger(daysArr[i]);
									if (day != 0) {
										System.out.println("######## postUser:" + postUser);
										if (day != postUser) {
											jsArr.put(day);
										}										
									}									
								}
								
							}
						} else {
							jsArr.put(cusFriends);
						}
					}
					jsonPack.put("users",jsArr);
				} else {
					jsonPack.put("users",jsArr);
				}
				
				/*Raghu*/
				System.out.println("----32-------");
				if (objList[32] != null) {					
					jsonPack.put("additional_data",Commonutility.stringToStringempty((String)objList[32]));
				} else {
					jsonPack.put("additional_data","");
				}
			}
		
			} catch(Exception ex) {
				log.logMessage("Exception found in feedDetailsPack:" +ex, "error", JsonpackDaoService.class);
				jsonPack = null;
			}
			System.out.println("############## jsonPack ##########");
			System.out.println("jsonPack:" + jsonPack);
			return jsonPack;
	}

	private JSONObject jsoncatPack(int privacyFlg) {
		// TODO Auto-generated method stub
		JSONObject jsonPack = new JSONObject();
		Log log = new Log();
		try {
			
			if (privacyFlg == 1) {
				jsonPack.put("cate_type","1");
				jsonPack.put("cate_name",getText("cate.type.1"));
				jsonPack.put("cate_url",getText("SOCIAL_INDIA_BASE_URL") +getText("external.templogo")+getText("external.cate.img.path")+"1"+getText("external.cate.img.path.extension"));
			} else if (privacyFlg == 2) {
				jsonPack.put("cate_type","2");
				jsonPack.put("cate_name",getText("cate.type.2"));
				jsonPack.put("cate_url",getText("SOCIAL_INDIA_BASE_URL") +getText("external.templogo")+getText("external.cate.img.path")+"2"+getText("external.cate.img.path.extension"));
			} else if (privacyFlg == 3) {
				jsonPack.put("cate_type","3");
				jsonPack.put("cate_name",getText("cate.type.3"));
				jsonPack.put("cate_url",getText("SOCIAL_INDIA_BASE_URL") +getText("external.templogo")+getText("external.cate.img.path")+"3"+getText("external.cate.img.path.extension"));
			} else if (privacyFlg == 4) {
				jsonPack.put("cate_type","4");
				jsonPack.put("cate_name",getText("cate.type.4"));
				jsonPack.put("cate_url",getText("SOCIAL_INDIA_BASE_URL") +getText("external.templogo")+getText("external.cate.img.path")+"4"+getText("external.cate.img.path.extension"));
			} else {
			}
		}catch(Exception ex) {
			log.logMessage("Exception found in feedDetailsPack:" +ex, "error", JsonpackDaoService.class);
			jsonPack = null;
		}
		return jsonPack;
	}

	private ArrayList<String> jsonspTabSplitIntoArraylist(String splitData) {
		// TODO Auto-generated method stub
		ArrayList<String> returnList = new ArrayList<String>();
		Log log= new Log();
		try {
			if (splitData.contains("<SP>")) {
				String[] dataArry = splitData.split("<SP>");
				for (int i=0;i<dataArry.length;i++) {
					returnList.add(dataArry[i]);						
				}				
			}
			System.out.println("array lis :" + returnList.toString());
		} catch (Exception ex) {
			log.logMessage("Exception found in spTagSplitIntoJsonArray:" +ex, "error", JsonpackDaoService.class);
		}
		return returnList;
	}

	@Override
	public JSONObject donationAdditonalJasonpackValues(MvpDonationTbl donationGetObj) {
		// TODO Auto-generated method stub
		JSONObject returnJsonData = new JSONObject();
		DonationDao donationDAOobj = new DonationDaoServices();
		Log log= new Log();
		try {
			if (donationGetObj != null) {
				System.out.println("donationGetObj.getDonateId()----" + donationGetObj.getDonateId());
				if (donationGetObj.getDonateId() != 0) {				
					returnJsonData.put("donate_id",Commonutility.intToString(donationGetObj.getDonateId()));					
				} else {
					returnJsonData.put("donate_id","");
				}
				System.out.println("donationGetObj.getMvpDonationCategoryMstTbl()----" + donationGetObj.getMvpDonationCategoryMstTbl());
				if (donationGetObj.getMvpDonationCategoryMstTbl() != null) {
					returnJsonData.put("category_id",Commonutility.intToString(donationGetObj.getMvpDonationCategoryMstTbl().getiVOCATEGORY_ID()));
					returnJsonData.put("category_name",Commonutility.stringToStringempty(donationGetObj.getMvpDonationCategoryMstTbl().getiVOCATEGORY_NAME()));	
				} else {
					returnJsonData.put("category_id","");
					returnJsonData.put("category_name","");
				}
				System.out.println("donationGetObj.getMvpDonationSubcategoryMstTbl()----" + donationGetObj.getMvpDonationSubcategoryMstTbl());
				if (donationGetObj.getMvpDonationSubcategoryMstTbl() != null) {
					returnJsonData.put("skill_id",Commonutility.intToString(donationGetObj.getMvpDonationSubcategoryMstTbl().getIvrBnSKILL_ID()));		
					returnJsonData.put("skill_name",Commonutility.stringToStringempty(donationGetObj.getMvpDonationSubcategoryMstTbl().getIvrBnSKILL_NAME()));
				} else {
					returnJsonData.put("skill_id","");
					returnJsonData.put("skill_name","");
				}
				System.out.println("donationGetObj.getFeedId()----" + donationGetObj.getFeedId());
				if (donationGetObj.getFeedId() != null) {
					returnJsonData.put("feed_id",Commonutility.intToString(donationGetObj.getFeedId().getFeedId()));		
				} else {
					returnJsonData.put("feed_id","");
				}
				System.out.println("donationGetObj.getTitle()----" + donationGetObj.getTitle());
				if (donationGetObj.getTitle() != null && donationGetObj.getTitle() != "" 
						&& donationGetObj.getTitle() != "null" && donationGetObj.getTitle().length() > 0) {
					returnJsonData.put("title",Commonutility.stringToStringempty(donationGetObj.getTitle()));		
				} else {
					returnJsonData.put("title","");
				}
				System.out.println("donationGetObj.getQuantity()----" + donationGetObj.getQuantity());
				if (donationGetObj.getQuantity() != null && donationGetObj.getQuantity() != 0) {
					returnJsonData.put("quantity",Commonutility.intToString(donationGetObj.getQuantity()));
				} else {
					returnJsonData.put("quantity","");
				}
				System.out.println("donationGetObj.getItemType()----" + donationGetObj.getItemType());
				if (donationGetObj.getItemType() != null && donationGetObj.getItemType() != 0) {
					returnJsonData.put("item_type_id",Commonutility.intToString(donationGetObj.getItemType()));
					MvpDonationItemTypeTblVo itemTypeObj = new MvpDonationItemTypeTblVo();
					itemTypeObj.setItemtypeId(donationGetObj.getItemType());
					itemTypeObj = donationDAOobj.itemTypeGetDetails(itemTypeObj);
					if (itemTypeObj != null) {
						returnJsonData.put("item_type_name",itemTypeObj.getItemtypeName());
					} else {
						returnJsonData.put("item_type_name","");
					}
				} else {
					returnJsonData.put("item_type_id","");
					returnJsonData.put("item_type_name","");
				}
				System.out.println("donationGetObj.getDescription()----" + donationGetObj.getDescription());
				if (donationGetObj.getDescription() != null && donationGetObj.getDescription() != "" 
						&& donationGetObj.getDescription() != "null" && donationGetObj.getDescription().length() > 0) {
					returnJsonData.put("description",Commonutility.stringToStringempty(donationGetObj.getDescription()));			
				} else {
					returnJsonData.put("description","");
				}
				System.out.println("donationGetObj.getOtherDescription()----" + donationGetObj.getOtherDescription());
				if (donationGetObj.getOtherDescription() != null && donationGetObj.getOtherDescription() != "" 
						&& donationGetObj.getOtherDescription() != "null" && donationGetObj.getOtherDescription().length() > 0) {
					String bookDetail = donationGetObj.getOtherDescription();
					String[] splitval = bookDetail.split("!_!");
					System.out.println("splitval-----------" + splitval.length);
					if (splitval.length > 0) {
					returnJsonData.put("authour",Commonutility.stringToStringempty(splitval[0]));	
					returnJsonData.put("pages",Commonutility.stringToStringempty(splitval[1]));
					returnJsonData.put("publisher",Commonutility.stringToStringempty(splitval[2]));
					returnJsonData.put("ratings",Commonutility.stringToStringempty(splitval[3]));
					} else {
						returnJsonData.put("authour","");
						returnJsonData.put("pages","");
						returnJsonData.put("publisher","");
						returnJsonData.put("ratings","");
					}
				} else {
					returnJsonData.put("authour","");
					returnJsonData.put("pages","");
					returnJsonData.put("publisher","");
					returnJsonData.put("ratings","");
				}
				System.out.println("donationGetObj.getDonateTo()----" + donationGetObj.getDonateTo());
				if (donationGetObj.getDonateTo() != null) {
					returnJsonData.put("donate_to_id",Commonutility.intToString(donationGetObj.getDonateTo().getInstitutionId()));		
					returnJsonData.put("donate_to_name",Commonutility.stringToStringempty(donationGetObj.getDonateTo().getInstitutionName()));
				} else {
					returnJsonData.put("donate_to_id","");
					returnJsonData.put("donate_to_name","");
				}
				System.out.println("donationGetObj.getAmount()----" + donationGetObj.getAmount());
				if (Commonutility.checkempty(Commonutility.floatToString(donationGetObj.getAmount()))) {
					returnJsonData.put("amount",Commonutility.floatToString(donationGetObj.getAmount()));				
				} else {
					returnJsonData.put("amount","");
				}
				System.out.println("donationGetObj.getUserId()----" + donationGetObj.getUserId());
				String userId = "";
				if (donationGetObj.getUserId() != null) {
					userId = Commonutility.intToString(donationGetObj.getUserId().getUserId());
					returnJsonData.put("user_id",userId);
					String profileFirstName="",profileName; 
					String profileLastName="";
					profileFirstName=Commonutility.toCheckNullEmpty(donationGetObj.getUserId().getFirstName());
					profileLastName=Commonutility.toCheckNullEmpty(donationGetObj.getUserId().getLastName());
					if(!profileFirstName.equalsIgnoreCase("")){
						profileName=profileFirstName+" "+profileLastName;
					}else{
						profileName=profileLastName;
					}
					returnJsonData.put("profile_name", Commonutility.toCheckNullEmpty(profileName));
					returnJsonData.put("profile_mobile",Commonutility.stringToStringempty(donationGetObj.getUserId().getMobileNo()));
					returnJsonData.put("profile_email",Commonutility.stringToStringempty(donationGetObj.getUserId().getEmailId()));
					String profileimgPath = getText("SOCIAL_INDIA_BASE_URL") + getText("external.templogo") + getText("external.view.profile.mobilepath");
					String imgName = Commonutility.stringToStringempty(donationGetObj.getUserId().getImageNameMobile());
					if (Commonutility.checkempty(imgName) && Commonutility.checkempty(userId)) {
						returnJsonData.put("profilepic", profileimgPath + userId + "/" + imgName);
						returnJsonData.put("profilepic_thumbnail",profileimgPath + userId + "/" + imgName);
					} else {
						returnJsonData.put("profilepic","");
						returnJsonData.put("profilepic_thumbnail","");
					}
					FlatMasterTblVO flatUserdata = new FlatMasterTblVO();
					profileDao profile=new profileDaoServices();
					flatUserdata = profile.getFlatUserInfo(donationGetObj.getUserId().getUserId());
					if (flatUserdata != null) {
						if(Commonutility.intToString(flatUserdata.getIvrBnismyself()).equalsIgnoreCase("1"))
						{
							returnJsonData.put("profile_location",Commonutility.stringToStringempty(flatUserdata.getWingsname()+" , "+(flatUserdata.getFlatno())));		
						}
						else
						{
							returnJsonData.put("profile_location","");	
						}
					} else {
						returnJsonData.put("ismyself","");
					}
				} else {
					returnJsonData.put("user_id","");
					returnJsonData.put("profile_name", "");
					returnJsonData.put("profile_mobile","");
					returnJsonData.put("profile_email","");
				}
				
				int varDonatoinAttach = donationGetObj.getDonateId();
				List<MvpDonationAttachTblVo> donationAttachList=new ArrayList<MvpDonationAttachTblVo>();
				
				JSONObject comVido = new JSONObject();
				JSONObject images = new JSONObject();
				JSONArray imagesArr = new JSONArray();
				JSONArray comVidoArr = new JSONArray();
				
				boolean flag = false;
				donationAttachList = donationDAOobj.getDonationAttachList(String.valueOf(varDonatoinAttach));
				System.out.println("===adPostAttachList==="+donationAttachList.size());
				MvpDonationAttachTblVo doanteAttachObj;
				for (Iterator<MvpDonationAttachTblVo> itObj = donationAttachList.iterator(); itObj.hasNext();) {
					doanteAttachObj = itObj.next();
					flag=true;
					//finalJsonarr = new JSONObject();
					images = new JSONObject();
					comVido = new JSONObject();
						if (doanteAttachObj.getFileType() == 1) {
							if (doanteAttachObj.getFileType()!=null) {
							images.put("img_id",String.valueOf(doanteAttachObj.getDonataionAttchId()));
							} else {
							images.put("img_id","");
							} if(doanteAttachObj.getAttachName()!=null) {
							images.put("img_url",getText("SOCIAL_INDIA_BASE_URL") +"/templogo/donation/mobile/"+doanteAttachObj.getDonateId().getDonateId()+"/"+doanteAttachObj.getAttachName());
							} else {
							images.put("img_url","");
							}
						}
						if (doanteAttachObj.getFileType() == 2) {
							System.out.println("==thumb_img===="+ doanteAttachObj.getThumbImage());
							if (doanteAttachObj.getFileType()!=null) {
							comVido.put("video_id",String.valueOf(doanteAttachObj.getDonataionAttchId()));
							} else {
							comVido.put("video_id","");
							} if (doanteAttachObj.getThumbImage()!=null) {
							comVido.put("thumb_img", getText("SOCIAL_INDIA_BASE_URL") +"/templogo/donation/thumbimage/"+doanteAttachObj.getDonateId().getDonateId()+"/"+doanteAttachObj.getThumbImage());
							} else {
							comVido.put("thumb_img","");
							} if (doanteAttachObj.getAttachName()!=null) {
							comVido.put("video_url", getText("SOCIAL_INDIA_BASE_URL") +"/templogo/donation/videos/"+doanteAttachObj.getDonateId().getDonateId()+"/"+doanteAttachObj.getAttachName());
							} else {
							comVido.put("video_url","");
							}
						}
						if (images!=null && images.length()>0) {
							imagesArr.put(images);
							returnJsonData.put("images",imagesArr);
						} else {
							returnJsonData.put("images",imagesArr); 
						}
						if (comVido!=null && comVido.length()>0) {
							comVidoArr.put(comVido);
							returnJsonData.put("videos",comVidoArr);
						} else {
							returnJsonData.put("videos",comVidoArr); 
						}
					}
				if(flag==false){
					returnJsonData.put("images",imagesArr);
					returnJsonData.put("videos",comVidoArr);
				}
				}
		} catch(Exception ex) {
			log.logMessage("Exception found in donationAdditonalJasonpackValues:" +ex, "error", JsonpackDaoService.class);
			returnJsonData = null;
		} 
		return returnJsonData;
	
	}




}
