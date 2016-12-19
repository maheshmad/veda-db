/**
 * 
 */
package com.taksila.veda.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.db.SQLDataBaseManager;
import com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventAttendance;
import com.taksila.veda.utils.CommonUtils;

/**
 * @author mahesh
 *
 */	

public class EventAttendanceDAO 
{
	private String schoolId = null;	
	private static String insert_class_session_attendance_sql = "INSERT INTO EVENT_ATTENDANCE("+CLASS_SESSION_ATTENDANCE_TABLE.eventScheduleId.value()+","+
																			CLASS_SESSION_ATTENDANCE_TABLE.startDatetime.value()+","+
																			CLASS_SESSION_ATTENDANCE_TABLE.endDatetime.value()+","+
																			CLASS_SESSION_ATTENDANCE_TABLE.userRecordId.value()+") "+
																"VALUES (?,?,?,?);";		
	
	private static String update_class_session_attendance_sql = "UPDATE CLASS_SESSION_ATTENDANCE SET "+
															CLASS_SESSION_ATTENDANCE_TABLE.startDatetime.value()+" = ? ,"+
															CLASS_SESSION_ATTENDANCE_TABLE.endDatetime.value()+" = ? ,"+
															CLASS_SESSION_ATTENDANCE_TABLE.userRecordId.value()+" = ? ,"+
													" WHERE "+CLASS_SESSION_ATTENDANCE_TABLE.id.value()+" = ?";
	
	private static String delete_class_session_attendance_sql = "DELETE FROM CLASS_SESSION_ATTENDANCE WHERE "+CLASS_SESSION_ATTENDANCE_TABLE.id.value()+" = ? ";	
		
	private static String search_class_session_attendance_by_id_sql = "SELECT * FROM CLASS_SESSION_ATTENDANCE WHERE "+CLASS_SESSION_ATTENDANCE_TABLE.id.value()+" = ? ";
	
	private static String search_class_session_attendance_by_class_session_schedule_sql = "SELECT * FROM CLASS_SESSION_ATTENDANCE WHERE "+CLASS_SESSION_ATTENDANCE_TABLE.eventScheduleId.value()+" = ? ";
	
	
	static Logger logger = LogManager.getLogger(EventAttendanceDAO.class.getName());
	SQLDataBaseManager sqlDBManager= null;
	
	public EventAttendanceDAO(String tenantId) 
	{
		logger.trace(" Initializing EventAttendanceDAO............ ");
		this.schoolId = tenantId;		
		
		this.sqlDBManager = new SQLDataBaseManager();
		logger.trace(" Completed initializing EventAttendanceDAO............ ");
		
	}
	
	public enum CLASS_SESSION_ATTENDANCE_TABLE
	{
		id("event_attendance_id"),
		eventScheduleId("event_schedule_id"),
		userRecordId("user_record_id"),
		startDatetime("start_datetime"),
		endDatetime("end_datetime");						
				
		private String name;       
	    private CLASS_SESSION_ATTENDANCE_TABLE(String s) 
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
	 */
	public static EventAttendance mapRow(ResultSet resultSet) throws SQLException 
	{
		EventAttendance classSessionAttendance = new EventAttendance();		
		
		try 
		{
			classSessionAttendance.setId(String.valueOf(resultSet.getInt(CLASS_SESSION_ATTENDANCE_TABLE.id.value())));
			classSessionAttendance.setEventScheduleId(String.valueOf(resultSet.getInt(CLASS_SESSION_ATTENDANCE_TABLE.eventScheduleId.value())));
			classSessionAttendance.setStartDate(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(CLASS_SESSION_ATTENDANCE_TABLE.startDatetime.value())));
			classSessionAttendance.setEndDate(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(CLASS_SESSION_ATTENDANCE_TABLE.endDatetime.value())));
			classSessionAttendance.setUserRecordId(resultSet.getString(CLASS_SESSION_ATTENDANCE_TABLE.userRecordId.value()));			
		}
		catch (DatatypeConfigurationException e) 
		{		
			e.printStackTrace();
		}
		
		return classSessionAttendance;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws NamingException 
	 */
	public List<EventAttendance> searchEventAttendanceBySessionScheduleId(String classroomid) throws SQLException, NamingException
	{						
		List<EventAttendance> attendanceHits = new ArrayList<EventAttendance>();	
		PreparedStatement stmt = null;	
		try
		{
			this.sqlDBManager.connect();
			stmt = this.sqlDBManager.getPreparedStatement(search_class_session_attendance_by_class_session_schedule_sql);
			stmt.setInt(1, Integer.parseInt(classroomid));
			ResultSet resultSet = stmt.executeQuery();	
			while (resultSet.next()) 
			{
				attendanceHits.add(mapRow(resultSet));
			}
			
			return attendanceHits;
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
	 * @throws SQLException
	 * @throws NamingException 
	 */
	public EventAttendance getEventAttendanceById(String id) throws SQLException, NamingException
	{						
		
		PreparedStatement stmt = null;	
		EventAttendance classSessionAttendance = null;
		try
		{
			this.sqlDBManager.connect();
			stmt = this.sqlDBManager.getPreparedStatement(search_class_session_attendance_by_id_sql);
			stmt.setInt(1, Integer.parseInt(id));
			ResultSet resultSet = stmt.executeQuery();	
			if (resultSet.next()) 
			{
				classSessionAttendance = mapRow(resultSet);
			}
			
			return classSessionAttendance;
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
	 * @param classroom
	 * @return
	 * @throws Exception
	 */	
	public EventAttendance insertEventAttendance(EventAttendance classSessionAttendance) throws Exception 
	{
		logger.debug("Entering into insertEventAttendance():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(insert_class_session_attendance_sql);
			
			stmt.setInt(1, Integer.valueOf(classSessionAttendance.getEventScheduleId()));
			stmt.setDate(2, CommonUtils.getSqlDateFromXMLGregorianCalendarDateTimestamp(classSessionAttendance.getStartDate()));
			stmt.setDate(3, CommonUtils.getSqlDateFromXMLGregorianCalendarDateTimestamp(classSessionAttendance.getEndDate()));
			stmt.setString(4, classSessionAttendance.getUserRecordId());
			
			
			stmt.executeUpdate();			
			ResultSet rs = stmt.getGeneratedKeys();			
			while (rs.next())
			{
				classSessionAttendance.setId(String.valueOf(rs.getInt(1)));
			}
			
			return classSessionAttendance;
			
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
	 * @param classroom
	 * @return
	 * @throws Exception
	 */	
	public boolean updateEventAttendance(EventAttendance classSessionAttendance) throws Exception 
	{
		logger.debug("Entering into updateEventAttendance():::::");		
		PreparedStatement stmt = null;
		try
		{
			this.sqlDBManager.connect();	
			stmt = this.sqlDBManager.getPreparedStatement(update_class_session_attendance_sql);
			
			stmt.setDate(1, CommonUtils.getSqlDateFromXMLGregorianCalendarDateTimestamp(classSessionAttendance.getStartDate()));
			stmt.setDate(2, CommonUtils.getSqlDateFromXMLGregorianCalendarDateTimestamp(classSessionAttendance.getEndDate()));
			stmt.setString(3, classSessionAttendance.getUserRecordId());			
			stmt.setInt(4, Integer.valueOf(classSessionAttendance.getId()));
			
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
	public boolean deleteEventAttendance(String id) throws Exception 
	{
		logger.debug("Entering into deleteEventAttendance():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(delete_class_session_attendance_sql);
			stmt.setInt(1, Integer.parseInt(id));
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
