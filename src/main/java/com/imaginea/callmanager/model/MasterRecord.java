package com.imaginea.callmanager.model;

import java.util.ArrayList;
import java.util.List;

import com.imaginea.bandwidth.usage.model.Request;
import com.imaginea.bandwidth.usage.model.RequestingDevice;

public class MasterRecord {
	/*
	 * CallManager attributes
	 */
	private List<CallRecord> hyderabadCallRecord = new ArrayList<CallRecord>();
	private List<CallRecord> chennaiCallRecord = new ArrayList<CallRecord>();
	private List<CallRecord> bangaloreCallRecord = new ArrayList<CallRecord>();
	private List<ServiceProvider> serviceProvider = new ArrayList<ServiceProvider>();
	private List<CallFrequency> callFrequency = new ArrayList<CallFrequency>();
	
	/*
	 * Bandwidth usage attributes
	 */

	private List<RequestingDevice> lanDeviceRequests = new ArrayList<RequestingDevice>();
	private List<RequestingDevice> wirelessDeviceRequest = new ArrayList<RequestingDevice>();
	private List<Request> mostIPHit = new ArrayList<Request>();
	
	public List<CallRecord> getHyderabadCallRecord() {
		return hyderabadCallRecord;
	}

	public void setHyderabadCallRecord(List<CallRecord> hyderabadCallRecord) {
		this.hyderabadCallRecord = hyderabadCallRecord;
	}

	public List<CallRecord> getChennaiCallRecord() {
		return chennaiCallRecord;
	}

	public void setChennaiCallRecord(List<CallRecord> chennaiCallRecord) {
		this.chennaiCallRecord = chennaiCallRecord;
	}

	public List<CallRecord> getBangaloreCallRecord() {
		return bangaloreCallRecord;
	}

	public void setBangaloreCallRecord(List<CallRecord> bangaloreCallRecord) {
		this.bangaloreCallRecord = bangaloreCallRecord;
	}

	public List<ServiceProvider> getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(List<ServiceProvider> serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public List<CallFrequency> getCallFrequency() {
		return callFrequency;
	}

	public void setCallFrequency(List<CallFrequency> callFrequency) {
		this.callFrequency = callFrequency;
	}

	public List<RequestingDevice> getLanDeviceRequests() {
		return lanDeviceRequests;
	}

	public void setLanDeviceRequests(List<RequestingDevice> lanDeviceRequests) {
		this.lanDeviceRequests = lanDeviceRequests;
	}

	public List<RequestingDevice> getWirelessDeviceRequest() {
		return wirelessDeviceRequest;
	}

	public void setWirelessDeviceRequest(
			List<RequestingDevice> wirelessDeviceRequest) {
		this.wirelessDeviceRequest = wirelessDeviceRequest;
	}

	public List<Request> getMostIPHit() {
		return mostIPHit;
	}

	public void setMostIPHit(List<Request> mostIPHit) {
		this.mostIPHit = mostIPHit;
	}
}
