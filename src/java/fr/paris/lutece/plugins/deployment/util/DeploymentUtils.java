package fr.paris.lutece.plugins.deployment.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.FilterDeployment;
import fr.paris.lutece.plugins.deployment.business.MavenUser;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;
import fr.paris.lutece.plugins.deployment.business.WorkflowDeploySiteContext;
import fr.paris.lutece.plugins.deployment.service.ISvnService;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.user.attribute.AdminUserField;
import fr.paris.lutece.portal.service.user.attribute.AdminUserFieldService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

public class DeploymentUtils {

	/**
	 * Builds a query with filters placed in parameters. Consider using
	 * {@link #buildQueryWithFilter(StringBuilder, List, String)} instead.
	 * 
	 * @param strSelect
	 *            the select of the query
	 * @param listStrFilter
	 *            the list of filter to add in the query
	 * @param strOrder
	 *            the order by of the query
	 * @return a query
	 */
	public static String buildRequetteWithFilter(String strSelect,
			List<String> listStrFilter, String strOrder) {
		return buildQueryWithFilter(new StringBuilder(strSelect),
				listStrFilter, strOrder);
	}

	/**
	 * Builds a query with filters placed in parameters
	 * 
	 * @param sbSQL
	 *            the beginning of the query
	 * @param listFilter
	 *            the list of filter to add in the query
	 * @param strOrder
	 *            the order by of the query
	 * @return a query
	 */
	public static String buildQueryWithFilter(StringBuilder sbSQL,
			List<String> listFilter, String strOrder) {
		int nCount = 0;

		for (String strFilter : listFilter) {
			if (++nCount == 1) {
				sbSQL.append(ConstanteUtils.CONSTANTE_SQL_WHERE);
			}

			sbSQL.append(strFilter);

			if (nCount != listFilter.size()) {
				sbSQL.append(ConstanteUtils.CONSTANTE_SQL_AND);
			}
		}

		if (strOrder != null) {
			sbSQL.append(strOrder);
		}

		return sbSQL.toString();
	}

	/**
	 * This method calls Rest WS archive
	 * 
	 * @param strUrl
	 *            the url
	 * @param params
	 *            the params to pass in the post
	 * @param listElements
	 *            the list of elements to include in the signature
	 * @return the response as a string
	 * @throws HttpAccessException
	 *             the exception if there is a problem
	 */
	public static String callPlateformEnvironmentWs(String strUrl)
			throws HttpAccessException {
		String strResponse = StringUtils.EMPTY;

		try {
			HttpAccess httpAccess = new HttpAccess();
			strResponse = httpAccess.doGet(strUrl);
		} catch (HttpAccessException e) {
			String strError = "ArchiveWebServices - Error connecting to '"
					+ strUrl + "' : ";
			AppLogService.error(strError + e.getMessage(), e);
			throw new HttpAccessException(strError, e);
		}

		return strResponse;
	}

	public static List<String> getJSONDictionary(String objectName,
			String dictionaryName, String strJSONFlux) {
		List<String> jsonCollection = new ArrayList<String>();
		JSONObject jo = (JSONObject) JSONSerializer.toJSON(strJSONFlux);
		JSONArray jsonArray = jo.getJSONObject(objectName).getJSONArray(
				dictionaryName);
		Iterator iterator = jsonArray.iterator();
		while (iterator.hasNext()) {
			jsonCollection.add((String) iterator.next());
		}
		return jsonCollection;
	}

	/**
	 * Retourne l'emplacement du pom
	 * 
	 * @param strBasePath
	 * @param strPluginName
	 * @return
	 */
	public static String getPathPomFile(String strPathSite) {
		return strPathSite + File.separator + ConstanteUtils.CONSTANTE_POM_XML;
	}

	/**
	 * Retourne l'emplacement du pom
	 * 
	 * @param strBasePath
	 * @param strPluginName
	 * @return
	 */
	public static String getPathArchiveGenerated(String strPathSite,
			String strWarName, String strExtension) {
		return strPathSite + File.separator + ConstanteUtils.CONSTANTE_TARGET
				+ File.separator + strWarName + strExtension;
	}

