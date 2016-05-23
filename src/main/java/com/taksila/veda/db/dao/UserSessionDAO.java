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

import com.taksila.veda.db.SQLDataBaseManager;
import com.taksila.veda.model.db.security.v1_0.UserSession;
import com.taksila.veda.utils.CommonUtils;

/**
 * @author Uma
 *
 */
public class UserSessionDAO 
{	
	static Logger logger = LogManager.getLogger(UserSessionDAO.class.getName());	
	private String tenantId = null;
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
	
																	
	public UserSessionDAO(String tenantId) 
	{
		logger.trace(" Initializing UserSessionDAO............ ");
		this.tenantId = tenantId;		
		
		this.sqlDBManager = new SQLDataBaseManager();
		logger.trace(" Completed initializing UserSessionDAO............ ");
	}
	
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
	
	/**
	 * 
	 * @param userSession
	 * @return
	 * @throws Exception 
	 */
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
	
	
	public boolean invalidateUserSession(String sessionid) throws SQLException, NamingException 
	{
		logger.debug("Entering into invalidateUserSession():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
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
