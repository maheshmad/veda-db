/**
 * 
 */
package com.taksila.veda.db.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.springframework.stereotype.Repository;

import com.taksila.veda.db.utils.TenantDBManager;
import com.taksila.veda.model.api.course.v1_0.Slide;
import com.taksila.veda.utils.CommonUtils;

/**
 * @author mahesh
 *
 */
@Repository
@Scope(value="prototype")
@Lazy(value = true)
public class SlidesDAO implements SlidesRepositoryInterface 
{
	@Autowired
	private TenantDBManager tenantDBManager;
	private String tenantId;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
    public SlidesDAO(@Value("tenantId") String tenantId)
    {
		logger.trace(" building for tenant id = "+tenantId);
		this.tenantId = tenantId;
    }
	
	private static String insert_slide_sql = "INSERT INTO SLIDES("+SLIDE_TABLE.slidename.value()+","+
																			SLIDE_TABLE.topicid.value()+","+
																			SLIDE_TABLE.title.value()+","+
																			SLIDE_TABLE.subTitle.value()+","+
																			SLIDE_TABLE.description.value()+","+																			
																			SLIDE_TABLE.textContent.value()+") "+
																	"VALUES (?,?,?,?,?,?);";		
			
	private static String update_slide_sql = "UPDATE SLIDES SET "+SLIDE_TABLE.slidename.value()+" = ? ,"+
																	SLIDE_TABLE.topicid.value()+","+
																	SLIDE_TABLE.title.value()+" = ? ,"+
																	SLIDE_TABLE.subTitle.value()+" = ? ,"+
																	SLIDE_TABLE.description.value()+" = ?, "+															
																	SLIDE_TABLE.textContent.value()+" = ? "+
															" WHERE "+SLIDE_TABLE.id.value()+" = ? ";
	
	private static String update_slide_content_image_large_sql = "UPDATE SLIDES SET "+
																	SLIDE_TABLE.contentImageLarge.value()+" = ? ,"+
																	SLIDE_TABLE.contentImageType.value()+" = ? "+																				
																	" WHERE "+SLIDE_TABLE.id.value()+" = ? ";
	
	private static String update_slide_content_image_thumb_sql = "UPDATE SLIDES SET "+
																		SLIDE_TABLE.contentImageThumb.value()+" = ? ,"+
																		SLIDE_TABLE.contentImageType.value()+" = ? "+																				
																		" WHERE "+SLIDE_TABLE.id.value()+" = ? ";
		
	private static String delete_slide_sql = "DELETE FROM SLIDES WHERE "+SLIDE_TABLE.id.value()+" = ? ";	
	private static String search_slide_by_title_sql = "SELECT * FROM SLIDES WHERE "+SLIDE_TABLE.title.value()+" like ? ";	
	private static String search_slide_by_id_sql = "SELECT * FROM SLIDES WHERE "+SLIDE_TABLE.id.value()+" = ? ";
	
	private static String search_slides_by_topicid_sql = "SELECT "+SLIDE_TABLE.id.value()+","+
																	SLIDE_TABLE.topicid.value()+","+
																	SLIDE_TABLE.title.value()+","+		
																	SLIDE_TABLE.slidename.value()+","+
																	SLIDE_TABLE.subTitle.value()+","+
																	SLIDE_TABLE.description.value()+","+
																	SLIDE_TABLE.lastUpdatedBy.value()+","+
																	SLIDE_TABLE.textContent.value()+","+
																	SLIDE_TABLE.lastUpdatedDatetime.value()+" "+
															"FROM SLIDES WHERE "+SLIDE_TABLE.topicid.value()+" = ?";
																	
	private static String search_slide_by_name_sql = "SELECT * FROM SLIDES WHERE "+SLIDE_TABLE.slidename.value()+" = ? ";	
	private static String search_all_slides_sql = "SELECT * FROM SLIDES";
	
	private static String search_slide_by_any_param_sql = "SELECT "+SLIDE_TABLE.id.value()+","+
																	SLIDE_TABLE.topicid.value()+","+
																	SLIDE_TABLE.title.value()+","+		
																	SLIDE_TABLE.slidename.value()+","+
																	SLIDE_TABLE.subTitle.value()+","+
																	SLIDE_TABLE.description.value()+","+
																	SLIDE_TABLE.lastUpdatedBy.value()+","+
																	SLIDE_TABLE.lastUpdatedBy.value()+","+
																	SLIDE_TABLE.lastUpdatedDatetime.value()+" "+
															"FROM SLIDES "+
															"WHERE "+SLIDE_TABLE.slidename.value()+" = ? OR "+
																	SLIDE_TABLE.topicid.value()+" = ? OR "+
																	SLIDE_TABLE.title.value()+" = ? OR "+
																	SLIDE_TABLE.title.value()+" = ? OR ";
		
