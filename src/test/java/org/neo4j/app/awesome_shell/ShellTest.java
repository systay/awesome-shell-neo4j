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

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.StringContains.containsString;

public class ShellTest extends ShellTestsBase
{
	private int called = 0;

	@Test
	public void helloWorldWorks()
	{
		addScriptLine( "3+5" );

		String result = runShell();
		assertThat( result, containsString( "8" ) );
	}

	private String runShell()
	{
		Shell shell = createShell();
		shell.startRepl();

		return output.toString();
	}


	@Test
	public void addedObjectsAreVisibleInJS() throws IOException
	{
		addScriptLine( "test.works();" );

		Shell shell = createShell();
		shell.addObject( "test", this );
		shell.startRepl();

		String result = output.toString();
		assertThat( result, containsString( "It sure does" ) );
		assertThat( called, is( 1 ) );
	}

	@Test
	public void shouldBeAbleToRunPreStartScript()
	{
		addScriptLine( "doIt();" );

		Shell shell = createShell();
		shell.addObject( "foo", this );
		shell.addStartUpScript( "function doIt() { foo.didIt('WORKS') }" );
		shell.startRepl();

		assertThat( deed, is( "WORKS" ) );
	}

	@Test
	public void shouldBeAbleToHandleMultiLines()
	{
		addScriptLine( "var r = {" );
		addScriptLine( "}" );

		String result = runShell();


		assertThat( result, not( containsString( "ERROR" ) ) );
	}

	@Test
	public void shouldPrintOutLatestStackTrace()
	{
		addScriptLine( "foo.crash();" );
		addScriptLine( "_stackTrace" );

		Shell shell = createShell();
		shell.addObject( "foo", this );
		shell.startRepl();
		String result = output.toString();

		assertThat( result, containsString( "inner_method" ) );
	}

	private String deed = "UNINITIALIZED";

	public void didIt( String deed )
	{
		this.deed = deed;
	}

	public void crash()
	{
		inner_method();
	}

	private void inner_method()
	{
		throw new RuntimeException( "Oh noes!" );
	}


	public String works()
	{
		called++;
		return "It sure does";
	}

}
