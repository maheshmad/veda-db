package com.taksila.veda.security;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.oauth1.signature.OAuth1Parameters;

import com.taksila.veda.db.dao.UserManagementDAO;
import com.taksila.veda.model.api.base.v1_0.BaseResponse;
import com.taksila.veda.model.api.security.v1_0.UserInfo;
import com.taksila.veda.model.api.security.v1_0.UserLoginResponse;
import com.taksila.veda.utils.CommonUtils;

@Path("/userauth")
public class UserManagementService 
{
	static Logger logger = LogManager.getLogger(UserManagementService.class.getName());
	public static final String USER_AUTH_SESSION_ATTR = "user-auth-info";
	Client client = ClientBuilder.newClient();   
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserLoginResponse getUserInfo(@Context HttpServletRequest request, @Context UriInfo uri,	@Context HttpServletResponse resp)
	{    				
		UserInfo userAuth = new UserInfo();
		UserLoginResponse loginResp = new UserLoginResponse();
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute(USER_AUTH_SESSION_ATTR) != null)
		{
			userAuth = (UserInfo) session.getAttribute(USER_AUTH_SESSION_ATTR); 
			if(userAuth != null) 
			{
				String tenantId = CommonUtils.getSubDomain(uri.getBaseUri());
				UserManagementDAO userManagementDAO = new UserManagementDAO(tenantId);
				userAuth = userManagementDAO.getUserInfo(userAuth.getUserId());
				session.setAttribute(USER_AUTH_SESSION_ATTR, userAuth);
				UserManagementComponent userManagementComponent = new UserManagementComponent(tenantId);
				BaseResponse baseResponse = userManagementComponent.getUserById(userInfoRequest)(userAuth.getUserId());
				loginResp.setUserInfo((UserInfo)baseResponse.getResponse());
				loginResp.setUserInfo(userAuth);
			}
			
		}
		else
		{
			userAuth = new UserInfo();			
			userAuth.setUserId("");
			userAuth.setOauthAccessToken("");		
			loginResp.setUserInfo(userAuth);
		}
		
