package fr.paris.lutece.plugins.deployment.service;

import java.util.Locale;

import fr.paris.lutece.plugins.deployment.business.WorkflowDeploySiteContext;

public interface IWorkflowDeploySiteService {

	int addWorkflowDeploySiteContext(WorkflowDeploySiteContext context);
	
	WorkflowDeploySiteContext getWorkflowDeploySiteContext(int nIdContext);
	
	String checkoutSite(WorkflowDeploySiteContext context,Locale locale);
	
	String tagSite(WorkflowDeploySiteContext context,Locale locale);
	
	String assemblySite(WorkflowDeploySiteContext context,Locale locale);
	
	String deploySite(WorkflowDeploySiteContext context,Locale locale);
	
	void initTagInformations(WorkflowDeploySiteContext context);
	

}