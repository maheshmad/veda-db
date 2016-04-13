package com.taksila.veda.security;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.db.dao.UserSessionDAO;
import com.taksila.veda.db.dao.UsersDAO;
import com.taksila.veda.model.api.base.v1_0.BaseResponse;
import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.model.api.security.v1_0.ResetPasswordResponse;
import com.taksila.veda.model.api.security.v1_0.UserLoginResponse;
import com.taksila.veda.model.db.security.v1_0.UserSession;
import com.taksila.veda.model.db.usermgmt.v1_0.User;
import com.taksila.veda.utils.CommonUtils;
import com.taksila.veda.utils.ValidationUtils;


public class UserAuthComponent 
{	
	private String tenantId =null;	
	private UsersDAO usersDAO = null;
	private UserSessionDAO userSessionDAO = null;
	private String dateFormat = "MM/dd/yyyy HH:mm:ss z";
	static Logger logger = LogManager.getLogger(UserAuthComponent.class.getName());
	
	public UserAuthComponent(String tenantId) 
	{
		this.tenantId = tenantId;
		this.usersDAO = new UsersDAO(tenantId);
		this.userSessionDAO = new UserSessionDAO(tenantId);
		
	}
	
	public UserLoginResponse getLoggedInUser(String sessionid)
	{
		UserLoginResponse resp = new UserLoginResponse();
		try 
		{
			UserSession userSession = userSessionDAO.getValidSession(sessionid);
			User userInfo = usersDAO.getUserByUserId(userSession.getUserId());
			resp.setUserInfo(userInfo);
			resp.setSessionInfo(userSession);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
	}
	
	/**
	 * 
	 * @param userId
	 * @param authTokenId
	 * @param newPassword
	 * @param confirmpassword
	 * @return
	 */
	public ResetPasswordResponse changePassword(String authTokenId,String newPassword, String confirmpassword)
	{
		ResetPasswordResponse resetResponse = new ResetPasswordResponse();
		try 
		{
			UserSession userSession = this.userSessionDAO.getValidSession(authTokenId);
			if(userSession == null) 
			{
				resetResponse.setStatus(StatusType.FAILED);
				resetResponse.setMsg("Un-authorized attempt to update the password. Please check your input!");								
			} 
			else 
			{
				if (!StringUtils.equals(newPassword, confirmpassword))
				{
					resetResponse.setStatus(StatusType.FAILED);
					resetResponse.setMsg("Passwords does not match. Please check your input!");			
				}
				else
				{
					if (this.usersDAO.updatePassword(userSession.getUserId(), CommonUtils.getSecureHash(newPassword)))
					{
						this.userSessionDAO.invalidateUserSession(userSession.getId());
						resetResponse.setStatus(StatusType.SUCCESS);
						resetResponse.setMsg("<span style='color:green'>Password successfully changed! Please login with your new password</span>");							
					}
					else
					{
						resetResponse.setStatus(StatusType.FAILED);
						resetResponse.setMsg("Attempt to change password failed. Please try again later or call support!");		
					}
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			CommonUtils.handleExceptionForResponse(resetResponse, e);
		}
		return resetResponse;
	}
	
	
	/**
	 * 
	 * @param sessionid
	 * @param userId
	 * @return
	 */
	public boolean logout(String sessionid)
	{
		try 
		{
			userSessionDAO.invalidateUserSession(sessionid);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 
	 * @param userid
	 * @param pwd
	 * @param sessionid
	 * @return
	 */
	public UserLoginResponse authenticate(String userid, String pwd, UserSession session) 
	{
		UserLoginResponse loginResp = new UserLoginResponse();
		try 
		{
			logger.trace("About to validate user = "+userid+" pwd = "+pwd);
			User user = usersDAO.authenticate(userid, pwd);
						
			if (user != null && userSessionDAO.addSession(session))
			{	
				loginResp.setUserInfo(user);				
				loginResp.setStatus(StatusType.SUCCESS);				
				loginResp.setSessionInfo(session);
			}
			else
			{
				loginResp.setStatus(StatusType.FAILED);
				loginResp.setMsg("Login failed, please check your userid / password");
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			CommonUtils.handleExceptionForResponse(loginResp, e);
		}
		
		return loginResp;
	}
	
}
