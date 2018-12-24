/**
 * 
 */
package com.taksila.veda.db.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.taksila.veda.db.utils.DBCommonUtils;
import com.taksila.veda.db.utils.DaoUtils;
import com.taksila.veda.db.utils.TenantDBManager;
import com.taksila.veda.model.db.usermgmt.v1_0.User;

/**
 * @author mahesh
 *
 */

@Repository
@Scope(value="prototype")
@Lazy(value = true)
public class UsersDAO implements UsersRepositoryInterface 
{
	@Autowired
	private TenantDBManager tenantDBManager;
	private String tenantId;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
    public UsersDAO(@Value("tenantId") String tenantId)
    {
		logger.trace(" building for tenant id = "+tenantId);
		this.tenantId = tenantId;
    }

	private static String insert_user_sql = "INSERT INTO USERS("+	USER_TABLE.userid.value()+","+
																		USER_TABLE.emailid.value()+","+
																		USER_TABLE.pswd.value()+","+
																		USER_TABLE.roles.value()+","+
																		USER_TABLE.firstName.value()+","+
																		USER_TABLE.middleName.value()+","+
																		USER_TABLE.lastName.value()+","+
																		USER_TABLE.addressLine1.value()+","+
																		USER_TABLE.addressLine2.value()+","+
																		USER_TABLE.city.value()+","+
																		USER_TABLE.state.value()+","+
																		USER_TABLE.postalcode.value()+","+
																		USER_TABLE.country.value()+","+
																		USER_TABLE.cellPhone.value()+","+
																		USER_TABLE.ok_to_text.value()+","+
																		USER_TABLE.landPhone.value()+","+
																		USER_TABLE.officePhone.value()+","+
																		USER_TABLE.officePhoneExt.value()+","+
																		USER_TABLE.lastUpdatedBy.value()+
																	") "+
																	"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";		
			
	private static String update_user_sql = "UPDATE USERS SET "+	USER_TABLE.userid.value()+" = ? ,"+
																		USER_TABLE.emailid.value()+" = ? ,"+
																		USER_TABLE.pswd.value()+" = ? ,"+
																		USER_TABLE.roles.value()+" = ? ,"+
																		USER_TABLE.firstName.value()+" = ? ,"+
																		USER_TABLE.middleName.value()+" = ? ,"+
																		USER_TABLE.lastName.value()+" = ? ,"+
																		USER_TABLE.addressLine1.value()+" = ? ,"+
																		USER_TABLE.addressLine2.value()+" = ? ,"+
																		USER_TABLE.city.value()+" = ? ,"+
																		USER_TABLE.state.value()+" = ? ,"+
																		USER_TABLE.postalcode.value()+" = ? ,"+
																		USER_TABLE.country.value()+" = ? ,"+
																		USER_TABLE.cellPhone.value()+" = ? ,"+
																		USER_TABLE.ok_to_text.value()+" = ? ,"+
																		USER_TABLE.landPhone.value()+" = ? ,"+
																		USER_TABLE.officePhone.value()+" = ? ,"+
																		USER_TABLE.officePhoneExt.value()+" = ? ,"+
																		USER_TABLE.lastUpdatedBy.value()+" = ?"+
															" WHERE "+USER_TABLE.id.value()+" = ? ";
	
	private static String update_user_password = "UPDATE USERS SET "+ USER_TABLE.pswd.value()+" = ? ,"+
																USER_TABLE.changePswd.value()+" = ? "+	
														" WHERE "+USER_TABLE.userid.value()+" = ? ";
	
	private static String delete_user_sql = "DELETE FROM USERS WHERE "+USER_TABLE.id.value()+" = ? ";	
	
	private static String search_users_by_userrecordid_sql = "SELECT * FROM USERS WHERE "+USER_TABLE.id.value()+" = ?";
	private static String search_users_by_userid_sql = "SELECT * FROM USERS WHERE "+USER_TABLE.userid.value()+" = ?";
	private static String search_users_by_emailid_sql = "SELECT * FROM USERS WHERE "+USER_TABLE.emailid.value()+" = ?";
	private static String authenticate_user_sql = "SELECT * FROM USERS WHERE ("+
													USER_TABLE.userid.value()+" = ? OR "+
													USER_TABLE.emailid.value()+" = ?) AND "+
													USER_TABLE.pswd.value()+" = ? ";
																	
	private static String search_all_users_sql = "SELECT * FROM USERS";
	
//	private static String search_user_by_any_param_sql = "SELECT * "+
//															"FROM USERS "+
//															"WHERE "+USER_TABLE.username.value()+" = ? OR "+
//																	USER_TABLE.topicid.value()+" = ? OR "+
//																	USER_TABLE.title.value()+" = ? OR "+
//																	USER_TABLE.title.value()+" = ? OR ";
		
