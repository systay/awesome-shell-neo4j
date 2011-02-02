//package org.neo4j.app.awesome_shell.kernel
//
//import org.neo4j.graphdb.index.Index
//
//abstract class SimpleIndex[T](container:SimpleGraphDatabase)
//{
//  def add(entity:T, property:String) = null
//
//  def getSingle(property:String, value:Any):T = new SimpleNode(inner.get(property, value).getSingle(), container)
//}