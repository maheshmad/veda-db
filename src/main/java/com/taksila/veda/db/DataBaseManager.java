package com.taksila.veda.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;



import java.util.Locale;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.taksila.veda.utils.AdminConfig;
import com.taksila.veda.utils.JDBCUtil;
import com.taksila.veda.utils.ResourceBundleUtils;
public class DataBaseManager 
{
	private Logger logger = Logger.getLogger(DataBaseManager.class);
	
	private Connection conn = null;	
	private Statement st = null;
	private BaseSQLDialect sqlDialect; 
	
	private String instantiatedFromClassName;
	private String dbName;
	
	public enum DB_SQL_DIALECT_TYPE
	{
		DB2,
		MYSQL,
		ORACLE,
		POSTGRES
	}
	
	/**
	 * Default constructor requires you to call connect() and close() explicitly
	 * @throws SQLException 
	 */
	public DataBaseManager(String InstantiatedFromClassName)
	{
		this.instantiatedFromClassName = InstantiatedFromClassName;
		dbName = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_CONFIG_BUNDLE, Locale.UK, AdminConfig.DB_NAME);
		this.setSqlDialect();
	}
		
	private void setSqlDialect() 
	{
		DB_SQL_DIALECT_TYPE dialect = this.readFromConfig();
		
		switch (dialect) 
		{
			case DB2:
				this.sqlDialect = new DB2SQLDialect();
				break;
			case ORACLE:
				this.sqlDialect = new OracleSQLDialect();
				break;
			case MYSQL:
				this.sqlDialect = new MySQLDialect();
				break;
			default:
				this.sqlDialect = new BaseSQLDialect();	
				break;
		}
	}
	
	private DB_SQL_DIALECT_TYPE readFromConfig()
	{
		if(DB_SQL_DIALECT_TYPE.ORACLE.toString().equals(dbName)) {
			return DB_SQL_DIALECT_TYPE.ORACLE;
		} else if(DB_SQL_DIALECT_TYPE.MYSQL.toString().equals(dbName)) {
			return DB_SQL_DIALECT_TYPE.MYSQL;
		}
		
		return null;
	}
	
	public void connect() throws SQLException 
	{		
		try	{
				if (conn == null || conn.isClosed()) 
				{
					logger.debug("############     	OPENING CONNECTION  (in class "+this.instantiatedFromClassName+")  ############");
					conn = JDBCUtil.getConnection(dbName);
				}
				else;
				
			}
			catch(SQLException ex) 
			{
				Calendar cal = Calendar.getInstance();
				logger.debug("**********************************************************************************************");
				logger.debug("*                                                                                            *");
				logger.debug("*                                                                                            *");
				logger.debug("############     COLLECT DB CONNECTION EXCEPTION OCCURED AT "+ cal.getTime()+"   ############");
				logger.debug("Error Code = "+ex.getErrorCode());
				logger.debug("SQL State = "+ex.getSQLState());
				logger.debug("Message = "+ex.getMessage());
				ex.printStackTrace();				
				logger.debug("*                                                                                            *");
				logger.debug("*                                                                                            *");
				logger.debug("**********************************************************************************************");
			}
			catch(Exception ex) {
				this.HandleException("connect()", ex);
			} 
		
	}
	/*
	 * 
	 */
	public void close()
	{
		try
		{
			if (this.st != null && !this.st.isClosed())
				this.st.close();
			else;
						
			if (this.conn != null)
			{	
				if (!this.conn.isClosed())
				{					
				
					logger.debug(this.getClass().getName()+ Level.TRACE + "############     	CLOSING DB CONNECTION   (in class "+this.instantiatedFromClassName+")   ############");
					this.conn.close();
				}
				else;
			}
			else;
			
			this.conn = null;
			this.st = null;
		}
		catch(SQLException ex)
		{
			Calendar cal = Calendar.getInstance();
			logger.debug("**********************************************************************************************");			
			logger.debug("############  COLLECT DB SQL EXCEPTION OCCURED AT "+ cal.getTime()+"   ############");
			logger.debug("Error Code = "+ex.getErrorCode());
			logger.debug("SQL State = "+ex.getSQLState());
			logger.debug("Message = "+ex.getMessage());
			logger.debug("Class = "+this.instantiatedFromClassName);
			ex.printStackTrace();							
			logger.debug("**********************************************************************************************");
		}
		
	}
	/*
	 * 
	 */
	public ResultSet executeQuery(String Query)
	{
		ResultSet rs = null;
		try
		{									
			this.st = conn.createStatement();
			rs = st.executeQuery(Query);			
			return rs;
		}
		catch(SQLException ex)
		{
			this.HandleException("executeQuery()", ex);
		}
		return rs;
	}
	
	/*
	 * 
	 */
	
	public boolean executeUpdate(String Query)
	{
		try
		{			
			this.st = conn.createStatement();
			st.executeUpdate(Query);
		}
		catch(SQLException ex)
		{
			this.HandleException("executeQuery()", ex);
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param SQLStmt
	 * @return
	 */
	
	
	public PreparedStatement getPreparedStatement(String SQLStmt)
	{
		try
		{					
			PreparedStatement PrepStmt = conn.prepareStatement(SQLStmt);
			return PrepStmt;
		}
		catch(SQLException ex)
		{
			this.HandleException("executeQuery()", ex);
			return null;
		}
	}
	
	/**
	 * 
	 * @param ErrorInMethod
	 * @param sqlex
	 */
	private void HandleException(String ErrorInMethod, SQLException sqlex)
	{
		try 
		{
			if (this.st != null && !this.st.isClosed())
				this.st.close();
			else;
			
			if (this.conn != null)
			{
				if (!this.conn.isClosed())
					this.conn.close();
				else;
			}
			else;
		} 
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
			logger.debug("---------COLLECT SQL EXCEPTION LOG START in " + ErrorInMethod + " DataBaseManager class---------");
			sqlex.printStackTrace();
			logger.debug("Error while executing Updating database string "+sqlex.getMessage()+sqlex.getSQLState());
			logger.debug("---------COLLECT SQL EXCEPTION LOG END in  " + ErrorInMethod + " DataBaseManager class---------");
		}	
	}
	
	/**
	 * 
	 * @param ErrorInMethod
	 * @param ex
	 */
	private void HandleException(String ErrorInMethod, Exception ex)
	{
		try 
		{
			if (this.st != null && !this.st.isClosed())
				this.st.close();
			else;
			
			if (this.conn != null)
			{
				if (!this.conn.isClosed())
					this.conn.close();
				else;
			}
			else;
		} 
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
			logger.debug("---------COLLECT ERROR LOG START in " + ErrorInMethod + " DataBaseManager class---------");
			logger.debug("Error while executing Updating database string "+ex.getMessage());
			logger.debug("---------COLLECT ERROR LOG END in  " + ErrorInMethod + " DataBaseManager class---------");
		}	
	}
	
	/**
	 * 
	 * @param autoCommit
	 * @param TransactionType
	 */
	public void setAutoCommit(boolean autoCommit, String TransactionType) 
	{
		if (conn != null) 
		{
			try 
			{
				logger.debug("############     	SETTING AUTOCOMMIT IN DB      ############");
				conn.setAutoCommit(autoCommit);
			} catch (SQLException e) 
			{
				this.HandleException("setAutoCommit()", e);
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Connection getConnection()
	{
		try 
		{
			if (this.conn == null || this.conn.isClosed())
			{				
				this.connect();				
			}
		} 
		catch (SQLException e) 
		{
			this.HandleException("getConnection()", e);
		}
		
		return this.conn;
	}
	
	public BaseSQLDialect getSqlDialect() {
		return sqlDialect;
	}

}
