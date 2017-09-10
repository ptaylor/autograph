
package org.pftylr.autograph

import org.pftylr.autograph.InputStreamDataSource
import org.pftylr.autograph.Graph
import org.pftylr.autograph.Test

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

     val WIDTH : Double = 800.0
     val HEIGHT : Double = 400.0
     val BG_COLOUR = Color.BLACK
     val FG_COLOUR = Color.WHITE

     fun go() {
        launch()
     }

    init {

	println("**** __INIT__ ${this}")
    }

    override fun init() {
        println("*** INIT ***")
    }

    override fun stop() {
        println("*** STOP ***")
    }

    fun change(listener: ChangeListener<Double>, x: Double, y: Double) {
      println("CHANGE")
    }

    override fun start(stage: Stage) {

	val root = Group()

        val scene = Scene(root, WIDTH, HEIGHT, BG_COLOUR);

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();

        val dataSource = InputStreamDataSource(System.`in`)

	Graph(root, dataSource, 600.0, 300.0, 100).run()

/*
	val changeListener = object : ChangeListener<Number?> {
            public override fun changed(observable: ObservableValue<out Number?>?, oldValue: Number?, newValue: Number?) {
                 println("old ${oldValue} new ${newValue}")
		 //println("canvas ${canvas.getHeight()} ${canvas.getWidth()}")
            }
        }


	stage.widthProperty().addListener(changeListener)
	stage.heightProperty().addListener(changeListener)
*/
    }

}

