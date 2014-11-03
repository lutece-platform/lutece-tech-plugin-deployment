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
package fr.paris.lutece.plugins.deployment.business;

import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


public class ApplicationDAO implements IApllicationDAO
{
    private static final String SQL_FILTER_ID_APPLICATION = " id_application = ? ";
    private static final String SQL_FILTER_CODE_CATEGORY = " code_category = ? ";
    private static final String SQL_ORDER_CODE_ASC = " ORDER BY code ASC ";
    private static final String SQL_QUERY_NEW_PK = " SELECT max(id_application) FROM deployment_application ";
    private static final String SQL_QUERY_SELECT_APPLICATION = "SELECT id_application,code,name,code_category,site_name,url_site,webapp_name FROM deployment_application ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO deployment_application (id_application,code,name,code_category,site_name,url_site,webapp_name)VALUES(?,?,?,?,?,?,?)";
    private static final String SQL_QUERY_UPDATE = "UPDATE deployment_application SET id_application=?,code=?,name=?,code_category=?,site_name=?,url_site=?,webapp_name=? WHERE" +
        SQL_FILTER_ID_APPLICATION;
    private static final String SQL_QUERY_DELETE = "DELETE FROM deployment_application WHERE" +
        SQL_FILTER_ID_APPLICATION;

    /**
     * Generates a new primary key
     *
     * @param plugin the plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    public void delete( int nIdApplication, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdApplication );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    public List<Application> findByFilter( FilterDeployment filter, Plugin plugin )
    {
        List<Application> listApllication = new ArrayList<Application>(  );
        List<String> listStrFilter = new ArrayList<String>(  );
        Application application;

        if ( filter.containsIdApplicationFilter(  ) )
        {
            listStrFilter.add( SQL_FILTER_ID_APPLICATION );
        }

        if ( filter.containsCodeCategoryFilter(  ) )
        {
            listStrFilter.add( SQL_FILTER_CODE_CATEGORY );
        }

        String strSQL = DeploymentUtils.buildRequetteWithFilter( SQL_QUERY_SELECT_APPLICATION, listStrFilter,
                SQL_ORDER_CODE_ASC );
        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );
        int nIndex = 1;

        if ( filter.containsIdApplicationFilter(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdApplication(  ) );
            nIndex++;
        }

        if ( filter.containsCodeCategoryFilter(  ) )
        {
            daoUtil.setString( nIndex, filter.getCodeCategory(  ) );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            application = new Application(  );
            application.setIdApplication( daoUtil.getInt( 1 ) );
            application.setCode( daoUtil.getString( 2 ) );
            application.setName( daoUtil.getString( 3 ) );
            application.setCodeCategory( daoUtil.getString( 4 ) );
            application.setSiteName( daoUtil.getString( 5 ) );
            application.setSvnUrlSite( daoUtil.getString( 6 ) );
            application.setWebAppName( daoUtil.getString( 7 ) );
            listApllication.add( application );
        }

        daoUtil.free(  );

        return listApllication;
    }

    public Application findByPrimaryKey( int nIdApplication, Plugin plugin )
    {
        FilterDeployment filter = new FilterDeployment(  );
        filter.setIdApplication( nIdApplication );

        List<Application> listApplications = findByFilter( filter, plugin );

        return ( listApplications.size(  ) > 0 ) ? listApplications.get( 0 ) : null;
    }

    public int insert( Application application, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setString( 2, application.getCode(  ) );
        daoUtil.setString( 3, application.getName(  ) );
        daoUtil.setString( 4, application.getCodeCategory(  ) );
        daoUtil.setString( 5, application.getSiteName(  ) );
        daoUtil.setString( 6, application.getSvnUrlSite(  ) );
        daoUtil.setString( 7, application.getWebAppName(  ) );

        application.setIdApplication( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, application.getIdApplication(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        return application.getIdApplication(  );
    }

    public void update( Application application, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, application.getIdApplication(  ) );
        daoUtil.setString( 2, application.getCode(  ) );
        daoUtil.setString( 3, application.getName(  ) );
        daoUtil.setString( 4, application.getCodeCategory(  ) );
        daoUtil.setString( 5, application.getSiteName(  ) );
        daoUtil.setString( 6, application.getSvnUrlSite(  ) );
        daoUtil.setString( 7, application.getWebAppName(  ) );
        daoUtil.setInt( 8, application.getIdApplication(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
