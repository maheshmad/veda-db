package com.taksila.veda.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.db.SQLDataBaseManager;
import com.taksila.veda.model.db.base.v1_0.UserRole;
import com.taksila.veda.model.db.config.v1_0.Config;
import com.taksila.veda.model.db.config.v1_0.ConfigGroup;
import com.taksila.veda.model.db.config.v1_0.ConfigId;
import com.taksila.veda.model.db.config.v1_0.ConfigSection;
import com.taksila.veda.utils.CommonUtils;

public class ConfigDAO 
{
	SQLDataBaseManager sqlDBManager= null;
	static Logger logger = LogManager.getLogger(ConfigDAO.class.getName());
	
	public ConfigDAO() 
	{
		logger.trace(" Initializing ConfigDAO............ ");		
		this.sqlDBManager = new SQLDataBaseManager();
		logger.trace(" Completed initializing ConfigDAO............ ");
		
	}
	
	public enum CONFIG_TABLE
	{
		configid("config_id"),
		config_group_id("config_group_id"),
		configName("config_name"),
		configValue("config_value"),
		updatedBy("updated_by"),
		lastUpdatedOn("last_updated_on");
		private String name;       
	    private CONFIG_TABLE(String s) 
	    {
	        name = s;
	    }
		
	    public String value() 
	    {
	        return this.name;
	    }
		
	};
	
	public enum CONFIG_GROUP_TABLE
	{		
		config_group_id("config_group_id"),
		config_section_id("config_section_id"),
		configGroupName("config_group_name"),
		footerNote("footer_note"),
		headerNote("header_note"),
		allowedRoles("allowed_roles"),
		updatedBy("updated_by"),		
		lastUpdatedOn("last_updated_on");
		private String name;       
	    private CONFIG_GROUP_TABLE(String s) 
	    {
	        name = s;
	    }
		
	    public String value() 
	    {
	        return this.name;
	    }
		
	};
	
	public enum CONFIG_SECTION_TABLE
	{		
		config_section_id("config_section_id"),
		configSectionName("config_section_name"),
		viewXtype("view_xtype"),
		allowedRoles("allowed_roles"),
		updatedBy("updated_by"),		
		lastUpdatedOn("last_updated_on");
		private String name;       
	    private CONFIG_SECTION_TABLE(String s) 
	    {
	        name = s;
	    }
		
	    public String value() 
	    {
	        return this.name;
	    }
		
	};
			
	private static String update_config_sql = "UPDATE CONFIG SET "+CONFIG_TABLE.configValue.value()+" = ? "+																	
															" WHERE "+CONFIG_TABLE.configid.value()+" = ? ";

	private static String select_config_by_role_sql = "select *  "+
														"from config_section cs join "+
												        "("+
												    	"		select c."+CONFIG_TABLE.configid.value()+","+
												    	"				c."+CONFIG_TABLE.config_group_id.value()+","+
												        "               c."+CONFIG_TABLE.configName.value()+","+
												        "               c."+CONFIG_TABLE.configValue.value()+","+
												        "               cg."+CONFIG_GROUP_TABLE.config_section_id.value()+","+
												        "               cg."+CONFIG_GROUP_TABLE.configGroupName.value()+","+
												        "               cg."+CONFIG_GROUP_TABLE.footerNote.value()+","+
												        "               cg."+CONFIG_GROUP_TABLE.headerNote.value()+","+
												        "               cg."+CONFIG_GROUP_TABLE.allowedRoles.value()+","+
												        "               cg."+CONFIG_GROUP_TABLE.updatedBy.value()+","+
												        "               cg."+CONFIG_GROUP_TABLE.lastUpdatedOn.value()+                    
												    	"		from config as c"+ 
												        "       join config_group as cg"+ 
												        "		on c."+CONFIG_TABLE.config_group_id.value()+" = cg."+CONFIG_GROUP_TABLE.config_group_id.value()+
												        "       and cg."+CONFIG_GROUP_TABLE.allowedRoles.value()+" like ?"+
												        ") as cc "+ 
												    	"on cs."+CONFIG_SECTION_TABLE.config_section_id.value() +" = "+" cc."+CONFIG_SECTION_TABLE.config_section_id.value();
	

	
	private Config mapConfigRow(ResultSet resultSet) throws SQLException, DatatypeConfigurationException 
	{
		Config config = new Config();
		config.setId(resultSet.getString(CONFIG_TABLE.configid.value()));
		config.setConfigValue(resultSet.getString(CONFIG_TABLE.configValue.value()));
		config.setLastUpdatedDateTime(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(CONFIG_TABLE.lastUpdatedOn.value())));
		config.setUpdatedBy(resultSet.getString(CONFIG_TABLE.updatedBy.value()));
		
