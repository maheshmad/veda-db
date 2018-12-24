///**
// * 
// */
//package com.taksila.veda.db.dao;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import javax.naming.NamingException;
//import javax.xml.datatype.DatatypeConfigurationException;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import com.taksila.veda.db.SQLDataBaseManager;
//import com.taksila.veda.model.db.classroom.v1_0.ClassSessionSchedule;
//import com.taksila.veda.model.db.classroom.v1_0.ClassSessionStatusType;
//import com.taksila.veda.utils.CommonUtils;
//
///**
// * @author mahesh
// *
// */	
//
//public class ClassSessionScheduleDAO 
//{
//	private String schoolId = null;	
//	private static String insert_class_session_schedule_sql = "INSERT INTO CLASS_SESSION_SCHEDULE("+CLASS_SESSION_SCHEDULE_TABLE.classroomid.value()+","+
//																			CLASS_SESSION_SCHEDULE_TABLE.sessionStartDatetime.value()+","+
//																			CLASS_SESSION_SCHEDULE_TABLE.sessionEndDatetime.value()+","+
//																			CLASS_SESSION_SCHEDULE_TABLE.classTeacherId.value()+","+
//																			CLASS_SESSION_SCHEDULE_TABLE.sessionCurrentStatus.value()+","+
//																			CLASS_SESSION_SCHEDULE_TABLE.lastUpdatedBy.value()+") "+
//																	"VALUES (?,?,?,?,?,?);";		
//	
//	private static String update_class_session_schedule_sql = "UPDATE CLASS_SESSION_SCHEDULE SET "+CLASS_SESSION_SCHEDULE_TABLE.classroomid.value()+" = ? ,"+
//															CLASS_SESSION_SCHEDULE_TABLE.sessionStartDatetime.value()+" = ? ,"+
//															CLASS_SESSION_SCHEDULE_TABLE.sessionEndDatetime.value()+" = ? ,"+
//															CLASS_SESSION_SCHEDULE_TABLE.classTeacherId.value()+" = ? ,"+
//															CLASS_SESSION_SCHEDULE_TABLE.sessionCurrentStatus.value()+" = ? "+
//															CLASS_SESSION_SCHEDULE_TABLE.lastUpdatedBy.value()+" = ? "+
//													" WHERE "+CLASS_SESSION_SCHEDULE_TABLE.id.value()+" = ? ";
//	
//	private static String delete_class_session_schedule_sql = "DELETE FROM CLASS_SESSION_SCHEDULE WHERE "+CLASS_SESSION_SCHEDULE_TABLE.id.value()+" = ? ";	
//		
//	private static String search_class_session_schedule_by_id_sql = "SELECT * FROM CLASS_SESSION_SCHEDULE WHERE "+CLASS_SESSION_SCHEDULE_TABLE.id.value()+" = ? ";
//	
//	private static String search_class_session_schedule_by_classroomid_sql = "SELECT * FROM CLASS_SESSION_SCHEDULE WHERE "+CLASS_SESSION_SCHEDULE_TABLE.classroomid.value()+" = ? ";
//	
//	
//	static Logger logger = LogManager.getLogger(ClassSessionScheduleDAO.class.getName());
//	SQLDataBaseManager sqlDBManager= null;
//	
//	public ClassSessionScheduleDAO(String tenantId) 
//	{
//		logger.trace(" Initializing ClassSessionScheduleDAO............ ");
//		this.schoolId = tenantId;		
//		
//		this.sqlDBManager = new SQLDataBaseManager();
//		logger.trace(" Completed initializing ClassSessionScheduleDAO............ ");
//		
//	}
//	
//	public enum CLASS_SESSION_SCHEDULE_TABLE
//	{
//		id("session_schedule_id"),
//		classroomid("classroomid"),
//		sessionStartDatetime("session_start_datetime"),
//		sessionEndDatetime("session_end_datetime"),		
//		classTeacherId("class_teacher_id"),
//		sessionCurrentStatus("session_current_status"),
//		lastUpdatedBy("last_updated_by"),	
//		lastUpdatedOn("last_updated_on");		
//		private String name;       
//	    private CLASS_SESSION_SCHEDULE_TABLE(String s) 
//	    {
//	        name = s;
//	    }
//		
//	    public String value() 
//	    {
//	        return this.name;
//	    }
//		
//	};
//			
//	
//	public static ClassSessionSchedule mapRow(ResultSet resultSet) throws SQLException 
//	{
//		ClassSessionSchedule classSessionSchedule = new ClassSessionSchedule();		
//		
//		try 
//		{
//			classSessionSchedule.setId(String.valueOf(resultSet.getInt(CLASS_SESSION_SCHEDULE_TABLE.id.value())));
//			classSessionSchedule.setClassroomid(String.valueOf(resultSet.getString(CLASS_SESSION_SCHEDULE_TABLE.classroomid.value())));
//			classSessionSchedule.setStartDate(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(CLASS_SESSION_SCHEDULE_TABLE.sessionStartDatetime.value())));
//			classSessionSchedule.setEndDate(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(CLASS_SESSION_SCHEDULE_TABLE.sessionEndDatetime.value())));
//			classSessionSchedule.setClassTeacherId(resultSet.getString(CLASS_SESSION_SCHEDULE_TABLE.classTeacherId.value()));
//			classSessionSchedule.setClassSessionStatus(ClassSessionStatusType.fromValue(resultSet.getString(CLASS_SESSION_SCHEDULE_TABLE.sessionCurrentStatus.value())));
//			classSessionSchedule.setUpdatedBy(resultSet.getString(CLASS_SESSION_SCHEDULE_TABLE.sessionCurrentStatus.value()));
//			classSessionSchedule.setLastUpdatedDateTime(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(CLASS_SESSION_SCHEDULE_TABLE.lastUpdatedOn.value())));
//		}
//		catch (DatatypeConfigurationException e) 
//		{		
//			e.printStackTrace();
//		}
//		
//		return classSessionSchedule;
//	}
//	
//	/**
//	 * 
//	 * @param id
//	 * @return
//	 * @throws SQLException
//	 * @throws NamingException 
//	 */
//	public ClassSessionSchedule getClassSessionScheduleByClassroomId(String classroomid) throws SQLException, NamingException
//	{						
//		PreparedStatement stmt = null;	
//		ClassSessionSchedule classSessionSchedule = null;
//		try
//		{
//			this.sqlDBManager.connect();
//			stmt = this.sqlDBManager.getPreparedStatement(search_class_session_schedule_by_classroomid_sql);
//			stmt.setInt(1, Integer.parseInt(classroomid));
//			ResultSet resultSet = stmt.executeQuery();	
//			if (resultSet.next()) 
//			{
//				classSessionSchedule = mapRow(resultSet);
//			}
//			
//			return classSessionSchedule;
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
//	}
//	
//	/**
//	 * 
//	 * @param id
//	 * @return
//	 * @throws SQLException
//	 * @throws NamingException 
//	 */
//	public ClassSessionSchedule getClassSessionScheduleById(String id) throws SQLException, NamingException
//	{						
//		PreparedStatement stmt = null;	
//		ClassSessionSchedule classSessionSchedule = null;
//		try
//		{
//			this.sqlDBManager.connect();
//			stmt = this.sqlDBManager.getPreparedStatement(search_class_session_schedule_by_id_sql);
//			stmt.setInt(1, Integer.parseInt(id));
//			ResultSet resultSet = stmt.executeQuery();	
//			if (resultSet.next()) 
//			{
//				classSessionSchedule = mapRow(resultSet);
//			}
//			
//			return classSessionSchedule;
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
//	}
//	
//		
//	/**
//	 * 
//	 * @param classroom
//	 * @return
//	 * @throws Exception
//	 */	
//	public ClassSessionSchedule insertClassSessionSchedule(ClassSessionSchedule classSessionSchedule) throws Exception 
//	{
//		logger.debug("Entering into insertClassSessionSchedule():::::");
//		this.sqlDBManager.connect();	
//		PreparedStatement stmt = null;
//		try
//		{
//			stmt = this.sqlDBManager.getPreparedStatement(insert_class_session_schedule_sql);
//			
//			stmt.setString(1, classSessionSchedule.getClassroomid());
//			stmt.setDate(2, CommonUtils.getSqlDateFromXMLGregorianCalendarDateTimestamp(classSessionSchedule.getStartDate()));
//			stmt.setDate(3, CommonUtils.getSqlDateFromXMLGregorianCalendarDateTimestamp(classSessionSchedule.getEndDate()));
//			stmt.setString(4, classSessionSchedule.getClassTeacherId());
//			stmt.setString(5, classSessionSchedule.getClassSessionStatus().value());
//			stmt.setString(6, classSessionSchedule.getUpdatedBy());
//			
//			stmt.executeUpdate();			
//			ResultSet rs = stmt.getGeneratedKeys();			
//			if (rs.next())
//			{
//				classSessionSchedule.setId(String.valueOf(rs.getInt(1)));
//			}
//			
//			return classSessionSchedule;
//			
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
//	}
//	
//	
//	/**
//	 * 
//	 * @param classroom
//	 * @return
//	 * @throws Exception
//	 */	
//	public boolean updateClassSessionSchedule(ClassSessionSchedule classSessionSchedule) throws Exception 
//	{
//		logger.debug("Entering into updateClassSessionSchedule():::::");		
//		PreparedStatement stmt = null;
//		try
//		{
//			this.sqlDBManager.connect();	
//			stmt = this.sqlDBManager.getPreparedStatement(update_class_session_schedule_sql);
//			
//			stmt.setString(1, classSessionSchedule.getClassroomid());
//			stmt.setDate(2, CommonUtils.getSqlDateFromXMLGregorianCalendarDateTimestamp(classSessionSchedule.getStartDate()));
//			stmt.setDate(3, CommonUtils.getSqlDateFromXMLGregorianCalendarDateTimestamp(classSessionSchedule.getEndDate()));
//			stmt.setString(4, classSessionSchedule.getClassTeacherId());
//			stmt.setString(5, classSessionSchedule.getClassSessionStatus().value());
//			stmt.setString(6, classSessionSchedule.getUpdatedBy());
//			stmt.setInt(7, Integer.valueOf(classSessionSchedule.getId()));
//			
//			int t = stmt.executeUpdate();
//			if (t > 0)
//				return true;
//			else
//				return false;
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
//	}
//	
//	/**
//	 * 
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean deleteClassSessionSchedule(String id) throws Exception 
//	{
//		logger.debug("Entering into deleteClassSessionSchedule():::::");
//		this.sqlDBManager.connect();	
//		PreparedStatement stmt = null;
//		try
//		{
//			stmt = this.sqlDBManager.getPreparedStatement(delete_class_session_schedule_sql);
//			stmt.setInt(1, Integer.parseInt(id));
//			int t = stmt.executeUpdate();
//			if (t > 0)
//				return true;
//			else
//				return false;
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
//	}
//	
//
//}
