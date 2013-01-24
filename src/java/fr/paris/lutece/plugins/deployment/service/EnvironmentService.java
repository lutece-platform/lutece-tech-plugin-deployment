package fr.paris.lutece.plugins.deployment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.business.FtpInfo;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationAction;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class EnvironmentService implements IEnvironmentService {
	
	
    
    
    
   
	
	//private static IEnvironmentService _singleton;
	private static HashMap<String,Environment>_hashEnvironements;
	private static HashMap<String,ServerApplicationInstance>_hashServerApplicationInstance;
	private static HashMap<String,ServerApplicationAction>_hashServerApplicationAction;
	
	
	private EnvironmentService()
	{
	
	}
	
	 

	 
	 
	 private void initHashServerApplicationInstance(  )
	 {
		  List<ServerApplicationInstance> listServerApplicationInstance = SpringContextService.getBeansOfType( ServerApplicationInstance.class );
		  _hashServerApplicationInstance = new HashMap<String, ServerApplicationInstance>(  );

	        if ( listServerApplicationInstance != null )
	        {
	            for ( ServerApplicationInstance serverApplicationInstance : listServerApplicationInstance )
	            {
	            	_hashServerApplicationInstance.put( serverApplicationInstance.getType()+ConstanteUtils.CONSTANTE_SEPARATOR_POINT+serverApplicationInstance.getCode(),serverApplicationInstance );
	            }
	        }
		 
	    }
	 
	 
	 private void initHashEnvironments(  )
	    {
	      
	       
	       List<Environment> listEnvironment = SpringContextService.getBeansOfType( Environment.class );
	       _hashEnvironements=new HashMap<String, Environment>();

		        if ( listEnvironment != null )
		        {
		            for ( Environment environment:listEnvironment)
		            {
		            	_hashEnvironements.put( environment.getCode(),environment);
		            }
		        }
	    
	        }
	 private void initHashServerApplicationAction(  )
	    {
	      
	       
	       List< ServerApplicationAction> listAction = SpringContextService.getBeansOfType( ServerApplicationAction.class );
	       _hashServerApplicationAction=new HashMap<String, ServerApplicationAction>();

		        if ( listAction != null )
		        {
		            for ( ServerApplicationAction action:listAction)
		            {
		            	_hashServerApplicationAction.put( action.getCode(),action);
		            }
		        }
	    
	        }
	 
	 
	 		/* (non-Javadoc)
			 * @see fr.paris.lutece.plugins.deployment.service.IEnvironmentService#getEnvironment(java.lang.String)
			 */
	 		public Environment getEnvironment(String strCode,Locale locale)
	 		{
	 			
	 			if(_hashEnvironements==null)
	 			{
	 				initHashEnvironments();
	 			}
	 			Environment environment= _hashEnvironements.get(strCode);
	 			environment.setName(I18nService.getLocalizedString(environment.getI18nKeyName(), locale));
	 			return environment;
	 		}
	 
	 	
		 	/* (non-Javadoc)
			 * @see fr.paris.lutece.plugins.deployment.service.IEnvironmentService#getListEnvironments(java.lang.String)
			 */
		 	public List<Environment> getListEnvironments(String strCodeApplication,Locale locale)
		 	{
		 		String strPlateformEnvironmentBaseUrl=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL);
		 		List<Environment> listEnvironments=new ArrayList<Environment>();
		 		String strJSONApllicationAreas=null;
		 		String strJSONEnvironment=null;
		 		List<String> listAreas;
		 		List<String> listStrEnvironment=null;
		 		String strCodeEnvironment;
		 		
		 		String strUrlApllication=strPlateformEnvironmentBaseUrl+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +DeploymentUtils.getPlateformUrlApplication(strCodeApplication);
		 		try
		 		{
		 			strJSONApllicationAreas=DeploymentUtils.callPlateformEnvironmentWs(strUrlApllication);	
		 			
		 		}catch (Exception e) {
		 				AppLogService.error(e);
		 		
		 		} 
					
		 		listAreas=getAreas(strJSONApllicationAreas);
		 		if(listAreas!=null && listAreas.size()>0)
		 		{
		 			for(String strArea:listAreas)
		 			{
		 				strJSONEnvironment=null;
		 				try
				 		{
		 				strJSONEnvironment=DeploymentUtils.callPlateformEnvironmentWs(strPlateformEnvironmentBaseUrl+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +DeploymentUtils.getPlateformUrlEnvironments(strCodeApplication,strArea));	
				 		}catch (Exception e) {
			 				AppLogService.error(e);
			 		
				 		}
				 		if(strJSONEnvironment!=null)
				 		{
				 			
				 			listStrEnvironment=getEnvironments(strJSONEnvironment);
				 			for(String strEnv:listStrEnvironment )
				 			{
				 				
				 				strCodeEnvironment=strArea.toLowerCase()+ConstanteUtils.CONSTANTE_SEPARATOR_POINT+strEnv.toLowerCase();
				 				Environment environment=getEnvironment(strCodeEnvironment, locale);
				 				listEnvironments.add(environment);
				 			}
				 		}
				 			
		 			}
		 				
		 		}
		 		return listEnvironments;
		 	}
		 	
		 	
		 	/* (non-Javadoc)
			 * @see fr.paris.lutece.plugins.deployment.service.IEnvironmentService#getListServerApplicationInstance(java.lang.String, java.lang.String)
			 */
		 	public HashMap<String,List<ServerApplicationInstance>> getHashServerApplicationInstance(String strCodeApplication,String strServerApplicationType,Locale locale,boolean withStatus,boolean withActions)
		 	{
		 		
		 		HashMap<String,List<ServerApplicationInstance>> hashServerApplicationInstance=new HashMap<String, List<ServerApplicationInstance>>();
		 		List<Environment> listEnvironments=getListEnvironments(strCodeApplication,locale);
		 		if(listEnvironments!=null)
		 		{
		 			List<ServerApplicationInstance> listServerApplicationInstance;
			 		
			 		for(Environment environment:listEnvironments)
			 		{
			 			listServerApplicationInstance=new ArrayList<ServerApplicationInstance>();
			 			listServerApplicationInstance.addAll(getListServerApplicationInstanceByEnvironment(strCodeApplication, environment.getCode(),strServerApplicationType,locale));
			 			hashServerApplicationInstance.put(environment.getCode(), listServerApplicationInstance);
			 		}
			 		
		 		}
		 		return hashServerApplicationInstance;
			 		
		 	}
		 	
		 	
		 	
		 	public List<ServerApplicationInstance> getListServerApplicationInstanceByEnvironment(String strCodeApplication,String strCodeEnvironment,String strServerApplicationType,Locale locale)
		 	{
		 		String strPlateformEnvironmentBaseUrl=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL);
		 		List<ServerApplicationInstance> listServerApplicationInstance=new ArrayList<ServerApplicationInstance>();
		 		List<String> listStrServerApplicationInstance;
		 		String strWebserviceServerApplicationJsonObjectName=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_OBJECT_NAME);
		 		String strWebserviceServerApplicationJsonDictionaryName=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_DICTIONARY_NAME);
		 		String strJSONServerApplicationInstances=null;
		 		
		 		
		 		
		 		try
		 		{
		 			strJSONServerApplicationInstances=DeploymentUtils.callPlateformEnvironmentWs(strPlateformEnvironmentBaseUrl+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +DeploymentUtils.getPlateformUrlServerApplicationInstances(strCodeApplication, strCodeEnvironment,strServerApplicationType));	
		 		}catch (Exception e) {
	 				AppLogService.error(e);
	 		
		 		}
		 		if(strJSONServerApplicationInstances!=null)
		 		{
		 			
		 			listStrServerApplicationInstance=DeploymentUtils.getJSONDictionary(strWebserviceServerApplicationJsonObjectName, strWebserviceServerApplicationJsonDictionaryName, strJSONServerApplicationInstances);
		 			for(String strServerApplicationInstance:listStrServerApplicationInstance )
		 			{
		 				
		 				listServerApplicationInstance.add(getServerApplicationInstance(strCodeApplication, strServerApplicationInstance.toLowerCase(), strCodeEnvironment,strServerApplicationType,locale));
		 			}
		 		}
		 		return listServerApplicationInstance;
		 		
		 		
		 	}
		 	
		 	private List<String> getEnvironments(String strJsonFlux)
		 	{
		 		
		 		String strWebserviceEnvJsonObjectName=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_WEBSERVICE_ENVIRONMENTS_JSON_OBJECT_NAME);
		 		String strWebserviceEnvJsonDictionaryName=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_WEBSERVICE_EVIRONMENTS_JSON_DICTIONARY_NAME);
		 		
			 	
		 		List<String> listEnvs=null;	
			 	
			 	if(strJsonFlux!=null)
			 	{
			 		listEnvs=DeploymentUtils.getJSONDictionary(strWebserviceEnvJsonObjectName, strWebserviceEnvJsonDictionaryName, strJsonFlux);
			 	}
			 	return listEnvs;	
		 	}
		 	
		 	
		 	public  ServerApplicationInstance getServerApplicationInstance(String strCodeApplication,String strServerInstanceCode,String strCodeEnvironment,String strServerType,Locale locale)
		 	{
		 		
		 		if(_hashServerApplicationInstance==null)
		 		{
		 			
		 			initHashServerApplicationInstance();
		 		}
		 		ServerApplicationInstance defaultServerApplicationInstance=_hashServerApplicationInstance.get(strServerType+ConstanteUtils.CONSTANTE_SEPARATOR_POINT+strServerInstanceCode);
		 		ServerApplicationInstance serverApplicationInstance=newServerApplicationInstance(defaultServerApplicationInstance.getBeanName(),locale);
		 		serverApplicationInstance.setCodeEnvironment(strCodeEnvironment);
		 		serverApplicationInstance.setName(I18nService.getLocalizedString(serverApplicationInstance.getI18nKeyName(), locale));
		 		setFtpInfo(serverApplicationInstance,strCodeApplication);
		 		
		 		return serverApplicationInstance;
		 	}
		 	
		 	
		 	
		 	public void setFtpInfo(ServerApplicationInstance serverApplicationInstance,String strCodeApplication)
		 	{
		 		
		 		
		 		//serverApplicationInstance.setFtpDirectoryTarget(DeploymentUtils.getDeployDirectoryTarget(strCodeApplication,serverApplicationInstance.getCodeEnvironment() , serverApplicationInstance.getCode()));
		 		FtpInfo ftpInfo=new FtpInfo();
		 		ftpInfo.setHost(AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_HOST));
		 		ftpInfo.setPort(AppPropertiesService.getPropertyInt (ConstanteUtils.PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_PORT,21));
		 		ftpInfo.setUserLogin(AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_USER_LOGIN));
		 		ftpInfo.setUserPassword(AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_USER_PASSWORD));
		 		serverApplicationInstance.setFtpInfo(ftpInfo);
		 		
		 		
		 		
		 	}
		 	
		 	
		 	
		 	
		 	private List<String> getAreas(String strJsonFlux)
		 	{
			 	
		 		String strWebserviceAreasJsonObjectName=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_WEBSERVICE_AREAS_JSON_OBJECT_NAME);
		 		String strWebserviceAreasJsonDictionaryName=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_WEBSERVICE_AREAS_JSON_DICTIONARY_NAME);
		 		
			 	
		 		List<String> listAreas=null;	
			 	
			 	if(strJsonFlux!=null)
			 	{
			 		listAreas=DeploymentUtils.getJSONDictionary(strWebserviceAreasJsonObjectName, strWebserviceAreasJsonDictionaryName, strJsonFlux);
			 	}
			 	return listAreas;	
		 		
		 	
		 		
		 	}

			
		 	
		 	
		 private ServerApplicationInstance newServerApplicationInstance(String strBeanName,Locale locale)
		 {
			
			return (ServerApplicationInstance) SpringContextService.getBean(strBeanName);
		 }
		 
		
		 	
		 
		 private ServerApplicationAction getServerApplicationAction(String strCode,Locale locale)
		 {
			 if(_hashServerApplicationAction==null)
		 		{
		 			
		 			initHashServerApplicationAction();
		 		}
			 	ServerApplicationAction serverApplicationAction=_hashServerApplicationAction.get(strCode);
			 	if(serverApplicationAction!=null)
			 	{
			 		serverApplicationAction.setName(I18nService.getLocalizedString(serverApplicationAction.getI18nKeyName(), locale));
			 	}
			 	return serverApplicationAction;
		 }	
		 	
		 	
		 
		 	
		 	
		 	
}
		 
	    
	 
	 
	
	
	


