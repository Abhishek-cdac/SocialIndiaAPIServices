package com.pack.timelinefeedvo.persistence;

import com.pack.timelinefeedvo.FeedsTblVO;

public interface FeedsDao {

	public int toInsertTimelineFeeds(FeedsTblVO pFeedsobj);

	public boolean toUpdateTimelineFeeds(String pFeedsupdqry);
	
	public boolean toDeactivateTimelineFeeds(String pFeedsdactveqry);

	public boolean toDeleteTimelineFeeds(String pFeedsdeleteqry);

	public int getInitTotal(String preNewscntqry);

	public int getTotalFilter(String preNewsfilterqry);
}
