package com.taksila.veda.db.dao;

import java.util.List;

import com.taksila.veda.model.api.classroom.v1_0.Classroom;

public interface ClassroomRepositoryInterface {

	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	List<Classroom> searchClassroomsByTitle(String q) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	Classroom getClassroomById(String id) throws Exception;

	/**
	 * 
	 * @param classroom
	 * @return
	 * @throws Exception
	 */
	Classroom insertClassroom(Classroom classroom) throws Exception;

	/**
	 * 
	 * @param classroom
	 * @return
	 * @throws Exception
	 */
	boolean updateClassroom(Classroom classroom) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean deleteClassroom(String id) throws Exception;

}