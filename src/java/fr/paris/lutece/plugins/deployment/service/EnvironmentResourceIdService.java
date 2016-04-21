/*
 * Copyright (c) 2002-2016, Mairie de Paris
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


import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.deployment.business.Application;
import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.business.FilterDeployment;
import fr.paris.lutece.plugins.deployment.service.DeploymentPlugin;
import fr.paris.lutece.plugins.deployment.service.IApplicationService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;


/**
 *
 * class DirectoryResourceIdService
 *
 */
public class EnvironmentResourceIdService extends ResourceIdService
{
	 private IEnvironmentService _environmentService = SpringContextService.getBean( "deployment.EnvironmentService" );
	      

    /** Permission for deploying an application */
    public static final String PERMISSION_DEPLOY_ON_ENVIROMENT= "DEPLOY_ON_ENVIROMENT";

    private static final String PROPERTY_LABEL_DEPLOY_ON_ENVIROMENT ="deployment.permission.label.environement.deploy_on_environment";
    
   /** Label resource type */
    public static final String PROPERTY_LABEL_RESOURCE_TYPE = "deployment.permission.label.resource_type_environment";
       
    /** Creates a new instance of ApplicationResourceIdService */
    public EnvironmentResourceIdService(  )
    {
        setPluginName( DeploymentPlugin.PLUGIN_NAME );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(  )
    {
        // Override the resource type DIRECTORY_DIRECTORY_TYPE
        ResourceType rt = ResourceTypeManager.getResourceType( Environment.RESOURCE_TYPE );

        if ( rt == null )
        {
            rt = new ResourceType(  );
            rt.setResourceIdServiceClass( EnvironmentResourceIdService.class.getName(  ) );
            rt.setPluginName( DeploymentPlugin.PLUGIN_NAME );
            rt.setResourceTypeKey( Environment.RESOURCE_TYPE );
            rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );
        }

      
        Permission p = new Permission(  );
        p.setPermissionKey( PERMISSION_DEPLOY_ON_ENVIROMENT);
        p.setPermissionTitleKey( PROPERTY_LABEL_DEPLOY_ON_ENVIROMENT);
        rt.registerPermission( p );
      
        ResourceTypeManager.registerResourceType( rt );
    }

    /**
     * Returns a list of directory resource ids
     * @param locale The current locale
     * @return A list of resource ids
     */
    @Override
    public ReferenceList getResourceIdList( Locale locale )
    {
        
    	return _environmentService.getEnvironmentRefList(locale);
    	
         
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( String strId, Locale locale )
    {
       
    	Environment environment=_environmentService.getEnvironment(strId, locale);

    	return environment!=null?environment.getName():null;
      
    }
}
