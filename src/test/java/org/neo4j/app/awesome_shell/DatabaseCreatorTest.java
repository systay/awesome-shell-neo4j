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
