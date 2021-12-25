package com.social.email;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.social.common.CommonDaoService;
import com.social.common.CommonService;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;
import com.social.email.persistense.EmailErrorTblVo;
import com.social.email.persistense.EmailInTblVo;
import com.social.email.persistense.EmailOutTblVo;
import com.social.utils.CommonUtils;
import com.social.utils.CommonUtilsDao;

public class EmailFunctions extends Thread {

  String hostName = "";
  String usrName = "";
  String passWrd = "";
  String fromId = "";
  String protoCol = "";
  String portno = "";
  Properties props = System.getProperties();// For SMTP
  boolean sessionDebug = true;// console print in maill send time
  String filePath = "";// thi path used to store and retrive
  int sno = 0;
  String fromIdd = "";
  String toIdd = "";
  String ccIdd = "";
  String bccIdd = "";
  String subjectIdd = "";
  String attFormat = "";
  String bodycontent = "";
  String tempIdd = "";
  String attFilename = "";
  String returnResult = "";
  String pdfpassword = "";
  String templateAttId = "";
  String templateValue = "";
  String templatePdf = "";
  String bdyCntTyp = "";
  String bdyOrignalCnt = "";
  String bdycntUrl = "";
  int trycount = 0;
  private List lis = new ArrayList();
  int ival = 0;
  String tempRead = "";
  HashMap emailtrdmap = EmailEngineCntrl.email_thrdmap;
  String lossbid = "";
  private EmailOutTblVo emailout = new EmailOutTblVo();

  private EmailErrorTblVo emailerror = new EmailErrorTblVo();
  CommonUtils common = new CommonUtilsDao();
  CommonService commonHibernate = new CommonDaoService();
  EmailEngineServices emailHbm = new EmailEngineDaoServices();

  public EmailFunctions() {

  }

  /**
   * Email Function - get all details.
   * 
   * @param list
   *          .
   * @param pro
   *          .
   * @param uname
   *          .
   * @param pass
   *          .
   * @param hostname
   *          .
   * @param exPath
   *          .
   */
  public EmailFunctions(List list, Properties pro, String uname, String pass, String hostname, String exPath) {
	  lis = list;
	  props = pro;
	  bdycntUrl = props.getProperty("BodyContentIp");
	  String host = props.getProperty("hostName");
	  System.out.println(host);
	  hostName = hostname;
	  usrName = uname;
	  passWrd = pass;
	  filePath = exPath;
  }

  /**
   * Email func run action.
   */
  public void run() {
    try {
      Thread ct = Thread.currentThread();     
      getEmailDetail_Db(lis);
    } catch (Exception ex) {
      System.out.println("Exception in emailFunction run()-- " + ex);
    }
  }

  /**
   * Getting Email Details.
   * 
   * @param lisst
   *          .
   * @return template send.
   */
  public String getEmailDetail_Db(List lisst) {
    try {
      EmailInTblVo emIdObj;
      List lst = lisst;
      for (Iterator<EmailInTblVo> it = lst.iterator(); it.hasNext();) {
        emIdObj = it.next();
        sno = emIdObj.getEmailId();
        fromIdd = common.checkNull(emIdObj.getFromId());
        toIdd = common.checkNull(emIdObj.getToId());
        ccIdd = common.checkNull(emIdObj.getCcId());
        bccIdd = common.checkNull(emIdObj.getBccId());
        subjectIdd = common.checkNull(emIdObj.getSubId());
        attFormat = common.checkNull(emIdObj.getContFormat());
        bodycontent = common.checkNull(emIdObj.getBodyContent());
        tempIdd = common.checkNull(emIdObj.getTempId());// temprory id unique id
        attFilename = common.checkNull(emIdObj.getFileName());
        pdfpassword = common.checkNull(emIdObj.getPdfPass());
        templateAttId = common.checkNull(emIdObj.getAttTempId());
        templateValue = common.checkNull(emIdObj.getTempVal());
        bdyCntTyp = common.checkNull(emIdObj.getBodyContFlag());
        bdyOrignalCnt = bodycontent;
        trycount = emIdObj.getTrycount();
        if (templateValue.equalsIgnoreCase("null")
            || templateValue.equalsIgnoreCase(null)) {
          templateValue = "";
        }
        if (attFormat == null || attFormat.equalsIgnoreCase("null")
            || attFormat.equalsIgnoreCase("")) {
          attFormat = "No Attachment";
        }
        if (templateAttId == null || templateAttId.equalsIgnoreCase("null")) {
          templateAttId = "";
        }
        templatePdf = "NoTemplate";// this variable used to template
        // pdf selection
        if (bdyCntTyp.equalsIgnoreCase("NFT")) {
          bodycontent = bdyOrignalCnt;
        }
        if (attFormat.equalsIgnoreCase("Other")) { // html to password
          // pdf file
          returnResult = sendmail_withouttemplate(fromIdd, toIdd, subjectIdd,
              bodycontent, ccIdd, bccIdd, sno, attFormat, attFilename,
              pdfpassword, templatePdf);
        } else {
          returnResult = sendmail_withouttemplate(fromIdd, toIdd, subjectIdd,
              bodycontent, ccIdd, bccIdd, sno, attFormat, attFilename,
              pdfpassword, templatePdf);
        }
        fromIdd = "";
        toIdd = "";
        ccIdd = "";
        bccIdd = "";
        subjectIdd = "";
        attFormat = "";
        bodycontent = "";
        tempIdd = "";
        trycount = 0;
        Thread.sleep(1000);
      }
      lst.clear();
      return returnResult;
    } catch (Exception ex) {
      System.out.println("Exception Found [getEmailDetail_Db()]------>>" + ex);
      return "NotSuccess";
    } finally {
      try {
        String sql = "update EmailInTblVo set statFlag='P' where emailId in";
        sql += " (";
        EmailInTblVo empIdObj;
        int kj = 0;
        for (Iterator<EmailInTblVo> it = lis.iterator(); it.hasNext();) {
          empIdObj = it.next();
          if (kj == 0) {
            sql += "'" + empIdObj.getEmailId() + "'";
          } else {
            sql += ",'" + empIdObj.getEmailId() + "'";
          }
          kj++;
        }
        sql += ")";
        System.out.println("sql==============" + sql);
        if (kj > 0) {
          commonHibernate.commonUpdate(sql);
        }
      } catch (Exception ex) {
        System.out
            .println("Exception Found [getEmailDetail_Db()]------>>" + ex);
      }
    }

  }

