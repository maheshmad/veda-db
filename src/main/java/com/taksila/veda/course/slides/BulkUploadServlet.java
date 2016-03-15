//package com.intellectseec.riskanalytics.servlets;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import javax.servlet.http.Part;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import com.taksila.veda.model.api.base.v1_0.Err;
//import com.taksila.veda.model.api.base.v1_0.ErrorInfo;
//import com.taksila.veda.utils.CommonUtils;
//
//@WebServlet(name = "BulkUploadServlet", urlPatterns = {"/bulkUploadservlet"})
//@MultipartConfig
//public class BulkUploadServlet extends HttpServlet
//{
//
//	private static Logger logger = LogManager.getLogger(BulkUploadServlet.class.getName());
//	private static final String ERROR_INVALID_SESSION = "Invalid Session / Unauthorized Resource access";
//	public static final String USER_AUTH_SESSION_ATTR = "user-auth-info";
//	
//	
//	protected void processRequest(HttpServletRequest request,  HttpServletResponse response)
//	        throws ServletException, IOException 
//	{	
//		logger.trace("Request URI : " +  request.getRequestURL());
//		
//		UserAuthInfo userAuthInfo = getLoggedInUser(request);
//		BulkUploadResponse resp = new BulkUploadResponse();
//		if(userAuthInfo != null)
//		{
//			String tenantId = CommonUtils.getSubDomain(request.getRequestURL().toString());
//			BulkUploadComponent bulkUploadComponent = new BulkUploadComponent(tenantId,userAuthInfo.getUserId());
//		    Part filePart = request.getPart("file");
//		    String fileName = getFileName(filePart);
//			//InputStream is = CommonUtils.readExcelFile("BulkUploadExcelFormat.xlsx");
//		    logger.trace("File Name : " + fileName);
//		   // String fileId = request.getParameter("fileId");
//		    if(filePart.getInputStream()!= null){
//				    XSSFWorkbook workbook = prepareExcelWorkBookRequest(filePart.getInputStream(),fileName);
//				    if(workbook!=null){
//				    	resp = bulkUploadComponent.processBulkSubWorkBook(workbook);
//				    }else{
//				    	ErrorInfo error = new ErrorInfo();
//				    	Err err = new Err();
//				    	err.setErrorFieldId(RAConstants.FILE_TYPE_ERROR_CODE);
//				    	err.setErrorMsg(RAConstants.FILE_TYPE_ERROR_MESSAGE);
//				    	error.getErrors().add(err);
//				    	resp.setErrorInfo(error);
//				    }
//		    }else{
//		    	ErrorInfo error = new ErrorInfo();
//		    	Err err = new Err();
//		    	err.setErrorFieldId("EM1002");
//		    	err.setErrorMsg("Unable to read file. Please try again or contact support center.");
//		    	error.getErrors().add(err);
//		    	resp.setErrorInfo(error);
//		    }
//		}else{
//			ErrorInfo error = new ErrorInfo();
//	    	Err err = new Err();
//	    	err.setErrorFieldId("10000");
//		    	err.setErrorFieldId(ERROR_INVALID_SESSION);
//		    	error.getErrors().add(err);
//		    	resp.setErrorInfo(error);
//		}
//   
//		
//	    response.setContentType("text/html;charset=UTF-8");
//	    response.getWriter().write(CommonUtils.toJson(resp));
//	    response.getWriter().flush();
//	    response.getWriter().close();
//	    
//	}
//	
//	@Override
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//			throws ServletException, IOException {
//		this.processRequest(req, resp);
//	}
//	
//	@Override
//	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//			throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		this.processRequest(req, resp);
//	}
//	
//	private String getFileName(final Part part) {
//	    final String partHeader = part.getHeader("content-disposition");
//	    logger.trace("Part Header = {0}", partHeader);
//	    for (String content : part.getHeader("content-disposition").split(";")) {
//	        if (content.trim().startsWith("filename")) {
//	            return content.substring(
//	                    content.indexOf('=') + 1).trim().replace("\"", "");
//	        }
//	    }
//	    return null;
//	}
//	
//	
//	private XSSFWorkbook prepareExcelWorkBookRequest(InputStream fileInputStream, String fileName){
//		
//		XSSFWorkbook workbook = null;
//		if(fileInputStream != null)
//		{
//			String fileExtension = fileName.substring(fileName.lastIndexOf("."));
//			fileExtension = fileExtension.trim();
//			if(fileExtension.equalsIgnoreCase(".csv")||fileExtension.equalsIgnoreCase(".xlsx")||fileExtension.equalsIgnoreCase(".xls")){
//				try {
//					workbook = new XSSFWorkbook(fileInputStream);
//					logger.trace("Excel workbook created Successfully!!!");
//				} catch (IOException e) {
//					System.out.println(e.getMessage());
//					logger.trace("Excel workbook creation failed");
//				}
//				
//			}
//		}
//		return workbook;
//	}
//
//	
//	
//}
