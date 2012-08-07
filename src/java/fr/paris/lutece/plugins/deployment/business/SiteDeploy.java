package fr.paris.lutece.plugins.deployment.business;

import java.sql.Date;

import fr.paris.lutece.plugins.deployment.util.SVNUtils;

public class SiteDeploy extends AbstractSite {
	
	private String _strWebappName;
	
	private String _strTagName;
	private String _strCodeEnvironment;
	private ServerApplicationInstance _serverApplicationInstance;
	private Date _deploymentDate;
	
	public String getTagName() {
		return _strTagName;
	}
	public void setTagName(String strTagName) {
		this._strTagName = strTagName;
	}

	public String getCodeEnvironment() {
		return _strCodeEnvironment;
	}
	public void setCodeEnvironment(String strEnvironment) {
		this._strCodeEnvironment = strEnvironment;
	}
	public ServerApplicationInstance getServerApplicationInstance() {
		return _serverApplicationInstance;
	}
	public void setServerApplicationInstance(ServerApplicationInstance serverApplicationInstance) {
		this._serverApplicationInstance = serverApplicationInstance;
	}
	public Date getDeploymentDate() {
		return _deploymentDate;
	}
	public void setDeploymentDate(Date deploymentDate) {
		_deploymentDate = deploymentDate;
	}
	
	
	public String getWebappName() {
		return _strWebappName;
	}
	public void setWebappName(String strWebappName) {
		_strWebappName = strWebappName;
	}
	
	@Override
	public String getCheckoutPath() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getSvnSiteUrl() {
		// TODO Auto-generated method stub
		return SVNUtils.getSvnUrlTagSite(getSvnBaseSiteUrl(), getTagName());
	}
	
	
	
	
}
