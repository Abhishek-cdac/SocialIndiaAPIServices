package com.mobi.merchant;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobi.common.mobiCommon;
import com.mobile.otpVo.otpDao;
import com.mobile.otpVo.otpDaoService;
import com.opensymphony.xwork2.ActionSupport;
import com.pack.bitly.GetBitlyLink;
import com.pack.utilitypkg.Common;
import com.pack.utilitypkg.CommonDao;
import com.pack.utilitypkg.Commonutility;
import com.siservices.login.EncDecrypt;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.social.email.EmailInsertFuntion;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailTemplateTblVo;
import com.social.sms.persistense.SmsEngineDaoServices;
import com.social.sms.persistense.SmsEngineServices;
import com.social.sms.persistense.SmsInpojo;
import com.social.sms.persistense.SmsTemplatepojo;
import com.social.utils.CommonUtilsDao;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;
import com.socialindiaservices.vo.MerchantCategoryTblVO;
import com.socialindiaservices.vo.MerchantProductItemsTblVO;
import com.socialindiaservices.vo.MerchantProductOrderTblVO;
import com.socialindiaservices.vo.MerchantTblVO;

public class CustomerProductOrder extends ActionSupport {
	Log log=new Log();	
	private String ivrparams;
	otpDao otp=new otpDaoService();
	MerchantDao merchanthbm=new MerchantDaoServices();
	List<MerchantCategoryTblVO> merchantCategoryList=new ArrayList<MerchantCategoryTblVO>();
	CommonUtils commjvm=new CommonUtils();
	ResourceBundle lvrRbdl = ResourceBundle.getBundle("applicationResources");
	Common locCommonObj=new CommonDao();
	SmsInpojo sms = new SmsInpojo();
	 private SmsEngineServices smsService = new SmsEngineDaoServices();
	 private SmsTemplatepojo smsTemplate;
	 CommonUtilsDao common=new CommonUtilsDao();	
	 
