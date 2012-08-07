package fr.paris.lutece.plugins.deployment.business;

public class Application
{
	
	private int _nIdApplication;
	private String _strCode;
	private String _strName;
	private String _strWebappName;
	private String  _strCodeCategory;
	private String _strSiteName;
	private String _strSvnUrlSite;
	
	
	
	public void setIdApplication(int _nIdApplication) {
		this._nIdApplication = _nIdApplication;
	}
	public int getIdApplication() {
		return _nIdApplication;
	}
	
	public String getCode() {
		return _strCode;
	}
	public void setCode(String strCode) {
		_strCode = strCode;
	}
	public String getName() {
		return _strName;
	}
	public void setName(String strName) {
		_strName = strName;
	}
	public String getCodeCategory() {
		return _strCodeCategory;
	}
	public void setCodeCategory(String strCodeCategory) {
		_strCodeCategory = strCodeCategory;
	}
	public String getSiteName() {
		return _strSiteName;
	}
	public void setSiteName(String strSiteName) {
		_strSiteName = strSiteName;
	}
	public String getSvnUrlSite() {
		return _strSvnUrlSite;
	}
	public void setSvnUrlSite(String strUrlSite) {
		_strSvnUrlSite = strUrlSite;
	}
	
	public String getWebAppName() {
		return _strWebappName;
	}
	public void setWebAppName(String strName) {
		_strWebappName = strName;
	}
	
	
}
