package com.taksila.veda.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
@PreMatching
@Priority(2)
public class CORSRequestFilter implements ContainerRequestFilter
{
	
	public static final String USER_AUTH_SESSION_ATTR = "user-auth-info";
	
	private static Logger logger = LogManager.getLogger(CORSRequestFilter.class.getName());
	
	private List<String> noAuthenticationURLList = new ArrayList<String>();
	
	static {
		
		
	}
	
	@Context
	HttpServletRequest webRequest;
	
	public CORSRequestFilter() {
		logger.trace("CORSRequestFilter initilized");
		
	}
   
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException 
	{
		String method = requestContext.getMethod();	   
		logger.trace("inside cors filter request method = " + method);;
	    if(method.equals("OPTIONS")) { 
	        throw new WebApplicationException(Status.OK);
	    }
	    
		if(webRequest != null)
		{
			logger.trace("webRequest, uri - " + webRequest.getRequestURI());
		
			String requestdURL = webRequest.getRequestURI();
			
			HttpSession session = webRequest.getSession(false);
			
		} else 
		{
			logger.trace("Request Object Empty....");
			throw new WebApplicationException(Status.FORBIDDEN);
		}
		//responseContext.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
		
	}
	
	
	

}
