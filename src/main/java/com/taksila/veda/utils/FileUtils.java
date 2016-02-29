package com.taksila.veda.utils;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUtils {

	static Logger logger = LogManager.getLogger(FileUtils.class.getName());
	
	public static byte[] getContentAsBytes(InputStream is, long fileSize) {
		byte[] content = null;
		try {
			if(is != null){
				content = new byte[(int)fileSize];
				is.read(content);
				is.close();
			}
			
		}catch(Exception e){
			logger.error("Error While Reading Stream >>>>>> " + e.getMessage());
			e.printStackTrace();
		}
		
		return content;
	}
	
	public static byte[] getContentAsBytes(InputStream is) {
		byte[] content = null;
		try {
			if(is != null){
				content = IOUtils.toByteArray(is);
			}
			
		}catch(Exception e){
			logger.error("Error While Reading Stream >>>>>> " + e.getMessage());
			e.printStackTrace();
		}
		
		return content;
	}
	
	public static String getContentAsString(InputStream is) {
		String content = "";
		try {
			if(is != null){
				byte[] contentArr = IOUtils.toByteArray(is);
				content = new String(contentArr);
			}
			
		}catch(Exception e){
			logger.error("Error While Reading Stream >>>>>> " + e.getMessage());
			e.printStackTrace();
		}
		
		return content;
	}
	
	public static byte[] getContentAsBytes(String fileName) {
		byte[] content = null;
		try {
			if(CommonUtils.isNotEmpty(fileName)){
				InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
				content = IOUtils.toByteArray(is);
			}
			
		}catch(Exception e){
			logger.error("Error While Reading File >>>>>> " + fileName);
			e.printStackTrace();
		}
		
		return content;
	}
}
