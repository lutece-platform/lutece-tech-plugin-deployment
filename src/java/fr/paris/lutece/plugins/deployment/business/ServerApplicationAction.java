package fr.paris.lutece.plugins.deployment.business;

public class ServerApplicationAction {
	
	private String _strCode;
	private String _strName;
	private String _strI18nKeyName;
	private Integer _strStatus;
	
	
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
	public void setI18nKeyName(String _strI18nKeyName) {
		this._strI18nKeyName = _strI18nKeyName;
	}
	public String getI18nKeyName() {
		return _strI18nKeyName;
	}
	public void setStatus(Integer _strStatus) {
		this._strStatus = _strStatus;
	}
	public Integer getStatus() {
		return _strStatus;
	}
	

}
