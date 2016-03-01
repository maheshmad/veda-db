package com.taksila.veda.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Validator;
import org.owasp.esapi.errors.ValidationException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

