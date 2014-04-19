package com.imaginea.callmanager.model;

import java.util.Date;

public class CallFrequency {
	private Date callTime;

	private int callFrequency;

	private String originatingCallLocation;
	
	public CallFrequency(Date callTime, int callFrequency, String originatingCallLocation){
		this.callTime = callTime;
		this.callFrequency = callFrequency;
		this.originatingCallLocation = originatingCallLocation;
	}

	public Date getCallTime() {
		return callTime;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}

	public int getCallFrequency() {
		return callFrequency;
	}

	public void setCallFrequency(int callFrequency) {
		this.callFrequency = callFrequency;
	}

	public String getOriginatingCallLocation() {
		return originatingCallLocation;
	}

	public void setOriginatingCallLocation(String originatingCallLocation) {
		this.originatingCallLocation = originatingCallLocation;
	}
}
