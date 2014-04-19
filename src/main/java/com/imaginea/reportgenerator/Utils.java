package com.imaginea.reportgenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;


@SuppressWarnings("deprecation")
public class Utils {

	private static final String URL = "http://192.168.3.62:9200/_all/_search?pretty";
	
	public static JSONObject getJSONObj(String response) {
		try {
			return new JSONObject(response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Long getEpochTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date date = cal.getTime();
		return date.getTime();
	}
	
	public static JSONObject callRESTService(StringEntity entity) {
		@SuppressWarnings("resource")
		HttpClient client = new DefaultHttpClient();
		StringBuffer buffer = new StringBuffer();
		String line = "";
		HttpPost post = new HttpPost(URL);
		try {
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			while ((line = rd.readLine()) != null) {
				buffer.append(line);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return getJSONObj(buffer.toString());
	}
	
	public static boolean isNotEmpty(String str) {
		if (null == str || "".equals(str))
			return false;
		return true;

	}
}
