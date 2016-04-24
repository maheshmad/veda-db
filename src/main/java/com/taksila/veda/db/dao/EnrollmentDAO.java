package com.taksila.veda.db.dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.db.SQLDataBaseManager;
import com.taksila.veda.db.dao.ClassroomDAO.CLASSROOM_TABLE;
import com.taksila.veda.db.dao.ConfigDAO.CONFIG_GROUP_TABLE;
import com.taksila.veda.db.dao.ConfigDAO.CONFIG_SECTION_TABLE;
import com.taksila.veda.db.dao.ConfigDAO.CONFIG_TABLE;
import com.taksila.veda.db.dao.UsersDAO.USER_TABLE;
import com.taksila.veda.model.api.classroom.v1_0.Enrollment;
import com.taksila.veda.model.db.classroom.v1_0.EnrollmentStatusType;
import com.taksila.veda.utils.CommonUtils;

public class EnrollmentDAO 
{
	private String schoolId = null;	
	private static String insert_enrollment_sql = "INSERT INTO ENROLLMENTS("+ENROLLMENT_TABLE.id.value()+","+
																			ENROLLMENT_TABLE.classroomid.value()+","+
																			ENROLLMENT_TABLE.userid.value()+","+
																			ENROLLMENT_TABLE.enrolledOn.value()+","+
																			ENROLLMENT_TABLE.verifiedBy.value()+","+
																			ENROLLMENT_TABLE.startDate.value()+","+
																			ENROLLMENT_TABLE.endDate.value()+","+
																			ENROLLMENT_TABLE.updatedBy.value()+","+																			
																			ENROLLMENT_TABLE.status.value()+") "+
																	"VALUES (?,?,?,?,?,?,?,?,?);";		
	
	private static String update_enrollment_sql = "UPDATE ENROLLMENTS SET "+ENROLLMENT_TABLE.enrolledOn.value()+" = ? ,"+
																			ENROLLMENT_TABLE.verifiedBy.value()+" = ? ,"+
																			ENROLLMENT_TABLE.startDate.value()+" = ? ,"+
																			ENROLLMENT_TABLE.endDate.value()+" = ? ,"+
																			ENROLLMENT_TABLE.updatedBy.value()+" = ? ,"+																			
																			ENROLLMENT_TABLE.status.value()+" = ? "+
													" WHERE "+ENROLLMENT_TABLE.id.value()+" = ?";
	
	private static String delete_enrollment_sql = "DELETE FROM ENROLLMENTS WHERE "+ENROLLMENT_TABLE.id.value()+" = ?";
//	private static String search_enrollment_by_classroomid_sql = "SELECT * FROM ENROLLMENTS WHERE "+ENROLLMENT_TABLE.classroomid.value()+" = ? ";
//	private static String search_enrollment_by_id_sql = "SELECT * FROM ENROLLMENTS WHERE "+ENROLLMENT_TABLE.id.value()+" = ? ";
	private static String get_enrolled_students_sql =  	"	select * "+                
												    	"		from enrollments as e "+ 
												        "       join users as u "+ 
												        "		on e."+ENROLLMENT_TABLE.userid.value()+" = u."+USER_TABLE.userid.value()+
												        "       where u."+USER_TABLE.roles.value()+" = 'STUDENT' "+
												        "       and e."+ENROLLMENT_TABLE.classroomid.value()+" = ? ";
	
	private static String get_enrolled_classes_sql =  	"	select * "+                
												    	"		from enrollments as e "+ 
												        "       join classroom as cl "+ 
												        "		on e."+ENROLLMENT_TABLE.classroomid.value()+" = cl."+CLASSROOM_TABLE.id.value()+												        
												        "       where e."+ENROLLMENT_TABLE.userid.value()+" = ? ";
												        
	private static String get_enrollment_by_id = "	select * "+                
											    	"	from enrollments as e, "+
											    	" classroom as c , users as u "+ 											        
													" where e."+ENROLLMENT_TABLE.classroomid.value()+" = c."+CLASSROOM_TABLE.id.value()+
													" and e."+ENROLLMENT_TABLE.userid.value()+" = u."+USER_TABLE.userid.value()+
													" and e."+ENROLLMENT_TABLE.id.value()+" = ?";
											       
	
	
	static Logger logger = LogManager.getLogger(EnrollmentDAO.class.getName());
	SQLDataBaseManager sqlDBManager= null;
	
