package com.aqiservice.generic.dal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.aqiservice.generic.domain.AqiInfo;
import com.thinkalike.generic.common.Util;

public class JsonAqiInfoParser implements AqiInfoParser {
	//-- Constants and Enums -----------------------------------
	private final static String TAG = JsonAqiInfoParser.class.getSimpleName();
	
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	private String _areaCode = "";
	
	//-- Properties --------------------------------------------
	public JsonAqiInfoParser setAreaCode(String areaCode){ _areaCode = areaCode; return this;}
	
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	public AqiInfo parse(InputStream is, String charsetName){
		AqiInfo result = null;
		
		//InputStream --> String
		BufferedReader in;
		StringBuffer buffer = new StringBuffer();
		String line = "";
		try {
			in = new BufferedReader(new InputStreamReader(is, charsetName));
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			Util.error(TAG, "InputStream read failed: " + e.getMessage());
			return null;
		}
		
		//String --> AqiInfo
		String jsonString = buffer.toString();
		Object obj = null;
		JSONParser parser=new JSONParser();
		try {
			obj = parser.parse(jsonString);
		} catch (ParseException e) {
			Util.error(TAG, "JSON parser failed: " + e.getMessage());
			return null;
		}
		JSONObject jsonObj = (JSONObject)((JSONArray)obj).get(0);
		int aqiValue = ((Number)jsonObj.get("aqi")).intValue(); 
		String aqiArea = (String)jsonObj.get("area");
		Date aqiDate = parseDate((String)jsonObj.get("time_point")); 
		int aqiPM2_5 = ((Number)jsonObj.get("pm2_5")).intValue(); 
		int aqiPM2_5_24h = ((Number)jsonObj.get("pm2_5_24h")).intValue();
		result = new AqiInfo(aqiValue, aqiArea, _areaCode, aqiDate, aqiPM2_5, aqiPM2_5_24h);
		
		return result;
	}
	
	//-- Private and Protected Methods -------------------------
	private static Date parseDate(String str){
		Date result = new Date(); //default|onError = current 
		if(str == null)
			return result;
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		try {
			result = df.parse(str);
		} catch (java.text.ParseException e) {
			return result;
		}
		
		return result;
	}
	
	//-- Event Handlers ----------------------------------------
}
