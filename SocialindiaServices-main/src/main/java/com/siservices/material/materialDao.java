package com.siservices.material;

import com.siservices.materialVo.MvpMaterialTbl;

public interface materialDao {
	
	public String materialCreationForm(MvpMaterialTbl materialMst);
	
	public MvpMaterialTbl getMaterialView(int materialid);
	
	public boolean materialDelete(int materialId);
	
	public boolean getmaterialUpdate(MvpMaterialTbl materialMst);
	
	

}
