package fr.paris.lutece.plugins.deployment.business;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.deployment.service.DeploymentPlugin;
import fr.paris.lutece.plugins.deployment.service.IActionService;
import fr.paris.lutece.plugins.deployment.service.IFtpService;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

public class InitMysqlDatabase extends DefaultAction
 {
	
	
   private IFtpService _ftpService=SpringContextService.getBean( "deployment.FtpService" );
   private IActionService _actionService=SpringContextService.getBean( "deployment.ActionService" );
   private static final String ACTION_EXECUTE="@EXECUTE_MYSQL";
   private static final String SCRIPT_INIT_NAME="init_db.sql";
   private static final String DATABASE="mysql";
   
   
    @Override
    public String run( Application application, ServerApplicationInstance serverApplicationInstance,
        CommandResult commandResult, ActionParameter... parameter )
    {
    	 
    	
    	 String strResult = "true";
    	 Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
         HashMap model = new HashMap(  );
         model.put(ConstanteUtils.MARK_APPLICATION,application);
	        
	     HtmlTemplate templateInitDatabaseContext = AppTemplateService.getTemplate( ConstanteUtils.TEMPLATE_INIT_DB,
	              Locale.FRENCH ,model );
	        
	     InputStream  iTemplateDatabaseContext=new ByteArrayInputStream(templateInitDatabaseContext.getHtml().getBytes());
	         
	      
	    
	   
      	_ftpService.uploadFile( SCRIPT_INIT_NAME,iTemplateDatabaseContext,serverApplicationInstance.getFtpInfo(  ),
			    DeploymentUtils.getDeployDirectoryTarget( application.getCode(  ), serverApplicationInstance ),
			    commandResult,false );
     
      	if(commandResult.getStatus()==CommandResult.STATUS_ERROR)
      	{
      		
      		return "false";
      	}
      	 
      	 IAction action = _actionService.getAction(ACTION_EXECUTE,  Locale.FRENCH );
         ActionParameter databaseParameter = new ActionParameter(  );
         databaseParameter.setName( ConstanteUtils.PARAM_CODE_DATABASE);
         databaseParameter.setValue(DATABASE);
         
         ActionParameter scriptParameter = new ActionParameter(  );
         scriptParameter.setName( ConstanteUtils.PARAM_SCRIPT_NAME);
         scriptParameter.setValue(SCRIPT_INIT_NAME);
         
         
         ActionParameter[] tabParameters=new ActionParameter[2];

         tabParameters[0]=databaseParameter;
         tabParameters[1]=scriptParameter;
         
      	 if ( action != null  && _actionService.canExecuteAction(application, action, serverApplicationInstance, commandResult, tabParameters))
	      {
      		
      		 strResult= Boolean.toString(_actionService.executeAction( application, action, serverApplicationInstance,
 	                commandResult,tabParameters) );
 	     }
         
      	return strResult;
    }
    
    
    

	@Override
	public boolean canRunAction(Application application, ServerApplicationInstance serverApplicationInstance,
            CommandResult commandResult, ActionParameter... parameter) {

		if(parameter!=null)
		{
			for (int i = 0; i < parameter.length; i++) {
				
	    		if(parameter[i].getName().equals(ConstanteUtils.PARAM_INIT_DATABASE))
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
