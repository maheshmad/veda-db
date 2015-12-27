package com.taksila.veda.utils;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.taksila.veda.utils.AdminConfig;
import com.taksila.veda.utils.ResourceBundleUtils;


public class EmailUtils {
	private static String TRANSPORT_PROTOCOL = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_CONFIG_BUNDLE, Locale.UK, AdminConfig.MAIL_TRANSPORT_PROTOCOL);
	private static String HOST_URL = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_CONFIG_BUNDLE, Locale.UK, AdminConfig.MAIL_SMTP_HOST_URL);
	private static String SOCKETFACTORY_PORT = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_CONFIG_BUNDLE, Locale.UK, AdminConfig.MAIL_SMTP_SOCKETFACTORY_PORT);
	private static String SOCKETFACTORY_CLASS = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_CONFIG_BUNDLE, Locale.UK, AdminConfig.MAIL_SMTP_SOCKETFACTORY_CLASS);
	private static String AUTH_ENABLE = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_CONFIG_BUNDLE, Locale.UK, AdminConfig.MAIL_SMTP_AUTH_ENABLE);
	private static String PORT= ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_CONFIG_BUNDLE, Locale.UK, AdminConfig.MAIL_SMTP_PORT);
	private static String AUTH_ID = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_CONFIG_BUNDLE, Locale.UK, AdminConfig.MAIL_SMTP_AUTH_ID);
	private static String AUTH_PASSWORD = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_CONFIG_BUNDLE, Locale.UK, AdminConfig.MAIL_SMTP_AUTH_PWD_ENCODED);
	
	public static String sendMail(String to, String from, String subject, String msg) throws Exception {
		String result = "MAIL SUCCESS";	
		Transport transport = null;

		Properties props = new Properties();
		props.put("mail.transport.protocol", TRANSPORT_PROTOCOL);
		props.put("mail.smtp.host", HOST_URL);
		props.put("mail.smtp.socketFactory.port", SOCKETFACTORY_PORT);
		props.put("mail.smtp.socketFactory.class", "");
		props.put("mail.smtp.auth", AUTH_ENABLE);
		props.put("mail.smtp.starttls.enable", "false"); 
		props.put("mail.smtp.port", PORT);

		try {
			Authenticator auth = new SMTPAuthenticator();
			
			Session mailSession = Session.getDefaultInstance(props, auth);
			//mailSession.setDebug(true);
			transport = mailSession.getTransport();
			MimeMessage message = new MimeMessage(mailSession);
			message.setContent(msg, "text/html; charset=utf-8");
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);

			transport.connect();
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
		} catch (Exception e) {
			e.printStackTrace();
			result = "MAIL FAILED";
		} finally {
			if(transport != null) {
				transport.close();
			}
		}

		return result;
	}
	
	public static String sendMailWithBCC(String to, String from, String subject, String msg,List<String> bccList,List<String> ccList) throws Exception {
		String result = "MAIL SUCCESS";	
		Transport transport = null;

		Properties props = new Properties();
		props.put("mail.transport.protocol", TRANSPORT_PROTOCOL);
		props.put("mail.smtp.host", HOST_URL);
		props.put("mail.smtp.socketFactory.port", SOCKETFACTORY_PORT);
		props.put("mail.smtp.socketFactory.class", "");
		props.put("mail.smtp.auth", AUTH_ENABLE);
		props.put("mail.smtp.starttls.enable", "false"); 
		props.put("mail.smtp.port", PORT);

		try {
			Authenticator auth = new SMTPAuthenticator();
			
			Session mailSession = Session.getDefaultInstance(props, auth);
			//mailSession.setDebug(true);
			transport = mailSession.getTransport();
			MimeMessage message = new MimeMessage(mailSession);
			message.setContent(msg, "text/html; charset=utf-8");
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			if(bccList!=null && bccList.size()>0){
				for (int i = 0; i < bccList.size(); i++) {
					message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bccList.get(i)));
				}
				
				
			}
			if(ccList!=null && ccList.size()>0){
				for (int i = 0; i < ccList.size(); i++) {
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccList.get(i)));
				}
				
			}
			message.setSubject(subject);
			transport.connect();
			transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			result = "MAIL FAILED";
		} finally {
			if(transport != null) {
				transport.close();
			}
		}

		return result;
	}
	
//	public static String sendTestMail(String to, String from, String subject, String msg) throws Exception
//	{
//		String result = "SUCCESS";  
//
//		try {
//			Properties props = System.getProperties();  
//			props.put("mail.smtp.starttls.enable", "true"); 
//			props.put("mail.smtp.host", "localhost");
//			props.put("mail.smtp.port", "26");
//			props.put("mail.smtp.auth", "false");
//
//			Session mailSession = Session.getDefaultInstance(props);
//			
//			Message message = new MimeMessage(mailSession);  
//			message.setFrom(new InternetAddress(from));  
//			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
//			message.setSubject("Hi"); 
//			Transport.send(message);
//		} catch (Exception e) {
//			e.printStackTrace();
//			result = "FAILED";
//		}
//		
//		return result;
//	}

	private static class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = AUTH_ID;
			String password = AUTH_PASSWORD;
			return new PasswordAuthentication(username, password);
		}
	}
}
