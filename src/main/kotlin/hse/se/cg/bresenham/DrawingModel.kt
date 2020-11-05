package hse.se.cg.bresenham

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics

class DrawingModel(
    val graphics: Graphics,
    val drawingAlgorithmsModel: DrawingAlgorithmsModel,
    val dimension: Dimension,
    private val colorModel: (x: Int, y: Int) -> Color
) {
    fun getColor(x: Int, y: Int): Color {
        return colorModel(x, y)
    }
}

interface Drawable {
    fun draw(color: Color, drawingModel: DrawingModel)

    fun drawPreview(color: Color, drawingModel: DrawingModel) = draw(color, drawingModel)
}
