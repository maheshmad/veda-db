/**
 * 
 */
package com.taksila.veda.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

import com.taksila.veda.db.utils.TenantDBManager;
import com.taksila.veda.model.api.classroom.v1_0.Classroom;
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
	
	
	
	@Override
	public Course mapRow(ResultSet resultSet) throws SQLException 
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
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		
		String sql = ""; 
				
		if (StringUtils.isNotBlank(q))		
			sql = search_course_by_title_sql;					
		else
			sql = search_all_courses_sql;
		
		
		return jdbcTemplate.execute(sql,new PreparedStatementCallback<List<Course>>()
		{  
			    @Override  
			    public List<Course> doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			    	List<Course> hits = new ArrayList<Course>();
			    	try 
			        {
			    		if (StringUtils.isNotBlank(q))
						{
							stmt.setString(1, q+"%");
							stmt.setString(2, q+"%");
							stmt.setString(3, q+"%");
						}
						else;
										    					    		
						ResultSet resultSet = stmt.executeQuery();
						while (resultSet.next()) 
						{
							hits.add(mapRow(resultSet));
						}
					} 
			        catch (SQLException e) 
			        {					
						e.printStackTrace();
					}
			    	
			    	return hits;
			    }  
		});
		
		
		
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.CoursesRepositoryInterface#getCoursesById(java.lang.String)
	 */
	@Override
	public Course getCoursesById(String id) throws Exception
	{						
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		
		return jdbcTemplate.execute(search_course_by_id_sql,new PreparedStatementCallback<Course>()
		{  
			    @Override  
			    public Course doInPreparedStatement(PreparedStatement ps) throws SQLException  			            
			    {  			              			    	
					ps.setInt(1, Integer.parseInt(id));
					ResultSet resultSet = ps.executeQuery();	
					if (resultSet.next()) 
					{
						return mapRow(resultSet);
					}
					
					return null;
			    }  
		});
		
				
	}
	
		
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.CoursesRepositoryInterface#insertCourse(com.taksila.veda.model.api.course.v1_0.Course)
	 */	
	@Override
	public Course insertCourse(Course course) throws Exception 
	{
		logger.debug("Entering into insertCourse():::::");
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		KeyHolder holder = new GeneratedKeyHolder();
		 
		jdbcTemplate.update(new PreparedStatementCreator() {           
		 
		    @Override
		    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException 
		    {
		        PreparedStatement stmt = connection.prepareStatement(insert_course_sql, Statement.RETURN_GENERATED_KEYS);
		        stmt.setString(1, course.getName());
				stmt.setString(2, course.getTitle());
				stmt.setString(3, course.getSubTitle());
				stmt.setString(4, course.getDescription());			   
		        return stmt;
		    }
			}, holder);
		
		course.setId(holder.getKey().toString());
		return course;
		
		
		
		 
								
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.CoursesRepositoryInterface#updateCourse(com.taksila.veda.model.api.course.v1_0.Course)
	 */	
	@Override
	public boolean updateCourse(Course course) throws Exception 
	{
		logger.debug("Entering into updateCourse():::::");	
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		Boolean insertSuccess = jdbcTemplate.execute(update_course_sql,new PreparedStatementCallback<Boolean>()
		{  
			    @Override  
			    public Boolean doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              			    	
					try 
					{
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
					catch (NumberFormatException e) 
					{					
						e.printStackTrace();
					} 
					catch (SQLException e) {
						e.printStackTrace();
					}
					
					return false;
			    }  
		});  
		
		if (!insertSuccess)
			throw new Exception("Unsuccessful in adding an entry into DB, please check logs");
		
		return insertSuccess;
				
								
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.CoursesRepositoryInterface#deleteCourse(java.lang.String)
	 */
	@Override
	public boolean deleteCourse(String id) throws Exception 
	{
		logger.debug("Entering into deleteCourse():::::");
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		return jdbcTemplate.execute(delete_course_sql,new PreparedStatementCallback<Boolean>()
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
	

}
