package com.taksila.veda.utils;

import java.io.IOException;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Uma
 *
 */
@Provider
@PreMatching
@Priority(4)
public class AuthorizationFilter implements ContainerRequestFilter{
	
	private static Logger logger = LogManager.getLogger(AuthorizationFilter.class.getName());
	
	@Context
	HttpServletRequest webRequest;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException 
	{
		logger.trace("in AuthorizationFilter.....");
		if (webRequest != null) 
		{
			String requestApi = webRequest.getRequestURI();
			 requestApi = requestApi.substring(webRequest.getContextPath().length());
			String method = requestContext.getMethod();
			logger.trace("REQUEST API:::"+requestApi+" Request METHOD::"+method);
			
			
		}
	}

}
