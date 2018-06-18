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
package fr.paris.lutece.plugins.deployment.util;

import fr.paris.lutece.plugins.deployment.business.Application;
import fr.paris.lutece.plugins.deployment.business.InvalidRepositoryUrlException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

public class RepositoryUtils 
{
    /**
     * Check if the provided Application has a recognized repo url, otherwise, throws an exception
     * @param application the Application
     * @throws InvalidRepositoryUrlException if the given application repo url isn't recognized 
     */
    public static void checkRepository ( Application application ) throws InvalidRepositoryUrlException
    {
        String strUrlRepo = application.getUrlRepo( );
        if ( !strUrlRepo.isEmpty( ) )
        {
            URLUtils.checkUrl( strUrlRepo );
            String strPath = URLUtils.getPath( strUrlRepo );               

            String strSvnUrlSites = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_SVN_SITES_URL );
            String strGithubUrlSites = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_GITHUB_BASE_URL );
            String strGitlabUrlSites = AppPropertiesService.getProperty( ConstanteUtils.PROPERTY_GITLAB_BASE_URL );

            if ( strUrlRepo.startsWith( strSvnUrlSites ) )
            {
                application.setRepoType( ConstanteUtils.CONSTANTE_REPO_TYPE_SVN );
            }
            else if ( strUrlRepo.startsWith( strGithubUrlSites ) )
            {

                List<String> listAuthorizedGithubRepoKeys = AppPropertiesService.getKeys( ConstanteUtils.PROPERTY_GITHUB_AUTHORIZED_REPO_NAME );
                List<String> listAuthorizedGithubRepo = new ArrayList<String>();
                for ( String strKey : listAuthorizedGithubRepoKeys )
                {
                    listAuthorizedGithubRepo.add( AppPropertiesService.getProperty( strKey ) );
                }

                String strRepoName = strPath.split( ConstanteUtils.CONSTANTE_SEPARATOR_SLASH )[1];
                if ( listAuthorizedGithubRepo.contains( strRepoName ) )
                {
                    application.setRepoType( ConstanteUtils.CONSTANTE_REPO_TYPE_GITHUB );
                }
                else
                {
                    throw new InvalidRepositoryUrlException();
                }
            }
            else if ( strUrlRepo.startsWith( strGitlabUrlSites ) )
            {
                application.setRepoType( ConstanteUtils.CONSTANTE_REPO_TYPE_GITLAB );
            }
            else
            {
                throw new InvalidRepositoryUrlException();
            }
            application.setArtifactId( DeploymentUtils.getVCSService( application.getRepoType( ) ).getArtifactId( strUrlRepo ) );
        }
        else
        {
            throw new InvalidRepositoryUrlException();
        }
    }
    
    /**
     * Check if the provided urlRepo is recognized as a correct repository url
     * @param strUrlRepo the repo of the url
     * @return a JSON mapped on a RepoValidation object
     */
    public static String checkRepository ( String strUrlRepo )
    {
        ObjectMapper mapper = new ObjectMapper();
        RepoValidation repoValidation = new RepoValidation();

        Application application= new Application( );
        application.setUrlRepo( strUrlRepo );
        try
        {
            checkRepository( application );
            repoValidation.setIsUrlValid( true );
            repoValidation.setRepoType( application.getRepoType( ) );
        }
        catch ( InvalidRepositoryUrlException e )
        {
            repoValidation.setIsUrlValid( false );
        }
        try 
        {
            return mapper.writeValueAsString( repoValidation );
        }
        catch ( IOException e )
        {
            AppLogService.error( "Unable to serealize repoValidation as Json obj", e);
        }
        return StringUtils.EMPTY;

    }
    
    /**
     * This class is only used for the JSON object mapper
     */
    private static class RepoValidation
    {
        @JsonProperty("valid_url")
        private boolean _isUrlValid;
        @JsonProperty("repo_type")
        private String _strRepoType;

        @JsonProperty("valid_url")
        public boolean isIsUrlValid( ) 
        {
            return _isUrlValid;
        }

        @JsonProperty("valid_url")
        public void setIsUrlValid( boolean isUrlValid ) 
        {
            _isUrlValid = isUrlValid;
        }

        @JsonProperty("repo_type")
        public String getRepoType( ) 
        {
            return _strRepoType;
        }

        @JsonProperty("repo_type")
        public void setRepoType(String strRepoType )
        {
            _strRepoType = strRepoType;
        }

    }
}
