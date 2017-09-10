package org.pftylr.autograph

import javafx.scene.canvas.Canvas
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue

class ResizableCanvas(width: Double, height: Double) : Canvas(width, height) {

    /*
    init {

	val changeListener = object : ChangeListener<Number?> {
            public override fun changed(observable: ObservableValue<out Number?>?, oldValue: Number?, newValue: Number?) {
                 println("CHANGE old ${oldValue} new ${newValue}")
            }
        }

	widthProperty().addListener(changeListener)
	heightProperty().addListener(changeListener)

    } 
    */     

    override fun isResizable(): Boolean  {
        return true
    }


}