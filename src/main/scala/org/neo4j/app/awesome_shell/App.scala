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