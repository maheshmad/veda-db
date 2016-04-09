package com.taksila.veda.course;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ManagedAsync;

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

@Path("/course")
public class CourseService 
{
	static Logger logger = LogManager.getLogger(CourseService.class.getName());	
		
	/**
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param title
	 * @param subtitle
	 * @param description
	 * @param uri
	 * @param asyncResp
	 */
	@POST	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ManagedAsync
    public void post(@Context HttpServletRequest request,@Context HttpServletResponse response,
    		@FormParam("name") String name,
    		@FormParam("title") String title,     		
    		@FormParam("subtitle") String subtitle,
    		@FormParam("description") String description,
    		@Context UriInfo uri,	
    		@Suspended final AsyncResponse asyncResp) 
    {    	
		
		CreateNewCourseResponse operResp = new CreateNewCourseResponse();
		try 
		{
			Course course = new Course();
			course.setName(name);
			course.setSubTitle(subtitle);
			course.setTitle(title);
			course.setDescription(description);
			
			CreateNewCourseRequest req = new CreateNewCourseRequest();
			req.setNewCourse(course);
			
			String schoolId = CommonUtils.getSubDomain(uri);
			CourseComponent courseComp = new CourseComponent(schoolId);
			operResp = courseComp.createNewCourse(req); 			
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
	 * @param courseid
	 * @param resp
	 * @param asyncResp
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	@Path("/{courseid}")
	public void getLoggedInUserInfo(@Context HttpServletRequest request, @Context UriInfo uri,		
			@PathParam("courseid") String courseid,
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		
		GetCourseInfoResponse operResp = new GetCourseInfoResponse();
		try 
		{
			GetCourseInfoRequest req = new GetCourseInfoRequest();
			req.setId(Integer.valueOf(courseid));;
			
			String schoolId = CommonUtils.getSubDomain(uri);
			CourseComponent courseComp = new CourseComponent(schoolId);
			operResp = courseComp.getCourse(req); 			
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
	 * @param courseid
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
	@Path("/{courseid}")
	public void updateCourse(@Context HttpServletRequest request, @Context UriInfo uri,	
			@FormParam("name") String name,
			@PathParam("courseid") String courseid,
    		@FormParam("title") String title,     		
    		@FormParam("subtitle") String subtitle,
    		@FormParam("description") String description,
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		UpdateCourseResponse operResp = null;
		try
		{
			logger.trace("About to update course record = "+courseid);
			
			Course course = new Course();
			course.setId(courseid);
			course.setName(name);
			course.setSubTitle(subtitle);
			course.setTitle(title);
			course.setDescription(description);
			
			UpdateCourseRequest req = new UpdateCourseRequest();
			req.setCourse(course);
			
			String schoolId = CommonUtils.getSubDomain(uri);
			CourseComponent courseComp = new CourseComponent(schoolId);
			operResp = courseComp.updateCourse(req);
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
	 * @param courseid
	 * @param resp
	 * @param asyncResp
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ManagedAsync
	@Path("/{courseid}")
	public void updateCourse(@Context HttpServletRequest request, @Context UriInfo uri,	@PathParam("courseid") String courseid,			
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		DeleteCourseResponse operResp = new DeleteCourseResponse();
		try
		{
			logger.trace("About to delete course record = "+courseid);						
			
			String schoolId = CommonUtils.getSubDomain(uri);
			CourseComponent courseComp = new CourseComponent(schoolId);
			DeleteCourseRequest req = new DeleteCourseRequest();
			req.setId(Integer.valueOf(courseid));
			operResp = courseComp.deleteCourse(req);
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
	public void searchCourses(@Context HttpServletRequest request, @Context UriInfo uri,@Context HttpServletResponse resp,
			@QueryParam("q") String name,@QueryParam("page") String page,@QueryParam("start") String start, @Suspended final AsyncResponse asyncResp)
	{    				
		
		logger.trace("inside search query = "+name);
		
		SearchCourseResponse searchResp = new SearchCourseResponse();		
		SearchCourseRequest req = new SearchCourseRequest();
		try 
		{
			req.setPage(Integer.valueOf(page));
			req.setPageOffset(Integer.valueOf(start));		
			req.setQuery(name);
			req.setRecordType("COURSE");
			
			String schoolId = CommonUtils.getSubDomain(uri);
			CourseComponent courseComp = new CourseComponent(schoolId);
			searchResp = courseComp.searchCourses(req);
		
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
		searchResp.setSuccess(true);
		asyncResp.resume(Response.ok(searchResp).build());
		
		
	}
	
	
	
	

	
}
