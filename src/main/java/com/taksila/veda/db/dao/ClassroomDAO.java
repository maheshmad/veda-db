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
import com.taksila.veda.db.eventsessions.EventSessionsRepository;
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
		logger.trace(" building EventSessionsRepository for tenant id = "+tenantId);
//    	this.dbManager = applicationContext.getBean(TenantDBManager.class,tenantId);
		this.tenantId = tenantId;
    }
	
	private String schoolId = null;	
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
	SQLDataBaseManager sqlDBManager= null;
	

	
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
		List<Classroom> classroomHits = new ArrayList<Classroom>();				
		PreparedStatement stmt = null;		
		try
		{
			this.sqlDBManager.connect();
			if (StringUtils.isNotBlank(q))
			{
				stmt = this.sqlDBManager.getPreparedStatement(search_classroom_by_title_sql);
				stmt.setString(1, q+"%");
				stmt.setString(2, q+"%");
				stmt.setString(3, q+"%");
			}
			else
				stmt = this.sqlDBManager.getPreparedStatement(search_all_classrooms_sql);
			
			ResultSet resultSet = stmt.executeQuery();	
			while (resultSet.next()) 
			{
				classroomHits.add(mapRow(resultSet));
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
		
		return classroomHits;
		
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.ClassroomRepositoryInterface#getClassroomById(java.lang.String)
	 */
	@Override
	public Classroom getClassroomById(String id) throws Exception
	{						
		PreparedStatement stmt = null;	
		Classroom classroom = null;
		try
		{
			this.sqlDBManager.connect();
			stmt = this.sqlDBManager.getPreparedStatement(search_classroom_by_id_sql);
			stmt.setInt(1, Integer.parseInt(id));
			ResultSet resultSet = stmt.executeQuery();	
			if (resultSet.next()) 
			{
				classroom = mapRow(resultSet);
			}
			
			return classroom;
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
	 * @see com.taksila.veda.db.dao.ClassroomRepositoryInterface#insertClassroom(com.taksila.veda.model.api.classroom.v1_0.Classroom)
	 */	
	@Override
	public Classroom insertClassroom(Classroom classroom) throws Exception 
	{
		logger.debug("Entering into insertClassroom():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(insert_classroom_sql);
			
			stmt.setString(1, classroom.getCourseRecordId());
			stmt.setString(2, classroom.getName());
			stmt.setString(3, classroom.getTitle());
			stmt.setString(4, classroom.getSubTitle());
			stmt.setString(5, classroom.getDescription());
			
			stmt.executeUpdate();			
			ResultSet rs = stmt.getGeneratedKeys();			
			if (rs.next())
			{
				classroom.setId(String.valueOf(rs.getInt(1)));
			}
			
			return classroom;
			
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
	 * @see com.taksila.veda.db.dao.ClassroomRepositoryInterface#updateClassroom(com.taksila.veda.model.api.classroom.v1_0.Classroom)
	 */	
	@Override
	public boolean updateClassroom(Classroom classroom) throws Exception 
	{
		logger.debug("Entering into updateClassroom():::::");		
		PreparedStatement stmt = null;
		try
		{
			this.sqlDBManager.connect();	
			stmt = this.sqlDBManager.getPreparedStatement(update_classroom_sql);
			
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
	 * @see com.taksila.veda.db.dao.ClassroomRepositoryInterface#deleteClassroom(java.lang.String)
	 */
	@Override
	public boolean deleteClassroom(String id) throws Exception 
	{
		logger.debug("Entering into deleteClassroom():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(delete_classroom_sql);
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
