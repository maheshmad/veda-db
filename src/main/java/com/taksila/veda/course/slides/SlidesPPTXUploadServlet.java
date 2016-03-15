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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.model.api.base.v1_0.BaseResponse;
import com.taksila.veda.model.api.base.v1_0.StatusType;
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
    	response.setContentType("text/json;charset=UTF-8");
        // Create path components to save the file
        final String path = "C:\\files\\upload\\";
        final Part filePart = request.getPart("file");
        final String fileName = getFileName(filePart);
        
        BaseResponse baseResp = new BaseResponse();
        OutputStream out = null;
        InputStream filecontent = null;
        final PrintWriter writer = response.getWriter();

        try 
        {
            out = new FileOutputStream(new File(path + File.separator   + fileName));
            filecontent = filePart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) 
            {
                out.write(bytes, 0, read);
            }
            
            baseResp.setStatus(StatusType.SUCCESS);
            baseResp.setSuccess(true);
            baseResp.setMsg("New file " + fileName + " created at " + path);
            logger.trace("File{0}being uploaded to {1}", new Object[]{fileName, path});
        } 
        catch (FileNotFoundException fne) 
        {
           CommonUtils.handleExceptionForResponse(baseResp, fne);
           logger.trace( "Problems during file upload. Error: {0}",  new Object[]{fne.getMessage()});
        } 
        finally 
        {        	        
        	writer.write(CommonUtils.toJson(baseResp));
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
        final String partHeader = part.getHeader("content-disposition");
        logger.trace( "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
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
