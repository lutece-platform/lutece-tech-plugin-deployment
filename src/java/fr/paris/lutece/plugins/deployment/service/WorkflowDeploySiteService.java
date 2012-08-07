package fr.paris.lutece.plugins.deployment.service;

import java.io.FileNotFoundException;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import fr.paris.lutece.plugins.deployment.business.Application;
import fr.paris.lutece.plugins.deployment.business.Environment;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;
import fr.paris.lutece.plugins.deployment.business.WorkflowDeploySiteContext;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.plugins.deployment.util.ReleaseUtils;
import fr.paris.lutece.plugins.deployment.util.SVNUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

public class WorkflowDeploySiteService implements IWorkflowDeploySiteService {

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
	
	
	public  int addWorkflowDeploySiteContext(WorkflowDeploySiteContext context)
	{
		
		return -1;
	}
	
	
	public  WorkflowDeploySiteContext getWorkflowDeploySiteContext(int nIdContext)
	{
		
		return null;
	}
	
	
	public  String checkoutSite(WorkflowDeploySiteContext context)
	{
			String strSvnCheckoutSiteUrl;
			Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
			Application application=_applicationService.getApplication(context.getIdApplication(), plugin);
			if(context.getTagName()!=null)
			{
				strSvnCheckoutSiteUrl=SVNUtils.getSvnUrlTagSite(application.getSvnUrlSite(), context.getTagName());
			
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
			e.printStackTrace();
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

			Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
			Application application=_applicationService.getApplication(context.getIdApplication(), plugin);
			_svnService.doSvnTagSite(application.getSiteName(), application.getSvnUrlSite(), context.getTagName(), context.getNextVersion(), context.getTagVersion(),context.getMavenUser(),context.getCommandResult());
			//update context status
			context.setTagToDeploy(context.getTagName());
		return null;
	}
	
	
	public String assemblySite(WorkflowDeploySiteContext context)
	{
		
		Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
		Application application=_applicationService.getApplication(context.getIdApplication(), plugin);
		
		Environment environment=_environmentService.getEnvironment(context.getCodeEnvironement());
		_mavenService.mvnSiteAssembly(application.getSiteName(),context.getTagName(), environment, context.getMavenUser(),context.getCommandResult());
		
		return null;
	
	}
	
	
	public String deploySite(WorkflowDeploySiteContext context)
	{
		
		Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
		Application application=_applicationService.getApplication(context.getIdApplication(), plugin);
		ServerApplicationInstance serverApplicationInstance=_environmentService.getServerApplicationInstance(application.getCode(),context.getCodeServerAppplicationInstance(),context.getCodeEnvironement());
		
		_ftpService.uploadFile(application.getWebAppName()+ConstanteUtils.CONSTANTE_SEPARATOR_POINT+ConstanteUtils.ARCHIVE_WAR_EXTENSION, DeploymentUtils.getPathArchiveGenerated(DeploymentUtils.getPathCheckoutSite(application.getSiteName()), context.getTagToDeploy(), ConstanteUtils.ARCHIVE_WAR_EXTENSION), serverApplicationInstance.getFtpInfo(), serverApplicationInstance.getFtpDeployDirectoryTarget()+ConstanteUtils.CONSTANTE_SEPARATOR_SLASH+application.getWebAppName(), context.getCommandResult());
		return null;
	
	}
	
	
	
	

}
