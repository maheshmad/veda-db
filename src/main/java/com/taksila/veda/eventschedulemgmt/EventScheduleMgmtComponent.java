package com.taksila.veda.eventschedulemgmt;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.classroom.ClassroomComponent;
import com.taksila.veda.db.dao.EventScheduleDAO;
import com.taksila.veda.model.api.base.v1_0.Err;
import com.taksila.veda.model.api.base.v1_0.ErrorInfo;
import com.taksila.veda.model.api.base.v1_0.SearchHitRecord;
import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.model.api.classroom.v1_0.Classroom;
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
import com.taksila.veda.utils.ValidationUtils;


public class EventScheduleMgmtComponent 
{	
	private String schoolId =null;	
	private EventScheduleDAO eventScheduleDAO = null;
	private ClassroomComponent classroomComp = null;
	static Logger logger = LogManager.getLogger(EventScheduleMgmtComponent.class.getName());
	
	public EventScheduleMgmtComponent(String tenantId) 
	{
		this.schoolId = tenantId;
		this.eventScheduleDAO = new EventScheduleDAO(tenantId);
		this.classroomComp = new ClassroomComponent(tenantId);	
	}
			
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public SearchEventScheduleResponse searchEventScheduleByClassroom(SearchEventScheduleRequest req)
	{
		SearchEventScheduleResponse resp = new SearchEventScheduleResponse();
		
		try 
		{
			List<EventSchedule> eventScheduleSearchHits = eventScheduleDAO.searchEventScheduleByClassroomId(req.getClassroomid());
			
			for(EventSchedule event: eventScheduleSearchHits)
			{
				SearchHitRecord rec = new SearchHitRecord();
				/*
				 * map search hits
				 */
				rec.setRecordId(String.valueOf(event.getId()));
				rec.setRecordTitle(event.getEventTitle());
				rec.setRecordSubtitle(event.getEventDescription()+" Starts at :"+event.getEventStartDate()+" Ends at: "+event.getEventEndDate());					
				
				resp.getHits().add(rec);
			}
			
			resp.setRecordType("EVENTS_SCHEDULE");
			resp.setPage(req.getPage());
			resp.setPageOffset(req.getPageOffset());
			resp.setTotalHits(eventScheduleSearchHits.size());
		} 
		catch (Exception e) 
		{			
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
	}
	
//	/**
//	 * 
//	 * @param req
//	 * @return
//	 */
//	public SearchEventScheduleResponse searchEventScheduleByUser(SearchEventScheduleRequest req)
//	{
//		SearchEventScheduleResponse resp = new SearchEventScheduleResponse();
//		try 
//		{
//			List<EventSchedule> eventScheduleSearchHits = eventScheduleDAO.searchEventScheduleByClassroomId(req.getUserRecordId());
//			resp.getEventSchedule().addAll(eventScheduleSearchHits);
//			resp.setStatus(StatusType.SUCCESS);
//			resp.setSuccess(true);
//		} 
//		catch (Exception e) 
//		{			
//			CommonUtils.handleExceptionForResponse(resp, e);
//		}
//		return resp;
//	}
	
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
		EventSchedule eventScheudle = req.getEventSchedule();
		try 
		{							
			/*
			 * validate
			 */
			ErrorInfo errorInfo = this.validateEventSchedule(eventScheudle);
//			errs.add(this.isValidId(eventScheudle.getId(), true));

			if (errorInfo.getErrors() != null && !errorInfo.getErrors().isEmpty())
			{
				resp.setErrorInfo(errorInfo);
				return resp;
			}
			
			EventSchedule eventSche = eventScheduleDAO.insertEventSchedule(req.getEventSchedule());
			resp.setSuccess(true);			
			if (StringUtils.isNotBlank(eventSche.getId()))
			{
				resp.setEventSchedule(eventSche);
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
		EventSchedule eventScheudle = req.getEventSchedule();
		try 
		{
			/*
			 * validate
			 */
			ErrorInfo errorInfo = this.validateEventSchedule(eventScheudle);
			
			
			if (errorInfo.getErrors() != null && !errorInfo.getErrors().isEmpty())
			{
				resp.setErrorInfo(errorInfo);
				return resp;
			}
			
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
	
	/**
	 * 
	 * @param formParams
	 * @param id
	 * @param updatedByUser
	 * @return
	 */
	public UpdateEventScheduleResponse updateEventSchedule(MultivaluedMap<String, String> formParams, String id, String updatedByUser) 
	{
		UpdateEventScheduleResponse resp = new UpdateEventScheduleResponse();
		try 
		{
			/*
			 * validate
			 */
			EventSchedule currentEventSchedule = this.eventScheduleDAO.getEventScheduleById(id);
			if (currentEventSchedule == null)
			{
				resp.setErrorInfo(CommonUtils.buildErrorInfo("id", "Event was not found! Please check your input!"));
			}
			else
			{
				this.mapFormFields(formParams, currentEventSchedule);
				UpdateEventScheduleRequest req = new UpdateEventScheduleRequest();
				currentEventSchedule.setUpdatedBy(updatedByUser);
				currentEventSchedule.setLastUpdatedDateTime(CommonUtils.getXMLGregorianCalendarNow());
				req.setEventSchedule(currentEventSchedule);
				resp = this.updateEventSchedule(req);
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
	 * @param formParams
	 * @param eventSchedule
	 */
	public void mapFormFields(MultivaluedMap<String, String> formParams, EventSchedule eventSchedule) 
	{
		
		for (String key: formParams.keySet())
		{
			if (StringUtils.equals(key, "id"))
				eventSchedule.setId(formParams.getFirst("id"));
					
			if (StringUtils.equals(key, "classroomid"))
				eventSchedule.setClassroomid(formParams.getFirst("classroomid"));
			
			if (StringUtils.equals(key, "eventDescription"))
				eventSchedule.setEventDescription(formParams.getFirst("eventDescription"));
											
			if (StringUtils.equals(key, "eventEndDate"))
				eventSchedule.setEventEndDate(CommonUtils.getXMLGregorianCalendarFromString(formParams.getFirst("eventEndDate"), "yyyy-MM-dd HH:mm"));
			
			if (StringUtils.equals(key, "eventStartDate"))
				eventSchedule.setEventStartDate(CommonUtils.getXMLGregorianCalendarFromString(formParams.getFirst("eventStartDate"), "yyyy-MM-dd HH:mm"));
					
			if (StringUtils.equals(key, "eventTitle"))
				eventSchedule.setEventTitle(formParams.getFirst("eventTitle"));
						
			if (StringUtils.equals(key, "eventStatus"))
				eventSchedule.setEventStatus(EventStatusType.fromValue(formParams.getFirst("eventStatus")));
			
			if (StringUtils.equals(key, "eventType"))
				eventSchedule.setEventType(EventType.fromValue(formParams.getFirst("eventType")));
			
						
		}
								
	}
	
	
	private ErrorInfo validateEventSchedule(EventSchedule eventScheudle) 
	{
		ErrorInfo errorInfo = new ErrorInfo();
		List<Err> errs = new ArrayList<Err>();
		
		if (eventScheudle == null)
			errs.add(CommonUtils.buildErr("INVALID", "No event schedule found in the request!"));
		
		errs.add(this.isValidEventDesc(eventScheudle.getEventDescription(), false));
		errs.add(this.isValidEventEndDate(eventScheudle.getEventEndDate(), true));
		errs.add(this.isValidEventStartDate(eventScheudle.getEventStartDate(), true));
		errs.add(this.isValidEventStatus(eventScheudle.getEventStatus(), false));
		errs.add(this.isValidEventTitle(eventScheudle.getEventTitle(), true));
		errs.add(this.isValidEventType(eventScheudle.getEventType(), true));
		
		for (Err er: errs)
		{
			if (er != null)
				errorInfo.getErrors().add(er);
		}
		
		return errorInfo;
	}

	
	
	/**
	 * 
	 * @param eventType
	 * @param checkMandatory
	 * @return
	 */
	public Err isValidEventType(EventType eventType, boolean checkMandatory)
	{
		if (checkMandatory && eventType == null) return CommonUtils.buildErr("eventType", "is missing, Please provide a valid value");
		
		return null;
//		try
//		{
//			EventType.fromValue(eventType);
//			return null;
//		}
//		catch(Exception ex)
//		{
//			return CommonUtils.buildErr("eventType", eventType+" is not a valid input. Please provide a valid value");
//		}
						
	}
	
	
	public Err isValidEventStatus(EventStatusType eventStatusType, boolean checkMandatory)
	{
		if (checkMandatory && eventStatusType == null) 
			return CommonUtils.buildErr("eventStatus", "is missing, Please provide a valid value");
		else
			return null;
		
//		
//		try
//		{
//			EventStatusType.fromValue(eventStatusType);
//			return null;
//		}
//		catch(Exception ex)
//		{
//			return CommonUtils.buildErr("eventStatus", eventStatusType+" is not a valid input. Please provide a valid value");
//		}
		
	}
	
	
	public Err isValidEventTitle(String val, boolean checkMandatory)
	{
		if (checkMandatory && StringUtils.isBlank(val)) return CommonUtils.buildErr("eventTitle", "is missing, Please provide a valid value");
		
				
		return null;
		
	}
	
	/**
	 * 
	 * @param val
	 * @param checkMandatory
	 * @return
	 */
	public Err isValidEventStartDate(XMLGregorianCalendar val, boolean checkMandatory)
	{
		return ValidationUtils.isValidDate("eventStartDate", val, "yyyy-MM-dd", checkMandatory);
				
	}
	
	public Err isValidEventEndDate(XMLGregorianCalendar val, boolean checkMandatory)
	{
		return ValidationUtils.isValidDate("eventEndDate", val,"yyyy-MM-dd", checkMandatory);	
	}
	
	public Err isValidEventDesc(String val, boolean checkMandatory)
	{
		if (checkMandatory && StringUtils.isBlank(val)) return CommonUtils.buildErr("eventTitle", "is missing, Please provide a valid value");
		
		return null;
		
				
		
	}
	
	public Err isValidEventRecordId(String val, boolean checkMandatory)
	{
		
		if (checkMandatory && StringUtils.isBlank(val)) return CommonUtils.buildErr("eventTitle", "is missing, Please provide a valid value");
		
		return this.classroomComp.checkClassroomidExists(val);
		
		
	}
	
	public Err isValidId(String val, boolean checkMandatory)
	{
		if (checkMandatory && StringUtils.isBlank(val)) return CommonUtils.buildErr("id", "is missing, Please provide a valid value");
		
		try 
		{
			if (this.eventScheduleDAO.getEventScheduleById(val) == null)
			{
				return CommonUtils.buildErr("id", "Did not find any schedule with id = "+val);				
			}
			else;
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
			return CommonUtils.buildErr("classroomid", "Could not locate classroom = "+val+" due to db exception, reason :"+e.getMessage());
		}		
	
		return null;
		
	}
	
	
	
}
