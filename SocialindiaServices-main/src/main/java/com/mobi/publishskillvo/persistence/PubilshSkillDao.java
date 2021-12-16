package com.mobi.publishskillvo.persistence;

import java.util.Date;
import java.util.List;

import com.mobi.publishskillvo.PublishSkillTblVO;

public interface PubilshSkillDao {
	
	public int insetPublishSkill(PublishSkillTblVO pubObj);
	
	public boolean editPublishSkill(PublishSkillTblVO pubObj);
	
	public List<Object[]> publishSkilSrchList(String searchQuery,String timestamp,int startLimit,int endLimit);
	
	public boolean deletepublish(int pubSkilId);

	public PublishSkillTblVO getPublishSkilData(int pubskilId,int rid);

}
