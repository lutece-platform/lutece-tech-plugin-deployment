package fr.paris.lutece.plugins.deployment.service;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.deployment.business.Application;
import fr.paris.lutece.plugins.deployment.business.FilterDeployment;
import fr.paris.lutece.plugins.deployment.business.IApllicationDAO;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

public class ApplicationService implements IApplicationService
{
    private static final String PROPERTY_SVN_CATEGORIES_LIST = "deployment.svnCategories.list";
    private static final String CONSTANTE_SVN_CATEGORY = "deployment.svnCategory.";
    private static final String CONSTANTE_SVN_CATEGORY_NAME = ".name";
    private static final String CONSTANTE_SEPARATOR = ",";
   // private static IApplicationService _singleton;
    private static ReferenceList _listCategory;
    @Inject
    private IApllicationDAO _applicationDAO;

    private ApplicationService(  )
    {
        init(  );
    }

//    public static IApplicationService getInstance(  )
//    {
//        if ( _singleton == null )
//        {
//            _singleton = new ApplicationService(  );
//        }
//
//        return _singleton;
//    }

    /* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#getListApplications(fr.paris.lutece.plugins.deployment.business.FilterDeployment, fr.paris.lutece.portal.service.plugin.Plugin)
	 */
    public List<Application> getListApplications( FilterDeployment filter,Plugin plugin )
    {
        return _applicationDAO.findByFilter( filter,plugin );
    }
    
    
    public Application getApplication( int nIdApplication,Plugin plugin )
    {
        return _applicationDAO.findByPrimaryKey(nIdApplication, plugin);
    }
    

    /* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#init()
	 */
    public void init(  )
    {
    	initListCategories(  );
    }

    private void initListCategories(  )
    {
        _listCategory= new ReferenceList();
        
         String strCategoriesList = AppPropertiesService.getProperty ( PROPERTY_SVN_CATEGORIES_LIST );

        if ( StringUtils.isNotBlank( strCategoriesList ) )
        {
            String[] tabCategoriesCode = strCategoriesList.split( CONSTANTE_SEPARATOR );
            String strCategoryName;

            for ( int i = 0; i < tabCategoriesCode.length; i++ )
            {
                strCategoryName = AppPropertiesService.getProperty ( CONSTANTE_SVN_CATEGORY + tabCategoriesCode[i] +
                                                                 CONSTANTE_SVN_CATEGORY_NAME );
                if ( StringUtils.isNotBlank( strCategoryName ) )
                {
                     _listCategory.addItem( tabCategoriesCode[i],strCategoryName);
                }
            }
        }
    }

    /* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#createApplication(fr.paris.lutece.plugins.deployment.business.Application, fr.paris.lutece.portal.service.plugin.Plugin)
	 */
    public void createApplication( Application application,Plugin plugin )
    {
  
    	_applicationDAO.insert( application,plugin );
    }

    /* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#updateApplication(fr.paris.lutece.plugins.deployment.business.Application, fr.paris.lutece.portal.service.plugin.Plugin)
	 */
    public void updateApplication( Application application,Plugin plugin )
    {
        _applicationDAO.update( application,plugin );
    }

    /* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#deleteApplication(int, fr.paris.lutece.portal.service.plugin.Plugin)
	 */
    public void deleteApplication( int nIdApplication ,Plugin plugin)
    {
        _applicationDAO.delete( nIdApplication,plugin );
    }

    /* (non-Javadoc)
	 * @see fr.paris.lutece.plugins.deployment.service.IApplicationService#getListCategory()
	 */
    public ReferenceList getListCategory(  )
    {
        return _listCategory;
    }
}
