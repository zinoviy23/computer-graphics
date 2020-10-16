package hse.se.cg.bresenham.filters.ui

import hse.se.cg.bresenham.DrawingModel
import hse.se.cg.bresenham.GraphicsObjectsModel
import hse.se.cg.bresenham.filters.CohenSutherlandFilter
import hse.se.cg.bresenham.filters.DefaultFilter
import java.awt.GridLayout
import java.awt.Point
import java.lang.Integer.min
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JPanel
import kotlin.math.abs

enum class DrawingFilterType(private val presentation: String) {
    DEFAULT("Без фильтров") {
        override fun panel(): JPanel? {
            GraphicsObjectsModel.Settings.currentDrawingFilter = DefaultFilter
            return null
        }
    },
    COHEN_SUTHERLAND("Окно Сазерленда-Коэна") {
        override fun panel(): JPanel {
            GraphicsObjectsModel.Settings.currentDrawingFilter =
                CohenSutherlandFilter.lastFilter ?: CohenSutherlandFilter.createFilter(Point(100, 100), 500, 500)

            return JPanel(GridLayout(2, 1)).apply {
                add(JCheckBox("Спрятать").apply {
                    isSelected = false
                    addActionListener {
                        GraphicsObjectsModel.Settings.currentDrawingFilter = if (isSelected) {
                            DefaultFilter
                        } else {
                            CohenSutherlandFilter.lastFilter!!
                        }
                    }
                })
                add(JButton("Изменить окно").apply {
                    addActionListener {
                        GraphicsObjectsModel.Settings.currentDrawingFilter = DefaultFilter
                        GraphicsObjectsModel.addPointConsumer(object : GraphicsObjectsModel.PointConsumer {
                            override fun consume(a: Point, b: Point) {
                                val x = min(a.x, b.x)
                                val y = min(a.y, b.y)
                                GraphicsObjectsModel.Settings.currentDrawingFilter =
                                    CohenSutherlandFilter.createFilter(Point(x, y), abs(a.x - b.x), abs(a.y - b.y))
                            }

                            override fun drawPreview(a: Point, b: Point, g: DrawingModel) {
                                g.graphics.color = GraphicsObjectsModel.Settings.testingColor
                                val x = min(a.x, b.x)
                                val y = min(a.y, b.y)
                                g.graphics.drawRect(x, y, abs(a.x - b.x), abs(a.y - b.y))
                            }
                        })
                    }
                })
            }
        }
    };

    abstract fun panel(): JPanel?

    override fun toString(): String {
        return presentation
    }
}
