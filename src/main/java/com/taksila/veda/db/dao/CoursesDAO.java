/**
 * 
 */
package com.taksila.veda.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.db.SQLDataBaseManager;
import com.taksila.veda.model.api.course.v1_0.Course;

/**
 * @author mahesh
 *
 */
public class CoursesDAO 
{
	private String schoolId = null;	
	private static String insert_course_sql = "INSERT INTO COURSES("+COURSE_TABLE.coursename.value()+","+
																			COURSE_TABLE.title.value()+","+
																			COURSE_TABLE.subTitle.value()+","+
																			COURSE_TABLE.description.value()+") "+
																	"VALUES (?,?,?,?);";		
	
	private static String update_course_sql = "UPDATE COURSES SET "+COURSE_TABLE.coursename.value()+" = ? ,"+
															COURSE_TABLE.title.value()+" = ? ,"+
															COURSE_TABLE.subTitle.value()+" = ? ,"+
															COURSE_TABLE.description.value()+" = ? "+
													" WHERE "+COURSE_TABLE.id.value()+" = ? ";
	
	private static String delete_course_sql = "DELETE FROM COURSES WHERE "+COURSE_TABLE.id.value()+" = ? ";	
	private static String search_course_by_title_sql = "SELECT * FROM COURSES WHERE "+COURSE_TABLE.title.value()+" like ? ";
	private static String search_course_by_id_sql = "SELECT * FROM COURSES WHERE "+COURSE_TABLE.id.value()+" = ? ";	
	
	
	static Logger logger = LogManager.getLogger(CoursesDAO.class.getName());
	SQLDataBaseManager sqlDBManager= null;
	
	public CoursesDAO(String tenantId) 
	{
		logger.trace(" Initializing CoursesDAO............ ");
		this.schoolId = tenantId;		
		
		this.sqlDBManager = new SQLDataBaseManager();
		logger.trace(" Completed initializing CoursesDAO............ ");
		
	}
	
	public enum COURSE_TABLE
	{
		id("id"),
		coursename("name"),
		title("title"),
		subTitle("sub_title"),
		description("description");		
		private String name;       
	    private COURSE_TABLE(String s) 
	    {
	        name = s;
	    }
		
	    public String value() 
	    {
	        return this.name;
	    }
		
	};
	
	
	
	
	private Course mapRow(ResultSet resultSet) throws SQLException 
	{
		logger.debug("Entering into course mapRow():::::");
		Course course = new Course();		
		
		course.setId(resultSet.getInt(COURSE_TABLE.id.value()));
		course.setName(resultSet.getString(COURSE_TABLE.coursename.value()));
		course.setTitle(resultSet.getString(COURSE_TABLE.title.value()));
		course.setSubTitle(resultSet.getString(COURSE_TABLE.subTitle.value()));
		course.setDescription(resultSet.getString(COURSE_TABLE.description.value()));
		
		logger.debug("Leaving from course mapRow():::::");
		return course;
	}
	
	/**
	 * 
	 * @param q
	 * @return
	 * @throws SQLException
	 * @throws NamingException 
	 */
	public List<Course> searchCoursesByTitle(String q) throws SQLException, NamingException
	{
		List<Course> courseHits = new ArrayList<Course>();				
		PreparedStatement stmt = null;		
		try
		{
			this.sqlDBManager.connect();
			stmt = this.sqlDBManager.getPreparedStatement(search_course_by_title_sql);
			stmt.setString(1, q);
			ResultSet resultSet = stmt.executeQuery();	
			while (resultSet.next()) 
			{
				courseHits.add(mapRow(resultSet));
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
		
		return courseHits;
		
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws NamingException 
	 */
	public Course getCoursesById(int id) throws SQLException, NamingException
	{						
		PreparedStatement stmt = null;	
		Course course = null;
		try
		{
			this.sqlDBManager.connect();
			stmt = this.sqlDBManager.getPreparedStatement(search_course_by_id_sql);
			stmt.setInt(1, id);
			ResultSet resultSet = stmt.executeQuery();	
			if (resultSet.next()) 
			{
				course = mapRow(resultSet);
			}
			
			return course;
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
	
		
	/**
	 * 
	 * @param course
	 * @return
	 * @throws Exception
	 */	
	public Course insertCourse(Course course) throws Exception 
	{
		logger.debug("Entering into insertCourse():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(insert_course_sql);
			
			stmt.setString(1, course.getName());
			stmt.setString(2, course.getTitle());
			stmt.setString(3, course.getSubTitle());
			stmt.setString(4, course.getDescription());
			
			stmt.executeUpdate();			
			ResultSet rs = stmt.getGeneratedKeys();			
			if (rs.next())
			{
				course.setId(rs.getInt(1));
			}
			
			return course;
			
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
	
	
	/**
	 * 
	 * @param course
	 * @return
	 * @throws Exception
	 */	
	public boolean updateCourse(Course course) throws Exception 
	{
		logger.debug("Entering into updateCourse():::::");		
		PreparedStatement stmt = null;
		try
		{
			this.sqlDBManager.connect();	
			stmt = this.sqlDBManager.getPreparedStatement(update_course_sql);
			
			stmt.setString(1, course.getName());
			stmt.setString(2, course.getTitle());
			stmt.setString(3, course.getSubTitle());
			stmt.setString(4, course.getDescription());
			stmt.setInt(5, course.getId());
			
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
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCourse(int id) throws Exception 
	{
		logger.debug("Entering into deleteCourse():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(delete_course_sql);
			stmt.setInt(1, id);
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
