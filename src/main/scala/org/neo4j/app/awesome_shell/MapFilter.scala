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