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




    