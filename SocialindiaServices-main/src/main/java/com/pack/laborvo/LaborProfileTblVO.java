package com.pack.laborvo;

import java.io.Serializable;
import java.util.Date;






import com.pack.commonvo.CompanyMstTblVO;
import com.pack.commonvo.LaborWrkTypMasterTblVO;
import com.siservices.signup.persistense.UserMasterTblVo;
import com.siservices.uam.persistense.GroupMasterTblVo;

public class LaborProfileTblVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer ivrBnLBR_ID;
	private String ivrBnLBR_SERVICE_ID;
	private String ivrBnLBR_NAME;
	private String ivrBnLBR_EMAIL;	
	private String ivrBnLBR_PH_NO;
	private String ivrBnLBR_ISD_CODE;
	private Integer ivrBnID_CARD_TYP;
	private String ivrBnID_CARD_NO;
	private String ivrBnLBR_ADD_1;
	private String ivrBnLBR_ADD_2;
	
	private Integer ivrBnVERIFIED_STATUS;
	private String  ivrBnKEY_FOR_SEARCH;
	private String  ivrBnLBR_DESCP;
	
	private Integer ivrBnLBR_PSTL_ID;
	private Integer ivrBnLBR_CITY_ID;
	private Integer ivrBnLBR_STATE_ID;
	private Integer ivrBnLBR_COUNTRY_ID;
	private Integer ivrBnLBR_STS;
	private String  ivrBnLBR_KYC_NAME;
	private Double ivrBnLBR_RATING;
	//private Integer ivrBnENTRY_BY;
	private String ivrBnIMAGE_NAME_WEB;
	private String ivrBnIMAGE_NAME_MOBILE;
	private Date ivrBnENTRY_DATETIME;
	
	private LaborWrkTypMasterTblVO wrktypId;	
	private GroupMasterTblVo ivrGrpcode;
	private UserMasterTblVo ivrBnENTRY_BY;
	private Float ivrBnLBR_EXPERIENCE;
	private Float ivrBnLBR_COST;
	private Integer ivrBnLBR_COSTPER;
	private CompanyMstTblVO ivrCOMPANY;
	
	
	
	public Float getIvrBnLBR_EXPERIENCE() {
		return ivrBnLBR_EXPERIENCE;
	}
	public void setIvrBnLBR_EXPERIENCE(Float ivrBnLBR_EXPERIENCE) {
		this.ivrBnLBR_EXPERIENCE = ivrBnLBR_EXPERIENCE;
	}
	public Float getIvrBnLBR_COST() {
		return ivrBnLBR_COST;
	}
	public void setIvrBnLBR_COST(Float ivrBnLBR_COST) {
		this.ivrBnLBR_COST = ivrBnLBR_COST;
	}
	public Integer getIvrBnLBR_COSTPER() {
		return ivrBnLBR_COSTPER;
	}
	public void setIvrBnLBR_COSTPER(Integer ivrBnLBR_COSTPER) {
		this.ivrBnLBR_COSTPER = ivrBnLBR_COSTPER;
	}
	public String getIvrBnIMAGE_NAME_WEB() {
		return ivrBnIMAGE_NAME_WEB;
	}
	public void setIvrBnIMAGE_NAME_WEB(String ivrBnIMAGE_NAME_WEB) {
		this.ivrBnIMAGE_NAME_WEB = ivrBnIMAGE_NAME_WEB;
	}
	public String getIvrBnIMAGE_NAME_MOBILE() {
		return ivrBnIMAGE_NAME_MOBILE;
	}
	public void setIvrBnIMAGE_NAME_MOBILE(String ivrBnIMAGE_NAME_MOBILE) {
		this.ivrBnIMAGE_NAME_MOBILE = ivrBnIMAGE_NAME_MOBILE;
	}
	public Integer getIvrBnLBR_ID() {
		return ivrBnLBR_ID;
	}
	public void setIvrBnLBR_ID(Integer ivrBnLBR_ID) {
		this.ivrBnLBR_ID = ivrBnLBR_ID;
	}
	public String getIvrBnLBR_SERVICE_ID() {
		return ivrBnLBR_SERVICE_ID;
	}
	public void setIvrBnLBR_SERVICE_ID(String ivrBnLBR_SERVICE_ID) {
		this.ivrBnLBR_SERVICE_ID = ivrBnLBR_SERVICE_ID;
	}
	public String getIvrBnLBR_NAME() {
		return ivrBnLBR_NAME;
	}
	public void setIvrBnLBR_NAME(String ivrBnLBR_NAME) {
		this.ivrBnLBR_NAME = ivrBnLBR_NAME;
	}
	public String getIvrBnLBR_EMAIL() {
		return ivrBnLBR_EMAIL;
	}
	public void setIvrBnLBR_EMAIL(String ivrBnLBR_EMAIL) {
		this.ivrBnLBR_EMAIL = ivrBnLBR_EMAIL;
	}
	public String getIvrBnLBR_PH_NO() {
		return ivrBnLBR_PH_NO;
	}
	public void setIvrBnLBR_PH_NO(String ivrBnLBR_PH_NO) {
		this.ivrBnLBR_PH_NO = ivrBnLBR_PH_NO;
	}
	public Integer getIvrBnID_CARD_TYP() {
		return ivrBnID_CARD_TYP;
	}
	public void setIvrBnID_CARD_TYP(Integer ivrBnID_CARD_TYP) {
		this.ivrBnID_CARD_TYP = ivrBnID_CARD_TYP;
	}
	public String getIvrBnID_CARD_NO() {
		return ivrBnID_CARD_NO;
	}
	public void setIvrBnID_CARD_NO(String ivrBnID_CARD_NO) {
		this.ivrBnID_CARD_NO = ivrBnID_CARD_NO;
	}
	public String getIvrBnLBR_ADD_1() {
		return ivrBnLBR_ADD_1;
	}
	public void setIvrBnLBR_ADD_1(String ivrBnLBR_ADD_1) {
		this.ivrBnLBR_ADD_1 = ivrBnLBR_ADD_1;
	}
	public String getIvrBnLBR_ADD_2() {
		return ivrBnLBR_ADD_2;
	}
	public void setIvrBnLBR_ADD_2(String ivrBnLBR_ADD_2) {
		this.ivrBnLBR_ADD_2 = ivrBnLBR_ADD_2;
	}
	
	
	public Integer getIvrBnLBR_PSTL_ID() {
		return ivrBnLBR_PSTL_ID;
	}
	public void setIvrBnLBR_PSTL_ID(Integer ivrBnLBR_PSTL_ID) {
		this.ivrBnLBR_PSTL_ID = ivrBnLBR_PSTL_ID;
	}
	
	
	public Integer getIvrBnLBR_CITY_ID() {
		return ivrBnLBR_CITY_ID;
	}
	public void setIvrBnLBR_CITY_ID(Integer ivrBnLBR_CITY_ID) {
		this.ivrBnLBR_CITY_ID = ivrBnLBR_CITY_ID;
	}
	public Integer getIvrBnLBR_STATE_ID() {
		return ivrBnLBR_STATE_ID;
	}
	public void setIvrBnLBR_STATE_ID(Integer ivrBnLBR_STATE_ID) {
		this.ivrBnLBR_STATE_ID = ivrBnLBR_STATE_ID;
	}
	public Integer getIvrBnLBR_COUNTRY_ID() {
		return ivrBnLBR_COUNTRY_ID;
	}
	public void setIvrBnLBR_COUNTRY_ID(Integer ivrBnLBR_COUNTRY_ID) {
		this.ivrBnLBR_COUNTRY_ID = ivrBnLBR_COUNTRY_ID;
	}
	public Integer getIvrBnLBR_STS() {
		return ivrBnLBR_STS;
	}
	public void setIvrBnLBR_STS(Integer ivrBnLBR_STS) {
		this.ivrBnLBR_STS = ivrBnLBR_STS;
	}
	public String getIvrBnLBR_KYC_NAME() {
		return ivrBnLBR_KYC_NAME;
	}
	public void setIvrBnLBR_KYC_NAME(String ivrBnLBR_KYC_NAME) {
		this.ivrBnLBR_KYC_NAME = ivrBnLBR_KYC_NAME;
	}
	public Double getIvrBnLBR_RATING() {
		return ivrBnLBR_RATING;
	}
	public void setIvrBnLBR_RATING(Double ivrBnLBR_RATING) {
		this.ivrBnLBR_RATING = ivrBnLBR_RATING;
	}
	
	/*public Integer getIvrBnENTRY_BY() {
		return ivrBnENTRY_BY;
	}
	public void setIvrBnENTRY_BY(Integer ivrBnENTRY_BY) {
		this.ivrBnENTRY_BY = ivrBnENTRY_BY;
	}*/
	public Date getIvrBnENTRY_DATETIME() {
		return ivrBnENTRY_DATETIME;
	}
	public void setIvrBnENTRY_DATETIME(Date ivrBnENTRY_DATETIME) {
		this.ivrBnENTRY_DATETIME = ivrBnENTRY_DATETIME;
	}
	public String getIvrBnLBR_ISD_CODE() {
		return ivrBnLBR_ISD_CODE;
	}
	public void setIvrBnLBR_ISD_CODE(String ivrBnLBR_ISD_CODE) {
		this.ivrBnLBR_ISD_CODE = ivrBnLBR_ISD_CODE;
	}
	public Integer getIvrBnVERIFIED_STATUS() {
		return ivrBnVERIFIED_STATUS;
	}
	public void setIvrBnVERIFIED_STATUS(Integer ivrBnVERIFIED_STATUS) {
		this.ivrBnVERIFIED_STATUS = ivrBnVERIFIED_STATUS;
	}
	public String getIvrBnKEY_FOR_SEARCH() {
		return ivrBnKEY_FOR_SEARCH;
	}
	public void setIvrBnKEY_FOR_SEARCH(String ivrBnKEY_FOR_SEARCH) {
		this.ivrBnKEY_FOR_SEARCH = ivrBnKEY_FOR_SEARCH;
	}
	
	public UserMasterTblVo getIvrBnENTRY_BY() {
		return ivrBnENTRY_BY;
	}
	public void setIvrBnENTRY_BY(UserMasterTblVo ivrBnENTRY_BY) {
		this.ivrBnENTRY_BY = ivrBnENTRY_BY;
	}
	//	public Integer getIvrBnWORK_TYPE_ID() {
