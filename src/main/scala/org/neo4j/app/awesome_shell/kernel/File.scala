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
package org.neo4j.app.awesome_shell.kernel

import au.com.bytecode.opencsv.CSVReader
import java.io.{FileReader, FileNotFoundException, File => JFile}
import org.neo4j.app.awesome_shell.csv.{CsvMapIterator, CsvIterator}
import org.neo4j.app.awesome_shell.logs.ApacheLogReader

object File
{
  def openCsvForReading(fileName: String): CsvIterator =
  {
    val file = new JFile(fileName)
    if ( !file.exists )
      throw new FileNotFoundException(fileName);

    new CsvIterator(new CSVReader(new FileReader(file)))
  }

  def openCsvWithHeaderForReading(fileName: String): CsvMapIterator = new CsvMapIterator(openCsvForReading(fileName))

  def openApacheLog(fileName: String): ApacheLogReader = new ApacheLogReader(secureOpenFile(fileName))

  private

  def secureOpenFile(fileName: String): FileReader =
  {
    val file = new JFile(fileName)
    if ( !file.exists )
      throw new FileNotFoundException(fileName);
    return new FileReader(file)
  }

}