package com.social.load;

public class LoadDaoServices implements LoadServices {

  private Load dao;

  public LoadDaoServices() {
    dao = new LoadDao();
  }

  @Override
  public void getAllMenuMasterList() {
    // TODO Auto-generated method stub
    dao.getAllMenuMasterList();
  }

  @Override
  public void getAllContextValues() {
    // TODO Auto-generated method stub
    dao.getAllContextValues();
  }
}
