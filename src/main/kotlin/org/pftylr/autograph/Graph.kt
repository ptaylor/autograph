package org.pftylr.autograph

import org.pftylr.autograph.Sampler
import org.pftylr.autograph.History

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.Group
import javafx.scene.text.TextAlignment
import javafx.scene.text.Font
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.geometry.VPos
import javafx.scene.canvas.GraphicsContext 
import javafx.concurrent.Task
import javafx.application.Platform
import kotlin.concurrent.thread

class Graph(val group: Group, val dataSource: InputStreamDataSource, var width: Double, var height: Double, val size: Int) : Sampler() {

    var count =  0
    var history : History
    var names = listOf<String>()
    var numValues = -1

    var minValue: Double = Double.MAX_VALUE
    var maxValue: Double = Double.MIN_VALUE
    var graphMaxValue: Double = Double.MIN_VALUE
    var graphMinValue: Double = Double.MAX_VALUE

    var borderTop: Double = 40.0
    var borderBottom: Double = 40.0
    var borderLeft: Double = 60.0
    var borderRight: Double = 60.0
    
    val canvas: Canvas
    val gc: GraphicsContext 


    // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/paint/Color.html
    val LINE_COLOURS = listOf(
    		       Color.ORANGE, 
		       Color.BLUE, 
                       Color.PINK, 
                       Color.GREEN,
                       Color.RED, 
                       Color.BROWN,
    		       Color.CYAN, 
                       Color.GRAY, 
                       Color.DARKRED,
		       Color.VIOLET, 
                       Color.GOLD, 
		       Color.IVORY
    )

    val LINE_WIDTH = 1.0
    val TEXT_WIDTH = 1.0
    val TEXT_COLOUR = Color.CHARTREUSE
    val AXIS_FONT = Font.font("Monospaced", 08.0)
    val LEGEND_FONT = Font.font("Monospaced", 12.0)

    val LEGEND_LINE_WIDTH = 2.0
    val LEGEND_TEXT_WIDTH = 1.0
    val LEGEND_LINE_LENGTH = 10.0
    val LEGEND_BORDER_LEFT = 20.0
    val BORDER_COLOUR = Color.CHARTREUSE
    val BORDER_WIDTH = 0.5
    val MIN_MAX_COLOUR = Color.YELLOW
    val MIN_MAX_WIDTH = 0.5
    val MIN_MAX_DASH_SIZE = 5.0
    val TICK_LENGTH = 4.0

    init {
        history = History(size + 1)

        canvas = Canvas(width, height)
        gc = canvas.getGraphicsContext2D()

	group.getChildren().add(canvas)

	resize()
    }

    fun run() {
    	
	val sampler = this
	thread(name = "sampler") {
	   dataSource.process(sampler)
	   println("*** PROCESS THREAD FINISHED")
	}
    }

    override fun newNames(strs: List<String>) {
        checkSize(strs.size)
	names = strs
    }

    override fun newValues(nums: List<Double>) {

        checkSize(nums.size)

	calculateMinMax(nums)
	resize()
        history.put(count++, nums)
	if (count > 1) {
  	    Platform.runLater {
	        draw()
            }
	}
    }

    private fun checkSize(s: Int) {
        if (numValues < 0) {
            numValues = s
        }

        if (s != numValues) {
	    fatal("number of data values (${s}) is not as expected (${numValues})")
	}
	
	if (s > LINE_COLOURS.size) {
	    fatal("too many data values (${s}) for number of available colours (${LINE_COLOURS.size})")
	}

    }

    private fun fatal(s: String) {
        System.err.println("ERROR: ${s}")
	Platform.exit()
    }


    private fun calculateMinMax(nums: List<Double>) {
        nums.forEach { n -> 
	   if (n < minValue) {
	       minValue = n
	   }
	   if (n > maxValue) {
	      maxValue = n
	   }
        }

	// TODO: make thibs more intelligent
	graphMaxValue = maxValue + 10.0
	graphMinValue = minValue - 10.0

	println("MIN/MAX ${graphMinValue} ${minValue} ${maxValue} ${graphMaxValue}")
    }

    private fun draw() {
        clearGraph()
	drawBackground()
	drawGraph()
    }

    private fun drawGraph() {

        gc.setLineWidth(LINE_WIDTH);

	var x = history.size - 1
    	var previous : List<Double>? = null
	for (nums in history.values) {
	    if (previous != null && nums != null) {
	        for (j in 0 .. nums.size - 1) {
	      	    val color = LINE_COLOURS[j]
                    gc.setStroke(color);

	            val v2 = previous[j]
		    val v1 = nums[j]
		    //println("x ${x}, v1 ${v1}, ${v2} = (${scaley(v1)}, ${scaley(v2)})")
		    plot(scalex(x.toDouble()), scaley(v1), scalex((x + 1).toDouble()), scaley(v2))
	        } 
	    }

            x = x - 1;
	    previous = nums
        }

      
    }
    private fun xdrawGraph() {

        gc.setLineWidth(LINE_WIDTH);

	var x : Int = -2
    	var previous : List<Double>? = null
	println("HISTORY ${history}");
	for (i in  history.size downTo 0) {
 	   val nums = history.get(i)
	   if (previous != null) {
	       if (previous != null && nums != null) {
	           for (j in 0 .. nums.size - 1) {
	      	       val color = LINE_COLOURS[j]
                       gc.setStroke(color);

	               val v2 = previous[j]
		       val v1 = nums[j]
		       println("x ${x}, v1 ${v1}, ${v2} = (${scaley(v1)}, ${scaley(v2)})")
		       plot(scalex((x + 1).toDouble()), scaley(v1), scalex(x.toDouble()), scaley(v2))
	           } 
	       }
	   }

           x = x + 1;
	   previous = nums
        }

      
    }

