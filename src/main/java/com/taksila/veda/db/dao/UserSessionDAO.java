/**
 * 
 */
package com.taksila.veda.db.dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.xml.datatype.DatatypeConfigurationException;

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
import com.taksila.veda.model.db.security.v1_0.UserSession;
import com.taksila.veda.utils.CommonUtils;

/**
 * @author Mahesh 
 *
 */
@Repository
@Scope(value="prototype")
@Lazy(value = true)
public class UserSessionDAO implements UserSessionRepositoryInterface 
{
	@Autowired
	private TenantDBManager tenantDBManager;
	private String tenantId;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
    public UserSessionDAO(@Value("tenantId") String tenantId)
    {
		logger.trace(" building for tenant id = "+tenantId);
		this.tenantId = tenantId;
    }
	
	SQLDataBaseManager sqlDBManager= null;
	
	public enum USER_SESSIONS_TABLE
	{
		sessionid("sessionid"),
		userid("userid"),
		expiresOn("expires_on"),
		ipAddr("ip_addr"),
		client("client_info"),
		lastUpdatedOn("last_updated_on");

		private String name;       
	    private USER_SESSIONS_TABLE(String s) 
	    {
	        name = s;
	    }
		
	    public String value() 
	    {
	        return this.name;
	    }
		
	};
	private static String insert_user_session_sql = "REPLACE INTO USER_SESSIONS("+USER_SESSIONS_TABLE.userid.value()+","+	
																		USER_SESSIONS_TABLE.sessionid.value()+","+																		
																		USER_SESSIONS_TABLE.client.value()+","+
																		USER_SESSIONS_TABLE.expiresOn.value()+","+
																		USER_SESSIONS_TABLE.ipAddr.value()+
																	") "+
																	"VALUES (?,?,?,?,?) ";
//																	"ON DUPLICATE KEY UPDATE "+
//																	USER_SESSIONS_TABLE.userid.value()+"=VALUES("+USER_SESSIONS_TABLE.userid.value()+");";		
//			
//	private static String update_user_session_sql = "UPDATE USER_SESSIONS SET "+ USER_SESSIONS_TABLE.sessionid.value()+" = ? ,"+
//																	USER_SESSIONS_TABLE.sessionid.value()+" = ? ,"+
//																	USER_SESSIONS_TABLE.userid.value()+" = ? ,"+
//																	USER_SESSIONS_TABLE.client.value()+" = ? ,"+
//																	USER_SESSIONS_TABLE.expiresOn.value()+" = ? ,"+
//																	USER_SESSIONS_TABLE.ipAddr.value()+" = ? "+
//															" WHERE "+USER_SESSIONS_TABLE.userid.value()+" = ? ";
	
	private static String delete_user_session_sql = "DELETE FROM USER_SESSIONS WHERE "+USER_SESSIONS_TABLE.sessionid.value()+" = ? ";
	
	private static String search_users_session_by_id_sql = "SELECT * FROM USER_SESSIONS WHERE "+USER_SESSIONS_TABLE.sessionid.value()+" = ? ";
	
	private static String search_users_session_sql = "SELECT * FROM USER_SESSIONS WHERE "+USER_SESSIONS_TABLE.sessionid.value()+" = ? AND "
																		+ USER_SESSIONS_TABLE.userid.value()+" = ?";
	
	
	
	private UserSession mapRow(ResultSet resultSet) throws SQLException, IOException, DatatypeConfigurationException 
	{
		UserSession userSession = new UserSession();		
		
		userSession.setId(resultSet.getString(USER_SESSIONS_TABLE.sessionid.value()));
		userSession.setClient(resultSet.getString(USER_SESSIONS_TABLE.client.value()));
		userSession.setIpAddr(resultSet.getString(USER_SESSIONS_TABLE.ipAddr.value()));
		userSession.setUserId(resultSet.getString(USER_SESSIONS_TABLE.userid.value()));		
		userSession.setExpiresOn(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(USER_SESSIONS_TABLE.expiresOn.value())));
		userSession.setLastLoginOn(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(USER_SESSIONS_TABLE.lastUpdatedOn.value())));
				
		return userSession;
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UserSessionRepositoryInterface#addSession(com.taksila.veda.model.db.security.v1_0.UserSession)
	 */
	@Override
	public boolean addSession(UserSession userSession) throws Exception
	{		
		logger.debug("Entering into authorizeSession():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(insert_user_session_sql);
						
			stmt.setString(1, userSession.getUserId());
			stmt.setString(2, userSession.getId());
			stmt.setString(3, userSession.getClient());
			stmt.setTimestamp(4, CommonUtils.geSQLDateTimestamp(userSession.getExpiresOn()));
			stmt.setString(5, userSession.getIpAddr());			
									
			int updates = stmt.executeUpdate();			
			if (updates > 0)			
				return true;
			else
				return false;
			
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();			
			throw ex;
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
	 * @see com.taksila.veda.db.dao.UserSessionRepositoryInterface#getValidSession(java.lang.String)
	 */
	@Override
	public UserSession getValidSession(String sessionid) throws Exception
	{
		logger.debug("Entering into getValidSession():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		UserSession userSession = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(search_users_session_by_id_sql);			
			stmt.setString(1, sessionid);
										
			ResultSet resultSet = stmt.executeQuery();	
			if (resultSet.next()) 
			{
				userSession = mapRow(resultSet);
			}
			
			return userSession;
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();			
			throw ex;
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
	 * @see com.taksila.veda.db.dao.UserSessionRepositoryInterface#invalidateUserSession(java.lang.String)
	 */
	@Override
	public boolean invalidateUserSession(String sessionid) throws Exception 
	{
		logger.debug("Entering into invalidateUserSession():::::");
		
		PreparedStatement stmt = null;
		try
		{
			this.sqlDBManager.connect();
			stmt = this.sqlDBManager.getPreparedStatement(delete_user_session_sql);
			stmt.setString(1, sessionid);			
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
