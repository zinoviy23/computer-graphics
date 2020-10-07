package hse.se.cg.bresenham

import java.awt.Graphics
import java.awt.Point

data class Line(val begin: Point, val end: Point) {
    val xDifference: Int
        get() = end.x - begin.x

    val yDifference: Int
        get() = end.y - begin.y

    fun shifted(shiftX: Int, shiftY: Int): Line =
        copy(begin = begin.shifted(shiftX, shiftY), end = end.shifted(shiftX, shiftY))
}

fun Point.shifted(shiftX: Int, shiftY: Int): Point =
    Point(x + shiftX, y + shiftY)

fun Graphics.drawLine(line: Line) {
    drawLine(line.begin.x, line.begin.y, line.end.x, line.end.y)
}