		return loginResp;
	}
	
	@GET
	@Path("/profileimage")
	@Produces("image/*")
	public Response getUserImage(@Context HttpServletRequest request,@Context UriInfo uri,
			@Context HttpServletResponse resp)
	{    						
		logger.trace("Inside getUserImage service!!!!!");
		String tenantId = CommonUtils.getSubDomain(uri.getBaseUri());
		HttpSession session = request.getSession(true);
		if (session == null)
			return Response.ok(Status.UNAUTHORIZED).build();
						
		UserInfo authInfo = (UserInfo) session.getAttribute(USER_AUTH_SESSION_ATTR);				
		byte[] userphoto;
		if (authInfo == null || authInfo.getUserProfileImage() == null)
			userphoto = null;
		else
		{
			//LDAPUtils ldapUtils = new LDAPUtils(tenantId);
			//userphoto = ldapUtils.getLDAPUserAttributeValue(authInfo.getUserId(), "jpegphoto");
			userphoto = null; 
			UserManagementComponent userManagementComponent = new UserManagementComponent(tenantId);
			BaseResponse baseResponse = userManagementComponent.getUserById(authInfo.getUserId());
			if(baseResponse != null && baseResponse.getResponse() != null) {
				UserAccountInfo userAccountInfo = (UserAccountInfo)baseResponse.getResponse();
				if(CommonUtils.isNotEmpty(userAccountInfo.getProfileImage())) {
					userphoto = Base64.decodeBase64(userAccountInfo.getProfileImage());
					logger.trace("User Profile Image" +userphoto );
				}
			}
		}
				
		
		if (userphoto == null)
			return Response.ok(CommonUtils.readImageFile("defaultprofileimage-128.png")).build();
		
		
		
		return Response.ok(userphoto).build();		
		
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/oauth")
	public Response saveUserAccessToken(@FormParam(OAuth1Parameters.TOKEN) String access_token,
							@Context UriInfo uri,@FormParam(OAuth1Parameters.CONSUMER_KEY) String userid,@Context HttpServletRequest request)
	{
		String tenantId = CommonUtils.getSubDomain(uri.getBaseUri());
		TenantConfigUtils tenantConfig = ConfigUtils.getTenantConfig(tenantId);
		
		logger.trace("Inside saveUserAccessToken!!!!!");
		logger.trace("Received access_token for user ="+userid+", token = "+access_token);		
		
		/*
		 * STAGE 1: First validate the access token received, by checking it in the local 
		 * store, 
		 * 
		 * if present move on to STAGE-2 check
		 * 
		 */
		if (access_token.equals(OAuthAccessTokenStore.getAccessToken(userid)))
		{
			/*
			 * STAGE 2: Security token validation check from the oauth-server.
			 * Pass the userid and access token to fetch the UserInfo from
			 * LDAP.
			 * 
			 * If it returns null then assume that your access_token is no more 
			 * valid.
			 *  
			 */			
			
			String targetServiceUrl = tenantConfig.getValue(TenantConfigType.OAUTH_SERVER_BASE_URI)+
										tenantConfig.getValue(TenantConfigType.OAUTH_USER_AUTH_INFO_URI)+
										"/"+userid+
										"?oauth_access_token="+access_token;
			
			logger.trace("About to fetch userauth data , rest api url = "+targetServiceUrl);
			HttpAuthenticationFeature httpFeature = HttpAuthenticationFeature.basic(tenantConfig.getValue(TenantConfigType.OAUTH_CONSUMER_AUTH_ID),
																			tenantConfig.getValue(TenantConfigType.OAUTH_CONSUMER_AUTH_PWD));			
			client.register(httpFeature);
			WebTarget target = client.target(targetServiceUrl);
			
			Response serviceResponse = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
			logger.trace("service return status code = "+serviceResponse.getStatus() );
			/*
			 * if failed to retrieve any info from webservice
			 */
			if (serviceResponse.getStatus() != 200)
			{				
				return Response.status(Status.SERVICE_UNAVAILABLE).build();
			}
			else; 
			
			/*
			 * if not userAuth found, send back a bad request message
			 */
			UserInfo userAuth = (UserInfo) serviceResponse.readEntity(UserInfo.class);			
			if (userAuth == null)
			{
				logger.trace("STAGE 2- SECURITY CHECK FAILED FOR USER = "+userid+": USER WAS NOT FOUND IN LDAP, PLEASE RESTART AUTH FLOW");
				return Response.ok("{'failure':'STAGE 2- SECURITY CHECK FAILED: USER "+userid+" WAS NOT FOUND IN LDAP, PLEASE RESTART AUTH FLOW'}").build();
			}
			else;
			
			/*
			 * at this point everything looks fine ,
			 * treat as if STAGE-2: security check is success
			 * 
			 *  set the UserInfo into session, so that subsequent 
			 *  request can access this information.
			 */
			logger.trace("Recieved valid token , Setting the access token in the session !!");
			logger.trace("STAGE 2: security check passed for user id = "+userAuth.getUserId());
			setSecurityUserAuthIntoSession(userAuth,request);
			
			URI redirectUri = UriBuilder.fromUri("../accessTokenGrantComplete.jsp")				 							 	
		            .queryParam(OAuth1Parameters.CONSUMER_KEY, userid)				            
		            .build();
			
			return Response.seeOther(redirectUri).build();
			
		}
		else
		{
			logger.trace("Access token not found in the server  returning BAD REQUEST!! !!");
			return Response.ok("{'failure':'ACCESS TOKEN NOT FOUND, PLEASE RESTART AUTH FLOW'}").build();	
		}
										
		
	}
	
	/*
	 * Login using local LDAP without oauth
	 */
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public UserLoginResponse post(@FormParam("username") String username, @Context HttpServletRequest request,@Context HttpServletResponse response,
    		@Context UriInfo uri,@FormParam("password") String password) 
    {    	
		UserInfo userInfo = null;
		UserLoginResponse loginresp = new UserLoginResponse();
        logger.trace("received login request from consumer = "+username);
        String tenantId = CommonUtils.getSubDomain(uri.getBaseUri());        
             
    	try
		{        	    		    		    				  	
    		logger.trace("checking user authentication for userid = "+username);
    		
//    		if (request.getUserPrincipal() != null)  /* all login will happen via LDAP not from container*/
//    			request.logout();    		
//    		request.login(username, password);    		
    		
    		/*
    		 * fetch the LDAP info
    		 */
    		logger.trace("getting user auth info from lDAP = "+username);
    		UserManagementDAO userManagementDAO = new UserManagementDAO(tenantId);
    		
    		userManagementDAO.authenticate(username, password); /* will throw an exception if this fails */
    		
    		userInfo = userManagementDAO.getUserInfo(username);
    		userInfo.setOauthAccessToken(CommonUtils.newUUIDString());
    		
    		UserManagementComponent userManagementComponent = new UserManagementComponent(tenantId);
			BaseResponse baseResponse = userManagementComponent.getUserById(username);
			
			loginresp.setUserAccountInfo((UserAccountInfo)baseResponse.getResponse());
    		/*
    		 * since login is success full set it into session
    		 */    		
    		setSecurityUserAuthIntoSession(userInfo,request);       		
    		loginresp.setUserInfo(userInfo); 
    		logger.trace("login successfull for user = "+userInfo.getUserFullname());
    		loginresp.setSuccess(true);
    		
		} catch (PasswordMustChangeException e) {
			Err error = new Err();
			error.setErrorFieldId("PwdChange");
			error.setErrorMsg("Error! Reason = "+e.getMessage());
			loginresp.setErr(error);
			loginresp.setSuccess(false);
		}   	
		catch (Exception e)
		{
			//e.printStackTrace();	/* no need to print this in the logs */ 		
			Err error = new Err();
			error.setErrorFieldId("LoginId");
			error.setErrorMsg("Error! Reason = "+e.getMessage());
			loginresp.setErr(error);
			loginresp.setSuccess(false);
		}
    	    			
    	
    	return loginresp;    	
  
        
    }
	
	
	/*
	 * Send reset link 
	 */
	@POST
	@Path("/email-reset-passwd-link")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response resetPassword(@FormParam("username") String username, @Context HttpServletRequest request,@Context HttpServletResponse response,
    		@Context UriInfo uri,	@FormParam("captcha") String password) 
    {    	
		String tenantId = CommonUtils.getSubDomain(uri.getBaseUri());
		TenantConfigUtils tenantConfig = ConfigUtils.getTenantConfig(tenantId);
		EmailResetResponse emailResetResponse = new EmailResetResponse();
    	try
		{        	    		    		    				  	
    		logger.trace("checking user authentication for userid = "+username);
    		/*
    		 * fetch the user from LDAP
    		 */
    		logger.trace("getting user auth info from lDAP = "+username);
    		UserManagementDAO userManagementDAO = new UserManagementDAO(tenantId);
    		UserInfo userInfo = null;
    		try
    		{
    			userInfo = userManagementDAO.getUserInfo(username);
    		}
    		catch(Exception ex)
    		{
    			/* assume user does not exists */
    		}
    		/*
    		 * check if the user exists on the LDAP
    		 */
    		if (userInfo != null)
    		{
    			/*
        		 * check if the email exists
        		 */
    			if (StringUtils.isNotBlank(userInfo.getMailId()))
    			{
    				logger.trace("found user's email id for sending reset link = "+userInfo.getMailId());
    				emailResetResponse.status = "SUCCESS";    			
    				emailResetResponse.msg = "A password reset link has being sent to your email = "+userInfo.getMailId();
	    			/*
	    			 * test password reset
	    			 */
//	    			if (ldapUtils.resetPassword(username, "abc@123"))
//	    				emailResetResponse.msg += ", password reset done!!!";
    				EmailUtils emailUtils = new EmailUtils();
    				emailUtils.sendMail(userInfo.getMailId(), "support@intellectseeclabs.com","Password reset", "Test message","");
    			}
    			else
    			{
    				
    				logger.trace("did not find user's email id for sending reset link ");
    				emailResetResponse.success = false; 
    				emailResetResponse.status = "INVALID";
    				emailResetResponse.msg = "Sorry! We did not find any valid emails associated with your account, Please contact support to fix this issue! ";    			
    				
    			}
    				
    				
    		}            
    		else
    		{
    			logger.trace("did not find the user in LDAP = "+username);
    			emailResetResponse.success = false;
				emailResetResponse.status = "INVALID";
				emailResetResponse.msg =  "We were unable to locate your account! Please check your username = "+username;    			
    		}
    		
		}    	
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
			emailResetResponse.success = false;
			emailResetResponse.status = "FAILED";
			emailResetResponse.msg = "Sorry! An exception occured ! Please contact support! cause =  "+e.getMessage();     
		}
    	    			
    	
    	return Response.ok(CommonUtils.toJson(emailResetResponse)).build();
    	//return emailResetResponse;
  
        
    }
	
	
	/*
	 * Logoff user
	 */
	@POST
	@Path("/logoff")
	@Produces(MediaType.APPLICATION_JSON)	
    public Response post(@Context HttpServletRequest request) 
    {    			
        logger.trace("received logout request for session");
        String username = "";     
    	try
		{        	    		    		    				  	    		
    		if (request.getUserPrincipal() != null)
    		{
    			logger.trace("remove user auth info from session for user = "+ request.getUserPrincipal().getName());
    			username = request.getUserPrincipal().getName();
    			request.logout();    		
    		}    		    		
    		
    		UserAuthUtils.invalidateSession(request);
		}    	
		catch (Exception e)
		{
			e.printStackTrace();			
            return Response.ok("{\"success\":false,\"error\":\""+e.getMessage()+"\"}").build();
		}
		
    	return Response.ok("{\"success\":true,\"loggedOutUser\":\""+username+"\"}").build();
  
        
    }
	
	/*
	 * set security auth into session
	 */
	private void setSecurityUserAuthIntoSession(UserInfo userAuth, HttpServletRequest request)
	{
		HttpSession session = request.getSession(true);																						
		session.setAttribute(USER_AUTH_SESSION_ATTR, userAuth);
		try
		{
			//boolean savedInMemFlag = MemCacheUtils.saveInCache(userAuth.getUserId(), userAuth, 6000);
		}
		catch(Exception e)
		{
			CommonUtils.logEyeCatchingMessage("FAILED TO STORE SESSION DATA INTO MEMCACHE :",true);
		}
		
		logger.trace("successfully saved the user token in session!!!! id = "+session.getId() );
	}
	
	
	
	
}
