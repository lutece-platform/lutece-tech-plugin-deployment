package fr.paris.lutece.plugins.deployment.business;

public class CommandResult implements Cloneable
{
		// synchro needed?
	
	public static int STATUS_EXCEPTION=Integer.MIN_VALUE;
	private StringBuffer _strLog;
	private int _nStatus;
	private boolean _bRunning;
	private String _strIdError;
	
	/**
	 * "Getter method" pour la variable {@link #_strLog}
	 * @return La variable {@link #_strLog}
	 */
	public StringBuffer getLog()
	{
		return _strLog;
	}
	/**
	 * "Setter method" pour la variable {@link #_strLog}
	 * @param strLog La nouvelle valeur de la variable {@link #_strLog}
	 */
	public void setLog( StringBuffer strLog )
	{
		_strLog = strLog;
	}
	/**
	 * "Getter method" pour la variable {@link #_nStatus}
	 * @return La variable {@link #_nStatus}
	 */
	public int getStatus()
	{
		return _nStatus;
	}
	/**
	 * "Setter method" pour la variable {@link #_nStatus}
	 * @param nStatus La nouvelle valeur de la variable {@link #_nStatus}
	 */
	public void setStatus( int nStatus )
	{
		_nStatus = nStatus;
	}
	/**
	 * "Getter method" pour la variable {@link #_bRunning}
	 * @return La variable {@link #_bRunning}
	 */
	public boolean isRunning(  )
	{
		return _bRunning;
	}
	/**
	 * "Setter method" pour la variable {@link #_bRunning}
	 * @param bRunning La nouvelle valeur de la variable {@link #_bRunning}
	 */
	public void setRunning( boolean bRunning )
	{
		_bRunning = bRunning;
	}
	
	/**
	 * 
	 *{@inheritDoc}
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		
		CommandResult clone = ( CommandResult ) super.clone(  );
		clone._bRunning = this._bRunning;
		clone._nStatus = this._nStatus;
		clone._strLog = this._strLog;
		clone._strIdError = this._strIdError;
		
		return clone;
	}
	/**
	 * "Getter method" pour la variable {@link #_nIdError}
	 * @return La variable {@link #_nIdError}
	 */
	public String getIdError()
	{
		return _strIdError;
	}
	/**
	 * "Setter method" pour la variable {@link #_nIdError}
	 * @param nIdError La nouvelle valeur de la variable {@link #_nIdError}
	 */
	public void setIdError( String strIdError )
	{
		_strIdError = strIdError;
	}
	
	
}
