package com.taksila.veda.utils;

import java.io.IOException;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
@Priority(2)
public class CORSRequestFilter implements ContainerRequestFilter
{
	
	public static final String USER_AUTH_SESSION_ATTR = "user-auth-info";
	
	static {
		
		
	}
	
	@Context
	HttpServletRequest webRequest;
	
	public CORSRequestFilter() {
		
	}
   
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException 
	{
		String method = requestContext.getMethod();	   
	    if(method.equals("OPTIONS")) { 
	        throw new WebApplicationException(Status.OK);
	    }
	    
		
		
	}
	

	

}
