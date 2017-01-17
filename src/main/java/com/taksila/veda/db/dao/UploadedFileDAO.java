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

import javax.xml.datatype.DatatypeConfigurationException;

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
import com.taksila.veda.model.api.course.v1_0.UploadedFile;
import com.taksila.veda.utils.CommonUtils;

/**
 * @author mahesh
 *
 */
@Repository
@Scope(value="prototype")
@Lazy(value = true)
public class UploadedFileDAO implements UploadedFileRepositoryInterface 
{
	@Autowired
	private TenantDBManager tenantDBManager;
	private String tenantId;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
    public UploadedFileDAO(@Value("tenantId") String tenantId)
    {
		logger.trace(" building for tenant id = "+tenantId);
		this.tenantId = tenantId;
    }
	
	private static String generic_select_fields_no_blob = "SELECT "+UPLOADED_FILES_TABLE.id.value()+","+
																	UPLOADED_FILES_TABLE.topicid.value()+","+
																	UPLOADED_FILES_TABLE.filename.value()+","+
																	UPLOADED_FILES_TABLE.uploaded_by.value()+","+
																	UPLOADED_FILES_TABLE.uploadedDatetime.value()+","+
																	UPLOADED_FILES_TABLE.fileType.value()+","+
																	UPLOADED_FILES_TABLE.fileProcessingCode.value()+" "+																	
															"FROM UPLOADED_FILES ";
	
	
	private static String search_uploadedfile_by_any_param_sql = generic_select_fields_no_blob+												
																"WHERE "+UPLOADED_FILES_TABLE.topicid.value()+" = ? OR "+
																		UPLOADED_FILES_TABLE.filename.value()+" = ? OR "+
																		UPLOADED_FILES_TABLE.uploaded_by.value()+" = ? OR "+
																		UPLOADED_FILES_TABLE.fileType.value()+" = ? OR "+
																		UPLOADED_FILES_TABLE.fileProcessingCode.value()+" = ? "+
																"LIMIT ?,?";		
	
	private static String search_uploadedfile_by_filename_sql = generic_select_fields_no_blob+"WHERE "+UPLOADED_FILES_TABLE.filename.value()+" like ? ";	
	private static String search_uploadedfile_by_id_sql = generic_select_fields_no_blob+"WHERE "+UPLOADED_FILES_TABLE.id.value()+" = ? ";
	private static String search_uploadedfile_by_topicid_sql = generic_select_fields_no_blob+"WHERE "+UPLOADED_FILES_TABLE.id.value()+" = ? ";
	
	private static String insert_uploadedfile_sql = "INSERT INTO UPLOADED_FILES("+UPLOADED_FILES_TABLE.topicid.value()+","+
																			UPLOADED_FILES_TABLE.filename.value()+","+
																			UPLOADED_FILES_TABLE.content.value()+","+
																			UPLOADED_FILES_TABLE.uploaded_by.value()+","+	
																			UPLOADED_FILES_TABLE.fileType.value()+","+	
																			UPLOADED_FILES_TABLE.fileProcessingCode.value()+") "+
																	"VALUES (?,?,?,?,?,?);";		
			
	private static String update_uploadedfile_sql = "UPDATE UPLOADED_FILES SET "+ UPLOADED_FILES_TABLE.topicid.value()+","+
																	UPLOADED_FILES_TABLE.filename.value()+" = ? ,"+																																		
																	UPLOADED_FILES_TABLE.fileType.value()+" = ? ,"+
																	UPLOADED_FILES_TABLE.uploaded_by.value()+","+	
																	UPLOADED_FILES_TABLE.fileProcessingCode.value()+" = ? "+																	
															" WHERE "+UPLOADED_FILES_TABLE.id.value()+" = ? ";
	
	private static String update_uploadedfile_content_sql = "UPDATE UPLOADED_FILES SET "+
																	UPLOADED_FILES_TABLE.content.value()+" = ? ,"+
																	UPLOADED_FILES_TABLE.fileType.value()+" = ? "+																				
																	" WHERE "+UPLOADED_FILES_TABLE.id.value()+" = ? ";
		
	private static String delete_uploadedfile_sql = "DELETE FROM UPLOADED_FILES WHERE "+UPLOADED_FILES_TABLE.id.value()+" = ? ";	
	
	static Logger logger = LogManager.getLogger(UploadedFileDAO.class.getName());		
	
