package fr.paris.lutece.plugins.deployment.service;

import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.deployment.business.IAction;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;

public interface IActionService {
	
	IAction getAction(String strCode,Locale locale);
	
	List<IAction> getListActionByServerApplicationInstance(String strCodeApplication,ServerApplicationInstance serverApplicationInstance,Locale locale);


	String executeAction(String strCodeApplication,IAction action,ServerApplicationInstance serverApplicationInstance);
}
