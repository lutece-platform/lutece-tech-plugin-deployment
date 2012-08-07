package fr.paris.lutece.plugins.deployment.service;

import java.util.List;

import fr.paris.lutece.plugins.deployment.business.Application;
import fr.paris.lutece.plugins.deployment.business.FilterDeployment;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;

public interface IApplicationService {

	Application getApplication( int nIdApplication,Plugin plugin );
	
	List<Application> getListApplications(FilterDeployment filter, Plugin plugin);

	void init();

	void createApplication(Application application, Plugin plugin);

	void updateApplication(Application application, Plugin plugin);

	void deleteApplication(int nIdApplication, Plugin plugin);

	ReferenceList getListCategory();

}