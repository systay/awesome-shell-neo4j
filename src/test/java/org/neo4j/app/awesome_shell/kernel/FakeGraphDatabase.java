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

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.event.KernelEventHandler;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.kernel.AbstractGraphDatabase;
import org.neo4j.kernel.Config;
import org.neo4j.kernel.ImpermanentGraphDatabase;

import java.io.IOException;

public class FakeGraphDatabase extends AbstractGraphDatabase
{
	private AbstractGraphDatabase inner;

	public FakeGraphDatabase()
	{
		try
		{
			this.inner = new ImpermanentGraphDatabase();
		} catch ( IOException e )
		{
			throw new RuntimeException( e );
		}
	}

	@Override
	public String getStoreDir()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Config getConfig()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public <T> T getManagementBean( Class<T> type )
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public boolean isReadOnly()
	{
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Node createNode()
	{
		return inner.createNode();
	}

	@Override
	public Node getNodeById( long id )
	{
		return inner.getNodeById( id );
	}

	@Override
	public Relationship getRelationshipById( long id )
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Node getReferenceNode()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Iterable<Node> getAllNodes()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Iterable<RelationshipType> getRelationshipTypes()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void shutdown()
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Transaction beginTx()
	{
		return new FakeTransaction( inner.beginTx() );
	}

	@Override
	public <T> TransactionEventHandler<T> registerTransactionEventHandler( TransactionEventHandler<T> handler )
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public <T> TransactionEventHandler<T> unregisterTransactionEventHandler( TransactionEventHandler<T> handler )
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public KernelEventHandler registerKernelEventHandler( KernelEventHandler handler )
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public KernelEventHandler unregisterKernelEventHandler( KernelEventHandler handler )
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public IndexManager index()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
