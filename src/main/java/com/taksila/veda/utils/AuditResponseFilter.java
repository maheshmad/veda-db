/**
 * 
 */
package com.taksila.veda.utils;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author admin
 *
 */
@Provider
@PreMatching
public class AuditResponseFilter implements ContainerResponseFilter
{
	
	@Context UriInfo uriInfo;
	static Logger logger = LogManager.getLogger(AuditResponseFilter.class.getName()); 
	@Override
	public void filter(ContainerRequestContext requestContext,
	            ContainerResponseContext responseContext) throws IOException 
	 {
		logger.trace("In AuditResponseFilter.....");
				
	 }

}
