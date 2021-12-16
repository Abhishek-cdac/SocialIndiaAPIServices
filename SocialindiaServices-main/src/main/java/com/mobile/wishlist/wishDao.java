package com.mobile.wishlist;

import java.util.List;

import com.siservices.uam.persistense.MvpFamilymbrTbl;

public interface wishDao {
	
	public List<MvpWishlistTbl> getWishList(String userId,String timestamp, String startlim,String totalrow);
	
	public boolean deleteWishListData(String userId,String wishlistid);
	
	public List<MvpWishlistTbl> getWishListAdded(String userId);
	
	public boolean insertWishList(MvpWishlistTbl wishListMst);
	
	public boolean checkWishExist(String userId,String wishlistid);
	
	public boolean checkExistWish(String userId,String wishlistid);

}
