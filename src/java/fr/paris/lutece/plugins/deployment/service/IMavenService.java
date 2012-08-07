package fr.paris.lutece.plugins.deployment.service;

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.business.MavenUser;

public interface IMavenService {

	/**
	 * 
	 * mvnReleasePrepare
	 * @param strBasePath chemin sur le disque pour l'acces au composant
	 * @param strPluginName le nom du composant
	 * @param strReleaseVersion la version a release
	 * @param strTag le nom du tag
	 * @param strDevelopmentVersion la prochaine version de developpement (avec -SNAPSHOT)
	 * @return le thread
	 */
	void mvnSiteAssembly(String strSiteName,String strTagName, Environment environment,
			MavenUser user,CommandResult commandResult );

}