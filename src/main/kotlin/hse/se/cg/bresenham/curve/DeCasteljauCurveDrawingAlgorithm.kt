package hse.se.cg.bresenham.curve

import hse.se.cg.bresenham.Line
import hse.se.cg.bresenham.line.BresenhamLineDrawingAlgorithm
import java.awt.Point

class DeCasteljauCurveDrawingAlgorithm(private val lineDrawingAlgorithm: BresenhamLineDrawingAlgorithm) {
    fun draw(points: List<Point>) {
        require(points.size > 1)

        var t = 0.0
        var prev = points.first()
        while (t <= 1) {
            val resultPoint = resultPoint(points, t)
            lineDrawingAlgorithm.drawLine(Line(prev, resultPoint))

            prev = resultPoint
            t += 0.04
        }

        lineDrawingAlgorithm.drawLine(Line(prev, points.last()))
    }

    private fun resultPoint(points: List<Point>, t: Double): Point {
        var pointsDouble = points.map { it.x.toDouble() to it.y.toDouble() }
        while (pointsDouble.size != 1) {
            val newPoints = mutableListOf<Pair<Double, Double>>()
            for (i in 1 until pointsDouble.size) {
                val x = meanT(pointsDouble[i - 1].first, pointsDouble[i].first, t)
                val y = meanT(pointsDouble[i - 1].second, pointsDouble[i].second, t)
                newPoints += x to y
            }
            pointsDouble = newPoints
        }

        return Point(pointsDouble[0].first.toInt(), pointsDouble[0].second.toInt())
    }

    private fun meanT(a: Double, b: Double, t: Double) = a * (1 - t) + b * t
}
