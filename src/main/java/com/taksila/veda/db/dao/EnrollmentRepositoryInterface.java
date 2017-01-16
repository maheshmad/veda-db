package com.taksila.veda.db.dao;

import java.util.List;

import com.taksila.veda.model.api.classroom.v1_0.Enrollment;

public interface EnrollmentRepositoryInterface {

	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	List<Enrollment> searchEnrollmentsByClassroomId(String classroomid) throws Exception;

	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	List<Enrollment> searchEnrollmentsByUserRecordId(String userRecordId) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	Enrollment getEnrollmentById(String enrollid) throws Exception;

	/**
	 * 
	 * @param enrollment
	 * @return
	 * @throws Exception
	 */
	Boolean insertEnrollment(Enrollment enrollment) throws Exception;

	/**
	 * 
	 * @param enrollment
	 * @return
	 * @throws Exception
	 */
	boolean updateEnrollment(Enrollment enrollment) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean deleteEnrollment(String id) throws Exception;

}