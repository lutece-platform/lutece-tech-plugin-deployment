package fr.paris.lutece.plugins.deployment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
	
	
	 
	 public IAction getAction(String strCode,Locale locale)
	 {
		 if(_hashServerApplicationAction==null)
	 		{
	 			
	 			initHashServerApplicationAction();
	 		}
		 	IAction serverApplicationAction=_hashServerApplicationAction.get(strCode);
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
	 			
	 			listStrActions=DeploymentUtils.getJSONDictionary( strWebserviceActionJsonDictionaryName, strJSONActions);
	 			for(String strActionCoce:listStrActions )
	 			{
	 				listActions.add(getAction(strActionCoce, locale));
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
		            	_hashServerApplicationAction.put( action.getCode(),action);
		            }
		        }
	    
	        }



	@Override
	public String executeAction(String strCodeApplication, IAction action,
			ServerApplicationInstance serverApplicationInstance) {
		// TODO Auto-generated method stub
		return  action.run(strCodeApplication, serverApplicationInstance);
	}



}
