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

import fr.paris.lutece.plugins.deployment.business.Application;
import fr.paris.lutece.plugins.deployment.business.FilterDeployment;
import fr.paris.lutece.plugins.deployment.business.IApllicationDAO;
import fr.paris.lutece.plugins.deployment.service.vcs.IVCSService;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import java.util.ArrayList;

import java.util.List;

import javax.inject.Inject;

public class ApplicationService implements IApplicationService
{

    // private static IApplicationService _singleton;

    @Inject
    private IApllicationDAO _applicationDAO;

    private ApplicationService( )
    {
        init( );
    }

    // public static IApplicationService getInstance( )
    // {
    // if ( _singleton == null )
    // {
    // _singleton = new ApplicationService( );
    // }
    //
    // return _singleton;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#getListApplications(fr.paris.lutece.plugins.deployment.business.FilterDeployment,
     * fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public List<Application> getListApplications( FilterDeployment filter, Plugin plugin )
    {
        return _applicationDAO.findByFilter( filter, plugin );
    }

    /**
     * {@inheriteDoc}
     */
    @Override
    public List<Application> getListApplicationFilteredBySearchName( List<Application> listApp, FilterDeployment filter, Plugin plugin )
    {
        List<Application> listAppFiltered = new ArrayList<Application>( );
        if ( filter.constainsSearchNameFilter( ) )
        {
            String strSearchName = filter.getSearchName( );
            for ( Application app : listApp )
            {
                String strSearchIn = new StringBuilder( ).append( app.getCode( ) ).append( " " ).append( app.getName( ) ).append( " " )
                        .append( app.getUrlRepo( ) ).toString( );

                // remove the application from the list if his name doesnt match.
                if ( strSearchIn.toLowerCase( ).contains( strSearchName.toLowerCase( ) ) )
                {
                    listAppFiltered.add( app );
                }
            }
            return listAppFiltered;
        }
        else
        {
            // no filter on search name.
            return listApp;
        }
    }

    public Application getApplication( int nIdApplication, Plugin plugin )
    {
        return _applicationDAO.findByPrimaryKey( nIdApplication, plugin );
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#init()
     */
    public void init( )
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#createApplication(fr.paris.lutece.plugins.deployment.business.Application,
     * fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void createApplication( Application application, Plugin plugin )
    {
        _applicationDAO.insert( application, plugin );
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#updateApplication(fr.paris.lutece.plugins.deployment.business.Application,
     * fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void updateApplication( Application application, Plugin plugin )
    {
        _applicationDAO.update( application, plugin );
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#deleteApplication(int, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void deleteApplication( int nIdApplication, Plugin plugin )
    {
        _applicationDAO.delete( nIdApplication, plugin );
    }

    public static boolean isPrivateRepo( Application application )
    {
        IVCSService vcsService = DeploymentUtils.getVCSService( application.getRepoType( ) );
        return vcsService.isPrivate( );
    }
}
