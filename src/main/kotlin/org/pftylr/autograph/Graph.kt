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

import org.pftylr.autograph.Sampler
import org.pftylr.autograph.History
import org.pftylr.autograph.ResizableCanvas
import org.pftylr.autograph.Options

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
import java.lang.Math

class Graph(val group: Group, val dataSource: InputStreamDataSource, var width: Double, var height: Double, val size: Int, val options: Options) : Sampler() {

    var count = 0
    var dataHistory: History<List<Double>>
    var timeHistory: History<Long>
    var currentDuration: Long = 0
    var names = listOf<String>()
    var numValues = -1

    var minValue: Double = Double.MAX_VALUE
    var maxValue: Double = Double.MIN_VALUE
    var graphMaxValue: Double = Double.MIN_VALUE
    var graphMinValue: Double = Double.MAX_VALUE

    val canvas: Canvas
    val gc: GraphicsContext


    val BORDER_TOP             : Double by lazy {options.getDoubleValue("border.top")!!}
    val BORDER_BOTTOM          : Double by lazy {options.getDoubleValue("border.bottom")!!}
    val BORDER_LEFT            : Double by lazy {options.getDoubleValue("border.left")!!}
    val BORDER_RIGHT           : Double by lazy {options.getDoubleValue("border.right")!!}

    val BORDER_COLOUR          : Color by lazy {getColorValue("border.colour")}
    val BORDER_WIDTH           : Double by lazy {options.getDoubleValue("border.width")!!}

    val DATA_LINE_WIDTH        : Double by lazy {options.getDoubleValue("data.line.width")!!}

    val DATA_LINE_COLOURS      : List<Color> by lazy {options.getListValue("data.line.colour")?.map { Color.valueOf(it) }!!}

    val XAXIS_FONT             : Font by lazy {getFontValue("yaxis.font")}
    val YAXIS_FONT             : Font by lazy {getFontValue("xaxis.font")}
    val AXIS_TICK_LENGTH       : Double by lazy {options.getDoubleValue("axis.tick.length")!!}
    val AXIS_TEXT_WIDTH        : Double by lazy {options.getDoubleValue("axis.text.width")!!}
    val AXIS_TEXT_COLOUR       : Color by lazy {getColorValue("axis.text.colour")}

    val LEGEND_LINE_WIDTH      : Double by lazy {options.getDoubleValue("legend.line.width")!!}
    val LEGEND_LINE_LENGTH     : Double by lazy {options.getDoubleValue("legend.line.length")!!}
    val LEGEND_TEXT_WIDTH      : Double by lazy {options.getDoubleValue("legend.text.width")!!}
    val LEGEND_TEXT_COLOUR     : Color by lazy {getColorValue("legend.text.colour")}
    val LEGEND_BORDER_LEFT     : Double by lazy {options.getDoubleValue("legend.border.left")!!}
    val LEGEND_FONT            : Font by lazy {getFontValue("legend.font")}

    val MIN_MAX_ENABLED        : Boolean by lazy {options.getBooleanValue("minmax.enabled")}
    val MIN_MAX_COLOUR         : Color by lazy {getColorValue("minmax.line.colour")}
    val MIN_MAX_WIDTH          : Double by lazy {options.getDoubleValue("minmax.line.width")!!}
    val MIN_MAX_DASH_SIZE      : Double by lazy {options.getDoubleValue("minmax.line.dash.length")!!}


    init {
        dataHistory = History<List<Double>>(size + 1)
	    timeHistory = History<Long>(size + 1)

        canvas = ResizableCanvas(width, height)
        gc = canvas.getGraphicsContext2D()

        group.getChildren().add(canvas)

        resize(width, height)
    }

    fun resize(width: Double, height: Double) {
        canvas.setWidth(width)
        canvas.setHeight(height)

        calculateSizes()
        Platform.runLater {
            draw()
        }
    }

    fun run() {

        val sampler = this
        thread(name = "sampler") {
            dataSource.process(sampler)
        }
    }

    override fun newNames(strs: List<String>) {
        checkSize(strs.size)
        names = strs
    }

    override fun newValues(nums: List<Double>) {

        checkSize(nums.size)

        calculateMinMax(nums)
        calculateSizes()
        dataHistory.put(count, nums)
        timeHistory.put(count, System.currentTimeMillis())

        val minIndex = Math.max(0, count - size)
        currentDuration = timeHistory.get(count)!! - timeHistory.get(minIndex)!!

        count++
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

        if (s > DATA_LINE_COLOURS.size) {
            fatal("too many data values (${s}) for number of available colours (${DATA_LINE_COLOURS.size})")
        }

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
        val diff = Math.abs(maxValue - minValue) * 0.10
        graphMaxValue = maxValue + diff
        graphMinValue = minValue - diff

    }

    private fun draw() {
        clearGraph()
        drawBackground()
        drawGraph()
    }

