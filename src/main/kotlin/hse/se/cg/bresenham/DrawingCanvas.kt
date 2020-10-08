package hse.se.cg.bresenham

import java.awt.*
import java.awt.event.*
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

        val bresenhamModel = BresenhamModel { x, y ->
            g.drawLine(x, y, x, y)
        }

        for (currentObject in GraphicsObjectsModel) {
            currentObject.drawable.draw(currentObject.color, g, bresenhamModel)
        }

        val pendingPoint = GraphicsObjectsModel.pendingBegin ?: return
        println(pendingPoint)
        val currentMousePosition = currentMousePosition ?: return
        GraphicsObjectsModel.Settings.currentInstrument
            .createObject(pendingPoint, currentMousePosition)
            .draw(GraphicsObjectsModel.Settings.color, g, bresenhamModel)
    }

    companion object {
        private val MIN_SIZE = Dimension(640, 480)
        private val PREFERRED_SIZE = Dimension(1080, 720)
    }
}
