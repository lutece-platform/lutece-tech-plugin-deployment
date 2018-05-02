/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import fr.paris.lutece.portal.service.rbac.RBACAction;

public class ManageApplicationAction implements RBACAction {

	private String _strPermission;
	private String _strUrl;
	private String _strIconCssClass;
	private String _strI18nKeyTitle;
	private String _strI18nKeyName;
	
	public String getI18nKeyTitle() {
		return _strI18nKeyTitle;
	}

	public void setI18nKeyTitle(String strTitle) {
		this._strI18nKeyTitle = strTitle;
	}
	
	public String getI18nKeyName() {
		return _strI18nKeyName;
	}

	public void setI18nKeyName(String strName) {
		this._strI18nKeyName = strName;
	}

	@Override
	public String getPermission() {
		return _strPermission;
	}

	public void setPermission(String strPermission) {
		// TODO Auto-generated method stub
		_strPermission = strPermission;
	}

	public String getUrl() {
		return _strUrl;
	}

	public void setUrl(String _strUrl) {
		this._strUrl = _strUrl;
	}

	public void setIconCssClass(String _strIconCssClass) {
		this._strIconCssClass = _strIconCssClass;
	}

	public String getIconCssClass() {
		return _strIconCssClass;
	}


}
