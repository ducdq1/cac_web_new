package com.viettel.voffice.DAO.DocIn.impl;

public abstract class DocInHandleProcess {
	public abstract Long getStatusAssignToProcess();
	
	public abstract void sendProcess();
	
	public abstract void setNewStatusForDoc();
	
	public abstract void finishDoc();
	
	public abstract void sendNotify();
	
	public abstract void saveAttachFile();
	
	public void executeProcess() {
		sendProcess();
		setNewStatusForDoc();
		finishDoc();
		sendNotify();
		saveAttachFile();
	}
}
