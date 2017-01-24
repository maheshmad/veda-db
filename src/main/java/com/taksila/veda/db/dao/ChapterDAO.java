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
import com.taksila.veda.model.api.course.v1_0.Chapter;

/**
 * @author mahesh
 *
 */
@Repository
@Scope(value="prototype")
@Lazy(value = true)
public class ChapterDAO implements ChapterRepositoryInterface 
{
	@Autowired
	private TenantDBManager tenantDBManager;
	static Logger logger = LogManager.getLogger(ChapterDAO.class.getName());
	private String tenantId;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
    public ChapterDAO(@Value("tenantId") String tenantId)
    {
		logger.trace(" building ChapterDAO for tenant id = "+tenantId);
//    	this.dbManager = applicationContext.getBean(TenantDBManager.class,tenantId);
		this.tenantId = tenantId;
    }

	private static String insert_chapter_sql = "INSERT INTO CHAPTERS("+CHAPTER_TABLE.courseid.value()+","+
																			CHAPTER_TABLE.chaptername.value()+","+
																			CHAPTER_TABLE.title.value()+","+
																			CHAPTER_TABLE.subTitle.value()+","+
																			CHAPTER_TABLE.description.value()+") "+
																	"VALUES (?,?,?,?,?);";		
	
	private static String update_chapter_sql = "UPDATE CHAPTERS SET "+CHAPTER_TABLE.chaptername.value()+" = ? ,"+
															CHAPTER_TABLE.title.value()+" = ? ,"+
															CHAPTER_TABLE.subTitle.value()+" = ? ,"+
															CHAPTER_TABLE.description.value()+" = ? "+
															CHAPTER_TABLE.courseid.value()+" = ? "+
													" WHERE "+CHAPTER_TABLE.id.value()+" = ? ";
	
	private static String delete_chapter_sql = "DELETE FROM CHAPTERS WHERE "+CHAPTER_TABLE.id.value()+" = ? ";	
	private static String search_chapter_by_title_sql = "SELECT * FROM CHAPTERS WHERE "+CHAPTER_TABLE.title.value()+" like ? ";
	private static String search_chapter_by_id_sql = "SELECT * FROM CHAPTERS WHERE "+CHAPTER_TABLE.id.value()+" = ? ";
	private static String search_chapter_by_courseid_sql = "SELECT * FROM CHAPTERS WHERE "+CHAPTER_TABLE.courseid.value()+" = ? ";
	private static String search_all_chapters_sql = "SELECT * FROM CHAPTERS";
	
	public enum CHAPTER_TABLE
	{
		id("id"),
		courseid("courseid"),
		chaptername("name"),
		title("title"),
		subTitle("sub_title"),
		description("description");		
		private String name;       
	    private CHAPTER_TABLE(String s) 
	    {
	        name = s;
	    }
		
	    public String value() 
	    {
	        return this.name;
	    }
		
	};
	
