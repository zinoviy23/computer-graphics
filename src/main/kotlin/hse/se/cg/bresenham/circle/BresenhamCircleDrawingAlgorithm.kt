package hse.se.cg.bresenham.circle

import hse.se.cg.bresenham.Circle
import java.awt.Point

/**
 * Класс, реализующий алгоритм рисования окружности Брезенхема
 */
class BresenhamCircleDrawingAlgorithm(private val pointDrawer: (x: Int, y: Int) -> Unit) {
    fun drawCircle(circle: Circle) {
        var x = 0
        var y = circle.radius
        var d = 3 - 2 * circle.radius
        printPoint(circle.center, x, y)

        while (y >= x) {
            x++
            d += if (d > 0) {
                y--
                4 * (x - y) + 10
            } else {
                4 * x + 6
            }
            printPoint(circle.center, x, y)
        }
    }

    private fun printPoint(center: Point, x: Int, y: Int) {
        pointDrawer(center.x + x, center.y + y)
        pointDrawer(center.x + x, center.y - y)
        pointDrawer(center.x - x, center.y + y)
        pointDrawer(center.x - x, center.y - y)
        pointDrawer(center.x + y, center.y + x)
        pointDrawer(center.x + y, center.y - x)
        pointDrawer(center.x - y, center.y + x)
        pointDrawer(center.x - y, center.y - x)
    }
}
