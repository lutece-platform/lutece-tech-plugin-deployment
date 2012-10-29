package fr.paris.lutece.plugins.deployment.service;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.UUID;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import fr.paris.lutece.plugins.deployment.business.Application;
import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;
import fr.paris.lutece.plugins.deployment.business.WorkflowDeploySiteContext;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.plugins.deployment.util.ReleaseUtils;
import fr.paris.lutece.plugins.deployment.util.SVNUtils;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;

public class WorkflowDeploySiteService implements IWorkflowDeploySiteService {

	
	
	HashMap<Integer,WorkflowDeploySiteContext> _mapWorkflowDeploySiteContext=new HashMap<Integer, WorkflowDeploySiteContext>();
	@Inject
	ISvnService _svnService;
	@Inject
	IMavenService _mavenService;
	@Inject
	IEnvironmentService _environmentService;
	@Inject
	IFtpService _ftpService;
	@Inject
	IApplicationService _applicationService;

	
	public synchronized int addWorkflowDeploySiteContext(WorkflowDeploySiteContext context)
	{
		int nIdKey=Integer.parseInt( DatastoreService.getDataValue(ConstanteUtils.CONSTANTE_MAX_DEPLOY_SITE_CONTEXT_KEY, "0"))+1;
		//stored key in database
		DatastoreService.setDataValue(ConstanteUtils.CONSTANTE_MAX_DEPLOY_SITE_CONTEXT_KEY,Integer.toString( nIdKey));
		context.setId(nIdKey);
		_mapWorkflowDeploySiteContext.put(nIdKey, context);
		return nIdKey;
	}
	
	
	public  WorkflowDeploySiteContext getWorkflowDeploySiteContext(int nIdContext)
	{
		
		return _mapWorkflowDeploySiteContext.get(nIdContext);
	}
	
	
	public  String checkoutSite(WorkflowDeploySiteContext context)
	{
			//reinitialisation du command Result
			DeploymentUtils.reInitCommandResult(context);
			
			String strSvnCheckoutSiteUrl;
			Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
			Application application=_applicationService.getApplication(context.getIdApplication(), plugin);
			if(context.getTagToDeploy()!=null)
			{
				strSvnCheckoutSiteUrl=SVNUtils.getSvnUrlTagSite(application.getSvnUrlSite(), context.getTagToDeploy());
			
			}
			else
			{
				strSvnCheckoutSiteUrl=SVNUtils.getSvnUrlTrunkSite(application.getSvnUrlSite());
			}
			
			_svnService.doSvnCheckoutSite(application.getSiteName(),strSvnCheckoutSiteUrl,context.getMavenUser(),context.getCommandResult());
			
			return null;
	}
	
	
	public void  initTagInformations(WorkflowDeploySiteContext context)
	{
		
		//reinitialisation du command Result
		DeploymentUtils.reInitCommandResult(context);
	
		Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
		Application application=_applicationService.getApplication(context.getIdApplication(), plugin);
	
		
		String strVersion=null;
		String strNextVersion=null;
		String strTagName=null;
		//release version and next version
		try {
			strVersion=ReleaseUtils.getReleaseVersion(DeploymentUtils.getPathCheckoutSite(application.getSiteName()));
			strNextVersion=ReleaseUtils.getNextVersion(strVersion);
			strTagName=ReleaseUtils.getReleaseName(application.getSiteName(), strVersion);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			AppLogService.error(e);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		context.setTagName(strTagName);
		context.setTagVersion(strVersion);
		context.setNextVersion(strNextVersion);
		
	}
	
	public  String tagSite(WorkflowDeploySiteContext context)
	{
			//reinitialisation du command Result
			DeploymentUtils.reInitCommandResult(context);
	
			Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
			Application application=_applicationService.getApplication(context.getIdApplication(), plugin);
			_svnService.doSvnTagSite(application.getSiteName(), application.getSvnUrlSite(), context.getTagName(), context.getNextVersion(), context.getTagVersion(),context.getMavenUser(),context.getCommandResult());
			//update context status
			context.setTagToDeploy(context.getTagName());
		return null;
	}
	
	
	public String assemblySite(WorkflowDeploySiteContext context)
	{
		//reinitialisation du command Result
		DeploymentUtils.reInitCommandResult(context);
	
		Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
		Application application=_applicationService.getApplication(context.getIdApplication(), plugin);
		
		Environment environment=_environmentService.getEnvironment(context.getCodeEnvironement());
		_mavenService.mvnSiteAssembly(application.getSiteName(),context.getTagName(), environment, context.getMavenUser(),context.getCommandResult());
		
		return null;
	
	}
	
	
	public String deploySite(WorkflowDeploySiteContext context)
	{
		//reinitialisation du command Result
		DeploymentUtils.reInitCommandResult(context);
	
		Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
		Application application=_applicationService.getApplication(context.getIdApplication(), plugin);
		ServerApplicationInstance serverApplicationInstance=_environmentService.getServerApplicationInstance(application.getCode(),context.getCodeServerAppplicationInstance(),context.getCodeEnvironement());
		
		_ftpService.uploadFile(application.getWebAppName()+ConstanteUtils.ARCHIVE_WAR_EXTENSION, DeploymentUtils.getPathArchiveGenerated(DeploymentUtils.getPathCheckoutSite(application.getSiteName()), context.getTagToDeploy(), ConstanteUtils.ARCHIVE_WAR_EXTENSION), serverApplicationInstance.getFtpInfo(), serverApplicationInstance.getFtpDeployDirectoryTarget(), context.getCommandResult());
		return null;
	
	}
	
	
	
	

}
