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

import java.util.List;


public abstract class Action implements IAction
{
    private String _strCode;
    private String _strName;
    private String _strI18nKeyName;
    private Integer _strStatus;
    private boolean _bUsedForStatus;
    private String _strIconCssClass;
    private boolean _bDisplay;
    private List<String> _listParameters;
    private String _strServerType;
    private boolean _bStopWorkflowIfExecutionError;

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.deployment.business.IAction#setCode(java.lang.String)
     */
    public void setCode( String _strCode )
    {
        this._strCode = _strCode;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.deployment.business.IAction#getCode()
     */
    public String getCode(  )
    {
        return _strCode;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.deployment.business.IAction#setName(java.lang.String)
     */
    public void setName( String _strName )
    {
        this._strName = _strName;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.deployment.business.IAction#getName()
     */
    public String getName(  )
    {
        return _strName;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.deployment.business.IAction#setI18nKeyName(java.lang.String)
     */
    public void setI18nKeyName( String _strI18nKeyName )
    {
        this._strI18nKeyName = _strI18nKeyName;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.deployment.business.IAction#getI18nKeyName()
     */
    public String getI18nKeyName(  )
    {
        return _strI18nKeyName;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.deployment.business.IAction#setStatus(java.lang.Integer)
     */
    public void setStatus( Integer _strStatus )
    {
        this._strStatus = _strStatus;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.deployment.business.IAction#getStatus()
     */
    public Integer getStatus(  )
    {
        return _strStatus;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.deployment.business.IAction#setUsedForStatus(boolean)
     */
    public void setUsedForStatus( boolean _bUsedForStatus )
    {
        this._bUsedForStatus = _bUsedForStatus;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.deployment.business.IAction#isUsedForStatus()
     */
    public boolean isUsedForStatus(  )
    {
        return _bUsedForStatus;
    }

    public void setIconCssClass( String _strIconCssClass )
    {
        this._strIconCssClass = _strIconCssClass;
    }

    public String getIconCssClass(  )
    {
        return _strIconCssClass;
    }

    public boolean isDisplay(  )
    {
        // TODO Auto-generated method stub
        return _bDisplay;
    }

    public void setDisplay( boolean bDisplay )
    {
        _bDisplay = bDisplay;
    }

    public void setParameters( List<String> _listParameters )
    {
        this._listParameters = _listParameters;
    }

    public List<String> getParameters(  )
    {
        return _listParameters;
    }

    public void setServerType( String _strServerType )
    {
        this._strServerType = _strServerType;
    }

    public String getServerType(  )
    {
        return _strServerType;
    }

	public boolean isStopWorkflowIfExecutionError() {
		return _bStopWorkflowIfExecutionError;
	}

	public void setStopWorkflowIfExecutionError(
			boolean _bStopWorkflowIfExecutionError) {
		this._bStopWorkflowIfExecutionError = _bStopWorkflowIfExecutionError;
	}
}
