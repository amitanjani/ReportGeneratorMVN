package com.imaginea.bandwidth.usage.model;

public class Request {

	private String sourceIpAddress;

	private int totalRequest;

	private String hostName;
	
	public Request(String sourceIpAddress, int totalRequest, String hostName){
		this.sourceIpAddress = sourceIpAddress;
		this.totalRequest = totalRequest;
		this.hostName = hostName;
	}

	public String getSourceIpAddress() {
		return sourceIpAddress;
	}

	public int getTotalRequest() {
		return totalRequest;
	}

	public String getHostName() {
		return hostName;
	}
	
	@Override
	public String toString() {
		return "sourceIpAddress:" + this.sourceIpAddress + " TotalRequest"
				+ this.totalRequest + " HostName" + this.hostName;
	}
}
