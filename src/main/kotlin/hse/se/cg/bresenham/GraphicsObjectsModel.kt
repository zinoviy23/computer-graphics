package hse.se.cg.bresenham

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.awt.Point

/**
 * Объект, управляющий всеми линиями для отображения
 */
object GraphicsObjectsModel : Iterable<Line> {
    private val GENERATED_POINT_RANGE = 150..450
    private val LOG: Logger = LogManager.getLogger(GraphicsObjectsModel::class.java)

    private val drawingListeners = mutableListOf<DrawingListener>()

    private val lines = generateOctants().toMutableList()

    var pendingBegin: Point? = null
        private set
    fun addPoint(p: Point) {
        LOG.debug("Add $p. Pending begin: $pendingBegin")
        val beginning = pendingBegin
        if (beginning == null) {
            pendingBegin = p
        } else {
            lines += Line(beginning, p)
            pendingBegin = null
        }

        fireEvent()
    }

    private fun fireEvent() {
        for (listener in drawingListeners) {
            listener.onGraphicsModelChanged()
        }
    }

    override fun iterator(): Iterator<Line> {
        return lines.iterator()
    }

    fun clean() {
        lines.clear()
        fireEvent()
    }

    /**
     * Генерирует линии во всех октантах
     */
    private fun generateOctants(): List<Line> {
        val lines = mutableListOf<Line>()
        val length = GENERATED_POINT_RANGE.last - GENERATED_POINT_RANGE.first
        val xStep = length / 2
        val yStep = length / 4
        for (xPart in 1..2) {
            val x = GENERATED_POINT_RANGE.first + (xStep * (xPart - 1)..xStep * xPart).random()
            for (yPart in 1..4) {
                val y = GENERATED_POINT_RANGE.first + (yStep * (yPart - 1)..yStep * yPart).random()
                val center = Point(GENERATED_POINT_RANGE.random(), GENERATED_POINT_RANGE.random())
                lines += Line(center, Point(x, y))
            }
        }
        return lines
    }

    fun addDrawingListener(listener: DrawingListener) {
        drawingListeners.add(listener)
    }

    fun removeDrawingListener(listener: DrawingListener) {
        drawingListeners.remove(listener)
    }

    fun interface DrawingListener {
        fun onGraphicsModelChanged()
    }

    object Settings {
        var isTestingMode: Boolean = false
            set(value) {
                field = value
                fireEvent()
            }
    }
}
