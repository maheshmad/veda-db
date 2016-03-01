package com.taksila.veda.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ManagedAsync;

import com.taksila.veda.model.api.base.v1_0.BaseResponse;
import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.model.api.security.v1_0.UserLoginResponse;
import com.taksila.veda.utils.CommonUtils;

@Path("/auth")
public class UserAuthService 
{
	static Logger logger = LogManager.getLogger(UserAuthService.class.getName());
	public static final String USER_AUTH_SESSION_COOKIE_NAME = "authsessionid";
	Client client = ClientBuilder.newClient();   
		
	/*
	 * Login using local LDAP without oauth
	 */
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ManagedAsync
    public void post(@FormParam("username") String userid, @Context HttpServletRequest request,@Context HttpServletResponse response,
    		@Context UriInfo uri,@FormParam("password") String password,@Suspended final AsyncResponse asyncResp) 
    {    	
		HttpSession session = request.getSession(true);		
		String tenantId = CommonUtils.getSubDomain(uri.getBaseUri());				
		/*
		 * get user info
		 */
		UserAuthComponent userAuthComponent = new UserAuthComponent(tenantId);
		UserLoginResponse loginResp = userAuthComponent.authenticate(userid,password,session.getId());
		
		if (loginResp.getErrorInfo() != null)
		{			
			asyncResp.resume(Response.status(403).entity(loginResp).build());
		}
		else
		{
			NewCookie cookie = new NewCookie(USER_AUTH_SESSION_COOKIE_NAME, loginResp.getSessionid());
			asyncResp.resume(Response.ok(loginResp).cookie(cookie).build());
		}
		        
    }
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	@Path("/user")
	public void getLoggedInUserInfo(@Context HttpServletRequest request, @Context UriInfo uri,	@Context HttpServletResponse resp,
			@CookieParam(USER_AUTH_SESSION_COOKIE_NAME) Cookie authCookie,@Suspended final AsyncResponse asyncResp)
	{    				
		
		if (authCookie == null)
			asyncResp.resume(Response.status(403).type("text/plain").entity("No auth cookie found! Please make sure the cookie feature is enabled").build());
		
		String authSessionId = authCookie.getValue();				
		String tenantId = CommonUtils.getSubDomain(uri.getBaseUri());				
		/*
		 * get user info
		 */
		UserAuthComponent userAuthComponent = new UserAuthComponent(tenantId);
		UserLoginResponse userInfoResp = userAuthComponent.getLoggedInUser(authSessionId);
		
		if (userInfoResp.getErrorInfo() != null)
		{			
			asyncResp.resume(Response.status(403).entity(userInfoResp).build());
		}
		else
		{
			NewCookie cookie = new NewCookie(USER_AUTH_SESSION_COOKIE_NAME, userInfoResp.getSessionid());
			asyncResp.resume(Response.ok(userInfoResp).cookie(cookie).build());
		}		
		
	}
	
	
	@GET
	@Path("/user/profileimage")
	@Produces("image/*")
	public Response getUserImage(@Context HttpServletRequest request,@Context UriInfo uri,
			@Context HttpServletResponse resp,@CookieParam(USER_AUTH_SESSION_COOKIE_NAME) Cookie authCookie,@Suspended final AsyncResponse asyncResp)
	{    						
		logger.trace("Inside getUserImage service!!!!!");
		String tenantId = CommonUtils.getSubDomain(uri.getBaseUri());		
		byte[] userphoto = CommonUtils.readImageFile("defaultprofileimage-128.png");
			
		return Response.ok(userphoto).build();
				
	}
				
	/*
	 * Logoff user
	 */
	@POST
	@Path("/logoff")
	@Produces(MediaType.APPLICATION_JSON)	
    public void post(@Context HttpServletRequest request,@CookieParam(USER_AUTH_SESSION_COOKIE_NAME) Cookie authCookie,@Context UriInfo uri,@Suspended final AsyncResponse asyncResp) 
    {    			
        logger.trace("received logout request for session");
        BaseResponse resp = new BaseResponse();
        resp.setSuccess(true);
    	try
		{        	    		    		    				  	    		
    		if (authCookie != null)
    		{	    		
	    		String authSessionId = authCookie.getValue();				
	    		String tenantId = CommonUtils.getSubDomain(uri.getBaseUri());				
	    		/*
	    		 * get user info
	    		 */
	    		UserAuthComponent userAuthComponent = new UserAuthComponent(tenantId);    		
	    		boolean logoutResp = userAuthComponent.logout(authSessionId);
	    		if (logoutResp)
	    		{
	    			resp.setStatus(StatusType.SUCCESS);
	    			resp.setMsg("Successfully logged out session = "+authSessionId);	
	    		}
	    		else
	    			resp.setStatus(StatusType.FAILED);
	    		
    		}
    		else
    		{
    			resp.setStatus(StatusType.INVALID);
    			resp.setMsg("No authorization cookie found! Operation is invalid");	       			
    		}
	    		
		}    	
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setStatus(StatusType.EXCEPTION);
			resp.setMsg("No authorization cookie found! Operation is invalid");			
		}
		    	
		asyncResp.resume(Response.ok(resp).build());
        
    }
	

	
}
