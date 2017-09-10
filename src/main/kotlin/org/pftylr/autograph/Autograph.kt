
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

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue


class Autograph : Application() {

    val title = "Autograph"

    val WIDTH = 800.0
    val HEIGHT = 400.0
    val SIZE = 100
    val BG_COLOUR = Color.BLACK

    fun go(args: Array<String>) {
    // TODO pass args
        launch()
     }

    override fun init() {
    }

    override fun stop() {
        println("*** STOP ***")
	System.exit(0)
    }

    override fun start(stage: Stage) {

	val root = Group()

        val scene = Scene(root, WIDTH, HEIGHT, BG_COLOUR);

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();

        val dataSource = InputStreamDataSource(System.`in`)

	val graph = Graph(root, dataSource, scene.width, scene.height, SIZE)

	val changeListener = object : ChangeListener<Number?> {
            public override fun changed(observable: ObservableValue<out Number?>?, oldValue: Number?, newValue: Number?) {
		 graph.resize(scene.width, scene.height)
            }
        }

	scene.widthProperty().addListener(changeListener)
	scene.heightProperty().addListener(changeListener)

	graph.run()

    }

}

