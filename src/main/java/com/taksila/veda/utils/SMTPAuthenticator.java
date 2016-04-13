package com.taksila.veda.utils;

import javax.mail.PasswordAuthentication;

import com.taksila.veda.config.ConfigComponent;
import com.taksila.veda.model.db.config.v1_0.ConfigId;

class SMTPAuthenticator extends javax.mail.Authenticator 
{	   
    public PasswordAuthentication getPasswordAuthentication() 
    {
       String username = ConfigComponent.getConfig(ConfigId.SMTP_AUTH_ID);
       String password = ConfigComponent.getConfig(ConfigId.SMTP_AUTH_PSWD);
       return new PasswordAuthentication(username, password);
    }
}