package com.taksila.veda.db.eventsessions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;
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
import com.taksila.veda.model.db.event_session.v1_0.EventSession;
import com.taksila.veda.utils.CommonUtils;

/*
* @Column(name="event_session_id")
* private long eventSessionId;
* @Column(name="user_record_id")
* private String userRecordId;
* @Column(name="joining_datetime")
* private DateTime joiningDatetime;
* @Column(name="leaving_datetime")
* private DateTime leavingDatetime;
*/


@Repository
@Scope(value="prototype")
@Lazy(value = true)
public class EventSessionsRepository implements EventSessionsRepositoryInterface  
{
	@Autowired
	private TenantDBManager tenantDBManager;
	static Logger logger = LogManager.getLogger(EventSessionsRepository.class.getName());
	private String tenantId;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
    public EventSessionsRepository(@Value("tenantId") String tenantId)
    {
		logger.trace(" building EventSessionsRepository for tenant id = "+tenantId);
//    	this.dbManager = applicationContext.getBean(TenantDBManager.class,tenantId);
		this.tenantId = tenantId;

    }
	
	@Override
	public Boolean save(EventSession newEventSession) throws Exception 
	{		
		logger.trace("about to save event session for tenantid = "+this.tenantId);
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		String insert_event_sessions_sql = "INSERT INTO event_sessions (event_sessions_id, user_record_id) "+																
														"VALUES (?,?);";		
		
		newEventSession.setEventSessionId(UUID.randomUUID().toString());
		
		 Boolean insertSuccess = jdbcTemplate.execute(insert_event_sessions_sql,new PreparedStatementCallback<Boolean>()
		 {  
			    @Override  
			    public Boolean doInPreparedStatement(PreparedStatement ps)  			            
			    {  			              
			        try 
			        {
						ps.setString(1, newEventSession.getEventSessionId());  
						ps.setString(2,newEventSession.getUserRecordId());  						
						ps.execute();
						return true;
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

	@Override
	public Boolean update(EventSession newEventSession) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(EventSession newEventSession) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventSession> findByEventSessionsId(String eventSessionsId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventSession> findByUserRecordId(String userRecordId) 
	{
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		String sql = "select * from event_sessions where user_record_id = ?";		
		logger.trace("****** about to run sql "+ sql);
		/*
		 * 
		 */
		
		return jdbcTemplate.execute(sql,new PreparedStatementCallback<List<EventSession>>()
		{  
		    @Override  
		    public List<EventSession> doInPreparedStatement(PreparedStatement ps)  			            
		    {  			              
		    	List<EventSession> rsList = new ArrayList<EventSession>();
		    	try 
		        {
					ps.setString(1, userRecordId);  
					ResultSet rs = ps.executeQuery();
					System.out.println("****** sql query result count = "+ rs.getFetchSize());
					while (rs.next()) 
					{
						rsList.add(rowMapper(rs));
					}
					
				} 
		        catch (SQLException | DatatypeConfigurationException e) 
		        {					
					e.printStackTrace();
				}  
		    	
		    	return rsList;
		    }  
		});  
		
		
	}

	@Override
	public EventSession findByUserRecordIdAndEventSessionsId(String eventSessionsId, String userRecordId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventSession rowMapper(ResultSet rs) throws SQLException, DatatypeConfigurationException 
	{
		EventSession eventSessionEntity = new EventSession();
		logger.trace("****** Inside row mapper ");
		eventSessionEntity.setEventSessionId(rs.getString("event_sessions_id"));
		eventSessionEntity.setUserRecordId(rs.getString("user_record_id"));
		eventSessionEntity.setJoiningDateTime(CommonUtils.getXMLGregorianCalendarDateTimestamp(rs.getTimestamp(("joining_datetime"))));
		eventSessionEntity.setLeavingDateTime(CommonUtils.getXMLGregorianCalendarDateTimestamp(rs.getTimestamp(("leaving_datetime"))));
		
		return eventSessionEntity;
	}

	
	 	
}
