package com.taksila.veda.eventschedulemgmt;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.db.dao.EventScheduleDAO;
import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.model.api.classroom.v1_0.Enrollment;
import com.taksila.veda.model.api.event_schedule_mgmt.v1_0.CreateEventScheduleRequest;
import com.taksila.veda.model.api.event_schedule_mgmt.v1_0.CreateEventScheduleResponse;
import com.taksila.veda.model.api.event_schedule_mgmt.v1_0.DeleteEventScheduleRequest;
import com.taksila.veda.model.api.event_schedule_mgmt.v1_0.DeleteEventScheduleResponse;
import com.taksila.veda.model.api.event_schedule_mgmt.v1_0.GetEventScheduleRequest;
import com.taksila.veda.model.api.event_schedule_mgmt.v1_0.GetEventScheduleResponse;
import com.taksila.veda.model.api.event_schedule_mgmt.v1_0.SearchEventScheduleRequest;
import com.taksila.veda.model.api.event_schedule_mgmt.v1_0.SearchEventScheduleResponse;
import com.taksila.veda.model.api.event_schedule_mgmt.v1_0.UpdateEventScheduleRequest;
import com.taksila.veda.model.api.event_schedule_mgmt.v1_0.UpdateEventScheduleResponse;
import com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventSchedule;
import com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventStatusType;
import com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventType;
import com.taksila.veda.utils.CommonUtils;


public class EventScheduleMgmtComponent 
{	
	private String schoolId =null;	
	private EventScheduleDAO eventScheduleDAO = null;
	static Logger logger = LogManager.getLogger(EventScheduleMgmtComponent.class.getName());
	
	public EventScheduleMgmtComponent(String tenantId) 
	{
		this.schoolId = tenantId;
		this.eventScheduleDAO = new EventScheduleDAO(tenantId);
			
	}
			
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public SearchEventScheduleResponse searchEventSchedule(SearchEventScheduleRequest req)
	{
		SearchEventScheduleResponse resp = new SearchEventScheduleResponse();
		try 
		{
			List<EventSchedule> eventScheduleSearchHits = eventScheduleDAO.searchEventScheduleByEventRecordId(req.getQuery());
			resp.getEventSchedule().addAll(eventScheduleSearchHits);
			resp.setStatus(StatusType.SUCCESS);
			resp.setSuccess(true);
		} 
		catch (Exception e) 
		{			
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public GetEventScheduleResponse getEventSchedule(GetEventScheduleRequest req)
	{
		GetEventScheduleResponse resp = new GetEventScheduleResponse();
		try 
		{
			EventSchedule eventSchedule = eventScheduleDAO.getEventScheduleById(req.getId());
			
			if (eventSchedule == null)
			{	
				resp.setMsg("Did not find any records with id = "+req.getId());
			}
			else
			{
//				eventSchedule.getEnrolledStudents().addAll(this.enrollmentDAO.searchStudentsByEventScheduleid(req.getId()));
				resp.setEventSchedule(eventSchedule);
			}					

		} 
		catch (Exception e) 
		{
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
	}
	
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public CreateEventScheduleResponse createNewEventSchedule(CreateEventScheduleRequest req)
	{
		CreateEventScheduleResponse resp = new CreateEventScheduleResponse();
		try 
		{							
			Boolean insertSuccess = eventScheduleDAO.insertEventSchedule(req.getEventSchedule());
			resp.setSuccess(true);
			resp.setEventSchedule(req.getEventSchedule());
			if (insertSuccess)
			{
				resp.setStatus(StatusType.SUCCESS);
				resp.setMsg("Successfully created a new event schedule id = "+resp.getEventSchedule().getId());
			}
			else
			{
				resp.setStatus(StatusType.FAILED);
				resp.setMsg("Updates to DB failed , please try again or contact support ");
				resp.setErrorInfo(CommonUtils.buildErrorInfo("FAILED", "Database was not updated! Please check your input"));
			}
		} 
		catch (Exception e) 
		{
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
		
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public UpdateEventScheduleResponse updateEventSchedule(UpdateEventScheduleRequest req)
	{
		UpdateEventScheduleResponse resp = new UpdateEventScheduleResponse();
		try 
		{
			//TODO validation
			
			boolean updateSucceded = eventScheduleDAO.updateEventSchedule(req.getEventSchedule());
			if (updateSucceded)
			{
				resp.setStatus(StatusType.SUCCESS);
				resp.setEventSchedule(req.getEventSchedule());
			}
			else
			{
				resp.setStatus(StatusType.FAILED);
				resp.setErrorInfo(CommonUtils.buildErrorInfo("FAILED", "Database was not updated! Please check your input"));
				resp.setMsg("Updates to DB failed , please try again or contact support ");

			}
			
		} 
		catch (Exception e) 
		{
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
		
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public DeleteEventScheduleResponse deleteEventSchedule(DeleteEventScheduleRequest req)
	{
		DeleteEventScheduleResponse resp = new DeleteEventScheduleResponse();
		try 
		{
			//TODO validation
			
			boolean updateSucceded = eventScheduleDAO.deleteEventSchedule(req.getId());
			if (updateSucceded)
			{
				resp.setStatus(StatusType.SUCCESS);
			}
			else
			{
				resp.setStatus(StatusType.FAILED);
				resp.setErrorInfo(CommonUtils.buildErrorInfo("FAILED", "Database was not updated! Please check your input"));
				resp.setMsg("Updates to DB failed , please try again or contact support ");
			}
			
		} 
		catch (Exception e) 
		{
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
		
	}
	
	
	public void mapFormFields(MultivaluedMap<String, String> formParams, EventSchedule eventSchedule) 
	{

		for (String key: formParams.keySet())
		{
			if (StringUtils.equals(key, "id"))
				eventSchedule.setId(formParams.getFirst("id"));
			
			if (StringUtils.equals(key, "eventDescription"))
				eventSchedule.setEventDescription(formParams.getFirst("eventDescription"));
			
			if (StringUtils.equals(key, "eventEndDate"))
				eventSchedule.setEventEndDate(CommonUtils.getXMLGregorianCalendarFromString(formParams.getFirst("eventEndDate"), "yyyy-MM-dd"));
			
			if (StringUtils.equals(key, "eventStartDate"))
				eventSchedule.setEventStartDate(CommonUtils.getXMLGregorianCalendarFromString(formParams.getFirst("eventStartDate"), "yyyy-MM-dd"));
			
			if (StringUtils.equals(key, "eventRecordId"))
				eventSchedule.setEventRecordId(formParams.getFirst("eventRecordId"));
			
			if (StringUtils.equals(key, "eventTitle"))
				eventSchedule.setEventTitle(formParams.getFirst("eventTitle"));
						
			if (StringUtils.equals(key, "eventStatus"))
				eventSchedule.setEventStatus(EventStatusType.fromValue(formParams.getFirst("eventStatus")));
			
			if (StringUtils.equals(key, "eventType"))
				eventSchedule.setEventType(EventType.fromValue(formParams.getFirst("eventType")));
			
						
		}
								
	}
	
	
}
