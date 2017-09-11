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
        println("addPropertiesFile ${name}")
        val file = File(name)
        if (file.exists()) {
            println("addPropertiesFile adding")
            addPropertiesInputStream(FileInputStream(file))
        } else {
            println("addPropertiesFile ${name} does not exist")
        }
    }

    fun addPropertiesResource(name: String) {
        println("addPropertiesResource ${name}")
        val i = Options::class.java.getClassLoader().getResourceAsStream(name)
        if (i != null) {
            println("addPropertiesResource adding")
            addPropertiesInputStream(i)
        } else {
            println("addPropertiesResource ${name} does not exist")
        }
    }

    fun addSystemProperties() {
        println("addPropertiesResource")
        properties += System.getProperties()
    }

    fun getStringValue(name: String) : String? {
        println("getStringValue ${name}")
        for (p in properties) {
            var s = p.getProperty(name)
            if (s != null) {
                return s
            }
        }
        return null
    }

    fun getIntValue(name: String): Int? {

        println("getIntValue ${name}")
        val s = getStringValue(name)

        if (s != null) {
            return s.toInt()
        }

        return null
    }

    fun getDoubleValue(name: String): Double? {

        println("getDoubleValue ${name}")
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


class Funky (val options: Options ){


    val foo : Int? by lazy {options.getIntValue("foo")}
    val bar : Double? by lazy {options.getDoubleValue("bar")}
    val baz : String? by lazy {options.getStringValue("baz")}
    val colours : List<Color>? by lazy {options.getListValue("colours")?.map { Color.valueOf(it) }}

}
    fun main(args: Array<String>) {
        println("version 2")
        val options = Options()
        val funky = Funky(options)
        options.addSystemProperties()
        options.addPropertiesFile("/tmp/options.properties")
        options.addPropertiesResource("org/pftylr/autograph/autograph.properties")

        println("")
        println("foo ${funky.foo}")
        println("bar ${funky.bar}")
        println("baz ${funky.baz}")
        println("colours ${funky.colours}")

        println("")
        println("foo ${funky.foo}")
        println("bar ${funky.bar}")
        println("baz ${funky.baz}")
        println("colours ${funky.colours}")

    }



