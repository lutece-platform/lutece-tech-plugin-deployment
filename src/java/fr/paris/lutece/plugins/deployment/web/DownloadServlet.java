/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.deployment.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.plugins.deployment.business.Application;
import fr.paris.lutece.plugins.deployment.business.CommandResult;
import fr.paris.lutece.plugins.deployment.business.ServerApplicationInstance;
import fr.paris.lutece.plugins.deployment.service.DeploymentPlugin;
import fr.paris.lutece.plugins.deployment.service.IApplicationService;
import fr.paris.lutece.plugins.deployment.service.IFtpService;
import fr.paris.lutece.plugins.deployment.service.IServerApplicationService;
import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.plugins.deployment.util.DeploymentUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;


/**
 * Servlet serving document file resources
 */
public class DownloadServlet extends HttpServlet
{
	/**
	 *
	 */
	private static final long serialVersionUID = 8625639667629973645L;
     private  IFtpService _ftpService = SpringContextService.getBean( 
            "deployment.FtpService" );
    private IServerApplicationService _serverApplicationService = SpringContextService.getBean( 
            "deployment.ServerApplicationService" );
    private IApplicationService _applicationService = SpringContextService.getBean( "deployment.ApplicationService" );
    
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException the servlet Exception
     * @throws IOException the io exception
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        

    	 Plugin plugin = PluginService.getPlugin( DeploymentPlugin.PLUGIN_NAME );
    	 String strCodeEnvironment = request.getParameter( ConstanteUtils.PARAM_CODE_ENVIRONMENT );
    	 String strCodeServerApplicationInstanceSql = request.getParameter( ConstanteUtils.PARAM_CODE_SERVER_APPLICATION_INSTANCE_SQL );
         String strCodeDatabase = request.getParameter( ConstanteUtils.PARAM_CODE_DATABASE );
         String strIdApplication= request.getParameter( ConstanteUtils.PARAM_CODE_APPLICATION );
         String strServerApplicationType = request.getParameter( ConstanteUtils.PARAM_SERVER_APPLICATION_TYPE );
         
         Application application = _applicationService.getApplication( DeploymentUtils.getIntegerParameter(strIdApplication), plugin );
         
         ServerApplicationInstance serverApplicationInstance = _serverApplicationService.getServerApplicationInstance( application,
        		 strCodeServerApplicationInstanceSql,
                 strCodeEnvironment, strServerApplicationType, request.getLocale(), false, false );
       
    	
         
    	 response.setHeader( "Content-Disposition", "attachment ;filename=\"dump_"+strCodeDatabase+ ".sql\";" );
         response.setHeader( "Pragma", "public" );
         response.setHeader( "Expires", "0" );
         response.setHeader( "Cache-Control", "must-revalidate,post-check=0,pre-check=0" );
         response.setHeader("Content-Type", "application/octet-stream");

     
         CommandResult commandResult=new CommandResult();
         
         try {
			_ftpService.getFile(response.getOutputStream(), serverApplicationInstance.getFtpInfo(  ), DeploymentUtils.getDumpFileDirectory(application.getCode(), serverApplicationInstance)+"FROM_Z00-"+strCodeDatabase+"-ALL_TABLES.sql", commandResult);
		} catch (IOException e) {
			AppLogService.error(e);
		}
    	

    }

    /**
     * Returns a short description of the servlet.
     * @return message
     */
    @Override
    public String getServletInfo(  )
    {
        return "Servlet serving files content from core_file and core_physical_file tables";
    }
}
