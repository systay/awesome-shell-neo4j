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

import org.neo4j.graphdb._
import scala.collection.JavaConverters._
import java.util.Map
import java.lang.Iterable

class SimpleNode(inner: Node, container: SimpleGraphDatabase)
{
  assert(inner != null, "Can not work with a null node")

  /**
   * Returns all relationships going in and out from this node
   */

  def getRelationships: Iterable[ SimpleRelationship ] = wrapInSimpleRelationships(inner.getRelationships())

  /**
   * Returns true if a property with the given key exists in the node
   */

  def hasProperty(key: String): Boolean = inner.hasProperty(key)

  /**
   * Returns the value of a property. Throws a NotFoundException if the property does not exist
   */

  def getProperty(key: String): Any = inner.getProperty(key)

  /**
   * Returns the value of a property, or the given default value if no such property exists
   */

  def getProperty(key: String, default: Any): Any = inner.getProperty(key, default)

  /**
   * Sets a property
   */

  def setProperty(key: String, value: Any): SimpleNode =
  {
    container.autocommit(() => inner.setProperty(key, value))
    return this
  }

  def setProperties(keys: Map[ String, Object ]) = container.autocommit(() => keys.asScala.foreach(
    x => inner.setProperty(x._1, x._2)))

  def removeProperty(key: String) = container.autocommit(() => inner.removeProperty(key))

  def getPropertyKeys: Iterable[ String ] = inner.getPropertyKeys

  def getPropertyValue: Iterable[ Object ] = inner.getPropertyValues

  def getRelationships(relType: String): java.lang.Iterable[ SimpleRelationship ] = wrapInSimpleRelationships(inner.getRelationships(DynamicRelationshipType.withName(relType)))

  def getId: Long = inner.getId

  def getGraphDatabase: GraphDatabaseService = container.getInner()

  def getInner = inner

  def delete = container.autocommit(() =>
    {
      inner.getRelationships.asScala.foreach(r => r.delete)
      inner.delete
    })

  def createRelationshipTo(other: SimpleNode,
                           relationshipName: String) = actuallyCreateRelationshipTo(other, relationshipName, true)

  def reallyCreateRelationshipTo(other: SimpleNode,
                                 relationshipName: String) = actuallyCreateRelationshipTo(other, relationshipName, false)

  def createRelationshipTo(other: SimpleNode, relationshipName: String, props: Map[ String, Object ]) =
  {
    container.autocommit(() =>
      {
        val relationship = inner.createRelationshipTo(other.getInner, DynamicRelationshipType.withName(relationshipName))
        props.asScala.foreach((kv) => relationship.setProperty(kv._1, kv._2))
      })
  }

  def followLinkedList(relationshipName: String, direction: Direction): SimpleNode =
  {
    var current = this
    var rel:SimpleRelationship = null
    do
    {
      rel = current.getSingleRelationship(relationshipName, direction)
      if ( rel != null )
      {
        current = rel.getEndNode()
      }
    } while ( rel != null )

    current
    //This is what I wish I could write. But, tail-call recursion is not still tricky in Scala :(

    //    val rel = getSingleRelationship(relationshipName, direction)
    //
    //    if ( rel == null )
    //    {
    //      this
    //    }
    //    else
    //    {
    //      rel.getEndNode().followLinkedList(relationshipName, direction)
    //    }
  }

  def getSingleRelationship(relName: String): SimpleRelationship = getSingleRelationship(relName, Direction.BOTH)

  def getSingleRelationship(relName: String,
                            direction: Direction): SimpleRelationship = wrapInSimpleRelationship(inner.getSingleRelationship(DynamicRelationshipType.withName(relName), direction))

  private def actuallyCreateRelationshipTo(other: SimpleNode, relationshipName: String,
                                           throwExceptionIfCaseDifference: Boolean): Unit =
  {
    if ( throwExceptionIfCaseDifference )
    {
      assertRelTypeDoesNotExist(relationshipName, () => actuallyCreateRelationshipTo(other, relationshipName, false))
    }

    container.autocommit(() => inner.createRelationshipTo(other.getInner, DynamicRelationshipType.withName(relationshipName)))
  }

  private def assertRelTypeDoesNotExist(name: String, userConfirmed: () => Unit) =
  {
    container.getRelationshipTypes.asScala.
      filter((relType) => relType.equalsIgnoreCase(name) && relType != name).
      foreach((r) => throw new RelationshipTypeWithDifferentCaseAlreadyExistsException(r, name, userConfirmed))
  }

  private def wrapInSimpleRelationships(rels: Iterable[ Relationship ]): Iterable[ SimpleRelationship ] =
    inner.getRelationships().asScala.map(x => new SimpleRelationship(x, container)).asJava

  private def wrapInSimpleRelationship(rel: Relationship): SimpleRelationship =
    if ( rel == null )
    {
      null
    }
    else
    {
      new SimpleRelationship(rel, container)
    }


  override def equals(other: Any): Boolean =
  {
    if ( other == null )
    {
      return false
    };

    if ( !other.isInstanceOf[ SimpleNode ] )
    {
      return false
    };

    return inner.equals(other.asInstanceOf[ SimpleNode ].getInner)
  }

  override def toString: String =
  {
    val sb = new StringBuilder("Node[id=")
    sb.append(getId)

    getPropertyKeys.asScala.foreach((k) => sb.append(",").append(k).append("=\"").append(getProperty(k)).append("\""))

    sb.append("]")
    return sb.toString
  }
}