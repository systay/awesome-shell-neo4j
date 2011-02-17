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
package org.neo4j.app.awesome_shell.csv;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CSVMapIteratorTest
{
    public ArrayList<String[]> input;

    @Before
    public void setUp() throws Exception
    {
        input = new ArrayList<String[]>();
    }

    @Test
    public void singleColumnSingleRowIsHandledCorrectly()
    {
        addRow("Column");
        addRow("Value");

        CsvMapIterator mapIterator = createCsvMapIterator();
        Map<String, Object> map = mapIterator.next();

        assertTrue( "Column name was not picked up correctly", map.keySet().contains( "Column" ) );
        assertThat( (String)map.get( "Column" ), is( "Value" ) );
    }

    @Test
    public void singleColumnmultipleRowsCanBeRead()
    {
        addRow("Column");
        addRow("value1");
        addRow("value2");

        CsvMapIterator mapIterator = createCsvMapIterator();
    }

    private CsvMapIterator createCsvMapIterator()
    {
        return new CsvMapIterator( input );
    }


    private void addRow(String... values)
    {
        input.add( values );
    }
}
