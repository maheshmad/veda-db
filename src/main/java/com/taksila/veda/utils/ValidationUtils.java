package com.taksila.veda.utils;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Validator;
import org.owasp.esapi.errors.ValidationException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taksila.veda.model.api.base.v1_0.Err;

public class ValidationUtils
{
	static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();	
	static Logger logger = LogManager.getLogger(ValidationUtils.class.getName());
	static Validator validator = ESAPI.validator();
		
	
	
	public static String getValidEmail(String input) 
	{			    
	    try 
	    {
			return validator.getValidInput("validation",input, "Email", 256, false);
		} 
	    catch (ValidationException e) 
	    {		
	    	return "";
		} 	    
	}
	
	public static String getValidSecurityKey(String input) 
	{			    
	    try 
	    {
	    	/*
	    	 * the security key is email in our case
	    	 */
			return validator.getValidInput("validation",input, "Email", 256, false);
		} 
	    catch (ValidationException e) 
	    {		
	    	return "";
		} 	    
	}
	
	public static String getValidSecurityToken(String input) 
	{			    
	    try 
	    {
	    	/*
	    	 * the security key is email in our case
	    	 */
			return validator.getValidInput("validation",input, "Token",1000,false);
		} 
	    catch (ValidationException e) 
	    {		
	    	return "";
		} 	    
	}
	
	
	public static Err isValidDate(String fieldId, XMLGregorianCalendar val, String dateFormat, boolean checkMandatory)
	{
		if (checkMandatory && val==null) 
			return CommonUtils.buildErr(fieldId, "is missing/invalid, Please provide a valid value in format "+dateFormat);
		
		return null;
//		try
//		{
//			if (CommonUtils.g))val, dateFormat) == null)
//				return CommonUtils.buildErr(fieldId, "is invalid, Please provide a valid value");
//			
//			return null;
//		}
//		catch(Exception ex)
//		{
//			return CommonUtils.buildErr(fieldId, val+" is not a valid input. Please provide a valid value");
//		}
				
	}
	
	public static String doPasswordValidation(String password) 
	{
		String result = "PASS";
		if(CommonUtils.isEmpty(password)) {
			result = "Password cannot be empty.";
		} else if(password.length() < 8) {
			result = "Password must be 8 character long.";
		}
		return result;
	}
}

