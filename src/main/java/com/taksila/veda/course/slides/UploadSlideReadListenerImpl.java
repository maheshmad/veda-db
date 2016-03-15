package com.taksila.veda.course.slides;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.model.api.base.v1_0.BaseResponse;
import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.utils.CommonUtils;

public class UploadSlideReadListenerImpl implements ReadListener
{
	private ServletInputStream input = null; 
	private HttpServletResponse servletResponse = null; 
	private HttpServletRequest servletRequest = null; 
	private AsyncContext asynCtx = null; 
	private Queue<BaseResponse> queue = new LinkedBlockingQueue<>();
	private static Logger logger = LogManager.getLogger(UploadSlideReadListenerImpl.class.getName());   
	
	public UploadSlideReadListenerImpl(AsyncContext c) 
	{
		servletRequest = (HttpServletRequest) c.getRequest();
		servletResponse = (HttpServletResponse) c.getResponse();
		asynCtx = c;
    }
	
	@Override
	public void onDataAvailable() throws IOException 
	{        
		System.out.println("inside slides upload servlet");
		servletResponse.setContentType("text/json;charset=UTF-8");
		
		
		BaseResponse baseResp = new BaseResponse();	
		final PrintWriter writer = servletResponse.getWriter();
		OutputStream out = null;
		InputStream filecontent = null;		
		
		try 
		{
			final String path = "C:\\files\\upload\\";
			final Part filePart = servletRequest.getPart("file");
			final String fileName = getFileName(filePart);
	
			
			out = new FileOutputStream(new File(path + File.separator + fileName));
			filecontent = filePart.getInputStream();

			int read = 0;
			final byte[] bytes = new byte[1024];

			while ((read = filecontent.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			baseResp.setStatus(StatusType.SUCCESS);
			baseResp.setSuccess(true);
			baseResp.setMsg("New file " + fileName + " created at " + path);
			logger.trace("File{0}being uploaded to {1}", new Object[] { fileName, path });	
			
			System.out.println("Data is all read");
	      
			
		} 
		catch (Exception ex) 
		{
			CommonUtils.handleExceptionForResponse(baseResp, ex);
			logger.trace("Problems during file upload. Error: {0}", new Object[] { ex.getMessage() });
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
            asynCtx.complete();
        }
	}

	@Override
	public void onAllDataRead() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Throwable t) {
		// TODO Auto-generated method stub
		
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
	
}
