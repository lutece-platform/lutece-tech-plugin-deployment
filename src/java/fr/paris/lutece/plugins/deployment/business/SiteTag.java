package fr.paris.lutece.plugins.deployment.business;

import java.sql.Date;

import fr.paris.lutece.plugins.deployment.util.SVNUtils;

public class SiteTag extends AbstractSite {
	
	private String _strTagName;
	private String _strNextVersion;
	private String _strTagVersion;
	private Date _tagDate;
	
	
	
	
	public void setTagName(String _strTagName) {
		this._strTagName = _strTagName;
	}
	public String getTagName() {
		return _strTagName;
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
	public void setTagDate(Date _tagDate) {
		this._tagDate = _tagDate;
	}
	public Date getTagDate() {
		return _tagDate;
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
		return SVNUtils.getSvnUrlTrunkSite(getSvnBaseSiteUrl());
	}
	
	
	
	
}
