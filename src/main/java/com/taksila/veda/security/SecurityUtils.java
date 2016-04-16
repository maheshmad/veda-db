package com.taksila.veda.security;

import com.taksila.veda.model.db.usermgmt.v1_0.User;

public class SecurityUtils 
{
	public static User getLoggedInPrincipal(String tenantId, String sessionid)
	{
		UserAuthComponent userAuth = new UserAuthComponent(tenantId);
		if (userAuth.getLoggedInUser(sessionid) != null)
			return userAuth.getLoggedInUser(sessionid).getUserInfo();
		else
			return null;
			
	}
}
