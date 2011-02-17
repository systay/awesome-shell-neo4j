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
