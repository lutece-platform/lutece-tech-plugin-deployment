package fr.paris.lutece.plugins.deployment.business;

public abstract class AbstractSite implements ISite{

	private String _strCodeApplication;
	private String _strSvnBaseSiteUrl;
	private CommandResult _commandResult;
	
	
	public String getCodeApplication() {
		return _strCodeApplication;
	}
	public void setCodeApplication(String strCodeApplication) {
		this._strCodeApplication = strCodeApplication;
	}
	
	public String getSvnBaseSiteUrl()
	{
		return _strSvnBaseSiteUrl;
		
	}
	public void setSvnBaseSiteUrl(String strSvnUrl)
	{
		_strSvnBaseSiteUrl=strSvnUrl;
	}
	public void setCommandResult(CommandResult _commandResult) {
		this._commandResult = _commandResult;
	}
	public CommandResult getCommandResult() {
		return _commandResult;
	}
}
