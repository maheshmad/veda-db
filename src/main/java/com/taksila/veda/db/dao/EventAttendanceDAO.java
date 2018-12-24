/**
 * 
 */
package com.taksila.veda.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.taksila.veda.db.utils.DBCommonUtils;
import com.taksila.veda.db.utils.TenantDBManager;
import com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventAttendance;

/**
 * @author mahesh
 *
 */	
@Repository
@Scope(value="prototype")
@Lazy(value = true)
public class EventAttendanceDAO implements EventAttendanceRepositoryInterface 
{
	@Autowired
	private TenantDBManager tenantDBManager;
	private String tenantId;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
    public EventAttendanceDAO(@Value("tenantId") String tenantId)
    {
		logger.trace(" building for tenant id = "+tenantId);
		this.tenantId = tenantId;
    }
	
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
			classSessionAttendance.setStartDate(DBCommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(CLASS_SESSION_ATTENDANCE_TABLE.startDatetime.value())));
			classSessionAttendance.setEndDate(DBCommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(CLASS_SESSION_ATTENDANCE_TABLE.endDatetime.value())));
			classSessionAttendance.setUserRecordId(resultSet.getString(CLASS_SESSION_ATTENDANCE_TABLE.userRecordId.value()));			
		}
		catch (DatatypeConfigurationException e) 
		{		
			e.printStackTrace();
		}
		
		return classSessionAttendance;
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.EventAttendanceRepositoryInterface#searchEventAttendanceBySessionScheduleId(java.lang.String)
	 */
	@Override
	public List<EventAttendance> searchEventAttendanceBySessionScheduleId(String classroomid) throws Exception
	{						
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		
		return jdbcTemplate.execute(search_class_session_attendance_by_class_session_schedule_sql,new PreparedStatementCallback<List<EventAttendance>>()
		{  
			    @Override  
			    public List<EventAttendance> doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			    	List<EventAttendance> hits = new ArrayList<EventAttendance>();
			    	try 
			        {
			    		stmt.setInt(1, Integer.parseInt(classroomid));
						ResultSet resultSet = stmt.executeQuery();	
						while (resultSet.next()) 
						{
							hits.add(mapRow(resultSet));
						}
						
						return hits;
			        }
			        catch (SQLException e) 
			        {					
						e.printStackTrace();
					}
			    	
			    	return hits;
			    }  
		}); 				
				
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.EventAttendanceRepositoryInterface#getEventAttendanceById(java.lang.String)
	 */
	@Override
	public EventAttendance getEventAttendanceById(String id) throws Exception
	{						
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		return jdbcTemplate.execute(search_class_session_attendance_by_id_sql,new PreparedStatementCallback<EventAttendance>()
		{  
			    @Override  
			    public EventAttendance doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
			    {  			              			    	
			    	stmt.setInt(1, Integer.parseInt(id));
					ResultSet resultSet = stmt.executeQuery();	
					if (resultSet.next()) 
					{
						return mapRow(resultSet);
					}
					else
						return null;
					
			    }  
		});
						
	}
	
		
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.EventAttendanceRepositoryInterface#insertEventAttendance(com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventAttendance)
	 */	
	@Override
	public EventAttendance insertEventAttendance(EventAttendance classSessionAttendance) throws Exception 
	{
		logger.debug("Entering into insertEventAttendance():::::");
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		KeyHolder holder = new GeneratedKeyHolder();
		 
		jdbcTemplate.update(new PreparedStatementCreator() {           
		 
		    @Override
		    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException 
		    {
		        try 
		        {
					PreparedStatement stmt = connection.prepareStatement(insert_class_session_attendance_sql, Statement.RETURN_GENERATED_KEYS);
					stmt.setInt(1, Integer.valueOf(classSessionAttendance.getEventScheduleId()));
					stmt.setDate(2, DBCommonUtils.getSqlDateFromXMLGregorianCalendarDateTimestamp(classSessionAttendance.getStartDate()));
					stmt.setDate(3, DBCommonUtils.getSqlDateFromXMLGregorianCalendarDateTimestamp(classSessionAttendance.getEndDate()));
					stmt.setString(4, classSessionAttendance.getUserRecordId());	   
					return stmt;
				} 
		        catch (Exception e) 
		        {				
					e.printStackTrace();
					return null;
				}
		    }
			}, holder);
		
		classSessionAttendance.setId(holder.getKey().toString());
		return classSessionAttendance;
				
						 
								
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.EventAttendanceRepositoryInterface#updateEventAttendance(com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventAttendance)
	 */	
	@Override
	public boolean updateEventAttendance(EventAttendance classSessionAttendance) throws Exception 
	{
		logger.debug("Entering into updateEventAttendance():::::");		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		Boolean insertSuccess = jdbcTemplate.execute(update_class_session_attendance_sql,new PreparedStatementCallback<Boolean>()
		{  
		    @Override  
		    public Boolean doInPreparedStatement(PreparedStatement stmt)  			            
		    {  			              
		        try 
		        {
		        	stmt.setDate(1, DBCommonUtils.getSqlDateFromXMLGregorianCalendarDateTimestamp(classSessionAttendance.getStartDate()));
					stmt.setDate(2, DBCommonUtils.getSqlDateFromXMLGregorianCalendarDateTimestamp(classSessionAttendance.getEndDate()));
					stmt.setString(3, classSessionAttendance.getUserRecordId());			
					stmt.setInt(4, Integer.valueOf(classSessionAttendance.getId()));
					
					int t = stmt.executeUpdate();
					if (t > 0)
						return true;
					else
						return false;
				} 
		        catch (SQLException e) 
		        {					
					e.printStackTrace();
					return false;
				}  			              
		    }  
		});  
		
		if (!insertSuccess)
			throw new Exception("Unsuccessful in adding an entry into DB, please check logs");
		
		return insertSuccess;
		
								
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.EventAttendanceRepositoryInterface#deleteEventAttendance(java.lang.String)
	 */
	@Override
	public boolean deleteEventAttendance(String id) throws Exception 
	{
		logger.debug("Entering into deleteEventAttendance():::::");

		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		return jdbcTemplate.execute(delete_class_session_attendance_sql,new PreparedStatementCallback<Boolean>()
		{  
			    @Override  
			    public Boolean doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			        try 
			        {
			        	stmt.setInt(1, Integer.parseInt(id));
						int t = stmt.executeUpdate();
						if (t > 0)
							return true;
						else
							return false;
					} 
			        catch (SQLException e) 
			        {					
						e.printStackTrace();
						return false;
					}  			              
			    }  
		}); 				
								
	}
	

}
