package com.taksila.veda.course.slides;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
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
@WebServlet(name="SlidesPPTXUploadServletAsync", urlPatterns={"/uploadslides2"}, asyncSupported=true)
@MultipartConfig
public class SlidesPPTXUploadServletAsync extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(SlidesPPTXUploadServletAsync.class.getName());   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SlidesPPTXUploadServletAsync() 
    {
        super();    
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)   throws ServletException, IOException 
    {        
    	
    	 AsyncContext asynContext = request.startAsync();
         // set up async listener
    	 asynContext.addListener(new AsyncListener() 
         {
             public void onComplete(AsyncEvent event) throws IOException 
             {
                 event.getSuppliedResponse().getOutputStream().print("Async Context Complete");
             }

             public void onError(AsyncEvent event) {
                 System.out.println(event.getThrowable());
             }

             public void onStartAsync(AsyncEvent event) {
             }

             public void onTimeout(AsyncEvent event) {
                 System.out.println("my asyncListener.onTimeout");
             }
         });
    	
    	
    	ServletInputStream input = request.getInputStream();
    	ReadListener readListener = new UploadSlideReadListenerImpl(asynContext);
        input.setReadListener(readListener);
    	
        
    }

   
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	// TODO Auto-generated method stub
    	this.processRequest(req, resp);
    }
	
}
