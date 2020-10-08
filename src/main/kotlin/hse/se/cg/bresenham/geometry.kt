package hse.se.cg.bresenham

import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import kotlin.math.abs

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

data class Circle(val center: Point, val radius: Int) : Drawable {
    constructor(center: Point, other: Point) : this(center, other.distance(center).toInt())

    override fun draw(color: Color, g: Graphics, b: BresenhamModel) {
        g.color = color
        b.circleAlgorithm.drawCircle(this)

        if (GraphicsObjectsModel.Settings.isTestingMode) {
            g.color = GraphicsObjectsModel.Settings.testingColor
            g.drawCircle(shifted(SHIFT, SHIFT))
        }
    }

    fun shifted(shiftX: Int, shiftY: Int): Circle {
        return copy(center = center.shifted(shiftX, shiftY))
    }
}

data class Ellipsis(val center: Point, val xRadius: Int, val yRadius: Int) : Drawable {
    override fun draw(color: Color, g: Graphics, b: BresenhamModel) {
        g.color = color
        b.ellipsisAlgorithm.drawEllipsis(this)

        if (GraphicsObjectsModel.Settings.isTestingMode) {
            g.color = GraphicsObjectsModel.Settings.testingColor
            g.drawOval(this.shifted(SHIFT, SHIFT))
        }
    }

    constructor(first: Point, second: Point): this(
        Point(mean(first.x, second.x), mean(first.y, second.y)),
        abs(first.x - second.x) / 2,
        abs(first.y - second.y) / 2
    )

    fun shifted(xShift: Int, yShift: Int) =
        copy(center = center.shifted(xShift, yShift))
}

fun Point.shifted(shiftX: Int, shiftY: Int): Point =
    Point(x + shiftX, y + shiftY)

private fun Graphics.drawLine(line: Line) {
    drawLine(line.begin.x, line.begin.y, line.end.x, line.end.y)
}

private fun Graphics.drawCircle(circle: Circle) {
    drawOval(
        circle.center.x - circle.radius,
        circle.center.y - circle.radius,
        circle.radius * 2,
        circle.radius * 2
    )
}

private fun Graphics.drawOval(ellipsis: Ellipsis) {
    drawOval(
        ellipsis.center.x - ellipsis.xRadius,
        ellipsis.center.y - ellipsis.yRadius,
        ellipsis.xRadius * 2,
        ellipsis.yRadius * 2
    )
}

private fun mean(a: Int, b: Int) = (a + b) / 2

private const val SHIFT = 10

