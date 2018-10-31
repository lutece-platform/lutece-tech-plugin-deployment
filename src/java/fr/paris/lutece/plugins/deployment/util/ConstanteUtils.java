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
package fr.paris.lutece.plugins.deployment.util;

public class ConstanteUtils
{
    // Beans
    public static final String BEAN_SVN_SERVICE = "deployment.SvnService";
    public static final String BEAN_GITHUB_SERVICE = "deployment.GithubService";
    public static final String BEAN_GITLAB_SERVICE = "deployment.GitlabService";

    // Constantes
    public static final int CONSTANTE_ID_NULL = -1;
    public static final String CONSTANTE_ALL = "all";
    public static final int CONSTANTE_DEFAULT_LOG_SIZE = 500;
    public static final String REGEX_ID = "^[\\d]+$";
    public static final String CONSTANTE_TAGS = "tags";
    public static final String CONSTANTE_TRUNK = "trunk";
    public static final String CONSTANTE_CHECKOUT_ERROR = "Checkout error";
    public static final String CONSTANTE_SEPARATOR_SLASH = "/";
    public static final String CONSTANTE_SEPARATOR_POINT = ".";
    public static final String CONSTANTE_SEPARATOR_VIRGULE = ",";
    public static final String CONSTANTE_EMPTY_STRING = "";
    public static final String CONSTANTE_STAR = "*";
    public static final String CONSTANTE_MARK_STACKTRACE = "stack trace      ";
    public static final String CONSTANTE_SPACE = " ";
    public static final String CONSTANTE_POM_XML = "pom.xml";
    public static final String CONSTANTE_TARGET = "target";
    public static final String CONSTANTE_MAVEN_HOME_PATH = "deployment.mavenHomePath";
    public static final String CONSTANTE_MAVEN_LOCAL_REPOSITORY = "deployment.mavenLocalRepository";
    public static final String CONSTANTE__ENVIRONMENT = "deployment.environment.";
    public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE = "deployment.serverApplicationInstance.";
    public static final String CONSTANTE__ENVIRONMENT_CODE = ".code";
    public static final String CONSTANTE__ENVIRONMENT_NAME = ".name";
    public static final String CONSTANTE__ENVIRONMENT_SERVER_APPLICATION_INSTANCE_LIST = ".serverApplicationInstanceList";
    public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_CODE = ".code";
    public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_NAME = ".name";
    public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_SERVER_NAME = ".serverName";
    public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_FTP_WEBAPP_Url = ".ftpWebAppUrl";
    public static final String CONSTANTE__SERVER_APPLICATION_INSTANCE_MAVEN_PROFILE = ".mavenProfile";
    public static final String CONSTANTE_SQL_WHERE = " WHERE ";
    public static final String CONSTANTE_SQL_AND = " AND ";
    public static final String CONSTANTE_SERVER_TOMCAT = "TOMCAT";
    public static final String CONSTANTE_SERVER_TOM = "TOM";
    public static final String CONSTANTE_SERVER_MYSQL = "MYSQL";
    public static final String CONSTANTE_SERVER_MYS = "MYS";
    public static final String CONSTANTE_SERVER_HTTPD = "HTTPD";
    public static final String CONSTANTE_SERVER_PSQ = "PSQ";
    public static final String CONSTANTE_REPO_TYPE_SVN = "svn";
    public static final String CONSTANTE_REPO_TYPE_GITHUB = "github";
    public static final String CONSTANTE_REPO_TYPE_GITLAB = "gitlab";
    public static final String CONSTANTE_BRANCH_DEVELOP = "develop";
    public static final String CONSTANTE_BRANCH_MASTER = "master";
    public static final String CONSTANT_MAVEN_BASE_CMD = "mvn ";

