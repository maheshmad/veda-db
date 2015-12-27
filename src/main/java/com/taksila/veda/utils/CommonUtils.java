package com.taksila.veda.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import com.taksila.veda.base.model.v1_0.Error;
import com.taksila.veda.base.model.v1_0.ErrorInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/*
 * Most parts of this class are intentionally copied from CommonUtils in RA services. So that same resources
 * may be reused during integration.
 */

public class CommonUtils 
{
	static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();			
	
	public static String getNowDateTime(String format) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(cal.getTime());
	}
	
	public static String getUniqueId() {
		 final String tmp = UUID.randomUUID().toString();
	      return tmp.replaceAll("-", "");
	}
	
	
	public static Error buildErr(String field, String msg) {
		Error err = new Error();
		err.setErrorFieldId(field);
		err.setErrorMsg(msg);

		return err;
	}

	public static ErrorInfo buildErrorInfo(String field, String msg) {
		ErrorInfo errInfo = new ErrorInfo();
		errInfo.getErrors().add(buildErr(field, msg));

		return errInfo;
	}
	
	public static String newUUIDString() {
        final String tmp = UUID.randomUUID().toString();
        return tmp.replaceAll("-", "");
    }
	
	/*public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}*/

	// MD5 Encryption goes here
	public static String encrypt(String toBeEncrypted) {
		String hashedString = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(toBeEncrypted.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			hashedString = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}
		return hashedString;
	}
	
	public static <T> T fromJson(String jsonString, Class<T> destclass)
	{
		if (jsonString != null && jsonString.length() > 0)
		{			
			return gson.fromJson(jsonString, destclass);
		}
		return null;
	}

	// Date to String
	public static String getStringTime(Date date, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(cal.getTime());
	}

	// String to Date
	public static Date getDateTime(String date, String format) {
		Date convertedDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			convertedDate = sdf.parse(date);
		} catch (ParseException e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return convertedDate;
	}

	// Adding expiryPeriod(int) to a Date_obj represented in String and
	// returning the new Date_obj as String
	public static String addPeriod(String startDate, String format, int period) {
		Date startingDate = CommonUtils.getDateTime(startDate, format);

		Calendar cal = Calendar.getInstance();
		cal.setTime(startingDate);
		cal.add(Calendar.DAY_OF_MONTH, period);
		Date endingDate = cal.getTime();
		String endDate = CommonUtils.getStringTime(endingDate, format);

		return endDate;
	}

	public static String toJson(Object modelObj) 
	{
		return gson.toJson(modelObj);
	}
	
	public static String toJson(Object modelObj, String rootId) 
	{
		JsonElement jsEl = gson.toJsonTree(modelObj);
	    JsonObject jsObj = new JsonObject();
	    jsObj.add(rootId, jsEl);
		
		return gson.toJson(jsObj);
	}

	public static JsonObject toJSONObject(String jSON) 
	{
		JsonParser jsonParser = new JsonParser();
		
		return (JsonObject)jsonParser.parse(jSON);
	}
	
	public static JsonArray toJSONArray(String jSON) 
	{
		JsonParser jsonParser = new JsonParser();
		
		return (JsonArray)jsonParser.parse(jSON);
	}
	
	public static boolean isNotNull(String value) {
		boolean isNotNull = false;
		
		if(value != null && !value.trim().equals("")) {
			isNotNull = true;
		}
		
		return isNotNull;
	}
	
	public static boolean isNotEmpty(String value) {
		boolean isNotEmpty = false;
		
		if(!value.equals("")) {
			isNotEmpty = true;
		}
		
		return isNotEmpty;
	}
	
	public static boolean isNotNull(Object object) {
		boolean isNotNull = false;
		
		if(object != null && !object.equals("")) {
			isNotNull = true;
		}
		
		return isNotNull;
	}
}
