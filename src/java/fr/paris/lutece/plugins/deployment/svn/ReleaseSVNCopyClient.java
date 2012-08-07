package fr.paris.lutece.plugins.deployment.svn;

import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNCopyClient;

public class ReleaseSVNCopyClient extends SVNCopyClient
{
	private boolean bCancelled;

	public ReleaseSVNCopyClient( ISVNAuthenticationManager authManager, ISVNOptions options )
	{
		super( authManager, options );
	}
	
	@Override
	public void checkCancelled() throws SVNCancelException
	{
		if ( bCancelled )
		{
			throw new SVNCancelException( SVNErrorMessage.create( SVNErrorCode.CANCELLED, "Canceled by user" ) );
		}
		super.checkCancelled();
	}
	
	public void doCancel(  )
	{
		bCancelled = true;
	}

}
