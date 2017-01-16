/**
 * 
 */
package com.taksila.veda.db.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.taksila.veda.db.SQLDataBaseManager;
import com.taksila.veda.db.utils.TenantDBManager;
import com.taksila.veda.model.db.usermgmt.v1_0.UserImageInfo;
import com.taksila.veda.model.db.usermgmt.v1_0.UserImageType;
import com.taksila.veda.utils.CommonUtils;

/**
 * @author mahesh
 *
 */
@Repository
@Scope(value="prototype")
@Lazy(value = true)
public class UserImagesDAO implements UserImagesRepositoryInterface 
{
	@Autowired
	private TenantDBManager tenantDBManager;
	private String tenantId;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
    public UserImagesDAO(@Value("tenantId") String tenantId)
    {
		logger.trace(" building for tenant id = "+tenantId);
		this.tenantId = tenantId;
    }
	
	private String schoolId = null;	
	private static String insert_user_image_sql = "REPLACE INTO USER_IMAGES("+USER_IMAGES_TABLE.userid.value()+","+
																	USER_IMAGES_TABLE.imageid.value()+","+
																	USER_IMAGES_TABLE.image_type.value()+","+
																	USER_IMAGES_TABLE.imageContentLarge.value()+","+
																	USER_IMAGES_TABLE.imageContentThumb.value()+","+
																	USER_IMAGES_TABLE.lastUpdatedBy.value()+																			
																	") VALUES (?,?,?,?,?,?);";		
	
	private static String search_by_userid_and_image_type_sql = "SELECT * FROM USER_IMAGES WHERE "+
																			USER_IMAGES_TABLE.userid.value()+" = ? AND " +
																			USER_IMAGES_TABLE.image_type.value()+" = ? ";
	
	private static String search_by_user_id_sql = "SELECT "+USER_IMAGES_TABLE.userid.value()+","+
															USER_IMAGES_TABLE.imageid.value()+","+
															USER_IMAGES_TABLE.image_type.value()+","+														
															USER_IMAGES_TABLE.lastUpdatedBy.value()+","+
															USER_IMAGES_TABLE.lastUpdatedDatetime.value()+
														"	FROM USER_IMAGES WHERE "+USER_IMAGES_TABLE.userid.value()+" = ? ";
	
	
	private static String search_by_image_id_sql = "SELECT "+USER_IMAGES_TABLE.userid.value()+","+
															USER_IMAGES_TABLE.imageid.value()+","+
															USER_IMAGES_TABLE.image_type.value()+","+														
															USER_IMAGES_TABLE.lastUpdatedBy.value()+","+
															USER_IMAGES_TABLE.lastUpdatedDatetime.value()+
														"	FROM USER_IMAGES WHERE "+USER_IMAGES_TABLE.imageid.value()+" = ? ";
	
	private static String read_image_by_image_id_sql = "SELECT * FROM USER_IMAGES WHERE "+USER_IMAGES_TABLE.imageid.value()+" = ? ";

																			
	private static String update_user_images_content_sql = "UPDATE USER_IMAGES SET "+
																	USER_IMAGES_TABLE.imageContentLarge.value()+" = ? ,"+
																	USER_IMAGES_TABLE.imageContentThumb.value()+" = ? "+																				
																	" WHERE "+USER_IMAGES_TABLE.imageid.value()+" = ? ";
	
		
	private static String delete_image_sql = "DELETE FROM USER_IMAGES WHERE "+USER_IMAGES_TABLE.imageid.value()+" = ? ";	
		
	static Logger logger = LogManager.getLogger(UserImagesDAO.class.getName());
	SQLDataBaseManager sqlDBManager= null;
		
	
	private enum USER_IMAGES_TABLE
	{
		imageid("image_id"),
		userid("userid"),
		image_type("image_type"),
		imageContentLarge("image_content_large"),
		imageContentThumb("image_content_thumb"),		
		lastUpdatedBy("last_updated_by"),
		lastUpdatedDatetime("last_updated_datetime");
		private String name;       
	    private USER_IMAGES_TABLE(String s) 
	    {
	        name = s;
	    }
		
	    public String value() 
	    {
	        return this.name;
	    }
		
	};
	
