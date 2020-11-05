package hse.se.cg.bresenham

import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import kotlin.math.min
import kotlin.math.abs
import kotlin.math.max

data class Line(val begin: Point, val end: Point) : Drawable {
    val xDifference: Int
        get() = end.x - begin.x

    val yDifference: Int
        get() = end.y - begin.y

    val xRange: IntRange
        get() = (min(begin.x, end.x)..max(begin.x, end.x))

    val yRange: IntRange
        get() = (min(begin.y, end.y)..max(begin.y, end.y))

    fun shifted(shiftX: Int, shiftY: Int): Line =
        copy(begin = begin.shifted(shiftX, shiftY), end = end.shifted(shiftX, shiftY))

    override fun draw(color: Color, drawingModel: DrawingModel) {
        drawingModel.graphics.color = color
        drawingModel.drawingAlgorithmsModel.lineAlgorithm.drawLine(this)

        if (GraphicsObjectsModel.Settings.isTestingMode) {
            drawingModel.graphics.color = GraphicsObjectsModel.Settings.testingColor
            drawingModel.graphics.drawLine(shifted(SHIFT, SHIFT))
        }
    }
}

data class Circle(val center: Point, val radius: Int) : Drawable {
    constructor(center: Point, other: Point) : this(center, other.distance(center).toInt())

    override fun draw(color: Color, drawingModel: DrawingModel) {
        drawingModel.graphics.color = color
        drawingModel.drawingAlgorithmsModel.circleAlgorithm.drawCircle(this)

        if (GraphicsObjectsModel.Settings.isTestingMode) {
            drawingModel.graphics.color = GraphicsObjectsModel.Settings.testingColor
            drawingModel.graphics.drawCircle(shifted(SHIFT, SHIFT))
        }
    }

    fun shifted(shiftX: Int, shiftY: Int): Circle {
        return copy(center = center.shifted(shiftX, shiftY))
    }
}

data class Ellipsis(val center: Point, val xRadius: Int, val yRadius: Int) : Drawable {
    override fun draw(color: Color, drawingModel: DrawingModel) {
        drawingModel.graphics.color = color
        drawingModel.drawingAlgorithmsModel.ellipsisAlgorithm.drawEllipsis(this)

        if (GraphicsObjectsModel.Settings.isTestingMode) {
            drawingModel.graphics.color = GraphicsObjectsModel.Settings.testingColor
            drawingModel.graphics.drawOval(this.shifted(SHIFT, SHIFT))
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

data class LineFloodFill(val center: Point): Drawable {
    override fun draw(color: Color, drawingModel: DrawingModel) {
        drawingModel.graphics.color = color
        if (GraphicsObjectsModel.Settings.isTestingMode) return
        drawingModel.drawingAlgorithmsModel
            .scanlineFillAlgorithm
            .fill(center, drawingModel.dimension) { x: Int, y: Int ->
                drawingModel.getColor(x, y)
            }
    }
}

data class Bezier3Curve(val point0: Point, val point1: Point, val point2: Point, val point3: Point): Drawable {
    override fun draw(color: Color, drawingModel: DrawingModel) {
        drawingModel.graphics.color = color
        drawingModel.drawingAlgorithmsModel
            .bezier3CurveDrawingAlgorithm
            .draw(listOf(point0, point1, point2, point3))

        if (GraphicsObjectsModel.Settings.isTestingMode) {
            drawingModel.graphics.color = GraphicsObjectsModel.Settings.testingColor

            drawingModel.graphics.fillOval(point0.x - 2, point0.y - 2, 4, 4)
            drawingModel.graphics.fillOval(point1.x - 2, point1.y - 2, 4, 4)
            drawingModel.graphics.fillOval(point2.x - 2, point2.y - 2, 4, 4)
            drawingModel.graphics.fillOval(point3.x - 2, point3.y - 2, 4, 4)

            drawingModel.graphics.drawLine(Line(point0, point1))
            drawingModel.graphics.drawLine(Line(point1, point2))
            drawingModel.graphics.drawLine(Line(point2, point3))
        }
    }
}

data class BezierCurve(val points: List<Point>): Drawable {
    override fun draw(color: Color, drawingModel: DrawingModel) {
        drawingModel.graphics.color = color
        drawingModel.drawingAlgorithmsModel.bezierCurveDrawingAlgorithm.draw(points)

        if (GraphicsObjectsModel.Settings.isTestingMode) {
            drawingModel.graphics.color = GraphicsObjectsModel.Settings.testingColor
            drawingModel.graphics.fillOval(points[0].x - 2, points[0].y - 2, 4, 4)
            for (i in 1 until points.size) {
                drawingModel.graphics.fillOval(points[i].x - 2, points[i].y - 2, 4, 4)
                drawingModel.graphics.drawLine(Line(points[i - 1], points[i]))
            }
        }
    }
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

