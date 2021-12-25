package com.pack.reconcile;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pack.reconcilevo.PaygaterecondataTblVo;
import com.pack.reconcilevo.PaygatesetmtfileTblVo;
import com.pack.reconcilevo.persistance.ReconcileDaoService;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.sisocial.load.HibernateUtil;
import com.social.utils.Log;
import com.socialindiaservices.common.CommonUtils;

public class PaygateReconcile {

  CommonUtils common = new CommonUtils();
  private ReconcileDaoService reconDaoService = new ReconcileDaoService();

  public String toStartPaygateReconcile() {
    Log log = null;
    Session session = null;
    Transaction tx = null;
    String readfrmxls = null;
    JSONObject jsnobject = null;
    JSONArray jsonArray = null;
    Query lvrQry = null;
    String lvrSltqry = null;
    String pgSettleId = null;
    String pgOldstlId = null;
    PaygatesetmtfileTblVo paystmtfile = new PaygatesetmtfileTblVo();
    UserMasterTblVo pgEntryBy = new UserMasterTblVo();
    Iterator paylistitr = null;
    ArrayList<PaygatesetmtfileTblVo> paylist = new ArrayList<PaygatesetmtfileTblVo>();
    session = HibernateUtil.getSessionFactory().openSession();
    tx = session.beginTransaction();
    boolean ifsaved = false;
    try {
      log = new Log();
      
      paylist = (ArrayList<PaygatesetmtfileTblVo>) reconDaoService
          .togetPaygateReconcilFiles();
      System.out.println("paylist====" + paylist.size());
      paylistitr = paylist.iterator();
      while (paylistitr.hasNext()) {
         ifsaved = false;
        try {
          
          paystmtfile = (PaygatesetmtfileTblVo) paylistitr.next();
          pgSettleId = Integer
              .toString(paystmtfile.getIvrBnPAYGATE_SETTLE_ID());
          paystmtfile.setIvrBnPAYGATE_SETTLE_ID(Integer.parseInt(pgSettleId));
          
          if (pgOldstlId == null) {
            pgOldstlId = pgSettleId;
          } else if (pgOldstlId != null
              && !pgOldstlId.equalsIgnoreCase(pgSettleId)) {
            System.out.println("pgOldstlId===" + pgOldstlId
                + "===pgSettleId===" + pgSettleId);
            if(tx != null) {
              tx.commit();
              tx = null;
            }
           
            tx = session.beginTransaction();
            String locreconQuery = "Update PaygatesetmtfileTblVo Set ivrBnExSTATUS = '1' where ivrBnPAYGATE_SETTLE_ID = '"
                + pgOldstlId + "'";
            boolean locLbrUpdSts = reconDaoService
                .updateReconFileStatus(locreconQuery);
            System.out.println("UpdStatus===" + locLbrUpdSts);
            if (locLbrUpdSts) {
              pgOldstlId = pgSettleId;
            }

          }
          System.out.println("filename========="
              + paystmtfile.getIvrBnFLIENAME());
          String xlsname = "C:\\socialindiaexternal\\paygate\\"
              + paystmtfile.getIvrBnFLIENAME();
          int startrow = 1;
          int columnlength = 26;
          String[] ext = paystmtfile.getIvrBnFLIENAME().split("\\.");
          System.out.println(ext[0] + "-------" + ext[1]);
          String extension = ext[1];
          readfrmxls = common.readExcel(xlsname, 0, startrow, columnlength,
              extension);
          System.out.println("readfrmxls=== " + readfrmxls);
          if (readfrmxls != null && readfrmxls.startsWith("{\"")) {

            Date curDates = new Date();
            jsnobject = new JSONObject(readfrmxls);
            jsonArray = jsnobject.getJSONArray("row");
            String pgTransactionId = null;
            String pgMerchantName = null;
            String pgGatewayRef = null;
            String pgOrderNo = null;
            String pgCountry = null;
            String pgCurrency = null;
            String pgGateway = null;
            String pgPaymode = null;
            String pgEmail = null;
            String pgMobile = null;
            String pgStatus = null;
            String pgSubStatus = null;
            String pgMerchantId = null;
            String pgDateNum = null;
            String pgUdf1 = null;
            String pgUdf3 = null;
            String pgUdf5 = null;
            String xlAmount = null;
            String xlTransactionFee = null;
            String xlServiceTax = null;
            String xlSwatBharatCess = null;
            String xlKrishKalyanCess = null;
            String xlNetAmount = null;
            String xlScheme = null;
            String xlActStatus = null;
            String xltransstsdate = null;
            String xltransdate = null;
            pgEntryBy.setUserId(1);
            System.out.println("length=========" + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {

              JSONObject explrObject = jsonArray.getJSONObject(i);
              JSONArray jsonColumnArray = explrObject.getJSONArray("column");
              ifsaved = false;

              Date curDate = new Date();
              Double pgAmount = 0.0;
              Double pgTransactionFee = 0.0;
              Double pgServiceTax = 0.0;
              Double pgSwatBharatCess = 0.0;
              Double pgKrishKalyanCess = 0.0;
              Double pgNetAmount = 0.0;
              int pgScheme = 0;
              int pgActStatus = 0;

              pgTransactionId = jsonColumnArray.getString(0);
              pgMerchantName = jsonColumnArray.getString(1);
              pgGatewayRef = jsonColumnArray.getString(2);
              pgOrderNo = jsonColumnArray.getString(3);
              xlAmount = jsonColumnArray.getString(4);
              xlTransactionFee = jsonColumnArray.getString(5);
              xlServiceTax = jsonColumnArray.getString(6);
              xlSwatBharatCess = jsonColumnArray.getString(7);
              xlKrishKalyanCess = jsonColumnArray.getString(8);
              xlNetAmount = jsonColumnArray.getString(9);
              pgCountry = jsonColumnArray.getString(10);
              pgCurrency = jsonColumnArray.getString(11);
              pgGateway = jsonColumnArray.getString(12);
              pgPaymode = jsonColumnArray.getString(13);
              xlScheme = jsonColumnArray.getString(14);
              pgEmail = jsonColumnArray.getString(15);
              pgMobile = jsonColumnArray.getString(16);
              pgStatus = jsonColumnArray.getString(17);
              pgSubStatus = jsonColumnArray.getString(18);
              xltransdate = jsonColumnArray.getString(19);
              pgMerchantId = jsonColumnArray.getString(20);
              xltransstsdate = jsonColumnArray.getString(21);
              pgDateNum = jsonColumnArray.getString(22);
              pgUdf1 = jsonColumnArray.getString(23);
              pgUdf3 = jsonColumnArray.getString(24);
              pgUdf5 = jsonColumnArray.getString(25);

              if (xlAmount != null && xlAmount.length() > 0) {
                pgAmount = Double.parseDouble(xlAmount);
              }
              if (xlTransactionFee != null && xlTransactionFee.length() > 0) {
                pgTransactionFee = Double.parseDouble(xlTransactionFee);
              }
              if (xlServiceTax != null && xlServiceTax.length() > 0) {
                pgServiceTax = Double.parseDouble(xlServiceTax);
              }
              if (xlSwatBharatCess != null && xlSwatBharatCess.length() > 0) {
                pgSwatBharatCess = Double.parseDouble(xlSwatBharatCess);
              }
              if (xlKrishKalyanCess != null && xlKrishKalyanCess.length() > 0) {
                pgKrishKalyanCess = Double.parseDouble(xlKrishKalyanCess);
              }
              if (xlNetAmount != null && xlNetAmount.length() > 0) {
                pgNetAmount = Double.parseDouble(xlNetAmount);
              }
              if (xlScheme != null && xlScheme.length() > 0) {
                pgScheme = Integer.parseInt(xlScheme);
              }
              if (xlActStatus != null && xlActStatus.length() > 0) {
                pgActStatus = Integer.parseInt(xlActStatus);
              }

              Date pgTransactionDate = common.stringTOModifyDate(xltransdate,
                  "yyyy-MM-dd hh:mm:ss");
              Date pgTransStatusDate = common.stringTOModifyDate(
                  xltransstsdate, "yyyy-MM-dd hh:mm:ss");

              PaygaterecondataTblVo paygaterecondata = new PaygaterecondataTblVo();
              paygaterecondata.setPgTransactionId(pgTransactionId);
              paygaterecondata.setPgMerchantName(pgMerchantName);
              paygaterecondata.setPgGatewayRef(pgGatewayRef);
              paygaterecondata.setPgOrderNo(pgOrderNo);
              paygaterecondata.setPgAmount(pgAmount);
              paygaterecondata.setPgTransactionFee(pgTransactionFee);
              paygaterecondata.setPgServiceTax(pgServiceTax);
              paygaterecondata.setPgSwatBharatCess(pgSwatBharatCess);
              paygaterecondata.setPgKrishKalyanCess(pgKrishKalyanCess);
              paygaterecondata.setPgNetAmount(pgNetAmount);
              paygaterecondata.setPgCountry(pgCountry);
              paygaterecondata.setPgCurrency(pgCurrency);
              paygaterecondata.setPgGateway(pgGateway);
              paygaterecondata.setPgPaymode(pgPaymode);
              paygaterecondata.setPgScheme(pgScheme);
              paygaterecondata.setPgEmail(pgEmail);
              paygaterecondata.setPgMobile(pgMobile);
              paygaterecondata.setPgStatus(pgStatus);
              paygaterecondata.setPgSubStatus(pgSubStatus);
              paygaterecondata.setPgTransactionDate(pgTransactionDate);
              paygaterecondata.setPgMerchantId(pgMerchantId);
              paygaterecondata.setPgTransStatusDate(pgTransStatusDate);
              paygaterecondata.setPgDateNum(pgDateNum);
              paygaterecondata.setPgUdf1(pgUdf1);
              paygaterecondata.setPgUdf3(pgUdf3);
              paygaterecondata.setPgUdf5(pgUdf5);
              paygaterecondata.setPgEntryDatetime(curDate);
              paygaterecondata.setPgEntryBy(paystmtfile.getIvrBnENTRY_BY());
              paygaterecondata.setPgActStatus(0);
              paygaterecondata.setPgPaygateSettleId(paystmtfile);
              System.out.println("allvalues========"
                  + paygaterecondata.toString());
              boolean insval = reconDaoService.toInsertPaygateReconData(
                  paygaterecondata, session);
              if(insval) {
                ifsaved = true;
              } else {
                ifsaved = false;
                tx.rollback();
                tx = null;
                String locreconQuery = "Update PaygatesetmtfileTblVo Set ivrBnExSTATUS = '1' where ivrBnPAYGATE_SETTLE_ID = '"
                    + pgOldstlId + "'";
                boolean locLbrUpdSts = reconDaoService
                    .updateReconFileStatus(locreconQuery);
                
                break;
              }
              
              System.out.println("insval========" + insval);
            }
          } else {
            System.out.println("Exception :error in file");
            log.logMessage("Exception :error in file", "error",
                PaygateReconcile.class);
            String errQuery = "Update PaygatesetmtfileTblVo Set ivrBnExSTATUS = '2' where ivrBnPAYGATE_SETTLE_ID = '"
                + pgOldstlId + "'";
            boolean locLbrUpdSts = reconDaoService
                .updateReconFileStatus(errQuery);
            tx.rollback();
            tx = null;
            ifsaved = false;
            break;
          }
        } catch (Exception ex) {
          System.out.println("Exception Found in PaygateReconcile : " + ex);
          log.logMessage("Exception : " + ex, "error", PaygateReconcile.class);
          String errQuery = "Update PaygatesetmtfileTblVo Set ivrBnExSTATUS = '2' where ivrBnPAYGATE_SETTLE_ID = '"
              + pgOldstlId + "'";
          boolean locLbrUpdSts = reconDaoService
              .updateReconFileStatus(errQuery);
          tx.rollback();
          tx = null;
          ifsaved = false;
          break;
        }

      }

      if (ifsaved) {
        tx.commit();
        tx = null;
        String locreconQuery = "Update PaygatesetmtfileTblVo Set ivrBnExSTATUS = '1' where ivrBnPAYGATE_SETTLE_ID = '"
            + pgOldstlId + "'";
        boolean locLbrUpdSts = reconDaoService
            .updateReconFileStatus(locreconQuery);
        System.out.println("UpdStatus==last Entry=" + locLbrUpdSts);
      }

    } catch (Exception ex) {

      System.out.println("Exception Found in PaygateReconcile : " + ex);
      log.logMessage("Exception : " + ex, "error", PaygateReconcile.class);
      return "FAILED";

    } finally {
      log = null;
      if (session != null) {
        session.close();
      }
    }
    return "SUCCESS";
  }

  public String toCallReconcileFiles(String societyId) {
    System.out.println("societyId==="+societyId);
    if(societyId != null && !societyId.equalsIgnoreCase("null") && !societyId.equalsIgnoreCase("")) {
      int locSocId = Integer.parseInt(societyId);
      String locLbrUpdSts = reconDaoService.toCallReconcileProc(locSocId);
      return "SUCCESS";
    } else {
      return "FAILED";
    }
    
  }

  public static void main(String[] args) {
    try {
      PaygateReconcile paydd = new PaygateReconcile();
      paydd.toStartPaygateReconcile();
      /*
       * DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm"); Date strdate
       * = df.parse("2017-01-16 21:01"); System.out.println(strdate);
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
