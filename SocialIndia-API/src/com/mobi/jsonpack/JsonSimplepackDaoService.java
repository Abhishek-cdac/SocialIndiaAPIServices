package com.mobi.jsonpack;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mobi.common.CommonMobiDao;
import com.mobi.common.CommonMobiDaoService;
import com.mobi.commonvo.WhyShouldIUpdateTblVO;
import com.mobi.utils.FunctionUtility;
import com.mobi.utils.FunctionUtilityServices;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.timelinefeedvo.FeedsTblVO;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.utils.Log;

public class JsonSimplepackDaoService extends ActionSupport implements JsonSimplepackDao {

	@Override
	public JSONArray feedListDetails(List<Object[]> feedListListObj,String imagePathWeb,String imagePathMobi,String videoPath,String videoPathThumb,String profileimgPath) {
		JSONArray returnJsonData = new JSONArray();
		Log log= new Log();
		FunctionUtility commonFn = new FunctionUtilityServices();
		try {
			Object[] objList;
			for(Iterator<Object[]> it=feedListListObj.iterator();it.hasNext();) {
				objList = it.next();
				if (objList != null) {
					JSONObject jsonPack = new JSONObject();
					jsonPack = feedDetailsPack(objList, imagePathWeb, imagePathMobi, videoPath, videoPathThumb, profileimgPath);
					
					System.out.println("-----------");
					returnJsonData.add(jsonPack);
				}
				
//				String feedIdPath ="";
//				if (objList[0] != null) {					
//					jsonPack.put("feed_id",Commonutility.intToString((int)objList[0]));
//					feedIdPath = (int)objList[0] + "/";
//				} else {
//					jsonPack.put("feed_id","");
//				}				
//				System.out.println("-----------1");
//				if (objList[1] != null) {
//					jsonPack.put("feed_type",Commonutility.intToString((int)objList[1]));					
//				} else {
//					jsonPack.put("feed_type","");
//				}
//				System.out.println("-----------2");
//				JSONArray listData = new JSONArray();
//				JSONArray listDataV = new JSONArray();
//				JSONObject locJsonImg = new JSONObject();
//				JSONObject locJsonVid = new JSONObject();
//				String attachNameList = null;
//				boolean fg = false;
//				if (objList[2] != null) {
//					attachNameList = (String) objList[2];
//					fg = true;
//				}
//				if (attachNameList.contains("<SP>")) {
//					String[] arratchArr = attachNameList.split("<SP>");
//					String fileList = null;
//					for (int i = 0;i<arratchArr.length;i++) {
//						fileList = arratchArr[i];
//						if (fileList.contains("<!_!>")) {
//							String[] fileArr = fileList.split("<!_!>");
//							if (fileArr.length == 4) {							
//								System.out.println(i + "---Attach ID---" + fileArr[0]);
//								System.out.println(i + "---Attach Name---" + fileArr[1]);
//								System.out.println(i + "---Thumb Name---" + fileArr[2]);
//								System.out.println(i + "---File Type---" + fileArr[3]);
//								String fileType = fileArr[3];
//								if (Commonutility.checkempty(fileType) && Commonutility.checkLengthNotZero(fileType)) {									
//									fileType = fileType.trim();
//									fg = true;
//									if (fileType.equalsIgnoreCase("1")) {
//										locJsonImg.put("img_id", Commonutility.stringToStringempty(fileArr[0]));
//										locJsonImg.put("img_url", imagePathMobi + feedIdPath + Commonutility.stringToStringempty(fileArr[1]));
//										listData.add(locJsonImg);
//									} else if (fileType.equalsIgnoreCase("2")) {
//										locJsonVid.put("video_id", Commonutility.stringToStringempty(fileArr[0]));
//										locJsonVid.put("video_url", videoPath + feedIdPath +  Commonutility.stringToStringempty(fileArr[1]));
//										locJsonVid.put("thumb_img", videoPathThumb + feedIdPath + Commonutility.stringToStringempty(fileArr[2]));
//										listDataV.add(locJsonVid);
//									}
//									
//								}
//							}
//						}
//					}
//				}
//				jsonPack.put("images",listData);
//				jsonPack.put("videos",listDataV);				
//				System.out.println("-----------5");
//				String urlThumbnail = null;
//				String urlTittle = null;
//				String pageurl = null;				
//				if (objList[3] != null) {
//					urlThumbnail = Commonutility.stringToStringempty((String)objList[3]);
//				}
//				if (objList[4] != null) {
//					urlTittle = Commonutility.stringToStringempty((String)objList[4]);
//				}
//				if (objList[5] != null) {
//					pageurl = Commonutility.stringToStringempty((String)objList[5]);
//				}
//				System.out.println("urlThumbnail:" + urlThumbnail);
//				System.out.println("urlTittle:" + urlTittle);
//				System.out.println("pageurl:" + pageurl);
//				JSONArray dataJsonArry = new JSONArray();	
//				if (Commonutility.checkempty(urlThumbnail) && Commonutility.checkempty(urlTittle) && Commonutility.checkempty(pageurl)) {
//					ArrayList<String> thumbList = new ArrayList<String>();
//					thumbList = spTabSplitIntoArraylist(urlThumbnail);
//					ArrayList<String> pageTittleList = new ArrayList<String>();
//					pageTittleList = spTabSplitIntoArraylist(urlTittle);
//					ArrayList<String> pageurlList = new ArrayList<String>();
//					pageurlList = spTabSplitIntoArraylist(pageurl);					
//					System.out.println("uu---"+thumbList.size());
//					System.out.println("---io--:"+pageTittleList.size());
//					System.out.println("--kl--:"+pageurlList.size());
//					if (thumbList.size() == pageTittleList.size()) {
//						if ( pageTittleList.size() == pageurlList.size()) {
//							for (int i=0;i<thumbList.size();i++) {
//								JSONObject dataJsonObj = new JSONObject();
//								dataJsonObj.put("thumb_img", thumbList.get(i));
//								dataJsonObj.put("title", pageTittleList.get(i));
//								dataJsonObj.put("pageurl", pageurlList.get(i));
//								dataJsonArry.add(dataJsonObj);
//							}
//						}						
//					}
//					if (dataJsonArry != null) {
//						jsonPack.put("urls",dataJsonArry);
//					} else {
//						jsonPack.put("urls",dataJsonArry);
//					}
//				} else {
//					jsonPack.put("urls",dataJsonArry);
//				}
//				System.out.println("-----6------");
//				if (objList[6] != null) {					
//					jsonPack.put("feed_category",Commonutility.stringToStringempty((String)objList[6]));
//				} else {
//					jsonPack.put("feed_category","");
//				}
//				System.out.println("-------7----");
//				if (objList[7] != null) {					
//					jsonPack.put("feed_title",Commonutility.stringToStringempty((String)objList[7]));
//				} else {
//					jsonPack.put("feed_title","");
//				}
//				System.out.println("-------8----");
//				if (objList[8] != null) {					
//					jsonPack.put("feed_stitle",Commonutility.stringToStringempty((String)objList[8]));
//				} else {
//					jsonPack.put("feed_stitle","");
//				}
//				System.out.println("----9-------");
//				if (objList[9] != null) {					
//					jsonPack.put("feed_desc",Commonutility.stringToStringempty((String)objList[9]));
//				} else {
//					jsonPack.put("feed_desc","");
//				}
//				System.out.println("-------10----");
//				if (objList[10] != null) {
//					Date entryDateTime = (Date)objList[10];
//					jsonPack.put("feed_time",commonFn.getPostedDateTime(entryDateTime));
//				} else {
//					jsonPack.put("feed_time","");
//				}
//				System.out.println("-----11------");
//				if (objList[11] != null) {					
//					jsonPack.put("amount",Commonutility.floatToString((float)objList[11]));
//				} else {
//					jsonPack.put("amount","");
//				}
//				System.out.println("--------12---");
//				if (objList[12] != null) {					
//					jsonPack.put("post_as",Commonutility.stringToStringempty((String)objList[12]));
//				} else {
//					jsonPack.put("post_as","");
//				}
//				if (objList[13] != null) {					
//					jsonPack.put("feed_privacy_flg",Commonutility.intToString((int)objList[13]));
//				} else {
//					jsonPack.put("feed_privacy_flg","");
//				}
//				System.out.println("---13--------");
//				if (objList[14] != null) {					
//					jsonPack.put("post_by",Commonutility.intToString((int)objList[14]));
//				} else {
//					jsonPack.put("post_by","");
//				}
//				if (objList[15] != null) {					
//					jsonPack.put("profile_name",Commonutility.stringToStringempty((String)objList[15]));
//				} else {
//					jsonPack.put("profile_name","");
//				}
//				System.out.println("-----14------");
//				String profileImgNmae = "";
//				if (objList[16] != null) {
//					System.out.println("profile_picturename :" + (String)objList[16]);
//					profileImgNmae = Commonutility.stringToStringempty((String)objList[16]);					
//				} else {
//					jsonPack.put("profile_picture","");
//				}
//				if (objList[17] != null) {					
//					jsonPack.put("feed_location",Commonutility.stringToStringempty((String)objList[17]));
//				} else {
//					jsonPack.put("feed_location","");
//				}/////////////////
//				System.out.println("------15-----");
//				if (objList[18] != null) {					
//					jsonPack.put("feed_msg",Commonutility.stringToStringempty((String)objList[18]));
//				} else {
//					jsonPack.put("feed_msg","");
//				}
//				if (objList[19] != null) {					
//					jsonPack.put("is_myfeed",Commonutility.intToString((int)objList[19]));
//				} else {
//					jsonPack.put("is_myfeed","");
//				}
//				System.out.println("----16-------");
//				String shareFlg = "";
//				if (objList[20] != null) {
//					shareFlg = Commonutility.intToString((int)objList[20]);
//					if (!Commonutility.checkempty(shareFlg)) {
//						shareFlg = "0";
//					}
//					jsonPack.put("is_shared",shareFlg);
//				} else {
//					jsonPack.put("is_shared","");
//				}
//				if (objList[21] != null) {				
//					jsonPack.put("originator_name",Commonutility.stringToStringempty((String)objList[21]));
//				} else {
//					jsonPack.put("originator_name","");
//				}
//				System.out.println("-----17------");
//				int originatorId = 0;
//				if (objList[22] != null) {
//					originatorId = (int)objList[22];
//					System.out.println("originator_id:::" + originatorId);
//					jsonPack.put("originator_id",Commonutility.intToString(originatorId));
//				} else {
//					jsonPack.put("originator_id","");
//				}
//				System.out.println("#############originator_id:::" + originatorId);
//				System.out.println("#############profileImgNmae:::" + profileImgNmae);
//				if (originatorId != 0) {
//					if (Commonutility.checkempty(profileImgNmae)) {
//						jsonPack.put("profile_picture",profileimgPath + originatorId + "/" +profileImgNmae);
//					} else {
//						jsonPack.put("profile_picture","");
//					}
//					
//				} else {
//					jsonPack.put("profile_picture","");
//				}
//				if (objList[23] != null) {	
//					System.out.println("liked-----------:" + (int)objList[23]);
//					String isLiked = Commonutility.intToString((int)objList[23]);
//					if (!Commonutility.checkempty(isLiked)) {
//						isLiked = "0";
//					}
//					jsonPack.put("is_liked",isLiked);
//				} else {
//					System.out.println("############### likedddd");
//					jsonPack.put("is_liked","0");
//				}
//				System.out.println("-----18------");
//				if (objList[24] != null) {			
//					jsonPack.put("like_count",commonFn.likeCountFormat((long)objList[24]));
//				} else {
//					jsonPack.put("like_count","");
//				}
//				if (objList[25] != null) {				
//					jsonPack.put("share_count",commonFn.likeCountFormat((long)objList[25]));
//				} else {
//					jsonPack.put("share_count","");
//				}
//				System.out.println("----19-------");
//				if (objList[26] != null) {					
//					jsonPack.put("comment_count",commonFn.likeCountFormat((long)objList[26]));
//				} else {
//					jsonPack.put("comment_count","");
//				}
//				System.out.println("--------6666");
//				if (objList[27] != null) {					
//					jsonPack.put("feed_type_id",Commonutility.intToString((int)objList[27]));
//				} else {
//					jsonPack.put("feed_type_id","");
//				}
//				System.out.println("9999");
//				if (objList[28] != null) {
//					int sharedUserId = (int)objList[28];
//					System.out.println("######## sharedUserId:" + sharedUserId + ":###shareFlg:" + shareFlg);
//					if (Commonutility.checkempty(shareFlg) && shareFlg.equalsIgnoreCase("1")) {
//						jsonPack.put("profile_picture",profileimgPath + sharedUserId + "/" +profileImgNmae);
//						String userName = "";
//						try {
//							UserMasterTblVo usrmas = new UserMasterTblVo();
//							CommonMobiDao commonServ = new CommonMobiDaoService();
//							usrmas = commonServ.getProfileDetails(sharedUserId);
//							userName = usrmas.getFirstName();
//						}catch(Exception ex) {
//							System.out.println("Exception found in feed list shared user name get:" + ex);
//						}
//						jsonPack.put("profile_name",Commonutility.stringToStringempty(userName));
//					} else {
////						jsonPack.put("profile_picture","");
////						jsonPack.put("profile_name","");
//					}
//					
//				}
//				if (objList[29] != null) {
//					jsonPack.put("feed_view_id",Commonutility.intToString((int)objList[29]));
//				} else {
//					jsonPack.put("feed_view_id","");
//				}
//				returnJsonData.add(jsonPack);
			}
			
		} catch(Exception ex) {
			log.logMessage("Exception found in feedListDetails:" +ex, "error", JsonSimplepackDaoService.class);
			returnJsonData = null;
		}
		return returnJsonData;
	}

