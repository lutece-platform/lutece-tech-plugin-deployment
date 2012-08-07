package fr.paris.lutece.plugins.deployment.util;


import java.util.ArrayList;
import java.util.List;

public final class MavenUtils
{
	private static final String TOKEN_RELEASE_VERSION = "$1";
	private static final String TOKEN_TAG = "$2";
	private static final String TOKEN_DEVELOPMENT_VERSION = "$3";
	private static final String TOKEN_USERNAME = "$4";
	private static final String TOKEN_PASSWORD = "$5";
	private static final String RELEASE_PREPARE_BASE = "release:prepare";
	private static final String RELEASE_PREPARE_ARGS = 
			"-DignoreSnapshots=true" +
			" -DreleaseVersion=" + TOKEN_RELEASE_VERSION + 
			" -Dtag=" + TOKEN_TAG + 
			" -DdevelopmentVersion=" + TOKEN_DEVELOPMENT_VERSION + 
			" -DforkMode=never" +
			" -Dusername=" + TOKEN_USERNAME +
			" -Dpassword=" + TOKEN_PASSWORD +
			" -Darguments=\"-Dmaven.test.skip=true\" --batch-mode";
	
	private MavenUtils(  )
	{
		// nothing
	}
	
	/**
	 * Builds maven arguments for release:prepare (with release:prepare)
	 * @param strReleaseVersion release version
	 * @param strTag tag
	 * @param strDevelopmentVersion development version
	 * @return the list
	 */
	
}
