package com.imaginea.bandwidth.usage.model;

public class RequestingDevice {
	private String sourceIpAddress;

	private int totalRequest;

	private String userID;
	
	public RequestingDevice(String sourceIpAddress, String userID, int totalRequest){
		this.sourceIpAddress = sourceIpAddress;
		this.totalRequest = totalRequest;
		this.userID = userID;
	}

	public String getSourceIpAddress() {
		return sourceIpAddress;
	}

	public int getTotalRequest() {
		return totalRequest;
	}

	public String getUserID() {
		return userID;
	}
	
	@Override
	public String toString() {
		return "SourceIpAddress:" + this.sourceIpAddress + " TotalRequest:"
				+ this.totalRequest + " UserID" + this.userID;
	}
}
