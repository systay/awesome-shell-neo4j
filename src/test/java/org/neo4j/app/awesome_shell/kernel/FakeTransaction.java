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

import org.neo4j.graphdb.Transaction;

public class FakeTransaction implements Transaction
{
	private Transaction inner;

	public FakeTransaction( Transaction inner )
	{
		this.inner = inner;
	}

	public static int failure = 0;
	public static int success = 0;
	public static int finish = 0;

	public static void reset()
	{
		failure = 0;
		success = 0;
		failure = 0;
	}

	@Override
	public void failure()
	{
		failure++;
		inner.failure();
	}

	@Override
	public void success()
	{
		success++;
		inner.success();
	}

	@Override
	public void finish()
	{
		finish++;
		inner.finish();
	}
}
