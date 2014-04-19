package com.imaginea.bandwidth.usage.model;

import java.util.ArrayList;
import java.util.List;

import com.imaginea.bandwidth.usage.model.Request;
import com.imaginea.bandwidth.usage.model.RequestingDevice;

public class SRXLogSummary {

	private List<RequestingDevice> wirelessDeviceLog = new ArrayList<RequestingDevice>();

	private List<RequestingDevice> lanDeviceLog = new ArrayList<RequestingDevice>();

	private List<Request> mostHitIP_Log = new ArrayList<Request>();

	public List<RequestingDevice> getWirelessDeviceLog() {
		return wirelessDeviceLog;
	}

	public List<RequestingDevice> getLanDeviceLog() {
		return lanDeviceLog;
	}

	public List<Request> getMostHitIP_Log() {
		return mostHitIP_Log;
	}

}
