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

import fr.paris.lutece.plugins.deployment.business.Application;
import fr.paris.lutece.plugins.deployment.business.FilterDeployment;
import fr.paris.lutece.plugins.deployment.business.IApllicationDAO;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

import org.apache.commons.lang.StringUtils;

import java.util.List;

import javax.inject.Inject;


public class ApplicationService implements IApplicationService
{
    private static final String PROPERTY_SVN_CATEGORIES_LIST = "deployment.svnCategories.list";
    private static final String CONSTANTE_SVN_CATEGORY = "deployment.svnCategory.";
    private static final String CONSTANTE_SVN_CATEGORY_NAME = ".name";
    private static final String CONSTANTE_SEPARATOR = ",";

    // private static IApplicationService _singleton;
    private static ReferenceList _listCategory;
    @Inject
    private IApllicationDAO _applicationDAO;

    private ApplicationService(  )
    {
        init(  );
    }

    //    public static IApplicationService getInstance(  )
    //    {
    //        if ( _singleton == null )
    //        {
    //            _singleton = new ApplicationService(  );
    //        }
    //
    //        return _singleton;
    //    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#getListApplications(fr.paris.lutece.plugins.deployment.business.FilterDeployment, fr.paris.lutece.portal.service.plugin.Plugin)
         */
    public List<Application> getListApplications( FilterDeployment filter, Plugin plugin )
    {
        return _applicationDAO.findByFilter( filter, plugin );
    }

    public Application getApplication( int nIdApplication, Plugin plugin )
    {
        return _applicationDAO.findByPrimaryKey( nIdApplication, plugin );
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#init()
         */
    public void init(  )
    {
        initListCategories(  );
    }

    private void initListCategories(  )
    {
        _listCategory = new ReferenceList(  );

        String strCategoriesList = AppPropertiesService.getProperty( PROPERTY_SVN_CATEGORIES_LIST );

        if ( StringUtils.isNotBlank( strCategoriesList ) )
        {
            String[] tabCategoriesCode = strCategoriesList.split( CONSTANTE_SEPARATOR );
            String strCategoryName;

            for ( int i = 0; i < tabCategoriesCode.length; i++ )
            {
                strCategoryName = AppPropertiesService.getProperty( CONSTANTE_SVN_CATEGORY + tabCategoriesCode[i] +
                        CONSTANTE_SVN_CATEGORY_NAME );

                if ( StringUtils.isNotBlank( strCategoryName ) )
                {
                    _listCategory.addItem( tabCategoriesCode[i], strCategoryName );
                }
            }
        }
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#createApplication(fr.paris.lutece.plugins.deployment.business.Application, fr.paris.lutece.portal.service.plugin.Plugin)
         */
    public void createApplication( Application application, Plugin plugin )
    {
        _applicationDAO.insert( application, plugin );
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#updateApplication(fr.paris.lutece.plugins.deployment.business.Application, fr.paris.lutece.portal.service.plugin.Plugin)
         */
    public void updateApplication( Application application, Plugin plugin )
    {
        _applicationDAO.update( application, plugin );
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#deleteApplication(int, fr.paris.lutece.portal.service.plugin.Plugin)
         */
    public void deleteApplication( int nIdApplication, Plugin plugin )
    {
        _applicationDAO.delete( nIdApplication, plugin );
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#getListCategory()
         */
    public ReferenceList getListCategory(  )
    {
        return _listCategory;
    }
}
