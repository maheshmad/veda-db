-- configuration sections
INSERT INTO `xe1`.`config_section`(`config_section_id`,`config_section_name`,`view_xtype`,`allowed_roles`,`updated_by`,`last_updated_on`) VALUES("SMTP_SECTION","Mail",null,"SYSADMIN","mahesh",CURRENT_TIMESTAMP);
INSERT INTO `xe1`.`config_section`(`config_section_id`,`config_section_name`,`view_xtype`,`allowed_roles`,`updated_by`,`last_updated_on`) VALUES("MQ_SECTION","MQ",null,"SYSADMIN","mahesh",CURRENT_TIMESTAMP);
INSERT INTO `xe1`.`config_section`(`config_section_id`,`config_section_name`,`view_xtype`,`allowed_roles`,`updated_by`,`last_updated_on`) VALUES("SECURITY_SECTION","Security",null,"SYSADMIN","mahesh",CURRENT_TIMESTAMP);
INSERT INTO `xe1`.`config_section`(`config_section_id`,`config_section_name`,`view_xtype`,`allowed_roles`,`updated_by`,`last_updated_on`) VALUES("GENERAL_SECTION","General",null,"SYSADMIN","mahesh",CURRENT_TIMESTAMP);

-- configuration groups
-- GENERAL
INSERT INTO `xe1`.`config_group`(`config_group_id`,`config_section_id`,`config_group_name`,`footer_note`,`header_note`,`allowed_roles`,`updated_by`,`last_updated_on`) VALUES("GEN_CFG_GRP_001","GENERAL_SECTION","General settings","General domain settings","","SYSADMIN","mahesh",CURRENT_TIMESTAMP);

-- SMTP
INSERT INTO `xe1`.`config_group`(`config_group_id`,`config_section_id`,`config_group_name`,`footer_note`,`header_note`,`allowed_roles`,`updated_by`,`last_updated_on`) VALUES("SMTP_CFG_GRP_001","SMTP_SECTION","SMTP settings","Email server configuration, all tenants will share this configuration to send and recieve emails","","SYSADMIN","mahesh",CURRENT_TIMESTAMP);
INSERT INTO `xe1`.`config_group`(`config_group_id`,`config_section_id`,`config_group_name`,`footer_note`,`header_note`,`allowed_roles`,`updated_by`,`last_updated_on`) VALUES("SMTP_CFG_GRP_002","SMTP_SECTION","SMTP settings","SMTP Security","","SYSADMIN","mahesh",CURRENT_TIMESTAMP);
-- MQ
INSERT INTO `xe1`.`config_group`(`config_group_id`,`config_section_id`,`config_group_name`,`footer_note`,`header_note`,`allowed_roles`,`updated_by`,`last_updated_on`) VALUES("MQ_CFG_GRP_001","MQ_SECTION","MQ Security","","","SYSADMIN","mahesh",CURRENT_TIMESTAMP);
INSERT INTO `xe1`.`config_group`(`config_group_id`,`config_section_id`,`config_group_name`,`footer_note`,`header_note`,`allowed_roles`,`updated_by`,`last_updated_on`) VALUES("MQ_CFG_GRP_002","MQ_SECTION","MQ Queues","","","SYSADMIN","mahesh",CURRENT_TIMESTAMP);
INSERT INTO `xe1`.`config_group`(`config_group_id`,`config_section_id`,`config_group_name`,`footer_note`,`header_note`,`allowed_roles`,`updated_by`,`last_updated_on`) VALUES("MQ_CFG_GRP_003","MQ_SECTION","MQ JMS Settings","","","SYSADMIN","mahesh",CURRENT_TIMESTAMP);
-- SECURITY
INSERT INTO `xe1`.`config_group`(`config_group_id`,`config_section_id`,`config_group_name`,`footer_note`,`header_note`,`allowed_roles`,`updated_by`,`last_updated_on`) VALUES("SECURITY_CFG_GRP_001","SECURITY_SECTION","Authentication","Authentication mechanism","","SYSADMIN","mahesh",CURRENT_TIMESTAMP);