    private fun drawBackground() {

    	drawBorders()
	drawXAxis()
	drawYAxis()
	drawMinMaxLines()	
	drawLegend()

    }

    private fun drawBorders() {
        gc.setStroke(BORDER_COLOUR)
	gc.setLineWidth(BORDER_WIDTH)
	line(borderLeft, borderTop, width - borderRight, borderTop)
	line(borderLeft, height - borderBottom, width - borderRight, height - borderBottom)
	line(borderLeft, borderTop, borderLeft, height - borderBottom)
	line(width - borderRight, borderTop, width - borderRight, height - borderBottom)
    }

    private fun drawYAxis() {


	// Verticle ticks (left side)
        gc.setTextBaseline(VPos.CENTER)
	gc.setTextAlign(TextAlignment.RIGHT)
	gc.setFont(AXIS_FONT)
	val s : Double = (graphMaxValue - graphMinValue) / 10.0
	var i : Double = graphMinValue
	while (i <= graphMaxValue) {
            val y = scaley(i.toDouble())

	    gc.setLineWidth(BORDER_WIDTH);
            gc.setStroke(BORDER_COLOUR)
	    line(borderLeft, y, borderLeft - TICK_LENGTH, y)

	    gc.setLineWidth(TEXT_WIDTH);
	    gc.setStroke(TEXT_COLOUR)
	    text("%.2f".format(i), borderLeft - (TICK_LENGTH + 10), y)

	    i = i + s
	}

    }

    private fun drawXAxis() {

	for (i in 0 .. size step size / 10) {
	    val x = scalex(i.toDouble())

	    gc.setLineWidth(BORDER_WIDTH);
	    line(x, height - borderBottom, x, height - borderBottom + TICK_LENGTH)
        }
    }

    private fun drawMinMaxLines() {
	gc.setStroke(MIN_MAX_COLOUR)
	gc.setLineWidth(MIN_MAX_WIDTH);
	dashedLine(borderLeft, scaley(minValue), width - borderRight, scaley(minValue), MIN_MAX_DASH_SIZE)
	dashedLine(borderLeft, scaley(maxValue), width - borderRight, scaley(maxValue), MIN_MAX_DASH_SIZE)
    }

    private fun drawLegend() {

        gc.setTextBaseline(VPos.CENTER)
        var i = 0
	val step = (graphMaxValue - graphMinValue) / (names.size + 2)
        for (name in names) {
 	    val colour = LINE_COLOURS[i]
	    gc.setStroke(colour)
	    val x = width - borderRight + LEGEND_BORDER_LEFT
	    val y = scaley((names.size - (i - 1) ) * step)
println("name ${name} ${i} x ${x} y ${y}")

            gc.setLineWidth(LEGEND_LINE_WIDTH)
	    gc.setTextAlign(TextAlignment.LEFT)
	    gc.setFont(LEGEND_FONT)
	    line(x, y, x + LEGEND_LINE_LENGTH, y)

            gc.setLineWidth(LEGEND_TEXT_WIDTH)
	    gc.setFont(LEGEND_FONT)
	    gc.setStroke(TEXT_COLOUR)
	    text(name, x + LEGEND_LINE_LENGTH * 2, y)
	    i = i + 1
        }
    
    }

    private fun plot(x1: Double, v1: Double, x2: Double, v2: Double) {
        //println("Plot ${x1},${v1} ${x2},${v2}")
        line(x1 , v1 , x2, v2)
    }

    private fun dashedLine(x1: Double, y1: Double, x2: Double, y2: Double, dashSize: Double) {
    	var x = x1
	val max = x2 - dashSize * 2
	while (x <= max) {
            line(x, y1, x + dashSize, y2)
	    x += dashSize * 2.0
        }
    }

    private fun line(x1: Double, y1: Double, x2: Double, y2: Double) {
        //println("LINE (${x1}, $y1}) - (${x2}, ${y2})")
	gc.strokeLine(x1, y1, x2, y2)
    }

    private fun text(s: String, x: Double, y: Double) {
        gc.strokeText(s, x, y)
    }

    fun clearGraph() {
        gc.setFill(Color.WHITE)
	gc.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight())
    }
 


    fun resize() {
        println("RESIZE ${canvas.width} ${canvas.height}")

	width = canvas.width
	height = canvas.height
	
    }

    private fun scalex(v: Double) : Double {

        val cw = width - (borderLeft + borderRight)
	val x = cw - (v / size) * cw + borderLeft
    	//println("scalex ${v} -> ${x} (cw ${cw})")
	return x
    }

    private fun scaley(v: Double) : Double {

        val h = graphMaxValue - graphMinValue
    	val ch = height - (borderTop + borderBottom)
	val y = height - ((v - graphMinValue) / h) * ch - borderBottom
    	//println("scaley ${v} -> ${y} (h ${h} ch ${ch}) ${v - graphMinValue}")

   	return y
    }

}