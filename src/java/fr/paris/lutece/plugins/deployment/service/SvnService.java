package fr.paris.lutece.plugins.deployment.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.tmatesoft.sqljet.core.internal.lang.SqlParser.result_column_return;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNEventAction;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.FilterDeployment;
import fr.paris.lutece.plugins.deployment.business.MavenUser;
import fr.paris.lutece.plugins.deployment.svn.ReleaseSVNCommitClient;
import fr.paris.lutece.plugins.deployment.svn.ReleaseSVNCopyClient;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.plugins.deployment.util.ReleaseSVNCheckoutClient;
import fr.paris.lutece.plugins.deployment.util.ReleaseUtils;
import fr.paris.lutece.plugins.deployment.util.SVNUtils;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

public class SvnService implements ISvnService
{
   
  
    
   // private static ISvnService _singleton;
    private static final Logger logger = Logger.getLogger( SvnService.class );

//    public static ISvnService getInstance(  )
//    {
//        if ( _singleton == null )
//        {
//            _singleton = new SvnService(  );
//        }
//
//        return _singleton;
//    }

    private SvnService(  )
    {
        init(  );
    }

    /* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.ISvnService#init()
	 */
    public void init(  )
    {
    	/*
	     * For using over http:// and https://
	     */
	    DAVRepositoryFactory.setup();
	    /*
	     * For using over svn:// and svn+xxx://
	     */
	    SVNRepositoryFactoryImpl.setup();
	    
	    /*
	     * For using over file:///
	     */
	    FSRepositoryFactory.setup();
    	
    }

    /* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.ISvnService#getSites(fr.paris.lutece.plugins.deployment.business.FilterDeploiement, fr.paris.lutece.plugins.deployment.business.User)
	 */
    public ReferenceList getSites( FilterDeployment filter,MavenUser user )
    {
    	ISVNAuthenticationManager authManager= SVNWCUtil.createDefaultAuthenticationManager( user.getLogin(), user.getPasword() );
    	ISVNOptions options = SVNWCUtil.createDefaultOptions( true );
		SVNClientManager clientManager=SVNClientManager.newInstance( options,authManager );
    	
		ReferenceList listReferenceItems = null;
         try
        {
            listReferenceItems = SVNUtils.getSvnSites( AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_SVN_SITES_URL ) +
                                                       filter.getCodeCategory(  ),clientManager);
        } catch ( Exception e )
        {
            logger.error( e );
        }

