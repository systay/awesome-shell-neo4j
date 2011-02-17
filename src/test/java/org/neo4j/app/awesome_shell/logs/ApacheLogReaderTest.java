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
package org.neo4j.app.awesome_shell.logs;

import org.junit.Ignore;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ApacheLogReaderTest
{
    public static final String Failing_Log_Line = "210.55.215.150 - - [23/Dec/2010:11:57:19 -0600] \"GET /?id=61a251f1cae21126+v=6704+source=maven+p=1 HTTP/1.1\" 304 - \"-\" \"Mozilla/4.0 (compatible;)\"";
    public static final String Failing_Log_Line2 = "94.46.240.121 - - [31/Dec/2010:06:15:39 -0600] \"GET / HTTP/1.0\" 200 164 \"-\" \"Pingdom.com_bot_version_1.4_(http://www.pingdom.com/)\"";
	public static final String         Log_Line = "213.50.11.35 - - [31/Oct/2010:07:10:47 -0500] \"GET /?id=f19c4af51914a3ab+v=6496+source=maven+p=1 HTTP/1.1\" 200 164 \"-\" \"Java/1.6.0_20\"";
	public static final String Log_Lines =
			"213.50.11.35 - - [31/Oct/2010:07:10:47 -0500] \"GET /?id=f19c4af51914a3ab+v=6496+source=maven+p=1 HTTP/1.1\" 200 164 \"-\" \"Java/1.6.0_20\"\n"+
			"213.50.11.35 - - [31/Oct/2010:07:10:50 -0500] \"GET /?id=f19c4af51914a3ab+v=6496+source=maven+p=2 HTTP/1.1\" 200 164 \"-\" \"Java/1.6.0_20\"\n"+
			"213.50.11.35 - - [31/Oct/2010:07:10:53 -0500] \"GET /?id=f19c4af51914a3ab+v=6496+source=maven+p=3 HTTP/1.1\" 200 164 \"-\" \"Java/1.6.0_20\"\n";



	@Test
	public void canParseIpAddress()
	{

		StringReader stringReader = new StringReader( Log_Line );
		ApacheLogReader reader = new ApacheLogReader( stringReader );

		List<Map<String, Object>> result = asList( reader );

		assertThat( (String) result.get( 0 ).get( "IP" ), is( "213.50.11.35" ) );
	}

	@Test
	public void canParseDate()
	{
		StringReader stringReader = new StringReader( Log_Line );
		ApacheLogReader reader = new ApacheLogReader( stringReader );

		List<Map<String, Object>> result = asList( reader );

		Calendar cal = new GregorianCalendar();
		Date d = new Date( (Long)result.get( 0 ).get( "Date" ) );
		cal.setTime( d );

		System.out.print( cal );

		assertThat( cal.get( Calendar.YEAR ), is( 2010 ) );
		assertThat( cal.get( Calendar.MONTH ), is( 9 ) ); //Zero based months FTW!
		assertThat( cal.get( Calendar.DAY_OF_MONTH ), is( 31 ) );
	}

	@Test
	public void canParseRequest()
	{

		StringReader stringReader = new StringReader( Log_Line );
		ApacheLogReader reader = new ApacheLogReader( stringReader );

		List<Map<String, Object>> result = asList( reader );

		Map<String,String> parameters = (Map<String, String>) result.get( 0 ).get( "Parameters" );
		assertThat(parameters.get("v"), is("6496"));
	}

	@Test
	public void canGetRequest()
	{

		StringReader stringReader = new StringReader( Log_Line );
		ApacheLogReader reader = new ApacheLogReader( stringReader );

		List<Map<String, Object>> result = asList( reader );

		assertThat( (String) result.get( 0 ).get( "Req" ), is( "id=f19c4af51914a3ab+v=6496+source=maven+p=1" ) );
	}


	@Test( expected = BadLogEntryException.class )
	public void throwsOnWeirdLogLines()
	{
		StringReader stringReader = new StringReader( "THIS LOG LINE SHOULD NOT PARSE!" );
		ApacheLogReader reader = new ApacheLogReader( stringReader );

		List<Map<String, Object>> result = asList( reader );
	}

	@Test
	public void canReadMultipleLines()
	{
		StringReader stringReader = new StringReader( Log_Lines );
		ApacheLogReader reader = new ApacheLogReader( stringReader );

		List<Map<String, Object>> result = asList( reader );

		assertThat( result.size(), is(3) );
	}

    @Test
    public void failingLineShouldWork() throws Exception
    {
        StringReader stringReader = new StringReader( Failing_Log_Line );
        ApacheLogReader reader = new ApacheLogReader( stringReader );

        List<Map<String, Object>> result = asList( reader );
    }

    @Test
    @Ignore
    public void failingLine2ShouldWork() throws Exception
    {
        StringReader stringReader = new StringReader( Failing_Log_Line2 );
        ApacheLogReader reader = new ApacheLogReader( stringReader );

        List<Map<String, Object>> result = asList( reader );
    }

    public static <T> List<T> asList( Iterable<T> iterable )
	{
		List<T> list = new ArrayList<T>();
		for ( T anIterable : iterable )
		{
			list.add( anIterable );
		}
		return list;
	}
}
