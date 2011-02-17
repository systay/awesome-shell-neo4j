/**
 * Copyright (c) 2002-2011 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
