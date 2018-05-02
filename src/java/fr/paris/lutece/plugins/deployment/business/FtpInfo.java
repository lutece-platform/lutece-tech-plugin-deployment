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

public class FtpInfo
{
    private String _strHost;
    private int _nPort;
    private String _strUserLogin;
    private String _strUserPassword;
    private String _strProxyHost;
    private int _nProxyPort;
    private String _strProxyUserLogin;
    private String _strProxyUserPassword;
    private long _lKeepAliveTimeout;

    public String getHost(  )
    {
        return _strHost;
    }

    public void setHost( String strHost )
    {
        _strHost = strHost;
    }

    public int getPort(  )
    {
        return _nPort;
    }

    public void setPort( int nPort )
    {
        _nPort = nPort;
    }

    public String getUserLogin(  )
    {
        return _strUserLogin;
    }

    public void setUserLogin( String strUserLogin )
    {
        _strUserLogin = strUserLogin;
    }

    public String getUserPassword(  )
    {
        return _strUserPassword;
    }

    public void setUserPassword( String strUserPassword )
    {
        _strUserPassword = strUserPassword;
    }

    public String getProxyHost(  )
    {
        return _strProxyHost;
    }

    public void setProxyHost( String strHost )
    {
        _strProxyHost = strHost;
    }

    public int getProxyPort(  )
    {
        return _nProxyPort;
    }

    public void setProxyPort( int port )
    {
        _nProxyPort = port;
    }

    public String getProxyUserLogin(  )
    {
        return _strProxyUserLogin;
    }

    public void setProxyUserLogin( String strUserLogin )
    {
        _strProxyUserLogin = strUserLogin;
    }

    public String getProxyUserPassword(  )
    {
        return _strProxyUserPassword;
    }

    public void setProxyUserPassword( String strUserPassword )
    {
        _strProxyUserPassword = strUserPassword;
    }

    public void setKeepAliveTimeout( long _lKeepAliveTimeout )
    {
        this._lKeepAliveTimeout = _lKeepAliveTimeout;
    }

    public long getKeepAliveTimeout(  )
    {
        return _lKeepAliveTimeout;
    }
}
