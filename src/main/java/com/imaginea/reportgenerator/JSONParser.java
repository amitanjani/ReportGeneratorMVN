package com.imaginea.reportgenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
	public static String parseTerms(JSONObject jsonObj) {
		Integer count = 0;
		String term = null;
		String response = "";
		try {
			if (null != jsonObj) {
				JSONObject terms = (JSONObject) ((JSONObject) jsonObj
						.get("facets")).get("terms");
				JSONArray array = (JSONArray) terms.get("terms");
				for (int i = 0; i < array.length(); i++) {
					count = (Integer) ((JSONObject) array.get(i)).get("count");
					term = (String) ((JSONObject) array.get(i)).get("term");
					response += " " + term + ":" + count;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public static String parseHits(JSONObject jsonObj, String fieldName){
		String fieldValue = null;
		try {
			if(null!=jsonObj){
				JSONObject obj = (JSONObject) jsonObj.get("hits");
				if(!obj.has("hits")){
					return "";
				}
				JSONArray hits = (JSONArray)obj.get("hits");
				if(hits.isNull(0)){
					return "";
				}
				fieldValue = (String)((JSONObject)((JSONObject)hits.get(0)).get("_source")).get(fieldName);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return fieldValue;
	}
}
