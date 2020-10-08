package hse.se.cg.bresenham

import hse.se.cg.bresenham.circle.BresenhamCircleDrawingAlgorithm
import hse.se.cg.bresenham.ellipsis.BresenhamEllipsisDrawingAlgorithm
import hse.se.cg.bresenham.line.BresenhamLineDrawingAlgorithm

class BresenhamModel(pointDrawer: (x: Int, y: Int) -> Unit) {
    val lineAlgorithm = BresenhamLineDrawingAlgorithm(pointDrawer)
    val circleAlgorithm = BresenhamCircleDrawingAlgorithm(pointDrawer)
    val ellipsisAlgorithm = BresenhamEllipsisDrawingAlgorithm(pointDrawer)
}
