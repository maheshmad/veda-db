/**
 * 
 */
package com.taksila.veda.db.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.model.api.security.v1_0.UserInfo;


/**
 * @author Uma
 *
 */
public class UserManagementDAO 
{
	private String tenantId = null;

	static Logger logger = LogManager.getLogger(UserManagementDAO.class.getName());

	public UserManagementDAO(String tenantId) {
		
	}

	public boolean authenticate(String userid, String pwd) throws Exception 
	{
		return true;
	}
	
	public UserInfo getUserInfo(String userid)
	{
		
		return new UserInfo();
	}
	
	public boolean resetPassword(String userid, String Newpassword) 
	{
		return true;
	}

	public boolean createNewUser(UserInfo userInfo) 
	{
		
		return true;
	}

	public boolean updateUser(UserInfo userInfo) 
	{
		
		return true;
	}

	public boolean updateUserImage(String userId, byte[] photo) 
	{		
		return true;
	}

	public boolean deleteUser(String userId) 
	{
		
		return true;
	}

	public List<UserInfo> getAllUsers() 
	{
		try 
		{
			List<UserInfo> userList = new ArrayList<UserInfo>();
			return userList;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			
		}
		return null;
	}

	public UserInfo getUserById(String userId) 
	{
		
		return new UserInfo();
	}

	public boolean changePassword(String userId, String oldPassword, String newPassword) 
	{
		return true;	
	}

}
