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