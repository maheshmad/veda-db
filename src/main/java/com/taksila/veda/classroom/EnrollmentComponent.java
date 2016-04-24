package com.taksila.veda.classroom;

import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.db.dao.ClassroomDAO;
import com.taksila.veda.db.dao.EnrollmentDAO;
import com.taksila.veda.db.dao.UsersDAO;
import com.taksila.veda.model.api.base.v1_0.ErrorInfo;
import com.taksila.veda.model.api.base.v1_0.SearchHitRecord;
import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.model.api.classroom.v1_0.Classroom;
import com.taksila.veda.model.api.classroom.v1_0.CreateEnrollmentRequest;
import com.taksila.veda.model.api.classroom.v1_0.CreateEnrollmentResponse;
import com.taksila.veda.model.api.classroom.v1_0.DeleteEnrollmentRequest;
import com.taksila.veda.model.api.classroom.v1_0.DeleteEnrollmentResponse;
import com.taksila.veda.model.api.classroom.v1_0.Enrollment;
import com.taksila.veda.model.api.classroom.v1_0.GetEnrollmentRequest;
import com.taksila.veda.model.api.classroom.v1_0.GetEnrollmentResponse;
import com.taksila.veda.model.api.classroom.v1_0.SearchEnrollmentRequest;
import com.taksila.veda.model.api.classroom.v1_0.SearchEnrollmentResponse;
import com.taksila.veda.model.api.classroom.v1_0.UpdateEnrollmentRequest;
import com.taksila.veda.model.api.classroom.v1_0.UpdateEnrollmentResponse;
import com.taksila.veda.model.db.usermgmt.v1_0.User;
import com.taksila.veda.utils.CommonUtils;


public class EnrollmentComponent 
{	
	private String schoolId =null;	
	private EnrollmentDAO enrollmentDAO = null;
	private UsersDAO usersDAO = null;
	private ClassroomDAO classroomDAO = null;
	static Logger logger = LogManager.getLogger(EnrollmentComponent.class.getName());
	
