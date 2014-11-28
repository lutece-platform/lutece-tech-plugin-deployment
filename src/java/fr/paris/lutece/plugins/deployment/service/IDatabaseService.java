package fr.paris.lutece.plugins.deployment.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;

public interface IDatabaseService {

	/**
	 * getDatabases List
	 * @param strCodeApplication the application code
	 * @param serverApplicationInstance the server application code 
	 * @param locale the lodale
	 * @return the list of databases
	 */
	public abstract List<String> getDatabases(String strCodeApplication,
			ServerApplicationInstance serverApplicationInstance, Locale locale);

	/**
	 * 
	 * @param strCodeApplication
	 * @param hashServerApplicationInstanceMysql
	 * @param locale
	 * @return
	 */
	public abstract HashMap<String, List<String>> getHashDatabases(
			String strCodeApplication,
			HashMap<String, List<ServerApplicationInstance>> hashServerApplicationInstanceMysql,
			Locale locale);

}