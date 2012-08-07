package fr.paris.lutece.plugins.deployment.util;

import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.ISVNRepositoryPool;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

public class ReleaseSVNCheckoutClient
    extends SVNUpdateClient
{
    private boolean bCancelled;

    public ReleaseSVNCheckoutClient( ISVNAuthenticationManager authManager, ISVNOptions options )
    {
        super( authManager, options );
    }

    public ReleaseSVNCheckoutClient( ISVNRepositoryPool pool, ISVNOptions options )
    {
        super( pool, options );
    }

    @Override
    public void checkCancelled(  )
                        throws SVNCancelException
    {
        if ( bCancelled )
        {
            throw new SVNCancelException( SVNErrorMessage.create( SVNErrorCode.CANCELLED, "Canceled by user" ) );
        }

        super.checkCancelled(  );
    }

    public void doCancel(  )
    {
        bCancelled = true;
    }
}
