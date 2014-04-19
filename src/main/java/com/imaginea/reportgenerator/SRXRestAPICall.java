package com.imaginea.reportgenerator;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.imaginea.bandwidth.usage.model.Request;
import com.imaginea.bandwidth.usage.model.RequestingDevice;
import com.imaginea.bandwidth.usage.model.SRXLogSummary;

public class SRXRestAPICall {
	private static SRXLogSummary srxLogSumary = new SRXLogSummary();
	
	private static final String HIT_IP_ENTITY_TYPE = "hitIP";

	private static final String WIRELESS_ENTITY_TYPE = "wirelessDevice";
	
	private static final String LAN_ENTITY_TYPE = "LanDevice";
	
	private static final String USER_ENTITY_TYPE = "UserMailId";
	
	private static final String HOST_ENTITY_TYPE = "RequestToHost";
	
	private String getTopRequest_WirelessDevice() {
		String srsLogDtl = null;
		try {
			StringEntity stringEntity = getStringEntity(WIRELESS_ENTITY_TYPE, "");
			srsLogDtl = JSONParser.parseTerms(Utils.callRESTService(stringEntity));
			for(String logInfo: srsLogDtl.split(" ")){
				if(Utils.isNotEmpty(logInfo)){
					String[] logDtl = logInfo.split(":");
					srxLogSumary.getWirelessDeviceLog().add(
							new RequestingDevice(logDtl[0], 
									getIPInfo(USER_ENTITY_TYPE, logDtl[0]), Integer.parseInt(logDtl[1])));
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return srsLogDtl;
	}

	private String getTopRequest_LANDevice() {
		String srsLogDtl = null;
		try {
			StringEntity stringEntity = getStringEntity(LAN_ENTITY_TYPE, "");
			srsLogDtl = JSONParser.parseTerms(Utils
					.callRESTService(stringEntity));
			for (String logInfo : srsLogDtl.split(" ")) {
				if (Utils.isNotEmpty(logInfo)) {
					String[] logDtl = logInfo.split(":");
					srxLogSumary.getLanDeviceLog().add(
							new RequestingDevice(logDtl[0], getIPInfo(
									USER_ENTITY_TYPE, logDtl[0]), Integer
									.parseInt(logDtl[1])));
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return srsLogDtl;
	}
	
	private String getMostHitIP() {
		String srsLogDtl = null;
		try {
			StringEntity stringEntity = getStringEntity(HIT_IP_ENTITY_TYPE, "");
			srsLogDtl = JSONParser.parseTerms(Utils
					.callRESTService(stringEntity));
			for (String logInfo : srsLogDtl.split(" ")) {
				if(Utils.isNotEmpty(logInfo)){
					String[] logDtl = logInfo.split(":");
					srxLogSumary.getMostHitIP_Log().add(
							new Request(logDtl[0], Integer.parseInt(logDtl[1]),
									getIPInfo(HOST_ENTITY_TYPE, logDtl[0])));
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return srsLogDtl;
	}
	
	private String getIPInfo(String entityType, String ipAddress) {
		StringEntity stringEntity = null;
		String info = null;
		try {
			stringEntity = getStringEntity(entityType, ipAddress);
			info = JSONParser.parseHits(Utils.callRESTService(stringEntity),
					entityType);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return info;
	}
	
	public SRXLogSummary getSRXLogSummary(){
		getTopRequest_WirelessDevice();
		getTopRequest_LANDevice();
		getMostHitIP();
		return srxLogSumary;
	}
	
	private static StringEntity getStringEntity(String entityType, String ipAddress) throws UnsupportedEncodingException {
		StringEntity stringEntity = null;
		if (USER_ENTITY_TYPE.equalsIgnoreCase(entityType)) {
			stringEntity = new StringEntity(
					"{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"query_string\":{\"query\":\"RequestIp:"
							+ ipAddress
							+ "\"}},{\"query_string\":{\"query\":\"_type:CorridorSysLog\"}}]}}}},\"size\":1}");
		} else if (HOST_ENTITY_TYPE.equalsIgnoreCase(entityType)) {
			stringEntity = new StringEntity(
					"{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"query_string\":{\"query\":\"RequestToIp:"
							+ ipAddress
							+ "\"}}]}},\"filter\":{\"bool\":{\"must\":[{\"fquery\":{\"query\":{\"query_string\":{\"query\":\"_type:SRXSysLog\"}},\"_cache\":true}},{\"range\":{\"@timestamp\":{\"from\":"
							+ Utils.getEpochTime()
							+ ",\"to\":\"now\"}}},{\"fquery\":{\"query\":{\"query_string\":{\"query\":\"SysLogType=\\\"SRXSysLogs\\\"\"}},\"_cache\":true}}],\"must_not\":[{\"fquery\":{\"query\":{\"query_string\":{\"query\":\"tags:parsing_failed\"}},\"_cache\":true}}]}}}},\"size\":1}");
		} else if (WIRELESS_ENTITY_TYPE.equalsIgnoreCase(entityType)) {
			stringEntity = new StringEntity(
					"{\"facets\":{\"terms\":{\"terms\":{\"field\":\"RequestIp\",\"size\":10,\"order\":\"count\",\"exclude\":[]},\"facet_filter\":{\"fquery\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"should\":[{\"query_string\":{\"query\":\"RequestIp:11.11.*\"}}]}},\"filter\":{\"bool\":{\"must\":[{\"fquery\":{\"query\":{\"query_string\":{\"query\":\"_type:SRXSysLog\"}},\"_cache\":true}},{\"range\":{\"@timestamp\":{\"from\":"
							+ Utils.getEpochTime()
							+ ",\"to\":\"now\"}}},{\"fquery\":{\"query\":{\"query_string\":{\"query\":\"SysLogType=\\\"SRXSysLogs\\\"\"}},\"_cache\":true}}],\"must_not\":[{\"fquery\":{\"query\":{\"query_string\":{\"query\":\"tags:parsing_failed\"}},\"_cache\":true}}]}}}}}}}},\"size\":0}");
		} else if (LAN_ENTITY_TYPE.equalsIgnoreCase(entityType)) {
			stringEntity = new StringEntity(
					"{\"facets\":{\"terms\":{\"terms\":{\"field\":\"RequestIp\",\"size\":10,\"order\":\"count\",\"exclude\":[]},\"facet_filter\":{\"fquery\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"should\":[{\"query_string\":{\"query\":\"RequestIp:172.16.*\"}}]}},\"filter\":{\"bool\":{\"must\":[{\"fquery\":{\"query\":{\"query_string\":{\"query\":\"_type:SRXSysLog\"}},\"_cache\":true}},{\"range\":{\"@timestamp\":{\"from\":"
							+ Utils.getEpochTime()
							+ ",\"to\":\"now\"}}},{\"fquery\":{\"query\":{\"query_string\":{\"query\":\"SysLogType=\\\"SRXSysLogs\\\"\"}},\"_cache\":true}}],\"must_not\":[{\"fquery\":{\"query\":{\"query_string\":{\"query\":\"tags:parsing_failed\"}},\"_cache\":true}}]}}}}}}}},\"size\":0}");
		} else if (HIT_IP_ENTITY_TYPE.equalsIgnoreCase(entityType)) {
			stringEntity = new StringEntity(
					"{\"facets\":{\"terms\":{\"terms\":{\"field\":\"RequestToIp\",\"size\":10,\"order\":\"count\",\"exclude\":[]},\"facet_filter\":{\"fquery\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"should\":[{\"query_string\":{\"query\":\"*\"}}]}},\"filter\":{\"bool\":{\"must\":[{\"fquery\":{\"query\":{\"query_string\":{\"query\":\"_type:SRXSysLog\"}},\"_cache\":true}},{\"range\":{\"@timestamp\":{\"from\":"
							+ Utils.getEpochTime()
							+ ",\"to\":\"now\"}}},{\"fquery\":{\"query\":{\"query_string\":{\"query\":\"SysLogType=\\\"SRXSysLogs\\\"\"}},\"_cache\":true}}],\"must_not\":[{\"fquery\":{\"query\":{\"query_string\":{\"query\":\"tags:parsing_failed\"}},\"_cache\":true}}]}}}}}}}},\"size\":0}");
		}
		return stringEntity;
	}
}
