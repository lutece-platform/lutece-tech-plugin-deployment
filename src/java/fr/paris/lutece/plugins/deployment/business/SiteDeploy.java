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

import fr.paris.lutece.plugins.deployment.service.vcs.IVCSService;

import java.sql.Date;


public class SiteDeploy extends AbstractSite
{
    private String _strWebappName;
    private String _strTagName;
    private String _strCodeEnvironment;
    private ServerApplicationInstance _serverApplicationInstance;
    private Date _deploymentDate;

    public String getTagName(  )
    {
        return _strTagName;
    }

    public void setTagName( String strTagName )
    {
        this._strTagName = strTagName;
    }

    public String getCodeEnvironment(  )
    {
        return _strCodeEnvironment;
    }

    public void setCodeEnvironment( String strEnvironment )
    {
        this._strCodeEnvironment = strEnvironment;
    }

    public ServerApplicationInstance getServerApplicationInstance(  )
    {
        return _serverApplicationInstance;
    }

    public void setServerApplicationInstance( ServerApplicationInstance serverApplicationInstance )
    {
        this._serverApplicationInstance = serverApplicationInstance;
    }

    public Date getDeploymentDate(  )
    {
        return _deploymentDate;
    }

    public void setDeploymentDate( Date deploymentDate )
    {
        _deploymentDate = deploymentDate;
    }

    public String getWebappName(  )
    {
        return _strWebappName;
    }

    public void setWebappName( String strWebappName )
    {
        _strWebappName = strWebappName;
    }

    @Override
    public String getCheckoutPath(  )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName(  )
    {
        // TODO Auto-generated method stub
        return null;
    }

}
