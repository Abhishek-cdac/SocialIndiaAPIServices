package com.social.email;


import com.opensymphony.xwork2.ActionSupport;
import com.social.email.persistense.EmailConfigTblVo;
import com.social.email.persistense.EmailEngineDaoServices;
import com.social.email.persistense.EmailEngineServices;

public class Emailcall extends ActionSupport {

  private static final long serialVersionUID = 1L;
  private String emailid;
  EmailConfigTblVo emailconfObj = new EmailConfigTblVo();
  EmailEngineServices emleng = new EmailEngineDaoServices();

  /**
   * Email call excute action.
   */
  @Override
  public String execute() {
    System.out.println("enter execute method");

    // email insert
    emailid = "test@ggm.comm";
    // String randompwd=common.randInt(99999,999999);
    //
    // String emqry = "FROM  EmailTemplateTblVo where tempName ='Reset Password' and status ='1'";
    // emailTemplate=emailservice.EmailTemplateData(emqry);
    // String emailMessage = emailTemplate.getTempContent();
    // emqry = common.templateParser(emailMessage,1,"",randompwd);
    // String[] qrySplit = emqry.split("!_!");
    // String qry = qrySplit[0]
    // + " FROM USER_MASTER_TBL  WHERE EMAILID='"+femailid+"'";
    // emailMessage = emailservice.templateParserChange(
    // qry, Integer.parseInt(qrySplit[1]),
    // emailMessage);
    // emailTemplate.setTempContent(emailMessage);
    // emailMessage = emailTemplate.getHeader()
    // + emailTemplate.getTempContent()
    // + emailTemplate.getFooter();
    // EmailInsertFuntion emailfun = new EmailInsertFuntion();
    // System.out.println("corpInfo========"+corpInfo.getEmailId());
    // emailfun.Test2(femailid, emailTemplate.getSubject(), emailMessage);
    // // email insert

    EmailInsertFuntion emailfun = new EmailInsertFuntion();
    for (int i = 0; i < 2; i++) {
      emailfun.test2(emailid, "Test Msg", "Test Template");
    }

    System.out.println("end execute method");

    emailconfObj = emleng.emailConfigval();

    return SUCCESS;
  }

  public EmailConfigTblVo getEmailconfObj() {
    return emailconfObj;
  }

  public void setEmailconfObj(EmailConfigTblVo emailconfObj) {
    this.emailconfObj = emailconfObj;
  }

}
