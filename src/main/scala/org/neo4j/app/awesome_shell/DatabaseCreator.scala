package org.neo4j.app.awesome_shell

import org.neo4j.graphdb.TransactionFailureException
import org.neo4j.kernel.{AbstractGraphDatabase, EmbeddedReadOnlyGraphDatabase, EmbeddedGraphDatabase}

class DatabaseCreator
{
  def createDatabase(location: String): AbstractGraphDatabase =
  {
    try
    {
      new EmbeddedGraphDatabase(location)
    } catch
    {
      case e: TransactionFailureException => if ( databaseIsAlreadyLocked(e) )
        new EmbeddedReadOnlyGraphDatabase(location)
      else
        throw e;
    }
  }

  def databaseIsAlreadyLocked(e: TransactionFailureException): Boolean =
  {
    val x = e.getCause.getCause
    return x.getMessage.startsWith("Unable to lock store")
  }
}