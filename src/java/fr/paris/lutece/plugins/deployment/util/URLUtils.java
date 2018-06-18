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

import fr.paris.lutece.plugins.deployment.business.InvalidRepositoryUrlException;
import fr.paris.lutece.portal.service.util.AppLogService;
import java.net.MalformedURLException;
import java.net.URL;

public class URLUtils 
{
    /**
     * Get the authority of the given url
     * @param strUrl the url
     * @return the authority of the url
     */
    public static String getAuthority( String strUrl )
    {
        URL url = getURL( strUrl );
        if ( url != null )
        {
            return url.getAuthority();
        }
        return null;
    }
    
    /**
     * Get the path of the given url
     * @param strUrl the path of the url
     * @return the path of the url
     */
    public static String getPath( String strUrl )
    {
        URL url = getURL( strUrl );
        if ( url != null )
        {
            return url.getPath( );
        }
        return null;
    }
    
    /**
     * Transform the provided url string into java.uri.URL
     * @param strUrl the url
     * @return a java URL
     */
    private static URL getURL( String strUrl )
    {
        try
        {
            URL url = new URL( strUrl );
            return url;
        }
        catch ( MalformedURLException e )
        {
            AppLogService.error( "Unable to get URL from strUrl ", e);
        }
        return null;
    }
    
    /**
     * Check if the provided url is well-formed
     * @param strUrl
     * @throws InvalidRepositoryUrlException 
     */
    public static void checkUrl( String strUrl ) throws InvalidRepositoryUrlException
    {
        try
        {
            URL url = new URL( strUrl );
        }
        catch ( MalformedURLException e )
        {
            throw new InvalidRepositoryUrlException( );
        }
    }
}
