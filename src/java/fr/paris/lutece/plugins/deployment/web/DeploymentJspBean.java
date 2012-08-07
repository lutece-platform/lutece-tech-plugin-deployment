package fr.paris.lutece.plugins.deployment.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.deployment.business.Application;
import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.business.FilterDeployment;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;
import fr.paris.lutece.plugins.deployment.business.WorkflowDeploySiteContext;
import fr.paris.lutece.plugins.deployment.service.IApplicationService;
import fr.paris.lutece.plugins.deployment.service.IEnvironmentService;
import fr.paris.lutece.plugins.deployment.service.ISvnService;
import fr.paris.lutece.plugins.deployment.service.IWorkflowDeploySiteService;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

public class DeploymentJspBean extends PluginAdminPageJspBean {
	
	public static final String RIGHT_DEPLOYMENT_MANAGEMENT = "DEPLOYMENT_MANAGEMENT";
	@Inject
	private IApplicationService _applicationService;
	@Inject
	private IEnvironmentService _environmentService;
	@Inject
	private ISvnService _svnService;
	@Inject
	private IWorkflowDeploySiteService _workflowDeploySiteService;
	
	private String _strCurrentPageIndex;
	private int _nItemsPerPage;
	
	private int _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt(
			ConstanteUtils.PROPERTY_NB_ITEM_PER_PAGE, 50);
	
	private int _nIdCurrentWorkflowDeploySiteContext;
	
 
	public String getManageApplication(HttpServletRequest request) {
		
		String strCodeCategory = request.getParameter(ConstanteUtils.PARAM_CODE_CATEGORY);
		_nItemsPerPage = Paginator.getItemsPerPage(request,
				Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
				_nDefaultItemsPerPage);

		//ReferenceList
		ReferenceList refListCategory=_applicationService.getListCategory();
		// build Filter
		FilterDeployment filter = new FilterDeployment();
		filter.setCodeCategory(strCodeCategory);
		List<Application> listApplication =_applicationService.getListApplications(filter, getPlugin()); 
			
		HashMap model = new HashMap();
		Paginator paginator = new Paginator(listApplication, _nItemsPerPage,
				getJspManageApplication(request), Paginator.PARAMETER_PAGE_INDEX,
				_strCurrentPageIndex);
		
		model.put(ConstanteUtils.MARK_APPLICATION_LIST, paginator.getPageItems());
		model.put(ConstanteUtils.MARK_CATEGORY_LIST, refListCategory);
		model.put(ConstanteUtils.MARK_PAGINATOR, paginator);
		
	
		
		  
		setPageTitleProperty( ConstanteUtils.PROPERTY_MANAGE_APPLICATION_PAGE_TITLE );


		HtmlTemplate templateList = AppTemplateService.getTemplate(
				ConstanteUtils.TEMPLATE_MANAGE_APPLICATION, getLocale(), model);
		
		return getAdminPage(templateList.getHtml());
		
	}
	
