package com.taksila.veda.usermgmt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.model.api.usermgmt.v1_0.UploadUserImageResponse;
import com.taksila.veda.model.db.usermgmt.v1_0.UserImageInfo;
import com.taksila.veda.utils.CommonUtils;

/**
 * Servlet implementation class SlidesPPTXUploadServlet
 */
@WebServlet(name="UserProfileImageUploadServlet", urlPatterns={"/upload_profile_image"}, asyncSupported=true)
@MultipartConfig
public class UserProfileImageUploadServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(UserProfileImageUploadServlet.class.getName());   
	Executor executor;
	/**
     * @see HttpServlet#HttpServlet()
     */
    public UserProfileImageUploadServlet() 
    {
    	 executor = Executors.newSingleThreadExecutor();
    }    
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)   throws ServletException, IOException 
    {
        System.out.println("inside profile image upload servlet");
        response.setContentType("application/json");
        // Create path components to save the file              
    	 final String tenantId = CommonUtils.getSubDomain(request.getRequestURL().toString());
        UserComponent userComp = new UserComponent(tenantId);
                          
        UploadUserImageResponse fileUploadResp = new UploadUserImageResponse();
        fileUploadResp.setSuccess(true);	            
        OutputStream out = null;
        InputStream filecontent = null;
        final PrintWriter writer = response.getWriter();

        try 
        {
        	final String profileuserid = request.getParameter("userId");
        	final String imageid = request.getParameter("imageid");
            final Part filePart = request.getPart("userimage");            
            final String fileName = System.currentTimeMillis()+getFileName(filePart);       
            final String path = userComp.getUserTempFilePath(profileuserid);
        	String fileExtension = FilenameUtils.getExtension(fileName);
        	/*
        	 * validation 
        	 */
            if (StringUtils.isBlank(profileuserid))
            {
            	fileUploadResp.setErrorInfo(CommonUtils.buildErrorInfo(fileUploadResp.getErrorInfo(),"Missing Parameter: userid", "Please provide a valid userid"));
            }
            
            if (StringUtils.isBlank(fileName))
            {
            	fileUploadResp.setErrorInfo(CommonUtils.buildErrorInfo(fileUploadResp.getErrorInfo(),"Missing Parameters: userimage", "File name is required"));
            }
            
            /*
             * should be either a jpg or png
             */
            logger.trace("checking file name ="+fileName +" extension = "+fileExtension);
            if (StringUtils.equalsIgnoreCase(fileExtension,"jpg") ||
            	StringUtils.equalsIgnoreCase(fileExtension,"jpeg") || 
            	StringUtils.equalsIgnoreCase(fileExtension,"png"))
            {
            	logger.trace("uploading image = "+fileName);
            }
            else
            {
            	fileUploadResp.setErrorInfo(CommonUtils.buildErrorInfo(fileUploadResp.getErrorInfo(),"Invalid file type", "The image has to be either jpg or png"));            	
            }
            
            String fileId = profileuserid+"_"+fileName+"_"+RandomStringUtils.random(8, true, true);
            fileId = CommonUtils.getSecureHash(fileId);
//            +"."+fileExtension;
            if (StringUtils.isNotBlank(imageid))
            {
            	fileId = imageid;
            	UserImageInfo userImgInfo = userComp.getUserImageInfo(imageid);
            	if (userImgInfo == null)
            	{
            		fileUploadResp.setErrorInfo(CommonUtils.buildErrorInfo(fileUploadResp.getErrorInfo(),"Not found", "File image id = "+imageid+" not found"));                    		
            	}
            }
                      
            
        	/*
        	 * no validation errors! so process the request.
        	 */
            if (fileUploadResp.getErrorInfo() == null)
            {
            	out = new FileOutputStream(new File(path + File.separator   + fileId));
	            filecontent = filePart.getInputStream();
	
	            int read = 0;
	            final byte[] bytes = new byte[1024];
	
	            while ((read = filecontent.read(bytes)) != -1) 
	            {
	                out.write(bytes, 0, read);
	            }
	            	           	           
	            /*
	             * 
	             * Process the files and store it into database
	             */
	            fileUploadResp.setUserImageInfo(userComp.processUserImageFile(profileuserid, fileId));
	            fileUploadResp.setStatus(StatusType.SUCCESS);	            	            
	            fileUploadResp.setMsg("Image successfully uploaded id = " + fileId);	            
	            
            }
            else
            {
            	fileUploadResp.setStatus(StatusType.INVALID);
            	fileUploadResp.setMsg("Please submit a valid form");
            }
            
          
            
        } 
        catch (Exception fne) 
        {
           CommonUtils.handleExceptionForResponse(fileUploadResp, fne);
           logger.trace( "Problems during file upload. Error: {0}",  new Object[]{fne.getMessage()});
        } 
        finally 
        {        	        
        	fileUploadResp.setSuccess(true);
        	writer.write(CommonUtils.toJson(fileUploadResp));
        	if (out != null) 
            {
                out.close();
            }
            if (filecontent != null) 
            {
                filecontent.close();
            }
            if (writer != null) 
            {
                writer.close();
            }
        }
    }

    private String getFileName(final Part part) 
    {
        if (part == null)
        	return "";
        	
    	final String partHeader = part.getHeader("content-disposition");
        logger.trace( "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) 
        {
            if (content.trim().startsWith("filename")) 
            {
                String name = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
                name = name.replaceAll(" ", "_"); 
                return name;
            }
        }
        return null;
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	// TODO Auto-generated method stub
    	this.processRequest(req, resp);
    }
	
}
