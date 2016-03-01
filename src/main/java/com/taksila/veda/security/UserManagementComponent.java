package com.taksila.veda.security;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.db.dao.UserManagementDAO;
import com.taksila.veda.db.dao.UserSessionDAO;
import com.taksila.veda.model.api.base.v1_0.BaseResponse;
import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.model.api.security.v1_0.GetUserInfoRequest;
import com.taksila.veda.model.api.security.v1_0.GetUserInfoResponse;
import com.taksila.veda.model.api.security.v1_0.SearchUsersRequest;
import com.taksila.veda.model.api.security.v1_0.SearchUsersResponse;
import com.taksila.veda.model.api.security.v1_0.UserInfo;
import com.taksila.veda.model.api.security.v1_0.UserLoginResponse;
import com.taksila.veda.utils.CommonUtils;
import com.taksila.veda.utils.ValidationUtils;


public class UserManagementComponent 
{	
	private String tenantId =null;	
	private UserManagementDAO userManagementDAO = null;
	private UserSessionDAO userSessionDAO = null;
	private String dateFormat = "MM/dd/yyyy HH:mm:ss z";
	static Logger logger = LogManager.getLogger(UserManagementComponent.class.getName());
	
	public UserManagementComponent(String tenantId) 
	{
		this.tenantId = tenantId;
		this.userManagementDAO = new UserManagementDAO(tenantId);
		this.userSessionDAO = new UserSessionDAO(tenantId);
		
	}
	
	public BaseResponse createUserAccount(UserInfo UserInfo,String password) 
	{
		BaseResponse baseResponse = new BaseResponse();
		try 
		{
			userManagementDAO.createNewUser(UserInfo);				
				
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			baseResponse.setStatus(StatusType.FAILED);
			baseResponse.setErrorInfo(CommonUtils.buildErrorInfo("ERROR", e.getMessage()));
		}
		return baseResponse;
	}
	
	public BaseResponse updateUserAccount(UserInfo UserInfo)
	{
		BaseResponse baseResponse = new BaseResponse();
		try 
		{
			userManagementDAO.updateUser(UserInfo);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			baseResponse.setStatus(StatusType.EXCEPTION);
			baseResponse.setErrorInfo(CommonUtils.buildErrorInfo("EXCEPTION", e.getMessage()));
		}
		return baseResponse;
	}
	
	
	public BaseResponse updateUserProfileImage(String userId,byte[] photo)
	{
		BaseResponse baseResponse = new BaseResponse();
		try 
		{
			userManagementDAO.updateUserImage(userId,photo);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			baseResponse.setStatus(StatusType.EXCEPTION);
			baseResponse.setErrorInfo(CommonUtils.buildErrorInfo("ERROR", e.getMessage()));
		}
		return baseResponse;
	}
	
	public SearchUsersResponse getAllUsers(SearchUsersRequest req)
	{
		SearchUsersResponse searchResp = new SearchUsersResponse();
		List<UserInfo> users;
		try 
		{
			users = userManagementDAO.getAllUsers();
			searchResp.getUsers().addAll(users);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			searchResp.setStatus(StatusType.FAILED);
			searchResp.setErrorInfo(CommonUtils.buildErrorInfo("ERROR", e.getMessage()));
		}
		return searchResp;
	}
	
	public GetUserInfoResponse getUserById(GetUserInfoRequest userInfoRequest)
	{
		GetUserInfoResponse resp = new GetUserInfoResponse();
		try 
		{
			UserInfo userInfo = userManagementDAO.getUserById(userInfoRequest.getUserId());
			resp.setUserInfo(userInfo);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			resp.setStatus(StatusType.FAILED);
			resp.setErrorInfo(CommonUtils.buildErrorInfo("ERROR", e.getMessage()));
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
	 * @param userId
	 * @return
	 */
	public BaseResponse deleteUser(String userId)
	{
		BaseResponse baseResponse = new BaseResponse();
		try 
		{
			userManagementDAO.deleteUser(userId);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			baseResponse.setStatus(StatusType.FAILED);
			baseResponse.setErrorInfo(CommonUtils.buildErrorInfo("ERROR", e.getMessage()));
		}
		return baseResponse;
	}
	
	
	
	
	
}
