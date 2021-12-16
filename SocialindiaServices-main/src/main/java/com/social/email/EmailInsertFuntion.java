package com.social.email;

import com.opensymphony.xwork2.ActionSupport;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailInTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;


public class EmailInsertFuntion extends ActionSupport {

  private static final long serialVersionUID = 1L;
  static int totcat = 0;
  static String attachement_typ = null;
  static String template_id = null;
  static String FileNAem = null;
  static String Password_pdf = null;
  static String tempvalue = null;
  static String to_id = null;
  static String cc_id = null;
  static String bcc_id = null;
  static String Subject = "Test";
  static String body = "No Body Content";
  static String ad_Id = null;
  static String cc_temp = null;
  static String bodyTYPE = "";
  private EmailInTblVo emailin = new EmailInTblVo();
  CommonUtils common = new CommonUtilsDao();

  /**
   * test mail insert.
   * 
   * @param to
   *          .
   * @param sub
   *          .
   * @param bodyy
   *          .
   * @return inserted.
   */
  public String test2(String to, String sub, String bodyy) {    
    String returnval = inser_query(to, cc_id, bcc_id, sub, attachement_typ,
        bodyy, template_id, FileNAem, Password_pdf, tempvalue, "NFT");
    return returnval;
  }

  /**
   * Sending mail action.
   * 
   * @param to
   *          .
   * @param cc
   *          .
   * @param bcc
   *          .
   * @param sub
   *          .
   * @param attType
   *          .
   * @param templateId
   *          .
   * @param firstName
   *          .
   * @param templateVal
   *          .
   * @param bodyType
   *          .
   * @param body
   *          .
   * @param passwordPdf
   *          .
   * @return mail sent success.
   */
  public String singleMail(String to, String cc, String bcc, String sub,
      String attType, String templateId, String firstName, String templateVal,
      String bodyType, String body, String passwordPdf) {
    String returnval = inser_query(to, cc, bcc, sub, attType, body, templateId,
        firstName, passwordPdf, templateVal, bodyType);
    return returnval;
  }

  /**
   * Inserting records for mail action.
   * 
   * @param toid
   *          .
   * @param cc
   *          .
   * @param bcc
   *          .
   * @param subb
   *          .
   * @param atchtyp
   *          .
   * @param bdyy
   *          .
   * @param atchfile
   *          .
   * @param bodyType
   *          .
   * @return records inserted.
   */
  public String sndwithattach(String toid, String cc, String bcc, String subb,
      String atchtyp, String bdyy, String atchfile, String bodyType) {
    String inserQuery = inser_query(toid, cc, bcc, subb, atchtyp, bdyy, "",
        atchfile, null, "", bodyType);
    return inserQuery;
  }

  /**
   * inser_query.
   * 
   * @param toId
   *          .
   * @param ccId
   *          .
   * @param bccId
   *          .
   * @param subject
   *          .
   * @param attachementTyp
   *          .
   * @param body
   *          .
   * @param templateId
   *          .
   * @param fileNAem
   *          .
   * @param passwordPdf
   *          .
   * @param tempvalue
   *          .
   * @param bodyType
   *          .
   * @return records.
   */
  public String inser_query(String toId, String ccId, String bccId,
      String subject, String attachementTyp, String body, String templateId,
      String fileNAem, String passwordPdf, String tempvalue, String bodyType) {
    EmailEngineServices emailHbm = new EmailEngineDaoServices();
    try {      
      int randNumber = (int) (Math.random() * 999);
      emailin.setToId(toId);
      emailin.setCcId(ccId);
      emailin.setBccId(bccId);
      emailin.setSubId(subject);
      emailin.setStatFlag("P");
      emailin.setContFormat(attachementTyp);
      emailin.setBodyContent(body);
      emailin.setAttTempId(templateId);
      emailin.setInDate(common.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
      emailin.setTempId("Temp_ID" + randNumber);
      emailin.setFileName(fileNAem);
      emailin.setPdfPass(passwordPdf);
      emailin.setTempVal(tempvalue);
      emailin.setBodyContFlag(bodyType);
      emailHbm.insertemailIntblval(emailin);
      return "Success";
    } catch (Exception ex) {
      return "Not Success";
    } finally {
      /**
       * Empty block.
       */
    }
  }

  public EmailInTblVo getEmailin() {
    return emailin;
  }

  public void setEmailin(EmailInTblVo emailin) {
    this.emailin = emailin;
  }

}
