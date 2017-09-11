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

            val history = History<Int>(3)

            assertEquals(0, history.size)

            history.put(1, 1)
            assertEquals(1, history.size)
            assertEquals(1, history.get(1))

            history.put(2, 2)
            assertEquals(2, history.size)
            assertEquals(1, history.get(1))
            assertEquals(2, history.get(2))

            history.put(3, 3)
            assertEquals(3, history.size)
            assertEquals(1, history.get(1))
            assertEquals(2, history.get(2))
            assertEquals(3, history.get(3))

            history.put(4, 4)
            assertEquals(3, history.size)
            assertNull(history.get(1))
            assertEquals(2, history.get(2))
            assertEquals(3, history.get(3))
            assertEquals(4, history.get(4))

            history.put(5, 5)
            assertEquals(3, history.size)
            assertNull(history.get(1))
            assertNull(history.get(2))
            assertEquals(3, history.get(3))
            assertEquals(4, history.get(4))
            assertEquals(5, history.get(5))

        }

    }

})