	public String execute(){
		
		System.out.println("************mobile Search CarPool List services******************");
		JSONObject json = new JSONObject();
		Integer otpcount=0;
		boolean updateResult=false;
		JSONObject locObjRecvJson = null;//Receive String to json	
		JSONObject locObjRecvdataJson = null;// Receive Data Json		
		JSONObject locObjRspdataJson = null;// Response Data Json.
		StringBuilder locErrorvalStrBuil =null;
		Date timeStamp = null;
		boolean flg = true;
		String servicecode="";
		try{
			locErrorvalStrBuil = new StringBuilder();
			ivrparams = EncDecrypt.decrypt(ivrparams);
			boolean ivIsJson = Commonutility.toCheckIsJSON(ivrparams);
			if (ivIsJson) {		
				locObjRecvJson = new JSONObject(ivrparams);
				 servicecode=(String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "servicecode");
				String townshipKey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "townshipid");
				String societykey = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson, "societykey");
				locObjRecvdataJson =(JSONObject) Commonutility.toHasChkJsonRtnValObj(locObjRecvJson,"data");
				String rid = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "rid");
				String toMerchent = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "merchant_id");
				String customerComments = (String) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "order_comments");
				JSONArray itemList = (JSONArray) Commonutility.toHasChkJsonRtnValObj(locObjRecvdataJson, "item_list");
				
				
				System.out.println("rid----------------"+rid);
				System.out.println("itemList-------------------"+itemList.toString());
				if (Commonutility.checkempty(servicecode)) {
					if (servicecode.trim().length() == Commonutility.stringToInteger(getText("service.code.fixed.length"))) {
						
					} else {
						String[] passData = { getText("service.code.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("service.code.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("Service code cannot be empty")));
				}
				if (Commonutility.checkempty(townshipKey)) {
					if (townshipKey.trim().length() == Commonutility.stringToInteger(getText("townshipid.fixed.length"))) {
						
					} else {
						String[] passData = { getText("townshipid.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("townshipid.error")));
				}
				if (Commonutility.checkempty(societykey)) {
					if (societykey.trim().length() == Commonutility.stringToInteger(getText("society.fixed.length"))) {
						
					} else {
						String[] passData = { getText("society.fixed.length") };
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.length", passData)));
					}
				} else {
					flg = false;
					locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("societykey.error")));
				}
				
				if (locObjRecvdataJson !=null) {
					if (Commonutility.checkempty(rid)) {
					} else {
						flg = false;
						locErrorvalStrBuil.append(Commonutility.validateErrmsgForm(getText("rid.error")));
					}
				}
				
				
				
				if(flg){
			boolean result=otp.checkTownshipKey(rid,townshipKey);
			if(result==true){
			System.out.println("********result*****************"+result);
			UserMasterTblVo userMst=new UserMasterTblVo();
			userMst=otp.checkSocietyKeyForList(societykey,rid);
			if(userMst!=null){
				Date dt=new Date();
				int societyId=userMst.getSocietyId().getSocietyId();
				MerchantProductOrderTblVO orderobj=new MerchantProductOrderTblVO();
				MerchantProductItemsTblVO itemobj=new MerchantProductItemsTblVO();
				MerchantTblVO merchantobj=new MerchantTblVO();
				merchantobj=merchanthbm.getMerchantobject(Integer.parseInt(toMerchent));
				if(merchantobj!=null){
				orderobj.setOrderComments(customerComments);
				orderobj.setStatus(1);
				orderobj.setEntryDatetime(dt);
				orderobj.setOrderBy(userMst);
				orderobj.setMrchntId(merchantobj);
				boolean isInsert=merchanthbm.saveorderTblObj(orderobj);
				String encryptflg = EncDecrypt.encrypt(""+orderobj.getOrderId());
				String finalFlag=URLEncoder.encode(encryptflg);
				String orderUrl=getText("SOCIAL_INDIA_BASE_URL")  +getText("web.project.name")+"/getProductOrderDetail?orderId="+finalFlag;
				System.out.println(orderobj.getOrderId()+"orderUrl==========="+orderUrl);
				String locbityurl = GetBitlyLink.toGetBitlyLinkMthd(orderUrl, "yes", getText("bitly.accesstocken"), getText("bitly.server.url"));
				System.out.println("locbityurl-------------"+locbityurl);
				JSONObject itemdetailobj=new JSONObject();
				for(int l=0;l<itemList.length();l++){
					itemdetailobj=new JSONObject();
					itemobj=new MerchantProductItemsTblVO();
					
					itemdetailobj=(JSONObject) itemList.get(l);
					String itemName = (String) Commonutility.toHasChkJsonRtnValObj(itemdetailobj, "item_name");
					String orderQuantity = (String) Commonutility.toHasChkJsonRtnValObj(itemdetailobj, "order_quantity");
					
					itemobj.setOrderId(orderobj);
					itemobj.setItemName(itemName);
					itemobj.setOrderQty(Integer.parseInt(orderQuantity));
					itemobj.setStatus(1);
					itemobj.setEntryDatetime(dt);
					boolean itemInsert=merchanthbm.saveitemTblObj(itemobj);
					itemdetailobj=null;
					itemobj=null;
				}
				
				if(isInsert){
					//SMS to Merchant
					  try {
						  
						  String mailRandamNumber;
				            mailRandamNumber = common.randInt(5555, 999999999);
				            String qry = "FROM SmsTemplatepojo where " + "templateName ='Customer order to merchant' and status ='1'";
				            smsTemplate = smsService.smsTemplateData(qry);
				            String smsMessage = smsTemplate.getTemplateContent();
				            qry = common.smsTemplateParser(smsMessage, 1, "", locbityurl);
				            String[] qrySplit = qry.split("!_!");
				            String qryform = qrySplit[0]+""
				           +" FROM UserMasterTblVo as cust where cust.userId='"  + userMst.getUserId() + "'";
				            smsMessage = smsService.smsTemplateParserChange(qryform,Integer.parseInt(qrySplit[1]), smsMessage);
				            sms.setSmsCardNo(mailRandamNumber);
				            sms.setSmsEntryDateTime(common.getStrCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				            sms.setSmsMobNumber(merchantobj.getMrchntPhNo());
				            sms.setSmspollFlag("F");
				            sms.setSmsMessage(smsMessage);
				            smsService.insertSmsInTable(sms);
				          } catch (Exception ex) {
				            System.out.println("Exception sms send usercreate---->> " + ex);
				            log.logMessage("Exception in Forgetpassword() smsFlow----> "
				                + ex, "error", CustomerProductOrder.class);
				           
				          }
					
					//Email to Merchant
					  EmailEngineServices emailservice = new EmailEngineDaoServices();
					  EmailTemplateTblVo emailTemplate;
						try {
							 String emqry = "FROM EmailTemplateTblVo where "+ "tempName ='Customer order to merchant' and status ='1'";
					            emailTemplate = emailservice.emailTemplateData(emqry);
					            String emailMessage = emailTemplate.getTempContent();
					            emailMessage = emailMessage.replace("[MERCHANTNAME]", merchantobj.getMrchntName()); 
					            emqry = common.templateParser(emailMessage, 1, "", locbityurl);
					            String[] qrySplit = emqry.split("!_!");
					            String qry = qrySplit[0] + " FROM UserMasterTblVo as cust where cust.userId='"  + userMst.getUserId() + "'";					           
					            emailMessage = emailservice.templateParserChange(qry, Integer.parseInt(qrySplit[1]),emailMessage);					           					            					          					          
					            emailTemplate.setTempContent(emailMessage);
					            emailMessage = emailTemplate.getHeader() + emailTemplate.getTempContent() + emailTemplate.getFooter();
					            
					            EmailInsertFuntion emailfun = new EmailInsertFuntion();
					            emailfun.test2(merchantobj.getMrchntEmail(), emailTemplate.getSubject(), emailMessage);
				          } catch (Exception ex) {
				            System.out.println("Excption found in CustomerProductOrder.class : " + ex);
				            log.logMessage("Exception in CustomerProductOrder flow emailFlow----> " + ex, "error",CustomerProductOrder.class);
				            
				          }	
					
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"00","R0001",mobiCommon.getMsg("R0001"),locObjRspdataJson);
				}else{
					locObjRspdataJson=new JSONObject();
					serverResponse(servicecode,"01","R0058",mobiCommon.getMsg("R0058"),locObjRspdataJson);
				}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0059",mobiCommon.getMsg("R0059"),locObjRspdataJson);
			}
				
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0026",mobiCommon.getMsg("R0026"),locObjRspdataJson);
			}
			}else{
				locObjRspdataJson=new JSONObject();
				serverResponse(servicecode,"01","R0015",mobiCommon.getMsg("R0015"),locObjRspdataJson);		
			}
			
			
			}else{
				locObjRspdataJson=new JSONObject();
				locObjRspdataJson.put("fielderror", Commonutility.removeSPtag(locErrorvalStrBuil.toString()));
				serverResponse(servicecode,getText("status.validation.error"),"R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
			}
		}else{
			locObjRspdataJson=new JSONObject();
			log.logMessage("Service code : "+servicecode+" Request values are not correct format", "info", CustomerProductOrder.class);
			serverResponse(servicecode,"01","R0016",mobiCommon.getMsg("R0016"),locObjRspdataJson);
		}
		}catch(Exception ex){
			System.out.println("=======signUpMobile====Exception===="+ex);
			log.logMessage("Service code : ivrservicecode, Sorry, signUpMobile an unhandled error occurred", "info", CustomerProductOrder.class);
			locObjRspdataJson=new JSONObject();
			serverResponse(servicecode,"01","R0002",mobiCommon.getMsg("R0002"),locObjRspdataJson);
		}
		
		return SUCCESS;
	}

	
	private void serverResponse(String serviceCode, String statusCode, String respCode, String message, JSONObject dataJson)
	{
		PrintWriter out=null;
		JSONObject responseMsg = new JSONObject();
		HttpServletResponse response=null;
		response = ServletActionContext.getResponse();		
		try {
			out = response.getWriter();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			mobiCommon mobicomn=new mobiCommon();
			String as = mobicomn.getServerResponse(serviceCode, statusCode, respCode, message, dataJson);
			out.print(as);
			out.close();
		} catch (Exception ex) {
			try{
			out = response.getWriter();
			out.print("{\"servicecode\":\"" + serviceCode + "\",");
			out.print("{\"statuscode\":\"2\",");
			out.print("{\"respcode\":\"E0002\",");
			out.print("{\"message\":\"Sorry, an unhandled error occurred\",");
			out.print("{\"data\":\"{}\"}");
			out.close();
			ex.printStackTrace();
			}catch(Exception e){}finally{}
		}
	}

	public String getIvrparams() {
		return ivrparams;
	}
	public void setIvrparams(String ivrparams) {
		this.ivrparams = ivrparams;
	}

	
}