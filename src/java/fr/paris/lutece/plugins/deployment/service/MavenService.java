/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.deployment.service;

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.business.MavenGoals;
import fr.paris.lutece.plugins.deployment.business.MavenUser;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.plugins.deployment.util.ReleaseUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;


/**
 *
 * MavenService : provides maven command launcher
 *
 */
public class MavenService implements IMavenService
{
    //private static IMavenService _singleton;
    private Invoker _invoker;

    private MavenService(  )
    {
        init(  );
    }

    public void init(  )
    {
        _invoker = new DefaultInvoker(  );
        _invoker.setMavenHome( new File( AppPropertiesService.getProperty( ConstanteUtils.CONSTANTE_MAVEN_HOME_PATH ) ) );
        _invoker.setLocalRepositoryDirectory( new File( AppPropertiesService.getProperty( 
                    ConstanteUtils.CONSTANTE_MAVEN_LOCAL_REPOSITORY ) ) );
    }


    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.deployment.service.IMavenService#mvnSiteAssembly(java.lang.String, fr.paris.lutece.plugins.deployment.business.Environment, fr.paris.lutece.plugins.deployment.business.MavenUser)
     */
    public void mvnSiteAssembly( String strSiteName, String strTagName, String strMavenProfile, MavenUser user,
        CommandResult commandResult )
    {
        String strSiteLocalBasePath = DeploymentUtils.getPathCheckoutSite( strSiteName );

        List<String> listGoals = MavenGoals.LUTECE_SITE_ASSEMBLY.asList(  );
        List<String> listGoalsProfile = new ArrayList<String>(  );
        listGoalsProfile.addAll( listGoals );
        listGoalsProfile.add( "-P " + strMavenProfile );
        listGoalsProfile.add( "-U" );
        mvn( strTagName, strSiteLocalBasePath, listGoalsProfile, commandResult );
    }
    
    
    public  String getSiteWarName( String strSiteName )
    {
	    String strWarGeneratedName=null;
	    try {
			String strSiteArtifactId=ReleaseUtils.getSiteArtifactId(DeploymentUtils.getPathCheckoutSite( strSiteName ));
			String strSiteVersion=ReleaseUtils.getSiteVersion(DeploymentUtils.getPathCheckoutSite( strSiteName ));
			strWarGeneratedName=strSiteArtifactId+"-"+strSiteVersion;
	    } catch (FileNotFoundException e) {
			AppLogService.error(e);
		} catch (JAXBException e) {
			
			AppLogService.error(e);
		}
	   return  strWarGeneratedName;
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

    /**
     * Launches mvn cmd
     * @param strPluginName plugin name (ex:  plugin-ods)
     * @param goals maven goals
     * @param strSVNBinPath svn bin path (ex:  /home/svn/apps/subversion/bin)
     */
    @SuppressWarnings( "unchecked" )
    private String mvn( String strSiteName, String strSitePath, List<String> goals, CommandResult commandResult )
    {
        InvocationRequest request = new DefaultInvocationRequest(  );
        request.setPomFile( new File( DeploymentUtils.getPathPomFile( strSitePath ) ) );
        request.setGoals( goals );

        try
        {
            final StringBuffer sbLog = commandResult.getLog(  );

            // logger
            _invoker.setOutputHandler( new InvocationOutputHandler(  )
                {
                    public void consumeLine( String strLine )
                    {
                        sbLog.append( strLine + "\n" );
                    }
                } );

            InvocationResult invocationResult = _invoker.execute( request );

            int nStatus = invocationResult.getExitCode(  );

            if ( nStatus != 0 )
            {
                commandResult.setError( commandResult.getLog(  ).toString(  ) );
                commandResult.setStatus( CommandResult.STATUS_ERROR );
            }
            else
            {
            	  commandResult.setStatus( CommandResult.STATUS_OK);
            }
            

           
        }
        catch ( Exception e )
        {
            StringWriter sw = new StringWriter(  );
            PrintWriter pw = new PrintWriter( sw );
            e.printStackTrace( pw );

            String errorLog = sw.toString(  );
            pw.flush(  );
            pw.close(  );

            try
            {
                sw.flush(  );
                sw.close(  );
            }
            catch ( IOException e1 )
            {
                // do nothing
                AppLogService.error( e1 );
            }

            commandResult.getLog(  ).append( errorLog );
            //_result.setIdError( ReleaseLogger.logError( _result.getLog(  ).toString(  ), e ) );
            commandResult.setStatus( CommandResult.STATUS_ERROR );
        }

        //_endTime = new Date(  );
        return null;
    }
  
    }
