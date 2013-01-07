package fr.paris.lutece.plugins.deployment.util;

public class ConstanteUtils {
	
	
		public static final int CONSTANTE_ID_NULL=-1; 	
		public static final String CONSTANTE_ALL="all";
		public static final int CONSTANTE_DEFAULT_LOG_SIZE=500;
		public static final String REGEX_ID = "^[\\d]+$";
		public static final String CONSTANTE_TAGS = "tags";
		public static final String CONSTANTE_TRUNK = "trunk";
		public static final String CONSTANTE_CHECKOUT_ERROR = "Checkout error";
		public static final String CONSTANTE_SEPARATOR_SLASH = "/";
		public static final String CONSTANTE_SEPARATOR_POINT = ".";
		public static final String CONSTANTE_SEPARATOR_VIRGULE = ",";
		public static final String CONSTANTE_EMPTY_STRING = "";
		public static final String CONSTANTE_MARK_STACKTRACE = "stack trace      ";
		public static final String CONSTANTE_SPACE = " ";
		public static final String CONSTANTE_POM_XML = "pom.xml";
		public static final String CONSTANTE_TARGET = "target";
		public static final String CONSTANTE_MAVEN_HOME_PATH="deployment.mavenHomePath";
		public static final String CONSTANTE_MAVEN_LOCAL_REPOSITORY="deployment.mavenLocalRepository";

		public static final String CONSTANTE__ENVIRONMENT= "deployment.environment.";
		public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE= "deployment.serverApplicationInstance.";
		public static final String CONSTANTE__ENVIRONMENT_CODE = ".code";
		
		public static final String CONSTANTE__ENVIRONMENT_NAME = ".name";
		public static final String CONSTANTE__ENVIRONMENT_SERVER_APPLICATION_INSTANCE_LIST = ".serverApplicationInstanceList";
		public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_CODE = ".code";
		public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_NAME = ".name";
		public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_SERVER_NAME = ".serverName";
		public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_FTP_WEBAPP_Url=".ftpWebAppUrl";
		public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_MAVEN_PROFILE = ".mavenProfile";
		
	    public static final String CONSTANTE_SQL_WHERE = " WHERE ";
	    public static final String CONSTANTE_SQL_AND = " AND ";
	    public static final String CONSTANTE_SERVER_TOMCAT="TOMCAT";
	    public static final String CONSTANTE_SERVER_RO="RO";
	    public static final String CONSTANTE_MAX_DEPLOY_SITE_CONTEXT_KEY="max_deploy_site_context_key";
	    
	    
	    public static final String JSON_STATUS="status";
	    public static final String JSON_LOG="log";
	    public static final String JSON_RUNNING="running";
	    public static final String JSON_ID_ERROR="id_error";
	    public static final String JSON_STATE="state";
	    public static final String JSON_ACTION_LIST="action_list";
	    public static final String JSON_ACTION_ID="id";
	    public static final String JSON_ACTION_NAME="name";
	    public static final String JSON_ACTION_DESCRIPTION="description";
	    public static final String JSON_JSP_FOM_DISPLAY="jsp_form_display";
	    public static final String JSON_FORM_ERROR="form_error";
	   
	    //PROPERTY

