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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import com.taksila.veda.db.utils.TenantDBManager;
import com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventSchedule;
import com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventStatusType;
import com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventType;
import com.taksila.veda.utils.CommonUtils;


@Repository
@Scope(value="prototype")
@Lazy(value = true)
public class EventScheduleDAO implements EventScheduleRepositoryInterface 
{
	@Autowired
	private TenantDBManager tenantDBManager;
	private String tenantId;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
    public EventScheduleDAO(@Value("tenantId") String tenantId)
    {
		logger.trace(" building for tenant id = "+tenantId);
		this.tenantId = tenantId;
    }
	
	private static String insert_eventSchedule_sql = "INSERT INTO EVENT_SCHEDULE("+																		
																			EVENT_SCHEDULE_TABLE.classroomId.value()+","+
																			EVENT_SCHEDULE_TABLE.startDatetime.value()+","+
																			EVENT_SCHEDULE_TABLE.endDatetime.value()+","+
																			EVENT_SCHEDULE_TABLE.eventTitle.value()+","+
																			EVENT_SCHEDULE_TABLE.eventDescription.value()+","+
																			EVENT_SCHEDULE_TABLE.updatedBy.value()+","+
																			EVENT_SCHEDULE_TABLE.eventType.value()+","+	
																			EVENT_SCHEDULE_TABLE.eventStatus.value()+","+	
																			EVENT_SCHEDULE_TABLE.eventSessionId.value()+") "+
																	"VALUES (?,?,?,?,?,?,?,?);";		
	
	private static String update_eventSchedule_sql = "UPDATE EVENT_SCHEDULE SET "+
																			EVENT_SCHEDULE_TABLE.classroomId.value()+" = ? ,"+
																			EVENT_SCHEDULE_TABLE.startDatetime.value()+" = ? ,"+
																			EVENT_SCHEDULE_TABLE.endDatetime.value()+" = ? ,"+
																			EVENT_SCHEDULE_TABLE.eventTitle.value()+" = ? ,"+
																			EVENT_SCHEDULE_TABLE.eventDescription.value()+" = ? ,"+	
																			EVENT_SCHEDULE_TABLE.lastUpdatedOn.value()+" = ? ,"+
																			EVENT_SCHEDULE_TABLE.updatedBy.value()+" = ? ,"+
																			EVENT_SCHEDULE_TABLE.eventType.value()+" = ? ,"+	
																			EVENT_SCHEDULE_TABLE.eventStatus.value()+" = ? "+
																			EVENT_SCHEDULE_TABLE.eventSessionId.value()+" = ? "+
													" WHERE "+EVENT_SCHEDULE_TABLE.id.value()+" = ?";
	
	private static String update_event_schedule_session_sql = "UPDATE EVENT_SCHEDULE SET "+																
																EVENT_SCHEDULE_TABLE.eventSessionId.value()+" = ? "+
																EVENT_SCHEDULE_TABLE.updatedBy.value()+" = ? "+
																EVENT_SCHEDULE_TABLE.lastUpdatedOn.value()+" = "+EVENT_SCHEDULE_TABLE.lastUpdatedOn.value()+", "+
																" WHERE "+EVENT_SCHEDULE_TABLE.id.value()+" = ?";
	
	private static String delete_eventSchedule_sql = "DELETE FROM EVENT_SCHEDULE WHERE "+EVENT_SCHEDULE_TABLE.id.value()+" = ?";
	private static String search_event_schedule_by_classroomid_sql = "SELECT * FROM EVENT_SCHEDULE  WHERE "+EVENT_SCHEDULE_TABLE.classroomId.value()+" = ?";	
	private static String search_event_schedule_by_id_sql = "SELECT * FROM EVENT_SCHEDULE  WHERE "+EVENT_SCHEDULE_TABLE.id.value()+" = ?";

