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

import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.business.FtpInfo;
import fr.paris.lutece.plugins.deployment.business.IAction;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;


public class ServerApplicationService implements IServerApplicationService
{
    private static HashMap<String, ServerApplicationInstance> _hashServerApplicationInstance;
    @Inject
    private IEnvironmentService _environmentService;
    @Inject
    private IActionService _actionService;

    //	public void setActionService(IActionService actionService) {
    //		_actionService = actionService;
    //	}

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.deployment.service.IEnvironmentService#getListServerApplicationInstance(java.lang.String, java.lang.String)
     */
    public HashMap<String, List<ServerApplicationInstance>> getHashServerApplicationInstance( 
        String strCodeApplication, String strServerApplicationType, Locale locale, boolean withActions,
        boolean withStatus )
    {
        HashMap<String, List<ServerApplicationInstance>> hashServerApplicationInstance = new HashMap<String, List<ServerApplicationInstance>>(  );
        List<Environment> listEnvironments = _environmentService.getListEnvironments( strCodeApplication, locale );

        if ( listEnvironments != null )
        {
            List<ServerApplicationInstance> listServerApplicationInstance;

            for ( Environment environment : listEnvironments )
            {
                listServerApplicationInstance = new ArrayList<ServerApplicationInstance>(  );
                listServerApplicationInstance.addAll( getListServerApplicationInstanceByEnvironment( 
                        strCodeApplication, environment.getCode(  ), strServerApplicationType, locale, withActions,
                        withStatus ) );
                hashServerApplicationInstance.put( environment.getCode(  ), listServerApplicationInstance );
            }
        }

        return hashServerApplicationInstance;
    }

    public List<ServerApplicationInstance> getListServerApplicationInstanceByEnvironment( String strCodeApplication,
        String strCodeEnvironment, String strServerApplicationType, Locale locale, boolean withActions,
        boolean withStatus )
    {
        String strPlateformEnvironmentBaseUrl = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL );
        List<ServerApplicationInstance> listServerApplicationInstance = new ArrayList<ServerApplicationInstance>(  );
        List<String> listStrServerApplicationInstance;
        String strWebserviceServerApplicationJsonObjectName = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_OBJECT_NAME );
        String strWebserviceServerApplicationJsonDictionaryName = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_WEBSERVICE_SERVER_APPLICATION_INSTANCES_JSON_DICTIONARY_NAME );
        String strJSONServerApplicationInstances = null;

        try
        {
            strJSONServerApplicationInstances = DeploymentUtils.callPlateformEnvironmentWs( strPlateformEnvironmentBaseUrl +
                    ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +
                    DeploymentUtils.getPlateformUrlServerApplicationInstances( strCodeApplication, strCodeEnvironment,
                        strServerApplicationType ) );
        }
        catch ( Exception e )
        {
            AppLogService.error( e );
        }

        if ( strJSONServerApplicationInstances != null )
        {
            listStrServerApplicationInstance = DeploymentUtils.getJSONDictionary( strWebserviceServerApplicationJsonObjectName,
                    strWebserviceServerApplicationJsonDictionaryName, strJSONServerApplicationInstances );

            for ( String strServerApplicationInstance : listStrServerApplicationInstance )
            {
                listServerApplicationInstance.add( getServerApplicationInstance( strCodeApplication,
                        strServerApplicationInstance.toLowerCase(  ), strCodeEnvironment, strServerApplicationType,
                        locale, withActions, withStatus ) );
            }
        }

        return listServerApplicationInstance;
    }

    public ServerApplicationInstance getServerApplicationInstance( String strCodeApplication,
        String strServerInstanceCode, String strCodeEnvironment, String strServerType, Locale locale,
        boolean withActions, boolean withStatus )
    {
        if ( _hashServerApplicationInstance == null )
        {
            initHashServerApplicationInstance(  );
        }

        ServerApplicationInstance defaultServerApplicationInstance = _hashServerApplicationInstance.get( strServerType +
                ConstanteUtils.CONSTANTE_SEPARATOR_POINT + strServerInstanceCode );
        ServerApplicationInstance serverApplicationInstance = newServerApplicationInstance( defaultServerApplicationInstance.getBeanName(  ),
                locale );
        serverApplicationInstance.setCodeEnvironment( strCodeEnvironment );
        serverApplicationInstance.setName( I18nService.getLocalizedString( 
                serverApplicationInstance.getI18nKeyName(  ), locale ) );
        setFtpInfo( serverApplicationInstance, strCodeApplication );

        if ( withActions )
        {
            serverApplicationInstance.setListServerApplicationAction( _actionService.getListActionByServerApplicationInstance( 
                    strCodeApplication, serverApplicationInstance, locale ) );
        }

        if ( withStatus )
        {
            serverApplicationInstance.setStatus( getStatus( strCodeApplication, serverApplicationInstance ) );
        }

        return serverApplicationInstance;
    }

    public void setFtpInfo( ServerApplicationInstance serverApplicationInstance, String strCodeApplication )
    {
        //serverApplicationInstance.setFtpDirectoryTarget(DeploymentUtils.getDeployDirectoryTarget(strCodeApplication,serverApplicationInstance.getCodeEnvironment() , serverApplicationInstance.getCode()));
        FtpInfo ftpInfo = new FtpInfo(  );
        ftpInfo.setHost( AppPropertiesService.getProperty( 
                ConstanteUtils.PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_HOST ) );
        ftpInfo.setPort( AppPropertiesService.getPropertyInt( 
                ConstanteUtils.PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_PORT, 21 ) );
        ftpInfo.setUserLogin( AppPropertiesService.getProperty( 
                ConstanteUtils.PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_USER_LOGIN ) );
        ftpInfo.setUserPassword( AppPropertiesService.getProperty( 
                ConstanteUtils.PROPERTY_DEPLOYMENT_SERVER_APPLICATION_FTP_USER_PASSWORD ) );
        serverApplicationInstance.setFtpInfo( ftpInfo );
    }

    private void initHashServerApplicationInstance(  )
    {
        List<ServerApplicationInstance> listServerApplicationInstance = SpringContextService.getBeansOfType( ServerApplicationInstance.class );
        _hashServerApplicationInstance = new HashMap<String, ServerApplicationInstance>(  );

        if ( listServerApplicationInstance != null )
        {
            for ( ServerApplicationInstance serverApplicationInstance : listServerApplicationInstance )
            {
                _hashServerApplicationInstance.put( serverApplicationInstance.getType(  ) +
                    ConstanteUtils.CONSTANTE_SEPARATOR_POINT + serverApplicationInstance.getCode(  ),
                    serverApplicationInstance );
            }
        }
    }

    private ServerApplicationInstance newServerApplicationInstance( String strBeanName, Locale locale )
    {
        return (ServerApplicationInstance) SpringContextService.getBean( strBeanName );
    }

    private Integer getStatus( String strCodeApplication, ServerApplicationInstance serverApplicationInstance )
    {
        if ( ( serverApplicationInstance != null ) &&
                ( serverApplicationInstance.getListServerApplicationAction(  ) != null ) )
        {
            for ( IAction action : serverApplicationInstance.getListServerApplicationAction(  ) )
            {
                if ( action.isUsedForStatus(  ) )
                {
                    boolean bResultOk = _actionService.executeAction( strCodeApplication, action,
                            serverApplicationInstance, null );

                    return   new Integer( new Boolean( bResultOk ) ? 1 : 0 ) ;
                }
            }
        }

        return null;
    }
}
