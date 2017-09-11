/*
 * MIT License
 *
 * Copyright (c) 2017 ptaylor
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package org.pftylr.autograph

import org.pftylr.autograph.InputStreamDataSource
import org.pftylr.autograph.Sampler

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

class InputStreamDataSourceSpek : Spek({

    describe("InputStreamDataSource class") {

        it("can process data separated with spaces") {

            val data : String = """
	    	one two three

		1.0 1.1 1.2
                2.0 2.1 2.2
 

	    	four five    six
                3.0  3.1 3.2 
		""".trimIndent()

	    val s = ByteArrayInputStream(data.toByteArray(StandardCharsets.UTF_8))

	    val ds = InputStreamDataSource(s)

	    var hn = LinkedHashMap<Int, List<String>>()
	    var hv = LinkedHashMap<Int, List<Double>>()

	    var count = 0
	    val sampler = object : Sampler() {
	    	override fun newNames(strs: List<String>) {
		    hn.put(count++, strs)
		}

                override fun newValues(nums: List<Double>) {
	            hv.put(count++, nums)
		}
            }
	    
	    ds.process(sampler)


	    assertEquals(2, hn.size)
	    assertEquals(listOf("one", "two", "three"), hn.get(0))
	    assertEquals(listOf("four", "five", "six"), hn.get(3))

	    assertEquals(3, hv.size)
	    assertEquals(listOf(1.0, 1.1, 1.2), hv.get(1))
	    assertEquals(listOf(2.0, 2.1, 2.2), hv.get(2))
	    assertEquals(listOf(3.0, 3.1, 3.2), hv.get(4))
        }

        it("can process data separated by comas") {

            val data : String = """
	    	1 one, 22 two, 333 three
		1.0, 1.1, 1.2
                2.0, 2.1, 2.2
                3.0  3.1 3.2 
		""".trimIndent()

	    val s = ByteArrayInputStream(data.toByteArray(StandardCharsets.UTF_8))

	    val ds = InputStreamDataSource(s)

	    var hn = LinkedHashMap<Int, List<String>>()
	    var hv = LinkedHashMap<Int, List<Double>>()

	    var count = 0
	    val sampler = object : Sampler() {
	    	override fun newNames(strs: List<String>) {
		    hn.put(count++, strs)
		}

                override fun newValues(nums: List<Double>) {
	            hv.put(count++, nums)
		}
            }
	    
	    ds.process(sampler)

	    assertEquals(1, hn.size)
	    assertEquals(listOf("1 one", "22 two", "333 three"), hn.get(0))

	    assertEquals(3, hv.size)
	    assertEquals(listOf(1.0, 1.1, 1.2), hv.get(1))
	    assertEquals(listOf(2.0, 2.1, 2.2), hv.get(2))
	    assertEquals(listOf(3.0, 3.1, 3.2), hv.get(3))
        }
    }

})