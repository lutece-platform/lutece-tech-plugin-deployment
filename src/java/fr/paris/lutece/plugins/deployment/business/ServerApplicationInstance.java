package fr.paris.lutece.plugins.deployment.business;

import java.util.HashMap;
import java.util.List;

public class ServerApplicationInstance {

	private String _strCode;
	private String _strCodeEnvironment;
	private String _strName;
	private String _strI18nKeyName;
	// private String _strServerName;
	private String _strFtpDirectoryTarget;
	private FtpInfo _ftpInfo;
	private String _strType;
	private HashMap<String, String> _hashMavenProfile;
	private String _strBeanName;
	private Integer _nStatus;
	private List<IAction> _listServerApplicationAction;

	public String getName() {
		return _strName;
	}

	public void setName(String strName) {
		_strName = strName;
	}
	public String getI18nKeyName() {
		return _strI18nKeyName;
	}

	public void setI18nKeyName(String strName) {
		_strI18nKeyName = strName;
	}
	
	public String getFtpDirectoryTarget() {
		return _strFtpDirectoryTarget;
	}

	public void setFtpDirectoryTarget(String strFtpDeployDirectoryTarget) {
		_strFtpDirectoryTarget = strFtpDeployDirectoryTarget;
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

		if (_strCodeEnvironment != null && _hashMavenProfile != null
				&& _hashMavenProfile.containsKey(_strCodeEnvironment)) {

			return _hashMavenProfile.get(_strCodeEnvironment);
		}
		return null;
	}

	public void setType(String _strType) {
		this._strType = _strType;
	}

	public String getType() {
		return _strType;
	}

	public void setHashMavenProfile(HashMap<String, String> _hashMavenProfile) {
		this._hashMavenProfile = _hashMavenProfile;
	}

	public HashMap<String, String> getHashMavenProfile() {
		return _hashMavenProfile;
	}

	public String getBeanName() {
		return _strBeanName;
	}

	public void setBeanName(String strBeanName) {
		_strBeanName = strBeanName;
	}

	public void setStatus(Integer _nStatus) {
		this._nStatus = _nStatus;
	}

	public Integer getStatus() {
		return _nStatus;
	}

	public void setListServerApplicationAction(
			List<IAction> _listServerApplicationAction) {
		this._listServerApplicationAction = _listServerApplicationAction;
	}

	public List<IAction> getListServerApplicationAction() {
		return _listServerApplicationAction;
	}

}
