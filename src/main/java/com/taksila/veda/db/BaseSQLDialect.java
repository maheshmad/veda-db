package com.taksila.veda.db;

import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taksila.veda.utils.AdminConfig;


public class BaseSQLDialect 
{
	private Logger logger = Logger.getLogger(BaseSQLDialect.class);
	
	public String USERACCOUNT_TABLE_NAME = "RA$APP_USERACCOUNT";
	public String PAYMENTMETHODS_TABLE_NAME = "RA$APP_PAYMENTMETHODS";
	public String PAYMENTINFO_TABLE_NAME = "RA$APP_PAYMENTINFO";
	public String ORDERINFO_TABLE_NAME = "RA$APP_ORDERINFO";
	public String INVOICEINFO_TABLE_NAME = "RA$APP_INVOICEINFO";
	public String COUPONINFO_TABLE_NAME = "RA$APP_COUPONINFO";
	public String PLANINFO_TABLE_NAME = "RA$APP_PLANINFO";
	public String TOKENINFO_TABLE_NAME = "RA$APP_TOKENINFO";
	public Set<Object> QUERY_PARAMS;
	
	// Oracle seq Tables
	public String USERACCOUNT_SEQ_TABLE_NAME = "RA$APP_USERACCOUNT_SEQ";
	public String PLANINFO_SEQ_TABLE_NAME = "RA$APP_PLANINFO_SEQ";
	public String COUPONINFO_SEQ_TABLE_NAME = "RA$APP_COUPONINFO_SEQ";
	public String ORDERINFO_SEQ_TABLE_NAME = "RA$APP_ORDERINFO_SEQ";
	public String PAYMENTINFO_SEQ_TABLE_NAME = "RA$APP_PAYMENTINFO_SEQ";
	
	public enum USER_ACCT_TABLE_COLUMN_NAME
	{
		ACCOUNTID,
		LOCALE,
		ISVALIDACCOUNT,
		THIRDPARTYAUTH,
		EMAILID,
		PASSWORD,
		FIRSTNAME,
		LASTNAME,
		PHONENO,
		TNCACCEPTEDDATE,
		TOKEN,
		TOKENGENDATE,
		TOKENEXPIRYDATE,
		USER_ADDRESS,
		USER_SUITE,
		ORGNAME,
		ORG_ADDRESS,
		ORG_SUITE,
		COUPONCODE,
		PLANID,
		FREETRIALSTARTDATE,
		FREETRIALENDDATE,
		PLANSTARTDATE,
		PLANENDDATE,
	}
	
	public enum PLAN_TABLE_COLUMN_NAME
	{ 
		PLANID, 
		PLANTYPE,
		PRICEPERUNDERWRITER,
		SPLTHREEPACK,
		NOOFREPORTS, 
		BULKUPLOAD,
		TRIALPERIOD,
		LOGINBYLINKEDIN,
		FAVORITES,
		ARCHIVEREPORTS,
		COVERAGES,
		CUSTOMICONS,
		STANDARDRULES,
		CREATERULES,
		INVITEUNDERWRITER,
		ACCESSTOFULLAPI,
		PAIDDATASRC,
		FREEDATASRC,
		CONNTOINTERNALSRC,
		FEEDBACKCONTENT,
		REMOVECONTENT,
		BRANDEDPORTAL,
		USERTRAINING,
		EXPORTREPORTS,
		FREEFILLACCORDFORMS,
		DUETODAY,
		YOUSAVE,
		DUEAFTERTRIAL,
		COUPONTRIALPERIOD,
		PLANHIGHLIGHTS,
	}
	
	
	public enum PAYMENTMETHOD_TABLE_COLUMN_NAME
	{
		METHODID,
		ACCOUNTID,
		SERVICEPROVIDER,
		STREETLINEONE,
		STREETLINETWO,
		CITY,
		STATEORPROVISION,
		COUNTRY,
		ZIP
	}
	
	public enum PAYMENTINFO_TABLE_COLUMN_NAME
	{
		TXNID,
		ACCOUNTID,
		INVOICEID,
		ORDERID,
		REQUESTEDDATE,
		PAYMENTMETHOD,
		TXNTYPE,
	}
	
	public enum ORDER_TABLE_COLUMN_NAME
	{
		ORDERID,
		ACCOUNTID,
		PRODUCTID,
		SUBSCRIPTIONPLANID,
		DOMAINID,
		DOMAINADMIN,
		ORDERSTATUS
	}
	
