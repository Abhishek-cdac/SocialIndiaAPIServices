package com.pack.eventvo;

import java.io.Serializable;
import java.util.Date;

import com.siservices.signup.persistense.UserMasterTblVo;
import com.socialindiaservices.vo.FacilityBookingTblVO;
import com.socialindiaservices.vo.FacilityMstTblVO;
import com.socialindiaservices.vo.FunctionMasterTblVO;
import com.socialindiaservices.vo.FunctionTemplateTblVO;

public class EventTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer ivrBnEVENT_ID;
	private String ivrBnEVENT_TITLE;
	private String ivrBnSHORT_DESC;
	private String ivrBnEVENT_DESC;
	private UserMasterTblVo ivrBnEVENT_CRT_USR_ID;
	private Integer ivrBnEVENT_CRT_GROUP_ID;
	private String ivrBnEVENT_FILE_NAME;
	private String ivrBnVIDEO_PATH;
	private String ivrBnSTARTDATE;
	private String ivrBnENDDATE;
	private String ivrBnSTARTTIME;
	private String ivrBnENDTIME;
	private String ivrBnEVENT_LOCATION;
	private Integer ivrBnEVENTSTATUS;
	private Integer ivrBnENTRY_BY;
	private Date ivrBnENTRY_DATETIME;
	private Date modifyDate;
	private String ivrBnLAT_LONG;
	private Integer ivrBnISRSVP;
	private FunctionMasterTblVO ivrBnFUNCTION_ID;
	private FunctionTemplateTblVO functionTemplateId;
	private FacilityMstTblVO faciltyTemplateId;
	private FacilityBookingTblVO faciltyBookingId;
	private Integer ivrBnEVENTT_TYPE;

	public Integer getIvrBnEVENT_ID() {
		return ivrBnEVENT_ID;
	}

	public void setIvrBnEVENT_ID(Integer ivrBnEVENT_ID) {
		this.ivrBnEVENT_ID = ivrBnEVENT_ID;
	}

	public String getIvrBnEVENT_TITLE() {
		return ivrBnEVENT_TITLE;
	}

	public void setIvrBnEVENT_TITLE(String ivrBnEVENT_TITLE) {
		this.ivrBnEVENT_TITLE = ivrBnEVENT_TITLE;
	}

	public String getIvrBnEVENT_DESC() {
		return ivrBnEVENT_DESC;
	}

	public void setIvrBnEVENT_DESC(String ivrBnEVENT_DESC) {
		this.ivrBnEVENT_DESC = ivrBnEVENT_DESC;
	}

	public Integer getIvrBnEVENT_CRT_GROUP_ID() {
		return ivrBnEVENT_CRT_GROUP_ID;
	}

	public UserMasterTblVo getIvrBnEVENT_CRT_USR_ID() {
		return ivrBnEVENT_CRT_USR_ID;
	}

	public void setIvrBnEVENT_CRT_USR_ID(UserMasterTblVo ivrBnEVENT_CRT_USR_ID) {
		this.ivrBnEVENT_CRT_USR_ID = ivrBnEVENT_CRT_USR_ID;
	}

	public void setIvrBnEVENT_CRT_GROUP_ID(Integer ivrBnEVENT_CRT_GROUP_ID) {
		this.ivrBnEVENT_CRT_GROUP_ID = ivrBnEVENT_CRT_GROUP_ID;
	}

	public String getIvrBnEVENT_FILE_NAME() {
		return ivrBnEVENT_FILE_NAME;
	}

	public void setIvrBnEVENT_FILE_NAME(String ivrBnEVENT_FILE_NAME) {
		this.ivrBnEVENT_FILE_NAME = ivrBnEVENT_FILE_NAME;
	}

	public String getIvrBnVIDEO_PATH() {
		return ivrBnVIDEO_PATH;
	}

	public void setIvrBnVIDEO_PATH(String ivrBnVIDEO_PATH) {
		this.ivrBnVIDEO_PATH = ivrBnVIDEO_PATH;
	}

	public Integer getIvrBnEVENTSTATUS() {
		return ivrBnEVENTSTATUS;
	}

	public void setIvrBnEVENTSTATUS(Integer ivrBnEVENTSTATUS) {
		this.ivrBnEVENTSTATUS = ivrBnEVENTSTATUS;
	}

	public Integer getIvrBnENTRY_BY() {
		return ivrBnENTRY_BY;
	}

	public void setIvrBnENTRY_BY(Integer ivrBnENTRY_BY) {
		this.ivrBnENTRY_BY = ivrBnENTRY_BY;
	}

	public Date getIvrBnENTRY_DATETIME() {
		return ivrBnENTRY_DATETIME;
	}

	public void setIvrBnENTRY_DATETIME(Date ivrBnENTRY_DATETIME) {
		this.ivrBnENTRY_DATETIME = ivrBnENTRY_DATETIME;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getIvrBnSHORT_DESC() {
		return ivrBnSHORT_DESC;
	}

	public void setIvrBnSHORT_DESC(String ivrBnSHORT_DESC) {
		this.ivrBnSHORT_DESC = ivrBnSHORT_DESC;
	}

	public String getIvrBnSTARTDATE() {
		return ivrBnSTARTDATE;
	}

	public void setIvrBnSTARTDATE(String ivrBnSTARTDATE) {
		this.ivrBnSTARTDATE = ivrBnSTARTDATE;
	}

	public String getIvrBnENDDATE() {
		return ivrBnENDDATE;
	}

	public void setIvrBnENDDATE(String ivrBnENDDATE) {
		this.ivrBnENDDATE = ivrBnENDDATE;
	}

	public String getIvrBnSTARTTIME() {
		return ivrBnSTARTTIME;
	}

	public void setIvrBnSTARTTIME(String ivrBnSTARTTIME) {
		this.ivrBnSTARTTIME = ivrBnSTARTTIME;
	}

	public String getIvrBnENDTIME() {
		return ivrBnENDTIME;
	}

	public void setIvrBnENDTIME(String ivrBnENDTIME) {
		this.ivrBnENDTIME = ivrBnENDTIME;
	}

	public String getIvrBnEVENT_LOCATION() {
		return ivrBnEVENT_LOCATION;
	}

	public void setIvrBnEVENT_LOCATION(String ivrBnEVENT_LOCATION) {
		this.ivrBnEVENT_LOCATION = ivrBnEVENT_LOCATION;
	}

	public String getIvrBnLAT_LONG() {
		return ivrBnLAT_LONG;
	}

	public void setIvrBnLAT_LONG(String ivrBnLAT_LONG) {
		this.ivrBnLAT_LONG = ivrBnLAT_LONG;
	}

	public Integer getIvrBnISRSVP() {
		return ivrBnISRSVP;
	}

	public void setIvrBnISRSVP(Integer ivrBnISRSVP) {
		this.ivrBnISRSVP = ivrBnISRSVP;
	}

	public FunctionMasterTblVO getIvrBnFUNCTION_ID() {
		return ivrBnFUNCTION_ID;
	}

	public void setIvrBnFUNCTION_ID(FunctionMasterTblVO ivrBnFUNCTION_ID) {
		this.ivrBnFUNCTION_ID = ivrBnFUNCTION_ID;
	}

	public FunctionTemplateTblVO getFunctionTemplateId() {
		return functionTemplateId;
	}

	public void setFunctionTemplateId(FunctionTemplateTblVO functionTemplateId) {
		this.functionTemplateId = functionTemplateId;
	}

	public FacilityMstTblVO getFaciltyTemplateId() {
		return faciltyTemplateId;
	}

	public void setFaciltyTemplateId(FacilityMstTblVO faciltyTemplateId) {
		this.faciltyTemplateId = faciltyTemplateId;
	}

	public FacilityBookingTblVO getFaciltyBookingId() {
		return faciltyBookingId;
	}

	public void setFaciltyBookingId(FacilityBookingTblVO faciltyBookingId) {
		this.faciltyBookingId = faciltyBookingId;
	}

	public Integer getIvrBnEVENTT_TYPE() {
		return ivrBnEVENTT_TYPE;
	}

	public void setIvrBnEVENTT_TYPE(Integer ivrBnEVENTT_TYPE) {
		this.ivrBnEVENTT_TYPE = ivrBnEVENTT_TYPE;
	}

}