    public static final String CONSTANTE_SERVER_RO = "RO";
    public static final String CONSTANTE_MAX_DEPLOY_SITE_CONTEXT_KEY = "max_deploy_site_context_key";
    public static final String CONSTANTE_ACTION_EXECUTE = "@EXECUTE";
    public static final String JSON_STATUS = "status";
    public static final String JSON_ERROR_TYPE = "error_type";
    public static final String JSON_LOG = "log";
    public static final String JSON_RUNNING = "running";
    public static final String JSON_ERROR = "error";
    public static final String JSON_STATE = "state";
    public static final String JSON_RESULT = "result";
    public static final String JSON_ACTION_LIST = "action_list";
    public static final String JSON_ACTION_ID = "id";
    public static final String JSON_ACTION_CODE = "action_code";
    public static final String JSON_ACTION_ICON_CSS_CLASS = "icon_css_class";
    public static final String JSON_ACTION_NAME = "name";
    public static final String JSON_ACTION_STATUS = "status";
    public static final String JSON_ACTION_DISPLAY = "display";
    public static final String JSON_ACTION_DESCRIPTION = "description";

    public static final String JSON_JSP_FOM_DISPLAY = "jsp_form_display";
    public static final String JSON_FORM_ERROR = "form_error";
    public static final String JSON_SERVER_STATUS = "server_status";
    public static final String JSON_ID_APPLIACTION = "id_application";
    public static final String JSON_CODE_ENVIRONMENT = "code_environment";
    public static final String JSON_CODE_SERVER_APPLICATION_INSTANCE = "code_server_application_instance";
    public static final String JSON_SERVER_APPLICATION_TYPE = "server_application_type";
    public static final String CONTEXT_DIRECTORY_NAME = "CONTEXT";

    // PROPERTY
    public static final String PROPERTY_NB_ITEM_PER_PAGE = "deployment.defaultNbItemPerPage";
    public static final String PROPERTY_MANAGE_APPLICATION_PAGE_TITLE = "deployment.manage_application.page_title";
    public static final String PROPERTY_CREATE_APPLICATION_PAGE_TITLE = "deployment.create_application.page_title";
    public static final String PROPERTY_MODIFY_APPLICATION_PAGE_TITLE = "deployment.create_application.page_title";
    public static final String PROPERTY_FORM_DEPLOY_SITE_PAGE_TITLE = "deployment.form_deploy_site.page_title";
    public static final String PROPERTY_LABEL_CODE = "deployment.create_application.label_code";
    public static final String PROPERTY_LABEL_CODE_ENVIRONMENT = "deployment.form_init_deploy_application.label_code_environment";
    public static final String PROPERTY_LABEL_CODE_SERVER_APPLICATION_INSTANCE_TOMCAT = "deployment.form_init_deploy_application.label_code_server_application_instance_tomcat";
    public static final String PROPERTY_LABEL_CODE_SERVER_APPLICATION_INSTANCE_MYSQL = "deployment.form_init_deploy_application.label_code_server_application_instance_mysql";
    public static final String PROPERTY_LABEL_CODE_DATABASE = "deployment.form_init_deploy_application.label_code_database";
    public static final String PROPERTY_LABEL_TAG_TO_DEPLOY = "deployment.form_init_deploy_application.label_tag_to_deploy";
        public static final String PROPERTY_LABEL_CUSTOM_MAVEN_GOAL = "deployment.form_init_deploy_application.label_custom_maven_goal";
    public static final String PROPERTY_LABEL_NAME = "deployment.create_application.label_name";
    public static final String PROPERTY_LABEL_WEBAPP_NAME = "deployment.create_application.label_webapp_name";
    public static final String PROPERTY_LABEL_SITE = "deployment.create_application.label_site";
    public static final String PROPERTY_LABEL_LUTECE_SITE = "deployment.create_application.label_lutece_site";
    public static final String PROPERTY_LABEL_URL_REPO = "deployment.create_application.label_url_repo";
    public static final String PROPERTY_LABEL_SCRIPT_UPLOAD = "deployment.form_init_deploy_application.label_script_upload";
    public static final String PROPERTY_MESSAGE_MANDATORY_FIELD = "deployment.message.mandatory_field";
    public static final String PROPERTY_MESSAGE_INVALID_REPO_URL = "deployment.message.invalidRepoUrl";
    public static final String PROPERTY_MESSAGE_CONFIRM_REMOVE_APPLICATION = "deployment.message.confirm_remove_application";
    public static final String PROPERTY_ADMINUSER_ID_ATTRIBUTE_SVN_LOGIN = "deployment.adminUser.idAttribute.svnLogin";
    public static final String PROPERTY_ADMINUSER_ID_ATTRIBUTE_SVN_PASSWORD = "deployment.adminUser.idAttribute.svnPassword";
    public static final String PROPERTY_SVN_USED_DEPLOYMENT_ACCOUNT = "deployment.svn.usedDeploymentAccount";
    public static final String PROPERTY_SVN_LOGIN_APPLICATION_DEPLOYMENT = "deployment.svn.svnLoginDeployment";
    public static final String PROPERTY_SVN_PASSWORD_APPLICATION_DEPLOYMENT = "deployment.svn.svnPasswordDeployment";

