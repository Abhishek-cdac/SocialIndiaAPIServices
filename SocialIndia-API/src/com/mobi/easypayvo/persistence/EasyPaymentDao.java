package com.mobi.easypayvo.persistence;

import java.util.List;

import com.mobi.easypayvo.MvpPaygateTbl;
import com.mobi.easypayvo.MvpTransactionTbl;
import com.mobi.easypayvo.MvpUtilityPayLogTbl;
import com.mobi.easypayvo.MvpUtilityPayTbl;
import com.pack.billpaymentvo.CyberplatoptrsTblVo;

public interface EasyPaymentDao {

	public int transactionFirstInsert(MvpTransactionTbl transActObj);

	public int payGateInsert(MvpPaygateTbl payGateObj);

	public boolean payGateUpdate(MvpPaygateTbl payGateUpdateObj);

	public boolean transactionUpdate(MvpTransactionTbl transActUpdateObj);

	public int utilityPayInsert(MvpUtilityPayTbl utilityPayObj);

	public boolean utilityPayUpdate(MvpUtilityPayTbl utilityPayUpdaeObj);

	public boolean transactionSecondUpdate(
			MvpTransactionTbl transActSecondUpdate);

	public String getConfigDetails(String key);

	public MvpPaygateTbl getPaygateDetails(int paymGateid,int rid);

	public MvpUtilityPayTbl getUtilityPayDetails(String resOrderno);

	public int payLogInsert(MvpUtilityPayLogTbl utilpayLogObj);

	public boolean transactionThirdUpdate(MvpTransactionTbl transActthirdUpdate);
	
	public List getTransactionList(String query, String startlim,String totalrow, String timestamp);

	public MvpTransactionTbl transGetFinalResponse(
			MvpTransactionTbl transActthirdUpdate);

	public CyberplatoptrsTblVo getCyperPlateOperator(int billerId,int billerCode);

	public List getTransactionListForReport(String query, String timestamp);

	public MvpTransactionTbl transReturnResponse(MvpTransactionTbl transActgetData);

	public boolean transRemarksUpdate(MvpTransactionTbl transRemarks,String seqNo);

}
