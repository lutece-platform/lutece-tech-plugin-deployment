/*
 * Copyright (c) 2002-2018, Mairie de Paris
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

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.vcs.GitUser;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.plugins.deployment.util.FileUtil;
import fr.paris.lutece.plugins.deployment.util.vcs.GitUtils;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;


public class AbstractGitService implements IVCSService<GitUser>
{
    
    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReferenceList getTagsSite(String strUrlSite, GitUser user, String strSiteName ) 
    {
        String strClonePath = DeploymentUtils.getPathCheckoutSite( strSiteName );
        CommandResult commandResult = new CommandResult();
        DeploymentUtils.startCommandResult(commandResult);
        Git git = GitUtils.cloneOrReturnGit( strClonePath, strUrlSite, commandResult, user, null, null);
        Collection<String> listTagName = GitUtils.getTagNameList(git);
        
        ReferenceList refList = new ReferenceList();
        for ( String strTagName : listTagName)
        {
            ReferenceItem item = new ReferenceItem();
            item.setName( strTagName );
            item.setCode( strTagName );
            refList.add( item );
        }
        DeploymentUtils.stopCommandResult( commandResult );
        return refList;
    }

    @Override
    public String doCheckoutSite(String strSiteName, String strUrl, GitUser user, CommandResult commandResult, String strBranch, String strTagName ) {
        String strClonePath = DeploymentUtils.getPathCheckoutSite( strSiteName );
        GitUtils.cloneOrReturnGit( strClonePath, strUrl, commandResult, user, strBranch, strTagName );
        
        return StringUtils.EMPTY;
    }

    @Override
    public ReferenceList getUpgradesFiles(String strSiteName, String strUrlSite, GitUser user) {
        CommandResult commandResult=new CommandResult();
        DeploymentUtils.startCommandResult(commandResult);
        ReferenceList upgradeResults=new ReferenceList();

        doCheckoutSite(strSiteName, strUrlSite, user, commandResult, ConstanteUtils.CONSTANTE_BRANCH_DEVELOP ,null );

        List<String> listUpgradeFiles=FileUtil.list(DeploymentUtils.getPathUpgradeFiles(DeploymentUtils.getPathCheckoutSite( strSiteName )), "sql");

        for(String upgradeFile:listUpgradeFiles)
        {
            upgradeResults.addItem(upgradeFile,upgradeFile);
        }

        DeploymentUtils.stopCommandResult(commandResult);

        return upgradeResults;
		
    }

    @Override
    public String getArtifactId(String strPathRepo) 
    {
        String strRegexExtractArtifact = AppPropertiesService.getProperty( ConstanteUtils.REGEX_GIT_EXTRACT_ARTIFACT_FROM_URL );
        Pattern r = Pattern.compile( strRegexExtractArtifact );
        String strArtifactId = null;

        Matcher m = r.matcher( strPathRepo );
        if (m.find( )) 
        {
           strArtifactId = m.group(2);
        }
        return strArtifactId;
    }
    
    @Override
    public boolean isPrivate() 
    {
        return true;
    }

    @Override
    public void checkAuthentication(String strRepoUrl, GitUser user) 
    {
    }
}