	private static String get_event_schedule_by_user_enrollment = "SELECT * FROM event_schedule as ev " +
																	"where "+EVENT_SCHEDULE_TABLE.classroomId.value()+" in "+
																	"(select "+EnrollmentRepositoryInterface.ENROLLMENT_TABLE.classroomid.value()+" from enrollments as enroll " +
																	"where "+EnrollmentRepositoryInterface.ENROLLMENT_TABLE.userRecordId.value()+" = ?) " +
																	"order by ev.start_datetime";

	
	
//	private static String search_eventSchedule_by_eventrecordid_sql = "SELECT * FROM EVENT_SCHEDULE WHERE "+EVENT_SCHEDULE_TABLE.eventrecordid.value()+" = ? ";
//	private static String search_eventSchedule_by_id_sql = "SELECT * FROM EVENT_SCHEDULE WHERE "+EVENT_SCHEDULE_TABLE.id.value()+" = ? ";
//	private static String get_enrolled_students_sql =  	"	select * "+                
//												    	"		from eventSchedule as e "+ 
//												        "       join users as u "+ 
//												        "		on e."+EVENT_SCHEDULE_TABLE.userRecordId.value()+" = u."+USER_TABLE.id.value()+
//												        "       where u."+USER_TABLE.roles.value()+" = 'STUDENT' "+
//												        "       and e."+EVENT_SCHEDULE_TABLE.eventrecordid.value()+" = ? ";
	
//	private static String get_enrolled_classes_sql =  	"	select * "+                
//												    	"		from eventSchedules as e "+ 
//												        "       join classroom as cl "+ 
//												        "		on e."+EVENT_SCHEDULE_TABLE.eventrecordid.value()+" = cl."+CLASSROOM_TABLE.id.value()+												        
//												        "       where e."+EVENT_SCHEDULE_TABLE.userRecordId.value()+" = ? ";
												        
//	private static String get_eventSchedule_by_id = "	select * "+                
//											    	"	from eventSchedules as e, "+
//											    	" classroom as c , users as u "+ 											        
//													" where e."+EVENT_SCHEDULE_TABLE.eventrecordid.value()+" = c."+CLASSROOM_TABLE.id.value()+
//													" and e."+EVENT_SCHEDULE_TABLE.userRecordId.value()+" = u."+USER_TABLE.id.value()+
//													" and e."+EVENT_SCHEDULE_TABLE.id.value()+" = ?";
											       
	
	