	static Logger logger = LogManager.getLogger(UsersDAO.class.getName());
	
	public enum USER_TABLE
	{
		id("user_record_id"),
		userid("userid"),
		emailid("emailid"),
		pswd("pswd"),
		roles("roles"),
		firstName("first_name"),
		middleName("middle_name"),
		lastName("last_name"),
		addressLine1("address_line1"),
		addressLine2("address_line2"),
		city("city"),
		state("state"),
		postalcode("postalcode"),
		country("country"),
		cellPhone("cell_phone"),
		ok_to_text("ok_to_text"),
		landPhone("land_phone"),
		officePhone("office_phone"),
		officePhoneExt("office_phone_ext"),
		lastUpdatedBy("last_updated_by"),
		changePswd("change_pswd"),
		accountDeleted("account_deleted"),
		accountDisabled("account_disabled"),
		lastLoginIp("last_login_ip"),
		lastUpdatedOn("last_updated_on");

		private String name;       
	    private USER_TABLE(String s) 
	    {
	        name = s;
	    }
		
	    public String value() 
	    {
	        return this.name;
	    }
		
	};
	
	public static User mapRow(ResultSet resultSet) throws SQLException, IOException, DatatypeConfigurationException 
	{
		User user = new User();		
		
		user.setId(resultSet.getString(USER_TABLE.id.value()));
		user.setUserId(resultSet.getString(USER_TABLE.userid.value()));
		user.setEmailId(resultSet.getString(USER_TABLE.emailid.value()));
		user.setUserPswd(resultSet.getString(USER_TABLE.pswd.value()));		
		user.getUserRoles().addAll(DaoUtils.convertStringToUserRoles(resultSet.getString(USER_TABLE.roles.value())));
		user.setFirstName(resultSet.getString(USER_TABLE.firstName.value()));
		user.setMiddleName(resultSet.getString(USER_TABLE.middleName.value()));
		user.setLastName(resultSet.getString(USER_TABLE.lastName.value()));
		
//		Address address = new Address();
//		user.setAddress(address);
		user.setAddressLine1(resultSet.getString(USER_TABLE.addressLine1.value()));
		user.setAddressLine2(resultSet.getString(USER_TABLE.addressLine2.value()));
		user.setCity(resultSet.getString(USER_TABLE.city.value()));
		user.setState(resultSet.getString(USER_TABLE.state.value()));
		user.setPostalcode(resultSet.getString(USER_TABLE.postalcode.value()));
		user.setCountry(resultSet.getString(USER_TABLE.country.value()));
		
		user.setCellphone(resultSet.getString(USER_TABLE.cellPhone.value()));
		user.setOkToText(resultSet.getBoolean(USER_TABLE.ok_to_text.value()));
		user.setLandlinephone(resultSet.getString(USER_TABLE.landPhone.value()));
		user.setOfficephone(resultSet.getString(USER_TABLE.officePhone.value()));
		user.setOfficephoneExt(resultSet.getString(USER_TABLE.officePhoneExt.value()));
		
		user.setChangePswd(resultSet.getBoolean(USER_TABLE.changePswd.value()));
		user.setAccountDeleted(resultSet.getBoolean(USER_TABLE.accountDeleted.value()));
		user.setAccountDisabled(resultSet.getBoolean(USER_TABLE.accountDisabled.value()));
		
		user.setLastLoginIp(resultSet.getString(USER_TABLE.lastLoginIp.value()));
		user.setUpdatedBy(resultSet.getString(USER_TABLE.lastUpdatedBy.value()));
		user.setLastUpdatedDateTime(DBCommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(USER_TABLE.lastUpdatedOn.value())));

		
		return user;
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UsersRepositoryInterface#getUserById(int)
	 */
	@Override
	public User getUserByUserRecordId(int id) throws Exception
	{								
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		
		return jdbcTemplate.execute(search_users_by_userrecordid_sql,new PreparedStatementCallback<User>()
		{  
			    @Override  
			    public User doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
			    {  			              			    	
			    	try 
			    	{
						stmt.setInt(1, id);
						ResultSet resultSet = stmt.executeQuery();	
						if (resultSet.next()) 
						{
							return mapRow(resultSet);
						}
					} 
			    	catch (Exception e) 
			    	{
						e.printStackTrace();
			    	}
					
					return null;
			    }  
		});
							
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UsersRepositoryInterface#getUserByEmailId(java.lang.String)
	 */
	@Override
	public User getUserByEmailId(String emailid) throws Exception
	{						
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		
		return jdbcTemplate.execute(search_users_by_emailid_sql,new PreparedStatementCallback<User>()
		{  
			    @Override  
			    public User doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
			    {  			              			    	
			    	try 
			    	{
						stmt.setString(1, emailid);
						ResultSet resultSet = stmt.executeQuery();	
						if (resultSet.next()) 
						{
							return mapRow(resultSet);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} 
					
					return null;
			    }  
		});
				
				
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UsersRepositoryInterface#searchUsers(java.lang.String)
	 */
	@Override
	public List<User> searchUsers(String name) throws Exception
	{						
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
						
		return jdbcTemplate.execute(search_all_users_sql,new PreparedStatementCallback<List<User>>()
		{  
			    @Override  
			    public List<User> doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			    	List<User> hits = new ArrayList<User>();
			    	try 
			        {
			    		ResultSet resultSet = stmt.executeQuery();	
						while (resultSet.next()) 
						{
							hits.add(mapRow(resultSet));
						}						
					} 
			        catch (Exception e) 
			        {					
						e.printStackTrace();
					}
			    	
			    	return hits;
			    }  
		});
			
	}
	
		
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UsersRepositoryInterface#insertUser(com.taksila.veda.model.db.usermgmt.v1_0.User)
	 */	
	@Override
	public User insertUser(User user) throws Exception 
	{
		logger.debug("Entering into insertUser():::::");		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);

		KeyHolder holder = new GeneratedKeyHolder();
		 
		jdbcTemplate.update(new PreparedStatementCreator() {           
		 
		    @Override
		    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException 
		    {
		        try 
		        {
					PreparedStatement stmt = connection.prepareStatement(insert_user_sql, Statement.RETURN_GENERATED_KEYS);
					stmt.setString(1, user.getUserId());
					stmt.setString(2, user.getEmailId());
					stmt.setString(3, user.getUserPswd());
					stmt.setString(4, DaoUtils.getStringFromRolesList(user.getUserRoles()));				
					stmt.setString(5, user.getFirstName());
					stmt.setString(6, user.getMiddleName());
					stmt.setString(7, user.getLastName());
					
//					Address addr = user.getAddress() == null ? new Address(): user.getAddress() ;
//					stmt.setString(8, addr.getAddressLine1());
//					stmt.setString(9, addr.getAddressLine2());
//					stmt.setString(10, addr.getCity());
//					stmt.setString(11, addr.getState());
//					stmt.setString(12, addr.getPostalcode());
//					stmt.setString(13, addr.getCountry());
					
					stmt.setString(8, user.getAddressLine1());
					stmt.setString(9, user.getAddressLine2());
					stmt.setString(10, user.getCity());
					stmt.setString(11, user.getState());
					stmt.setString(12, user.getPostalcode());
					stmt.setString(13, user.getCountry());
					
					
					stmt.setString(14, user.getCellphone());
					stmt.setBoolean(15, user.isOkToText());
					stmt.setString(16, user.getLandlinephone());
					stmt.setString(17, user.getOfficephone());
					stmt.setString(18, user.getOfficephoneExt());
					stmt.setString(19, user.getUpdatedBy());	
								
					
					return stmt;
				} 
		        catch (Exception e) 
		        {				
					e.printStackTrace();
					return null;
				}
		    }
			}, holder);
		
		user.setId(holder.getKey().toString());
		return user;
		
						 
								
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UsersRepositoryInterface#updateUser(com.taksila.veda.model.db.usermgmt.v1_0.User)
	 */	
	@Override
	public boolean updateUser(User user) throws Exception 
	{
		logger.debug("Entering into updateUser():::::");		

		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		Boolean insertSuccess = jdbcTemplate.execute(update_user_sql,new PreparedStatementCallback<Boolean>()
		{  
			    @Override  
			    public Boolean doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			        try 
			        {
			        	stmt.setString(1, user.getUserId());
						stmt.setString(2, user.getEmailId());
						stmt.setString(3, user.getUserPswd());
						stmt.setString(4, DaoUtils.getStringFromRolesList(user.getUserRoles()));									
						stmt.setString(5, user.getFirstName());
						stmt.setString(6, user.getMiddleName());
						stmt.setString(7, user.getLastName());
						
//						Address addr = user.getAddress() == null ? new Address(): user.getAddress() ;
//						stmt.setString(8, addr.getAddressLine1());
//						stmt.setString(9, addr.getAddressLine2());
//						stmt.setString(10, addr.getCity());
//						stmt.setString(11, addr.getState());
//						stmt.setString(12, addr.getPostalcode());
//						stmt.setString(13, addr.getCountry());
						
						stmt.setString(8, user.getAddressLine1());
						stmt.setString(9, user.getAddressLine2());
						stmt.setString(10, user.getCity());
						stmt.setString(11, user.getState());
						stmt.setString(12, user.getPostalcode());
						stmt.setString(13, user.getCountry());
						
						stmt.setString(14, user.getCellphone());
						stmt.setBoolean(15, user.isOkToText());
						stmt.setString(16, user.getLandlinephone());
						stmt.setString(17, user.getOfficephone());
						stmt.setString(18, user.getOfficephoneExt());
						stmt.setString(19, user.getUpdatedBy());

						stmt.setString(20, user.getId());
						
						int t = stmt.executeUpdate();
						if (t > 0)
							return true;
						else
							return false;
					} 
			        catch (SQLException e) 
			        {					
						e.printStackTrace();
						return false;
					}  			              
			    }  
		});  
		
		if (!insertSuccess)
			throw new Exception("Unsuccessful in adding an entry into DB, please check logs");
		
		return insertSuccess;
		
		
								
	}
		
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UsersRepositoryInterface#deleteUser(java.lang.String)
	 */
	@Override
	public boolean deleteUser(String id) throws Exception 
	{
		logger.debug("Entering into deleteUser():::::");
				
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		return jdbcTemplate.execute(delete_user_sql,new PreparedStatementCallback<Boolean>()
		{  
			    @Override  
			    public Boolean doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			        try 
			        {
			        	stmt.setInt(1, Integer.parseInt(id));
						int t = stmt.executeUpdate();
						if (t > 0)
							return true;
						else
							return false;
					} 
			        catch (SQLException e) 
			        {					
						e.printStackTrace();
						return false;
					}  			              
			    }  
		}); 
										
	}


	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UsersRepositoryInterface#updateUserImage(java.lang.String, java.io.InputStream, java.lang.String, double)
	 */
	@Override
	public boolean updateUserImage(String userId, InputStream userContentImageIs, String imageType, double scale) {
		// TODO Auto-generated method stub
		return false;
	}


	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UsersRepositoryInterface#readUserImage(int, double)
	 */
	@Override
	public ByteArrayOutputStream readUserImage(int userId, double scale) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UsersRepositoryInterface#getUserByUserId(java.lang.String)
	 */
	@Override
	public User getUserByUserId(String userid) throws Exception
	{								
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		
		return jdbcTemplate.execute(search_users_by_userid_sql,new PreparedStatementCallback<User>()
		{  
			    @Override  
			    public User doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
			    {  			              			    	
			    	try 
			    	{
						logger.trace("about to run sql = "+search_users_by_userid_sql);
			    		stmt.setString(1, userid);
						ResultSet resultSet = stmt.executeQuery();	
						if (resultSet.next()) 
						{
							return mapRow(resultSet);
						}
					} 
			    	catch (Exception e) 
			    	{						
						e.printStackTrace();
					}
					
					return null;
			    }  
		});
				
	}
	
