package fr.paris.lutece.plugins.deployment.business;

import net.sf.json.JSONObject;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class Action implements IAction {
	
	private String _strCode;
	private String _strName;
	private String _strI18nKeyName;
	private Integer _strStatus;
	private boolean  _bUsedForStatus;
	private String _strIconCssClass;
	
	
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#setCode(java.lang.String)
	 */
	public void setCode(String _strCode) {
		this._strCode = _strCode;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#getCode()
	 */
	public String getCode() {
		return _strCode;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#setName(java.lang.String)
	 */
	public void setName(String _strName) {
		this._strName = _strName;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#getName()
	 */
	public String getName() {
		return _strName;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#setI18nKeyName(java.lang.String)
	 */
	public void setI18nKeyName(String _strI18nKeyName) {
		this._strI18nKeyName = _strI18nKeyName;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#getI18nKeyName()
	 */
	public String getI18nKeyName() {
		return _strI18nKeyName;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#setStatus(java.lang.Integer)
	 */
	public void setStatus(Integer _strStatus) {
		this._strStatus = _strStatus;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#getStatus()
	 */
	public Integer getStatus() {
		return _strStatus;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#setUsedForStatus(boolean)
	 */
	public void setUsedForStatus(boolean _bUsedForStatus) {
		this._bUsedForStatus = _bUsedForStatus;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#isUsedForStatus()
	 */
	public boolean isUsedForStatus() {
		return _bUsedForStatus;
	}

	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#run(java.lang.String, fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance)
	 */
	public String run(String strCodeApplication,ServerApplicationInstance serverApplicationInstance)
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
	public void setIconCssClass(String _strIconCssClass) {
		this._strIconCssClass = _strIconCssClass;
	}
	public String getIconCssClass() {
		return _strIconCssClass;
	}
	
	

}
