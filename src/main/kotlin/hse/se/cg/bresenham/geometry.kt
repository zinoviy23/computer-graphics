package hse.se.cg.bresenham

import java.awt.Color
import java.awt.Graphics
import java.awt.Point

interface Drawable {
    fun draw(color: Color, g: Graphics, b: BresenhamModel)
}

data class Line(val begin: Point, val end: Point) : Drawable {
    val xDifference: Int
        get() = end.x - begin.x

    val yDifference: Int
        get() = end.y - begin.y

    fun shifted(shiftX: Int, shiftY: Int): Line =
        copy(begin = begin.shifted(shiftX, shiftY), end = end.shifted(shiftX, shiftY))

    override fun draw(color: Color, g: Graphics, b: BresenhamModel) {
        g.color = color
        b.lineAlgorithm.drawLine(this)

        if (GraphicsObjectsModel.Settings.isTestingMode) {
            g.color = GraphicsObjectsModel.Settings.testingColor
            g.drawLine(shifted(SHIFT, SHIFT))
        }
    }
}

fun Point.shifted(shiftX: Int, shiftY: Int): Point =
    Point(x + shiftX, y + shiftY)

private fun Graphics.drawLine(line: Line) {
    drawLine(line.begin.x, line.begin.y, line.end.x, line.end.y)
}

private const val SHIFT = 10

