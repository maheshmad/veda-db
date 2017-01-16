package com.taksila.veda.db.dao;

import com.taksila.veda.model.db.security.v1_0.UserSession;

public interface UserSessionRepositoryInterface {

	/**
	 * 
	 * @param userSession
	 * @return
	 * @throws Exception 
	 */
	boolean addSession(UserSession userSession) throws Exception;

	UserSession getValidSession(String sessionid) throws Exception;

	boolean invalidateUserSession(String sessionid) throws Exception;

}