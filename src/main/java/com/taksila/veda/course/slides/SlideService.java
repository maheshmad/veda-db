package com.taksila.veda.course.slides;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

import com.taksila.veda.model.api.base.v1_0.BaseResponse;
import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.model.api.course.v1_0.CreateSlideRequest;
import com.taksila.veda.model.api.course.v1_0.CreateSlideResponse;
import com.taksila.veda.model.api.course.v1_0.DeleteSlideRequest;
import com.taksila.veda.model.api.course.v1_0.DeleteSlideResponse;
import com.taksila.veda.model.api.course.v1_0.GetSlideRequest;
import com.taksila.veda.model.api.course.v1_0.GetSlideResponse;
import com.taksila.veda.model.api.course.v1_0.SearchSlidesRequest;
import com.taksila.veda.model.api.course.v1_0.SearchSlidesResponse;
import com.taksila.veda.model.api.course.v1_0.Slide;
import com.taksila.veda.model.api.course.v1_0.UpdateSlideRequest;
import com.taksila.veda.model.api.course.v1_0.UpdateSlideResponse;
import com.taksila.veda.utils.CommonUtils;


@Path("/slides")
public class SlideService 
{
	static Logger logger = LogManager.getLogger(SlideService.class.getName());	
	Executor executor;

   public SlideService() 
   {
      executor = Executors.newSingleThreadExecutor();
   }
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	@Path("/generate/{topicid}/{uploadedfileid}")
	public void convertSlides(@Context HttpServletRequest request, 
			@Context final UriInfo uri,
			@PathParam("topicid") final String topicid,
			@PathParam("uploadedfileid") final String uploadedfileid,
			@Context HttpServletResponse resp,
			@Suspended final AsyncResponse asyncResp)
	{    						
		logger.trace("********  inside pptx to ");
		
		executor.execute( new Runnable() 
		{
			public void run() 
			{		
				logger.trace("********  inside thread to convert images  ");
				
				BaseResponse bResp = new BaseResponse(); 
				String schoolId = CommonUtils.getSubDomain(uri);
				SlideComponent slideComp = new SlideComponent(schoolId);
				try 
				{
					bResp = slideComp.generateImagesFromPptx(topicid,uploadedfileid);
					bResp.setStatus(StatusType.SUCCESS);
					bResp.setMsg("File was successfully processed");
					
				} 
				catch (Exception e) 
				{		
					CommonUtils.handleExceptionForResponse(bResp, e);
				}
				
				logger.trace("********  exiting thread to convert images ");
				asyncResp.resume(Response.ok(bResp).build());
			}
		});
		
		logger.trace("********  outside pptx to png  ");

		
	}
		
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
    public void createNewSlide(@Context HttpServletRequest request,@Context HttpServletResponse response,
    		@FormParam("name") final String name,
    		@FormParam("title") final String title,     		
    		@FormParam("subtitle") final String subtitle,
    		@FormParam("description") final String description,
    		@FormParam("textcontent") final String textContent,
    		@Context final UriInfo uri,	
    		@Suspended final AsyncResponse asyncResp) 
    {    	
		
		executor.execute(new Runnable() 
		{
			public void run() 
			{	
		
				CreateSlideResponse operResp = new CreateSlideResponse();
				try 
				{
					Slide slide = new Slide();
					slide.setName(name);
					slide.setSubTitle(subtitle);
					slide.setTitle(title);
					slide.setDescription(description);
					slide.setTextContent(textContent);
					
					CreateSlideRequest req = new CreateSlideRequest();
					req.setSlide(slide);
					
					String schoolId = CommonUtils.getSubDomain(uri);
					SlideComponent slideComp = new SlideComponent(schoolId);
					operResp = slideComp.createNewSlide(req); 			
					operResp.setSuccess(true);
				} 
				catch (Exception ex) 
				{		
					ex.printStackTrace();
					CommonUtils.handleExceptionForResponse(operResp, ex);
				}
				
				asyncResp.resume(Response.ok(operResp).build());
			}
		});
		
		logger.trace("********  exiting createNewSlide service ");


    }
	