-- config

-- GENERAL
INSERT INTO `xe1`.`config`(`config_id`,`config_group_id`,`config_name`,`config_value`,`updated_by`,`last_updated_on`) VALUES("GENERAL_DOMAIN_ROOT","GEN_CFG_GRP_001","General domain root","cloud.localhost/xe1","mahesh",CURRENT_TIMESTAMP);

-- SMTP
INSERT INTO `xe1`.`config`(`config_id`,`config_group_id`,`config_name`,`config_value`,`updated_by`,`last_updated_on`) VALUES("SMTP_HOST_URL","SMTP_CFG_GRP_001","SMTP Host URL","mail.intellectseeclabs.com","mahesh",CURRENT_TIMESTAMP);
INSERT INTO `xe1`.`config`(`config_id`,`config_group_id`,`config_name`,`config_value`,`updated_by`,`last_updated_on`) VALUES("SMTP_TRANSPORT_PROTOCOL","SMTP_CFG_GRP_001","SMTP Transport Protocol","SMTP","mahesh",CURRENT_TIMESTAMP);
INSERT INTO `xe1`.`config`(`config_id`,`config_group_id`,`config_name`,`config_value`,`updated_by`,`last_updated_on`) VALUES("SMTP_SOCKET_FACTORY_PORT","SMTP_CFG_GRP_001","SMTP Socket Factory PORT","587","mahesh",CURRENT_TIMESTAMP);
INSERT INTO `xe1`.`config`(`config_id`,`config_group_id`,`config_name`,`config_value`,`updated_by`,`last_updated_on`) VALUES("SMTP_SOCKET_FACTORY_CLASS","SMTP_CFG_GRP_001","SMTP Socket Factory Class","javax.net.ssl.SSLSocketFactory","mahesh",CURRENT_TIMESTAMP);
INSERT INTO `xe1`.`config`(`config_id`,`config_group_id`,`config_name`,`config_value`,`updated_by`,`last_updated_on`) VALUES("SMTP_ENABLE_AUTHENTICATION","SMTP_CFG_GRP_001","Enable Authentication","true","mahesh",CURRENT_TIMESTAMP);
INSERT INTO `xe1`.`config`(`config_id`,`config_group_id`,`config_name`,`config_value`,`updated_by`,`last_updated_on`) VALUES("SMTP_ENABLE_START_TLS","SMTP_CFG_GRP_001","Enable Start TLS","true","mahesh",CURRENT_TIMESTAMP);
INSERT INTO `xe1`.`config`(`config_id`,`config_group_id`,`config_name`,`config_value`,`updated_by`,`last_updated_on`) VALUES("SMTP_PORT","SMTP_CFG_GRP_001","SMTP Port","true","mahesh",CURRENT_TIMESTAMP);
INSERT INTO `xe1`.`config`(`config_id`,`config_group_id`,`config_name`,`config_value`,`updated_by`,`last_updated_on`) VALUES("SMTP_AUTH_ID","SMTP_CFG_GRP_002","SMTP Authentication Id","","mahesh",CURRENT_TIMESTAMP);
INSERT INTO `xe1`.`config`(`config_id`,`config_group_id`,`config_name`,`config_value`,`updated_by`,`last_updated_on`) VALUES("SMTP_AUTH_PSWD","SMTP_CFG_GRP_002","SMTP Authentication Password","","mahesh",CURRENT_TIMESTAMP);

-- MQ
INSERT INTO `xe1`.`config`(`config_id`,`config_group_id`,`config_name`,`config_value`,`updated_by`,`last_updated_on`) VALUES("MQ_AUTH_ID","MQ_CFG_GRP_001","MQ Authentication Id"," ","mahesh",CURRENT_TIMESTAMP);
INSERT INTO `xe1`.`config`(`config_id`,`config_group_id`,`config_name`,`config_value`,`updated_by`,`last_updated_on`) VALUES("MQ_AUTH_PSWD","MQ_CFG_GRP_001","MQ Authentication Password"," ","mahesh",CURRENT_TIMESTAMP);