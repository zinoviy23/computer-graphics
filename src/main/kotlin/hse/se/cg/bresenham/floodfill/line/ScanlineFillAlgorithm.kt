package hse.se.cg.bresenham.floodfill.line

import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.util.*

/**
 * Алгоритм рисования построчной заливки
 */
class ScanlineFillAlgorithm(private val pointDrawer: (x: Int, y: Int) -> Unit) {
    fun fill(center: Point, dimension: Dimension, colorExtractor: (x: Int, y: Int) -> Color) {
        val colorToFill = colorExtractor(center.x, center.y)

        val queue: Queue<Point> = ArrayDeque()
        queue.add(center)

        while (queue.isNotEmpty()) {
            val currentPoint = queue.poll()
            pointDrawer(currentPoint.x, currentPoint.y)

            val y = currentPoint.y
            var xRight = currentPoint.x

            while (xRight + 1 < dimension.width && colorExtractor(xRight + 1, y) == colorToFill) {
                xRight++
                pointDrawer(xRight, y)
            }

            var xLeft = currentPoint.x
            while (xLeft > 0 && colorExtractor(xLeft - 1, y) == colorToFill) {
                xLeft--
                pointDrawer(xLeft, y)
            }

            if (y > 0) {
                queue.addAll(scanLine(y - 1, xLeft, xRight, colorToFill, colorExtractor))
            }
            if (y < dimension.height - 1) {
                queue.addAll(scanLine(y + 1, xLeft, xRight, colorToFill, colorExtractor))
            }
        }
    }

    private fun scanLine(
        y: Int,
        fromX: Int,
        toX: Int,
        colorToFill: Color,
        colorExtractor: (x: Int, y: Int) -> Color
    ): List<Point> {
        return mutableListOf<Point>().apply {
            var prevInOurColor = false
            for (x in (fromX..toX)) {
                val currentColor = colorExtractor(x, y)
                if (currentColor != colorToFill && prevInOurColor) {
                    add(Point(x - 1, y))
                }
                prevInOurColor = currentColor == colorToFill
            }
            if (prevInOurColor) {
                add(Point(toX, y))
            }
        }
    }
}