//		return ivrBnWORK_TYPE_ID;
//	}
//	public void setIvrBnWORK_TYPE_ID(Integer ivrBnWORK_TYPE_ID) {
//		this.ivrBnWORK_TYPE_ID = ivrBnWORK_TYPE_ID;
//	}
	public String getIvrBnLBR_DESCP() {
		return ivrBnLBR_DESCP;
	}
	public void setIvrBnLBR_DESCP(String ivrBnLBR_DESCP) {
		this.ivrBnLBR_DESCP = ivrBnLBR_DESCP;
	}
	public LaborWrkTypMasterTblVO getWrktypId() {
		return wrktypId;
	}
	public void setWrktypId(LaborWrkTypMasterTblVO wrktypId) {
		this.wrktypId = wrktypId;
	}
	public GroupMasterTblVo getIvrGrpcode() {
		return ivrGrpcode;
	}
	public void setIvrGrpcode(GroupMasterTblVo ivrGrpcode) {
		this.ivrGrpcode = ivrGrpcode;
	}
	public CompanyMstTblVO getIvrCOMPANY() {
		return ivrCOMPANY;
	}
	public void setIvrCOMPANY(CompanyMstTblVO ivrCOMPANY) {
		this.ivrCOMPANY = ivrCOMPANY;
	}

	
	
}
