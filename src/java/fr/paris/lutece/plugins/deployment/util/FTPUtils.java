package fr.paris.lutece.plugins.deployment.util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPReply;

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.FtpInfo;

public class FTPUtils {

	
	public static String deleteFile(FtpInfo ftpInfo, boolean useSSL) {
		boolean bError = true;
		final FTPClient ftp = getFtpClient(ftpInfo);

		try {
			if (!ftp.login(ftpInfo.getUserLogin(), ftpInfo.getUserPassword())) {
				ftp.logout();
				bError = true;
			}

			boolean resultat = ftp.deleteFile("Documents/test.war");
			if (resultat) {
				System.out.println("Le Fichier a été supprimé");
			} else {
				System.out.println("Impossible de supprimer ce fichier");
			}

			ftp.noop(); // check that control connection is working OK

			ftp.logout();
			ftp.disconnect();

		} catch (FTPConnectionClosedException e) {
			bError = true;
			System.err.println("Server closed connection.");
			e.printStackTrace();
		} catch (IOException e) {
			bError = true;
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					// do nothing
				}
			}
		}
			return null;
	}
	
	public static String mvFile(FtpInfo ftpInfo, boolean useSSL) {
		boolean bError = true;
		final FTPClient ftp = getFtpClient(ftpInfo);

		try {
			if (!ftp.login(ftpInfo.getUserLogin(), ftpInfo.getUserPassword())) {
				ftp.logout();
				bError = true;
			}

			boolean resultat = ftp.rename("Documents/test.war", "test.war.old");
			if (resultat) {
				System.out.println("Le fichier a été déplacé");
			} else {
				System.out.println("Impossible de déplacer le fichier");
			}

			ftp.noop(); // check that control connection is working OK

			ftp.logout();
			ftp.disconnect();

		} catch (FTPConnectionClosedException e) {
			bError = true;
			System.err.println("Server closed connection.");
			e.printStackTrace();
		} catch (IOException e) {
			bError = true;
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					// do nothing
				}
			}
		}
			return null;
	}


	public static String uploadFile(String fileName,String pathLocalFile,FtpInfo ftpInfo,String remoteDirectoryPath,CommandResult commandResult) {
		final FTPClient ftp = getFtpClient(ftpInfo);
		boolean bError;
		// login

		try {
			if (!ftp.login(ftpInfo.getUserLogin(), ftpInfo.getUserPassword())) {
				ftp.logout();
				bError = true;
			}

			InputStream input = null;

			try {
				input = new FileInputStream(pathLocalFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ftp.storeFile(remoteDirectoryPath+ConstanteUtils.CONSTANTE_SEPARATOR_SLASH+fileName, input);

			input.close();

			ftp.noop(); // check that control connection is working OK

			ftp.logout();

		}

		catch (FTPConnectionClosedException e) {
			bError = true;
			System.err.println("Server closed connection.");
			e.printStackTrace();
		} catch (IOException e) {
			bError = true;
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					// do nothing
				}
			}
		}

		// upload File

		return null;

	}

	private static FTPClient getFtpClient(FtpInfo ftpInfo) {
		final FTPClient ftp;

		
		
		
	        
		if (ftpInfo.getProxyHost() != null) {
			System.out.println("Using HTTP proxy server: "
					+ ftpInfo.getProxyHost());
			ftp = new FTPHTTPClient(ftpInfo.getProxyHost(), ftpInfo
					.getProxyPort(), ftpInfo.getProxyUserLogin(), ftpInfo
					.getProxyUserPassword());
		} else {
			ftp = new FTPClient();
		}
		//add keepAlive 
		 if (ftpInfo.getKeepAliveTimeout() >= 0) {
				
	            ftp.setControlKeepAliveTimeout(ftpInfo.getKeepAliveTimeout());
	        }
         
       
		try {
			int reply;
			if (ftpInfo.getPort() > 0) {
				ftp.connect(ftpInfo.getHost(), ftpInfo.getPort());
			} else {
				ftp.connect(ftpInfo.getHost());
			}
			System.out.println("Connected to "
					+ ftpInfo.getHost()
					+ " on "
					+ (ftpInfo.getPort() > 0 ? ftpInfo.getPort() : ftp
							.getDefaultPort()));

			// After connection attempt, you should check the reply code to
			// verify
			// success.
			reply = ftp.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				System.err.println("FTP server refused connection.");
				System.exit(1);
			}
			return ftp;
		} catch (IOException e) {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					// do nothing
				}
			}
			System.err.println("Could not connect to server.");
			e.printStackTrace();
			System.exit(1);
		}
		return null;

	}

	
}
