package fr.paris.lutece.plugins.deployment.service;

import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.deployment.business.Environment;

public interface IEnvironmentService {

	Environment getEnvironment(String strCode,Locale locale);

	List<Environment> getListEnvironments(String strCodeApplication,Locale locale);

	
	

}