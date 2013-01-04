package fr.paris.lutece.plugins.deployment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.business.FtpInfo;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class EnvironmentService implements IEnvironmentService {
	
	
    
    
    
   
	
	//private static IEnvironmentService _singleton;
	private static HashMap<String,Environment>_hashEnvironements;
	
	
	private EnvironmentService()
	{
		init();
	}
	
//	 public static IEnvironmentService getInstance(  )
//	 {
//        if ( _singleton == null )
//        {
//            _singleton = new EnvironementService(  );
//        }
//
//        return _singleton;
//	  }
	 
	 private void init()
	 {
	
		 initHashEnvironments();
		 
	 }
	 
	 
	 private void initHashEnvironments(  )
	    {
	       _hashEnvironements=new HashMap<String, Environment>();
	       
	       Environment environment;
	       String strEnvironmentsList = AppPropertiesService.getProperty ( ConstanteUtils.PROPERTY_ENVIRONMENTS_LIST);
	        
	        if ( StringUtils.isNotBlank( strEnvironmentsList ) )
	        {
	            String[] tabEnvironments = strEnvironmentsList.split( ConstanteUtils.CONSTANTE_SEPARATOR_VIRGULE );
	            

	            for ( int i = 0; i < tabEnvironments.length; i++ )
	            {
	                
	            	environment=new Environment();
	            	environment.setCode( AppPropertiesService.getProperty ( ConstanteUtils.CONSTANTE__ENVIRONMENT + tabEnvironments[i] +
                            ConstanteUtils.CONSTANTE__ENVIRONMENT_CODE ));
	            	environment.setName(AppPropertiesService.getProperty ( ConstanteUtils.CONSTANTE__ENVIRONMENT + tabEnvironments[i] +
	            			ConstanteUtils.CONSTANTE__ENVIRONMENT_NAME ));
	            	
	            		
	            		if(environment.getCode()!=null)
	            		{
	            			_hashEnvironements.put(environment.getCode(),environment );
	            		}
	            	}
	            }
	        }
	 
	 		/* (non-Javadoc)
			 * @see fr.paris.lutece.plugins.deployment.service.IEnvironmentService#getEnvironment(java.lang.String)
			 */
	 		public Environment getEnvironment(String strCode)
	 		{
	 			
	 			return _hashEnvironements.get(strCode);
	 			
	 		}
	 
	 	
		 	/* (non-Javadoc)
			 * @see fr.paris.lutece.plugins.deployment.service.IEnvironmentService#getListEnvironments(java.lang.String)
			 */
		 	public List<Environment> getListEnvironments(String strCodeApplication)
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
				 				
				 				strCodeEnvironment=AppPropertiesService.getProperty ( ConstanteUtils.CONSTANTE__ENVIRONMENT + strArea.toLowerCase()+ConstanteUtils.CONSTANTE_SEPARATOR_POINT+strEnv.toLowerCase()+
				 						ConstanteUtils.CONSTANTE__ENVIRONMENT_CODE );
				 				Environment environment=_hashEnvironements.containsKey(strCodeEnvironment)?_hashEnvironements.get(strCodeEnvironment):null;
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
		 	public HashMap<String,List<ServerApplicationInstance>> getHashServerApplicationInstance(String strCodeApplication)
		 	{
		 		
		 		HashMap<String,List<ServerApplicationInstance>> hashServerApplicationInstance=new HashMap<String, List<ServerApplicationInstance>>();
		 		List<Environment> listEnvironments=getListEnvironments(strCodeApplication);
		 		if(listEnvironments!=null)
		 		{
		 			List<ServerApplicationInstance> listServerApplicationInstance;
			 		
			 		for(Environment environment:listEnvironments)
			 		{
			 			listServerApplicationInstance=new ArrayList<ServerApplicationInstance>();
			 			listServerApplicationInstance.addAll(getListServerApplicationInstanceByEnvironment(strCodeApplication, environment.getCode()));
			 			hashServerApplicationInstance.put(environment.getCode(), listServerApplicationInstance);
			 		}
			 		
		 		}
		 		return hashServerApplicationInstance;
			 		
		 	}
		 	
		 	
		 	
		 	public List<ServerApplicationInstance> getListServerApplicationInstanceByEnvironment(String strCodeApplication,String strCodeEnvironment)
		 	{
		 		String strPlateformEnvironmentBaseUrl=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL);
		 		List<ServerApplicationInstance> listServerApplicationInstance=new ArrayList<ServerApplicationInstance>();
		 		List<String> listStrServerApplicationInstance;
		 		String strWebserviceServerApplicationJsonObjectName=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_OBJECT_NAME);
		 		String strWebserviceServerApplicationJsonDictionaryName=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_DICTIONARY_NAME);
		 		String strJSONServerApplicationInstances=null;
		 		
		 		
		 		
		 		try
		 		{
		 			strJSONServerApplicationInstances=DeploymentUtils.callPlateformEnvironmentWs(strPlateformEnvironmentBaseUrl+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +DeploymentUtils.getPlateformUrlServerApplicationInstances(strCodeApplication, strCodeEnvironment));	
		 		}catch (Exception e) {
	 				AppLogService.error(e);
	 		
		 		}
		 		if(strJSONServerApplicationInstances!=null)
		 		{
		 			
		 			listStrServerApplicationInstance=DeploymentUtils.getJSONDictionary(strWebserviceServerApplicationJsonObjectName, strWebserviceServerApplicationJsonDictionaryName, strJSONServerApplicationInstances);
		 			for(String strServerApplicationInstance:listStrServerApplicationInstance )
		 			{
		 				
		 				listServerApplicationInstance.add(getServerApplicationInstance(strCodeApplication, strServerApplicationInstance.toLowerCase(), strCodeEnvironment));
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
		 	
		 	
		 	public  ServerApplicationInstance getServerApplicationInstance(String strCodeApplication,String strServerInstanceCode,String strCodeEnvironment)
		 	{
		 		
		 		
		 		ServerApplicationInstance serverApplicationInstance=new ServerApplicationInstance();
		 		
		 		serverApplicationInstance.setCode(AppPropertiesService.getProperty ( ConstanteUtils.CONSTANTE__SERVER_APPLICATION_INSTANCE + strServerInstanceCode +
		 				ConstanteUtils.CONSTANTE__SERVER_APPLICATION_INSTANCE_CODE ));
		 		serverApplicationInstance.setName(AppPropertiesService.getProperty ( ConstanteUtils.CONSTANTE__SERVER_APPLICATION_INSTANCE + strServerInstanceCode +
		 				ConstanteUtils.CONSTANTE__SERVER_APPLICATION_INSTANCE_NAME ));
		 		serverApplicationInstance.setServerName(AppPropertiesService.getProperty ( ConstanteUtils.CONSTANTE__SERVER_APPLICATION_INSTANCE + strServerInstanceCode +
		 				ConstanteUtils.CONSTANTE__SERVER_APPLICATION_INSTANCE_SERVER_NAME));
		 		serverApplicationInstance.setMavenProfile(AppPropertiesService.getProperty ( ConstanteUtils.CONSTANTE__SERVER_APPLICATION_INSTANCE + strServerInstanceCode +
		 			 ConstanteUtils.CONSTANTE_SEPARATOR_POINT + strCodeEnvironment+ConstanteUtils.CONSTANTE__SERVER_APPLICATION_INSTANCE_MAVEN_PROFILE));
		 		
		 		serverApplicationInstance.setCodeEnvironment(strCodeEnvironment);
		 		
		 		setFtpInfo(serverApplicationInstance,strCodeApplication);
		 		
		 		return serverApplicationInstance;
		 	}
		 	
		 	
		 	
		 	public void setFtpInfo(ServerApplicationInstance serverApplicationInstance,String strCodeApplication)
		 	{
		 		
		 		
		 		serverApplicationInstance.setFtpDeployDirectoryTarget(DeploymentUtils.getDeployDirectoryTarget(strCodeApplication,serverApplicationInstance.getCodeEnvironment() , serverApplicationInstance.getCode()));
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

			
		 	
		 	
		 
		 	
		 
		 	
		 	
		 	
		 
		 	
		 	
		 	
}
		 
	    
	 
	 
	
	
	