    public static final String PROPERTY_ID_WORKFLOW_TAG_AND_DEPLOY_SITE = "deployment.idWorkflowTagAndDeploySite";
    public static final String PROPERTY_ID_WORKFLOW_TAG_AUTOMATICALLY_AND_DEPLOY_SITE = "deployment.idWorkflowTagAutomaticallyAndDeploySite";

    public static final String PROPERTY_ID_WORKFLOW_DEPLOY_SITE = "deployment.idWorkflowDeploySite";
    public static final String PROPERTY_ID_WORKFLOW_DEPLOY_NON_LUTECE = "deployment.idWorkflowDeploySiteNonLutece";
    public static final String PROPERTY_ID_WORKFLOW_TAG_SITE = "deployment.idWorkflowTagSite";
    public static final String PROPERTY_ID_WORKFLOW_DEPLOY_SCRIPT = "deployment.idWorkflowDeployScript";
    public static final String PROPERTY_ID_WORKFLOW_INIT_APP_CONTEXT = "deployment.idWorkflowInitAppContext";
    public static final String PROPERTY_ID_WORKFLOW_INIT_DATABASE = "deployment.idWorkflowInitDatabase";

    public static final String PROPERTY_DEPLOY_SITE_PAGE_TITLE = "deployment.deploy_site.page_title";
    public static final String PROPERTY_MAX_LOG_SIZE = "deployment.maxLogSize";
    public static final String PROPERTY_ENVIRONMENTS_LIST = "deployment.environments.list";
    public static final String PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL = "deployment.plateformEnvironment.baseUrl";
    public static final String PROPERTY_WEBSERVICE_AREAS_JSON_OBJECT_NAME = "deployment.webservice.areas.jsonObjectName";
    public static final String PROPERTY_WEBSERVICE_AREAS_JSON_DICTIONARY_NAME = "deployment.webservice.areas.jsonDictionaryName";
    public static final String PROPERTY_WEBSERVICE_ENVIRONMENTS_JSON_OBJECT_NAME = "deployment.webservice.environments.jsonObjectName";
    public static final String PROPERTY_WEBSERVICE_EVIRONMENTS_JSON_DICTIONARY_NAME = "deployment.webservice.environments.jsonDictionaryName";
    public static final String PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_OBJECT_NAME = "deployment.webservice.serverApplicationInstances.jsonObjectName";
    public static final String PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_DICTIONARY_NAME = "deployment.webservice.serverApplicationInstances.jsonDictionaryName";
    public static final String PROPERTY_WEBSERVICE_SERVER_ACTIONS_JSON_DICTIONARY_NAME = "deployment.webservice.serverActions.jsonDictionaryName";
    public static final String PROPERTY_WEBSERVICE_ACTION_RESULT_JSON_PROPERTY_RESULT = "deployment.webservices.serverAction.jsonPropertyResult";
    public static final String PROPERTY_WEBSERVICE_INSTALLABLE_WAR_ACTION_OBJECT_NAME = "deployment.webservices.installableWarAction.jsonObjectName";
    public static final String PROPERTY_WEBSERVICE_INSTALLABLE_WAR_ACTION_DICTIONARY_NAME = "deployment.webservice.installableWarAction.jsonDictionaryName";
    public static final String PROPERTY_WEBSERVICE_DATABASES_JSON_OBJECT_NAME = "deployment.webservice.databases.jsonObjectName";
    public static final String PROPERTY_WEBSERVICE_DATABASES_JSON_DICTIONARY_NAME = "deployment.webservice.databases.jsonDictionaryName";
    public static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_HOST = "deployment.serverApplicationFtp.host";
    public static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_PORT = "deployment.serverApplicationFtp.port";
    public static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_USER_LOGIN = "deployment.serverApplicationFtp.userLogin";
    public static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_USER_PASSWORD = "deployment.serverApplicationFtp.userPassword";
    public static final String PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_DPLOY_DIRECTORY_TARGET = "deployment.serverApplicationFtp.deployDirectoryTarget";
    public static final String PROPERTY_SVN_SITES_URL = "deployment.svnSitesUrl";
    public static final String PROPERTY_GITHUB_BASE_URL = "deployment.githubBaseUrl";
    public static final String PROPERTY_GITHUB_AUTHORIZED_REPO_NAME = "deployment.github.authorizedRepositoryName";
    public static final String PROPERTY_GITLAB_BASE_URL = "deployment.gitlabSitesUrl";
    public static final String PROPERTY_CHECKOUT_BASE_PAH = "deployment.server.checkout.basePath";
    public static final String PROPERTY_MESSAGE_CHECKOUT_ERROR = "message_checkout_error";
    public static final String PROPERTY_MESSAGE_CHECKOUT_ERROR_SITE_EMPTY = "message_checkout_error_site_empty";
    public static final String PROPERTY_MESSAGE_CHECKOUT_ERROR_LOGIN_MDP_EMPTY = "message_checkout_error_login_mdp_empty";
    public static final String PROPERTY_MESSAGE_REPO_UNAUTHORIZED_ACCESS = "message_unauthorized_repo_access";
    public static final String PROPERTY_TASKS_FORM_WORKFLOW_PAGE_TITLE = "deployment.tasks_form_workflow.page_title";
    public static final String PROPERTY_FORM_ACTION_SERVER_PAGE_TITLE = "deployment.form_action_server.page_title";

