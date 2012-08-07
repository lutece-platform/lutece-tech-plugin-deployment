package fr.paris.lutece.plugins.deployment.service;

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.FilterDeployment;
import fr.paris.lutece.plugins.deployment.business.MavenUser;
import fr.paris.lutece.util.ReferenceList;

public interface ISvnService {

	void init();

	ReferenceList getSites(FilterDeployment filter, MavenUser user);

	ReferenceList getTagsSite(String strUrlSite, MavenUser user);
	
	
	    
	
	String doSvnCheckoutSite( String strSiteName, String strUrl,MavenUser user,CommandResult commandResult );
	
	
	/**
	 * Checkout l'application choisie
	 *
	 * @param request
	 * @return
	 */
	String doSvnTagSite(String strSiteName, String strUrlSite,
			String strTagName, String strNextVersion, String strVersion,
			MavenUser user,CommandResult commandResult );
	
	
	
	

}