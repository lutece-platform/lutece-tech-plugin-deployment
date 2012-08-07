package fr.paris.lutece.plugins.deployment.business;

public class FormDeploySiteDTO {
	
	
	private String _strCodeEnvironement;
	private String _strCodeServerAppplicationInstance;
	private boolean _bTagSiteBeforeDeploy;
	private String _strTagToDeploy;
	
	
	public String getCodeEnvironement() {
		return _strCodeEnvironement;
	}
	public void setCodeEnvironement(String strCodeEnvironement) {
		_strCodeEnvironement = strCodeEnvironement;
	}
	public String getCodeServerAppplicationInstance() {
		return _strCodeServerAppplicationInstance;
	}
	public void setCodeServerAppplicationInstance(
			String strCodeServerAppplicationInstance) {
		_strCodeServerAppplicationInstance = strCodeServerAppplicationInstance;
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
}
