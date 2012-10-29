package fr.paris.lutece.plugins.deployment.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.business.MavenGoals;
import fr.paris.lutece.plugins.deployment.business.MavenUser;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * 
 * MavenService : provides maven command launcher
 * 
 */
public class MavenService implements IMavenService
{

	//private static IMavenService _singleton;
	private  Invoker _invoker;

	
	
	private   MavenService()
	{
		init();	
	}
	
	public void init(  )
	{
		_invoker = new DefaultInvoker(  );
		_invoker.setMavenHome( new File(AppPropertiesService.getProperty(ConstanteUtils.CONSTANTE_MAVEN_HOME_PATH) ));
		_invoker.setLocalRepositoryDirectory( new File(AppPropertiesService.getProperty(ConstanteUtils.CONSTANTE_MAVEN_LOCAL_REPOSITORY) ));
		
	}
		
	/**
	 * Launches mvn cmd
	 * @param strPluginName plugin name (ex:  plugin-ods)
	 * @param goals maven goals
	 * @param strSVNBinPath svn bin path (ex:  /home/svn/apps/subversion/bin)
	 */
	@SuppressWarnings("unchecked")
	private String mvn(  String strSiteName,String strSitePath, List<String> goals,CommandResult commandResult)
	{
		InvocationRequest request = new DefaultInvocationRequest(  );
		request.setPomFile( new File( DeploymentUtils.getPathPomFile( strSitePath) ) );
		request.setGoals( goals );
	
		try
		{
			final StringBuffer sbLog = new StringBuffer(  );
			commandResult.setRunning( true );
			commandResult.setLog( sbLog );
			
			
			
			// logger
			_invoker.setOutputHandler( new InvocationOutputHandler(  ) {
				public void consumeLine( String strLine )
				{
					sbLog.append( strLine + "\n" );
				}
			});
			InvocationResult invocationResult = _invoker.execute( request );
					
			int nStatus = invocationResult.getExitCode(  );
			
			if ( nStatus != 0 )
			{
				commandResult.setIdError(commandResult.getLog().toString());
			}
			
			
			
			commandResult.setStatus( nStatus );
			commandResult.setRunning( false );
		}
		catch ( Exception e )
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter( sw );
			e.printStackTrace( pw );
			String errorLog = sw.toString();
			pw.flush();
			pw.close();
			try
			{
				sw.flush();
				sw.close();
			}
			catch ( IOException e1 )
			{
				// do nothing
				AppLogService.error(e1);
			}
			commandResult.setLog( commandResult.getLog(  ).append( errorLog ) );
			//_result.setIdError( ReleaseLogger.logError( _result.getLog(  ).toString(  ), e ) );
			commandResult.setStatus( CommandResult.STATUS_EXCEPTION );
			commandResult.setRunning( false );
		}
		//_endTime = new Date(  );
		
		return null;
		
	}
	
	
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.IMavenService#mvnSiteAssembly(java.lang.String, fr.paris.lutece.plugins.deployment.business.Environment, fr.paris.lutece.plugins.deployment.business.MavenUser)
	 */
	public void mvnSiteAssembly(String strSiteName, String strTagName,Environment environment,MavenUser user,CommandResult commandResult )
	{
		String strSiteLocalBasePath = DeploymentUtils.getPathCheckoutSite(strSiteName);
    	
		List<String> listGoals =MavenGoals.LUTECE_SITE_ASSEMBLY.asList();
		List<String> listGoalsProfile=new ArrayList<String>();
		listGoalsProfile.addAll(listGoals);
		listGoalsProfile.add("-P "+environment.getMavenProfile());
		 mvn( strTagName,strSiteLocalBasePath, listGoalsProfile,commandResult);
		
	}
	

	
	/**
	 * Transforme la liste en chaine, pour passer l'argument � la ligne de commande
	 * @param goals
	 * @return
	 */
	private String getGoalToString( List<String> goals )
	{
		StringBuilder sbGoal = new StringBuilder(  );
		for ( String strGoal : goals )
		{
			sbGoal.append( strGoal ).append( ConstanteUtils.CONSTANTE_SPACE );
		}
		
		return sbGoal.toString(  );
	}
	
	
	
//	public static IMavenService getInstance()
//	{
//		
//		if(_singleton ==null)
//		{
//			_singleton=new MavenService();
//		}
//		
//		return _singleton;
//		
//	}
//	
}
