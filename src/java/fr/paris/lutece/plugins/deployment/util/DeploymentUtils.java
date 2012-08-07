package fr.paris.lutece.plugins.deployment.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.MavenUser;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.service.user.attribute.AdminUserFieldService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

public class DeploymentUtils {
	
	

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
  public static  String callPlateformEnvironmentWs( String strUrl )
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
  
  
  
  
  public static List<String> getJSONDictionary(String objectName,String dictionaryName,String strJSONFlux)
  {
  	List<String> jsonCollection=new ArrayList<String>();
	JSONObject jo=(JSONObject) JSONSerializer.toJSON( strJSONFlux);  
	JSONArray jsonArray=jo.getJSONObject(objectName).getJSONArray(dictionaryName);
	Iterator iterator=jsonArray.iterator();
	while(iterator.hasNext())
	{
		jsonCollection.add((String)iterator.next());
	}
	return jsonCollection;
  }
  
  /**
	 * Retourne l'emplacement du pom
	 * @param strBasePath
	 * @param strPluginName
	 * @return
	 */
	public static String getPathPomFile( String strPathSite)
	{
		return strPathSite+ File.separator + ConstanteUtils.CONSTANTE_POM_XML;
	}
	
	
	/**
	 * Retourne l'emplacement du pom
	 * @param strBasePath
	 * @param strPluginName
	 * @return
	 */
	public static String getPathArchiveGenerated( String strPathSite,String strWarName,String strExtension)
	{
		return strPathSite+ File.separator + ConstanteUtils.CONSTANTE_TARGET+File.separator+strWarName+strExtension;
	}
	
	
	/**
	 * Retourne l'emplacement du pom
	 * @param strBasePath
	 * @param strPluginName
	 * @return
	 */
	public static String getPathCheckoutSite( String strSiteName)
	{
		String strCheckoutBasePath=AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_CHECKOUT_BASE_PAH );
		return strCheckoutBasePath+ File.separator + strSiteName ;
	}
	
	
	
	
	
	
	
	public static String getPlateformUrlApplication( String strCodeApplication)
 	{
 		//String strPlateformEnvironmentBaseUrl=  AppPropertiesService.getProperty (PROPERTY_ENVIRONMENT_PLATEFORM_BASE_URL);
 		return strCodeApplication;
	 	
 	}
 	
	public static String getPlateformUrlEnvironments(String strCodeApplication,String strArea)
 	{
 		
 		return getPlateformUrlApplication(strCodeApplication)+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +strArea;
 		
 	}
 	
 	
	public static String getPlateformUrlServerApplicationInstances(String strCodeApplication,String strCodeEnvironment)
 	{
 		String strPathEnvironment=strCodeEnvironment.replaceAll(ConstanteUtils.CONSTANTE_SEPARATOR_POINT, ConstanteUtils.CONSTANTE_SEPARATOR_SLASH);
 		return getPlateformUrlApplication(strCodeApplication)+ ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +strPathEnvironment+ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +ConstanteUtils.CONSTANTE_SERVER_TOMCAT;
 		
 	}
 	
 	
 	
	public static String getDeployDirectoryTarget(String strCodeApplication,String strCodeEnvironment,String strCodeServerApplicationInstance)
 	{
 		return getPlateformUrlServerApplicationInstances(strCodeApplication,strCodeEnvironment)	+ConstanteUtils.CONSTANTE_SEPARATOR_SLASH+strCodeServerApplicationInstance+ConstanteUtils.CONSTANTE_SEPARATOR_SLASH+ConstanteUtils.CONSTANTE_SERVER_RO;
 	}
	
	
    /**
     * convert a string to int
     * @param strParameter the string parameter to convert
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
    
    
    
    public static MavenUser getMavenUser(int nIdAdminUser,Locale locale)
    {
    
    	String strIdAttributeLogin=  AppPropertiesService.getProperty(ConstanteUtils.PROPERTY_ADMINUSER_ID_ATTRIBUTE_SVN_LOGIN);
    	String strIdAttributePasssword=  AppPropertiesService.getProperty(ConstanteUtils.PROPERTY_ADMINUSER_ID_ATTRIBUTE_SVN_PASSWORD);
    	
    	MavenUser mavenUser=null;
    	Map<String,Object> mapAttributeUser  =AdminUserFieldService.getAdminUserFields(nIdAdminUser,locale);
    	if(mapAttributeUser.containsKey(strIdAttributeLogin) && mapAttributeUser.containsKey(strIdAttributePasssword))
    	{
    		
    		mavenUser=new MavenUser();
    		mavenUser.setLogin((String)mapAttributeUser.get(strIdAttributeLogin));
    		mavenUser.setPaswword((String)mapAttributeUser.get(strIdAttributePasssword));
    		
    	}
    	
    	return mavenUser;
     }
    
    public static JSONObject getJSONForCommandResult( CommandResult result )
    {
    	JSONObject jo = new JSONObject(  );
    	try
		{
			jo.put( ConstanteUtils.JSON_STATUS, result.getStatus(  ) );
			// pour les logs tr�s longs, on ne prend que la fin
			
			StringBuffer sbLog = result.getLog();
			int nMaxLogSize = AppPropertiesService.getPropertyInt(ConstanteUtils.PROPERTY_MAX_LOG_SIZE,ConstanteUtils.CONSTANTE_ID_NULL);
			String strLog;
			if ( nMaxLogSize == -ConstanteUtils.CONSTANTE_ID_NULL )
			{
				nMaxLogSize = ConstanteUtils.CONSTANTE_DEFAULT_LOG_SIZE;
			}
			// sbLog null entre le lancement du thread et la premiere requete
			if  ( sbLog != null )
			{
				if ( sbLog.length(  ) >  nMaxLogSize )
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
				strLog =  ConstanteUtils.CONSTANTE_EMPTY_STRING;
			}
			jo.put( ConstanteUtils.JSON_LOG, strLog );
			jo.put( ConstanteUtils.JSON_RUNNING, result.isRunning(  ) );
			jo.put( ConstanteUtils.JSON_ID_ERROR, result.getIdError(  ) );
		}
		catch ( JSONException e )
		{
			System.err.println("JSON error : " + e.getMessage(  ) );
		}
    	
    	return jo;
    }
    
    public static  void addWorkflowInformations( JSONObject jo,State state,Collection<Action> listAction )
    {
    	JSONObject joAction;
    	JSONArray joListAction = new JSONArray(  );
    	jo.put(ConstanteUtils.JSON_STATE, state.getName());
    	if(listAction !=null)
    	{
	    	for(Action action:listAction)
	    	{
	    		joAction=new JSONObject();
	    		joAction.put(ConstanteUtils.JSON_ACTION_ID,action.getId());
	    		joAction.put(ConstanteUtils.JSON_ACTION_NAME,action.getName());
	    		joListAction.add(joAction);
	    	}
	    	jo.put(ConstanteUtils.JSON_ACTION_LIST, joListAction);
    	}
    }
    
    
    
    
    public static int  getIdWorkflowSiteDeploy(boolean bTagSiteBeforDeploy)
   {
    	
    	int nIdWorkflowSiteDeploy=ConstanteUtils.CONSTANTE_ID_NULL;
	    if(bTagSiteBeforDeploy)
		{
			
			nIdWorkflowSiteDeploy=AppPropertiesService.getPropertyInt (ConstanteUtils.PROPERTY_ID_WORKFLOW_TAG_AND_DEPLOY_SITE,ConstanteUtils.CONSTANTE_ID_NULL);
				
		}
		else
		{
			nIdWorkflowSiteDeploy=AppPropertiesService.getPropertyInt (ConstanteUtils.PROPERTY_ID_WORKFLOW_DEPLOY_SITE,ConstanteUtils.CONSTANTE_ID_NULL);
		}
	    
	    return nIdWorkflowSiteDeploy;
		
	    
    }


}
