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

import org.pftylr.autograph.Options

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

import java.io.File

class OptionsSpec : Spek({

    describe("Options class") {

        it("get*Value can get typed values") {

            val options = Options()
            options.addPropertiesResource("org/pftylr/autograph/test1.properties")

            assertEquals(11, options.getIntValue("p1"))
            assertEquals(2.2, options.getDoubleValue("p2"))
            assertEquals("three", options.getStringValue("p3"))
            assertEquals(null, options.getIntValue("p4"))
        }


        it("get*Value returns the first value found") {

            val options = Options()
            System.setProperty("p5", "five")
            options.addSystemProperties()
            val file = Options::class.java.getClassLoader().getResource("org/pftylr/autograph/test3.properties").toURI().getPath()
            options.addPropertiesFile(file)
            options.addPropertiesResource("org/pftylr/autograph/test2.properties")
            options.addPropertiesResource("org/pftylr/autograph/test1.properties")


            assertEquals(11, options.getIntValue("p1"))
            assertEquals(3.3, options.getDoubleValue("p2"))
            assertEquals("THREE", options.getStringValue("p3"))
            assertEquals(44, options.getIntValue("p4"))
            assertEquals("five", options.getStringValue("p5"))
            assertEquals(666, options.getIntValue("p6"))
        }

        it("getListValue returns a list of strings") {

            val options = Options()
            options.addPropertiesResource("org/pftylr/autograph/test1.properties")

            assertEquals(listOf("one", "two", "three"), options.getListValue("list1"))
            assertEquals(listOf(), options.getListValue("list2"))
        }
    }

})