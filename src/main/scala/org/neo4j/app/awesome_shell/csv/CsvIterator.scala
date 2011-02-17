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

import au.com.bytecode.opencsv.CSVReader;
import java.util.Iterator

class CsvIterator(reader: CSVReader) extends Iterator[Array[String]] {
  private var nextLine : Array[String]  = reader.readNext

  def next() : Array[String] = {
    val temp = nextLine
    nextLine = reader.readNext
    return temp
  }

  def hasNext() = nextLine != null

  def remove() = throw new UnsupportedOperationException( "This is a read only iterator." )
}