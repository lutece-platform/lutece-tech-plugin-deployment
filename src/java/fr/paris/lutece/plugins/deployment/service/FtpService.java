package fr.paris.lutece.plugins.deployment.service;

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.FtpInfo;
import fr.paris.lutece.plugins.deployment.util.FTPUtils;

public class FtpService implements IFtpService {
	
	
	
	
	/* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.IFtpService#uploadFile(java.lang.String, java.lang.String, fr.paris.lutece.plugins.deployment.business.FtpInfo, java.lang.String, fr.paris.lutece.plugins.deployment.business.CommandResult)
	 */
	public String uploadFile(String fileName,String pathLocalFile,FtpInfo ftpInfo,String remoteDirectoryPath,CommandResult commandResult)
	{
		final StringBuffer sbLog = new StringBuffer(  );
		sbLog.append( "Starting  deploy  Site...\n" );
		commandResult.setRunning( true );
		commandResult.setLog( sbLog );
		FTPUtils.uploadFile(fileName, pathLocalFile, ftpInfo, remoteDirectoryPath, commandResult);
		commandResult.setRunning( false );
		return null;
	}
	

}