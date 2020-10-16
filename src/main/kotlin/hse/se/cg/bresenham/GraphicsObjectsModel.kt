package hse.se.cg.bresenham

import hse.se.cg.bresenham.filters.DefaultFilter
import hse.se.cg.bresenham.filters.DrawingFilter
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.awt.Color
import java.awt.Point
import java.util.*

/**
 * Объект, управляющий всеми объектами для отображения
 */
object GraphicsObjectsModel : Iterable<GraphicsObjectsModel.GraphicalObject> {
    private val GENERATED_POINT_RANGE = 150..450
    private val LOG: Logger = LogManager.getLogger(GraphicsObjectsModel::class.java)

    private val drawingListeners = mutableListOf<DrawingListener>()

    private val objects = generateOctants().toMutableList()

    private val pointConsumers: Queue<PointConsumer> = ArrayDeque()

    var pendingBegin: Point? = null
        private set

    val currentPointConsumer: PointConsumer?
        get() = pointConsumers.peek()

    fun addPoint(p: Point) {
        LOG.debug("Add $p. Pending begin: $pendingBegin")
        val beginning = pendingBegin
        val pointConsumer = pointConsumers.peek()
        if (pointConsumer != null) {
            if (beginning != null) {
                pointConsumers.poll()
                pointConsumer.consume(beginning, p)
                pendingBegin = null
            } else {
                pendingBegin = p
            }
            return
        }
        if (Settings.currentInstrument.needTwoPoints) {
            if (beginning == null) {
                pendingBegin = p
            } else {
                objects += GraphicalObject(Settings.color, Settings.currentInstrument.createObject(beginning, p))
                pendingBegin = null
            }
        } else {
            objects += GraphicalObject(Settings.color, Settings.currentInstrument.createObject(p, p))
        }

        fireEvent()
    }

    fun undo() {
        if (pendingBegin == null) {
            objects.removeLastOrNull()
            fireEvent()
        }
    }

    private fun fireEvent() {
        for (listener in drawingListeners) {
            listener.onGraphicsModelChanged()
        }
    }

    override fun iterator(): Iterator<GraphicalObject> {
        return objects.asSequence()
            .mapNotNull { it.applyFilter(Settings.currentDrawingFilter) }
            .iterator()
    }

    fun clean() {
        objects.clear()
        fireEvent()
    }

    /**
     * Генерирует линии во всех октантах
     */
    private fun generateOctants(): List<GraphicalObject> {
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
        return lines.map { GraphicalObject(Settings.color, it) }
    }

    fun addPointConsumer(pointConsumer: PointConsumer) {
        pointConsumers.add(pointConsumer)
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

        var color: Color = Color.RED
            set(value) {
                field = value
                fireEvent()
            }

        var currentInstrument: ObjectType = ObjectType.Line
            set(value) {
                field = value
                pendingBegin = null
                fireEvent()
            }

        val testingColor: Color = Color.BLUE

        var currentDrawingFilter: DrawingFilter = DefaultFilter
            set(value) {
                field = value
                fireEvent()
            }
    }

    data class GraphicalObject(val color: Color, val drawable: Drawable) {
        fun applyFilter(drawingFilter: DrawingFilter): GraphicalObject? {
            return drawingFilter.filter(drawable)?.let {
                GraphicalObject(color, it)
            }
        }
    }

    interface PointConsumer {
        fun consume(a: Point, b: Point)

        fun drawPreview(a: Point, b: Point, g: DrawingModel)
    }
}