	public String getCreateApplication(HttpServletRequest request)
	{
		
		//ReferenceList
		ReferenceList refListCategory=_applicationService.getListCategory();
		// build Filter
			
		HashMap model = new HashMap();
		model.put(ConstanteUtils.MARK_CATEGORY_LIST, refListCategory);
		
		setPageTitleProperty( ConstanteUtils.PROPERTY_MANAGE_APPLICATION_PAGE_TITLE );
		HtmlTemplate templateList = AppTemplateService.getTemplate(
				ConstanteUtils.TEMPLATE_CREATE_APPLICATION, getLocale(), model);
		
		return getAdminPage(templateList.getHtml());
		
	}
	
	
	public String getModifyApplication(HttpServletRequest request)
	{
		
		String strIdApplication=request.getParameter(ConstanteUtils.PARAM_ID_APPLICATION);
		int nIdApplication=DeploymentUtils.getIntegerParameter(strIdApplication);
		
		//ReferenceList
		ReferenceList refListCategory=_applicationService.getListCategory();
		Application application=_applicationService.getApplication(nIdApplication,getPlugin());
		// build Filter
			
		HashMap model = new HashMap();
		model.put(ConstanteUtils.MARK_CATEGORY_LIST, refListCategory);
		model.put(ConstanteUtils.MARK_APPLICATION, application);
		
		setPageTitleProperty( ConstanteUtils.PROPERTY_CREATE_APPLICATION_PAGE_TITLE );
		HtmlTemplate templateList = AppTemplateService.getTemplate(
				ConstanteUtils.TEMPLATE_CREATE_APPLICATION, getLocale(), model);
		
		return getAdminPage(templateList.getHtml());
		
	}
	
	
	public String doModifyApplication(HttpServletRequest request)
	{
		
		String strIdApplication = request.getParameter(ConstanteUtils.PARAM_ID_APPLICATION);
		int nIdApplication = DeploymentUtils.getIntegerParameter(strIdApplication);

		if ((request.getParameter(ConstanteUtils.PARAM_CANCEL) == null)
				&& (nIdApplication != ConstanteUtils.CONSTANTE_ID_NULL))
	 {
		
		
		Application application=_applicationService.getApplication(nIdApplication, getPlugin());	
		String strError = getApplicationData(request, application);

		if (strError != null) {
			return strError;
		}
		
		_applicationService.updateApplication(application, getPlugin());
		
	}
		return getJspManageApplication(request);
		
	}
	
	
	public String doCreateApplication(HttpServletRequest request)
	{
		
		Application application = new Application();
		String strError = getApplicationData(request, application);

		if (strError != null) {
			return strError;
		}
		
		_applicationService.createApplication(application, getPlugin());
		return getJspManageApplication(request);
		
	}
	
	
	
	
	
	
	/**
	 * Gets the confirmation page of delete digg submit
	 * 
	 * @param request
	 *            The HTTP request
	 * @return the confirmation page of delete digg submit
	 */
	public String getConfirmRemoveApplication(HttpServletRequest request) {
		
		String strIdApplication = request.getParameter(ConstanteUtils.PARAM_ID_APPLICATION);
		int nIdApplication = DeploymentUtils.getIntegerParameter(strIdApplication);


		
		if (nIdApplication == ConstanteUtils.CONSTANTE_ID_NULL) {
			return getJspManageApplication(request);
		}

		String strMessage = ConstanteUtils.PROPERTY_MESSAGE_CONFIRM_REMOVE_APPLICATION;
		UrlItem url = new UrlItem(ConstanteUtils.JSP_REMOVE_APPLICATION);
		url.addParameter(ConstanteUtils.PARAM_ID_APPLICATION, nIdApplication);

		return AdminMessageService.getMessageUrl(request, strMessage, url
				.getUrl(), AdminMessage.TYPE_CONFIRMATION);
	}
	
	
	/**
	 * Gets the confirmation page of delete digg submit
	 * 
	 * @param request
	 *            The HTTP request
	 * @return the confirmation page of delete digg submit
	 */
	public String DoRemoveApplication(HttpServletRequest request) {
		
		String strIdApplication = request.getParameter(ConstanteUtils.PARAM_ID_APPLICATION);
		int nIdApplication = DeploymentUtils.getIntegerParameter(strIdApplication);

		_applicationService.deleteApplication(nIdApplication, getPlugin());
		return getJspManageApplication(request);

	}
	
	
	public  String getFormDeploySite(HttpServletRequest request) 
	{
		
		AdminUser adminUser=getUser();
		
		String strIdApplication = request.getParameter(ConstanteUtils.PARAM_ID_APPLICATION);
		int nIdApplication = DeploymentUtils.getIntegerParameter(strIdApplication);

		if (nIdApplication == ConstanteUtils.CONSTANTE_ID_NULL) {
			return getJspManageApplication(request);
		}
		
		Application application=_applicationService.getApplication(nIdApplication, getPlugin());
		
		List<Environment> listEnvironments=_environmentService.getListEnvironments(application.getCode());
		List<ServerApplicationInstance> listServerApplicationInstance=_environmentService.getListServerApplicationInstance(application.getCode());
		ReferenceList refListTagSite=_svnService.getTagsSite(application.getSvnUrlSite(), DeploymentUtils.getMavenUser(adminUser.getUserId(), getLocale()));
		
		
		
		setPageTitleProperty( ConstanteUtils.PROPERTY_MANAGE_APPLICATION_PAGE_TITLE );
		HashMap model = new HashMap();
		model.put(ConstanteUtils.MARK_ENVIRONMENT_LIST, listEnvironments);
		model.put(ConstanteUtils.MARK_SERVER_INSTANCE_LIST, listServerApplicationInstance);
		model.put(ConstanteUtils.MARK_SITE_LIST, refListTagSite);
		
		HtmlTemplate template = AppTemplateService.getTemplate(
				ConstanteUtils.TEMPLATE_FORM_POST_DEPLOY_SITE, getLocale(), model);
		return getAdminPage(template.getHtml());
		
	}
	
	
	public  String getProcessDeploySite(HttpServletRequest request) 
	{
		
		AdminUser adminUser=getUser();
		WorkflowDeploySiteContext workflowDeploySiteContext=_workflowDeploySiteService.getWorkflowDeploySiteContext(_nIdCurrentWorkflowDeploySiteContext);
		int nIdWorkflowSiteDeploy=DeploymentUtils.getIdWorkflowSiteDeploy(workflowDeploySiteContext.isTagSiteBeforeDeploy());
		Application  application=_applicationService.getApplication(workflowDeploySiteContext.getId(), getPlugin());
		Environment environment=_environmentService.getEnvironment(workflowDeploySiteContext.getCodeEnvironement());
		ServerApplicationInstance serverApplicationInstance=_environmentService.getServerApplicationInstance(application.getCode(), workflowDeploySiteContext.getCodeServerAppplicationInstance(), workflowDeploySiteContext.getCodeEnvironement());
		//workflow informations
		Collection<Action> listAction=WorkflowService.getInstance().getActions(workflowDeploySiteContext.getId(), WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE, nIdWorkflowSiteDeploy, adminUser);
		State state=WorkflowService.getInstance().getState(workflowDeploySiteContext.getId(), WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE, nIdWorkflowSiteDeploy, ConstanteUtils.CONSTANTE_ID_NULL);
		
		HashMap model = new HashMap();
		model.put(ConstanteUtils.MARK_APPLICATION, application);
		model.put(ConstanteUtils.MARK_SERVER_INSTANCE, serverApplicationInstance);
		model.put(ConstanteUtils.MARK_ENVIRONMENT, environment);
		model.put(ConstanteUtils.MARK_STATE,state.getName());
		model.put(ConstanteUtils.MARK_ACTION_LIST,listAction);
		
		setPageTitleProperty( ConstanteUtils.PROPERTY_DEPLOY_SITE_PAGE_TITLE);
		HtmlTemplate template = AppTemplateService.getTemplate(
				ConstanteUtils.TEMPLATE_DEPLOY_SITE_PROCESS, getLocale(), model);
		return getAdminPage(template.getHtml());
		
	}
	
	
	
