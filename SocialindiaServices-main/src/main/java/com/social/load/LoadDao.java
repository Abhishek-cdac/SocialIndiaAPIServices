package com.social.load;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

public class LoadDao implements Load {

  private static Logger log = Logger.getLogger(LoadDao.class);

  @Override
  public void getAllContextValues() {
    // TODO Auto-generated method stub
    getAllMenuMasterList();
  }

  @Override
  public void getAllMenuMasterList() {
    // TODO Auto-generated method stub
    
  }

}
