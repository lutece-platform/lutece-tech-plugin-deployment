package fr.paris.lutece.plugins.deployment.business;

import java.util.Arrays;
import java.util.List;


public enum MavenGoals
{
	LUTECE_SITE_ASSEMBLY( "clean", "lutece:site-assembly");
	
		
	private List<String> _goals;
	
	MavenGoals( String... goals  )
	{
		_goals = Arrays.asList( goals );
	}
	
	public List<String> asList()
	{
		return _goals;
	}
}
