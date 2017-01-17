package com.taksila.veda.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import com.taksila.veda.model.api.course.v1_0.Chapter;

public interface ChapterRepositoryInterface {

	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	List<Chapter> searchChaptersByCourseId(String courseid) throws Exception;

	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	List<Chapter> searchChaptersByTitle(String q) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	Chapter getChapterById(String id) throws Exception;

	/**
	 * 
	 * @param chapter
	 * @return
	 * @throws Exception
	 */
	Chapter insertChapter(Chapter chapter) throws Exception;

	/**
	 * 
	 * @param chapter
	 * @return
	 * @throws Exception
	 */
	boolean updateChapter(Chapter chapter) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean deleteChapter(String id) throws Exception;
	
	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 * @throws DatatypeConfigurationException
	 */
	Chapter rowMapper(ResultSet rs)  throws SQLException, DatatypeConfigurationException;

}