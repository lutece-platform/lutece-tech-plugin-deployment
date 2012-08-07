package fr.paris.lutece.plugins.deployment.business;


import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;

public interface IApllicationDAO
{
    Application findByPrimaryKey( int nIdApplication,Plugin plugin );

    List<Application> findByFilter( FilterDeployment filter,Plugin plugin );

    int insert( Application application,Plugin plugin );

    void update( Application application,Plugin plugin );

    void delete(int nIdApplication,Plugin plugin );
}
