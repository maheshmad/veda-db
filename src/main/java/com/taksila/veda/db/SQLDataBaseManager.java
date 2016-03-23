package com.taksila.veda.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SQLDataBaseManager 
{
	private static Logger logger = LogManager.getLogger(SQLDataBaseManager.class.getName());
	
	private Connection conn = null;		
	private BaseSQLDialect sqlDialect; 		
	private String instantiatedFromClassName = "";
	private Map<String, String> dbProperties= null;
	
	public enum DB_SQL_DIALECT_TYPE
	{
		DB2,
		MYSQL,
		ORACLE,
		POSTGRES
	}
	
	public enum DB_PROPERTIES
	{
		DB_SQL_JDBC_HOST_URL,
		DB_SQL_JDBC_UID,
		DB_SQL_JDBC_PWD,
		DB_SQL_DIALECT_TYPE,
		INSTANTIATED_FROM_CLASS
	}
		
	
	private void setSqlDialect() 
	{				
		logger.trace("setting sql dialect for "+this.dbProperties.get(DB_PROPERTIES.DB_SQL_DIALECT_TYPE.name()));
		try
		{
			DB_SQL_DIALECT_TYPE dialect = DB_SQL_DIALECT_TYPE.valueOf(this.dbProperties.get(DB_PROPERTIES.DB_SQL_DIALECT_TYPE.name()));
			switch (dialect) 
			{			
				case MYSQL:
					this.sqlDialect = new MySQLDialect();
					break;
				default:
					this.sqlDialect = new BaseSQLDialect();	
					break;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void connect() throws SQLException, NamingException 
	{		
		try	
		{
				if (conn == null || conn.isClosed()) 
				{
					logger.debug("############     	OPENING CONNECTION  (in class "+this.instantiatedFromClassName+")  ############");					

					Context initContext = new InitialContext();
					Context envContext  = (Context)initContext.lookup("java:/comp/env");
					DataSource ds = (DataSource)envContext.lookup("jdbc/xe1");
					conn = ds.getConnection();

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
				throw ex;
			}
			catch(NamingException nex)
			{
				nex.printStackTrace();
				throw nex;
			}
			catch(Exception ex) 
			{
				this.HandleException("connect()", ex);
				throw ex;
			} 
		
	}
	/*
	 * 
	 */
	public void close() throws SQLException
	{
		try
		{			
			if (this.conn != null)
			{	
				if (!this.conn.isClosed())
				{					
				
					logger.debug("############     	CLOSING DB CONNECTION   (in class "+this.instantiatedFromClassName+")   ############");
					this.conn.close();
				}
				else;
			}
			else;
			
			this.conn = null;		
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
			throw ex;
		}
		
	}
	
	public void close(PreparedStatement stmt)
	{
		try
		{			
			if (stmt != null)
			{
				stmt.close();
			}
		}
		catch(SQLException ex)
		{
			Calendar cal = Calendar.getInstance();
			logger.debug("**********************************************************************************************");			
			logger.debug("############   DB SQL EXCEPTION OCCURED AT "+ cal.getTime()+"   ############");
			logger.debug("Error Code = "+ex.getErrorCode());
			logger.debug("SQL State = "+ex.getSQLState());
			logger.debug("Message = "+ex.getMessage());
			logger.debug("Class = "+this.instantiatedFromClassName);
			ex.printStackTrace();							
			logger.debug("**********************************************************************************************");
		}
		finally
		{
			try 
			{
				this.close();
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 
	 */
	public ResultSet executeQuery(String Query)
	{
		ResultSet rs = null;
		Statement st = null;
		try
		{									
			st = conn.createStatement();
			rs = st.executeQuery(Query);			
			return rs;
		}
		catch(SQLException ex)
		{
			this.HandleException("executeQuery()", ex);
		}
		finally
		{
			try 
			{
				if (st != null)
					st.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		return rs;
	}
	
	/*
	 * 
	 */
	
	public boolean executeUpdate(String Query)
	{
		Statement st = null;
		try
		{			
			st = conn.createStatement();
			st.executeUpdate(Query);
		}
		catch(SQLException ex)
		{
			this.HandleException("executeQuery()", ex);
			return false;
		}
		finally
		{
			try 
			{
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		PreparedStatement prepStmt = null;
		try
		{					
			prepStmt = conn.prepareStatement(SQLStmt);
			return prepStmt;
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
	
	
	
	
	public BaseSQLDialect getSqlDialect() {
		return sqlDialect;
	}

}