	 /**
     * Do process the workflow actions
     * @param request the HTTP request
     * @return the JSP return
     */
    public String doProcessAction( HttpServletRequest request )
    {
    	WorkflowDeploySiteContext workflowDeploySiteContext=_workflowDeploySiteService.getWorkflowDeploySiteContext(_nIdCurrentWorkflowDeploySiteContext);
		String strIdAction = request.getParameter( ConstanteUtils.PARAM_ID_ACTION );
        int nIdAction = DeploymentUtils.getIntegerParameter(strIdAction );

            if ( WorkflowService.getInstance(  ).isDisplayTasksForm( nIdAction, getLocale(  ) ) )
            {
                return getJspTasksForm( request, _nIdCurrentWorkflowDeploySiteContext, nIdAction);
            }

          
                         doProcessAction( workflowDeploySiteContext.getId(), nIdAction,  getPlugin(  ),
                getLocale(  ), request );

          return null;
                      
    }
    
    
    
    
    /**
     * save the tasks form
     * @param request the httpRequest
     * @return The URL to go after performing the action
     */
    public String doSaveTasksForm( HttpServletRequest request )
    {
    	
    	
    	
    	WorkflowDeploySiteContext workflowDeploySiteContext=_workflowDeploySiteService.getWorkflowDeploySiteContext(_nIdCurrentWorkflowDeploySiteContext);
		String strIdAction = request.getParameter( ConstanteUtils.PARAM_ID_ACTION );
	    int nIdAction = DeploymentUtils.getIntegerParameter(strIdAction );

		
		
      if ( _nIdCurrentWorkflowDeploySiteContext !=ConstanteUtils.CONSTANTE_ID_NULL )
        {
            
            if ( request.getParameter( ConstanteUtils.PARAM_CANCEL ) == null )
            {
               
             

              
                String strError = doSaveTaskForm(  workflowDeploySiteContext.getId(), nIdAction, getPlugin(  ), getLocale(  ), request );

                if ( StringUtils.isNotBlank( strError ) )
                {
                    return strError;
                }

            }
        }
      return null;
    }
    
    


