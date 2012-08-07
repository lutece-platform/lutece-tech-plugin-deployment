package fr.paris.lutece.plugins.deployment.business;

import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;
import fr.paris.lutece.util.string.StringUtil;

public class FilterDeployment
{
    private String _strCodeCategory;
    private int   _nIdApplication=ConstanteUtils.CONSTANTE_ID_NULL;

   
    
    
    public void setCodeCategory( String strCodeCategory )
    {
        this._strCodeCategory = strCodeCategory;
    }

    public String getCodeCategory(  )
    {
        return _strCodeCategory;
    }

    public boolean containsCodeCategoryFilter(  )
    {
        return   _strCodeCategory!= null && _strCodeCategory.isEmpty() ;
    }

	public void setIdApplication(int nIdApplication) {
		this._nIdApplication = nIdApplication;
	}

	public int getIdApplication() {
		return _nIdApplication;
	}
	 public boolean containsIdApplicationFilter(  )
	  {
	        return _nIdApplication != ConstanteUtils.CONSTANTE_ID_NULL;
	    }
}
