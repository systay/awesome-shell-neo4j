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