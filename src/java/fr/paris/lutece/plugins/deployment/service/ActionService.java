package fr.paris.lutece.plugins.deployment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import fr.paris.lutece.plugins.deployment.business.ActionParameter;
import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.IAction;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class ActionService implements IActionService {
	
	
	
	private static HashMap<String,IAction>_hashServerApplicationAction;
	
	
	 
	 public IAction getAction(String strKey,Locale locale)
	 {
		 if(_hashServerApplicationAction==null)
	 		{
	 			
	 			initHashServerApplicationAction();
	 		}
		 	IAction serverApplicationAction=_hashServerApplicationAction.get(strKey);
		 	if(serverApplicationAction!=null)
		 	{
		 		serverApplicationAction.setName(I18nService.getLocalizedString(serverApplicationAction.getI18nKeyName(), locale));
		 	}
		 	return serverApplicationAction;
	 }	
	 
	 
	 
	 public List<IAction> getListActionByServerApplicationInstance(String strCodeApplication,ServerApplicationInstance serverApplicationInstance,Locale locale)
	 	{
	 		String strPlateformEnvironmentBaseUrl=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL);
	 		List<IAction> listActions=new ArrayList<IAction>();
	 		List<String> listStrActions;
	 			String strWebserviceActionJsonDictionaryName=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_WEBSERVICE_SERVER_ACTIONS_JSON_DICTIONARY_NAME);
	 		String strJSONActions=null;
	 		
	 		
	 		
	 		try
	 		{
	 			strJSONActions=DeploymentUtils.callPlateformEnvironmentWs(strPlateformEnvironmentBaseUrl+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +DeploymentUtils.getPlateformUrlServerApplicationActions(strCodeApplication, serverApplicationInstance));	
	 		}catch (Exception e) {
				AppLogService.error(e);
		
	 		}
	 		if(strJSONActions!=null)
	 		{
	 			IAction action;
	 			listStrActions=DeploymentUtils.getJSONDictionary( strWebserviceActionJsonDictionaryName, strJSONActions);
	 			for(String strActionCode:listStrActions )
	 			{
	 				
	 				action=getAction(DeploymentUtils.getActionKey(strActionCode, serverApplicationInstance.getType()), locale);
	 				if(action!=null)
	 				{
	 						listActions.add(action);
	 				}
	 			}
	 		}
	 		return listActions;
	 		
	 		
	 	}
	 
	 private void initHashServerApplicationAction(  )
	    {
	      
	       
	       List< IAction> listAction = SpringContextService.getBeansOfType( IAction.class );
	       _hashServerApplicationAction=new HashMap<String, IAction>();

		        if ( listAction != null )
		        {
		            for ( IAction action:listAction)
		            {
		            	_hashServerApplicationAction.put( DeploymentUtils.getActionKey(action.getCode(), action.getServerType()),action);
		            }
		        }
	    
	        }

	 
	 public List<IAction> getListAction(Locale locale)
	 {
		 List<IAction> listActions=new ArrayList<IAction>();
		 IAction action;
		 if(_hashServerApplicationAction==null)
		 {
			 initHashServerApplicationAction();
		 }
		 for(Entry<String, IAction> entry :_hashServerApplicationAction.entrySet())
		 {
			 action=entry.getValue();
			 action.setName(I18nService.getLocalizedString(action.getI18nKeyName(), locale));
			 listActions.add(action);
		 }
		 return listActions;
		
	 }
	 
	 

	@Override
	public String executeAction(String strCodeApplication, IAction action,
			ServerApplicationInstance serverApplicationInstance,CommandResult commandResult,ActionParameter... parameter) {
		// TODO Auto-generated method stub
		return  action.run(strCodeApplication, serverApplicationInstance,commandResult,parameter);
	}
	
	
	



}
