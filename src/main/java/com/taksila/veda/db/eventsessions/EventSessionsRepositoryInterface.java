package com.taksila.veda.db.eventsessions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import com.taksila.veda.model.db.event_session.v1_0.EventSession;

public interface EventSessionsRepositoryInterface  
{	
	Boolean save(EventSession newEventSession) throws Exception;
	Boolean update(EventSession newEventSession);
	Boolean delete(EventSession newEventSession);
	List<EventSession> findByEventSessionsId(String eventSessionsId);
	List<EventSession> findByUserRecordId(String userRecordId);
	EventSession findByUserRecordIdAndEventSessionsId(String eventSessionsId,String userRecordId);
	EventSession rowMapper(ResultSet rs)  throws SQLException, DatatypeConfigurationException;
}
