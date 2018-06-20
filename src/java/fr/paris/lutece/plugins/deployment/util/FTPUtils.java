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
package fr.paris.lutece.plugins.deployment.util;

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.FtpInfo;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FTPUtils
{

    public static String uploadFile( String fileName, InputStream inputStream, FtpInfo ftpInfo, String remoteDirectoryPath, CommandResult commandResult,
            boolean bBinaryFile )
    {
        final FTPClient ftp = getFtpClient( ftpInfo );
        String strRemoteFilePath = remoteDirectoryPath + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + fileName;

        if ( ftp != null )

        {
            // login
            try
            {
                if ( !ftp.login( ftpInfo.getUserLogin( ), ftpInfo.getUserPassword( ) ) )
                {
                    ftp.logout( );
                    DeploymentUtils.addTechnicalError( commandResult, "Probleme de connexion FTP,le compte FTP n'est pas reconnu" + strRemoteFilePath );
                }

                if ( bBinaryFile )
                {
                    ftp.setFileType( FTP.BINARY_FILE_TYPE );
                }
                boolean bStorefile = ftp.storeFile( strRemoteFilePath, inputStream );

                if ( !bStorefile )
                {
                    // try again
                    bStorefile = ftp.storeFile( strRemoteFilePath, inputStream );
                }

                if ( !bStorefile )
                {
                    DeploymentUtils.addTechnicalError( commandResult, "Probleme lors du dépot du fichier en FTP " + strRemoteFilePath );
                }

                inputStream.close( );

                ftp.noop( ); // check that control connection is working OK

                ftp.logout( );
            }

            catch( FTPConnectionClosedException e )
            {
                DeploymentUtils.addTechnicalError( commandResult, "Une erreur est survenue lors de la fermeture de la connexion FTP:" + e.getMessage( )
                        + strRemoteFilePath, e );
            }
            catch( IOException e )
            {
                DeploymentUtils.addTechnicalError( commandResult, "Une erreur est survenue lors du transfert FTP" + e.getMessage( ) + strRemoteFilePath, e );
            }
            finally
            {
                if ( ftp.isConnected( ) )
                {
                    try
                    {
                        ftp.disconnect( );
                    }
                    catch( IOException f )
                    {
                        // do nothing
                    }
                }
            }
        }
        else
        {
            DeploymentUtils.addTechnicalError( commandResult, "Probleme de connexion FTP" + strRemoteFilePath );

        }

        // upload File
        return null;
    }

    public static String getFile( OutputStream outputStream, FtpInfo ftpInfo, String remoteFilePath, CommandResult commandResult )
    {
        final FTPClient ftp = getFtpClient( ftpInfo );

        if ( ftp != null )

        {

            // login
            try
            {
                if ( !ftp.login( ftpInfo.getUserLogin( ), ftpInfo.getUserPassword( ) ) )
                {
                    DeploymentUtils.addTechnicalError( commandResult, "Probleme de connexion FTP,le compte FTP n'est pas reconnu" + remoteFilePath );

                    ftp.logout( );

                }

                boolean bRetrieve = ftp.retrieveFile( remoteFilePath, outputStream );

                if ( !bRetrieve )
                {
                    // Try Again
                    bRetrieve = ftp.retrieveFile( remoteFilePath, outputStream );
                }

                if ( !bRetrieve )
                {
                    DeploymentUtils.addTechnicalError( commandResult, "Probleme lors de la récupération du fichier en FTP" + remoteFilePath );
                }

                outputStream.flush( );
                // close output stream
                outputStream.close( );

                ftp.noop( ); // check that control connection is working OK

                ftp.logout( );
            }

            catch( FTPConnectionClosedException e )
            {
                DeploymentUtils.addTechnicalError( commandResult, "Une erreur est survenue lors de la fermeture de la connexion FTP:" + e.getMessage( )
                        + remoteFilePath, e );

            }
            catch( IOException e )
            {
                DeploymentUtils.addTechnicalError( commandResult, "Une erreur est survenue lors du transfert FTP:" + e.getMessage( ) + remoteFilePath, e );

            }
            finally
            {
                if ( ftp.isConnected( ) )
                {
                    try
                    {
                        ftp.disconnect( );
                    }
                    catch( IOException f )
                    {
                        // do nothing
                    }
                }
            }
        }
        else
        {
            DeploymentUtils.addTechnicalError( commandResult, "Probleme de connexion FTP" + remoteFilePath );

        }

        // upload File
        return null;
    }

    public static String uploadFile( String fileName, String pathLocalFile, FtpInfo ftpInfo, String remoteDirectoryPath, CommandResult commandResult,
            boolean bBinaryFile )
    {
        final FTPClient ftp = getFtpClient( ftpInfo );

        // login

        InputStream input = null;

        try
        {
            input = new FileInputStream( pathLocalFile );
        }
        catch( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            DeploymentUtils.addTechnicalError( commandResult, "Une erreur est survenue lors de l'upload du fichier" + pathLocalFile + ":" + e.getMessage( ), e );

        }

        return uploadFile( fileName, input, ftpInfo, remoteDirectoryPath, commandResult, bBinaryFile );

    }

    private static FTPClient getFtpClient( FtpInfo ftpInfo )
    {
        final FTPClient ftp;

        if ( ftpInfo.getProxyHost( ) != null )
        {
            ftp = new FTPHTTPClient( ftpInfo.getProxyHost( ), ftpInfo.getProxyPort( ), ftpInfo.getProxyUserLogin( ), ftpInfo.getProxyUserPassword( ) );
        }
        else
        {
            ftp = new FTPClient( );
        }

        // add keepAlive
        if ( ftpInfo.getKeepAliveTimeout( ) >= 0 )
        {
            ftp.setControlKeepAliveTimeout( ftpInfo.getKeepAliveTimeout( ) );
        }

        try
        {
            int reply;

            if ( ftpInfo.getPort( ) > 0 )
            {
                ftp.connect( ftpInfo.getHost( ), ftpInfo.getPort( ) );
            }
            else
            {
                ftp.connect( ftpInfo.getHost( ) );
            }

            AppLogService.debug( "Connected to " + ftpInfo.getHost( ) + " on " + ( ( ftpInfo.getPort( ) > 0 ) ? ftpInfo.getPort( ) : ftp.getDefaultPort( ) ) );

            // After connection attempt, you should check the reply code to
            // verify
            // success
            reply = ftp.getReplyCode( );

            if ( !FTPReply.isPositiveCompletion( reply ) )
            {
                ftp.disconnect( );
                AppLogService.error( "FTP server refused connection." );

            }

            return ftp;
        }
        catch( IOException e )
        {
            if ( ftp.isConnected( ) )
            {
                try
                {
                    ftp.disconnect( );
                }
                catch( IOException f )
                {
                    // do nothing
                }
            }

            AppLogService.error( "Could not connect to server." + e, e );

        }

        return null;
    }
}
