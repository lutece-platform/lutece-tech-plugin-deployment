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
package fr.paris.lutece.plugins.deployment.business;

import fr.paris.lutece.plugins.deployment.service.ApplicationService;
import fr.paris.lutece.portal.service.rbac.RBACResource;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupResource;

public class Application implements AdminWorkgroupResource, RBACResource
{
    public static final String RESOURCE_TYPE = "DEPLOYMENT_APPLICATION";
    private int _nIdApplication;
    private String _strCode;
    private String _strName;
    private String _strWebappName;
    private String _strArtifactId;
    private String _strUrlRepo;
    private String _strRepoType;
    private String _strWorkgroup;
    private boolean _bLuteceSite;
    private String _strMavenCustomDeployGoal;

    public void setIdApplication( int _nIdApplication )
    {
        this._nIdApplication = _nIdApplication;
    }

    public int getIdApplication( )
    {
        return _nIdApplication;
    }

    public String getCode( )
    {
        return _strCode;
    }

    public void setCode( String strCode )
    {
        _strCode = strCode;
    }

    public String getName( )
    {
        return _strName;
    }

    public void setName( String strName )
    {
        _strName = strName;
    }

    public String getWebAppName( )
    {
        return _strWebappName;
    }

    public void setWebAppName( String strName )
    {
        _strWebappName = strName;
    }

    public String getWorkgroup( )
    {
        return _strWorkgroup;
    }

    public void setWorkgroup( String workGroup )
    {
        _strWorkgroup = workGroup;
    }

    @Override
    public String getResourceTypeCode( )
    {
        // TODO Auto-generated method stub
        return RESOURCE_TYPE;
    }

    @Override
    public String getResourceId( )
    {

        return Integer.toString( getIdApplication( ) );
    }

    /**
     * Get the URL of the repo
     * 
     * @return the URL of the repo
     */
    public String getUrlRepo( )
    {
        return _strUrlRepo;
    }

    /**
     * Set the URL of the repo
     * 
     * @param strUrlRepo
     *            the Url of the Repo
     */
    public void setUrlRepo( String strUrlRepo )
    {
        _strUrlRepo = strUrlRepo;
    }

    /**
     * Get the repo type of the application
     * 
     * @return the repo type
     */
    public String getRepoType( )
    {
        return _strRepoType;
    }

    /**
     * Set the repo type of the application
     * 
     * @param strRepoType
     *            the repo type
     */
    public void setRepoType( String strRepoType )
    {
        _strRepoType = strRepoType;
    }

    /**
     * Get the artifact id of the application
     * 
     * @return the artifact id of the application
     */
    public String getArtifactId( )
    {
        return _strArtifactId;
    }

    /**
     * Set the artifact if of the application
     * 
     * @param strArtifactId
     *            the artifact id of the application
     */
    public void setArtifactId( String strArtifactId )
    {
        this._strArtifactId = strArtifactId;
    }

    /**
     * Check if the application has a private repo
     * 
     * @return true if the application has a private repo, false otherwise
     */
    public boolean hasPrivateRepo( )
    {
        return ApplicationService.isPrivateRepo( this );
    }

    /**
     * Get the is LuteceSite boolean
     * @return return true if the application is a Lutece App, false otherwise
     */
    public boolean isLuteceSite( ) 
    {
        return _bLuteceSite;
    }

    /**
     * Set the Lutece Site boolean
     * @param bLuteceSite the Lutece Site boolean
     */
    public void setLuteceSite(boolean bLuteceSite) 
    {
        _bLuteceSite = bLuteceSite;
    }

    /**
     * Get the maven custom deploy goal
     * @return the custom maven deploy goal
     */
    public String getMavenCustomDeployGoal() 
    {
        return _strMavenCustomDeployGoal;
    }

    /**
     * Get the maven custom deploy goal
     * @param _strMavenCustomDeployGoal
     *                  The custom Maven Custom deploy goal
     */
    public void setMavenCustomDeployGoal(String _strMavenCustomDeployGoal) 
    {
        this._strMavenCustomDeployGoal = _strMavenCustomDeployGoal;
    }
    
    

}