    public static final String PROPERTY_SERVER_TYPE_TOMCAT_LABEL = "deployment.server_type_tomcat_label";
    public static final String PROPERTY_SERVER_TYPE_HTTPD_LABEL = "deployment.server_type_httpd_label";
    public static final String PROPERTY_SERVER_TYPE_MYSQL_LABEL = "deployment.server_type_mysql_label";
    public static final String PROPERTY_UPGRADE_DIRECTORY_PATH = "deployment.server.upgradeDirectoryPath";;
    // STATE
    public static final String CONSTANTE_COMMAND_RESULT_STATUS_EXCEPTION_VALUE = "exception";

    // STATUS SERVER APLLICATION STATE
    public static final Integer STATUS_KO = 0;
    public static final Integer STATUS_OK = 1;

    // MARK
    public static final String MARK_SITE_TAG_VERSION = "site_tag_version";
    public static final String MARK_SITE_TAG_NAME = "site_tag_name";
    public static final String MARK_SITE_NEXT_VERSION = "site_next_version";
    public static final String MARK_APPLICATION_LIST = "application_list";
    public static final String MARK_SCRIPT_LIST = "script_list";
    public static final String MARK_PAGINATOR = "paginator";
    public static final String MARK_APPLICATION = "application";
    public static final String MARK_ENVIRONMENT_LIST = "environment_list";
    public static final String MARK_SERVER_INSTANCE_MAP_TOMCAT = "server_instance_map_tomcat";
    public static final String MARK_SERVER_INSTANCE_MAP_SQL = "server_instance_map_sql";
    public static final String MARK_SERVER_INSTANCE_MAP_HTTPD = "server_instance_map_httpd";
    public static final String MARK_DATABASE_MAP = "database_map";
    public static final String MARK_SITE_LIST = "site_list";
    public static final String MARK_SERVER_INSTANCE = "server_instance";
    public static final String MARK_ENVIRONMENT = "environment";
    public static final String MARK_ACTION_LIST = "action_list";
    public static final String MARK_SERVER_TYPE_LIST = "server_type_list";
    public static final String MARK_STATE = "state";
    public static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    public static final String MARK_ERROR_MESSAGE = "error_message";
    public static final String MARK_ID_USER = "id_user";
    public static final String MARK_TAG_TO_DEPLOY = "tag_to_deploy";
    public static final String MARK_ID_ACTION = "id_action";
    public static final String MARK_TASKS_FORM = "tasks_form";
    public static final String MARK_FORM_ACTION = "form_action";
    public static final String MARK_CONFIG = "config";
    public static final String MARK_DEPLOY_WAR = "deploy_war";
    public static final String MARK_DEPLOY_SQL = "deploy_sql";
    public static final String MARK_DEPLOY_NON_LUTECE = "deploy_site_non_lutece";
    public static final String MARK_INIT_DATABASE = "init_database";
    public static final String MARK_INIT_APP_CONTEXT = "init_app_context";
    public static final String MARK_CAN_CREATE_APPLICATION = "can_create_application";
    public static final String MARK_HANDLER = "handler";
    public static final String MARK_SEARCH_NAME = "search_name";
    public static final String MARK_SCRIPT_NAME = "script_name";
    public static final String MARK_DUMP_FILE_URL = "dump_file_url";
    public static final String MARK_DATABASE_LIST = "database_list";
    public static final String MARK_USER_WORKGROUP_REF_LIST = "user_workgroup_list";
    public static final String MARK_USER_WORKGROUP_SELECTED = "user_workgroup_selected";
    public static final String MARK_UPGRADE_FILE_REF_LIST = "upgrade_file_list";
    public static final String MARK_MANAGE_APPLICATION_ACTIONS = "map_application_actions";
    public static final String MARK_VCS_SERVICE = "vcs_service";
    public static final String MARK_ACTION_URL = "action_url";
    public static final String MARK_USER = "user";

