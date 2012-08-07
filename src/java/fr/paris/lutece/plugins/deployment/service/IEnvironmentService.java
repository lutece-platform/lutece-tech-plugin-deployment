package fr.paris.lutece.plugins.deployment.service;

import java.util.List;

import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;

public interface IEnvironmentService {

	Environment getEnvironment(String strCode);

	List<Environment> getListEnvironments(String strCodeApplication);

	List<ServerApplicationInstance> getListServerApplicationInstanceByEnvironment(
			String strCodeApplication, String strCodeEnvironment);
	
	void setFtpInfo(ServerApplicationInstance serverApplicationInstance,String strCodeApplication);
	
	ServerApplicationInstance getServerApplicationInstance(String strCodeApplication,String strServerInstanceCode,String strCodeEnvironment);
	
	
	List<ServerApplicationInstance> getListServerApplicationInstance(
			String strCodeApplication);
	

}