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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;


/**
 * 
 * DatabaseService
 *
 */
public class DatabaseService implements IDatabaseService {

	
	
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.IDatabaseService#getDatabases(java.lang.String, fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance, java.util.Locale)
	 */
	@Override
	public List<String> getDatabases(String strCodeApplication,
			ServerApplicationInstance serverApplicationInstance, Locale locale) {

		String strPlateformEnvironmentBaseUrl = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL);
		List<String> listStrDatabases = new ArrayList<String>();
		
		String strWebserviceDatabasesJsonObjectName = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_WEBSERVICE_DATABASES_JSON_OBJECT_NAME);
		String strWebserviceDatabasesJsonDictionaryName = AppPropertiesService
				.getProperty(ConstanteUtils.PROPERTY_WEBSERVICE_DATABASES_JSON_DICTIONARY_NAME);
		String strJSONServerDatabases = null;

		try {
			strJSONServerDatabases = DeploymentUtils
					.callPlateformEnvironmentWs(strPlateformEnvironmentBaseUrl
							+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
							+ DeploymentUtils.getPlateformUrlDatabases(
									strCodeApplication,
									serverApplicationInstance));
		} catch (Exception e) {
			AppLogService.error(e);
		}

		if (strJSONServerDatabases != null) {
			
			boolean  bExecute=false;
			JSONObject jo = DeploymentUtils.getJSONOBject( strJSONServerDatabases );
			String strWebserviceActionJsonPropery = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_WEBSERVICE_ACTION_RESULT_JSON_PROPERTY_RESULT );
            if ( jo != null )
            {
            	bExecute = jo.getBoolean( strWebserviceActionJsonPropery );
            }
			
            if(bExecute)
            {
				listStrDatabases = DeploymentUtils.getJSONDictionary(
						strWebserviceDatabasesJsonObjectName,
						strWebserviceDatabasesJsonDictionaryName,
						strJSONServerDatabases);
            }

		}

		return listStrDatabases;

	}
	
	


	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.IDatabaseService#getHashDatabases(java.lang.String, java.util.HashMap, java.util.Locale)
	 */
	@Override
	public HashMap<String,List<String>> getHashDatabases(String strCodeApplication,
			HashMap<String, List<ServerApplicationInstance>> hashServerApplicationInstanceMysql, Locale locale) {
		
		HashMap<String,List<String>> hashDatabases=new HashMap<String, List<String>>();
		StringBuffer strKey;
		List<String> lsDatabases;
		for(Entry<String,List<ServerApplicationInstance>> entry: hashServerApplicationInstanceMysql.entrySet())
		{
			
			for(ServerApplicationInstance serverApplicationInstance:entry.getValue())
			{
			
				strKey=new StringBuffer();
				lsDatabases=getDatabases(strCodeApplication, serverApplicationInstance, locale);
				strKey.append(serverApplicationInstance.getCodeEnvironment());
				strKey.append(".");
				strKey.append(serverApplicationInstance.getCode());
				strKey.append("_"+serverApplicationInstance.getType());
				
				hashDatabases.put(strKey.toString(), lsDatabases);
			}
			
		}
		return hashDatabases;
	}
	
	

}
