package com.taksila.veda.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.taksila.veda.model.api.course.v1_0.Course;

public interface CoursesRepositoryInterface {

	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	List<Course> searchCoursesByTitle(String q) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	Course getCoursesById(String id) throws Exception;

	/**
	 * 
	 * @param course
	 * @return
	 * @throws Exception
	 */
	Course insertCourse(Course course) throws Exception;

	/**
	 * 
	 * @param course
	 * @return
	 * @throws Exception
	 */
	boolean updateCourse(Course course) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean deleteCourse(String id) throws Exception;
	
	/**
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	Course mapRow(ResultSet resultSet) throws SQLException;

}