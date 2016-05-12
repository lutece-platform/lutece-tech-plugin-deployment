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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.deployment.service.DeploymentPlugin;
import fr.paris.lutece.plugins.deployment.service.IDatabaseService;
import fr.paris.lutece.plugins.deployment.service.IFtpService;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 *
 * WarInstallerAction
 *
 */
public class InitAppContextAction extends DefaultAction
{

	IFtpService _ftpService=SpringContextService.getBean( "deployment.FtpService" );
    @Override
    public String run( Application application, ServerApplicationInstance serverApplicationInstance,
        CommandResult commandResult, ActionParameter... parameter )
    {
    	 
    	
    	String strResult = "true";
    	 Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
         HashMap model = new HashMap(  );
         model.put(ConstanteUtils.MARK_APPLICATION,application);
	        
	     HtmlTemplate templateInitAppContext = AppTemplateService.getTemplate( ConstanteUtils.TEMPLATE_INIT_APP_CONTEXT,
	              Locale.FRENCH ,model );
	        
	      InputStream  iTemplateInitAppContext=new ByteArrayInputStream(templateInitAppContext.getHtml().getBytes());
	         
	    _ftpService.uploadFile( application.getWebAppName()+".xml",iTemplateInitAppContext,serverApplicationInstance.getFtpInfo(  ),
				    DeploymentUtils.getContextDirectoryTarget( application.getCode(  ), serverApplicationInstance ),
				    commandResult,false );
	         
		 return strResult;
    }
    
    
    

	@Override
	public boolean canRunAction(Application application, ServerApplicationInstance serverApplicationInstance,
            CommandResult commandResult, ActionParameter... parameter) {
		
		if(parameter!=null)
		{
			// TODO Auto-generated method stub
			for (int i = 0; i < parameter.length; i++) {
				
	    		if(parameter[i].getName().equals(ConstanteUtils.PARAM_INIT_APP_CONTEXT))
	    		{
	    			
	    			return new Boolean(parameter[i].getValue());
	    		}
			}
		}
    	
    	return false;
   }

	@Override
	public String getTemplateFormAction(Application application, ServerApplicationInstance serverApplicationInstance,Locale locale) {
		
		return null;
	}
}
