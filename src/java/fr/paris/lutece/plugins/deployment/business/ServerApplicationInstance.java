package fr.paris.lutece.plugins.deployment.business;

public class ServerApplicationInstance {

	private String _strCode;
	private String _strCodeEnvironment;
	private String _strName;
	private String _strServerName;
	private String _strDeployDirectoryTarget;
	private FtpInfo _ftpInfo;
	private String _strMavenProfile;
	
	
	
	
	public String getName() {
		return _strName;
	}
	public void setName(String strName) {
		_strName = strName;
	}
	public String getServerName() {
		return _strServerName;
	}
	public void setServerName(String strServerName) {
		_strServerName = strServerName;
	}
	public String getFtpDeployDirectoryTarget() {
		return _strDeployDirectoryTarget;
	}
	public void setFtpDeployDirectoryTarget(String strFtpDeployDirectoryTarget) {
		_strDeployDirectoryTarget = strFtpDeployDirectoryTarget;
	}
	public void setCode(String _strCode) {
		this._strCode = _strCode;
	}
	public String getCode() {
		return _strCode;
	}
	
	public void setCodeEnvironment(String _strCodeEnvironment) {
		this._strCodeEnvironment = _strCodeEnvironment;
	}
	public String getCodeEnvironment() {
		return _strCodeEnvironment;
	}
	public void setFtpInfo(FtpInfo _ftpInfo) {
		this._ftpInfo = _ftpInfo;
	}
	public FtpInfo getFtpInfo() {
		return _ftpInfo;
	}
	
	public String getMavenProfile() {
		return _strMavenProfile;
	}
	public void setMavenProfile(String strMavenProfile) {
		_strMavenProfile = strMavenProfile;
	}

}