    private fun drawGraph() {

        gc.setLineWidth(DATA_LINE_WIDTH);

        var x = dataHistory.size - 1
        var previous: List<Double>? = null
        for (nums in dataHistory.values) {
            if (previous != null) {
                for (j in 0..nums.size - 1) {
                    val color = DATA_LINE_COLOURS[j]
                    gc.setStroke(color);

                    val v2 = previous[j]
                    val v1 = nums[j]
                    plot(scalex(x.toDouble()), scaley(v1), scalex((x + 1).toDouble()), scaley(v2))
                }
            }

            x = x - 1;
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

        line(BORDER_LEFT, BORDER_TOP, width - BORDER_RIGHT, BORDER_TOP)
        line(BORDER_LEFT, height - BORDER_BOTTOM, width - BORDER_RIGHT, height - BORDER_BOTTOM)
        line(BORDER_LEFT, BORDER_TOP, BORDER_LEFT, height - BORDER_BOTTOM)
        line(width - BORDER_RIGHT, BORDER_TOP, width - BORDER_RIGHT, height - BORDER_BOTTOM)
    }

    private fun drawYAxis() {

        gc.setTextBaseline(VPos.CENTER)
        gc.setTextAlign(TextAlignment.RIGHT)
        gc.setFont(YAXIS_FONT)

        val format = createDecimalFormat(graphMaxValue)

        val s: Double = (graphMaxValue - graphMinValue) / 10.0
        var i: Double = graphMinValue
        while (i <= graphMaxValue) {
            val y = scaley(i.toDouble())

            gc.setLineWidth(BORDER_WIDTH);
            gc.setStroke(BORDER_COLOUR)
            line(BORDER_LEFT, y, BORDER_LEFT - AXIS_TICK_LENGTH, y)

            gc.setLineWidth(AXIS_TEXT_WIDTH);
            gc.setStroke(AXIS_TEXT_COLOUR)
            text(format.format(i), BORDER_LEFT - (AXIS_TICK_LENGTH + 10), y)

            i = i + s
        }

    }

    private fun drawXAxis() {

        val s = size / 10
        if (s > 0) {
            for (i in 0..size step size / 10) {
                val x = scalex(i.toDouble())

                gc.setLineWidth(BORDER_WIDTH);
                line(x, height - BORDER_BOTTOM, x, height - BORDER_BOTTOM + AXIS_TICK_LENGTH)
            }
        }

        gc.setTextBaseline(VPos.CENTER)
        gc.setTextAlign(TextAlignment.CENTER)
        gc.setFont(XAXIS_FONT)
        gc.setStroke(AXIS_TEXT_COLOUR)
        gc.setLineWidth(AXIS_TEXT_WIDTH);
        text("${time2text(currentDuration)}", scalex(size / 2.0), height - BORDER_BOTTOM / 2)

    }

    private fun drawMinMaxLines() {
        if (MIN_MAX_ENABLED) {
            gc.setStroke(MIN_MAX_COLOUR)
            gc.setLineWidth(MIN_MAX_WIDTH);
            dashedLine(BORDER_LEFT, scaley(minValue), width - BORDER_RIGHT, scaley(minValue), MIN_MAX_DASH_SIZE)
            dashedLine(BORDER_LEFT, scaley(maxValue), width - BORDER_RIGHT, scaley(maxValue), MIN_MAX_DASH_SIZE)
        }
    }

    private fun drawLegend() {

        gc.setTextAlign(TextAlignment.LEFT)
        gc.setTextBaseline(VPos.CENTER)
        gc.setFont(LEGEND_FONT)

        var y = scaley(maxValue)
        var i = 0
        for (name in names) {
            val colour = DATA_LINE_COLOURS[i++]
            gc.setStroke(colour)
            val x = width - BORDER_RIGHT + LEGEND_BORDER_LEFT

            gc.setLineWidth(LEGEND_LINE_WIDTH)
            line(x, y, x + LEGEND_LINE_LENGTH, y)

            gc.setLineWidth(LEGEND_TEXT_WIDTH)
            gc.setStroke(LEGEND_TEXT_COLOUR)
            text(name, x + LEGEND_LINE_LENGTH * 2, y)
            y += 20
        }

    }

    private fun plot(x1: Double, v1: Double, x2: Double, v2: Double) {
        line(x1, v1, x2, v2)
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
        gc.strokeLine(x1, y1, x2, y2)
    }

    private fun text(s: String, x: Double, y: Double) {
        gc.strokeText(s, x, y)
    }

    fun clearGraph() {
        gc.setFill(Color.WHITE)
        gc.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight())
    }


    fun calculateSizes() {
        width = canvas.width
        height = canvas.height
    }

    private fun scalex(v: Double): Double {

        val cw = width - (BORDER_LEFT + BORDER_RIGHT)
        val x = cw - (v / size) * cw + BORDER_LEFT
        return x
    }

    private fun scaley(v: Double): Double {

        val h = graphMaxValue - graphMinValue
        val ch = height - (BORDER_TOP + BORDER_BOTTOM)
        val y = height - ((v - graphMinValue) / h) * ch - BORDER_BOTTOM

        return y
    }

    private fun getColorValue(n: String) : Color {
        return Color.valueOf(options.getStringValue(n)!!)
    }

    private fun getFontValue(n: String) : Font {
        val fontName = options.getStringValue("${n}.name")!!
        val fontSize = options.getDoubleValue("${n}.size")!!
        return Font.font(fontName, fontSize)
    }

    private fun fatal(s: String) {
        System.err.println("ERROR: ${s}")
        Platform.exit()
    }




}