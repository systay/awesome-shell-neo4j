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