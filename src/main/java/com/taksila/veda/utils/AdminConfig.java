package com.taksila.veda.utils;

public class AdminConfig {
	
	// DB Connection Properties
	public static final String DB_NAME = "DB_NAME";
	public static final String D2C_ORACLE_DB_BUNDLE = "properties/d2c-oracle_db";
	public static final String D2C_MYSQL_DB_BUNDLE = "properties/d2c-mysql_db";
	public static final String DRIVER_CLASS = "DRIVER_CLASS";
	public static final String DRIVER_URL = "DRIVER_URL";
	public static final String DRIVER_USERNAME = "DRIVER_USERNAME";
	public static final String DRIVER_PASSWORD = "DRIVER_PASSWORD";
	public static final String INSERT_INTO_USERACCOUNT = "INSERT_INTO_USERACCOUNT";
	public static final String SELECT_EMAILID_FROM_USERACCOUNT = "SELECT_EMAILID_FROM_USERACCOUNT";
	public static final String USERACCOUNT_SEQNO = "USERACCOUNT_SEQNO";

	// Property files declarations
	public static final String D2C_CONFIG_BUNDLE = "properties/config";
	public static final String D2C_EMAIL_TEMPLATES = "properties/emailtemplates";

	// Error messages
	public static final String ERROR_INVALID_USERID = "EMD2C001";
	public static final String ERROR_INVALID_TOKEN = "ERROR_INVALID_TOKEN";
	public static final String ERROR_INVALID_ACCOUNTID = "ERROR_INVALID_ACCOUNTID";
	public static final String ERROR_INVALID_ACTIONVALUE = "ERROR_INVALID_ACTIONVALUE";
	public static final String ERROR_INVALID_EMAILIDPASSWORD = "ERROR_INVALID_EMAILIDPASSWORD";
	public static final String ERROR_INVALID_EMAILID = "ERROR_INVALID_EMAILID";
	public static final String ERROR_INVALID_PLANID = "ERROR_INVALID_PLANID";
	public static final String ERROR_INVALID_COUPONCODE = "EMD2C004";
	public static final String ERROR_COUPON_ISSUED = "EMD2C005";
	public static final String ERROR_ACCOUNT_INACTIVE = "ERROR_ACCOUNT_INACTIVE";
	public static final String ERROR_INTERNAL_SERVER_ERROR = "ERROR_INTERNAL_SERVER_ERROR";
	public static final String ERROR_OBJECT_IS_EMPTY = "ERROR_OBJECT_IS_EMPTY";
	public static final String ERROR_INVALID_ORDERID="ERROR_INVALID_ORDERID";
	public static final String ERROR_COUPON_EXPIRED = "ERROR_COUPON_EXPIRED";
	public static final String ERROR_TOKEN_EXPIRED = "ERROR_TOKEN_EXPIRED";
	public static final String ERROR_NO_INCMPL_ORDER="ERROR_NO_INCMPL_ORDER";
	public static final String ERROR_INVALID_ACCOUNTID_NO_ORDER="ERROR_INVALID_ACCOUNTID_NO_ORDER";
	public static final String ERROR_DOMAINID_ISSUED="ERROR_DOMAINID_ISSUED";
	
	// Related to SMTP Server
	public static final String MAIL_SMTP_HOST_URL = "MAIL_SMTP_HOST_URL";
	public static final String MAIL_SMTP_AUTH_ID = "MAIL_SMTP_AUTH_ID";
	public static final String MAIL_SMTP_AUTH_PWD_ENCODED = "MAIL_SMTP_AUTH_PWD_ENCODED";
	public static final String MAIL_TRANSPORT_PROTOCOL = "MAIL_TRANSPORT_PROTOCOL";
	public static final String MAIL_SMTP_SOCKETFACTORY_PORT = "MAIL_SMTP_SOCKETFACTORY_PORT";
	public static final String MAIL_SMTP_SOCKETFACTORY_CLASS = "MAIL_SMTP_SOCKETFACTORY_CLASS";
	public static final String MAIL_SMTP_AUTH_ENABLE = "MAIL_SMTP_AUTH_ENABLE";
	public static final String MAIL_SMTP_PORT = "MAIL_SMTP_PORT";

	// Related to message body
	public static final String USER_REGISTRATION_EMAIL_TEMPLATE = "USER_REGISTRATION_EMAIL_TEMPLATE";
	public static final String USER_REGISTRATION_EMAIL_SUBJECT = "USER_REGISTRATION_EMAIL_SUBJECT";
	public static final String USER_PASSWORD_RESET_EMAIL_TEMPLATE = "USER_PASSWORD_RESET_EMAIL_TEMPLATE";
	public static final String USER_PASSWORD_RESET_EMAIL_SUBJECT = "USER_PASSWORD_RESET_EMAIL_SUBJECT";
	public static final String ACTION_URL = "ACTION_URL";
	public static final String SUPPORT_EMAILID = "SUPPORT_EMAILID";
	public static final String UWX_HELPLINE = "UWX_HELPLINE";
	public static final String ADDRESS = "ADDRESS";
	public static final String MAIL_TEMPLATE = "MAIL_TEMPLATE";
	public static final String PHONE_NUMBER = "PHONE_NUMBER";
	public static final String INTELLECT_SEEC_LOGO = "INTELLECT_SEEC_LOGO";

	//URLs
	public static final String URL_USER_REG_CONFIRMATION = "URL_USER_REG_CONFIRMATION";
	public static final String QUERY_TYPE_SELECT = "SELECT";
	public static final String QUERY_TYPE_UPDATE = "UPDATE";
	public static final String PASSWORD_RESET_URL="PASSWORD_RESET_URL";
	public static final String D2C_LOGIN_URL = "D2C_LOGIN_URL";
	public static final String CONTEXT_ROOT_URI = "CONTEXT_ROOT_URI";
	public static final String CHECK_TENANT_AVAILABILITY_RESOURCE="CHECK_TENANT_AVAILABILITY_RESOURCE";
	public static final String TENANT_SUBSCRIPTION_RESOURCE="TENANT_SUBSCRIPTION_RESOURCE"; 
	 
	// Actions
	public static final String CHECK_EMAILID = "CHECK_EMAILID";
	public static final String CHECK_ACCOUNTID = "CHECK_ACCOUNTID";
	public static final String SEARCH = "SEARCH";
	public static final String VALIDATE_COUPON = "VALIDATE_COUPON";
	public static final String GET_PLAN_BY_COUPON = "GET_PLAN_BY_COUPON";
	public static final String VERIFY_ACCOUNT = "VERIFY_ACCOUNT";
	public static final String CHECK_TENANT_AVAILABILITY = "CHECK_TENANT_AVAILABILITY";
	public static final String GET_INCOMPLETE_ORDER = "GET_INCOMPLETE_ORDER";
	public static final String ORDER_INCOMPLETE = "INCOMPLETE";
	public static final String ORDER_COMPLETE = "COMPLETE";
	
	//Recaptcha 
	public static final String RECAPTCHA_SECRET = "RECAPTCHA_SECRET";
	public static final String RECAPTCHA_VERIFICATION_URL = "RECAPTCHA_VERIFICATION_URL";
	
	// Regarding Session
	public static final String USER_AUTH_SESSION_ATTR = "user-auth-info";
}
