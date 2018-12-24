package com.taksila.veda.db.utils;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
public class TenantDBManager 
{
	private Map<String, DataSource> dsMap = new HashMap<String,DataSource>();
		
	private DataSource getTenantDataSource(String tenantId)
	{
		if (dsMap.get(tenantId) == null)
		{
			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName("com.mysql.jdbc.Driver");
			String dsParams = "?verifyServerCertificate=false&useSSL=false&requireSSL=false"; 
//			dataSource.setUrl("jdbc:mysql://localhost:3306/xe1_"+tenantId+"_db");
			
//			if ("demo".equals(tenantId))
//				dataSource.setUrl("jdbc:mysql://localhost:3306/xe1"+dsParams);
//			else
			dataSource.setUrl("jdbc:mysql://localhost:3306/xe1_"+tenantId+"_db"+dsParams);
			
			dataSource.setUsername("xe1jdbcuser");
			dataSource.setPassword("xe1jdbcuser@123");
			
			dsMap.put(tenantId, dataSource);
		}
		
		return dsMap.get(tenantId);
	}
	
	public JdbcTemplate getJdbcTemplate(String tenantId)
	{
		return new JdbcTemplate(this.getTenantDataSource(tenantId));
	}
	
}
