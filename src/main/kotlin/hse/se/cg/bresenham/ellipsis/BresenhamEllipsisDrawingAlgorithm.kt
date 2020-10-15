package hse.se.cg.bresenham.ellipsis

import hse.se.cg.bresenham.Ellipsis
import org.apache.logging.log4j.LogManager
import java.awt.Point

/**
 * Алгоритм рисования эллипса по [статье](https://dai.fmph.uniba.sk/upload/0/01/Ellipse.pdf)
 *
 */
class BresenhamEllipsisDrawingAlgorithm(private val pointDrawer: (x: Int, y: Int) -> Unit) {
    fun drawEllipsis(ellipsis: Ellipsis) {
        LOG.debug("draw ellipsis=$ellipsis")
        if (ellipsis.xRadius == 0 || ellipsis.yRadius == 0) return

        firstPart(ellipsis)
        secondPart(ellipsis)
    }

    private fun firstPart(ellipsis: Ellipsis) {
        val twoASquare = 2 * ellipsis.xRadius * ellipsis.xRadius
        val twoBSquare = 2 * ellipsis.yRadius * ellipsis.yRadius

        var x = ellipsis.xRadius
        var y = 0
        var xChange = ellipsis.yRadius * ellipsis.yRadius * (1 - 2 * ellipsis.xRadius)
        var yChange = ellipsis.xRadius * ellipsis.xRadius
        var error = 0
        var stoppingX = twoBSquare * ellipsis.xRadius
        var stoppingY = 0

        while (stoppingX >= stoppingY) {
            drawPoint(ellipsis.center, x, y)
            y++
            stoppingY += twoASquare
            error += yChange
            yChange += twoASquare
            if ((2 * error + xChange) > 0) {
                x--
                stoppingX -= twoBSquare
                error += xChange
                xChange += twoBSquare
            }
        }
    }

    private fun secondPart(ellipsis: Ellipsis) {
        val twoASquare = 2 * ellipsis.xRadius * ellipsis.xRadius
        val twoBSquare = 2 * ellipsis.yRadius * ellipsis.yRadius

        var x = 0
        var y = ellipsis.yRadius
        var xChange = ellipsis.yRadius * ellipsis.yRadius
        var yChange = ellipsis.xRadius * ellipsis.xRadius * (1 - 2 * ellipsis.yRadius)
        var error = 0
        var stoppingX = 0
        var stoppingY = twoASquare * ellipsis.yRadius

        while (stoppingX <= stoppingY) {
            drawPoint(ellipsis.center, x, y)
            x++
            stoppingX += twoBSquare
            error += xChange
            xChange += twoBSquare
            if ((2 * error + yChange) > 0) {
                y--
                stoppingY -= twoASquare
                error += yChange
                yChange += twoASquare
            }
        }
    }

    private fun drawPoint(center: Point, x: Int, y: Int) {
        pointDrawer(center.x + x, center.y + y)
        pointDrawer(center.x + x, center.y - y)
        pointDrawer(center.x - x, center.y + y)
        pointDrawer(center.x - x, center.y - y)
    }

    companion object {
        private val LOG = LogManager.getLogger(BresenhamEllipsisDrawingAlgorithm::class.java)
    }
}
