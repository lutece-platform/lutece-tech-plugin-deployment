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

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileUtil
{
    public static final boolean STATUS_OK = true;
    public static final boolean STATUS_ERROR = false;

    public static boolean delete( File file, StringBuffer logBuffer )
    {
        if ( file.isDirectory(  ) )
        {
            boolean bStatus = STATUS_OK;

            for ( String fileName : file.list(  ) )
            {
                //Names denoting the directory itself and the directory's parent directory are not included in the result
                bStatus &= delete( new File( file.getAbsolutePath(  ) + File.separator + fileName ), logBuffer );
            }
        }

        logBuffer.append( "DELETING " + file.getAbsolutePath(  ) + "\n" );

        if ( !file.delete(  ) )
        {
            logBuffer.append( "UNABLE TO DELETE : " + file.getAbsolutePath(  ) );

            return STATUS_ERROR;
        }

        return STATUS_OK;
    }

    public static List<String> list( String strDirPath, String strFileExtension )
    {
        return list( strDirPath, strFileExtension, true );
    }
    
    public static List<String> list( String strDirPath, String strFileExtension, boolean bRecursive )
    {
        List<String> strFileList = new ArrayList<String>(  );
        File file = new File( strDirPath );

        if ( file.isDirectory(  ) )
        {
            for ( File fileChild : file.listFiles( ) )
            {
                if ( !fileChild.isDirectory(  ) || bRecursive )
                {
                    strFileList.addAll( list( fileChild.getAbsolutePath( ), strFileExtension , bRecursive) );
                }
            }
        }
        else if ( ( strFileExtension == null ) || file.getName(  ).endsWith( strFileExtension ) )
        {
            strFileList.add( file.getName(  ) );
        }

        return strFileList;
    }
    
   
}
