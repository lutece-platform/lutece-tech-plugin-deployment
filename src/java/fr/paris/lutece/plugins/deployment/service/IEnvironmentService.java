package fr.paris.lutece.plugins.deployment.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;

public interface IEnvironmentService {

	Environment getEnvironment(String strCode,Locale locale);

	List<Environment> getListEnvironments(String strCodeApplication,Locale locale);

	List<ServerApplicationInstance> getListServerApplicationInstanceByEnvironment(
			String strCodeApplication, String strCodeEnvironment,String strServerApplicationType,Locale locale);
	
	void setFtpInfo(ServerApplicationInstance serverApplicationInstance,String strCodeApplication);
	
	ServerApplicationInstance getServerApplicationInstance(String strCodeApplication,String strServerInstanceCode,String strCodeEnvironment,String strServerApplicationType,Locale locale);
	
	
	HashMap<String,List<ServerApplicationInstance>> getHashServerApplicationInstance(
			String strCodeApplication,String strServerApplicationType,Locale locale,boolean withStatus,boolean withActions);
	

}