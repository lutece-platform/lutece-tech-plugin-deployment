package fr.paris.lutece.plugins.deployment.business;

public class FtpInfo {
	
	private String _strHost;
	private int _nPort;
	private String _strUserLogin;
	private String _strUserPassword;
	
	private String _strProxyHost;
	private int _nProxyPort;
	private String _strProxyUserLogin;
	private String _strProxyUserPassword;
	private long _lKeepAliveTimeout;
	
	public String getHost() {
		return _strHost;
	}
	public void setHost(String strHost) {
		_strHost = strHost;
	}
	public int getPort() {
		return _nPort;
	}
	public void setPort(int nPort) {
		_nPort = nPort;
	}
	public String getUserLogin() {
		return _strUserLogin;
	}
	public void setUserLogin(String strUserLogin) {
		_strUserLogin = strUserLogin;
	}
	public String getUserPassword() {
		return _strUserPassword;
	}
	public void setUserPassword(String strUserPassword) {
		_strUserPassword = strUserPassword;
	}
	
	
	
	public String getProxyHost() {
		return _strProxyHost;
	}
	public void setProxyHost(String strHost) {
		_strProxyHost = strHost;
	}
	public int getProxyPort() {
		return _nProxyPort;
	}
	public void setProxyPort(int port) {
		_nProxyPort = port;
	}
	public String getProxyUserLogin() {
		return _strProxyUserLogin;
	}
	public void setProxyUserLogin(String strUserLogin) {
		_strProxyUserLogin = strUserLogin;
	}
	public String getProxyUserPassword() {
		return _strProxyUserPassword;
	}
	public void setProxyUserPassword(String strUserPassword) {
		_strProxyUserPassword = strUserPassword;
	}
	
	public void setKeepAliveTimeout(long _lKeepAliveTimeout) {
		this._lKeepAliveTimeout = _lKeepAliveTimeout;
	}
	public long getKeepAliveTimeout() {
		return _lKeepAliveTimeout;
	}
		

}
