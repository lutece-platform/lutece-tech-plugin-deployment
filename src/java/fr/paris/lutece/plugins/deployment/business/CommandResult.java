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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class CommandResult implements Cloneable, Serializable
{
    /**
    *
    */
    private static final long serialVersionUID = 1L;

    // synchro needed?

    /**
     *
     */
    public static int STATUS_EXCEPTION = Integer.MIN_VALUE;
    private StringBuffer _strLog;
    private int _nStatus;
    private boolean _bRunning;
    private String _strIdError;
    private Map<String,String> _mResultInformations=new HashMap<String, String>();

    /**
     * "Getter method" pour la variable {@link #_strLog}
     * @return La variable {@link #_strLog}
     */
    public StringBuffer getLog(  )
    {
        return _strLog;
    }

    /**
     * "Setter method" pour la variable {@link #_strLog}
     * @param strLog La nouvelle valeur de la variable {@link #_strLog}
     */
    public void setLog( StringBuffer strLog )
    {
        _strLog = strLog;
    }

    /**
     * "Getter method" pour la variable {@link #_nStatus}
     * @return La variable {@link #_nStatus}
     */
    public int getStatus(  )
    {
        return _nStatus;
    }

    /**
     * "Setter method" pour la variable {@link #_nStatus}
     * @param nStatus La nouvelle valeur de la variable {@link #_nStatus}
     */
    public void setStatus( int nStatus )
    {
        _nStatus = nStatus;
    }

    /**
     * "Getter method" pour la variable {@link #_bRunning}
     * @return La variable {@link #_bRunning}
     */
    public boolean isRunning(  )
    {
        return _bRunning;
    }

    /**
     * "Setter method" pour la variable {@link #_bRunning}
     * @param bRunning La nouvelle valeur de la variable {@link #_bRunning}
     */
    public void setRunning( boolean bRunning )
    {
        _bRunning = bRunning;
    }

    /**
     *
     *{@inheritDoc}
     */
    @Override
    public Object clone(  ) throws CloneNotSupportedException
    {
        CommandResult clone = (CommandResult) super.clone(  );
        clone._bRunning = this._bRunning;
        clone._nStatus = this._nStatus;
        clone._strLog = this._strLog;
        clone._strIdError = this._strIdError;

        return clone;
    }

    /**
     * "Getter method" pour la variable {@link #_nIdError}
     * @return La variable {@link #_nIdError}
     */
    public String getIdError(  )
    {
        return _strIdError;
    }

    /**
     * "Setter method" pour la variable {@link #_nIdError}
     * @param nIdError La nouvelle valeur de la variable {@link #_nIdError}
     */
    public void setIdError( String strIdError )
    {
        _strIdError = strIdError;
    }

	public Map<String,String> getResultInformations() {
		return _mResultInformations;
	}

	public void setResultInformations(Map<String,String> _mResultInformations) {
		this._mResultInformations = _mResultInformations;
	}
}
