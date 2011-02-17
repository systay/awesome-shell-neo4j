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
package org.neo4j.app.awesome_shell.kernel

import java.lang.Iterable
import scala.collection.JavaConverters._
import org.neo4j.graphdb.{Transaction, NotInTransactionException}
import java.util.Map
import java.io.File
import org.apache.commons.io.FileUtils
import org.neo4j.kernel.AbstractGraphDatabase
import org.neo4j.onlinebackup.Neo4jBackup

class SimpleGraphDatabase(inner: AbstractGraphDatabase)
{
  var chunkSize: Int = 0
  var leftToCommit: Int = 0
  var currentChunkyTransaction: Transaction = null
  var commit_function: ( () => Any ) => Any = autoCommit_not_chunky
  @scala.volatile var getInner: () => AbstractGraphDatabase = () => inner
  //This is the only way the inner db should be accessed!

  assert(inner != null, "The inner database should never be null")

  /**
   * Cleanly shuts down the database
   */

  def shutdown = inner.shutdown

  /**
   * Retrieves a node by it Id. Throws an NotFoundException if the node is not available
   */

  def getNodeById(id: Long): SimpleNode = new SimpleNode(getInner().getNodeById(id), this)

  /**
   * Creates and returns a new node
   */

  def createNode: SimpleNode = autocommit(() => new SimpleNode(getInner().createNode, this))


  /**
   * Creates and returns a new node, and sets properties on it from the supplied Map
   */

  def createNode(props: Map[ String, Object ]): SimpleNode = autocommit(() =>
    {
      val node = getInner().createNode
      props.asScala.foreach((kv) => node.setProperty(kv._1, kv._2))
      new SimpleNode(node, this)
    })

  /**
   * Returns the names of all relationship types
   */

  def getRelationshipTypes: Iterable[ String ] = getInner().getRelationshipTypes().asScala.map((x) => x.name).asJava

  /**
   * Creates a transaction and returns it. You are then responsible for marking the transaction
   * successful or not, and finished.
   */

  def beginTx: Transaction = getInner().beginTx

  /**
   * Retrieves the reference node of the graph
   */

  def getReferenceNode: SimpleNode = new SimpleNode(getInner().getReferenceNode(), this)


  /**
   * Sets the database in auto-commit every X changes mode. Use commitChunkyTx when
   * no more changes are left to commit
   */

  def beginChunkyTx(chunkSize: Int) =
  {
    currentChunkyTransaction = getInner().beginTx
    this.chunkSize = chunkSize
    leftToCommit = chunkSize
    commit_function = autoCommit_chunky_mode
  }

  /**
   * Commits any outstanding transactions, and returns the database to normal mode
   */

  def commitChunkyTx =
  {
    try
    {
      currentChunkyTransaction.success
    } finally
    {
      currentChunkyTransaction.finish
      commit_function = autoCommit_not_chunky
    }
  }

  private def autoCommit_chunky_mode(f: () => Any): Any =
  {
    if ( leftToCommit == 0 )
    {
      currentChunkyTransaction.success
      currentChunkyTransaction.finish
      currentChunkyTransaction = getInner().beginTx
      leftToCommit = chunkSize
    }

    val result = f.apply()

    leftToCommit = leftToCommit - 1

    return result
  }

  private def autoCommit_not_chunky(f: () => Any): Any = try
  {
    f()
  } catch
  {
    case e: NotInTransactionException =>
    {
      val tx = getInner().beginTx

      try
      {
        val result = f()
        tx.success
        return result
      } finally tx.finish
    }
  }

  def initializeBackupTo(folder: String) =
  {
    if ( !getInner().isInstanceOf[ AbstractGraphDatabase ] )
    {
      error("Don't know how to make backup of this database. Only support AbstractGraphDatabase")
    }

    val innerDb = getInner().asInstanceOf[ AbstractGraphDatabase ]

    val sourceDir = new File(innerDb.getStoreDir())
    val destDir = new File(folder)

    assert(sourceDir.exists, "Source directory doesn't exist. Weird!")

    makeSureDestinationExistsAndIsEmpty(destDir)

    assert(destDir.exists, "Destination does not exist")

    val temp = getInner
    try
    {
      getInner = () => error("Database locked to perform backup")
      FileUtils.copyDirectory(sourceDir, destDir)
    } finally
    {
      getInner = temp;
    }
  }

  def backupTo(location: String) =
  {
    val backup = Neo4jBackup.neo4jDataSource(getInner(), location)
    backup.doBackup();
  }

  /**
   * Internal method. Don't use this.
   */

  def autocommit[ T ](f: () => T): T = commit_function.apply(f).asInstanceOf[ T ]

  def nodeIndex(indexName:String):SimpleNodeIndex=new SimpleNodeIndex(getInner().index().forNodes(indexName), this)

  private def makeSureDestinationExistsAndIsEmpty(directory: File) =
  {
    if ( directory.exists )
    {
      assert(directory.isDirectory, "Destination must be a directory")
      assert(directory.listFiles.length == 0, "Destination directory must be empty")
    }
    else
    {
      if ( !directory.mkdir() )
      {
        error("Couldn't create destionation directory")
      }
    }
  }
}