  /**
   * Send mail template.
   * 
   * @param fromide
   *          .
   * @param toId
   *          .
   * @param subj
   *          .
   * @param bodyc
   *          .
   * @param cc
   *          .
   * @param bcc
   *          .
   * @param sno
   *          .
   * @param attTypee
   *          .
   * @param attchFilenaem
   *          .
   * @param pdfpassword1
   *          .
   * @param templatePdf
   *          .
   * @return Email successfully sent.
   */
  public String sendmail_withouttemplate(String fromide, String toId,
      String subj, String bodyc, String cc, String bcc, int sno,
      String attTypee, String attchFilenaem, String pdfpassword1,
      String templatePdf) {
    try {
      
       //Session mailSession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
      // protected PasswordAuthentication getPasswordAuthentication() { return new PasswordAuthentication(usrName, passWrd); } });
      
       Session mailSession = Session.getDefaultInstance(props,
               new javax.mail.Authenticator() {
                   protected PasswordAuthentication getPasswordAuthentication() {
                       return new PasswordAuthentication(usrName, passWrd);
                   }
               });       
     //Session mailSession = Session.getDefaultInstance(props, null);
      mailSession.setDebug(sessionDebug);
      Message msg = new MimeMessage(mailSession);
      msg.setFrom(new InternetAddress(usrName, usrName));
      if (toId == null || toId == "" || toId.equalsIgnoreCase("")
          || toId.equalsIgnoreCase("null")) {
        /**
         * Empty block.
         */
      } else {
        InternetAddress toAddress1 = new InternetAddress(toId);
        msg.addRecipient(Message.RecipientType.TO, toAddress1);
      }
      msg.setSubject(subj);
      MimeBodyPart mbp1 = new MimeBodyPart();
      MimeBodyPart mbp2 = new MimeBodyPart();
      MimeBodyPart mbp3 = new MimeBodyPart();
      mbp1.setContent(bodyc, "text/html"); // use setText if you want to
      // send text
      Multipart mp = new MimeMultipart();
      mp.addBodyPart(mbp1);
      // if attachmet type null or empty
      if (attTypee == null || attTypee == "No Attachment"
          || attTypee.equalsIgnoreCase("No Attachment") || attTypee == ""
          || attTypee.equalsIgnoreCase("null")) {
        /**
         * No attachment.
         */
      } else {
        FileDataSource fds = new FileDataSource(filePath + attchFilenaem);
        mbp2.setDataHandler(new DataHandler(fds));
        String sendatthmentfilename = "";
        String realFilename = fds.getName();
        if (fds.getName().contains("!_!")) {
          sendatthmentfilename = realFilename.substring(0,
              realFilename.indexOf("!_!"));
        } else {
          sendatthmentfilename = realFilename;
        }
        mbp2.setFileName(sendatthmentfilename);
        mp.addBodyPart(mbp2);
      }

      if (templatePdf.equalsIgnoreCase("NoTemplate")) {
        /**
         * No template.
         */
      } else {
        String templateHtml = templatePdf.replace("pdf", "html");
        File ff = new File(filePath + templateHtml);
        ff.delete();
        FileDataSource fds = new FileDataSource(filePath + templatePdf);
        mbp3.setDataHandler(new DataHandler(fds));
        mbp3.setFileName(fds.getName());
        mp.addBodyPart(mbp3);
      }     
      msg.setContent(mp);
      Transport transport = mailSession.getTransport("smtp");
      transport.connect(hostName, usrName, passWrd);
      transport.sendMessage(msg, msg.getAllRecipients());
      emailout.setFromId(usrName);
      emailout.setToId(toId);
      emailout.setCcId(cc);
      emailout.setBccId(bcc);
      emailout.setSubId(subj);
      emailout.setStatFlag("SU");
      emailout.setContFormat(attTypee);
      if (templateAttId == null || templateAttId.equalsIgnoreCase("") || templateAttId.equalsIgnoreCase("null")) {
        emailout.setBodyContent(bdyOrignalCnt);
      } else {
        emailout.setBodyContent("Template Send in Body (refferd in template id)");
      }
      emailout.setAttTempId(templateAttId);
      emailout.setOutDate(common.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
      emailout.setFileName(attchFilenaem);
      emailout.setPdfPass(pdfpassword1);
      emailHbm.insertemailOuttblval(emailout);
      String del = emailHbm.deletemailintblval(sno);
      if (del.equalsIgnoreCase("Successfully Deleted")) {
        System.out.println("mail delete = " + toId + "  SNO='" + sno + "'");
      } else {
        System.out.println("not deleted " + toId + " SNO='" + sno + "'");
      }
      if (templatePdf.equalsIgnoreCase("NoTemplate")) {
        /**
         * No template.
         */
      } else {
        File temltatt = new File(filePath + templatePdf);
        temltatt.delete();
      }
      return "SUCCESS";
    } catch (Exception ex) {
      try {
        String tval = errorEmailHandling(fromide, toId, subj, bodyc, cc, bcc, sno, attTypee, attchFilenaem, pdfpassword1, templatePdf, trycount);       
      } catch (Exception excep) {
        System.out.println("Exception Found [sendmail_withouttemplate ().errorEmailHandling insert ]: " + excep);

      }
      	System.out.println("Exception Found [sendmail_withouttemplate().own]: " + ex);
      if (ex.getMessage().contains("Daily sending quota exceeded")) {
        System.err.println("Daily sending quota exceeded");
      } else {
        /**
         * Empty block.
         */
      }

      return "Not Success";
    }
  }

  /**
   * Error email insert action.
   * 
   * @param fromide
   *          .
   * @param to
   *          .
   * @param subj
   *          .
   * @param bodyc
   *          .
   * @param cc
   *          .
   * @param bcc
   *          .
   * @param sno
   *          .
   * @param attTypee
   *          .
   * @param attchFilenaem
   *          .
   * @param pdfpassword1
   *          .
   * @param templatePdf
   *          .
   * @param trycount
   *          .
   * @return email inserted successfully.
   */
  public String errorEmailHandling(String fromide, String to, String subj,
      String bodyc, String cc, String bcc, int sno, String attTypee,
      String attchFilenaem, String pdfpassword1, String templatePdf,
      int trycount) {
    try {
      if (trycount >= 5 || (to==null || to.equalsIgnoreCase("")|| to.equalsIgnoreCase("null")) || (fromide==null || fromide.equalsIgnoreCase("")|| fromide.equalsIgnoreCase("null"))) {
        emailerror.setFromId(fromide);
        emailerror.setToId(to);
        emailerror.setCcId(cc);
        emailerror.setBccId(bcc);
        emailerror.setSubId(subj);
        emailerror.setStatFlag("ER");
        emailerror.setContFormat(attTypee);
        if (templateAttId == null || templateAttId.equalsIgnoreCase("") || templateAttId.equalsIgnoreCase("null")) {
          emailerror.setBodyContent(bodyc);
        }
        emailerror.setAttTempId(templateAttId);
        emailerror.setOutDate(common.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
        emailerror.setFileName(attchFilenaem);
        emailerror.setPdfPass(pdfpassword1);
        emailHbm.insertemailErrortblval(emailerror);
        emailHbm.deletemailintblval(sno);
        System.err.println("#######SUCCESS FULY INSERT TO ERROR TBL#####");
      } else {

        String qry = "update EmailInTblVo set statFlag='P', trycount=trycount+1 where emailId=" + sno + " and statFlag='SU'";
        emailHbm.reportFlgUpdate(qry);
      }
      return "SUCCESS";
    } catch (Exception ex) {
      System.err.println("Exception found---Error_EmailHandling()--->" + ex);
      return "NOT SUCCESS";
    }
  }

  public EmailOutTblVo getEmailout() {
    return emailout;
  }

  public void setEmailout(EmailOutTblVo emailout) {
    this.emailout = emailout;
  }

  public EmailErrorTblVo getEmailerror() {
    return emailerror;
  }

  public void setEmailerror(EmailErrorTblVo emailerror) {
    this.emailerror = emailerror;
  }

}
