package com.taksila.veda.db.dao;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import com.taksila.veda.model.db.usermgmt.v1_0.UserImageInfo;

public interface UserImagesRepositoryInterface {

	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	List<UserImageInfo> search(UserImageInfo searchUserImage) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	UserImageInfo getUserImageInfoById(String imageid) throws Exception;

	/**
	 * 
	 * @param UserImageInfo
	 * @return
	 * @throws Exception
	 */
	UserImageInfo insertUserImageInfo(UserImageInfo UserImageInfo, InputStream imageLarge, InputStream imageThumb)
			throws Exception;

	/**
	 * 
	 * @param UserImageInfo
	 * @return
	 * @throws Exception
	 */
	ByteArrayOutputStream readUserImageContent(String imageId, double scale) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean deleteUserImageInfo(String imageId) throws Exception;

}