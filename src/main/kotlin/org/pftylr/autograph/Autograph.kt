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
import org.pftylr.autograph.Graph

import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.Group
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.scene.canvas.GraphicsContext
import javafx.concurrent.Task
import javafx.application.Platform


import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import java.util.Properties


class Autograph : Application() {


    fun run(args: Array<String>) {
        launch(*args)
    }


    override fun init() {
    }

    override fun stop() {
        System.exit(0)
    }

    override fun start(stage: Stage) {

        try {
            val options = Options()

            options.addProperties(processArgs())
            options.addSystemProperties()
            options.addPropertiesFile("${System.getProperty("user.home")}/.autograph.properties")
            options.addPropertiesResource("org/pftylr/autograph/autograph.properties")

            val width = options.getDoubleValue("width")
            val height = options.getDoubleValue("height")
            val size = options.getIntValue("size")
            val bg_colour = Color.valueOf(options.getStringValue("bg_colour"))
	        val title = options.getStringValue("title")

            val root = Group()
            val scene = Scene(root, width!!, height!!, bg_colour);

            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();

            val dataSource = InputStreamDataSource(System.`in`)

            val graph = Graph(root, dataSource, scene.width, scene.height, size!!, options)

            val changeListener = object : ChangeListener<Number?> {
                public override fun changed(observable: ObservableValue<out Number?>?, oldValue: Number?, newValue: Number?) {
                    graph.resize(scene.width, scene.height)
                }
            }

            scene.widthProperty().addListener(changeListener)
            scene.heightProperty().addListener(changeListener)

            graph.run()

        } catch (e: Exception) {
            System.err.println("exception ${e}")
        }

    }

    private fun processArgs() : Properties {
        val p = Properties()

        val args = getParameters().getRaw()

        var skip : Boolean = false
        args.forEachIndexed { i, v ->

            if (skip) {
                skip = false
            } else {
                when (v) {
                    "--title", "-t" -> {
                        setStringArg(p, "title", v, args, i + 1)
                        skip = true
                    }
                    "--width", "-w" -> {
                        setStringArg(p, "width", v, args, i + 1)
                        skip = true
                    }
                    "--height", "-h" -> {
                        setStringArg(p, "height", v, args, i + 1)
                        skip = true
                    }
                    "--size", "-s" -> {
                        setStringArg(p, "size", v, args, i + 1)
                        skip = true
                    }
                    "--minmax", "-m" -> {
                        setBooleanArg(p, "minmax.enabled", v, args, i + 1)
                        skip = true
                    }

                    else -> fatal("unknown argument: ${v}")
                }
            }

        }

        return p
    }

    private fun setStringArg(p: Properties, name: String, aname: String, args: List<String>, i: Int) {
        p.setProperty(name, getArgValue(aname, args, i))
    }

    private fun setBooleanArg(p: Properties, name: String, aname: String, args: List<String>, i: Int) {
        val v = getArgValue(aname, args, i)
        when (v.toLowerCase()) {
            "true", "t", "yes", "on" -> p.setProperty(name, "true")
            else -> p.setProperty(name, "false")
        }
    }

    private fun getArgValue(aname: String, args: List<String>, i: Int) : String {
        try {
            return args.get(i)
        } catch (e: IndexOutOfBoundsException) {
            fatal("missing value for argument: ${aname}")
            return ""
        }
    }

    private fun fatal(s: String) {
        System.err.println("ERROR: ${s}")
        System.err.println("""
            |usage: autograph
            |
            | --title|-t <TITLE>  - set the window title to <TITLE>.
            | --width|-w <N>      - set the window width to <N>
            | --height|-h <N>     - set the window height to <N>
            | --size|-s <N>       - set the max number of data elements to <N>
            | --minmax|-m <B>     - if <B> is true display min/max lines
            |
            """.trimMargin())
        Platform.exit()
    }


}

