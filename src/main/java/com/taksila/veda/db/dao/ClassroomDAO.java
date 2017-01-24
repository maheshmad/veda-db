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

/**
 * @author mahesh
 *
 */

@Repository
@Scope(value="prototype")
@Lazy(value = true)
public class ClassroomDAO implements ClassroomRepositoryInterface {
	@Autowired
	private TenantDBManager tenantDBManager;
	private String tenantId;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
    public ClassroomDAO(@Value("tenantId") String tenantId)
    {
		logger.trace(" building ClassroomDAO for tenant id = "+tenantId);
//    	this.dbManager = applicationContext.getBean(TenantDBManager.class,tenantId);
		this.tenantId = tenantId;
    }
	
	private static String insert_classroom_sql = "INSERT INTO CLASSROOM("+CLASSROOM_TABLE.classname.value()+","+
																			CLASSROOM_TABLE.courseRecordId.value()+","+
																			CLASSROOM_TABLE.title.value()+","+
																			CLASSROOM_TABLE.subTitle.value()+","+
																			CLASSROOM_TABLE.description.value()+") "+
																	"VALUES (?,?,?,?,?);";		
	
	private static String update_classroom_sql = "UPDATE CLASSROOM SET "+CLASSROOM_TABLE.classname.value()+" = ? ,"+
															CLASSROOM_TABLE.courseRecordId.value()+" = ? ,"+
															CLASSROOM_TABLE.title.value()+" = ? ,"+
															CLASSROOM_TABLE.subTitle.value()+" = ? ,"+
															CLASSROOM_TABLE.description.value()+" = ? "+
													" WHERE "+CLASSROOM_TABLE.id.value()+" = ? ";
	
	private static String delete_classroom_sql = "DELETE FROM CLASSROOM WHERE "+CLASSROOM_TABLE.id.value()+" = ? ";	
	private static String search_classroom_by_title_sql = "SELECT * FROM CLASSROOM "
												+ "WHERE "+CLASSROOM_TABLE.title.value()+" like ? "
												+ "OR "+CLASSROOM_TABLE.classname.value()+" like ? "
												+ "OR "+CLASSROOM_TABLE.subTitle.value()+" like ? ";
	
	private static String search_all_classrooms_sql = "SELECT * FROM CLASSROOM";
	private static String search_classroom_by_id_sql = "SELECT * FROM CLASSROOM WHERE "+CLASSROOM_TABLE.id.value()+" = ? ";	
	
	
	static Logger logger = LogManager.getLogger(ClassroomDAO.class.getName());	

	
	public enum CLASSROOM_TABLE
	{
		id("classroomid"),
		courseRecordId("course_record_id"),
		classname("name"),
		title("title"),
		subTitle("sub_title"),
		description("description");		
		private String name;       
	    private CLASSROOM_TABLE(String s) 
	    {
	        name = s;
	    }
		
	    public String value() 
	    {
	        return this.name;
	    }
		
	};
			
	public static Classroom mapRow(ResultSet resultSet) throws SQLException 
	{
		Classroom classroom = new Classroom();		
		
		classroom.setId(String.valueOf(resultSet.getInt(CLASSROOM_TABLE.id.value())));
		classroom.setCourseRecordId(String.valueOf(resultSet.getString(CLASSROOM_TABLE.courseRecordId.value())));
		classroom.setName(resultSet.getString(CLASSROOM_TABLE.classname.value()));
		classroom.setTitle(resultSet.getString(CLASSROOM_TABLE.title.value()));
		classroom.setSubTitle(resultSet.getString(CLASSROOM_TABLE.subTitle.value()));
		classroom.setDescription(resultSet.getString(CLASSROOM_TABLE.description.value()));
		
		return classroom;
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.ClassroomRepositoryInterface#searchClassroomsByTitle(java.lang.String)
	 */
	@Override
	public List<Classroom> searchClassroomsByTitle(String q) throws Exception
	{
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		
		String sql = ""; 
				
		if (StringUtils.isNotBlank(q))		
			sql = search_classroom_by_title_sql;					
		else
			sql = search_all_classrooms_sql;
		
		
		return jdbcTemplate.execute(sql,new PreparedStatementCallback<List<Classroom>>()
		{  
			    @Override  
			    public List<Classroom> doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			    	List<Classroom> hits = new ArrayList<Classroom>();
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
	 * @see com.taksila.veda.db.dao.ClassroomRepositoryInterface#getClassroomById(java.lang.String)
	 */
	@Override
	public Classroom getClassroomById(String id) throws Exception
	{						
		logger.trace("searching chapters by courseid ="+id);
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		
		return jdbcTemplate.execute(search_classroom_by_id_sql,new PreparedStatementCallback<Classroom>()
		{  
			    @Override  
			    public Classroom doInPreparedStatement(PreparedStatement ps) throws SQLException  			            
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
	 * @see com.taksila.veda.db.dao.ClassroomRepositoryInterface#insertClassroom(com.taksila.veda.model.api.classroom.v1_0.Classroom)
	 */	
	@Override
	public Classroom insertClassroom(Classroom classroom) throws Exception 
	{
		logger.debug("Entering into insertClassroom():::::");
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		KeyHolder holder = new GeneratedKeyHolder();
		 
		jdbcTemplate.update(new PreparedStatementCreator() {           
		 
		    @Override
		    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException 
		    {
		        PreparedStatement stmt = connection.prepareStatement(insert_classroom_sql, Statement.RETURN_GENERATED_KEYS);
		        stmt.setString(1, classroom.getCourseRecordId());
				stmt.setString(2, classroom.getName());
				stmt.setString(3, classroom.getTitle());
				stmt.setString(4, classroom.getSubTitle());
				stmt.setString(5, classroom.getDescription());				   
		        return stmt;
		    }
			}, holder);
		
		classroom.setId(holder.getKey().toString());
		return classroom;
		
		
		
						
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.ClassroomRepositoryInterface#updateClassroom(com.taksila.veda.model.api.classroom.v1_0.Classroom)
	 */	
	@Override
	public boolean updateClassroom(Classroom classroom) throws Exception 
	{
		logger.debug("Entering into updateClassroom():::::");		
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		Boolean insertSuccess = jdbcTemplate.execute(update_classroom_sql,new PreparedStatementCallback<Boolean>()
		{  
			    @Override  
			    public Boolean doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			        try 
			        {
			        	stmt.setString(1, classroom.getCourseRecordId());
						stmt.setString(2, classroom.getName());
						stmt.setString(3, classroom.getTitle());
						stmt.setString(4, classroom.getSubTitle());
						stmt.setString(5, classroom.getDescription());
						stmt.setInt(6, Integer.valueOf(classroom.getId()));
						
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
	 * @see com.taksila.veda.db.dao.ClassroomRepositoryInterface#deleteClassroom(java.lang.String)
	 */
	@Override
	public boolean deleteClassroom(String id) throws Exception 
	{
		logger.debug("Entering into deleteClassroom():::::");
				
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		return jdbcTemplate.execute(delete_classroom_sql,new PreparedStatementCallback<Boolean>()
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
