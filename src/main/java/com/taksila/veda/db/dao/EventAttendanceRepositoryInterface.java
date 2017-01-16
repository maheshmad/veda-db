package com.taksila.veda.db.dao;

import java.util.List;

import com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventAttendance;

public interface EventAttendanceRepositoryInterface {

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	List<EventAttendance> searchEventAttendanceBySessionScheduleId(String classroomid) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	EventAttendance getEventAttendanceById(String id) throws Exception;

	/**
	 * 
	 * @param classroom
	 * @return
	 * @throws Exception
	 */
	EventAttendance insertEventAttendance(EventAttendance classSessionAttendance) throws Exception;

	/**
	 * 
	 * @param classroom
	 * @return
	 * @throws Exception
	 */
	boolean updateEventAttendance(EventAttendance classSessionAttendance) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean deleteEventAttendance(String id) throws Exception;

}