package com.pack.reconcilevo;

import java.io.Serializable;
import java.util.Date;

import com.mobi.common.mobiCommon;
import com.mobi.easypayvo.MvpPaygateTbl;
import com.mobi.easypayvo.MvpTransactionTbl;
import com.mobi.easypayvo.MvpUtilityPayTbl;
import com.pack.utilitypkg.Commonutility;
import com.siservices.signup.persistense.UserMasterTblVo;

public class ReconicileresultTblVo implements Serializable{

	/**
	 * 
	 */
private static final long serialVersionUID = 1L;
	
private Integer ivrRECONID;
private MvpTransactionTbl ivrTRANSID;
private MvpPaygateTbl ivrPAYGATEID;
private MvpUtilityPayTbl ivrUTILITYPAYID;
private String ivrORDERNO;
private Float ivrTXNAMOUNT;
private Date ivrGMT_DATE;
private Date ivrTRANS_DATE;
private Integer ivrTRANMATCHSTATUS;
private Integer ivrTRANRECONSTATUS;
private String ivrSTATUS_COMMENT;
private String ivrCOMMENTS;
private Integer ivrSTATUS;
private Date entryDateTime;
private String ivrTRANMATCHSTATUSStr;
private String ivrTRANRECONSTATUSStr;
private String ivrAmtstr;
private UserMasterTblVo ivrUamobj;
private String ivrBthmatchreconsts;
private String ivrTypetrans;
private String ivrreconrespones;

public Integer getIvrRECONID() {
	return ivrRECONID;
}
public void setIvrRECONID(Integer ivrRECONID) {
	this.ivrRECONID = ivrRECONID;
}
public MvpTransactionTbl getIvrTRANSID() {
	return ivrTRANSID;
}
public void setIvrTRANSID(MvpTransactionTbl ivrTRANSID) {
	this.ivrTRANSID = ivrTRANSID;
	if(ivrTRANSID.getPaymentType()!=null){
		if(ivrTRANSID.getPaymentType()==1){
			ivrTypetrans="Utility payment";
		}else if(ivrTRANSID.getPaymentType()==2){
			ivrTypetrans="Maintenance payment";
		}else{
			ivrTypetrans="Not Found";
		}
	}
}
public MvpPaygateTbl getIvrPAYGATEID() {
	return ivrPAYGATEID;
}
public void setIvrPAYGATEID(MvpPaygateTbl ivrPAYGATEID) {
	this.ivrPAYGATEID = ivrPAYGATEID;
}
public MvpUtilityPayTbl getIvrUTILITYPAYID() {
	return ivrUTILITYPAYID;
}
public void setIvrUTILITYPAYID(MvpUtilityPayTbl ivrUTILITYPAYID) {
	this.ivrUTILITYPAYID = ivrUTILITYPAYID;
}
public String getIvrORDERNO() {
	return ivrORDERNO;
}
public void setIvrORDERNO(String ivrORDERNO) {
	this.ivrORDERNO = ivrORDERNO;
}
public Float getIvrTXNAMOUNT() {
	return ivrTXNAMOUNT;
}
public void setIvrTXNAMOUNT(Float ivrTXNAMOUNT) {
	this.ivrTXNAMOUNT = ivrTXNAMOUNT;
	ivrAmtstr = Commonutility.toAmontDecimalfrmt(ivrTXNAMOUNT);
}
public Date getIvrGMT_DATE() {
	return ivrGMT_DATE;
}
public void setIvrGMT_DATE(Date ivrGMT_DATE) {
	this.ivrGMT_DATE = ivrGMT_DATE;
}
public Date getIvrTRANS_DATE() {
	return ivrTRANS_DATE;
}
public void setIvrTRANS_DATE(Date ivrTRANS_DATE) {
	this.ivrTRANS_DATE = ivrTRANS_DATE;
}
public Integer getIvrTRANMATCHSTATUS() {
	return ivrTRANMATCHSTATUS;
}
public void setIvrTRANMATCHSTATUS(Integer ivrTRANMATCHSTATUS) {
	this.ivrTRANMATCHSTATUS = ivrTRANMATCHSTATUS;
	if(ivrTRANMATCHSTATUS==0){
		ivrTRANMATCHSTATUSStr ="Match";
		setIvrTRANMATCHSTATUSStr("Match");
	} else if(ivrTRANMATCHSTATUS==1){
		ivrTRANMATCHSTATUSStr ="Unmatch";
		setIvrTRANMATCHSTATUSStr("Unmatch");
	} else if(ivrTRANMATCHSTATUS==2){
		ivrTRANMATCHSTATUSStr ="Not Found";
		setIvrTRANMATCHSTATUSStr("Not Found");
	} else {
		ivrTRANMATCHSTATUSStr ="Not Found";
		setIvrTRANMATCHSTATUSStr("Not Found");
	}
	setIvrBthmatchreconsts(ivrTRANMATCHSTATUSStr + " - " + ivrTRANRECONSTATUSStr);
}
public Integer getIvrTRANRECONSTATUS() {
	return ivrTRANRECONSTATUS;
}
public void setIvrTRANRECONSTATUS(Integer ivrTRANRECONSTATUS) {
	this.ivrTRANRECONSTATUS = ivrTRANRECONSTATUS;
	if(ivrTRANRECONSTATUS==0){
		ivrTRANRECONSTATUSStr ="Success";
		setIvrTRANRECONSTATUSStr("Success");
	} else if(ivrTRANRECONSTATUS==1){
		ivrTRANRECONSTATUSStr ="Failed";
		setIvrTRANRECONSTATUSStr("Failed");
	} else {
		ivrTRANRECONSTATUSStr ="Failed";
		setIvrTRANRECONSTATUSStr("Failed");
	}
	setIvrBthmatchreconsts(ivrTRANMATCHSTATUSStr + " - " + ivrTRANRECONSTATUSStr);
}
public String getIvrSTATUS_COMMENT() {
	return ivrSTATUS_COMMENT;
}
public void setIvrSTATUS_COMMENT(String ivrSTATUS_COMMENT) {
	this.ivrSTATUS_COMMENT = ivrSTATUS_COMMENT;
	if(ivrSTATUS_COMMENT!=null && !ivrSTATUS_COMMENT.equalsIgnoreCase("null") && !ivrSTATUS_COMMENT.equalsIgnoreCase("")){
		ivrreconrespones="";
		if (Commonutility.checkempty(ivrSTATUS_COMMENT) && ivrSTATUS_COMMENT.contains(",")) {
			String statuscode[] = ivrSTATUS_COMMENT.split(",");
			for(int i=0;i<statuscode.length;i++){
				String stsval="";
				stsval = mobiCommon.getMsg(statuscode[i]);						
				ivrreconrespones += stsval + ", ";											
			}
		} else {
			ivrreconrespones = mobiCommon.getMsg(ivrSTATUS_COMMENT);		
		}if (Commonutility.checkempty(ivrreconrespones) && ivrreconrespones.endsWith(", ")) {
			ivrreconrespones = ivrreconrespones.substring(0, ivrreconrespones.length()-2);
		}
	}else{
		ivrreconrespones = "";		
	}
	
	System.out.println("Final step : --------- "+ivrreconrespones);
}

public String getIvrCOMMENTS() {
	return ivrCOMMENTS;
}
public void setIvrCOMMENTS(String ivrCOMMENTS) {
	this.ivrCOMMENTS = ivrCOMMENTS;
}
public Integer getIvrSTATUS() {
	return ivrSTATUS;
}
public void setIvrSTATUS(Integer ivrSTATUS) {
	this.ivrSTATUS = ivrSTATUS;
}
public Date getEntryDateTime() {
	return entryDateTime;
}
public void setEntryDateTime(Date entryDateTime) {
	this.entryDateTime = entryDateTime;
}
public String getIvrTRANMATCHSTATUSStr() {
	return ivrTRANMATCHSTATUSStr;
}
public void setIvrTRANMATCHSTATUSStr(String ivrTRANMATCHSTATUSStr) {
	this.ivrTRANMATCHSTATUSStr = ivrTRANMATCHSTATUSStr;
}
public String getIvrTRANRECONSTATUSStr() {
	return ivrTRANRECONSTATUSStr;
}
public void setIvrTRANRECONSTATUSStr(String ivrTRANRECONSTATUSStr) {
	this.ivrTRANRECONSTATUSStr = ivrTRANRECONSTATUSStr;
}
public UserMasterTblVo getIvrUamobj() {
	return ivrUamobj;
}
public void setIvrUamobj(UserMasterTblVo ivrUamobj) {
	this.ivrUamobj = ivrUamobj;
}
public String getIvrAmtstr() {
	return ivrAmtstr;
}
public void setIvrAmtstr(String ivrAmtstr) {
	this.ivrAmtstr = ivrAmtstr;
}
public String getIvrBthmatchreconsts() {
	return ivrBthmatchreconsts;
}
public void setIvrBthmatchreconsts(String ivrBthmatchreconsts) {
	this.ivrBthmatchreconsts = ivrBthmatchreconsts;
	
}
public String getIvrTypetrans() {
	return ivrTypetrans;
}
public void setIvrTypetrans(String ivrTypetrans) {
	this.ivrTypetrans = ivrTypetrans;
}
public String getIvrreconrespones() {
	return ivrreconrespones;
}
public void setIvrreconrespones(String ivrreconrespones) {
	this.ivrreconrespones = ivrreconrespones;
}




}