	private UserImageInfo mapRow(ResultSet resultSet) throws SQLException, IOException, DatatypeConfigurationException 
	{
		UserImageInfo UserImageInfo = new UserImageInfo();		
		
		UserImageInfo.setUserId(resultSet.getString(USER_IMAGES_TABLE.userid.value()));
		UserImageInfo.setImageid(resultSet.getString(USER_IMAGES_TABLE.imageid.value()));
		UserImageInfo.setUserImageType(UserImageType.fromValue(resultSet.getString(USER_IMAGES_TABLE.image_type.value())));
		UserImageInfo.setUpdatedBy(resultSet.getString(USER_IMAGES_TABLE.lastUpdatedBy.value()));
		UserImageInfo.setLastUpdatedDateTime(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(USER_IMAGES_TABLE.lastUpdatedDatetime.value())));

		
		return UserImageInfo;
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UserImagesRepositoryInterface#search(com.taksila.veda.model.db.usermgmt.v1_0.UserImageInfo)
	 */
	@Override
	public List<UserImageInfo> search(UserImageInfo searchUserImage) throws Exception
	{
		List<UserImageInfo> UserImageInfoHits = new ArrayList<UserImageInfo>();				
		PreparedStatement stmt = null;				

		try
		{
			this.sqlDBManager.connect();
			if (StringUtils.isNotBlank(searchUserImage.getImageid()))
			{
				logger.trace("searching UserImageInfo query = "+search_by_image_id_sql +"param 1 ="+searchUserImage.getImageid());
				stmt = this.sqlDBManager.getPreparedStatement(search_by_image_id_sql);				
				stmt.setString(1, searchUserImage.getImageid());			
			}
			else
			{
				if (searchUserImage.getUserImageType() == null)
				{				
					stmt = this.sqlDBManager.getPreparedStatement(search_by_user_id_sql);				
					stmt.setString(1, searchUserImage.getUserId());								
				}
				else
				{
					logger.trace("searching UserImageInfo query = "+search_by_userid_and_image_type_sql +"param 1 ="+searchUserImage.getUserId()+", param 2 = "+searchUserImage.getUserImageType().value());
					stmt = this.sqlDBManager.getPreparedStatement(search_by_userid_and_image_type_sql);
					stmt.setString(1, searchUserImage.getUserId());	
					stmt.setString(2, searchUserImage.getUserImageType().value());
				}
			}
			
			
									
			ResultSet resultSet = stmt.executeQuery();	
			while (resultSet.next()) 
			{
				UserImageInfoHits.add(mapRow(resultSet));
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		finally
		{
			this.sqlDBManager.close(stmt);
		}
		
		return UserImageInfoHits;
		
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UserImagesRepositoryInterface#getUserImageInfoById(java.lang.String)
	 */
	@Override
	public UserImageInfo getUserImageInfoById(String imageid) throws Exception
	{						
		PreparedStatement stmt = null;	
		UserImageInfo UserImageInfo = null;
		try
		{
			this.sqlDBManager.connect();
			stmt = this.sqlDBManager.getPreparedStatement(search_by_image_id_sql);
			stmt.setString(1, imageid);
			ResultSet resultSet = stmt.executeQuery();	
			if (resultSet.next()) 
			{
				UserImageInfo = mapRow(resultSet);
			}
			
			return UserImageInfo;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		finally
		{
			this.sqlDBManager.close(stmt);
		}
				
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UserImagesRepositoryInterface#insertUserImageInfo(com.taksila.veda.model.db.usermgmt.v1_0.UserImageInfo, java.io.InputStream, java.io.InputStream)
	 */	
	@Override
	public UserImageInfo insertUserImageInfo(UserImageInfo UserImageInfo,InputStream imageLarge, InputStream imageThumb) throws Exception 
	{
		logger.debug("Entering into insertUserImageInfo():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(insert_user_image_sql);
			
			stmt.setString(1, UserImageInfo.getUserId());
			stmt.setString(2, UserImageInfo.getImageid());
			stmt.setString(3, UserImageInfo.getUserImageType().value());
			stmt.setBinaryStream(4,imageLarge);			
			stmt.setBinaryStream(5, imageThumb);
			stmt.setString(6, UserImageInfo.getUpdatedBy());
			
			stmt.executeUpdate();			
			ResultSet rs = stmt.getGeneratedKeys();			
			if (rs.next())
			{
				UserImageInfo.setId(String.valueOf(rs.getInt(1)));
			}
			
			return UserImageInfo;
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
			throw ex;
		}
		finally
		{
			this.sqlDBManager.close(stmt);
			imageLarge.close();
			imageThumb.close();
			
		}				 
								
	}
	
					
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UserImagesRepositoryInterface#readUserImageContent(java.lang.String, double)
	 */	
	@Override
	public ByteArrayOutputStream readUserImageContent(String imageId, double scale) throws Exception 
	{
		logger.trace("Entering into readUserImageInfoImage():::::");		
		PreparedStatement stmt = null;
		ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
		try
		{
			this.sqlDBManager.connect();
			logger.trace("search images :"+read_image_by_image_id_sql);
			stmt = this.sqlDBManager.getPreparedStatement(read_image_by_image_id_sql);								
			stmt.setString(1, imageId);									
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) 
			{
				InputStream imgIns = null;
				if (scale < 1)
					imgIns = resultSet.getBinaryStream(USER_IMAGES_TABLE.imageContentThumb.value());	
				else
					imgIns = resultSet.getBinaryStream(USER_IMAGES_TABLE.imageContentLarge.value());	
				
				if (imgIns != null)
				{
					logger.trace("Found an image:::::size = "+imgIns.available());
					int data = imgIns.read();
			        while (data >= 0) 
			        {
			        	imageOut.write((char) data);
			        	data = imgIns.read();
			        }
			        imageOut.flush();
			        imgIns.close();
				}
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
			throw ex;
		}
		finally
		{
			this.sqlDBManager.close(stmt);			
		}
		
		return imageOut;
								
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UserImagesRepositoryInterface#deleteUserImageInfo(java.lang.String)
	 */
	@Override
	public boolean deleteUserImageInfo(String imageId) throws Exception 
	{
		logger.debug("Entering into deleteUserImageInfo():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(delete_image_sql);
			stmt.setString(1, imageId);
			int t = stmt.executeUpdate();
			if (t > 0)
				return true;
			else
				return false;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
			throw ex;
		}
		finally
		{
			this.sqlDBManager.close(stmt);
		}
								
	}

	

}
