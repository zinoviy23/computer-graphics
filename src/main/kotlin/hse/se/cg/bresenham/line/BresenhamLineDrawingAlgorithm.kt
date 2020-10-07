package hse.se.cg.bresenham.line

import hse.se.cg.bresenham.Line
import org.apache.logging.log4j.LogManager
import kotlin.math.abs

/**
 * Класс, реализующий алгоритм Брезенхема. Принимает функцию для рисования точки: [pointDrawer]
 */
class BresenhamLineDrawingAlgorithm(private val pointDrawer: (x: Int, y: Int) -> Unit) {
    fun drawLine(line: Line) {
        LOG.debug("drawing line $line")

        var x = line.begin.x
        var y = line.begin.y
        var deltaX = abs(line.xDifference)
        var deltaY = abs(line.yDifference)
        val signX = sign(line.xDifference)
        val signY = sign(line.yDifference)

        val swapped = deltaX < deltaY
        if (swapped) {
            val tmp = deltaY
            deltaY = deltaX
            deltaX = tmp
        }

        var e = 2 * deltaY - deltaX
        repeat(deltaX) {
            pointDrawer(x, y)
            while (e >= 0) {
                if (swapped) {
                    x += signX
                } else {
                    y += signY
                }
                e -= 2 * deltaX
            }
            if (swapped) {
                y += signY
            } else {
                x += signX
            }
            e += 2 * deltaY
        }
    }

    private fun sign(n: Int) = when {
        n == 0 -> 0
        n < 0 -> -1
        else -> 1
    }

    companion object {
        private val LOG = LogManager.getLogger(BresenhamLineDrawingAlgorithm::class.java)
    }
}

