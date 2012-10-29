package fr.paris.lutece.plugins.deployment.service;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.map.HashedMap;

import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;

public interface IEnvironmentService {

	Environment getEnvironment(String strCode);

	List<Environment> getListEnvironments(String strCodeApplication);

	List<ServerApplicationInstance> getListServerApplicationInstanceByEnvironment(
			String strCodeApplication, String strCodeEnvironment);
	
	void setFtpInfo(ServerApplicationInstance serverApplicationInstance,String strCodeApplication);
	
	ServerApplicationInstance getServerApplicationInstance(String strCodeApplication,String strServerInstanceCode,String strCodeEnvironment);
	
	
	HashMap<String,List<ServerApplicationInstance>> getHashServerApplicationInstance(
			String strCodeApplication);
	

}