	@Override
	public Chapter rowMapper(ResultSet resultSet) throws SQLException 
	{
		Chapter chapter = new Chapter();		
		
		chapter.setId(String.valueOf(resultSet.getInt(CHAPTER_TABLE.id.value())));
		chapter.setName(resultSet.getString(CHAPTER_TABLE.chaptername.value()));
		chapter.setTitle(resultSet.getString(CHAPTER_TABLE.title.value()));
		chapter.setSubTitle(resultSet.getString(CHAPTER_TABLE.subTitle.value()));
		chapter.setDescription(resultSet.getString(CHAPTER_TABLE.description.value()));
		
		return chapter;
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.ChapterRepositoryInterface#searchChaptersByCourseId(java.lang.String)
	 */
	@Override
	public List<Chapter> searchChaptersByCourseId(String courseid) throws Exception
	{			
		logger.trace("searching chapters by courseid ="+courseid);
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		
		return jdbcTemplate.execute(search_chapter_by_courseid_sql,new PreparedStatementCallback<List<Chapter>>()
		{  
			    @Override  
			    public List<Chapter> doInPreparedStatement(PreparedStatement ps)  			            
			    {  			              
			    	List<Chapter> hits = new ArrayList<Chapter>();
			    	try 
			        {
			        	ps.setInt(1, Integer.parseInt(courseid)); 						
						ResultSet resultSet = ps.executeQuery();
						while (resultSet.next()) 
						{
							hits.add(rowMapper(resultSet));
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
	 * @see com.taksila.veda.db.dao.ChapterRepositoryInterface#searchChaptersByTitle(java.lang.String)
	 */
	@Override
	public List<Chapter> searchChaptersByTitle(String q) throws Exception
	{
		logger.trace("searching chapters by query ="+q);
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		
		String sql = ""; 
				
		if (StringUtils.isNotBlank(q))		
			sql = search_chapter_by_title_sql;					
		else
			sql = search_all_chapters_sql;
		
		
		return jdbcTemplate.execute(sql,new PreparedStatementCallback<List<Chapter>>()
		{  
			    @Override  
			    public List<Chapter> doInPreparedStatement(PreparedStatement ps)  			            
			    {  			              
			    	List<Chapter> hits = new ArrayList<Chapter>();
			    	try 
			        {
			    		if (StringUtils.isNotBlank(q))
						{
							ps.setString(1, q+"%");
						}
						else;
										    					    		
						ResultSet resultSet = ps.executeQuery();
						while (resultSet.next()) 
						{
							hits.add(rowMapper(resultSet));
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
	 * @see com.taksila.veda.db.dao.ChapterRepositoryInterface#getChapterById(java.lang.String)
	 */
	@Override
	public Chapter getChapterById(String id) throws Exception
	{								
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
				
		return jdbcTemplate.execute(search_chapter_by_id_sql,new PreparedStatementCallback<Chapter>()
		{  
			    @Override  
			    public Chapter doInPreparedStatement(PreparedStatement ps) throws SQLException  			            
			    {  			              			    	
					ps.setInt(1, Integer.parseInt(id));
					ResultSet resultSet = ps.executeQuery();	
					if (resultSet.next()) 
					{
						return rowMapper(resultSet);
					}
					
					return null;
			    }  
		});
				
	}
	
		
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.ChapterRepositoryInterface#insertChapter(com.taksila.veda.model.api.course.v1_0.Chapter)
	 */	
	@Override
	public Chapter insertChapter(Chapter chapter) throws Exception 
	{
		logger.debug("Entering into insertChapter():::::");
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		KeyHolder holder = new GeneratedKeyHolder();
		 
		jdbcTemplate.update(new PreparedStatementCreator() {           
		 
		    @Override
		    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException 
		    {
		        PreparedStatement stmt = connection.prepareStatement(insert_chapter_sql, Statement.RETURN_GENERATED_KEYS);
		        stmt.setInt(1, Integer.parseInt(chapter.getCourseid()));
				stmt.setString(2, chapter.getName());
				stmt.setString(3, chapter.getTitle());
				stmt.setString(4, chapter.getSubTitle());
				stmt.setString(5, chapter.getDescription());	    
		        return stmt;
		    }
			}, holder);
		
		chapter.setId(holder.getKey().toString());
		return chapter;
//		
//		return jdbcTemplate.execute(insert_chapter_sql,new PreparedStatementCallback<Chapter>()
//		{  
//			    @Override  
//			    public Chapter doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
//			    {  			              			    	
//					stmt.setInt(1, Integer.parseInt(chapter.getCourseid()));
//					stmt.setString(2, chapter.getName());
//					stmt.setString(3, chapter.getTitle());
//					stmt.setString(4, chapter.getSubTitle());
//					stmt.setString(5, chapter.getDescription());					
//					stmt.executeUpdate();			
//					ResultSet rs = stmt.getGeneratedKeys();			
//					if (rs.next())
//					{
//						chapter.setId(String.valueOf(rs.getInt(1)));
//					}
//					
//					return chapter;					
//					
//			    }  
//		});
					 
								
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.ChapterRepositoryInterface#updateChapter(com.taksila.veda.model.api.course.v1_0.Chapter)
	 */	
	@Override
	public boolean updateChapter(Chapter chapter) throws Exception 
	{
		logger.debug("Entering into updateChapter():::::");		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		Boolean insertSuccess = jdbcTemplate.execute(update_chapter_sql,new PreparedStatementCallback<Boolean>()
		{  
			    @Override  
			    public Boolean doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			        try 
			        {
			        	stmt.setString(1, chapter.getName());
						stmt.setString(2, chapter.getTitle());
						stmt.setString(3, chapter.getSubTitle());
						stmt.setString(4, chapter.getDescription());
						stmt.setInt(5, Integer.parseInt(chapter.getCourseid()));
						stmt.setInt(6, Integer.valueOf(chapter.getId()));
						
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
	 * @see com.taksila.veda.db.dao.ChapterRepositoryInterface#deleteChapter(java.lang.String)
	 */
	@Override
	public boolean deleteChapter(String id) throws Exception 
	{
		logger.debug("Entering into deleteChapter():::::");
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		return jdbcTemplate.execute(delete_chapter_sql,new PreparedStatementCallback<Boolean>()
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
