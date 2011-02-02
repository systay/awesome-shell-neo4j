package org.neo4j.app.awesome_shell;

import org.junit.Before;

import java.io.*;
import java.util.ArrayList;

public abstract class ShellTestsBase
{
	public OutputStream output;
	private ArrayList<String> script;

	protected void addScriptLine( String line )
	{
		script.add( line );
	}

	protected Shell createShell()
	{
		StringBuilder sb = new StringBuilder();
		for ( String line : script )
		{
			sb.append( line ).append( "\n" );
		}
		return new Shell( createScript( sb.toString() ), output );
	}

	private InputStream createScript( String script )

	{
		byte[] bytes;
		try
		{
			bytes = ( script + "\n" ).getBytes( "UTF-8" );
		} catch ( UnsupportedEncodingException e )
		{
			throw new RuntimeException( e );
		}
		return new ByteArrayInputStream( bytes );
	}

	@Before
	public void init() throws Exception
	{
		output = new ByteArrayOutputStream();
		script = new ArrayList<String>();
	}
}
