package fr.paris.lutece.plugins.deployment.service;

import fr.paris.lutece.plugins.deployment.business.WorkflowDeploySiteContext;

public interface IWorkflowDeploySiteService {

	int addWorkflowDeploySiteContext(WorkflowDeploySiteContext context);
	
	WorkflowDeploySiteContext getWorkflowDeploySiteContext(int nIdContext);
	
	String checkoutSite(WorkflowDeploySiteContext context);
	
	String tagSite(WorkflowDeploySiteContext context);
	
	String assemblySite(WorkflowDeploySiteContext context);
	
	
	void initTagInformations(WorkflowDeploySiteContext context);
	

}