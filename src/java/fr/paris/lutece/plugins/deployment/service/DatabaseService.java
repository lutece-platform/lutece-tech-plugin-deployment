package fr.paris.lutece.plugins.deployment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;


/**
 * 
 * DatabaseService
 *
 */
public class DatabaseService implements IDatabaseService {

	
	
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.IDatabaseService#getDatabases(java.lang.String, fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance, java.util.Locale)
	 */
	@Override
	public List<String> getDatabases(String strCodeApplication,
			ServerApplicationInstance serverApplicationInstance, Locale locale) {

		String strPlateformEnvironmentBaseUrl = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL);
		List<String> listStrDatabases = new ArrayList<String>();
		
		String strWebserviceDatabasesJsonObjectName = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_WEBSERVICE_DATABASES_JSON_OBJECT_NAME);
		String strWebserviceDatabasesJsonDictionaryName = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_WEBSERVICE_DATABASES_JSON_DICTIONARY_NAME);
		String strJSONServerDatabases = null;

		try {
			strJSONServerDatabases = DeploymentUtils
					.callPlateformEnvironmentWs(strPlateformEnvironmentBaseUrl
							+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
							+ DeploymentUtils.getPlateformUrlDatabases(
									strCodeApplication,
									serverApplicationInstance));
		} catch (Exception e) {
			AppLogService.error(e);
		}

		if (strJSONServerDatabases != null) {
			
			boolean  bExecute=false;
			JSONObject jo = DeploymentUtils.getJSONOBject( strJSONServerDatabases );
			String strWebserviceActionJsonPropery = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_WEBSERVICE_ACTION_RESULT_JSON_PROPERTY_RESULT );
            if ( jo != null )
            {
            	bExecute = jo.getBoolean( strWebserviceActionJsonPropery );
            }
			
            if(bExecute)
            {
				listStrDatabases = DeploymentUtils.getJSONDictionary(
						strWebserviceDatabasesJsonObjectName,
						strWebserviceDatabasesJsonDictionaryName,
						strJSONServerDatabases);
            }

		}

		return listStrDatabases;

	}
	
	


	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.IDatabaseService#getHashDatabases(java.lang.String, java.util.HashMap, java.util.Locale)
	 */
	@Override
	public HashMap<String,List<String>> getHashDatabases(String strCodeApplication,
			HashMap<String, List<ServerApplicationInstance>> hashServerApplicationInstanceMysql, Locale locale) {
		
		HashMap<String,List<String>> hashDatabases=new HashMap<String, List<String>>();
		StringBuffer strKey;
		List<String> lsDatabases;
		for(Entry<String,List<ServerApplicationInstance>> entry: hashServerApplicationInstanceMysql.entrySet())
		{
			
			for(ServerApplicationInstance serverApplicationInstance:entry.getValue())
			{
			
				strKey=new StringBuffer();
				lsDatabases=getDatabases(strCodeApplication, serverApplicationInstance, locale);
				strKey.append(serverApplicationInstance.getCodeEnvironment());
				strKey.append(".");
				strKey.append(serverApplicationInstance.getCode());
				hashDatabases.put(strKey.toString(), lsDatabases);
			}
			
		}
		return hashDatabases;
	}
	
	

}
