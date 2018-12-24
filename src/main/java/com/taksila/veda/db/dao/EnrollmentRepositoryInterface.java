package com.taksila.veda.db.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import com.taksila.veda.model.api.classroom.v1_0.Enrollment;

public interface EnrollmentRepositoryInterface {

	public enum ENROLLMENT_TABLE
	{		
		id("id"),
		userRecordId("user_record_id"),
		classroomid("classroomid"),
		enrolledOn("enrolled_on"),
		verifiedBy("verified_by"),
		startDate("start_date"),
		endDate("end_date"),
		updatedBy("updated_by"),
		lastUpdatedOn("last_updated_on"),
		status("status");
		private String name;       
	    private ENROLLMENT_TABLE(String s) 
	    {
	        name = s;
	    }
		
	    public String value() 
	    {
	        return this.name;
	    }
		
	};
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
	 * @return Enrollment
	 * @throws Exception
	 */
	Enrollment insertEnrollment(Enrollment enrollment) throws Exception;

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
	
	/**
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 * @throws DatatypeConfigurationException
	 * @throws IOException
	 */
	Enrollment mapRow(ResultSet resultSet) throws SQLException, DatatypeConfigurationException, IOException;

	Enrollment getEnrollmentByUserAndClassroom(String userRecordId, String classroomId) throws Exception;

}