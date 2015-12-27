package com.taksila.veda.db;

public class DB2SQLDialect extends BaseSQLDialect
{
	private String UPDATE_USERACCOUNT_SQL_STMT = "UPDATE "+USERACCOUNT_TABLE_NAME+" SET "+												
														USER_ACCT_TABLE_COLUMN_NAME.LOCALE.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.ISVALIDACCOUNT.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.THIRDPARTYAUTH.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.EMAILID.name()+" = ? ,"+
														USER_ACCT_TABLE_COLUMN_NAME.PASSWORD.name()+" = ? ,"+
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
														" WHERE "+USER_ACCT_TABLE_COLUMN_NAME.ACCOUNTID.name()+" = ? ";
	
	@Override
	public String getUPDATE_USERACCOUNT_SQL_STMT() {
		return UPDATE_USERACCOUNT_SQL_STMT;
	}
			
	
}
