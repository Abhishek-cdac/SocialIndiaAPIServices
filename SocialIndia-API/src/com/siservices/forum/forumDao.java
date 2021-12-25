package com.siservices.forum;

import java.util.List;

import com.siservices.forumVo.MvpFourmDiscussTbl;
import com.siservices.forumVo.MvpFourmTopicsTbl;

public interface forumDao  {
	
	public String forumCreationForm(MvpFourmTopicsTbl forumTopicMst);
	
	public boolean forumDelete(int topicsId);
	
	 public List<MvpFourmDiscussTbl> forumViewAction(int topicsId);
	 
	 public MvpFourmTopicsTbl getforumtopicsdetails(int topicsId);

	 public boolean forumCreationComments(MvpFourmDiscussTbl forumDiscussMst);
	 
	 public int  gettopicsCount(int topicsId);
	 
	 public boolean getforumtopicsUpdate(MvpFourmTopicsTbl forumTopicsMst);
}

