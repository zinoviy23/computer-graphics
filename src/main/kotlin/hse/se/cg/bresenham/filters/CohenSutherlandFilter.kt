package hse.se.cg.bresenham.filters

import hse.se.cg.bresenham.Drawable
import hse.se.cg.bresenham.DrawingModel
import hse.se.cg.bresenham.GraphicsObjectsModel
import hse.se.cg.bresenham.Line
import java.awt.Point

class CohenSutherlandFilter private constructor(
    private val point: Point,
    private val width: Int,
    private val height: Int
) : DrawingFilter {
    private val xMin = point.x
    private val xMax = point.x + width
    private val yMin = point.y
    private val yMax = point.y + height

    override fun filter(drawable: Drawable): Drawable? {
        if (drawable !is Line) return null
        return filterLine(drawable)
    }

    override fun drawPreview(drawingModel: DrawingModel) {
        drawingModel.graphics.color = GraphicsObjectsModel.Settings.testingColor
        drawingModel.graphics.drawRect(point.x, point.y, width, height)
    }

    private fun filterLine(line: Line): Line? {
        val beginCode = line.begin.positionCode
        val endCode = line.end.positionCode
        if (beginCode == 0 && endCode == 0) {
            return line
        }
        if (beginCode and endCode != 0) {
            return null
        }

        return findIntersection(line)
    }

    private fun findIntersection(line: Line): Line? {
        val points = listOf(line.begin, line.end)
        val begin = points.minByOrNull { it.x } ?: return null
        val end = points.first { it != begin }
        val incline = incline(begin, end)
        val resultBegin = updatePoint(begin, incline, line) ?: return null
        val resultEnd = updatePoint(end, incline, line) ?: return null
        return Line(resultBegin, resultEnd)
    }

    private fun updatePoint(point: Point, incline: NumberOrInfinity, line: Line): Point? {
        val code = point.positionCode
        if (point.positionCode == 0) return point

        if (code and 0x01 != 0 && incline is NumberOrInfinity.Number) {
            val yL = (incline * (xMin.toDouble() - point.x) + point.y).toInt()
            if (yL in line.yRange && yL in (yMin..yMax)) {
                return Point(xMin, yL)
            }
        }
        if (code and 0x02 != 0 && incline is NumberOrInfinity.Number) {
            val yR = (incline * (xMax.toDouble() - point.x) + point.y).toInt()
            if (yR in line.yRange && yR in (yMin..yMax)) {
                return Point(xMax, yR)
            }
        }
        if (code and 0x04 != 0 && incline is NumberOrInfinity.NonZero) {
            val xT = (point.x + (1.0 / incline) * (yMin - point.y)).toInt()
            if (xT in line.xRange && xT in (xMin..xMax)) {
                return Point(xT, yMin)
            }
        }
        if (code and 0x08 != 0 && incline is NumberOrInfinity.NonZero) {
            val xB = (point.x + (1.0 / incline) * (yMax - point.y)).toInt()
            if (xB in line.xRange && xB in (xMin..xMax)) {
                return Point(xB, yMax)
            }
        }
        return null
    }

    private fun incline(a: Point, b: Point): NumberOrInfinity {
        if (a.x - b.x == 0) return NumberOrInfinity.Infinity
        if ((a.y - b.y) == 0) return NumberOrInfinity.Number.Zero
        return NumberOrInfinity.Number.NonZeroNumber((a.y.toDouble() - b.y) / (a.x.toDouble() - b.x))
    }

    private val Point.positionCode: Int
        get() {
            var code = 0
            if (x < xMin) code = code or 0x01
            if (x > xMax) code = code or 0x02
            if (y < yMin) code = code or 0x04
            if (y > yMax) code = code or 0x08
            return code
        }

    companion object {
        var lastFilter: CohenSutherlandFilter? = null
            private set

        fun createFilter(point: Point, width: Int, height: Int): CohenSutherlandFilter {
            lastFilter = CohenSutherlandFilter(point, width, height)
            return lastFilter!!
        }
    }

    private sealed class NumberOrInfinity {
        interface NonZero

        object Infinity : NumberOrInfinity(), NonZero
        sealed class Number : NumberOrInfinity() {
            abstract val number: Double

            operator fun times(double: Double) = number * double

            object Zero : Number() {
                override val number: Double = 0.0
            }

            class NonZeroNumber(override val number: Double) : Number(), NonZero
        }
    }

    private operator fun Double.div(number: NumberOrInfinity.NonZero) = when (number) {
        is NumberOrInfinity.Infinity -> 0.0
        is NumberOrInfinity.Number.NonZeroNumber -> this / number.number
        else -> error("cannot be here")
    }
}
