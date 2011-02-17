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