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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ManagedAsync;

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

@Path("/classroom")
public class ClassroomService 
{
	static Logger logger = LogManager.getLogger(ClassroomService.class.getName());	
		
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
    		@FormParam("subTitle") String subtitle,
    		@FormParam("description") String description,
    		@Context UriInfo uri,	
    		@Suspended final AsyncResponse asyncResp) 
    {    	
		
		CreateClassroomResponse operResp = new CreateClassroomResponse();
		try 
		{
			Classroom classroom = new Classroom();
			classroom.setName(name);
			classroom.setSubTitle(subtitle);
			classroom.setTitle(title);
			classroom.setDescription(description);
			
			CreateClassroomRequest req = new CreateClassroomRequest();
			req.setClassroom(classroom);
			
			String schoolId = CommonUtils.getSubDomain(uri);
			ClassroomComponent classroomComp = new ClassroomComponent(schoolId);
			operResp = classroomComp.createNewClassroom(req); 			
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
	 * @param classroomid
	 * @param resp
	 * @param asyncResp
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	@Path("/{classroomid}")
	public void getLoggedInUserInfo(@Context HttpServletRequest request, @Context UriInfo uri,		
			@PathParam("classroomid") String classroomid,
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		
		GetClassroomResponse operResp = new GetClassroomResponse();
		try 
		{
			GetClassroomRequest req = new GetClassroomRequest();
			req.setId(classroomid);;
			
			String schoolId = CommonUtils.getSubDomain(uri);
			ClassroomComponent classroomComp = new ClassroomComponent(schoolId);
			operResp = classroomComp.getClassroom(req); 			
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
	 * @param classroomid
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
	@Path("/{classroomid}")
	public void updateClassroom(@Context HttpServletRequest request, @Context UriInfo uri,	
			@FormParam("name") String name,
			@PathParam("classroomid") String classroomid,
    		@FormParam("title") String title,     		
    		@FormParam("subTitle") String subtitle,
    		@FormParam("description") String description,
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		UpdateClassroomResponse operResp = null;
		try
		{
			logger.trace("About to update classroom record = "+classroomid);
			
			Classroom classroom = new Classroom();
			classroom.setId(classroomid);
			classroom.setName(name);
			classroom.setSubTitle(subtitle);
			classroom.setTitle(title);
			classroom.setDescription(description);
			
			UpdateClassroomRequest req = new UpdateClassroomRequest();
			req.setClassroom(classroom);
			
			String schoolId = CommonUtils.getSubDomain(uri);
			ClassroomComponent classroomComp = new ClassroomComponent(schoolId);
			operResp = classroomComp.updateClassroom(req);
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
	 * @param classroomid
	 * @param resp
	 * @param asyncResp
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ManagedAsync
	@Path("/{classroomid}")
	public void updateClassroom(@Context HttpServletRequest request, @Context UriInfo uri,	@PathParam("classroomid") String classroomid,			
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		DeleteClassroomResponse operResp = new DeleteClassroomResponse();
		try
		{
			logger.trace("About to delete classroom record = "+classroomid);						
			
			String schoolId = CommonUtils.getSubDomain(uri);
			ClassroomComponent classroomComp = new ClassroomComponent(schoolId);
			DeleteClassroomRequest req = new DeleteClassroomRequest();
			req.setId(classroomid);
			operResp = classroomComp.deleteClassroom(req);
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
	public void searchClassrooms(@Context HttpServletRequest request, @Context UriInfo uri,@Context HttpServletResponse resp,
			@QueryParam("q") String name,@QueryParam("page") String page,@QueryParam("start") String start, @Suspended final AsyncResponse asyncResp)
	{    				
		
		logger.trace("inside search query = "+name);
		
		SearchClassroomResponse searchResp = new SearchClassroomResponse();		
		SearchClassroomRequest req = new SearchClassroomRequest();
		try 
		{
			if (StringUtils.isNotBlank(page))			
				req.setPage(Integer.valueOf(page));
			if (StringUtils.isNotBlank(start))	
				req.setPageOffset(Integer.valueOf(start));
			
			
			req.setQuery(name);
			req.setRecordType("CLASSROOM");
			
			String schoolId = CommonUtils.getSubDomain(uri);
			ClassroomComponent classroomComp = new ClassroomComponent(schoolId);
			searchResp = classroomComp.searchClassroom(req);
		
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
		searchResp.setSuccess(true);
		asyncResp.resume(Response.ok(searchResp).build());
		
		
	}
	
	
	
	
	

	
}
