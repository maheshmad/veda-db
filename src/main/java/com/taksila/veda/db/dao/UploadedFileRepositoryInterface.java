package com.taksila.veda.db.dao;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import com.taksila.veda.model.api.course.v1_0.UploadedFile;

public interface UploadedFileRepositoryInterface {

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	UploadedFile getUploadedFileById(int id) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	List<UploadedFile> searchUploadedFileByTopicId(int topicid) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	UploadedFile getUploadedFileByName(String filename) throws Exception;

	/**
	 * 
	 * @param uploadedfile
	 * @return
	 * @throws Exception
	 */
	UploadedFile insertUploadedFile(UploadedFile uploadedfile, InputStream uploadedfileContentImageIs) throws Exception;

	/**
	 * 
	 * @param uploadedfile
	 * @return
	 * @throws Exception
	 */
	boolean updateUploadedFile(UploadedFile uploadedfile) throws Exception;

	/**
	 * 
	 * @param uploadedfile
	 * @return
	 * @throws Exception
	 */
	boolean updateUploadedFileContent(int uploadedfileId, String fileType, InputStream uploadedfileContentIs)
			throws Exception;

	/**
	 * 
	 * @param uploadedfile
	 * @return
	 * @throws Exception
	 */
	ByteArrayOutputStream readUploadedFileImage(int uploadedfileId) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean deleteUploadedFile(int id) throws Exception;

}