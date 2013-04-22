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
	
	public static List<String> list( String strDirPath,String strFileExtension)
	{
		List<String> strFileList= new ArrayList<String>();
		File file=new File(strDirPath);
		
		if ( file.isDirectory(  ) )
		{
			
			for ( String fileName : file.list() )
			{
				
			
				strFileList.addAll(list( file.getAbsolutePath() + File.separator + fileName ,strFileExtension));
			}
		}
		else if(strFileExtension== null ||file.getName().endsWith(strFileExtension))
		{
			strFileList.add(file.getName());
			
		}
		return strFileList;
	}
}
