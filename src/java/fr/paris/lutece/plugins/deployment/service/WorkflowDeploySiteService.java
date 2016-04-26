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

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import fr.paris.lutece.plugins.deployment.business.Application;
import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.IAction;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;
import fr.paris.lutece.plugins.deployment.business.WorkflowDeploySiteContext;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.plugins.deployment.util.ReleaseUtils;
import fr.paris.lutece.plugins.deployment.util.SVNUtils;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.html.HtmlTemplate;


public class WorkflowDeploySiteService implements IWorkflowDeploySiteService
{
    HashMap<Integer, WorkflowDeploySiteContext> _mapWorkflowDeploySiteContext = new HashMap<Integer, WorkflowDeploySiteContext>(  );
    @Inject
    ISvnService _svnService;
    @Inject
    IMavenService _mavenService;
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
        int nIdKey = Integer.parseInt( DatastoreService.getDataValue( 
                    ConstanteUtils.CONSTANTE_MAX_DEPLOY_SITE_CONTEXT_KEY, "0" ) ) + 1;
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
        String strSvnCheckoutSiteUrl;
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        Application application = _applicationService.getApplication( context.getIdApplication(  ), plugin );

        if ( context.getTagToDeploy(  ) != null )
        {
            strSvnCheckoutSiteUrl = SVNUtils.getSvnUrlTagSite( application.getSvnUrlSite(  ), context.getTagToDeploy(  ) );
        }
        else
        {
            strSvnCheckoutSiteUrl = SVNUtils.getSvnUrlTrunkSite( application.getSvnUrlSite(  ) );
        }

        context.getCommandResult(  ).getLog(  ).append( "Starting Action Checkout  Site...\n" );
        _svnService.doSvnCheckoutSite( application.getSiteName(  ), strSvnCheckoutSiteUrl, context.getMavenUser(  ),
            context.getCommandResult(  ) );
        //throw RuntimeException for stopping Workflow 
    
        stopWorkflowIfTechnicalError("Error Chekout Site", context.getCommandResult());
        
        context.getCommandResult(  ).getLog(  ).append( "End Action Checkout   Site...\n" );

