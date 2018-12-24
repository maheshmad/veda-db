package com.taksila.veda.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import com.taksila.veda.model.db.base.v1_0.UserRole;
import com.taksila.veda.model.db.config.v1_0.Config;
import com.taksila.veda.model.db.config.v1_0.ConfigId;
import com.taksila.veda.model.db.config.v1_0.ConfigSection;

public interface ConfigRepositoryInterface {

	/**
	 * 
	 * @param sysadmin
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	List<ConfigSection> getConfigSectionsByRole(UserRole userrole) throws SQLException, Exception;

	/**
	 * 
	 * @param sysadmin
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	Map<ConfigId, String> getConfigsByRole(UserRole userrole) throws SQLException, Exception;

	/**
	 * 
	 * @param course
	 * @return
	 * @throws Exception
	 */
	boolean updateConfigs(List<Config> configs) throws Exception;
	/**
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 * @throws DatatypeConfigurationException
	 * @throws Exception 
	 */
	Config mapConfigRow(ResultSet resultSet) throws Exception;

}