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

import org.pftylr.autograph.History

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

import java.io.File

class HistorySpek : Spek({

    describe("History class") {

        it("removes values once full") {

            val history = History(3)

            assertEquals(0, history.size)

            history.put(1, listOf<Double>(1.0, 1.1))
            assertEquals(1, history.size)
            assertEquals(listOf<Double>(1.0, 1.1), history.get(1))

            history.put(2, listOf<Double>(2.0, 2.1))
            assertEquals(2, history.size)
            assertEquals(listOf<Double>(1.0, 1.1), history.get(1))
            assertEquals(listOf<Double>(2.0, 2.1), history.get(2))

            history.put(3, listOf<Double>(3.0, 3.1))
            assertEquals(3, history.size)
            assertEquals(listOf<Double>(1.0, 1.1), history.get(1))
            assertEquals(listOf<Double>(2.0, 2.1), history.get(2))
            assertEquals(listOf<Double>(3.0, 3.1), history.get(3))

            history.put(4, listOf<Double>(4.0, 4.1))
            assertEquals(3, history.size)
            assertNull(history.get(1))
            assertEquals(listOf<Double>(2.0, 2.1), history.get(2))
            assertEquals(listOf<Double>(3.0, 3.1), history.get(3))
            assertEquals(listOf<Double>(4.0, 4.1), history.get(4))

            history.put(5, listOf<Double>(5.0, 5.1))
            assertEquals(3, history.size)
            assertNull(history.get(1))
            assertNull(history.get(2))
            assertEquals(listOf<Double>(3.0, 3.1), history.get(3))
            assertEquals(listOf<Double>(4.0, 4.1), history.get(4))
            assertEquals(listOf<Double>(5.0, 5.1), history.get(5))

        }

    }

})