        return null;
    }

    public void initTagInformations( WorkflowDeploySiteContext context )
    {
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        Application application = _applicationService.getApplication( context.getIdApplication(  ), plugin );

        String strVersion = null;
        String strNextVersion = null;
        String strTagName = null;

        // release version and next version
        try
        {
            strVersion = ReleaseUtils.getReleaseVersion( DeploymentUtils.getPathCheckoutSite( 
                        application.getSiteName(  ) ) );
            strNextVersion = ReleaseUtils.getNextVersion( strVersion );
            strTagName = ReleaseUtils.getReleaseName( application.getSiteName(  ), strVersion );
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            AppLogService.error( e );
        }
        catch ( JAXBException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace(  );
        }

        context.setTagName( strTagName );
        context.setTagVersion( strVersion );
        context.setNextVersion( strNextVersion );
    }

    public String tagSite( WorkflowDeploySiteContext context, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        Application application = _applicationService.getApplication( context.getIdApplication(  ), plugin );
        context.getCommandResult(  ).getLog(  ).append( "Starting Action Tag  Site...\n" );
        _svnService.doSvnTagSite( application.getSiteName(  ), application.getSvnUrlSite(  ), context.getTagName(  ),
            context.getNextVersion(  ), context.getTagVersion(  ), context.getMavenUser(  ),
            context.getCommandResult(  ) );
        //throw RuntimeException for stopping Workflow 
        stopWorkflowIfTechnicalError("Error During Tag Site", context.getCommandResult());
        
        context.getCommandResult(  ).getLog(  ).append( "End Action Tag  Site...\n" );
        // update context status
        context.setTagToDeploy( context.getTagName(  ) );

        return null;
    }

    public String assemblySite( WorkflowDeploySiteContext context, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        Application application = _applicationService.getApplication( context.getIdApplication(  ), plugin );

        ServerApplicationInstance serverApplicationInstance = _serverApplicationService.getServerApplicationInstance( application,
                context.getCodeServerInstance( ConstanteUtils.CONSTANTE_SERVER_TOMCAT ),
                context.getCodeEnvironement(  ), ConstanteUtils.CONSTANTE_SERVER_TOMCAT, locale, false, false );
        context.getCommandResult(  ).getLog(  ).append( "Starting Action Assembly  Site...\n" );
        _mavenService.mvnSiteAssembly( application.getSiteName(  ), context.getTagName(  ),
            serverApplicationInstance.getMavenProfile(  ), context.getMavenUser(  ), context.getCommandResult(  ) );
        //throw RuntimeException for stopping Workflow 
        stopWorkflowIfTechnicalError("Error During assembly Site", context.getCommandResult());
        context.getCommandResult(  ).getLog(  ).append( "End Action Assembly  Site...\n" );

        return null;
    }

    public String deploySite( WorkflowDeploySiteContext context, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        Application application = _applicationService.getApplication( context.getIdApplication(  ), plugin );
        ServerApplicationInstance serverApplicationInstance = _serverApplicationService.getServerApplicationInstance( application,
                context.getCodeServerInstance( ConstanteUtils.CONSTANTE_SERVER_TOMCAT ),
                context.getCodeEnvironement(  ), ConstanteUtils.CONSTANTE_SERVER_TOMCAT, locale, false, false );
        context.getCommandResult(  ).getLog(  ).append( "Starting Action Deploy  Site...\n" );
       
        String strWarGeneratedName=_mavenService.getSiteWarName(application.getSiteName());
       
        
        _ftpService.uploadFile( application.getWebAppName(  ) + ConstanteUtils.ARCHIVE_WAR_EXTENSION,
            DeploymentUtils.getPathArchiveGenerated( DeploymentUtils.getPathCheckoutSite( application.getSiteName(  ) ),
            		strWarGeneratedName , ConstanteUtils.ARCHIVE_WAR_EXTENSION ),
            serverApplicationInstance.getFtpInfo(  ),
            DeploymentUtils.getDeployDirectoryTarget( application.getCode(  ), serverApplicationInstance ),
            context.getCommandResult(  ) ,true);
        //throw RuntimeException for stopping Workflow 
        stopWorkflowIfTechnicalError("Error During Deploy Script", context.getCommandResult());
        
        context.getCommandResult(  ).getLog(  ).append( "End Action Deploy  Site...\n" );

        return null;
    }
    
    public String initAppContext( WorkflowDeploySiteContext context, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        
       
        	Application application = _applicationService.getApplication( context.getIdApplication(  ), plugin );
        	 ServerApplicationInstance serverApplicationInstance = _serverApplicationService.getServerApplicationInstance( application,
                     context.getCodeServerInstance( ConstanteUtils.CONSTANTE_SERVER_TOMCAT ),
                     context.getCodeEnvironement(  ), ConstanteUtils.CONSTANTE_SERVER_TOMCAT, locale, false, false );
              
            
        	HashMap model = new HashMap(  );
	        model.put(ConstanteUtils.MARK_APPLICATION,application);
	        
	        HtmlTemplate templateInitAppContext = AppTemplateService.getTemplate( ConstanteUtils.TEMPLATE_INIT_APP_CONTEXT,
	              locale ,model );
	        
	        context.getCommandResult(  ).getLog(  ).append( "Starting Action  Init App Context...\n" );
	        
	        InputStream  iTemplateInitAppContext=new ByteArrayInputStream(templateInitAppContext.getHtml().getBytes());
	         
        	_ftpService.uploadFile( application.getWebAppName()+".xml",iTemplateInitAppContext,serverApplicationInstance.getFtpInfo(  ),
				    DeploymentUtils.getContextDirectoryTarget( application.getCode(  ), serverApplicationInstance ),
				    context.getCommandResult(  ),false );
	        //throw RuntimeException for stopping Workflow 
	      	stopWorkflowIfTechnicalError("Error During Init App Context", context.getCommandResult());
	        
			
	        context.getCommandResult(  ).getLog(  ).append( "End Action Init App Context...\n");
        

        return null;
    }

    
    public String deployScript( WorkflowDeploySiteContext context, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        Application application = _applicationService.getApplication( context.getIdApplication(  ), plugin );
        ServerApplicationInstance serverApplicationInstance = _serverApplicationService.getServerApplicationInstance( application,
                context.getCodeServerInstance( ConstanteUtils.CONSTANTE_SERVER_MYSQL ),
                context.getCodeEnvironement(  ), ConstanteUtils.CONSTANTE_SERVER_MYSQL, locale, false, false );
       
        context.getCommandResult(  ).getLog(  ).append( "Starting Action Deploy  Script...\n" );
        
      	_ftpService.uploadFile( context.getScriptFileItemName(),context.getScriptFileItem(),serverApplicationInstance.getFtpInfo(  ),
			    DeploymentUtils.getDeployDirectoryTarget( application.getCode(  ), serverApplicationInstance ),
			    context.getCommandResult(  ),false );
        //throw RuntimeException for stopping Workflow 
      	stopWorkflowIfTechnicalError("Error During Deploy Script", context.getCommandResult());
        
		
        context.getCommandResult(  ).getLog(  ).append( "End Action Deploy  Script...\n" );

        return null;
    }

    public String executeServerAction( String strActionKey, HttpServletRequest request,
        WorkflowDeploySiteContext context, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        Application application = _applicationService.getApplication( context.getIdApplication(  ), plugin );
        IAction action = _actionService.getAction( strActionKey, locale );
        ServerApplicationInstance serverApplicationInstance = _serverApplicationService.getServerApplicationInstance( application,
                context.getCodeServerInstance( action.getServerType(  ) ), context.getCodeEnvironement(  ),
                action.getServerType(  ), locale, false, false );
        boolean bResult;
        if ( action != null )
        {
            context.getCommandResult(  ).getLog(  ).append( "Starting Action " + action.getName(  ) + " \n" );
            bResult= _actionService.executeAction( application, action, serverApplicationInstance,
                context.getCommandResult(  ), DeploymentUtils.getActionParameters(context)) ;
            if(!bResult && action.isStopWorkflowIfExecutionError() )
            {	 context.getCommandResult(  ).setErrorType(CommandResult.ERROR_TYPE_STOP);
            	//throw RuntimeException for stopping Workflow 
            	   throw new RuntimeException("Error During Server Action Execution");
            	
            }
            context.getCommandResult(  ).getLog(  ).append( "End Action " + action.getName(  ) + " \n" );
            
        }

        return null;
    }
    
    private void stopWorkflowIfTechnicalError(String strProcess,CommandResult commandResult)throws RuntimeException
    {
    	
    	 if( commandResult!=null && commandResult.getStatus()==CommandResult.STATUS_ERROR && commandResult.getErrorType()==CommandResult.ERROR_TYPE_STOP)
         {
    		 throw new RuntimeException(strProcess);
         }
    }
    
}
