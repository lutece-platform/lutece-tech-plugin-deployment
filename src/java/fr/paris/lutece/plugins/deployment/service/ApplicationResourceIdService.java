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
public class ApplicationResourceIdService extends ResourceIdService
{
	private IApplicationService _applicationService = SpringContextService.getBean( "deployment.ApplicationService" );
	   
	/** Permission for creating an application */
    public static final String PERMISSION_CREATE = "CREATE";

    /** Permission for deleting an application*/
    public static final String PERMISSION_DELETE = "DELETE";

       /** Permission for modifying an application */
    public static final String PERMISSION_MODIFY = "MODIFY";
    
    /** Permission for modifying an application */
    public static final String PERMISSION_VIEW = "VIEW";

    /** Permission for deploying an application */
    public static final String PERMISSION_DEPLOY_APPLICATION = "DEPLOY_APPLICATION";

    /** Permission for deploying a script */
    public static final String PERMISSION_DEPLOY_SCRIPT = "DEPLOY_SCRIPT";
    
    /** Permission for init database */
    public static final String PERMISSION_INIT_DATABASE = "INIT_DATABASE";
    
    /** Permission for init App context */
    public static final String PERMISSION_INIT_APP_CONTEXT = "INIT_APP_CONTEXT";

   
    /** Label resource type */
    public static final String PROPERTY_LABEL_RESOURCE_TYPE = "deployment.permission.label.resource_type_application";
    private static final String PROPERTY_LABEL_CREATE = "deployment.permission.label.application.create_application";
    private static final String PROPERTY_LABEL_DELETE = "deployment.permission.label.application.delete_application";
    private static final String PROPERTY_LABEL_MODIFY = "deployment.permission.label.application.modify_application";
    private static final String PROPERTY_LABEL_VIEW = "deployment.permission.label.application.view_application";
    private static final String PROPERTY_LABEL_DEPLOY_APPLICATION= "deployment.permission.label.application.deploy_application";
    private static final String PROPERTY_LABEL_DEPLOY_SCRIPT = "deployment.permission.label.application.deploy_script";
    private static final String PROPERTY_LABEL_INIT_APP_CONTEXT= "deployment.permission.label.application.init_app_context";
    private static final String PROPERTY_LABEL_INIT_DATABASE= "deployment.permission.label.application.init_database";
     
    /** Creates a new instance of ApplicationResourceIdService */
    public ApplicationResourceIdService(  )
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
        ResourceType rt = ResourceTypeManager.getResourceType( Application.RESOURCE_TYPE );

        if ( rt == null )
        {
            rt = new ResourceType(  );
            rt.setResourceIdServiceClass( ApplicationResourceIdService.class.getName(  ) );
            rt.setPluginName( DeploymentPlugin.PLUGIN_NAME );
            rt.setResourceTypeKey( Application.RESOURCE_TYPE );
            rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );
        }

        Permission p = new Permission(  );
        p.setPermissionKey( PERMISSION_CREATE );
        p.setPermissionTitleKey( PROPERTY_LABEL_CREATE );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_MODIFY );
        p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY );
        rt.registerPermission( p );
        
        p = new Permission(  );
        p.setPermissionKey( PERMISSION_VIEW);
        p.setPermissionTitleKey( PROPERTY_LABEL_VIEW );
        rt.registerPermission( p );
        
        p = new Permission(  );
        p.setPermissionKey( PERMISSION_DELETE );
        p.setPermissionTitleKey( PROPERTY_LABEL_DELETE );
        rt.registerPermission( p );

        
        p = new Permission(  );
        p.setPermissionKey( PERMISSION_DEPLOY_APPLICATION);
        p.setPermissionTitleKey( PROPERTY_LABEL_DEPLOY_APPLICATION);
        rt.registerPermission( p );
        
        p = new Permission(  );
        p.setPermissionKey( PERMISSION_DEPLOY_SCRIPT);
        p.setPermissionTitleKey( PROPERTY_LABEL_DEPLOY_SCRIPT);
        rt.registerPermission( p );
        
        p = new Permission(  );
        p.setPermissionKey( PERMISSION_INIT_APP_CONTEXT);
        p.setPermissionTitleKey( PROPERTY_LABEL_INIT_APP_CONTEXT);
        rt.registerPermission( p );
        
        p = new Permission(  );
        p.setPermissionKey( PERMISSION_INIT_DATABASE);
        p.setPermissionTitleKey( PROPERTY_LABEL_INIT_DATABASE);
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
         List<Application> listApplications=_applicationService.getListApplications(new FilterDeployment(), PluginService.getPlugin(DeploymentPlugin.PLUGIN_NAME));
         ReferenceList refListApplication=new ReferenceList();
         for(Application application:listApplications)
         {
        	 refListApplication.addItem(application.getIdApplication(), application.getName());
        	 
         }
         return refListApplication;
         
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( String strId, Locale locale )
    {
       
    	Application application = _applicationService.getApplication( Integer.parseInt(strId),
                PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME ) );

        return ( application != null ) ? application.getName(  ) : null;
    }
}
