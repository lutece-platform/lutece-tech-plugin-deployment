package fr.paris.lutece.plugins.deployment.business;

import java.io.Serializable;

public class ActionParameter implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _strName;
	private String _strValue;
	
	
	
	public void setName(String _strName) {
		this._strName = _strName;
	}
	public String getName() {
		return _strName;
	}
	public void setValue(String _strValue) {
		this._strValue = _strValue;
	}
	public String getValue() {
		return _strValue;
	}

}
