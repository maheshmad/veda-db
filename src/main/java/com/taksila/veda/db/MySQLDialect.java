package com.taksila.veda.db;

public class MySQLDialect extends BaseSQLDialect
{
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
														PLAN_TABLE_COLUMN_NAME.LOGINBYLINKEDIN.name()+" = ? ,"+
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
	
	@Override
	public String getUPDATE_USERACCOUNT_SQL_STMT() {
		return UPDATE_USERACCOUNT_SQL_STMT;
	}
	
	@Override
	public String getUPDATE_ORDERINFO_SQL_STMT() {
		return UPDATE_ORDERINFO_SQL_STMT;
	}
	
	@Override
	public String getUPDATE_ORDERINFO_BY_ACCOUNTID_SQL_STMT() {
		return UPDATE_ORDERINFO_BY_ACCOUNTID_SQL_STMT;
	}

	@Override
	public String getUPDATE_PLANINFO_SQL_STMT() {
		return UPDATE_PLANINFO_SQL_STMT;
	}

	@Override
	public String getUPDATE_COUPONINFO_BY_ACCOUNTID_SQL_STMT() {
		return UPDATE_COUPONINFO_BY_ACCOUNTID_SQL_STMT;
	}
	@Override
	public String getUPDATE_COUPONINFO_SQL_STMT() {
		return UPDATE_COUPONINFO_SQL_STMT;
	}
	@Override
	public String getUPDATE_COUPONINFO_BY_COUPONCODE_SQL_STMT() {
		return UPDATE_COUPONINFO_BY_COUPONCODE_SQL_STMT;
	}
	@Override
	public String getUPDATE_PAYMENTINFO_SQL_STMT() {
		return UPDATE_PAYMENTINFO_SQL_STMT;
	}
}