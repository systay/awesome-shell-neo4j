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
package org.neo4j.app.awesome_shell

import java.io.{InputStream, OutputStream}
import kernel.{File, SimpleGraphDatabase}
import org.neo4j.graphdb.Direction
import org.neo4j.kernel.AbstractGraphDatabase

class ShellCreator
{
  val filterFunction = "function filter(map) { return MapFilter.filter(map, Array.prototype.slice.call(arguments,1)); }"

  def createStandardShell(input: InputStream, output: OutputStream, db: AbstractGraphDatabase): Shell =
  {

    val shell = new Shell(System.in, System.out)
    shell.addObject("db", new SimpleGraphDatabase(db))
    shell.addObject("File", File)
    shell.addObject("MapFilter", MapFilter)
    shell.addObject("out", output)
    shell.addObject("OUTGOING", Direction.OUTGOING)
    shell.addObject("INCOMING", Direction.INCOMING)
    shell.addObject("BOTH", Direction.BOTH)
    shell.addStartUpScript(filterFunction);

    return shell
  }
}