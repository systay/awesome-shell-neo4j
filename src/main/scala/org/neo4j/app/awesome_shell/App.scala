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

import scala.io.Source.fromFile
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase

object App
{
  def main(args: Array[ String ]) =
  {

    if ( args.isEmpty || args.size > 2 )
    {
      System.out.println("ash /path/to/db [scriptFile]")
    }
    else
    {
      val creator = new ShellCreator
      val databaseCreator = new DatabaseCreator
      val db = databaseCreator.createDatabase(args(0))

      if(db.isInstanceOf[EmbeddedReadOnlyGraphDatabase])
      {
        System.out.println("WARNING: Database already in use. Shell is in read-only mode!")
      }

      val shell = creator.createStandardShell(System.in, System.out, db)

      if ( args.size == 2 )
      {
        val script = fromFile(args(1)).mkString
        shell.addStartUpScript(script)
        shell.startScript()
      } else
      {
        shell.startRepl()
      }

      db.shutdown();
    }
  }
}