	static Logger logger = LogManager.getLogger(SlidesDAO.class.getName());	
	
	public enum SLIDE_TABLE
	{
		id("id"),
		topicid("topicid"),
		slidename("name"),
		title("title"),
		subTitle("sub_title"),
		description("description"),
		contentImageId("content_image_id"),
		textContent("text_content"),
		contentImageType("content_image_type"),
		lastUpdatedBy("last_updated_by"),
		lastUpdatedDatetime("last_updated_datetime"),
		contentImageThumb("content_image_thumb"),
		contentImageLarge("content_image_large");
		private String name;       
	    private SLIDE_TABLE(String s) 
	    {
	        name = s;
	    }
		
	    public String value() 
	    {
	        return this.name;
	    }
		
	};
	
	private Slide mapRow(ResultSet resultSet) throws SQLException, IOException 
	{
		Slide slide = new Slide();		
		
		slide.setId(String.valueOf(resultSet.getInt(SLIDE_TABLE.id.value())));
		slide.setTopicid(String.valueOf(resultSet.getInt(SLIDE_TABLE.topicid.value())));
		slide.setName(resultSet.getString(SLIDE_TABLE.slidename.value()));
		slide.setTitle(resultSet.getString(SLIDE_TABLE.title.value()));
		slide.setSubTitle(resultSet.getString(SLIDE_TABLE.subTitle.value()));
		slide.setDescription(resultSet.getString(SLIDE_TABLE.description.value()));		
		slide.setTextContent(CommonUtils.readSQLTextColumn(resultSet,SLIDE_TABLE.textContent.value()));
		slide.setUpdatedBy(resultSet.getString(SLIDE_TABLE.textContent.value()));
		
		return slide;
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.SlidesRepositoryInterface#searchSlidesByTitle(java.lang.String)
	 */
	@Override
	public List<Slide> searchSlidesByTitle(String q) throws Exception
	{
		logger.trace("searching slides query ="+q);

		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		
		String sql;
		if (StringUtils.isNotBlank(q))		
			sql = search_slide_by_title_sql;		
		else
			sql = search_all_slides_sql;
				
		return jdbcTemplate.execute(sql,new PreparedStatementCallback<List<Slide>>()
		{  
			    @Override  
			    public List<Slide> doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			    	List<Slide> hits = new ArrayList<Slide>();
			    	try 
			        {
			    		if (StringUtils.isNotBlank(q))
						{
							stmt.setString(1, q+"%");
							stmt.setString(2, q);
						}
						else;
						
						ResultSet resultSet = stmt.executeQuery();	
						while (resultSet.next()) 
						{
							hits.add(mapRow(resultSet));
						}
					} 
			        catch (SQLException | IOException e) 
			        {					
						e.printStackTrace();
					}
			    	
			    	return hits;
			    }  
		}); 
		
		
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.SlidesRepositoryInterface#getSlideById(java.lang.String)
	 */
	@Override
	public Slide getSlideById(String id) throws Exception
	{						
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		return jdbcTemplate.execute(search_slide_by_id_sql,new PreparedStatementCallback<Slide>()
		{  
			    @Override  
			    public Slide doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
			    {  			              			    	
			    	try 
			    	{
						stmt.setInt(1, Integer.parseInt(id));
						ResultSet resultSet = stmt.executeQuery();	
						if (resultSet.next()) 
						{
							return mapRow(resultSet);
						}
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					return null;
			    }  
		});
		
				
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.SlidesRepositoryInterface#searchSlidesByTopicId(java.lang.String)
	 */
	@Override
	public List<Slide> searchSlidesByTopicId(String topicid) throws Exception
	{		
		logger.trace("searching slides query by topic id ="+topicid);
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		
		return jdbcTemplate.execute(search_slides_by_topicid_sql,new PreparedStatementCallback<List<Slide>>()
		{  
			    @Override  
			    public List<Slide> doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			    	List<Slide> hits = new ArrayList<Slide>();
			    	try 
			        {
						stmt.setInt(1, Integer.parseInt(topicid));
						ResultSet resultSet = stmt.executeQuery();	
						while (resultSet.next()) 
						{
							hits.add(mapRow(resultSet));
						}
					} 
			        catch (SQLException | IOException e) 
			        {					
						e.printStackTrace();
					}
			    	
			    	return hits;
			    }  
		}); 
		
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.SlidesRepositoryInterface#getSlideByName(java.lang.String)
	 */
	@Override
	public Slide getSlideByName(String name) throws Exception
	{								
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		return jdbcTemplate.execute(search_slide_by_name_sql,new PreparedStatementCallback<Slide>()
		{  
			    @Override  
			    public Slide doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
			    {  			              			    	
			    	try 
			    	{
						stmt.setString(1, name);
						ResultSet resultSet = stmt.executeQuery();	
						if (resultSet.next()) 
						{
							return mapRow(resultSet);
						}
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					return null;
			    }  
		});
		
				
	}
	
		
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.SlidesRepositoryInterface#insertSlide(com.taksila.veda.model.api.course.v1_0.Slide)
	 */	
	@Override
	public Slide insertSlide(Slide slide) throws Exception 
	{
		logger.debug("Entering into insertSlide():::::");
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		return jdbcTemplate.execute(insert_slide_sql,new PreparedStatementCallback<Slide>()
		{  
		    @Override  
		    public Slide doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
		    {  			              			    	
		    	stmt.setString(1, slide.getName());
				stmt.setInt(2, Integer.parseInt(slide.getTopicid()));
				stmt.setString(3, slide.getTitle());
				stmt.setString(4, slide.getSubTitle());
				stmt.setString(5, slide.getDescription());			
				stmt.setString(6, slide.getTextContent());			
				
				stmt.executeUpdate();			
				ResultSet rs = stmt.getGeneratedKeys();			
				if (rs.next())
				{
					slide.setId(String.valueOf(rs.getInt(1)));
				}
				
				return slide;					
				
		    }  
		});
			 
								
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.SlidesRepositoryInterface#updateSlide(com.taksila.veda.model.api.course.v1_0.Slide)
	 */	
	@Override
	public boolean updateSlide(Slide slide) throws Exception 
	{
		logger.debug("Entering into updateSlide():::::");		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		Boolean insertSuccess = jdbcTemplate.execute(update_slide_sql,new PreparedStatementCallback<Boolean>()
		{  
		    @Override  
		    public Boolean doInPreparedStatement(PreparedStatement stmt)  			            
		    {  			              
		        try 
		        {
		        	stmt.setString(1, slide.getName());
					stmt.setString(2, slide.getTitle());
					stmt.setString(3, slide.getSubTitle());
					stmt.setString(4, slide.getDescription());			
					stmt.setString(5, slide.getTextContent());
					stmt.setInt(6, Integer.valueOf(slide.getId()));
					
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
	 * @see com.taksila.veda.db.dao.SlidesRepositoryInterface#updateSlideImage(java.lang.String, java.io.InputStream, java.lang.String, double)
	 */	
	@Override
	public boolean updateSlideImage(String slideId,InputStream slideContentImageIs, String imageType, double scale) throws Exception 
	{
		logger.debug("Entering into updateSlideLargeImage():::::");		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		String sql;
		if (scale < 1)
			sql = update_slide_content_image_thumb_sql;
		else
			sql = update_slide_content_image_large_sql;
		
		Boolean insertSuccess = jdbcTemplate.execute(sql,new PreparedStatementCallback<Boolean>()
		{  
		    @Override  
		    public Boolean doInPreparedStatement(PreparedStatement stmt)  			            
		    {  			              
		        try 
		        {
		        	stmt.setBinaryStream(1, slideContentImageIs);
					stmt.setString(2, imageType);			
					stmt.setInt(3, Integer.valueOf(slideId));
					
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
		        finally
		        {
		        	try {
						slideContentImageIs.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    }  
		});  
		
		if (!insertSuccess)
			throw new Exception("Unsuccessful in adding an entry into DB, please check logs");
		
		return insertSuccess;
		
								
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.SlidesRepositoryInterface#readSlideImage(int, double)
	 */	
	@Override
	public ByteArrayOutputStream readSlideImage(int slideId, double scale) throws Exception 
	{
		logger.trace("Entering into readSlideImage():::::");		
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		return jdbcTemplate.execute(search_slide_by_id_sql,new PreparedStatementCallback<ByteArrayOutputStream>()
		{  
		    @Override  
		    public ByteArrayOutputStream doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
		    {  			              			    	
				ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
		    	try 
		    	{
					stmt.setInt(1, slideId);									
					ResultSet resultSet = stmt.executeQuery();
					if (resultSet.next()) 
					{
						logger.trace("Found an image:::::");
						InputStream imgIns = null;
						if (scale < 1)
							imgIns = resultSet.getBinaryStream("content_image_thumb");	
						else
							imgIns = resultSet.getBinaryStream("content_image_large");	
						
						if (imgIns != null)
						{
					        int data = imgIns.read();
					        while (data >= 0) 
					        {
					        	imageOut.write((char) data);
					          data = imgIns.read();
					        }
					        imageOut.flush();
					        imgIns.close();
						}
					}
				} catch (Exception e) 
		    	{
					e.printStackTrace();
				}
				
				return imageOut;
				
		    }  
		});
		
	
								
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.SlidesRepositoryInterface#deleteSlide(java.lang.String)
	 */
	@Override
	public boolean deleteSlide(String id) throws Exception 
	{
		logger.debug("Entering into deleteSlide():::::");

		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		return jdbcTemplate.execute(delete_slide_sql,new PreparedStatementCallback<Boolean>()
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
