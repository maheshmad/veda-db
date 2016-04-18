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

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.db.SQLDataBaseManager;
import com.taksila.veda.model.api.course.v1_0.UploadedFile;
import com.taksila.veda.utils.CommonUtils;

/**
 * @author mahesh
 *
 */
public class UploadedFileDAO 
{
	private String schoolId = null;	
	
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
	SQLDataBaseManager sqlDBManager= null;
	
	public UploadedFileDAO(String tenantId) 
	{
		logger.trace(" Initializing UploadedFileDAO............ ");
		this.schoolId = tenantId;		
		
		this.sqlDBManager = new SQLDataBaseManager();
		logger.trace(" Completed initializing UploadedFileDAO............ ");
		
	}
	
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
	
	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public UploadedFile getUploadedFileById(int id) throws Exception
	{						
		PreparedStatement stmt = null;	
		UploadedFile uploadedfile = null;
		try
		{
			this.sqlDBManager.connect();
			stmt = this.sqlDBManager.getPreparedStatement(search_uploadedfile_by_id_sql);
			stmt.setInt(1, id);
			ResultSet resultSet = stmt.executeQuery();	
			if (resultSet.next()) 
			{
				uploadedfile = mapRow(resultSet);
			}
			
			return uploadedfile;
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
	public List<UploadedFile> searchUploadedFileByTopicId(int topicid) throws Exception
	{
		List<UploadedFile> uploadedfileHits = new ArrayList<UploadedFile>();				
		PreparedStatement stmt = null;		
		logger.trace("searching uploadedfiles query by topic id ="+topicid);

		try
		{
			this.sqlDBManager.connect();						
			stmt = this.sqlDBManager.getPreparedStatement(search_uploadedfile_by_topicid_sql);
			stmt.setInt(1, topicid);
			ResultSet resultSet = stmt.executeQuery();	
			while (resultSet.next()) 
			{
				uploadedfileHits.add(mapRow(resultSet));
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
		
		return uploadedfileHits;
		
	}
	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public UploadedFile getUploadedFileByName(String filename) throws Exception
	{						
		PreparedStatement stmt = null;	
		UploadedFile uploadedfile = null;
		try
		{
			this.sqlDBManager.connect();
			stmt = this.sqlDBManager.getPreparedStatement(search_uploadedfile_by_filename_sql);
			stmt.setString(1, filename);
			ResultSet resultSet = stmt.executeQuery();	
			if (resultSet.next()) 
			{
				uploadedfile = mapRow(resultSet);
			}
			
			return uploadedfile;
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
	 * @param uploadedfile
	 * @return
	 * @throws Exception
	 */	
	public UploadedFile insertUploadedFile(UploadedFile uploadedfile,InputStream uploadedfileContentImageIs) throws Exception 
	{
		logger.debug("Entering into insertUploadedFile():::::");
				
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(insert_uploadedfile_sql);
						
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
	 * @param uploadedfile
	 * @return
	 * @throws Exception
	 */	
	public boolean updateUploadedFile(UploadedFile uploadedfile) throws Exception 
	{
		logger.debug("Entering into updateUploadedFile():::::");		
		PreparedStatement stmt = null;
		try
		{
			this.sqlDBManager.connect();	
			stmt = this.sqlDBManager.getPreparedStatement(update_uploadedfile_sql);
			
			/*"UPDATE UPLOADED_FILES SET "+ UPLOADED_FILES_TABLE.topicid.value()+","+
																	UPLOADED_FILES_TABLE.filename.value()+" = ? ,"+																																		
																	UPLOADED_FILES_TABLE.fileType.value()+" = ? ,"+
																	UPLOADED_FILES_TABLE.uploaded_by.value()+","+	
																	UPLOADED_FILES_TABLE.fileProcessingCode.value()+" = ? "+																	
															" WHERE "+UPLOADED_FILES_TABLE.id.value()+" = ? "; */
			
			
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
	 * @param uploadedfile
	 * @return
	 * @throws Exception
	 */	
	public boolean updateUploadedFileContent(int uploadedfileId,String fileType,InputStream uploadedfileContentIs) throws Exception 
	{
		logger.debug("Entering into updateUploadedFileLargeImage():::::");		
		PreparedStatement stmt = null;
		try
		{
			this.sqlDBManager.connect();
						
			stmt = this.sqlDBManager.getPreparedStatement(update_uploadedfile_content_sql);
			
			stmt.setBinaryStream(1, uploadedfileContentIs);
			stmt.setString(2, fileType);			
			stmt.setInt(3, uploadedfileId);
			
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
			uploadedfileContentIs.close();
		}
								
	}
	
	
	/**
	 * 
	 * @param uploadedfile
	 * @return
	 * @throws Exception
	 */	
	public ByteArrayOutputStream readUploadedFileImage(int uploadedfileId) throws Exception 
	{
		logger.trace("Entering into readUploadedFileImage():::::");		
		PreparedStatement stmt = null;
		ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
		try
		{
			this.sqlDBManager.connect();									
			stmt = this.sqlDBManager.getPreparedStatement(search_uploadedfile_by_id_sql);								
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
	public boolean deleteUploadedFile(int id) throws Exception 
	{
		logger.debug("Entering into deleteUploadedFile():::::");
		this.sqlDBManager.connect();	
		PreparedStatement stmt = null;
		try
		{
			stmt = this.sqlDBManager.getPreparedStatement(delete_uploadedfile_sql);
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
