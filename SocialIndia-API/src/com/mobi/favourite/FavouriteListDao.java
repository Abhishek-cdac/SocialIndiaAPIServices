package com.mobi.favourite;

import com.mobi.commonvo.PrivacyInfoTblVO;
import com.socialindiaservices.vo.MvpFavouriteTbl;


public interface FavouriteListDao {

	public MvpFavouriteTbl insertMvpFavouriteTbl(MvpFavouriteTbl favouriteObj);
	public boolean updateMvpFavouriteTblByQuery(String query);
	public boolean updateFavouriteMenu(PrivacyInfoTblVO privacyObj);
}
