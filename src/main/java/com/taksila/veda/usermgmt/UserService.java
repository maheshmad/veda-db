package com.taksila.veda.usermgmt;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import org.apache.commons.codec.binary.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ManagedAsync;

import com.taksila.veda.db.dao.UsersDAO.USER_TABLE;
import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.model.api.usermgmt.v1_0.CreateNewUserRequest;
import com.taksila.veda.model.api.usermgmt.v1_0.CreateNewUserResponse;
import com.taksila.veda.model.api.usermgmt.v1_0.DeleteUserRequest;
import com.taksila.veda.model.api.usermgmt.v1_0.DeleteUserResponse;
import com.taksila.veda.model.api.usermgmt.v1_0.GetUserRequest;
import com.taksila.veda.model.api.usermgmt.v1_0.GetUserResponse;
import com.taksila.veda.model.api.usermgmt.v1_0.SearchUserRequest;
import com.taksila.veda.model.api.usermgmt.v1_0.SearchUserResponse;
import com.taksila.veda.model.api.usermgmt.v1_0.UpdateUserRequest;
import com.taksila.veda.model.api.usermgmt.v1_0.UpdateUserResponse;
import com.taksila.veda.model.db.base.v1_0.UserRole;
import com.taksila.veda.model.db.usermgmt.v1_0.User;
import com.taksila.veda.utils.CommonUtils;


@Path("/user")
public class UserService 
{
	static Logger logger = LogManager.getLogger(UserService.class.getName());	
	Executor executor;

   public UserService() 
   {
      executor = Executors.newSingleThreadExecutor();
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
    public void createNewUser(@Context HttpServletRequest request,@Context HttpServletResponse response,
    		final MultivaluedMap<String, String> formParams,
    		@Context final UriInfo uri,	
    		@Suspended final AsyncResponse asyncResp) 
    {    	
		
		executor.execute(new Runnable() 
		{
			public void run() 
			{	
				String schoolId = CommonUtils.getSubDomain(uri);				
				CreateNewUserResponse operResp = new CreateNewUserResponse();
				try 
				{					
					UserComponent userComp = new UserComponent(schoolId);					
					operResp = userComp.createNewUser(formParams); 
					if (operResp.getErrorInfo() == null)
						operResp.setSuccess(true);
					else
						operResp.setSuccess(false);
				} 
				catch (Exception ex) 
				{		
					CommonUtils.handleExceptionForResponse(operResp, ex);
					operResp.setSuccess(false);
				}
				
				asyncResp.resume(Response.ok(operResp).build());
			}
		});
		
		logger.trace("********  exiting createNewUser service ");


    }
	
	/**
	 * 
	 * @param request
	 * @param uri
	 * @param userid
	 * @param resp
	 * @param asyncResp
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	@Path("/{id}")
	public void getUser(@Context HttpServletRequest request, @Context final UriInfo uri,		
			@PathParam("id") final int id,
			@Context HttpServletResponse resp,
			@Suspended final AsyncResponse asyncResp)
	{    				
		
		executor.execute(new Runnable() 
		{
			public void run() 
			{	
				GetUserResponse operResp = new GetUserResponse();
				try 
				{
					String schoolId = CommonUtils.getSubDomain(uri);
					UserComponent userComp = new UserComponent(schoolId);
					operResp = userComp.getUser(id); 			
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
		
		
		logger.trace("********  exiting getUser service ");
		
	}
	
			
	/**
	 * 
	 * @param request
	 * @param uri
	 * @param userid
	 * @param resp
	 * @param asyncResp
	 */
	@GET
	@Produces("image/*")
	@ManagedAsync
	@Path("/image/{size}/{userid}")
	public void getUserImage(@Context HttpServletRequest request, 
			@Context final UriInfo uri,		
			@PathParam("userid") final String userid,
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
					UserComponent userComp = new UserComponent(schoolId);
					double scale = "large".equals(size)?1.0:0.5;
					operResp = userComp.getUserImage(Integer.parseInt(userid), scale); 								
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
		
		
		logger.trace("********  exiting getUserImage service ");
		
	}
	
	
	/**
	 * 
	 * @param request
	 * @param uri
	 * @param name
	 * @param userid
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
	@Path("/{id}")
	public void updateUser(@Context HttpServletRequest request, @Context final UriInfo uri,				
			@PathParam("id") final int id, 
			final MultivaluedMap<String, String> formParams,
			@Context HttpServletResponse resp,@Suspended final AsyncResponse asyncResp)
	{    				
			
		executor.execute(new Runnable() 
		{
			public void run() 
			{	
				UpdateUserResponse operResp = new UpdateUserResponse();
				String schoolId = CommonUtils.getSubDomain(uri);
				UserComponent userComp = new UserComponent(schoolId);
				
				try 
				{											
					operResp = userComp.updateUser(id, formParams); 			
					operResp.setSuccess(true);
				} 
				catch (Exception ex) 
				{		
					CommonUtils.handleExceptionForResponse(operResp, ex);
				}
				
				asyncResp.resume(Response.ok(operResp).build());
			}
		});
		
		logger.trace("************ exiting updateUser() in service");
				
	}
	
	/**
	 * 
	 * @param request
	 * @param uri
	 * @param userid
	 * @param resp
	 * @param asyncResp
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ManagedAsync
	@Path("/{userid}")
	public void deleteUser(@Context HttpServletRequest request, 
			@Context final UriInfo uri,	
			@PathParam("userid") final String userid,			
			@Context HttpServletResponse resp,
			@Suspended final AsyncResponse asyncResp)
	{    				
		executor.execute(new Runnable() 
		{
			public void run() 
			{		
				DeleteUserResponse operResp = new DeleteUserResponse();
				try
				{
					logger.trace("About to delete user record = "+userid);						
					
					String schoolId = CommonUtils.getSubDomain(uri);
					UserComponent userComp = new UserComponent(schoolId);
					DeleteUserRequest req = new DeleteUserRequest();
					req.setId(Integer.valueOf(userid));
					operResp = userComp.deleteUser(req);
					operResp.setSuccess(true);
				}
				catch(Exception ex)
				{
					CommonUtils.handleExceptionForResponse(operResp, ex);
				}
				
				asyncResp.resume(Response.ok(operResp).build());
			}
		});
		
		logger.trace("************ exiting deleteUser() in service");
		
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
	public void searchUsers(@Context HttpServletRequest request, 
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
				SearchUserResponse searchResp = new SearchUserResponse();		
				SearchUserRequest req = new SearchUserRequest();
				try 
				{					
					req.setPage(page == null?1:Integer.valueOf(page));
					req.setPageOffset(start == null ? 1: Integer.valueOf(start));		
					req.setQuery(name == null ? "": name);
					req.setRecordType("USERS");
					
					String schoolId = CommonUtils.getSubDomain(uri);
					UserComponent userComp = new UserComponent(schoolId);
					searchResp = userComp.searchUsers(req);
					searchResp.setStatus(StatusType.SUCCESS);;
				} 
				catch (Exception e) 
				{								
					CommonUtils.handleExceptionForResponse(searchResp, e);
				}
				
				searchResp.setSuccess(true);
				asyncResp.resume(Response.ok(searchResp).build());
			}
		});
		
		logger.trace("************ exiting searchUser() in service");
		
		
	}
	
	
	
	
	
	

	
}
