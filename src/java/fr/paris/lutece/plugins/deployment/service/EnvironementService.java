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

public class EnvironementService implements IEnvironmentService {
	
	private static final String PROPERTY_ENVIRONMENTS_LIST = "deployment.environments.list";
	private static final String PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL = "deployment.plateformEnvironment.baseUrl";
	private static final String PROPERTY_WEBSERVICE_AREAS_JSON_OBJECT_NAME = "deployment.webservice.areas.jsonObjectName";
	private static final String PROPERTY_WEBSERVICE_AREAS_JSON_DICTIONARY_NAME = "deployment.webservice.areas.jsonDictionaryName";
	private static final String PROPERTY_WEBSERVICE_ENVIRONMENTS_JSON_OBJECT_NAME = "deployment.webservice.environments.jsonObjectName";
	private static final String PROPERTY_WEBSERVICE_EVIRONMENTS_JSON_DICTIONARY_NAME = "deployment.webservice.environments.jsonDictionaryName";
	private static final String PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_OBJECT_NAME = "deployment.webservice.serverApplicationInstances.jsonObjectName";
	private static final String PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_DICTIONARY_NAME = "deployment.webservice.serverApplicationInstances.jsonDictionaryName";
	
	
	
	private static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_HOST= "deployment.serverApplicationFtp.host";
	private static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_PORT= "deployment.serverApplicationFtp.port";
	private static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_USER_LOGIN= "deployment.serverApplicationFtp.userLogin";
	private static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_USER_PASSWORD= "deployment.serverApplicationFtp.userPassword";
	
	
    private static final String CONSTANTE__ENVIRONMENT= "deployment.environment.";
    private static final String CONSTANTE__SERVER_APPLICATION_INSTANCE= "deployment.serverApllicationInstance.";
    private static final String CONSTANTE__ENVIRONMENT_CODE = ".code";
    private static final String CONSTANTE__ENVIRONMENT_MAVEN_PROFILE = ".mavenProfile";
    private static final String CONSTANTE__ENVIRONMENT_NAME = ".name";
    private static final String CONSTANTE__ENVIRONMENT_SERVER_APPLICATION_INSTANCE_LIST = ".serverApplicationInstanceList";
    private static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_CODE = ".code";
    private static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_NAME = ".name";
    private static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_SERVER_NAME = ".serverName";
    private static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_FTP_WEBAPP_Url=".ftpWebAppUrl";

    
    
    
    private static final String CONSTANTE_SEPARATOR = ",";
	
	//private static IEnvironmentService _singleton;
	private static HashMap<String,Environment>_hashEnvironements;
	
	
	private EnvironementService()
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
	       String strEnvironmentsList = AppPropertiesService.getProperty ( PROPERTY_ENVIRONMENTS_LIST);
	        
