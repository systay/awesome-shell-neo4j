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
