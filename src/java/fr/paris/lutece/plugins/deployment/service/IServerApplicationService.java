package fr.paris.lutece.plugins.deployment.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;

public interface IServerApplicationService {

	List<ServerApplicationInstance> getListServerApplicationInstanceByEnvironment(
			String strCodeApplication, String strCodeEnvironment,
			String strServerApplicationType, Locale locale, boolean withActions,boolean withStatus);

	void setFtpInfo(ServerApplicationInstance serverApplicationInstance,
			String strCodeApplication);

	ServerApplicationInstance getServerApplicationInstance(
			String strCodeApplication, String strServerInstanceCode,
			String strCodeEnvironment, String strServerApplicationType,
			Locale locale, boolean withActionss,boolean withStatus);

	HashMap<String, List<ServerApplicationInstance>> getHashServerApplicationInstance(
			String strCodeApplication, String strServerApplicationType,
			Locale locale, boolean withActions,boolean withStatus);

}