	public enum UPLOADED_FILES_TABLE
	{
		id("id"),
		topicid("topicid"),
		filename("file_name"),
		content("content"),
		uploaded_by("uploaded_by"),
		uploadedDatetime("uploaded_datetime"),
		fileType("file_type"),
		fileProcessingCode("file_processing_code");
		
		private String name;       
	    private UPLOADED_FILES_TABLE(String s) 
	    {
	        name = s;
	    }
		
	    public String value() 
	    {
	        return this.name;
	    }
		
	};
	
	private UploadedFile mapRow(ResultSet resultSet) throws SQLException, IOException, DatatypeConfigurationException 
	{
		UploadedFile uploadedfile = new UploadedFile();		
		
		uploadedfile.setId(String.valueOf(resultSet.getInt(UPLOADED_FILES_TABLE.id.value())));
		uploadedfile.setTopicid(String.valueOf(resultSet.getInt(UPLOADED_FILES_TABLE.topicid.value())));
		uploadedfile.setFilename(resultSet.getString(UPLOADED_FILES_TABLE.filename.value()));	
		uploadedfile.setFileType(resultSet.getString(UPLOADED_FILES_TABLE.fileType.value()));
		uploadedfile.setFileProcessingCode(resultSet.getString(UPLOADED_FILES_TABLE.fileProcessingCode.value()));		
		uploadedfile.setUpdatedBy(resultSet.getString(UPLOADED_FILES_TABLE.uploaded_by.value()));
		uploadedfile.setLastUpdatedDateTime(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(UPLOADED_FILES_TABLE.uploadedDatetime.value())));		
		
		return uploadedfile;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UploadedFileRepositoryInterface#getUploadedFileById(int)
	 */
	@Override
	public UploadedFile getUploadedFileById(int id) throws Exception
	{						
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		return jdbcTemplate.execute(search_uploadedfile_by_id_sql,new PreparedStatementCallback<UploadedFile>()
		{  
			    @Override  
			    public UploadedFile doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
			    {  			              			    	
			    	try 
			    	{
						stmt.setInt(1, id);
						ResultSet resultSet = stmt.executeQuery();	
						if (resultSet.next()) 
						{
							return mapRow(resultSet);
						}
					} catch (IOException | DatatypeConfigurationException e) 
			    	{
						e.printStackTrace();
					}
					
					return null;
			    }  
		});
					
	}
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UploadedFileRepositoryInterface#searchUploadedFileByTopicId(int)
	 */
	@Override
	public List<UploadedFile> searchUploadedFileByTopicId(int topicid) throws Exception
	{
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);	
		
