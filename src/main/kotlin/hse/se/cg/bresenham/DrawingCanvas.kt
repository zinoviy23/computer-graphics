package hse.se.cg.bresenham

import hse.se.cg.bresenham.line.BresenhamLineDrawingAlgorithm
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import javax.swing.JPanel
import javax.swing.SwingUtilities

/**
 * Панель для рисования линий. Рисует линии по клику мыши
 */
class DrawingCanvas : JPanel() {
    private var currentMousePosition: Point? = null

    init {
        minimumSize = MIN_SIZE
        preferredSize = PREFERRED_SIZE
        isVisible = true

        cursor = Cursor(Cursor.CROSSHAIR_CURSOR)

        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                GraphicsObjectsModel.addPoint(e.point)
            }

            override fun mouseReleased(e: MouseEvent) {
                if (GraphicsObjectsModel.pendingBegin != null) {
                    GraphicsObjectsModel.addPoint(e.point)
                }
            }
        })

        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                currentMousePosition = e.point
                SwingUtilities.invokeLater {
                    repaint()
                }
            }

            override fun mouseMoved(e: MouseEvent) {
                currentMousePosition = e.point
            }
        })

        GraphicsObjectsModel.addDrawingListener {
            SwingUtilities.invokeLater {
                repaint()
            }
        }
    }

    override fun paint(g: Graphics) {
        g.clearRect(0, 0, width, height)

        val lineAlgorithm = BresenhamLineDrawingAlgorithm { x, y ->
            g.drawLine(x, y, x, y)
        }

        for (line in GraphicsObjectsModel) {
            g.drawLine(lineAlgorithm, line)
        }

        val pendingPoint = GraphicsObjectsModel.pendingBegin ?: return
        println(pendingPoint)
        val currentMousePosition = currentMousePosition ?: return
        g.drawLine(
            lineAlgorithm,
            Line(pendingPoint, currentMousePosition)
        )
    }

    private fun Graphics.drawLine(
        lineAlgorithm: BresenhamLineDrawingAlgorithm,
        line: Line
    ) {
        color = Color.RED
        lineAlgorithm.drawLine(line)

        if (GraphicsObjectsModel.Settings.isTestingMode) {
            color = Color.BLUE
            drawLine(line.shifted(SHIFT, SHIFT))
        }
    }

    companion object {
        private const val SHIFT = 10
        private val MIN_SIZE = Dimension(640, 480)
        private val PREFERRED_SIZE = Dimension(1080, 720)
    }
}
