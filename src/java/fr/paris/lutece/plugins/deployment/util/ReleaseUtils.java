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

import fr.paris.lutece.plugins.deployment.business.jaxb.maven.Model;
import fr.paris.lutece.plugins.deployment.business.jaxb.maven.ObjectFactory;
import fr.paris.lutece.plugins.deployment.util.vcs.ReleaseSVNCommitClient;

import org.apache.commons.lang.StringUtils;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNCommitPacket;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public final class ReleaseUtils
{
    public static final String CONSTANTE_EMPTY_STRING = "";
    public static final String CONSTANTE_SNAPSHOT = "-SNAPSHOT";
    public static final String CONSTANT_CORE = "core";
    public static final String CONSTANT_CORE_PREFIX = "lutece-";
    public static final String CONSTANT_POM_PARENT = "lutece-parent-pom";
    public static final String CONSTANT_POM_PLUGIN = "lutece-plugins-pom";

    /**
     * Safe split (no empty value)
     * @param strToSplit
     * @param strSeparator
     * @return
     */
    public static List<String> split( String strToSplit, String strSeparator )
    {
        List<String> listSplit = new ArrayList<String>(  );
        String[] splitted = strToSplit.split( strSeparator );

        if ( splitted != null )
        {
            for ( String strValue : splitted )
            {
                if ( !StringUtils.isBlank( strValue ) )
                {
                    // add to list
                    listSplit.add( strValue );
                }
            }
        }

        return listSplit;
    }

    public static String getReleaseName( final String strPluginName, String strVersion )
    {
        if ( CONSTANT_CORE.equals( strPluginName ) )
        {
            return CONSTANT_CORE_PREFIX + strPluginName + "-" + strVersion;
        }

        return strPluginName + "-" + strVersion;
    }

    public static <T> T unmarshal( Class<T> docClass, InputStream inputStream )
        throws JAXBException
    {
        String packageName = docClass.getPackage(  ).getName(  );
        JAXBContext jc = JAXBContext.newInstance( packageName );
        Unmarshaller u = jc.createUnmarshaller(  );
        JAXBElement<T> doc = (JAXBElement<T>) u.unmarshal( inputStream );

        return doc.getValue(  );
    }

    public static void save( Model model, OutputStream outputStream )
        throws JAXBException
    {
        String packageName = model.getClass(  ).getPackage(  ).getName(  );
        ObjectFactory factory = new ObjectFactory(  );
        JAXBElement<Model> element = factory.createProject( model );

        JAXBContext jc = JAXBContext.newInstance( packageName );
        Marshaller m = jc.createMarshaller(  );
        m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        m.setProperty( Marshaller.JAXB_SCHEMA_LOCATION, "http://maven.apache.org/maven-v4_0_0.xsd" );
        m.marshal( element, outputStream );
    }

    public static String getNextVersion( String strVersion )
    {
        String strNextVersion = CONSTANTE_EMPTY_STRING;

        if ( strVersion.contains( CONSTANTE_SNAPSHOT ) )
        {
            strVersion = strVersion.substring( 0, strVersion.indexOf( CONSTANTE_SNAPSHOT ) );
        }

        int nLastIndex = strVersion.lastIndexOf( "." );

        try
        {
            int nMinorVersion = Integer.parseInt( strVersion.substring( nLastIndex + 1, strVersion.length(  ) ) );
            nMinorVersion++;

            strNextVersion += strVersion.substring( 0, nLastIndex + 1 );
            strNextVersion += nMinorVersion;
            strNextVersion += CONSTANTE_SNAPSHOT;
        }
        catch ( NumberFormatException nfe )
        {
            nfe.printStackTrace(  );
        }

        return strNextVersion;
    }

    /**
     * Retourne la version [stable] courante (sans -SNAPSHOT si present dans le pom)
     * @return
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    public static String getReleaseVersion( String strPathLocalSiteName )
        throws JAXBException, FileNotFoundException
    {
        InputStream is = new FileInputStream( DeploymentUtils.getPathPomFile( strPathLocalSiteName ) );

        Model model = unmarshal( Model.class, is );

        String strVersion = model.getVersion(  );
      

        if ( strVersion.contains( "-SNAPSHOT" ) )
        {
            strVersion = strVersion.substring( 0, strVersion.indexOf( "-SNAPSHOT" ) );
        }

        return strVersion;
    }
    
    
    /**
     * Retourne la version  dans le pom)
     * @return
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    public static String getSiteVersion( String strPathLocalSiteName )
        throws JAXBException, FileNotFoundException
    {
        InputStream is = new FileInputStream( DeploymentUtils.getPathPomFile( strPathLocalSiteName ) );

        Model model = unmarshal( Model.class, is );

        String strVersion = model.getVersion(  );
        return strVersion;
    }
    
    
    /**
     * Retourne la version  dans le pom)
     * @return
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    public static String getSiteArtifactId( String strPathLocalSiteName )
        throws JAXBException, FileNotFoundException
    {
        InputStream is = new FileInputStream( DeploymentUtils.getPathPomFile( strPathLocalSiteName ) );

        Model model = unmarshal( Model.class, is );

        String strVersion = model.getArtifactId();
        return strVersion;
    }
    
    

    public static String updateReleaseVersion( String strPathLocalSiteName, String strNewVersion, String commitMessage,
        SVNCommitClient svnCommitClient )
        throws ParserConfigurationException, SAXException, IOException, TransformerException, SVNException,
            JAXBException
    {
        String strPomFile = DeploymentUtils.getPathPomFile( strPathLocalSiteName );
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try
        {
            inputStream = new FileInputStream( strPomFile );

            Model model = unmarshal( Model.class, inputStream );

            if ( !model.getVersion(  ).equals( strNewVersion ) )
            {
                model.setVersion( strNewVersion );

                outputStream = new FileOutputStream( strPomFile );

                save( model, outputStream );

                // COMMIT           
                /*
                 *  paths - an array of local items which should be traversed to collect information on every changed item (one SVNCommitItem per each modified local item)
                         *        keepLocks - if true and there are local items that were locked then these items will be left locked after traversing all of them, otherwise the items will be unlocked
                         *        force - forces collecting commit items for a non-recursive commit
                         *        recursive - relevant only for directory items: if true then the entire directory tree will be traversed including all child directories, otherwise only items located in the directory itself will be processed
                 */
                SVNCommitPacket commitPacket = svnCommitClient.doCollectCommitItems( new File[] { new File( strPomFile ) },
                        false, false, false );

                if ( !SVNCommitPacket.EMPTY.equals( commitPacket ) )
                {
                    svnCommitClient.doCommit( commitPacket, false, commitMessage );
                }
            }
            else
            {
                return "Pom already up to date\n";
            }
        }
        finally
        {
            if ( outputStream != null )
            {
                try
                {
                    outputStream.close(  );
                }
                catch ( IOException ex )
                {
                    // nothing...
                    ex.printStackTrace(  );
                }
            }
        }

        return "";
    }
}
