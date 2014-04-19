package com.imaginea.reportgenerator;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.imaginea.callmanager.model.CallFrequency;
import com.imaginea.callmanager.model.CallRecord;
import com.imaginea.callmanager.model.MasterRecord;
import com.imaginea.callmanager.model.ServiceProvider;

public class CallManagerRestAPICall {
	
	private MasterRecord masterRecord = new MasterRecord();

	private final String HYDERABAD = "Hyderabad";
	
	private final String CHENNAI = "Chennai";
	
	private final String BANGALORE = "Bangalore";
	
	private final String UNICODE_LOGIN_ID = "UnicodeLoginId";
	
	private final String CALLING_PARTY_UNICODE_LOGIN_ID = "callingPartyUnicodeLoginUserID";
	
	private final String SERVICE_USAGE = "ServiceUsage";
	
	private final String CALL_FREQUENCY = "CallFrequency";
	
	public MasterRecord generateReport() {
		List<StringEntity> stringEntityList = new ArrayList<StringEntity>();
		String[] locations = { HYDERABAD, CHENNAI, BANGALORE };
		Long fromTimestamp = Utils.getEpochTime();
		int count = 0;
		stringEntityList.add(getStringEntity(HYDERABAD));
		stringEntityList.add(getStringEntity(CHENNAI));
		stringEntityList.add(getStringEntity(BANGALORE));
		for (StringEntity input : stringEntityList) {
			masterRecord = parseTerms(Utils.callRESTService(input),
					locations[count++]);
		}
		getServiceUsageGraphDetails(fromTimestamp);
		getCallFrequencyGraph(fromTimestamp);
		return masterRecord;
	}
	
	
	private String getUnicodeLoginId(int callingPartyNumber) {
		return JSONParser.parseHits(Utils.callRESTService(getStringEntity(
				UNICODE_LOGIN_ID, callingPartyNumber)),
				CALLING_PARTY_UNICODE_LOGIN_ID);
	}
	
	
	private MasterRecord getServiceUsageGraphDetails(Long fromTimestamp) {
		return parseServiceUsageResponse(Utils
				.callRESTService(getStringEntity(SERVICE_USAGE)));
	}
	

	private MasterRecord getCallFrequencyGraph(Long fromTimestamp) {
		return parseCallFrequencyResponse(Utils
				.callRESTService(getStringEntity(CALL_FREQUENCY)));
	}
	
