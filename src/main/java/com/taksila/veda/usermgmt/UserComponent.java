package com.taksila.veda.usermgmt;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.db.dao.UsersDAO;
import com.taksila.veda.db.dao.UsersDAO.USER_TABLE;
import com.taksila.veda.model.api.base.v1_0.SearchHitRecord;
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


public class UserComponent 
{
	static Logger logger = LogManager.getLogger(UserComponent.class.getName());	
	private UsersDAO usersDAO = null;
	private String schoolId =null;	
	
	public UserComponent(String tenantId) 
	{
		this.schoolId = tenantId;
		this.usersDAO = new UsersDAO(this.schoolId);				
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public SearchUserResponse searchUsers(SearchUserRequest req)
	{
		SearchUserResponse resp = new SearchUserResponse();
		try 
		{
			List<User> userSearchHits = usersDAO.searchUsers(req.getQuery());
			
			for(User user: userSearchHits)
			{
				SearchHitRecord rec = new SearchHitRecord();
				/*
				 * map search hits
				 */
				rec.setRecordId(String.valueOf(user.getId()));
				rec.setRecordTitle(user.getFirstName());
				rec.setRecordSubtitle(user.getLastName());				
				
				resp.getHits().add(rec);
			}
			
			resp.setRecordType("USERS");
			resp.setPage(req.getPage());
			resp.setPageOffset(req.getPageOffset());
			resp.setTotalHits(userSearchHits.size());

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
	public GetUserResponse getUser(GetUserRequest req)
	{
		GetUserResponse resp = new GetUserResponse();
		try 
		{
			User user = usersDAO.getUserById(req.getId());
			
			if (user == null)
			{	
				resp.setMsg("Did not find any records with id = "+req.getId());
			}
			else
			{
				resp.setUser(user);
			}
			
			logger.trace("++++++++  exiting getUser component ");

		} 
		catch (Exception e) 
		{
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
	}
	
	
//	/**
//	 * 
//	 * @param req
//	 * @return
//	 */
//	public GetUserResponse getUserByName(GetUserRequest req)
//	{
//		GetUserResponse resp = new GetUserResponse();
//		try 
//		{
//			User user = userDAO.getUserByName(req.getName());
//			
//			if (user == null)
//			{	
//				resp.setMsg("Did not find any records with id = "+req.getId());
//			}
//			else
//			{
//				resp.setUser(user);
//			}
//			
//			logger.trace("++++++++  exiting getUser component ");
//
//		} 
//		catch (Exception e) 
//		{
//			CommonUtils.handleExceptionForResponse(resp, e);
//		}
//		return resp;
//	}
	
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public CreateNewUserResponse createNewUser(CreateNewUserRequest req)
	{
		CreateNewUserResponse resp = new CreateNewUserResponse();
		try 
		{
			//TODO validation
			
			User user = usersDAO.insertUser(req.getNewUser());			
			resp.setUser(user);
			resp.setStatus(StatusType.SUCCESS);
			resp.setMsg("New user : "+user.getFirstName()+" "+user.getLastName()+" successfully added, record id = "+user.getId());
			resp.setSuccess(true);
			logger.trace("********  exiting user component createNewUser ");
		} 
		catch (Exception e) 
		{
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
		
	}
	
	/**
	 * 
	 * @param formParams
	 * @return
	 */
	public CreateNewUserResponse createNewUser(MultivaluedMap<String, String> formParams)
	{				
		User user = new User();
		CreateNewUserRequest operReq = new CreateNewUserRequest();
		
		mapFormValuesToUser(formParams, user); /* bind the form values to new user object */		
		operReq.setNewUser(user);
		
		return this.createNewUser(operReq); 		
		
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public UpdateUserResponse updateUser(UpdateUserRequest req)
	{		
		UpdateUserResponse resp = new UpdateUserResponse();
		try 
		{
			//TODO validation
			
			boolean updateSucceded = usersDAO.updateUser(req.getUser());
			if (updateSucceded)
			{
				resp.setStatus(StatusType.SUCCESS);
				resp.setUser(req.getUser());
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
	 * @param formParams
	 * @return
	 */
	public UpdateUserResponse updateUser(int id,MultivaluedMap<String, String> formParams)
	{						
		UpdateUserResponse operResp = new UpdateUserResponse();
		try	
		{
			logger.trace("About to update user record = "+id);			
			/*
			 * first fetch the original user record 
			 */
			GetUserRequest req = new GetUserRequest();
			req.setId(id);
			GetUserResponse userResp = this.getUser(req);
			
			/*
			 * check if the record exists before it can be updated
			 */
			if (userResp == null || userResp.getUser() == null)
			{						
				operResp.setStatus(StatusType.FAILED);
				operResp.setMsg("Could not located record id = "+id+"! Update was cancelled");
			}
			else
			{																
				User user = userResp.getUser();
				mapFormValuesToUser(formParams, user);						
				UpdateUserRequest updateUserReq = new UpdateUserRequest();
				updateUserReq.setUser(user);
				operResp = this.updateUser(updateUserReq);						
			}											
			
			operResp.setSuccess(true);
		}
		catch(Exception ex)
		{
			CommonUtils.handleExceptionForResponse(operResp, ex);
		}
		
		return operResp; 		
		
	}
	
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public UpdateUserResponse updateUserImage(String userId,InputStream userContentImageIs, String imageType, double scale)
	{
		logger.trace("++++++++  ABOUT to insert image into Users id= "+userId);
		UpdateUserResponse resp = new UpdateUserResponse();
		try 
		{
			//TODO validation
			
			boolean updateSucceded = usersDAO.updateUserImage(userId, userContentImageIs, imageType,scale);
			if (updateSucceded)
			{
				resp.setStatus(StatusType.SUCCESS);	
				resp.setSuccess(true);
			}
			else
			{
				resp.setSuccess(false);
				resp.setStatus(StatusType.FAILED);
				resp.setErrorInfo(CommonUtils.buildErrorInfo("FAILED", "Image content was not updated! Please check your data"));				
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
	 * @throws Exception 
	 */
	public ByteArrayOutputStream getUserImage(int userId,double scale) throws Exception
	{
		logger.trace("++++++++  ABOUT to getUserImage image = "+userId);				
			//TODO validation			
		return usersDAO.readUserImage(userId, scale);					
		
	}
	
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public DeleteUserResponse deleteUser(DeleteUserRequest req)
	{
		DeleteUserResponse resp = new DeleteUserResponse();
		try 
		{
			//TODO validation
			
			boolean updateSucceded = usersDAO.deleteUser(req.getId());
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
	
	/**
	 * This function is used by both create new user and update user
	 * 
	 * @param formParams
	 * @param user
	 */
	private void mapFormValuesToUser(MultivaluedMap<String, String> formParams, User user)
	{		
		
		/*
		 * update only the fields that have being sent in the 
		 * form
		 */
		for (String key: formParams.keySet())
		{
			if (StringUtils.equals(key, "id"))
				user.setId(formParams.getFirst("id"));
			
			if (StringUtils.equals(key, "userId"))
				user.setUserId(formParams.getFirst("userId"));
			
			if (StringUtils.equals(key, "emailId"))
				user.setEmailId(formParams.getFirst("emailId"));
			
			if (StringUtils.equals(key, "userrole"))						
				user.setUserrole(UserRole.fromValue(formParams.getFirst("userrole")));		
			
			if (StringUtils.equals(key, "firstName"))
				user.setFirstName(formParams.getFirst("firstName"));
			
			if (StringUtils.equals(key, "middleName"))
				user.setMiddleName(formParams.getFirst("middleName"));
			
			if (StringUtils.equals(key, "lastName"))
				user.setLastName(formParams.getFirst("lastName"));
									
			if (StringUtils.equals(key, "addressLine1"))
				user.setAddressLine1(formParams.getFirst("addressLine1"));
			
			if (StringUtils.equals(key, "addressLine2"))
				user.setAddressLine2(formParams.getFirst("addressLine2"));
			
			if (StringUtils.equals(key, "city"))
				user.setCity(formParams.getFirst("city"));
			
			if (StringUtils.equals(key, "state"))
				user.setState(formParams.getFirst("state"));
			
			if (StringUtils.equals(key,"postalcode"))
				user.setPostalcode(formParams.getFirst("postalcode"));
			
			if (StringUtils.equals(key,"country"))
				user.setCountry(formParams.getFirst("country"));
			
			if (StringUtils.equals(key, "cellphone"))
				user.setCellphone(formParams.getFirst("cellphone"));
			
			if (StringUtils.equals(key, "emailId"))
				user.setOkToText(Boolean.getBoolean(formParams.getFirst("emailId")));
			
			if (StringUtils.equals(key, "landlinephone"))
				user.setLandlinephone(formParams.getFirst("landlinephone"));
			
			if (StringUtils.equals(key, "officephone"))
				user.setOfficephone(formParams.getFirst("officephone"));
			
			if (StringUtils.equals(key, "officephoneExt"))
				user.setOfficephoneExt(formParams.getFirst("officephoneExt"));
			
			if (StringUtils.equals(key, "updatedBy"))
				user.setUpdatedBy(formParams.getFirst("updatedBy"));
		}
		
		
		
	}

	
}
