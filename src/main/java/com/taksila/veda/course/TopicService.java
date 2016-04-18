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

import com.taksila.veda.model.api.course.v1_0.Topic;
import com.taksila.veda.model.api.course.v1_0.CreateTopicRequest;
import com.taksila.veda.model.api.course.v1_0.CreateTopicResponse;
import com.taksila.veda.model.api.course.v1_0.DeleteTopicRequest;
import com.taksila.veda.model.api.course.v1_0.DeleteTopicResponse;
import com.taksila.veda.model.api.course.v1_0.GetTopicRequest;
import com.taksila.veda.model.api.course.v1_0.GetTopicResponse;
import com.taksila.veda.model.api.course.v1_0.SearchTopicsRequest;
import com.taksila.veda.model.api.course.v1_0.SearchTopicsResponse;
import com.taksila.veda.model.api.course.v1_0.UpdateTopicRequest;
import com.taksila.veda.model.api.course.v1_0.UpdateTopicResponse;
import com.taksila.veda.utils.CommonUtils;

@Path("/topic")
public class TopicService 
{
	static Logger logger = LogManager.getLogger(TopicService.class.getName());	
		
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
    		@FormParam("chapterid") String chapterid,
    		@FormParam("name") String name,
    		@FormParam("title") String title,     		
    		@FormParam("subtitle") String subtitle,
    		@FormParam("description") String description,
    		@Context UriInfo uri,	
    		@Suspended final AsyncResponse asyncResp) 
    {    	
		
		CreateTopicResponse operResp = new CreateTopicResponse();
		try 
		{
			Topic topic = new Topic();
			topic.setName(name);
			topic.setSubTitle(subtitle);
			topic.setTitle(title);
			topic.setDescription(description);
			topic.setChapterid(chapterid);
			
			CreateTopicRequest req = new CreateTopicRequest();
			req.setTopic(topic);
			
			String schoolId = CommonUtils.getSubDomain(uri);
			TopicComponent topicComp = new TopicComponent(schoolId);
			operResp = topicComp.createNewTopic(req); 			
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
	 * @param topicid
	 * @param resp
	 * @param asyncResp
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	@Path("/{topicid}")
	public void getLoggedInUserInfo(@Context HttpServletRequest request, @Context UriInfo uri,		
			@PathParam("topicid") String topicid,
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		
		GetTopicResponse operResp = new GetTopicResponse();
		try 
		{
			GetTopicRequest req = new GetTopicRequest();
			req.setId(Integer.valueOf(topicid));;
			
			String schoolId = CommonUtils.getSubDomain(uri);
			TopicComponent topicComp = new TopicComponent(schoolId);
			operResp = topicComp.getTopic(req); 			
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
	 * @param topicid
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
	@Path("/{topicid}")
	public void updateTopic(@Context HttpServletRequest request, @Context UriInfo uri,	
			@FormParam("chapterid") String chapterid,
			@FormParam("name") String name,
			@PathParam("topicid") String topicid,
    		@FormParam("title") String title,     		
    		@FormParam("subtitle") String subtitle,
    		@FormParam("description") String description,
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		UpdateTopicResponse operResp = null;
		try
		{
			logger.trace("About to update topic record = "+topicid);
			
			Topic topic = new Topic();
			topic.setId(topicid);
			topic.setName(name);
			topic.setSubTitle(subtitle);
			topic.setTitle(title);
			topic.setDescription(description);
			topic.setChapterid(chapterid);
			
			UpdateTopicRequest req = new UpdateTopicRequest();
			req.setTopic(topic);
			
			String schoolId = CommonUtils.getSubDomain(uri);
			TopicComponent topicComp = new TopicComponent(schoolId);
			operResp = topicComp.updateTopic(req);
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
	 * @param topicid
	 * @param resp
	 * @param asyncResp
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ManagedAsync
	@Path("/{topicid}")
	public void updateTopic(@Context HttpServletRequest request, @Context UriInfo uri,	@PathParam("topicid") String topicid,			
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		DeleteTopicResponse operResp = new DeleteTopicResponse();
		try
		{
			logger.trace("About to delete topic record = "+topicid);						
			
			String schoolId = CommonUtils.getSubDomain(uri);
			TopicComponent topicComp = new TopicComponent(schoolId);
			DeleteTopicRequest req = new DeleteTopicRequest();
			req.setId(Integer.valueOf(topicid));
			operResp = topicComp.deleteTopic(req);
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
	public void searchTopics(@Context HttpServletRequest request, @Context UriInfo uri,@Context HttpServletResponse resp,
			@QueryParam("q") String name,@QueryParam("page") String page,@QueryParam("start") String start, @Suspended final AsyncResponse asyncResp)
	{    				
		
		logger.trace("inside search query = "+name);
		
		SearchTopicsResponse searchResp = new SearchTopicsResponse();		
		SearchTopicsRequest req = new SearchTopicsRequest();
		try 
		{
			req.setPage(Integer.valueOf(page));
			req.setPageOffset(Integer.valueOf(start));		
			req.setQuery(name);
			req.setRecordType("TOPIC");
			
			String schoolId = CommonUtils.getSubDomain(uri);
			TopicComponent topicComp = new TopicComponent(schoolId);
			searchResp = topicComp.searchTopic(req);
		
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		
		searchResp.setSuccess(true);
		asyncResp.resume(Response.ok(searchResp).build());
		
		
	}
	
	
	
	

	
}
