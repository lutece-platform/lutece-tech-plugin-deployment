package fr.paris.lutece.plugins.deployment.util;

public class ConstanteUtils {
	
	
		public static final int CONSTANTE_ID_NULL=-1; 	
		public static final int CONSTANTE_DEFAULT_LOG_SIZE=500;
		public static final String REGEX_ID = "^[\\d]+$";
		public static final String PROPERTY_SVN_SITES_URL = "svn_sites_url";
		public static final String PROPERTY_CHECKOUT_BASE_PAH = "server_checkout_base_path";
		public static final String PROPERTY_MESSAGE_CHECKOUT_ERROR = "message_checkout_error";
		public static final String PROPERTY_MESSAGE_CHECKOUT_ERROR_SITE_EMPTY = "message_checkout_error_site_empty";
		public static final String PROPERTY_MESSAGE_CHECKOUT_ERROR_LOGIN_MDP_EMPTY ="message_checkout_error_login_mdp_empty";
		public static final String CONSTANTE_TAGS = "tags";
		public static final String CONSTANTE_TRUNK = "trunk";
		public static final String CONSTANTE_CHECKOUT_ERROR = "Checkout error";
		public static final String CONSTANTE_SEPARATOR_SLASH = "/";
		public static final String CONSTANTE_SEPARATOR_POINT = ".";
		public static final String CONSTANTE_EMPTY_STRING = "";
		public static final String CONSTANTE_MARK_STACKTRACE = "stack trace      ";
		public static final String CONSTANTE_SPACE = " ";
		public static final String CONSTANTE_POM_XML = "pom.xml";
		public static final String CONSTANTE_TARGET = "target";
		public static final String CONSTANTE_MAVEN_HOME_PATH="maven_home_path";
		public static final String CONSTANTE_MAVEN_LOCAL_REPOSITORY="maven_local_repository";
		public static final String PROPERTY_THREAD_JOB_TIMER="thread.job_timer";
		public static final String PROPERTY_THREAD_TIMEOUT="thread.timeout";
		public static final String PROPERTY_THREAD_KEEP="thread.keep";
	    public static final String CONSTANTE_SQL_WHERE = " WHERE ";
	    public static final String CONSTANTE_SQL_AND = " AND ";
	    public static final String CONSTANTE_SERVER_TOMCAT="TOMCAT";
	    public static final String CONSTANTE_SERVER_RO="RO";
	    public static final String JSON_STATUS="status";
	    public static final String JSON_LOG="log";
	    public static final String JSON_RUNNING="running";
	    public static final String JSON_ID_ERROR="id_error";
	    public static final String JSON_STATE="state_name";
	    public static final String JSON_ACTION_LIST="action_list";
	    public static final String JSON_ACTION_ID="action_id";
	    public static final String JSON_ACTION_NAME="action_name";
	   
	    //PROPERTY
	    public static final String PROPERTY_NB_ITEM_PER_PAGE="deployment.defaultNbItemPerPage";
	    public static final String PROPERTY_MANAGE_APPLICATION_PAGE_TITLE="deployment.manage_application.page_title";
	    public static final String PROPERTY_CREATE_APPLICATION_PAGE_TITLE="deployment.create_application.page_title";
	    public static final String PROPERTY_MODIFY_APPLICATION_PAGE_TITLE="deployment.create_application.page_title";
	    public static final String PROPERTY_FORM_DEPLOY_SITE_PAGE_TITLE="deployment.form_deploy_site.page_title";
	    public static final String PROPERTY_LABEL_CODE = "deployment.create_application.label_code";
	    public static final String PROPERTY_LABEL_CODE_ENVIRONMENT = "deployment.deploy_application.label_code_environment";
	    public static final String PROPERTY_LABEL_CODE_SERVER_APPLICATION_INSTANCE = "deployment.deploy_application.label_code_server_application_instance";
	    public static final String PROPERTY_LABEL_TAG_TO_DEPLOY = "deployment.deploy_application.label_tag_to_deploy";
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
	    //STATE
	    public static final String CONSTANTE_COMMAND_RESULT_STATUS_EXCEPTION_VALUE="exception";

	    //MARK
	    public static final String MARK_SITE_TAG_VERSION="site_version";
	    public static final String MARK_SITE_TAG_NAME="site_tag_name";
	    public static final String MARK_SITE_NEXT_VERSION="site_next_version";
	    public static final String MARK_APPLICATION_LIST="application_list";
	    public static final String MARK_CATEGORY_LIST="category_list";
	    public static final String MARK_PAGINATOR="paginator";
	    public static final String MARK_APPLICATION="application";
	    public static final String MARK_CODE_CATEGORY="code_category";
	    public static final String MARK_ENVIRONMENT_LIST="environment_list";
	    public static final String MARK_SERVER_INSTANCE_LIST="server_instance_list";
	    public static final String MARK_SITE_LIST="site_list";
	    public static final String MARK_SERVER_INSTANCE="server_instance";
	    public static final String MARK_ENVIRONMENT="environment";
	    public static final String MARK_ACTION_LIST="action_list";
	    public static final String MARK_STATE="state";
	    
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
	    public static final String TEMPLATE_FORM_POST_DEPLOY_SITE = "admin/plugins/deployment/form_post_deploy_site.html";
	    public static final String TEMPLATE_DEPLOY_SITE_PROCESS = "admin/plugins/deployment/deploy_site_process.html";
	    //JSP
	    public static final String JSP_MANAGE_APPLICATION="jsp/admin/plugins/deployment/ManageApplication.jsp";
	    public static final String JSP_REMOVE_APPLICATION="jsp/admin/plugins/deployment/DoRemoveApplication.jsp";
	    public static final String JSP_TASK_FORM="jsp/admin/plugins/deployment/DisplayTaskForm.jsp";
	    
	    
	  
	    
}
