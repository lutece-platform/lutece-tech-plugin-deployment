package fr.paris.lutece.plugins.deployment.business;

public class Environment {
	
	private String _strCode;
	private String _strName;
	private String _strMavenProfile;
	
	
	
	public void setCode(String _strCode) {
		this._strCode = _strCode;
	}
	public String getCode() {
		return _strCode;
	}
	public void setName(String _strName) {
		this._strName = _strName;
	}
	public String getName() {
		return _strName;
	}
	
	public String getMavenProfile() {
		return _strMavenProfile;
	}
	public void setMavenProfile(String strMavenProfile) {
		_strMavenProfile = strMavenProfile;
	}
	
	

}