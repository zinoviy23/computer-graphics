package hse.se.cg.bresenham.filters

import hse.se.cg.bresenham.Drawable
import hse.se.cg.bresenham.DrawingModel

interface DrawingFilter {
    fun filter(drawable: Drawable): Drawable?

    fun drawPreview(drawingModel: DrawingModel) = Unit
}

object DefaultFilter : DrawingFilter {
    override fun filter(drawable: Drawable): Drawable = drawable
}
