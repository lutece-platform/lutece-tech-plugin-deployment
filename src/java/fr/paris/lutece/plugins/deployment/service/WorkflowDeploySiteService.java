/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import fr.paris.lutece.plugins.deployment.service.vcs.IVCSService;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.deployment.business.Application;
import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.IAction;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;
import fr.paris.lutece.plugins.deployment.business.WorkflowDeploySiteContext;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.plugins.deployment.util.ReleaseUtils;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;

public class WorkflowDeploySiteService implements IWorkflowDeploySiteService
{
    HashMap<Integer, WorkflowDeploySiteContext> _mapWorkflowDeploySiteContext = new HashMap<Integer, WorkflowDeploySiteContext>( );

    @Inject
    IEnvironmentService _environmentService;
    @Inject
    IServerApplicationService _serverApplicationService;
    @Inject
    IFtpService _ftpService;
    @Inject
    IApplicationService _applicationService;
    @Inject
    private IActionService _actionService;

    public synchronized int addWorkflowDeploySiteContext( WorkflowDeploySiteContext context )
    {
        int nIdKey = Integer.parseInt( DatastoreService.getDataValue( ConstanteUtils.CONSTANTE_MAX_DEPLOY_SITE_CONTEXT_KEY, "0" ) ) + 1;
        // stored key in database
        DatastoreService.setDataValue( ConstanteUtils.CONSTANTE_MAX_DEPLOY_SITE_CONTEXT_KEY, Integer.toString( nIdKey ) );
        context.setId( nIdKey );
        _mapWorkflowDeploySiteContext.put( nIdKey, context );

        return nIdKey;
    }

    public WorkflowDeploySiteContext getWorkflowDeploySiteContext( int nIdContext )
    {
        return _mapWorkflowDeploySiteContext.get( nIdContext );
    }

    public String checkoutSite( WorkflowDeploySiteContext context, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        Application application = _applicationService.getApplication( context.getIdApplication( ), plugin );
        IVCSService vcsService = DeploymentUtils.getVCSService( application.getRepoType( ) );
        context.getCommandResult( ).getLog( ).append( "Starting Action Checkout  Site...\n" );

        vcsService.checkAuthentication( context.getSvnBaseSiteUrl( ), context.getVcsUser( ) );

        vcsService.doCheckoutSite( application.getArtifactId( ), application.getUrlRepo( ), context.getVcsUser( ), context.getCommandResult( ), null,
                context.getTagToDeploy( ) );

        // throw RuntimeException for stopping Workflow

        stopWorkflowIfTechnicalError( "Error Chekout Site", context.getCommandResult( ) );

        context.getCommandResult( ).getLog( ).append( "End Action Checkout   Site...\n" );

        return null;
    }

    public void initTagInformations( WorkflowDeploySiteContext context )
    {
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        Application application = _applicationService.getApplication( context.getIdApplication( ), plugin );

        String strVersion = null;
        String strNextVersion = null;
        String strTagName = null;

        // release version and next version
        try
        {
            strVersion = ReleaseUtils.getReleaseVersion( DeploymentUtils.getPathCheckoutSite( application.getUrlRepo( ) ) );
            strNextVersion = ReleaseUtils.getNextVersion( strVersion );
            strTagName = ReleaseUtils.getReleaseName( application.getName( ), strVersion );
        }
        catch( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            AppLogService.error( e );
        }
        catch( JAXBException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace( );
        }

        context.setTagName( strTagName );
        context.setTagVersion( strVersion );
        context.setNextVersion( strNextVersion );
    }

    public String assemblySite( WorkflowDeploySiteContext context, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        Application application = _applicationService.getApplication( context.getIdApplication( ), plugin );

        ServerApplicationInstance serverApplicationInstance = _serverApplicationService.getServerApplicationInstance( application,
                context.getCodeServerInstance( ConstanteUtils.CONSTANTE_SERVER_TOMCAT ), context.getCodeEnvironement( ),
                ConstanteUtils.CONSTANTE_SERVER_TOMCAT, locale, false, false );
        context.getCommandResult( ).getLog( ).append( "Starting Action Assembly  Site...\n" );
        MavenService.getService( ).mvnSiteAssembly( application.getArtifactId( ),
                serverApplicationInstance.getMavenProfile( application.getIdApplication( ) ), context.getVcsUser( ), context.getCommandResult( ) );
        // throw RuntimeException for stopping Workflow
        stopWorkflowIfTechnicalError( "Error During assembly Site", context.getCommandResult( ) );
        context.getCommandResult( ).getLog( ).append( "End Action Assembly  Site...\n" );

        return null;
    }

    public String deploySite( WorkflowDeploySiteContext context, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        Application application = _applicationService.getApplication( context.getIdApplication( ), plugin );
        ServerApplicationInstance serverApplicationInstance = _serverApplicationService.getServerApplicationInstance( application,
                context.getCodeServerInstance( ConstanteUtils.CONSTANTE_SERVER_TOMCAT ), context.getCodeEnvironement( ),
                ConstanteUtils.CONSTANTE_SERVER_TOMCAT, locale, false, false );
        context.getCommandResult( ).getLog( ).append( "Starting Action Deploy  Site...\n" );

        String strWarGeneratedName = MavenService.getService( ).getSiteWarName( application.getArtifactId( ) );

        _ftpService.uploadFile( application.getWebAppName( ) + ConstanteUtils.ARCHIVE_WAR_EXTENSION, DeploymentUtils.getPathArchiveGenerated(
                DeploymentUtils.getPathCheckoutSite( application.getArtifactId( ) ), strWarGeneratedName, ConstanteUtils.ARCHIVE_WAR_EXTENSION ),
                serverApplicationInstance.getFtpInfo( ), DeploymentUtils.getDeployDirectoryTarget( application.getCode( ), serverApplicationInstance ), context
                        .getCommandResult( ), true );
        // throw RuntimeException for stopping Workflow
        stopWorkflowIfTechnicalError( "Error During Deploy Site", context.getCommandResult( ) );

        context.getCommandResult( ).getLog( ).append( "End Action Deploy  Site...\n" );

        return null;
    }

