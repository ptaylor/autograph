
package org.pftylr.autograph

import org.pftylr.autograph.*

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UtilsSpec: Spek({

    describe("splitIntoStrings") {
    		
       listOf(
	  //listOf("test 1", "", ArrayList()),
	  listOf("one item", "one", listOf("one")),
	  listOf("three items", "one two three", listOf("one", "two", "three")),
	  listOf("lots of spaces", "  one  two   three ", listOf("one", "two", "three"))
       ).forEach { test ->
        
          it("converts a list with ${test[0]}") {
      	       assertEquals(test[2], splitIntoStrings(test[1] as String))
      	  }
      }

    }

    describe("toNumList") {

       listOf(
	  //listOf("test 1", "", ArrayList()),
	  listOf("one item", listOf("1"), listOf(1.0)),
	  listOf("three items", listOf("1", "2", "3"), listOf(1.0, 2.0, 3.0)),
	  
	  listOf("double", listOf("3.14"), listOf(3.14))
	  //listOf("not a number", listOf("1", "2", "X"), null)
       ).forEach { test ->
        
          it("converts a list with ${test[0]}") {
      	       assertEquals(test[2] as List<Double>, toNumList(test[1] as List<String>))
      	  }
      }


      listOf(
	  listOf("non number first", listOf("X", "2", "3")),
	  listOf("non number last", listOf("1", "2", "X"))
       ).forEach { test ->
        
          it("returns null for a list with ${test[0]}") {
      	       assertNull(toNumList(test[1] as List<String>))
      	  }
      }

    }

})