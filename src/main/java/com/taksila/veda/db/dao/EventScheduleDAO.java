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
import com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventSchedule;
import com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventStatusType;
import com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventType;
import com.taksila.veda.utils.CommonUtils;

public class EventScheduleDAO 
{
	private String schoolId = null;	
	
	private static String insert_eventSchedule_sql = "INSERT INTO EVENT_SCHEDULE("+
																			EVENT_SCHEDULE_TABLE.eventRecordId.value()+","+
																			EVENT_SCHEDULE_TABLE.startDatetime.value()+","+
																			EVENT_SCHEDULE_TABLE.endDatetime.value()+","+
																			EVENT_SCHEDULE_TABLE.eventTitle.value()+","+
																			EVENT_SCHEDULE_TABLE.eventDescription.value()+","+
																			EVENT_SCHEDULE_TABLE.updatedBy.value()+","+
																			EVENT_SCHEDULE_TABLE.eventType.value()+","+		
																			EVENT_SCHEDULE_TABLE.eventStatus.value()+") "+
																	"VALUES (?,?,?,?,?,?,?,?);";		
	
	private static String update_eventSchedule_sql = "UPDATE EVENT_SCHEDULE SET "+EVENT_SCHEDULE_TABLE.eventRecordId.value()+" = ? ,"+
																			EVENT_SCHEDULE_TABLE.startDatetime.value()+" = ? ,"+
																			EVENT_SCHEDULE_TABLE.endDatetime.value()+" = ? ,"+
																			EVENT_SCHEDULE_TABLE.eventTitle.value()+" = ? ,"+
																			EVENT_SCHEDULE_TABLE.eventDescription.value()+" = ? ,"+	
																			EVENT_SCHEDULE_TABLE.lastUpdatedOn.value()+" = ? ,"+
																			EVENT_SCHEDULE_TABLE.updatedBy.value()+" = ? ,"+
																			EVENT_SCHEDULE_TABLE.eventType.value()+" = ? ,"+	
																			EVENT_SCHEDULE_TABLE.eventStatus.value()+" = ? "+
													" WHERE "+EVENT_SCHEDULE_TABLE.id.value()+" = ?";
	
	private static String delete_eventSchedule_sql = "DELETE FROM EVENT_SCHEDULE WHERE "+EVENT_SCHEDULE_TABLE.id.value()+" = ?";
	private static String search_event_schedule_by_eventrecordid_sql = "SELECT * FROM EVENT_SCHEDULE  WHERE "+EVENT_SCHEDULE_TABLE.eventRecordId.value()+" = ?";
	private static String search_event_schedule_by_id_sql = "SELECT * FROM EVENT_SCHEDULE  WHERE "+EVENT_SCHEDULE_TABLE.id.value()+" = ?";

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
	SQLDataBaseManager sqlDBManager= null;
	
	public EventScheduleDAO(String tenantId) 
	{
		logger.trace(" Initializing EventScheduleDAO............ ");
		this.schoolId = tenantId;		
		
		this.sqlDBManager = new SQLDataBaseManager();
		logger.trace(" Completed initializing EventScheduleDAO............ ");
		
	}
	
	private enum EVENT_SCHEDULE_TABLE
	{		
		id("event_schedule_id"),
		eventRecordId("event_record_id"),
		startDatetime("start_datetime"),
		endDatetime("end_datetime"),
		eventTitle("event_title"),
		eventDescription("event_description"),
		updatedBy("updated_by"),
		lastUpdatedOn("last_updated_on"),
		eventType("event_type"),
		eventStatus("event_status");
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
		eventSchedule.setEventRecordId(resultSet.getString(EVENT_SCHEDULE_TABLE.eventRecordId.value()));
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
				
		
		return eventSchedule;
	}
	
	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	public List<EventSchedule> searchEventScheduleById(String eventScheduleid) throws Exception
	{
		List<EventSchedule> eventScheduleHits = new ArrayList<EventSchedule>();				
		PreparedStatement stmt = null;		
		logger.trace("searching eventSchedules by eventrecordid ="+eventScheduleid);

		try
		{
			this.sqlDBManager.connect();			
			stmt = this.sqlDBManager.getPreparedStatement(search_event_schedule_by_id_sql);
			stmt.setString(1, eventScheduleid);
			
			ResultSet resultSet = stmt.executeQuery();	
			while (resultSet.next()) 
			{
				EventSchedule enroll = mapRow(resultSet);
				eventScheduleHits.add(enroll);
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
		
		return eventScheduleHits;
		
	}
	
	
	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	public List<EventSchedule> searchEventScheduleByEventRecordId(String eventRecordId) throws Exception
	{
		List<EventSchedule> eventScheduleHits = new ArrayList<EventSchedule>();				
		PreparedStatement stmt = null;		
		logger.trace("searching eventSchedules by event record id ="+eventRecordId+ "sql = "+search_event_schedule_by_eventrecordid_sql);
 
		try
		{
			this.sqlDBManager.connect();			
			stmt = this.sqlDBManager.getPreparedStatement(search_event_schedule_by_eventrecordid_sql);
			stmt.setString(1, eventRecordId);
			
			ResultSet resultSet = stmt.executeQuery();	
			while (resultSet.next()) 
			{
				EventSchedule enroll = mapRow(resultSet);
				eventScheduleHits.add(enroll);
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
		
		return eventScheduleHits;
		
	}
	
	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public EventSchedule getEventScheduleById(String enrollid) throws Exception
	{						
		PreparedStatement stmt = null;	
		EventSchedule eventSchedule = null;
		logger.trace("searching eventSchedules by id ="+enrollid+" sql = "+search_event_schedule_by_id_sql);
		try
		{
			this.sqlDBManager.connect();			
			stmt = this.sqlDBManager.getPreparedStatement(search_event_schedule_by_id_sql);
			stmt.setString(1, enrollid);
			ResultSet resultSet = stmt.executeQuery();	
			if (resultSet.next()) 
			{
				eventSchedule = mapRow(resultSet);							
			}
			
			return eventSchedule;
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
	 * @param eventSchedule
	 * @return
	 * @throws Exception
	 */	
	public EventSchedule insertEventSchedule(EventSchedule eventSchedule) throws Exception 
	{
		logger.debug("Entering into insertEventSchedule():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(insert_eventSchedule_sql);
						
			stmt.setString(1, eventSchedule.getEventRecordId());			
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
			
			return eventSchedule;
			
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
	 * @param eventSchedule
	 * @return
	 * @throws Exception
	 */	
	public boolean updateEventSchedule(EventSchedule eventSchedule) throws Exception 
	{
		logger.debug("Entering into updateEventSchedule():::::");		
		PreparedStatement stmt = null;
		try
		{
			this.sqlDBManager.connect();	
			stmt = this.sqlDBManager.getPreparedStatement(update_eventSchedule_sql);			
			
			stmt.setString(1, eventSchedule.getEventRecordId());			
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
	public boolean deleteEventSchedule(String id) throws Exception 
	{
		logger.debug("Entering into deleteEventSchedule():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(delete_eventSchedule_sql);
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
