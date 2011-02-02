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
