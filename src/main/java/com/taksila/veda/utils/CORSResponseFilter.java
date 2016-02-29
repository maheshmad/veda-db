package com.taksila.veda.utils;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
@PreMatching
public class CORSResponseFilter implements ContainerResponseFilter
{
	static Logger logger = LogManager.getLogger(CORSResponseFilter.class.getName()); 
	@Override
	public void filter(ContainerRequestContext requestContext,
	            ContainerResponseContext responseContext) throws IOException 
	 {
						
		logger.trace("Entity tag = "+responseContext.getEntityTag());
		if (!"*".equals(responseContext.getHeaders().get("Access-Control-Allow-Origin")))
		{		
			logger.trace("setting the Access-Origin-Allow = "+" * ");
			responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
		    responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type,X-Requested-With, accept, authorization");
		    responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
		    responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		    responseContext.getHeaders().add("Access-Control-Max-Age", "1209600");
		}
	 }

}
