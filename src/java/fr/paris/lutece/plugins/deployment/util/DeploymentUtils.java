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
package fr.paris.lutece.plugins.deployment.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.deployment.business.ActionParameter;
import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.FilterDeployment;
import fr.paris.lutece.plugins.deployment.business.IAction;
import fr.paris.lutece.plugins.deployment.business.SvnUser;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;
import fr.paris.lutece.plugins.deployment.business.WorkflowDeploySiteContext;
import fr.paris.lutece.plugins.deployment.service.ISvnService;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.user.attribute.AdminUserField;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.user.attribute.AdminUserFieldService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;


public class DeploymentUtils
{
    /**
     * Builds a query with filters placed in parameters. Consider using
     * {@link #buildQueryWithFilter(StringBuilder, List, String)} instead.
     *
     * @param strSelect
     *            the select of the query
     * @param listStrFilter
     *            the list of filter to add in the query
     * @param strOrder
     *            the order by of the query
     * @return a query
     */
    public static String buildRequetteWithFilter( String strSelect, List<String> listStrFilter, String strOrder )
    {
        return buildQueryWithFilter( new StringBuilder( strSelect ), listStrFilter, strOrder );
    }

    /**
     * Builds a query with filters placed in parameters
     *
     * @param sbSQL
     *            the beginning of the query
     * @param listFilter
     *            the list of filter to add in the query
     * @param strOrder
     *            the order by of the query
     * @return a query
     */
    public static String buildQueryWithFilter( StringBuilder sbSQL, List<String> listFilter, String strOrder )
    {
        int nCount = 0;

        for ( String strFilter : listFilter )
        {
            if ( ++nCount == 1 )
            {
                sbSQL.append( ConstanteUtils.CONSTANTE_SQL_WHERE );
            }

            sbSQL.append( strFilter );

            if ( nCount != listFilter.size(  ) )
            {
                sbSQL.append( ConstanteUtils.CONSTANTE_SQL_AND );
            }
        }

        if ( strOrder != null )
        {
            sbSQL.append( strOrder );
        }

        return sbSQL.toString(  );
    }

    /**
     * This method calls Rest WS archive
     *
     * @param strUrl
     *            the url
     * @param params
     *            the params to pass in the post
     * @param listElements
     *            the list of elements to include in the signature
     * @return the response as a string
     * @throws HttpAccessException
     *             the exception if there is a problem
     */
    public static String callPlateformEnvironmentWs( String strUrl )
        throws HttpAccessException
    {
        String strResponse = StringUtils.EMPTY;

        try
        {
            HttpAccess httpAccess = new HttpAccess(  );
            strResponse = httpAccess.doGet( strUrl );
        }
        catch ( HttpAccessException e )
        {
            String strError = "ArchiveWebServices - Error connecting to '" + strUrl + "' : ";
            AppLogService.error( strError + e.getMessage(  ), e );
            throw new HttpAccessException( strError, e );
        }

        return strResponse;
    }

    public static List<String> getJSONDictionary( String dictionaryName, String strJSONFlux )
    {
        List<String> jsonCollection = new ArrayList<String>(  );
        JSONObject jo = (JSONObject) JSONSerializer.toJSON( strJSONFlux );
        JSONArray jsonArray = jo.getJSONArray( dictionaryName );
        Iterator iterator = jsonArray.iterator(  );

        while ( iterator.hasNext(  ) )
        {
            jsonCollection.add( (String) iterator.next(  ) );
        }

        return jsonCollection;
    }

    public static List<String> getJSONDictionary( String objectName, String dictionaryName, String strJSONFlux )
    {
        List<String> jsonCollection = new ArrayList<String>(  );
        JSONObject jo = (JSONObject) JSONSerializer.toJSON( strJSONFlux );
        
   
        JSONArray jsonArray = jo.getJSONObject( objectName ).getJSONArray( dictionaryName );
        Iterator iterator = jsonArray.iterator(  );

        while ( iterator.hasNext(  ) )
        {
            jsonCollection.add( (String) iterator.next(  ) );
        }

        return jsonCollection;
    }

