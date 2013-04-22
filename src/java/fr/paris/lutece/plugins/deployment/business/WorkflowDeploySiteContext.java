package fr.paris.lutece.plugins.deployment.business;

import java.io.Serializable;
import java.util.HashMap;


public class WorkflowDeploySiteContext  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String WORKFLOW_RESOURCE_TYPE = "WORKFLOW_DEPLOY_SITE_CONTEXT";
	private int _nId;
	private int _nIdApplication;
	private String _strSvnBaseSiteUrl;
	
	private HashMap<String,String> _hashServerApplicationInstance;
	private String _strCodeEnvironement;
	private String _strCodeServerAppplicationInstanceTomcat;
	private String _strCodeServerAppplicationInstanceMysql;
	private boolean _bTagSiteBeforeDeploy;
	private String _strTagToDeploy;
	private String _strTagName;
	private String _strNextVersion;
	private String _strTagVersion;
	private CommandResult _commandResult;
	private MavenUser _mavenUser;
	private boolean _bDeployWar;
	private boolean _bDeploySql;
	
	

	public int getId() {
		return _nId;
	}
	public void setId(int nId) {
		_nId = nId;
	}
	
	
	public int getIdApplication() {
		return _nIdApplication;
	}
	public void setIdApplication(int nIdApplication) {
		this._nIdApplication = nIdApplication;
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
	
	public void setNextVersion(String _strNextVersion) {
		this._strNextVersion = _strNextVersion;
	}
	public String getNextVersion() {
		return _strNextVersion;
	}
	public void setTagVersion(String _strTagVersion) {
		this._strTagVersion = _strTagVersion;
	}
	public String getTagVersion() {
		return _strTagVersion;
	}

	public String getCodeEnvironement() {
		return _strCodeEnvironement;
	}
	
	public void setCodeEnvironement(String strCodeEnvironement) {
		_strCodeEnvironement = strCodeEnvironement;
	}
	
	public String getCodeServerInstance(String strType) {
		if(_hashServerApplicationInstance!=null)
		{
			return _hashServerApplicationInstance.get(strType);
		}
		return null;
		
	}
	
	public void setCodeServerInstance(
			String strCodeServerAppplicationInstance,String strType) {
		
		if(_hashServerApplicationInstance==null)
		{
			_hashServerApplicationInstance=new HashMap<String, String>();
		}
		_hashServerApplicationInstance.put(strType, strCodeServerAppplicationInstance);
	
	}
	
	public boolean isTagSiteBeforeDeploy() {
		return _bTagSiteBeforeDeploy;
	}
	
	public void setTagSiteBeforeDeploy(boolean bTagSiteBeforeDeploy) {
		_bTagSiteBeforeDeploy = bTagSiteBeforeDeploy;
	}
	
	public String getTagToDeploy() {
		return _strTagToDeploy;
	}
	
	public void setTagToDeploy(String strTagToDeploy) {
		_strTagToDeploy = strTagToDeploy;
	}
	
	public String getTagName() {
		return _strTagName;
	}
	
	public void setTagName(String strTagName) {
		this._strTagName = strTagName;
	}
	public void setMavenUser(MavenUser _mavenUser) {
		this._mavenUser = _mavenUser;
	}
	public MavenUser getMavenUser() {
		return _mavenUser;
	}

	public void setDeployWar(boolean _bDeployWar) {
		this._bDeployWar = _bDeployWar;
	}
	public boolean isDeployWar() {
		return _bDeployWar;
	}
	public boolean isDeploySql() {
		return _bDeploySql;
	}
	public void setDeploySql(boolean bDeploySql) {
		_bDeploySql = bDeploySql;
	}
	

}
