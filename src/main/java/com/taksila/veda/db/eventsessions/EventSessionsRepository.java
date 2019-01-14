package com.taksila.veda.db.eventsessions;

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

import com.taksila.veda.db.utils.DBCommonUtils;
import com.taksila.veda.db.utils.TenantDBManager;
import com.taksila.veda.model.db.event_session.v1_0.EventSession;

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
		logger.trace(" **************************************  building EventSessionsRepository for tenant id = "+tenantId);
//    	this.dbManager = applicationContext.getBean(TenantDBManager.class,tenantId);
		this.tenantId = tenantId;

    }
	
	@Override
	public Boolean save(EventSession newEventSession) throws Exception 
	{		
		logger.trace("about to save event session for tenantid = "+this.tenantId);
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		String insert_event_sessions_sql = "INSERT INTO event_sessions (event_session_id, user_record_id,is_presenter) "+																
														"VALUES (?,?,?) ON DUPLICATE KEY UPDATE event_session_id=event_session_id, user_record_id=user_record_id,is_presenter=is_presenter";		
			
		 Boolean insertSuccess = jdbcTemplate.execute(insert_event_sessions_sql,new PreparedStatementCallback<Boolean>()
		 {  
			    @Override  
			    public Boolean doInPreparedStatement(PreparedStatement ps)  			            
			    {  			              
			        try 
			        {
						ps.setString(1, newEventSession.getEventSessionId());  
						ps.setString(2,newEventSession.getUserRecordId()); 
						if (newEventSession.isPresenter())
							ps.setInt(3,1);
						else
							ps.setInt(3,0);
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
	public EventSession findByEventSessionById(String eventSessionsId) 
	{
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		String sql = "select * from event_sessions where event_session_id = ?";		
		logger.trace("****** about to run sql "+ sql);
		/*
		 * 
		 */
		
		return jdbcTemplate.execute(sql,new PreparedStatementCallback<EventSession>()
		{  
		    @Override  
		    public EventSession doInPreparedStatement(PreparedStatement ps)  			            
		    {  			              
		    	EventSession eventSession = new EventSession();
		    	try 
		        {
					ps.setString(1, eventSessionsId);  
					ResultSet rs = ps.executeQuery();
					System.out.println("****** sql query result count = "+ rs.getFetchSize());
					if (rs.next()) 
					{
						eventSession = rowMapper(rs);
					}
					else
						return null;
					
				} 
		        catch (SQLException | DatatypeConfigurationException e) 
		        {					
					e.printStackTrace();
				}  
		    	
		    	return eventSession;
		    }  
		});  
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
	public EventSession findByUserRecordIdAndEventSessionsId(String eventSessionsId, String userRecordId) 
	{
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		String sql = "select * from event_sessions where event_session_id = ? and user_record_id = ?";		
		logger.trace("****** about to run sql "+ sql);
		/*
		 * 
		 */
		
		return jdbcTemplate.execute(sql,new PreparedStatementCallback<EventSession>()
		{  
		    @Override  
		    public EventSession doInPreparedStatement(PreparedStatement ps)  			            
		    {  			              
		    	EventSession eventSession = new EventSession();
		    	try 
		        {
					ps.setString(1, eventSessionsId);
					ps.setString(2, userRecordId);
					ResultSet rs = ps.executeQuery();
					System.out.println("****** sql query result count = "+ rs.getFetchSize());
					if (rs.next()) 
					{
						eventSession = rowMapper(rs);
					}
					else
						return null;
					
				} 
		        catch (SQLException | DatatypeConfigurationException e) 
		        {					
					e.printStackTrace();
				}  
		    	
		    	return eventSession;
		    }  
		});  
	}

	@Override
	public EventSession rowMapper(ResultSet rs) throws SQLException, DatatypeConfigurationException 
	{
		EventSession eventSessionEntity = new EventSession();
		logger.trace("****** Inside row mapper ");
		eventSessionEntity.setEventSessionId(rs.getString("event_session_id"));
		eventSessionEntity.setUserRecordId(rs.getString("user_record_id"));
		eventSessionEntity.setJoiningDateTime(DBCommonUtils.getXMLGregorianCalendarDateTimestamp(rs.getTimestamp(("joining_datetime"))));
		eventSessionEntity.setLeavingDateTime(DBCommonUtils.getXMLGregorianCalendarDateTimestamp(rs.getTimestamp(("leaving_datetime"))));
		eventSessionEntity.setPresenter(rs.getInt("is_presenter") == 1 ? true:false);
		
		return eventSessionEntity;
	}

	
}
