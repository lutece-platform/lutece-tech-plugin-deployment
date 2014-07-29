package fr.paris.lutece.plugins.deployment.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.junit.Test;

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.FtpInfo;
import fr.paris.lutece.test.LuteceTestCase;

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
    public void testUploadFile()
    {
        
    	
    	
    	String strFtpHost  = getTestProperties("deployment.serverApplicationFtp.host");
    	String strFtpPort= getTestProperties("deployment.serverApplicationFtp.port");
        String strFtpUserLogin= getTestProperties("deployment.serverApplicationFtp.userLogin");
        String strFtpUserPassword= getTestProperties("deployment.serverApplicationFtp.userPassword");
        String strFtpFileLocalName = getTestProperties("deployment.ftp.fileLocalName");
        String strFtpFileLocalPath = getTestProperties("deployment.ftp.fileLocalPath");
        String strFtpRemoteDirectoryTarget = getTestProperties("deployment.ftp.remoteDirectoryTarget");
        
        IFtpService _ftpService=new FtpService();
        
        FtpInfo ftpInfo=new FtpInfo();
        ftpInfo.setHost(strFtpHost);
        ftpInfo.setPort(Integer.parseInt(strFtpPort));
        ftpInfo.setUserLogin(strFtpUserLogin)    ;
        ftpInfo.setUserPassword(strFtpUserPassword);
        ftpInfo.setKeepAliveTimeout(5000);
        
        CommandResult commandResult=new CommandResult();
        commandResult.setLog(new StringBuffer());
         _ftpService.uploadFile(strFtpFileLocalName, strFtpFileLocalPath, ftpInfo, strFtpRemoteDirectoryTarget, commandResult);
        
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
