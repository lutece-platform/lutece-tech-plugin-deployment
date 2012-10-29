package fr.paris.lutece.plugins.deployment.util;

import java.io.File;

public class DeleteUtil
{
	
	public static final boolean STATUS_OK = true;
	public static final boolean STATUS_ERROR = false;
	
	public static boolean delete( File file, StringBuffer logBuffer )
	{
		if ( file.isDirectory(  ) )
		{
			boolean bStatus = STATUS_OK;
			for ( String fileName : file.list() )
			{
				//Names denoting the directory itself and the directory's parent directory are not included in the result
				bStatus &= delete( new File( file.getAbsolutePath() + File.separator + fileName ), logBuffer );
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
}