	public EnrollmentComponent(String tenantId) 
	{
		this.schoolId = tenantId;
		this.enrollmentDAO = new EnrollmentDAO(this.schoolId);
		this.classroomDAO = new ClassroomDAO(schoolId);
		this.usersDAO = new UsersDAO(this.schoolId);
	}
			
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public SearchEnrollmentResponse searchEnrollmentByClassroom(SearchEnrollmentRequest req)
	{
		SearchEnrollmentResponse resp = new SearchEnrollmentResponse();
		try 
		{
			List<Enrollment> enrollmentSearchHits = enrollmentDAO.searchEnrollmentsByClassroomId(req.getQuery());
			
			for(Enrollment enrollment: enrollmentSearchHits)
			{
				SearchHitRecord rec = new SearchHitRecord();
				String subtitle = "";					
				subtitle += "Status: "+(enrollment.getEnrollStatus() != null ? enrollment.getEnrollStatus().value() : "N/A");
				/*
				 * map search hits
				 */
				rec.setRecordId(String.valueOf(enrollment.getId()));				
				if (enrollment.getStudent() != null)
				{
					User student = enrollment.getStudent();					
					subtitle += ", Email: "+(student.getEmailId() != null ? student.getEmailId() : "N/A");
					subtitle += ", Cellphone: "+(student.getCellphone() != null ? student.getCellphone() : "N/A");					
											
					rec.setRecordTitle(student.getLastName()+", "+student.getFirstName());
					rec.setRecordId(student.getId());
					
				}
				rec.setRecordSubtitle(subtitle);				
				resp.getHits().add(rec);
			}
			
			resp.setRecordType("ENROLLMENT");
			resp.setPage(req.getPage());
			resp.setPageOffset(req.getPageOffset());
			resp.setTotalHits(enrollmentSearchHits.size());

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
	public SearchEnrollmentResponse searchEnrollmentByUserid(SearchEnrollmentRequest req)
	{
		SearchEnrollmentResponse resp = new SearchEnrollmentResponse();
		try 
		{
			List<Enrollment> enrollmentSearchHits = enrollmentDAO.searchEnrollmentsByUserId(req.getQuery());
			
			for(Enrollment enrollment: enrollmentSearchHits)
			{
				SearchHitRecord rec = new SearchHitRecord();
				String subtitle = "";					
				subtitle += "Status: "+(enrollment.getEnrollStatus() != null ? enrollment.getEnrollStatus().value() : "N/A");
				/*
				 * map search hits
				 */
				rec.setRecordId(String.valueOf(enrollment.getId()));				
				if (enrollment.getClassroom() != null)
				{
					Classroom classroom = (Classroom) enrollment.getClassroom();					
//					subtitle += ", Email: "+(student.getEmailId() != null ? student.getEmailId() : "N/A");
//					subtitle += ", Cellphone: "+(student.getCellphone() != null ? student.getCellphone() : "N/A");					//											
					rec.setRecordTitle(classroom.getTitle());
					rec.setRecordSubtitle(classroom.getTitle());
//					rec.setRecordId(classroom.getId());
					
				}
				rec.setRecordSubtitle(subtitle);				
				resp.getHits().add(rec);
			}
			
			resp.setRecordType("ENROLLMENT");
			resp.setPage(req.getPage());
			resp.setPageOffset(req.getPageOffset());
			resp.setTotalHits(enrollmentSearchHits.size());

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
	public GetEnrollmentResponse getEnrollment(GetEnrollmentRequest req)
	{
		GetEnrollmentResponse resp = new GetEnrollmentResponse();
		try 
		{
			Enrollment enrollment = enrollmentDAO.getEnrollmentById(req.getId());
			
			if (enrollment == null)
			{	
				resp.setMsg("Did not find any records with id = "+req.getId());
			}
			else
			{
				enrollment.setStudent(this.usersDAO.getUserByUserId(enrollment.getUserId()));
				resp.setEnrollment(enrollment);
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
	public CreateEnrollmentResponse createNewEnrollment(CreateEnrollmentRequest req)
	{
		CreateEnrollmentResponse resp = new CreateEnrollmentResponse();
		Enrollment enroll = req.getEnrollment();
		try 
		{							
			/*
			 * validate
			 */
			resp.setErrorInfo(this.validate(enroll));
						
			/*
			 * check response
			 */
			if (resp.getErrorInfo() == null)
			{										
				/*
				 * generate enrollment id 
				 */
				req.getEnrollment().setId(this.generateEnrollmentId(enroll.getUserId(), enroll.getClassroomid()));
				
				Boolean insertsuccess = enrollmentDAO.insertEnrollment(req.getEnrollment());	
				if (insertsuccess)
				{
					resp.setStatus(StatusType.SUCCESS);
					resp.setMsg("Successfully enrolled for user = "+req.getEnrollment().getUserId()+" in classroom "+req.getEnrollment().getClassroomid());
				}
				else
				{
					resp.setStatus(StatusType.FAILED);
					resp.setErrorInfo(CommonUtils.buildErrorInfo("FAILED", "Database was not updated! Please check your input"));
				}
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
	public UpdateEnrollmentResponse updateEnrollment(UpdateEnrollmentRequest req)
	{
		UpdateEnrollmentResponse resp = new UpdateEnrollmentResponse();
		Enrollment enroll = req.getEnrollment();
		try 
		{
			/*
			 * validate
			 */
			resp.setErrorInfo(this.validate(enroll));
						
			/*
			 * check response
			 */
			if (resp.getErrorInfo() == null)
			{		
				boolean updateSucceded = enrollmentDAO.updateEnrollment(req.getEnrollment());
				if (updateSucceded)
				{
					resp.setStatus(StatusType.SUCCESS);
					resp.setEnrollment(req.getEnrollment());
					resp.setMsg("Successfully updated enrollment for user = "+req.getEnrollment().getUserId());
				}
				else
				{
					resp.setStatus(StatusType.FAILED);
					resp.setErrorInfo(CommonUtils.buildErrorInfo("FAILED", "Database was not updated! Please check your input"));
				}
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
	public DeleteEnrollmentResponse deleteEnrollment(DeleteEnrollmentRequest req)
	{
		DeleteEnrollmentResponse resp = new DeleteEnrollmentResponse();
		try 
		{
			//TODO validation
			
			boolean updateSucceded = enrollmentDAO.deleteEnrollment(req.getId());
			if (updateSucceded)
			{
				resp.setStatus(StatusType.SUCCESS);
				resp.setMsg("User successfully un-enrolled from the class");
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
	 * @param resp
	 * @param enroll
	 * @throws Exception 
	 */
	
	private ErrorInfo validate(Enrollment enroll) throws Exception
	{
		ErrorInfo errorInfo = new ErrorInfo();
		if (StringUtils.isBlank(enroll.getUserId()))
			CommonUtils.buildErrorInfo(errorInfo, "userId", "Please provide a valid userid");
		else
		{
			User user = this.usersDAO.getUserByUserId(enroll.getUserId());			
			if (user == null)
				CommonUtils.buildErrorInfo(errorInfo, "userId", "Student not found user id = "+enroll.getUserId());
		}
			
		
		if (StringUtils.isBlank(enroll.getClassroomid()))
			CommonUtils.buildErrorInfo(errorInfo, "classroomid", "Please provide a valid classroom id");
		else
		{
			Classroom classroom = this.classroomDAO.getClassroomById(enroll.getClassroomid());				
			if (classroom == null)
				CommonUtils.buildErrorInfo(errorInfo, "classroomid", "Classroom not found classroom id = "+enroll.getClassroomid());
		}
		
		if (errorInfo.getErrors() == null || errorInfo.getErrors().isEmpty())
			return null;
		else
			return errorInfo;
					
	}
	
	private String generateEnrollmentId(String userid, String classroomid)
	{
		return 	CommonUtils.getSecureHash("user:"+userid+"-class:"+classroomid);

	}
	
	
	
}