	public EnrollmentDAO(String tenantId) 
	{
		logger.trace(" Initializing EnrollmentsDAO............ ");
		this.schoolId = tenantId;		
		
		this.sqlDBManager = new SQLDataBaseManager();
		logger.trace(" Completed initializing EnrollmentsDAO............ ");
		
	}
	
	private enum ENROLLMENT_TABLE
	{		
		id("id"),
		userid("userid"),
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
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 * @throws DatatypeConfigurationException
	 * @throws IOException 
	 */
	private static Enrollment mapRow(ResultSet resultSet) throws SQLException, DatatypeConfigurationException, IOException 
	{
		Enrollment enrollment = new Enrollment();		
		
		enrollment.setId(resultSet.getString(ENROLLMENT_TABLE.id.value()));
		enrollment.setClassroomid(resultSet.getString(ENROLLMENT_TABLE.classroomid.value()));
		enrollment.setEndDate(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(ENROLLMENT_TABLE.endDate.value())));
		enrollment.setEnrolledOn(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(ENROLLMENT_TABLE.enrolledOn.value())));
		enrollment.setLastUpdatedDateTime(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(ENROLLMENT_TABLE.lastUpdatedOn.value())));
		enrollment.setStartDate(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(ENROLLMENT_TABLE.startDate.value())));
		enrollment.setUpdatedBy(resultSet.getString(ENROLLMENT_TABLE.updatedBy.value()));
		enrollment.setUserId(resultSet.getString(ENROLLMENT_TABLE.userid.value()));
		enrollment.setVerifiedBy(resultSet.getString(ENROLLMENT_TABLE.verifiedBy.value()));
		if (resultSet.getString(ENROLLMENT_TABLE.status.value()) != null)
			enrollment.setEnrollStatus(EnrollmentStatusType.fromValue(resultSet.getString(ENROLLMENT_TABLE.status.value())));
				
		
		return enrollment;
	}
	
	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	public List<Enrollment> searchEnrollmentsByClassroomId(String classroomid) throws Exception
	{
		List<Enrollment> enrollmentHits = new ArrayList<Enrollment>();				
		PreparedStatement stmt = null;		
		logger.trace("searching enrollments by classroomid ="+classroomid);

		try
		{
			this.sqlDBManager.connect();			
			stmt = this.sqlDBManager.getPreparedStatement(get_enrolled_students_sql);
			stmt.setString(1, classroomid);
			
			ResultSet resultSet = stmt.executeQuery();	
			while (resultSet.next()) 
			{
				Enrollment enroll = mapRow(resultSet);
				enrollmentHits.add(enroll);
				enroll.setStudent(UsersDAO.mapRow(resultSet));				
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		finally
		{
			this.sqlDBManager.close(stmt);
		}
		
		return enrollmentHits;
		
	}
	
	
	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	public List<Enrollment> searchEnrollmentsByUserId(String userid) throws Exception
	{
		List<Enrollment> enrollmentHits = new ArrayList<Enrollment>();				
		PreparedStatement stmt = null;		
		logger.trace("searching enrollments by userid ="+userid+ "sql = "+get_enrolled_classes_sql);

		try
		{
			this.sqlDBManager.connect();			
			stmt = this.sqlDBManager.getPreparedStatement(get_enrolled_classes_sql);
			stmt.setString(1, userid);
			
			ResultSet resultSet = stmt.executeQuery();	
			while (resultSet.next()) 
			{
				Enrollment enroll = mapRow(resultSet);
				enrollmentHits.add(enroll);
				enroll.setClassroom(ClassroomDAO.mapRow(resultSet));				
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		finally
		{
			this.sqlDBManager.close(stmt);
		}
		
		return enrollmentHits;
		
	}
	
	
//	/**
//	 * 
//	 * @param q
//	 * @return
//	 * @throws SQLException
//	 * @throws NamingException 
//	 */
//	public List<Enrollment> searchEnrollmentsByTitle(String q) throws SQLException, NamingException
//	{
//		List<Enrollment> enrollmentHits = new ArrayList<Enrollment>();				
//		PreparedStatement stmt = null;		
//		logger.trace("searching enrollments query ="+q);
//
//		try
//		{
//			this.sqlDBManager.connect();
//			
//			if (StringUtils.isNotBlank(q))
//			{
//				stmt = this.sqlDBManager.getPreparedStatement(search_enrollment_by_title_sql);
//				stmt.setString(1, q+"%");
//			}
//			else
//				stmt = this.sqlDBManager.getPreparedStatement(search_all_enrollments_sql);
//			
//			ResultSet resultSet = stmt.executeQuery();	
//			while (resultSet.next()) 
//			{
//				enrollmentHits.add(mapRow(resultSet));
//			}
//		}
//		catch(Exception ex)
//		{
//			ex.printStackTrace();
//			throw ex;
//		}
//		finally
//		{
//			this.sqlDBManager.close(stmt);
//		}
//		
//		return enrollmentHits;
//		
//	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public Enrollment getEnrollmentById(String enrollid) throws Exception
	{						
		PreparedStatement stmt = null;	
		Enrollment enrollment = null;
		logger.trace("searching enrollments by id ="+enrollid);
		try
		{
			this.sqlDBManager.connect();			
			stmt = this.sqlDBManager.getPreparedStatement(get_enrollment_by_id);
			stmt.setString(1, enrollid);
			ResultSet resultSet = stmt.executeQuery();	
			if (resultSet.next()) 
			{
				enrollment = mapRow(resultSet);				
				enrollment.setStudent(UsersDAO.mapRow(resultSet));
				enrollment.setClassroom(ClassroomDAO.mapRow(resultSet));
			}
			
			return enrollment;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		finally
		{
			this.sqlDBManager.close(stmt);
		}
				
	}
	
		
	/**
	 * 
	 * @param enrollment
	 * @return
	 * @throws Exception
	 */	
	public Boolean insertEnrollment(Enrollment enrollment) throws Exception 
	{
		logger.debug("Entering into insertEnrollment():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(insert_enrollment_sql);
			
			stmt.setString(1, enrollment.getId());
			stmt.setString(2, enrollment.getClassroomid());
			stmt.setString(3, enrollment.getUserId());
			stmt.setDate(4, CommonUtils.geSQLDateTimestamp(enrollment.getEnrolledOn()));
			stmt.setString(5, enrollment.getVerifiedBy());
			stmt.setDate(6, CommonUtils.geSQLDateTimestamp(enrollment.getStartDate()));
			stmt.setDate(7, CommonUtils.geSQLDateTimestamp(enrollment.getEndDate()));
			stmt.setString(8, enrollment.getUpdatedBy());
			if (enrollment.getEnrollStatus() != null)
				stmt.setString(9, enrollment.getEnrollStatus().value());
			else
				stmt.setString(9, null);
									
			int t = stmt.executeUpdate();
			if (t > 0)
				return true;
			else
				return false;
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
			throw ex;
		}
		finally
		{
			this.sqlDBManager.close(stmt);
		}				 
								
	}
	
	
	/**
	 * 
	 * @param enrollment
	 * @return
	 * @throws Exception
	 */	
	public boolean updateEnrollment(Enrollment enrollment) throws Exception 
	{
		logger.debug("Entering into updateEnrollment():::::");		
		PreparedStatement stmt = null;
		try
		{
			this.sqlDBManager.connect();	
			stmt = this.sqlDBManager.getPreparedStatement(update_enrollment_sql);			
			
			stmt.setDate(1, CommonUtils.geSQLDateTimestamp(enrollment.getEnrolledOn()));
			stmt.setString(2, enrollment.getVerifiedBy());
			stmt.setDate(3, CommonUtils.geSQLDateTimestamp(enrollment.getStartDate()));
			stmt.setDate(4, CommonUtils.geSQLDateTimestamp(enrollment.getEndDate()));
			stmt.setString(5, enrollment.getUpdatedBy());
			if (enrollment.getEnrollStatus() != null)
				stmt.setString(6, enrollment.getEnrollStatus().value());
			else
				stmt.setString(6, null);
			stmt.setString(7, enrollment.getId());
			
			int t = stmt.executeUpdate();
			if (t > 0)
				return true;
			else
				return false;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
			throw ex;
		}
		finally
		{
			this.sqlDBManager.close(stmt);
		}
								
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEnrollment(String id) throws Exception 
	{
		logger.debug("Entering into deleteEnrollment():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(delete_enrollment_sql);
			stmt.setString(1, id);
			int t = stmt.executeUpdate();
			if (t > 0)
				return true;
			else
				return false;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
			throw ex;
		}
		finally
		{
			this.sqlDBManager.close(stmt);
		}
								
	}

}
