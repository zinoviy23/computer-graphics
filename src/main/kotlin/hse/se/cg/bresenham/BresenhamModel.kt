package hse.se.cg.bresenham

import hse.se.cg.bresenham.line.BresenhamLineDrawingAlgorithm

class BresenhamModel(pointDrawer: (x: Int, y: Int) -> Unit) {
    val lineAlgorithm = BresenhamLineDrawingAlgorithm(pointDrawer)
}