    /**
     * Do process the workflow action
     * @param nIdDirectory the id directory
     * @param nIdAction the id action
     * @param listIdsDirectoryRecord the list of ids record to execute the action
     * @param plugin the plugin
     * @param locale the locale
     * @param request the HTTP request
     */
    private void doProcessAction( int nIdWorkflowSiteContext, int nIdAction, Plugin plugin,
        Locale locale, HttpServletRequest request )
    {
   

            if ( WorkflowService.getInstance(  )
                                    .canProcessAction( nIdWorkflowSiteContext, WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE, nIdAction,
                        ConstanteUtils.CONSTANTE_ID_NULL, request, false ) )
            {
                boolean bHasSucceed = false;

                try
                {
                    WorkflowService.getInstance(  )
                                   .doProcessAction( nIdWorkflowSiteContext, WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE, nIdAction, ConstanteUtils.CONSTANTE_ID_NULL,
                        request, locale, false );
                    bHasSucceed = true;
                }
                catch ( Exception e )
                {
                	WorkflowDeploySiteContext workflowDeploySiteContext=_workflowDeploySiteService.getWorkflowDeploySiteContext(_nIdCurrentWorkflowDeploySiteContext);
            		
                    AppLogService.error( "Error processing during deployment of application  '" + workflowDeploySiteContext.getIdApplication() + "' - cause : " +
                        e.getMessage(  ), e );
                }

                   }
        
        }
    

    /**
     * Do save the task form
     * @param nIdDirectory the id directory
     * @param nIdAction the id action
     * @param listIdsDirectoryRecord the list of id records
     * @param plugin the plugin
     * @param locale the locale
     * @param request the HTTP request
     * @return an error message if there is an error, null otherwise
     */
    private String doSaveTaskForm( int nIdWorkflowSiteContext, int nIdAction,Plugin plugin,
        Locale locale, HttpServletRequest request )
    {
        
            if ( WorkflowService.getInstance(  )
                                    .canProcessAction( nIdWorkflowSiteContext, WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE, nIdAction,
                                    		ConstanteUtils.CONSTANTE_ID_NULL, request, false ) )
            {
                boolean bHasSucceed = false;

                try
                {
                    String strError = WorkflowService.getInstance(  )
                                                     .doSaveTasksForm( nIdWorkflowSiteContext, WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE,
                            nIdAction, ConstanteUtils.CONSTANTE_ID_NULL, request, locale );

                    if ( strError != null )
                    {
                        return strError;
                    }

                    bHasSucceed = true;
                }
                catch ( Exception e )
                {
                
                	WorkflowDeploySiteContext workflowDeploySiteContext=_workflowDeploySiteService.getWorkflowDeploySiteContext(_nIdCurrentWorkflowDeploySiteContext);
            		
                    AppLogService.error( "Error processing during deployment of application  '" + workflowDeploySiteContext.getIdApplication() + "' - cause : " +
                            e.getMessage(  ), e );
                                }
                
            }
           
        

        return null;
    }
    
