package org.neo4j.app.awesome_shell.kernel;

import org.junit.Test;
import org.neo4j.kernel.ImpermanentGraphDatabase;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimpleGraphDatabaseTest
{
	@Test
	public void shouldAutoCommitWhileNotInTransactions() throws IOException
	{
		SimpleGraphDatabase db = new SimpleGraphDatabase( new ImpermanentGraphDatabase() );
		db.createNode();    // If this test doesn't work, this method call will cast an exception
		db.shutdown();
	}

	@Test
	public void shouldBeAbleToCommitEvery3Changes()
	{
		SimpleGraphDatabase db = new SimpleGraphDatabase( new FakeGraphDatabase() );
		SimpleNode node = db.createNode();
		FakeTransaction.reset();

		db.beginChunkyTx( 3 );
		for ( int i = 0; i < 5; i++ )
		{
			node.setProperty( "key" + i, "value" );
		}
		db.commitChunkyTx();

		assertThat( FakeTransaction.success, is( 2 ) );
	}

	@Test
	public void shouldCommitEvenIfTheChunkIsNotFull()
	{
		SimpleGraphDatabase db = new SimpleGraphDatabase( new FakeGraphDatabase() );
		SimpleNode node = db.createNode();
		FakeTransaction.reset();

		db.beginChunkyTx( 100 );
		for ( int i = 0; i < 5; i++ )
		{
			node.setProperty( "key" + i, "value" );
		}
		db.commitChunkyTx();

		assertThat( FakeTransaction.success, is( 1 ) );
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowIfGivenANullInnerDb() throws Exception
	{
		new SimpleGraphDatabase( null );
	}


}
