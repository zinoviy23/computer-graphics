package hse.se.cg.bresenham

import hse.se.cg.bresenham.circle.BresenhamCircleDrawingAlgorithm
import hse.se.cg.bresenham.ellipsis.BresenhamEllipsisDrawingAlgorithm
import hse.se.cg.bresenham.floodfill.line.ScanlineFillAlgorithm
import hse.se.cg.bresenham.line.BresenhamLineDrawingAlgorithm

class DrawingAlgorithmsModel(pointDrawer: (x: Int, y: Int) -> Unit) {
    val lineAlgorithm = BresenhamLineDrawingAlgorithm(pointDrawer)
    val circleAlgorithm = BresenhamCircleDrawingAlgorithm(pointDrawer)
    val ellipsisAlgorithm = BresenhamEllipsisDrawingAlgorithm(pointDrawer)
    val scanlineFillAlgorithm = ScanlineFillAlgorithm(pointDrawer)
}
