package com.mobi.easypayvo.persistence;

import java.util.List;
import org.json.JSONArray;

import com.mobi.easypayvo.PassportTblVo;

public interface PassportDao {

	public List<PassportTblVo> passportListDetail(PassportTblVo passportObj);

	public JSONArray passportDetailsPack(List<PassportTblVo> listObj);

	public int insetPassportData(PassportTblVo passportObj);

	public boolean editPassportData(PassportTblVo passportObj);

}
