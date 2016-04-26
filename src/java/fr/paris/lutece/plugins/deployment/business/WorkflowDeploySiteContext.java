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
package fr.paris.lutece.plugins.deployment.business;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;


public class WorkflowDeploySiteContext implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String WORKFLOW_RESOURCE_TYPE = "WORKFLOW_DEPLOY_SITE_CONTEXT";
    private int _nId;
    private int _nIdApplication;
    private String _strSvnBaseSiteUrl;
    private HashMap<String, String> _hashServerApplicationInstance;
    private String _strCodeEnvironement;
    private boolean _bTagSiteBeforeDeploy;
    private String _strTagToDeploy;
    private String _strTagName;
    private String _strNextVersion;
    private String _strTagVersion;
    private CommandResult _commandResult;
    private SvnUser _mavenUser;
    private boolean _bDeployWar;
    private boolean _bTagAutomatically;
    private boolean _bDeploySql;
    private String _strDatabaseName;
    private InputStream _scriptFileItem;
    private String _scriptFileItemName;
    private String _scriptFileSelected;
    private boolean _bInitBdd;
    private boolean _bInitAppContext;
    
    public int getId(  )
    {
        return _nId;
    }

    public void setId( int nId )
    {
        _nId = nId;
    }

    public int getIdApplication(  )
    {
        return _nIdApplication;
    }

    public void setIdApplication( int nIdApplication )
    {
        this._nIdApplication = nIdApplication;
    }

    public String getSvnBaseSiteUrl(  )
    {
        return _strSvnBaseSiteUrl;
    }

    public void setSvnBaseSiteUrl( String strSvnUrl )
    {
        _strSvnBaseSiteUrl = strSvnUrl;
    }

    public void setCommandResult( CommandResult _commandResult )
    {
        this._commandResult = _commandResult;
    }

    public CommandResult getCommandResult(  )
    {
        return _commandResult;
    }

    public void setNextVersion( String _strNextVersion )
    {
        this._strNextVersion = _strNextVersion;
    }

    public String getNextVersion(  )
    {
        return _strNextVersion;
    }

    public void setTagVersion( String _strTagVersion )
    {
        this._strTagVersion = _strTagVersion;
    }

    public String getTagVersion(  )
    {
        return _strTagVersion;
    }

    public String getCodeEnvironement(  )
    {
        return _strCodeEnvironement;
    }

    public void setCodeEnvironement( String strCodeEnvironement )
    {
        _strCodeEnvironement = strCodeEnvironement;
    }

    public String getCodeServerInstance( String strType )
    {
        if ( _hashServerApplicationInstance != null )
        {
            return _hashServerApplicationInstance.get( strType );
        }

        return null;
    }

    public void setCodeServerInstance( String strCodeServerAppplicationInstance, String strType )
    {
        if ( _hashServerApplicationInstance == null )
        {
            _hashServerApplicationInstance = new HashMap<String, String>(  );
        }

        _hashServerApplicationInstance.put( strType, strCodeServerAppplicationInstance );
    }

    public boolean isTagSiteBeforeDeploy(  )
    {
        return _bTagSiteBeforeDeploy;
    }

    public void setTagSiteBeforeDeploy( boolean bTagSiteBeforeDeploy )
    {
        _bTagSiteBeforeDeploy = bTagSiteBeforeDeploy;
    }

    public String getTagToDeploy(  )
    {
        return _strTagToDeploy;
    }

    public void setTagToDeploy( String strTagToDeploy )
    {
        _strTagToDeploy = strTagToDeploy;
    }

    public String getTagName(  )
    {
        return _strTagName;
    }

    public void setTagName( String strTagName )
    {
        this._strTagName = strTagName;
    }

    public void setSvnUser( SvnUser _mavenUser )
    {
        this._mavenUser = _mavenUser;
    }

    public SvnUser getMavenUser(  )
    {
        return _mavenUser;
    }

    public void setDeployWar( boolean _bDeployWar )
    {
        this._bDeployWar = _bDeployWar;
    }

    public boolean isDeployWar(  )
    {
        return _bDeployWar;
    }

    public boolean isDeploySql(  )
    {
        return _bDeploySql;
    }

    public void setDeploySql( boolean bDeploySql )
    {
        _bDeploySql = bDeploySql;
    }

	public String getDatabaseName() {
		return _strDatabaseName;
	}

	public void setDatabaseName(String _strDatabaseName) {
		this._strDatabaseName = _strDatabaseName;
	}

	public String getScriptFileItemName() {
		return _scriptFileItemName;
	}

	public void setScriptFileItemName(String _scriptFileItemName) {
		this._scriptFileItemName = _scriptFileItemName;
	}
	

	public InputStream getScriptFileItem() {
		return _scriptFileItem;
	}

	public void setScriptFileItem(InputStream _scriptFileItem) {
		this._scriptFileItem = _scriptFileItem;
	}

	public String getScriptFileSelected() {
		return _scriptFileSelected;
	}

	public void setScriptFileSelected(String _scriptFileSelected) {
		this._scriptFileSelected = _scriptFileSelected;
	}

	public boolean isTagAutomatically() {
		return _bTagAutomatically;
	}

	public void setTagAutomatically(boolean _bDeployWarFast) {
		this._bTagAutomatically = _bDeployWarFast;
	}

	public boolean isInitBdd() {
		return _bInitBdd;
	}

	public void setInitBdd(boolean _bInitBdd) {
		this._bInitBdd = _bInitBdd;
	}

	public boolean isInitAppContext() {
		return _bInitAppContext;
	}

	public void setInitAppContext(boolean _bInitAppContext) {
		this._bInitAppContext = _bInitAppContext;
	}

	
	
}