		public static final String PROPERTY_NB_ITEM_PER_PAGE="deployment.defaultNbItemPerPage";
	    public static final String PROPERTY_MANAGE_APPLICATION_PAGE_TITLE="deployment.manage_application.page_title";
	    public static final String PROPERTY_CREATE_APPLICATION_PAGE_TITLE="deployment.create_application.page_title";
	    public static final String PROPERTY_MODIFY_APPLICATION_PAGE_TITLE="deployment.create_application.page_title";
	    public static final String PROPERTY_FORM_DEPLOY_SITE_PAGE_TITLE="deployment.form_deploy_site.page_title";
	    public static final String PROPERTY_LABEL_CODE = "deployment.create_application.label_code";
	    public static final String PROPERTY_LABEL_CODE_ENVIRONMENT = "deployment.form_init_deploy_application.label_code_environment";
	    public static final String PROPERTY_LABEL_CODE_SERVER_APPLICATION_INSTANCE = "deployment.form_init_deploy_application.label_code_server_application_instance";
	    public static final String PROPERTY_LABEL_TAG_TO_DEPLOY = "deployment.form_init_deploy_application.label_tag_to_deploy";
	    public static final String PROPERTY_LABEL_CODE_CATEGORY = "deployment.create_application.label_code_category";
	    public static final String PROPERTY_LABEL_NAME = "deployment.create_application.label_name";
	    public static final String PROPERTY_LABEL_WEBAPP_NAME = "deployment.create_application.label_webapp_name";
	    public static final String PROPERTY_LABEL_SITE = "deployment.create_application.label_site";
	    public static final String PROPERTY_MESSAGE_MANDATORY_FIELD = "deployment.message.mandatory_field";
	    public static final String PROPERTY_MESSAGE_CONFIRM_REMOVE_APPLICATION = "deployment.message.confirm_remove_application";
	    public static final String PROPERTY_ADMINUSER_ID_ATTRIBUTE_SVN_LOGIN = "deployment.adminUser.idAttribute.svnLogin";
	    public static final String PROPERTY_ADMINUSER_ID_ATTRIBUTE_SVN_PASSWORD = "deployment.adminUser.idAttribute.svnPassword";
	    public static final String PROPERTY_ID_WORKFLOW_TAG_AND_DEPLOY_SITE="deployment.idWorkflowTagAndDeploySite";
	    public static final String PROPERTY_ID_WORKFLOW_DEPLOY_SITE="deployment.idWorkflowDeploySite";
	    public static final String PROPERTY_ID_WORKFLOW_TAG_SITE="deployment.idWorkflowTagSite";
	    public static final String PROPERTY_DEPLOY_SITE_PAGE_TITLE="deployment.deploy_site.page_title";
	    public static final String PROPERTY_MAX_LOG_SIZE="deployment.maxLogSize";
	    public static final String PROPERTY_ENVIRONMENTS_LIST = "deployment.environments.list";
	    public static final String PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL = "deployment.plateformEnvironment.baseUrl";
	    public static final String PROPERTY_WEBSERVICE_AREAS_JSON_OBJECT_NAME = "deployment.webservice.areas.jsonObjectName";
	    public static final String PROPERTY_WEBSERVICE_AREAS_JSON_DICTIONARY_NAME = "deployment.webservice.areas.jsonDictionaryName";
	    public static final String PROPERTY_WEBSERVICE_ENVIRONMENTS_JSON_OBJECT_NAME = "deployment.webservice.environments.jsonObjectName";
	    public static final String PROPERTY_WEBSERVICE_EVIRONMENTS_JSON_DICTIONARY_NAME = "deployment.webservice.environments.jsonDictionaryName";
	    public static final String PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_OBJECT_NAME = "deployment.webservice.serverApplicationInstances.jsonObjectName";
	    public static final String PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_DICTIONARY_NAME = "deployment.webservice.serverApplicationInstances.jsonDictionaryName";
	    
	    public static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_HOST= "deployment.serverApplicationFtp.host";
	    public static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_PORT= "deployment.serverApplicationFtp.port";
	    public static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_USER_LOGIN= "deployment.serverApplicationFtp.userLogin";
	    public static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_USER_PASSWORD= "deployment.serverApplicationFtp.userPassword";
		public static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_DPLOY_DIRECTORY_TARGET= "deployment.serverApplicationFtp.deployDirectoryTarget";
	    
	    
	    public static final String PROPERTY_SVN_SITES_URL = "deployment.svnSitesUrl";
		public static final String PROPERTY_CHECKOUT_BASE_PAH = "deployment.server.checkout.basePath";
		public static final String PROPERTY_MESSAGE_CHECKOUT_ERROR = "message_checkout_error";
		public static final String PROPERTY_MESSAGE_CHECKOUT_ERROR_SITE_EMPTY = "message_checkout_error_site_empty";
		public static final String PROPERTY_MESSAGE_CHECKOUT_ERROR_LOGIN_MDP_EMPTY ="message_checkout_error_login_mdp_empty";
		public static final String PROPERTY_TASKS_FORM_WORKFLOW_PAGE_TITLE ="deployment.tasks_form_workflow.page_title";
	
		
	  
	    //STATE
	    public static final String CONSTANTE_COMMAND_RESULT_STATUS_EXCEPTION_VALUE="exception";

