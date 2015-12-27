package com.taksila.veda.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

public class JDBCUtil {
	
	public static Connection getConnection(String dBName) {
		Connection connection = null;

		try {
			if("ORACLE".equals(dBName)) {
				String DRIVER = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_ORACLE_DB_BUNDLE, Locale.UK, AdminConfig.DRIVER_CLASS);
				String URL = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_ORACLE_DB_BUNDLE, Locale.UK, AdminConfig.DRIVER_URL);
				String USERNAME = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_ORACLE_DB_BUNDLE, Locale.UK, AdminConfig.DRIVER_USERNAME);
				String PASSWORD = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_ORACLE_DB_BUNDLE, Locale.UK, AdminConfig.DRIVER_PASSWORD);
				
				Class.forName(DRIVER);
				connection = DriverManager.getConnection(URL, USERNAME,PASSWORD);
			} else if("MYSQL".equals(dBName)) {
				String DRIVER = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_MYSQL_DB_BUNDLE, Locale.UK, AdminConfig.DRIVER_CLASS);
				String URL = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_MYSQL_DB_BUNDLE, Locale.UK, AdminConfig.DRIVER_URL);
				String USERNAME = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_MYSQL_DB_BUNDLE, Locale.UK, AdminConfig.DRIVER_USERNAME);
				String PASSWORD = ResourceBundleUtils.getStringFromBundle(AdminConfig.D2C_MYSQL_DB_BUNDLE, Locale.UK, AdminConfig.DRIVER_PASSWORD);
				
				Class.forName(DRIVER);
				connection = DriverManager.getConnection(URL, USERNAME,PASSWORD);
			}

			if( null != connection ) {
				return connection;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
}
