package org.neo4j.app.awesome_shell.kernel

import org.neo4j.graphdb.Relationship

class SimpleRelationship(inner: Relationship, container: SimpleGraphDatabase)
{
  def getProperty(key: String): Object = inner.getProperty(key)

  def getTypeName() = inner.getType().name;

  def getEndNode() = new SimpleNode(inner.getEndNode, container)

  def getStartNode() = new SimpleNode(inner.getStartNode, container)
}