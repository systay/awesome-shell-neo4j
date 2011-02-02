package org.neo4j.app.awesome_shell.logs

import java.lang.Iterable
import java.io.{BufferedReader, Reader}
import java.util.regex.{Matcher, Pattern}
import java.util.{HashMap, Map, Iterator}
import java.text.SimpleDateFormat

class ApacheLogReader(input: Reader) extends Iterable[ Map[ String, Object ] ] with Iterator[ Map[ String, Object ] ]
{
  val reader = new BufferedReader(input)
  var nextLine: String = reader.readLine
  val pattern = Pattern.compile("^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"GET /\\?(.+?) HTTP/([^\"]+)\" (\\d{3}) (\\d+|-) \"([^\"]+)\" \"([^\"]+)\"")
  val dateTimeFormat = new SimpleDateFormat("dd/MMM/yyyy:kk:m:ss")

  val ParamRE = """(.+)=(.+)""".r

  def next(): Map[ String, Object ] =
  {
    val current = nextLine
    nextLine = reader.readLine

    val matcher: Matcher = pattern.matcher(current)
    if ( !matcher.matches )
    {
      throw new BadLogEntryException(current);
    } else
    {

      val hashMap = new HashMap[ String, Object ]
      hashMap.put("IP", matcher.group(1))
      hashMap.put("Date", dateTimeFormat.parse(matcher.group(4)).getTime.asInstanceOf[AnyRef] )
      val request = matcher.group(5)
      hashMap.put("Req", request)
      hashMap.put("Parameters", getParamsMap(request))
      return hashMap
    }


  }

  private def getParamsMap(request: String) =
  {
    val paramMap = new HashMap[ String, String ]

    request.split("\\+").foreach(_ match
    {
      case ParamRE(key, value) => paramMap.put(key, value)
      case _ =>;
    })
    paramMap
  }

  def hasNext() = nextLine != null

  def iterator: Iterator[ Map[ String, Object ] ] = this

  def remove = throw new UnsupportedOperationException("Read only iterator.")
}

class BadLogEntryException(line: String) extends RuntimeException("Couldn't parse line: " + line)