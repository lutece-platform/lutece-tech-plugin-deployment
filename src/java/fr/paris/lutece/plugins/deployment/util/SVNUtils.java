package fr.paris.lutece.plugins.deployment.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.ISVNDirEntryHandler;
import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNEventAction;
import org.tmatesoft.svn.core.wc.SVNRevision;

import fr.paris.lutece.plugins.deployment.business.Application;
import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

public final class SVNUtils
{
    private static final String MESSAGE_ERROR_SVN =
        "Impossible de se connecter au SVN. Veuillez verifier vos identifiants";
    private static final String CONSTANTE_SLASH = "/";
    private static final Logger logger = Logger.getLogger( SVNUtils.class );

    /**
     * Constructeur vide
     */
    private SVNUtils(  )
    {
        // nothing
    }

    /**
     * Initialise les diff�rentes factory pour le svn
     */
    public static void init(  )
    {
        /*
        * For using over http:// and https:/
        */
        DAVRepositoryFactory.setup(  );
        /*
         * For using over svn:// and svn+xxx:/
         */
        SVNRepositoryFactoryImpl.setup(  );

        /*
         * For using over file://
         */
        FSRepositoryFactory.setup(  );
    }

    /**
     * Tag un site
     * @param strSiteName le nom du site
     * @param strTagName le nom du tag
     * @param copyClient le client svn permettant la copie
     * @throws SVNException
     */
    public static String doTagSite( String strSiteName, String strTagName, String strSrcURL, String strDstURL,
                                    SVNCopyClient copyClient )
                            throws SVNException
    {
        // COPY from trunk to tags/tagName
        SVNURL srcURL = SVNURL.parseURIEncoded( strSrcURL );
        SVNURL dstURL = SVNURL.parseURIEncoded( strDstURL );
        SVNCopySource svnCopySource = new SVNCopySource( SVNRevision.HEAD, SVNRevision.HEAD, srcURL );
        SVNCopySource[] tabSVNCopy = new SVNCopySource[1];
        tabSVNCopy[0] = svnCopySource;

        SVNCommitInfo info =
            copyClient.doCopy( tabSVNCopy, dstURL, false, false, false,
                               "[site-release] Tag site " + strSiteName + " to " + strTagName, null );

        if ( info.getErrorMessage(  ) != null )
        {
            return info.getErrorMessage(  ).getMessage(  );
        }

        return null;
    }

    public static String doSvnCheckoutSite( String strSiteName, String strUrl, String strCheckoutBaseSitePath,
                                                       ReleaseSVNCheckoutClient updateClient,CommandResult result )
                                               throws SVNException
    {
        SVNURL url = SVNURL.parseURIEncoded( strUrl );
        File file = new File( strCheckoutBaseSitePath );
        if(file.exists())
        {
        	if(!FileUtil.delete(file, result.getLog()))
        	{
        		result.setIdError(result.getLog().toString()); 
        		return result.getLog().toString();
        	}
        	
        }
        SVNRepository repository = SVNRepositoryFactory.create( url, null );
        final StringBuffer logBuffer=	result.getLog();
             
        try
        {
          
            
             updateClient.setEventHandler( new ISVNEventHandler(  )
                {
                    public void checkCancelled(  )
                                        throws SVNCancelException
                    {
                        // Do nothing
                    }
                    
                    public void handleEvent( SVNEvent event, double progress )
                                     throws SVNException
                    {
                    	logBuffer.append( ( 
                                          ( event.getAction(  ) == SVNEventAction.UPDATE_ADD ) ? "ADDED "
                                                                                               : event.getAction(  )
                                       ) + " " + event.getFile(  ) + "\n" );
                    }
                } );

          
            // SVNDepth.INFINITY + dernier param�tre � FALSE pour la version 1.3.2
            updateClient.doCheckout( repository.getLocation(  ), file, SVNRevision.HEAD, SVNRevision.HEAD, true );
            
            

         } catch ( SVNAuthenticationException e )
        {
//            _result.getLog(  ).append( CONSTANTE_NO_LOGIN_PASSWORD );
//            _result.setStatus( ICommandThread.STATUS_EXCEPTION );
//            _result.setRunning( false );

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
              //  _logger.error( e1 );
            }

//            _result.setLog( _result.getLog(  ).append( errorLog ) );
//            _logger.error( e );

            //_result.setIdError( ReleaseLogger.logError( _result.getLog(  ).toString(  ), e ) );
        } catch ( Exception e )
        {
//            _result.setStatus( ICommandThread.STATUS_EXCEPTION );
//            _result.setRunning( false );

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
//                _logger.error( e1 );
            }

//            _result.setLog( _result.getLog(  ).append( errorLog ) );
            //_result.setIdError( ReleaseLogger.logError( _result.getLog(  ).toString(  ), e ) );
//            _logger.error( e );
        }
        
        
        
        
        
        
        

        return null;
    }

    public static ReferenceList getSvnSites( String strUrlSite, SVNClientManager clientManager )
                                           throws SVNException
    {
        
    	
    	final ReferenceList listSites = new ReferenceList();
        final SVNURL url;

        url = SVNURL.parseURIEncoded( strUrlSite );

        SVNRepository repository = SVNRepositoryFactory.create( url, null );

        clientManager.getLogClient(  ).doList( repository.getLocation(  ),
                                               SVNRevision.HEAD,
                                               SVNRevision.HEAD,
                                               false,
                                               false,
                                               new ISVNDirEntryHandler(  )
            {
                public void handleDirEntry( SVNDirEntry entry )
                                    throws SVNException
                {
                	ReferenceItem referenceItem;
                    if ( ! url.equals( entry.getURL(  ) ) )
                    {
                        if ( entry.getKind(  ) == SVNNodeKind.DIR )
                        {
                          listSites.addItem(entry.getName(), entry.getName());
                        }
                    }
                }
            } );

        return listSites;
    }
    
    public static String getSvnUrlTrunkSite( String strUrlSite)
    {
    	return strUrlSite + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + ConstanteUtils.CONSTANTE_TRUNK;
    	
    }

    public static String getSvnUrlTagSite( String strUrlSite, String strTagName)
    {
        return strUrlSite +ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + ConstanteUtils.CONSTANTE_TAGS + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + strTagName;

      }
    public static String getSvnUrlSite(Application application)
    {
    	String strUrlSite=AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_SVN_SITES_URL ) +
    	application.getCodeCategory()+ConstanteUtils.CONSTANTE_SEPARATOR_SLASH+application.getSiteName();	
    	return strUrlSite;
    }
    
   
    
   
	
	

    
}
