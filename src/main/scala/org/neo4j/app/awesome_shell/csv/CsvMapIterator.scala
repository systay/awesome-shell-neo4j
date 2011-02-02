package org.neo4j.app.awesome_shell.csv

import java.util.Iterator
import java.lang.Iterable
import java.util.Map
import java.util.HashMap

/**
 * This class wraps a Iterable<String[]> and outputs a map for each line, with key/value pairs
 */
class CsvMapIterator(input: Iterator[Array[String]])
        extends Iterator[Map[String, Object]] with Iterable[Map[String, Object]] {

  def this(iterable:Iterable[Array[String]]) = this(iterable.iterator)

  private val columnNames = input.next()

  def next(): Map[String, Object] = {
    val currentLine = input.next
    val map = new HashMap[String, Object]()

    (0 until columnNames.length).
            foreach(i => map.put(columnNames(i), currentLine(i)))

    return map
  }

  def iterator() = this

  def hasNext() = input.hasNext

  def remove() = throw new UnsupportedOperationException("This is a read-only iterator")
}




    