    public static JSONObject getJSONOBject( String strJSONFlux )
    {
        JSONObject jo = (JSONObject) JSONSerializer.toJSON( strJSONFlux );

        return jo;
    }

    /**
     * Retourne l'emplacement du pom
     *
     * @param strBasePath
     * @param strPluginName
     * @return
     */
    public static String getPathPomFile( String strPathSite )
    {
        return strPathSite + File.separator + ConstanteUtils.CONSTANTE_POM_XML;
    }
    
    
    /**
     * Retourne l'emplacement du pom
     *
     * @param strBasePath
     * @param strPluginName
     * @return
     */
    public static String getPathUpgradeFiles( String strPathSite )
    {
    	
    	String strPath=AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_UPGRADE_DIRECTORY_PATH );
    	StringBuffer strBuffer=new StringBuffer(strPathSite); 
    	
    	for(String strP:strPath.split(ConstanteUtils.CONSTANTE_SEPARATOR_SLASH))
    	{
    		strBuffer.append(File.separator);
    		strBuffer.append(strP);
    		
    		
    	}
    	
       return strBuffer.toString();
    }
    
    /**
     * Retourne l'emplacement du pom
     *
     * @param strBasePath
     * @param strPluginName
     * @return
     */
    public static String getPathUpgradeFile( String strPathSite,String strFileName )
    {
    	
    	StringBuffer strBuffer=new StringBuffer(getPathUpgradeFiles(strPathSite));
    	strBuffer.append(File.separator);
    	strBuffer.append(strFileName);
    	return strBuffer.toString();
    }
    
    
    
    
    
    

    /**
     * Retourne l'emplacement du pom
     *
     * @param strBasePath
     * @param strPluginName
     * @return
     */
    public static String getPathArchiveGenerated( String strPathSite, String strWarName, String strExtension )
    {
        return strPathSite + File.separator + ConstanteUtils.CONSTANTE_TARGET + File.separator + strWarName +
        strExtension;
    }

    /**
     * Retourne l'emplacement du pom
     *
     * @param strBasePath
     * @param strPluginName
     * @return
     */
    public static String getPathCheckoutSite( String strSiteName )
    {
        String strCheckoutBasePath = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_CHECKOUT_BASE_PAH );

        return strCheckoutBasePath + File.separator + strSiteName;
    }

    public static String getPlateformUrlApplication( String strCodeApplication )
    {
        // String strPlateformEnvironmentBaseUrl=
        // AppPropertiesService.getProperty
        // (PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL);
        return strCodeApplication;
    }

    public static String getPlateformUrlEnvironments( String strCodeApplication, String strArea )
    {
        return getPlateformUrlApplication( strCodeApplication ) + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + strArea;
    }

    public static String getPlateformUrlServerApplicationInstances( String strCodeApplication,
        String strCodeEnvironment, String strServerApplicationType )
    {
        String strPathEnvironment = ( strCodeEnvironment.replace( ConstanteUtils.CONSTANTE_SEPARATOR_POINT,
                ConstanteUtils.CONSTANTE_SEPARATOR_SLASH ) ).toUpperCase(  );

        return getPlateformUrlApplication( strCodeApplication ) + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +
        strPathEnvironment + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +
        getPathServerByType( strServerApplicationType, strCodeEnvironment );
    }

    public static String getPlateformUrlServerApplicationActions( String strCodeApplication,
        ServerApplicationInstance serverApplicationInstance )
    {
        return getPlateformUrlServerApplicationInstances( strCodeApplication,
            serverApplicationInstance.getCodeEnvironment(  ), serverApplicationInstance.getType(  ) ) +
        ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + serverApplicationInstance.getCode(  ).toUpperCase(  );
    }
    
    public static String getPlateformUrlDatabases( String strCodeApplication,
            ServerApplicationInstance serverApplicationInstance )
        {
            return getPlateformUrlServerApplicationInstances( strCodeApplication,
                serverApplicationInstance.getCodeEnvironment(  ), serverApplicationInstance.getType(  ) ) +
            ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + serverApplicationInstance.getCode(  ).toUpperCase(  ) +
            ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + ConstanteUtils.CONSTANTE_ACTION_EXECUTE ;
        }
    
    

    public static String getPlateformUrlServerApplicationAction( String strCodeApplication,
        ServerApplicationInstance serverApplicationInstance, String strCodeAction )
    {
        return getPlateformUrlServerApplicationActions( strCodeApplication, serverApplicationInstance ) +
        ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + strCodeAction;
    }

    public static String getDeployDirectoryTarget( String strCodeApplication,
        ServerApplicationInstance serverApplicationInstance )
    {
        return getPlateformUrlServerApplicationInstances( strCodeApplication,
            serverApplicationInstance.getCodeEnvironment(  ), serverApplicationInstance.getType(  ) ) +
        ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + serverApplicationInstance.getCode(  ) +
        ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + serverApplicationInstance.getFtpDirectoryTarget(  );
    }
    
    public static String getContextDirectoryTarget( String strCodeApplication,
            ServerApplicationInstance serverApplicationInstance )
        {
            return getPlateformUrlServerApplicationInstances( strCodeApplication,
                serverApplicationInstance.getCodeEnvironment(  ), serverApplicationInstance.getType(  ) ) +
            ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + serverApplicationInstance.getCode(  ) +
            ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + ConstanteUtils.CONTEXT_DIRECTORY_NAME;
        }
    
    public static String getDumpFileDirectory( String strCodeApplication,
            ServerApplicationInstance serverApplicationInstance )
        {
            return getPlateformUrlServerApplicationInstances( strCodeApplication,
                serverApplicationInstance.getCodeEnvironment(  ), serverApplicationInstance.getType(  ) ) +
            ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + serverApplicationInstance.getCode(  ) +
            ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + serverApplicationInstance.getFtpDirectoryDump(  )+ConstanteUtils.CONSTANTE_SEPARATOR_SLASH  ;
        }
    
    

    /**
     * convert a string to int
     *
     * @param strParameter
     *            the string parameter to convert
     * @return the conversion
     */
    public static int getIntegerParameter( String strParameter )
    {
        int nIdParameter = -1;

        try
        {
            if ( ( strParameter != null ) && strParameter.matches( ConstanteUtils.REGEX_ID ) )
            {
                nIdParameter = Integer.parseInt( strParameter );
            }
        }
        catch ( NumberFormatException ne )
        {
            AppLogService.error( ne );
        }

        return nIdParameter;
    }

    public static SvnUser getSvnUser( int nIdAdminUser, Locale locale )
    {
        
    	SvnUser mavenUser = null;
    	boolean bUsedApplicationAccount= AppPropertiesService.getPropertyBoolean( ConstanteUtils.PROPERTY_SVN_USED_DEPLOYMENT_ACCOUNT ,false);
        if(!bUsedApplicationAccount)
        	
        {
    	
	    	String strIdAttributeLogin = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_ADMINUSER_ID_ATTRIBUTE_SVN_LOGIN );
	        String strIdAttributePasssword = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_ADMINUSER_ID_ATTRIBUTE_SVN_PASSWORD );
	        String strLoginValue = null;
	        String strPasswordValue = null;
	       
	        Map<String, Object> mapAttributeUser = AdminUserFieldService.getAdminUserFields( nIdAdminUser, locale );
	
	        if ( mapAttributeUser.containsKey( strIdAttributeLogin ) &&
	                mapAttributeUser.containsKey( strIdAttributePasssword ) )
	        {
	            strLoginValue = ( (ArrayList<AdminUserField>) mapAttributeUser.get( strIdAttributeLogin ) ).get( 0 )
	                              .getValue(  );
	            strPasswordValue = ( (ArrayList<AdminUserField>) mapAttributeUser.get( strIdAttributePasssword ) ).get( 0 )
	                                 .getValue(  );
	
	            if ( !StringUtils.isEmpty( strLoginValue ) && !( StringUtils.isEmpty( strPasswordValue ) ) )
	            {
	                mavenUser = new SvnUser(  );
	                mavenUser.setLogin( strLoginValue );
	                mavenUser.setPassword( strPasswordValue );
	            }
	        }
        }
        else
        {
        	String strApplicationLogin = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_SVN_LOGIN_APPLICATION_DEPLOYMENT);
	        String strApplicationPasssword = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_SVN_PASSWORD_APPLICATION_DEPLOYMENT );
	    
        	 mavenUser = new SvnUser(  );
             mavenUser.setLogin( strApplicationLogin );
             mavenUser.setPassword( strApplicationPasssword );
       }

        return mavenUser;
    }

    public static JSONObject getJSONForCommandResult( CommandResult result )
    {
        JSONObject jo = new JSONObject(  );
        JSONObject joResult = new JSONObject(  );

        try
        {
            jo.put( ConstanteUtils.JSON_STATUS, result.getStatus(  ) );
            

            // pour les logs tr�s longs, on ne prend que la fin
            StringBuffer sbLog = result.getLog(  );
            int nMaxLogSize = AppPropertiesService.getPropertyInt( ConstanteUtils.PROPERTY_MAX_LOG_SIZE,
                    ConstanteUtils.CONSTANTE_ID_NULL );
            String strLog;

            if ( nMaxLogSize == -ConstanteUtils.CONSTANTE_ID_NULL )
            {
                nMaxLogSize = ConstanteUtils.CONSTANTE_DEFAULT_LOG_SIZE;
            }

            // sbLog null entre le lancement du thread et la premiere requete
            if ( sbLog != null )
            {
                if ( sbLog.length(  ) > nMaxLogSize )
                {
                    strLog = sbLog.substring( sbLog.length(  ) - nMaxLogSize );
                }
                else
                {
                    strLog = sbLog.toString(  );
                }
            }
            else
            {
                strLog = ConstanteUtils.CONSTANTE_EMPTY_STRING;
            }

            jo.put( ConstanteUtils.JSON_LOG, strLog );
            jo.put( ConstanteUtils.JSON_RUNNING, result.isRunning(  ) );
            jo.put( ConstanteUtils.JSON_ERROR, result.getError(  ) );
            jo.put( ConstanteUtils.JSON_ERROR_TYPE, result.getErrorType());
            
            for (  Entry<String,String> resultInformations: result.getResultInformations().entrySet()) {
            	
            	joResult.put(resultInformations.getKey(),resultInformations.getValue());
			}
            jo.put(ConstanteUtils.JSON_RESULT,joResult);
            
        }
        catch ( JSONException e )
        {
            AppLogService.error( "JSON error : " + e.getMessage(  ) );
        }

        return jo;
    }

    public static JSONObject getJSONForWorkflowAction( String strJspForceRedirect, String strFormError,
        CommandResult result, State state, Collection<Action> listAction )
    {
        JSONObject jo = new JSONObject(  );

        try
        {
            jo.put( ConstanteUtils.JSON_JSP_FOM_DISPLAY,
                ( strJspForceRedirect != null ) ? strJspForceRedirect : ConstanteUtils.CONSTANTE_EMPTY_STRING );

            jo.put( ConstanteUtils.JSON_STATE,
                ( state != null ) ? state.getName(  ) : ConstanteUtils.CONSTANTE_EMPTY_STRING );
            jo.put( ConstanteUtils.JSON_FORM_ERROR,
                ( strFormError != null ) ? strFormError : ConstanteUtils.CONSTANTE_EMPTY_STRING );

            JSONObject joAction;
            JSONArray joListAction = new JSONArray(  );

            if ( listAction != null )
            {
                for ( Action action : listAction )
                {
                    joAction = new JSONObject(  );
                    joAction.put( ConstanteUtils.JSON_ACTION_ID, action.getId(  ) );
                    joAction.put( ConstanteUtils.JSON_ACTION_NAME, action.getName(  ) );
                    joAction.put( ConstanteUtils.JSON_ACTION_DESCRIPTION, action.getDescription(  ) );

                    joListAction.add( joAction );
                }
            }

            jo.put( ConstanteUtils.JSON_ACTION_LIST, joListAction );

            jo.put( ConstanteUtils.JSON_RUNNING,
                ( result != null ) ? result.isRunning(  ) : ConstanteUtils.CONSTANTE_EMPTY_STRING );
            jo.put( ConstanteUtils.JSON_ERROR,
                ( result != null ) ? result.getError(  ) : ConstanteUtils.CONSTANTE_EMPTY_STRING );
        }
        catch ( JSONException e )
        {
            AppLogService.error( "JSON error : " + e.getMessage(  ) );
        }

        return jo;
    }
    
    
    public static JSONObject getJSONForServerAction( int nIdApplication,String strCodeEnvironment,String strServerCodeInstance,String strServerApplicationType,String strJspForceRedirect,
            CommandResult result,List<IAction> listServersActions,Integer newServerStatus)
        {
            JSONObject jo = getJSONForCommandResult(result);

            try
            {
                jo.put( ConstanteUtils.JSON_JSP_FOM_DISPLAY,
                    ( strJspForceRedirect != null ) ? strJspForceRedirect : ConstanteUtils.CONSTANTE_EMPTY_STRING );

                 
            }
            catch ( JSONException e )
            {
                AppLogService.error( "JSON error : " + e.getMessage(  ) );
            }
            JSONObject joAction;
            JSONArray joListAction = new JSONArray(  );

            if ( listServersActions != null )
            {
                for ( IAction action : listServersActions )
                {
                	if( action.isDisplay() && (action.getStatus()==null||action.getStatus().equals(newServerStatus)))
                	{
                	
	                    joAction = new JSONObject(  );
	                    joAction.put( ConstanteUtils.JSON_ACTION_CODE, action.getCode() );
	                    joAction.put( ConstanteUtils.JSON_ACTION_NAME, action.getName(  ) );
	                    joAction.put( ConstanteUtils.JSON_ACTION_ICON_CSS_CLASS, action.getIconCssClass());
	                    joListAction.add( joAction );
                	}
                }
            }
            jo.put( ConstanteUtils.JSON_ACTION_LIST, joListAction );
            jo.put( ConstanteUtils.JSON_SERVER_STATUS,
                    ( newServerStatus != null ) ? newServerStatus : ConstanteUtils.CONSTANTE_EMPTY_STRING );
            jo.put( ConstanteUtils.JSON_ID_APPLIACTION,nIdApplication);
            jo.put( ConstanteUtils.JSON_CODE_ENVIRONMENT,strCodeEnvironment);
            jo.put( ConstanteUtils.JSON_CODE_SERVER_APPLICATION_INSTANCE ,  strServerCodeInstance);
            jo.put( ConstanteUtils.JSON_SERVER_APPLICATION_TYPE ,  strServerApplicationType);
            return jo;
        }
    
   
    public static int getIdWorkflowSiteDeploy(WorkflowDeploySiteContext workflowDeploySiteContext)
    {
        int nIdWorkflow = ConstanteUtils.CONSTANTE_ID_NULL;
        
        if(workflowDeploySiteContext.isDeploySql())
        {
        	 nIdWorkflow = AppPropertiesService.getPropertyInt( ConstanteUtils.PROPERTY_ID_WORKFLOW_DEPLOY_SCRIPT,
                     ConstanteUtils.CONSTANTE_ID_NULL );
        }
        else if(workflowDeploySiteContext.isInitAppContext())
        {
        	 nIdWorkflow = AppPropertiesService.getPropertyInt( ConstanteUtils.PROPERTY_ID_WORKFLOW_INIT_APP_CONTEXT,
                     ConstanteUtils.CONSTANTE_ID_NULL );
        }
        else if(workflowDeploySiteContext.isInitBdd())
        {
        	 nIdWorkflow = AppPropertiesService.getPropertyInt( ConstanteUtils.PROPERTY_ID_WORKFLOW_INIT_DATABASE,
                     ConstanteUtils.CONSTANTE_ID_NULL );
        }
        else if ( workflowDeploySiteContext.isTagSiteBeforeDeploy(  )  )
        {
        	if(workflowDeploySiteContext.isTagAutomatically())
        	{
        		  nIdWorkflow = AppPropertiesService.getPropertyInt( ConstanteUtils.PROPERTY_ID_WORKFLOW_TAG_AUTOMATICALLY_AND_DEPLOY_SITE,
                          ConstanteUtils.CONSTANTE_ID_NULL );
        		
        	}
        	else
        	{
        	
            nIdWorkflow = AppPropertiesService.getPropertyInt( ConstanteUtils.PROPERTY_ID_WORKFLOW_TAG_AND_DEPLOY_SITE,
                    ConstanteUtils.CONSTANTE_ID_NULL );
        	}
        }
        else
        {
            nIdWorkflow = AppPropertiesService.getPropertyInt( ConstanteUtils.PROPERTY_ID_WORKFLOW_DEPLOY_SITE,
                    ConstanteUtils.CONSTANTE_ID_NULL );
        }

        return nIdWorkflow;
    }

    public static HashMap<String, ReferenceList> getHashCategoryListSite( ReferenceList categoryRefList,
        ISvnService svnService, SvnUser mavenUser )
    {
        HashMap<String, ReferenceList> hashCategoryListSite = new HashMap<String, ReferenceList>(  );
        FilterDeployment filter = new FilterDeployment(  );

        for ( ReferenceItem item : categoryRefList )
        {
            filter.setCodeCategory( item.getCode(  ) );

            hashCategoryListSite.put( item.getCode(  ), svnService.getSites( filter, mavenUser ) );
        }

        return hashCategoryListSite;
    }

    public static void startCommandResult( WorkflowDeploySiteContext context )
    {
        CommandResult commandResult = new CommandResult(  );
        startCommandResult(commandResult);
        context.setCommandResult( commandResult );
        
    }
    
    public static void startCommandResult(CommandResult commandResult )
    {
    	
         commandResult.setLog( new StringBuffer(  ) );
         commandResult.setRunning( true );
         commandResult.setStatus(CommandResult.STATUS_OK);
    }

    public static void stopCommandResult( WorkflowDeploySiteContext context )
    {
    	stopCommandResult(context.getCommandResult(  ));
    }
    
    public static void stopCommandResult(CommandResult commandResult  )
    {
    	commandResult.setRunning( false );
    }
    
    public static void addTechnicalError(CommandResult commandResult,String strError  )
    {
    	AppLogService.error(strError);
    	if(commandResult!=null)
    	{
	    	commandResult.setError(strError);
	    	commandResult.setStatus(CommandResult.STATUS_ERROR);
	    	commandResult.setRunning(false);
	    	commandResult.setErrorType(CommandResult.ERROR_TYPE_STOP);
    	}
	  }


    public static ReferenceList addEmptyRefenceItem( ReferenceList referenceList )
    {
    	ReferenceList referenceList2=new ReferenceList();
    	
    	ReferenceItem referenceItem = new ReferenceItem(  );
        
    	referenceItem.setCode( ConstanteUtils.CONSTANTE_EMPTY_STRING );
        referenceItem.setName( ConstanteUtils.CONSTANTE_EMPTY_STRING );
        
        referenceList2.add( 0, referenceItem );
        referenceList2.addAll(referenceList);
        
        return referenceList2; 
    }

    public static ActionParameter[] getActionParameters( HttpServletRequest request, List<String> listParameterNames )
    {
        if ( listParameterNames != null )
        {
            List<ActionParameter> listActionParameters = new ArrayList<ActionParameter>(  );
            ActionParameter actionParameter;

            for ( String param : listParameterNames )
            {
                actionParameter = new ActionParameter(  );
                actionParameter.setName( param );
                actionParameter.setValue( request.getParameter( param ) );
                listActionParameters.add(actionParameter);
            }

            return listActionParameters.toArray( new ActionParameter[listParameterNames.size(  )] );
        }

        return null;
    }
    
    
    public static ActionParameter[] getActionParameters( WorkflowDeploySiteContext workkflowContext)
    {
      
            List<ActionParameter> listActionParameters = new ArrayList<ActionParameter>(  );
            ActionParameter actionParameter;
            if(workkflowContext.isDeployWar()||workkflowContext.isInitAppContext())
            {
	            actionParameter = new ActionParameter(  );
	            actionParameter.setName( ConstanteUtils.PARAM_TAG_TO_DEPLOY  );
	            actionParameter.setValue( workkflowContext.getTagToDeploy());

	            listActionParameters.add(actionParameter);

	            
	            ActionParameter initAppContext = new ActionParameter(  );
	            initAppContext.setName( ConstanteUtils.PARAM_INIT_APP_CONTEXT  );
	            initAppContext.setValue( Boolean.toString( workkflowContext.isInitAppContext()));
	            
	            
	            listActionParameters.add(initAppContext);
	            
	       }
            
            if(workkflowContext.isDeploySql()||workkflowContext.isInitBdd())
            {
            	if( !workkflowContext.isInitBdd())
 	           	{
            	 actionParameter = new ActionParameter(  );
	             actionParameter.setName( ConstanteUtils.PARAM_CODE_DATABASE  );
	             actionParameter.setValue( workkflowContext.getDatabaseName());
	             listActionParameters.add(actionParameter);
 	           	}
	            
	            if(workkflowContext.getScriptFileItemName() !=null)
	            {
		            actionParameter = new ActionParameter(  );
		            actionParameter.setName( ConstanteUtils.PARAM_SCRIPT_NAME );
		            actionParameter.setValue( workkflowContext.getScriptFileItemName());
		            listActionParameters.add(actionParameter);
		            
	            }
	           
	           if( workkflowContext.isInitBdd())
	           {
		            actionParameter = new ActionParameter(  );
		            actionParameter.setName( ConstanteUtils.PARAM_INIT_DATABASE );
		            actionParameter.setValue( Boolean.toString( workkflowContext.isInitBdd()));
		            listActionParameters.add(actionParameter);
	           	}
		            
	          }
	            
	        
	       

            return listActionParameters.toArray( new ActionParameter[listActionParameters.size(  )] );
        

   
    }

    public static ReferenceList getReferenceListServerType( Locale locale )
    {
        ReferenceList referenceList = new ReferenceList(  );
        referenceList.addItem( ConstanteUtils.CONSTANTE_EMPTY_STRING, ConstanteUtils.CONSTANTE_EMPTY_STRING );
        referenceList.addItem( ConstanteUtils.CONSTANTE_SERVER_TOMCAT,
            I18nService.getLocalizedString( ConstanteUtils.PROPERTY_SERVER_TYPE_TOMCAT_LABEL, locale ) );
        referenceList.addItem( ConstanteUtils.CONSTANTE_SERVER_MYSQL,
            I18nService.getLocalizedString( ConstanteUtils.PROPERTY_SERVER_TYPE_MYSQL_LABEL, locale ) );
        referenceList.addItem( ConstanteUtils.CONSTANTE_SERVER_HTTPD,
            I18nService.getLocalizedString( ConstanteUtils.PROPERTY_SERVER_TYPE_HTTPD_LABEL, locale ) );

        return referenceList;
    }

    public static ReferenceList getReferenceListAction( List<IAction> listAction )
    {
        ReferenceList referenceList = new ReferenceList(  );
        referenceList.addItem( ConstanteUtils.CONSTANTE_EMPTY_STRING, ConstanteUtils.CONSTANTE_EMPTY_STRING );

        for ( IAction action : listAction )
        {
            referenceList.addItem( getActionKey( action.getCode(  ), action.getServerType(  ) ),action.getName());
        }

        return referenceList;
    }

    public static String getActionKey( String strCode, String strServerType )
    {
        return strCode + "_" + strServerType;
    }

    private static String getPathServerByType( String strApplicationType, String strEvironementCode )
    {
        String strPathServer = strApplicationType;

        if ( strEvironementCode.contains( "v1" ) )
        {
            if ( ConstanteUtils.CONSTANTE_SERVER_TOMCAT.equals( strApplicationType ) )
            {
                strPathServer = ConstanteUtils.CONSTANTE_SERVER_TOM;
            }
            else if ( ConstanteUtils.CONSTANTE_SERVER_MYSQL.equals( strApplicationType ) )
            {
                strPathServer = ConstanteUtils.CONSTANTE_SERVER_MYS;
            }
        }

        return strPathServer;
    }
    
    
    public static ReferenceList getSimpleReferenceList( List<String> list )
    {
    	  ReferenceList reflist = new ReferenceList(  );
    	  for(String strCode:list)
    	  {
    		  reflist.addItem(strCode, strCode);
    		  
    	  }
    	  return reflist;
    	  	  
    }
  
}