    // PARAM
    public static final String PARAM_SITE_TAG_VERSION = "site_tag_version";
    public static final String PARAM_SITE_TAG_NAME = "site_tag_name";
    public static final String PARAM_SITE_NEXT_VERSION = "site_next_version";
    public static final String PARAM_CODE_ENVIRONMENT = "code_environment";
    public static final String PARAM_CODE_SERVER_APPLICATION_INSTANCE_TOMCAT = "code_server_application_instance_tomcat";
    public static final String PARAM_CODE_SERVER_APPLICATION_INSTANCE_SQL = "code_server_application_instance_sql";
    public static final String PARAM_CODE_SERVER_APPLICATION_INSTANCE = "code_server_application_instance";
    public static final String PARAM_SERVER_APPLICATION_TYPE = "server_application_type";
    public static final String PARAM_TAG_TO_DEPLOY = "tag_to_deploy";
    public static final String PARAM_DEPLOY_DEV_SITE = "deploy_dev_site";
    public static final String PARAM_CODE = "code";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_WEBAPP_NAME = "webapp_name";
    public static final String PARAM_LUTECE_SITE = "is_lutece_site";
    public static final String PARAM_CUSTOM_MAVEN_DEPLOY_GOAL = "maven_custom_deploy_goal";
    public static final String PARAM_SITE = "site";
    public static final String PARAM_ID_APPLICATION = "id_application";
    public static final String PARAM_MAVEN_PROFIL = "maven_profil";
    public static final String PARAM_CANCEL = "cancel";
    public static final String PARAM_ID_ACTION = "id_action";
    public static final String PARAM_ACTION_CODE = "action_code";
    public static final String PARAM_ID_WORKFLOW_CONTEXT = "id_workflow_context";
    public static final String PARAM_KEY_ACTION_DEPLOYMENT = "key_action";
    public static final String PARAM_DEPLOY_WAR = "deploy_war";
    public static final String PARAM_DEPLOY_SQL = "deploy_sql";
    public static final String PARAM_DEPLOY_NON_LUTECE = "deploy_site_non_lutece";
    public static final String PARAM_SCRIPT = "script";
    public static final String PARAM_CODE_DATABASE = "code_database";
    public static final String PARAM_SCRIPT_UPLOAD = "script_upload";
    public static final String PARAM_SCRIPT_UPGRADE_SELECTED = "script_upgrade_selected";
    public static final String PARAM_INIT_DATABASE = "init_database";
    public static final String PARAM_INIT_APP_CONTEXT = "init_app_context";
    public static final String PARAM_SEARCH_NAME = "search_name";
    public static final String PARAM_URL_REPO = "url_repo";
    public static final String PARAM_LOGIN = "login";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_ACTION_URL = "action_url";

