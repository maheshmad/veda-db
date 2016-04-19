package com.taksila.veda.classroom;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.db.dao.ClassroomDAO;
import com.taksila.veda.db.dao.EnrollmentDAO;
import com.taksila.veda.model.api.base.v1_0.SearchHitRecord;
import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.model.api.classroom.v1_0.Classroom;
import com.taksila.veda.model.api.classroom.v1_0.CreateClassroomRequest;
import com.taksila.veda.model.api.classroom.v1_0.CreateClassroomResponse;
import com.taksila.veda.model.api.classroom.v1_0.DeleteClassroomRequest;
import com.taksila.veda.model.api.classroom.v1_0.DeleteClassroomResponse;
import com.taksila.veda.model.api.classroom.v1_0.GetClassroomRequest;
import com.taksila.veda.model.api.classroom.v1_0.GetClassroomResponse;
import com.taksila.veda.model.api.classroom.v1_0.SearchClassroomRequest;
import com.taksila.veda.model.api.classroom.v1_0.SearchClassroomResponse;
import com.taksila.veda.model.api.classroom.v1_0.UpdateClassroomRequest;
import com.taksila.veda.model.api.classroom.v1_0.UpdateClassroomResponse;
import com.taksila.veda.utils.CommonUtils;


public class ClassroomComponent 
{	
	private String schoolId =null;	
	private EnrollmentDAO enrollmentDAO = null;
	private ClassroomDAO classroomDAO = null;
	static Logger logger = LogManager.getLogger(ClassroomComponent.class.getName());
	
	public ClassroomComponent(String tenantId) 
	{
		this.schoolId = tenantId;
		this.enrollmentDAO = new EnrollmentDAO(this.schoolId);	
		this.classroomDAO = new ClassroomDAO(this.schoolId);	
	}
			
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public SearchClassroomResponse searchClassroom(SearchClassroomRequest req)
	{
		SearchClassroomResponse resp = new SearchClassroomResponse();
		try 
		{
			List<Classroom> classroomSearchHits = classroomDAO.searchClassroomsByTitle(req.getQuery());
			
			for(Classroom classroom: classroomSearchHits)
			{
				SearchHitRecord rec = new SearchHitRecord();
				/*
				 * map search hits
				 */
				rec.setRecordId(String.valueOf(classroom.getId()));
				rec.setRecordTitle(classroom.getTitle());
				rec.setRecordSubtitle(classroom.getSubTitle());				
				
				resp.getHits().add(rec);
			}
			
			resp.setRecordType("CLASSROOM");
			resp.setPage(req.getPage());
			resp.setPageOffset(req.getPageOffset());
			resp.setTotalHits(classroomSearchHits.size());

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
	public GetClassroomResponse getClassroom(GetClassroomRequest req)
	{
		GetClassroomResponse resp = new GetClassroomResponse();
		try 
		{
			Classroom classroom = classroomDAO.getClassroomById(req.getId());
			
			if (classroom == null)
			{	
				resp.setMsg("Did not find any records with id = "+req.getId());
			}
			else
			{
//				classroom.getEnrolledStudents().addAll(this.enrollmentDAO.searchStudentsByClassroomid(req.getId()));
				resp.setClassroom(classroom);
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
	public CreateClassroomResponse createNewClassroom(CreateClassroomRequest req)
	{
		CreateClassroomResponse resp = new CreateClassroomResponse();
		try 
		{							
			Classroom course = classroomDAO.insertClassroom(req.getClassroom());
			resp.setClassroom(course);
			resp.setStatus(StatusType.SUCCESS);
			resp.setMsg("Successfully created a new classroom id = "+resp.getClassroom().getId());
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
	public UpdateClassroomResponse updateClassroom(UpdateClassroomRequest req)
	{
		UpdateClassroomResponse resp = new UpdateClassroomResponse();
		try 
		{
			//TODO validation
			
			boolean updateSucceded = classroomDAO.updateClassroom(req.getClassroom());
			if (updateSucceded)
			{
				resp.setStatus(StatusType.SUCCESS);
				resp.setClassroom(req.getClassroom());
			}
			else
			{
				resp.setStatus(StatusType.FAILED);
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
	public DeleteClassroomResponse deleteClassroom(DeleteClassroomRequest req)
	{
		DeleteClassroomResponse resp = new DeleteClassroomResponse();
		try 
		{
			//TODO validation
			
			boolean updateSucceded = classroomDAO.deleteClassroom(req.getId());
			if (updateSucceded)
			{
				resp.setStatus(StatusType.SUCCESS);
			}
			else
			{
				resp.setStatus(StatusType.FAILED);
				resp.setErrorInfo(CommonUtils.buildErrorInfo("FAILED", "Database was not updated! Please check your input"));
			}
			
		} 
		catch (Exception e) 
		{
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
		
	}
	
}