	@Override
	public JSONArray spTagSplitIntoJsonArray(String splitData,String imageFilePath) {
		JSONArray listData = new JSONArray();
		Log log= new Log();
		try {
			if (splitData.contains("<SP>")) {
				String[] dataArry = splitData.split("<SP>");
				for (int i=0;i<dataArry.length;i++) {
					listData.add(imageFilePath + dataArry[i]);						
				}				
			}
			System.out.println(listData.toString());
			
		} catch (Exception ex) {
			log.logMessage("Exception found in spTagSplitIntoJsonArray:" +ex, "error", JsonSimplepackDaoService.class);
		}
		return listData;
	}

	@Override
	public ArrayList<String> spTabSplitIntoArraylist(String splitData) {
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
			log.logMessage("Exception found in spTagSplitIntoJsonArray:" +ex, "error", JsonSimplepackDaoService.class);
		}
		return returnList;
	}

	@Override
	public JSONArray whyIupdateDetails(List<WhyShouldIUpdateTblVO> listObj) {
		JSONArray retArryData = new JSONArray();
		Log log= new Log();
		try {
			WhyShouldIUpdateTblVO whyIObj;
			for(Iterator<WhyShouldIUpdateTblVO> it = listObj.iterator();it.hasNext();) {
				whyIObj = it.next();
				retArryData.add(Commonutility.stringToStringempty(whyIObj.getDescp()));
				log.logMessage("why should update data :" + whyIObj.getDescp() , "info", JsonSimplepackDaoService.class);
			}
			
			log.logMessage("why should update data end" , "info", JsonSimplepackDaoService.class);
			
		} catch (Exception ex) {
			log.logMessage("Exception found in spTagSplitIntoJsonArray:" +ex, "error", JsonSimplepackDaoService.class);
		}
		return retArryData;
	}

	@Override
	public JSONArray pubSkillListDetails(List<Object[]> pubSkillListObj,
			String profileimgPath) {
		JSONArray returnJsonData = new JSONArray();
		Log log= new Log();
		FunctionUtility commonFn = new FunctionUtilityServices();
		try {
			Object[] objList;
			for(Iterator<Object[]> it=pubSkillListObj.iterator();it.hasNext();) {
				objList = it.next();
				JSONObject jsonPack = new JSONObject();
				System.out.println("-----------");
				String feedIdPath ="";
				System.out.println("---:" + objList[0]);
				if (objList[0] != null) {				
					jsonPack.put("publish_skil_id",Commonutility.stringToStringempty((String)objList[0]));					
				} else {
					jsonPack.put("publish_skil_id","");
				}				
				System.out.println("-----------1");
				if (objList[1] != null) {
					jsonPack.put("title",Commonutility.stringToStringempty((String)objList[1]));			
				} else {
					jsonPack.put("title","");
				}
				String duration = null;
				if (objList[2] != null) {
					duration = Commonutility.stringToStringempty((String)objList[2]);
//					jsonPack.put("duration",Commonutility.stringToStringempty((String)objList[2]));				
				}
				if (objList[3] != null) {
					String durationFlg = Commonutility.stringToStringempty((String)objList[3]);
					if (Commonutility.checkempty(duration) && Commonutility.checkempty(durationFlg)) {
						if (durationFlg.equalsIgnoreCase("1")) {//1-HOURS PER DAY,2-HOURS PER MONTH
							jsonPack.put("duration",duration);
						} else if (durationFlg.equalsIgnoreCase("2")) {
							jsonPack.put("duration",duration);
						} else {
							jsonPack.put("duration","");
						}
						jsonPack.put("duration_flag",durationFlg);
					} else {
						jsonPack.put("duration","");
						jsonPack.put("duration_flag","");
					}
//					jsonPack.put("duration",Commonutility.stringToStringempty((String)objList[3]));				
				} else {
					jsonPack.put("duration","");
					jsonPack.put("duration_flag","");
				}
				JSONArray jsArr = new JSONArray();
				if (objList[4] != null) {
					String aviDays = Commonutility.stringToStringempty((String)objList[4]);
					if (Commonutility.checkempty(aviDays)) {
						/*if (aviDays.contains(",")) {
							String[] daysArr = aviDays.split(",");
							for (int i =0;i<daysArr.length;i++) {
								System.out.println(i + "---" + daysArr[i]);
								if (Commonutility.toCheckisNumeric(daysArr[i])) {
									int day = Commonutility.stringToInteger(daysArr[i]);
									if (day != 0) {
										jsArr.add(day);
									}									
								}
								
							}
						} else {
							jsArr.add(aviDays);
						}*/
						jsonPack.put("avbil_days",aviDays);
					} else {
						jsonPack.put("avbil_days","");
					}
									
				} else {
					jsonPack.put("avbil_days","");
				}
				if (objList[5] != null) {
					jsonPack.put("brief_desc",Commonutility.stringToStringempty((String)objList[5]));			
				} else {
					jsonPack.put("brief_desc","");
				}
				if (objList[6] != null) {
					jsonPack.put("fees",Commonutility.stringToStringempty((String)objList[6]));				
				} else {
					jsonPack.put("fees","");
				}
				if (objList[7] != null) {
					jsonPack.put("status",Commonutility.stringToStringempty((String)objList[7]));					
				} else {
					jsonPack.put("status","");
				}
				String entryById = null;
				if (objList[8] != null) {
					entryById = Commonutility.stringToStringempty((String)objList[8]);					
				}
				String enteryDateTime = null;
				if (objList[9] != null) {
					enteryDateTime = Commonutility.stringToStringempty((String)objList[9]);		
				}
				if (objList[10] != null) {
					jsonPack.put("category_id",Commonutility.stringToStringempty((String)objList[10]));					
				} else {
					jsonPack.put("category_id","");
				}
				if (objList[11] != null) {
					jsonPack.put("category_name",Commonutility.stringToStringempty((String)objList[11]));					
				} else {
					jsonPack.put("category_name","");
				}
				if (objList[12] != null) {
					jsonPack.put("skill_id",Commonutility.stringToStringempty((String)objList[12]));					
				} else {
					jsonPack.put("skill_id","");
				}
				if (objList[13] != null) {
					jsonPack.put("skill_name",Commonutility.stringToStringempty((String)objList[13]));					
				} else {
					jsonPack.put("skill_name","");
				}
				String userId = "";
				if (objList[14] != null) {
					userId = Commonutility.stringToStringempty((String)objList[14]);		
					jsonPack.put("user_id",Commonutility.stringToStringempty((String)objList[14]));
				} else {
					jsonPack.put("user_id","");
				}
				String profileFirstName="",profileName; 
				String profileLastName="";
				profileFirstName=Commonutility.toCheckNullEmpty(objList[24]);
				profileLastName=Commonutility.toCheckNullEmpty(objList[25]);
				if(!profileFirstName.equalsIgnoreCase("")){
					profileName=profileFirstName+" "+profileLastName;
				}else{
					profileName=profileLastName;
				}
				jsonPack.put("profile_name", Commonutility.toCheckNullEmpty(profileName));

				/*if (objList[15] != null) {
					jsonPack.put("profile_name",Commonutility.stringToStringempty((String)objList[15]));					
				} else {
					jsonPack.put("profile_name","");
				}*/
				if (objList[16] != null) {
					jsonPack.put("profile_mobile",Commonutility.stringToStringempty((String)objList[16]));				
				} else {
					jsonPack.put("profile_mobile","");
				}
				if (objList[17] != null) {
					jsonPack.put("profile_email",Commonutility.stringToStringempty((String)objList[17]));					
				} else {
					jsonPack.put("profile_email","");
				}
				if (objList[18] != null) {
					String imgName = Commonutility.stringToStringempty((String)objList[18]);
					if (Commonutility.checkempty(imgName) && Commonutility.checkempty(userId)) {
						jsonPack.put("profilepic", profileimgPath + userId + "/" + imgName);
						jsonPack.put("profilepic_thumbnail",profileimgPath + userId + "/" + imgName);
					} else {
						jsonPack.put("profilepic","");
						jsonPack.put("profilepic_thumbnail","");
					}
								
				} else {
					jsonPack.put("profilepic","");
					jsonPack.put("profilepic_thumbnail","");
				}
				/*if (objList[19] != null) {
					jsonPack.put("profile_location",Commonutility.stringToStringempty((String)objList[19]));			
				} else {
					jsonPack.put("profile_location","");
				}	*/			
				if (objList[20] != null) {
					jsonPack.put("feedId",Commonutility.stringToStringempty((String)objList[20]));		
				} else {
					jsonPack.put("feedId","");
				}	
				if (objList[23] != null) {
					if(Commonutility.stringToStringempty((String)objList[23]).equalsIgnoreCase("1"))
					{
						jsonPack.put("profile_location",Commonutility.stringToStringempty((String)objList[21]+" , "+(String)objList[22]));		
					}
					else
					{
						jsonPack.put("profile_location","");	
					}
					
					
				} else {
					jsonPack.put("ismyself","");
				}	
				returnJsonData.add(jsonPack);
			}
			
		} catch(Exception ex) {
			log.logMessage("Exception found in publish list:" +ex, "error", JsonSimplepackDaoService.class);
			returnJsonData = null;
		}
		return returnJsonData;
	}

	@Override
	public JSONArray packSingleArry(String data) {
		Log log = new Log();
		JSONArray retJsonArry = new JSONArray();
		try {
			if (Commonutility.checkempty(data)) {
				if (data.contains(",")) {
					String[] dataArr = data.split(",");
					for (int i =0;i<dataArr.length;i++) {
						System.out.println(i + "---" + dataArr[i]);
						if (Commonutility.toCheckisNumeric(dataArr[i])) {
							int packData = Commonutility.stringToInteger(dataArr[i]);
							if (packData != 0) {
								retJsonArry.add(packData);
							}									
						}
						
					}
				} else {
					retJsonArry.add(data);
				}
			}
			
		}catch(Exception ex) {
			log.logMessage("Exception found in publish list:" +ex, "error", JsonSimplepackDaoService.class);
		}
		return retJsonArry;
	}

	@Override
	public JSONObject feedDetailsPack(Object[] objList,String imagePathWeb,String imagePathMobi,String videoPath,String videoPathThumb,String profileimgPath) {
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
									if((int)objList[1]==9|| (int)objList[1]==10 || (int)objList[1]==8 || (int)objList[1]==12){
										feedIdPath=Commonutility.stringToStringempty(fileArr[0])+"/";
										imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") + getText("external.uploadfile.event.img.webpath");
										imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.event.img.mobilepath");
										videoPath = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.event.videopath");
										videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.event.video.thumbpath");
									}
									if((int)objList[1]==7) {
										if (objList[27] != null) {					
											feedIdPath=Commonutility.stringToStringempty(Commonutility.intToString((int)objList[27]))+"/";
										} else {
											feedIdPath=Commonutility.stringToStringempty(fileArr[0])+"/";
										}
										imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +"complaint/web/";
										imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +"complaint/mobile/";
										videoPath = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +"complaint/videos/";
										videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +"complaint/thumbimage/";
									}
									if (fileType.equalsIgnoreCase("1")) {
										locJsonImg = new JSONObject();
										if(fileArr[1]!=null && fileArr[1].length()>0){
										locJsonImg.put("img_id", Commonutility.stringToStringempty(fileArr[0]));
										locJsonImg.put("img_url", imagePathMobi + feedIdPath + Commonutility.stringToStringempty(fileArr[1]));
										listData.add(locJsonImg);
										}
										
										locJsonImg=null;
									} else if (fileType.equalsIgnoreCase("2")) {
										locJsonVid=new JSONObject();
										if(fileArr[1]!=null && fileArr[1].length()>0){
										locJsonVid.put("video_id", Commonutility.stringToStringempty(fileArr[0]));
										locJsonVid.put("video_url", videoPath + feedIdPath +  Commonutility.stringToStringempty(fileArr[1]));
										locJsonVid.put("thumb_img", videoPathThumb + feedIdPath + Commonutility.stringToStringempty(fileArr[2]));
										listDataV.add(locJsonVid);
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
								imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") + getText("external.uploadfile.event.img.webpath");
								imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.event.img.mobilepath");
								videoPath = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.event.videopath");
								videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +  getText("external.uploadfile.event.video.thumbpath");
							}
							if((int)objList[1]==7) {
								if (objList[27] != null) {					
									feedIdPath=Commonutility.stringToStringempty(Commonutility.intToString((int)objList[27]))+"/";
								} else {
									feedIdPath=Commonutility.stringToStringempty(fileArr[0])+"/";
								}
								imagePathWeb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +"complaint/web/";
								imagePathMobi = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +"complaint/mobile/";
								videoPath = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +"complaint/videos/";
								videoPathThumb = getText("SOCIAL_INDIA_BASE_URL")   + getText("external.templogo") +"complaint/thumbimage/";
							}
							if (fileType.equalsIgnoreCase("1")) {
								locJsonImg=new JSONObject();
								if(fileArr[1]!=null && fileArr[1].length()>0){
								locJsonImg.put("img_id", Commonutility.stringToStringempty(fileArr[0]));
								locJsonImg.put("img_url", imagePathMobi + feedIdPath + Commonutility.stringToStringempty(fileArr[1]));
								listData.add(locJsonImg);
								}
								
								locJsonImg=null;
							} else if (fileType.equalsIgnoreCase("2")) {
								locJsonVid=new JSONObject();
								if(fileArr[1]!=null && fileArr[1].length()>0){
								locJsonVid.put("video_id", Commonutility.stringToStringempty(fileArr[0]));
								locJsonVid.put("video_url", videoPath + feedIdPath +  Commonutility.stringToStringempty(fileArr[1]));
								locJsonVid.put("thumb_img", videoPathThumb + feedIdPath + Commonutility.stringToStringempty(fileArr[2]));
								listDataV.add(locJsonVid);
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
					thumbList = spTabSplitIntoArraylist(urlThumbnail);
					ArrayList<String> pageTittleList = new ArrayList<String>();
					pageTittleList = spTabSplitIntoArraylist(urlTittle);
					ArrayList<String> pageurlList = new ArrayList<String>();
					pageurlList = spTabSplitIntoArraylist(pageurl);					
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
								dataJsonArry.add(dataJsonObj);
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
						jsonPack.put("privacy_categories", catPack(privacyFlg));
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
											jsArr.add(day);
										}										
									}									
								}
								
							}
						} else {
							jsArr.add(cusFriends);
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
			log.logMessage("Exception found in feedDetailsPack:" +ex, "error", JsonSimplepackDaoService.class);
			jsonPack = null;
		}
		System.out.println("############## jsonPack ##########");
		System.out.println("jsonPack:" + jsonPack);
		return jsonPack;
	}
	
	public JSONObject catPack(int privacyFlg) {
		JSONObject jsonPack = new JSONObject();
		Log log = new Log();
		try {
			
			if (privacyFlg == 1) {
				jsonPack.put("cate_type","1");
				jsonPack.put("cate_name",getText("cate.type.1"));
				jsonPack.put("cate_url",getText("SOCIAL_INDIA_BASE_URL")  +getText("external.templogo")+getText("external.cate.img.path")+"1"+getText("external.cate.img.path.extension"));
//				jsonPack.put("is_list_users","0");
			} else if (privacyFlg == 2) {
				jsonPack.put("cate_type","2");
				jsonPack.put("cate_name",getText("cate.type.2"));
				jsonPack.put("cate_url",getText("SOCIAL_INDIA_BASE_URL")  +getText("external.templogo")+getText("external.cate.img.path")+"2"+getText("external.cate.img.path.extension"));
//				jsonPack.put("is_list_users","0");
			} else if (privacyFlg == 3) {
				jsonPack.put("cate_type","3");
				jsonPack.put("cate_name",getText("cate.type.3"));
				jsonPack.put("cate_url",getText("SOCIAL_INDIA_BASE_URL")  +getText("external.templogo")+getText("external.cate.img.path")+"3"+getText("external.cate.img.path.extension"));
//				jsonPack.put("is_list_users","0");
			} else if (privacyFlg == 4) {
				jsonPack.put("cate_type","4");
				jsonPack.put("cate_name",getText("cate.type.4"));
				jsonPack.put("cate_url",getText("SOCIAL_INDIA_BASE_URL") +getText("external.templogo")+getText("external.cate.img.path")+"4"+getText("external.cate.img.path.extension"));
//				jsonPack.put("is_list_users","1"); 
			} else {
//				jsonPack = null;
			}
		}catch(Exception ex) {
			log.logMessage("Exception found in feedDetailsPack:" +ex, "error", JsonSimplepackDaoService.class);
			jsonPack = null;
		}
		return jsonPack;
	}

	@Override
	public JSONArray feedListSearchDetails(List<FeedsTblVO> feedListListObj,
			String imagePathWeb, String imagePathMobi, String videoPath,
			String videoPathThumb, String profileimgPath) {
		// TODO Auto-generated method stub
		JSONArray returnJsonData = new JSONArray();
		Log log= new Log();
		FunctionUtility commonFn = new FunctionUtilityServices();
		try {
			FeedsTblVO objList;
			
			for(Iterator<FeedsTblVO> it=feedListListObj.iterator();it.hasNext();) {
				objList = it.next();
				if (objList != null) {
					JSONObject jsonPack = new JSONObject();
					jsonPack.put("feed_id",objList.getFeedId());
					if(objList.getFeedType()!=null){
					jsonPack.put("feed_type",objList.getFeedType());
					}else{
						jsonPack.put("feed_type","");
					}
					if(objList.getFeedTypeId()!=null){
						jsonPack.put("feed_type_id",objList.getFeedTypeId());
						}else{
							jsonPack.put("feed_type_id","");
						}
					
					if(objList.getFeedMsg()!=null){
						jsonPack.put("feed_message",objList.getFeedMsg());
						}else{
							jsonPack.put("feed_message","");
						}
					if(objList.getFeedTitle()!=null){
						jsonPack.put("feed_title",objList.getFeedTitle());
						}else{
							jsonPack.put("feed_title","");
						}
					if(objList.getFeedStitle()!=null){
						jsonPack.put("feed_stitle",objList.getFeedStitle());
						}else{
							jsonPack.put("feed_stitle","");
						}
					if(objList.getFeedDesc()!=null){
						jsonPack.put("feed_desc",objList.getFeedDesc());
						}else{
							jsonPack.put("feed_desc","");
						}
					if(objList.getAmount()!=null && objList.getAmount()>0){
						jsonPack.put("amount",objList.getAmount());
						}else{
							jsonPack.put("amount","0");
						}
					if(objList.getIsMyfeed()!=null){
						jsonPack.put("is_my_feed",objList.getIsMyfeed());
						}else{
							jsonPack.put("is_my_feed","");
						}
					if(objList.getOriginatorName()!=null){
						jsonPack.put("originator_name",objList.getOriginatorName());
						}else{
							jsonPack.put("originator_name","");
						}
					if(objList.getLikeCount()!=null && objList.getLikeCount()>0){
						jsonPack.put("like_count",objList.getLikeCount());
						}else{
							jsonPack.put("like_count","0");
						}
					if(objList.getShareCount()!=null && objList.getShareCount()>0){
						jsonPack.put("share_count",objList.getShareCount());
						}else{
							jsonPack.put("share_count","0");
						}
					if(objList.getCommentCount()!=null && objList.getCommentCount()>0){
						jsonPack.put("comment_count",objList.getCommentCount());
						}else{
							jsonPack.put("comment_count","0");
						}
					if(objList.getAdditionalData()!=null){
					jsonPack.put("additionalData",objList.getAdditionalData());
					}else{
						jsonPack.put("additionalData","");
					}
					System.out.println("-----------");
					returnJsonData.add(jsonPack);
				}

			}
			
		} catch(Exception ex) {
			log.logMessage("Exception found in feedListDetails:" +ex, "error", JsonSimplepackDaoService.class);
			returnJsonData = null;
		}
		return returnJsonData;
	}

}
