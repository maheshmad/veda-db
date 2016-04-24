package com.taksila.veda.security;

import javax.servlet.http.HttpServletRequest;

import com.taksila.veda.model.db.usermgmt.v1_0.User;
import com.taksila.veda.utils.CommonUtils;

public class SecurityUtils 
{
	public static User getLoggedInPrincipal(String tenantId, String sessionid)
	{
		if (sessionid == null)
		{
			UserAuthComponent userAuth = new UserAuthComponent(tenantId);
			if (userAuth.getLoggedInUser(sessionid) != null)
				return userAuth.getLoggedInUser(sessionid).getUserInfo();
			else
				return null;
		}
		else
			return null;
			
	}
	
	public static User getLoggedInPrincipal(String tenantId, HttpServletRequest request)
	{
		String usersessionid = CommonUtils.getCookie(UserAuthService.USER_AUTH_SESSION_COOKIE_NAME, request);
		return getLoggedInPrincipal(tenantId, usersessionid);
			
	}
	
	public static String getLoggedInPrincipalUserid(String tenantId, HttpServletRequest request)
	{
		User user = getLoggedInPrincipal(tenantId, request);
		if (user != null)
			return user.getUserId();
		else
			return "";
			
	}
	
}
