package fr.paris.lutece.plugins.deployment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class EnvironmentService implements IEnvironmentService {

	// private static IEnvironmentService _singleton;
	private static HashMap<String, Environment> _hashEnvironements;

	private EnvironmentService() {

	}

	private void initHashEnvironments() {

		List<Environment> listEnvironment = SpringContextService
				.getBeansOfType(Environment.class);
		_hashEnvironements = new HashMap<String, Environment>();

		if (listEnvironment != null) {
			for (Environment environment : listEnvironment) {
				_hashEnvironements.put(environment.getCode(), environment);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.paris.lutece.plugins.deployment.service.IEnvironmentService#getEnvironment
	 * (java.lang.String)
	 */
	public Environment getEnvironment(String strCode, Locale locale) {

		if (_hashEnvironements == null) {
			initHashEnvironments();
		}
		Environment environment = _hashEnvironements.get(strCode);
		environment.setName(I18nService.getLocalizedString(environment
				.getI18nKeyName(), locale));
		return environment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seefr.paris.lutece.plugins.deployment.service.IEnvironmentService#
	 * getListEnvironments(java.lang.String)
	 */
	public List<Environment> getListEnvironments(String strCodeApplication,
			Locale locale) {
		String strPlateformEnvironmentBaseUrl = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL);
		List<Environment> listEnvironments = new ArrayList<Environment>();
		String strJSONApllicationAreas = null;
		String strJSONEnvironment = null;
		List<String> listAreas;
		List<String> listStrEnvironment = null;
		String strCodeEnvironment;

		String strUrlApllication = strPlateformEnvironmentBaseUrl
				+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
				+ DeploymentUtils
						.getPlateformUrlApplication(strCodeApplication);
		try {
			strJSONApllicationAreas = DeploymentUtils
					.callPlateformEnvironmentWs(strUrlApllication);

		} catch (Exception e) {
			AppLogService.error(e);

		}

		listAreas = getAreas(strJSONApllicationAreas);
		if (listAreas != null && listAreas.size() > 0) {
			for (String strArea : listAreas) {
				strJSONEnvironment = null;
				try {
					strJSONEnvironment = DeploymentUtils
							.callPlateformEnvironmentWs(strPlateformEnvironmentBaseUrl
									+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
									+ DeploymentUtils
											.getPlateformUrlEnvironments(
													strCodeApplication, strArea));
				} catch (Exception e) {
					AppLogService.error(e);

				}
				if (strJSONEnvironment != null) {

					listStrEnvironment = getEnvironments(strJSONEnvironment);
					for (String strEnv : listStrEnvironment) {

						strCodeEnvironment = strArea.toLowerCase()
								+ ConstanteUtils.CONSTANTE_SEPARATOR_POINT
								+ strEnv.toLowerCase();
						Environment environment = getEnvironment(
								strCodeEnvironment, locale);
						listEnvironments.add(environment);
					}
				}

			}

		}
		return listEnvironments;
	}

	private List<String> getEnvironments(String strJsonFlux) {

		String strWebserviceEnvJsonObjectName = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_WEBSERVICE_ENVIRONMENTS_JSON_OBJECT_NAME);
		String strWebserviceEnvJsonDictionaryName = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_WEBSERVICE_EVIRONMENTS_JSON_DICTIONARY_NAME);

		List<String> listEnvs = null;

		if (strJsonFlux != null) {
			listEnvs = DeploymentUtils.getJSONDictionary(
					strWebserviceEnvJsonObjectName,
					strWebserviceEnvJsonDictionaryName, strJsonFlux);
		}
		return listEnvs;
	}

	private List<String> getAreas(String strJsonFlux) {

		String strWebserviceAreasJsonObjectName = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_WEBSERVICE_AREAS_JSON_OBJECT_NAME);
		String strWebserviceAreasJsonDictionaryName = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_WEBSERVICE_AREAS_JSON_DICTIONARY_NAME);

		List<String> listAreas = null;

		if (strJsonFlux != null) {
			listAreas = DeploymentUtils.getJSONDictionary(
					strWebserviceAreasJsonObjectName,
					strWebserviceAreasJsonDictionaryName, strJsonFlux);
		}
		return listAreas;

	}

}
