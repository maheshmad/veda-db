package com.taksila.veda.course.slides;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

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

import com.taksila.veda.course.TopicComponent;
import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.model.api.course.v1_0.GetTopicRequest;
import com.taksila.veda.model.api.course.v1_0.GetTopicResponse;
import com.taksila.veda.model.api.course.v1_0.UploadFileResponse;
import com.taksila.veda.utils.CommonUtils;

/**
 * Servlet implementation class SlidesPPTXUploadServlet
 */
@WebServlet(name="SlidesPPTXUploadServlet", urlPatterns={"/uploadslides"}, asyncSupported=true)
@MultipartConfig
public class SlidesPPTXUploadServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(SlidesPPTXUploadServlet.class.getName());   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SlidesPPTXUploadServlet() 
    {
        super();    
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)   throws ServletException, IOException 
    {
        System.out.println("inside slides upload servlet");
        response.setContentType("application/json");        
                   
        UploadFileResponse fileUploadResp = new UploadFileResponse();
        OutputStream out = null;
        InputStream filecontent = null;
        final PrintWriter writer = response.getWriter();
        final String tenantId = CommonUtils.getSubDomain(request.getRequestURL().toString());
    	TopicComponent topicComp = new TopicComponent(tenantId);

        try 
        {
        	final String topicid = request.getParameter("topicid");
        	final Part filePart = request.getPart("slidecontent");
            final String fileName = System.currentTimeMillis()+CommonUtils.getFileName(filePart);           
        	String fileExtension = FilenameUtils.getExtension(fileName);
        	final String path = CommonUtils.getUserTempFilePath("slides",topicid);
        	/*
        	 * validation 
        	 */
            if (StringUtils.isBlank(topicid))
            {
            	fileUploadResp.setErrorInfo(CommonUtils.buildErrorInfo(fileUploadResp.getErrorInfo(),"Missing Parameter: topicid", "Please provide a valid topicid"));
            }
            
            if (StringUtils.isBlank(fileName))
            {
            	fileUploadResp.setErrorInfo(CommonUtils.buildErrorInfo(fileUploadResp.getErrorInfo(),"Missing Parameters: slidecontent", "File name is required"));
            }
        	
            /*
             * check if the topic exists
             */
            logger.trace("checking file name ="+fileName +" extension = "+fileExtension);
            if (!StringUtils.equalsIgnoreCase(fileExtension,"pptx"))            
            {
            	fileUploadResp.setErrorInfo(CommonUtils.buildErrorInfo(fileUploadResp.getErrorInfo(),"File not supported", "The file has to be a power point presentation with pptx extension"));            	
            }
            
            String fileId = fileName+"_"+RandomStringUtils.random(8, true, true);
            fileId = CommonUtils.getSecureHash(fileId).substring(0,20);
            
            /*
             * check if topic is present 
             */
            if (StringUtils.isNotBlank(topicid))
            {            	
            	GetTopicRequest getTopicReq = new GetTopicRequest();
            	getTopicReq.setId(Integer.parseInt(topicid));
				GetTopicResponse topicResp = topicComp.getTopic(getTopicReq);
            	if (topicResp == null || topicResp.getTopic() == null)
            	{
            		fileUploadResp.setErrorInfo(CommonUtils.buildErrorInfo(fileUploadResp.getErrorInfo(),"Not found", "Topic id = "+topicid+" not found"));                    		
            	}
            }
            
            /*
        	 * no validation errors! so process the request.
        	 */
            if (fileUploadResp.getErrorInfo() == null)
            {        	
	        	out = new FileOutputStream(new File(path + File.separator   + fileName));
	            filecontent = filePart.getInputStream();
	
	            int read = 0;
	            final byte[] bytes = new byte[1024];
	
	            while ((read = filecontent.read(bytes)) != -1) 
	            {
	                out.write(bytes, 0, read);
	            }
	            
	            fileUploadResp.setStatus(StatusType.SUCCESS);
	            fileUploadResp.setSuccess(true);
	            fileUploadResp.setMsg("New file " + fileName + " created at " + path);
	            fileUploadResp.setFileid(fileName);
	            logger.trace("File{0}being uploaded to {1}", new Object[]{fileName, path});
	            	            
            }
            
           
            
        } 
        catch (FileNotFoundException fne) 
        {
           CommonUtils.handleExceptionForResponse(fileUploadResp, fne);
           logger.trace( "Problems during file upload. Error: {0}",  new Object[]{fne.getMessage()});
        } 
        finally 
        {        	        
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
    
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	// TODO Auto-generated method stub
    	this.processRequest(req, resp);
    }
	
}
