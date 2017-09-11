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

import java.util.Properties
import java.io.InputStream
import java.io.File
import java.io.FileInputStream
import javafx.scene.paint.Color

open class Options {

    var properties = listOf<Properties>()


    fun addPropertiesInputStream(i : InputStream) {
        val p = Properties()
        p.load(i)
        properties += p
    }

    fun addPropertiesFile(name: String) {
        //println("addPropertiesFile ${name}")
        val file = File(name)
        if (file.exists()) {
            //println("addPropertiesFile adding")
            addPropertiesInputStream(FileInputStream(file))
        } else {
            //println("addPropertiesFile ${name} does not exist")
        }
    }

    fun addPropertiesResource(name: String) {
        //println("addPropertiesResource ${name}")
        val i = Options::class.java.getClassLoader().getResourceAsStream(name)
        if (i != null) {
            //println("addPropertiesResource adding")
            addPropertiesInputStream(i)
        } else {
            println("addPropertiesResource ${name} does not exist")
        }
    }

    fun addSystemProperties() {
        //println("addPropertiesResource")
        properties += System.getProperties()
    }

    fun getStringValue(name: String) : String? {
        //println("getStringValue ${name}")
        for (p in properties) {
            var s = p.getProperty(name)
            if (s != null) {
                //println("-> ${s}")
                return s
            }
        }
        return null
    }

    fun getIntValue(name: String): Int? {

        //println("getIntValue ${name}")
        val s = getStringValue(name)

        if (s != null) {
            return s.toInt()
        }

        return null
    }

    fun getDoubleValue(name: String): Double? {

        //println("getDoubleValue ${name}")
        val s = getStringValue(name)

        if (s != null) {
            return s.toDouble()
        }

        return null
    }

    fun getListValue(name: String): List<String>? {
        var l = listOf<String>()

        var i = 0
        var s : String?
        do {
            s = getStringValue("${name}.${i}")
            if (s != null) {
                l += s
                i++
            }
        } while (s != null)

        return l
    }

}
