package com.study.manager.domain;

public class Goal {

	private long bookId;
	
	private int noOfPagesRead;
	
	private boolean revisionCompleted;
	
	public boolean isRevisionCompleted() {
		return revisionCompleted;
	}

	public void setRevisionCompleted(boolean revisionCompleted) {
		this.revisionCompleted = revisionCompleted;
	}

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

	public int getNoOfPagesRead() {
		return noOfPagesRead;
	}

	public void setNoOfPagesRead(int noOfPagesRead) {
		this.noOfPagesRead = noOfPagesRead;
	}
	
	
}