	private MasterRecord parseTerms(JSONObject jsonObj, String location){
		try {
			if(null != jsonObj){
				JSONObject terms = (JSONObject)((JSONObject) jsonObj.get("facets")).get("terms");
				JSONArray array = (JSONArray) terms.get("terms");
				for(int i=0; i< array.length(); i++){
					Double duration = (Double) ((JSONObject)array.get(i)).get("total");
					Integer extension = (Integer) ((JSONObject)array.get(i)).get("term");
					String callerName = getUnicodeLoginId(extension);
					if(HYDERABAD.equals(location)){
						masterRecord.getHyderabadCallRecord().add(new CallRecord(callerName, duration.intValue(), extension));
					}else if(CHENNAI.equals(location)){
						masterRecord.getChennaiCallRecord().add(new CallRecord(callerName, duration.intValue(), extension));
					}else if(BANGALORE.equals(location)){
						masterRecord.getBangaloreCallRecord().add(new CallRecord(callerName, duration.intValue(), extension));
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return masterRecord;
	}
	
	
	private MasterRecord parseServiceUsageResponse(JSONObject jsonObj){
		try {
			if(null != jsonObj){
				JSONObject terms = (JSONObject)((JSONObject) jsonObj.get("facets")).get("terms");
				JSONArray array = (JSONArray) terms.get("terms");
				for(int i=0; i< array.length(); i++){
					String name = (String) ((JSONObject)array.get(i)).get("term");
					Double duration = (Double) ((JSONObject)array.get(i)).get("total");
					masterRecord.getServiceProvider().add(new ServiceProvider(name, duration.intValue()));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return masterRecord;
	}
	
	
	private MasterRecord parseCallFrequencyResponse(JSONObject jsonObj){
		try {
			if(null != jsonObj){
				JSONObject facets = (JSONObject)((JSONObject) jsonObj.get("facets"));
				@SuppressWarnings("unchecked")
				Iterator<String> itr = facets.keys();
				while(itr.hasNext()){
					String key = itr.next().toString();
					JSONArray entries = (JSONArray)((JSONObject)facets.get(key)).get("entries");
					for(int index=0; index<entries.length(); index++){
						Long time = (Long)((JSONObject)entries.get(index)).get("time");
						Integer count = (Integer)((JSONObject)entries.get(index)).get("count");
						if("1".equals(key)){
							masterRecord.getCallFrequency().add(new CallFrequency(new Date(time), count, HYDERABAD));
						}else if("7".equals(key)){
							masterRecord.getCallFrequency().add(new CallFrequency(new Date(time), count, CHENNAI));
						}else if("8".equals(key)){
							masterRecord.getCallFrequency().add(new CallFrequency(new Date(time), count, BANGALORE));
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return masterRecord;
	}
	
	private StringEntity getStringEntity(String param1){
		return getStringEntity(param1, 0);
	}
	
	private StringEntity getStringEntity(String param1, int param2){
		StringEntity stringEntity = null;
		Long fromTimestamp = Utils.getEpochTime();

		try {
			if (HYDERABAD.equals(param1)) {
				stringEntity = new StringEntity(
						"{\"facets\": {\"terms\": {\"terms_stats\": {\"value_field\": \"duration\",\"key_field\": \"callingPartyNumber\",\"size\": 10,\"order\": \"total\"},\"facet_filter\": {\"fquery\": {\"query\": {\"filtered\": {\"query\": {\"bool\": {\"should\": [{\"query_string\": {\"query\": \"originalCalledPartyNumberPartition:\\\"HYD-\\\" AND totalDigitsinCallingPartyNumber:[0 TO 5]\"}}]}},\"filter\": {\"bool\": {\"must\": [{\"terms\": {\"_type\": [\"CallManagerLog\"]}},{\"range\": {\"@timestamp\": {\"from\": "
								+ fromTimestamp
								+ ",\"to\": \"now\"}}}]}}}}}}}},\"size\": 0}");
			} else if (CHENNAI.equals(param1)){
				stringEntity = new StringEntity(
						"{\"facets\":{\"terms\":{\"terms_stats\":{\"value_field\":\"duration\",\"key_field\":\"callingPartyNumber\",\"size\":10,\"order\":\"total\"},\"facet_filter\":{\"fquery\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"should\":[{\"query_string\":{\"query\":\"ServiceUsed:\\\"Chennai_Reliance\\\" AND totalDigitsinCallingPartyNumber:[0 TO 5]\"}}]}},\"filter\":{\"bool\":{\"must\":[{\"terms\":{\"_type\":[\"CallManagerLog\"]}},{\"range\":{\"@timestamp\":{\"from\":"
								+ fromTimestamp
								+ ",\"to\":\"now\"}}}]}}}}}}}},\"size\":0}");
			} else if (BANGALORE.equals(param1)){
				stringEntity = new StringEntity(
						"{\"facets\":{\"terms\":{\"terms_stats\":{\"value_field\":\"duration\",\"key_field\":\"callingPartyNumber\",\"size\":10,\"order\":\"total\"},\"facet_filter\":{\"fquery\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"should\":[{\"query_string\":{\"query\":\"ServiceUsed:\\\"Bangalore_Reliance\\\" AND totalDigitsinCallingPartyNumber:[0 TO 5]\"}}]}},\"filter\":{\"bool\":{\"must\":[{\"terms\":{\"_type\":[\"CallManagerLog\"]}},{\"range\":{\"@timestamp\":{\"from\":"
								+ fromTimestamp
								+ ",\"to\":\"now\"}}}]}}}}}}}},\"size\":0}");
			} else if (UNICODE_LOGIN_ID.equals(param1)){
				stringEntity = new StringEntity("{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"query_string\":{\"query\":\"callingPartyNumber:"
						+ param2
						+ "\"}},{\"query_string\":{\"query\":\"_type:CallManagerLog\"}}]}}}},\"size\":1}");
			} else if (SERVICE_USAGE.equals(param1)) {
				stringEntity = new StringEntity(
						"{\"facets\": {\"terms\": {\"terms_stats\": {\"value_field\": \"duration\",\"key_field\": \"ServiceUsed\",\"size\": 100,\"order\": \"total\"},\"facet_filter\": {\"fquery\": {\"query\": {\"filtered\": {\"query\": {\"bool\": {\"should\": [{\"query_string\": {\"query\": \"originalCalledPartyNumberPartition:\\\"CHN-\\\" AND totalDigitsinCallingPartyNumber:[0 TO 5]\"}},{\"query_string\": {\"query\": \"originalCalledPartyNumberPartition:\\\"HYD-\\\" AND totalDigitsinCallingPartyNumber:[0 TO 5]\"}},{\"query_string\": {\"query\": \"originalCalledPartyNumberPartition:* and -originalCalledPartyNumberPartition:\\\"INTERNAL\\\"\"}},{\"query_string\": {\"query\": \"*\"}},{\"query_string\": {\"query\": \"originalCalledPartyNumberPartition:\\\"BLR-\\\" AND totalDigitsinCallingPartyNumber:[0 TO 5]\"}},{\"query_string\": {\"query\": \"totalDigitsinCallingPartyNumber:[10 TO 12]\"}},{\"query_string\": {\"query\": \"totalDigitsinCallingPartyNumber:[0 TO 5]\"}},{\"query_string\": {\"query\": \"ServiceUsed:\\\"Hyderabad_Reliance\\\" OR ServiceUsed:\\\"Hyderabad_VOIP\\\" OR ServiceUsed:\\\"Hyderabad_Airtel\\\"\"}},{\"query_string\": {\"query\": \"ServiceUsed:\\\"Chennai_Reliance\\\"\"}},{\"query_string\": {\"query\": \"ServiceUsed:\\\"Bangalore_Reliance\\\"\"}}]}},\"filter\": {\"bool\": {\"must\": [{\"terms\": {\"_type\": [\"CallManagerLog\"]}},{\"range\": {\"@timestamp\": {\"from\": "
								+ fromTimestamp
								+ ",\"to\": \"now\"}}}]}}}}}}}},\"size\": 0}");
			} else if (CALL_FREQUENCY.equals(param1)) {
				stringEntity = new StringEntity(
						"{\"facets\": {\"1\": {\"date_histogram\": {\"field\": \"@timestamp\",\"interval\": \"1d\"},\"global\": true,\"facet_filter\": {\"fquery\": {\"query\": {\"filtered\": {\"query\": {\"query_string\": {\"query\": \"originalCalledPartyNumberPartition:\\\"HYD-\\\" AND totalDigitsinCallingPartyNumber:[0 TO 5]\"}},\"filter\": {\"bool\": {\"must\": [{\"terms\": {\"_type\": [\"CallManagerLog\"]}},{\"range\": {\"@timestamp\": {\"from\": 1397469411368,\"to\": \"now\"}}}]}}}}}}},\"5\": {\"date_histogram\": {\"field\": \"@timestamp\",\"interval\": \"1d\"},\"global\": true,\"facet_filter\": {\"fquery\": {\"query\": {\"filtered\": {\"query\": {\"query_string\": {\"query\": \"totalDigitsinCallingPartyNumber:[10 TO 12]\"}},\"filter\": {\"bool\": {\"must\": [{\"terms\": {\"_type\": [\"CallManagerLog\"]}},{\"range\": {\"@timestamp\": {\"from\": 1397469411369,\"to\": \"now\"}}}]}}}}}}},\"7\": {\"date_histogram\": {\"field\": \"@timestamp\",\"interval\": \"1d\"},\"global\": true,\"facet_filter\": {\"fquery\": {\"query\": {\"filtered\": {\"query\": {\"query_string\": {\"query\": \"ServiceUsed:\\\"Chennai_Reliance\\\" AND totalDigitsinCallingPartyNumber:[0 TO 5]\"}},\"filter\": {\"bool\": {\"must\": [{\"terms\": {\"_type\": [\"CallManagerLog\"]}},{\"range\": {\"@timestamp\": {\"from\": 1397469411369,\"to\": \"now\"}}}]}}}}}}},\"8\": {\"date_histogram\": {\"field\": \"@timestamp\",\"interval\": \"1d\"},\"global\": true,\"facet_filter\": {\"fquery\": {\"query\": {\"filtered\": {\"query\": {\"query_string\": {\"query\": \"ServiceUsed:\\\"Bangalore_Reliance\\\" AND totalDigitsinCallingPartyNumber:[0 TO 5]\"}},\"filter\": {\"bool\": {\"must\": [{\"terms\": {\"_type\": [\"CallManagerLog\"]}},{\"range\": {\"@timestamp\": {\"from\": "
								+ fromTimestamp
								+ ",\"to\": \"now\"}}}]}}}}}}}},\"size\": 0}");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return stringEntity;
	}
}
