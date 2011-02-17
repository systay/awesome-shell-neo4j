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
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class DatabaseCreatorTest
{
	@Test
	public void shouldCreateDatabaseFromString() throws Exception
	{
		DatabaseCreator dbCreator = new DatabaseCreator();
		String tempDir = createTempDir();
		GraphDatabaseService db = dbCreator.createDatabase( tempDir );
		assertThat( db, not( nullValue() ) );
	}

	@Test
	public void shouldReturnReadonlyDatabaseIfLocationAlreadyTaken() throws Exception
	{
		String tempDir = createTempDir();

		EmbeddedGraphDatabase lock = new EmbeddedGraphDatabase( tempDir );

		DatabaseCreator dbCreator = new DatabaseCreator();
		GraphDatabaseService db = dbCreator.createDatabase( tempDir );
		assertThat( db, is( instanceOf( EmbeddedReadOnlyGraphDatabase.class ) ) );
	}


	private static String createTempDir() throws IOException
	{

		File d = File.createTempFile( "neo4j-test", "dir" );
		if ( !d.delete() )
		{
			throw new RuntimeException( "temp config directory pre-delete failed" );
		}
		if ( !d.mkdirs() )
		{
			throw new RuntimeException( "temp config directory not created" );
		}
		d.deleteOnExit();
		return d.getAbsolutePath();
	}
}
