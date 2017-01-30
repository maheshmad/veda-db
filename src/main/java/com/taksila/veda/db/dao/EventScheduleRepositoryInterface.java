package com.taksila.veda.db.dao;

import java.util.List;

import com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventSchedule;

public interface EventScheduleRepositoryInterface {

	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	List<EventSchedule> searchEventScheduleById(String eventScheduleid) throws Exception;

	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	List<EventSchedule> searchEventScheduleByClassroomId(String classroomid) throws Exception;

	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	List<EventSchedule> searchEventScheduleByUserid(String userid) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	EventSchedule getEventScheduleById(String scheduleId) throws Exception;

	/**
	 * 
	 * @param eventSchedule
	 * @return
	 * @throws Exception
	 */
	EventSchedule insertEventSchedule(EventSchedule eventSchedule) throws Exception;

	/**
	 * 
	 * @param eventSchedule
	 * @return
	 * @throws Exception
	 */
	boolean updateEventSchedule(EventSchedule eventSchedule) throws Exception;

	/**
	 * 
	 * @param eventSchedule
	 * @return
	 * @throws Exception
	 */
	boolean updateEventScheduleSession(String scheduleId, String sessionid, String byUser) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean deleteEventSchedule(String id) throws Exception;
	/**
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	EventSchedule getEventScheduleBySessionId(String sessionId) throws Exception;

}