package com.taksila.veda.config;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.veda.db.dao.ConfigDAO;
import com.taksila.veda.model.api.base.v1_0.StatusType;
import com.taksila.veda.model.api.config.v1_0.GetConfigurationRequest;
import com.taksila.veda.model.api.config.v1_0.GetConfigurationResponse;
import com.taksila.veda.model.api.config.v1_0.UpdateConfigRequest;
import com.taksila.veda.model.api.config.v1_0.UpdateConfigResponse;
import com.taksila.veda.model.db.config.v1_0.ConfigId;
import com.taksila.veda.utils.CommonUtils;

public class ConfigComponent 
{	
	private static Logger logger = LogManager.getLogger(ConfigComponent.class.getName());
	static Map<ConfigId, String> configsMap = null;
	private String tenantlId;
	
	static
	{
		try 
		{			
			logger.trace("About to load configuration from database");			
		} 
		catch (Exception e) 
		{		
			logger.error("Error occured during configuration load"+e.getMessage());
			e.printStackTrace();
		}				
	}


	public static String getConfig(ConfigId key)
	{
		if (configsMap == null)
		{
			logger.trace("About to load configuration from database");
			loadConfigs(true);
		}
					
		if (configsMap.get(key) == null)
			CommonUtils.logEyeCatchingMessage("Config KEY = "+key.toString()+" Not found, please check your config file", true);	
		else
			configsMap.get(key).trim();

		return null;
	}

	
	public ConfigComponent(String tenantId) 
	{
		this.tenantlId = tenantId;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public GetConfigurationResponse getConfigSection(GetConfigurationRequest request)
	{
		GetConfigurationResponse resp = new GetConfigurationResponse();
		resp.setForRole(request.getForRole());
		ConfigDAO configDAO = new ConfigDAO();
		
		try 
		{
			resp.getSections().addAll(configDAO.getConfigSectionsByRole(request.getForRole()));
			resp.setSuccess(true);
		} 
		catch (Exception e) 
		{			
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;

	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public UpdateConfigResponse updateConfig(UpdateConfigRequest request)
	{
		UpdateConfigResponse resp = new UpdateConfigResponse();
		ConfigDAO configDAO = new ConfigDAO();
		
		try 
		{
			boolean success = configDAO.updateConfigs(request.getConfigs());
			resp.setMsg("Configuration updated !!");
			if (success)			
				resp.setStatus(StatusType.SUCCESS);							
			else
			{	
				resp.setStatus(StatusType.FAILED);
				resp.setMsg("Some or All of config updates failed! Please try again or check your inputs");
			}
		} 
		catch (Exception e) 
		{			
			CommonUtils.handleExceptionForResponse(resp, e);
		}
		return resp;
	}
	
	
	private static synchronized void loadConfigs(boolean isLatestUpdateRequired)
	{
		try
		{
			ConfigDAO configDAO = new ConfigDAO();
			configsMap = configDAO.getConfigsByRole(null);			
		}		 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
