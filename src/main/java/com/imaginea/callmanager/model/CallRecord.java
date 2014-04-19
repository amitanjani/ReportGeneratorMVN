package com.imaginea.callmanager.model;

public class CallRecord {
	private String callerName;
	
	private int duration;
	
	private int extension;
	
	public CallRecord(String callerName, int duration, int extension){
		this.callerName = callerName;
		this.duration = duration;
		this.extension = extension;
	}

	public String getCallerName() {
		return callerName;
	}

	public void setCallerName(String callerName) {
		this.callerName = callerName;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getExtension() {
		return extension;
	}

	public void setExtension(int extension) {
		this.extension = extension;
	}
}
