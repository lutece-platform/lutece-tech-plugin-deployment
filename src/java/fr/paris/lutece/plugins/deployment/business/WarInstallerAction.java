package fr.paris.lutece.plugins.deployment.business;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import net.sf.json.JSONObject;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * 
 * WarInstallerAction
 *
 */
public class WarInstallerAction extends DefaultAction{
	
	
	
	@Override
	public String run(String strCodeApplication,ServerApplicationInstance serverApplicationInstance,CommandResult commandResult,ActionParameter... parameter)
	{
		
		
		
		String strPlateformEnvironmentBaseUrl=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL);
 		String strWebserviceActionJsonPropery=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_WEBSERVICE_ACTION_RESULT_JSON_PROPERTY_RESULT);
 		String strResult=null;
 		String strWebserviceEnvJsonObjectName = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_WEBSERVICE_INSTALLABLE_WAR_ACTION_OBJECT_NAME);
		String strWebserviceEnvJsonDictionaryName = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_WEBSERVICE_INSTALLABLE_WAR_ACTION_DICTIONARY_NAME);

 		String strJSONWarInstalled=null;
 		String strJSONAction=null;
 		try
 		{
 			strJSONWarInstalled=DeploymentUtils.callPlateformEnvironmentWs(strPlateformEnvironmentBaseUrl+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +DeploymentUtils.getPlateformUrlServerApplicationAction(strCodeApplication, serverApplicationInstance, this.getCode()));	
 			
 			List<String> listWarInstalls = DeploymentUtils.getJSONDictionary(
 					strWebserviceEnvJsonObjectName,
 					strWebserviceEnvJsonDictionaryName, strJSONWarInstalled);
 			if(!CollectionUtils.isEmpty(listWarInstalls))
 			{
 				
 				strJSONAction=DeploymentUtils.callPlateformEnvironmentWs(strPlateformEnvironmentBaseUrl+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +DeploymentUtils.getPlateformUrlServerApplicationAction(strCodeApplication, serverApplicationInstance, this.getCode())+ConstanteUtils.CONSTANTE_SEPARATOR_SLASH+listWarInstalls.get(0));	
 		 		if(strJSONAction != null)
 		 		{
 		 			AppLogService.info("Résultat de la commande @WAR_INSTALLER --> " +strJSONAction);
 		 			
 		 		}
 			}
 				
 				
 		
 		}catch (Exception e) {
			AppLogService.error(e);
	
 		}
 		
 		if(strJSONAction!=null)
 		{
 			JSONObject jo=DeploymentUtils.getJSONOBject(strJSONAction);
 			if(jo!=null)
 			{
 				strResult=jo.getString(strWebserviceActionJsonPropery);
 			}
 		}
 		
 	
		return strResult;
	}


}