		return jdbcTemplate.execute(search_uploadedfile_by_topicid_sql,new PreparedStatementCallback<List<UploadedFile>>()
		{  
			    @Override  
			    public List<UploadedFile> doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			    	List<UploadedFile> hits = new ArrayList<UploadedFile>();
			    	try 
			        {
			    		stmt.setInt(1, topicid);
						ResultSet resultSet = stmt.executeQuery();	
						while (resultSet.next()) 
						{
							hits.add(mapRow(resultSet));
						}
					} 
			        catch (SQLException | IOException | DatatypeConfigurationException e) 
			        {					
						e.printStackTrace();
					}
			    	
			    	return hits;
			    }  
		}); 
		
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UploadedFileRepositoryInterface#getUploadedFileByName(java.lang.String)
	 */
	@Override
	public UploadedFile getUploadedFileByName(String filename) throws Exception
	{						
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		return jdbcTemplate.execute(search_uploadedfile_by_filename_sql,new PreparedStatementCallback<UploadedFile>()
		{  
			    @Override  
			    public UploadedFile doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
			    {  			              			    	
			    	try 
			    	{
						stmt.setString(1, filename);
						ResultSet resultSet = stmt.executeQuery();	
						if (resultSet.next()) 
						{
							return mapRow(resultSet);
						}
					} catch (SQLException | IOException | DatatypeConfigurationException e) 
			    	{
						e.printStackTrace();
			    	}
					
					return null;
			    }  
		});
	
				
	}
	
		
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UploadedFileRepositoryInterface#insertUploadedFile(com.taksila.veda.model.api.course.v1_0.UploadedFile, java.io.InputStream)
	 */	
	@Override
	public UploadedFile insertUploadedFile(UploadedFile uploadedfile,InputStream uploadedfileContentImageIs) throws Exception 
	{
		logger.debug("Entering into insertUploadedFile():::::");
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		return jdbcTemplate.execute(insert_uploadedfile_sql,new PreparedStatementCallback<UploadedFile>()
		{  
			    @Override  
			    public UploadedFile doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
			    {  			              			    	
			    	stmt.setInt(1, Integer.parseInt(uploadedfile.getTopicid()));
					stmt.setString(2, uploadedfile.getFilename());
					stmt.setBinaryStream(3, uploadedfileContentImageIs);
					stmt.setString(4, uploadedfile.getUpdatedBy());
					stmt.setString(5, uploadedfile.getFileType());			
					stmt.setString(6, uploadedfile.getFileProcessingCode());			
					
					stmt.executeUpdate();			
					ResultSet rs = stmt.getGeneratedKeys();			
					if (rs.next())
					{
						uploadedfile.setId(String.valueOf(rs.getInt(1)));
					}
					
					return uploadedfile;			
					
			    }  
		});
		
					 
								
	}
	
	
	/* (non-Javadoc)
	 * @see com.taksila.veda.db.dao.UploadedFileRepositoryInterface#updateUploadedFile(com.taksila.veda.model.api.course.v1_0.UploadedFile)
	 */	
	@Override
	public boolean updateUploadedFile(UploadedFile uploadedfile) throws Exception 
	{
		logger.debug("Entering into updateUploadedFile():::::");		
		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		Boolean insertSuccess = jdbcTemplate.execute(update_uploadedfile_sql,new PreparedStatementCallback<Boolean>()
		{  
			    @Override  
			    public Boolean doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			        try 
			        {
			        	stmt.setString(1, uploadedfile.getFilename());
						stmt.setString(2, uploadedfile.getFileType());
						stmt.setString(3, uploadedfile.getUpdatedBy());
						stmt.setString(4, uploadedfile.getFileProcessingCode());						
						stmt.setInt(6, Integer.valueOf(uploadedfile.getId()));
						
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
	 * @see com.taksila.veda.db.dao.UploadedFileRepositoryInterface#updateUploadedFileContent(int, java.lang.String, java.io.InputStream)
	 */	
	@Override
	public boolean updateUploadedFileContent(int uploadedfileId,String fileType,InputStream uploadedfileContentIs) throws Exception 
	{
		logger.debug("Entering into updateUploadedFileLargeImage():::::");		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		Boolean insertSuccess = jdbcTemplate.execute(update_uploadedfile_content_sql,new PreparedStatementCallback<Boolean>()
		{  
			    @Override  
			    public Boolean doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			        try 
			        {
						stmt.setBinaryStream(1, uploadedfileContentIs);
						stmt.setString(2, fileType);			
						stmt.setInt(3, uploadedfileId);
						
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
						try 
						{
							uploadedfileContentIs.close();
						} catch (IOException e) 
						{
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
	 * @see com.taksila.veda.db.dao.UploadedFileRepositoryInterface#readUploadedFileImage(int)
	 */	
	@Override
	public ByteArrayOutputStream readUploadedFileImage(int uploadedfileId) throws Exception 
	{
		logger.trace("Entering into readUploadedFileImage():::::");		
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		
		return jdbcTemplate.execute(search_uploadedfile_by_id_sql,new PreparedStatementCallback<ByteArrayOutputStream>()
		{  
		    @Override  
		    public ByteArrayOutputStream doInPreparedStatement(PreparedStatement stmt) throws SQLException  			            
		    {  			              			    	
				ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
		    	try 
		    	{
		    		stmt.setInt(1, uploadedfileId);										
					ResultSet resultSet = stmt.executeQuery();
					if (resultSet.next()) 
					{
						logger.trace("Found an image:::::");
						InputStream imgIns = null;
						imgIns = resultSet.getBinaryStream(UPLOADED_FILES_TABLE.content.value());	
						
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
	 * @see com.taksila.veda.db.dao.UploadedFileRepositoryInterface#deleteUploadedFile(int)
	 */
	@Override
	public boolean deleteUploadedFile(int id) throws Exception 
	{
		logger.debug("Entering into deleteUploadedFile():::::");
		JdbcTemplate jdbcTemplate = this.tenantDBManager.getJdbcTemplate(this.tenantId);
		return jdbcTemplate.execute(delete_uploadedfile_sql,new PreparedStatementCallback<Boolean>()
		{  
			    @Override  
			    public Boolean doInPreparedStatement(PreparedStatement stmt)  			            
			    {  			              
			        try 
			        {
			        	stmt.setInt(1, id);
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
