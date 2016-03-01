/**
 * 
 */
package com.taksila.veda.db.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.model.api.security.v1_0.UserInfo;
import com.taksila.veda.model.db.security.v1_0.UserSessionTable;

/**
 * @author Uma
 *
 */
public class UserSessionDAO 
{
	private String tenantId = null;

	static Logger logger = LogManager.getLogger(UserSessionDAO.class.getName());

	public UserSessionDAO(String tenantId) 
	{
		this.tenantId = tenantId;
	}

	public boolean authorizeSession(String sessionid, String userid) throws Exception 
	{
		return true;
	}
		
	
	public boolean isValidSession(String sessionid, String userid)
	{
		return true;
	}
	
	
	public boolean invalidateUserSession(String sessionid) 
	{
		return true;	
	}

	public UserInfo getUserFromSession(String sessionid) 
	{		
		return null;
	}

}
