package fr.paris.lutece.plugins.deployment.business;

import java.util.List;


public abstract class Action implements IAction {
	
	private String _strCode;
	private String _strName;
	private String _strI18nKeyName;
	private Integer _strStatus;
	private boolean  _bUsedForStatus;
	private String _strIconCssClass;
	private boolean  _bDisplay;
	private List<String> _listParameters;
	
	
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#setCode(java.lang.String)
	 */
	public void setCode(String _strCode) {
		this._strCode = _strCode;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#getCode()
	 */
	public String getCode() {
		return _strCode;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#setName(java.lang.String)
	 */
	public void setName(String _strName) {
		this._strName = _strName;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#getName()
	 */
	public String getName() {
		return _strName;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#setI18nKeyName(java.lang.String)
	 */
	public void setI18nKeyName(String _strI18nKeyName) {
		this._strI18nKeyName = _strI18nKeyName;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#getI18nKeyName()
	 */
	public String getI18nKeyName() {
		return _strI18nKeyName;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#setStatus(java.lang.Integer)
	 */
	public void setStatus(Integer _strStatus) {
		this._strStatus = _strStatus;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#getStatus()
	 */
	public Integer getStatus() {
		return _strStatus;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#setUsedForStatus(boolean)
	 */
	public void setUsedForStatus(boolean _bUsedForStatus) {
		this._bUsedForStatus = _bUsedForStatus;
	}
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.business.IAction#isUsedForStatus()
	 */
	public boolean isUsedForStatus() {
		return _bUsedForStatus;
	}

	
	public void setIconCssClass(String _strIconCssClass) {
		this._strIconCssClass = _strIconCssClass;
	}
	public String getIconCssClass() {
		return _strIconCssClass;
	}
	
	

	public boolean isDisplay() {
		// TODO Auto-generated method stub
		return _bDisplay;
	}

	public void setDisplay(boolean bDisplay) {
		_bDisplay=bDisplay;
	}
	public void setParameters(List<String> _listParameters) {
		this._listParameters = _listParameters;
	}
	public List<String> getParameters() {
		return _listParameters;
	}
	
	

}
