package com.socialindiaservices.issuemgmt.persistence;

import java.util.Date;

public class IssueTblVO implements java.io.Serializable {

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private Integer issueId;
		private String issueList;
		private Integer status;
		private Date entryDatetime;
		private Date modifyDatetime;

		public Integer getIssueId() {
			return this.issueId;
		}

		public void setIssueId(Integer issueId) {
			this.issueId = issueId;
		}

		public String getIssueList() {
			return this.issueList;
		}

		public void setIssueList(String issueList) {
			this.issueList = issueList;
		}

		public Integer getStatus() {
			return this.status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public Date getEntryDatetime() {
			return this.entryDatetime;
		}

		public void setEntryDatetime(Date entryDatetime) {
			this.entryDatetime = entryDatetime;
		}

		public Date getModifyDatetime() {
			return this.modifyDatetime;
		}

		public void setModifyDatetime(Date modifyDatetime) {
			this.modifyDatetime = modifyDatetime;
		}

}
