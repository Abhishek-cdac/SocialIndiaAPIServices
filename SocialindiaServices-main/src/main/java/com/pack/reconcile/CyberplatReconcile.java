package com.pack.reconcile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.reconcilevo.CyberplatrecondataTblVo;
import com.pack.reconcilevo.CyberplatsetmtfileTblVo;
import com.pack.reconcilevo.PaygaterecondataTblVo;
import com.pack.reconcilevo.PaygatesetmtfileTblVo;
import com.pack.reconcilevo.persistance.ReconcileDaoService;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;

public class CyberplatReconcile {

  CommonUtils common = new CommonUtils();
  private ReconcileDaoService reconDaoService = new ReconcileDaoService();
  ResourceBundle ivrRbundle = ResourceBundle.getBundle("applicationResources");
  public String toStartCyberplatReconcile() {
    Log log = null;
    Session session = null;
    Transaction tx = null;
    String readfrmxls = null;
    JSONObject jsnobject = null;
    JSONArray jsonArray = null;
    String cpSettleId = null;
    String cpOldstlId = null;
    
    CyberplatsetmtfileTblVo cyberplatstmtfile = new CyberplatsetmtfileTblVo();
    UserMasterTblVo cpEntryBy = new UserMasterTblVo();
    Iterator cyberlistitr = null;
    ArrayList<CyberplatsetmtfileTblVo> cyberlist = new ArrayList<CyberplatsetmtfileTblVo>();
    session = HibernateUtil.getSessionFactory().openSession();
    tx = session.beginTransaction();
    boolean ifsaved = false;
    try {
      log = new Log();
      cyberlist = (ArrayList<CyberplatsetmtfileTblVo>) reconDaoService
          .togetCyberplatReconcilFiles();
      /*
       * lvrSltqry = "from CyberplatsetmtfileTblVo where statusFlag=1"; lvrQry =
       * session.createQuery(lvrSltqry); paylistitr = lvrQry.list().iterator();
       */
      cyberlistitr = cyberlist.iterator();
      while (cyberlistitr.hasNext()) {

        ifsaved = false;
        try {
          cyberplatstmtfile = (CyberplatsetmtfileTblVo) cyberlistitr.next();
          System.out.println("filename========="
              + cyberplatstmtfile.getIvrBnFLIENAME());
          cpSettleId = Integer.toString(cyberplatstmtfile
              .getIvrBnCBPLT_FILE_ID());
          cyberplatstmtfile.setIvrBnCBPLT_FILE_ID(Integer.parseInt(cpSettleId));
          if (cpOldstlId == null) {
            cpOldstlId = cpSettleId;
          } else if (cpOldstlId != null
              && !cpOldstlId.equalsIgnoreCase(cpSettleId)) {
            System.out.println("cpOldstlId===" + cpOldstlId
                + "===cpSettleId===" + cpSettleId);
            if (tx != null) {
              tx.commit();
              tx = null;
            }

            tx = session.beginTransaction();
            String locreconQuery = "Update CyberplatsetmtfileTblVo Set ivrBnExctSTATUS = '1' where ivrBnCBPLT_FILE_ID = '"
                + cpOldstlId + "'";
            boolean locLbrUpdSts = reconDaoService
                .updateReconFileStatus(locreconQuery);
            System.out.println("UpdStatus===" + locLbrUpdSts);
            if (locLbrUpdSts) {
              cpOldstlId = cpSettleId;
            }

          }
        String lvrXlsexterlfldr = ivrRbundle.getString("external.path") + ivrRbundle.getString("external.cyberplateuplfldr");
        String xlsname = lvrXlsexterlfldr + cyberplatstmtfile.getIvrBnFLIENAME();
        //String xlsname ="C:\\socialindiaexternal\\cyberplat\\"+cyberplatstmtfile.getIvrBnFLIENAME();
         // String xlsname = "C:\\socialindiaexternal\\cyberplat\\Sample Report format.csv";
          int startrow = 1;
          int columnlength = 10;
          String[] ext = cyberplatstmtfile.getIvrBnFLIENAME().split("\\.");
          System.out.println(ext[0] + "-------" + ext[1]);
          String extension = ext[1];
          // String extension = null;
          readfrmxls = common.readExcel(xlsname, 0, startrow, columnlength,
              extension);
          System.out.println("readfrmxls=========" + readfrmxls);
          if (readfrmxls != null && readfrmxls.startsWith("{\"")) {
            session = HibernateUtil.getSessionFactory().openSession();
            Date curDates = new Date();
            tx = session.beginTransaction();
            jsnobject = new JSONObject(readfrmxls);
            jsonArray = jsnobject.getJSONArray("row");
            String xlOutletCode = null;
            String xlDateRecPayment = null;
            String xlDateCompPayment = null;
            String cpProviderName = null;
            String cpNumber = null;
            String xlAmount = null;
            String cpDealerTransId = null;
            String cpOptorTransId = null;
            String cpStatus = null;
            String cpCyberplatTransId = null;
            cpEntryBy.setUserId(1);
            System.out.println("Array length=========" + jsonArray.length());
            for (int i = 1; i < jsonArray.length(); i++) {

              JSONObject explrObject = jsonArray.getJSONObject(i);
              JSONArray jsonColumnArray = explrObject.getJSONArray("column");
              System.out.println("json Array------------"
                  + jsonColumnArray.toString());
              ifsaved = false;

              Date curDate = new Date();
              int cpOutletCode = 0;
              int cpReconStatus = 0;
              Double cpAmount = 0.0;
              System.out.println("json Array------------"
                  + jsonColumnArray.toString());
              xlDateRecPayment = jsonColumnArray.getString(0);
              xlDateCompPayment = jsonColumnArray.getString(1);
              xlOutletCode = jsonColumnArray.getString(2);
              cpProviderName = jsonColumnArray.getString(3);
              cpNumber = jsonColumnArray.getString(4);
              xlAmount = jsonColumnArray.getString(5);
              cpDealerTransId = jsonColumnArray.getString(6);
              cpCyberplatTransId = jsonColumnArray.getString(7);
              cpStatus = jsonColumnArray.getString(8);
              cpOptorTransId = jsonColumnArray.getString(9);

              System.out.println("json Array------------"
                  + jsonColumnArray.toString());
              if (xlOutletCode != null && xlOutletCode.length() > 0) {
                cpOutletCode = Integer.parseInt(xlOutletCode);
              }
              if (xlAmount != null && xlAmount.length() > 0) {
                cpAmount = Double.parseDouble(xlAmount);
              }
              System.out.println("json Array------------"
                  + jsonColumnArray.toString());
              Date cpDateRecPayment = common.stringTOModifyDate(
                  xlDateRecPayment, "dd.MM.yyyy hh:mm");
              Date cpDateCompPayment = common.stringTOModifyDate(
                  xlDateCompPayment, "dd.MM.yyyy hh:mm");
              System.out.println("json Array------------"
                  + jsonColumnArray.toString());
              CyberplatrecondataTblVo cyberplatrecondata = new CyberplatrecondataTblVo();
              cyberplatrecondata.setCpOutletCode(cpOutletCode);
              cyberplatrecondata.setCpProviderName(cpProviderName);
              cyberplatrecondata.setCpNumber(cpNumber);
              cyberplatrecondata.setCpAmount(cpAmount);
              cyberplatrecondata.setCpDealerTransId(cpDealerTransId);
              cyberplatrecondata.setCpOptorTransId(cpOptorTransId);
              cyberplatrecondata.setCpCyberplatTransId(cpCyberplatTransId);
              cyberplatrecondata.setCpDateRecPayment(cpDateRecPayment);
              cyberplatrecondata.setCpDateCompPayment(cpDateCompPayment);
              cyberplatrecondata.setCpStatus(cpStatus);
              cyberplatrecondata.setCpReconStatus(cpReconStatus);
              cyberplatrecondata.setCpEntryBy(cpEntryBy);
              cyberplatrecondata.setCpEntryDatetime(curDate);
              System.out.println("Array length=========" + jsonArray.length());
              boolean insval = reconDaoService.toInsertCyberplatReconData(
                  cyberplatrecondata, session);
              System.out.println("insval==========" + insval);
              if (insval) {
                ifsaved = true;
              } else {
                ifsaved = false;
                tx.rollback();
                tx = null;
                String errQuery = "Update CyberplatsetmtfileTblVo Set ivrBnExctSTATUS = '2' where ivrBnPAYGATE_SETTLE_ID = '"
                    + cpOldstlId + "'";
                boolean locLbrUpdSts = reconDaoService
                    .updateReconFileStatus(errQuery);
                break;
              }

            }

          } else {
            System.out.println("Exception :error in file");
            log.logMessage("Exception :error in file", "error",
                PaygateReconcile.class);
            String errQuery = "Update CyberplatsetmtfileTblVo Set ivrBnExctSTATUS = '2' where ivrBnPAYGATE_SETTLE_ID = '"
                + cpOldstlId + "'";
            boolean locLbrUpdSts = reconDaoService
                .updateReconFileStatus(errQuery);
            tx.rollback();
            tx = null;
            ifsaved = false;
            break;
          }
        } catch (Exception ex) {
          System.out.println("Exception Found in CyberplatReconcile : " + ex);
          log.logMessage("Exception : " + ex, "error", PaygateReconcile.class);
          String errQuery = "Update CyberplatsetmtfileTblVo Set ivrBnExctSTATUS = '2' where ivrBnPAYGATE_SETTLE_ID = '"
              + cpOldstlId + "'";
          boolean locLbrUpdSts = reconDaoService
              .updateReconFileStatus(errQuery);
          tx.rollback();
          tx = null;
          ifsaved = false;
          break;

        }

      }

      if (ifsaved) {
    	  tx.commit();tx = null;
        String locreconQuery = "Update CyberplatsetmtfileTblVo Set ivrBnExctSTATUS = '1' where ivrBnCBPLT_FILE_ID = '"
            + cpOldstlId + "'";
        boolean locLbrUpdSts = reconDaoService
            .updateReconFileStatus(locreconQuery);
        System.out.println("UpdStatus==last Entry=" + locLbrUpdSts);
      }

    } catch (Exception ex) {

      System.out.println("Exception Found in CyberplatReconcile : " + ex);
      log.logMessage("Exception : " + ex, "error", PaygateReconcile.class);
      return "done";

    } finally {
      log = null;
      if (session != null) {
        session.close();
      }
    }

    return "success";
  }

  public static void main(String[] args) {
    try {
      CyberplatReconcile paydd = new CyberplatReconcile();
      paydd.toStartCyberplatReconcile();
      /*
       * DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm"); Date strdate
       * = df.parse("10.01.2017 16:33"); System.out.println(strdate);
       */
      /*
       * String auth = "AuthTest.xlsx"; String ext[] = auth.split("\\.");
       * System.out.println(ext[0]);
       */
    } catch (Exception ex) {
      System.out.println(ex);
    }
  }

}
