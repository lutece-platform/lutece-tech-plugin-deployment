/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
package fr.paris.lutece.plugins.deployment.business;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.deployment.util.ConstanteUtils;

public class FilterDeployment {
	private String _strCodeCategory;
	private String _strWorkgroup;

	private int _nIdApplication = ConstanteUtils.CONSTANTE_ID_NULL;

	public void setCodeCategory(String strCodeCategory) {
		this._strCodeCategory = strCodeCategory;
	}

	public String getCodeCategory() {
		return _strCodeCategory;
	}

	public boolean containsCodeCategoryFilter() {
		return (_strCodeCategory != null) && !_strCodeCategory.isEmpty();
	}

	public void setIdApplication(int nIdApplication) {
		this._nIdApplication = nIdApplication;
	}

	public int getIdApplication() {
		return _nIdApplication;
	}

	public boolean containsIdApplicationFilter() {
		return _nIdApplication != ConstanteUtils.CONSTANTE_ID_NULL;
	}

	public String getWorkgroup() {
		return _strWorkgroup;
	}

	public void setWorkGroup(String workgroup) {
		_strWorkgroup = workgroup;
	}

	  /**
    *
    * @return true if the filter contain workgroup criteria
    */
   public boolean containsWorkgroupFilter(  )
   {
       return (!StringUtils.isEmpty( _strWorkgroup) && !ConstanteUtils.CONSTANTE_ALL.equals( _strWorkgroup));
   }
}
