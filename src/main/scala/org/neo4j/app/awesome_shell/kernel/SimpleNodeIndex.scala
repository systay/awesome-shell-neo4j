package org.neo4j.app.awesome_shell.kernel

import org.neo4j.graphdb.Node
import org.neo4j.graphdb.index.Index

class SimpleNodeIndex(inner: Index[ Node ], container: SimpleGraphDatabase)
{
  def add(entity: SimpleNode, property: String) = container.autocommit( () => inner.add(entity.getInner, property, entity.getProperty(property)))

  def getSingle(property: String, value: Any): SimpleNode =
  {
    val node: Node = inner.get(property, value).getSingle()
    if(node == null)
      throw new NoSuchElementException("Could not find such an element in the index")

    new SimpleNode(node, container)
  }
}