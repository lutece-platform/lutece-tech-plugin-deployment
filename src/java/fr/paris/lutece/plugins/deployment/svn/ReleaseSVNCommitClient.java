package fr.paris.lutece.plugins.deployment.svn;

import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNCommitClient;

public class ReleaseSVNCommitClient extends SVNCommitClient
{
	private boolean bCancelled;

	public ReleaseSVNCommitClient( ISVNAuthenticationManager authManager, ISVNOptions options )
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
