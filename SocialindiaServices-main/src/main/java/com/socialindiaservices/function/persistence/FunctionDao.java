package com.socialindiaservices.function.persistence;

import com.socialindiaservices.vo.FunctionMasterTblVO;
import com.socialindiaservices.vo.FunctionTemplateTblVO;

public interface FunctionDao {

	int saveFunctiontxt(FunctionTemplateTblVO inrlocfun);

	int saveFunctionMas(FunctionMasterTblVO locfdbkvoObj);
	

}
