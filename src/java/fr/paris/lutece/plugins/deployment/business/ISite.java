package fr.paris.lutece.plugins.deployment.business;

public interface ISite {
	
	String getCodeApplication();
	void setCodeApplication(String strCodeApplication);
	String getSvnSiteUrl();
	String getCheckoutPath(); 
	String getName();
	String getSvnBaseSiteUrl();
	void setSvnBaseSiteUrl(String strSvnUrl);
	CommandResult getCommandResult();
	void setCommandResult(CommandResult commandResult);
	
	
}
