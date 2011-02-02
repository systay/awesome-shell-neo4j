package org.neo4j.app.awesome_shell;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class MapFilterTest
{
	@Test
	public void emptyMapReturnsEmpty()
	{
		Map<String, String> map = createMap();
		Map<String, String> result = MapFilter.filter( map, "key" );

		assertThat( result.keySet().size(), is( 0 ) );
	}

	private Map<String, String> createMap( String... keys )
	{
		Map<String, String> map = new HashMap<String, String>();
		for ( String key : keys )
		{
			map.put( key, key + "VAL" );
		}
		return map;
	}

	@Test
	public void singleKeyMapIsReturnedUnchanged()
	{
		Map<String, String> map = createMap( "key" );
		Map<String, String> result = MapFilter.filter( map, "key" );


		assertThat( result.get( "key" ), is( "keyVAL" ) );
	}

	@Test
	public void twoColumnsFilteredDownToOne()
	{
		Map<String, String> map = createMap( "key1", "key2" );
		Map<String, String> result = MapFilter.filter( map, "key2" );

		assertThat( result.keySet().size(), is( 1 ) );
		assertThat( result.get( "key2" ), is( "key2VAL" ) );
	}
}
