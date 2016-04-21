<%@ page errorPage="../../ErrorPage.jsp" %>
<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="deployment" scope="session" class="fr.paris.lutece.plugins.deployment.web.DeploymentJspBean" />
<%
    deployment.init( request, fr.paris.lutece.plugins.deployment.web.DeploymentJspBean.RIGHT_DEPLOYMENT_MANAGEMENT );
    String strResult=deployment.getProcessDeployApplication( request ) ;
%>
<jsp:include page="../../AdminHeader.jsp" />
<%= strResult %>
<%@ include file="../../AdminFooter.jsp" %>