    public String deployScript( WorkflowDeploySiteContext context, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        Application application = _applicationService.getApplication( context.getIdApplication( ), plugin );

        String strServerType = !StringUtils.isEmpty( context.getCodeServerInstance( ConstanteUtils.CONSTANTE_SERVER_MYSQL ) ) ? ConstanteUtils.CONSTANTE_SERVER_MYSQL
                : ConstanteUtils.CONSTANTE_SERVER_PSQ;
        ServerApplicationInstance serverApplicationInstance = _serverApplicationService.getServerApplicationInstance( application,
                context.getCodeServerInstance( strServerType ), context.getCodeEnvironement( ), strServerType, locale, false, false );

        context.getCommandResult( ).getLog( ).append( "Starting Action Deploy  Script...\n" );

        _ftpService.uploadFile( context.getScriptFileItemName( ), context.getScriptFileItem( ), serverApplicationInstance.getFtpInfo( ),
                DeploymentUtils.getDeployDirectoryTarget( application.getCode( ), serverApplicationInstance ), context.getCommandResult( ), false );
        // throw RuntimeException for stopping Workflow
        stopWorkflowIfTechnicalError( "Error During Deploy Script", context.getCommandResult( ) );

        context.getCommandResult( ).getLog( ).append( "End Action Deploy  Script...\n" );

        return null;
    }

    public String executeServerAction( String strActionKey, HttpServletRequest request, WorkflowDeploySiteContext context, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        Application application = _applicationService.getApplication( context.getIdApplication( ), plugin );
        IAction action = _actionService.getAction( strActionKey, locale );
        // test if the server instance is in the deployment context
        if ( context.getCodeServerInstance( action.getServerType( ) ) != null )
        {

            ServerApplicationInstance serverApplicationInstance = _serverApplicationService.getServerApplicationInstance( application,
                    context.getCodeServerInstance( action.getServerType( ) ), context.getCodeEnvironement( ), action.getServerType( ), locale, false, false );
            boolean bResult;
            if ( action != null
                    && _actionService.canExecuteAction( application, action, serverApplicationInstance, context.getCommandResult( ),
                            DeploymentUtils.getActionParameters( context ) ) )
            {
                context.getCommandResult( ).getLog( ).append( "Starting Action " + action.getName( ) + " \n" );
                bResult = _actionService.executeAction( application, action, serverApplicationInstance, context.getCommandResult( ),
                        DeploymentUtils.getActionParameters( context ) );
                if ( !bResult && action.isStopWorkflowIfExecutionError( ) )
                {
                    context.getCommandResult( ).setErrorType( CommandResult.ERROR_TYPE_STOP );
                    // throw RuntimeException for stopping Workflow
                    throw new RuntimeException( "Error During Server Action Execution" );

                }
                context.getCommandResult( ).getLog( ).append( "End Action " + action.getName( ) + " \n" );

            }
        }

        return null;
    }

    private void stopWorkflowIfTechnicalError( String strProcess, CommandResult commandResult ) throws RuntimeException
    {

        if ( commandResult != null && commandResult.getStatus( ) == CommandResult.STATUS_ERROR
                && commandResult.getErrorType( ) == CommandResult.ERROR_TYPE_STOP )
        {
            throw new RuntimeException( strProcess );
        }
    }
    
    /**
     * Run a custom maven command
     * @param context
     *              The workflow deploy site context
     * @param locale
     *              The locale
     * @return the logs of the command
     */
    @Override
    public String runCustomMavenCommand( WorkflowDeploySiteContext context, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        Application application = _applicationService.getApplication( context.getIdApplication( ), plugin );

        ServerApplicationInstance serverApplicationInstance = _serverApplicationService.getServerApplicationInstance( application,
                context.getCodeServerInstance( ConstanteUtils.CONSTANTE_SERVER_TOMCAT ), context.getCodeEnvironement( ),
                ConstanteUtils.CONSTANTE_SERVER_TOMCAT, locale, false, false );
        
        context.getCommandResult( ).getLog( ).append( "Starting Action Run Custom Maven Cmd " );
        context.getCommandResult( ).getLog( ).append( context.getCustomMavenCommand( ) );
        context.getCommandResult( ).getLog( ).append( "\n" );
        MavenService.getService( ).runCustomMavenCommand( application.getArtifactId( ), serverApplicationInstance.getMavenProfile( application.getIdApplication( ) ),
                context.getCustomMavenCommand(), context.getCommandResult( ) );
        
        // throw RuntimeException for stopping Workflow
        stopWorkflowIfTechnicalError( "Error During the run of the custom maven cmd " + context.getCustomMavenCommand(), context.getCommandResult( ) );
        context.getCommandResult( ).getLog( ).append( "End Action Run Custom Maven Cmd \n" );

        return null;
    }
    
}