	    //MARK
	    public static final String MARK_SITE_TAG_VERSION="site_tag_version";
	    public static final String MARK_SITE_TAG_NAME="site_tag_name";
	    public static final String MARK_SITE_NEXT_VERSION="site_next_version";
	    public static final String MARK_APPLICATION_LIST="application_list";
	    public static final String MARK_CATEGORY_LIST="category_list";
	    public static final String MARK_CATEGORY_MAP="category_map";
	    public static final String MARK_CATEGORY_LIST_SITE_MAP="category_list_site_map";
	    public static final String MARK_CATEGORY_SELECTED="category_selected";
	    public static final String MARK_PAGINATOR="paginator";
	    public static final String MARK_APPLICATION="application";
	    public static final String MARK_CODE_CATEGORY="code_category";
	    public static final String MARK_ENVIRONMENT_LIST="environment_list";
	    public static final String MARK_SERVER_INSTANCE_MAP="server_instance_map";
	    public static final String MARK_SITE_LIST="site_list";
	    public static final String MARK_SERVER_INSTANCE="server_instance";
	    public static final String MARK_ENVIRONMENT="environment";
	    public static final String MARK_ACTION_LIST="action_list";
	    public static final String MARK_STATE="state";
	    public static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
	    public static final String MARK_ERROR_MESSAGE = "error_message";
	    public static final String MARK_ID_USER = "id_user";
	    public static final String MARK_TAG_TO_DEPLOY="tag_to_deploy";
	    public static final String MARK_ID_ACTION = "id_action";
	    public static final String MARK_TASKS_FORM = "tasks_form";
	    //PARAM
	    public static final String PARAM_SITE_TAG_VERSION="site_tag_version";
	    public static final String PARAM_SITE_TAG_NAME="site_tag_name";
	    public static final String PARAM_SITE_NEXT_VERSION="site_next_version";
	    public static final String PARAM_CODE_CATEGORY="code_category";
	    public static final String PARAM_CODE_ENVIRONMENT="code_environment";
	    public static final String PARAM_CODE_SERVER_APPLICATION_INSTANCE="code_server_application_instance";
	    public static final String PARAM_TAG_SITE_BEFORE_DEPLOY="tag_site_before_deploy";
	    public static final String PARAM_TAG_TO_DEPLOY="tag_to_deploy";
	    public static final String PARAM_CODE="code";
	    public static final String PARAM_NAME="name";
	    public static final String PARAM_WEBAPP_NAME="webapp_name";
	    public static final String PARAM_SITE="site";
	    public static final String PARAM_ID_APPLICATION="id_application";
	    public static final String PARAM_CANCEL="cancel";
	    public static final String PARAM_ID_ACTION="id_action";
	    public static final String PARAM_ID_WORKFLOW_CONTEXT="id_workflow_context";
	    
	    
	    //ARCHIVE TYPE
	    public static final String ARCHIVE_WAR_EXTENSION=".war";
	    public static final String ARCHIVE_ZIP_EXTENSION=".zip";
	    //TEMPLATE
	    public static final String TEMPLATE_MANAGE_APPLICATION = "admin/plugins/deployment/manage_application.html";
	    public static final String TEMPLATE_CREATE_APPLICATION = "admin/plugins/deployment/create_application.html";
	    public static final String TEMPLATE_MODIFY_APPLICATION = "admin/plugins/deployment/modify_application.html";
	    public static final String TEMPLATE_FORM_INIT_DEPLOY_APPLICATION= "admin/plugins/deployment/form_init_deploy_application.html";
	    public static final String TEMPLATE_DEPLOY_APPLICATION_PROCESS = "admin/plugins/deployment/deploy_application_process.html";
	    public static final String TEMPLATE_TASKS_FORM_WORKFLOW ="admin/plugins/deployment/tasks_form_workflow.html";
	    //JSP
	    public static final String JSP_MANAGE_APPLICATION="jsp/admin/plugins/deployment/ManageApplication.jsp";
	    public static final String JSP_REMOVE_APPLICATION="jsp/admin/plugins/deployment/DoRemoveApplication.jsp";
	    public static final String JSP_TASK_FORM="jsp/admin/plugins/deployment/TasksFormWorkflow.jsp";
	    public static final String JSP_DEPLOY_APPLICATION_PROCESS="jsp/admin/plugins/deployment/DeployApplicationProcess.jsp";
	    //I18nMessage
	    public static final String I18n_MESSAGE_ERROR_MAVEN_USER_IS_NOT_SET="deployment.message.maven_user_is_not_set";
	  
	    
}
