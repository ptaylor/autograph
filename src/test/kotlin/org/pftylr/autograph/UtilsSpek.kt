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

import org.pftylr.autograph.*

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UtilsSpec : Spek({

     describe("splitIntoStrings") {

        listOf(
                //listOf("test 1", "", ArrayList()),
                listOf("one item", "one", listOf("one")),
                listOf("three items", "one two three", listOf("one", "two", "three")),
                listOf("lots of spaces", "  one  two   three ", listOf("one", "two", "three")),
                listOf("data with commas", "  one,  two ,  three ", listOf("one", "two", "three"))
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

    describe("time2text") {

        listOf(
                listOf(0L, "00:00:00"),
                listOf(500L, "00:00:00"),
                listOf(1000L, "00:00:01"),
                listOf(60000L, "00:01:00"),
                listOf(3600000L, "01:00:00"),
                listOf(82800000L, "23:00:00"),
                listOf(20601000L, "05:43:21")
        ).forEach { test ->

            it("converts ${test[0]} to ${test[1]}") {
                assertEquals(test[1], time2text(test[0] as Long))
            }
        }

    }


})