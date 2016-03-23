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

import com.taksila.veda.db.SQLDataBaseManager;
import com.taksila.veda.model.api.course.v1_0.Slide;
import com.taksila.veda.utils.CommonUtils;

/**
 * @author mahesh
 *
 */
public class SlidesDAO 
{
	private String schoolId = null;	
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
	SQLDataBaseManager sqlDBManager= null;
	
	public SlidesDAO(String tenantId) 
	{
		logger.trace(" Initializing SlidesDAO............ ");
		this.schoolId = tenantId;		
		
		this.sqlDBManager = new SQLDataBaseManager();
		logger.trace(" Completed initializing SlidesDAO............ ");
		
	}
	
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
		
		slide.setId(resultSet.getInt(SLIDE_TABLE.id.value()));
		slide.setTopicid(resultSet.getInt(SLIDE_TABLE.topicid.value()));
		slide.setName(resultSet.getString(SLIDE_TABLE.slidename.value()));
		slide.setTitle(resultSet.getString(SLIDE_TABLE.title.value()));
		slide.setSubTitle(resultSet.getString(SLIDE_TABLE.subTitle.value()));
		slide.setDescription(resultSet.getString(SLIDE_TABLE.description.value()));		
		slide.setTextContent(CommonUtils.readSQLTextColumn(resultSet,SLIDE_TABLE.textContent.value()));
		slide.setUpdatedBy(resultSet.getString(SLIDE_TABLE.textContent.value()));
		
		return slide;
	}
	
	/**
	 * 
	 * @param q
	 * @return
	 * @throws Exception 
	 */
	public List<Slide> searchSlidesByTitle(String q) throws Exception
	{
		List<Slide> slideHits = new ArrayList<Slide>();				
		PreparedStatement stmt = null;		
		logger.trace("searching slides query ="+q);

		try
		{
			this.sqlDBManager.connect();
			
			if (StringUtils.isNotBlank(q))
			{
				stmt = this.sqlDBManager.getPreparedStatement(search_slide_by_title_sql);
				stmt.setString(1, q+"%");
				stmt.setString(2, q);
			}
			else
				stmt = this.sqlDBManager.getPreparedStatement(search_all_slides_sql);
			
			ResultSet resultSet = stmt.executeQuery();	
			while (resultSet.next()) 
			{
				slideHits.add(mapRow(resultSet));
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
		
		return slideHits;
		
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public Slide getSlideById(int id) throws Exception
	{						
		PreparedStatement stmt = null;	
		Slide slide = null;
		try
		{
			this.sqlDBManager.connect();
			stmt = this.sqlDBManager.getPreparedStatement(search_slide_by_id_sql);
			stmt.setInt(1, id);
			ResultSet resultSet = stmt.executeQuery();	
			if (resultSet.next()) 
			{
				slide = mapRow(resultSet);
			}
			
			return slide;
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
	public List<Slide> searchSlidesByTopicId(int topicid) throws Exception
	{
		List<Slide> slideHits = new ArrayList<Slide>();				
		PreparedStatement stmt = null;		
		logger.trace("searching slides query by topic id ="+topicid);

		try
		{
			this.sqlDBManager.connect();						
			stmt = this.sqlDBManager.getPreparedStatement(search_slides_by_topicid_sql);
			stmt.setInt(1, topicid);
			ResultSet resultSet = stmt.executeQuery();	
			while (resultSet.next()) 
			{
				slideHits.add(mapRow(resultSet));
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
		
		return slideHits;
		
	}
	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public Slide getSlideByName(String name) throws Exception
	{						
		PreparedStatement stmt = null;	
		Slide slide = null;
		try
		{
			this.sqlDBManager.connect();
			stmt = this.sqlDBManager.getPreparedStatement(search_slide_by_name_sql);
			stmt.setString(1, name);
			ResultSet resultSet = stmt.executeQuery();	
			if (resultSet.next()) 
			{
				slide = mapRow(resultSet);
			}
			
			return slide;
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
	 * @param slide
	 * @return
	 * @throws Exception
	 */	
	public Slide insertSlide(Slide slide) throws Exception 
	{
		logger.debug("Entering into insertSlide():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(insert_slide_sql);
			
			stmt.setString(1, slide.getName());
			stmt.setInt(2, slide.getTopicid());
			stmt.setString(3, slide.getTitle());
			stmt.setString(4, slide.getSubTitle());
			stmt.setString(5, slide.getDescription());			
			stmt.setString(6, slide.getTextContent());			
			
			stmt.executeUpdate();			
			ResultSet rs = stmt.getGeneratedKeys();			
			if (rs.next())
			{
				slide.setId(rs.getInt(1));
			}
			
			return slide;
			
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
	 * @param slide
	 * @return
	 * @throws Exception
	 */	
	public boolean updateSlide(Slide slide) throws Exception 
	{
		logger.debug("Entering into updateSlide():::::");		
		PreparedStatement stmt = null;
		try
		{
			this.sqlDBManager.connect();	
			stmt = this.sqlDBManager.getPreparedStatement(update_slide_sql);
			
			stmt.setString(1, slide.getName());
			stmt.setString(2, slide.getTitle());
			stmt.setString(3, slide.getSubTitle());
			stmt.setString(4, slide.getDescription());			
			stmt.setString(5, slide.getTextContent());
			stmt.setInt(6, slide.getId());
			
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
	 * @param slide
	 * @return
	 * @throws Exception
	 */	
	public boolean updateSlideImage(int slideId,InputStream slideContentImageIs, String imageType, double scale) throws Exception 
	{
		logger.debug("Entering into updateSlideLargeImage():::::");		
		PreparedStatement stmt = null;
		try
		{
			this.sqlDBManager.connect();
			
			if (scale < 1)
				stmt = this.sqlDBManager.getPreparedStatement(update_slide_content_image_thumb_sql);
			else
				stmt = this.sqlDBManager.getPreparedStatement(update_slide_content_image_large_sql);
			
			stmt.setBinaryStream(1, slideContentImageIs);
			stmt.setString(2, imageType);			
			stmt.setInt(3, slideId);
			
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
			slideContentImageIs.close();
		}
								
	}
	
	
	/**
	 * 
	 * @param slide
	 * @return
	 * @throws Exception
	 */	
	public ByteArrayOutputStream readSlideImage(int slideId, double scale) throws Exception 
	{
		logger.trace("Entering into readSlideImage():::::");		
		PreparedStatement stmt = null;
		ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
		try
		{
			this.sqlDBManager.connect();									
			stmt = this.sqlDBManager.getPreparedStatement(search_slide_by_id_sql);								
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
		
		return imageOut;
								
	}
	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSlide(int id) throws Exception 
	{
		logger.debug("Entering into deleteSlide():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(delete_slide_sql);
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
