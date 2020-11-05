package hse.se.cg.bresenham

import java.awt.*
import java.awt.event.*
import java.awt.image.BufferedImage
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
                if (!GraphicsObjectsModel.Settings.currentInstrument.fixedNumberOfPoints && e.button == MouseEvent.BUTTON3) {
                    GraphicsObjectsModel.finish()
                } else {
                    GraphicsObjectsModel.addPoint(e.point)
                }
            }

            override fun mouseReleased(e: MouseEvent) {
                if (GraphicsObjectsModel.pendingPoints.isNotEmpty() &&
                    GraphicsObjectsModel.Settings.currentInstrument.addPointOnRelease
                ) {
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
                SwingUtilities.invokeLater {
                    repaint()
                }
            }
        })

        GraphicsObjectsModel.addDrawingListener {
            SwingUtilities.invokeLater {
                repaint()
            }
        }
    }

    override fun update(g: Graphics) {
        paint(g)
    }

    override fun paint(g: Graphics) {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val graphics = image.graphics

        graphics.color = Color.WHITE
        graphics.fillRect(0, 0, width, height)

        val bresenhamModel = DrawingAlgorithmsModel { x, y ->
            graphics.drawLine(x, y, x, y)
        }

        val drawingModel = DrawingModel(
            graphics,
            bresenhamModel,
            Dimension(image.width, image.height)
        ) { x: Int, y: Int ->
            Color(image.getRGB(x, y))
        }

        for (currentObject in GraphicsObjectsModel) {
            currentObject.drawable.draw(currentObject.color, drawingModel)
        }

        drawTestingObject(drawingModel)
        GraphicsObjectsModel.Settings.currentDrawingFilter.drawPreview(drawingModel)

        g.drawImage(image, 0, 0, this)
        graphics.dispose()
    }

    private fun drawTestingObject(drawingModel: DrawingModel) {
        val pendingPoints = GraphicsObjectsModel.pendingPoints.takeIf { it.isNotEmpty() } ?: return
        val currentMousePosition = currentMousePosition ?: return

        val pointConsumer = GraphicsObjectsModel.currentPointConsumer
        if (pointConsumer != null && pendingPoints.size > 0) {
            pointConsumer.drawPreview(pendingPoints[0], currentMousePosition, drawingModel)
            return
        }
        val pointsToDraw = pendingPoints + currentMousePosition
        val (previewDrawable, pointsLeft) = GraphicsObjectsModel.Settings.currentInstrument
            .createObject(pointsToDraw)
        previewDrawable?.drawPreview(GraphicsObjectsModel.Settings.color, drawingModel)
        for (point in pointsToDraw.takeLast(pointsLeft)) {
            drawingModel.graphics.color = GraphicsObjectsModel.Settings.testingColor
            drawingModel.graphics.fillOval(
                point.x - PREVIEW_POINT_RADIUS,
                point.y - PREVIEW_POINT_RADIUS,
                2 * PREVIEW_POINT_RADIUS,
                2 * PREVIEW_POINT_RADIUS
            )
        }
    }

    companion object {
        private val MIN_SIZE = Dimension(640, 480)
        private val PREFERRED_SIZE = Dimension(1080, 720)
        private const val PREVIEW_POINT_RADIUS = 5
    }
}