	/*
	 * 
	 */
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UsersRepositoryInterface#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public User authenticate(String userid, String pswd) throws Exception
	{
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		
		return jdbcTemplate.execute(authenticate_user_sql,new PreparedStatementCallback<User>()
		{  
			    @Override  
			    public User doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
			    {  			              			    	
			    	try 
			    	{
						stmt.setString(1, userid);
						stmt.setString(2, userid);
						stmt.setString(3, DBCommonUtils.getSecureHash(pswd));
						ResultSet resultSet = stmt.executeQuery();	
						if (resultSet.next()) 
						{
							return mapRow(resultSet);
						}
					} 
			    	catch (Exception e) 
			    	{						
						e.printStackTrace();
					}
					
					return null;
			    }  
		});
		
		
	}

	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UsersRepositoryInterface#updatePassword(java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	@Override
	public boolean updatePassword(String userId, String passwordHash, Boolean temporary) throws Exception 
	{
		logger.debug("Entering into updatePassword():::::");		
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		Boolean insertSuccess = jdbcTemplate.execute(update_user_password,new PreparedStatementCallback<Boolean>()
		{  
			    @Override  
			    public Boolean doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			        try 
			        {
						stmt.setString(1, passwordHash);
						stmt.setBoolean(2, temporary);
						stmt.setString(3, userId);
									
						int t = stmt.executeUpdate();
						if (t > 0)
							return true;
						else
							return false;
					} 
			        catch (SQLException e) 
			        {					
						e.printStackTrace();
						return false;
					}  			              
			    }  
		});  
		
		if (!insertSuccess)
			throw new Exception("Unsuccessful in adding an entry into DB, please check logs");
		
		return insertSuccess;
		
		
	}

	

}
