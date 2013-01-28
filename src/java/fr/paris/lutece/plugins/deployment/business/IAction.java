package fr.paris.lutece.plugins.deployment.business;

import java.util.List;

public interface IAction {

	void setCode(String strCode);

	String getCode();

	void setName(String strName);

	String getName();

	void setI18nKeyName(String strI18nKeyName);

	String getI18nKeyName();

	void setStatus(Integer strStatus);

	Integer getStatus();

	void setUsedForStatus(boolean bUsedForStatus);

	boolean isUsedForStatus();
	
	boolean isDisplay();
	
	void setDisplay(boolean bDisplay);
	
	void setParameters(List<String> _listParameters);
	
	List<String> getParameters();
	

	String run(String strCodeApplication,
			ServerApplicationInstance serverApplicationInstance,CommandResult commandResult,ActionParameter... parameter);
	void setIconCssClass(String _strIconCssClass);
	String getIconCssClass() ;
}