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
package fr.paris.lutece.plugins.deployment.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import fr.paris.lutece.plugins.deployment.business.Application;
import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.business.FilterDeployment;
import fr.paris.lutece.plugins.deployment.business.IAction;
import fr.paris.lutece.plugins.deployment.business.InvalidRepositoryUrlException;
import fr.paris.lutece.plugins.deployment.business.ManageApplicationAction;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;
import fr.paris.lutece.plugins.deployment.business.vcs.SvnUser;
import fr.paris.lutece.plugins.deployment.business.WorkflowDeploySiteContext;
import fr.paris.lutece.plugins.deployment.business.vcs.AbstractVCSUser;
import fr.paris.lutece.plugins.deployment.service.ApplicationResourceIdService;
import fr.paris.lutece.plugins.deployment.service.ApplicationService;
import fr.paris.lutece.plugins.deployment.service.DeploymentPlugin;
import fr.paris.lutece.plugins.deployment.service.EnvironmentResourceIdService;
import fr.paris.lutece.plugins.deployment.service.IActionService;
import fr.paris.lutece.plugins.deployment.service.IApplicationService;
import fr.paris.lutece.plugins.deployment.service.IDatabaseService;
import fr.paris.lutece.plugins.deployment.service.IEnvironmentService;
import fr.paris.lutece.plugins.deployment.service.IFtpService;
import fr.paris.lutece.plugins.deployment.service.IServerApplicationService;
import fr.paris.lutece.plugins.deployment.service.IWorkflowDeploySiteService;
import fr.paris.lutece.plugins.deployment.service.MavenService;
import fr.paris.lutece.plugins.deployment.service.vcs.IVCSService;
import fr.paris.lutece.plugins.deployment.uploadhandler.DeploymentUploadHandler;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.plugins.deployment.util.RepositoryUtils;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;
import javax.servlet.http.HttpSession;

