package com.taksila.veda.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.ESAPI;

import com.taksila.veda.config.ConfigComponent;
import com.taksila.veda.model.db.config.v1_0.ConfigId;

public class EmailUtils
{	
	public void sendMail(String to, String from, String subject, String msg,String cc) throws Exception
	{
	            
		   Properties props = new Properties();
	       props.put("mail.transport.protocol", ConfigComponent.getConfig(ConfigId.SMTP_TRANSPORT_PROTOCOL));
	       props.put("mail.smtp.host", ConfigComponent.getConfig(ConfigId.SMTP_HOST_URL));
	       props.put("mail.smtp.socketFactory.port", ConfigComponent.getConfig(ConfigId.SMTP_SOCKET_FACTORY_PORT));  
	       props.put("mail.smtp.socketFactory.class",  ConfigComponent.getConfig(ConfigId.SMTP_SOCKET_FACTORY_CLASS));  
	       props.put("mail.smtp.auth", ConfigComponent.getConfig(ConfigId.SMTP_ENABLE_AUTHENTICATION));  
	       //props.put("mail.smtp.starttls.enable", Config.getConfig(ConfigId.SMTP_STARTTLS_ENABLE));
	       props.put("mail.smtp.port", ConfigComponent.getConfig(ConfigId.SMTP_PORT)); 
	              
		   Authenticator auth = new SMTPAuthenticator();
		   Session mailSession = Session.getDefaultInstance(props, auth);
		   // uncomment for debugging infos to stdout
		   mailSession.setDebug(true);		   
		   Transport transport = mailSession.getTransport();

		   MimeMessage message = new MimeMessage(mailSession);
		   message.setContent(msg, "text/html; charset=utf-8");
//		   message.setSubject(ESAPI.validator().getValidInput("Email Subject", subject, "AlphaNumericWithSpaces", 256, false));
		   message.setSubject(subject);
		   message.setFrom(new InternetAddress(ESAPI.validator().getValidInput("From Email", from, "Email", 255, false)));
		   message.addRecipient(Message.RecipientType.TO,   new InternetAddress(ESAPI.validator().getValidInput("User Email", to, "Email", 255, false)));
		   
		   if (StringUtils.isNotBlank(cc))
			   message.addRecipient(Message.RecipientType.CC, new InternetAddress(ESAPI.validator().getValidInput("User Email", cc, "Email", 255, false)));

		   transport.connect();
		   transport.sendMessage(message,      message.getRecipients(Message.RecipientType.TO));
		   transport.close();
		
	}
   
}
