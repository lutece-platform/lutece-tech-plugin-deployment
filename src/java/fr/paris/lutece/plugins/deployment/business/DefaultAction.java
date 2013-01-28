package fr.paris.lutece.plugins.deployment.business;

import net.sf.json.JSONObject;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class DefaultAction  extends Action{
	
	
	
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#run(java.lang.String, fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance)
	 */
	public String run(String strCodeApplication,ServerApplicationInstance serverApplicationInstance,CommandResult commandResult,ActionParameter... parameter)
	{
		
		String strPlateformEnvironmentBaseUrl=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL);
 		String strWebserviceActionJsonPropery=  AppPropertiesService.getProperty (ConstanteUtils.PROPERTY_WEBSERVICE_ACTION_RESULT_JSON_PROPERTY_RESULT);
 		String strJSONAction=null;
 		String strResult=null;
 		
 		
 		try
 		{
 			strJSONAction=DeploymentUtils.callPlateformEnvironmentWs(strPlateformEnvironmentBaseUrl+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +DeploymentUtils.getPlateformUrlServerApplicationAction(strCodeApplication, serverApplicationInstance, this.getCode()));	
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
