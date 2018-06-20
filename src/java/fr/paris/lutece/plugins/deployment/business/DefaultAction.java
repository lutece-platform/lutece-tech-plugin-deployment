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
package fr.paris.lutece.plugins.deployment.business;

import java.util.Locale;

import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import net.sf.json.JSONObject;

public class DefaultAction extends Action
{
    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.deployment.business.IAction#run(java.lang.String, fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance)
     */
    public String run( Application application, ServerApplicationInstance serverApplicationInstance, CommandResult commandResult, ActionParameter... parameter )
    {
        String strPlateformEnvironmentBaseUrl = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL );
        String strWebserviceActionJsonPropery = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_WEBSERVICE_ACTION_RESULT_JSON_PROPERTY_RESULT );
        String strJSONAction = null;
        String strResult = null;

        try
        {
            strJSONAction = DeploymentUtils.callPlateformEnvironmentWs( strPlateformEnvironmentBaseUrl + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH
                    + DeploymentUtils.getPlateformUrlServerApplicationAction( application.getCode( ), serverApplicationInstance, this.getCode( ) ) );
        }
        catch( Exception e )
        {
            AppLogService.error( e );
            if ( commandResult != null )
            {
                commandResult.setError( "<h1>Erreur  lors de l'exécution de la commande" + this.getCode( ) + " de cloudmgrws</h1> <br> " + e.getMessage( ) );
            }
        }

        if ( strJSONAction != null )
        {
            JSONObject jo = DeploymentUtils.getJSONOBject( strJSONAction );

            if ( jo != null )
            {
                strResult = jo.getString( strWebserviceActionJsonPropery );
                if ( strResult != null && !new Boolean( strResult ) && commandResult != null )
                {

                    commandResult.setError( "<h1>Erreur lors de l'exécution de la commande" + this.getCode( ) + " de cloudmgrws</h1> <br> : " + strJSONAction );

                }
            }
        }

        return strResult;
    }

    @Override
    public boolean canRunAction( Application application, ServerApplicationInstance serverApplicationInstance, CommandResult commandResult,
            ActionParameter... parameter )
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String getTemplateFormAction( Application application, ServerApplicationInstance serverApplicationInstance, Locale locale )
    {
        // TODO Auto-generated method stub
        return null;
    }
}
