package org.neo4j.app.awesome_shell.logs;

import org.junit.Test;

import java.io.StringReader;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ApacheLogReaderTest
{
    public static final String Failing_Log_Line = "210.55.215.150 - - [23/Dec/2010:11:57:19 -0600] \"GET /?id=61a251f1cae21126+v=6704+source=maven+p=1 HTTP/1.1\" 304 - \"-\" \"Mozilla/4.0 (compatible;)\"";
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
