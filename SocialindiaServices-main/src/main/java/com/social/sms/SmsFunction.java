package com.social.sms;

import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsErrorpojo;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmsOutpojo;
import com.social.sms.persistense.SmssmppConfpojo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;







import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class SmsFunction extends Thread {

  public int smsstatus = 0;
  Properties props = System.getProperties();// For SMTP
  private SmssmppConfpojo smsconf = new SmssmppConfpojo();
  private SmsErrorpojo smserror = new SmsErrorpojo();
  private SmsOutpojo smsout = new SmsOutpojo();
  private List<SmsInpojo> smsconfig;
  private List lis = new ArrayList();
  public static int ccount = 0;

  SmsEngineServices smseng = new SmsEngineDaoServices();
  java.util.Date now = new java.util.Date();
  DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  CommonUtils common = new CommonUtilsDao();

  public SmsFunction() {
  }

  /**
   * Sms configuration.
   * 
   * @param list
   *          .
   * @param smscon
   *          .
   */
  public SmsFunction(List list, SmssmppConfpojo smscon) {

    lis = list;
    smsconf.setHttpUrl(smscon.getHttpUrl());
    smsconf.setUserName(smscon.getUserName());
    smsconf.setPassword(smscon.getPassword());
    smsconf.setSenderName(smscon.getSenderName());
    smsconf.setCdmaSender(smscon.getCdmaSender());
    smsconf.setProviderName(smscon.getProviderName());
    smsconf.setFetchcount(smscon.getFetchcount());
  }

  /**
   * call sms detail.
   */
  public void run() {
    try {
      ccount++;
      getSsmDeatilDb(lis);
    } catch (Exception ex) {
      System.out.println("Exception in sms details-------->> " + ex);
    }
  }

  /**
   * For getting sms details.
   * 
   * @param lisst
   *          . .
   * @return sms details.
   */
  private String getSsmDeatilDb(List lisst) {
		String result = "";
		try {
			SmsInpojo empIdObj;
			List lst = lisst;
			int ival = 0;
			for (Iterator<SmsInpojo> it = lst.iterator(); it.hasNext();) {
				empIdObj = it.next();
				String nmobno = empIdObj.getSmsMobNumber();
				String cardno = empIdObj.getSmsCardNo();
				String message = empIdObj.getSmsMessage();
				String pollFlg = empIdObj.getSmspollFlag();
				String smsTemplateid = empIdObj.getSmsTempId();

				result = sendSms(nmobno, cardno, message, pollFlg);
				String errorst = "";
				if (result.contains("<IB>")) {
					String[] rst = result.split("<IB>");
					result = rst[0];
					if (rst.length > 1) {
						errorst = rst[1];
					}

				}
        // result = "Send Success";
				System.out.println("empIdObj.getTrycount()-------------"+empIdObj.getTrycount());
				System.out.println("result---------------"+result);
        if (result.equalsIgnoreCase("Send Success")) {
          System.out.println("out table");
          String outTblres = insetSmsOutTable(nmobno, cardno, message, pollFlg, smsTemplateid);
          System.out.println(outTblres);
        } else {
          String errorTblres = insetSmsErrorTable(nmobno, cardno, message, pollFlg, smsTemplateid, empIdObj.getTrycount(), errorst);
          System.out.println(errorTblres);
        }
        ival++;
        if (smsconf.getFetchcount() == ival) {
          break;
        }
      }
      Thread.sleep(1000);
      ccount--;
      return result;
    } catch (Exception ex) {
    	System.out.println("Excception Found in "+getClass().getSimpleName()+".class getSsmDeatilDb() : " + ex);
      return "error";
    }
  }

  /**
   * Sending sms by using sms provider.
   * 
   * @param mobnumber
   *          .
   * @param crdno
   *          .
   * @param msgg
   *          .
   * @param polflg
   *          .
   * @return send success.
   */
  public String sendSms(String mobnumber, String crdno, String msgg,
      String polflg) {
		BufferedReader br = null;
		int respCode;
		String respMsg;
		int year;
		try {
			String content = "";
			URL url = null;
			String online = "";
			String sender = "";
			Date date = new Date(); // your date
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			String mnt = "";
			if (month < 10) {
				mnt = "0";
			}
			int day = cal.get(Calendar.DAY_OF_MONTH);
			String dy = "";
			if (day < 10) {
				dy = "0";
			}
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			String hr = "";
			if (hour < 10) {
				hr = "0";
			}
			int minute = cal.get(Calendar.MINUTE);
			String min = "";
			if (minute < 10) {
				min = "0";
			}
			int second = cal.get(Calendar.SECOND);
			String sec = "";
			if (second < 10) {
				sec = "0";
			}
			String autonum = Integer.toString(year) + mnt + Integer.toString(month)
					+ dy + Integer.toString(day) + hr + Integer.toString(hour) + min
					+ Integer.toString(minute) + sec + Integer.toString(second);
			System.out.println(autonum);
      // try {
      // System.out.println("Url==="+smsconf.gethttpUrl());
      // url = new URL(smsconf.gethttpUrl());
      // } catch (MalformedURLException ex) {
      // System.out.println("Exception Found URL--" + ex);
      // }
      // if ((smsconf.getProviderName()).equalsIgnoreCase("india.timessms")) {
      // content = "username=" + smsconf.getUserName() + "&password=" +
      // smsconf.getPassword() +
      // "&sender=" + smsconf.getSenderName() + "&cdmasender="
      // + smsconf.getCdmaSender() + "&to=" + mobnumber + "&message=" + msgg +
      // "xx&transid=" +
      // autonum;
      // content = "username=" + smsconf.getUserName() + "&password=" +
      // smsconf.getPassword() +
      // "&sender="
      // + smsconf.getSenderName() + "&cdmasender=" + smsconf.getCdmaSender() +
      // "&to=" + mobnumber +
      // "&message=" + msgg ;
      // } else if ((smsconf.getProviderName()).equalsIgnoreCase("Nexmo")) {
      // content = "api_key=" + smsconf.getUserName() + "&api_secret=" +
      // smsconf.getPassword() + ""
      // + "&from=InternetBanking&to=" + mobnumber + "&client-ref=" + autonum +
      // "&text=" + msgg;
      // }
      // System.out.println("content---"+content);
      // HttpURLConnection connection = (HttpURLConnection)
      // url.openConnection();
      // connection.setRequestMethod("POST");
      // connection.setDoOutput(true);
      // connection.setDoInput(true);
      // connection.setUseCaches(false);
      // OutputStreamWriter wr = new OutputStreamWriter(
      // connection.getOutputStream());
      // wr.write(content);
      // wr.flush();
      // wr.close();
      //
      // connection.connect();// throw exception
      // br = new BufferedReader(new
      // InputStreamReader(connection.getInputStream())); // resd
      // response data from // URL
      // respCode = connection.getResponseCode();// 200, 404, etc
      // respMsg = connection.getResponseMessage();// OK, Forbidden, etc

			if ((smsconf.getProviderName()).equalsIgnoreCase("mVaayoo")) {
				String encodedMsg = URLEncoder.encode(msgg, "UTF-8");
				sender = URLEncoder.encode(smsconf.getSenderName(), "UTF-8");
        		content = "user=" + smsconf.getUserName() + ":" + smsconf.getPassword()
        				+ "&senderID=" + sender + "&dcs=0&receipientno=" + mobnumber
        				+ "&msgtxt=" + encodedMsg + "&state=0";
      		} else if ((smsconf.getProviderName()).equalsIgnoreCase("india.timessms")) {

      			String encodedMsg = URLEncoder.encode(msgg, "UTF-8");
      			content = "username=" + smsconf.getUserName() + "&password="
      					+ smsconf.getPassword() + "&sender=" + sender + "&cdmasender="
      					+ smsconf.getCdmaSender() + "&to=" + mobnumber + "&message="
      					+ encodedMsg;

			} else if ((smsconf.getProviderName()).equalsIgnoreCase("mysmsmantra")) {
				if (mobnumber.indexOf("+91") > -1) {
					mobnumber = mobnumber.substring(1);
				}
				content = "username=" + smsconf.getUserName() + "&password="
						+ smsconf.getPassword() + "&sendername=Peninlog&" + "mobileno="
						+ mobnumber + "&message=" + msgg;
			} else if ((smsconf.getProviderName()).equalsIgnoreCase("nexmo")) {
				if (mobnumber != null && mobnumber.indexOf("+91") > -1) {
					mobnumber = mobnumber.substring(1);
				} else if (mobnumber != null && mobnumber.length() == 10) {
					mobnumber = "91" + mobnumber;
				}
				msgg=msgg.replaceAll("\\s", "%20");
				content = "&from=SendVoucher&" + "to="+ mobnumber + "&text=" + msgg;
			} else if((smsconf.getProviderName()).equalsIgnoreCase("gupshup")){
    	  		// msgg = "This is text message"&mask="+smsconf.getSenderName();
				if (mobnumber != null && mobnumber.indexOf("+91") > -1) {
					mobnumber = mobnumber.substring(1);
				} else if (mobnumber != null && mobnumber.length() == 10) {
					mobnumber = "91" + mobnumber;
				}
    	  		msgg = URLEncoder.encode(msgg);
    	  		content = "&userid=" + smsconf.getUserName() + "&password=" + smsconf.getPassword() + "&send_to=" + mobnumber + "&msg=" + msgg+"&override_dnd=TRUE";
			} else{
				
			}
			try {
				url = new URL(smsconf.getHttpUrl() + content); // whenever send sms
				// url = new URL("");
			} catch (MalformedURLException ex) {
				System.out.println("Exception Found URL : " + ex);
			}

			// http://india.timessms.com/http-api/receiverall.asp?username=pkaran_bsc&password=33707215&sender=Demo&cdmasender=9952590015&to=919884303150&message=Hello
			// http://india.timessms.com/http-api/receiverall.asp?username=pkaran_bsc&password=33707215&sender=Demo&cdmasender=9952590015&to=9787103262&message=Peninlog
			// Test
			// http://api.mvaayoo.com/mvaayooapi/MessageCompose?user=jsureshkumar.sureshkumar@gmail.com:02122511&senderID=BCAG&receipientno=+919884303150&msgtxt=TEST%20MESSAGE&state=0
			System.out.println("API URL : " + smsconf.getHttpUrl() + content);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();
			br = new BufferedReader(new InputStreamReader(conn.getInputStream())); // resd
			// data from URL
			respCode = conn.getResponseCode();// 200, 404, etc
			respMsg = conn.getResponseMessage();// OK, Forbidden, etc
			System.out.println("respMsg----------------"+respMsg);
			if ((smsconf.getProviderName()).equalsIgnoreCase("mVaayoo")) {
				if (respCode == 200 && respMsg.equalsIgnoreCase("OK")) {
					online = br.readLine();
					Pattern pt = Pattern.compile(",");
					String[] resultt = pt.split(online);
					if (resultt[0].equalsIgnoreCase("Status=0") && resultt[1].equalsIgnoreCase("success")) {
						return "Send Success<IB>";
					} else {
						return "Not Send<IB>";
					}
				} else {
					online = "Not Send<IB>";
					return online;
				}
			} else if ((smsconf.getProviderName()).equalsIgnoreCase("india.timessms")) {
				if (respCode == 200 && respMsg.equalsIgnoreCase("OK")) {
					online = br.readLine();
					if (online.length() > 0) {
						return "Send Success<IB>";
					} else {
						return "Not Send<IB>";
					}

				} else {
					online = "Not Send<IB>";
					return online;
				}
			} else if ((smsconf.getProviderName()).equalsIgnoreCase("Nexmo")) {
				if (respCode == 200 && respMsg.equalsIgnoreCase("OK")) {
					String errortxt = "";
					try {
						StringBuilder getresponse = new StringBuilder();
						String ipline;
						while ((ipline = br.readLine()) != null) {
							getresponse.append(ipline);
						}
						JSONObject messagest = new JSONObject(getresponse.toString());
						JSONArray jarr = (JSONArray) messagest.get("messages");
						JSONObject jsonObject = (JSONObject) jarr.get(0);
						String status = "";
						if(jsonObject!=null && jsonObject.has("status")){
							Object statusobj= jsonObject.get("status");
							status = (String) statusobj;
						}
						if (status != null && status.equalsIgnoreCase("0")) {
							return "Send Success<IB>" + errortxt;
						} else {
							try {
								errortxt = jsonObject.getString("error-text");
							} catch (Exception ex) {
								errortxt = "";
							}
							return "Not Send<IB>" + errortxt;
						}
					} catch (Exception ex) {
						System.out.println("exception ex-------------" + ex);
						return "Send Success<IB>" + errortxt;
					}
				} else {
					online = "Not Send<IB>";
					return online;
				}
			} else if ((smsconf.getProviderName()).equalsIgnoreCase("gupshup")) {
				if (respCode == 200 && respMsg.equalsIgnoreCase("OK")) {
					StringBuilder getresponse = new StringBuilder();
					String ipline;
					while ((ipline = br.readLine()) != null) {
						getresponse.append(ipline);
					}
					if (getresponse != null && getresponse.toString().contains("|")){        		  
						String lvrRsp = getresponse.toString().substring(0,getresponse.indexOf("|")).trim();
						String lvrRsperr = getresponse.toString().substring(getresponse.lastIndexOf("|")+1,getresponse.length()).trim();
						if(lvrRsp!=null && lvrRsp.trim().equalsIgnoreCase("success")){
							System.out.println("gupshup : SMS send success");
							return "Send Success<IB>";
						} else if (lvrRsp != null && lvrRsp.trim().equalsIgnoreCase("error")) {
							return "Not Send<IB>" + lvrRsperr;
						} else {
							return "Not Send<IB>";
						}
					}
				} else {
					return "Not Send<IB>";
				}
			}
			return "error<IB>";
		} catch (Exception ex) {
			System.out.println("exception occur yy=====" + ex);
			return "error<IB>";
		}
	}

  /**
   * Sms records moved from in table to out tbl.
   * 
   * @param mobno
   *          .
   * @param carno
   *          .
   * @param messagee
   *          .
   * @param pflag
   *          .
   * @param tempid
   *          .
   * @return success.
   */
  public String insetSmsOutTable(String mobno, String carno, String messagee,
      String pflag, String tempid) {
    try {
      Date date = common.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
      String retu = "";

      if (mobno.contains(",")) {
        String[] mob = mobno.split(",");
        for (int inc = 0; inc < mob.length; inc++) {
          smsout.setMobNo(mob[inc]);
          smsout.setCardNo(carno);
          smsout.setMessage(messagee);
          smsout.setEntryDate(date);
          smsout.setPoolFlag(pflag);
          smsout.setSmstempId(tempid);
          retu = smseng.insertsmsout(smsout);
        }
        smseng.deletesmsout(smsout);
      } else {
        smsout.setMobNo(mobno);
        smsout.setCardNo(carno);
        smsout.setMessage(messagee);
        smsout.setEntryDate(date);
        smsout.setPoolFlag(pflag);
        smsout.setSmstempId(tempid);
        retu = smseng.insertsmsout(smsout);
        smseng.deletesmsout(smsout);
      }
      return retu;
    } catch (Exception ex) {
      System.out.println("Exception found ---->Inset SMS_Out_TABLE " + ex);
      return "Not Insert";
    } finally {
      /**
       * Empty block.
       */
    }

  }

  /**
   * Sms records inserted into error table.
   * 
   * @param nmobno
   *          .
   * @param cardno
   *          .
   * @param message
   *          .
   * @param Pollflg
   *          .
   * @param temp_id
   *          .
   * @param tyrcnt
   *          .
   * @param errst
   *          .
   * @return sms inserted.
   */
  private String insetSmsErrorTable(String nmobno, String cardno, String message, String pollFlg, String tempid, int tyrcnt, String errst) {
    try {
      Date date = common.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
      String retu = "";
      if (nmobno.contains(",")) {
        String[] mob = nmobno.split(",");
        for (int inc = 0; inc < mob.length; inc++) {
          smserror.setMobNo(mob[inc]);
          smserror.setCardNo(cardno);
          smserror.setMessage(message);
          smserror.setEntryDate(date);
          smserror.setPoolFlag(pollFlg);
          smserror.setSmstempId(tempid);
          retu = smseng.insertsmserror(smserror);
        }
        smseng.deletesmserror(smserror);
      } else {
        if (tyrcnt >= 5) {
          smserror.setMobNo(nmobno);
          smserror.setCardNo(cardno);
          smserror.setMessage(message);
          smserror.setEntryDate(date);
          smserror.setPoolFlag(pollFlg);
          smserror.setSmstempId(tempid);
          smserror.setErrorstatus(errst);
          retu = smseng.insertsmserror(smserror);
          smseng.deletesmserror(smserror);
        } else {
        	String qry = null;
        	if (cardno==null) {
        		qry = "update SmsInpojo set smspollFlag='F', trycount=trycount+1 where smsCardNo=" + cardno + " and smspollFlag='P'";
        	} else {
        		qry = "update SmsInpojo set smspollFlag='F', trycount=trycount+1 where smsCardNo='" + cardno + "' and smspollFlag='P'";
        	}
          
          smseng.reportFlgUpdate(qry);
        }
      }
      return retu;
    } catch (Exception ex) {
      System.out.println("Exception found ---->InsetSMS_Error_TABLE" + ex);
      return "Not Insert";
    } finally {
      /**
       * Empty block.
       */
    }
  }

  public List<SmsInpojo> getSmsconfig() {
    return smsconfig;
  }

  public void setSmsconfig(List<SmsInpojo> smsconfig) {
    this.smsconfig = smsconfig;
  }

  public SmssmppConfpojo getSmsconf() {
    return smsconf;
  }

  public void setSmsconf(SmssmppConfpojo smsconf) {
    this.smsconf = smsconf;
  }

  public SmsErrorpojo getSmserror() {
    return smserror;
  }

  public void setSmserror(SmsErrorpojo smserror) {
    this.smserror = smserror;
  }

  public SmsOutpojo getSmsout() {
    return smsout;
  }

  public void setSmsout(SmsOutpojo smsout) {
    this.smsout = smsout;
  }
}
