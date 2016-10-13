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

import java.util.HashMap;
import java.util.Locale;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.deployment.service.IDatabaseService;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
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
public class DumpAction extends DefaultAction
{
	private IDatabaseService _databaseService = SpringContextService.getBean( "deployment.DatabaseService" );
    @Override
    public String run( Application application, ServerApplicationInstance serverApplicationInstance,
        CommandResult commandResult, ActionParameter... parameter )
    {
    	 
    	
    	String strResult = null;
    	
    	String strDataBase=null;
    	for (int i = 0; i < parameter.length; i++) {
			
    		if(parameter[i].getName().equals(ConstanteUtils.PARAM_CODE_DATABASE))
    		{
    			
    			strDataBase=parameter[i].getValue();
    		}
		}
    	
    	
    	if(!StringUtils.isEmpty(strDataBase))
    	{
	    	 String strPlateformEnvironmentBaseUrl = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL );
	         String strWebserviceActionJsonPropery = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_WEBSERVICE_ACTION_RESULT_JSON_PROPERTY_RESULT );
	         String strJSONDump = null;
	        
	
	         try
	         {
	             strJSONDump = DeploymentUtils.callPlateformEnvironmentWs( strPlateformEnvironmentBaseUrl +
	                     ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +
	                     DeploymentUtils.getPlateformUrlServerApplicationAction( application.getCode(),
	                         serverApplicationInstance, this.getCode(  ) ) + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + strDataBase + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH+ConstanteUtils.CONSTANTE_STAR);
	         }
	         catch ( Exception e )
	         {
	        	  DeploymentUtils.addTechnicalError(commandResult, "<h1>Erreur lors de l'exécution de la commande"+this.getCode(  )+" de cloudmgrws</h1> <br> "+e.getMessage(),e);  
	        	  AppLogService.error( e );
	             
	         }
	         
	
	         if ( strJSONDump != null )
	         {
	             JSONObject jo = DeploymentUtils.getJSONOBject( strJSONDump );
	
	             if ( jo != null )
	             {
	                 strResult = jo.getString( strWebserviceActionJsonPropery );
	                 if(strResult!=null && !new Boolean( strResult ))
	                 {
	                 
	              	   DeploymentUtils.addTechnicalError(commandResult, "<h1>Erreur lors de l'exécution de la commande"+this.getCode(  )+" de cloudmgrws</h1> <br> "+strJSONDump);

	                 }
	             }
	             commandResult.getResultInformations().put(ConstanteUtils.MARK_DUMP_FILE_URL,"servlet/plugins/deployment/download?code_application="+application.getIdApplication()+"&code_environment="+serverApplicationInstance.getCodeEnvironment()+"&code_server_application_instance_sql="+serverApplicationInstance.getCode()+"&code_database="+strDataBase+"&plugin_name=deployment"+"&server_application_type="+ serverApplicationInstance.getType());
	         	
	          }
    	}
         

         return strResult;
    }
    
    
    

	@Override
	public boolean canRunAction(Application application, ServerApplicationInstance serverApplicationInstance,
            CommandResult commandResult, ActionParameter... parameter) {
		// TODO Auto-generated method stub
		String strDataBase=null;
    	for (int i = 0; i < parameter.length; i++) {
			
    		if(parameter[i].getName().equals(ConstanteUtils.PARAM_CODE_DATABASE))
    		{
    			
    			strDataBase=parameter[i].getValue();
    		}
		}
    	
    	return !StringUtils.isEmpty(strDataBase);
   }

	@Override
	public String getTemplateFormAction(Application application, ServerApplicationInstance serverApplicationInstance,Locale locale) {
		
		HashMap model = new HashMap(  );
		
		model.put(ConstanteUtils.MARK_DATABASE_LIST, DeploymentUtils.getSimpleReferenceList(_databaseService.getDatabases(application.getCode(), serverApplicationInstance, locale)));
		
		HtmlTemplate template = AppTemplateService.getTemplate( ConstanteUtils.TEMPLATE_FORM_ACTION_DUMP,
				 locale, model );
		
		return template.getHtml();
	}
}
