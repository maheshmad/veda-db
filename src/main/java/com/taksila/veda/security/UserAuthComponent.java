package com.taksila.veda.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.db.dao.UserManagementDAO;
import com.taksila.veda.db.dao.UserSessionDAO;
import com.taksila.veda.model.api.base.v1_0.BaseResponse;
import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.model.api.security.v1_0.UserInfo;
import com.taksila.veda.model.api.security.v1_0.UserLoginResponse;
import com.taksila.veda.utils.CommonUtils;
import com.taksila.veda.utils.ValidationUtils;


public class UserAuthComponent 
{	
	private String tenantId =null;	
	private UserManagementDAO userManagementDAO = null;
	private UserSessionDAO userSessionDAO = null;
	private String dateFormat = "MM/dd/yyyy HH:mm:ss z";
	static Logger logger = LogManager.getLogger(UserAuthComponent.class.getName());
	
	public UserAuthComponent(String tenantId) 
	{
		this.tenantId = tenantId;
		this.userManagementDAO = new UserManagementDAO(tenantId);
		this.userSessionDAO = new UserSessionDAO(tenantId);
		
	}
	
	public UserLoginResponse getLoggedInUser(String sessionid)
	{
		UserLoginResponse resp = new UserLoginResponse();
		try 
		{
			UserInfo userInfo = userSessionDAO.getUserFromSession(sessionid);
			resp.setUserInfo(userInfo);
			resp.setSessionid(sessionid);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			resp.setStatus(StatusType.FAILED);
			resp.setErrorInfo(CommonUtils.buildErrorInfo("FAILED", e.getMessage()));
		}
		return resp;
	}
	
	public BaseResponse changePassword(String userId,String oldPassword,String newPassword)
	{
		BaseResponse baseResponse = new BaseResponse();
		try 
		{
			String passWordValidationRes = ValidationUtils.doPasswordValidation(newPassword);
			if("PASS".equals(passWordValidationRes)) 
			{
				userManagementDAO.changePassword(userId,oldPassword, newPassword);
			} 
			else 
			{
				baseResponse.setStatus(StatusType.FAILED);
				baseResponse.setErrorInfo(CommonUtils.buildErrorInfo("ERROR", passWordValidationRes));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			baseResponse.setStatus(StatusType.FAILED);
			baseResponse.setErrorInfo(CommonUtils.buildErrorInfo("ERROR", e.getMessage()));
		}
		return baseResponse;
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
	public UserLoginResponse authenticate(String userid, String pwd, String sessionid) 
	{
		UserLoginResponse loginResp = new UserLoginResponse();
		try 
		{
			logger.trace("About to validate user = "+userid+" pwd = "+pwd);
			UserInfo userInfo = userManagementDAO.authenticate(userid, pwd);
			if (userInfo != null)
			{	
				loginResp.setUserInfo(userInfo);
				loginResp.setSuccess(true);
				loginResp.setStatus(StatusType.SUCCESS);
				userSessionDAO.authorizeSession(sessionid, userid);
				loginResp.setSessionid(sessionid);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return loginResp;
	}
	
}