	/**
	 * 
	 * @param request
	 * @param uri
	 * @param slideid
	 * @param resp
	 * @param asyncResp
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	@Path("/{slideid}")
	public void getSlide(@Context HttpServletRequest request, @Context final UriInfo uri,		
			@PathParam("slideid") final String slideid,
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		
		executor.execute(new Runnable() 
		{
			public void run() 
			{	
				GetSlideResponse operResp = new GetSlideResponse();
				try 
				{
					GetSlideRequest req = new GetSlideRequest();
					req.setId(Integer.valueOf(slideid));;
					
					String schoolId = CommonUtils.getSubDomain(uri);
					SlideComponent slideComp = new SlideComponent(schoolId);
					operResp = slideComp.getSlide(req); 			
					operResp.setSuccess(true);
				} 
				catch (Exception ex) 
				{		
					ex.printStackTrace();
					CommonUtils.handleExceptionForResponse(operResp, ex);
				}
				
				asyncResp.resume(Response.ok(operResp).build());
			}
		});
		
		
		logger.trace("********  exiting getSlide service ");
		
	}
	
	
	/**
	 * 
	 * @param request
	 * @param uri
	 * @param slideid
	 * @param resp
	 * @param asyncResp
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	@Path("/topic/{topicid}")
	public void getSlidesOfTopicId(@Context HttpServletRequest request, @Context final UriInfo uri,		
			@PathParam("topicid") final String topicid,
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
		
		executor.execute(new Runnable() 
		{
			public void run() 
			{	
				SearchSlidesResponse operResp = new SearchSlidesResponse();
				try 
				{
					SearchSlidesRequest req = new SearchSlidesRequest();
					req.setSearchParam(new Slide());
					req.getSearchParam().setTopicid(topicid);
					
					String schoolId = CommonUtils.getSubDomain(uri);
					SlideComponent slideComp = new SlideComponent(schoolId);
					operResp = slideComp.getSlidesByTopicId(req); 			
					operResp.setSuccess(true);
				} 
				catch (Exception ex) 
				{		
					ex.printStackTrace();
					CommonUtils.handleExceptionForResponse(operResp, ex);
				}
				
				asyncResp.resume(Response.ok(operResp).build());
			}
		});
		
		
		logger.trace("********  exiting getSlidesOfTopicId service ");
		
	}
	
	/**
	 * 
	 * @param request
	 * @param uri
	 * @param slideid
	 * @param resp
	 * @param asyncResp
	 */
	@GET
	@Produces("image/*")
	@ManagedAsync
	@Path("/image/{size}/{slideid}")
	public void getSlideImage(@Context HttpServletRequest request, 
			@Context final UriInfo uri,		
			@PathParam("slideid") final String slideid,
			@PathParam("size") final String size,
			@Context HttpServletResponse resp,
			@Suspended final AsyncResponse asyncResp)
	{    				
		
		executor.execute(new Runnable() 
		{
			public void run() 
			{					
				ByteArrayOutputStream operResp = null;
				try 
				{										
					String schoolId = CommonUtils.getSubDomain(uri);
					SlideComponent slideComp = new SlideComponent(schoolId);
					double scale = "large".equals(size)?1.0:0.5;
					operResp = slideComp.getSlideImage(Integer.parseInt(slideid), scale); 								
				} 
				catch (Exception ex) 
				{		
					ex.printStackTrace();
				}
				
				if (operResp != null)
					asyncResp.resume(Response.ok(operResp.toByteArray()).build());
				else
					Response.ok(CommonUtils.readImageFile("defaultprofileimage-128.png")).build();
			}
		});
		
		
		logger.trace("********  exiting getSlideImage service ");
		
	}
	
	
	/**
	 * 
	 * @param request
	 * @param uri
	 * @param name
	 * @param slideid
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
	@Path("/{slideid}")
	public void updateSlide(@Context HttpServletRequest request, @Context final UriInfo uri,	
			@FormParam("name") final String name,
			@PathParam("slideid") final String slideid,
    		@FormParam("title") final String title,     		
    		@FormParam("subtitle") final String subtitle,
    		@FormParam("description") final String description,
    		@FormParam("textcontent") final String textContent,
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
			
		executor.execute(new Runnable() 
		{
			public void run() 
			{	
				UpdateSlideResponse operResp = null;
				try
				{
					logger.trace("About to update slide record = "+slideid);
					
					Slide slide = new Slide();
					slide.setId(slideid);
					slide.setName(name);
					slide.setSubTitle(subtitle);
					slide.setTitle(title);
					slide.setDescription(description);
					slide.setTextContent(textContent);
					
					UpdateSlideRequest req = new UpdateSlideRequest();
					req.setSlide(slide);
					
					String schoolId = CommonUtils.getSubDomain(uri);
					SlideComponent slideComp = new SlideComponent(schoolId);
					operResp = slideComp.updateSlide(req);
					operResp.setSuccess(true);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					CommonUtils.handleExceptionForResponse(operResp, ex);
				}
				
				asyncResp.resume(Response.ok(operResp).build());
			}
		});
		
		logger.trace("************ exiting updateSlide() in service");
		
	}
	
	/**
	 * 
	 * @param request
	 * @param uri
	 * @param slideid
	 * @param resp
	 * @param asyncResp
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ManagedAsync
	@Path("/{slideid}")
	public void deleteSlide(@Context HttpServletRequest request, 
			@Context final UriInfo uri,	
			@PathParam("slideid") final String slideid,			
			@Context HttpServletResponse resp,
			@Suspended final AsyncResponse asyncResp)
	{    				
		executor.execute(new Runnable() 
		{
			public void run() 
			{		
				DeleteSlideResponse operResp = new DeleteSlideResponse();
				try
				{
					logger.trace("About to delete slide record = "+slideid);						
					
					String schoolId = CommonUtils.getSubDomain(uri);
					SlideComponent slideComp = new SlideComponent(schoolId);
					DeleteSlideRequest req = new DeleteSlideRequest();
					req.setId(Integer.valueOf(slideid));
					operResp = slideComp.deleteSlide(req);
					operResp.setSuccess(true);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					CommonUtils.handleExceptionForResponse(operResp, ex);
				}
				
				asyncResp.resume(Response.ok(operResp).build());
			}
		});
		
		logger.trace("************ exiting deleteSlide() in service");
		
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
	public void searchSlides(@Context HttpServletRequest request, 
			@Context final UriInfo uri,
			@Context HttpServletResponse resp,
			@QueryParam("q") final String name,
			@QueryParam("page") final String page,
			@QueryParam("start") final String start, 
			@Suspended final AsyncResponse asyncResp)
	{    				
		
		logger.trace("inside search query = "+name);
		executor.execute(new Runnable() 
		{
			public void run() 
			{	
				SearchSlidesResponse searchResp = new SearchSlidesResponse();		
				SearchSlidesRequest req = new SearchSlidesRequest();
				try 
				{					
					req.setPage(page == null?1:Integer.valueOf(page));
					req.setPageOffset(start == null ? 1: Integer.valueOf(start));		
					req.setQuery(name == null ? "": name);
					req.setRecordType("SLIDE");
					
					String schoolId = CommonUtils.getSubDomain(uri);
					SlideComponent slideComp = new SlideComponent(schoolId);
					searchResp = slideComp.searchSlide(req);
				
				} 
				catch (Exception e) 
				{			
					e.printStackTrace();
					CommonUtils.handleExceptionForResponse(searchResp, e);
				}
				
				searchResp.setSuccess(true);
				asyncResp.resume(Response.ok(searchResp).build());
			}
		});
		
		logger.trace("************ exiting searchSlide() in service");
		
		
	}
	
	
	
	
	

	
}
