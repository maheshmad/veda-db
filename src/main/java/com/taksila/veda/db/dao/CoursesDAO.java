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
import com.taksila.veda.model.api.course.v1_0.Course;

/**
 * @author mahesh
 *
 */

@Repository
@Scope(value="prototype")
@Lazy(value = true)
public class CoursesDAO implements CoursesRepositoryInterface 
{
	@Autowired
	private TenantDBManager tenantDBManager;
	private String tenantId;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
    public CoursesDAO(@Value("tenantId") String tenantId)
    {
		logger.trace(" building for tenant id = "+tenantId);
		this.tenantId = tenantId;
    }
	
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
	private static String search_course_by_title_sql = "SELECT * FROM COURSES "
												+ "WHERE "+COURSE_TABLE.title.value()+" like ? "
												+ "OR "+COURSE_TABLE.coursename.value()+" like ? "
												+ "OR "+COURSE_TABLE.subTitle.value()+" like ? ";
	
	private static String search_all_courses_sql = "SELECT * FROM COURSES";
	private static String search_course_by_id_sql = "SELECT * FROM COURSES WHERE "+COURSE_TABLE.id.value()+" = ? ";	
	
	
	static Logger logger = LogManager.getLogger(CoursesDAO.class.getName());
	SQLDataBaseManager sqlDBManager= null;
	
	
	
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
		Course course = new Course();		
		
		course.setId(String.valueOf(resultSet.getInt(COURSE_TABLE.id.value())));
		course.setName(resultSet.getString(COURSE_TABLE.coursename.value()));
		course.setTitle(resultSet.getString(COURSE_TABLE.title.value()));
		course.setSubTitle(resultSet.getString(COURSE_TABLE.subTitle.value()));
		course.setDescription(resultSet.getString(COURSE_TABLE.description.value()));
		
		return course;
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.CoursesRepositoryInterface#searchCoursesByTitle(java.lang.String)
	 */
	@Override
	public List<Course> searchCoursesByTitle(String q) throws Exception
	{
		List<Course> courseHits = new ArrayList<Course>();				
		PreparedStatement stmt = null;		
		try
		{
			this.sqlDBManager.connect();
			if (StringUtils.isNotBlank(q))
			{
				stmt = this.sqlDBManager.getPreparedStatement(search_course_by_title_sql);
				stmt.setString(1, q+"%");
				stmt.setString(2, q+"%");
				stmt.setString(3, q+"%");
			}
			else
				stmt = this.sqlDBManager.getPreparedStatement(search_all_courses_sql);
			
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
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.CoursesRepositoryInterface#getCoursesById(java.lang.String)
	 */
	@Override
	public Course getCoursesById(String id) throws Exception
	{						
		PreparedStatement stmt = null;	
		Course course = null;
		try
		{
			this.sqlDBManager.connect();
			stmt = this.sqlDBManager.getPreparedStatement(search_course_by_id_sql);
			stmt.setInt(1, Integer.parseInt(id));
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
	
		
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.CoursesRepositoryInterface#insertCourse(com.taksila.veda.model.api.course.v1_0.Course)
	 */	
	@Override
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
				course.setId(String.valueOf(rs.getInt(1)));
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
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.CoursesRepositoryInterface#updateCourse(com.taksila.veda.model.api.course.v1_0.Course)
	 */	
	@Override
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
			stmt.setInt(5, Integer.valueOf(course.getId()));
			
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
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.CoursesRepositoryInterface#deleteCourse(java.lang.String)
	 */
	@Override
	public boolean deleteCourse(String id) throws Exception 
	{
		logger.debug("Entering into deleteCourse():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(delete_course_sql);
			stmt.setInt(1, Integer.parseInt(id));
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
