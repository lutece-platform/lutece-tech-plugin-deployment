package fr.paris.lutece.plugins.deployment.business;

import java.util.HashMap;
import java.util.Map;

public class MavenUser {
	
	public static final String MARK_CURRENT_THREAD="current_thread";
	public static final String MARK_AUTHENTIFICATION_MANAGER="authentification_manager";
	public static final String MARK_OUR_CLIENT_MANAGER ="our_client_manager";
	
	private String _strLogin;
	private String _strPaswword;
	private Map<String, Object> _mUserContext;
	
	public MavenUser()
	{
		setUserContext(new HashMap<String, Object>());
		
	}

	public void setLogin(String strLogin) {
		this._strLogin = strLogin;
	}

	public String getLogin() {
		return _strLogin;
	}

	public void setPaswword(String strPaswword) {
		this._strPaswword = strPaswword;
	}

	public String getPasword() {
		return _strPaswword;
	}

	public void setUserContext(Map<String, Object> _mUserContex) {
		this._mUserContext = _mUserContex;
	}

	public Map<String, Object> getUserContext() {
		return _mUserContext;
	}
	
	
	

}