    public String getJSONCommandResult(HttpServletRequest request)
    {
    	WorkflowDeploySiteContext workflowDeploySiteContext=_workflowDeploySiteService.getWorkflowDeploySiteContext(_nIdCurrentWorkflowDeploySiteContext);
    	CommandResult result=workflowDeploySiteContext.getCommandResult();
    	
    	
    	JSONObject jo=DeploymentUtils.getJSONForCommandResult(result);
    	int nIdWorkflowSiteDeploy=DeploymentUtils.getIdWorkflowSiteDeploy(workflowDeploySiteContext.isTagSiteBeforeDeploy());
    	if(!result.isRunning())
    	{
    		//workflow informations
    		Collection<Action> listAction=WorkflowService.getInstance().getActions(workflowDeploySiteContext.getId(), WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE, nIdWorkflowSiteDeploy, getUser());
    		State state=WorkflowService.getInstance().getState(workflowDeploySiteContext.getId(), WorkflowDeploySiteContext.WORKFLOW_RESOURCE_TYPE, nIdWorkflowSiteDeploy, ConstanteUtils.CONSTANTE_ID_NULL);
    		DeploymentUtils.addWorkflowInformations(jo, state, listAction);
    		
    	}
    	return jo.toString();
    }
	

	public  String doDeploySite(HttpServletRequest request) 
	{
		
		
		AdminUser adminUser=getUser();
		String strIdApplication = request.getParameter(ConstanteUtils.PARAM_ID_APPLICATION);
		int nIdApplication = DeploymentUtils.getIntegerParameter(strIdApplication);
		if ((request.getParameter(ConstanteUtils.PARAM_CANCEL) == null)
				&& (nIdApplication != ConstanteUtils.CONSTANTE_ID_NULL))
		{
		
			WorkflowDeploySiteContext workflowContext=new WorkflowDeploySiteContext();
			String strError = getFormDeployData(request, workflowContext);

			if (strError != null) {
				return strError;
			}
			
			Application application=_applicationService.getApplication(nIdApplication, getPlugin());
			
			
			workflowContext.setMavenUser(DeploymentUtils.getMavenUser(adminUser.getUserId(), getLocale()));
			workflowContext.setIdApplication(application.getIdApplication());
			workflowContext.setSvnBaseSiteUrl(application.getSvnUrlSite());
			
			_nIdCurrentWorkflowDeploySiteContext=_workflowDeploySiteService.addWorkflowDeploySiteContext(workflowContext);
			
			}
		
		return getJspManageApplication(request);
		
	}
	
	
	
	
	
