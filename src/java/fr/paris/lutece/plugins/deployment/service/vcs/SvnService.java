/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.plugins.deployment.service.vcs;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.vcs.SvnUser;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.plugins.deployment.util.FileUtil;
import fr.paris.lutece.plugins.deployment.util.ReleaseSVNCheckoutClient;
import fr.paris.lutece.plugins.deployment.util.vcs.SVNUtils;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;


public class SvnService implements IVCSService<SvnUser>
{
    // private static IVCSService _singleton;
  
    //    public static IVCSService getInstance(  )
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
         * @see fr.paris.lutece.plugins.deployment.service.IVCSService#init()
         */
    public void init(  )
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
    
    /* (non-Javadoc)
         * @see fr.paris.lutece.plugins.deployment.service.IVCSService#getTagsSite(java.lang.String, fr.paris.lutece.plugins.deployment.business.User)
         */
    public ReferenceList getTagsSite( String strUrlSite, SvnUser user, String strSiteName )
    {
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( user.getLogin(  ),
                user.getPassword(  ) );
        ISVNOptions options = SVNWCUtil.createDefaultOptions( true );
        SVNClientManager clientManager = SVNClientManager.newInstance( options, authManager );
        ReferenceList listReferenceItems = null;

        try
        {
            listReferenceItems = SVNUtils.getSvnDirChildren( strUrlSite + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH +
                    ConstanteUtils.CONSTANTE_TAGS, clientManager );
        }
        catch ( Exception e )
        {
        	AppLogService.error( e );
        }

        return listReferenceItems;
    }

    public String doCheckoutSite( String strSiteName, String strRepoUrl, SvnUser user, CommandResult commandResult, String strBranch, String strTagToDeploy )
    {
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( user.getLogin(  ),
                user.getPassword(  ) );
        String strSiteLocalBasePath = DeploymentUtils.getPathCheckoutSite( strSiteName );

        if ( StringUtils.isNotBlank( strSiteName ) && ( user != null ) )
        {
            ReleaseSVNCheckoutClient updateClient = new ReleaseSVNCheckoutClient( authManager,
                    SVNWCUtil.createDefaultOptions( false ) );

            String strError = null;
            
            String strSvnCheckoutSiteUrl = null;
            if ( strTagToDeploy != null )
            {
                strSvnCheckoutSiteUrl = getUrlTagSite( strRepoUrl, strTagToDeploy );
            }
            else
            {
                strSvnCheckoutSiteUrl = getUrlDevSite( strRepoUrl );
            }


            try
            {
                strError = SVNUtils.doSvnCheckoutSite( strSiteName, strSvnCheckoutSiteUrl, strSiteLocalBasePath, updateClient,
                        commandResult );
            }
            catch ( Exception e )
            {
            	DeploymentUtils.addTechnicalError(commandResult,"errreur lors du checkout du site "+ e.getMessage(),e);
            }
        }
        else
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

	@Override
	public ReferenceList getUpgradesFiles(String strSiteName, String strUrlSite, SvnUser user) {
		
		
		CommandResult commandResult=new CommandResult();
		DeploymentUtils.startCommandResult(commandResult);
		ReferenceList upgradeResults=new ReferenceList();
		// TODO Auto-generated method stub
		String strUrlTrunkSite= getUrlDevSite( strUrlSite );
		
		doCheckoutSite(strSiteName, strUrlTrunkSite, user, commandResult, null, null);
		
		List<String> listUpgradeFiles=FileUtil.list(DeploymentUtils.getPathUpgradeFiles(DeploymentUtils.getPathCheckoutSite( strSiteName )), "sql");
                
		for(String upgradeFile:listUpgradeFiles)
		{
			upgradeResults.addItem(upgradeFile,upgradeFile);
			
		}
		
		DeploymentUtils.stopCommandResult(commandResult);
		
		return upgradeResults;
		
	}
        
    public ReferenceList getSVNDirChildren( String strRepoUrl, SvnUser user )
    {
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( user.getLogin(  ),
                user.getPassword(  ) );
        ISVNOptions options = SVNWCUtil.createDefaultOptions( true );
        SVNClientManager clientManager = SVNClientManager.newInstance( options, authManager );
        try
        {
            return SVNUtils.getSvnDirChildren( strRepoUrl, clientManager );
        }
        catch ( SVNException e )
        {
            return null;
        }

    }

    @Override
    public String getArtifactId( String strPathRepo ) 
    {
        String[] tabPathSplit = strPathRepo.split( ConstanteUtils.CONSTANTE_SEPARATOR_SLASH );
        return tabPathSplit[ tabPathSplit.length - 1];
    }

    public String getUrlTagSite(String strBaseUrl, String strTagName) {
        return strBaseUrl + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + ConstanteUtils.CONSTANTE_TAGS +
        ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + strTagName;
    }

    public String getUrlDevSite(String strBaseUrl) 
    {
        return strBaseUrl + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + ConstanteUtils.CONSTANTE_TRUNK;    
    }
    
    @Override
    public boolean isPrivate( )
    {
        return true;
    }

    @Override
    public void checkAuthentication(String strRepoUrl, SvnUser user) 
    {
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( user.getLogin(  ),
                user.getPassword(  ) );
        if ( !SVNUtils.checkAuthentication( authManager, strRepoUrl))
        {
            throw new AppException( "Bad credentials to SVN : Unauthorized");
        }
    }
}
