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
