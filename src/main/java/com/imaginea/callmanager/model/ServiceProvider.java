package com.imaginea.callmanager.model;

public class ServiceProvider {
	private String name;

	private int callDuration;
	
	public ServiceProvider(String name, int callDuration){
		this.name = name;
		this.callDuration = callDuration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(int callDuration) {
		this.callDuration = callDuration;
	}
}
