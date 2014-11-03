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

import fr.paris.lutece.plugins.deployment.business.ActionParameter;
import fr.paris.lutece.plugins.deployment.business.CommandResult;
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
import java.util.Map.Entry;


public class ActionService implements IActionService
{
    private static HashMap<String, IAction> _hashServerApplicationAction;

    public IAction getAction( String strKey, Locale locale )
    {
        if ( _hashServerApplicationAction == null )
        {
            initHashServerApplicationAction(  );
        }

        IAction serverApplicationAction = _hashServerApplicationAction.get( strKey );

        if ( serverApplicationAction != null )
        {
            serverApplicationAction.setName( I18nService.getLocalizedString( 
                    serverApplicationAction.getI18nKeyName(  ), locale ) );
        }

        return serverApplicationAction;
    }

    public List<IAction> getListActionByServerApplicationInstance( String strCodeApplication,
        ServerApplicationInstance serverApplicationInstance, Locale locale )
    {
        String strPlateformEnvironmentBaseUrl = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL );
        List<IAction> listActions = new ArrayList<IAction>(  );
        List<String> listStrActions;
        String strWebserviceActionJsonDictionaryName = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_WEBSERVICE_SERVER_ACTIONS_JSON_DICTIONARY_NAME );
        String strJSONActions = null;

        try
        {
            strJSONActions = DeploymentUtils.callPlateformEnvironmentWs( strPlateformEnvironmentBaseUrl +
                    ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +
                    DeploymentUtils.getPlateformUrlServerApplicationActions( strCodeApplication,
                        serverApplicationInstance ) );
        }
        catch ( Exception e )
        {
            AppLogService.error( e );
        }

        if ( strJSONActions != null )
        {
            IAction action;
            listStrActions = DeploymentUtils.getJSONDictionary( strWebserviceActionJsonDictionaryName, strJSONActions );

            for ( String strActionCode : listStrActions )
            {
                action = getAction( DeploymentUtils.getActionKey( strActionCode, serverApplicationInstance.getType(  ) ),
                        locale );

                if ( action != null )
                {
                    listActions.add( action );
                }
            }
        }

        return listActions;
    }

    private void initHashServerApplicationAction(  )
    {
        List<IAction> listAction = SpringContextService.getBeansOfType( IAction.class );
        _hashServerApplicationAction = new HashMap<String, IAction>(  );

        if ( listAction != null )
        {
            for ( IAction action : listAction )
            {
                _hashServerApplicationAction.put( DeploymentUtils.getActionKey( action.getCode(  ),
                        action.getServerType(  ) ), action );
            }
        }
    }

    public List<IAction> getListAction( Locale locale )
    {
        List<IAction> listActions = new ArrayList<IAction>(  );
        IAction action;

        if ( _hashServerApplicationAction == null )
        {
            initHashServerApplicationAction(  );
        }

        for ( Entry<String, IAction> entry : _hashServerApplicationAction.entrySet(  ) )
        {
            action = entry.getValue(  );
            action.setName( I18nService.getLocalizedString( action.getI18nKeyName(  ), locale ) );
            listActions.add( action );
        }

        return listActions;
    }

    @Override
    public String executeAction( String strCodeApplication, IAction action,
        ServerApplicationInstance serverApplicationInstance, CommandResult commandResult, ActionParameter... parameter )
    {
        // TODO Auto-generated method stub
        return action.run( strCodeApplication, serverApplicationInstance, commandResult, parameter );
    }
}
