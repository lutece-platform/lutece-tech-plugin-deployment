package fr.paris.lutece.plugins.deployment.service;

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.FtpInfo;

public interface IFtpService {

	String uploadFile(String fileName, String pathLocalFile, FtpInfo ftpInfo,
			String remoteDirectoryPath, CommandResult commandResult);

}