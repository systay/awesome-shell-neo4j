package org.neo4j.app.awesome_shell

import jline.ConsoleReader
import scala.collection.mutable.HashMap
import org.mozilla.javascript._
import java.io._

class Shell(input: InputStream, output: OutputStream)
{
  private val in: ConsoleReader = new ConsoleReader(input, new OutputStreamWriter(output))
  private val out: PrintStream = new PrintStream(output)
  private val shellObjects = new HashMap[ String, Object ]()
  private val startupScript = new StringBuilder
  private var lastStackTrace: String = ""

  def addObject(name: String, obj: Object) = shellObjects.put(name, obj)

  def addStartUpScript(script: String) = startupScript.append(script).append("\n")

  private def runShell(interactive: Boolean) =
  {
    val ctx = Context.enter
    try
    {
      val scope = ctx.initStandardObjects
      addObjectsToShell(scope)
      val result = ctx.evaluateString(scope, startupScript.toString, "", 1, null)

      var continue = interactive
      while ( continue )
      {
        val source = readFromLine(ctx)
        source match
        {
          case "quit" =>
          {
            continue = false
            out.println("Quitting... Thanks for today!")
          }
          case "_stackTrace" => out.println(lastStackTrace)
          case null => continue = false
          case _ => parseSource(source, ctx, scope, interactive)
        }

      }
    } finally Context.exit
  }

  private def parseSource(source: String, ctx: Context, scope: ScriptableObject, interactive: Boolean) =
  {
    val result = try
    {
      ctx.evaluateString(scope, source, "", 1, null);
    } catch
    {
      case e: WrappedException => handleException(e.getWrappedException())
      case e => handleException(e)
    }

    out.println(Context.toString(result));
  }

  private def handleException(e: Throwable): String =
  {
    val result = new StringWriter
    val printWriter = new PrintWriter(result)
    e.printStackTrace(printWriter);
    lastStackTrace = result.toString();

    return "ERROR: " + e.getMessage
  }

  private def readFromLine(ctx: Context): String =
  {
    var source = in.readLine("neo4j: ")
    if ( source == null )
      return null

    while ( !ctx.stringIsCompilableUnit(source) )
    {
      val line = in.readLine(" > ")
      source = source + line + "\n"
    }

    return source
  }

  def startScript() = runShell(false)

  def startRepl() = runShell(true)

  private def addObjectsToShell(scope: ScriptableObject) =
    shellObjects.foreach((keyVal) =>
      {
        val wrappedObject = Context.javaToJS(keyVal._2, scope)
        ScriptableObject.putProperty(scope, keyVal._1, wrappedObject)
      })
}