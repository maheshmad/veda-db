package com.taksila.veda.classroom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ManagedAsync;

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
import com.taksila.veda.model.db.classroom.v1_0.EnrollmentStatusType;
import com.taksila.veda.security.SecurityUtils;
import com.taksila.veda.utils.CommonUtils;

@Path("/enrollment")
public class EnrollmentService 
{
	static Logger logger = LogManager.getLogger(EnrollmentService.class.getName());	
		
	/**
	 * 	 
	 */
	@POST	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ManagedAsync
    public void post(@Context HttpServletRequest request,@Context HttpServletResponse response,
    		final MultivaluedMap<String, String> formParams,    		
    		@Context UriInfo uri,	
    		@Suspended final AsyncResponse asyncResp) 
    {    	
		String tenantId = CommonUtils.getSubDomain(uri);
		EnrollmentComponent enrollmentComp = new EnrollmentComponent(tenantId);
		CreateEnrollmentResponse operResp = new CreateEnrollmentResponse();
		
		logger.trace("processing enrollment creation..");
		try 
		{
			String principalUserId = SecurityUtils.getLoggedInPrincipalUserid(tenantId, request);
			
			Enrollment enrollment = new Enrollment();
			enrollmentComp.mapFormFields(formParams, enrollment);
			enrollment.setUpdatedBy(principalUserId);
			
			CreateEnrollmentRequest req = new CreateEnrollmentRequest();
			req.setEnrollment(enrollment);
			
			operResp = enrollmentComp.createNewEnrollment(req); 			
			operResp.setSuccess(true);
		} 
		catch (Exception ex) 
		{		
			ex.printStackTrace();
			CommonUtils.handleExceptionForResponse(operResp, ex);
		}
		
		asyncResp.resume(Response.ok(operResp).build());

    }
	
	/**
	 * 
	 * @param request
	 * @param uri
	 * @param enrollmentid
	 * @param resp
	 * @param asyncResp
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	@Path("/{enrollmentid}")
	public void getLoggedInUserInfo(@Context HttpServletRequest request, @Context UriInfo uri,		
			@PathParam("enrollmentid") String enrollmentid,
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		
		GetEnrollmentResponse operResp = new GetEnrollmentResponse();
		try 
		{
			GetEnrollmentRequest req = new GetEnrollmentRequest();
			req.setId(enrollmentid);;
			
			String schoolId = CommonUtils.getSubDomain(uri);
			EnrollmentComponent enrollmentComp = new EnrollmentComponent(schoolId);
			operResp = enrollmentComp.getEnrollment(req); 			
			operResp.setSuccess(true);
		} 
		catch (Exception ex) 
		{		
			ex.printStackTrace();
			CommonUtils.handleExceptionForResponse(operResp, ex);
		}
		
		asyncResp.resume(Response.ok(operResp).build());
		
	}
		
	
	
	/**
	 * 
	 * @param request
	 * @param uri
	 * @param name
	 * @param enrollmentid
	 * @param title
	 * @param subtitle
	 * @param description
	 * @param resp
	 * @param asyncResp
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ManagedAsync
	@Path("/{enrollmentid}")
	public void put(@Context HttpServletRequest request, @Context UriInfo uri,	
			@PathParam("enrollmentid") String enrollmentid,
    		final MultivaluedMap<String, String> formParams,    		
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		String schoolId = CommonUtils.getSubDomain(uri);
		EnrollmentComponent enrollmentComp = new EnrollmentComponent(schoolId);
		UpdateEnrollmentResponse operResp = new UpdateEnrollmentResponse();		
		String principalUserId = SecurityUtils.getLoggedInPrincipalUserid(schoolId, request);
		try
		{
			logger.trace("About to update enrollment record = "+enrollmentid);
			
			Enrollment enrollment = new Enrollment();
			enrollment.setId(enrollmentid);
			enrollment.setUpdatedBy(principalUserId);
			enrollmentComp.mapFormFields(formParams, enrollment);
			
			UpdateEnrollmentRequest req = new UpdateEnrollmentRequest();
			req.setEnrollment(enrollment);
									
			operResp = enrollmentComp.updateEnrollment(req);
			operResp.setSuccess(true);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			CommonUtils.handleExceptionForResponse(operResp, ex);
		}
		
		asyncResp.resume(Response.ok(operResp).build());
		
	}
	
	/**
	 * 
	 * @param request
	 * @param uri
	 * @param enrollmentid
	 * @param resp
	 * @param asyncResp
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ManagedAsync
	@Path("/{enrollmentid}")
	public void deleteEnrollment(@Context HttpServletRequest request, @Context UriInfo uri,	@PathParam("enrollmentid") String enrollmentid,			
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		DeleteEnrollmentResponse operResp = new DeleteEnrollmentResponse();
		try
		{
			logger.trace("About to delete enrollment record = "+enrollmentid);						
			
			String schoolId = CommonUtils.getSubDomain(uri);
			EnrollmentComponent enrollmentComp = new EnrollmentComponent(schoolId);
			DeleteEnrollmentRequest req = new DeleteEnrollmentRequest();
			req.setId(enrollmentid);
			operResp = enrollmentComp.deleteEnrollment(req);
			operResp.setSuccess(true);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			CommonUtils.handleExceptionForResponse(operResp, ex);
		}
		
		asyncResp.resume(Response.ok(operResp).build());
		
	}
	
	/**
	 * 
	 * @param request
	 * @param uri
	 * @param resp
	 * @param name
	 * @param page
	 * @param start
	 * @param asyncResp
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	@Path("/search")
	public void searchEnrollments(@Context HttpServletRequest request, @Context UriInfo uri,@Context HttpServletResponse resp,
			@QueryParam("userid") String userid,
			@QueryParam("classroomid") String classroomid,
			@QueryParam("page") String page,
			@QueryParam("start") String start, 
			@Suspended final AsyncResponse asyncResp)
	{    				
		
		logger.trace("inside search enrollment query = "+classroomid);
		
		SearchEnrollmentResponse searchResp = new SearchEnrollmentResponse();		
		SearchEnrollmentRequest req = new SearchEnrollmentRequest();
		String schoolId = CommonUtils.getSubDomain(uri);
		EnrollmentComponent enrollmentComp = new EnrollmentComponent(schoolId);
		
		try 
		{
			if (StringUtils.isNotBlank(page))			
				req.setPage(Integer.valueOf(page));
			if (StringUtils.isNotBlank(start))		
				req.setPageOffset(Integer.valueOf(start));
						
			req.setRecordType("ENROLLMENT");
			
			if (StringUtils.isBlank(userid))
			{
				req.setQuery(classroomid);
				searchResp = enrollmentComp.searchEnrollmentByClassroom(req);
			}
			else
			{
				req.setQuery(userid);
				searchResp = enrollmentComp.searchEnrollmentByUserid(req);
			}
						
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
		searchResp.setSuccess(true);
		asyncResp.resume(Response.ok(searchResp).build());
		
		
	}
	
	
	
	
	

	
}
