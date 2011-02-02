package org.neo4j.app.awesome_shell.kernel;

import org.junit.Test;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.ImpermanentGraphDatabase;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BackupTests
{
	@Test
	public void shouldBeAbleToCreateFirstDbBackup() throws Exception
	{
		SimpleGraphDatabase db = new SimpleGraphDatabase( new ImpermanentGraphDatabase() );
		db.getReferenceNode().setProperty( "I'm", "ALIVE!!!" );


		String temp = tempPath();
		db.initializeBackupTo( temp );

		EmbeddedGraphDatabase backup = new EmbeddedGraphDatabase( temp );

		String result = (String) backup.getReferenceNode().getProperty( "I'm" );
		assertThat( result, is( "ALIVE!!!" ) );
		backup.shutdown();
	}

	@Test
	public void shouldBeAbleToBackupChangesToDestination() throws Exception
	{
        HashMap<String, String> props = new HashMap<String, String>();
        props.put( "keep_logical_logs", "true" );
        ImpermanentGraphDatabase inner = new ImpermanentGraphDatabase(props);
        SimpleGraphDatabase db = new SimpleGraphDatabase( inner );
		String temp = tempPath();
		db.initializeBackupTo( temp );

		db.getReferenceNode().setProperty( "monkeys", "ROCK" );

		db.backupTo(temp);

		EmbeddedGraphDatabase backup = new EmbeddedGraphDatabase( temp );

		String result = (String) backup.getReferenceNode().getProperty( "monkeys" );
		assertThat( result, is( "ROCK" ) );
		backup.shutdown();
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowIfCanNotCreateDestination() throws Exception
	{
		SimpleGraphDatabase db = new SimpleGraphDatabase( new ImpermanentGraphDatabase() );
		db.initializeBackupTo( "/this/folder/probably/does/not/exist" );
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowIfDestinationAlreadyHasDataInIt() throws Exception
	{
		SimpleGraphDatabase db = new SimpleGraphDatabase( new ImpermanentGraphDatabase() );
		File dir = tempDir();
		createEmptyFileInsideDestinationDir( dir );
		db.initializeBackupTo( dir.getAbsolutePath() );
	}

	private void createEmptyFileInsideDestinationDir( File dir )
			throws IOException
	{
		dir.mkdir();
		new File(dir, "empty.file").createNewFile();
	}

	private static String tempPath() throws IOException
	{
		return tempDir().getAbsolutePath();
	}


	private static java.io.File tempDir() throws IOException
	{

		java.io.File d = java.io.File.createTempFile( "neo4j-test", "dir" );
		if ( !d.delete() )
		{
			throw new RuntimeException( "temp config directory pre-delete failed" );
		}
		if ( !d.mkdirs() )
		{
			throw new RuntimeException( "temp config directory not created" );
		}
		d.deleteOnExit();
		return d;
	}
}
