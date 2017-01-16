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
import com.taksila.veda.model.api.course.v1_0.Topic;

/**
 * @author mahesh
 *
 */
 
@Repository
@Scope(value="prototype")
@Lazy(value = true)
public class TopicDAO implements TopicRepositoryInterface
{
	@Autowired
	private TenantDBManager tenantDBManager;
	private String tenantId;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
    public TopicDAO(@Value("tenantId") String tenantId)
    {
		logger.trace(" building for tenant id = "+tenantId);
		this.tenantId = tenantId;
    }

	private String schoolId = null;	
	private static String insert_topic_sql = "INSERT INTO TOPICS("+TOPIC_TABLE.chapterid.value()+","+
																			TOPIC_TABLE.topicname.value()+","+
																			TOPIC_TABLE.title.value()+","+
																			TOPIC_TABLE.subTitle.value()+","+
																			TOPIC_TABLE.description.value()+") "+
																	"VALUES (?,?,?,?,?);";		
	
	private static String update_topic_sql = "UPDATE TOPICS SET "+ TOPIC_TABLE.chapterid.value()+" = ? ,"+
															TOPIC_TABLE.topicname.value()+" = ? ,"+
															TOPIC_TABLE.title.value()+" = ? ,"+
															TOPIC_TABLE.subTitle.value()+" = ? ,"+
															TOPIC_TABLE.description.value()+" = ? "+
													" WHERE "+TOPIC_TABLE.id.value()+" = ? ";
	
	private static String delete_topic_sql = "DELETE FROM TOPICS WHERE "+TOPIC_TABLE.id.value()+" = ? ";	
	private static String search_topic_by_title_sql = "SELECT * FROM TOPICS WHERE "+TOPIC_TABLE.title.value()+" like ? ";
	private static String search_topic_by_id_sql = "SELECT * FROM TOPICS WHERE "+TOPIC_TABLE.id.value()+" = ? ";
	private static String search_topic_by_chapter_id_sql = "SELECT * FROM TOPICS WHERE "+TOPIC_TABLE.chapterid.value()+" = ? ";
	private static String search_all_topics_sql = "SELECT * FROM TOPICS";
	
	
	static Logger logger = LogManager.getLogger(TopicDAO.class.getName());
	SQLDataBaseManager sqlDBManager= null;
	

	
	public enum TOPIC_TABLE
	{
		id("id"),
		chapterid("chapterid"),
		topicname("name"),
		title("title"),
		subTitle("sub_title"),
		description("description");		
		private String name;       
	    private TOPIC_TABLE(String s) 
	    {
	        name = s;
	    }
		
	    public String value() 
	    {
	        return this.name;
	    }
		
	};
	
	private Topic mapRow(ResultSet resultSet) throws SQLException 
	{
		Topic topic = new Topic();		
		
		topic.setChapterid(String.valueOf(resultSet.getInt(TOPIC_TABLE.chapterid.value())));
		topic.setId(String.valueOf(resultSet.getInt(TOPIC_TABLE.id.value())));
		topic.setName(resultSet.getString(TOPIC_TABLE.topicname.value()));
		topic.setTitle(resultSet.getString(TOPIC_TABLE.title.value()));
		topic.setSubTitle(resultSet.getString(TOPIC_TABLE.subTitle.value()));
		topic.setDescription(resultSet.getString(TOPIC_TABLE.description.value()));
		
		return topic;
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.TopicRepositoryInterface#searchTopicsByTitle(java.lang.String)
	 */
	@Override
	public List<Topic> searchTopicsByTitle(String q) throws Exception
	{
		List<Topic> topicHits = new ArrayList<Topic>();				
		PreparedStatement stmt = null;		
		logger.trace("searching topics query ="+q);

		try
		{
			this.sqlDBManager.connect();
			
			if (StringUtils.isNotBlank(q))
			{
				stmt = this.sqlDBManager.getPreparedStatement(search_topic_by_title_sql);
				stmt.setString(1, q+"%");
			}
			else
				stmt = this.sqlDBManager.getPreparedStatement(search_all_topics_sql);
			
			ResultSet resultSet = stmt.executeQuery();	
			while (resultSet.next()) 
			{
				topicHits.add(mapRow(resultSet));
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
		
		return topicHits;
		
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.TopicRepositoryInterface#searchTopicsByChapterid(java.lang.String)
	 */
	@Override
	public List<Topic> searchTopicsByChapterid(String chapterid) throws Exception
	{
		List<Topic> topicHits = new ArrayList<Topic>();				
		PreparedStatement stmt = null;		
		logger.trace("searching topics by chapter id query ="+chapterid);

		try
		{
			this.sqlDBManager.connect();						
			stmt = this.sqlDBManager.getPreparedStatement(search_topic_by_chapter_id_sql);
			stmt.setInt(1, Integer.parseInt(chapterid));
			
			ResultSet resultSet = stmt.executeQuery();	
			while (resultSet.next()) 
			{
				topicHits.add(mapRow(resultSet));
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
		
		return topicHits;
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.TopicRepositoryInterface#getTopicById(java.lang.String)
	 */
	@Override
	public Topic getTopicById(String id) throws Exception
	{						
		PreparedStatement stmt = null;	
		Topic topic = null;
		try
		{
			this.sqlDBManager.connect();
			stmt = this.sqlDBManager.getPreparedStatement(search_topic_by_id_sql);
			stmt.setInt(1, Integer.parseInt(id));
			ResultSet resultSet = stmt.executeQuery();	
			if (resultSet.next()) 
			{
				topic = mapRow(resultSet);
			}
			
			return topic;
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
	 * @see com.taksila.veda.db.dao.TopicRepositoryInterface#insertTopic(com.taksila.veda.model.api.course.v1_0.Topic)
	 */	
	@Override
	public Topic insertTopic(Topic topic) throws Exception 
	{
		logger.debug("Entering into insertTopic():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(insert_topic_sql);
			
			stmt.setInt(1, Integer.parseInt(topic.getChapterid()));
			stmt.setString(2, topic.getName());
			stmt.setString(3, topic.getTitle());
			stmt.setString(4, topic.getSubTitle());
			stmt.setString(5, topic.getDescription());
			
			stmt.executeUpdate();			
			ResultSet rs = stmt.getGeneratedKeys();			
			if (rs.next())
			{
				topic.setId(String.valueOf(rs.getInt(1)));
			}
			
			return topic;
			
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
	 * @see com.taksila.veda.db.dao.TopicRepositoryInterface#updateTopic(com.taksila.veda.model.api.course.v1_0.Topic)
	 */	
	@Override
	public boolean updateTopic(Topic topic) throws Exception 
	{
		logger.debug("Entering into updateTopic():::::");		
		PreparedStatement stmt = null;
		try
		{
			this.sqlDBManager.connect();	
			stmt = this.sqlDBManager.getPreparedStatement(update_topic_sql);
			
			stmt.setInt(1, Integer.parseInt(topic.getChapterid()));
			stmt.setString(2, topic.getName());
			stmt.setString(3, topic.getTitle());
			stmt.setString(4, topic.getSubTitle());
			stmt.setString(5, topic.getDescription());
			stmt.setInt(6, Integer.valueOf(topic.getId()));
			
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
	 * @see com.taksila.veda.db.dao.TopicRepositoryInterface#deleteTopic(java.lang.String)
	 */
	@Override
	public boolean deleteTopic(String id) throws Exception 
	{
		logger.debug("Entering into deleteTopic():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(delete_topic_sql);
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