    public static final String PARAM_SCRIPT_NAME = "script_name";
    public static final String PARAM_CODE_APPLICATION = "code_application";
    public static final String PARAM_WORKGROUP = "workgroup";
    public static final String PARAM_CUSTOM_MAVEN_GOAL = "maven_custom_goal";

    // ARCHIVE TYPE
    public static final String ARCHIVE_WAR_EXTENSION = ".war";
    public static final String ARCHIVE_ZIP_EXTENSION = ".zip";

    // TEMPLATE
    public static final String TEMPLATE_MANAGE_APPLICATION = "admin/plugins/deployment/manage_application.html";
    public static final String TEMPLATE_CREATE_APPLICATION = "admin/plugins/deployment/create_application.html";
    public static final String TEMPLATE_MODIFY_APPLICATION = "admin/plugins/deployment/modify_application.html";
    public static final String TEMPLATE_VIEW_APPLICATION = "admin/plugins/deployment/view_application.html";
    public static final String TEMPLATE_FORM_INIT_DEPLOY_APPLICATION = "admin/plugins/deployment/form_init_deploy_application.html";
    public static final String TEMPLATE_DEPLOY_APPLICATION_PROCESS = "admin/plugins/deployment/deploy_application_process.html";
    public static final String TEMPLATE_TASKS_FORM_WORKFLOW = "admin/plugins/deployment/tasks_form_workflow.html";
    public static final String TEMPLATE_FORM_ACTION_SERVER = "admin/plugins/deployment/form_action_server.html";
    public static final String TEMPLATE_FORM_ACTION_DUMP = "admin/plugins/deployment/form_action_dump.html";
    public static final String TEMPLATE_INIT_DB = "admin/plugins/deployment/init/init_db_template.html";
    public static final String TEMPLATE_INIT_APP_CONTEXT = "admin/plugins/deployment/init/init_app_context.html";
    public static final String TEMPLATE_ASK_CREDENTIALS = "admin/plugins/deployment/ask_credentials.html";

    // JSP
    public static final String JSP_MANAGE_APPLICATION = "jsp/admin/plugins/deployment/ManageApplication.jsp";
    public static final String JSP_VIEW_APPLICATION = "jsp/admin/plugins/deployment/ViewApplication.jsp";
    public static final String JSP_REMOVE_APPLICATION = "jsp/admin/plugins/deployment/DoRemoveApplication.jsp";
    public static final String JSP_TASK_FORM = "jsp/admin/plugins/deployment/TasksFormWorkflow.jsp";
    public static final String JSP_FORM_SERVER_ACTION = "jsp/admin/plugins/deployment/FormServerAction.jsp";

    public static final String JSP_DEPLOY_APPLICATION_PROCESS = "jsp/admin/plugins/deployment/DeployApplicationProcess.jsp";

    // I18nMessage
    public static final String I18n_MESSAGE_ERROR_MAVEN_USER_IS_NOT_SET = "deployment.message.maven_user_is_not_set";

    // Regex
    public static final String REGEX_GIT_EXTRACT_ARTIFACT_FROM_URL = "deployment.regex.git.artifactId.extractFromUrl";

    // Attributes
    public static final String ATTRIBUTE_VCS_USER = "vcs_user"; 
}
