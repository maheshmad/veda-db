package com.taksila.veda.db.dao;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import com.taksila.veda.model.db.usermgmt.v1_0.User;

public interface UsersRepositoryInterface {

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	User getUserByUserRecordId(int id) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	User getUserByEmailId(String emailid) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	List<User> searchUsers(String name) throws Exception;

	/**
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	User insertUser(User user) throws Exception;

	/**
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	boolean updateUser(User user) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean deleteUser(String id) throws Exception;

	boolean updateUserImage(String userId, InputStream userContentImageIs, String imageType, double scale);

	ByteArrayOutputStream readUserImage(int userId, double scale);

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	User getUserByUserId(String userid) throws Exception;

	/*
	 * 
	 */
	User authenticate(String userid, String pswd) throws Exception;

	/**
	 * 
	 * @param userId
	 * @param passwordHash
	 * @return
	 * @throws Exception
	 */
	boolean updatePassword(String userId, String passwordHash, Boolean temporary) throws Exception;

}