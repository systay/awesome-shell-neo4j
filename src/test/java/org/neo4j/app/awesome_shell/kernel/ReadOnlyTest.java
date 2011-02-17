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
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReadOnlyTest
{
	@Test
	public void shouldNotMessUpLogs() throws Exception
	{
		System.out.println("Starting RW database");
		EmbeddedGraphDatabase rw = new EmbeddedGraphDatabase( "target/temp" );
		Transaction tx = rw.beginTx();

		Node node = rw.createNode();
		long id = node.getId();
		node.setProperty( "monkeys", "rock" );

		rw.index().forNodes( "nodes" ).add( node, "id", id );
		System.out.println(id);

		tx.success();

		System.out.println("Starting RO database");
		EmbeddedReadOnlyGraphDatabase ro = new EmbeddedReadOnlyGraphDatabase( rw.getStoreDir() );

		tx.finish();

		System.out.println("Shutting down RO database");
		ro.shutdown();
		System.out.println("Shutting down RW database");
		rw.shutdown();

		System.out.println("Starting RW database");
		rw = new EmbeddedGraphDatabase( "target/temp" );
		node = rw.getNodeById( id );

		assertThat( (String)node.getProperty( "monkeys" ), is( "rock" ) );

		Node result = rw.index().forNodes( "nodes" ).get( "id", id ).getSingle();

		assertThat(result.getId(), is(id));

		rw.shutdown();
		System.out.println("Shutting down  RW database");
	}
}
