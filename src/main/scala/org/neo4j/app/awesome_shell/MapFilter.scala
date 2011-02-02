package org.neo4j.app.awesome_shell

import java.util.{HashMap => JHashMap, Map => JMap}

object MapFilter
{

  def filter[ V ](map: JMap[ String, V ], keys: Array[ String ]): JMap[ String, V ] =
  {
    val result = new JHashMap[ String, V ]
    keys.foreach((x) =>
      {
        val resultValue = map.get(x)
        if ( resultValue != null )
          result.put(x, resultValue)
      })
    result
  }

  def filter[ V ](map: JMap[ String, V ]): JMap[ String, V ] =
    new JHashMap[ String, V ]

  def filter[ V ](map: JMap[ String, V ], key: String): JMap[ String, V ] =
    filter(map, Array(key))

  def filter[ V ](map: JMap[ String, V ], key1: String, key2: String): JMap[ String, V ] =
    filter(map, Array(key1, key2))

  def filter[ V ](map: JMap[ String, V ], key1: String, key2: String, key3: String): JMap[ String, V ] =
    filter(map, Array(key1, key2, key3))

  def filter[ V ](map: JMap[ String, V ], key1: String, key2: String, key3: String, key4: String): JMap[ String, V ] =
    filter(map, Array(key1, key2, key3, key4))

  def filter[ V ](map: JMap[ String, V ], key1: String, key2: String, key3: String, key4: String, key5: String): JMap[ String, V ] =
    filter(map, Array(key1, key2, key3, key4, key5))

  def filter[ V ](map: JMap[ String, V ], key1: String, key2: String, key3: String, key4: String, key5: String, key6: String): JMap[ String, V ] =
    filter(map, Array(key1, key2, key3, key4, key5, key6))

  def filter[ V ](map: JMap[ String, V ], key1: String, key2: String, key3: String, key4: String, key5: String, key6: String, key7: String): JMap[ String, V ] =
    filter(map, Array(key1, key2, key3, key4, key5, key6, key7))

  def filter[ V ](map: JMap[ String, V ], key1: String, key2: String, key3: String, key4: String, key5: String, key6: String, key7: String, key8: String): JMap[ String, V ] =
    filter(map, Array(key1, key2, key3, key4, key5, key6, key7, key8))

  def filter[ V ](map: JMap[ String, V ], key1: String, key2: String, key3: String, key4: String, key5: String, key6: String, key7: String, key8: String, key9: String): JMap[ String, V ] =
    filter(map, Array(key1, key2, key3, key4, key5, key6, key7, key8, key9))
}