		return config;
	}
	
	/**
	 * 
	 * @param sysadmin
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<ConfigSection> getConfigSectionsByRole(UserRole userrole) throws SQLException,Exception
	{		
		Map<String, ConfigSection> configSectionsMap = new HashMap<String, ConfigSection>();
		Map<String, ConfigGroup> configGroupMap = new HashMap<String, ConfigGroup>();
		
		PreparedStatement stmt = null;		
		try
		{
			this.sqlDBManager.connect();
			
			logger.trace("Executing sql"+select_config_by_role_sql);
			stmt = this.sqlDBManager.getPreparedStatement(select_config_by_role_sql);
			if (userrole == null)
				stmt.setString(1,"%");
			else
				stmt.setString(1,userrole.name());
			
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) 
			{
				String sectionId = resultSet.getString(CONFIG_SECTION_TABLE.config_section_id.value());
				ConfigSection currentSection = configSectionsMap.get(sectionId);
				
				if (currentSection == null)
				{
					currentSection = new ConfigSection();
					configSectionsMap.put(sectionId, currentSection);
				}
				
				currentSection.setId(sectionId);
				currentSection.setLastUpdatedDateTime(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(CONFIG_SECTION_TABLE.lastUpdatedOn.value())));				
				currentSection.setSectionName(resultSet.getString(CONFIG_SECTION_TABLE.configSectionName.value()));
				currentSection.setUpdatedBy(resultSet.getString(CONFIG_SECTION_TABLE.updatedBy.value()));
				currentSection.setViewXtype(resultSet.getString(CONFIG_SECTION_TABLE.viewXtype.value()));
				
				String allowedRulesString = resultSet.getString(CONFIG_SECTION_TABLE.allowedRoles.value());
				List<UserRole> rolesList = DaoUtils.getUserRolesFromString(allowedRulesString);
				currentSection.getAllowedRoles().addAll(rolesList);
				List<UserRole> dedupeList = DaoUtils.removeDuplicates(currentSection.getAllowedRoles());
				currentSection.getAllowedRoles().clear();
				currentSection.getAllowedRoles().addAll(dedupeList);
				
				
				/*
				 * map config group
				 */
				String configGroupId = resultSet.getString(CONFIG_GROUP_TABLE.config_group_id.value()); 
				ConfigGroup configGroup = configGroupMap.get(configGroupId);
				if (configGroup == null)
				{
					configGroup = new ConfigGroup();
					configGroupMap.put(configGroupId, configGroup);
					currentSection.getConfigGroups().add(configGroup);				
				}
				
				configGroup.setId(configGroupId);
				configGroup.setConfigGroupName(resultSet.getString(CONFIG_GROUP_TABLE.configGroupName.value()));
				configGroup.setFooterNote(resultSet.getString(CONFIG_GROUP_TABLE.footerNote.value()));
				configGroup.setHeaderNote(resultSet.getString(CONFIG_GROUP_TABLE.headerNote.value()));
				configGroup.setLastUpdatedDateTime(CommonUtils.getXMLGregorianCalendarDateTimestamp(resultSet.getDate(CONFIG_GROUP_TABLE.lastUpdatedOn.value())));
				configGroup.setUpdatedBy(resultSet.getString(CONFIG_GROUP_TABLE.updatedBy.value()));
				
				/*
				 * configs
				 */
				Config config = mapConfigRow(resultSet);
				configGroup.getConfigs().add(config);
				
			}
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		finally
		{
			this.sqlDBManager.close(stmt);
		}
		
		List<ConfigSection> configSectionList = new ArrayList<ConfigSection>(configSectionsMap.values());
		
		return configSectionList;
		
	}
	
	 
	/**
	 * 
	 * @param sysadmin
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public Map<ConfigId, String> getConfigsByRole(UserRole userrole) throws SQLException,Exception
	{		
		Map<ConfigId, String> configsMap = new HashMap<ConfigId, String>();
		
		PreparedStatement stmt = null;		
		try
		{
			this.sqlDBManager.connect();
			
			stmt = this.sqlDBManager.getPreparedStatement(select_config_by_role_sql);
			if (userrole == null)
				stmt.setString(1,"%");
			else
				stmt.setString(1,userrole.name());
			
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) 
			{
				Config config = mapConfigRow(resultSet);	
				configsMap.put(ConfigId.fromValue(config.getId()), config.getConfigValue());
			}
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		finally
		{
			this.sqlDBManager.close(stmt);
		}
		
		return configsMap;
		
	}
	
	
	/**
	 * 
	 * @param course
	 * @return
	 * @throws Exception
	 */	
	public boolean updateConfigs(List<Config> configs) throws Exception 
	{		
		logger.trace("Entering into updateConfigs():::::");		
		PreparedStatement stmt = null;
		try
		{
			this.sqlDBManager.connect();
			this.sqlDBManager.setAutoCommit(false, "UpdateConfigs");
			stmt = this.sqlDBManager.getPreparedStatement(update_config_sql);
			
			for (Config config: configs)
			{				
				logger.trace("CONFIG KEY = "+config.getId()+" VALUE = "+config.getConfigValue());
				stmt.setString(1, config.getConfigValue());
				stmt.setString(2, config.getId());
				stmt.addBatch();				
			}
			
			int[] t = stmt.executeBatch();
			int totalUpdated = 0;
			for (int i:t)
			{
				totalUpdated += i;
			}
			
			this.sqlDBManager.commit();
			if (totalUpdated < configs.size())
			{
				logger.trace("Only updated "+totalUpdated+"/"+configs.size()+" rows in config");
				return false;
			}
			else
			{
				return true;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
			throw ex;
		}
		finally
		{
			this.sqlDBManager.close(stmt);
			logger.trace("Completed updateConfigs():::::");	
		}
				
								
	}
	
	
	

}
