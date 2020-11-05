package hse.se.cg.bresenham.curve

import hse.se.cg.bresenham.Line
import hse.se.cg.bresenham.line.BresenhamLineDrawingAlgorithm
import hse.se.cg.bresenham.math.Matrix
import hse.se.cg.bresenham.math.Vector
import hse.se.cg.bresenham.math.times
import java.awt.Point
import kotlin.math.pow

class BezierCurveDrawingAlgorithm(private val lineDrawingAlgorithm: BresenhamLineDrawingAlgorithm) {
    fun draw(points: List<Point>) {
        require(points.size > 1) { "size is ${points.size}" }
        val pointMatrix = Matrix(listOf(points.map { it.x.toDouble() }, points.map { it.y.toDouble() }))

        var t = 0.0
        var prev = points.first()
        while (t <= 1) {
            val (x, y) = pointMatrix * vectorFromT(t, points.size - 1)
            val end = Point(x.toInt(), y.toInt())
            lineDrawingAlgorithm.drawLine(Line(prev, end))
            t += 0.04
            prev = end
        }
        lineDrawingAlgorithm.drawLine(Line(prev, points.last()))
    }

    private fun factorial(n: Int) = (1..n).fold(1L) { acc, i -> acc * i }

    private fun vectorFromT(t: Double, n: Int): Vector {
        return Vector(
            (0..n).map {
                factorial(n) / factorial(it) / factorial(n - it) * t.pow(it) * (1 - t).pow(n - it)
            }
        )
    }
}
