package fr.paris.lutece.plugins.deployment.business;

import fr.paris.lutece.portal.service.rbac.RBACAction;

public class ManageApplicationAction implements RBACAction {

	private String _strPermission;
	private String _strUrl;
	private String _strIconCssClass;
	private String _strI18nKeyTitle;
	private String _strI18nKeyName;
	
	public String getI18nKeyTitle() {
		return _strI18nKeyTitle;
	}

	public void setI18nKeyTitle(String strTitle) {
		this._strI18nKeyTitle = strTitle;
	}
	
	public String getI18nKeyName() {
		return _strI18nKeyName;
	}

	public void setI18nKeyName(String strName) {
		this._strI18nKeyName = strName;
	}

	@Override
	public String getPermission() {
		return _strPermission;
	}

	public void setPermission(String strPermission) {
		// TODO Auto-generated method stub
		_strPermission = strPermission;
	}

	public String getUrl() {
		return _strUrl;
	}

	public void setUrl(String _strUrl) {
		this._strUrl = _strUrl;
	}

	public void setIconCssClass(String _strIconCssClass) {
		this._strIconCssClass = _strIconCssClass;
	}

	public String getIconCssClass() {
		return _strIconCssClass;
	}


}