	private String getFormDeployData(HttpServletRequest request,WorkflowDeploySiteContext workflowDeploySiteContext)
	{
		
		String strCodeEnvironment = request.getParameter(ConstanteUtils.PARAM_CODE_ENVIRONMENT);
		String strCodeServerApplicationInstance = request.getParameter(ConstanteUtils.PARAM_CODE_SERVER_APPLICATION_INSTANCE);
		String strTagSiteBeforeDeploy = request.getParameter(ConstanteUtils.PARAM_TAG_SITE_BEFORE_DEPLOY);
		String strTagToDeploy = request.getParameter(ConstanteUtils.PARAM_TAG_TO_DEPLOY);
		
		String strFieldError = ConstanteUtils.CONSTANTE_EMPTY_STRING;
		
		if (StringUtils.isEmpty(strCodeEnvironment)) {
			strFieldError = ConstanteUtils.PROPERTY_LABEL_CODE_ENVIRONMENT;
		}
		else if (StringUtils.isEmpty(strCodeServerApplicationInstance)) {
			strFieldError = ConstanteUtils.PROPERTY_LABEL_CODE_SERVER_APPLICATION_INSTANCE;
		}
		else if(StringUtils.isEmpty(strTagSiteBeforeDeploy) &&StringUtils.isEmpty(strTagToDeploy) )
		{
			strFieldError = ConstanteUtils.PROPERTY_LABEL_TAG_TO_DEPLOY;
		}
		
		
		if (!StringUtils.isEmpty(strFieldError)) {
			Object[] tabRequiredFields = { I18nService.getLocalizedString(
					strFieldError, getLocale()) };

			return AdminMessageService.getMessageUrl(request,
					ConstanteUtils.PROPERTY_MESSAGE_MANDATORY_FIELD, tabRequiredFields,
					AdminMessage.TYPE_STOP);
		}
		
		workflowDeploySiteContext.setCodeEnvironement(strCodeEnvironment);
		workflowDeploySiteContext.setCodeServerAppplicationInstance(strCodeServerApplicationInstance);
		workflowDeploySiteContext.setTagSiteBeforeDeploy(!StringUtils.isEmpty(strTagSiteBeforeDeploy));
		workflowDeploySiteContext.setTagToDeploy(strTagToDeploy);
		return null;
		
	}
	
	
	/**
	 * Get the request data and if there is no error insert the data in the digg
	 * specified in parameter. return null if there is no error or else return
	 * the error page url
	 * 
	 * @param request
	 *            the request
	 * @param digg
	 *            digg
	 * 
	 * @return null if there is no error or else return the error page url
	 */
	private String getApplicationData(HttpServletRequest request,Application application) {
		
		
		String strCode = request.getParameter(ConstanteUtils.PARAM_CODE);
		String strName = request.getParameter(ConstanteUtils.PARAM_NAME);
		String strWebAppName = request.getParameter(ConstanteUtils.PARAM_WEBAPP_NAME);
		String strCodeCategory = request.getParameter(ConstanteUtils.PARAM_CODE_CATEGORY);
		String strSite = request.getParameter(ConstanteUtils.PARAM_SITE);
		
		String strFieldError = ConstanteUtils.CONSTANTE_EMPTY_STRING;
		
		if (StringUtils.isEmpty(strCode)) {
			strFieldError = ConstanteUtils.PROPERTY_LABEL_CODE;
		}
		else if (StringUtils.isEmpty(strName)) {
			strFieldError = ConstanteUtils.PROPERTY_LABEL_NAME;
		}
		else if (StringUtils.isEmpty(strWebAppName)) {
			strFieldError = ConstanteUtils.PROPERTY_LABEL_WEBAPP_NAME;
		}
		else if (StringUtils.isEmpty(strCodeCategory)) {
			strFieldError = ConstanteUtils.PROPERTY_LABEL_CODE_CATEGORY;
		}
		else if (StringUtils.isEmpty(strSite)) {
			strFieldError = ConstanteUtils.PROPERTY_LABEL_SITE;
		}
		if (!StringUtils.isEmpty(strFieldError)) {
			Object[] tabRequiredFields = { I18nService.getLocalizedString(
					strFieldError, getLocale()) };

			return AdminMessageService.getMessageUrl(request,
					ConstanteUtils.PROPERTY_MESSAGE_MANDATORY_FIELD, tabRequiredFields,
					AdminMessage.TYPE_STOP);
		}

		application.setCode(strCodeCategory);
		application.setName(strName);
		application.setWebAppName(strWebAppName);
		application.setCodeCategory(strCodeCategory);
		application.setSiteName(strSite);
		return null; // No error
	}
	
	

	
	
	
	private String getJspManageApplication(HttpServletRequest request) {
		return AppPathService.getBaseUrl(request) + ConstanteUtils.JSP_MANAGE_APPLICATION;
	}
	
	
	/**
     * return url of the jsp manage commentaire
     * @param request The HTTP request
     * @param listIdsTestResource the list if id resource
     * @param nIdAction the id action
     * @param bShowActionResult true if it must show the action result, false otherwise
     * @return url of the jsp manage commentaire
     */
    private String getJspTasksForm( HttpServletRequest request, int nIdWorkFlowContext, int nIdAction )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + ConstanteUtils.JSP_TASK_FORM );
        url.addParameter( ConstanteUtils.PARAM_ID_ACTION, nIdAction );
        url.addParameter( ConstanteUtils.PARAM_ID_WORKFLOW_CONTEXT, nIdWorkFlowContext );
        return url.getUrl(  );
    }
	

}