	public enum INVOICE_TABLE_COLUMN_NAME
	{
		INVOICEID,
		ACCOUNTID,
		ORDERID,
	}
	
	public enum COUPON_TABLE_COLUMN_NAME
	{
		COUPONID,
		ACCOUNTID,
		PLANID,
		COUPONCODE,
		EXPIRYDATE,
		ISCOUPONVALID
	}
	
	public enum TOKEN_TABLE_COLUMN_NAME 
	{
		ID,
		TOKENID,
		ACCOUNTID,
		TOKEN,
		TOKENGENDATE,
		TOKENEXPIRYDATE,
		PURPOSE,
	}
	
	private String INSERT_INTO_USERACCOUNT_SQL_STMT = "INSERT INTO "+USERACCOUNT_TABLE_NAME+" ("+									
														USER_ACCT_TABLE_COLUMN_NAME.ACCOUNTID.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.LOCALE.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.ISVALIDACCOUNT.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.THIRDPARTYAUTH.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.EMAILID.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.PASSWORD.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.FIRSTNAME.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.LASTNAME.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.PHONENO.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.TNCACCEPTEDDATE.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.TOKEN.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.TOKENGENDATE.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.TOKENEXPIRYDATE.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.USER_ADDRESS.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.USER_SUITE.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.ORGNAME.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.ORG_ADDRESS.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.ORG_SUITE.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.COUPONCODE.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.PLANID.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.FREETRIALSTARTDATE.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.FREETRIALENDDATE.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.PLANSTARTDATE.name()+","+
														USER_ACCT_TABLE_COLUMN_NAME.PLANENDDATE.name()+")"+
														" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private String INSERT_INTO_ORDERINFO_SQL_STMT = "INSERT INTO "+ORDERINFO_TABLE_NAME+" ("+									
														ORDER_TABLE_COLUMN_NAME.ORDERID.name()+","+
														ORDER_TABLE_COLUMN_NAME.ACCOUNTID.name()+","+
														ORDER_TABLE_COLUMN_NAME.PRODUCTID.name()+","+
														ORDER_TABLE_COLUMN_NAME.SUBSCRIPTIONPLANID.name()+","+
														ORDER_TABLE_COLUMN_NAME.DOMAINID.name()+","+
														ORDER_TABLE_COLUMN_NAME.DOMAINADMIN.name()+","+
														ORDER_TABLE_COLUMN_NAME.ORDERSTATUS.name()+")"+
														" VALUES (?,?,?,?,?,?,?)";
	
	private String INSERT_INTO_PLANINFO_SQL_STMT = "INSERT INTO "+PLANINFO_TABLE_NAME+" ("+	
														PLAN_TABLE_COLUMN_NAME.PLANID.name()+","+ 
														PLAN_TABLE_COLUMN_NAME.PLANTYPE.name()+","+
														PLAN_TABLE_COLUMN_NAME.PRICEPERUNDERWRITER.name()+","+
														PLAN_TABLE_COLUMN_NAME.SPLTHREEPACK.name()+","+
														PLAN_TABLE_COLUMN_NAME.NOOFREPORTS.name()+","+
														PLAN_TABLE_COLUMN_NAME.BULKUPLOAD.name()+","+
														PLAN_TABLE_COLUMN_NAME.TRIALPERIOD.name()+","+
														PLAN_TABLE_COLUMN_NAME.LOGINBYLINKEDIN.name()+","+
														PLAN_TABLE_COLUMN_NAME.FAVORITES.name()+","+
														PLAN_TABLE_COLUMN_NAME.ARCHIVEREPORTS.name()+","+
														PLAN_TABLE_COLUMN_NAME.COVERAGES.name()+","+
														PLAN_TABLE_COLUMN_NAME.CUSTOMICONS.name()+","+
														PLAN_TABLE_COLUMN_NAME.STANDARDRULES.name()+","+
														PLAN_TABLE_COLUMN_NAME.CREATERULES.name()+","+
														PLAN_TABLE_COLUMN_NAME.INVITEUNDERWRITER.name()+","+
														PLAN_TABLE_COLUMN_NAME.ACCESSTOFULLAPI.name()+","+
														PLAN_TABLE_COLUMN_NAME.PAIDDATASRC.name()+","+
														PLAN_TABLE_COLUMN_NAME.FREEDATASRC.name()+","+
														PLAN_TABLE_COLUMN_NAME.CONNTOINTERNALSRC.name()+","+
														PLAN_TABLE_COLUMN_NAME.FEEDBACKCONTENT.name()+","+
														PLAN_TABLE_COLUMN_NAME.REMOVECONTENT.name()+","+
														PLAN_TABLE_COLUMN_NAME.BRANDEDPORTAL.name()+","+
														PLAN_TABLE_COLUMN_NAME.USERTRAINING.name()+","+
														PLAN_TABLE_COLUMN_NAME.EXPORTREPORTS.name()+","+
														PLAN_TABLE_COLUMN_NAME.FREEFILLACCORDFORMS.name()+")"+
														" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private String INSERT_INTO_PAYMENTINFO_SQL_STMT = "INSERT INTO "+PAYMENTINFO_TABLE_NAME+" ("+									
														PAYMENTINFO_TABLE_COLUMN_NAME.TXNID.name()+","+
														PAYMENTINFO_TABLE_COLUMN_NAME.ACCOUNTID.name()+","+
														PAYMENTINFO_TABLE_COLUMN_NAME.INVOICEID.name()+","+
														PAYMENTINFO_TABLE_COLUMN_NAME.ORDERID.name()+","+
														PAYMENTINFO_TABLE_COLUMN_NAME.REQUESTEDDATE.name()+","+
														PAYMENTINFO_TABLE_COLUMN_NAME.PAYMENTMETHOD.name()+","+
														PAYMENTINFO_TABLE_COLUMN_NAME.TXNTYPE.name()+")"+
														" VALUES (?,?,?,?,?,?,?)";
	
	private String INSERT_INTO_COUPONINFO_SQL_STMT = "INSERT INTO "+COUPONINFO_TABLE_NAME+" ("+									
														COUPON_TABLE_COLUMN_NAME.COUPONID.name()+","+
														COUPON_TABLE_COLUMN_NAME.ACCOUNTID.name()+","+
														COUPON_TABLE_COLUMN_NAME.PLANID.name()+","+
														COUPON_TABLE_COLUMN_NAME.COUPONCODE.name()+","+
														COUPON_TABLE_COLUMN_NAME.EXPIRYDATE.name()+","+
														COUPON_TABLE_COLUMN_NAME.ISCOUPONVALID.name()+")"+
														" VALUES (?,?,?,?,?,?)";
	
	private String INSERT_INTO_TOKENINFO_SQL_STMT = "INSERT INTO "+TOKENINFO_TABLE_NAME+" ("+
														TOKEN_TABLE_COLUMN_NAME.TOKENID.name()+", "+
														TOKEN_TABLE_COLUMN_NAME.ACCOUNTID.name()+", "+
														TOKEN_TABLE_COLUMN_NAME.TOKEN.name()+", "+
														TOKEN_TABLE_COLUMN_NAME.TOKENGENDATE.name()+", "+
														TOKEN_TABLE_COLUMN_NAME.TOKENEXPIRYDATE.name()+", "+
														TOKEN_TABLE_COLUMN_NAME.PURPOSE.name()+")"+
														" VALUES (?,?,?,?,?,?)";
	
	private String UPDATE_USERACCOUNT_SQL_STMT = "UPDATE "+USERACCOUNT_TABLE_NAME+" SET "+												
														USER_ACCT_TABLE_COLUMN_NAME.LOCALE.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.ISVALIDACCOUNT.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.THIRDPARTYAUTH.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.EMAILID.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.FIRSTNAME.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.LASTNAME.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.PHONENO.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.TNCACCEPTEDDATE.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.TOKEN.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.TOKENGENDATE.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.TOKENEXPIRYDATE.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.USER_ADDRESS.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.USER_SUITE.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.ORGNAME.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.ORG_ADDRESS.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.ORG_SUITE.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.COUPONCODE.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.PLANID.name()+" = ? "+
														" WHERE "+USER_ACCT_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? ";
	
	private String UPDATE_USERACCOUNT_EMAIL_SQL_STMT = "UPDATE "+USERACCOUNT_TABLE_NAME+" SET "+												
			USER_ACCT_TABLE_COLUMN_NAME.EMAILID.name()+" = ? "+
			" WHERE "+USER_ACCT_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? ";
	
	private String UPDATE_ACCOUNTPLANINFO_SQL_STMT = "UPDATE "+USERACCOUNT_TABLE_NAME+" SET "+												
														USER_ACCT_TABLE_COLUMN_NAME.PLANID.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.COUPONCODE.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.FREETRIALSTARTDATE.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.FREETRIALENDDATE.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.PLANSTARTDATE.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.PLANENDDATE.name()+" = ? "+
														" WHERE "+USER_ACCT_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? ";
	
	private String UPDATE_USERINFO_SQL_STMT = "UPDATE "+USERACCOUNT_TABLE_NAME+" SET "+												
														USER_ACCT_TABLE_COLUMN_NAME.THIRDPARTYAUTH.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.EMAILID.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.FIRSTNAME.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.LASTNAME.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.PHONENO.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.TNCACCEPTEDDATE.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.TOKEN.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.TOKENGENDATE.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.TOKENEXPIRYDATE.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.USER_ADDRESS.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.USER_SUITE.name()+" = ? "+
														" WHERE "+USER_ACCT_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? ";
	
	private String UPDATE_ORGINFO_SQL_STMT = "UPDATE "+USERACCOUNT_TABLE_NAME+" SET "+												
														USER_ACCT_TABLE_COLUMN_NAME.ORGNAME.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.ORG_ADDRESS.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.ORG_SUITE.name()+" = ? "+
														" WHERE "+USER_ACCT_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? ";
	
	private String UPDATE_ORDERINFO_SQL_STMT = "UPDATE "+ORDERINFO_TABLE_NAME+" SET "+												
														ORDER_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? ,"+
														ORDER_TABLE_COLUMN_NAME.PRODUCTID.name()+" = ? ,"+
														ORDER_TABLE_COLUMN_NAME.SUBSCRIPTIONPLANID.name()+" = ? ,"+
														ORDER_TABLE_COLUMN_NAME.DOMAINID.name()+" = ? ,"+
														ORDER_TABLE_COLUMN_NAME.DOMAINADMIN.name()+" = ? ,"+
														ORDER_TABLE_COLUMN_NAME.ORDERSTATUS.name()+" = ? "+
														" WHERE "+ORDER_TABLE_COLUMN_NAME.ORDERID.name()+" = ? ";
	
	private String UPDATE_ORDERINFO_BY_ACCOUNTID_SQL_STMT = "UPDATE "+ORDERINFO_TABLE_NAME+" SET "+												
														ORDER_TABLE_COLUMN_NAME.ORDERID.name()+" = ? ,"+
														ORDER_TABLE_COLUMN_NAME.PRODUCTID.name()+" = ? ,"+
														ORDER_TABLE_COLUMN_NAME.SUBSCRIPTIONPLANID.name()+" = ? ,"+
														ORDER_TABLE_COLUMN_NAME.DOMAINID.name()+" = ? ,"+
														ORDER_TABLE_COLUMN_NAME.DOMAINADMIN.name()+" = ? ,"+
														ORDER_TABLE_COLUMN_NAME.ORDERSTATUS.name()+" = ? "+
														" WHERE "+ORDER_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? ";
	
	private String UPDATE_PLANINFO_SQL_STMT = "UPDATE "+PLANINFO_TABLE_NAME+" SET "+	
														PLAN_TABLE_COLUMN_NAME.PLANTYPE.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.PRICEPERUNDERWRITER.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.SPLTHREEPACK.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.NOOFREPORTS.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.BULKUPLOAD.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.TRIALPERIOD.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.LOGINBYLINKEDIN.name()+" = ?,"+
														PLAN_TABLE_COLUMN_NAME.FAVORITES.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.ARCHIVEREPORTS.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.COVERAGES.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.CUSTOMICONS.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.STANDARDRULES.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.CREATERULES.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.INVITEUNDERWRITER.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.ACCESSTOFULLAPI.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.PAIDDATASRC.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.FREEDATASRC.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.CONNTOINTERNALSRC.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.FEEDBACKCONTENT.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.REMOVECONTENT.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.BRANDEDPORTAL.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.USERTRAINING.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.EXPORTREPORTS.name()+" = ? ,"+
														PLAN_TABLE_COLUMN_NAME.FREEFILLACCORDFORMS.name()+" = ? "+
														" WHERE "+PLAN_TABLE_COLUMN_NAME.PLANID.name()+" = ? ";
	
	private String UPDATE_PAYMENTINFO_SQL_STMT = "UPDATE "+PAYMENTINFO_TABLE_NAME+" SET "+												
														PAYMENTINFO_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? ,"+
														PAYMENTINFO_TABLE_COLUMN_NAME.INVOICEID.name()+" = ? ,"+
														PAYMENTINFO_TABLE_COLUMN_NAME.ORDERID.name()+" = ? ,"+
														PAYMENTINFO_TABLE_COLUMN_NAME.REQUESTEDDATE.name()+" = ? , "+
														PAYMENTINFO_TABLE_COLUMN_NAME.PAYMENTMETHOD.name()+" = ? , "+
														PAYMENTINFO_TABLE_COLUMN_NAME.TXNTYPE.name()+" = ? "+
														" WHERE "+PAYMENTINFO_TABLE_COLUMN_NAME.TXNID.name()+" = ? ";
	
	private String UPDATE_COUPONINFO_BY_ACCOUNTID_SQL_STMT = "UPDATE "+COUPONINFO_TABLE_NAME+" SET "+												
														COUPON_TABLE_COLUMN_NAME.COUPONID.name()+" = ? ,"+
														COUPON_TABLE_COLUMN_NAME.PLANID.name()+" = ? ,"+
														COUPON_TABLE_COLUMN_NAME.COUPONCODE.name()+" = ? ,"+
														COUPON_TABLE_COLUMN_NAME.EXPIRYDATE.name()+" = ? "+
														" WHERE "+COUPON_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? ";
	
	private String UPDATE_COUPONINFO_SQL_STMT = "UPDATE "+COUPONINFO_TABLE_NAME+" SET "+												
														COUPON_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? ,"+
														COUPON_TABLE_COLUMN_NAME.PLANID.name()+" = ? ,"+
														COUPON_TABLE_COLUMN_NAME.COUPONCODE.name()+" = ? ,"+
														COUPON_TABLE_COLUMN_NAME.EXPIRYDATE.name()+" = ? "+
														" WHERE "+COUPON_TABLE_COLUMN_NAME.COUPONID.name()+" = ? ";
	
	private String UPDATE_COUPONINFO_BY_COUPONCODE_SQL_STMT = "UPDATE "+COUPONINFO_TABLE_NAME+" SET "+												
														COUPON_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? "+
														" WHERE "+COUPON_TABLE_COLUMN_NAME.COUPONCODE.name()+" = ? ";
	
	private String UPDATE_TOKENINFO_BY_ID_SQL_STMT = "UPDATE "+TOKENINFO_TABLE_NAME+" SET "+
														TOKEN_TABLE_COLUMN_NAME.TOKENID.name()+" = ? "+
														" WHERE "+TOKEN_TABLE_COLUMN_NAME.ID.name()+" = ? ";
	
	private String SELECT_USERACCOUNT_SQL_STMT = "SELECT * FROM "+USERACCOUNT_TABLE_NAME+" WHERE "+USER_ACCT_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? ";

	private String SELECT_USERACCOUNT_SEQ_SQL_STMT = "SELECT count(*)+1 FROM "+USERACCOUNT_TABLE_NAME;
	
	private String SELECT_USERACCOUNT_SEQ_STMT = "SELECT "+USERACCOUNT_SEQ_TABLE_NAME+".NEXTVAL FROM DUAL";
	
	private String SELECT_PLANINFO_SEQ_STMT = "SELECT "+PLANINFO_SEQ_TABLE_NAME+".NEXTVAL FROM DUAL";
	
	private String SELECT_PLANINFO_SEQ_SQL_STMT = "SELECT count(*)+1 FROM "+PLANINFO_TABLE_NAME;
	
	private String SELECT_COUPONINFO_SEQ_STMT = "SELECT "+COUPONINFO_SEQ_TABLE_NAME+".NEXTVAL FROM DUAL";
	
	private String SELECT_ORDERINFO_SEQ_STMT = "SELECT "+ORDERINFO_SEQ_TABLE_NAME+".NEXTVAL FROM DUAL";
	
	private String SELECT_ORDERINFO_SEQ_SQL_STMT = "SELECT count(*)+1 from "+ORDERINFO_TABLE_NAME;
	
	private String SELECT_PAYMENTINFO_SEQ_STMT = "SELECT "+PAYMENTINFO_SEQ_TABLE_NAME+".NEXTVAL FROM DUAL";
	
	private String SELECT_ALL_PLANS_SQL_STMT = "SELECT * FROM "+PLANINFO_TABLE_NAME;
	
	private String SELECT_USERACCOUNT_FOR_EMAIL_SQL_STMT = "SELECT EMAILID FROM "+USERACCOUNT_TABLE_NAME+" WHERE "+USER_ACCT_TABLE_COLUMN_NAME.EMAILID.name()+" = ? "+"GROUP BY "+USER_ACCT_TABLE_COLUMN_NAME.EMAILID.name()+" HAVING COUNT("+USER_ACCT_TABLE_COLUMN_NAME.EMAILID.name()+") > 0";
	
	private String DELETE_USERACCOUNT_SQL_STMT = "DELETE FROM "+USERACCOUNT_TABLE_NAME+" WHERE "+USER_ACCT_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? ";
	
	private String DELETE_ORDERINFO_SQL_STMT = "DELETE FROM "+ORDERINFO_TABLE_NAME+" WHERE "+ORDER_TABLE_COLUMN_NAME.ORDERID.name()+" = ? ";
	
	private String DELETE_ORDERINFO_BY_ACC_ID_SQL_STMT = "DELETE FROM "+ORDERINFO_TABLE_NAME+" WHERE "+ORDER_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? ";
	
	private String DELETE_PLANINFO_SQL_STMT = "DELETE FROM "+PLANINFO_TABLE_NAME+" WHERE "+PLAN_TABLE_COLUMN_NAME.PLANID.name()+" = ? ";
	
	private String DELETE_PAYMENTINFO_SQL_STMT = "DELETE FROM "+PAYMENTINFO_TABLE_NAME+" WHERE "+PAYMENTINFO_TABLE_COLUMN_NAME.TXNID.name()+" = ? ";
	
	public String getUSERACCOUNT_TABLE_NAME() {
		return USERACCOUNT_TABLE_NAME;
	}

	public String getPAYMENTMETHODS_TABLE_NAME() {
		return PAYMENTMETHODS_TABLE_NAME;
	}

	public String getSELECT_USERACCOUNT_SQL_STMT() {
		return SELECT_USERACCOUNT_SQL_STMT;
	}
	
	public String getSELECT_USERACCOUNT_SEQ_SQL_STMT() {
		return SELECT_USERACCOUNT_SEQ_SQL_STMT;
	}

	public String getPAYMENTINFO_TABLE_NAME() {
		return PAYMENTINFO_TABLE_NAME;
	}

	public String getORDERINFO_TABLE_NAME() {
		return ORDERINFO_TABLE_NAME;
	}

	public String getINVOICEINFO_TABLE_NAME() {
		return INVOICEINFO_TABLE_NAME;
	}

	public String getINSERT_INTO_USERACCOUNT_SQL_STMT() {
		return INSERT_INTO_USERACCOUNT_SQL_STMT;
	}

	public String getUPDATE_USERACCOUNT_SQL_STMT() {
		return UPDATE_USERACCOUNT_SQL_STMT;
	}
	
	public String getUPDATE_USERACCOUNTEMAIL_SQL_STMT() {
		return UPDATE_USERACCOUNT_EMAIL_SQL_STMT;
	}

	public String getUPDATE_ACCOUNTPLANINFO_SQL_STMT() {
		return UPDATE_ACCOUNTPLANINFO_SQL_STMT;
	}

	public String getUPDATE_USERINFO_SQL_STMT() {
		return UPDATE_USERINFO_SQL_STMT;
	}

	public String getUPDATE_ORGINFO_SQL_STMT() {
		return UPDATE_ORGINFO_SQL_STMT;
	}

	public String getSELECT_USERACCOUNT_SEQ_STMT() {
		return SELECT_USERACCOUNT_SEQ_STMT;
	}
	
	public String getSELECT_PLANINFO_SEQ_STMT() {
		return SELECT_PLANINFO_SEQ_STMT;
	}
	
	public String getSELECT_PLANINFO_SEQ_SQL_STMT() {
		return SELECT_PLANINFO_SEQ_SQL_STMT;
	}
	
	public String getSELECT_COUPONINFO_SEQ_STMT() {
		return SELECT_COUPONINFO_SEQ_STMT;
	}
	
	public String getSELECT_ORDERINFO_SEQ_STMT() {
		return SELECT_ORDERINFO_SEQ_STMT;
	}
	
	public String getSELECT_ORDERINFO_SEQ_SQL_STMT() {
		return SELECT_ORDERINFO_SEQ_SQL_STMT;
	}
	
	public String getSELECT_PAYMENTINFO_SEQ_STMT() {
		return SELECT_PAYMENTINFO_SEQ_STMT;
	}

	public String getSELECT_ALL_PLANS_SQL_STMT() {
		return SELECT_ALL_PLANS_SQL_STMT;
	}
	
	public String getSELECT_USERACCOUNT_FOR_EMAIL_SQL_STMT() {
		return SELECT_USERACCOUNT_FOR_EMAIL_SQL_STMT;
	}
	
	public String getDELETE_USERACCOUNT_SQL_STMT() {
		return DELETE_USERACCOUNT_SQL_STMT;
	}

	public String getINSERT_INTO_ORDERINFO_SQL_STMT() {
		return INSERT_INTO_ORDERINFO_SQL_STMT;
	}

	public String getUPDATE_ORDERINFO_SQL_STMT() {
		return UPDATE_ORDERINFO_SQL_STMT;
	}
	
	public String getUPDATE_ORDERINFO_BY_ACCOUNTID_SQL_STMT() {
		return UPDATE_ORDERINFO_BY_ACCOUNTID_SQL_STMT;
	}

	public String getINSERT_INTO_PLANINFO_SQL_STMT() {
		return INSERT_INTO_PLANINFO_SQL_STMT;
	}

	public String getUPDATE_PLANINFO_SQL_STMT() {
		return UPDATE_PLANINFO_SQL_STMT;
	}

	public String getUPDATE_PAYMENTINFO_SQL_STMT() {
		return UPDATE_PAYMENTINFO_SQL_STMT;
	}

	public String getINSERT_INTO_PAYMENTINFO_SQL_STMT() {
		return INSERT_INTO_PAYMENTINFO_SQL_STMT;
	}

	public String getDELETE_ORDERINFO_SQL_STMT() {
		return DELETE_ORDERINFO_SQL_STMT;
	}
	
	public String getDELETE_ORDERINFO_BY_ACC_ID_SQL_STMT() {
		return DELETE_ORDERINFO_BY_ACC_ID_SQL_STMT;
	}

	public String getDELETE_PLANINFO_SQL_STMT() {
		return DELETE_PLANINFO_SQL_STMT;
	}

	public String getDELETE_PAYMENTINFO_SQL_STMT() {
		return DELETE_PAYMENTINFO_SQL_STMT;
	}

	public Set<Object> getQUERY_PARAMS() {
		return QUERY_PARAMS;
	}

	public String getUPDATE_COUPONINFO_BY_ACCOUNTID_SQL_STMT() {
		return UPDATE_COUPONINFO_BY_ACCOUNTID_SQL_STMT;
	}

	public String getINSERT_INTO_COUPONINFO_SQL_STMT() {
		return INSERT_INTO_COUPONINFO_SQL_STMT;
	}
	
	public String getINSERT_INTO_TOKENINFO_SQL_STMT() {
		return INSERT_INTO_TOKENINFO_SQL_STMT;
	}
	
	public String getUPDATE_COUPONINFO_SQL_STMT() {
		return UPDATE_COUPONINFO_SQL_STMT;
	}

	public String getUPDATE_COUPONINFO_BY_COUPONCODE_SQL_STMT() {
		return UPDATE_COUPONINFO_BY_COUPONCODE_SQL_STMT;
	}
	
	public String getUPDATE_TOKENINFO_BY_ID_SQL_STMT() {
		return UPDATE_TOKENINFO_BY_ID_SQL_STMT;
	}

	public void setQUERY_PARAMS(Set<Object> qUERY_PARAMS) {
		QUERY_PARAMS = qUERY_PARAMS;
	}

	public String getSELECT_USERACCOUNT_BY_SEARCHPARAMS_SQL_STMT() {
		StringBuilder whereClause = new StringBuilder();
		whereClause.append("WHERE ");
		whereClause.append(addQueryParams("SELECT"));

		String SELECT_USERACCOUNT_BY_SEARCHPARAMS_SQL_STMT = "SELECT * FROM "+USERACCOUNT_TABLE_NAME+" "+whereClause;
		return SELECT_USERACCOUNT_BY_SEARCHPARAMS_SQL_STMT;
	}
	
	public String getSELECT_ORDERINFO_BY_SEARCHPARAMS_SQL_STMT() {
		StringBuilder whereClause = new StringBuilder();
		whereClause.append("WHERE ");
		whereClause.append(addQueryParams("SELECT"));

		String SELECT_ORDERINFO_BY_SEARCHPARAMS_SQL_STMT = "SELECT * FROM "+ORDERINFO_TABLE_NAME+" "+whereClause;
		return SELECT_ORDERINFO_BY_SEARCHPARAMS_SQL_STMT;
	}
	
	public String getSELECT_PLANINFO_BY_SEARCHPARAMS_SQL_STMT() {
		StringBuilder whereClause = new StringBuilder();
		whereClause.append("WHERE ");
		whereClause.append(addQueryParams("SELECT"));
		
		String SELECT_PLANINFO_BY_SEARCHPARAMS_SQL_STMT = "SELECT * FROM "+PLANINFO_TABLE_NAME+" "+whereClause;
		return SELECT_PLANINFO_BY_SEARCHPARAMS_SQL_STMT;
	}
	
	public String getSELECT_PAYMENTINFO_BY_SEARCHPARAMS_SQL_STMT() {
		StringBuilder whereClause = new StringBuilder();
		whereClause.append("WHERE ");
		whereClause.append(addQueryParams("SELECT"));
		
		String SELECT_PAYMENTINFO_BY_SEARCHPARAMS_SQL_STMT = "SELECT * FROM "+PAYMENTINFO_TABLE_NAME+" "+whereClause;
		return SELECT_PAYMENTINFO_BY_SEARCHPARAMS_SQL_STMT;
	}
	
	public String getSELECT_COUPONINFO_BY_SEARCHPARAMS_SQL_STMT() {
		StringBuilder whereClause = new StringBuilder();
		whereClause.append("WHERE ");
		whereClause.append(addQueryParams("SELECT"));
		
		String SELECT_PAYMENTINFO_BY_SEARCHPARAMS_SQL_STMT = "SELECT * FROM "+COUPONINFO_TABLE_NAME+" "+whereClause;
		return SELECT_PAYMENTINFO_BY_SEARCHPARAMS_SQL_STMT;
	}
	
	public String getUPDATE_USERACCOUNT_BY_PARAMS_SQL_STMT() {
		String UPDATE_USERACCOUNT_BY_PARAMS_SQL_STMT = "UPDATE "+USERACCOUNT_TABLE_NAME+" SET "+ addQueryParams("UPDATE") +" WHERE "+USER_ACCT_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? ";
		return UPDATE_USERACCOUNT_BY_PARAMS_SQL_STMT;
	}
	
	public String getUPDATE_ORDERINFO_BY_PARAMS_SQL_STMT() {
		String UPDATE_ORDERINFO_BY_PARAMS_SQL_STMT = "UPDATE "+ORDERINFO_TABLE_NAME+" SET "+ addQueryParams("UPDATE") +" WHERE "+ORDER_TABLE_COLUMN_NAME.ORDERID.name()+" = ? ";
		return UPDATE_ORDERINFO_BY_PARAMS_SQL_STMT;
	}
	
	public String getUPDATE_PLANINFO_BY_PARAMS_SQL_STMT() {
		String UPDATE_PLANINFO_BY_PARAMS_SQL_STMT = "UPDATE "+PLANINFO_TABLE_NAME+" SET "+ addQueryParams("UPDATE") +" WHERE "+PLAN_TABLE_COLUMN_NAME.PLANID.name()+" = ? ";
		return UPDATE_PLANINFO_BY_PARAMS_SQL_STMT;
	}
	
	public String getUPDATE_PAYMENTINFO_BY_PARAMS_SQL_STMT() {
		String UPDATE_PAYMENTINFO_BY_PARAMS_SQL_STMT = "UPDATE "+PAYMENTINFO_TABLE_NAME+" SET "+ addQueryParams("UPDATE") +" WHERE "+PAYMENTINFO_TABLE_COLUMN_NAME.TXNID.name()+" = ? ";
		return UPDATE_PAYMENTINFO_BY_PARAMS_SQL_STMT;
	}
	
	public String getUPDATE_COUPONINFO_BY_PARAMS_SQL_STMT() {
		String UPDATE_PAYMENTINFO_BY_PARAMS_SQL_STMT = "UPDATE "+COUPONINFO_TABLE_NAME+" SET "+ addQueryParams("UPDATE") +" WHERE "+COUPON_TABLE_COLUMN_NAME.COUPONID.name()+" = ? ";
		return UPDATE_PAYMENTINFO_BY_PARAMS_SQL_STMT;
	}
	
	private String addQueryParams(String queryType) 
	{
		logger.debug("############     	OPENING addQueryParams  With Type "+queryType+"  ############");
		StringBuilder whereClause = new StringBuilder();

		Set<Object> set = getQUERY_PARAMS();
		int size = set.size()-1;
		int count = 0;

		Iterator<Object> itr = set.iterator();
		while(itr.hasNext()) {
			whereClause.append(itr.next()).append(" = ?");
			if(count != size) {
				if(queryType.equals(AdminConfig.QUERY_TYPE_SELECT)) {
					whereClause.append(" AND ");
				} else if(queryType.equals(AdminConfig.QUERY_TYPE_UPDATE)) {
					whereClause.append(" , ");
				}
			} else {
				break;
			}

			count++;
		}

		logger.debug("############     	whereClause IS  ############"+whereClause.toString());
		logger.debug("############     	CLOSING addQueryParams()  ############");
		return whereClause.toString();
	}
}