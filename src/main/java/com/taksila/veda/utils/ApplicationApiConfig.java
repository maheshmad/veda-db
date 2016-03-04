package com.taksila.veda.utils;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
/**
 * The @ApplicationPath annotation is used to define the URL mapping for the application. 
 * The path specified by @ApplicationPath is the base URI for all resource URIs specified by @Path annotations in the resource class. 
 * You may only apply @ApplicationPath to a subclass of javax.ws.rs.core.Application.
 * 
 * @author      Mahesh M
 * @version     %I%, %G%
 * @since       1.0
 */
@ApplicationPath("api")
public class ApplicationApiConfig extends ResourceConfig
{
	public ApplicationApiConfig()
	{
		System.out.println("Inside veda application api config !!!!!!!!!!!!!");
		
	}
	
}