	/**
	 * Retourne l'emplacement du pom
	 * 
	 * @param strBasePath
	 * @param strPluginName
	 * @return
	 */
	public static String getPathCheckoutSite(String strSiteName) {
		String strCheckoutBasePath = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_CHECKOUT_BASE_PAH);
		return strCheckoutBasePath + File.separator + strSiteName;
	}

	public static String getPlateformUrlApplication(String strCodeApplication) {
		// String strPlateformEnvironmentBaseUrl=
		// AppPropertiesService.getProperty
		// (PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL);
		return strCodeApplication;

	}

	public static String getPlateformUrlEnvironments(String strCodeApplication,
			String strArea) {

		return getPlateformUrlApplication(strCodeApplication)
				+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + strArea;

	}

	public static String getPlateformUrlServerApplicationInstances(
			String strCodeApplication, String strCodeEnvironment,
			String strServerApplicationType) {
		String strPathEnvironment = (strCodeEnvironment.replace(
				ConstanteUtils.CONSTANTE_SEPARATOR_POINT,
				ConstanteUtils.CONSTANTE_SEPARATOR_SLASH)).toUpperCase();
		return getPlateformUrlApplication(strCodeApplication)
				+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + strPathEnvironment
				+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
				+ strServerApplicationType;

	}

	public static String getDeployDirectoryTarget(String strCodeApplication,
			ServerApplicationInstance serverApplicationInstance) {
		return getPlateformUrlServerApplicationInstances(strCodeApplication,
				serverApplicationInstance.getCodeEnvironment(),
				serverApplicationInstance.getType())
				+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
				+ serverApplicationInstance.getCode()
				+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
				+ serverApplicationInstance.getFtpDirectoryTarget();
	}

	/**
	 * convert a string to int
	 * 
	 * @param strParameter
	 *            the string parameter to convert
	 * @return the conversion
	 */
	public static int getIntegerParameter(String strParameter) {
		int nIdParameter = -1;

		try {
			if ((strParameter != null)
					&& strParameter.matches(ConstanteUtils.REGEX_ID)) {
				nIdParameter = Integer.parseInt(strParameter);
			}
		} catch (NumberFormatException ne) {
			AppLogService.error(ne);
		}

		return nIdParameter;
	}

	public static MavenUser getMavenUser(int nIdAdminUser, Locale locale) {

		String strIdAttributeLogin = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_ADMINUSER_ID_ATTRIBUTE_SVN_LOGIN);
		String strIdAttributePasssword = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_ADMINUSER_ID_ATTRIBUTE_SVN_PASSWORD);
		String strLoginValue = null;
		String strPasswordValue = null;
		MavenUser mavenUser = null;
		Map<String, Object> mapAttributeUser = AdminUserFieldService
				.getAdminUserFields(nIdAdminUser, locale);
		if (mapAttributeUser.containsKey(strIdAttributeLogin)
				&& mapAttributeUser.containsKey(strIdAttributePasssword)) {
			strLoginValue = ((ArrayList<AdminUserField>) mapAttributeUser
					.get(strIdAttributeLogin)).get(0).getValue();
			strPasswordValue = ((ArrayList<AdminUserField>) mapAttributeUser
					.get(strIdAttributePasssword)).get(0).getValue();
			if (!StringUtils.isEmpty(strLoginValue)
					&& !(StringUtils.isEmpty(strPasswordValue))) {
				mavenUser = new MavenUser();
				mavenUser.setLogin(strLoginValue);
				mavenUser.setPaswword(strPasswordValue);
			}
		}

		return mavenUser;
	}

	public static JSONObject getJSONForCommandResult(CommandResult result) {
		JSONObject jo = new JSONObject();
		try {
			jo.put(ConstanteUtils.JSON_STATUS, result.getStatus());
			// pour les logs tr�s longs, on ne prend que la fin

			StringBuffer sbLog = result.getLog();
			int nMaxLogSize = AppPropertiesService.getPropertyInt(
					ConstanteUtils.PROPERTY_MAX_LOG_SIZE,
					ConstanteUtils.CONSTANTE_ID_NULL);
			String strLog;
			if (nMaxLogSize == -ConstanteUtils.CONSTANTE_ID_NULL) {
				nMaxLogSize = ConstanteUtils.CONSTANTE_DEFAULT_LOG_SIZE;
			}
			// sbLog null entre le lancement du thread et la premiere requete
			if (sbLog != null) {
				if (sbLog.length() > nMaxLogSize) {
					strLog = sbLog.substring(sbLog.length() - nMaxLogSize);
				} else {
					strLog = sbLog.toString();
				}
			} else {
				strLog = ConstanteUtils.CONSTANTE_EMPTY_STRING;
			}
			jo.put(ConstanteUtils.JSON_LOG, strLog);
			jo.put(ConstanteUtils.JSON_RUNNING, result.isRunning());
			jo.put(ConstanteUtils.JSON_ID_ERROR, result.getIdError());
		} catch (JSONException e) {
			AppLogService.error("JSON error : " + e.getMessage());
		}

		return jo;
	}

	public static JSONObject getJSONForWorkflowAction(
			String strJspForceRedirect, String strFormError,
			CommandResult result, State state, Collection<Action> listAction) {
		JSONObject jo = new JSONObject();
		try {

			jo.put(ConstanteUtils.JSON_JSP_FOM_DISPLAY,
					strJspForceRedirect != null ? strJspForceRedirect
							: ConstanteUtils.CONSTANTE_EMPTY_STRING);

			jo.put(ConstanteUtils.JSON_STATE, state != null ? state.getName()
					: ConstanteUtils.CONSTANTE_EMPTY_STRING);
			jo.put(ConstanteUtils.JSON_FORM_ERROR,
					strFormError != null ? strFormError
							: ConstanteUtils.CONSTANTE_EMPTY_STRING);
			JSONObject joAction;
			JSONArray joListAction = new JSONArray();
			if (listAction != null) {

				for (Action action : listAction) {
					joAction = new JSONObject();
					joAction.put(ConstanteUtils.JSON_ACTION_ID, action.getId());
					joAction.put(ConstanteUtils.JSON_ACTION_NAME, action
							.getName());
					joAction.put(ConstanteUtils.JSON_ACTION_DESCRIPTION, action
							.getDescription());

					joListAction.add(joAction);
				}
			}
			jo.put(ConstanteUtils.JSON_ACTION_LIST, joListAction);

			jo.put(ConstanteUtils.JSON_RUNNING, result != null ? result
					.isRunning() : ConstanteUtils.CONSTANTE_EMPTY_STRING);
			jo.put(ConstanteUtils.JSON_ID_ERROR, result != null ? result
					.getIdError() : ConstanteUtils.CONSTANTE_EMPTY_STRING);

		} catch (JSONException e) {
			AppLogService.error("JSON error : " + e.getMessage());
		}

		return jo;
	}

	public static int getIdWorkflowSiteDeploy(boolean bTagSiteBeforDeploy) {

		int nIdWorkflow = ConstanteUtils.CONSTANTE_ID_NULL;
		if (bTagSiteBeforDeploy) {

			nIdWorkflow = AppPropertiesService.getPropertyInt(
					ConstanteUtils.PROPERTY_ID_WORKFLOW_TAG_AND_DEPLOY_SITE,
					ConstanteUtils.CONSTANTE_ID_NULL);

		} else {
			nIdWorkflow = AppPropertiesService.getPropertyInt(
					ConstanteUtils.PROPERTY_ID_WORKFLOW_DEPLOY_SITE,
					ConstanteUtils.CONSTANTE_ID_NULL);
		}

		return nIdWorkflow;

	}

	public static HashMap<String, ReferenceList> getHashCategoryListSite(
			ReferenceList categoryRefList, ISvnService svnService,
			MavenUser mavenUser) {
		HashMap<String, ReferenceList> hashCategoryListSite = new HashMap<String, ReferenceList>();
		FilterDeployment filter = new FilterDeployment();
		for (ReferenceItem item : categoryRefList) {
			filter.setCodeCategory(item.getCode());

			hashCategoryListSite.put(item.getCode(), svnService.getSites(
					filter, mavenUser));
		}
		return hashCategoryListSite;

	}

	public static void startCommandResult(WorkflowDeploySiteContext context) {
		CommandResult commandResult = new CommandResult();
		commandResult.setLog(new StringBuffer());
		context.setCommandResult(commandResult);
		commandResult.setRunning(true);

	}
	
	public static void stopCommandResult(WorkflowDeploySiteContext context) {
	
		context.getCommandResult().setRunning(false);

	}

	public static void addEmptyRefenceItem(ReferenceList referenceList) {
		if (referenceList != null) {

			ReferenceItem referenceItem = new ReferenceItem();
			referenceItem.setCode(ConstanteUtils.CONSTANTE_EMPTY_STRING);
			referenceItem.setName(ConstanteUtils.CONSTANTE_EMPTY_STRING);
			referenceList.add(0, referenceItem);
		}
	}

}
