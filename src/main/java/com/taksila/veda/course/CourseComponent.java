package com.taksila.veda.course;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.db.dao.CoursesDAO;
import com.taksila.veda.model.api.base.v1_0.SearchHitRecord;
import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.model.api.course.v1_0.Course;
import com.taksila.veda.model.api.course.v1_0.CreateNewCourseRequest;
import com.taksila.veda.model.api.course.v1_0.CreateNewCourseResponse;
import com.taksila.veda.model.api.course.v1_0.DeleteCourseRequest;
import com.taksila.veda.model.api.course.v1_0.DeleteCourseResponse;
import com.taksila.veda.model.api.course.v1_0.GetCourseInfoRequest;
import com.taksila.veda.model.api.course.v1_0.GetCourseInfoResponse;
import com.taksila.veda.model.api.course.v1_0.SearchCourseRequest;
import com.taksila.veda.model.api.course.v1_0.SearchCourseResponse;
import com.taksila.veda.model.api.course.v1_0.UpdateCourseRequest;
import com.taksila.veda.model.api.course.v1_0.UpdateCourseResponse;
import com.taksila.veda.utils.CommonUtils;


public class CourseComponent 
{	
	private String schoolId =null;	
	private CoursesDAO coursesDAO = null;
	static Logger logger = LogManager.getLogger(CourseComponent.class.getName());
	
	public CourseComponent(String tenantId) 
	{
		this.schoolId = tenantId;
		this.coursesDAO = new CoursesDAO(this.schoolId);				
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public SearchCourseResponse searchCourses(SearchCourseRequest req)
	{
		SearchCourseResponse resp = new SearchCourseResponse();
		try 
		{
			List<Course> courseSearchHits = coursesDAO.searchCoursesByTitle(req.getQuery());
			
			for(Course course: courseSearchHits)
			{
				SearchHitRecord rec = new SearchHitRecord();
				/*
				 * map search hits
				 */
				rec.setRecordId(String.valueOf(course.getId()));
				rec.setRecordTitle(course.getTitle());
				rec.setRecordSubtitle(course.getSubTitle());				
				
				resp.getHits().add(rec);
			}
			
			resp.setRecordType("COURSE");
			resp.setPage(req.getPage());
			resp.setPageOffset(req.getPageOffset());
			resp.setTotalHits(courseSearchHits.size());

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
	public GetCourseInfoResponse getCourse(GetCourseInfoRequest req)
	{
		GetCourseInfoResponse resp = new GetCourseInfoResponse();
		try 
		{
			Course course = coursesDAO.getCoursesById(req.getId());
			
			if (course == null)
			{	
				resp.setMsg("Did not find any records with id = "+req.getId());
			}
			else
			{
				resp.setCourse(course);
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
	public CreateNewCourseResponse createNewCourse(CreateNewCourseRequest req)
	{
		CreateNewCourseResponse resp = new CreateNewCourseResponse();
		try 
		{
			//TODO validation
			
			Course course = coursesDAO.insertCourse(req.getNewCourse());
			resp.setCourse(course);
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
	public UpdateCourseResponse updateCourse(UpdateCourseRequest req)
	{
		UpdateCourseResponse resp = new UpdateCourseResponse();
		try 
		{
			//TODO validation
			
			boolean updateSucceded = coursesDAO.updateCourse(req.getCourse());
			if (updateSucceded)
			{
				resp.setStatus(StatusType.SUCCESS);
				resp.setCourse(req.getCourse());
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
	public DeleteCourseResponse deleteCourse(DeleteCourseRequest req)
	{
		DeleteCourseResponse resp = new DeleteCourseResponse();
		try 
		{
			//TODO validation
			
			boolean updateSucceded = coursesDAO.deleteCourse(req.getId());
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