	        if ( StringUtils.isNotBlank( strEnvironmentsList ) )
	        {
	            String[] tabEnvironments = strEnvironmentsList.split( CONSTANTE_SEPARATOR );
	            

	            for ( int i = 0; i < tabEnvironments.length; i++ )
	            {
	                
	            	environment=new Environment();
	            	environment.setCode( AppPropertiesService.getProperty ( CONSTANTE__ENVIRONMENT + tabEnvironments[i] +
                            CONSTANTE__ENVIRONMENT_CODE ));
	            	environment.setName(AppPropertiesService.getProperty ( CONSTANTE__ENVIRONMENT + tabEnvironments[i] +
                            CONSTANTE__ENVIRONMENT_NAME ));
	            	environment.setMavenProfile(AppPropertiesService.getProperty ( CONSTANTE__ENVIRONMENT + tabEnvironments[i] +
	            				CONSTANTE__ENVIRONMENT_MAVEN_PROFILE ));
	            		
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
		 		String strPlateformEnvironmentBaseUrl=  AppPropertiesService.getProperty (PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL);
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
				 				
				 				strCodeEnvironment=AppPropertiesService.getProperty ( CONSTANTE__ENVIRONMENT +ConstanteUtils.CONSTANTE_SEPARATOR_POINT+ strArea.toLowerCase()+ConstanteUtils.CONSTANTE_SEPARATOR_POINT+strEnv.toLowerCase()+
			                            CONSTANTE__ENVIRONMENT_CODE );
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
		 	public List<ServerApplicationInstance> getListServerApplicationInstance(String strCodeApplication)
		 	{
		 		
		 		
		 		List<ServerApplicationInstance> listServerApplicationInstance=new ArrayList<ServerApplicationInstance>();
		 		List<Environment> listEnvironments=getListEnvironments(strCodeApplication);
		 		if(listEnvironments!=null)
		 		{
			 		for(Environment environment:listEnvironments)
			 		{
			 			
			 			listServerApplicationInstance.addAll(getListServerApplicationInstanceByEnvironment(strCodeApplication, environment.getCode()));
			 		}
		 		}
		 		return listServerApplicationInstance;
			 		
		 	}
		 	
		 	
		 	
		 	public List<ServerApplicationInstance> getListServerApplicationInstanceByEnvironment(String strCodeApplication,String strCodeEnvironment)
		 	{
		 		List<ServerApplicationInstance> listServerApplicationInstance=new ArrayList<ServerApplicationInstance>();
		 		List<String> listStrServerApplicationInstance;
		 		String strWebserviceServerApplicationJsonObjectName=  AppPropertiesService.getProperty (PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_OBJECT_NAME);
		 		String strWebserviceServerApplicationJsonDictionaryName=  AppPropertiesService.getProperty (PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_OBJECT_NAME);
		 		String strJSONServerApplicationInstances=null;
		 		
		 		
		 		
		 		try
		 		{
		 			strJSONServerApplicationInstances=DeploymentUtils.callPlateformEnvironmentWs(DeploymentUtils.getPlateformUrlServerApplicationInstances(strCodeApplication, strCodeEnvironment));	
		 		}catch (Exception e) {
	 				AppLogService.error(e);
	 		
		 		}
		 		if(strJSONServerApplicationInstances!=null)
		 		{
		 			
		 			listStrServerApplicationInstance=DeploymentUtils.getJSONDictionary(strWebserviceServerApplicationJsonObjectName, strWebserviceServerApplicationJsonDictionaryName, strJSONServerApplicationInstances);
		 			for(String strServerApplicationInstance:listStrServerApplicationInstance )
		 			{
		 				
		 				listServerApplicationInstance.add(getServerApplicationInstance(strCodeApplication, strServerApplicationInstance, strCodeEnvironment));
		 			}
		 		}
		 		return listServerApplicationInstance;
		 		
		 		
		 	}
		 	
		 	private List<String> getEnvironments(String strJsonFlux)
		 	{
		 		
		 		String strWebserviceEnvJsonObjectName=  AppPropertiesService.getProperty (PROPERTY_WEBSERVICE_ENVIRONMENTS_JSON_OBJECT_NAME);
		 		String strWebserviceEnvJsonDictionaryName=  AppPropertiesService.getProperty (PROPERTY_WEBSERVICE_EVIRONMENTS_JSON_DICTIONARY_NAME);
		 		
			 	
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
		 		
		 		serverApplicationInstance.setCode(AppPropertiesService.getProperty ( CONSTANTE__SERVER_APPLICATION_INSTANCE + strServerInstanceCode +
	                    CONSTANTE__SERVER_APPLICATION_INSTANCE_CODE ));
		 		serverApplicationInstance.setName(AppPropertiesService.getProperty ( CONSTANTE__SERVER_APPLICATION_INSTANCE + strServerInstanceCode +
		 				CONSTANTE__SERVER_APPLICATION_INSTANCE_NAME ));
		 		serverApplicationInstance.setServerName(AppPropertiesService.getProperty ( CONSTANTE__SERVER_APPLICATION_INSTANCE + strServerInstanceCode +
		 				CONSTANTE__SERVER_APPLICATION_INSTANCE_SERVER_NAME));
		 		serverApplicationInstance.setCodeEnvironment(strCodeEnvironment);
		 		
		 		setFtpInfo(serverApplicationInstance,strCodeApplication);
		 		
		 		return serverApplicationInstance;
		 	}
		 	
		 	
		 	
		 	public void setFtpInfo(ServerApplicationInstance serverApplicationInstance,String strCodeApplication)
		 	{
		 		
		 		
		 		serverApplicationInstance.setFtpDeployDirectoryTarget(DeploymentUtils.getDeployDirectoryTarget(strCodeApplication,serverApplicationInstance.getCodeEnvironment() , serverApplicationInstance.getCode()));
		 		FtpInfo ftpInfo=new FtpInfo();
		 		ftpInfo.setHost(AppPropertiesService.getProperty (PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_HOST));
		 		ftpInfo.setPort(AppPropertiesService.getPropertyInt (PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_PORT,21));
		 		ftpInfo.setUserLogin(AppPropertiesService.getProperty (PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_USER_LOGIN));
		 		ftpInfo.setUserPassword(AppPropertiesService.getProperty (PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_USER_PASSWORD));
		 		
		 		
		 		
		 		
		 	}
		 	
		 	
		 	
		 	
		 	private List<String> getAreas(String strJsonFlux)
		 	{
			 	
		 		String strWebserviceAreasJsonObjectName=  AppPropertiesService.getProperty (PROPERTY_WEBSERVICE_AREAS_JSON_OBJECT_NAME);
		 		String strWebserviceAreasJsonDictionaryName=  AppPropertiesService.getProperty (PROPERTY_WEBSERVICE_AREAS_JSON_DICTIONARY_NAME);
		 		
			 	
		 		List<String> listAreas=null;	
			 	
			 	if(strJsonFlux!=null)
			 	{
			 		listAreas=DeploymentUtils.getJSONDictionary(strWebserviceAreasJsonObjectName, strWebserviceAreasJsonDictionaryName, strJsonFlux);
			 	}
			 	return listAreas;	
		 		
		 	
		 		
		 	}

			
		 	
		 	
		 
		 	
		 
		 	
		 	
		 	
		 
		 	
		 	
		 	
}
		 
	    
	 
	 
	
	
	


