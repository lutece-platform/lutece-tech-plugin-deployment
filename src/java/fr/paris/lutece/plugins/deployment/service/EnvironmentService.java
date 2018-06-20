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

import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class EnvironmentService implements IEnvironmentService
{
    // private static IEnvironmentService _singleton;
    private static HashMap<String, Environment> _hashEnvironements;

    private EnvironmentService( )
    {
    }

    private void initHashEnvironments( )
    {
        List<Environment> listEnvironment = SpringContextService.getBeansOfType( Environment.class );
        _hashEnvironements = new HashMap<String, Environment>( );

        if ( listEnvironment != null )
        {
            for ( Environment environment : listEnvironment )
            {
                _hashEnvironements.put( environment.getCode( ), environment );
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.deployment.service.IEnvironmentService#getEnvironment (java.lang.String)
     */
    public Environment getEnvironment( String strCode, Locale locale )
    {
        if ( _hashEnvironements == null )
        {
            initHashEnvironments( );
        }

        Environment environment = _hashEnvironements.get( strCode );
        if ( environment != null )
        {
            environment.setName( I18nService.getLocalizedString( environment.getI18nKeyName( ), locale ) );
        }
        return environment;
    }

    public ReferenceList getEnvironmentRefList( Locale locale )
    {

        ReferenceList refListEnv = new ReferenceList( );
        if ( _hashEnvironements == null )
        {
            initHashEnvironments( );
        }

        for ( Environment environment : _hashEnvironements.values( ) )
        {
            refListEnv.addItem( environment.getCode( ), I18nService.getLocalizedString( environment.getI18nKeyName( ), locale ) );
        }
        return refListEnv;
    }

    /*
     * (non-Javadoc)
     * 
     * @seefr.paris.lutece.plugins.deployment.service.IEnvironmentService# getListEnvironments(java.lang.String)
     */
    public List<Environment> getListEnvironments( String strCodeApplication, Locale locale )
    {
        String strPlateformEnvironmentBaseUrl = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL );
        List<Environment> listEnvironments = new ArrayList<Environment>( );
        String strJSONApllicationAreas = null;
        String strJSONEnvironment = null;
        List<String> listAreas;
        List<String> listStrEnvironment = null;
        String strCodeEnvironment;

        String strUrlApllication = strPlateformEnvironmentBaseUrl + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
                + DeploymentUtils.getPlateformUrlApplication( strCodeApplication );

        try
        {
            strJSONApllicationAreas = DeploymentUtils.callPlateformEnvironmentWs( strUrlApllication );
        }
        catch( Exception e )
        {
            AppLogService.error( e );
        }

        listAreas = getAreas( strJSONApllicationAreas );

        if ( ( listAreas != null ) && ( listAreas.size( ) > 0 ) )
        {
            for ( String strArea : listAreas )
            {
                strJSONEnvironment = null;

                try
                {
                    strJSONEnvironment = DeploymentUtils.callPlateformEnvironmentWs( strPlateformEnvironmentBaseUrl + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
                            + DeploymentUtils.getPlateformUrlEnvironments( strCodeApplication, strArea ) );
                }
                catch( Exception e )
                {
                    AppLogService.error( e );
                }

                if ( strJSONEnvironment != null )
                {
                    listStrEnvironment = getEnvironments( strJSONEnvironment );

                    for ( String strEnv : listStrEnvironment )
                    {
                        strCodeEnvironment = strArea.toLowerCase( ) + ConstanteUtils.CONSTANTE_SEPARATOR_POINT + strEnv.toLowerCase( );

                        Environment environment = getEnvironment( strCodeEnvironment, locale );
                        if ( environment != null )
                        {
                            listEnvironments.add( environment );
                        }
                    }
                }
            }
        }

        return listEnvironments;
    }

    private List<String> getEnvironments( String strJsonFlux )
    {
        String strWebserviceEnvJsonObjectName = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_WEBSERVICE_ENVIRONMENTS_JSON_OBJECT_NAME );
        String strWebserviceEnvJsonDictionaryName = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_WEBSERVICE_EVIRONMENTS_JSON_DICTIONARY_NAME );

        List<String> listEnvs = null;

        if ( strJsonFlux != null )
        {
            listEnvs = DeploymentUtils.getJSONDictionary( strWebserviceEnvJsonObjectName, strWebserviceEnvJsonDictionaryName, strJsonFlux );
        }

        return listEnvs;
    }

    private List<String> getAreas( String strJsonFlux )
    {
        String strWebserviceAreasJsonObjectName = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_WEBSERVICE_AREAS_JSON_OBJECT_NAME );
        String strWebserviceAreasJsonDictionaryName = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_WEBSERVICE_AREAS_JSON_DICTIONARY_NAME );

        List<String> listAreas = null;

        if ( strJsonFlux != null )
        {
            listAreas = DeploymentUtils.getJSONDictionary( strWebserviceAreasJsonObjectName, strWebserviceAreasJsonDictionaryName, strJsonFlux );
        }

        return listAreas;
    }
}