public class DeploymentJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_DEPLOYMENT_MANAGEMENT = "DEPLOYMENT_MANAGEMENT";
    private static final String MESSAGE_ACCESS_DENIED = "deployment.message.error.accessDenied";
    private IApplicationService _applicationService = SpringContextService.getBean( "deployment.ApplicationService" );
    private IEnvironmentService _environmentService = SpringContextService.getBean( "deployment.EnvironmentService" );
    private IServerApplicationService _serverApplicationService = SpringContextService.getBean( "deployment.ServerApplicationService" );

    private IDatabaseService _databaseService = SpringContextService.getBean( "deployment.DatabaseService" );
    private IWorkflowDeploySiteService _workflowDeploySiteService = SpringContextService.getBean( "deployment.WorkflowDeploySiteService" );

    private IFtpService _ftpService = SpringContextService.getBean( "deployment.FtpService" );

    private IActionService _actionService = SpringContextService.getBean( "deployment.ActionService" );

    private String _strCurrentPageIndex;
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( ConstanteUtils.PROPERTY_NB_ITEM_PER_PAGE, 50 );
    private Integer _nIdCurrentWorkflowDeploySiteContext;
    private DeploymentUploadHandler _handler = SpringContextService.getBean( DeploymentUploadHandler.BEAN_NAME );
    private FilterDeployment _filterDeployment;

    /**
     * @param request
     *            the HTTP request
     * @param strRight
     *            The right
     * @throws fr.paris.lutece.portal.service.admin.AccessDeniedException
     *             Access denied exception
     */
    @Override
    public void init( HttpServletRequest request, String strRight ) throws AccessDeniedException
    {
        super.init( request, strRight );
        if ( _filterDeployment == null )
        {
            _filterDeployment = new FilterDeployment( );
        }
    }

    public String getManageApplication( HttpServletRequest request )
    {
        String strWorkgroup = request.getParameter( ConstanteUtils.PARAM_WORKGROUP );
        String strSearchName = request.getParameter( ConstanteUtils.PARAM_SEARCH_NAME );

        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefaultItemsPerPage );
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        // ReferenceList

        ReferenceList refListWorkGroups = AdminWorkgroupService.getUserWorkgroups( getUser( ), getLocale( ) );

        // build Filter stored in session
        if ( strWorkgroup != null )
        {
            _filterDeployment.setWorkGroup( strWorkgroup );
        }
        if ( strSearchName != null )
        {
            _filterDeployment.setSearchName( strSearchName );
        }

        // filter by app type and app workgroup
        List<Application> listApplications = _applicationService.getListApplications( _filterDeployment, getPlugin( ) );

        List<ManageApplicationAction> listManageActions = SpringContextService.getBeansOfType( ManageApplicationAction.class );

        // filter by search name with java.match
        listApplications = _applicationService.getListApplicationFilteredBySearchName( listApplications, _filterDeployment, getPlugin( ) );

        // filter by workgroup
        listApplications = (List<Application>) AdminWorkgroupService.getAuthorizedCollection( listApplications, getUser( ) );

        HashMap<String, Collection<ManageApplicationAction>> hashManageActions = new HashMap<String, Collection<ManageApplicationAction>>( );

        for ( Application application : listApplications )
        {
            hashManageActions.put( Integer.toString( application.getIdApplication( ) ),
                    RBACService.getAuthorizedActionsCollection( listManageActions, application, getUser( ) ) );
        }

        HashMap model = new HashMap( );
        Paginator paginator = new Paginator( listApplications, _nItemsPerPage, getJspManageApplication( request ), Paginator.PARAMETER_PAGE_INDEX,
                _strCurrentPageIndex );

        model.put( ConstanteUtils.MARK_APPLICATION_LIST, paginator.getPageItems( ) );
        model.put( ConstanteUtils.MARK_PAGINATOR, paginator );
        model.put( ConstanteUtils.MARK_NB_ITEMS_PER_PAGE, paginator.getItemsPerPage( ) );
        model.put( ConstanteUtils.MARK_USER_WORKGROUP_REF_LIST, refListWorkGroups );
        model.put( ConstanteUtils.MARK_USER_WORKGROUP_SELECTED, ( _filterDeployment.getWorkgroup( ) != null ) ? _filterDeployment.getWorkgroup( )
                : ConstanteUtils.CONSTANTE_ALL );
        model.put( ConstanteUtils.MARK_MANAGE_APPLICATION_ACTIONS, hashManageActions );
        model.put( ConstanteUtils.MARK_SEARCH_NAME, ( _filterDeployment.getSearchName( ) != null ) ? _filterDeployment.getSearchName( ) : StringUtils.EMPTY );
        setPageTitleProperty( ConstanteUtils.PROPERTY_MANAGE_APPLICATION_PAGE_TITLE );

        model.put( ConstanteUtils.MARK_CAN_CREATE_APPLICATION,
                RBACService.isAuthorized( Application.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, ApplicationResourceIdService.PERMISSION_CREATE, getUser( ) ) );
        HtmlTemplate templateList = AppTemplateService.getTemplate( ConstanteUtils.TEMPLATE_MANAGE_APPLICATION, getLocale( ), model );

        return getAdminPage( templateList.getHtml( ) );
    }

    public String getCreateApplication( HttpServletRequest request ) throws AccessDeniedException
    {

        if ( !RBACService.isAuthorized( Application.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, ApplicationResourceIdService.PERMISSION_CREATE, getUser( ) ) )
        {
            throw new AccessDeniedException( I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) ) );
        }

        ReferenceList refListWorkGroups = AdminWorkgroupService.getUserWorkgroups( getUser( ), getLocale( ) );

        HashMap model = new HashMap( );

        model.put( ConstanteUtils.MARK_USER_WORKGROUP_REF_LIST, refListWorkGroups );

        // build Filter
        setPageTitleProperty( ConstanteUtils.PROPERTY_CREATE_APPLICATION_PAGE_TITLE );

        HtmlTemplate templateList = AppTemplateService.getTemplate( ConstanteUtils.TEMPLATE_CREATE_APPLICATION, getLocale( ), model );

        return getAdminPage( templateList.getHtml( ) );
    }

    public String getModifyApplication( HttpServletRequest request ) throws AccessDeniedException
    {

        ReferenceList refListWorkGroups = AdminWorkgroupService.getUserWorkgroups( getUser( ), getLocale( ) );

        String strIdApplication = request.getParameter( ConstanteUtils.PARAM_ID_APPLICATION );

        int nIdApplication = DeploymentUtils.getIntegerParameter( strIdApplication );

        Application application = _applicationService.getApplication( nIdApplication, getPlugin( ) );

        if ( !isAuthorized( application, ApplicationResourceIdService.PERMISSION_MODIFY ) )
        {
            throw new AccessDeniedException( I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) ) );
        }

        // build Filter
        HashMap model = new HashMap( );

        model.put( ConstanteUtils.MARK_USER_WORKGROUP_REF_LIST, refListWorkGroups );

        model.put( ConstanteUtils.MARK_APPLICATION, application );

        setPageTitleProperty( ConstanteUtils.PROPERTY_MODIFY_APPLICATION_PAGE_TITLE );

        HtmlTemplate templateList = AppTemplateService.getTemplate( ConstanteUtils.TEMPLATE_MODIFY_APPLICATION, getLocale( ), model );

        return getAdminPage( templateList.getHtml( ) );
    }

    public String getViewApplication( HttpServletRequest request ) throws AccessDeniedException
    {
        AdminUser adminUser = getUser( );
        SvnUser mavenUser = DeploymentUtils.getSvnUser( adminUser.getUserId( ), getLocale( ) );
        String strIdApplication = request.getParameter( ConstanteUtils.PARAM_ID_APPLICATION );

        int nIdApplication = DeploymentUtils.getIntegerParameter( strIdApplication );

        setPageTitleProperty( ConstanteUtils.PROPERTY_MANAGE_APPLICATION_PAGE_TITLE );

        HashMap model = new HashMap( );

        if ( nIdApplication == ConstanteUtils.CONSTANTE_ID_NULL )
        {
            return getJspManageApplication( request );
        }

        if ( mavenUser != null )
        {
            Application application = _applicationService.getApplication( nIdApplication, getPlugin( ) );

            if ( !isAuthorized( application, ApplicationResourceIdService.PERMISSION_VIEW ) )
            {
                throw new AccessDeniedException( I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) ) );
            }

            List<Environment> listEnvironments = _environmentService.getListEnvironments( application.getCode( ), getLocale( ) );

            Collection<Environment> colEnvironmentsFilter = RBACService.getAuthorizedCollection( listEnvironments,
                    EnvironmentResourceIdService.PERMISSION_DEPLOY_ON_ENVIROMENT, getUser( ) );

            HashMap<String, List<ServerApplicationInstance>> hashServerApplicationInstanceTomcat = _serverApplicationService.getHashServerApplicationInstance(
                    application, ConstanteUtils.CONSTANTE_SERVER_TOMCAT, getLocale( ), true, true );

            HashMap<String, List<ServerApplicationInstance>> hashServerApplicationInstanceSql = _serverApplicationService.getHashServerApplicationInstance(
                    application, ConstanteUtils.CONSTANTE_SERVER_MYSQL, getLocale( ), true, true );

            HashMap<String, List<ServerApplicationInstance>> hashServerApplicationInstancePsq = _serverApplicationService.getHashServerApplicationInstance(
                    application, ConstanteUtils.CONSTANTE_SERVER_PSQ, getLocale( ), true, true );

            for ( Map.Entry<String, List<ServerApplicationInstance>> entry : hashServerApplicationInstancePsq.entrySet( ) )
            {
                String key = entry.getKey( );
                List<ServerApplicationInstance> listServerPsq = entry.getValue( );

                if ( hashServerApplicationInstanceSql.containsKey( entry.getKey( ) ) && hashServerApplicationInstanceSql.get( entry.getKey( ) ) != null
                        && listServerPsq != null )
                {
                    hashServerApplicationInstanceSql.get( entry.getKey( ) ).addAll( listServerPsq );
                }
                else
                {
                    hashServerApplicationInstanceSql.put( key, listServerPsq );

                }
            }

            ReferenceList refListEnvironements = ReferenceList.convert( colEnvironmentsFilter, "code", "name", false );
            model.put( ConstanteUtils.MARK_ENVIRONMENT_LIST, refListEnvironements );
            model.put( ConstanteUtils.MARK_SERVER_INSTANCE_MAP_TOMCAT, hashServerApplicationInstanceTomcat );
            model.put( ConstanteUtils.MARK_SERVER_INSTANCE_MAP_SQL, hashServerApplicationInstanceSql );

            model.put( ConstanteUtils.MARK_APPLICATION, application );
        }
        else
        {
            String strErrorMessage = I18nService.getLocalizedString( ConstanteUtils.I18n_MESSAGE_ERROR_MAVEN_USER_IS_NOT_SET, getLocale( ) );
            model.put( ConstanteUtils.MARK_ERROR_MESSAGE, strErrorMessage );
            model.put( ConstanteUtils.MARK_ID_USER, getUser( ).getUserId( ) );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( ConstanteUtils.TEMPLATE_VIEW_APPLICATION, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    public String doModifyApplication( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdApplication = request.getParameter( ConstanteUtils.PARAM_ID_APPLICATION );
        int nIdApplication = DeploymentUtils.getIntegerParameter( strIdApplication );

        if ( ( request.getParameter( ConstanteUtils.PARAM_CANCEL ) == null ) && ( nIdApplication != ConstanteUtils.CONSTANTE_ID_NULL ) )
        {
            Application application = _applicationService.getApplication( nIdApplication, getPlugin( ) );
            if ( !isAuthorized( application, ApplicationResourceIdService.PERMISSION_MODIFY ) )
            {
                throw new AccessDeniedException( I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) ) );
            }

            String strError = getApplicationData( request, application );

            if ( strError != null )
            {
                return strError;
            }
            // test if the user is authorized to select the workgroup
            if ( !StringUtils.isEmpty( application.getWorkgroup( ) ) && !AdminWorkgroupService.isAuthorized( application, getUser( ) ) )
            {
                return getJspManageApplication( request );

            }
            _applicationService.updateApplication( application, getPlugin( ) );
        }

        return getJspManageApplication( request );
    }

    public String doCreateApplication( HttpServletRequest request ) throws AccessDeniedException
    {
        Application application = new Application( );
        String strError = getApplicationData( request, application );

        if ( !RBACService.isAuthorized( Application.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, ApplicationResourceIdService.PERMISSION_CREATE, getUser( ) ) )
        {
            throw new AccessDeniedException( I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) ) );
        }

        if ( strError != null )
        {
            return strError;
        }

        // test if the user is authorized to select the workgroup
        if ( !StringUtils.isEmpty( application.getWorkgroup( ) ) && !AdminWorkgroupService.isAuthorized( application, getUser( ) ) )
        {
            return getJspManageApplication( request );

        }

        _applicationService.createApplication( application, getPlugin( ) );

        return getJspManageApplication( request );
    }

    /**
     * Gets the confirmation page of delete digg submit
     *
     * @param request
     *            The HTTP request
     * @return the confirmation page of delete digg submit
     */
    public String getConfirmRemoveApplication( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdApplication = request.getParameter( ConstanteUtils.PARAM_ID_APPLICATION );
        int nIdApplication = DeploymentUtils.getIntegerParameter( strIdApplication );

        if ( nIdApplication == ConstanteUtils.CONSTANTE_ID_NULL )
        {
            return getJspManageApplication( request );
        }

        Application application = _applicationService.getApplication( nIdApplication, getPlugin( ) );

        if ( !isAuthorized( application, ApplicationResourceIdService.PERMISSION_DELETE ) )
        {
            throw new AccessDeniedException( I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) ) );
        }
        String strMessage = ConstanteUtils.PROPERTY_MESSAGE_CONFIRM_REMOVE_APPLICATION;
        UrlItem url = new UrlItem( ConstanteUtils.JSP_REMOVE_APPLICATION );
        url.addParameter( ConstanteUtils.PARAM_ID_APPLICATION, nIdApplication );

        return AdminMessageService.getMessageUrl( request, strMessage, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Gets the confirmation page of delete digg submit
     *
     * @param request
     *            The HTTP request
     * @return the confirmation page of delete digg submit
     */
    public String DoRemoveApplication( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdApplication = request.getParameter( ConstanteUtils.PARAM_ID_APPLICATION );
        int nIdApplication = DeploymentUtils.getIntegerParameter( strIdApplication );

        Application application = _applicationService.getApplication( nIdApplication, getPlugin( ) );

        if ( !isAuthorized( application, ApplicationResourceIdService.PERMISSION_DELETE ) )
        {
            throw new AccessDeniedException( I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) ) );
        }

        _applicationService.deleteApplication( nIdApplication, getPlugin( ) );

        return getJspManageApplication( request );
    }

    public String getFormDeployApplication( HttpServletRequest request ) throws AccessDeniedException
    {
        String strDeployWar = request.getParameter( ConstanteUtils.PARAM_DEPLOY_WAR );
        String strDeploySql = request.getParameter( ConstanteUtils.PARAM_DEPLOY_SQL );
        String strInitDatabase = request.getParameter( ConstanteUtils.PARAM_INIT_DATABASE );
        String strInitContext = request.getParameter( ConstanteUtils.PARAM_INIT_APP_CONTEXT );

        Boolean bDeployWar = !StringUtils.isEmpty( strDeployWar );
        Boolean bDeploySql = !StringUtils.isEmpty( strDeploySql );
        Boolean bInitDatabase = !StringUtils.isEmpty( strInitDatabase );
        Boolean bInitContext = !StringUtils.isEmpty( strInitContext );

        String strIdApplication = request.getParameter( ConstanteUtils.PARAM_ID_APPLICATION );
        int nIdApplication = DeploymentUtils.getIntegerParameter( strIdApplication );
        if ( nIdApplication == ConstanteUtils.CONSTANTE_ID_NULL )
        {
            return getJspManageApplication( request );
        }

        Application application = _applicationService.getApplication( nIdApplication, getPlugin( ) );

        AbstractVCSUser vcsUser = DeploymentUtils.getVCSUser( request, application );
        if ( bDeployWar )
        {

            IVCSService vcsService = DeploymentUtils.getVCSService( application.getRepoType( ) );
            try
            {
                vcsService.checkAuthentication( application.getUrlRepo( ), vcsUser );
                // Set the VCSUser in session for the application deployment process
                // For not asking credentials again
                HttpSession session = request.getSession( true );
                session.setAttribute( ConstanteUtils.CONSTANTE_VCS_USER, vcsUser );
            }
            catch( AppException e )
            {
                throw new AccessDeniedException( I18nService.getLocalizedString( ConstanteUtils.PROPERTY_MESSAGE_REPO_UNAUTHORIZED_ACCESS, getLocale( ) ) );
            }
        }

        if ( ( bDeployWar && !isAuthorized( application, ApplicationResourceIdService.PERMISSION_DEPLOY_APPLICATION ) )
                || ( bDeploySql && !isAuthorized( application, ApplicationResourceIdService.PERMISSION_DEPLOY_SCRIPT ) )
                || ( bInitDatabase && !isAuthorized( application, ApplicationResourceIdService.PERMISSION_INIT_DATABASE ) )
                || ( bInitContext && !isAuthorized( application, ApplicationResourceIdService.PERMISSION_INIT_APP_CONTEXT ) ) )
        {
            throw new AccessDeniedException( I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) ) );
        }

        setPageTitleProperty( ConstanteUtils.PROPERTY_MANAGE_APPLICATION_PAGE_TITLE );

        HashMap model = new HashMap( );

        if ( vcsUser != null )
        {

            List<Environment> listEnvironments = _environmentService.getListEnvironments( application.getCode( ), getLocale( ) );

            Collection<Environment> colEnvironmentsFilter = RBACService.getAuthorizedCollection( listEnvironments,
                    EnvironmentResourceIdService.PERMISSION_DEPLOY_ON_ENVIROMENT, getUser( ) );

            if ( bDeployWar || bInitContext )
            {
                HashMap<String, List<ServerApplicationInstance>> hashServerApplicationInstanceTomcat = _serverApplicationService
                        .getHashServerApplicationInstance( application, ConstanteUtils.CONSTANTE_SERVER_TOMCAT, getLocale( ), true, true );

                model.put( ConstanteUtils.MARK_SERVER_INSTANCE_MAP_TOMCAT, hashServerApplicationInstanceTomcat );
                ReferenceList refListTagSite = DeploymentUtils.getVCSService( application.getRepoType( ) ).getTagsSite( application.getUrlRepo( ), vcsUser,
                        application.getArtifactId( ) );
                model.put( ConstanteUtils.MARK_SITE_LIST, refListTagSite );
            }
            else
                if ( bDeploySql || bInitDatabase )
                {

                    ReferenceList refListUpgradeFilesList = !bInitDatabase ? DeploymentUtils.getVCSService( application.getRepoType( ) ).getUpgradesFiles(
                            application.getArtifactId( ), application.getUrlRepo( ), vcsUser ) : null;
                    if ( refListUpgradeFilesList != null )
                    {
                        refListUpgradeFilesList = DeploymentUtils.addEmptyRefenceItem( refListUpgradeFilesList );
                    }

                    HashMap<String, List<ServerApplicationInstance>> hashServerApplicationInstanceSql = _serverApplicationService
                            .getHashServerApplicationInstance( application, ConstanteUtils.CONSTANTE_SERVER_MYSQL, getLocale( ), true, true );

                    if ( !bInitDatabase )
                    {

                        HashMap<String, List<ServerApplicationInstance>> hashServerApplicationInstancePSQ = _serverApplicationService
                                .getHashServerApplicationInstance( application, ConstanteUtils.CONSTANTE_SERVER_PSQ, getLocale( ), true, true );

                        for ( Map.Entry<String, List<ServerApplicationInstance>> entry : hashServerApplicationInstancePSQ.entrySet( ) )
                        {
                            String key = entry.getKey( );
                            List<ServerApplicationInstance> listServerPsq = entry.getValue( );

                            if ( hashServerApplicationInstanceSql.containsKey( entry.getKey( ) )
                                    && hashServerApplicationInstanceSql.get( entry.getKey( ) ) != null && listServerPsq != null )
                            {
                                hashServerApplicationInstanceSql.get( entry.getKey( ) ).addAll( listServerPsq );
                            }
                            else
                            {
                                hashServerApplicationInstanceSql.put( key, listServerPsq );

                            }
                        }
                    }

                    HashMap<String, List<String>> hashDatabase = !bInitDatabase ? _databaseService.getHashDatabases( application.getCode( ),
                            hashServerApplicationInstanceSql, getLocale( ) ) : null;

                    model.put( ConstanteUtils.MARK_SERVER_INSTANCE_MAP_SQL, hashServerApplicationInstanceSql );
                    model.put( ConstanteUtils.MARK_DATABASE_MAP, hashDatabase );
                    model.put( ConstanteUtils.MARK_UPGRADE_FILE_REF_LIST, refListUpgradeFilesList );

                }

            HashMap<String, List<ServerApplicationInstance>> hashServerApplicationInstanceMysql = new HashMap<String, List<ServerApplicationInstance>>( );

            ReferenceList refListEnvironements = ReferenceList.convert( colEnvironmentsFilter, "code", "name", false );
            refListEnvironements = DeploymentUtils.addEmptyRefenceItem( refListEnvironements );

            model.put( ConstanteUtils.MARK_ENVIRONMENT_LIST, refListEnvironements );
            model.put( ConstanteUtils.MARK_APPLICATION, application );

            model.put( ConstanteUtils.MARK_DEPLOY_WAR, bDeployWar );
            model.put( ConstanteUtils.MARK_DEPLOY_SQL, bDeploySql );
            model.put( ConstanteUtils.MARK_INIT_DATABASE, bInitDatabase );
            model.put( ConstanteUtils.MARK_INIT_APP_CONTEXT, bInitContext );
            model.put( ConstanteUtils.MARK_INIT_APP_CONTEXT, bInitContext );
            model.put( ConstanteUtils.MARK_HANDLER, SpringContextService.getBean( DeploymentUploadHandler.BEAN_NAME ) );

        }
        else
        {
            String strErrorMessage = I18nService.getLocalizedString( ConstanteUtils.I18n_MESSAGE_ERROR_MAVEN_USER_IS_NOT_SET, getLocale( ) );
            model.put( ConstanteUtils.MARK_ERROR_MESSAGE, strErrorMessage );
            model.put( ConstanteUtils.MARK_ID_USER, getUser( ).getUserId( ) );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( ConstanteUtils.TEMPLATE_FORM_INIT_DEPLOY_APPLICATION, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    public String getProcessDeployApplication( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( _nIdCurrentWorkflowDeploySiteContext != null )
        {
            AdminUser adminUser = getUser( );

            WorkflowDeploySiteContext workflowDeploySiteContext = _workflowDeploySiteService
                    .getWorkflowDeploySiteContext( _nIdCurrentWorkflowDeploySiteContext );

            int nIdWorkflowSiteDeploy = DeploymentUtils.getIdWorkflowSiteDeploy( workflowDeploySiteContext );
            Application application = _applicationService.getApplication( workflowDeploySiteContext.getIdApplication( ), getPlugin( ) );
            Environment environment = _environmentService.getEnvironment( workflowDeploySiteContext.getCodeEnvironement( ), getLocale( ) );

            HashMap model = new HashMap( );

            if ( ( workflowDeploySiteContext.isDeployWar( ) && !isAuthorized( application, ApplicationResourceIdService.PERMISSION_DEPLOY_APPLICATION,
                    environment ) )
                    || ( workflowDeploySiteContext.isDeploySql( ) && !isAuthorized( application, ApplicationResourceIdService.PERMISSION_DEPLOY_SCRIPT,
                            environment ) )
                    || ( workflowDeploySiteContext.isInitBdd( ) && !isAuthorized( application, ApplicationResourceIdService.PERMISSION_INIT_DATABASE,
                            environment ) )
                    || ( workflowDeploySiteContext.isInitAppContext( ) && !isAuthorized( application, ApplicationResourceIdService.PERMISSION_INIT_APP_CONTEXT,
                            environment ) ) )
            {

                throw new AccessDeniedException( I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) ) );
            }

            if ( workflowDeploySiteContext.isDeployWar( ) || workflowDeploySiteContext.isInitAppContext( ) )
            {

                ServerApplicationInstance serverApplicationInstanceTomcat = _serverApplicationService.getServerApplicationInstance( application,
                        workflowDeploySiteContext.getCodeServerInstance( ConstanteUtils.CONSTANTE_SERVER_TOMCAT ),
                        workflowDeploySiteContext.getCodeEnvironement( ), ConstanteUtils.CONSTANTE_SERVER_TOMCAT, getLocale( ), false, false );
                model.put( ConstanteUtils.MARK_SERVER_INSTANCE, serverApplicationInstanceTomcat );

                model.put( ConstanteUtils.MARK_TAG_TO_DEPLOY, workflowDeploySiteContext.getTagToDeploy( ) );

            }
            else
                if ( workflowDeploySiteContext.isDeploySql( ) || workflowDeploySiteContext.isInitBdd( ) )
                {
                    String serverType = !StringUtils.isEmpty( workflowDeploySiteContext.getCodeServerInstance( ConstanteUtils.CONSTANTE_SERVER_MYSQL ) ) ? ConstanteUtils.CONSTANTE_SERVER_MYSQL
                            : ConstanteUtils.CONSTANTE_SERVER_PSQ;
                    ServerApplicationInstance serverApplicationInstanceSql = _serverApplicationService.getServerApplicationInstance( application,
                            workflowDeploySiteContext.getCodeServerInstance( serverType ), workflowDeploySiteContext.getCodeEnvironement( ), serverType,
                            getLocale( ), false, false );
                    model.put( ConstanteUtils.MARK_SERVER_INSTANCE, serverApplicationInstanceSql );
                    model.put( ConstanteUtils.MARK_SCRIPT_NAME, workflowDeploySiteContext.getScriptFileItemName( ) );
                }

            // workflow informations
            Collection<Action> listAction = WorkflowService.getInstance( ).getActions( workflowDeploySiteContext.getId( ),
                    WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE, nIdWorkflowSiteDeploy, adminUser );
            State state = WorkflowService.getInstance( ).getState( workflowDeploySiteContext.getId( ), WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE,
                    nIdWorkflowSiteDeploy, ConstanteUtils.CONSTANTE_ID_NULL );

            model.put( ConstanteUtils.MARK_APPLICATION, application );
            model.put( ConstanteUtils.MARK_ENVIRONMENT, environment );
            model.put( ConstanteUtils.MARK_STATE, state.getName( ) );
            model.put( ConstanteUtils.MARK_ACTION_LIST, listAction );
            model.put( ConstanteUtils.MARK_DEPLOY_WAR, workflowDeploySiteContext.isDeployWar( ) );
            model.put( ConstanteUtils.MARK_DEPLOY_SQL, workflowDeploySiteContext.isDeploySql( ) );
            model.put( ConstanteUtils.MARK_INIT_APP_CONTEXT, workflowDeploySiteContext.isInitAppContext( ) );
            model.put( ConstanteUtils.MARK_INIT_DATABASE, workflowDeploySiteContext.isInitBdd( ) );

            setPageTitleProperty( ConstanteUtils.PROPERTY_DEPLOY_SITE_PAGE_TITLE );

            HtmlTemplate template = AppTemplateService.getTemplate( ConstanteUtils.TEMPLATE_DEPLOY_APPLICATION_PROCESS, getLocale( ), model );

            return getAdminPage( template.getHtml( ) );
        }

        return getManageApplication( request );
    }

    /**
     * Do process the workflow actions
     *
     * @param request
     *            the HTTP request
     * @return the JSP return
     */
    public String doProcessActionJSON( HttpServletRequest request )
    {
        String strIdAction = request.getParameter( ConstanteUtils.PARAM_ID_ACTION );
        int nIdAction = DeploymentUtils.getIntegerParameter( strIdAction );

        Collection<Action> listAction = null;
        State state = null;
        String strJspFormDisplay = null;
        CommandResult result = null;

        if ( ( nIdAction != ConstanteUtils.CONSTANTE_ID_NULL ) && ( _nIdCurrentWorkflowDeploySiteContext != null ) )
        {
            WorkflowDeploySiteContext workflowDeploySiteContext = _workflowDeploySiteService
                    .getWorkflowDeploySiteContext( _nIdCurrentWorkflowDeploySiteContext );

            int nIdWorkflowSiteDeploy = DeploymentUtils.getIdWorkflowSiteDeploy( workflowDeploySiteContext );
            result = workflowDeploySiteContext.getCommandResult( );

            if ( WorkflowService.getInstance( ).isDisplayTasksForm( nIdAction, getLocale( ) ) )
            {
                strJspFormDisplay = getJspTasksForm( request, _nIdCurrentWorkflowDeploySiteContext, nIdAction );
            }
            else
            {
                doProcessAction( workflowDeploySiteContext, nIdAction, getPlugin( ), getLocale( ), request );
                DeploymentUtils.stopCommandResult( workflowDeploySiteContext );
                // End Action
                listAction = WorkflowService.getInstance( ).getActions( workflowDeploySiteContext.getId( ), WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE,
                        nIdWorkflowSiteDeploy, getUser( ) );
                state = WorkflowService.getInstance( ).getState( workflowDeploySiteContext.getId( ), WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE,
                        nIdWorkflowSiteDeploy, ConstanteUtils.CONSTANTE_ID_NULL );
            }
        }

        return DeploymentUtils.getJSONForWorkflowAction( strJspFormDisplay, null, result, state, listAction ).toString( );
    }

    /**
     * Do process the workflow actions
     *
     * @param request
     *            the HTTP request
     * @return the JSP return
     */
    public String doProcessAction( HttpServletRequest request )
    {
        String strIdAction = request.getParameter( ConstanteUtils.PARAM_ID_ACTION );
        int nIdAction = DeploymentUtils.getIntegerParameter( strIdAction );

        if ( ( nIdAction != ConstanteUtils.CONSTANTE_ID_NULL ) && ( _nIdCurrentWorkflowDeploySiteContext != null ) )
        {
            WorkflowDeploySiteContext workflowDeploySiteContext = _workflowDeploySiteService
                    .getWorkflowDeploySiteContext( _nIdCurrentWorkflowDeploySiteContext );

            if ( WorkflowService.getInstance( ).isDisplayTasksForm( nIdAction, getLocale( ) ) )
            {
                return getJspTasksForm( request, _nIdCurrentWorkflowDeploySiteContext, nIdAction );
            }

            doProcessAction( workflowDeploySiteContext, nIdAction, getPlugin( ), getLocale( ), request );

            return getJspDeployApplicationProcess( request );
        }

        return getJspManageApplication( request );
    }

    /**
     * save the tasks form
     *
     * @param request
     *            the httpRequest
     * @return The URL to go after performing the action
     */
    public String doSaveTasksFormJSON( HttpServletRequest request )
    {
        String strIdAction = request.getParameter( ConstanteUtils.PARAM_ID_ACTION );
        int nIdAction = DeploymentUtils.getIntegerParameter( strIdAction );

        Collection<Action> listAction = null;
        State state = null;

        CommandResult result = null;
        String strError = null;

        if ( ( nIdAction != ConstanteUtils.CONSTANTE_ID_NULL ) && ( _nIdCurrentWorkflowDeploySiteContext != null ) )
        {
            WorkflowDeploySiteContext workflowDeploySiteContext = _workflowDeploySiteService
                    .getWorkflowDeploySiteContext( _nIdCurrentWorkflowDeploySiteContext );

            int nIdWorkflowSiteDeploy = DeploymentUtils.getIdWorkflowSiteDeploy( workflowDeploySiteContext );
            result = workflowDeploySiteContext.getCommandResult( );

            strError = doSaveTaskForm( workflowDeploySiteContext.getId( ), nIdAction, getPlugin( ), getLocale( ), request );

            listAction = WorkflowService.getInstance( ).getActions( workflowDeploySiteContext.getId( ), WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE,
                    nIdWorkflowSiteDeploy, getUser( ) );
            state = WorkflowService.getInstance( ).getState( workflowDeploySiteContext.getId( ), WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE,
                    nIdWorkflowSiteDeploy, ConstanteUtils.CONSTANTE_ID_NULL );
        }

        return DeploymentUtils.getJSONForWorkflowAction( null, strError, result, state, listAction ).toString( );
    }

    /**
     * save the tasks form
     *
     * @param request
     *            the httpRequest
     * @return The URL to go after performing the action
     */
    public String doSaveTasksForm( HttpServletRequest request )
    {
        WorkflowDeploySiteContext workflowDeploySiteContext = _workflowDeploySiteService.getWorkflowDeploySiteContext( _nIdCurrentWorkflowDeploySiteContext );
        String strIdAction = request.getParameter( ConstanteUtils.PARAM_ID_ACTION );
        int nIdAction = DeploymentUtils.getIntegerParameter( strIdAction );

        if ( _nIdCurrentWorkflowDeploySiteContext != ConstanteUtils.CONSTANTE_ID_NULL )
        {
            if ( request.getParameter( ConstanteUtils.PARAM_CANCEL ) == null )
            {
                String strError = doSaveTaskForm( workflowDeploySiteContext.getId( ), nIdAction, getPlugin( ), getLocale( ), request );

                if ( StringUtils.isNotBlank( strError ) )
                {
                    return AdminMessageService.getMessageUrl( request, strError, AdminMessage.TYPE_STOP );
                }
            }

            return getJspDeployApplicationProcess( request );
        }

        return getJspManageApplication( request );
    }

    /**
     * Do process the workflow action
     *
     * @param nIdDirectory
     *            the id directory
     * @param nIdAction
     *            the id action
     * @param listIdsDirectoryRecord
     *            the list of ids record to execute the action
     * @param plugin
     *            the plugin
     * @param locale
     *            the locale
     * @param request
     *            the HTTP request
     */
    private void doProcessAction( WorkflowDeploySiteContext workflowDeploySiteContext, int nIdAction, Plugin plugin, Locale locale, HttpServletRequest request )
    {

        if ( WorkflowService.getInstance( ).canProcessAction( workflowDeploySiteContext.getId( ), WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE, nIdAction,
                ConstanteUtils.CONSTANTE_ID_NULL, request, false ) )
        {
            try
            {

                DeploymentUtils.startCommandResult( workflowDeploySiteContext );

                Application application = _applicationService.getApplication( workflowDeploySiteContext.getIdApplication( ), plugin );
                Environment environment = _environmentService.getEnvironment( workflowDeploySiteContext.getCodeEnvironement( ), getLocale( ) );

                if ( ( workflowDeploySiteContext.isDeployWar( ) && !isAuthorized( application, ApplicationResourceIdService.PERMISSION_DEPLOY_APPLICATION,
                        environment ) )
                        || ( workflowDeploySiteContext.isDeploySql( ) && !isAuthorized( application, ApplicationResourceIdService.PERMISSION_DEPLOY_SCRIPT,
                                environment ) )
                        || ( workflowDeploySiteContext.isInitBdd( ) && !isAuthorized( application, ApplicationResourceIdService.PERMISSION_INIT_DATABASE,
                                environment ) )
                        || ( workflowDeploySiteContext.isInitAppContext( ) && !isAuthorized( application,
                                ApplicationResourceIdService.PERMISSION_INIT_APP_CONTEXT, environment ) ) )
                {

                    throw new AccessDeniedException( I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) ) );
                }

                WorkflowService.getInstance( ).doProcessAction( workflowDeploySiteContext.getId( ), WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE,
                        nIdAction, ConstanteUtils.CONSTANTE_ID_NULL, request, locale, false );
            }
            catch( AccessDeniedException e )
            {
                AppLogService.error(
                        "Error processing during deployment of application" + workflowDeploySiteContext.getIdApplication( ) + "' - cause : " + e.getMessage( ),
                        e );
            }
            catch( Exception e )
            {
                AppLogService.error( "Error processing during deployment of application  '" + workflowDeploySiteContext.getIdApplication( ) + "' - cause : "
                        + e.getMessage( ), e );
            }
            finally
            {
                DeploymentUtils.stopCommandResult( workflowDeploySiteContext );

            }

        }
    }

    /**
     * Do save the task form
     *
     * @param nIdDirectory
     *            the id directory
     * @param nIdAction
     *            the id action
     * @param listIdsDirectoryRecord
     *            the list of id records
     * @param plugin
     *            the plugin
     * @param locale
     *            the locale
     * @param request
     *            the HTTP request
     * @return an error message if there is an error, null otherwise
     */
    private String doSaveTaskForm( int nIdWorkflowSiteContext, int nIdAction, Plugin plugin, Locale locale, HttpServletRequest request )
    {
        if ( WorkflowService.getInstance( ).canProcessAction( nIdWorkflowSiteContext, WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE, nIdAction,
                ConstanteUtils.CONSTANTE_ID_NULL, request, false ) )
        {
            boolean bHasSucceed = false;

            try
            {
                String strError = WorkflowService.getInstance( ).doSaveTasksForm( nIdWorkflowSiteContext, WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE,
                        nIdAction, ConstanteUtils.CONSTANTE_ID_NULL, request, locale );

                if ( strError != null )
                {
                    return strError;
                }

                bHasSucceed = true;
            }
            catch( Exception e )
            {
                WorkflowDeploySiteContext workflowDeploySiteContext = _workflowDeploySiteService
                        .getWorkflowDeploySiteContext( _nIdCurrentWorkflowDeploySiteContext );

                AppLogService.error( "Error processing during deployment of application  '" + workflowDeploySiteContext.getIdApplication( ) + "' - cause : "
                        + e.getMessage( ), e );
            }
        }

        return null;
    }

    public String getJSONCommandResult( HttpServletRequest request )
    {
        WorkflowDeploySiteContext workflowDeploySiteContext = _workflowDeploySiteService.getWorkflowDeploySiteContext( _nIdCurrentWorkflowDeploySiteContext );
        CommandResult result = workflowDeploySiteContext.getCommandResult( );
        String strReturn = ConstanteUtils.CONSTANTE_EMPTY_STRING;

        if ( result != null )
        {
            JSONObject jo = DeploymentUtils.getJSONForCommandResult( result );
            strReturn = jo.toString( );
        }

        return strReturn;
    }

    public String doDeployApplication( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdApplication = request.getParameter( ConstanteUtils.PARAM_ID_APPLICATION );
        int nIdApplication = DeploymentUtils.getIntegerParameter( strIdApplication );

        if ( ( request.getParameter( ConstanteUtils.PARAM_CANCEL ) == null ) && ( nIdApplication != ConstanteUtils.CONSTANTE_ID_NULL ) )
        {
            WorkflowDeploySiteContext workflowContext = new WorkflowDeploySiteContext( );
            String strError = getFormDeployData( request, workflowContext );

            if ( strError != null )
            {
                return strError;
            }

            Application application = _applicationService.getApplication( nIdApplication, getPlugin( ) );

            if ( !StringUtils.isEmpty( workflowContext.getScriptFileSelected( ) ) )
            {

                try
                {
                    workflowContext.setScriptFileItem( new FileInputStream( DeploymentUtils.getPathUpgradeFile(
                            DeploymentUtils.getPathCheckoutSite( application.getArtifactId( ) ), workflowContext.getScriptFileSelected( ) ) ) );
                }
                catch( FileNotFoundException e )
                {

                    AppLogService.error( e );
                }
                workflowContext.setScriptFileItemName( workflowContext.getScriptFileSelected( ) );
            }

            HttpSession session = request.getSession( true );
            if ( session.getAttribute( ConstanteUtils.CONSTANTE_VCS_USER ) != null )
            {
                AbstractVCSUser user = new AbstractVCSUser( );
                user = (AbstractVCSUser) session.getAttribute( ConstanteUtils.CONSTANTE_VCS_USER );
                workflowContext.setVcsUser( user );
                session.removeAttribute( ConstanteUtils.CONSTANTE_VCS_USER );
            }
            else
            {
                workflowContext.setVcsUser( null );
            }

            workflowContext.setIdApplication( application.getIdApplication( ) );
            workflowContext.setSvnBaseSiteUrl( application.getUrlRepo( ) );

            CommandResult commandResult = new CommandResult( );
            // IVCSService vcsService = DeploymentUtils.getVCSService( application.getRepoType( ) );
            // vcsService.doCheckoutSite( application.getArtifactId(), application.getUrlRepo( ), DeploymentUtils.getVCSUser(application), commandResult, null,
            // workflowContext.getTagName( ) );
            _nIdCurrentWorkflowDeploySiteContext = _workflowDeploySiteService.addWorkflowDeploySiteContext( workflowContext );

            return getJspDeployApplicationProcess( request );
        }

        return getJspManageApplication( request );
    }

    /**
     * return the tasks form
     *
     * @param request
     *            the request
     * @return the tasks form
     */
    public String getTasksForm( HttpServletRequest request )
    {
        String strIdAction = request.getParameter( ConstanteUtils.PARAM_ID_ACTION );

        if ( ( _nIdCurrentWorkflowDeploySiteContext != null ) && StringUtils.isNotBlank( strIdAction ) && StringUtils.isNumeric( strIdAction ) )
        {
            int nIdAction = DeploymentUtils.getIntegerParameter( strIdAction );

            String strHtmlTasksForm = WorkflowService.getInstance( ).getDisplayTasksForm( _nIdCurrentWorkflowDeploySiteContext,
                    WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE, nIdAction, request, getLocale( ) );

            Map<String, Object> model = new HashMap<String, Object>( );

            model.put( ConstanteUtils.MARK_TASKS_FORM, strHtmlTasksForm );
            model.put( ConstanteUtils.MARK_ID_ACTION, nIdAction );

            setPageTitleProperty( ConstanteUtils.PROPERTY_TASKS_FORM_WORKFLOW_PAGE_TITLE );

            HtmlTemplate templateList = AppTemplateService.getTemplate( ConstanteUtils.TEMPLATE_TASKS_FORM_WORKFLOW, getLocale( ), model );

            return templateList.getHtml( );
        }

        return getManageApplication( request );
    }

    /**
     * return the tasks form
     *
     * @param request
     *            the request
     * @return the tasks form
     */
    public String getFormActionServer( HttpServletRequest request )
    {

        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        String strIdApplication = request.getParameter( ConstanteUtils.PARAM_ID_APPLICATION );
        String strCodeEnvironment = request.getParameter( ConstanteUtils.PARAM_CODE_ENVIRONMENT );
        String strCodeServerApplicationInstance = request.getParameter( ConstanteUtils.PARAM_CODE_SERVER_APPLICATION_INSTANCE );
        String strServerAppicationType = request.getParameter( ConstanteUtils.PARAM_SERVER_APPLICATION_TYPE );
        String strActionCode = request.getParameter( ConstanteUtils.PARAM_ACTION_CODE );

        Application application = _applicationService.getApplication( DeploymentUtils.getIntegerParameter( strIdApplication ), plugin );
        IAction action = _actionService.getAction( DeploymentUtils.getActionKey( strActionCode, strServerAppicationType ), request.getLocale( ) );

        ServerApplicationInstance serverApplicationInstance = _serverApplicationService.getServerApplicationInstance( application,
                strCodeServerApplicationInstance, strCodeEnvironment, strServerAppicationType, request.getLocale( ), false, false );

        if ( action != null )
        {
            String strHtmlFormAction = action.getTemplateFormAction( application, serverApplicationInstance, request.getLocale( ) );

            Map<String, Object> model = new HashMap<String, Object>( );

            model.put( ConstanteUtils.MARK_FORM_ACTION, strHtmlFormAction );

            model.put( ConstanteUtils.PARAM_ID_APPLICATION, application.getIdApplication( ) );
            model.put( ConstanteUtils.PARAM_ACTION_CODE, strActionCode );
            model.put( ConstanteUtils.PARAM_CODE_ENVIRONMENT, strCodeEnvironment );
            model.put( ConstanteUtils.PARAM_CODE_SERVER_APPLICATION_INSTANCE, strCodeServerApplicationInstance );
            model.put( ConstanteUtils.PARAM_SERVER_APPLICATION_TYPE, strServerAppicationType );

            setPageTitleProperty( ConstanteUtils.PROPERTY_FORM_ACTION_SERVER_PAGE_TITLE );

            HtmlTemplate templateList = AppTemplateService.getTemplate( ConstanteUtils.TEMPLATE_FORM_ACTION_SERVER, getLocale( ), model );

            return templateList.getHtml( );
        }

        return getManageApplication( request );
    }

    public String doRunActionServer( HttpServletRequest request ) throws AccessDeniedException
    {
        CommandResult commandResult = new CommandResult( );
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        String strIdApplication = request.getParameter( ConstanteUtils.PARAM_ID_APPLICATION );
        String strCodeEnvironment = request.getParameter( ConstanteUtils.PARAM_CODE_ENVIRONMENT );
        String strCodeServerApplicationInstance = request.getParameter( ConstanteUtils.PARAM_CODE_SERVER_APPLICATION_INSTANCE );
        String strServerAppicationType = request.getParameter( ConstanteUtils.PARAM_SERVER_APPLICATION_TYPE );
        String strActionCode = request.getParameter( ConstanteUtils.PARAM_ACTION_CODE );
        String strJspFormToDisplay = null;

        Environment environment = _environmentService.getEnvironment( strCodeEnvironment, getLocale( ) );

        Application application = _applicationService.getApplication( DeploymentUtils.getIntegerParameter( strIdApplication ), plugin );
        IAction action = _actionService.getAction( DeploymentUtils.getActionKey( strActionCode, strServerAppicationType ), request.getLocale( ) );

        ServerApplicationInstance serverApplicationInstance = _serverApplicationService.getServerApplicationInstance( application,
                strCodeServerApplicationInstance, strCodeEnvironment, strServerAppicationType, request.getLocale( ), false, false );

        List<IAction> listNewServersActions = null;
        Integer nNewServersStatus = null;

        if ( action != null )
        {

            if ( !isAuthorized( application, ApplicationResourceIdService.PERMISSION_VIEW, environment ) )
            {

                commandResult.setError( I18nService.getLocalizedString( MESSAGE_ACCESS_DENIED, getLocale( ) ) );
                commandResult.setStatus( CommandResult.STATUS_ERROR );

                return DeploymentUtils.getJSONForCommandResult( commandResult ).toString( );

                // throw new AccessDeniedException( I18nService.getLocalizedString(MESSAGE_ACCESS_DENIED, getLocale()));
            }

            DeploymentUtils.startCommandResult( commandResult );

            if ( _actionService.canExecuteAction( application, action, serverApplicationInstance, commandResult,
                    DeploymentUtils.getActionParameters( request, action.getParameters( ) ) ) )
            {

                commandResult.getLog( ).append( "Starting Action " + action.getName( ) + " \n" );

                _actionService.executeAction( application, action, serverApplicationInstance, commandResult,
                        DeploymentUtils.getActionParameters( request, action.getParameters( ) ) );
                commandResult.getLog( ).append( "End Action " + action.getName( ) + " \n" );

                DeploymentUtils.stopCommandResult( commandResult );

                // get new status
                serverApplicationInstance = _serverApplicationService.getServerApplicationInstance( application, strCodeServerApplicationInstance,
                        strCodeEnvironment, strServerAppicationType, request.getLocale( ), true, true );
                listNewServersActions = serverApplicationInstance.getListServerApplicationAction( );
                nNewServersStatus = serverApplicationInstance.getStatus( );
            }
            else
                if ( _actionService.getTemplateFormAction( application, action, serverApplicationInstance, getLocale( ) ) != null )
                {
                    strJspFormToDisplay = getJspFormActionServer( request, strActionCode, DeploymentUtils.getIntegerParameter( strIdApplication ),
                            strCodeEnvironment, strCodeServerApplicationInstance, strServerAppicationType );
                }

        }

        JSONObject jo = DeploymentUtils.getJSONForServerAction( application.getIdApplication( ), serverApplicationInstance.getCodeEnvironment( ),
                serverApplicationInstance.getCode( ), serverApplicationInstance.getType( ), strJspFormToDisplay, commandResult, listNewServersActions,
                nNewServersStatus );

        return jo.toString( );

    }

    public String doModifyMavenProfil( HttpServletRequest request ) throws AccessDeniedException
    {
        Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
        String strIdApplication = request.getParameter( ConstanteUtils.PARAM_ID_APPLICATION );
        String strCodeEnvironment = request.getParameter( ConstanteUtils.PARAM_CODE_ENVIRONMENT );
        String strCodeServerApplicationInstance = request.getParameter( ConstanteUtils.PARAM_CODE_SERVER_APPLICATION_INSTANCE );
        String strMavenProfil = request.getParameter( ConstanteUtils.PARAM_MAVEN_PROFIL );

        Environment environment = _environmentService.getEnvironment( strCodeEnvironment, getLocale( ) );

        Application application = _applicationService.getApplication( DeploymentUtils.getIntegerParameter( strIdApplication ), plugin );

        if ( isAuthorized( application, ApplicationResourceIdService.PERMISSION_VIEW, environment ) )
        {

            MavenService.getService( ).saveMvnProfilName( strMavenProfil, strIdApplication, strCodeEnvironment, strCodeServerApplicationInstance );
        }

        return getJspViewApplication( request, DeploymentUtils.getIntegerParameter( strIdApplication ) );

    }

    private String getFormDeployData( HttpServletRequest request, WorkflowDeploySiteContext workflowDeploySiteContext )
    {
        String strCodeEnvironment = request.getParameter( ConstanteUtils.PARAM_CODE_ENVIRONMENT );

        String strDeployWar = request.getParameter( ConstanteUtils.PARAM_DEPLOY_WAR );
        String strDeploySql = request.getParameter( ConstanteUtils.PARAM_DEPLOY_SQL );

        String strCodeServerApplicationInstanceTomcat = request.getParameter( ConstanteUtils.PARAM_CODE_SERVER_APPLICATION_INSTANCE_TOMCAT );
        String strCodeServerApplicationInstanceMysql = request.getParameter( ConstanteUtils.PARAM_CODE_SERVER_APPLICATION_INSTANCE_SQL );
        String strDeployDevSite = request.getParameter( ConstanteUtils.PARAM_DEPLOY_DEV_SITE );
        String strTagToDeploy = request.getParameter( ConstanteUtils.PARAM_TAG_TO_DEPLOY );

        String strCodeDatabase = request.getParameter( ConstanteUtils.PARAM_CODE_DATABASE );
        String strScriptUpgradeSelected = request.getParameter( ConstanteUtils.PARAM_SCRIPT_UPGRADE_SELECTED );
        String strInitDatabase = request.getParameter( ConstanteUtils.PARAM_INIT_DATABASE );
        String strInitAppContext = request.getParameter( ConstanteUtils.PARAM_INIT_APP_CONTEXT );

        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        FileItem scriptItem = null;

        String strFieldError = ConstanteUtils.CONSTANTE_EMPTY_STRING;

        if ( StringUtils.isEmpty( strCodeEnvironment ) )
        {
            strFieldError = ConstanteUtils.PROPERTY_LABEL_CODE_ENVIRONMENT;
        }
        else
            if ( !StringUtils.isEmpty( strDeployWar ) && StringUtils.isEmpty( strCodeServerApplicationInstanceTomcat ) )
            {
                strFieldError = ConstanteUtils.PROPERTY_LABEL_CODE_SERVER_APPLICATION_INSTANCE_TOMCAT;
            }
            else
                if ( !StringUtils.isEmpty( strDeploySql ) && StringUtils.isEmpty( strCodeServerApplicationInstanceMysql ) )
                {
                    strFieldError = ConstanteUtils.PROPERTY_LABEL_CODE_SERVER_APPLICATION_INSTANCE_MYSQL;
                }
                else
                    if ( !StringUtils.isEmpty( strDeploySql ) && StringUtils.isEmpty( strCodeDatabase ) && StringUtils.isEmpty( strInitDatabase ) )
                    {
                        strFieldError = ConstanteUtils.PROPERTY_LABEL_CODE_DATABASE;
                    }
                    else
                        if ( !StringUtils.isEmpty( strDeployWar ) && StringUtils.isEmpty( strDeployDevSite ) && StringUtils.isEmpty( strTagToDeploy ) )
                        {
                            strFieldError = ConstanteUtils.PROPERTY_LABEL_TAG_TO_DEPLOY;
                        }
                        else
                            if ( !StringUtils.isEmpty( strDeploySql ) )
                            {
                                List<FileItem> scriptListItem = _handler.getListUploadedFiles( ConstanteUtils.PARAM_SCRIPT_UPLOAD, request.getSession( ) );
                                if ( !CollectionUtils.isEmpty( scriptListItem ) )
                                {
                                    scriptItem = scriptListItem.get( 0 );

                                }
                                if ( StringUtils.isEmpty( strScriptUpgradeSelected ) && StringUtils.isEmpty( strInitDatabase )
                                        && ( scriptItem == null || scriptItem.getSize( ) == 0 ) )
                                {
                                    strFieldError = ConstanteUtils.PROPERTY_LABEL_SCRIPT_UPLOAD;
                                }
                            }

        if ( !StringUtils.isEmpty( strFieldError ) )
        {
            Object [ ] tabRequiredFields = {
                I18nService.getLocalizedString( strFieldError, getLocale( ) )
            };

            return AdminMessageService.getMessageUrl( request, ConstanteUtils.PROPERTY_MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        workflowDeploySiteContext.setCodeEnvironement( strCodeEnvironment );

        if ( !StringUtils.isEmpty( strCodeServerApplicationInstanceTomcat ) )
        {
            workflowDeploySiteContext.setCodeServerInstance( strCodeServerApplicationInstanceTomcat, ConstanteUtils.CONSTANTE_SERVER_TOMCAT );
        }

        if ( !StringUtils.isEmpty( strCodeServerApplicationInstanceMysql ) )
        {
            workflowDeploySiteContext.setCodeServerInstance( strCodeServerApplicationInstanceMysql.split( "_" ) [0],
                    strCodeServerApplicationInstanceMysql.split( "_" ) [1] );

        }

        workflowDeploySiteContext.setDeployDevSite( ( strDeployDevSite != null ) ? true : false );
        workflowDeploySiteContext.setDeployWar( !StringUtils.isEmpty( strDeployWar ) );
        workflowDeploySiteContext.setDeploySql( !StringUtils.isEmpty( strDeploySql ) );
        workflowDeploySiteContext.setInitAppContext( !StringUtils.isEmpty( strInitAppContext ) );
        workflowDeploySiteContext.setInitBdd( !StringUtils.isEmpty( strInitDatabase ) );

        if ( StringUtils.isEmpty( strDeployDevSite ) )
        {
            workflowDeploySiteContext.setTagToDeploy( strTagToDeploy );
            workflowDeploySiteContext.setDeployDevSite( false );
        }
        else
        {
            workflowDeploySiteContext.setDeployDevSite( true );
        }

        if ( !StringUtils.isEmpty( strDeploySql ) )
        {
            workflowDeploySiteContext.setDatabaseName( strCodeDatabase );
            workflowDeploySiteContext.setInitBdd( !StringUtils.isEmpty( strInitDatabase ) );

            if ( !StringUtils.isEmpty( strScriptUpgradeSelected ) )
            {
                workflowDeploySiteContext.setScriptFileSelected( strScriptUpgradeSelected );
            }
            else
            {
                try
                {
                    workflowDeploySiteContext.setScriptFileItem( scriptItem.getInputStream( ) );
                }
                catch( IOException e )
                {
                    AppLogService.error( e );
                }
                workflowDeploySiteContext.setScriptFileItemName( scriptItem.getFieldName( ) );

            }
        }

        return null;
    }

    /**
     * Get the request data and if there is no error insert the data in the digg specified in parameter. return null if there is no error or else return the
     * error page url
     *
     * @param request
     *            the request
     * @param digg
     *            digg
     *
     * @return null if there is no error or else return the error page url
     */
    private String getApplicationData( HttpServletRequest request, Application application )
    {
        String strCode = request.getParameter( ConstanteUtils.PARAM_CODE );
        String strName = request.getParameter( ConstanteUtils.PARAM_NAME );
        String strWebAppName = request.getParameter( ConstanteUtils.PARAM_WEBAPP_NAME );
        String strWorkgroup = request.getParameter( ConstanteUtils.PARAM_WORKGROUP );
        String strUrlRepo = request.getParameter( ConstanteUtils.PARAM_URL_REPO );

        String strFieldError = ConstanteUtils.CONSTANTE_EMPTY_STRING;

        if ( StringUtils.isEmpty( strCode ) )
        {
            strFieldError = ConstanteUtils.PROPERTY_LABEL_CODE;
        }
        else
            if ( StringUtils.isEmpty( strName ) )
            {
                strFieldError = ConstanteUtils.PROPERTY_LABEL_NAME;
            }
            else
                if ( StringUtils.isEmpty( strWebAppName ) )
                {
                    strFieldError = ConstanteUtils.PROPERTY_LABEL_WEBAPP_NAME;
                }
                else
                    if ( StringUtils.isEmpty( strUrlRepo ) )
                    {
                        strFieldError = ConstanteUtils.PROPERTY_LABEL_URL_REPO;
                    }

        if ( !StringUtils.isEmpty( strFieldError ) )
        {
            Object [ ] tabRequiredFields = {
                I18nService.getLocalizedString( strFieldError, getLocale( ) )
            };

            return AdminMessageService.getMessageUrl( request, ConstanteUtils.PROPERTY_MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        application.setCode( strCode );
        application.setName( strName );
        application.setWebAppName( strWebAppName );
        application.setWorkgroup( strWorkgroup );
        application.setUrlRepo( strUrlRepo );

        try
        {
            RepositoryUtils.checkRepository( application );
        }
        catch( InvalidRepositoryUrlException e )
        {
            return AdminMessageService.getMessageUrl( request, ConstanteUtils.PROPERTY_MESSAGE_INVALID_REPO_URL, AdminMessage.TYPE_STOP );
        }

        return null; // No error
    }

    private String getJspManageApplication( HttpServletRequest request )
    {
        return AppPathService.getBaseUrl( request ) + ConstanteUtils.JSP_MANAGE_APPLICATION;
    }

    private String getJspViewApplication( HttpServletRequest request, int nIdApplication )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + ConstanteUtils.JSP_VIEW_APPLICATION );
        url.addParameter( ConstanteUtils.PARAM_ID_APPLICATION, nIdApplication );

        return url.getUrl( );
    }

    /**
     * return url of the jsp manage commentaire
     *
     * @param request
     *            The HTTP request
     * @param listIdsTestResource
     *            the list if id resource
     * @param nIdAction
     *            the id action
     * @param bShowActionResult
     *            true if it must show the action result, false otherwise
     * @return url of the jsp manage commentaire
     */
    private String getJspTasksForm( HttpServletRequest request, int nIdWorkFlowContext, int nIdAction )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + ConstanteUtils.JSP_TASK_FORM );
        url.addParameter( ConstanteUtils.PARAM_ID_ACTION, nIdAction );
        url.addParameter( ConstanteUtils.PARAM_ID_WORKFLOW_CONTEXT, nIdWorkFlowContext );

        return url.getUrl( );
    }

    /**
     * return url of the jsp manage commentaire
     *
     * @param request
     *            The HTTP request
     * @param listIdsTestResource
     *            the list if id resource
     * @param nIdAction
     *            the id action
     * @param bShowActionResult
     *            true if it must show the action result, false otherwise
     * @return url of the jsp manage commentaire
     */
    private String getJspFormActionServer( HttpServletRequest request, String strActionCode, int nIdApplication, String strCodeEnvironment,
            String strCodeServerApplicationInstance, String strServerAppicationType )
    {

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + ConstanteUtils.JSP_FORM_SERVER_ACTION );
        url.addParameter( ConstanteUtils.PARAM_ID_APPLICATION, nIdApplication );
        url.addParameter( ConstanteUtils.PARAM_ACTION_CODE, strActionCode );
        url.addParameter( ConstanteUtils.PARAM_CODE_ENVIRONMENT, strCodeEnvironment );
        url.addParameter( ConstanteUtils.PARAM_CODE_SERVER_APPLICATION_INSTANCE, strCodeServerApplicationInstance );
        url.addParameter( ConstanteUtils.PARAM_SERVER_APPLICATION_TYPE, strServerAppicationType );

        return url.getUrl( );
    }

    private String getJspDeployApplicationProcess( HttpServletRequest request )
    {
        return AppPathService.getBaseUrl( request ) + ConstanteUtils.JSP_DEPLOY_APPLICATION_PROCESS;
    }

    private boolean isAuthorized( Application application, String strPermission )
    {

        return RBACService.isAuthorized( Application.RESOURCE_TYPE, Integer.toString( application.getIdApplication( ) ), strPermission, getUser( ) )
                && AdminWorkgroupService.isAuthorized( application, getUser( ) );

    }

    private boolean isAuthorized( Application application, String strPermission, Environment environment )
    {

        return RBACService.isAuthorized( Application.RESOURCE_TYPE, Integer.toString( application.getIdApplication( ) ), strPermission, getUser( ) )
                && AdminWorkgroupService.isAuthorized( application, getUser( ) )
                && RBACService.isAuthorized( Environment.RESOURCE_TYPE, environment.getResourceId( ),
                        EnvironmentResourceIdService.PERMISSION_DEPLOY_ON_ENVIROMENT, getUser( ) );

    }

    /**
     * Get the credentials form
     * 
     * @param request
     *            the HttpServletRequest
     * @return the credentials form
     * @throws AccessDeniedException
     */
    public String getFormAskCredentials( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdApplication = request.getParameter( ConstanteUtils.PARAM_ID_APPLICATION );
        int nIdApplication = Integer.parseInt( strIdApplication );
        String strActionUrl = request.getParameter( ConstanteUtils.PARAM_ACTION_URL );
        Application application = _applicationService.getApplication( nIdApplication, getPlugin( ) );
        if ( ApplicationService.isPrivateRepo( application ) )
        {
            Map<String, Object> model = new HashMap<>( );
            model.put( ConstanteUtils.MARK_VCS_SERVICE, DeploymentUtils.getVCSService( application.getRepoType( ) ) );
            model.put( ConstanteUtils.MARK_APPLICATION, application );
            model.put( ConstanteUtils.MARK_ACTION_URL, strActionUrl );

            // Return ask credentials form
            HtmlTemplate template = AppTemplateService.getTemplate( ConstanteUtils.TEMPLATE_ASK_CREDENTIALS, getLocale( ), model );
            return template.getHtml( );
        }
        else
        {
            // No ask for credentials for scripts deployment
            return doDeployApplication( request );
        }
    }

    /**
     * Check if the provided repository is valid
     * 
     * @param request
     *            the HttpServletRequest request
     * @return a JSON with the validity and type of the repo
     */
    public String checkRepository( HttpServletRequest request )
    {
        String strRepoUrl = request.getParameter( ConstanteUtils.PARAM_URL_REPO );
        return RepositoryUtils.checkRepository( strRepoUrl );
    }

}
