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

import com.taksila.veda.db.SQLDataBaseManager;
import com.taksila.veda.model.api.course.v1_0.Chapter;

/**
 * @author mahesh
 *
 */
public class ChapterDAO 
{
	private String schoolId = null;	
	private static String insert_chapter_sql = "INSERT INTO CHAPTERS("+CHAPTER_TABLE.chaptername.value()+","+
																			CHAPTER_TABLE.title.value()+","+
																			CHAPTER_TABLE.subTitle.value()+","+
																			CHAPTER_TABLE.description.value()+") "+
																	"VALUES (?,?,?,?);";		
	
	private static String update_chapter_sql = "UPDATE CHAPTERS SET "+CHAPTER_TABLE.chaptername.value()+" = ? ,"+
															CHAPTER_TABLE.title.value()+" = ? ,"+
															CHAPTER_TABLE.subTitle.value()+" = ? ,"+
															CHAPTER_TABLE.description.value()+" = ? "+
													" WHERE "+CHAPTER_TABLE.id.value()+" = ? ";
	
	private static String delete_chapter_sql = "DELETE FROM CHAPTERS WHERE "+CHAPTER_TABLE.id.value()+" = ? ";	
	private static String search_chapter_by_title_sql = "SELECT * FROM CHAPTERS WHERE "+CHAPTER_TABLE.title.value()+" like ? ";
	private static String search_chapter_by_id_sql = "SELECT * FROM CHAPTERS WHERE "+CHAPTER_TABLE.id.value()+" = ? ";	
	private static String search_all_chapters_sql = "SELECT * FROM CHAPTERS";
	
	
	static Logger logger = LogManager.getLogger(ChapterDAO.class.getName());
	SQLDataBaseManager sqlDBManager= null;
	
	public ChapterDAO(String tenantId) 
	{
		logger.trace(" Initializing ChaptersDAO............ ");
		this.schoolId = tenantId;		
		
		this.sqlDBManager = new SQLDataBaseManager();
		logger.trace(" Completed initializing ChaptersDAO............ ");
		
	}
	
	public enum CHAPTER_TABLE
	{
		id("id"),
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
	
	private Chapter mapRow(ResultSet resultSet) throws SQLException 
	{
		Chapter chapter = new Chapter();		
		
		chapter.setId(String.valueOf(resultSet.getInt(CHAPTER_TABLE.id.value())));
		chapter.setName(resultSet.getString(CHAPTER_TABLE.chaptername.value()));
		chapter.setTitle(resultSet.getString(CHAPTER_TABLE.title.value()));
		chapter.setSubTitle(resultSet.getString(CHAPTER_TABLE.subTitle.value()));
		chapter.setDescription(resultSet.getString(CHAPTER_TABLE.description.value()));
		
		return chapter;
	}
	
	/**
	 * 
	 * @param q
	 * @return
	 * @throws SQLException
	 * @throws NamingException 
	 */
	public List<Chapter> searchChaptersByTitle(String q) throws SQLException, NamingException
	{
		List<Chapter> chapterHits = new ArrayList<Chapter>();				
		PreparedStatement stmt = null;		
		logger.trace("searching chapters query ="+q);

		try
		{
			this.sqlDBManager.connect();
			
			if (StringUtils.isNotBlank(q))
			{
				stmt = this.sqlDBManager.getPreparedStatement(search_chapter_by_title_sql);
				stmt.setString(1, q+"%");
			}
			else
				stmt = this.sqlDBManager.getPreparedStatement(search_all_chapters_sql);
			
			ResultSet resultSet = stmt.executeQuery();	
			while (resultSet.next()) 
			{
				chapterHits.add(mapRow(resultSet));
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
		
		return chapterHits;
		
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws NamingException 
	 */
	public Chapter getChapterById(int id) throws SQLException, NamingException
	{						
		PreparedStatement stmt = null;	
		Chapter chapter = null;
		try
		{
			this.sqlDBManager.connect();
			stmt = this.sqlDBManager.getPreparedStatement(search_chapter_by_id_sql);
			stmt.setInt(1, id);
			ResultSet resultSet = stmt.executeQuery();	
			if (resultSet.next()) 
			{
				chapter = mapRow(resultSet);
			}
			
			return chapter;
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
	 * @param chapter
	 * @return
	 * @throws Exception
	 */	
	public Chapter insertChapter(Chapter chapter) throws Exception 
	{
		logger.debug("Entering into insertChapter():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(insert_chapter_sql);
			
			stmt.setString(1, chapter.getName());
			stmt.setString(2, chapter.getTitle());
			stmt.setString(3, chapter.getSubTitle());
			stmt.setString(4, chapter.getDescription());
			
			stmt.executeUpdate();			
			ResultSet rs = stmt.getGeneratedKeys();			
			if (rs.next())
			{
				chapter.setId(String.valueOf(rs.getInt(1)));
			}
			
			return chapter;
			
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
	 * @param chapter
	 * @return
	 * @throws Exception
	 */	
	public boolean updateChapter(Chapter chapter) throws Exception 
	{
		logger.debug("Entering into updateChapter():::::");		
		PreparedStatement stmt = null;
		try
		{
			this.sqlDBManager.connect();	
			stmt = this.sqlDBManager.getPreparedStatement(update_chapter_sql);
			
			stmt.setString(1, chapter.getName());
			stmt.setString(2, chapter.getTitle());
			stmt.setString(3, chapter.getSubTitle());
			stmt.setString(4, chapter.getDescription());
			stmt.setInt(5, Integer.valueOf(chapter.getId()));
			
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
	public boolean deleteChapter(int id) throws Exception 
	{
		logger.debug("Entering into deleteChapter():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(delete_chapter_sql);
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
