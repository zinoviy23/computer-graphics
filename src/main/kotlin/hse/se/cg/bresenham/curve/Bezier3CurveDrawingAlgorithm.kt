package hse.se.cg.bresenham.curve

import hse.se.cg.bresenham.Line
import hse.se.cg.bresenham.line.BresenhamLineDrawingAlgorithm
import hse.se.cg.bresenham.math.Matrix
import hse.se.cg.bresenham.math.Vector
import hse.se.cg.bresenham.math.times
import java.awt.Point
import kotlin.math.pow

class Bezier3CurveDrawingAlgorithm(private val lineDrawingAlgorithm: BresenhamLineDrawingAlgorithm) {
    fun draw(points: List<Point>) {
        require(points.size == 4)
        val pointMatrix = Matrix(listOf(points.map { it.x.toDouble() }, points.map { it.y.toDouble() }))

        var t = 0.0
        var prev = points.first()
        while (t <= 1) {
            val (x, y) = pointMatrix * vectorFromT(t)
            val end = Point(x.toInt(), y.toInt())
            lineDrawingAlgorithm.drawLine(Line(prev, end))
            t += 0.04
            prev = end
        }
        lineDrawingAlgorithm.drawLine(Line(prev, points.last()))
    }

    private fun vectorFromT(t: Double): Vector {
        return Vector(listOf(
            (1 - t).pow(3),
            3 * t * (1 - t).pow(2),
            3 * t.pow(2) * (1 - t),
            t.pow(3)
        ))
    }
}
