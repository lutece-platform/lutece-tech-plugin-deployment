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
package fr.paris.lutece.plugins.deployment.service;

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.FtpInfo;
import fr.paris.lutece.test.LuteceTestCase;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

import java.net.URL;

import java.util.Properties;


/**
 *
 * FtpServiceTest
 *
 */
public class FtpServiceTest extends LuteceTestCase
{
    /**
     * Test of testUploadFile
     */
    @Test
    public void testUploadFile(  )
    {
        String strFtpHost = getTestProperties( "deployment.serverApplicationFtp.host" );
        String strFtpPort = getTestProperties( "deployment.serverApplicationFtp.port" );
        String strFtpUserLogin = getTestProperties( "deployment.serverApplicationFtp.userLogin" );
        String strFtpUserPassword = getTestProperties( "deployment.serverApplicationFtp.userPassword" );
        String strFtpFileLocalName = getTestProperties( "deployment.ftp.fileLocalName" );
        String strFtpFileLocalPath = getTestProperties( "deployment.ftp.fileLocalPath" );
        String strFtpRemoteDirectoryTarget = getTestProperties( "deployment.ftp.remoteDirectoryTarget" );

        IFtpService _ftpService = new FtpService(  );

        FtpInfo ftpInfo = new FtpInfo(  );
        ftpInfo.setHost( strFtpHost );
        ftpInfo.setPort( Integer.parseInt( strFtpPort ) );
        ftpInfo.setUserLogin( strFtpUserLogin );
        ftpInfo.setUserPassword( strFtpUserPassword );
        ftpInfo.setKeepAliveTimeout( 5000 );

        CommandResult commandResult = new CommandResult(  );
        commandResult.setLog( new StringBuffer(  ) );
        _ftpService.uploadFile( strFtpFileLocalName, strFtpFileLocalPath, ftpInfo, strFtpRemoteDirectoryTarget,
            commandResult );

        assertNotNull( commandResult );
    }

    /**
     * get Property in test properties file
     * @param strProperty the property key
     * @return the property in test properties file
     */
    public static String getTestProperties( String strProperty )
    {
        String strPropertyValue = null;

        try
        {
            URL url = Thread.currentThread(  ).getContextClassLoader(  ).getResource( "deployment-test.properties" );
            FileInputStream file = new FileInputStream( url.getPath(  ) );
            Properties properties = new Properties(  );
            properties.load( file );
            strPropertyValue = properties.getProperty( strProperty );
        }
        catch ( IOException ex )
        {
            throw new RuntimeException( "Unable to load test file : " + ex.getMessage(  ) );
        }

        return strPropertyValue;
    }
}
