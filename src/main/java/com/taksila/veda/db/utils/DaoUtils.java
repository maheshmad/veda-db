package com.taksila.veda.db.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.model.db.base.v1_0.UserRole;

public class DaoUtils 
{		
	static Logger logger = LogManager.getLogger(DaoUtils.class.getName());
	public static List<UserRole> getUserRolesFromString(String userRoleString)
	{
		List<String> rolesStringList = Arrays.asList(userRoleString.split(","));
		List<UserRole> userRolesList = new ArrayList<UserRole>();
		
		try
		{
			for (String roleStr: rolesStringList)
			{
				if (!userRolesList.contains(roleStr)) /* skip duplicates */
					userRolesList.add(UserRole.fromValue(roleStr));
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
		}
		
		return userRolesList;

	}
	
	
	public static String getStringFromRolesList(List<UserRole> roles)
	{
		String userRolesString = "";
		try
		{
			for (UserRole role: roles)
			{
				userRolesString += role.value()+",";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
		}
		
		userRolesString = StringUtils.removeEnd(userRolesString, ",");
		
		return userRolesString;

	}
	
	
	public static List<UserRole> removeDuplicates(List<UserRole> originalList)
	{
//		List<UserRole> newList = new ArrayList<UserRole>();
//		for (UserRole userRole: originalList)
//		{			
//			boolean found = false;
//			logger.trace("size of new list check1 = "+newList.size());
//			for (UserRole newRole: newList)
//			{
//				logger.trace("check "+userRole.name()+ " with "+newRole.name());
//				if (StringUtils.equals(newRole.name(),userRole.name()))
//				{
//					found = true;
//					break;
//				}
//			}
//						
//			if (!found)
//			{
//				logger.trace("checking duplicate passed for role = "+userRole);
//				newList.add(userRole);
//				logger.trace("size of new list check2 = "+newList.size());
//			}
//		}
		
		EnumSet<UserRole> dedupe = EnumSet.copyOf(originalList);		
		List<UserRole> newList = new ArrayList<UserRole>(dedupe);
		
		return newList;
	}
	

}
