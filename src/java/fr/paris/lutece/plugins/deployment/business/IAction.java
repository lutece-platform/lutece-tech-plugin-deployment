package fr.paris.lutece.plugins.deployment.business;

public interface IAction {

	void setCode(String _strCode);

	String getCode();

	void setName(String _strName);

	String getName();

	void setI18nKeyName(String _strI18nKeyName);

	String getI18nKeyName();

	void setStatus(Integer _strStatus);

	Integer getStatus();

	void setUsedForStatus(boolean _bUsedForStatus);

	boolean isUsedForStatus();

	String run(String strCodeApplication,
			ServerApplicationInstance serverApplicationInstance);
	void setIconCssClass(String _strIconCssClass);
	String getIconCssClass() ;
}