	static Logger logger = LogManager.getLogger(EventScheduleDAO.class.getName());
	
	
	public enum EVENT_SCHEDULE_TABLE
	{		
		id("event_schedule_id"),
		classroomId("classroomid"),
		startDatetime("start_datetime"),
		endDatetime("end_datetime"),
		eventTitle("event_title"),
		eventDescription("event_description"),
		updatedBy("updated_by"),
		lastUpdatedOn("last_updated_on"),
		eventType("event_type"),
		eventStatus("event_status"),
		eventSessionId("event_session_id");
		private String name;       
	    private EVENT_SCHEDULE_TABLE(String s) 
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
	private static EventSchedule mapRow(ResultSet resultSet) throws SQLException, DatatypeConfigurationException, IOException 
	{
		EventSchedule eventSchedule = new EventSchedule();		
		
		eventSchedule.setId(String.valueOf(resultSet.getInt(EVENT_SCHEDULE_TABLE.id.value())));
//		eventSchedule.setEventRecordId(resultSet.getString(EVENT_SCHEDULE_TABLE.eventRecordId.value()));
		eventSchedule.setClassroomid(String.valueOf(resultSet.getInt(EVENT_SCHEDULE_TABLE.classroomId.value())));
		eventSchedule.setEventStartDate(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getTimestamp(EVENT_SCHEDULE_TABLE.startDatetime.value())));
		eventSchedule.setEventEndDate(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getTimestamp(EVENT_SCHEDULE_TABLE.endDatetime.value())));
		eventSchedule.setLastUpdatedDateTime(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getTimestamp(EVENT_SCHEDULE_TABLE.lastUpdatedOn.value())));
		eventSchedule.setUpdatedBy(resultSet.getString(EVENT_SCHEDULE_TABLE.updatedBy.value()));
		eventSchedule.setEventTitle(resultSet.getString(EVENT_SCHEDULE_TABLE.eventTitle.value()));
		eventSchedule.setEventDescription(resultSet.getString(EVENT_SCHEDULE_TABLE.eventDescription.value()));
		if (resultSet.getString(EVENT_SCHEDULE_TABLE.eventStatus.value()) != null)
			eventSchedule.setEventStatus(EventStatusType.fromValue(resultSet.getString(EVENT_SCHEDULE_TABLE.eventStatus.value())));
		if (resultSet.getString(EVENT_SCHEDULE_TABLE.eventType.value()) != null)
			eventSchedule.setEventType(EventType.fromValue(resultSet.getString(EVENT_SCHEDULE_TABLE.eventType.value())));
		
		eventSchedule.setEventSessionId(resultSet.getString(EVENT_SCHEDULE_TABLE.eventSessionId.value()));
		
		return eventSchedule;
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.EventScheduleRepositoryInterface#searchEventScheduleById(java.lang.String)
	 */
	@Override
	public List<EventSchedule> searchEventScheduleById(String eventScheduleid) throws Exception
	{
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		return jdbcTemplate.execute(search_event_schedule_by_id_sql,new PreparedStatementCallback<List<EventSchedule>>()
		{  
			    @Override  
			    public List<EventSchedule> doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			    	List<EventSchedule> hits = new ArrayList<EventSchedule>();
			    	try 
			        {
			    		stmt.setString(1, eventScheduleid);						
						ResultSet resultSet = stmt.executeQuery();	
						while (resultSet.next()) 
						{
							EventSchedule eventSche = mapRow(resultSet);
							hits.add(eventSche);
						}
					} 
			        catch (SQLException e) 
			        {					
						e.printStackTrace();
					} catch (DatatypeConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	
			    	return hits;
			    }  
		});
		
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.EventScheduleRepositoryInterface#searchEventScheduleByClassroomId(java.lang.String)
	 */
	@Override
	public List<EventSchedule> searchEventScheduleByClassroomId(String classroomid) throws Exception
	{
		logger.trace("searching eventSchedules by event record id ="+classroomid+ "sql = "+search_event_schedule_by_classroomid_sql);
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		return jdbcTemplate.execute(search_event_schedule_by_classroomid_sql,new PreparedStatementCallback<List<EventSchedule>>()
		{  
			    @Override  
			    public List<EventSchedule> doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			    	List<EventSchedule> hits = new ArrayList<EventSchedule>();
			    	try 
			        {
			    		stmt.setString(1, classroomid);
						
						ResultSet resultSet = stmt.executeQuery();	
						while (resultSet.next()) 
						{
							EventSchedule evntSche = mapRow(resultSet);
							hits.add(evntSche);
						}
					} 
			        catch (SQLException e) 
			        {					
						e.printStackTrace();
					} catch (DatatypeConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	
			    	return hits;
			    }  
		});
		
		
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.EventScheduleRepositoryInterface#searchEventScheduleByUserid(java.lang.String)
	 */
	@Override
	public List<EventSchedule> searchEventScheduleByUserid(String userid) throws Exception
	{
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		return jdbcTemplate.execute(get_event_schedule_by_user_enrollment,new PreparedStatementCallback<List<EventSchedule>>()
		{  
			    @Override  
			    public List<EventSchedule> doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			    	List<EventSchedule> hits = new ArrayList<EventSchedule>();
			    	try 
			        {
			    		stmt.setString(1, userid);
						
						ResultSet resultSet = stmt.executeQuery();	
						while (resultSet.next()) 
						{
							EventSchedule evntSche = mapRow(resultSet);
							hits.add(evntSche);
						}
					} 
			        catch (SQLException | DatatypeConfigurationException | IOException e) 
			        {					
						e.printStackTrace();
					}
			    	
			    	return hits;
			    }  
		});
		
		
	}

	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.EventScheduleRepositoryInterface#getEventScheduleById(java.lang.String)
	 */
	@Override
	public EventSchedule getEventScheduleById(String scheduleId) throws Exception
	{						
		logger.trace("searching eventSchedules by id ="+scheduleId+" sql = "+search_event_schedule_by_id_sql);
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		return jdbcTemplate.execute(search_event_schedule_by_id_sql,new PreparedStatementCallback<EventSchedule>()
		{  
			    @Override  
			    public EventSchedule doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
			    {  			              			    	
			    	stmt.setString(1, scheduleId);
					try 
					{
						ResultSet resultSet = stmt.executeQuery();	
						if (resultSet.next()) 
						{
							return mapRow(resultSet);							
						}
					} catch (DatatypeConfigurationException | IOException e) {
						e.printStackTrace();
					}
					
					return null;
			    }  
		});				
				
	}
	
		
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.EventScheduleRepositoryInterface#insertEventSchedule(com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventSchedule)
	 */	
	@Override
	public EventSchedule insertEventSchedule(EventSchedule eventSchedule) throws Exception 
	{
		logger.debug("Entering into insertEventSchedule():::::");
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		return jdbcTemplate.execute(insert_eventSchedule_sql,new PreparedStatementCallback<EventSchedule>()
		{  
			    @Override  
			    public EventSchedule doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
			    {  			              			    	
			    	try 
			    	{
						stmt.setInt(1, Integer.valueOf(eventSchedule.getClassroomid()));
						stmt.setTimestamp(2, CommonUtils.geSQLDateTimestamp(eventSchedule.getEventStartDate()));
						stmt.setTimestamp(3, CommonUtils.geSQLDateTimestamp(eventSchedule.getEventEndDate()));			
						stmt.setString(4, eventSchedule.getEventTitle());
						stmt.setString(5, eventSchedule.getEventDescription());			
						stmt.setString(6, eventSchedule.getUpdatedBy());
						
						if (eventSchedule.getEventType() != null)
							stmt.setString(7, eventSchedule.getEventType().value());
						else
							stmt.setString(7, null);
						
						if (eventSchedule.getEventStatus() != null)
							stmt.setString(8, eventSchedule.getEventStatus().value());
						else
							stmt.setString(8, null);
						
						stmt.executeUpdate();	
						ResultSet rs = stmt.getGeneratedKeys();			
						if (rs.next())
						{
							eventSchedule.setId(String.valueOf(rs.getInt(1)));
						}
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DatatypeConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					return eventSchedule;			
					
			    }  
		});
		
								
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.EventScheduleRepositoryInterface#updateEventSchedule(com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventSchedule)
	 */	
	@Override
	public boolean updateEventSchedule(EventSchedule eventSchedule) throws Exception 
	{
		logger.debug("Entering into updateEventSchedule():::::");		
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		Boolean insertSuccess = jdbcTemplate.execute(update_eventSchedule_sql,new PreparedStatementCallback<Boolean>()
		{  
			    @Override  
			    public Boolean doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			        try 
			        {
			        	stmt.setInt(1, Integer.valueOf(eventSchedule.getClassroomid()));
						stmt.setTimestamp(2, CommonUtils.geSQLDateTimestamp(eventSchedule.getEventStartDate()));
						stmt.setTimestamp(3, CommonUtils.geSQLDateTimestamp(eventSchedule.getEventEndDate()));			
						stmt.setString(4, eventSchedule.getEventTitle());
						stmt.setString(5, eventSchedule.getEventDescription());			
						stmt.setTimestamp(6, CommonUtils.geSQLDateTimestamp(CommonUtils.getXMLGregorianCalendarNow()));
						stmt.setString(7, eventSchedule.getUpdatedBy());
						if (eventSchedule.getEventType() != null)
							stmt.setString(8, eventSchedule.getEventType().value());
						else
							stmt.setString(8, null);
						
						if (eventSchedule.getEventStatus() != null)
							stmt.setString(9, eventSchedule.getEventStatus().value());
						else
							stmt.setString(9, null);
						
						stmt.setInt(10, Integer.parseInt(eventSchedule.getId()));
						
						int t = stmt.executeUpdate();
						if (t > 0)
							return true;
						else
							return false;
					} 
			        catch (SQLException | DatatypeConfigurationException e) 
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
	 * @see com.taksila.veda.db.dao.EventScheduleRepositoryInterface#updateEventScheduleSession(java.lang.String, java.lang.String, java.lang.String)
	 */	
	@Override
	public boolean updateEventScheduleSession(String scheduleId, String sessionid, String byUser) throws Exception 
	{
		logger.debug("Entering into updateEventScheduleSession():::::");		
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		Boolean insertSuccess = jdbcTemplate.execute(update_eventSchedule_sql,new PreparedStatementCallback<Boolean>()
		{  
			    @Override  
			    public Boolean doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			        try 
			        {						
						stmt.setString(1, sessionid);			
						stmt.setString(2, byUser);
						stmt.setInt(3, Integer.parseInt(scheduleId));			
						
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
	 * @see com.taksila.veda.db.dao.EventScheduleRepositoryInterface#deleteEventSchedule(java.lang.String)
	 */
	@Override
	public boolean deleteEventSchedule(String id) throws Exception 
	{
		logger.debug("Entering into deleteEventSchedule():::::");
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		return jdbcTemplate.execute(delete_eventSchedule_sql,new PreparedStatementCallback<Boolean>()
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
