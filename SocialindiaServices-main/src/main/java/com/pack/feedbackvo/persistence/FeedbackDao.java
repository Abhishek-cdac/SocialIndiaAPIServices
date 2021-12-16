package com.pack.feedbackvo.persistence;

import com.pack.feedbackvo.FeedbackTblVO;

public interface FeedbackDao {

	public int toInsertFeedback(FeedbackTblVO pFeedbckvoobj);
	public boolean toUpdateFeedback(String pFeedupdqry);
	public boolean toDeactiveFeedback(String pFeedbckvoobj);
	
}
