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

import com.taksila.veda.model.api.course.v1_0.Chapter;
import com.taksila.veda.model.api.course.v1_0.CreateChapterRequest;
import com.taksila.veda.model.api.course.v1_0.CreateChapterResponse;
import com.taksila.veda.model.api.course.v1_0.DeleteChapterRequest;
import com.taksila.veda.model.api.course.v1_0.DeleteChapterResponse;
import com.taksila.veda.model.api.course.v1_0.GetChapterRequest;
import com.taksila.veda.model.api.course.v1_0.GetChapterResponse;
import com.taksila.veda.model.api.course.v1_0.SearchChaptersRequest;
import com.taksila.veda.model.api.course.v1_0.SearchChaptersResponse;
import com.taksila.veda.model.api.course.v1_0.UpdateChapterRequest;
import com.taksila.veda.model.api.course.v1_0.UpdateChapterResponse;
import com.taksila.veda.utils.CommonUtils;

@Path("/chapter")
public class ChapterService 
{
	static Logger logger = LogManager.getLogger(ChapterService.class.getName());	
		
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
    		@FormParam("courseid") String courseid,
    		@FormParam("name") String name,
    		@FormParam("title") String title,     		
    		@FormParam("subtitle") String subtitle,
    		@FormParam("description") String description,
    		@Context UriInfo uri,	
    		@Suspended final AsyncResponse asyncResp) 
    {    	
		
		CreateChapterResponse operResp = new CreateChapterResponse();
		try 
		{
			Chapter chapter = new Chapter();
			chapter.setName(name);
			chapter.setSubTitle(subtitle);
			chapter.setTitle(title);
			chapter.setDescription(description);
			chapter.setCourseid(courseid);
			
			CreateChapterRequest req = new CreateChapterRequest();
			req.setChapter(chapter);
			
			String schoolId = CommonUtils.getSubDomain(uri);
			ChapterComponent chapterComp = new ChapterComponent(schoolId);
			operResp = chapterComp.createNewChapter(req); 			
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
	 * @param chapterid
	 * @param resp
	 * @param asyncResp
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	@Path("/{chapterid}")
	public void getLoggedInUserInfo(@Context HttpServletRequest request, @Context UriInfo uri,		
			@PathParam("chapterid") String chapterid,
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		
		GetChapterResponse operResp = new GetChapterResponse();
		try 
		{
			GetChapterRequest req = new GetChapterRequest();
			req.setId(Integer.valueOf(chapterid));;
			
			String schoolId = CommonUtils.getSubDomain(uri);
			ChapterComponent chapterComp = new ChapterComponent(schoolId);
			operResp = chapterComp.getChapter(req); 			
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
	 * @param chapterid
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
	@Path("/{chapterid}")
	public void updateChapter(@Context HttpServletRequest request, @Context UriInfo uri,	
			@FormParam("name") String name,
			@PathParam("chapterid") String chapterid,
			@PathParam("courseid") String courseid,
    		@FormParam("title") String title,     		
    		@FormParam("subtitle") String subtitle,
    		@FormParam("description") String description,
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		UpdateChapterResponse operResp = null;
		try
		{
			logger.trace("About to update chapter record = "+chapterid);
			
			Chapter chapter = new Chapter();
			chapter.setId(chapterid);
			chapter.setName(name);
			chapter.setSubTitle(subtitle);
			chapter.setTitle(title);
			chapter.setDescription(description);
			chapter.setCourseid(courseid);
			
			UpdateChapterRequest req = new UpdateChapterRequest();
			req.setChapter(chapter);
			
			String schoolId = CommonUtils.getSubDomain(uri);
			ChapterComponent chapterComp = new ChapterComponent(schoolId);
			operResp = chapterComp.updateChapter(req);
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
	 * @param chapterid
	 * @param resp
	 * @param asyncResp
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ManagedAsync
	@Path("/{chapterid}")
	public void updateChapter(@Context HttpServletRequest request, @Context UriInfo uri,	@PathParam("chapterid") String chapterid,			
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		DeleteChapterResponse operResp = new DeleteChapterResponse();
		try
		{
			logger.trace("About to delete chapter record = "+chapterid);						
			
			String schoolId = CommonUtils.getSubDomain(uri);
			ChapterComponent chapterComp = new ChapterComponent(schoolId);
			DeleteChapterRequest req = new DeleteChapterRequest();
			req.setId(Integer.valueOf(chapterid));
			operResp = chapterComp.deleteChapter(req);
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
	public void searchChapters(@Context HttpServletRequest request, @Context UriInfo uri,@Context HttpServletResponse resp,
			@QueryParam("q") String name,@QueryParam("page") String page,@QueryParam("start") String start, @Suspended final AsyncResponse asyncResp)
	{    				
		
		logger.trace("inside search query = "+name);
		
		SearchChaptersResponse searchResp = new SearchChaptersResponse();		
		SearchChaptersRequest req = new SearchChaptersRequest();
		try 
		{
			req.setPage(Integer.valueOf(page));
			req.setPageOffset(Integer.valueOf(start));		
			req.setQuery(name);
			req.setRecordType("CHAPTER");
			
			String schoolId = CommonUtils.getSubDomain(uri);
			ChapterComponent chapterComp = new ChapterComponent(schoolId);
			searchResp = chapterComp.searchChapter(req);
		
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
		searchResp.setSuccess(true);
		asyncResp.resume(Response.ok(searchResp).build());
		
		
	}
	
	
	
	

	
}