        return listReferenceItems;
    }

    /* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.ISvnService#getTagsSite(java.lang.String, fr.paris.lutece.plugins.deployment.business.User)
	 */
    public ReferenceList getTagsSite( String strUrlSite ,MavenUser user)
    {
    	ISVNAuthenticationManager authManager= SVNWCUtil.createDefaultAuthenticationManager( user.getLogin(), user.getPasword() );
    	ISVNOptions options = SVNWCUtil.createDefaultOptions( true );
		SVNClientManager clientManager=SVNClientManager.newInstance( options ,authManager);
		ReferenceList listReferenceItems = null;

        try
        {
            listReferenceItems = SVNUtils.getSvnSites( strUrlSite + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + ConstanteUtils.CONSTANTE_TAGS,
            		clientManager);
        } catch ( Exception e )
        {
            logger.error( e );
        }

        return listReferenceItems;
    }

   


    public String doSvnCheckoutSite( String strSiteName, String strSvnUrl,MavenUser user,CommandResult commandResult )
    {
      	
    	ISVNAuthenticationManager authManager= SVNWCUtil.createDefaultAuthenticationManager( user.getLogin(), user.getPasword() );
    	String strSiteLocalBasePath = DeploymentUtils.getPathCheckoutSite(strSiteName);
    	
    	 if( StringUtils.isNotBlank( strSiteName ) && user!=null )
         {	
          
            ReleaseSVNCheckoutClient updateClient =
                new ReleaseSVNCheckoutClient( authManager,
                                              SVNWCUtil.createDefaultOptions( false ) );

            String strError= null;

            try
            {
                strError= SVNUtils.doSvnCheckoutSite( strSiteName, strSvnUrl, strSiteLocalBasePath , updateClient,commandResult );
            } catch ( Exception e )
            {
                logger.error( e );

               
            }

                        
         }else
         {
        	 
//        	 ErrorCommandThread thread;
//         	if ( StringUtils.isBlank( strSiteName) )
//         	{
//             	thread = new ErrorCommandThread( AppPropertiesService
//                        .getProperty( ConstanteUtils.PROPERTY_MESSAGE_CHECKOUT_ERROR_SITE_EMPTY ), ConstanteUtils.CONSTANTE_CHECKOUT_ERROR );
//         	}
//         	else
//         	{
//             	thread = new ErrorCommandThread( AppPropertiesService
//                        .getProperty(ConstanteUtils.PROPERTY_MESSAGE_CHECKOUT_ERROR_LOGIN_MDP_EMPTY ), ConstanteUtils.CONSTANTE_CHECKOUT_ERROR );
//         	}
//         	
//         	ThreadUtils.launchThread( thread, user );
//        	 
         }
        
    	
        return ConstanteUtils.CONSTANTE_EMPTY_STRING;
    }

    /* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.ISvnService#doSvnTagSite(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, fr.paris.lutece.plugins.deployment.business.User)
	 */
    public String doSvnTagSite( String strSiteName, String strUrlSite, String strTagName,String strNextVersion,String strVersion, MavenUser user,CommandResult commandResult )
    {
    	String strSiteLocalBasePath = DeploymentUtils.getPathCheckoutSite(strSiteName);
    	    
    	ISVNAuthenticationManager authManager= SVNWCUtil.createDefaultAuthenticationManager( user.getLogin(), user.getPasword() );
    	
    	String strSrcURL = SVNUtils.getSvnUrlTrunkSite(strUrlSite);
    	String strDstURL =SVNUtils.getSvnUrlTagSite(strUrlSite, strTagName);
    	
        ReleaseSVNCommitClient commitClient = new ReleaseSVNCommitClient( authManager, SVNWCUtil.createDefaultOptions( false ) );
		ReleaseSVNCopyClient copyClient = new ReleaseSVNCopyClient( authManager, SVNWCUtil.createDefaultOptions( false ) );
    	
    	    
    	
    	
    	
    	 try
         {
             
    		 final StringBuffer sbLog = commandResult.getLog();
             commitClient.setEventHandler( new ISVNEventHandler(  )
                 {
                     public void checkCancelled(  )
                                         throws SVNCancelException
                     {
                         // Do nothing
                     }

                     public void handleEvent( SVNEvent event, double progress )
                                      throws SVNException
                     {
                         sbLog.append( ( 
                                           ( event.getAction(  ) == SVNEventAction.COMMIT_ADDED ) ? "Commit "
                                                                                                  : event.getAction(  )
                                        ) + " " + ObjectUtils.toString( event.getFile(  ) ) + "\n" );
                     }
                 } );

             	copyClient.setEventHandler( new ISVNEventHandler(  )
                 {
                     public void checkCancelled(  )
                                         throws SVNCancelException
                     {
                         // Do nothing
                     }

                     public void handleEvent( SVNEvent event, double progress )
                                      throws SVNException
                     {
                         sbLog.append( ( ( event.getAction(  ) == SVNEventAction.COPY ) ? "Tag " : event.getAction(  ) ) +
                                       " " + ObjectUtils.toString( event.getFile(  ) ) + "\n" );
                     }
                 } );

             sbLog.append( "Preparing tag\n" );
             sbLog.append( "Updating pom version to " + strVersion + "...\n" );
             sbLog.append( ReleaseUtils.updateReleaseVersion(strSiteLocalBasePath, strVersion, "[site-release] Prepare tag for " + strSiteName,commitClient) );
             sbLog.append( "Pom updated\n" );

             sbLog.append( "Tagging site to " + strTagName + "...\n" );
             SVNUtils.doTagSite( strSiteName, strTagName,strSrcURL,strDstURL, copyClient );
             sbLog.append( "Tag done\n" );

             sbLog.append( "Updating pom to next development " + strNextVersion + "\n" );
             sbLog.append( ReleaseUtils.updateReleaseVersion(strSiteLocalBasePath,strNextVersion,
                                                 "[site-release] Update pom version for " + strSiteName,commitClient) );
             sbLog.append( "Pom updated\n" );
             
         
            
         } catch ( Exception e )
         {
             //commandResult.setStatus( ICommandThread.STATUS_EXCEPTION );
             

             StringWriter sw = new StringWriter(  );
             PrintWriter pw = new PrintWriter( sw );
             e.printStackTrace( pw );

             String errorLog = sw.toString(  );
             pw.flush(  );
             pw.close(  );

             try
             {
                 sw.flush(  );
                 sw.close(  );
             } catch ( IOException e1 )
             {
                 // do nothing
                 e1.printStackTrace(  );
             }

            commandResult.getLog(  ).append( errorLog ) ;
             /**
             _result.setIdError( ReleaseLogger.logError( 
                                                         _result.getLog(  ).toString(  ),
                                                         e ) );
                                                         **/
         }

    	
    	
    	
    	
    	
    	
    	
    	
    	return ConstanteUtils.CONSTANTE_EMPTY_STRING;
        
      }


}
