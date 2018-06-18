package fr.paris.lutece.plugins.deployment.util.vcs;

import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.vcs.GitUser;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.signrequest.BasicAuthorizationAuthenticator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.io.FileUtils;





public class GitUtils  {
	
    public static final String MASTER_BRANCH = "master";
    public static final String DEVELOP_BRANCH = "develop";
    private static final String CONSTANTE_REF_TAG = "refs/tags/";
    
    public static Collection<String> getTagNameList( Git git )
    {
        List<String> listTagName = new ArrayList<>();
        Collection<Ref> colTags = git.getRepository().getTags().values();
        for ( Ref ref : colTags )
        {
            listTagName.add( ref.getName().replace(CONSTANTE_REF_TAG, "") );
        }
        return listTagName;
        
    }

    public static  Git cloneOrReturnGit(String sClonePath, String sRepoURL, CommandResult commandResult,GitUser user, String strBranch, String strTagName ) 
    {
        Git git=null;
        Repository repository=null;
        try
        {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            File fGitDir = new File(sClonePath);

            File gitFile = new File( sClonePath + "/.git");
            repository = builder.setGitDir(fGitDir).readEnvironment().findGitDir().build();
            
            if ( gitFile.exists( ) )
            {
                git = Git.open( gitFile );
                checkout( git, strBranch, strTagName, commandResult );
                return git;
            }

            CloneCommand clone = Git.cloneRepository().setBare(false).setCloneAllBranches(true).setDirectory(fGitDir).setURI(getRepoUrl( sRepoURL ));

            if ( user.getLogin() != null && user.getPassword() != null )
            {
                clone.setCredentialsProvider(new UsernamePasswordCredentialsProvider( user.getLogin(), user.getPassword( ) ) );
            }
            git=clone.call( );
            
            createLocalBranch( git, DEVELOP_BRANCH, commandResult );
            createLocalBranch( git, MASTER_BRANCH, commandResult );
            repository.getConfig( ).setString( "user", null, "name", user.getLogin( ) );
            repository.getConfig( ).save( );


        }
        catch( InvalidRemoteException  e )
        {

            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );

        }
        catch( TransportException e )
        {
            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );

        }
        catch( IOException e )
        {
            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );
        }
        catch( GitAPIException e )
        {
            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );
        }
        finally
        {
            repository.close();
        }
        return git;

    }


    public static void checkoutRepoBranch(Git git, String sBranchName,CommandResult commandResult)
    {
        try
        {
            git.checkout()
                .setName(sBranchName)
                .setForce(true)
                .call(); 
        }
        catch( InvalidRemoteException  e )
        {

            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );

        }
        catch( TransportException e )
        {
            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );

        }

        catch( GitAPIException e )
        {
            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );
        }

    }

    public static void createLocalBranch(Git git, String sBranchName,CommandResult commandResult) 
    {
        try
        {
            git.branchCreate() 
               .setName(sBranchName)
               .setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
               .setStartPoint("origin/" + sBranchName)
               .setForce(true)
               .call();
        }
        catch( InvalidRemoteException  e )
        {

            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );

        }
        catch( TransportException e )
        {
            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );

        }

        catch( GitAPIException e )
        {
            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );
        }

    }
	
    public static String getRefBranch(Git git, String sBranchName,CommandResult commandResult) 
    {
	    
        String refLastCommit=null;
        try
        {
             git.checkout().setName(sBranchName).call();
             refLastCommit= getLastCommitId( git );
        }
  
     
        catch( RefAlreadyExistsException e )
        {
            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );
        }
        catch( RefNotFoundException e )
        {
            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );
        }
        catch( InvalidRefNameException e )
        {
            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );
        }
        catch( CheckoutConflictException e )
        {
            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );
        }
        catch( GitAPIException e )
        {
            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );
        }
        return refLastCommit;
    }

    public static void  pushForce(Git git, String strRefSpec, String strUserName, String strPassword) throws InvalidRemoteException, TransportException, GitAPIException
    {

        git.push( ).setRemote( "origin" ).setRefSpecs( new RefSpec( strRefSpec ) ).setForce( true ).setCredentialsProvider(new UsernamePasswordCredentialsProvider(strUserName, strPassword)).call();

    }
	
    public static PullResult pullRepoBranch(Git git, String sRepoURL, String sBranchName) throws IOException, WrongRepositoryStateException, InvalidConfigurationException, DetachedHeadException, InvalidRemoteException, CanceledException, RefNotFoundException, NoHeadException, TransportException, GitAPIException
    {
            PullResult pPullResult = git.pull().call();		
            return pPullResult;	
    }
	
    public static MergeResult mergeRepoBranch(Git git, String strBranchToMerge) throws IOException, WrongRepositoryStateException, InvalidConfigurationException, DetachedHeadException, InvalidRemoteException, CanceledException, RefNotFoundException, NoHeadException, TransportException, GitAPIException
    {
            List<Ref> call = git.branchList().call();
            Ref mergedBranchRef = null;
            for (Ref ref : call) 
            {
                    if (ref.getName().equals("refs/heads/" + strBranchToMerge)) {
                            mergedBranchRef = ref;
                            break;
                    }
            }
            MergeResult mergeResult = git.merge().include(mergedBranchRef).call();
            return mergeResult;	
    }
	
    public  static String getLastLog(Git git, int nMaxCommit) throws NoHeadException, GitAPIException
    {
            Iterable<RevCommit> logList = git.log().setMaxCount(1).call();
            Iterator i = logList.iterator();
            String sCommitMessages = "";
            while (i.hasNext())
            {
                    RevCommit revCommit = (RevCommit) i.next();
                    sCommitMessages += revCommit.getFullMessage();
                    sCommitMessages += "\n";
                    sCommitMessages += revCommit.getCommitterIdent();
            }
            return sCommitMessages;
    }
	
	
    public  static String getLastCommitId(Git git) throws NoHeadException, GitAPIException
    {
        Iterable<RevCommit> logList = git.log().setMaxCount(1).call();
        Iterator i = logList.iterator();
       String strCommitId = null;
        while (i.hasNext())
        {
            RevCommit revCommit = (RevCommit) i.next();
            strCommitId = revCommit.getName( );
          
        }
        return strCommitId;
    }
    
	
	  
    public static MergeResult mergeBack(Git git, String strUserName, String strPassword, CommandResult commandResult) throws IOException, GitAPIException
    {
        
            
        Ref tag = getTagLinkedToLastRelease(git);

        git.checkout().setName(MASTER_BRANCH).call();
        List<Ref> call = git.branchList().call();

        Ref mergedBranchRef = null;
        for (Ref ref : call) 
        {
           if (ref.getName().equals("refs/heads/"+DEVELOP_BRANCH)) 
           {
               mergedBranchRef = ref;
               break;
           }
        }

       if (tag != null) 
       {
           mergedBranchRef = tag;
       }
       MergeResult mergeResult = git.merge().include(mergedBranchRef).call();
       if (mergeResult.getMergeStatus().equals(MergeResult.MergeStatus.CHECKOUT_CONFLICT) || mergeResult.getMergeStatus().equals(MergeResult.MergeStatus.CONFLICTING) || mergeResult.getMergeStatus().equals(MergeResult.MergeStatus.FAILED) || mergeResult.getMergeStatus().equals(MergeResult.MergeStatus.NOT_SUPPORTED))
       {

           DeploymentUtils.addTechnicalError( commandResult, mergeResult.getMergeStatus().toString()
                   + "\nPlease merge manually master into"+ DEVELOP_BRANCH +"branch." );
       }
       else
       {
           git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(strUserName, strPassword)).call();
           commandResult.getLog( ).append(mergeResult.getMergeStatus());
       }
       return mergeResult;
            
    }
    
    
    public  static String getFileContent(String strFullName,String strPathFile,String strBranch,String strUserName, String strPassword)
    {
        HttpAccess httpAccess = new HttpAccess(  );
       String strUrl = "https://raw.githubusercontent.com/"+strFullName+"/"+strBranch+"/"+ strPathFile;
        String strResponse = "";
        
        try
        {
            strResponse = httpAccess.doGet( strUrl, new BasicAuthorizationAuthenticator(  strUserName, strPassword ),null );
        }
        catch ( HttpAccessException ex )
        {
            AppLogService.error( ex );
        }

        return strResponse;
    }
    
    
    private static Ref getTagLinkedToLastRelease(Git git) throws GitAPIException {
        final String TOKEN = "[maven-release-plugin] prepare release ";
        Ref res = null;
        String sTagName = null;
        
        Iterable<RevCommit> logList = git.log().setMaxCount(10).call();
        Iterator i = logList.iterator();
        String sCommitMessages = "";
        while (i.hasNext())
        {
            RevCommit revCommit = (RevCommit) i.next();
            sCommitMessages = revCommit.getFullMessage();
            int index = sCommitMessages.indexOf(TOKEN);
            if (index >= 0) {
                sTagName = sCommitMessages.replace(TOKEN, "");
                break;
            }
        }
        
        if ( (sTagName != null) && (!(sTagName.trim().equals(""))) ) {
            List<Ref> tags = git.tagList().call();
            for (int j=0; j<tags.size(); j++) {
                Ref tag = tags.get(tags.size() - 1 - j);
                String tagName = tag.getName();
                if (tagName.equals("refs/tags/" + sTagName)) {
                    res = tag;
                    break;
                }
            }
        }
        
        return res;
    }



    private static String getRepoUrl(String strRepoUrl)
    {

        if(strRepoUrl!=null && strRepoUrl.startsWith( "scm:git:" ))
         {
            strRepoUrl=strRepoUrl.substring( 8 );


              }

        return strRepoUrl;
    }
    
    private static void checkout( Git git, String strBranch, String strTagName, CommandResult commandResult )
    {
        if ( strBranch == null && strTagName == null )
        {
            checkoutRepoBranch( git, DEVELOP_BRANCH, commandResult );
            return;
        }
        if ( strBranch != null && strTagName != null )
        {
            DeploymentUtils.addTechnicalError( commandResult, "A branch name and a tag name are provided while checkouting the git repo" );
            return;
        }
        if ( strBranch != null )
        {
            checkoutRepoBranch( git, strBranch, commandResult );
        }
        if ( strTagName != null )
        {
            checkoutTag( git, strTagName, commandResult );
        }
    }
    
    private static void checkoutTag( Git git, String strTagName, CommandResult commandResult )
    {
        
        try
        {
            git.checkout().setName( CONSTANTE_REF_TAG + strTagName ).call();

        }
        catch( InvalidRemoteException  e )
        {

            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );

        }
        catch( TransportException e )
        {
            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );

        }

        catch( GitAPIException e )
        {
            DeploymentUtils.addTechnicalError( commandResult, e.getMessage( ), e );
        }
    }
    
    public static boolean checkAuthentication( String strRepoUrl, String strLocalTempPath, String strLocalTempDir, GitUser user )
    {
        boolean bAuth = true;
        Path localPath = null;
        try
        {
            Path basePath = Paths.get( strLocalTempPath );
            localPath = Files.createTempDirectory( basePath, strLocalTempDir );
            Git.cloneRepository()
                  .setURI (strRepoUrl )
                  .setCredentialsProvider(new UsernamePasswordCredentialsProvider(user.getLogin(), user.getPassword()))
                  .setDirectory( localPath.toFile( ) )
                  .call();
        }
        catch( Exception e )
        {
            bAuth = false;
        }
        finally
        {
            if ( localPath != null )
            {
                try
                {
                   FileUtils.forceDelete(new File(localPath.toString( ) ) );
                }
                catch ( IOException e )
                {
                    AppLogService.error( "Unable to delete dir " + localPath.toString(), e);
                }
            }
        }
        return bAuth;
    }
}
