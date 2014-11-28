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
    public static String deleteFile( FtpInfo ftpInfo, boolean useSSL )
    {
        boolean bError = true;
        final FTPClient ftp = getFtpClient( ftpInfo );

        try
        {
            if ( !ftp.login( ftpInfo.getUserLogin(  ), ftpInfo.getUserPassword(  ) ) )
            {
                ftp.logout(  );
                bError = true;
            }

            boolean resultat = ftp.deleteFile( "Documents/test.war" );

            if ( resultat )
            {
                System.out.println( "Le Fichier a été supprimé" );
            }
            else
            {
                System.out.println( "Impossible de supprimer ce fichier" );
            }

            ftp.noop(  ); // check that control connection is working OK

            ftp.logout(  );
            ftp.disconnect(  );
        }
        catch ( FTPConnectionClosedException e )
        {
            bError = true;
            System.err.println( "Server closed connection." );
            e.printStackTrace(  );
        }
        catch ( IOException e )
        {
            bError = true;
            e.printStackTrace(  );
        }
        finally
        {
            if ( ftp.isConnected(  ) )
            {
                try
                {
                    ftp.disconnect(  );
                }
                catch ( IOException f )
                {
                    // do nothing
                }
            }
        }

        return null;
    }

    public static String mvFile( FtpInfo ftpInfo, boolean useSSL )
    {
        boolean bError = true;
        final FTPClient ftp = getFtpClient( ftpInfo );

        try
        {
            if ( !ftp.login( ftpInfo.getUserLogin(  ), ftpInfo.getUserPassword(  ) ) )
            {
                ftp.logout(  );
                bError = true;
            }

            boolean resultat = ftp.rename( "Documents/test.war", "test.war.old" );

            if ( resultat )
            {
                System.out.println( "Le fichier a été déplacé" );
            }
            else
            {
                System.out.println( "Impossible de déplacer le fichier" );
            }

            ftp.noop(  ); // check that control connection is working OK

            ftp.logout(  );
            ftp.disconnect(  );
        }
        catch ( FTPConnectionClosedException e )
        {
            bError = true;
            AppLogService.error( "Server closed connection." );
           
        }
        catch ( IOException e )
        {
            bError = true;
            AppLogService.error( e);
        }
        finally
        {
            if ( ftp.isConnected(  ) )
            {
                try
                {
                    ftp.disconnect(  );
                }
                catch ( IOException f )
                {
                	 AppLogService.error( "Server closed connection." + f );
                }
            }
        }

        return null;
    }

    
    public static String uploadFile( String fileName, InputStream inputStream, FtpInfo ftpInfo,
            String remoteDirectoryPath, CommandResult commandResult,boolean bBinaryFile )
        {
            final FTPClient ftp = getFtpClient( ftpInfo );

            boolean bError;

            // login
            try
            {
                if ( !ftp.login( ftpInfo.getUserLogin(  ), ftpInfo.getUserPassword(  ) ) )
                {
                    ftp.logout(  );
                    bError = true;
                }

                if(bBinaryFile)
                {
                	ftp.setFileType( FTP.BINARY_FILE_TYPE );
                }
                ftp.storeFile( remoteDirectoryPath + ConstanteUtils.CONSTANTE_SEPARATOR_SLASH + fileName, inputStream );

                inputStream.close(  );

                ftp.noop(  ); // check that control connection is working OK

                ftp.logout(  );
            }

            catch ( FTPConnectionClosedException e )
            {
                bError = true;
                AppLogService.error( "Server closed connection." + e );
            }
            catch ( IOException e )
            {
                bError = true;
                AppLogService.error( e );
            }
            finally
            {
                if ( ftp.isConnected(  ) )
                {
                    try
                    {
                        ftp.disconnect(  );
                    }
                    catch ( IOException f )
                    {
                        // do nothing
                    }
                }
            }

            // upload File
            return null;
        }
    
    
    
    
    public static String getFile(OutputStream outputStream, FtpInfo ftpInfo,
            String remoteFilePath, CommandResult commandResult )
        {
            final FTPClient ftp = getFtpClient( ftpInfo );

            boolean bError;

            // login
            try
            {
                if ( !ftp.login( ftpInfo.getUserLogin(  ), ftpInfo.getUserPassword(  ) ) )
                {
                    ftp.logout(  );
                    bError = true;
                }

               
                ftp.retrieveFile(remoteFilePath, outputStream);

                 //close output stream
                outputStream.close();

                ftp.noop(  ); // check that control connection is working OK

                ftp.logout(  );
            }

            catch ( FTPConnectionClosedException e )
            {
                bError = true;
                AppLogService.error( "Server closed connection." + e );
            }
            catch ( IOException e )
            {
                bError = true;
                AppLogService.error( e );
            }
            finally
            {
                if ( ftp.isConnected(  ) )
                {
                    try
                    {
                        ftp.disconnect(  );
                    }
                    catch ( IOException f )
                    {
                        // do nothing
                    }
                }
            }

            // upload File
            return null;
        }
    
    
    
    public static String uploadFile( String fileName, String pathLocalFile, FtpInfo ftpInfo,
        String remoteDirectoryPath, CommandResult commandResult,boolean bBinaryFile )
    {
        final FTPClient ftp = getFtpClient( ftpInfo );

        boolean bError;

        // login
       
            InputStream input = null;

            try
            {
                input = new FileInputStream( pathLocalFile );
            }
            catch ( FileNotFoundException e )
            {
                // TODO Auto-generated catch block
                AppLogService.error(e);
            }
            
            return uploadFile(fileName, input, ftpInfo, remoteDirectoryPath, commandResult,bBinaryFile);

           
    }

    private static FTPClient getFtpClient( FtpInfo ftpInfo )
    {
        final FTPClient ftp;

        if ( ftpInfo.getProxyHost(  ) != null )
        {
            System.out.println( "Using HTTP proxy server: " + ftpInfo.getProxyHost(  ) );
            ftp = new FTPHTTPClient( ftpInfo.getProxyHost(  ), ftpInfo.getProxyPort(  ), ftpInfo.getProxyUserLogin(  ),
                    ftpInfo.getProxyUserPassword(  ) );
        }
        else
        {
            ftp = new FTPClient(  );
        }

        //add keepAlive 
        if ( ftpInfo.getKeepAliveTimeout(  ) >= 0 )
        {
            ftp.setControlKeepAliveTimeout( ftpInfo.getKeepAliveTimeout(  ) );
        }

        try
        {
            int reply;

            if ( ftpInfo.getPort(  ) > 0 )
            {
                ftp.connect( ftpInfo.getHost(  ), ftpInfo.getPort(  ) );
            }
            else
            {
                ftp.connect( ftpInfo.getHost(  ) );
            }

            AppLogService.debug( "Connected to " + ftpInfo.getHost(  ) + " on " +
                ( ( ftpInfo.getPort(  ) > 0 ) ? ftpInfo.getPort(  ) : ftp.getDefaultPort(  ) ) );

            // After connection attempt, you should check the reply code to
            // verify
            // success
            reply = ftp.getReplyCode(  );

            if ( !FTPReply.isPositiveCompletion( reply ) )
            {
                ftp.disconnect(  );
                AppLogService.error( "FTP server refused connection." );
                System.exit( 1 );
            }

            return ftp;
        }
        catch ( IOException e )
        {
            if ( ftp.isConnected(  ) )
            {
                try
                {
                    ftp.disconnect(  );
                }
                catch ( IOException f )
                {
                    // do nothing
                }
            }

            AppLogService.error( "Could not connect to server." + e );
            System.exit( 1 );
        }

        return null;
    }
}
